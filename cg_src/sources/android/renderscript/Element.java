package android.renderscript;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Element.class */
public class Element extends BaseObj {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Element$DataKind.class */
    public enum DataKind {
        PIXEL_A,
        PIXEL_DEPTH,
        PIXEL_L,
        PIXEL_LA,
        PIXEL_RGB,
        PIXEL_RGBA,
        USER
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Element$DataType.class */
    public enum DataType {
        BOOLEAN,
        FLOAT_32,
        FLOAT_64,
        MATRIX_2X2,
        MATRIX_3X3,
        MATRIX_4X4,
        NONE,
        RS_ALLOCATION,
        RS_ELEMENT,
        RS_FONT,
        RS_MESH,
        RS_PROGRAM_FRAGMENT,
        RS_PROGRAM_RASTER,
        RS_PROGRAM_STORE,
        RS_PROGRAM_VERTEX,
        RS_SAMPLER,
        RS_SCRIPT,
        RS_TYPE,
        SIGNED_16,
        SIGNED_32,
        SIGNED_64,
        SIGNED_8,
        UNSIGNED_16,
        UNSIGNED_32,
        UNSIGNED_4_4_4_4,
        UNSIGNED_5_5_5_1,
        UNSIGNED_5_6_5,
        UNSIGNED_64,
        UNSIGNED_8
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Element$Builder.class */
    public static class Builder {
        public Builder(RenderScript rs) {
            throw new RuntimeException("Stub!");
        }

        public Builder add(Element element, String name, int arraySize) {
            throw new RuntimeException("Stub!");
        }

        public Builder add(Element element, String name) {
            throw new RuntimeException("Stub!");
        }

        public Element create() {
            throw new RuntimeException("Stub!");
        }
    }

    Element() {
        throw new RuntimeException("Stub!");
    }

    public int getBytesSize() {
        throw new RuntimeException("Stub!");
    }

    public int getVectorSize() {
        throw new RuntimeException("Stub!");
    }

    public boolean isComplex() {
        throw new RuntimeException("Stub!");
    }

    public int getSubElementCount() {
        throw new RuntimeException("Stub!");
    }

    public Element getSubElement(int index) {
        throw new RuntimeException("Stub!");
    }

    public String getSubElementName(int index) {
        throw new RuntimeException("Stub!");
    }

    public int getSubElementArraySize(int index) {
        throw new RuntimeException("Stub!");
    }

    public int getSubElementOffsetBytes(int index) {
        throw new RuntimeException("Stub!");
    }

    public DataType getDataType() {
        throw new RuntimeException("Stub!");
    }

    public DataKind getDataKind() {
        throw new RuntimeException("Stub!");
    }

    public static Element BOOLEAN(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U8(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I8(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U16(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I16(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U32(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I32(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U64(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I64(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element F32(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element F64(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element ELEMENT(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element TYPE(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element ALLOCATION(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element SAMPLER(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element SCRIPT(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element MESH(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element PROGRAM_FRAGMENT(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element PROGRAM_VERTEX(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element PROGRAM_RASTER(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element PROGRAM_STORE(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element FONT(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element A_8(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element RGB_565(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element RGB_888(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element RGBA_5551(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element RGBA_4444(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element RGBA_8888(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element F32_2(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element F32_3(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element F32_4(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element F64_2(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element F64_3(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element F64_4(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U8_2(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U8_3(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U8_4(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I8_2(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I8_3(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I8_4(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U16_2(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U16_3(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U16_4(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I16_2(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I16_3(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I16_4(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U32_2(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U32_3(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U32_4(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I32_2(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I32_3(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I32_4(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U64_2(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U64_3(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element U64_4(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I64_2(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I64_3(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element I64_4(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element MATRIX_4X4(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static Element MATRIX4X4(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element MATRIX_3X3(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element MATRIX_2X2(RenderScript rs) {
        throw new RuntimeException("Stub!");
    }

    public static Element createVector(RenderScript rs, DataType dt, int size) {
        throw new RuntimeException("Stub!");
    }

    public static Element createPixel(RenderScript rs, DataType dt, DataKind dk) {
        throw new RuntimeException("Stub!");
    }

    public boolean isCompatible(Element e) {
        throw new RuntimeException("Stub!");
    }
}
