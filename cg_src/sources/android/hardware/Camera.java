package android.hardware;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.view.SurfaceHolder;
import java.io.IOException;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera.class */
public class Camera {
    public static final String ACTION_NEW_PICTURE = "android.hardware.action.NEW_PICTURE";
    public static final String ACTION_NEW_VIDEO = "android.hardware.action.NEW_VIDEO";
    public static final int CAMERA_ERROR_UNKNOWN = 1;
    public static final int CAMERA_ERROR_SERVER_DIED = 100;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera$AutoFocusCallback.class */
    public interface AutoFocusCallback {
        void onAutoFocus(boolean z, Camera camera);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera$AutoFocusMoveCallback.class */
    public interface AutoFocusMoveCallback {
        void onAutoFocusMoving(boolean z, Camera camera);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera$ErrorCallback.class */
    public interface ErrorCallback {
        void onError(int i, Camera camera);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera$FaceDetectionListener.class */
    public interface FaceDetectionListener {
        void onFaceDetection(Face[] faceArr, Camera camera);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera$OnZoomChangeListener.class */
    public interface OnZoomChangeListener {
        void onZoomChange(int i, boolean z, Camera camera);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera$PictureCallback.class */
    public interface PictureCallback {
        void onPictureTaken(byte[] bArr, Camera camera);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera$PreviewCallback.class */
    public interface PreviewCallback {
        void onPreviewFrame(byte[] bArr, Camera camera);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera$ShutterCallback.class */
    public interface ShutterCallback {
        void onShutter();
    }

    public static native int getNumberOfCameras();

    public static native void getCameraInfo(int i, CameraInfo cameraInfo);

    public final native void unlock();

    public final native void lock();

    public final native void reconnect() throws IOException;

    public final native void setPreviewTexture(SurfaceTexture surfaceTexture) throws IOException;

    public final native void startPreview();

    public final native void startSmoothZoom(int i);

    public final native void stopSmoothZoom();

    public final native void setDisplayOrientation(int i);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera$CameraInfo.class */
    public static class CameraInfo {
        public static final int CAMERA_FACING_BACK = 0;
        public static final int CAMERA_FACING_FRONT = 1;
        public int facing;
        public int orientation;

        public CameraInfo() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera$Face.class */
    public static class Face {
        public Rect rect;
        public int score;
        public int id;
        public Point leftEye;
        public Point rightEye;
        public Point mouth;

        public Face() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera$Size.class */
    public class Size {
        public int width;
        public int height;

        public Size(int w, int h) {
            throw new RuntimeException("Stub!");
        }

        public boolean equals(Object obj) {
            throw new RuntimeException("Stub!");
        }

        public int hashCode() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera$Area.class */
    public static class Area {
        public Rect rect;
        public int weight;

        public Area(Rect rect, int weight) {
            throw new RuntimeException("Stub!");
        }

