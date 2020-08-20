package com.example.spotifyexercise;

public class Album {
    private String id, name, artists, dateReleased, imageURL, spotifyLink;
    private int numTracks;

    public Album(String id, String name, int numTracks, String artists, String dateReleased, String imageURL, String spotifyLink){
        this.id = id;
        this.name = name;
        this.numTracks = numTracks;
        this.artists = artists;
        this.dateReleased = dateReleased;
        this.imageURL = imageURL;
        this.spotifyLink = spotifyLink;
    }

    public String getID(){
        return  this.id;
    }

    public String getName(){
        return this.name;
    }

    public int getNumTracks(){
        return this.numTracks;
    }

    public String getArtists(){
        return this.artists;
    }

    public String getDateReleased(){
        return this.dateReleased;
    }

    public String getImageURL(){
        return this.imageURL;
    }

    public String getSpotifyLink(){
        return  this.spotifyLink;
    }
}
