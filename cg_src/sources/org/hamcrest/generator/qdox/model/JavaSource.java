package org.hamcrest.generator.qdox.model;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.hamcrest.generator.qdox.JavaClassContext;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/JavaSource.class */
public class JavaSource implements Serializable, JavaClassParent {
    private static final Set PRIMITIVE_TYPES = new HashSet();
    private JavaPackage packge;
    private List imports;
    private String[] importsArray;
    private List classes;
    private JavaClass[] classesArray;
    private JavaClassContext context;
    private Map resolvedTypeCache;
    private URL url;

    static {
        PRIMITIVE_TYPES.add("boolean");
        PRIMITIVE_TYPES.add("byte");
        PRIMITIVE_TYPES.add("char");
        PRIMITIVE_TYPES.add("double");
        PRIMITIVE_TYPES.add(Jimple.FLOAT);
        PRIMITIVE_TYPES.add("int");
        PRIMITIVE_TYPES.add("long");
        PRIMITIVE_TYPES.add("short");
        PRIMITIVE_TYPES.add(Jimple.VOID);
    }

    public JavaSource() {
        this(new JavaClassContext((ClassLibrary) null));
    }

    public JavaSource(JavaClassContext context) {
        this.imports = new LinkedList();
        this.classes = new LinkedList();
        this.resolvedTypeCache = new HashMap();
        this.context = context;
    }

    public void setURL(URL url) {
        this.url = url;
    }

    public URL getURL() {
        return this.url;
    }