        public boolean equals(Object obj) {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/Camera$Parameters.class */
    public class Parameters {
        public static final String WHITE_BALANCE_AUTO = "auto";
        public static final String WHITE_BALANCE_INCANDESCENT = "incandescent";
        public static final String WHITE_BALANCE_FLUORESCENT = "fluorescent";
        public static final String WHITE_BALANCE_WARM_FLUORESCENT = "warm-fluorescent";
        public static final String WHITE_BALANCE_DAYLIGHT = "daylight";
        public static final String WHITE_BALANCE_CLOUDY_DAYLIGHT = "cloudy-daylight";
        public static final String WHITE_BALANCE_TWILIGHT = "twilight";
        public static final String WHITE_BALANCE_SHADE = "shade";
        public static final String EFFECT_NONE = "none";
        public static final String EFFECT_MONO = "mono";
        public static final String EFFECT_NEGATIVE = "negative";
        public static final String EFFECT_SOLARIZE = "solarize";
        public static final String EFFECT_SEPIA = "sepia";
        public static final String EFFECT_POSTERIZE = "posterize";
        public static final String EFFECT_WHITEBOARD = "whiteboard";
        public static final String EFFECT_BLACKBOARD = "blackboard";
        public static final String EFFECT_AQUA = "aqua";
        public static final String ANTIBANDING_AUTO = "auto";
        public static final String ANTIBANDING_50HZ = "50hz";
        public static final String ANTIBANDING_60HZ = "60hz";
        public static final String ANTIBANDING_OFF = "off";
        public static final String FLASH_MODE_OFF = "off";
        public static final String FLASH_MODE_AUTO = "auto";
        public static final String FLASH_MODE_ON = "on";
        public static final String FLASH_MODE_RED_EYE = "red-eye";
        public static final String FLASH_MODE_TORCH = "torch";
        public static final String SCENE_MODE_AUTO = "auto";
        public static final String SCENE_MODE_ACTION = "action";
        public static final String SCENE_MODE_PORTRAIT = "portrait";
        public static final String SCENE_MODE_LANDSCAPE = "landscape";
        public static final String SCENE_MODE_NIGHT = "night";
        public static final String SCENE_MODE_NIGHT_PORTRAIT = "night-portrait";
        public static final String SCENE_MODE_THEATRE = "theatre";
        public static final String SCENE_MODE_BEACH = "beach";
        public static final String SCENE_MODE_SNOW = "snow";
        public static final String SCENE_MODE_SUNSET = "sunset";
        public static final String SCENE_MODE_STEADYPHOTO = "steadyphoto";
        public static final String SCENE_MODE_FIREWORKS = "fireworks";
        public static final String SCENE_MODE_SPORTS = "sports";
        public static final String SCENE_MODE_PARTY = "party";
        public static final String SCENE_MODE_CANDLELIGHT = "candlelight";
        public static final String SCENE_MODE_BARCODE = "barcode";
        public static final String FOCUS_MODE_AUTO = "auto";
        public static final String FOCUS_MODE_INFINITY = "infinity";
        public static final String FOCUS_MODE_MACRO = "macro";
        public static final String FOCUS_MODE_FIXED = "fixed";
        public static final String FOCUS_MODE_EDOF = "edof";
        public static final String FOCUS_MODE_CONTINUOUS_VIDEO = "continuous-video";
        public static final String FOCUS_MODE_CONTINUOUS_PICTURE = "continuous-picture";
        public static final int FOCUS_DISTANCE_NEAR_INDEX = 0;
        public static final int FOCUS_DISTANCE_OPTIMAL_INDEX = 1;
        public static final int FOCUS_DISTANCE_FAR_INDEX = 2;
        public static final int PREVIEW_FPS_MIN_INDEX = 0;
        public static final int PREVIEW_FPS_MAX_INDEX = 1;

        Parameters() {
            throw new RuntimeException("Stub!");
        }

        public String flatten() {
            throw new RuntimeException("Stub!");
        }

        public void unflatten(String flattened) {
            throw new RuntimeException("Stub!");
        }

        public void remove(String key) {
            throw new RuntimeException("Stub!");
        }

        public void set(String key, String value) {
            throw new RuntimeException("Stub!");
        }

        public void set(String key, int value) {
            throw new RuntimeException("Stub!");
        }

        public String get(String key) {
            throw new RuntimeException("Stub!");
        }

        public int getInt(String key) {
            throw new RuntimeException("Stub!");
        }

        public void setPreviewSize(int width, int height) {
            throw new RuntimeException("Stub!");
        }

        public Size getPreviewSize() {
            throw new RuntimeException("Stub!");
        }

        public List<Size> getSupportedPreviewSizes() {
            throw new RuntimeException("Stub!");
        }

        public List<Size> getSupportedVideoSizes() {
            throw new RuntimeException("Stub!");
        }

        public Size getPreferredPreviewSizeForVideo() {
            throw new RuntimeException("Stub!");
        }

        public void setJpegThumbnailSize(int width, int height) {
            throw new RuntimeException("Stub!");
        }

        public Size getJpegThumbnailSize() {
            throw new RuntimeException("Stub!");
        }

        public List<Size> getSupportedJpegThumbnailSizes() {
            throw new RuntimeException("Stub!");
        }

        public void setJpegThumbnailQuality(int quality) {
            throw new RuntimeException("Stub!");
        }

        public int getJpegThumbnailQuality() {
            throw new RuntimeException("Stub!");
        }

        public void setJpegQuality(int quality) {
            throw new RuntimeException("Stub!");
        }

        public int getJpegQuality() {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public void setPreviewFrameRate(int fps) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public int getPreviewFrameRate() {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public List<Integer> getSupportedPreviewFrameRates() {
            throw new RuntimeException("Stub!");
        }

        public void setPreviewFpsRange(int min, int max) {
            throw new RuntimeException("Stub!");
        }

        public void getPreviewFpsRange(int[] range) {
            throw new RuntimeException("Stub!");
        }

        public List<int[]> getSupportedPreviewFpsRange() {
            throw new RuntimeException("Stub!");
        }

        public void setPreviewFormat(int pixel_format) {
            throw new RuntimeException("Stub!");
        }

        public int getPreviewFormat() {
            throw new RuntimeException("Stub!");
        }

        public List<Integer> getSupportedPreviewFormats() {
            throw new RuntimeException("Stub!");
        }

        public void setPictureSize(int width, int height) {
            throw new RuntimeException("Stub!");
        }

        public Size getPictureSize() {
            throw new RuntimeException("Stub!");
        }

        public List<Size> getSupportedPictureSizes() {
            throw new RuntimeException("Stub!");
        }

        public void setPictureFormat(int pixel_format) {
            throw new RuntimeException("Stub!");
        }

        public int getPictureFormat() {
            throw new RuntimeException("Stub!");
        }

        public List<Integer> getSupportedPictureFormats() {
            throw new RuntimeException("Stub!");
        }

        public void setRotation(int rotation) {
            throw new RuntimeException("Stub!");
        }

        public void setGpsLatitude(double latitude) {
            throw new RuntimeException("Stub!");
        }

        public void setGpsLongitude(double longitude) {
            throw new RuntimeException("Stub!");
        }

        public void setGpsAltitude(double altitude) {
            throw new RuntimeException("Stub!");
        }

        public void setGpsTimestamp(long timestamp) {
            throw new RuntimeException("Stub!");
        }

        public void setGpsProcessingMethod(String processing_method) {
            throw new RuntimeException("Stub!");
        }

        public void removeGpsData() {
            throw new RuntimeException("Stub!");
        }

        public String getWhiteBalance() {
            throw new RuntimeException("Stub!");
        }

        public void setWhiteBalance(String value) {
            throw new RuntimeException("Stub!");
        }

        public List<String> getSupportedWhiteBalance() {
            throw new RuntimeException("Stub!");
        }

        public String getColorEffect() {
            throw new RuntimeException("Stub!");
        }

        public void setColorEffect(String value) {
            throw new RuntimeException("Stub!");
        }

        public List<String> getSupportedColorEffects() {
            throw new RuntimeException("Stub!");
        }

        public String getAntibanding() {
            throw new RuntimeException("Stub!");
        }

        public void setAntibanding(String antibanding) {
            throw new RuntimeException("Stub!");
        }

        public List<String> getSupportedAntibanding() {
            throw new RuntimeException("Stub!");
        }

        public String getSceneMode() {
            throw new RuntimeException("Stub!");
        }

        public void setSceneMode(String value) {
            throw new RuntimeException("Stub!");
        }

        public List<String> getSupportedSceneModes() {
            throw new RuntimeException("Stub!");
        }

        public String getFlashMode() {
            throw new RuntimeException("Stub!");
        }

        public void setFlashMode(String value) {
            throw new RuntimeException("Stub!");
        }

        public List<String> getSupportedFlashModes() {
            throw new RuntimeException("Stub!");
        }

        public String getFocusMode() {
            throw new RuntimeException("Stub!");
        }

        public void setFocusMode(String value) {
            throw new RuntimeException("Stub!");
        }

        public List<String> getSupportedFocusModes() {
            throw new RuntimeException("Stub!");
        }

        public float getFocalLength() {
            throw new RuntimeException("Stub!");
        }

        public float getHorizontalViewAngle() {
            throw new RuntimeException("Stub!");
        }

        public float getVerticalViewAngle() {
            throw new RuntimeException("Stub!");
        }

        public int getExposureCompensation() {
            throw new RuntimeException("Stub!");
        }

        public void setExposureCompensation(int value) {
            throw new RuntimeException("Stub!");
        }

        public int getMaxExposureCompensation() {
            throw new RuntimeException("Stub!");
        }

        public int getMinExposureCompensation() {
            throw new RuntimeException("Stub!");
        }

        public float getExposureCompensationStep() {
            throw new RuntimeException("Stub!");
        }

        public void setAutoExposureLock(boolean toggle) {
            throw new RuntimeException("Stub!");
        }

        public boolean getAutoExposureLock() {
            throw new RuntimeException("Stub!");
        }

        public boolean isAutoExposureLockSupported() {
            throw new RuntimeException("Stub!");
        }

        public void setAutoWhiteBalanceLock(boolean toggle) {
            throw new RuntimeException("Stub!");
        }

        public boolean getAutoWhiteBalanceLock() {
            throw new RuntimeException("Stub!");
        }

        public boolean isAutoWhiteBalanceLockSupported() {
            throw new RuntimeException("Stub!");
        }

        public int getZoom() {
            throw new RuntimeException("Stub!");
        }

        public void setZoom(int value) {
            throw new RuntimeException("Stub!");
        }

        public boolean isZoomSupported() {
            throw new RuntimeException("Stub!");
        }

        public int getMaxZoom() {
            throw new RuntimeException("Stub!");
        }

        public List<Integer> getZoomRatios() {
            throw new RuntimeException("Stub!");
        }

        public boolean isSmoothZoomSupported() {
            throw new RuntimeException("Stub!");
        }

        public void getFocusDistances(float[] output) {
            throw new RuntimeException("Stub!");
        }

        public int getMaxNumFocusAreas() {
            throw new RuntimeException("Stub!");
        }

        public List<Area> getFocusAreas() {
            throw new RuntimeException("Stub!");
        }

        public void setFocusAreas(List<Area> focusAreas) {
            throw new RuntimeException("Stub!");
        }

        public int getMaxNumMeteringAreas() {
            throw new RuntimeException("Stub!");
        }

        public List<Area> getMeteringAreas() {
            throw new RuntimeException("Stub!");
        }

        public void setMeteringAreas(List<Area> meteringAreas) {
            throw new RuntimeException("Stub!");
        }

        public int getMaxNumDetectedFaces() {
            throw new RuntimeException("Stub!");
        }

        public void setRecordingHint(boolean hint) {
            throw new RuntimeException("Stub!");
        }

        public boolean isVideoSnapshotSupported() {
            throw new RuntimeException("Stub!");
        }

        public void setVideoStabilization(boolean toggle) {
            throw new RuntimeException("Stub!");
        }

        public boolean getVideoStabilization() {
            throw new RuntimeException("Stub!");
        }

        public boolean isVideoStabilizationSupported() {
            throw new RuntimeException("Stub!");
        }
    }

    Camera() {
        throw new RuntimeException("Stub!");
    }

    public static Camera open(int cameraId) {
        throw new RuntimeException("Stub!");
    }

    public static Camera open() {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() {
        throw new RuntimeException("Stub!");
    }

    public final void release() {
        throw new RuntimeException("Stub!");
    }

    public final void setPreviewDisplay(SurfaceHolder holder) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public final void stopPreview() {
        throw new RuntimeException("Stub!");
    }

    public final void setPreviewCallback(PreviewCallback cb) {
        throw new RuntimeException("Stub!");
    }

    public final void setOneShotPreviewCallback(PreviewCallback cb) {
        throw new RuntimeException("Stub!");
    }

    public final void setPreviewCallbackWithBuffer(PreviewCallback cb) {
        throw new RuntimeException("Stub!");
    }

    public final void addCallbackBuffer(byte[] callbackBuffer) {
        throw new RuntimeException("Stub!");
    }

    public final void autoFocus(AutoFocusCallback cb) {
        throw new RuntimeException("Stub!");
    }

    public final void cancelAutoFocus() {
        throw new RuntimeException("Stub!");
    }

    public void setAutoFocusMoveCallback(AutoFocusMoveCallback cb) {
        throw new RuntimeException("Stub!");
    }

    public final void takePicture(ShutterCallback shutter, PictureCallback raw, PictureCallback jpeg) {
        throw new RuntimeException("Stub!");
    }

    public final void takePicture(ShutterCallback shutter, PictureCallback raw, PictureCallback postview, PictureCallback jpeg) {
        throw new RuntimeException("Stub!");
    }

    public final void setZoomChangeListener(OnZoomChangeListener listener) {
        throw new RuntimeException("Stub!");
    }

    public final void setFaceDetectionListener(FaceDetectionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public final void startFaceDetection() {
        throw new RuntimeException("Stub!");
    }

    public final void stopFaceDetection() {
        throw new RuntimeException("Stub!");
    }

    public final void setErrorCallback(ErrorCallback cb) {
        throw new RuntimeException("Stub!");
    }

    public void setParameters(Parameters params) {
        throw new RuntimeException("Stub!");
    }

    public Parameters getParameters() {
        throw new RuntimeException("Stub!");
    }
}
