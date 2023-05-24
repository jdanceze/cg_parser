package soot.JastAddJ;

import java.util.ArrayList;
import java.util.Collection;
import soot.tagkit.AnnotationArrayElem;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ElementArrayValue.class */
public class ElementArrayValue extends ElementValue implements Cloneable {
    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode, beaver.Symbol
    public ElementArrayValue clone() throws CloneNotSupportedException {
        ElementArrayValue node = (ElementArrayValue) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            ElementArrayValue node = clone();
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
        s.append("{");
        for (int i = 0; i < getNumElementValue(); i++) {
            getElementValue(i).toString(s);
            s.append(", ");
        }
        s.append("}");
    }

    @Override // soot.JastAddJ.ElementValue
    public void appendAsAttributeTo(Collection list, String name) {
        ArrayList elemVals = new ArrayList();
        for (int i = 0; i < getNumElementValue(); i++) {
            getElementValue(i).appendAsAttributeTo(elemVals, "default");
        }
        list.add(new AnnotationArrayElem(elemVals, '[', name));
    }

    public ElementArrayValue() {
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
        setChild(new List(), 0);
    }

    public ElementArrayValue(List<ElementValue> p0) {
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

    public void setElementValueList(List<ElementValue> list) {
        setChild(list, 0);
    }

    public int getNumElementValue() {
        return getElementValueList().getNumChild();
    }

    public int getNumElementValueNoTransform() {
        return getElementValueListNoTransform().getNumChildNoTransform();
    }

    public ElementValue getElementValue(int i) {
        return getElementValueList().getChild(i);
    }

    public void addElementValue(ElementValue node) {
        List<ElementValue> list = (this.parent == null || state == null) ? getElementValueListNoTransform() : getElementValueList();
        list.addChild(node);
    }

    public void addElementValueNoTransform(ElementValue node) {
        List<ElementValue> list = getElementValueListNoTransform();
        list.addChild(node);
    }

    public void setElementValue(ElementValue node, int i) {
        List<ElementValue> list = getElementValueList();
        list.setChild(node, i);
    }

    public List<ElementValue> getElementValues() {
        return getElementValueList();
    }

    public List<ElementValue> getElementValuesNoTransform() {
        return getElementValueListNoTransform();
    }

    public List<ElementValue> getElementValueList() {
        List<ElementValue> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    public List<ElementValue> getElementValueListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ElementValue
    public boolean validTarget(Annotation a) {
        state();
        for (int i = 0; i < getNumElementValue(); i++) {
            if (getElementValue(i).validTarget(a)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.ElementValue
    public ElementValue definesElementTypeValue(String name) {
        state();
        for (int i = 0; i < getNumElementValue(); i++) {
            if (getElementValue(i).definesElementTypeValue(name) != null) {
                return getElementValue(i).definesElementTypeValue(name);
            }
        }
        return null;
    }

    @Override // soot.JastAddJ.ElementValue
    public boolean hasValue(String s) {
        state();
        for (int i = 0; i < getNumElementValue(); i++) {
            if (getElementValue(i).hasValue(s)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.JastAddJ.ElementValue
    public boolean commensurateWithArrayDecl(ArrayDecl type) {
        state();
        for (int i = 0; i < getNumElementValue(); i++) {
            if (!type.componentType().commensurateWith(getElementValue(i))) {
                return false;
            }
        }
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public ElementValue Define_ElementValue_lookupElementTypeValue(ASTNode caller, ASTNode child, String name) {
        if (caller == getElementValueListNoTransform()) {
            caller.getIndexOfChild(child);
            return definesElementTypeValue(name);
        }
        return getParent().Define_ElementValue_lookupElementTypeValue(this, caller, name);
    }

    @Override // soot.JastAddJ.ElementValue, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
