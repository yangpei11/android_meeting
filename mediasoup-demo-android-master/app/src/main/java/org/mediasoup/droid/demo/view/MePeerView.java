package org.mediasoup.droid.demo.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import org.mediasoup.droid.demo.R;
import org.mediasoup.droid.demo.databinding.ViewMeBindingImpl;
import org.mediasoup.droid.demo.databinding.ViewMePeerViewBindingImpl;
import org.mediasoup.droid.demo.vm.MeProps;
import org.mediasoup.droid.lib.RoomClient;


public class MePeerView extends RelativeLayout {

    public MePeerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MePeerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MePeerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MePeerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    ViewMePeerViewBindingImpl mBinding;

    private void init(Context context) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_me_peer_view, this, true);
        //mBinding.peerView.videoRenderer.init(PeerConnectionUtils.getEglContext(), null);
    }

    public void setProps(MeProps props, final RoomClient roomClient) {

        // set view model.
        //mBinding.peerView.setPeerViewProps(props);

        // register click listener.
//    mBinding.peerView.info.setOnClickListener(
//        view -> {
//          Boolean showInfo = props.getShowInfo().get();
//          props.getShowInfo().set(showInfo != null && showInfo ? Boolean.FALSE : Boolean.TRUE);
//        });
//
//    mBinding.peerView.meDisplayName.setOnEditorActionListener(
//        (textView, actionId, keyEvent) -> {
//          if (actionId == EditorInfo.IME_ACTION_DONE) {
//            roomClient.changeDisplayName(textView.getText().toString().trim());
//            return true;
//          }
//          return false;
//        });
//    mBinding.peerView.stats.setOnClickListener(
//        view -> {
//          // TODO(HaiyangWU): Handle inner click event;
//        });
//
//    mBinding.peerView.videoRenderer.setZOrderMediaOverlay(true);

        // set view model.
        mBinding.setMeProps(props);
    }
}
