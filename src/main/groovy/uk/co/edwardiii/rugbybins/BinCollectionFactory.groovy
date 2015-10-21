package uk.co.edwardiii.rugbybins

import java.text.DateFormat

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


