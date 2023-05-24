package soot.JastAddJ;

import beaver.Symbol;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ElementValuePair.class */
public class ElementValuePair extends ASTNode<ASTNode> implements Cloneable {
    protected String tokenString_Name;
    public int Namestart;
    public int Nameend;
    protected boolean type_computed = false;
    protected TypeDecl type_value;

    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.type_computed = false;
        this.type_value = null;
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public ElementValuePair clone() throws CloneNotSupportedException {
        ElementValuePair node = (ElementValuePair) super.mo287clone();
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
            ElementValuePair node = clone();
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
    public void typeCheck() {
        if (!type().commensurateWith(getElementValue())) {
            error("can not construct annotation with " + getName() + " = " + getElementValue().toString() + "; " + type().typeName() + " is not commensurate with " + getElementValue().type().typeName());
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append(String.valueOf(getName()) + " = ");
        getElementValue().toString(s);
    }

    public ElementValuePair() {
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public ElementValuePair(String p0, ElementValue p1) {
        setName(p0);
        setChild(p1, 0);
    }

    public ElementValuePair(Symbol p0, ElementValue p1) {
        setName(p0);
        setChild(p1, 0);
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return true;
    }

    public void setName(String value) {
        this.tokenString_Name = value;
    }

    public void setName(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setName is only valid for String lexemes");
        }
        this.tokenString_Name = (String) symbol.value;
        this.Namestart = symbol.getStart();
        this.Nameend = symbol.getEnd();
    }

    public String getName() {
        return this.tokenString_Name != null ? this.tokenString_Name : "";
    }

    public void setElementValue(ElementValue node) {
        setChild(node, 0);
    }

    public ElementValue getElementValue() {
        return (ElementValue) getChild(0);
    }

    public ElementValue getElementValueNoTransform() {
        return (ElementValue) getChildNoTransform(0);
    }

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
        Map methodMap = enclosingAnnotationDecl().localMethodsSignatureMap();
        MethodDecl method = (MethodDecl) methodMap.get(String.valueOf(getName()) + "()");
        if (method != null) {
            return method.type();
        }
        return unknownType();
    }

    public TypeDecl unknownType() {
        state();
        TypeDecl unknownType_value = getParent().Define_TypeDecl_unknownType(this, null);
        return unknownType_value;
    }

    public TypeDecl enclosingAnnotationDecl() {
        state();
        TypeDecl enclosingAnnotationDecl_value = getParent().Define_TypeDecl_enclosingAnnotationDecl(this, null);
        return enclosingAnnotationDecl_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        if (type().isArrayDecl() && (getElementValue() instanceof ElementConstantValue)) {
            state().duringAnnotations++;
            ASTNode result = rewriteRule0();
            state().duringAnnotations--;
            return result;
        }
        return super.rewriteTo();
    }

    private ElementValuePair rewriteRule0() {
        setElementValue(new ElementArrayValue(new List().add(getElementValue())));
        return this;
    }
}
