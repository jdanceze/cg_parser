package soot.jimple.toolkits.typing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.G;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.NullType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.Type;
import soot.TypeSwitch;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/ClassHierarchy.class */
public class ClassHierarchy {
    public final TypeNode OBJECT;
    public final TypeNode CLONEABLE;
    public final TypeNode SERIALIZABLE;
    public final TypeNode NULL;
    public final TypeNode INT;
    private final List<TypeNode> typeNodeList = new ArrayList();
    private final HashMap<Type, TypeNode> typeNodeMap = new HashMap<>();
    private final ToInt transform = new ToInt(null);
    private final ConstructorChooser make = new ConstructorChooser(null);

    private ClassHierarchy(Scene scene) {
        if (scene == null) {
            throw new InternalTypingException();
        }
        G.v().ClassHierarchy_classHierarchyMap.put(scene, this);
        this.NULL = typeNode(NullType.v());
        this.OBJECT = typeNode(Scene.v().getObjectType());
        if (!Options.v().j2me() && Options.v().src_prec() != 7) {
            this.CLONEABLE = typeNode(RefType.v("java.lang.Cloneable"));
            this.SERIALIZABLE = typeNode(RefType.v(JavaBasicTypes.JAVA_IO_SERIALIZABLE));
        } else {
            this.CLONEABLE = null;
            this.SERIALIZABLE = null;
        }
        this.INT = typeNode(IntType.v());
    }

    public static ClassHierarchy classHierarchy(Scene scene) {
        if (scene == null) {
            throw new InternalTypingException();
        }
        ClassHierarchy classHierarchy = G.v().ClassHierarchy_classHierarchyMap.get(scene);
        if (classHierarchy == null) {
            classHierarchy = new ClassHierarchy(scene);
        }
        return classHierarchy;
    }

    public TypeNode typeNode(Type type) {
        if (type == null) {
            throw new InternalTypingException();
        }
        Type type2 = this.transform.toInt(type);
        TypeNode typeNode = this.typeNodeMap.get(type2);
        if (typeNode == null) {
            int id = this.typeNodeList.size();
            this.typeNodeList.add(null);
            typeNode = this.make.typeNode(id, type2, this);
            this.typeNodeList.set(id, typeNode);
            this.typeNodeMap.put(type2, typeNode);
        }
        return typeNode;
    }

    public String toString() {
        StringBuilder s = new StringBuilder("ClassHierarchy:{");
        boolean colon = false;
        for (TypeNode typeNode : this.typeNodeList) {
            if (colon) {
                s.append(',');
            } else {
                colon = true;
            }
            s.append(typeNode);
        }
        s.append('}');
        return s.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/ClassHierarchy$ToInt.class */
    public static class ToInt extends TypeSwitch<Type> {
        private final Type intType;

        private ToInt() {
            this.intType = IntType.v();
        }

        /* synthetic */ ToInt(ToInt toInt) {
            this();
        }

        public Type toInt(Type type) {
            type.apply(this);
            return getResult();
        }

        @Override // soot.TypeSwitch, soot.ITypeSwitch
        public void caseBooleanType(BooleanType type) {
            setResult(this.intType);
        }

        @Override // soot.TypeSwitch, soot.ITypeSwitch
        public void caseByteType(ByteType type) {
            setResult(this.intType);
        }

        @Override // soot.TypeSwitch, soot.ITypeSwitch
        public void caseShortType(ShortType type) {
            setResult(this.intType);
        }

        @Override // soot.TypeSwitch, soot.ITypeSwitch
        public void caseCharType(CharType type) {
            setResult(this.intType);
        }

        @Override // soot.TypeSwitch, soot.ITypeSwitch
        public void defaultCase(Type type) {
            setResult(type);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/ClassHierarchy$ConstructorChooser.class */
    public static class ConstructorChooser extends TypeSwitch<TypeNode> {
        private int id;
        private ClassHierarchy hierarchy;

        private ConstructorChooser() {
        }

        /* synthetic */ ConstructorChooser(ConstructorChooser constructorChooser) {
            this();
        }

        public TypeNode typeNode(int id, Type type, ClassHierarchy hierarchy) {
            if (type == null || hierarchy == null) {
                throw new InternalTypingException();
            }
            this.id = id;
            this.hierarchy = hierarchy;
            type.apply(this);
            return getResult();
        }

        @Override // soot.TypeSwitch, soot.ITypeSwitch
        public void caseRefType(RefType type) {
            setResult(new TypeNode(this.id, type, this.hierarchy));
        }

        @Override // soot.TypeSwitch, soot.ITypeSwitch
        public void caseArrayType(ArrayType type) {
            setResult(new TypeNode(this.id, type, this.hierarchy));
        }

        @Override // soot.TypeSwitch, soot.ITypeSwitch
        public void defaultCase(Type type) {
            setResult(new TypeNode(this.id, type, this.hierarchy));
        }
    }
}
