package android.renderscript;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/ProgramFragmentFixedFunction.class */
public class ProgramFragmentFixedFunction extends ProgramFragment {

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/ProgramFragmentFixedFunction$Builder.class */
    public static class Builder {
        @Deprecated
        public static final int MAX_TEXTURE = 2;

        @Deprecated
        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/ProgramFragmentFixedFunction$Builder$EnvMode.class */
        public enum EnvMode {
            DECAL,
            MODULATE,
            REPLACE
        }

        @Deprecated
        /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/ProgramFragmentFixedFunction$Builder$Format.class */
        public enum Format {
            ALPHA,
            LUMINANCE_ALPHA,
            RGB,
            RGBA
        }

        @Deprecated
        public Builder(RenderScript rs) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Builder setTexture(EnvMode env, Format fmt, int slot) throws IllegalArgumentException {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Builder setPointSpriteTexCoordinateReplacement(boolean enable) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Builder setVaryingColor(boolean enable) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public ProgramFragmentFixedFunction create() {
            throw new RuntimeException("Stub!");
        }
    }

    ProgramFragmentFixedFunction() {
        throw new RuntimeException("Stub!");
    }
}
