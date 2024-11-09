package org.mediasoup.droid.demo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import org.mediasoup.droid.demo.vm.MeProps;
import org.mediasoup.droid.lib.RoomClient;
import org.mediasoup.droid.lib.model.DeviceInfo;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

public class BindingAdapters {

  private static final String TAG = "BindingAdapters";

  @BindingAdapter({"bind:edias_state", "bind:edias_state_animation"})
  public static void roomState(
      ImageView view, RoomClient.ConnectionState state, Animation animation) {
    if (state == null) {
      return;
    }
    if (RoomClient.ConnectionState.CONNECTING.equals(state)) {
      view.setImageResource(R.drawable.ic_state_connecting);
      view.startAnimation(animation);
    } else if (RoomClient.ConnectionState.CONNECTED.equals(state)) {
      view.setImageResource(R.drawable.ic_state_connected);
      animation.cancel();
      view.clearAnimation();
    } else {
      view.setImageResource(R.drawable.ic_state_new_close);
      animation.cancel();
      view.clearAnimation();
    }
  }

//  @BindingAdapter({"bind:edias_enable_audio", "bind:edias_is_speaking", "bind:volumeBar"})
//  public static void micState(ImageView view, boolean enableAudio, boolean isSpeaking, View volumeBar) {
//    if (!enableAudio) {
//      view.setImageResource(R.drawable.icon_mic_white_off);
//      volumeBar.clearAnimation();
//      volumeBar.setVisibility(View.GONE);
//    } else if (!isSpeaking) {
//      view.setImageResource(R.mipmap.icon_microphone_normal);
//      //view.setImageResource(R.drawable.icon_mic_black_on);
//      volumeBar.clearAnimation();
//      volumeBar.setVisibility(View.GONE);
//    } else {
//      view.setImageResource(R.mipmap.icon_microphone_normal);
//      volumeBar.setVisibility(View.VISIBLE);
//      int targetHeight = 100; // 目标高度为100dp
//
//      ValueAnimator animator = ValueAnimator.ofInt(volumeBar.getHeight(), dpToPx(volumeBar.getContext(), targetHeight));
//      animator.addUpdateListener(animation -> {
//        int animatedValue = (int) animation.getAnimatedValue();
//        ViewGroup.LayoutParams layoutParams = volumeBar.getLayoutParams();
//        layoutParams.height = animatedValue;
//        volumeBar.setLayoutParams(layoutParams);
//      });
//      animator.setDuration(500);
//      animator.setRepeatMode(ValueAnimator.REVERSE);
//      animator.setRepeatCount(ValueAnimator.INFINITE);
//      animator.start();
//    }
//  }

  @BindingAdapter({"bind:edias_enable_audio", "bind:edias_is_speaking", "bind:edias_volume"})
  public static void micState(ImageView view, boolean enableAudio, boolean isSpeaking, int volume) {
    if (!enableAudio) {
      view.setImageResource(R.drawable.icon_mic_white_off);
    } else if (!isSpeaking) {
      view.setImageResource(R.mipmap.icon_microphone_normal);
      //view.setImageResource(R.drawable.icon_mic_black_on);
    } else {
      view.setImageResource(R.drawable.layer_recording_animation);
      Drawable drawable = view.getDrawable();
      int baseLevel = 4300;
      int realLevel = baseLevel + (10000-4300)*volume/100;
      int lastLevel = drawable.getLevel();
      if (drawable instanceof LayerDrawable) {
        LayerDrawable layerDrawable = (LayerDrawable) drawable;
        ClipDrawable clipDrawable = (ClipDrawable) layerDrawable.findDrawableByLayerId(android.R.id.progress);

        // 创建并启动 ObjectAnimator 动画
        ObjectAnimator animator = ObjectAnimator.ofInt(clipDrawable, "level", lastLevel, realLevel);
        animator.setDuration(300); // 动画持续时间，例如1000毫秒
        animator.setInterpolator(new LinearInterpolator()); // 设置插值器，也可以选择其他如 AccelerateInterpolator 等
        animator.start();
      }
    }
  }

  private static int dpToPx(Context context, int dp) {
    return (int) (dp * context.getResources().getDisplayMetrics().density);
  }


  @BindingAdapter({"bind:edias_link"})
  public static void inviteLink(TextView view, String inviteLink) {
    view.setVisibility(TextUtils.isEmpty(inviteLink) ? View.INVISIBLE : View.VISIBLE);
  }

  @BindingAdapter({"bind:edias_hide_videos", "bind:edias_hide_videos_progress"})
  public static void hideVideos(ImageView view, boolean audioOnly, boolean audioOnlyInProgress) {
    view.setEnabled(!audioOnlyInProgress);
    if (!audioOnly) {
      view.setBackgroundResource(R.drawable.bg_left_box_off);
      view.setImageResource(R.drawable.icon_video_white_off);
    } else {
      view.setBackgroundResource(R.drawable.bg_left_box_on);
      view.setImageResource(R.drawable.icon_video_black_on);
    }
  }

