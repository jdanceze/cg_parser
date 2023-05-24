package javassist.bytecode.stackmap;

import javassist.bytecode.stackmap.TypeData;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/bytecode/stackmap/TypeTag.class */
public interface TypeTag {
    public static final String TOP_TYPE = "*top*";
    public static final TypeData.BasicType TOP = new TypeData.BasicType(TOP_TYPE, 0, ' ');
    public static final TypeData.BasicType INTEGER = new TypeData.BasicType("int", 1, 'I');
    public static final TypeData.BasicType FLOAT = new TypeData.BasicType(Jimple.FLOAT, 2, 'F');
    public static final TypeData.BasicType DOUBLE = new TypeData.BasicType("double", 3, 'D');
    public static final TypeData.BasicType LONG = new TypeData.BasicType("long", 4, 'J');
}
