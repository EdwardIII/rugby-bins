package uk.co.edwardiii.rugbybins

import java.text.DateFormat

class Main {
    static void main(def args){
        def council = new Council()
        def binCollections = council.getBinCollectionsFor('Temple Street');
    }
} 

