package soot.JastAddJ;

import beaver.Symbol;
import soot.JastAddJ.ASTNode;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/PrimitiveCompilationUnit.class */
public class PrimitiveCompilationUnit extends CompilationUnit implements Cloneable {
    protected TypeDecl typeBoolean_value;
    protected TypeDecl typeByte_value;
    protected TypeDecl typeShort_value;
    protected TypeDecl typeChar_value;
    protected TypeDecl typeInt_value;
    protected TypeDecl typeLong_value;
    protected TypeDecl typeFloat_value;
    protected TypeDecl typeDouble_value;
    protected TypeDecl typeVoid_value;
    protected TypeDecl typeNull_value;
    protected TypeDecl unknownType_value;
    protected boolean typeBoolean_computed = false;
    protected boolean typeByte_computed = false;
    protected boolean typeShort_computed = false;
    protected boolean typeChar_computed = false;
    protected boolean typeInt_computed = false;
    protected boolean typeLong_computed = false;
    protected boolean typeFloat_computed = false;
    protected boolean typeDouble_computed = false;
    protected boolean typeVoid_computed = false;
    protected boolean typeNull_computed = false;
    protected boolean unknownType_computed = false;

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.typeBoolean_computed = false;
        this.typeBoolean_value = null;
        this.typeByte_computed = false;
        this.typeByte_value = null;
        this.typeShort_computed = false;
        this.typeShort_value = null;
        this.typeChar_computed = false;
        this.typeChar_value = null;
        this.typeInt_computed = false;
        this.typeInt_value = null;
        this.typeLong_computed = false;
        this.typeLong_value = null;
        this.typeFloat_computed = false;
        this.typeFloat_value = null;
        this.typeDouble_computed = false;
        this.typeDouble_value = null;
        this.typeVoid_computed = false;
        this.typeVoid_value = null;
        this.typeNull_computed = false;
        this.typeNull_value = null;
        this.unknownType_computed = false;
        this.unknownType_value = null;
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode, beaver.Symbol
    public PrimitiveCompilationUnit clone() throws CloneNotSupportedException {
        PrimitiveCompilationUnit node = (PrimitiveCompilationUnit) super.clone();
        node.typeBoolean_computed = false;
        node.typeBoolean_value = null;
        node.typeByte_computed = false;
        node.typeByte_value = null;
        node.typeShort_computed = false;
        node.typeShort_value = null;
        node.typeChar_computed = false;
        node.typeChar_value = null;
        node.typeInt_computed = false;
        node.typeInt_value = null;
        node.typeLong_computed = false;
        node.typeLong_value = null;
        node.typeFloat_computed = false;
        node.typeFloat_value = null;
        node.typeDouble_computed = false;
        node.typeDouble_value = null;
        node.typeVoid_computed = false;
        node.typeVoid_value = null;
        node.typeNull_computed = false;
        node.typeNull_value = null;
        node.unknownType_computed = false;
        node.unknownType_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            PrimitiveCompilationUnit node = clone();
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

    public PrimitiveCompilationUnit() {
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
        setChild(new List(), 0);
        setChild(new List(), 1);
    }

    public PrimitiveCompilationUnit(String p0, List<ImportDecl> p1, List<TypeDecl> p2) {
        setPackageDecl(p0);
        setChild(p1, 0);
        setChild(p2, 1);
    }

    public PrimitiveCompilationUnit(Symbol p0, List<ImportDecl> p1, List<TypeDecl> p2) {
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

    public TypeDecl typeBoolean() {
        if (this.typeBoolean_computed) {
            return this.typeBoolean_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.typeBoolean_value = typeBoolean_compute();
        this.typeBoolean_value.setParent(this);
        this.typeBoolean_value.is$Final = true;
        this.typeBoolean_computed = true;
        return this.typeBoolean_value;
    }

    private TypeDecl typeBoolean_compute() {
        BooleanType type = new BooleanType();
        type.setModifiers(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))));
        type.setID("boolean");
        type.setSuperClassAccess(unknownType().createQualifiedAccess());
        return type;
    }

