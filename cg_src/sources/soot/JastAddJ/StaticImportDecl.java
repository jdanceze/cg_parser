package soot.JastAddJ;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/StaticImportDecl.class */
public abstract class StaticImportDecl extends ImportDecl implements Cloneable {
    protected Map importedTypes_String_values;
    protected Map importedFields_String_values;
    protected Map importedMethods_String_values;

    public abstract TypeDecl type();

    @Override // soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.importedTypes_String_values = null;
        this.importedFields_String_values = null;
        this.importedMethods_String_values = null;
    }

    @Override // soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode, beaver.Symbol
    public StaticImportDecl clone() throws CloneNotSupportedException {
        StaticImportDecl node = (StaticImportDecl) super.clone();
        node.importedTypes_String_values = null;
        node.importedFields_String_values = null;
        node.importedMethods_String_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    public StaticImportDecl() {
    }

    @Override // soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
    }

    public StaticImportDecl(Access p0) {
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
        for (TypeDecl decl : type().memberTypes(name)) {
            if (decl.isStatic() && decl.accessibleFromPackage(packageName())) {
                set = set.add(decl);
            }
        }
        return set;
    }

    @Override // soot.JastAddJ.ImportDecl
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
        SimpleSet set = SimpleSet.emptySet;
        for (FieldDeclaration decl : type().memberFields(name)) {
            if (decl.isStatic() && (decl.isPublic() || (!decl.isPrivate() && decl.hostType().topLevelType().packageName().equals(packageName())))) {
                set = set.add(decl);
            }
        }
        return set;
    }

    @Override // soot.JastAddJ.ImportDecl
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
        Collection set = new HashSet();
        for (MethodDecl decl : type().memberMethods(name)) {
            if (decl.isStatic() && (decl.isPublic() || (!decl.isPrivate() && decl.hostType().topLevelType().packageName().equals(packageName())))) {
                set.add(decl);
            }
        }
        return set;
    }

    @Override // soot.JastAddJ.ImportDecl, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
