package soot.jimple.toolkits.typing.fast;

import java.util.Collection;
import java.util.Collections;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.IntType;
import soot.IntegerType;
import soot.ShortType;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/AugHierarchy.class */
public class AugHierarchy implements IHierarchy {
    public static Collection<Type> lcas_(Type a, Type b, boolean useWeakObjectType) {
        if (TypeResolver.typesEqual(a, b)) {
            return Collections.singletonList(a);
        }
        if (a instanceof BottomType) {
            return Collections.singletonList(b);
        }
        if (b instanceof BottomType) {
            return Collections.singletonList(a);
        }
        if (a instanceof WeakObjectType) {
            return Collections.singletonList(b);
        }
        if (b instanceof WeakObjectType) {
            return Collections.singletonList(a);
        }
        if ((a instanceof IntegerType) && (b instanceof IntegerType)) {
            if (a instanceof Integer1Type) {
                return Collections.singletonList(b);
            }
            if (b instanceof Integer1Type) {
                return Collections.singletonList(a);
            }
            if ((a instanceof BooleanType) || (b instanceof BooleanType)) {
                return Collections.emptyList();
            }
            if (((a instanceof ByteType) && (b instanceof Integer32767Type)) || ((b instanceof ByteType) && (a instanceof Integer32767Type))) {
                return Collections.singletonList(ShortType.v());
            }
            if (((a instanceof CharType) && ((b instanceof ShortType) || (b instanceof ByteType))) || ((b instanceof CharType) && ((a instanceof ShortType) || (a instanceof ByteType)))) {
                return Collections.singletonList(IntType.v());
            }
            if (ancestor_(a, b)) {
                return Collections.singletonList(a);
            }
            return Collections.singletonList(b);
        } else if ((a instanceof IntegerType) || (b instanceof IntegerType)) {
            return Collections.emptyList();
        } else {
            return BytecodeHierarchy.lcas_(a, b, useWeakObjectType);
        }
    }

    public static boolean ancestor_(Type ancestor, Type child) {
        if (TypeResolver.typesEqual(ancestor, child)) {
            return true;
        }
        if ((ancestor instanceof ArrayType) && (child instanceof ArrayType)) {
            Type at = ((ArrayType) ancestor).getElementType();
            Type ct = ((ArrayType) child).getElementType();
            if (at instanceof Integer1Type) {
                return ct instanceof BottomType;
            }
            if (at instanceof BooleanType) {
                return (ct instanceof BottomType) || (ct instanceof Integer1Type);
            } else if (at instanceof Integer127Type) {
                return (ct instanceof BottomType) || (ct instanceof Integer1Type);
            } else if ((at instanceof ByteType) || (at instanceof Integer32767Type)) {
                return (ct instanceof BottomType) || (ct instanceof Integer1Type) || (ct instanceof Integer127Type);
            } else if (at instanceof CharType) {
                return (ct instanceof BottomType) || (ct instanceof Integer1Type) || (ct instanceof Integer127Type) || (ct instanceof Integer32767Type);
            } else if (ancestor instanceof ShortType) {
                return (ct instanceof BottomType) || (ct instanceof Integer1Type) || (ct instanceof Integer127Type) || (ct instanceof Integer32767Type);
            } else if (at instanceof IntType) {
                return (ct instanceof BottomType) || (ct instanceof Integer1Type) || (ct instanceof Integer127Type) || (ct instanceof Integer32767Type);
            } else if (ct instanceof IntegerType) {
                return false;
            } else {
                return BytecodeHierarchy.ancestor_(ancestor, child);
            }
        } else if ((ancestor instanceof IntegerType) && (child instanceof IntegerType)) {
            return IntUtils.getMaxValue((IntegerType) ancestor) >= IntUtils.getMaxValue((IntegerType) child);
        } else if (ancestor instanceof Integer1Type) {
            return child instanceof BottomType;
        } else {
            if (ancestor instanceof BooleanType) {
                return (child instanceof BottomType) || (child instanceof Integer1Type);
            } else if (ancestor instanceof Integer127Type) {
                return (child instanceof BottomType) || (child instanceof Integer1Type);
            } else if ((ancestor instanceof ByteType) || (ancestor instanceof Integer32767Type)) {
                return (child instanceof BottomType) || (child instanceof Integer1Type) || (child instanceof Integer127Type);
            } else if (ancestor instanceof CharType) {
                return (child instanceof BottomType) || (child instanceof Integer1Type) || (child instanceof Integer127Type) || (child instanceof Integer32767Type);
            } else if (ancestor instanceof ShortType) {
                return (child instanceof BottomType) || (child instanceof Integer1Type) || (child instanceof Integer127Type) || (child instanceof Integer32767Type) || (child instanceof ByteType);
            } else if (ancestor instanceof IntType) {
                return (child instanceof BottomType) || (child instanceof Integer1Type) || (child instanceof Integer127Type) || (child instanceof Integer32767Type) || (child instanceof ByteType) || (child instanceof CharType) || (child instanceof ShortType);
            } else if (child instanceof IntegerType) {
                return false;
            } else {
                return BytecodeHierarchy.ancestor_(ancestor, child);
            }
        }
    }

    @Override // soot.jimple.toolkits.typing.fast.IHierarchy
    public Collection<Type> lcas(Type a, Type b, boolean useWeakObjectType) {
        return lcas_(a, b, useWeakObjectType);
    }

    @Override // soot.jimple.toolkits.typing.fast.IHierarchy
    public boolean ancestor(Type ancestor, Type child) {
        return ancestor_(ancestor, child);
    }
}
