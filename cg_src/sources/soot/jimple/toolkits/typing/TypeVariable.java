package soot.jimple.toolkits.typing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.JavaBasicTypes;
import soot.RefType;
import soot.options.Options;
import soot.util.BitVector;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/TypeVariable.class */
public class TypeVariable implements Comparable<Object> {
    private static final Logger logger = LoggerFactory.getLogger(TypeVariable.class);
    private static final boolean DEBUG = false;
    private final int id;
    private final TypeResolver resolver;
    private TypeNode approx;
    private TypeNode type;
    private TypeVariable array;
    private TypeVariable element;
    private int depth;
    private BitVector ancestors;
    private BitVector indirectAncestors;
    private TypeVariable rep = this;
    private int rank = 0;
    private List<TypeVariable> parents = Collections.emptyList();
    private List<TypeVariable> children = Collections.emptyList();

    public TypeVariable(int id, TypeResolver resolver) {
        this.id = id;
        this.resolver = resolver;
    }

    public TypeVariable(int id, TypeResolver resolver, TypeNode type) {
        this.id = id;
        this.resolver = resolver;
        this.type = type;
        this.approx = type;
        for (TypeNode parent : type.parents()) {
            addParent(resolver.typeVariable(parent));
        }
        if (type.hasElement()) {
            this.element = resolver.typeVariable(type.element());
            this.element.array = this;
        }
    }

    public int hashCode() {
        return this.rep != this ? ecr().hashCode() : this.id;
    }

    public boolean equals(Object obj) {
        if (this.rep != this) {
            return ecr().equals(obj);
        }
        return obj != null && obj.getClass().equals(getClass()) && ((TypeVariable) obj).ecr() == this;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object o) {
        if (this.rep != this) {
            return ecr().compareTo(o);
        }
        return this.id - ((TypeVariable) o).ecr().id;
    }

    private TypeVariable ecr() {
        if (this.rep != this) {
            this.rep = this.rep.ecr();
        }
        return this.rep;
    }

    public TypeVariable union(TypeVariable var) throws TypeException {
        if (this.rep != this) {
            return ecr().union(var);
        }
        TypeVariable y = var.ecr();
        if (this == y) {
            return this;
        }
        if (this.rank > y.rank) {
            y.rep = this;
            merge(y);
            y.clear();
            return this;
        }
        this.rep = y;
        if (this.rank == y.rank) {
            y.rank++;
        }
        y.merge(this);
        clear();
        return y;
    }

    private void clear() {
        this.approx = null;
        this.type = null;
        this.element = null;
        this.array = null;
        this.parents = null;
        this.children = null;
        this.ancestors = null;
        this.indirectAncestors = null;
    }

    private void merge(TypeVariable var) throws TypeException {
        if (this.depth != 0 || var.depth != 0) {
            throw new InternalTypingException();
        }
        if (this.type == null) {
            this.type = var.type;
        } else if (var.type != null) {
            error("Type Error(1): Attempt to merge two types.");
        }
        Set<TypeVariable> set = new TreeSet<>(this.parents);
        set.addAll(var.parents);
        set.remove(this);
        this.parents = Collections.unmodifiableList(new LinkedList(set));
        Set<TypeVariable> set2 = new TreeSet<>(this.children);
        set2.addAll(var.children);
        set2.remove(this);
        this.children = Collections.unmodifiableList(new LinkedList(set2));
    }

    void validate() throws TypeException {
        if (this.rep != this) {
            ecr().validate();
            return;
        }
        TypeNode thisType = this.type;
        if (thisType != null) {
            for (TypeVariable typeVariable : this.parents) {
                TypeNode parentType = typeVariable.ecr().type;
                if (parentType != null && !thisType.hasAncestor(parentType)) {
                    error("Type Error(2): Parent type is not a valid ancestor.");
                }
            }
            for (TypeVariable typeVariable2 : this.children) {
                TypeVariable child = typeVariable2.ecr();
                TypeNode childType = child.type;
                if (childType != null && !thisType.hasDescendant(childType)) {
                    error("Type Error(3): Child type is not a valid descendant.");
                }
            }
        }
    }

    public void removeIndirectRelations() {
        if (this.rep != this) {
            ecr().removeIndirectRelations();
            return;
        }
        if (this.indirectAncestors == null) {
            fixAncestors();
        }
        List<TypeVariable> parentsToRemove = new LinkedList<>();
        for (TypeVariable parent : this.parents) {
            if (this.indirectAncestors.get(parent.id())) {
                parentsToRemove.add(parent);
            }
        }
        for (TypeVariable parent2 : parentsToRemove) {
            removeParent(parent2);
        }
    }

