package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ParTypeAccess.class */
public class ParTypeAccess extends Access implements Cloneable {
    protected boolean type_computed = false;
    protected TypeDecl type_value;

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode, beaver.Symbol
    public ParTypeAccess clone() throws CloneNotSupportedException {
        ParTypeAccess node = (ParTypeAccess) super.clone();
        node.type_computed = false;
        node.type_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ParTypeAccess node = clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: fullCopy */
    public ASTNode<ASTNode> fullCopy2() {
        ASTNode<ASTNode> copy2 = copy2();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    copy2.setChild(child.fullCopy(), i);
                }
            }
        }
        return copy2;
    }

    public boolean isRaw() {
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public void typeCheck() {
        super.typeCheck();
        if (!genericDecl().isUnknown()) {
            TypeDecl type = type();
            if (!genericDecl().isGenericType()) {
                error(String.valueOf(genericDecl().typeName()) + " is not a generic type but used as one in " + this);
            } else if (!type.isRawType() && type.isNestedType() && type.enclosingType().isRawType()) {
                error("Can not access a member type of a raw type as a parameterized type");
            } else {
                GenericTypeDecl decl = (GenericTypeDecl) genericDecl();
                GenericTypeDecl original = (GenericTypeDecl) decl.original();
                if (original.getNumTypeParameter() != getNumTypeArgument()) {
                    error(String.valueOf(decl.typeName()) + " takes " + original.getNumTypeParameter() + " type parameters, not " + getNumTypeArgument() + " as used in " + this);
                    return;
                }
                ParTypeDecl parTypeDecl = (ParTypeDecl) type();
                for (int i = 0; i < getNumTypeArgument(); i++) {
                    if (!getTypeArgument(i).type().instanceOf(original.getTypeParameter(i))) {
                        error("type argument " + i + " is of type " + getTypeArgument(i).type().typeName() + " which is not a subtype of " + original.getTypeParameter(i).typeName());
                    }
                }
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        getTypeAccess().toString(s);
        s.append("<");
        for (int i = 0; i < getNumTypeArgument(); i++) {
            if (i != 0) {
                s.append(", ");
            }
            getTypeArgument(i).toString(s);
        }
        s.append(">");
    }

    public ParTypeAccess() {
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
        setChild(new List(), 1);
    }

    public ParTypeAccess(Access p0, List<Access> p1) {
        setChild(p0, 0);
        setChild(p1, 1);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setTypeAccess(Access node) {
        setChild(node, 0);
    }

    public Access getTypeAccess() {
        return (Access) getChild(0);
    }

    public Access getTypeAccessNoTransform() {
        return (Access) getChildNoTransform(0);
    }

    public void setTypeArgumentList(List<Access> list) {
        setChild(list, 1);
    }

    public int getNumTypeArgument() {
        return getTypeArgumentList().getNumChild();
    }

    public int getNumTypeArgumentNoTransform() {
        return getTypeArgumentListNoTransform().getNumChildNoTransform();
    }

    public Access getTypeArgument(int i) {
        return getTypeArgumentList().getChild(i);
    }

    public void addTypeArgument(Access node) {
        List<Access> list = (this.parent == null || state == null) ? getTypeArgumentListNoTransform() : getTypeArgumentList();
        list.addChild(node);
    }

    public void addTypeArgumentNoTransform(Access node) {
        List<Access> list = getTypeArgumentListNoTransform();
        list.addChild(node);
    }

    public void setTypeArgument(Access node, int i) {
        List<Access> list = getTypeArgumentList();
        list.setChild(node, i);
    }

    public List<Access> getTypeArguments() {
        return getTypeArgumentList();
    }

    public List<Access> getTypeArgumentsNoTransform() {
        return getTypeArgumentListNoTransform();
    }

    public List<Access> getTypeArgumentList() {
        List<Access> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    public List<Access> getTypeArgumentListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.Access
    public Expr unqualifiedScope() {
        state();
        return getParent() instanceof Access ? ((Access) getParent()).unqualifiedScope() : super.unqualifiedScope();
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr
    public TypeDecl type() {
        if (this.type_computed) {
            return this.type_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.type_value = type_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.type_computed = true;
        }
        return this.type_value;
    }

    private TypeDecl type_compute() {
        TypeDecl typeDecl = genericDecl();
        if (typeDecl instanceof GenericTypeDecl) {
            if (unqualifiedScope().inExtendsOrImplements()) {
                return ((GenericTypeDecl) typeDecl).lookupParTypeDecl(this);
            }
            ArrayList args = new ArrayList();
            for (int i = 0; i < getNumTypeArgument(); i++) {
                args.add(getTypeArgument(i).type());
            }
            return ((GenericTypeDecl) typeDecl).lookupParTypeDecl(args);
        }
        return typeDecl;
    }

    public TypeDecl genericDecl() {
        state();
        return getTypeAccess().type();
    }

    @Override // soot.JastAddJ.Expr
    public boolean isTypeAccess() {
        state();
        return true;
    }

    @Override // soot.JastAddJ.Access
    public Access substituted(Collection<TypeVariable> original, List<TypeVariable> substitution) {
        state();
        List<Access> substArgs = new List<>();
        Iterator<Access> it = getTypeArgumentList().iterator();
        while (it.hasNext()) {
            Access arg = it.next();
            substArgs.add(arg.substituted(original, substitution));
        }
        return new ParTypeAccess(getTypeAccess().substituted(original, substitution), substArgs);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        if (caller == getTypeArgumentListNoTransform()) {
            caller.getIndexOfChild(child);
            return unqualifiedScope().lookupType(name);
        }
        return getParent().Define_SimpleSet_lookupType(this, caller, name);
    }

    @Override // soot.JastAddJ.Access, soot.JastAddJ.Expr, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
