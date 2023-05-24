package soot.JastAddJ;

import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/SingleTypeImportDecl.class */
public class SingleTypeImportDecl extends ImportDecl implements Cloneable {
    protected Map importedTypes_String_values;

    @Override // soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.importedTypes_String_values = null;
    }

    @Override // soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public SingleTypeImportDecl clone() throws CloneNotSupportedException {
        SingleTypeImportDecl node = (SingleTypeImportDecl) super.clone();
        node.importedTypes_String_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            SingleTypeImportDecl node = clone();
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
        if (!getAccess().type().typeName().equals(typeName()) && !getAccess().type().isUnknown()) {
            error("Single-type import " + typeName() + " is not the canonical name of type " + getAccess().type().typeName());
        } else if (allImportedTypes(getAccess().type().name()).size() > 1) {
            error(String.valueOf(getAccess().type().name()) + " is imported multiple times");
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        s.append("import ");
        getAccess().toString(s);
        s.append(";\n");
    }

    public SingleTypeImportDecl() {
    }

    @Override // soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public SingleTypeImportDecl(Access p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.ImportDecl
    public void setAccess(Access node) {
        setChild(node, 0);
    }

    @Override // soot.JastAddJ.ImportDecl
    public Access getAccess() {
        return (Access) getChild(0);
    }

    @Override // soot.JastAddJ.ImportDecl
    public Access getAccessNoTransform() {
        return (Access) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.ImportDecl
    public SimpleSet importedTypes(String name) {
        if (this.importedTypes_String_values == null) {
            this.importedTypes_String_values = new HashMap(4);
        }
        if (this.importedTypes_String_values.containsKey(name)) {
            return (SimpleSet) this.importedTypes_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet importedTypes_String_value = importedTypes_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.importedTypes_String_values.put(name, importedTypes_String_value);
        }
        return importedTypes_String_value;
    }

    private SimpleSet importedTypes_compute(String name) {
        SimpleSet set = SimpleSet.emptySet;
        if (getAccess().type().name().equals(name)) {
            set = set.add(getAccess().type());
        }
        return set;
    }

    public SimpleSet allImportedTypes(String name) {
        state();
        SimpleSet allImportedTypes_String_value = getParent().Define_SimpleSet_allImportedTypes(this, null, name);
        return allImportedTypes_String_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getAccessNoTransform()) {
            return NameType.TYPE_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
