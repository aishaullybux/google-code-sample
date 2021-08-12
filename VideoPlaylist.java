package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private String name;
    private List<Video> contents;

    VideoPlaylist(String name)
    {
        this.name = name;
        this.contents = new ArrayList<>();
    }

    String getName()
    {
        return this.name;
    }
    List<Video> getContents()
    {
        return this.contents;
    }


    void addVideo(Video video){
        contents.add(video);
    }

    void removeVideo(Video video)
    {
        contents.remove(video);
    }

    void clearPlaylist()
    {
        contents.clear();
    }



}
