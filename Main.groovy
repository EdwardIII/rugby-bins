package uk.co.edwardiii.rugbybins

import org.ccil.cowan.tagsoup.Parser;
import java.net.URLEncoder;
import groovy.transform.InheritConstructors
import java.text.DateFormat

class Main {
    static void main(def args){
        Council council = new Council()
        def binCollections = council.getBinCollectionsFor('Temple Street');
    }
} 

class Council {
    protected String baseUrl = "https://www.rugby.gov.uk"
    protected String encoding = "UTF-8"

    protected getUrl(path){
        return "${this.baseUrl}${path}"
    }

    protected getSearchPath(path){
        return this.getUrl("/site/custom_scripts/${path}");
    }

    protected getParser(){
        @Grapes( @Grab('org.ccil.cowan.tagsoup:tagsoup:1.2') )
        def parser = new XmlSlurper(new Parser() )
        return parser;
    }

    public getUrlFor(def locationName){

        def locationNameEncoded = URLEncoder.encode(locationName)
        def url = this.getSearchPath("/wcsc.php?View=Get&keyWord=${locationNameEncoded}&submitkeyword=Go")

       new URL(url).withReader (this.encoding) { reader -> 
            def document = this.getParser().parse(reader) 
            def townLink = document.'**'.find{ 
                it.attributes().containsKey('title') &&
                it.attributes()['title'].toLowerCase().contains(locationName.toLowerCase())
            }

            if(!townLink)
                throw new LocationNotFound("Could not find this town")

            return this.getSearchPath(townLink.attributes()['href'])
            }
    }

    public getBinCollectionsFor(locationName){
        String url = this.getUrlFor(locationName)

        new URL(url).withReader (this.encoding) { reader -> 
            def document = this.getParser().parse(reader) 
            def binTypes = document.depthFirst().findAll {
                //!it.childNodes() && it.h3 && it.text().toLowerCase().contains('bin')
                it.name() == 'h3' && it.text().toLowerCase().contains('bin')
            }.collect { it.text() }
            
            def binDates = document.depthFirst().findAll { 
                it.name() == 'b'
            }.collect { it.text() }

            def factory = new BinCollectionFactory()
            return factory.fromTypesAndDates(binTypes, binDates)
        }
    }
}

class BinCollectionFactory {

    // Thursday 29th October 2015
    String format = "E d MMM y"

    protected fromArrayRow(def array){
        def b = new BinCollection()
        b.name = array[0]
        b.date = Date.parse(this.format, array[1].replaceAll("(?:st|nd|rd|th)", ""))
    }

    protected fromArray(def array){
        return array.collect { this.fromArrayRow(it) }
    }

    public fromTypesAndDates(types, dates)
    {
        return this.fromArray(GroovyCollections.transpose(types, dates))
    }
}

class BinCollection {
    public Date date
    public String name
}

@InheritConstructors class LocationNotFound extends Exception {}
