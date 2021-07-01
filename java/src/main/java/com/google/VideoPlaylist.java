package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private String playlistName;
    private List<Video> videoContentList;
    private List<String> videoTitle;


    public VideoPlaylist(String playlistName) {
        this.playlistName = playlistName;
        this.videoContentList = new ArrayList<>();
        this.videoTitle = new ArrayList<>();
    }

    /**
     * Adds video to playlist
     * @param video
     */
    public void addVideos(Video video) {
        this.videoContentList.add(video);
    }
    /** Gets list of video objects */
    public List<Video> getVideos(){
        return this.videoContentList;
    }
    /** Gets playlist name  */
    public String getPlaylistName() {
        return playlistName;
    }
    /** Gets list of video title names */
    public List<String> getVideosTitles() {
        return videoTitle;
    }
}
