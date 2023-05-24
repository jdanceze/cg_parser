package soot.jimple.toolkits.typing;

import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.JavaBasicTypes;
import soot.RefType;
import soot.options.Options;
import soot.util.BitSetIterator;
import soot.util.BitVector;
/* JADX INFO: Access modifiers changed from: package-private */
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/TypeVariableBV.class */
public class TypeVariableBV implements Comparable<Object> {
    private static final Logger logger = LoggerFactory.getLogger(TypeVariableBV.class);
    private static final boolean DEBUG = false;
    private final int id;
    private final TypeResolverBV resolver;
    private TypeNode approx;
    private TypeNode type;
    private TypeVariableBV array;
    private TypeVariableBV element;
    private int depth;
    private BitVector ancestors;
    private BitVector indirectAncestors;
    private TypeVariableBV rep = this;
    private int rank = 0;
    private BitVector parents = new BitVector();
    private BitVector children = new BitVector();

    public TypeVariableBV(int id, TypeResolverBV resolver) {
        this.id = id;
        this.resolver = resolver;
    }

    public TypeVariableBV(int id, TypeResolverBV resolver, TypeNode type) {
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
        if (this.rep != this) {
            return ecr().hashCode();
        }
        return this.id;
    }

    public boolean equals(Object obj) {
        if (this.rep != this) {
            return ecr().equals(obj);
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        TypeVariableBV ecr = ((TypeVariableBV) obj).ecr();
        if (ecr != this) {
            return false;
        }
        return true;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object o) {
        if (this.rep != this) {
            return ecr().compareTo(o);
        }
        return this.id - ((TypeVariableBV) o).ecr().id;
    }

    private TypeVariableBV ecr() {
        if (this.rep != this) {
            this.rep = this.rep.ecr();
        }
        return this.rep;
    }

    public TypeVariableBV union(TypeVariableBV var) throws TypeException {
        if (this.rep != this) {
            return ecr().union(var);
        }
        TypeVariableBV y = var.ecr();
        if (this == y) {
            this.parents.clear(var.ownId());
            this.children.clear(var.ownId());
            return this;
        } else if (this.rank > y.rank) {
            this.resolver.invalidateId(y.id());
            y.rep = this;
            merge(y);
            y.clear();
            return this;
        } else {
            this.resolver.invalidateId(id());
            this.rep = y;
            if (this.rank == y.rank) {
                y.rank++;
            }
            y.merge(this);
            clear();
            return y;
        }
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

    private void merge(TypeVariableBV var) throws TypeException {
        if (this.depth != 0 || var.depth != 0) {
            throw new InternalTypingException();
        }
        if (this.type == null) {
            this.type = var.type;
        } else if (var.type != null) {
            error("Type Error(1): Attempt to merge two types.");
        }
        this.parents.or(var.parents);
        this.parents.clear(var.ownId());
        this.parents.clear(ownId());
        this.children.or(var.children);
        this.children.clear(var.ownId());
        this.children.clear(ownId());
    }

    void validate() throws TypeException {
        if (this.rep != this) {
            ecr().validate();
        } else if (this.type != null) {
            BitSetIterator i = this.parents.iterator();
            while (i.hasNext()) {
                TypeVariableBV parent = this.resolver.typeVariableForId(i.next()).ecr();
                if (parent.type != null && !this.type.hasAncestor(parent.type)) {
                    error("Type Error(2): Parent type is not a valid ancestor.");
                }
            }
            BitSetIterator i2 = this.children.iterator();
            while (i2.hasNext()) {
                TypeVariableBV child = this.resolver.typeVariableForId(i2.next()).ecr();
                if (child.type != null && !this.type.hasDescendant(child.type)) {
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
        BitVector parentsToRemove = new BitVector();
        BitSetIterator parentIt = this.parents.iterator();
        while (parentIt.hasNext()) {
            int parent = parentIt.next();
            if (this.indirectAncestors.get(parent)) {
                parentsToRemove.set(parent);
            }
        }
        BitSetIterator i = parentsToRemove.iterator();
        while (i.hasNext()) {
            removeParent(this.resolver.typeVariableForId(i.next()));
        }
    }

    private void fixAncestors() {
        BitVector ancestors = new BitVector(0);
        BitVector indirectAncestors = new BitVector(0);
        fixParents();
        BitSetIterator i = this.parents.iterator();
        while (i.hasNext()) {
            TypeVariableBV parent = this.resolver.typeVariableForId(i.next()).ecr();
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

    private void fixParents() {
        if (this.rep != this) {
            ecr().fixParents();
        }
        BitVector invalid = new BitVector();
        invalid.or(this.parents);
        invalid.and(this.resolver.invalidIds());
        BitSetIterator i = invalid.iterator();
        while (i.hasNext()) {
            this.parents.set(this.resolver.typeVariableForId(i.next()).id());
        }
        this.parents.clear(this.id);
        this.parents.clear(id());
        this.parents.andNot(invalid);
    }

    public int id() {
        if (this.rep != this) {
            return ecr().id();
        }
        return this.id;
    }

    public int ownId() {
        return this.id;
    }

    public void addParent(TypeVariableBV variable) {
        if (this.rep != this) {
            ecr().addParent(variable);
            return;
        }
        TypeVariableBV var = variable.ecr();
        if (var == this) {
            return;
        }
        this.parents.set(var.id);
        var.children.set(this.id);
    }

    public void removeParent(TypeVariableBV variable) {
        if (this.rep != this) {
            ecr().removeParent(variable);
            return;
        }
        this.parents.clear(variable.id());
        this.parents.clear(variable.ownId());
        variable.children().clear(this.id);
    }

    public void addChild(TypeVariableBV variable) {
        if (this.rep != this) {
            ecr().addChild(variable);
            return;
        }
        TypeVariableBV var = variable.ecr();
        if (var == this) {
            return;
        }
        this.children.set(var.id);
        this.parents.set(var.id);
    }

    public void removeChild(TypeVariableBV variable) {
        if (this.rep != this) {
            ecr().removeChild(variable);
            return;
        }
        TypeVariableBV var = variable.ecr();
        this.children.clear(var.id);
        var.parents.clear(var.id);
    }

    public int depth() {
        if (this.rep != this) {
            return ecr().depth();
        }
        return this.depth;
    }

    public void makeElement() {
        if (this.rep != this) {
            ecr().makeElement();
        } else if (this.element == null) {
            this.element = this.resolver.typeVariable();
            this.element.array = this;
        }
    }

    public TypeVariableBV element() {
        if (this.rep != this) {
            return ecr().element();
        }
        if (this.element == null) {
            return null;
        }
        return this.element.ecr();
    }

    public TypeVariableBV array() {
        if (this.rep != this) {
            return ecr().array();
        }
        if (this.array == null) {
            return null;
        }
        return this.array.ecr();
    }

    public BitVector parents() {
        if (this.rep != this) {
            return ecr().parents();
        }
        return this.parents;
    }

    public BitVector children() {
        if (this.rep != this) {
            return ecr().children();
        }
        return this.children;
    }

    public TypeNode approx() {
        if (this.rep != this) {
            return ecr().approx();
        }
        return this.approx;
    }

    public TypeNode type() {
        if (this.rep != this) {
            return ecr().type();
        }
        return this.type;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void error(String message) throws TypeException {
        try {
            throw new TypeException(message);
        } catch (TypeException e) {
            throw e;
        }
    }

    public static void computeApprox(TreeSet<TypeVariableBV> workList) throws TypeException {
        while (workList.size() > 0) {
            TypeVariableBV var = workList.first();
            workList.remove(var);
            var.fixApprox(workList);
        }
    }

    private void fixApprox(TreeSet<TypeVariableBV> workList) throws TypeException {
        TypeNode type;
        if (this.rep != this) {
            ecr().fixApprox(workList);
            return;
        }
        if (this.type == null && this.approx != this.resolver.hierarchy().NULL) {
            TypeVariableBV element = element();
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
            TypeVariableBV array = array();
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
        BitSetIterator i = this.parents.iterator();
        while (i.hasNext()) {
            TypeVariableBV parent = this.resolver.typeVariableForId(i.next()).ecr();
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
            TypeVariableBV element = element();
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
        BitSetIterator i = this.parents.iterator();
        while (i.hasNext()) {
            TypeVariableBV var = this.resolver.typeVariableForId(i.next()).ecr();
            if (var.depth() == this.depth) {
                element().addParent(var.element());
            } else if (var.depth() == 0) {
                if (var.type() == null && !Options.v().j2me()) {
                    var.addChild(this.resolver.typeVariable(this.resolver.hierarchy().CLONEABLE));
                    var.addChild(this.resolver.typeVariable(this.resolver.hierarchy().SERIALIZABLE));
                }
            } else if (var.type() == null && !Options.v().j2me()) {
                var.addChild(this.resolver.typeVariable(ArrayType.v(RefType.v("java.lang.Cloneable"), var.depth())));
                var.addChild(this.resolver.typeVariable(ArrayType.v(RefType.v(JavaBasicTypes.JAVA_IO_SERIALIZABLE), var.depth())));
            }
        }
        BitSetIterator varIt = this.parents.iterator();
        while (varIt.hasNext()) {
            removeParent(this.resolver.typeVariableForId(varIt.next()));
        }
    }

    public String toString() {
        if (this.rep != this) {
            return ecr().toString();
        }
        StringBuffer s = new StringBuffer();
        s.append(",[parents:");
        boolean comma = false;
        BitSetIterator i = this.parents.iterator();
        while (i.hasNext()) {
            if (comma) {
                s.append(",");
            } else {
                comma = true;
            }
            s.append(i.next());
        }
        s.append("],[children:");
        boolean comma2 = false;
        BitSetIterator i2 = this.children.iterator();
        while (i2.hasNext()) {
            if (comma2) {
                s.append(",");
            } else {
                comma2 = true;
            }
            s.append(i2.next());
        }
        s.append("]");
        return "[id:" + this.id + ",depth:" + this.depth + (this.type != null ? ",type:" + this.type : "") + ",approx:" + this.approx + ((Object) s) + (this.element == null ? "" : ",arrayof:" + this.element.id()) + "]";
    }
}
