package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationTag;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ElementAnnotationValue.class */
public class ElementAnnotationValue extends ElementValue implements Cloneable {
    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode, beaver.Symbol
    public ElementAnnotationValue clone() throws CloneNotSupportedException {
        ElementAnnotationValue node = (ElementAnnotationValue) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ElementAnnotationValue node = clone();
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

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        getAnnotation().toString(s);
    }

    @Override // soot.JastAddJ.ElementValue
    public void appendAsAttributeTo(Collection list, String name) {
        ArrayList elemVals = new ArrayList();
        getAnnotation().appendAsAttributeTo(elemVals);
        list.add(new AnnotationAnnotationElem((AnnotationTag) elemVals.get(0), '@', name));
    }

    public ElementAnnotationValue() {
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public ElementAnnotationValue(Annotation p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setAnnotation(Annotation node) {
        setChild(node, 0);
    }

    public Annotation getAnnotation() {
        return (Annotation) getChild(0);
    }

    public Annotation getAnnotationNoTransform() {
        return (Annotation) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ElementValue
    public boolean commensurateWithTypeDecl(TypeDecl type) {
        state();
        return type() == type;
    }

    @Override // soot.JastAddJ.ElementValue
    public TypeDecl type() {
        state();
        return getAnnotation().type();
    }

    public Annotation lookupAnnotation(TypeDecl typeDecl) {
        state();
        Annotation lookupAnnotation_TypeDecl_value = getParent().Define_Annotation_lookupAnnotation(this, null, typeDecl);
        return lookupAnnotation_TypeDecl_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
        if (caller == getAnnotationNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public Annotation Define_Annotation_lookupAnnotation(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
        if (caller == getAnnotationNoTransform()) {
            return getAnnotation().type() == typeDecl ? getAnnotation() : lookupAnnotation(typeDecl);
        }
        return getParent().Define_Annotation_lookupAnnotation(this, caller, typeDecl);
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
