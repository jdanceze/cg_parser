package soot.jimple.toolkits.typing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.NullType;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Type;
import soot.options.Options;
import soot.util.BitVector;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/TypeNode.class */
public class TypeNode {
    private static final Logger logger = LoggerFactory.getLogger(TypeNode.class);
    private static final boolean DEBUG = false;
    private final int id;
    private final Type type;
    private final ClassHierarchy hierarchy;
    private TypeNode parentClass;
    private TypeNode element;
    private TypeNode array;
    private List<TypeNode> parents;
    private final BitVector ancestors;
    private final BitVector descendants;

    public TypeNode(int id, Type type, ClassHierarchy hierarchy) {
        this.parents = Collections.emptyList();
        this.ancestors = new BitVector(0);
        this.descendants = new BitVector(0);
        if (type == null || hierarchy == null) {
            throw new InternalTypingException();
        }
        if (!(type instanceof PrimType) && !(type instanceof RefType) && !(type instanceof ArrayType) && !(type instanceof NullType)) {
            logger.debug("Unhandled type: " + type);
            throw new InternalTypingException();
        }
        this.id = id;
        this.type = type;
        this.hierarchy = hierarchy;
    }

    public TypeNode(int id, RefType type, ClassHierarchy hierarchy) {
        this(id, (Type) type, hierarchy);
        SootClass sClass = type.getSootClass();
        if (sClass == null) {
            throw new RuntimeException("Oops, forgot to load " + type);
        }
        if (sClass.isPhantomClass()) {
            throw new RuntimeException("Jimplification requires " + sClass + ", but it is a phantom ref.");
        }
        List<TypeNode> plist = new LinkedList<>();
        SootClass superclass = sClass.getSuperclassUnsafe();
        if (superclass != null && !sClass.getName().equals(Scene.v().getObjectType().toString())) {
            TypeNode parent = hierarchy.typeNode(RefType.v(sClass.getSuperclass().getName()));
            plist.add(parent);
            this.parentClass = parent;
        }
        for (SootClass sootClass : sClass.getInterfaces()) {
            plist.add(hierarchy.typeNode(RefType.v(sootClass.getName())));
        }
        this.parents = Collections.unmodifiableList(plist);
        this.descendants.set(hierarchy.NULL.id);
        hierarchy.NULL.ancestors.set(id);
        for (TypeNode parent2 : this.parents) {
            this.ancestors.set(parent2.id);
            this.ancestors.or(parent2.ancestors);
            parent2.fixDescendants(id);
        }
    }

    public TypeNode(int id, ArrayType type, ClassHierarchy hierarchy) {
        this(id, (Type) type, hierarchy);
        if (type.numDimensions < 1) {
            throw new InternalTypingException();
        }
        if (type.numDimensions == 1) {
            this.element = hierarchy.typeNode(type.baseType);
        } else {
            this.element = hierarchy.typeNode(ArrayType.v(type.baseType, type.numDimensions - 1));
        }
        if (this.element != hierarchy.INT) {
            if (this.element.array != null) {
                throw new InternalTypingException();
            }
            this.element.array = this;
        }
        List<TypeNode> plist = new LinkedList<>();
        if (type.baseType instanceof RefType) {
            RefType baseType = (RefType) type.baseType;
            SootClass sClass = baseType.getSootClass();
            SootClass superClass = sClass.getSuperclassUnsafe();
            if (superClass != null && !superClass.getName().equals(Scene.v().getObjectType().toString())) {
                TypeNode parent = hierarchy.typeNode(ArrayType.v(RefType.v(sClass.getSuperclass().getName()), type.numDimensions));
                plist.add(parent);
                this.parentClass = parent;
            } else if (type.numDimensions == 1) {
                plist.add(hierarchy.OBJECT);
                if (!Options.v().j2me()) {
                    plist.add(hierarchy.CLONEABLE);
                    plist.add(hierarchy.SERIALIZABLE);
                }
                this.parentClass = hierarchy.OBJECT;
            } else {
                plist.add(hierarchy.typeNode(ArrayType.v(hierarchy.OBJECT.type(), type.numDimensions - 1)));
                if (!Options.v().j2me()) {
                    plist.add(hierarchy.typeNode(ArrayType.v(hierarchy.CLONEABLE.type(), type.numDimensions - 1)));
                    plist.add(hierarchy.typeNode(ArrayType.v(hierarchy.SERIALIZABLE.type(), type.numDimensions - 1)));
                }
                this.parentClass = hierarchy.typeNode(ArrayType.v(hierarchy.OBJECT.type(), type.numDimensions - 1));
            }
            for (SootClass sootClass : sClass.getInterfaces()) {
                plist.add(hierarchy.typeNode(ArrayType.v(RefType.v(sootClass.getName()), type.numDimensions)));
            }
        } else if (type.numDimensions == 1) {
            plist.add(hierarchy.OBJECT);
            if (!Options.v().j2me()) {
                plist.add(hierarchy.CLONEABLE);
                plist.add(hierarchy.SERIALIZABLE);
            }
            this.parentClass = hierarchy.OBJECT;
        } else {
            plist.add(hierarchy.typeNode(ArrayType.v(hierarchy.OBJECT.type(), type.numDimensions - 1)));
            if (!Options.v().j2me()) {
                plist.add(hierarchy.typeNode(ArrayType.v(hierarchy.CLONEABLE.type(), type.numDimensions - 1)));
                plist.add(hierarchy.typeNode(ArrayType.v(hierarchy.SERIALIZABLE.type(), type.numDimensions - 1)));
            }
            this.parentClass = hierarchy.typeNode(ArrayType.v(hierarchy.OBJECT.type(), type.numDimensions - 1));
        }
        this.parents = Collections.unmodifiableList(plist);
        this.descendants.set(hierarchy.NULL.id);
        hierarchy.NULL.ancestors.set(id);
        for (TypeNode parent2 : this.parents) {
            this.ancestors.set(parent2.id);
            this.ancestors.or(parent2.ancestors);
            parent2.fixDescendants(id);
        }
    }

