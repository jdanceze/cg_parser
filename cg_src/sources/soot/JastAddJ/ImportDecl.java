package soot.JastAddJ;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ImportDecl.class */
public abstract class ImportDecl extends ASTNode<ASTNode> implements Cloneable {
    protected Map importedTypes_String_values;
    protected Map importedFields_String_values;
    protected Map importedMethods_String_values;

    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.importedTypes_String_values = null;
        this.importedFields_String_values = null;
        this.importedMethods_String_values = null;
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public ImportDecl clone() throws CloneNotSupportedException {
        ImportDecl node = (ImportDecl) super.mo287clone();
        node.importedTypes_String_values = null;
        node.importedFields_String_values = null;
        node.importedMethods_String_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    public ImportDecl() {
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public ImportDecl(Access p0) {
        setChild(p0, 0);
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setAccess(Access node) {
        setChild(node, 0);
    }

    public Access getAccess() {
        return (Access) getChild(0);
    }

    public Access getAccessNoTransform() {
        return (Access) getChildNoTransform(0);
    }

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
        return SimpleSet.emptySet;
    }

    public boolean isOnDemand() {
        state();
        return false;
    }

    public String typeName() {
        state();
        Access a = getAccess().lastAccess();
        String name = a.isTypeAccess() ? ((TypeAccess) a).nameWithPackage() : "";
        while (a.hasPrevExpr() && (a.prevExpr() instanceof Access)) {
            Access pred = (Access) a.prevExpr();
            if (pred.isTypeAccess()) {
                name = String.valueOf(((TypeAccess) pred).nameWithPackage()) + "." + name;
            }
            a = pred;
        }
        return name;
    }

    public SimpleSet importedFields(String name) {
        if (this.importedFields_String_values == null) {
            this.importedFields_String_values = new HashMap(4);
        }
        if (this.importedFields_String_values.containsKey(name)) {
            return (SimpleSet) this.importedFields_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet importedFields_String_value = importedFields_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.importedFields_String_values.put(name, importedFields_String_value);
        }
        return importedFields_String_value;
    }

    private SimpleSet importedFields_compute(String name) {
        return SimpleSet.emptySet;
    }

    public Collection importedMethods(String name) {
        if (this.importedMethods_String_values == null) {
            this.importedMethods_String_values = new HashMap(4);
        }
        if (this.importedMethods_String_values.containsKey(name)) {
            return (Collection) this.importedMethods_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        Collection importedMethods_String_value = importedMethods_compute(name);
        if (isFinal && num == state().boundariesCrossed) {
            this.importedMethods_String_values.put(name, importedMethods_String_value);
        }
        return importedMethods_String_value;
    }

    private Collection importedMethods_compute(String name) {
        return Collections.EMPTY_LIST;
    }

    public String packageName() {
        state();
        String packageName_value = getParent().Define_String_packageName(this, null);
        return packageName_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
        if (caller == getAccessNoTransform()) {
            return false;
        }
        return getParent().Define_boolean_isDest(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        if (caller == getAccessNoTransform()) {
            return true;
        }
        return getParent().Define_boolean_isSource(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
