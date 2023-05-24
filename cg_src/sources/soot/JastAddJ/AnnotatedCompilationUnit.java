package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Iterator;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/AnnotatedCompilationUnit.class */
public class AnnotatedCompilationUnit extends CompilationUnit implements Cloneable {
    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode, beaver.Symbol
    public AnnotatedCompilationUnit clone() throws CloneNotSupportedException {
        AnnotatedCompilationUnit node = (AnnotatedCompilationUnit) super.clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            AnnotatedCompilationUnit node = clone();
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
    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
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

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public void nameCheck() {
        super.nameCheck();
        if (!relativeName().endsWith("package-info.java")) {
            error("package annotations should be in a file package-info.java");
        }
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        getModifiers().toString(s);
        super.toString(s);
    }

    @Override // soot.JastAddJ.ASTNode
    public void jimplify1phase2() {
        super.jimplify1phase2();
        ArrayList c = new ArrayList();
        getModifiers().addAllAnnotations(c);
        Iterator iter = c.iterator();
        while (iter.hasNext()) {
            Tag tag = (Tag) iter.next();
        }
    }

    public AnnotatedCompilationUnit() {
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[3];
        setChild(new List(), 0);
        setChild(new List(), 1);
    }

    public AnnotatedCompilationUnit(String p0, List<ImportDecl> p1, List<TypeDecl> p2, Modifiers p3) {
        setPackageDecl(p0);
        setChild(p1, 0);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    public AnnotatedCompilationUnit(Symbol p0, List<ImportDecl> p1, List<TypeDecl> p2, Modifiers p3) {
        setPackageDecl(p0);
        setChild(p1, 0);
        setChild(p2, 1);
        setChild(p3, 2);
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 3;
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    @Override // soot.JastAddJ.CompilationUnit
    public void setPackageDecl(String value) {
        this.tokenjava_lang_String_PackageDecl = value;
    }

    @Override // soot.JastAddJ.CompilationUnit
    public void setPackageDecl(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setPackageDecl is only valid for String lexemes");
        }
        this.tokenjava_lang_String_PackageDecl = (String) symbol.value;
        this.PackageDeclstart = symbol.getStart();
        this.PackageDeclend = symbol.getEnd();
    }

    @Override // soot.JastAddJ.CompilationUnit
    public String getPackageDecl() {
        return this.tokenjava_lang_String_PackageDecl != null ? this.tokenjava_lang_String_PackageDecl : "";
    }

    @Override // soot.JastAddJ.CompilationUnit
    public void setImportDeclList(List<ImportDecl> list) {
        setChild(list, 0);
    }

    @Override // soot.JastAddJ.CompilationUnit
    public int getNumImportDecl() {
        return getImportDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.CompilationUnit
    public int getNumImportDeclNoTransform() {
        return getImportDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.CompilationUnit
    public ImportDecl getImportDecl(int i) {
        return getImportDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.CompilationUnit
    public void addImportDecl(ImportDecl node) {
        List<ImportDecl> list = (this.parent == null || state == null) ? getImportDeclListNoTransform() : getImportDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.CompilationUnit
    public void addImportDeclNoTransform(ImportDecl node) {
        List<ImportDecl> list = getImportDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.CompilationUnit
    public void setImportDecl(ImportDecl node, int i) {
        List<ImportDecl> list = getImportDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.CompilationUnit
    public List<ImportDecl> getImportDecls() {
        return getImportDeclList();
    }

    @Override // soot.JastAddJ.CompilationUnit
    public List<ImportDecl> getImportDeclsNoTransform() {
        return getImportDeclListNoTransform();
    }

    @Override // soot.JastAddJ.CompilationUnit
    public List<ImportDecl> getImportDeclList() {
        List<ImportDecl> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.CompilationUnit
    public List<ImportDecl> getImportDeclListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    @Override // soot.JastAddJ.CompilationUnit
    public void setTypeDeclList(List<TypeDecl> list) {
        setChild(list, 1);
    }

    @Override // soot.JastAddJ.CompilationUnit
    public int getNumTypeDecl() {
        return getTypeDeclList().getNumChild();
    }

    @Override // soot.JastAddJ.CompilationUnit
    public int getNumTypeDeclNoTransform() {
        return getTypeDeclListNoTransform().getNumChildNoTransform();
    }

    @Override // soot.JastAddJ.CompilationUnit
    public TypeDecl getTypeDecl(int i) {
        return getTypeDeclList().getChild(i);
    }

    @Override // soot.JastAddJ.CompilationUnit
    public void addTypeDecl(TypeDecl node) {
        List<TypeDecl> list = (this.parent == null || state == null) ? getTypeDeclListNoTransform() : getTypeDeclList();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.CompilationUnit
    public void addTypeDeclNoTransform(TypeDecl node) {
        List<TypeDecl> list = getTypeDeclListNoTransform();
        list.addChild(node);
    }

    @Override // soot.JastAddJ.CompilationUnit
    public void setTypeDecl(TypeDecl node, int i) {
        List<TypeDecl> list = getTypeDeclList();
        list.setChild(node, i);
    }

    @Override // soot.JastAddJ.CompilationUnit
    public List<TypeDecl> getTypeDecls() {
        return getTypeDeclList();
    }

    @Override // soot.JastAddJ.CompilationUnit
    public List<TypeDecl> getTypeDeclsNoTransform() {
        return getTypeDeclListNoTransform();
    }

    @Override // soot.JastAddJ.CompilationUnit
    public List<TypeDecl> getTypeDeclList() {
        List<TypeDecl> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    @Override // soot.JastAddJ.CompilationUnit
    public List<TypeDecl> getTypeDeclListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    public void setModifiers(Modifiers node) {
        setChild(node, 2);
    }

    public Modifiers getModifiers() {
        return (Modifiers) getChild(2);
    }

    public Modifiers getModifiersNoTransform() {
        return (Modifiers) getChildNoTransform(2);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
        if (caller == getModifiersNoTransform()) {
            return name.equals("PACKAGE");
        }
        return getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public String Define_String_hostPackage(ASTNode caller, ASTNode child) {
        if (caller == getModifiersNoTransform()) {
            return packageName();
        }
        return super.Define_String_hostPackage(caller, child);
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
