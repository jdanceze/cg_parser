package android.renderscript;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Script.class */
public class Script extends BaseObj {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Script$Builder.class */
    public static class Builder {
        Builder() {
            throw new RuntimeException("Stub!");
        }
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Script$FieldBase.class */
    public static class FieldBase {
        protected Element mElement;
        protected Allocation mAllocation;

        protected FieldBase() {
            throw new RuntimeException("Stub!");
        }

        protected void init(RenderScript rs, int dimx) {
            throw new RuntimeException("Stub!");
        }

        protected void init(RenderScript rs, int dimx, int usages) {
            throw new RuntimeException("Stub!");
        }

        public Element getElement() {
            throw new RuntimeException("Stub!");
        }

        public Type getType() {
            throw new RuntimeException("Stub!");
        }

        public Allocation getAllocation() {
            throw new RuntimeException("Stub!");
        }

        public void updateAllocation() {
            throw new RuntimeException("Stub!");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Script() {
        throw new RuntimeException("Stub!");
    }

    protected void invoke(int slot) {
        throw new RuntimeException("Stub!");
    }

    protected void invoke(int slot, FieldPacker v) {
        throw new RuntimeException("Stub!");
    }

    protected void forEach(int slot, Allocation ain, Allocation aout, FieldPacker v) {
        throw new RuntimeException("Stub!");
    }

    public void bindAllocation(Allocation va, int slot) {
        throw new RuntimeException("Stub!");
    }

    public void setVar(int index, float v) {
        throw new RuntimeException("Stub!");
    }

    public void setVar(int index, double v) {
        throw new RuntimeException("Stub!");
    }

    public void setVar(int index, int v) {
        throw new RuntimeException("Stub!");
    }

    public void setVar(int index, long v) {
        throw new RuntimeException("Stub!");
    }

    public void setVar(int index, boolean v) {
        throw new RuntimeException("Stub!");
    }

    public void setVar(int index, BaseObj o) {
        throw new RuntimeException("Stub!");
    }

    public void setVar(int index, FieldPacker v) {
        throw new RuntimeException("Stub!");
    }

    public void setVar(int index, FieldPacker v, Element e, int[] dims) {
        throw new RuntimeException("Stub!");
    }

    public void setTimeZone(String timeZone) {
        throw new RuntimeException("Stub!");
    }
}
