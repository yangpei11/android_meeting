package org.mediasoup.droid.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import org.mediasoup.droid.demo.R;
import org.mediasoup.droid.demo.databinding.TwoPersonBinding;
import org.mediasoup.droid.demo.vm.MeProps;
import org.mediasoup.droid.demo.vm.PeerProps;
import org.mediasoup.droid.lib.PeerConnectionUtils;
import org.mediasoup.droid.lib.RoomClient;
import org.mediasoup.droid.lib.lv.RoomStore;
import org.mediasoup.droid.lib.model.Peer;

public class TwoPersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    @NonNull private RoomStore mStore;
    @NonNull private LifecycleOwner mLifecycleOwner;

    @NonNull private MeProps mMeProps;

    @NonNull private Peer mPeerInfo;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TwoPersonBinding binding = DataBindingUtil.inflate(inflater, R.layout.two_person, parent, false);
        return new TwoPersonViewHolder(binding, new PeerProps(((AppCompatActivity) context).getApplication(), mStore));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TwoPersonViewHolder) {
            ((TwoPersonViewHolder) holder).bind(mLifecycleOwner, mMeProps, mPeerInfo);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    public TwoPersonAdapter(
            @NonNull RoomStore store,
            @NonNull LifecycleOwner lifecycleOwner,
            @NonNull MeProps meProps,
            @NonNull Peer peer) {
        mStore = store;
        mLifecycleOwner = lifecycleOwner;
        mMeProps = meProps;
        mPeerInfo = peer;
    }

    public static class TwoPersonViewHolder extends RecyclerView.ViewHolder {
        private final TwoPersonBinding binding;
        @NonNull final PeerProps mPeerProps;

        public TwoPersonViewHolder(TwoPersonBinding binding, PeerProps peerProps) {
            super(binding.getRoot());
            this.binding = binding;
            mPeerProps = peerProps;
            binding.videoRenderer.init(PeerConnectionUtils.getEglContext(), null);
            binding.videoRenderer1.init(PeerConnectionUtils.getEglContext(), null);
        }

        public void bind(LifecycleOwner owner, MeProps meProps, Peer peerInfo) {
            binding.setMeViewProps(meProps);
            mPeerProps.connect(owner, peerInfo.getId());
            binding.setPeerViewProps(mPeerProps);
            binding.executePendingBindings(); // 确保绑定立即执行
        }
    }

}
