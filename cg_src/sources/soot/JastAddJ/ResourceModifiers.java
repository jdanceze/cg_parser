package soot.JastAddJ;

import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ResourceModifiers.class */
public class ResourceModifiers extends Modifiers implements Cloneable {
    protected boolean isFinal_computed = false;
    protected boolean isFinal_value;

    @Override // soot.JastAddJ.Modifiers, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.isFinal_computed = false;
    }

    @Override // soot.JastAddJ.Modifiers, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.Modifiers, soot.JastAddJ.ASTNode, beaver.Symbol
    public ResourceModifiers clone() throws CloneNotSupportedException {
        ResourceModifiers node = (ResourceModifiers) super.clone();
        node.isFinal_computed = false;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.Modifiers, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            ResourceModifiers node = clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.Modifiers, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> fullCopy() {
        ASTNode<ASTNode> copy = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    copy.setChild(child.fullCopy(), i);
                }
            }
        }
        return copy;
    }

    public ResourceModifiers() {
    }

    @Override // soot.JastAddJ.Modifiers, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
        setChild(new List(), 0);
    }

    public ResourceModifiers(List<Modifier> p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.Modifiers, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.Modifiers, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.Modifiers
    public void setModifierList(List<Modifier> list) {
        setChild(list, 0);
    }

    @Override // soot.JastAddJ.Modifiers
    public int getNumModifier() {
        return getModifierList().getNumChild();
    }

    @Override // soot.JastAddJ.Modifiers
    public int getNumModifierNoTransform() {
        return getModifierListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.Modifiers
    public Modifier getModifier(int i) {
        return getModifierList().getChild(i);
    }

    @Override // soot.JastAddJ.Modifiers
    public void addModifier(Modifier node) {
        List<Modifier> list = (this.parent == null || state == null) ? getModifierListNoTransform() : getModifierList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.Modifiers
    public void addModifierNoTransform(Modifier node) {
        List<Modifier> list = getModifierListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.Modifiers
    public void setModifier(Modifier node, int i) {
        List<Modifier> list = getModifierList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.Modifiers
    public List<Modifier> getModifiers() {
        return getModifierList();
    }

    @Override // soot.JastAddJ.Modifiers
    public List<Modifier> getModifiersNoTransform() {
        return getModifierListNoTransform();
    }

    @Override // soot.JastAddJ.Modifiers
    public List<Modifier> getModifierList() {
        List<Modifier> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.Modifiers
    public List<Modifier> getModifierListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.Modifiers
    public boolean isFinal() {
        if (this.isFinal_computed) {
            return this.isFinal_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.isFinal_value = isFinal_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.isFinal_computed = true;
        }
        return this.isFinal_value;
    }

    private boolean isFinal_compute() {
        return true;
    }

    @Override // soot.JastAddJ.Modifiers, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
