package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private boolean flagStatus;
  private String flagReason;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
    this.flagStatus = false;
    this.flagReason = "";
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns boolean flag status of video  */
  boolean getFlagStatus() {
    return this.flagStatus;
  }

  /**  Returns flag reasons of video */
  String getFlagReason(){
    return this.flagReason;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  /** Sets value of flagStatus */
  void setFlagStatus(boolean flagStatus){
    this.flagStatus = flagStatus;
  }

  /** Sets value of flagReason */
  void setFlagReason(String flagReason){
      this.flagReason = flagReason;
  }
}
