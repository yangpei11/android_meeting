package org.mediasoup.droid.lib.model;

import static org.mediasoup.droid.lib.RoomClient.ConnectionState;
import static org.mediasoup.droid.lib.RoomClient.ConnectionState.NEW;

import java.util.HashMap;
import java.util.Map;

public class RoomInfo {

  private String mUrl;
  private String mRoomId;
  private ConnectionState mConnectionState = NEW;
  private String mActiveSpeakerId;
  private String mStatsPeerId;
  private boolean mFaceDetection = false;

  private Map<String, Integer> mActiveSpeakers = new HashMap<>();
  public String getUrl() {
    return mUrl;
  }

  public void setUrl(String url) {
    this.mUrl = url;
  }

  public String getRoomId() {
    return mRoomId;
  }

  public void setRoomId(String roomId) {
    this.mRoomId = roomId;
  }

  public ConnectionState getConnectionState() {
    return mConnectionState;
  }

  public void setConnectionState(ConnectionState connectionState) {
    this.mConnectionState = connectionState;
  }

  public String getActiveSpeakerId() {
    return mActiveSpeakerId;
  }

  public int getVolume(String peerId){
    if(mActiveSpeakers.containsKey(peerId)){
      //-127-0
      int volume = mActiveSpeakers.get(peerId);
      return (volume+127) * 100/127;
    }else{
      return -1;
    }
  }

  public void setActiveSpeakerId(String activeSpeakerId, int volume) {
    this.mActiveSpeakerId = activeSpeakerId;
    mActiveSpeakers.put(activeSpeakerId, volume);
  }

  public void clearActiveSpeakers(){
    mActiveSpeakers.clear();
  }

  public String getStatsPeerId() {
    return mStatsPeerId;
  }

  public void setStatsPeerId(String statsPeerId) {
    this.mStatsPeerId = statsPeerId;
  }

  public boolean isFaceDetection() {
    return mFaceDetection;
  }

  public void setFaceDetection(boolean faceDetection) {
    this.mFaceDetection = faceDetection;
  }
}
