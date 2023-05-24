package android.media;

import android.graphics.Bitmap;
import android.graphics.PointF;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/FaceDetector.class */
public class FaceDetector {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/media/FaceDetector$Face.class */
    public class Face {
        public static final float CONFIDENCE_THRESHOLD = 0.4f;
        public static final int EULER_X = 0;
        public static final int EULER_Y = 1;
        public static final int EULER_Z = 2;

        Face() {
            throw new RuntimeException("Stub!");
        }

        public float confidence() {
            throw new RuntimeException("Stub!");
        }

        public void getMidPoint(PointF point) {
            throw new RuntimeException("Stub!");
        }

        public float eyesDistance() {
            throw new RuntimeException("Stub!");
        }

        public float pose(int euler) {
            throw new RuntimeException("Stub!");
        }
    }

    public FaceDetector(int width, int height, int maxFaces) {
        throw new RuntimeException("Stub!");
    }

    public int findFaces(Bitmap bitmap, Face[] faces) {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
}
