package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  public boolean isPlaying;
  public boolean isPaused;
  private String flag;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  String videoString()
  {
    String tagString = "";
    for (int i = 0; i < tags.size(); i++)
      tagString = tagString + " " + tags.get(i);

    //remove extra space at beginning of tagString
    if (tagString.length() > 0)
    {
      tagString = tagString.substring(1);
    }
    return (title + " (" + videoId + ") [" + tagString + "]");
  }
  String videoStringWithFlag()
  {
    String tagString = "";
    for (int i = 0; i < tags.size(); i++)
      tagString = tagString + " " + tags.get(i);

    //remove extra space at beginning of tagString
    if (tagString.length() > 0)
    {
      tagString = tagString.substring(1);
    }
    String toReturn = title + " (" + videoId + ") [" + tagString + "]";
    if (this.isFlagged())
    {
      toReturn = toReturn + " - FLAGGED (reason: " + this.flagReason() + ")";
    }
    return toReturn;
  }


  void flagVideo (String reason)
  {
    this.flag = reason;
  }
  String flagReason()
  {
    return flag;
  }
  boolean isFlagged()
  {
    return flag != null;
  }
  void removeFlag()
  {
    this.flag = null;
  }

}
