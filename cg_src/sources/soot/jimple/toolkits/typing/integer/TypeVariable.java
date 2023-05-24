package soot.jimple.toolkits.typing.integer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/integer/TypeVariable.class */
public class TypeVariable implements Comparable<Object> {
    private static final Logger logger = LoggerFactory.getLogger(TypeVariable.class);
    private static final boolean DEBUG = false;
    private final int id;
    private TypeNode approx;
    private TypeNode inv_approx;
    private TypeNode type;
    private TypeVariable rep = this;
    private int rank = 0;
    private List<TypeVariable> parents = Collections.emptyList();
    private List<TypeVariable> children = Collections.emptyList();

    public TypeVariable(int id, TypeResolver resolver) {
        this.id = id;
    }

    public TypeVariable(int id, TypeResolver resolver, TypeNode type) {
        this.id = id;
        this.type = type;
        this.approx = type;
        this.inv_approx = type;
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
        this.inv_approx = null;
        this.approx = null;
        this.type = null;
        this.parents = null;
        this.children = null;
    }

    private void merge(TypeVariable var) throws TypeException {
        if (this.type == null) {
            this.type = var.type;
        } else if (var.type != null) {
            error("Type Error(22): Attempt to merge two types.");
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

    public List<TypeVariable> parents() {
        return this.rep != this ? ecr().parents() : this.parents;
    }

    public List<TypeVariable> children() {
        return this.rep != this ? ecr().children() : this.children;
    }

    public TypeNode approx() {
        return this.rep != this ? ecr().approx() : this.approx;
    }

    public TypeNode inv_approx() {
        return this.rep != this ? ecr().inv_approx() : this.inv_approx;
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

    public static void computeInvApprox(TreeSet<TypeVariable> workList) throws TypeException {
        while (workList.size() > 0) {
            TypeVariable var = workList.first();
            workList.remove(var);
            var.fixInvApprox(workList);
        }
    }

    private void fixApprox(TreeSet<TypeVariable> workList) throws TypeException {
        if (this.rep != this) {
            ecr().fixApprox(workList);
            return;
        }
        for (TypeVariable typeVariable : this.parents) {
            TypeVariable parent = typeVariable.ecr();
            if (parent.approx == null) {
                parent.approx = this.approx;
                workList.add(parent);
            } else {
                TypeNode type = parent.approx.lca_2(this.approx);
                if (type != parent.approx) {
                    parent.approx = type;
                    workList.add(parent);
                }
            }
        }
        if (this.type != null) {
            this.approx = this.type;
        }
    }

    private void fixInvApprox(TreeSet<TypeVariable> workList) throws TypeException {
        if (this.rep != this) {
            ecr().fixInvApprox(workList);
            return;
        }
        for (TypeVariable typeVariable : this.children) {
            TypeVariable child = typeVariable.ecr();
            if (child.inv_approx == null) {
                child.inv_approx = this.inv_approx;
                workList.add(child);
            } else {
                TypeNode type = child.inv_approx.gcd_2(this.inv_approx);
                if (type != child.inv_approx) {
                    child.inv_approx = type;
                    workList.add(child);
                }
            }
        }
        if (this.type != null) {
            this.inv_approx = this.type;
        }
    }

    public String toString() {
        if (this.rep != this) {
            return ecr().toString();
        }
        StringBuilder s = new StringBuilder();
        s.append("[id:").append(this.id);
        if (this.type != null) {
            s.append(",type:").append(this.type);
        }
        s.append(",approx:").append(this.approx);
        s.append(",inv_approx:").append(this.inv_approx);
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
        s.append("]]");
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
