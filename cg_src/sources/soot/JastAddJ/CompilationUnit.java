package soot.JastAddJ;

import beaver.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import soot.JastAddJ.ASTNode;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CompilationUnit.class */
public class CompilationUnit extends ASTNode<ASTNode> implements Cloneable {
    private String relativeName;
    private String pathName;
    private boolean fromSource;
    protected String tokenjava_lang_String_PackageDecl;
    public int PackageDeclstart;
    public int PackageDeclend;
    protected String packageName_value;
    protected Map lookupType_String_values;
    protected ArrayList errors = new ArrayList();
    protected ArrayList warnings = new ArrayList();
    protected Collection parseErrors = new ArrayList();
    public boolean isResolved = false;
    protected boolean packageName_computed = false;

    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.packageName_computed = false;
        this.packageName_value = null;
        this.lookupType_String_values = null;
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public CompilationUnit clone() throws CloneNotSupportedException {
        CompilationUnit node = (CompilationUnit) super.mo287clone();
        node.packageName_computed = false;
        node.packageName_value = null;
        node.lookupType_String_values = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.JastAddJ.ASTNode
    public ASTNode<ASTNode> copy() {
        try {
            CompilationUnit node = clone();
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
    @Override // soot.JastAddJ.ASTNode
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

    public void setRelativeName(String name) {
        this.relativeName = name;
    }

    public void setPathName(String name) {
        this.pathName = name;
    }

    public void setFromSource(boolean value) {
        this.fromSource = value;
    }

    public Collection parseErrors() {
        return this.parseErrors;
    }

    public void addParseError(Problem msg) {
        this.parseErrors.add(msg);
    }

    public void errorCheck(Collection collection) {
        collectErrors();
        collection.addAll(this.errors);
    }

    public void errorCheck(Collection err, Collection warn) {
        collectErrors();
        err.addAll(this.errors);
        warn.addAll(this.warnings);
    }

    public void refined_NameCheck_CompilationUnit_nameCheck() {
        for (int i = 0; i < getNumImportDecl(); i++) {
            ImportDecl decl = getImportDecl(i);
            if (decl instanceof SingleTypeImportDecl) {
                TypeDecl importedType = decl.getAccess().type();
                for (TypeDecl local : localLookupType(importedType.name())) {
                    if (local != importedType) {
                        error("imported type " + decl + " is conflicting with visible type");
                    }
                }
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        try {
            if (!getPackageDecl().equals("")) {
                s.append("package " + getPackageDecl() + ";\n");
            }
            for (int i = 0; i < getNumImportDecl(); i++) {
                getImportDecl(i).toString(s);
            }
            for (int i2 = 0; i2 < getNumTypeDecl(); i2++) {
                getTypeDecl(i2).toString(s);
                s.append("\n");
            }
        } catch (NullPointerException e) {
            System.out.print("Error in compilation unit hosting " + getTypeDecl(0).typeName());
            throw e;
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void transformation() {
        if (fromSource()) {
            for (int i = 0; i < getNumTypeDecl(); i++) {
                getTypeDecl(i).transformation();
            }
        }
    }

    public CompilationUnit() {
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[2];
        setChild(new List(), 0);
        setChild(new List(), 1);
    }

    public CompilationUnit(String p0, List<ImportDecl> p1, List<TypeDecl> p2) {
        setPackageDecl(p0);
        setChild(p1, 0);
        setChild(p2, 1);
    }

    public CompilationUnit(Symbol p0, List<ImportDecl> p1, List<TypeDecl> p2) {
        setPackageDecl(p0);
        setChild(p1, 0);
        setChild(p2, 1);
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 2;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setPackageDecl(String value) {
        this.tokenjava_lang_String_PackageDecl = value;
    }

    public void setPackageDecl(Symbol symbol) {
        if (symbol.value != null && !(symbol.value instanceof String)) {
            throw new UnsupportedOperationException("setPackageDecl is only valid for String lexemes");
        }
        this.tokenjava_lang_String_PackageDecl = (String) symbol.value;
        this.PackageDeclstart = symbol.getStart();
        this.PackageDeclend = symbol.getEnd();
    }

    public String getPackageDecl() {
        return this.tokenjava_lang_String_PackageDecl != null ? this.tokenjava_lang_String_PackageDecl : "";
    }

    public void setImportDeclList(List<ImportDecl> list) {
        setChild(list, 0);
    }

    public int getNumImportDecl() {
        return getImportDeclList().getNumChild();
    }

    public int getNumImportDeclNoTransform() {
        return getImportDeclListNoTransform().getNumChildNoTransform();
    }

    public ImportDecl getImportDecl(int i) {
        return getImportDeclList().getChild(i);
    }

    public void addImportDecl(ImportDecl node) {
        List<ImportDecl> list = (this.parent == null || state == null) ? getImportDeclListNoTransform() : getImportDeclList();
        list.addChild(node);
    }

    public void addImportDeclNoTransform(ImportDecl node) {
        List<ImportDecl> list = getImportDeclListNoTransform();
        list.addChild(node);
    }

    public void setImportDecl(ImportDecl node, int i) {
        List<ImportDecl> list = getImportDeclList();
        list.setChild(node, i);
    }

    public List<ImportDecl> getImportDecls() {
        return getImportDeclList();
    }

    public List<ImportDecl> getImportDeclsNoTransform() {
        return getImportDeclListNoTransform();
    }

    public List<ImportDecl> getImportDeclList() {
        List<ImportDecl> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    public List<ImportDecl> getImportDeclListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    public void setTypeDeclList(List<TypeDecl> list) {
        setChild(list, 1);
    }

    public int getNumTypeDecl() {
        return getTypeDeclList().getNumChild();
    }

    public int getNumTypeDeclNoTransform() {
        return getTypeDeclListNoTransform().getNumChildNoTransform();
    }

    public TypeDecl getTypeDecl(int i) {
        return getTypeDeclList().getChild(i);
    }

    public void addTypeDecl(TypeDecl node) {
        List<TypeDecl> list = (this.parent == null || state == null) ? getTypeDeclListNoTransform() : getTypeDeclList();
        list.addChild(node);
    }

    public void addTypeDeclNoTransform(TypeDecl node) {
        List<TypeDecl> list = getTypeDeclListNoTransform();
        list.addChild(node);
    }

    public void setTypeDecl(TypeDecl node, int i) {
        List<TypeDecl> list = getTypeDeclList();
        list.setChild(node, i);
    }

    public List<TypeDecl> getTypeDecls() {
        return getTypeDeclList();
    }

    public List<TypeDecl> getTypeDeclsNoTransform() {
        return getTypeDeclListNoTransform();
    }

    public List<TypeDecl> getTypeDeclList() {
        List<TypeDecl> list = (List) getChild(1);
        list.getNumChild();
        return list;
    }

    public List<TypeDecl> getTypeDeclListNoTransform() {
        return (List) getChildNoTransform(1);
    }

    @Override // soot.JastAddJ.ASTNode
    public void nameCheck() {
        refined_NameCheck_CompilationUnit_nameCheck();
        for (int i = 0; i < getNumImportDecl(); i++) {
            if (getImportDecl(i) instanceof SingleStaticImportDecl) {
                SingleStaticImportDecl decl = (SingleStaticImportDecl) getImportDecl(i);
                String name = decl.name();
                if (!decl.importedTypes(name).isEmpty()) {
                    TypeDecl type = (TypeDecl) decl.importedTypes(name).iterator().next();
                    if (localLookupType(name).contains(type)) {
                        decl.error(String.valueOf(packageName()) + "." + name + " is already defined in this compilation unit");
                    }
                }
            }
        }
    }

    private SimpleSet refined_TypeScopePropagation_CompilationUnit_Child_lookupType_String(String name) {
        SimpleSet set = localLookupType(name);
        if (set.isEmpty()) {
            SimpleSet set2 = importedTypes(name);
            if (set2.isEmpty()) {
                TypeDecl result = lookupType(packageName(), name);
                if (result != null && result.accessibleFromPackage(packageName())) {
                    return SimpleSet.emptySet.add(result);
                }
                SimpleSet set3 = importedTypesOnDemand(name);
                if (set3.isEmpty()) {
                    TypeDecl result2 = lookupType("@primitive", name);
                    if (result2 != null) {
                        return SimpleSet.emptySet.add(result2);
                    }
                    TypeDecl result3 = lookupType("java.lang", name);
                    if (result3 != null && result3.accessibleFromPackage(packageName())) {
                        return SimpleSet.emptySet.add(result3);
                    }
                    return lookupType(name);
                }
                return set3;
            }
            return set2;
        }
        return set;
    }

    public String relativeName() {
        state();
        return this.relativeName;
    }

    public String pathName() {
        state();
        return this.pathName;
    }

    public boolean fromSource() {
        state();
        return this.fromSource;
    }

    public SimpleSet localLookupType(String name) {
        state();
        for (int i = 0; i < getNumTypeDecl(); i++) {
            if (getTypeDecl(i).name().equals(name)) {
                return SimpleSet.emptySet.add(getTypeDecl(i));
            }
        }
        return SimpleSet.emptySet;
    }

    public SimpleSet importedTypes(String name) {
        state();
        SimpleSet set = SimpleSet.emptySet;
        for (int i = 0; i < getNumImportDecl(); i++) {
            if (!getImportDecl(i).isOnDemand()) {
                for (Object obj : getImportDecl(i).importedTypes(name)) {
                    set = set.add(obj);
                }
            }
        }
        return set;
    }

    public SimpleSet importedTypesOnDemand(String name) {
        state();
        SimpleSet set = SimpleSet.emptySet;
        for (int i = 0; i < getNumImportDecl(); i++) {
            if (getImportDecl(i).isOnDemand()) {
                for (Object obj : getImportDecl(i).importedTypes(name)) {
                    set = set.add(obj);
                }
            }
        }
        return set;
    }

    @Override // soot.JastAddJ.ASTNode
    public String dumpString() {
        state();
        return String.valueOf(getClass().getName()) + " [" + getPackageDecl() + "]";
    }

    public String packageName() {
        if (this.packageName_computed) {
            return this.packageName_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.packageName_value = packageName_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.packageName_computed = true;
        }
        return this.packageName_value;
    }

    private String packageName_compute() {
        return getPackageDecl();
    }

    public SimpleSet importedFields(String name) {
        state();
        SimpleSet set = SimpleSet.emptySet;
        for (int i = 0; i < getNumImportDecl(); i++) {
            if (!getImportDecl(i).isOnDemand()) {
                for (Object obj : getImportDecl(i).importedFields(name)) {
                    set = set.add(obj);
                }
            }
        }
        return set;
    }

    public SimpleSet importedFieldsOnDemand(String name) {
        state();
        SimpleSet set = SimpleSet.emptySet;
        for (int i = 0; i < getNumImportDecl(); i++) {
            if (getImportDecl(i).isOnDemand()) {
                for (Object obj : getImportDecl(i).importedFields(name)) {
                    set = set.add(obj);
                }
            }
        }
        return set;
    }

    public Collection importedMethods(String name) {
        state();
        Collection list = new ArrayList();
        for (int i = 0; i < getNumImportDecl(); i++) {
            if (!getImportDecl(i).isOnDemand()) {
                list.addAll(getImportDecl(i).importedMethods(name));
            }
        }
        return list;
    }

    public Collection importedMethodsOnDemand(String name) {
        state();
        Collection list = new ArrayList();
        for (int i = 0; i < getNumImportDecl(); i++) {
            if (getImportDecl(i).isOnDemand()) {
                list.addAll(getImportDecl(i).importedMethods(name));
            }
        }
        return list;
    }

    public TypeDecl lookupType(String packageName, String typeName) {
        state();
        TypeDecl lookupType_String_String_value = getParent().Define_TypeDecl_lookupType(this, null, packageName, typeName);
        return lookupType_String_String_value;
    }

    public SimpleSet lookupType(String name) {
        if (this.lookupType_String_values == null) {
            this.lookupType_String_values = new HashMap(4);
        }
        if (this.lookupType_String_values.containsKey(name)) {
            return (SimpleSet) this.lookupType_String_values.get(name);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        SimpleSet lookupType_String_value = getParent().Define_SimpleSet_lookupType(this, null, name);
        if (isFinal && num == state().boundariesCrossed) {
            this.lookupType_String_values.put(name, lookupType_String_value);
        }
        return lookupType_String_value;
    }

    public SimpleSet lookupVariable(String name) {
        state();
        SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);
        return lookupVariable_String_value;
    }

    public Collection lookupMethod(String name) {
        state();
        Collection lookupMethod_String_value = getParent().Define_Collection_lookupMethod(this, null, name);
        return lookupMethod_String_value;
    }

    @Override // soot.JastAddJ.ASTNode
    public CompilationUnit Define_CompilationUnit_compilationUnit(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return this;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
        if (caller == getTypeDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_isIncOrDec(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
        if (caller == getImportDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return !exceptionType.isUncheckedException();
        } else if (caller == getTypeDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return !exceptionType.isUncheckedException();
        } else {
            return getParent().Define_boolean_handlesException(this, caller, exceptionType);
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        if (caller == getImportDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return lookupType(name);
        }
        getIndexOfChild(caller);
        SimpleSet result = SimpleSet.emptySet;
        for (TypeDecl typeDecl : refined_TypeScopePropagation_CompilationUnit_Child_lookupType_String(name)) {
            if (typeDecl instanceof ParTypeDecl) {
                result = result.add(((ParTypeDecl) typeDecl).genericDecl());
            } else {
                result = result.add(typeDecl);
            }
        }
        return result;
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_allImportedTypes(ASTNode caller, ASTNode child, String name) {
        if (caller == getImportDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return importedTypes(name);
        }
        return getParent().Define_SimpleSet_allImportedTypes(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public String Define_String_packageName(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return packageName();
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        if (caller == getImportDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return NameType.PACKAGE_NAME;
        }
        return getParent().Define_NameType_nameType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_enclosingType(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isNestedType(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
        if (caller == getTypeDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return false;
        }
        return getParent().Define_boolean_isMemberType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isLocalClass(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public String Define_String_hostPackage(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return packageName();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
        if (caller == getImportDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            return null;
        }
        return getParent().Define_TypeDecl_hostType(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        if (caller == getTypeDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            SimpleSet set = importedFields(name);
            if (set.isEmpty()) {
                SimpleSet set2 = importedFieldsOnDemand(name);
                return !set2.isEmpty() ? set2 : lookupVariable(name);
            }
            return set;
        }
        return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
        if (caller == getTypeDeclListNoTransform()) {
            caller.getIndexOfChild(child);
            Collection list = importedMethods(name);
            if (list.isEmpty()) {
                Collection list2 = importedMethodsOnDemand(name);
                return !list2.isEmpty() ? list2 : lookupMethod(name);
            }
            return list;
        }
        return getParent().Define_Collection_lookupMethod(this, caller, name);
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
