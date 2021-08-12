package com.google;

import java.util.*;
import java.util.stream.Collectors;

public class VideoPlayer {

  private List<VideoPlaylist> playList = new ArrayList<>();
  private final VideoLibrary videoLibrary;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    List<Video> sortedVideos = orderVideosByTitle(videoLibrary.getVideos());
    for (int i = 0; i < sortedVideos.size(); i++)
    {
      System.out.println(sortedVideos.get(i).videoStringWithFlag());
    }
  }

  public void playVideo(String videoId) {
    Video videoToPlay = videoLibrary.getVideo(videoId);
    resetPause();

    if (videoToPlay != null) {
      //check if video is flagged
      if (videoToPlay.isFlagged())
      {
        System.out.println("Cannot play video: Video is currently flagged (reason: "+ videoToPlay.flagReason() + ")" );
        return;
      }
      //stop currently playing video
      if (this.videoPlaying() != null) {
        this.stopVideo();
      }
      //play chosen video
      videoToPlay.isPlaying = true;
      System.out.println("Playing video: " + videoToPlay.getTitle());
    }
    else
      System.out.println("Cannot play video: Video does not exist");
  }

  public void stopVideo() {
    Video currentlyPlaying = videoPlaying();
    if (currentlyPlaying != null)
    {
      currentlyPlaying.isPlaying = false;
      System.out.println("Stopping video: "+ currentlyPlaying.getTitle());
    }
    else
    {
      System.out.println("Cannot stop video: No video is currently playing");
    }
  }

  public void playRandomVideo() {
    if (videoLibrary.getVideos().size() == 0)
    {
      System.out.println("No videos available");
      return;
    }
    if (this.videoPlaying() != null){
      this.stopVideo();
    }

    if (checkVideoAvailability()) //if playable video exists
    {
      Random rand = new Random();
      while (true) //loop until a video without flag is selected
      {
        int videoNumber = rand.nextInt(videoLibrary.getVideos().size());
        Video videoToPlay = videoLibrary.getVideos().get(videoNumber);
        if (!videoToPlay.isFlagged())
        {
          playVideo(videoToPlay.getVideoId());
          return;
        }
      }
    }
    else
    {
      System.out.println("No videos available");
    }
  }

  public void pauseVideo() {
    Video currentlyPlaying = videoPlaying();
    if (currentlyPlaying != null)
    {
      if (!currentlyPlaying.isPaused)
      {
        currentlyPlaying.isPaused = true;
        System.out.println("Pausing video: "+ currentlyPlaying.getTitle());
      }
      else
      {
        System.out.println("Video already paused: " + currentlyPlaying.getTitle());
      }
    }
    else
    {
      System.out.println("Cannot pause video: No video is currently playing");
    }
  }

  public void continueVideo() {
    Video currentlyPlaying = videoPlaying();
    if (currentlyPlaying != null)
    {
      if (currentlyPlaying.isPaused)
      {
        System.out.println("Continuing video: "+ currentlyPlaying.getTitle());
        currentlyPlaying.isPaused = false;
      }
      else
      {
        System.out.println("Cannot continue video: Video is not paused");
      }
    }
    else
    {
      System.out.println("Cannot continue video: No video is currently playing");
    }
  }

  public void showPlaying() {
    Video currentlyPlaying = videoPlaying();
    if (currentlyPlaying != null)
    {
      String display = "Currently playing: " + currentlyPlaying.videoString();
      if (currentlyPlaying.isPaused)
      {
        display = display + " - PAUSED";
      }
      System.out.println(display);
    }
    else
    {
      System.out.println("No video is currently playing");
    }
  }

  public VideoPlaylist findPlaylist(String playlistName)
  {
    for (int i = 0; i < playList.size(); i++) {
      if (playlistName.equalsIgnoreCase(playList.get(i).getName())) {
        return playList.get(i);
      }
    }
    return null;
  }

  public void createPlaylist(String playlistName) {
    if (findPlaylist(playlistName) != null)
    {
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    }
    else
    {
      VideoPlaylist newPlaylist = new VideoPlaylist(playlistName);
      playList.add(newPlaylist);
      System.out.println("Successfully created new playlist: "+ playlistName);
    }
  }

  public boolean isInPlaylist(VideoPlaylist list, String videoId)
  {
    List<Video> contents = list.getContents();
    for (int i = 0; i < contents.size(); i++)
    {
      if (contents.get(i).getVideoId().equals(videoId))
      {
        return true;
      }
    }
    return false;
  }


  public void addVideoToPlaylist(String playlistName, String videoId) {
    VideoPlaylist currentPlaylist = findPlaylist(playlistName);
    if (currentPlaylist != null)
    {
      Video video = videoLibrary.getVideo(videoId);
      if (video != null)
      {
        if (video.isFlagged()) //check if flagged
        {
          System.out.println("Cannot add video to "+ playlistName+ ": Video is currently flagged (reason: "+ video.flagReason()+ ")");
          return;
        }
        if (isInPlaylist(currentPlaylist, videoId))
        {
          System.out.println("Cannot add video to "+ playlistName+ ": Video already added");
        }
        else
        {
          System.out.println("Added video to "+ playlistName + ": "+ video.getTitle());
          currentPlaylist.addVideo(video);
        }
      }
      else
      {
        System.out.println("Cannot add video to "+ playlistName + ": Video does not exist");
      }
    }
    else
    {
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
    }
  }

  public void showAllPlaylists() {
    if (playList.size() == 0)
    {
      System.out.println("No playlists exist yet");
    }
    else
    {
      List<String> playlistNames = new ArrayList<>();
      for (int i = 0; i < playList.size(); i++)
      {
        playlistNames.add(playList.get(i).getName());
      }
      System.out.println("Showing all playlists:");
      List<String> sortedPlaylist = playlistNames.stream().sorted().collect(Collectors.toList()); //sorting videos by title
      sortedPlaylist.forEach(System.out::println);
    }
  }

  public void showPlaylist(String playlistName) {
    if (findPlaylist(playlistName) == null)
    {
      System.out.println("Cannot show playlist "+playlistName+": Playlist does not exist");
    }
    else
    {
      System.out.println("Showing playlist: "+ playlistName);
      List<Video> videos = findPlaylist(playlistName).getContents();
      if (videos.size() == 0)
      {
        System.out.println("No videos here yet");
      }
      else
      {
        for (int i = 0; i < videos.size(); i++)
        {
          System.out.println(videos.get(i).videoStringWithFlag());
        }
      }
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    VideoPlaylist playlist = findPlaylist(playlistName);
    Video video = videoLibrary.getVideo(videoId);
    if (playlist != null)
    {
      if (video == null)
      {
        System.out.println("Cannot remove video from "+playlistName+": Video does not exist");
      }
      else if (isInPlaylist(playlist, videoId))
      {
        playlist.removeVideo(video);
        System.out.println("Removed video from " + playlistName + ": "+ videoLibrary.getVideo(videoId).getTitle());
      }
      else
      {
        System.out.println("Cannot remove video from "+playlistName+": Video is not in playlist");
      }
    }
    else
    {
      System.out.println("Cannot remove video from "+playlistName+": Playlist does not exist");
    }

  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist playlist = findPlaylist(playlistName);
    if (playlist != null)
    {
      playlist.clearPlaylist();
      System.out.println("Successfully removed all videos from "+ playlistName);
    }
    else
    {
      System.out.println("Cannot clear playlist "+ playlistName + ": Playlist does not exist");
    }
  }

  public void deletePlaylist(String playlistName) {
    VideoPlaylist playlist = findPlaylist(playlistName);
    if (playlist != null)
    {
      playlist = null;
      System.out.println("Deleted playlist: "+playlistName);
    }
    else
    {
      System.out.println("Cannot delete playlist "+ playlistName + ": Playlist does not exist");
    }
  }

  public void searchVideos(String searchTerm) {

    String strippedSearchTerm = searchTerm.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    List<Video> videos = videoLibrary.getVideos();
    List<Video> searchResults = new ArrayList<>();
    //getting search results
    for (int i = 0; i < videos.size(); i++)
    {
      String videoName = videos.get(i).getTitle();
      if (videoName.replaceAll("[^a-zA-Z0-9]", "").toLowerCase().contains(strippedSearchTerm))
      {
        if (!videos.get(i).isFlagged())
        {
          searchResults.add(videos.get(i));
        }
      }
    }
    if (searchResults.size() != 0)
    {
      printAndPlayAfterSearch(orderVideosByTitle(searchResults), searchTerm); //print sorted results
    }
    else
    {
      System.out.println("No search results for "+ searchTerm);
    }
  }

  //asks user if he wants to play a video from a list
  public void printAndPlayAfterSearch(List<Video> videos, String searchTerm)
  {
    System.out.println("Here are the results for "+ searchTerm + ":");
    for (int i = 0; i < videos.size(); i++)
    {
      System.out.println((i+1) + ") " + videos.get(i).videoString());
    }
    System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
    System.out.println("If your answer is not a valid number, we will assume it's a no.");
    //get input
    Scanner input = new Scanner(System.in);
    try {
      int videoNumber = input.nextInt();
      if (videoNumber > 0 && videoNumber < videos.size() + 1) {
        playVideo(videos.get(videoNumber-1).getVideoId());
      }
    }
    catch (InputMismatchException ignored)
    {
    }
  }

  public List<Video> orderVideosByTitle(List<Video> unsortedVideos)
  {
    // make a string list of unsorted videos
    List <String> unsortedVideoTitles = new ArrayList<>();
    for (int i = 0; i < unsortedVideos.size(); i++)
    {
      unsortedVideoTitles.add(unsortedVideos.get(i).getTitle());
    }
    //sort string list
    List<String> sortedVideoTitles = unsortedVideoTitles.stream().sorted().collect(Collectors.toList());

    //convert back to video list
    List<Video> sortedVideos = new ArrayList<>();
    for (int i = 0; i < sortedVideoTitles.size(); i++) //loop through sorted list
    {
      for (int j = 0; j < unsortedVideos.size();j++) //loop through unsorted list
      {
        //get corresponding video
        if (sortedVideoTitles.get(i).equals(unsortedVideos.get(j).getTitle()))
        {
          sortedVideos.add(unsortedVideos.get(j));
        }
      }
    }
    return sortedVideos;
  }

  public void searchVideosWithTag(String videoTag) {
    List<Video> videos = videoLibrary.getVideos();
    List<Video> searchResults = new ArrayList<>();
    //getting search results
    for (int i = 0; i < videos.size(); i++)
    {
      List<String> videoTags = videos.get(i).getTags();
      for (int j = 0; j < videoTags.size(); j++)
      {
        if (videoTags.get(j).equals(videoTag))
        {
          if (!videos.get(i).isFlagged())
          {
            searchResults.add(videos.get(i));
          }
        }
      }
    }
    if (searchResults.size() != 0)
    {
      printAndPlayAfterSearch(orderVideosByTitle(searchResults), videoTag);
    }
    else
    {
      System.out.println("No search results for "+ videoTag);
    }
  }

  public void flagVideo(String videoId) {
    flagVideo(videoId, "Not supplied");
  }

  public void flagVideo(String videoId, String reason) {
    Video video = videoLibrary.getVideo(videoId);
    if (video != null)
    {
      if (!video.isFlagged())
      {
        video.flagVideo(reason);
        if (video.isPlaying)
        {
          stopVideo();
        }
        System.out.println("Successfully flagged video: "+ video.getTitle() + " (reason: "+ reason + ")");
      }
      else
      {
        System.out.println("Cannot flag video: Video is already flagged");
      }
    }
    else
    {
      System.out.println("Cannot flag video: Video does not exist");
    }
  }

  public void allowVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if (video != null)
    {
      if (video.isFlagged())
      {
        video.removeFlag();
        System.out.println("Successfully removed flag from video: "+ video.getTitle());
      }
      else
      {
        System.out.println("Cannot remove flag from video: Video is not flagged");
      }
    }
    else
    {
      System.out.println("Cannot remove flag from video: Video does not exist");
    }
  }

  public Video videoPlaying()
  {
    for (int i = 0; i < videoLibrary.getVideos().size(); i++)
    {
      Video video = videoLibrary.getVideos().get(i);
      if (video.isPlaying)
        return video;
    }
    return null;
  }

  public void resetPause()
  {
    for (int i = 0; i < videoLibrary.getVideos().size(); i++)
    {
      Video video = videoLibrary.getVideos().get(i);
      if (video.isPaused)
      {
        video.isPaused = false;
      }
    }
  }

  public boolean checkVideoAvailability()
  {
    List<Video> videos =videoLibrary.getVideos();
    for (int i = 0; i < videos.size(); i++)
    {
      if (!videos.get(i).isFlagged())
      {
        return true;
      }
    }
    return false;
  }

}