package android.renderscript;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Mesh.class */
public class Mesh extends BaseObj {

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Mesh$Primitive.class */
    public enum Primitive {
        LINE,
        LINE_STRIP,
        POINT,
        TRIANGLE,
        TRIANGLE_FAN,
        TRIANGLE_STRIP
    }

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Mesh$Builder.class */
    public static class Builder {
        @Deprecated
        public Builder(RenderScript rs, int usage) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public int getCurrentVertexTypeIndex() {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public int getCurrentIndexSetIndex() {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Builder addVertexType(Type t) throws IllegalStateException {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Builder addVertexType(Element e, int size) throws IllegalStateException {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Builder addIndexSetType(Type t, Primitive p) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Builder addIndexSetType(Primitive p) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Builder addIndexSetType(Element e, int size, Primitive p) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Mesh create() {
            throw new RuntimeException("Stub!");
        }
    }

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Mesh$AllocationBuilder.class */
    public static class AllocationBuilder {
        @Deprecated
        public AllocationBuilder(RenderScript rs) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public int getCurrentVertexTypeIndex() {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public int getCurrentIndexSetIndex() {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public AllocationBuilder addVertexAllocation(Allocation a) throws IllegalStateException {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public AllocationBuilder addIndexSetAllocation(Allocation a, Primitive p) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public AllocationBuilder addIndexSetType(Primitive p) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Mesh create() {
            throw new RuntimeException("Stub!");
        }
    }

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Mesh$TriangleMeshBuilder.class */
    public static class TriangleMeshBuilder {
        @Deprecated
        public static final int COLOR = 1;
        @Deprecated
        public static final int NORMAL = 2;
        @Deprecated
        public static final int TEXTURE_0 = 256;

        @Deprecated
        public TriangleMeshBuilder(RenderScript rs, int vtxSize, int flags) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public TriangleMeshBuilder addVertex(float x, float y) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public TriangleMeshBuilder addVertex(float x, float y, float z) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public TriangleMeshBuilder setTexture(float s, float t) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public TriangleMeshBuilder setNormal(float x, float y, float z) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public TriangleMeshBuilder setColor(float r, float g, float b, float a) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public TriangleMeshBuilder addTriangle(int idx1, int idx2, int idx3) {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Mesh create(boolean uploadToBufferObject) {
            throw new RuntimeException("Stub!");
        }
    }

    Mesh() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getVertexAllocationCount() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public Allocation getVertexAllocation(int slot) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getPrimitiveCount() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public Allocation getIndexSetAllocation(int slot) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public Primitive getPrimitive(int slot) {
        throw new RuntimeException("Stub!");
    }
}
