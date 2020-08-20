package com.example.spotifyexercise;

public class Artist {
    private String artistImageURL;
    private int popularity;
    private int numFollowers;
    private String id, name;

    public Artist(String id, String name, int foll, int popul, String image){
        this.id = id;
        this.name = name;
        this.numFollowers = foll;
        this.popularity = popul;
        this.artistImageURL = image;
    }

    public String getID() { return this.id; }

    public String getName(){
        return this.name;
    }

    public int getNumFollowers(){
        return this.numFollowers;
    }

    public int getPopularity(){
        return this.popularity;
    }

    public String getArtistImage(){
        return this.artistImageURL;
    }
}
