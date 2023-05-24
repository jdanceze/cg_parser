package soot.JastAddJ;

import java.util.Collection;
import soot.tagkit.AnnotationBooleanElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationDoubleElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationFloatElem;
import soot.tagkit.AnnotationIntElem;
import soot.tagkit.AnnotationLongElem;
import soot.tagkit.AnnotationStringElem;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ElementConstantValue.class */
public class ElementConstantValue extends ElementValue implements Cloneable {
    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode, beaver.Symbol
    public ElementConstantValue clone() throws CloneNotSupportedException {
        ElementConstantValue node = (ElementConstantValue) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ElementConstantValue node = clone();
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
    public void nameCheck() {
        Variable v;
        if (enclosingAnnotationDecl().fullName().equals("java.lang.annotation.Target") && (v = getExpr().varDecl()) != null && v.hostType().fullName().equals("java.lang.annotation.ElementType") && lookupElementTypeValue(v.name()) != this) {
            error("repeated annotation target");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        getExpr().toString(s);
    }

    @Override // soot.JastAddJ.ElementValue
    public void appendAsAttributeTo(Collection list, String name) {
        if (getExpr().isConstant() && !getExpr().type().isEnumDecl()) {
            char kind = getExpr().type().isString() ? 's' : getExpr().type().typeDescriptor().charAt(0);
            TypeDecl type = getExpr().type();
            if (type.isLong()) {
                list.add(new AnnotationLongElem(getExpr().constant().longValue(), kind, name));
            } else if (type.isDouble()) {
                list.add(new AnnotationDoubleElem(getExpr().constant().doubleValue(), kind, name));
            } else if (type.isFloat()) {
                list.add(new AnnotationFloatElem(getExpr().constant().floatValue(), kind, name));
            } else if (type.isString()) {
                list.add(new AnnotationStringElem(getExpr().constant().stringValue(), kind, name));
            } else if (type.isIntegralType()) {
                list.add(new AnnotationIntElem(getExpr().constant().intValue(), kind, name));
            } else if (type().isBoolean()) {
                list.add(new AnnotationBooleanElem(getExpr().constant().booleanValue(), kind, name));
            } else {
                throw new UnsupportedOperationException("Unsupported attribute constant type " + type.typeName());
            }
        } else if (getExpr().isClassAccess()) {
            list.add(new AnnotationClassElem(getExpr().type().typeDescriptor(), 'c', name));
        } else {
            Variable v = getExpr().varDecl();
            if (v == null) {
                throw new Error("Expected Enumeration constant");
            }
            list.add(new AnnotationEnumElem(v.type().typeDescriptor(), v.name(), 'e', name));
        }
    }

    public ElementConstantValue() {
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public ElementConstantValue(Expr p0) {
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

    public void setExpr(Expr node) {
        setChild(node, 0);
    }

    public Expr getExpr() {
        return (Expr) getChild(0);
    }

    public Expr getExprNoTransform() {
        return (Expr) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ElementValue
    public boolean validTarget(Annotation a) {
        state();
        Variable v = getExpr().varDecl();
        if (v == null) {
            return true;
        }
        return v.hostType().fullName().equals("java.lang.annotation.ElementType") && a.mayUseAnnotationTarget(v.name());
    }

    @Override // soot.JastAddJ.ElementValue
    public ElementValue definesElementTypeValue(String name) {
        state();
        Variable v = getExpr().varDecl();
        if (v != null && v.hostType().fullName().equals("java.lang.annotation.ElementType") && v.name().equals(name)) {
            return this;
        }
        return null;
    }

    @Override // soot.JastAddJ.ElementValue
    public boolean hasValue(String s) {
        state();
        return getExpr().type().isString() && getExpr().isConstant() && getExpr().constant().stringValue().equals(s);
    }

    @Override // soot.JastAddJ.ElementValue
    public boolean commensurateWithTypeDecl(TypeDecl type) {
        state();
        Expr v = getExpr();
        if (!v.type().assignConversionTo(type, v)) {
            return false;
        }
        if (((type.isPrimitive() || type.isString()) && !v.isConstant()) || v.type().isNull()) {
            return false;
        }
        if (type.fullName().equals("java.lang.Class") && !v.isClassAccess()) {
            return false;
        }
        if (type.isEnumDecl()) {
            if (v.varDecl() == null || !(v.varDecl() instanceof EnumConstant)) {
                return false;
            }
            return true;
        }
        return true;
    }

    @Override // soot.JastAddJ.ElementValue
    public TypeDecl type() {
        state();
        return getExpr().type();
    }

    public ElementValue lookupElementTypeValue(String name) {
        state();
        ElementValue lookupElementTypeValue_String_value = getParent().Define_ElementValue_lookupElementTypeValue(this, null, name);
        return lookupElementTypeValue_String_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getExprNoTransform()) {
            return NameType.AMBIGUOUS_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public String Define_String_methodHost(ASTNode caller, ASTNode child) {
        if (caller == getExprNoTransform()) {
            return enclosingAnnotationDecl().typeName();
        }
        return getParent().Define_String_methodHost(this, caller);
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
