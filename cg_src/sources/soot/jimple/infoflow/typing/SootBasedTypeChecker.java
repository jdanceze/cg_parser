package soot.jimple.infoflow.typing;

import soot.ArrayType;
import soot.FastHierarchy;
import soot.PrimType;
import soot.Scene;
import soot.Type;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/typing/SootBasedTypeChecker.class */
public class SootBasedTypeChecker implements ITypeChecker {
    @Override // soot.jimple.infoflow.typing.ITypeChecker
    public Type getMorePreciseType(Type tp1, Type tp2) {
        Type preciseType;
        FastHierarchy fastHierarchy = Scene.v().getOrMakeFastHierarchy();
        if (tp1 == null) {
            return tp2;
        }
        if (tp2 == null) {
            return tp1;
        }
        if (tp1 == tp2) {
            return tp1;
        }
        if (TypeUtils.isObjectLikeType(tp1)) {
            return tp2;
        }
        if (TypeUtils.isObjectLikeType(tp2)) {
            return tp1;
        }
        if ((tp1 instanceof PrimType) && (tp2 instanceof PrimType)) {
            return tp1;
        }
        if (fastHierarchy.canStoreType(tp2, tp1)) {
            return tp2;
        }
        if (fastHierarchy.canStoreType(tp1, tp2)) {
            return tp1;
        }
        if ((tp1 instanceof ArrayType) && (tp2 instanceof ArrayType)) {
            ArrayType at1 = (ArrayType) tp1;
            ArrayType at2 = (ArrayType) tp2;
            if (at1.numDimensions != at2.numDimensions || (preciseType = getMorePreciseType(at1.getElementType(), at2.getElementType())) == null) {
                return null;
            }
            return ArrayType.v(preciseType, at1.numDimensions);
        } else if (tp1 instanceof ArrayType) {
            ArrayType at = (ArrayType) tp1;
            return getMorePreciseType(at.getElementType(), tp2);
        } else if (tp2 instanceof ArrayType) {
            ArrayType at3 = (ArrayType) tp2;
            return getMorePreciseType(tp1, at3.getElementType());
        } else {
            return null;
        }
    }
}