  @BindingAdapter({"bind:edias_audio_muted"})
  public static void audioMuted(ImageView view, boolean audioMuted) {
    if (!audioMuted) {
      view.setBackgroundResource(R.drawable.bg_left_box_off);
      view.setImageResource(R.drawable.icon_volume_white_off);
    } else {
      view.setBackgroundResource(R.drawable.bg_left_box_on);
      view.setImageResource(R.drawable.icon_volume_black_on);
    }
  }

  @BindingAdapter({"bind:edias_restart_ice_progress", "bind:edias_restart_ice_ani"})
  public static void restartIce(
      ImageView view, boolean restart_ice_in_progress, Animation animation) {
    Log.d(TAG, "restartIce() " + restart_ice_in_progress);
    view.setEnabled(!restart_ice_in_progress);
    if (restart_ice_in_progress) {
      view.startAnimation(animation);
    } else {
      animation.cancel();
      view.clearAnimation();
    }
  }

  @BindingAdapter({"bind:edias_device"})
  public static void deviceInfo(TextView view, DeviceInfo deviceInfo) {
    if (deviceInfo == null) {
      return;
    }

    int deviceIcon = R.drawable.ic_unknown;
    if (!TextUtils.isEmpty(deviceInfo.getFlag())) {
      switch (deviceInfo.getFlag().toLowerCase()) {
        case "chrome":
          deviceIcon = R.mipmap.chrome;
          break;
        case "firefox":
          deviceIcon = R.mipmap.firefox;
          break;
        case "safari":
          deviceIcon = R.mipmap.safari;
          break;
        case "opera":
          deviceIcon = R.mipmap.opera;
          break;
        case "edge":
          deviceIcon = R.mipmap.edge;
          break;
        case "android":
          deviceIcon = R.mipmap.android;
          break;
      }
      view.setText(deviceInfo.getName() + " " + deviceInfo.getVersion());
    } else {
      view.setText("");
    }
    view.setCompoundDrawablesWithIntrinsicBounds(deviceIcon, 0, 0, 0);
  }

  @BindingAdapter({"edias_mic_state"})
  public static void deviceMicState(ImageView imageView, MeProps.DeviceState state) {
    if (state == null) {
      return;
    }
    Log.d(TAG, "edias_mic_state: " + state.name());
    if (MeProps.DeviceState.ON.equals(state)) {
      imageView.setBackgroundResource(R.drawable.bg_media_box_on);
    } else {
      imageView.setBackgroundResource(R.drawable.bg_media_box_off);
    }

    switch (state) {
      case ON:
        imageView.setImageResource(R.drawable.icon_mic_black_on);
        break;
      case OFF:
        imageView.setImageResource(R.drawable.icon_mic_white_off);
        break;
      case UNSUPPORTED:
        imageView.setImageResource(R.drawable.icon_mic_white_unsupported);
        break;
    }
  }

  @BindingAdapter({"edias_cam_state"})
  public static void deviceCamState(ImageView imageView, MeProps.DeviceState state) {
    if (state == null) {
      return;
    }
    Log.d(TAG, "edias_cam_state: " + state.name());
    if (MeProps.DeviceState.ON.equals(state)) {
      imageView.setBackgroundResource(R.drawable.bg_media_box_on);
    } else {
      imageView.setBackgroundResource(R.drawable.bg_media_box_off);
    }

    switch (state) {
      case ON:
        imageView.setImageResource(R.drawable.icon_webcam_black_on);
        break;
      case OFF:
        imageView.setImageResource(R.drawable.icon_webcam_white_off);
        break;
      case UNSUPPORTED:
        imageView.setImageResource(R.drawable.icon_webcam_white_unsupported);
        break;
    }
  }

  @BindingAdapter({"edias_change_came_state"})
  public static void changeCamState(View view, MeProps.DeviceState state) {
    if (state == null) {
      return;
    }
    Log.d(TAG, "edias_change_came_state: " + state.name());
    if (MeProps.DeviceState.ON.equals(state)) {
      view.setEnabled(true);
    } else {
      view.setEnabled(false);
    }
  }

  @BindingAdapter({"edias_share_state"})
  public static void shareState(View view, MeProps.DeviceState state) {
    if (state == null) {
      return;
    }
    Log.d(TAG, "edias_share_state: " + state.name());
    if (MeProps.DeviceState.ON.equals(state)) {
      view.setEnabled(true);
    } else {
      view.setEnabled(false);
    }
  }

  @BindingAdapter({"edias_render"})
  public static void render(SurfaceViewRenderer renderer, VideoTrack track) {
    Log.d(TAG, "edias_render: " + (track != null));
    if (track != null) {
      track.addSink(renderer);
      renderer.setVisibility(View.VISIBLE);
    } else {
      renderer.setVisibility(View.GONE);
    }
  }

  @BindingAdapter({"edias_render_empty"})
  public static void renderEmpty(View renderer, VideoTrack track) {
    Log.d(TAG, "edias_render_empty: " + (track != null));
    if (track == null) {
      renderer.setVisibility(View.VISIBLE);
    } else {
      renderer.setVisibility(View.GONE);
    }
  }
}
