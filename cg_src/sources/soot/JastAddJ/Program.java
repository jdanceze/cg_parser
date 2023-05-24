package soot.JastAddJ;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import soot.JastAddJ.ASTNode;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Program.class */
public class Program extends ASTNode<ASTNode> implements Cloneable {
    protected BytecodeReader bytecodeReader;
    protected JavaParser javaParser;
    private ArrayList classPath;
    private ArrayList sourcePath;
    public int classFileReadTime;
    public static final int SRC_PREC_JAVA = 1;
    public static final int SRC_PREC_CLASS = 2;
    public static final int SRC_PREC_ONLY_CLASS = 3;
    protected TypeDecl typeObject_value;
    protected TypeDecl typeCloneable_value;
    protected TypeDecl typeSerializable_value;
    protected TypeDecl typeBoolean_value;
    protected TypeDecl typeByte_value;
    protected TypeDecl typeShort_value;
    protected TypeDecl typeChar_value;
    protected TypeDecl typeInt_value;
    protected TypeDecl typeLong_value;
    protected TypeDecl typeFloat_value;
    protected TypeDecl typeDouble_value;
    protected TypeDecl typeString_value;
    protected TypeDecl typeVoid_value;
    protected TypeDecl typeNull_value;
    protected TypeDecl unknownType_value;
    protected Map hasPackage_String_values;
    protected Map lookupType_String_String_values;
    protected Map lookupLibType_String_String_values;
    protected Map getLibCompilationUnit_String_values;
    protected List getLibCompilationUnit_String_list;
    protected PrimitiveCompilationUnit getPrimitiveCompilationUnit_value;
    protected ConstructorDecl unknownConstructor_value;
    protected WildcardsCompilationUnit wildcards_value;
    private boolean pathsInitialized = false;
    private FileNamesPart sourceFiles = new FileNamesPart(this);
    private int srcPrec = 0;
    private HashMap loadedCompilationUnit = new HashMap();
    protected boolean typeObject_computed = false;
    protected boolean typeCloneable_computed = false;
    protected boolean typeSerializable_computed = false;
    protected boolean typeBoolean_computed = false;
    protected boolean typeByte_computed = false;
    protected boolean typeShort_computed = false;
    protected boolean typeChar_computed = false;
    protected boolean typeInt_computed = false;
    protected boolean typeLong_computed = false;
    protected boolean typeFloat_computed = false;
    protected boolean typeDouble_computed = false;
    protected boolean typeString_computed = false;
    protected boolean typeVoid_computed = false;
    protected boolean typeNull_computed = false;
    protected boolean unknownType_computed = false;
    protected boolean getPrimitiveCompilationUnit_computed = false;
    protected boolean unknownConstructor_computed = false;
    protected boolean wildcards_computed = false;

    @Override // soot.JastAddJ.ASTNode
    public void flushCache() {
        super.flushCache();
        this.typeObject_computed = false;
        this.typeObject_value = null;
        this.typeCloneable_computed = false;
        this.typeCloneable_value = null;
        this.typeSerializable_computed = false;
        this.typeSerializable_value = null;
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
        this.typeString_computed = false;
        this.typeString_value = null;
        this.typeVoid_computed = false;
        this.typeVoid_value = null;
        this.typeNull_computed = false;
        this.typeNull_value = null;
        this.unknownType_computed = false;
        this.unknownType_value = null;
        this.hasPackage_String_values = null;
        this.lookupType_String_String_values = null;
        this.lookupLibType_String_String_values = null;
        this.getLibCompilationUnit_String_values = null;
        this.getLibCompilationUnit_String_list = null;
        this.getPrimitiveCompilationUnit_computed = false;
        this.getPrimitiveCompilationUnit_value = null;
        this.unknownConstructor_computed = false;
        this.unknownConstructor_value = null;
        this.wildcards_computed = false;
        this.wildcards_value = null;
    }

    @Override // soot.JastAddJ.ASTNode
    public void flushCollectionCache() {
        super.flushCollectionCache();
    }

