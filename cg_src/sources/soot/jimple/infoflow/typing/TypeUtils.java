package soot.jimple.infoflow.typing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FastHierarchy;
import soot.FloatType;
import soot.Hierarchy;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.LongType;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.Type;
import soot.jimple.Jimple;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.AccessPath;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/typing/TypeUtils.class */
public class TypeUtils {
    private final InfoflowManager manager;
    private final List<ITypeChecker> typeCheckers = new ArrayList();

    public TypeUtils(InfoflowManager manager) {
        this.manager = manager;
        this.typeCheckers.add(new SootBasedTypeChecker());
    }

    public static boolean isStringType(Type tp) {
        if (!(tp instanceof RefType)) {
            return false;
        }
        RefType refType = (RefType) tp;
        return refType.getClassName().equals("java.lang.String");
    }

    public static boolean isObjectLikeType(Type tp) {
        if (!(tp instanceof RefType)) {
            return false;
        }
        RefType rt = (RefType) tp;
        String className = rt.getSootClass().getName();
        return className.equals(JavaBasicTypes.JAVA_LANG_OBJECT) || className.equals(JavaBasicTypes.JAVA_IO_SERIALIZABLE) || className.equals("java.lang.Cloneable");
    }

    public boolean checkCast(Type destType, Type sourceType) {
        if (!this.manager.getConfig().getEnableTypeChecking() || sourceType == null || sourceType == destType) {
            return true;
        }
        FastHierarchy hierarchy = this.manager.getHierarchy();
        if (hierarchy != null && (hierarchy.canStoreType(destType, sourceType) || this.manager.getHierarchy().canStoreType(sourceType, destType))) {
            return true;
        }
        if ((destType instanceof PrimType) && (sourceType instanceof PrimType)) {
            return true;
        }
        return false;
    }

    public boolean checkCast(AccessPath accessPath, Type type) {
        if (!this.manager.getConfig().getEnableTypeChecking()) {
            return true;
        }
        int fragmentCount = accessPath.getFragmentCount();
        int fieldStartIdx = 0;
        if (accessPath.isStaticFieldRef()) {
            if (!checkCast(type, accessPath.getFirstFieldType())) {
                return false;
            }
            if (isPrimitiveArray(type) && fragmentCount > 1) {
                return false;
            }
            fieldStartIdx = 1;
        } else if (!checkCast(type, accessPath.getBaseType())) {
            return false;
        } else {
            if (isPrimitiveArray(type) && !accessPath.isLocal()) {
                return false;
            }
        }
        if (accessPath.isFieldRef() && fragmentCount > fieldStartIdx && !checkCast(type, accessPath.getFragments()[fieldStartIdx].getField().getDeclaringClass().getType())) {
            return false;
        }
        return true;
    }

    public static boolean isPrimitiveArray(Type type) {
        if (type instanceof ArrayType) {
            ArrayType at = (ArrayType) type;
            if (at.getArrayElementType() instanceof PrimType) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean hasCompatibleTypesForCall(AccessPath apBase, SootClass dest) {
        if (!this.manager.getConfig().getEnableTypeChecking()) {
            return true;
        }
        if (apBase.getBaseType() instanceof PrimType) {
            return false;
        }
        if (apBase.getBaseType() instanceof ArrayType) {
            return dest.getName().equals(JavaBasicTypes.JAVA_LANG_OBJECT);
        }
        return checkCast(apBase, dest.getType());
    }

    public Type getMorePreciseType(Type tp1, Type tp2) {
        for (ITypeChecker checker : this.typeCheckers) {
            Type tp = checker.getMorePreciseType(tp1, tp2);
            if (tp != null) {
                return tp;
            }
        }
        return null;
    }

    public String getMorePreciseType(String tp1, String tp2) {
        Type newType = getMorePreciseType(getTypeFromString(tp1), getTypeFromString(tp2));
        if (newType == null) {
            return null;
        }
        return new StringBuilder().append(newType).toString();
    }

    public static Type getTypeFromString(String type) {
        Type t;
        if (type == null || type.isEmpty()) {
            return null;
        }
        int numDimensions = 0;
        while (type.endsWith("[]")) {
            numDimensions++;
            type = type.substring(0, type.length() - 2);
        }
        if (type.equals("int")) {
            t = IntType.v();
        } else if (type.equals("long")) {
            t = LongType.v();
        } else if (type.equals(Jimple.FLOAT)) {
            t = FloatType.v();
        } else if (type.equals("double")) {
            t = DoubleType.v();
        } else if (type.equals("boolean")) {
            t = BooleanType.v();
        } else if (type.equals("char")) {
            t = CharType.v();
        } else if (type.equals("short")) {
            t = ShortType.v();
        } else if (type.equals("byte")) {
            t = ByteType.v();
        } else if (Scene.v().containsClass(type)) {
            t = RefType.v(type);
        } else {
            return null;
        }
        if (numDimensions == 0) {
            return t;
        }
        return ArrayType.v(t, numDimensions);
    }

    public static Type buildArrayOrAddDimension(Type type, Type arrayType) {
        if (!(type instanceof ArrayType)) {
            return arrayType;
        }
        if (type instanceof ArrayType) {
            ArrayType array = (ArrayType) type;
            if (array.numDimensions >= 3) {
                return null;
            }
            return array.makeArrayType();
        }
        return ArrayType.v(type, 1);
    }

    public void registerTypeChecker(ITypeChecker checker) {
        this.typeCheckers.add(checker);
    }

    public static List<SootClass> getAllDerivedClasses(SootClass classOrInterface) {
        Hierarchy h = Scene.v().getActiveHierarchy();
        if (classOrInterface.isInterface()) {
            return (List) h.getSubinterfacesOfIncluding(classOrInterface).stream().flatMap(i -> {
                return h.getImplementersOf(i).stream();
            }).collect(Collectors.toList());
        }
        return h.getSubclassesOfIncluding(classOrInterface);
    }

    public static boolean canStoreType(FastHierarchy fh, Type child, Type parent) {
        if (child == null || parent == null) {
            return false;
        }
        return fh.canStoreType(child, parent);
    }
}
