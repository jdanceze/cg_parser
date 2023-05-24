package soot.JastAddJ;

import java.util.Collection;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ElementValue.class */
public abstract class ElementValue extends ASTNode<ASTNode> implements Cloneable {
    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public ElementValue clone() throws CloneNotSupportedException {
        ElementValue node = (ElementValue) super.mo287clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    public void appendAsAttributeTo(Collection list, String name) {
        throw new Error(String.valueOf(getClass().getName()) + " does not support appendAsAttributeTo(Attribute buf)");
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 0;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public boolean validTarget(Annotation a) {
        state();
        return false;
    }

    public ElementValue definesElementTypeValue(String name) {
        state();
        return null;
    }

    public boolean hasValue(String s) {
        state();
        return false;
    }

    public boolean commensurateWithTypeDecl(TypeDecl type) {
        state();
        return false;
    }

    public boolean commensurateWithArrayDecl(ArrayDecl type) {
        state();
        return type.componentType().commensurateWith(this);
    }

    public TypeDecl type() {
        state();
        return unknownType();
    }

    public TypeDecl enclosingAnnotationDecl() {
        state();
        TypeDecl enclosingAnnotationDecl_value = getParent().Define_TypeDecl_enclosingAnnotationDecl(this, null);
        return enclosingAnnotationDecl_value;
    }

    public TypeDecl unknownType() {
        state();
        TypeDecl unknownType_value = getParent().Define_TypeDecl_unknownType(this, null);
        return unknownType_value;
    }

    public TypeDecl hostType() {
        state();
        TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);
        return hostType_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