    private void fixAncestors() {
        BitVector ancestors = new BitVector(0);
        BitVector indirectAncestors = new BitVector(0);
        for (TypeVariable typeVariable : this.parents) {
            TypeVariable parent = typeVariable.ecr();
            if (parent.ancestors == null) {
                parent.fixAncestors();
            }
            ancestors.set(parent.id);
            ancestors.or(parent.ancestors);
            indirectAncestors.or(parent.ancestors);
        }
        this.ancestors = ancestors;
        this.indirectAncestors = indirectAncestors;
    }

    public int id() {
        return this.rep != this ? ecr().id() : this.id;
    }

    public void addParent(TypeVariable variable) {
        if (this.rep != this) {
            ecr().addParent(variable);
            return;
        }
        TypeVariable var = variable.ecr();
        if (var == this) {
            return;
        }
        Set<TypeVariable> set = new TreeSet<>(this.parents);
        set.add(var);
        this.parents = Collections.unmodifiableList(new LinkedList(set));
        Set<TypeVariable> set2 = new TreeSet<>(var.children);
        set2.add(this);
        var.children = Collections.unmodifiableList(new LinkedList(set2));
    }

    public void removeParent(TypeVariable variable) {
        if (this.rep != this) {
            ecr().removeParent(variable);
            return;
        }
        TypeVariable var = variable.ecr();
        Set<TypeVariable> set = new TreeSet<>(this.parents);
        set.remove(var);
        this.parents = Collections.unmodifiableList(new LinkedList(set));
        Set<TypeVariable> set2 = new TreeSet<>(var.children);
        set2.remove(this);
        var.children = Collections.unmodifiableList(new LinkedList(set2));
    }

    public void addChild(TypeVariable variable) {
        if (this.rep != this) {
            ecr().addChild(variable);
            return;
        }
        TypeVariable var = variable.ecr();
        if (var == this) {
            return;
        }
        Set<TypeVariable> set = new TreeSet<>(this.children);
        set.add(var);
        this.children = Collections.unmodifiableList(new LinkedList(set));
        Set<TypeVariable> set2 = new TreeSet<>(var.parents);
        set2.add(this);
        var.parents = Collections.unmodifiableList(new LinkedList(set2));
    }

    public void removeChild(TypeVariable variable) {
        if (this.rep != this) {
            ecr().removeChild(variable);
            return;
        }
        TypeVariable var = variable.ecr();
        Set<TypeVariable> set = new TreeSet<>(this.children);
        set.remove(var);
        this.children = Collections.unmodifiableList(new LinkedList(set));
        Set<TypeVariable> set2 = new TreeSet<>(var.parents);
        set2.remove(this);
        var.parents = Collections.unmodifiableList(new LinkedList(set2));
    }

    public int depth() {
        return this.rep != this ? ecr().depth() : this.depth;
    }

    public void makeElement() {
        if (this.rep != this) {
            ecr().makeElement();
        } else if (this.element == null) {
            this.element = this.resolver.typeVariable();
            this.element.array = this;
        }
    }

    public TypeVariable element() {
        if (this.rep != this) {
            return ecr().element();
        }
        if (this.element == null) {
            return null;
        }
        return this.element.ecr();
    }

    public TypeVariable array() {
        if (this.rep != this) {
            return ecr().array();
        }
        if (this.array == null) {
            return null;
        }
        return this.array.ecr();
    }

    public List<TypeVariable> parents() {
        return this.rep != this ? ecr().parents() : this.parents;
    }

    public List<TypeVariable> children() {
        return this.rep != this ? ecr().children() : this.children;
    }

    public TypeNode approx() {
        return this.rep != this ? ecr().approx() : this.approx;
    }

