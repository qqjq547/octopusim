package framework.telegram.ui.cameraview.options;


import androidx.annotation.Nullable;

import framework.telegram.ui.cameraview.CameraView;

/**
 * Constants for selecting the encoder of video recordings.
 * https://developer.android.com/guide/topics/media/media-formats.html#video-formats
 *
 * @see CameraView#setVideoCodec(VideoCodec)
 */
public enum VideoCodec implements Control {


    /**
     * Let the device choose its codec.
     */
    DEVICE_DEFAULT(0),

    /**
     * The H.263 codec.
     */
    H_263(1),

    /**
     * The H.264 codec.
     */
    H_264(2);

    public static final VideoCodec DEFAULT = DEVICE_DEFAULT;

    private int value;

    VideoCodec(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    @Nullable
    public static VideoCodec fromValue(int value) {
        VideoCodec[] list = VideoCodec.values();
        for (VideoCodec action : list) {
            if (action.value() == value) {
                return action;
            }
        }
        return null;
    }
}