    @Override // soot.JastAddJ.ASTNode, beaver.Symbol
    public Program clone() throws CloneNotSupportedException {
        Program node = (Program) super.mo287clone();
        node.typeObject_computed = false;
        node.typeObject_value = null;
        node.typeCloneable_computed = false;
        node.typeCloneable_value = null;
        node.typeSerializable_computed = false;
        node.typeSerializable_value = null;
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
        node.typeString_computed = false;
        node.typeString_value = null;
        node.typeVoid_computed = false;
        node.typeVoid_value = null;
        node.typeNull_computed = false;
        node.typeNull_value = null;
        node.unknownType_computed = false;
        node.unknownType_value = null;
        node.hasPackage_String_values = null;
        node.lookupType_String_String_values = null;
        node.lookupLibType_String_String_values = null;
        node.getLibCompilationUnit_String_values = null;
        node.getLibCompilationUnit_String_list = null;
        node.getPrimitiveCompilationUnit_computed = false;
        node.getPrimitiveCompilationUnit_value = null;
        node.unknownConstructor_computed = false;
        node.unknownConstructor_value = null;
        node.wildcards_computed = false;
        node.wildcards_value = null;
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    @Override // soot.JastAddJ.ASTNode
    /* renamed from: copy */
    public ASTNode<ASTNode> copy2() {
        try {
            Program node = clone();
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

    public void initBytecodeReader(BytecodeReader r) {
        this.bytecodeReader = r;
    }

    public void initJavaParser(JavaParser p) {
        this.javaParser = p;
    }

    public CompilationUnit addSourceFile(String name) {
        return this.sourceFiles.addSourceFile(name);
    }

    public Iterator compilationUnitIterator() {
        initPaths();
        return new Iterator() { // from class: soot.JastAddJ.Program.1
            int index = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.index < Program.this.getNumCompilationUnit() || !Program.this.sourceFiles.isEmpty();
            }

            @Override // java.util.Iterator
            public Object next() {
                if (Program.this.getNumCompilationUnit() == this.index) {
                    String typename = (String) Program.this.sourceFiles.keySet().iterator().next();
                    CompilationUnit u = Program.this.getCompilationUnit(typename);
                    if (u != null) {
                        Program.this.addCompilationUnit(u);
                        Program.this.getCompilationUnit(Program.this.getNumCompilationUnit() - 1);
                    } else {
                        throw new Error("File " + typename + " not found");
                    }
                }
                Program program = Program.this;
                int i = this.index;
                this.index = i + 1;
                return program.getCompilationUnit(i);
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public InputStream getInputStream(String name) {
        initPaths();
        try {
            Iterator iter = this.classPath.iterator();
            while (iter.hasNext()) {
                PathPart part = (PathPart) iter.next();
                if (part.selectCompilationUnit(name)) {
                    return part.getInputStream();
                }
            }
        } catch (IOException e) {
        }
        throw new Error("Could not find nested type " + name);
    }

    public boolean isPackage(String name) {
        if (this.sourceFiles.hasPackage(name)) {
            return true;
        }
        Iterator iter = this.classPath.iterator();
        while (iter.hasNext()) {
            PathPart part = (PathPart) iter.next();
            if (part.hasPackage(name)) {
                return true;
            }
        }
        Iterator iter2 = this.sourcePath.iterator();
        while (iter2.hasNext()) {
            PathPart part2 = (PathPart) iter2.next();
            if (part2.hasPackage(name)) {
                return true;
            }
        }
        return false;
    }

    public void pushClassPath(String name) {
        PathPart part = PathPart.createSourcePath(name, this);
        if (part != null) {
            this.sourcePath.add(part);
            System.out.println("Pushing source path " + name);
            PathPart part2 = PathPart.createClassPath(name, this);
            if (part2 != null) {
                this.classPath.add(part2);
                System.out.println("Pushing class path " + name);
                return;
            }
            return;
        }
        throw new Error("Could not push source path " + name);
    }

    public void popClassPath() {
        if (this.sourcePath.size() > 0) {
            this.sourcePath.remove(this.sourcePath.size() - 1);
        }
        if (this.classPath.size() > 0) {
            this.classPath.remove(this.classPath.size() - 1);
        }
    }

    public void initPaths() {
        String[] bootclasspaths;
        String[] extdirs;
        String[] userClasses;
        if (!this.pathsInitialized) {
            this.pathsInitialized = true;
            ArrayList classPaths = new ArrayList();
            ArrayList sourcePaths = new ArrayList();
            if (options().hasValueForOption("-bootclasspath")) {
                bootclasspaths = options().getValueForOption("-bootclasspath").split(File.pathSeparator);
            } else {
                bootclasspaths = System.getProperty("sun.boot.class.path").split(File.pathSeparator);
            }
            for (String str : bootclasspaths) {
                classPaths.add(str);
            }
            if (options().hasValueForOption("-extdirs")) {
                extdirs = options().getValueForOption("-extdirs").split(File.pathSeparator);
            } else {
                extdirs = System.getProperty("java.ext.dirs").split(File.pathSeparator);
            }
            for (String str2 : extdirs) {
                classPaths.add(str2);
            }
            if (options().hasValueForOption("-classpath")) {
                userClasses = options().getValueForOption("-classpath").split(File.pathSeparator);
            } else if (options().hasValueForOption("-cp")) {
                userClasses = options().getValueForOption("-cp").split(File.pathSeparator);
            } else {
                userClasses = ".".split(File.pathSeparator);
            }
            if (!options().hasValueForOption("-sourcepath")) {
                for (int i = 0; i < userClasses.length; i++) {
                    classPaths.add(userClasses[i]);
                    sourcePaths.add(userClasses[i]);
                }
            } else {
                for (String str3 : userClasses) {
                    classPaths.add(str3);
                }
                String[] userClasses2 = options().getValueForOption("-sourcepath").split(File.pathSeparator);
                for (String str4 : userClasses2) {
                    sourcePaths.add(str4);
                }
            }
            this.classPath = new ArrayList();
            this.sourcePath = new ArrayList();
            Iterator iter = classPaths.iterator();
            while (iter.hasNext()) {
                String s = (String) iter.next();
                PathPart part = PathPart.createClassPath(s, this);
                if (part != null) {
                    this.classPath.add(part);
                } else if (options().verbose()) {
                    System.out.println("Warning: Could not use " + s + " as class path");
                }
            }
            Iterator iter2 = sourcePaths.iterator();
            while (iter2.hasNext()) {
                String s2 = (String) iter2.next();
                PathPart part2 = PathPart.createSourcePath(s2, this);
                if (part2 != null) {
                    this.sourcePath.add(part2);
                } else if (options().verbose()) {
                    System.out.println("Warning: Could not use " + s2 + " as source path");
                }
            }
        }
    }

    public void addClassPath(PathPart pathPart) {
        this.classPath.add(pathPart);
        pathPart.setProgram(this);
    }

    public void addSourcePath(PathPart pathPart) {
        this.sourcePath.add(pathPart);
        pathPart.setProgram(this);
    }

    public void simpleReset() {
        this.lookupType_String_String_values = new HashMap();
        this.hasPackage_String_values = new HashMap();
        List list = new List();
        for (int i = 0; i < getNumCompilationUnit(); i++) {
            CompilationUnit unit = getCompilationUnit(i);
            if (!unit.fromSource()) {
                list.add(unit);
            }
        }
        setCompilationUnitList(list);
    }

    public void errorCheck(Collection collection) {
        Iterator iter = compilationUnitIterator();
        while (iter.hasNext()) {
            CompilationUnit cu = (CompilationUnit) iter.next();
            if (cu.fromSource()) {
                cu.collectErrors();
                collection.addAll(cu.errors);
            }
        }
    }

    public void errorCheck(Collection collection, Collection warn) {
        Iterator iter = compilationUnitIterator();
        while (iter.hasNext()) {
            CompilationUnit cu = (CompilationUnit) iter.next();
            if (cu.fromSource()) {
                cu.collectErrors();
                collection.addAll(cu.errors);
                warn.addAll(cu.warnings);
            }
        }
    }

    public boolean errorCheck() {
        Collection<String> collection = new LinkedList();
        errorCheck(collection);
        if (collection.isEmpty()) {
            return false;
        }
        System.out.println("Errors:");
        for (String s : collection) {
            System.out.println(s);
        }
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public void toString(StringBuffer s) {
        Iterator iter = compilationUnitIterator();
        while (iter.hasNext()) {
            CompilationUnit cu = (CompilationUnit) iter.next();
            if (cu.fromSource()) {
                cu.toString(s);
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public String dumpTree() {
        StringBuffer s = new StringBuffer();
        Iterator iter = compilationUnitIterator();
        while (iter.hasNext()) {
            CompilationUnit cu = (CompilationUnit) iter.next();
            if (cu.fromSource()) {
                s.append(cu.dumpTree());
            }
        }
        return s.toString();
    }

    public void jimplify1() {
        Iterator iter = compilationUnitIterator();
        while (iter.hasNext()) {
            CompilationUnit u = (CompilationUnit) iter.next();
            if (u.fromSource()) {
                u.jimplify1phase1();
            }
        }
        Iterator iter2 = compilationUnitIterator();
        while (iter2.hasNext()) {
            CompilationUnit u2 = (CompilationUnit) iter2.next();
            if (u2.fromSource()) {
                u2.jimplify1phase2();
            }
        }
    }

    @Override // soot.JastAddJ.ASTNode
    public void jimplify2() {
        Iterator iter = compilationUnitIterator();
        while (iter.hasNext()) {
            CompilationUnit u = (CompilationUnit) iter.next();
            if (u.fromSource()) {
                u.jimplify2();
            }
        }
    }

    public void setSrcPrec(int i) {
        this.srcPrec = i;
    }

    public boolean hasLoadedCompilationUnit(String fileName) {
        return this.loadedCompilationUnit.containsKey(fileName);
    }

    public CompilationUnit getCachedOrLoadCompilationUnit(String fileName) {
        if (this.loadedCompilationUnit.containsKey(fileName)) {
            return (CompilationUnit) this.loadedCompilationUnit.get(fileName);
        }
        addSourceFile(fileName);
        return (CompilationUnit) this.loadedCompilationUnit.get(fileName);
    }

    public void releaseCompilationUnitForFile(String fileName) {
        this.lookupType_String_String_values = new HashMap();
        this.hasPackage_String_values = new HashMap();
        this.loadedCompilationUnit.remove(fileName);
        List<CompilationUnit> newList = new List<>();
        Iterator<CompilationUnit> it = getCompilationUnits().iterator();
        while (it.hasNext()) {
            CompilationUnit cu = it.next();
            boolean dontAdd = false;
            if (cu.fromSource()) {
                String pathName = cu.pathName();
                if (pathName.equals(fileName)) {
                    dontAdd = true;
                }
            }
            if (!dontAdd) {
                newList.add(cu);
            }
        }
        setCompilationUnitList(newList);
    }

    public Program() {
        is$Final(true);
    }

    @Override // soot.JastAddJ.ASTNode
    public void init$Children() {
        this.children = new ASTNode[1];
        setChild(new List(), 0);
    }

    public Program(List<CompilationUnit> p0) {
        setChild(p0, 0);
        is$Final(true);
    }

    @Override // soot.JastAddJ.ASTNode
    protected int numChildren() {
        return 1;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean mayHaveRewrite() {
        return false;
    }

    public void setCompilationUnitList(List<CompilationUnit> list) {
        setChild(list, 0);
    }

    public int getNumCompilationUnit() {
        return getCompilationUnitList().getNumChild();
    }

    public int getNumCompilationUnitNoTransform() {
        return getCompilationUnitListNoTransform().getNumChildNoTransform();
    }

    public CompilationUnit getCompilationUnit(int i) {
        return (CompilationUnit) getCompilationUnitList().getChild(i);
    }

    public void refined__Program_addCompilationUnit(CompilationUnit node) {
        List<CompilationUnit> list = (this.parent == null || state == null) ? getCompilationUnitListNoTransform() : getCompilationUnitList();
        list.addChild(node);
    }

    public void addCompilationUnitNoTransform(CompilationUnit node) {
        List<CompilationUnit> list = getCompilationUnitListNoTransform();
        list.addChild(node);
    }

    public void setCompilationUnit(CompilationUnit node, int i) {
        List<CompilationUnit> list = getCompilationUnitList();
        list.setChild(node, i);
    }

    public List<CompilationUnit> getCompilationUnits() {
        return getCompilationUnitList();
    }

    public List<CompilationUnit> getCompilationUnitsNoTransform() {
        return getCompilationUnitListNoTransform();
    }

    public List<CompilationUnit> refined__Program_getCompilationUnitList() {
        List<CompilationUnit> list = (List) getChild(0);
        list.getNumChild();
        return list;
    }

    public List<CompilationUnit> getCompilationUnitListNoTransform() {
        return (List) getChildNoTransform(0);
    }

    public CompilationUnit getCompilationUnit(String name) {
        initPaths();
        try {
            if (this.sourceFiles.selectCompilationUnit(name)) {
                return this.sourceFiles.getCompilationUnit();
            }
            PathPart sourcePart = null;
            PathPart classPart = null;
            Iterator iter = this.sourcePath.iterator();
            while (iter.hasNext() && sourcePart == null) {
                PathPart part = (PathPart) iter.next();
                if (part.selectCompilationUnit(name)) {
                    sourcePart = part;
                }
            }
            Iterator iter2 = this.classPath.iterator();
            while (iter2.hasNext() && classPart == null) {
                PathPart part2 = (PathPart) iter2.next();
                if (part2.selectCompilationUnit(name)) {
                    classPart = part2;
                }
            }
            if (sourcePart != null && this.srcPrec == 1) {
                CompilationUnit unit = getCachedOrLoadCompilationUnit(new File(sourcePart.pathName).getCanonicalPath());
                int index = name.lastIndexOf(46);
                if (index == -1) {
                    return unit;
                }
                String pkgName = name.substring(0, index);
                if (pkgName.equals(unit.getPackageDecl())) {
                    return unit;
                }
            }
            if (classPart != null && this.srcPrec == 2) {
                CompilationUnit unit2 = classPart.getCompilationUnit();
                int index2 = name.lastIndexOf(46);
                if (index2 == -1) {
                    return unit2;
                }
                String pkgName2 = name.substring(0, index2);
                if (pkgName2.equals(unit2.getPackageDecl())) {
                    return unit2;
                }
            }
            if (this.srcPrec == 3) {
                if (classPart != null) {
                    CompilationUnit unit3 = classPart.getCompilationUnit();
                    int index3 = name.lastIndexOf(46);
                    if (index3 == -1) {
                        return unit3;
                    }
                    String pkgName3 = name.substring(0, index3);
                    if (pkgName3.equals(unit3.getPackageDecl())) {
                        return unit3;
                    }
                    return null;
                }
                return null;
            } else if (sourcePart != null && (classPart == null || classPart.getAge() <= sourcePart.getAge())) {
                CompilationUnit unit4 = getCachedOrLoadCompilationUnit(new File(sourcePart.pathName).getCanonicalPath());
                int index4 = name.lastIndexOf(46);
                if (index4 == -1) {
                    return unit4;
                }
                String pkgName4 = name.substring(0, index4);
                if (pkgName4.equals(unit4.getPackageDecl())) {
                    return unit4;
                }
                return null;
            } else if (classPart != null) {
                CompilationUnit unit5 = classPart.getCompilationUnit();
                int index5 = name.lastIndexOf(46);
                if (index5 == -1) {
                    return unit5;
                }
                String pkgName5 = name.substring(0, index5);
                if (pkgName5.equals(unit5.getPackageDecl())) {
                    return unit5;
                }
                return null;
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addCompilationUnit(CompilationUnit unit) {
        try {
            if (unit.pathName() != null) {
                String fileName = new File(unit.pathName()).getCanonicalPath();
                this.loadedCompilationUnit.put(fileName, unit);
            }
        } catch (IOException e) {
        }
        refined__Program_addCompilationUnit(unit);
    }

    public List getCompilationUnitList() {
        initPaths();
        return refined__Program_getCompilationUnitList();
    }

    public TypeDecl typeObject() {
        if (this.typeObject_computed) {
            return this.typeObject_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeObject_value = typeObject_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeObject_computed = true;
        }
        return this.typeObject_value;
    }

    private TypeDecl typeObject_compute() {
        return lookupType("java.lang", "Object");
    }

    public TypeDecl typeCloneable() {
        if (this.typeCloneable_computed) {
            return this.typeCloneable_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeCloneable_value = typeCloneable_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeCloneable_computed = true;
        }
        return this.typeCloneable_value;
    }

    private TypeDecl typeCloneable_compute() {
        return lookupType("java.lang", "Cloneable");
    }

    public TypeDecl typeSerializable() {
        if (this.typeSerializable_computed) {
            return this.typeSerializable_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeSerializable_value = typeSerializable_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeSerializable_computed = true;
        }
        return this.typeSerializable_value;
    }

    private TypeDecl typeSerializable_compute() {
        return lookupType("java.io", "Serializable");
    }

    public TypeDecl typeBoolean() {
        if (this.typeBoolean_computed) {
            return this.typeBoolean_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeBoolean_value = typeBoolean_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeBoolean_computed = true;
        }
        return this.typeBoolean_value;
    }

    private TypeDecl typeBoolean_compute() {
        return lookupType("@primitive", "boolean");
    }

    public TypeDecl typeByte() {
        if (this.typeByte_computed) {
            return this.typeByte_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeByte_value = typeByte_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeByte_computed = true;
        }
        return this.typeByte_value;
    }

    private TypeDecl typeByte_compute() {
        return lookupType("@primitive", "byte");
    }

    public TypeDecl typeShort() {
        if (this.typeShort_computed) {
            return this.typeShort_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeShort_value = typeShort_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeShort_computed = true;
        }
        return this.typeShort_value;
    }

    private TypeDecl typeShort_compute() {
        return lookupType("@primitive", "short");
    }

    public TypeDecl typeChar() {
        if (this.typeChar_computed) {
            return this.typeChar_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeChar_value = typeChar_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeChar_computed = true;
        }
        return this.typeChar_value;
    }

    private TypeDecl typeChar_compute() {
        return lookupType("@primitive", "char");
    }

    public TypeDecl typeInt() {
        if (this.typeInt_computed) {
            return this.typeInt_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeInt_value = typeInt_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeInt_computed = true;
        }
        return this.typeInt_value;
    }

    private TypeDecl typeInt_compute() {
        return lookupType("@primitive", "int");
    }

    public TypeDecl typeLong() {
        if (this.typeLong_computed) {
            return this.typeLong_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeLong_value = typeLong_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeLong_computed = true;
        }
        return this.typeLong_value;
    }

    private TypeDecl typeLong_compute() {
        return lookupType("@primitive", "long");
    }

    public TypeDecl typeFloat() {
        if (this.typeFloat_computed) {
            return this.typeFloat_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeFloat_value = typeFloat_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeFloat_computed = true;
        }
        return this.typeFloat_value;
    }

    private TypeDecl typeFloat_compute() {
        return lookupType("@primitive", Jimple.FLOAT);
    }

    public TypeDecl typeDouble() {
        if (this.typeDouble_computed) {
            return this.typeDouble_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeDouble_value = typeDouble_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeDouble_computed = true;
        }
        return this.typeDouble_value;
    }

    private TypeDecl typeDouble_compute() {
        return lookupType("@primitive", "double");
    }

    public TypeDecl typeString() {
        if (this.typeString_computed) {
            return this.typeString_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeString_value = typeString_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeString_computed = true;
        }
        return this.typeString_value;
    }

    private TypeDecl typeString_compute() {
        return lookupType("java.lang", "String");
    }

    public TypeDecl typeVoid() {
        if (this.typeVoid_computed) {
            return this.typeVoid_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeVoid_value = typeVoid_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeVoid_computed = true;
        }
        return this.typeVoid_value;
    }

    private TypeDecl typeVoid_compute() {
        return lookupType("@primitive", Jimple.VOID);
    }

    public TypeDecl typeNull() {
        if (this.typeNull_computed) {
            return this.typeNull_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.typeNull_value = typeNull_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.typeNull_computed = true;
        }
        return this.typeNull_value;
    }

    private TypeDecl typeNull_compute() {
        return lookupType("@primitive", Jimple.NULL);
    }

    public TypeDecl unknownType() {
        if (this.unknownType_computed) {
            return this.unknownType_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.unknownType_value = unknownType_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.unknownType_computed = true;
        }
        return this.unknownType_value;
    }

    private TypeDecl unknownType_compute() {
        return lookupType("@primitive", "Unknown");
    }

    public boolean hasPackage(String packageName) {
        if (this.hasPackage_String_values == null) {
            this.hasPackage_String_values = new HashMap(4);
        }
        if (this.hasPackage_String_values.containsKey(packageName)) {
            return ((Boolean) this.hasPackage_String_values.get(packageName)).booleanValue();
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        boolean hasPackage_String_value = hasPackage_compute(packageName);
        if (isFinal && num == state().boundariesCrossed) {
            this.hasPackage_String_values.put(packageName, Boolean.valueOf(hasPackage_String_value));
        }
        return hasPackage_String_value;
    }

    private boolean hasPackage_compute(String packageName) {
        return isPackage(packageName);
    }

    public TypeDecl lookupType(String packageName, String typeName) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(packageName);
        arrayList.add(typeName);
        if (this.lookupType_String_String_values == null) {
            this.lookupType_String_String_values = new HashMap(4);
        }
        if (this.lookupType_String_String_values.containsKey(arrayList)) {
            return (TypeDecl) this.lookupType_String_String_values.get(arrayList);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        TypeDecl lookupType_String_String_value = lookupType_compute(packageName, typeName);
        if (isFinal && num == state().boundariesCrossed) {
            this.lookupType_String_String_values.put(arrayList, lookupType_String_String_value);
        }
        return lookupType_String_String_value;
    }

    private TypeDecl lookupType_compute(String packageName, String typeName) {
        String fullName = packageName.equals("") ? typeName : String.valueOf(packageName) + "." + typeName;
        for (int i = 0; i < getNumCompilationUnit(); i++) {
            for (int j = 0; j < getCompilationUnit(i).getNumTypeDecl(); j++) {
                TypeDecl type = getCompilationUnit(i).getTypeDecl(j);
                if (type.fullName().equals(fullName)) {
                    return type;
                }
            }
        }
        return lookupLibType(packageName, typeName);
    }

    public TypeDecl lookupLibType(String packageName, String typeName) {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(packageName);
        arrayList.add(typeName);
        if (this.lookupLibType_String_String_values == null) {
            this.lookupLibType_String_String_values = new HashMap(4);
        }
        if (this.lookupLibType_String_String_values.containsKey(arrayList)) {
            return (TypeDecl) this.lookupLibType_String_String_values.get(arrayList);
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        TypeDecl lookupLibType_String_String_value = lookupLibType_compute(packageName, typeName);
        if (isFinal && num == state().boundariesCrossed) {
            this.lookupLibType_String_String_values.put(arrayList, lookupLibType_String_String_value);
        }
        return lookupLibType_String_String_value;
    }

    private TypeDecl lookupLibType_compute(String packageName, String typeName) {
        String fullName = packageName.equals("") ? typeName : String.valueOf(packageName) + "." + typeName;
        if (packageName.equals("@primitive")) {
            PrimitiveCompilationUnit unit = getPrimitiveCompilationUnit();
            if (typeName.equals("boolean")) {
                return unit.typeBoolean();
            }
            if (typeName.equals("byte")) {
                return unit.typeByte();
            }
            if (typeName.equals("short")) {
                return unit.typeShort();
            }
            if (typeName.equals("char")) {
                return unit.typeChar();
            }
            if (typeName.equals("int")) {
                return unit.typeInt();
            }
            if (typeName.equals("long")) {
                return unit.typeLong();
            }
            if (typeName.equals(Jimple.FLOAT)) {
                return unit.typeFloat();
            }
            if (typeName.equals("double")) {
                return unit.typeDouble();
            }
            if (typeName.equals(Jimple.NULL)) {
                return unit.typeNull();
            }
            if (typeName.equals(Jimple.VOID)) {
                return unit.typeVoid();
            }
            if (typeName.equals("Unknown")) {
                return unit.unknownType();
            }
        }
        CompilationUnit libUnit = getLibCompilationUnit(fullName);
        if (libUnit != null) {
            for (int j = 0; j < libUnit.getNumTypeDecl(); j++) {
                TypeDecl type = libUnit.getTypeDecl(j);
                if (type.fullName().equals(fullName)) {
                    return type;
                }
            }
            return null;
        }
        return null;
    }

    public CompilationUnit getLibCompilationUnit(String fullName) {
        if (this.getLibCompilationUnit_String_values == null) {
            this.getLibCompilationUnit_String_values = new HashMap(4);
        }
        if (this.getLibCompilationUnit_String_values.containsKey(fullName)) {
            return (CompilationUnit) this.getLibCompilationUnit_String_values.get(fullName);
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        CompilationUnit getLibCompilationUnit_String_value = getLibCompilationUnit_compute(fullName);
        if (this.getLibCompilationUnit_String_list == null) {
            this.getLibCompilationUnit_String_list = new List();
            this.getLibCompilationUnit_String_list.is$Final = true;
            this.getLibCompilationUnit_String_list.setParent(this);
        }
        this.getLibCompilationUnit_String_list.add(getLibCompilationUnit_String_value);
        if (getLibCompilationUnit_String_value != null) {
            getLibCompilationUnit_String_value.is$Final = true;
        }
        this.getLibCompilationUnit_String_values.put(fullName, getLibCompilationUnit_String_value);
        return getLibCompilationUnit_String_value;
    }

    private CompilationUnit getLibCompilationUnit_compute(String fullName) {
        return getCompilationUnit(fullName);
    }

    public PrimitiveCompilationUnit getPrimitiveCompilationUnit() {
        if (this.getPrimitiveCompilationUnit_computed) {
            return this.getPrimitiveCompilationUnit_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.getPrimitiveCompilationUnit_value = getPrimitiveCompilationUnit_compute();
        this.getPrimitiveCompilationUnit_value.setParent(this);
        this.getPrimitiveCompilationUnit_value.is$Final = true;
        this.getPrimitiveCompilationUnit_computed = true;
        return this.getPrimitiveCompilationUnit_value;
    }

    private PrimitiveCompilationUnit getPrimitiveCompilationUnit_compute() {
        PrimitiveCompilationUnit u = new PrimitiveCompilationUnit();
        u.setPackageDecl("@primitive");
        return u;
    }

    public ConstructorDecl unknownConstructor() {
        if (this.unknownConstructor_computed) {
            return this.unknownConstructor_value;
        }
        ASTNode.State state = state();
        int num = state.boundariesCrossed;
        boolean isFinal = is$Final();
        this.unknownConstructor_value = unknownConstructor_compute();
        if (isFinal && num == state().boundariesCrossed) {
            this.unknownConstructor_computed = true;
        }
        return this.unknownConstructor_value;
    }

    private ConstructorDecl unknownConstructor_compute() {
        return (ConstructorDecl) unknownType().constructors().iterator().next();
    }

    public WildcardsCompilationUnit wildcards() {
        if (this.wildcards_computed) {
            return this.wildcards_value;
        }
        ASTNode.State state = state();
        int i = state.boundariesCrossed;
        is$Final();
        this.wildcards_value = wildcards_compute();
        this.wildcards_value.setParent(this);
        this.wildcards_value.is$Final = true;
        this.wildcards_computed = true;
        return this.wildcards_value;
    }

    private WildcardsCompilationUnit wildcards_compute() {
        return new WildcardsCompilationUnit("wildcards", new List(), new List());
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_superType(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public ConstructorDecl Define_ConstructorDecl_constructorDecl(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_componentType(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return unknownType();
    }

    @Override // soot.JastAddJ.ASTNode
    public LabeledStmt Define_LabeledStmt_lookupLabel(ASTNode caller, ASTNode child, String name) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        getIndexOfChild(caller);
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        getIndexOfChild(caller);
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeException(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return lookupType("java.lang", "Exception");
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeRuntimeException(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return lookupType("java.lang", "RuntimeException");
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeError(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return lookupType("java.lang", "Error");
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeNullPointerException(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return lookupType("java.lang", "NullPointerException");
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeThrowable(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return lookupType("java.lang", "Throwable");
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
        getIndexOfChild(caller);
        throw new Error("Operation handlesException not supported");
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupConstructor(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return Collections.EMPTY_LIST;
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupSuperConstructor(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return Collections.EMPTY_LIST;
    }

    @Override // soot.JastAddJ.ASTNode
    public Expr Define_Expr_nestedScope(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        throw new UnsupportedOperationException();
    }

    @Override // soot.JastAddJ.ASTNode
    public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
        getIndexOfChild(caller);
        return Collections.EMPTY_LIST;
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeObject(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeObject();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeCloneable(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeCloneable();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeSerializable(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeSerializable();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeBoolean(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeBoolean();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeByte(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeByte();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeShort(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeShort();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeChar(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeChar();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeInt(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeInt();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeLong(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeLong();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeFloat(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeFloat();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeDouble(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeDouble();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeString(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeString();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeVoid(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeVoid();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeNull(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeNull();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_unknownType(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return unknownType();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
        getIndexOfChild(caller);
        return hasPackage(packageName);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_lookupType(ASTNode caller, ASTNode child, String packageName, String typeName) {
        getIndexOfChild(caller);
        return lookupType(packageName, typeName);
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        getIndexOfChild(caller);
        return SimpleSet.emptySet;
    }

    @Override // soot.JastAddJ.ASTNode
    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        getIndexOfChild(caller);
        return SimpleSet.emptySet;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBePublic(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeProtected(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBePrivate(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeVolatile(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeTransient(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeStrictfp(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeSynchronized(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayBeNative(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode Define_ASTNode_enclosingBlock(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        throw new UnsupportedOperationException("outerScope() not defined");
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_insideSwitch(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public Case Define_Case_bind(ASTNode caller, ASTNode child, Case c) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public String Define_String_typeDeclIndent(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return "";
    }

    @Override // soot.JastAddJ.ASTNode
    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return NameType.NO_NAME;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isAnonymous(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public Variable Define_Variable_unknownField(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return unknownType().findSingleVariable("unknown");
    }

    @Override // soot.JastAddJ.ASTNode
    public MethodDecl Define_MethodDecl_unknownMethod(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        Iterator iter = unknownType().memberMethods("unknown").iterator();
        if (iter.hasNext()) {
            MethodDecl m = (MethodDecl) iter.next();
            return m;
        }
        throw new Error("Could not find method unknown in type Unknown");
    }

    @Override // soot.JastAddJ.ASTNode
    public ConstructorDecl Define_ConstructorDecl_unknownConstructor(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return unknownConstructor();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public BodyDecl Define_BodyDecl_enclosingBodyDecl(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_switchType(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return unknownType();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_returnType(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeVoid();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public String Define_String_methodHost(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        throw new Error("Needs extra equation for methodHost()");
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return true;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isExceptionHandlerParameter(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public ElementValue Define_ElementValue_lookupElementTypeValue(ASTNode caller, ASTNode child, String name) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_withinSuppressWarnings(ASTNode caller, ASTNode child, String s) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_withinDeprecatedAnnotation(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public Annotation Define_Annotation_lookupAnnotation(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_enclosingAnnotationDecl(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return unknownType();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return typeNull();
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_inExtendsOrImplements(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_typeWildcard(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return wildcards().typeWildcard();
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_lookupWildcardExtends(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
        getIndexOfChild(caller);
        return wildcards().lookupWildcardExtends(typeDecl);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_lookupWildcardSuper(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
        getIndexOfChild(caller);
        return wildcards().lookupWildcardSuper(typeDecl);
    }

    @Override // soot.JastAddJ.ASTNode
    public LUBType Define_LUBType_lookupLUBType(ASTNode caller, ASTNode child, Collection bounds) {
        getIndexOfChild(caller);
        return wildcards().lookupLUBType(bounds);
    }

    @Override // soot.JastAddJ.ASTNode
    public GLBType Define_GLBType_lookupGLBType(ASTNode caller, ASTNode child, ArrayList bounds) {
        getIndexOfChild(caller);
        return wildcards().lookupGLBType(bounds);
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_genericDecl(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_variableArityValid(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
        if (caller == getCompilationUnitListNoTransform()) {
            caller.getIndexOfChild(child);
            throw new Error("condition_false_label not implemented");
        }
        return getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
        if (caller == getCompilationUnitListNoTransform()) {
            caller.getIndexOfChild(child);
            throw new Error("condition_true_label not implemented");
        }
        return getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public int Define_int_localNum(ASTNode caller, ASTNode child) {
        if (caller == getCompilationUnitListNoTransform()) {
            caller.getIndexOfChild(child);
            return 0;
        }
        return getParent().Define_int_localNum(this, caller);
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_enclosedByExceptionHandler(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public ArrayList Define_ArrayList_exceptionRanges(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isCatchParam(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public CatchClause Define_CatchClause_catchClause(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        throw new IllegalStateException("Could not find parent catch clause");
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_resourcePreviouslyDeclared(ASTNode caller, ASTNode child, String name) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public ClassInstanceExpr Define_ClassInstanceExpr_getClassInstanceExpr(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return null;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isAnonymousDecl(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public boolean Define_boolean_isExplicitGenericConstructorAccess(ASTNode caller, ASTNode child) {
        getIndexOfChild(caller);
        return false;
    }

    @Override // soot.JastAddJ.ASTNode
    public ASTNode rewriteTo() {
        return super.rewriteTo();
    }
}