    public TypeDecl typeByte() {
        if (this.typeByte_computed) {
            return this.typeByte_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.typeByte_value = typeByte_compute();
        this.typeByte_value.setParent(this);
        this.typeByte_value.is$Final = true;
        this.typeByte_computed = true;
        return this.typeByte_value;
    }

    private TypeDecl typeByte_compute() {
        ByteType type = new ByteType();
        type.setModifiers(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))));
        type.setID("byte");
        type.setSuperClassAccess(typeShort().createQualifiedAccess());
        return type;
    }

    public TypeDecl typeShort() {
        if (this.typeShort_computed) {
            return this.typeShort_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.typeShort_value = typeShort_compute();
        this.typeShort_value.setParent(this);
        this.typeShort_value.is$Final = true;
        this.typeShort_computed = true;
        return this.typeShort_value;
    }

    private TypeDecl typeShort_compute() {
        ShortType type = new ShortType();
        type.setModifiers(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))));
        type.setID("short");
        type.setSuperClassAccess(typeInt().createQualifiedAccess());
        return type;
    }

    public TypeDecl typeChar() {
        if (this.typeChar_computed) {
            return this.typeChar_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.typeChar_value = typeChar_compute();
        this.typeChar_value.setParent(this);
        this.typeChar_value.is$Final = true;
        this.typeChar_computed = true;
        return this.typeChar_value;
    }

    private TypeDecl typeChar_compute() {
        CharType type = new CharType();
        type.setModifiers(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))));
        type.setID("char");
        type.setSuperClassAccess(typeInt().createQualifiedAccess());
        return type;
    }

    public TypeDecl typeInt() {
        if (this.typeInt_computed) {
            return this.typeInt_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.typeInt_value = typeInt_compute();
        this.typeInt_value.setParent(this);
        this.typeInt_value.is$Final = true;
        this.typeInt_computed = true;
        return this.typeInt_value;
    }

    private TypeDecl typeInt_compute() {
        IntType type = new IntType();
        type.setModifiers(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))));
        type.setID("int");
        type.setSuperClassAccess(typeLong().createQualifiedAccess());
        return type;
    }

    public TypeDecl typeLong() {
        if (this.typeLong_computed) {
            return this.typeLong_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.typeLong_value = typeLong_compute();
        this.typeLong_value.setParent(this);
        this.typeLong_value.is$Final = true;
        this.typeLong_computed = true;
        return this.typeLong_value;
    }

    private TypeDecl typeLong_compute() {
        LongType type = new LongType();
        type.setModifiers(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))));
        type.setID("long");
        type.setSuperClassAccess(typeFloat().createQualifiedAccess());
        return type;
    }

    public TypeDecl typeFloat() {
        if (this.typeFloat_computed) {
            return this.typeFloat_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.typeFloat_value = typeFloat_compute();
        this.typeFloat_value.setParent(this);
        this.typeFloat_value.is$Final = true;
        this.typeFloat_computed = true;
        return this.typeFloat_value;
    }

    private TypeDecl typeFloat_compute() {
        FloatType type = new FloatType();
        type.setModifiers(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))));
        type.setID(Jimple.FLOAT);
        type.setSuperClassAccess(typeDouble().createQualifiedAccess());
        return type;
    }

    public TypeDecl typeDouble() {
        if (this.typeDouble_computed) {
            return this.typeDouble_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.typeDouble_value = typeDouble_compute();
        this.typeDouble_value.setParent(this);
        this.typeDouble_value.is$Final = true;
        this.typeDouble_computed = true;
        return this.typeDouble_value;
    }

    private TypeDecl typeDouble_compute() {
        DoubleType type = new DoubleType();
        type.setModifiers(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))));
        type.setID("double");
        type.setSuperClassAccess(unknownType().createQualifiedAccess());
        return type;
    }

    public TypeDecl typeVoid() {
        if (this.typeVoid_computed) {
            return this.typeVoid_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.typeVoid_value = typeVoid_compute();
        this.typeVoid_value.setParent(this);
        this.typeVoid_value.is$Final = true;
        this.typeVoid_computed = true;
        return this.typeVoid_value;
    }

    private TypeDecl typeVoid_compute() {
        VoidType classDecl = new VoidType();
        classDecl.setModifiers(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))));
        classDecl.setID(Jimple.VOID);
        return classDecl;
    }

    public TypeDecl typeNull() {
        if (this.typeNull_computed) {
            return this.typeNull_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.typeNull_value = typeNull_compute();
        this.typeNull_value.setParent(this);
        this.typeNull_value.is$Final = true;
        this.typeNull_computed = true;
        return this.typeNull_value;
    }

    private TypeDecl typeNull_compute() {
        NullType classDecl = new NullType();
        classDecl.setModifiers(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))));
        classDecl.setID(Jimple.NULL);
        return classDecl;
    }

    public TypeDecl unknownType() {
        if (this.unknownType_computed) {
            return this.unknownType_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.unknownType_value = unknownType_compute();
        this.unknownType_value.setParent(this);
        this.unknownType_value.is$Final = true;
        this.unknownType_computed = true;
        return this.unknownType_value;
    }

    private TypeDecl unknownType_compute() {
        ClassDecl classDecl = new UnknownType();
        classDecl.setModifiers(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))));
        classDecl.setID("Unknown");
        MethodDecl methodDecl = new MethodDecl(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))), new PrimitiveTypeAccess("Unknown"), "unknown", new List(), new List(), new Opt());
        classDecl.addBodyDecl(methodDecl);
        FieldDeclaration fieldDecl = new FieldDeclaration(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))), new PrimitiveTypeAccess("Unknown"), "unknown", new Opt());
        classDecl.addBodyDecl(fieldDecl);
        ConstructorDecl constrDecl = new ConstructorDecl(new Modifiers(new List().add(new Modifier(Jimple.PUBLIC))), "Unknown", new List(), new List(), new Opt(), new Block());
        classDecl.addBodyDecl(constrDecl);
        return classDecl;
    }

    @Override // soot.JastAddJ.CompilationUnit, soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
