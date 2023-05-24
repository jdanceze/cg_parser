package android.renderscript;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/ProgramStore.class */
public class ProgramStore extends BaseObj {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/ProgramStore$BlendDstFunc.class */
    public enum BlendDstFunc {
        DST_ALPHA,
        ONE,
        ONE_MINUS_DST_ALPHA,
        ONE_MINUS_SRC_ALPHA,
        ONE_MINUS_SRC_COLOR,
        SRC_ALPHA,
        SRC_COLOR,
        ZERO
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/ProgramStore$BlendSrcFunc.class */
    public enum BlendSrcFunc {
        DST_ALPHA,
        DST_COLOR,
        ONE,
        ONE_MINUS_DST_ALPHA,
        ONE_MINUS_DST_COLOR,
        ONE_MINUS_SRC_ALPHA,
        SRC_ALPHA,
        SRC_ALPHA_SATURATE,
        ZERO
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/ProgramStore$DepthFunc.class */
    public enum DepthFunc {
        ALWAYS,
        EQUAL,
        GREATER,
        GREATER_OR_EQUAL,
        LESS,
        LESS_OR_EQUAL,
        NOT_EQUAL
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/ProgramStore$Builder.class */
    public static class Builder {
        public Builder(RenderScript rs) {
            throw new RuntimeException("Stub!");
        }

        public Builder setDepthFunc(DepthFunc func) {
            throw new RuntimeException("Stub!");
        }

        public Builder setDepthMaskEnabled(boolean enable) {
            throw new RuntimeException("Stub!");
        }

        public Builder setColorMaskEnabled(boolean r, boolean g, boolean b, boolean a) {
            throw new RuntimeException("Stub!");
        }

        public Builder setBlendFunc(BlendSrcFunc src, BlendDstFunc dst) {
            throw new RuntimeException("Stub!");
        }

        public Builder setDitherEnabled(boolean enable) {
            throw new RuntimeException("Stub!");
        }

        public ProgramStore create() {
            throw new RuntimeException("Stub!");
        }
    }

    ProgramStore() {
        throw new RuntimeException("Stub!");
    }

    public DepthFunc getDepthFunc() {
        throw new RuntimeException("Stub!");
    }

    public boolean isDepthMaskEnabled() {
        throw new RuntimeException("Stub!");
    }

    public boolean isColorMaskRedEnabled() {
        throw new RuntimeException("Stub!");
    }

    public boolean isColorMaskGreenEnabled() {
        throw new RuntimeException("Stub!");
    }

    public boolean isColorMaskBlueEnabled() {
        throw new RuntimeException("Stub!");
    }

    public boolean isColorMaskAlphaEnabled() {
        throw new RuntimeException("Stub!");
    }

    public BlendSrcFunc getBlendSrcFunc() {
        throw new RuntimeException("Stub!");
    }

    public BlendDstFunc getBlendDstFunc() {
        throw new RuntimeException("Stub!");
    }

    public boolean isDitherEnabled() {
        throw new RuntimeException("Stub!");
    }

    public static ProgramStore BLEND_NONE_DEPTH_TEST(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static ProgramStore BLEND_NONE_DEPTH_NONE(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static ProgramStore BLEND_ALPHA_DEPTH_TEST(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static ProgramStore BLEND_ALPHA_DEPTH_NONE(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }
}
