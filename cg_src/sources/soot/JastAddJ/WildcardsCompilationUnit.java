package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.bytebuddy.description.type.TypeDescription;
import soot.JastAddJ.ASTNode;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/WildcardsCompilationUnit.class */
public class WildcardsCompilationUnit extends CompilationUnit implements Cloneable {
    protected boolean typeWildcard_computed = false;
    protected TypeDecl typeWildcard_value;
    protected Map lookupWildcardExtends_TypeDecl_values;
    protected List lookupWildcardExtends_TypeDecl_list;
    protected Map lookupWildcardSuper_TypeDecl_values;
    protected List lookupWildcardSuper_TypeDecl_list;
    protected Map lookupLUBType_Collection_values;
    protected List lookupLUBType_Collection_list;
    protected Map lookupGLBType_ArrayList_values;
    protected List lookupGLBType_ArrayList_list;

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.typeWildcard_computed = false;
        this.typeWildcard_value = null;
        this.lookupWildcardExtends_TypeDecl_values = null;
        this.lookupWildcardExtends_TypeDecl_list = null;
        this.lookupWildcardSuper_TypeDecl_values = null;
        this.lookupWildcardSuper_TypeDecl_list = null;
        this.lookupLUBType_Collection_values = null;
        this.lookupLUBType_Collection_list = null;
        this.lookupGLBType_ArrayList_values = null;
        this.lookupGLBType_ArrayList_list = null;
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode, beaver.Symbol
    public WildcardsCompilationUnit clone() throws CloneNotSupportedException {
        WildcardsCompilationUnit node = (WildcardsCompilationUnit) super.clone();
        node.typeWildcard_computed = false;
        node.typeWildcard_value = null;
        node.lookupWildcardExtends_TypeDecl_values = null;
        node.lookupWildcardExtends_TypeDecl_list = null;
        node.lookupWildcardSuper_TypeDecl_values = null;
        node.lookupWildcardSuper_TypeDecl_list = null;
        node.lookupLUBType_Collection_values = null;
        node.lookupLUBType_Collection_list = null;
        node.lookupGLBType_ArrayList_values = null;
        node.lookupGLBType_ArrayList_list = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            WildcardsCompilationUnit node = clone();
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

    public static LUBType createLUBType(Collection bounds) {
        List boundList = new List();
        StringBuffer name = new StringBuffer();
        Iterator iter = bounds.iterator();
        while (iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) iter.next();
            boundList.add(typeDecl.createBoundAccess());
            name.append("& " + typeDecl.typeName());
        }
        LUBType decl = new LUBType(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))), name.toString(), new List(), boundList);
        return decl;
    }

    public WildcardsCompilationUnit() {
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
        setChild(new List(), 0);
        setChild(new List(), 1);
    }

    public WildcardsCompilationUnit(String p0, List<ImportDecl> p1, List<TypeDecl> p2) {
        setPackageDecl(p0);
        setChild(p1, 0);
        setChild(p2, 1);
    }

    public WildcardsCompilationUnit(Symbol p0, List<ImportDecl> p1, List<TypeDecl> p2) {
        setPackageDecl(p0);
        setChild(p1, 0);
        setChild(p2, 1);
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
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

    public TypeDecl typeWildcard() {
        if (this.typeWildcard_computed) {
            return this.typeWildcard_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.typeWildcard_value = typeWildcard_compute();
        this.typeWildcard_value.setParent(this);
        this.typeWildcard_value.is$Final = true;
        this.typeWildcard_computed = true;
        return this.typeWildcard_value;
    }

    private TypeDecl typeWildcard_compute() {
        TypeDecl decl = new WildcardType(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))), TypeDescription.Generic.OfWildcardType.SYMBOL, new List());
        return decl;
    }

    public TypeDecl lookupWildcardExtends(TypeDecl bound) {
        if (this.lookupWildcardExtends_TypeDecl_values == null) {
            this.lookupWildcardExtends_TypeDecl_values = new HashMap(4);
        }
        if (this.lookupWildcardExtends_TypeDecl_values.containsKey(bound)) {
            return (TypeDecl) this.lookupWildcardExtends_TypeDecl_values.get(bound);
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        TypeDecl lookupWildcardExtends_TypeDecl_value = lookupWildcardExtends_compute(bound);
        if (this.lookupWildcardExtends_TypeDecl_list == null) {
            this.lookupWildcardExtends_TypeDecl_list = new List();
            this.lookupWildcardExtends_TypeDecl_list.is$Final = true;
            this.lookupWildcardExtends_TypeDecl_list.setParent(this);
        }
        this.lookupWildcardExtends_TypeDecl_list.add(lookupWildcardExtends_TypeDecl_value);
        if (lookupWildcardExtends_TypeDecl_value != null) {
            lookupWildcardExtends_TypeDecl_value.is$Final = true;
        }
        this.lookupWildcardExtends_TypeDecl_values.put(bound, lookupWildcardExtends_TypeDecl_value);
        return lookupWildcardExtends_TypeDecl_value;
    }

    private TypeDecl lookupWildcardExtends_compute(TypeDecl bound) {
        TypeDecl decl = new WildcardExtendsType(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))), "? extends " + bound.fullName(), new List(), bound.createBoundAccess());
        return decl;
    }

    public TypeDecl lookupWildcardSuper(TypeDecl bound) {
        if (this.lookupWildcardSuper_TypeDecl_values == null) {
            this.lookupWildcardSuper_TypeDecl_values = new HashMap(4);
        }
        if (this.lookupWildcardSuper_TypeDecl_values.containsKey(bound)) {
            return (TypeDecl) this.lookupWildcardSuper_TypeDecl_values.get(bound);
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        TypeDecl lookupWildcardSuper_TypeDecl_value = lookupWildcardSuper_compute(bound);
        if (this.lookupWildcardSuper_TypeDecl_list == null) {
            this.lookupWildcardSuper_TypeDecl_list = new List();
            this.lookupWildcardSuper_TypeDecl_list.is$Final = true;
            this.lookupWildcardSuper_TypeDecl_list.setParent(this);
        }
        this.lookupWildcardSuper_TypeDecl_list.add(lookupWildcardSuper_TypeDecl_value);
        if (lookupWildcardSuper_TypeDecl_value != null) {
            lookupWildcardSuper_TypeDecl_value.is$Final = true;
        }
        this.lookupWildcardSuper_TypeDecl_values.put(bound, lookupWildcardSuper_TypeDecl_value);
        return lookupWildcardSuper_TypeDecl_value;
    }

    private TypeDecl lookupWildcardSuper_compute(TypeDecl bound) {
        TypeDecl decl = new WildcardSuperType(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))), "? super " + bound.fullName(), new List(), bound.createBoundAccess());
        return decl;
    }

    public LUBType lookupLUBType(Collection bounds) {
        if (this.lookupLUBType_Collection_values == null) {
            this.lookupLUBType_Collection_values = new HashMap(4);
        }
        if (this.lookupLUBType_Collection_values.containsKey(bounds)) {
            return (LUBType) this.lookupLUBType_Collection_values.get(bounds);
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        LUBType lookupLUBType_Collection_value = lookupLUBType_compute(bounds);
        if (this.lookupLUBType_Collection_list == null) {
            this.lookupLUBType_Collection_list = new List();
            this.lookupLUBType_Collection_list.is$Final = true;
            this.lookupLUBType_Collection_list.setParent(this);
        }
        this.lookupLUBType_Collection_list.add(lookupLUBType_Collection_value);
        if (lookupLUBType_Collection_value != null) {
            lookupLUBType_Collection_value.is$Final = true;
        }
        this.lookupLUBType_Collection_values.put(bounds, lookupLUBType_Collection_value);
        return lookupLUBType_Collection_value;
    }

    private LUBType lookupLUBType_compute(Collection bounds) {
        return createLUBType(bounds);
    }

    public GLBType lookupGLBType(ArrayList bounds) {
        if (this.lookupGLBType_ArrayList_values == null) {
            this.lookupGLBType_ArrayList_values = new HashMap(4);
        }
        if (this.lookupGLBType_ArrayList_values.containsKey(bounds)) {
            return (GLBType) this.lookupGLBType_ArrayList_values.get(bounds);
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        GLBType lookupGLBType_ArrayList_value = lookupGLBType_compute(bounds);
        if (this.lookupGLBType_ArrayList_list == null) {
            this.lookupGLBType_ArrayList_list = new List();
            this.lookupGLBType_ArrayList_list.is$Final = true;
            this.lookupGLBType_ArrayList_list.setParent(this);
        }
        this.lookupGLBType_ArrayList_list.add(lookupGLBType_ArrayList_value);
        if (lookupGLBType_ArrayList_value != null) {
            lookupGLBType_ArrayList_value.is$Final = true;
        }
        this.lookupGLBType_ArrayList_values.put(bounds, lookupGLBType_ArrayList_value);
        return lookupGLBType_ArrayList_value;
    }

    private GLBType lookupGLBType_compute(ArrayList bounds) {
        List boundList = new List();
        StringBuffer name = new StringBuffer();
        Iterator iter = bounds.iterator();
        while (iter.hasNext()) {
            TypeDecl typeDecl = (TypeDecl) iter.next();
            boundList.add(typeDecl.createBoundAccess());
            name.append("& " + typeDecl.typeName());
        }
        GLBType decl = new GLBType(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))), name.toString(), new List(), boundList);
        return decl;
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
