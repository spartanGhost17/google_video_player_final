package com.google;

import java.util.*;
import java.util.stream.Collectors;


public class VideoPlayer {
  private String currentlyPlayingID;
  private String pausedID;
  private HashMap<String, VideoPlaylist> playlistManager;
  private final VideoLibrary videoLibrary;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    this.pausedID = "";
    this.currentlyPlayingID = "";
    this.playlistManager = new HashMap<>();
  }

  /**
   * Get Video obj of currently playing video by field Id
   * @return Video object
   */
  public Video currentlyPlayingVideo(){ return videoLibrary.getVideo(currentlyPlayingID); }

  /**
   * sort videos by method reference getTitle
   * @return sorted list of videos
   */
  public List<Video> sortedVideos(){
    //sort by title without modifying original List use method reference title as sort comparator
     List<Video> sortedVideos = videoLibrary.getVideos().stream()
            .sorted(Comparator.comparing(Video::getTitle)).collect(Collectors.toList());
     return sortedVideos;
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  /** Shows all videos in video library */
  public void showAllVideos() {
    //sort by title
    System.out.println("Here's a list of all available videos:");
    //print all videos
    for(Video video : sortedVideos()){
      if(video.getFlagStatus())  //if video is flagged
          System.out.printf("%s (%s) %s - FLAGGED (reason: %s)\n", video.getTitle(),
                  video.getVideoId(), Arrays.toString(video.getTags().toArray())
                  .replaceAll(",",""), video.getFlagReason());
      else //video was not flagged
          System.out.printf("%s (%s) %s\n", video.getTitle(),
                  video.getVideoId(), Arrays.toString(video.getTags().toArray())
                  .replaceAll(",",""));
    }

  }
  /** Plays video and may reset pausedId*/
  public void playVideo(String videoId) {
    Video currentVideo = videoLibrary.getVideo(videoId);
    Video previousVideo = currentlyPlayingVideo();

    if(currentVideo == null){//if video doesn't exist
        System.out.println("Cannot play video: Video does not exist");
    }
    else if(currentVideo.getFlagStatus()){//if video is flagged
        System.out.printf("Cannot play video: Video is currently flagged (reason: %s)",currentVideo.getFlagReason());
    }
    //if video exists
    else{
        //if a video is currently playing, stop
        // else no video was playing
        if(currentlyPlayingID !="")
            System.out.printf("Stopping video: %s\n",previousVideo.getTitle());
        currentlyPlayingID = videoId;//remember id of video currently playing
        System.out.printf("Playing video: %s\n", currentVideo.getTitle());
        pausedID = "";//reset paused status

    }

  }

  /**
   * Stops video and resets currentlyPlayingID
   */
  public void stopVideo() {
    Video previousVideo = currentlyPlayingVideo();
    if(currentlyPlayingID!=""){//if video playing
      System.out.printf("Stopping video: %s\n",previousVideo.getTitle());
      currentlyPlayingID = "";//reset current playing video (it's been paused)
    }
    else//if no video is playing
      System.out.println("Cannot stop video: No video is currently playing");
  }

  /** Plays random video */
  public void playRandomVideo() {
    List<Video> currentList = new ArrayList<>();
    for(Video video : videoLibrary.getVideos()){//remove all flagged videos
        if(!video.getFlagStatus())//if video isn't flagged add video to list
            currentList.add(video);
    }

    //if no available video in available
    if(currentList.size()<=0)
        System.out.println("No videos available");
    else {//play video if available
        Random rng = new Random();
        //generate a random index in range of currentList size
        int randomVideoIndex = rng.nextInt(currentList.size());
        Video video = currentList.get(randomVideoIndex);
        playVideo(video.getVideoId());
    }

  }

  /** Pauses video */
  public void pauseVideo() {
    Video previousVideo = currentlyPlayingVideo();
    if(currentlyPlayingID!=""){//if video currently playing

      if(pausedID.compareTo(currentlyPlayingID)!=0){//if video id was NOT already paused
            pausedID = currentlyPlayingID;//update paused video id
            System.out.printf("Pausing video: %s\n",previousVideo.getTitle());
        }
        else if(pausedID.compareTo(currentlyPlayingID)==0){//if same video paused
            System.out.printf("Video already paused: %s\n",previousVideo.getTitle());
        }
    }
    else {
        //no video currently playing
        System.out.println("Cannot pause video: No video is currently playing");
    }

  }

  public void continueVideo() {
    Video currentVideo = currentlyPlayingVideo();
    if(currentlyPlayingID!=""){//if video is playing
        //if current video is not paused and video
        if(pausedID.compareTo(currentlyPlayingID)!=0){
          System.out.println("Cannot continue video: Video is not paused");
        }
        else if(pausedID.compareTo(currentlyPlayingID)==0){//if current video paused
          System.out.printf("Continuing video: %s", currentVideo.getTitle());
          pausedID = "";//reset paused status
        }
    }
    else//no video was playing
        System.out.println("Cannot continue video: No video is currently playing");

  }

  public void showPlaying() {
      Video currentVideo = currentlyPlayingVideo();
      if(currentlyPlayingID!=""){//if video is playing
          if(pausedID.compareTo(currentlyPlayingID)!=0){//if current video not paused
              System.out.printf("Currently playing: %s (%s) %s\n", currentVideo.getTitle(), currentVideo.getVideoId(), Arrays.toString(currentVideo.getTags().toArray())
                      .replaceAll(",",""));
          }
          else if(pausedID.compareTo(currentlyPlayingID)==0){
              System.out.printf("Currently playing: %s (%s) %s - PAUSED (reason: %s)\n", currentVideo.getTitle(), currentVideo.getVideoId(),
                      Arrays.toString(currentVideo.getTags().toArray())
                      .replaceAll(",",""),currentVideo.getFlagReason());
          }
      }
      else//nothing playing
          System.out.println("No video is currently playing");

  }

  public void createPlaylist(String playlistName) {

      if(playlistManager.containsKey(playlistName.toLowerCase())){//if playlist already exists
            System.out.println("Cannot create playlist: A playlist with the same name already exists");
      }
      else{
            //keep key lower case to avoid clone playlist names
            playlistManager.put(playlistName.toLowerCase(), new VideoPlaylist(playlistName));
            System.out.printf("Successfully created new playlist: %s\n", playlistName);
      }
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
      if(!playlistManager.containsKey(playlistName.toLowerCase()) &&
      videoLibrary.getVideo(videoId)==null)//if both playlist and video don't exist
          System.out.printf("Cannot add video to %s: Playlist does not exist",playlistName);

      else if(!playlistManager.containsKey(playlistName.toLowerCase()) ||
      videoLibrary.getVideo(videoId)==null){//if either playlist or video don't exist
          if(!playlistManager.containsKey(playlistName.toLowerCase()))
            System.out.printf("Cannot add video to %s: Playlist does not exist\n",playlistName);
          else
              System.out.printf("Cannot add video to %s: Video does not exist\n",playlistName);
      }
      else if(videoLibrary.getVideo(videoId).getFlagStatus()){//if video is flagged
          System.out.printf("Cannot add video to my_playlist: " +
                  "Video is currently flagged (reason: %s)\n",videoLibrary.getVideo(videoId).getFlagReason());
      }
      else{

          boolean videoAlreadyExists = playlistManager.get(playlistName.toLowerCase())
                  .getVideosTitles().contains(videoLibrary.getVideo(videoId).getTitle());
          String videoName = videoLibrary.getVideo(videoId).getTitle();

          if(videoAlreadyExists){
              System.out.printf("Cannot add video to %s: Video already added\n",playlistName);
          }
          else{
              playlistManager.get(playlistName.toLowerCase()).getVideosTitles().add(videoLibrary.getVideo(videoId).getTitle());
              //add video to playlist
              playlistManager.get(playlistName.toLowerCase()).addVideos(videoLibrary.getVideo(videoId));
              System.out.printf("Added video to %s: %s\n",playlistName, videoName);
          }
      }
  }

  public void showAllPlaylists() {
      if(playlistManager.keySet().size()>0){
          //sort list of key in ascending order
          List<String> keys = playlistManager.keySet().stream().sorted().collect(Collectors.toList());
          System.out.println("Showing all playlists:");
          for(Map.Entry<String, VideoPlaylist> value : playlistManager.entrySet())
              System.out.println(value.getValue().getPlaylistName());
      }
      else
          System.out.println("No playlists exist yet");

  }

  public void showPlaylist(String playlistName) {
        if(playlistManager.containsKey(playlistName.toLowerCase())){//if playlist exists
            List<Video> listOfVideos = playlistManager.get(playlistName.toLowerCase()).getVideos();
            if(listOfVideos.size()>0){//if playlist contains videos
                System.out.printf("Showing playlist: %s\n",playlistName);
                for(Video video : listOfVideos){
                    if(video.getFlagStatus())//if video flagged
                        System.out.printf("%s (%s) %s - FLAGGED (reason: %s)\n", video.getTitle(), video.getVideoId(),
                                video.getTags().toString().replaceAll(",", ""),video.getFlagReason());
                    else
                        System.out.printf("%s (%s) %s\n",video.getTitle(), video.getVideoId(),
                                video.getTags().toString().replaceAll(",",""));
                }
            }
            else {//no video in playlist
                System.out.printf("Showing playlist: %s\n",playlistName);
                System.out.print("No videos here yet\n");
            }
        }
        else
            System.out.printf("Cannot show playlist %s: Playlist does not exist\n",playlistName);
  }

  public void removeFromPlaylist(String playlistName, String videoId) {

      if(!playlistManager.containsKey(playlistName.toLowerCase())
      && videoLibrary.getVideo(videoId)==null)//if playlist doesn't exist
          System.out.printf("Cannot remove video from %s: Playlist does not exist\n", playlistName);

      else if(!playlistManager.containsKey(playlistName.toLowerCase())
      || videoLibrary.getVideo(videoId)==null){//if playlist or video don't exist
          if(!playlistManager.containsKey(playlistName.toLowerCase()))
                System.out.printf("Cannot remove video from %s: Playlist does not exist\n", playlistName);
          else if(videoLibrary.getVideo(videoId)==null)
               System.out.printf("Cannot remove video from %s: Video does not exist\n", playlistName);
      }

      else{
          Video video = videoLibrary.getVideo(videoId);
          //remove video from playlist if video in playlist
          if(playlistManager.get(playlistName.toLowerCase()).getVideosTitles().contains(video.getTitle())){
              playlistManager.get(playlistName.toLowerCase()).getVideos().remove(video);//remove video from playlist
              playlistManager.get(playlistName.toLowerCase()).getVideosTitles().remove(video.getTitle());
              System.out.printf("Removed video from %s: %s\n", playlistName, video.getTitle());
          }
          else{
              System.out.printf("Cannot remove video from %s: Video is not in playlist\n", playlistName);
          }

      }
  }

  public void clearPlaylist(String playlistName) {

      if(!playlistManager.containsKey(playlistName.toLowerCase())){//playlist doesn't exist
          System.out.printf("Cannot clear playlist %s: Playlist does not exist\n", playlistName);
      }
      else{//playlist does exist
          playlistManager.get(playlistName.toLowerCase()).getVideos().clear();
          playlistManager.get(playlistName.toLowerCase()).getVideosTitles().clear();
          System.out.printf("Successfully removed all videos from %s\n",playlistName);

      }
  }

  public void deletePlaylist(String playlistName) {
      if(!playlistManager.containsKey(playlistName.toLowerCase())){//if playlist doesn't exist
          System.out.printf("Cannot delete playlist %s: Playlist does not exist\n",playlistName);
      }
      else{
          playlistManager.remove(playlistName.toLowerCase());//delete entry if playlist exists
          System.out.printf("Deleted playlist: %s\n",playlistName);
      }
  }

  public void searchVideos(String searchTerm) {
      List<Video> matchingVideos = new ArrayList<>();
      //find matching substring for each video title in videoLibrary
      for(Video video : videoLibrary.getVideos()){
          if(video.getTitle().toLowerCase()
                  .contains(searchTerm.toLowerCase())){
              if(!video.getFlagStatus())//if video is not flagged add to list
                  matchingVideos.add(video);
          }
      }
      //helper function handles user script interaction
      helperMetd_SearchVideoWithAndWithoutTag(matchingVideos, searchTerm);

  }

    /**
     * helper method for user script interraction for both
     * searchVideos and searchVideosWithTag methods
     * @param matchingVideos list of videos matching search term
     * @param searchTerm String search term or search term with tag
     */
  public void helperMetd_SearchVideoWithAndWithoutTag (List<Video> matchingVideos, String searchTerm){
      matchingVideos.sort(Comparator.comparing(Video::getTitle));//sort original list (ascending order)
      if(matchingVideos.size()<=0)//search term not found
          System.out.printf("No search results for %s\n",searchTerm);

      else{//search term found
          System.out.printf("Here are the results for %s:\n",searchTerm);
          int i = 1;
          for(Video vid : matchingVideos){
              System.out.printf("%d) %s (%s) %s\n",i,vid.getTitle(),vid.getVideoId(), vid.getTags().toString().replaceAll(",",""));
              i++;
          }

          System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
          Scanner scanner = new Scanner(System.in);
          System.out.println("If your answer is not a valid number, we will assume it's a no.");
          try{
              int index = scanner.nextInt();//get index of video user wants to watch
              if(index < matchingVideos.size())//if user input is in range of list of videos
                  playVideo(matchingVideos.get((index-1)).getVideoId());//play desired video
          }
          catch (Exception e){//user did not enter integer
          }

      }
  }


  public void searchVideosWithTag(String videoTag) {
      List<Video> matchingVideos = new ArrayList<>();

      //find matching substring for each video title in videoLibrary
      for(Video video : videoLibrary.getVideos()){
          if(video.getTags().toString().toLowerCase()
                  .contains(videoTag.toLowerCase())){
              if(!video.getFlagStatus())//if video is not flagged add to list
                  matchingVideos.add(video);
          }
      }
      //helper function handles user script interaction
      helperMetd_SearchVideoWithAndWithoutTag(matchingVideos, videoTag);
  }

  public void flagVideo(String videoId) {
      String flagReason = "Not supplied";//default flag reason
      if(videoLibrary.getVideo(videoId)==null)//if video does not exist
          System.out.println("Cannot flag video: Video does not exist");

      else if(videoLibrary.getVideo(videoId).getFlagStatus())//if already flagged
          System.out.println("Cannot flag video: Video is already flagged");

      else{
          //flag video
          Video video = videoLibrary.getVideo(videoId);
          video.setFlagReason(flagReason);
          video.setFlagStatus(true);
          System.out.printf("Successfully flagged video: %s (reason: %s)\n", video.getTitle(),flagReason);
      }
  }

  public void flagVideo(String videoId, String reason) {
      if(videoLibrary.getVideo(videoId)==null)//if video does not exist
          System.out.println("Cannot flag video: Video does not exist");

      else if(videoLibrary.getVideo(videoId).getFlagStatus())//if already flagged
          System.out.println("Cannot flag video: Video is already flagged");

      else{//if video isn't flagged and exits
          //flag video
          Video video = videoLibrary.getVideo(videoId);
          video.setFlagReason(reason);
          video.setFlagStatus(true);
          if(videoId.compareTo(currentlyPlayingID)==0){//if flagged video is same as currently playing
              currentlyPlayingID = "";
              System.out.printf("Stopping video: %s\n",video.getTitle());
          }
          System.out.printf("Successfully flagged video: %s (reason: %s)\n", video.getTitle(),reason);

      }
  }

  public void allowVideo(String videoId) {
      Video video = videoLibrary.getVideo(videoId);
      if(video==null)//if video does not exist
          System.out.println("Cannot remove flag from video: Video does not exist");

      else if(!video.getFlagStatus())
          System.out.printf("Cannot remove flag from video: Video is not flagged");
      else{
          //remove video flag
          video.setFlagReason("");
          video.setFlagStatus(false);
          System.out.printf("Successfully removed flag from video: %s\n",video.getTitle());
      }

  }
}