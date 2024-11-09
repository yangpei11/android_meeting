package org.mediasoup.droid.demo.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import org.mediasoup.droid.Logger;
import org.mediasoup.droid.demo.R;
import org.mediasoup.droid.demo.view.MePeerView;
import org.mediasoup.droid.demo.view.PeerView;
import org.mediasoup.droid.demo.vm.EdiasProps;
import org.mediasoup.droid.demo.vm.MeProps;
import org.mediasoup.droid.demo.vm.PeerProps;
import org.mediasoup.droid.lib.RoomClient;
import org.mediasoup.droid.lib.lv.RoomStore;
import org.mediasoup.droid.lib.model.Peer;

import java.util.LinkedList;
import java.util.List;

//这是适配器
public class PeerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final String TAG = "PeerAdapter";

  private static final int VIEW_TYPE_ME = 0;
  private static final int VIEW_TYPE_PEER = 1;

  @NonNull private RoomStore mStore;
  @NonNull private LifecycleOwner mLifecycleOwner;
  @NonNull private RoomClient mRoomClient;

  @NonNull private MeProps mMeProps;

  int height = 0;

  private List<Peer> mPeers = new LinkedList<>();

  //

  private int containerHeight;

  public PeerAdapter(
      @NonNull RoomStore store,
      @NonNull LifecycleOwner lifecycleOwner,
      @NonNull RoomClient roomClient,
      @NonNull MeProps meProps) {
    mStore = store;
    mLifecycleOwner = lifecycleOwner;
    mRoomClient = roomClient;
    mMeProps = meProps;
  }

  public void replacePeers(@NonNull List<Peer> peers) {
    mPeers = peers;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    containerHeight = parent.getHeight();
    Context context = parent.getContext();
    height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());
//    View view = LayoutInflater.from(context).inflate(R.layout.item_remote_peer, parent, false);
//    return new PeerViewHolder(
//        view, new PeerProps(((AppCompatActivity) context).getApplication(), mStore));
    if (viewType == VIEW_TYPE_ME) {
      View meView = LayoutInflater.from(context).inflate(R.layout.item_local_me, parent, false);
      return new MeViewHolder(meView, mMeProps);  // 使用相同的 ViewHolder 或为 MeView 创建一个专门的 ViewHolder
    } else {
      View peerView = LayoutInflater.from(context).inflate(R.layout.item_remote_peer, parent, false);
      return new PeerViewHolder(peerView, new PeerProps(((AppCompatActivity) context).getApplication(), mStore));
    }
  }


  @Override
  public int getItemViewType(int position) {
    return position == 0 ? VIEW_TYPE_ME : VIEW_TYPE_PEER;
  }

//  @Override
//  public void onBindViewHolder(@NonNull PeerViewHolder holder, int position) {
//    // update height
//    ViewGroup.LayoutParams layoutParams = holder.mPeerView.getLayoutParams();
//    layoutParams.height = getItemHeight();
//    Logger.d(TAG, "posX: " + position);
//    Logger.d(TAG, "peer size is: " + mPeers.size());
//    holder.mPeerView.setLayoutParams(layoutParams);
//    // bind
//    holder.bind(mLifecycleOwner, mRoomClient, mPeers.get(position));
//  }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    Logger.d("pos", "pos is: " + position);
      if (getItemViewType(position) == VIEW_TYPE_ME) {
        // Cast and bind for MeViewHolder
        ViewGroup.LayoutParams layoutParams = ((MeViewHolder)holder).mMeView.getLayoutParams();
        layoutParams.height = getItemHeight();
        ((MeViewHolder)holder).mMeView.setLayoutParams(layoutParams);
        ((MeViewHolder) holder).bind(mLifecycleOwner, mRoomClient, mMeProps);
      } else {
        // Cast and bind for PeerViewHolder
        ViewGroup.LayoutParams layoutParams = ((PeerViewHolder)holder).mPeerView.getLayoutParams();
        layoutParams.height = getItemHeight();
        ((PeerViewHolder)holder).mPeerView.setLayoutParams(layoutParams);
        ((PeerViewHolder) holder).bind(mLifecycleOwner, mRoomClient, mPeers.get(position-1));
      }
    }

  @Override
  public int getItemCount() {
    return mPeers.size()+1;
  }

  private int getItemHeight() {
//    int itemCount = getItemCount();
//    if (itemCount <= 1) {
//      return containerHeight;
//    } else if (itemCount <= 3) {
//      return containerHeight / itemCount;
//    } else {
//      return (int) (containerHeight / 3.2);
//    }
    return height;

  }

  static class PeerViewHolder extends RecyclerView.ViewHolder {

    @NonNull final PeerView mPeerView;
    @NonNull final PeerProps mPeerProps;

    PeerViewHolder(@NonNull View view, @NonNull PeerProps peerProps) {
      super(view);
      mPeerView = view.findViewById(R.id.remote_peer);
      mPeerProps = peerProps;
    }

    void bind(LifecycleOwner owner, RoomClient roomClient, @NonNull Peer peer) {
      Logger.d(TAG, "bind() id: " + peer.getId() + ", name: " + peer.getDisplayName());
      mPeerProps.connect(owner, peer.getId());
      mPeerView.setProps(mPeerProps, roomClient);
    }
  }


  static class MeViewHolder extends RecyclerView.ViewHolder {

    @NonNull final MePeerView mMeView;
    @NonNull final MeProps mMeProps;

    MeViewHolder(@NonNull View view, @NonNull MeProps meProps) {
      super(view);
      mMeView = view.findViewById(R.id.local_me);
      mMeProps = meProps;
    }

    void bind(LifecycleOwner owner, RoomClient roomClient, MeProps meProps) {
//      Logger.d(TAG, "bind() id: " + peer.getId() + ", name: " + peer.getDisplayName());
//      mPeerProps.connect(owner, peer.getId());
      mMeView.setProps(meProps, roomClient);
    }
  }
}
