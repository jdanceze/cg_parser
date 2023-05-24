package android.renderscript;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/ProgramRaster.class */
public class ProgramRaster extends BaseObj {

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/ProgramRaster$CullMode.class */
    public enum CullMode {
        BACK,
        FRONT,
        NONE
    }

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/ProgramRaster$Builder.class */
    public static class Builder {
        @Deprecated
        public Builder(RenderScript rs) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Builder setPointSpriteEnabled(boolean enable) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Builder setCullMode(CullMode m) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public ProgramRaster create() {
            throw new RuntimeException("Stub!");
        }
    }

    ProgramRaster() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public boolean isPointSpriteEnabled() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public CullMode getCullMode() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static ProgramRaster CULL_BACK(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static ProgramRaster CULL_FRONT(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static ProgramRaster CULL_NONE(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }
}
