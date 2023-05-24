package android.renderscript;

import android.content.res.Resources;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Program.class */
public class Program extends BaseObj {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Program$TextureType.class */
    public enum TextureType {
        TEXTURE_2D,
        TEXTURE_CUBE
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Program$BaseProgramBuilder.class */
    public static class BaseProgramBuilder {
        /* JADX INFO: Access modifiers changed from: protected */
        public BaseProgramBuilder(RenderScript rs) {
            throw new RuntimeException("Stub!");
        }

        public BaseProgramBuilder setShader(String s) {
            throw new RuntimeException("Stub!");
        }

        public BaseProgramBuilder setShader(Resources resources, int resourceID) {
            throw new RuntimeException("Stub!");
        }

        public int getCurrentConstantIndex() {
            throw new RuntimeException("Stub!");
        }

        public int getCurrentTextureIndex() {
            throw new RuntimeException("Stub!");
        }

        public BaseProgramBuilder addConstant(Type t) throws IllegalStateException {
            throw new RuntimeException("Stub!");
        }

        public BaseProgramBuilder addTexture(TextureType texType) throws IllegalArgumentException {
            throw new RuntimeException("Stub!");
        }

        public BaseProgramBuilder addTexture(TextureType texType, String texName) throws IllegalArgumentException {
            throw new RuntimeException("Stub!");
        }

        protected void initProgram(Program p) {
            throw new RuntimeException("Stub!");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Program() {
        throw new RuntimeException("Stub!");
    }

    public int getConstantCount() {
        throw new RuntimeException("Stub!");
    }

    public Type getConstant(int slot) {
        throw new RuntimeException("Stub!");
    }

    public int getTextureCount() {
        throw new RuntimeException("Stub!");
    }

    public TextureType getTextureType(int slot) {
        throw new RuntimeException("Stub!");
    }

    public String getTextureName(int slot) {
        throw new RuntimeException("Stub!");
    }

    public void bindConstants(Allocation a, int slot) {
        throw new RuntimeException("Stub!");
    }

    public void bindTexture(Allocation va, int slot) throws IllegalArgumentException {
        throw new RuntimeException("Stub!");
    }

    public void bindSampler(Sampler vs, int slot) throws IllegalArgumentException {
        throw new RuntimeException("Stub!");
    }
}
