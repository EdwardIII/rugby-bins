package uk.co.edwardiii.rugbybins

class Main {
    static void main(def args){
        def council = new Council()
        def binCollections = council.getBinCollectionsFor('Temple Street');
    }
} 