    public void setFile(File file) {
        try {
            setURL(file.toURL());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public File getFile() {
        return new File(this.url.getFile());
    }

    public JavaPackage getPackage() {
        return this.packge;
    }

    public void setPackage(JavaPackage packge) {
        this.packge = packge;
    }

    public void addImport(String imp) {
        this.imports.add(imp);
        this.importsArray = null;
    }

    public String[] getImports() {
        if (this.importsArray == null) {
            this.importsArray = new String[this.imports.size()];
            this.imports.toArray(this.importsArray);
        }
        return this.importsArray;
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassParent
    public void addClass(JavaClass cls) {
        cls.setSource(this);
        this.classes.add(cls);
        this.classesArray = null;
    }

    public JavaClass[] getClasses() {
        if (this.classesArray == null) {
            this.classesArray = new JavaClass[this.classes.size()];
            this.classes.toArray(this.classesArray);
        }
        return this.classesArray;
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassParent
    public JavaClassContext getJavaClassContext() {
        return this.context;
    }

    public void setClassLibrary(ClassLibrary classLibrary) {
        this.context.setClassLibrary(classLibrary);
    }

    public String getCodeBlock() {
        IndentBuffer result = new IndentBuffer();
        if (this.packge != null) {
            result.write("package ");
            result.write(this.packge.getName());
            result.write(';');
            result.newline();
            result.newline();
        }
        String[] imports = getImports();
        for (int i = 0; imports != null && i < imports.length; i++) {
            result.write("import ");
            result.write(imports[i]);
            result.write(';');
            result.newline();
        }
        if (imports != null && imports.length > 0) {
            result.newline();
        }
        JavaClass[] classes = getClasses();
        for (int i2 = 0; i2 < classes.length; i2++) {
            if (i2 > 0) {
                result.newline();
            }
            classes[i2].write(result);
        }
        return result.toString();
    }

    public String toString() {
        return getCodeBlock();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassParent
    public String resolveType(String typeName) {
        if (this.resolvedTypeCache.containsKey(typeName)) {
            return (String) this.resolvedTypeCache.get(typeName);
        }
        String resolved = resolveTypeInternal(typeName);
        if (resolved != null) {
            this.resolvedTypeCache.put(typeName, resolved);
        }
        return resolved;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x00a7, code lost:
        if (r7 != null) goto L4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String resolveTypeInternal(java.lang.String r6) {
        /*
            r5 = this;
            r0 = 0
            r7 = r0
            java.util.Set r0 = org.hamcrest.generator.qdox.model.JavaSource.PRIMITIVE_TYPES
            r1 = r6
            boolean r0 = r0.contains(r1)
            if (r0 == 0) goto L13
            r0 = r6
            r7 = r0
            goto Lbe
        L13:
            r0 = r6
            r8 = r0
            r0 = r6
            r1 = 46
            r2 = 36
            java.lang.String r0 = r0.replace(r1, r2)
            r9 = r0
            r0 = r6
            r1 = 46
            int r0 = r0.indexOf(r1)
            r10 = r0
            r0 = r10
            if (r0 < 0) goto L34
            r0 = r6
            r1 = 0
            r2 = r10
            java.lang.String r0 = r0.substring(r1, r2)
            r8 = r0
        L34:
            r0 = r5
            r1 = r6
            r2 = r9
            r3 = 1
            java.lang.String r0 = r0.resolveImportedType(r1, r2, r3)
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L44
            goto Lbe
        L44:
            r0 = r5
            r1 = r8
            r2 = r9
            r3 = 0
            java.lang.String r0 = r0.resolveImportedType(r1, r2, r3)
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L54
            goto Lbe
        L54:
            r0 = r5
            r1 = r6
            java.lang.String r0 = r0.resolveFullyQualifiedType(r1)
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L61
            goto Lbe
        L61:
            r0 = r5
            org.hamcrest.generator.qdox.JavaClassContext r0 = r0.context
            org.hamcrest.generator.qdox.model.ClassLibrary r0 = r0.getClassLibrary()
            if (r0 == 0) goto Lad
            r0 = r5
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r2 = r1
            r2.<init>()
            r2 = r5
            java.lang.String r2 = r2.getClassNamePrefix()
            java.lang.StringBuffer r1 = r1.append(r2)
            r2 = r9
            java.lang.StringBuffer r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r0 = r0.resolveFromLibrary(r1)
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L8d
            goto Lbe
        L8d:
            r0 = r5
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r2 = r1
            r2.<init>()
            java.lang.String r2 = "java.lang."
            java.lang.StringBuffer r1 = r1.append(r2)
            r2 = r9
            java.lang.StringBuffer r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r0 = r0.resolveFromLibrary(r1)
            r7 = r0
            r0 = r7
            if (r0 == 0) goto Lad
            goto Lbe
        Lad:
            r0 = r5
            java.lang.String r1 = "*"
            r2 = r9
            r3 = 0
            java.lang.String r0 = r0.resolveImportedType(r1, r2, r3)
            r7 = r0
            r0 = r7
            if (r0 == 0) goto Lbe
            goto Lbe
        Lbe:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.hamcrest.generator.qdox.model.JavaSource.resolveTypeInternal(java.lang.String):java.lang.String");
    }

    private String resolveImportedType(String importSpec, String typeName, boolean fullMatch) {
        String[] imports = getImports();
        String resolvedName = null;
        String dotSuffix = new StringBuffer().append(".").append(importSpec).toString();
        for (int i = 0; i < imports.length && resolvedName == null; i++) {
            if (imports[i].equals(importSpec) || (!fullMatch && imports[i].endsWith(dotSuffix))) {
                String candidateName = new StringBuffer().append(imports[i].substring(0, imports[i].length() - importSpec.length())).append(typeName).toString();
                resolvedName = resolveFullyQualifiedType(candidateName);
                if (resolvedName == null && !"*".equals(importSpec)) {
                    resolvedName = candidateName;
                }
            }
        }
        return resolvedName;
    }

    private String resolveFromLibrary(String typeName) {
        if (this.context.getClassLibrary().contains(typeName)) {
            return typeName;
        }
        return null;
    }

    private String resolveFullyQualifiedType(String typeName) {
        if (this.context.getClassLibrary() != null) {
            int indexOfLastDot = typeName.lastIndexOf(46);
            if (indexOfLastDot >= 0) {
                String root = typeName.substring(0, indexOfLastDot);
                String leaf = typeName.substring(indexOfLastDot + 1);
                String resolvedTypeName = resolveFullyQualifiedType(new StringBuffer().append(root).append("$").append(leaf).toString());
                if (resolvedTypeName != null) {
                    return resolvedTypeName;
                }
            }
            if (this.context.getClassLibrary().contains(typeName)) {
                return typeName;
            }
            return null;
        }
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassParent
    public String getClassNamePrefix() {
        return getPackage() == null ? "" : new StringBuffer().append(getPackage().getName()).append(".").toString();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassParent
    public JavaSource getParentSource() {
        return this;
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassParent
    public JavaClass getNestedClassByName(String name) {
        JavaClass result = null;
        ListIterator i = this.classes.listIterator();
        while (true) {
            if (!i.hasNext()) {
                break;
            }
            JavaClass candidateClass = (JavaClass) i.next();
            if (candidateClass.getName().equals(name)) {
                result = candidateClass;
                break;
            }
        }
        return result;
    }

    public ClassLibrary getClassLibrary() {
        return this.context.getClassLibrary();
    }

    public String getPackageName() {
        return this.packge == null ? "" : this.packge.getName();
    }
}
