package android.renderscript;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Sampler.class */
public class Sampler extends BaseObj {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Sampler$Value.class */
    public enum Value {
        CLAMP,
        LINEAR,
        LINEAR_MIP_LINEAR,
        LINEAR_MIP_NEAREST,
        NEAREST,
        WRAP
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Sampler$Builder.class */
    public static class Builder {
        public Builder(RenderScript rs) {
            throw new RuntimeException("Stub!");
        }

        public void setMinification(Value v) {
            throw new RuntimeException("Stub!");
        }

        public void setMagnification(Value v) {
            throw new RuntimeException("Stub!");
        }

        public void setWrapS(Value v) {
            throw new RuntimeException("Stub!");
        }

        public void setWrapT(Value v) {
            throw new RuntimeException("Stub!");
        }

        public void setAnisotropy(float v) {
            throw new RuntimeException("Stub!");
        }

        public Sampler create() {
            throw new RuntimeException("Stub!");
        }
    }

    Sampler() {
        throw new RuntimeException("Stub!");
    }

    public Value getMinification() {
        throw new RuntimeException("Stub!");
    }

    public Value getMagnification() {
        throw new RuntimeException("Stub!");
    }

    public Value getWrapS() {
        throw new RuntimeException("Stub!");
    }

    public Value getWrapT() {
        throw new RuntimeException("Stub!");
    }

    public float getAnisotropy() {
        throw new RuntimeException("Stub!");
    }

    public static Sampler CLAMP_NEAREST(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Sampler CLAMP_LINEAR(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Sampler CLAMP_LINEAR_MIP_LINEAR(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Sampler WRAP_NEAREST(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Sampler WRAP_LINEAR(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Sampler WRAP_LINEAR_MIP_LINEAR(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }
}
