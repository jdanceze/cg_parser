package soot;
/* loaded from: gencallgraphv3.jar:soot/PrimType.class */
public abstract class PrimType extends Type {
    public abstract Class<?> getJavaBoxedType();

    public abstract Class<?> getJavaPrimitiveType();

    public abstract String getTypeAsString();

    public RefType boxedType() {
        return RefType.v(getTypeAsString());
    }

    @Override // soot.Type
    public boolean isAllowedInFinalCode() {
        return true;
    }
}