    public TypeNode type() {
        return this.rep != this ? ecr().type() : this.type;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void error(String message) throws TypeException {
        TypeException e = new TypeException(message);
        throw e;
    }

    public static void computeApprox(TreeSet<TypeVariable> workList) throws TypeException {
        while (workList.size() > 0) {
            TypeVariable var = workList.first();
            workList.remove(var);
            var.fixApprox(workList);
        }
    }

    private void fixApprox(TreeSet<TypeVariable> workList) throws TypeException {
        TypeNode type;
        if (this.rep != this) {
            ecr().fixApprox(workList);
            return;
        }
        if (this.type == null && this.approx != this.resolver.hierarchy().NULL) {
            TypeVariable element = element();
            if (element != null) {
                if (!this.approx.hasElement()) {
                    logger.debug("*** " + this + " ***");
                    error("Type Error(4)");
                }
                TypeNode temp = this.approx.element();
                if (element.approx == null) {
                    element.approx = temp;
                    workList.add(element);
                } else {
                    TypeNode type2 = element.approx.lca(temp);
                    if (type2 != element.approx) {
                        element.approx = type2;
                        workList.add(element);
                    } else if (element.approx != this.resolver.hierarchy().INT && (type = this.approx.lca(element.approx.array())) != this.approx) {
                        this.approx = type;
                        workList.add(this);
                    }
                }
            }
            TypeVariable array = array();
            if (array != null && this.approx != this.resolver.hierarchy().NULL && this.approx != this.resolver.hierarchy().INT) {
                TypeNode temp2 = this.approx.array();
                if (array.approx == null) {
                    array.approx = temp2;
                    workList.add(array);
                } else {
                    TypeNode type3 = array.approx.lca(temp2);
                    if (type3 != array.approx) {
                        array.approx = type3;
                        workList.add(array);
                    } else {
                        TypeNode type4 = this.approx.lca(array.approx.element());
                        if (type4 != this.approx) {
                            this.approx = type4;
                            workList.add(this);
                        }
                    }
                }
            }
        }
        for (TypeVariable typeVariable : this.parents) {
            TypeVariable parent = typeVariable.ecr();
            if (parent.approx == null) {
                parent.approx = this.approx;
                workList.add(parent);
            } else {
                TypeNode type5 = parent.approx.lca(this.approx);
                if (type5 != parent.approx) {
                    parent.approx = type5;
                    workList.add(parent);
                }
            }
        }
        if (this.type != null) {
            this.approx = this.type;
        }
    }

    public void fixDepth() throws TypeException {
        if (this.rep != this) {
            ecr().fixDepth();
            return;
        }
        if (this.type != null) {
            if (this.type.type() instanceof ArrayType) {
                ArrayType at = (ArrayType) this.type.type();
                this.depth = at.numDimensions;
            } else {
                this.depth = 0;
            }
        } else if (this.approx.type() instanceof ArrayType) {
            ArrayType at2 = (ArrayType) this.approx.type();
            this.depth = at2.numDimensions;
        } else {
            this.depth = 0;
        }
        if (this.depth == 0 && element() != null) {
            error("Type Error(11)");
        } else if (this.depth > 0 && element() == null) {
            makeElement();
            TypeVariable element = element();
            element.depth = this.depth - 1;
            while (element.depth != 0) {
                element.makeElement();
                element.element().depth = element.depth - 1;
                element = element.element();
            }
        }
    }

    public void propagate() {
        if (this.rep != this) {
            ecr().propagate();
        }
        if (this.depth == 0) {
            return;
        }
        for (TypeVariable typeVariable : this.parents) {
            TypeVariable var = typeVariable.ecr();
            int varDepth = var.depth();
            if (varDepth == this.depth) {
                element().addParent(var.element());
            } else if (varDepth == 0) {
                if (var.type() == null && !Options.v().j2me()) {
                    var.addChild(this.resolver.typeVariable(this.resolver.hierarchy().CLONEABLE));
                    var.addChild(this.resolver.typeVariable(this.resolver.hierarchy().SERIALIZABLE));
                }
            } else if (var.type() == null && !Options.v().j2me()) {
                var.addChild(this.resolver.typeVariable(ArrayType.v(RefType.v("java.lang.Cloneable"), varDepth)));
                var.addChild(this.resolver.typeVariable(ArrayType.v(RefType.v(JavaBasicTypes.JAVA_IO_SERIALIZABLE), varDepth)));
            }
        }
        for (TypeVariable var2 : this.parents) {
            removeParent(var2);
        }
    }

    public String toString() {
        if (this.rep != this) {
            return ecr().toString();
        }
        StringBuilder s = new StringBuilder();
        s.append("[id:").append(this.id).append(",depth:").append(this.depth);
        if (this.type != null) {
            s.append(",type:").append(this.type);
        }
        s.append(",approx:").append(this.approx);
        s.append(",[parents:");
        boolean comma = false;
        for (TypeVariable typeVariable : this.parents) {
            if (comma) {
                s.append(',');
            } else {
                comma = true;
            }
            s.append(typeVariable.id());
        }
        s.append("],[children:");
        boolean comma2 = false;
        for (TypeVariable typeVariable2 : this.children) {
            if (comma2) {
                s.append(',');
            } else {
                comma2 = true;
            }
            s.append(typeVariable2.id());
        }
        s.append(']');
        if (this.element != null) {
            s.append(",arrayof:").append(this.element.id());
        }
        s.append(']');
        return s.toString();
    }

    public void fixParents() {
        if (this.rep != this) {
            ecr().fixParents();
        } else {
            this.parents = Collections.unmodifiableList(new LinkedList(new TreeSet(this.parents)));
        }
    }

    public void fixChildren() {
        if (this.rep != this) {
            ecr().fixChildren();
        } else {
            this.children = Collections.unmodifiableList(new LinkedList(new TreeSet(this.children)));
        }
    }
}
