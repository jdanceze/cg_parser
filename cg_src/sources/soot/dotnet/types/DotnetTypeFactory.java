package soot.dotnet.types;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.ArrayList;
import java.util.List;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.RefType;
import soot.ShortType;
import soot.Type;
import soot.Value;
import soot.VoidType;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
/* loaded from: gencallgraphv3.jar:soot/dotnet/types/DotnetTypeFactory.class */
public class DotnetTypeFactory {
    public static Type toSootType(String type) {
        if (type.equals(IntType.v().getTypeAsString()) || type.equals(DotnetBasicTypes.SYSTEM_INTPTR) || type.equals(DotnetBasicTypes.SYSTEM_UINTPTR) || type.equals("nint") || type.equals("nuint")) {
            return IntType.v();
        }
        if (type.equals(ByteType.v().getTypeAsString())) {
            return ByteType.v();
        }
        if (type.equals(CharType.v().getTypeAsString())) {
            return CharType.v();
        }
        if (type.equals(DoubleType.v().getTypeAsString())) {
            return DoubleType.v();
        }
        if (type.equals(FloatType.v().getTypeAsString())) {
            return FloatType.v();
        }
        if (type.equals(LongType.v().getTypeAsString())) {
            return LongType.v();
        }
        if (type.equals(ShortType.v().getTypeAsString())) {
            return ShortType.v();
        }
        if (type.equals(BooleanType.v().getTypeAsString())) {
            return BooleanType.v();
        }
        if (type.equals(DotnetBasicTypes.SYSTEM_VOID)) {
            return VoidType.v();
        }
        if (type.equals(DotnetBasicTypes.SYSTEM_UINT32)) {
            return IntType.v();
        }
        if (type.equals(DotnetBasicTypes.SYSTEM_SBYTE)) {
            return ByteType.v();
        }
        if (type.equals(DotnetBasicTypes.SYSTEM_DECIMAL)) {
            return DoubleType.v();
        }
        if (type.equals(DotnetBasicTypes.SYSTEM_UINT64)) {
            return LongType.v();
        }
        if (type.equals(DotnetBasicTypes.SYSTEM_UINT16)) {
            return ShortType.v();
        }
        if (type.startsWith("`") || type.startsWith("``")) {
            return RefType.v(DotnetBasicTypes.SYSTEM_OBJECT);
        }
        return RefType.v(type);
    }

    public static Type toSootType(ProtoAssemblyAllTypes.TypeDefinition dotnetType) {
        if (dotnetType.getTypeKind().equals(ProtoAssemblyAllTypes.TypeKindDef.ARRAY) || dotnetType.getTypeKind().equals(ProtoAssemblyAllTypes.TypeKindDef.BY_REF_AND_ARRAY)) {
            return ArrayType.v(toSootType(dotnetType.getFullname()), dotnetType.getArrayDimensions());
        }
        return toSootType(dotnetType.getFullname());
    }

    public static Type toSootType(Type type) {
        if (type instanceof RefType) {
            return toSootType(type.toString());
        }
        return type;
    }

    public static List<Type> toSootTypeList(List<String> types) {
        ArrayList<Type> ret = new ArrayList<>();
        for (String type : types) {
            ret.add(toSootType(type));
        }
        return ret;
    }

    public static Value initType(Local variable) {
        Type t = variable.getType();
        return initType(t);
    }

    public static Value initType(Type t) {
        if (t instanceof IntType) {
            return IntConstant.v(0);
        }
        if (t instanceof FloatType) {
            return FloatConstant.v(0.0f);
        }
        if (t instanceof DoubleType) {
            return DoubleConstant.v(Const.default_value_double);
        }
        if (t instanceof LongType) {
            return LongConstant.v(0L);
        }
        if ((t instanceof ByteType) || (t instanceof BooleanType) || (t instanceof ShortType) || (t instanceof CharType)) {
            return IntConstant.v(0);
        }
        return NullConstant.v();
    }

    public static List<String> listOfCilPrimitives() {
        ArrayList<String> lst = new ArrayList<>();
        lst.add(DotnetBasicTypes.SYSTEM_INTPTR);
        lst.add(DotnetBasicTypes.SYSTEM_UINTPTR);
        lst.add("nint");
        lst.add("nuint");
        lst.add(DotnetBasicTypes.SYSTEM_UINT32);
        lst.add(DotnetBasicTypes.SYSTEM_SBYTE);
        lst.add(DotnetBasicTypes.SYSTEM_DECIMAL);
        lst.add(DotnetBasicTypes.SYSTEM_UINT64);
        lst.add(DotnetBasicTypes.SYSTEM_UINT16);
        return lst;
    }
}