    private void fixDescendants(int id) {
        if (this.descendants.get(id)) {
            return;
        }
        for (TypeNode parent : this.parents) {
            parent.fixDescendants(id);
        }
        this.descendants.set(id);
    }

    public int id() {
        return this.id;
    }

    public Type type() {
        return this.type;
    }

    public boolean hasAncestor(TypeNode typeNode) {
        return this.ancestors.get(typeNode.id);
    }

    public boolean hasAncestorOrSelf(TypeNode typeNode) {
        if (typeNode == this) {
            return true;
        }
        return this.ancestors.get(typeNode.id);
    }

    public boolean hasDescendant(TypeNode typeNode) {
        return this.descendants.get(typeNode.id);
    }

    public boolean hasDescendantOrSelf(TypeNode typeNode) {
        if (typeNode == this) {
            return true;
        }
        return this.descendants.get(typeNode.id);
    }

    public List<TypeNode> parents() {
        return this.parents;
    }

    public TypeNode parentClass() {
        return this.parentClass;
    }

    public String toString() {
        return String.valueOf(this.type.toString()) + "(" + this.id + ")";
    }

    public TypeNode lca(TypeNode type) throws TypeException {
        if (type == null) {
            throw new InternalTypingException();
        }
        if (type == this) {
            return this;
        }
        if (hasAncestor(type)) {
            return type;
        }
        if (hasDescendant(type)) {
            return this;
        }
        do {
            type = type.parentClass;
            if (type == null) {
                try {
                    TypeVariable.error("Type Error(12)");
                } catch (TypeException e) {
                    throw e;
                }
            }
        } while (!hasAncestor(type));
        return type;
    }

    public TypeNode lcaIfUnique(TypeNode type) throws TypeException {
        if (type == null) {
            throw new InternalTypingException();
        }
        if (type == this) {
            return this;
        }
        if (hasAncestor(type)) {
            return type;
        }
        if (hasDescendant(type)) {
            return this;
        }
        while (type.parents.size() == 1) {
            type = type.parents.get(0);
            if (hasAncestor(type)) {
                return type;
            }
        }
        return null;
    }

    public boolean hasElement() {
        return this.element != null;
    }

    public TypeNode element() {
        if (this.element == null) {
            throw new InternalTypingException();
        }
        return this.element;
    }

    public TypeNode array() {
        if (this.array != null) {
            return this.array;
        }
        if (this.type instanceof ArrayType) {
            ArrayType atype = (ArrayType) this.type;
            this.array = this.hierarchy.typeNode(ArrayType.v(atype.baseType, atype.numDimensions + 1));
            return this.array;
        } else if ((this.type instanceof PrimType) || (this.type instanceof RefType)) {
            this.array = this.hierarchy.typeNode(ArrayType.v(this.type, 1));
            return this.array;
        } else {
            throw new InternalTypingException();
        }
    }

    public boolean isNull() {
        if (this.type instanceof NullType) {
            return true;
        }
        return false;
    }

    public boolean isClass() {
        if (!(this.type instanceof ArrayType) && !(this.type instanceof NullType)) {
            if ((this.type instanceof RefType) && !((RefType) this.type).getSootClass().isInterface()) {
                return true;
            }
            return false;
        }
        return true;
    }

    public boolean isClassOrInterface() {
        if ((this.type instanceof ArrayType) || (this.type instanceof NullType) || (this.type instanceof RefType)) {
            return true;
        }
        return false;
    }

    public boolean isArray() {
        if ((this.type instanceof ArrayType) || (this.type instanceof NullType)) {
            return true;
        }
        return false;
    }
}
