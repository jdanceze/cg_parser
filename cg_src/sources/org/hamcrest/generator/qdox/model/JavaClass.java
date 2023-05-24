package org.hamcrest.generator.qdox.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hamcrest.generator.qdox.JavaClassContext;
import org.hamcrest.generator.qdox.model.util.OrderedMap;
import soot.JavaBasicTypes;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/JavaClass.class */
public class JavaClass extends AbstractInheritableJavaEntity implements JavaClassParent {
    private static Type OBJECT = new Type(JavaBasicTypes.JAVA_LANG_OBJECT);
    private static Type ENUM = new Type("java.lang.Enum");
    private static Type ANNOTATION = new Type("java.lang.annotation.Annotation");
    private List methods;
    private JavaMethod[] methodsArray;
    private List fields;
    private JavaField[] fieldsArray;
    private List classes;
    private JavaClass[] classesArray;
    private boolean interfce;
    private boolean isEnum;
    private boolean isAnnotation;
    private Type type;
    private Type superClass;
    private Type[] implementz;
    private TypeVariable[] typeParameters;
    private JavaClassContext context;
    private JavaPackage javaPackage;
    private JavaSource source;

    public JavaClass() {
        this.methods = new LinkedList();
        this.fields = new LinkedList();
        this.classes = new LinkedList();
        this.implementz = new Type[0];
        this.typeParameters = TypeVariable.EMPTY_ARRAY;
    }

    public JavaClass(String name) {
        this.methods = new LinkedList();
        this.fields = new LinkedList();
        this.classes = new LinkedList();
        this.implementz = new Type[0];
        this.typeParameters = TypeVariable.EMPTY_ARRAY;
        setName(name);
    }

    public void setJavaClassContext(JavaClassContext context) {
        this.context = context;
        OBJECT = context.getClassByName(JavaBasicTypes.JAVA_LANG_OBJECT).asType();
    }

    public boolean isInterface() {
        return this.interfce;
    }

    public boolean isEnum() {
        return this.isEnum;
    }

    public Type getSuperClass() {
        boolean iAmJavaLangObject = OBJECT.equals(asType());
        if (this.isEnum) {
            return ENUM;
        }
        if (!this.interfce && !this.isAnnotation && this.superClass == null && !iAmJavaLangObject) {
            return OBJECT;
        }
        return this.superClass;
    }

    public JavaClass getSuperJavaClass() {
        if (getSuperClass() != null) {
            return getSuperClass().getJavaClass();
        }
        return null;
    }

    public Type[] getImplements() {
        return this.implementz;
    }

    public JavaClass[] getImplementedInterfaces() {
        Type[] type = getImplements();
        JavaClass[] result = new JavaClass[type.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = type[i].getJavaClass();
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    protected void writeBody(IndentBuffer result) {
        writeAccessibilityModifier(result);
        writeNonAccessibilityModifiers(result);
        result.write(this.isEnum ? "enum " : this.interfce ? "interface " : this.isAnnotation ? "@interface " : "class ");
        result.write(this.name);
        if (this.superClass != null) {
            result.write(" extends ");
            result.write(this.superClass.getValue());
        }
        if (this.implementz.length > 0) {
            result.write(this.interfce ? " extends " : " implements ");
            for (int i = 0; i < this.implementz.length; i++) {
                if (i > 0) {
                    result.write(", ");
                }
                result.write(this.implementz[i].getValue());
            }
        }
        result.write(" {");
        result.newline();
        result.indent();
        for (JavaField javaField : this.fields) {
            result.newline();
            javaField.write(result);
        }
        for (JavaMethod javaMethod : this.methods) {
            result.newline();
            javaMethod.write(result);
        }
        for (JavaClass javaClass : this.classes) {
            result.newline();
            javaClass.write(result);
        }
        result.deindent();
        result.newline();
        result.write('}');
        result.newline();
    }

    public void setInterface(boolean interfce) {
        this.interfce = interfce;
    }

    public void setEnum(boolean isEnum) {
        this.isEnum = isEnum;
    }

    public void setAnnotation(boolean isAnnotation) {
        this.isAnnotation = isAnnotation;
    }

    public void addMethod(JavaMethod meth) {
        meth.setParentClass(this);
        this.methods.add(meth);
        this.methodsArray = null;
    }

    public void setSuperClass(Type type) {
        if (this.isEnum) {
            throw new IllegalArgumentException("enums cannot extend other classes");
        }
        this.superClass = type;
    }

    public void setImplementz(Type[] implementz) {
        this.implementz = implementz;
    }

    public TypeVariable[] getTypeParameters() {
        return this.typeParameters;
    }

    public void setTypeParameters(TypeVariable[] typeParameters) {
        this.typeParameters = typeParameters;
    }

    public void addField(JavaField javaField) {
        javaField.setParentClass(this);
        this.fields.add(javaField);
        this.fieldsArray = null;
    }

    public void setJavaPackage(JavaPackage javaPackage) {
        this.javaPackage = javaPackage;
    }

    public void setSource(JavaSource source) {
        this.source = source;
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassParent
    public JavaSource getParentSource() {
        return getParentClass() != null ? getParentClass().getParentSource() : this.source;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public JavaSource getSource() {
        return getParentSource();
    }

    public JavaPackage getPackage() {
        return getParentSource() != null ? getParentSource().getPackage() : this.javaPackage;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public JavaClassParent getParent() {
        JavaClassParent result = getParentClass();
        if (result == null) {
            result = getParentSource();
        }
        return result;
    }

    public String getPackageName() {
        JavaPackage javaPackage = getPackage();
        return (javaPackage == null || javaPackage.getName() == null) ? "" : javaPackage.getName();
    }

    public String getFullyQualifiedName() {
        return new StringBuffer().append(getParentClass() != null ? getParentClass().getClassNamePrefix() : getPackage() != null ? new StringBuffer().append(getPackage().getName()).append(".").toString() : "").append(getName()).toString();
    }

    public boolean isInner() {
        return getParentClass() != null;
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassParent
    public String resolveType(String typeName) {
        JavaClass[] innerClasses = getNestedClasses();
        for (int i = 0; i < innerClasses.length; i++) {
            if (innerClasses[i].getName().equals(typeName)) {
                return innerClasses[i].getFullyQualifiedName();
            }
        }
        return getParent().resolveType(typeName);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassParent
    public JavaClassContext getJavaClassContext() {
        return getParent().getJavaClassContext();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassParent
    public String getClassNamePrefix() {
        return new StringBuffer().append(getFullyQualifiedName()).append("$").toString();
    }

    public Type asType() {
        if (this.type == null) {
            this.type = new Type(getFullyQualifiedName(), 0, this);
        }
        return this.type;
    }

    public JavaMethod[] getMethods() {
        if (this.methodsArray == null) {
            this.methodsArray = new JavaMethod[this.methods.size()];
            this.methods.toArray(this.methodsArray);
        }
        return this.methodsArray;
    }

    public JavaMethod[] getMethods(boolean superclasses) {
        if (superclasses) {
            Set signatures = new HashSet();
            List methods = new ArrayList();
            addMethodsFromSuperclassAndInterfaces(signatures, methods, this);
            return (JavaMethod[]) methods.toArray(new JavaMethod[methods.size()]);
        }
        return getMethods();
    }

    private void addMethodsFromSuperclassAndInterfaces(Set signatures, List methodList, JavaClass callingClazz) {
        JavaMethod[] methods = callingClazz.getMethods();
        addNewMethods(signatures, methodList, methods);
        JavaClass superclass = callingClazz.getSuperJavaClass();
        if (superclass != null && superclass != callingClazz) {
            callingClazz.addMethodsFromSuperclassAndInterfaces(signatures, methodList, superclass);
        }
        JavaClass[] implementz = callingClazz.getImplementedInterfaces();
        for (int i = 0; i < implementz.length; i++) {
            if (implementz[i] != null) {
                callingClazz.addMethodsFromSuperclassAndInterfaces(signatures, methodList, implementz[i]);
            }
        }
    }

    private void addNewMethods(Set signatures, List methodList, JavaMethod[] methods) {
        for (JavaMethod method : methods) {
            if (!method.isPrivate()) {
                String signature = method.getDeclarationSignature(false);
                if (!signatures.contains(signature)) {
                    methodList.add(new JavaMethodDelegate(this, method));
                    signatures.add(signature);
                }
            }
        }
    }

    public JavaMethod getMethodBySignature(String name, Type[] parameterTypes) {
        return getMethod(name, parameterTypes, false);
    }

    public JavaMethod getMethod(String name, Type[] parameterTypes, boolean varArgs) {
        JavaMethod[] methods = getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].signatureMatches(name, parameterTypes, varArgs)) {
                return methods[i];
            }
        }
        return null;
    }

    public JavaMethod getMethodBySignature(String name, Type[] parameterTypes, boolean superclasses) {
        return getMethodBySignature(name, parameterTypes, superclasses, false);
    }

    public JavaMethod getMethodBySignature(String name, Type[] parameterTypes, boolean superclasses, boolean varArg) {
        JavaMethod[] result = getMethodsBySignature(name, parameterTypes, superclasses, varArg);
        if (result.length > 0) {
            return result[0];
        }
        return null;
    }

    public JavaMethod[] getMethodsBySignature(String name, Type[] parameterTypes, boolean superclasses) {
        return getMethodsBySignature(name, parameterTypes, superclasses, false);
    }

    public JavaMethod[] getMethodsBySignature(String name, Type[] parameterTypes, boolean superclasses, boolean varArg) {
        JavaMethod method;
        List result = new ArrayList();
        JavaMethod methodInThisClass = getMethod(name, parameterTypes, varArg);
        if (methodInThisClass != null) {
            result.add(methodInThisClass);
        }
        if (superclasses) {
            JavaClass superclass = getSuperJavaClass();
            if (superclass != null && (method = superclass.getMethodBySignature(name, parameterTypes, true, varArg)) != null && !method.isPrivate()) {
                result.add(new JavaMethodDelegate(this, method));
            }
            JavaClass[] implementz = getImplementedInterfaces();
            for (JavaClass javaClass : implementz) {
                JavaMethod method2 = javaClass.getMethodBySignature(name, parameterTypes, true, varArg);
                if (method2 != null) {
                    result.add(new JavaMethodDelegate(this, method2));
                }
            }
        }
        return (JavaMethod[]) result.toArray(new JavaMethod[result.size()]);
    }

    public JavaField[] getFields() {
        if (this.fieldsArray == null) {
            this.fieldsArray = new JavaField[this.fields.size()];
            this.fields.toArray(this.fieldsArray);
        }
        return this.fieldsArray;
    }

    public JavaField getFieldByName(String name) {
        JavaField[] fields = getFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(name)) {
                return fields[i];
            }
        }
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassParent
    public void addClass(JavaClass cls) {
        cls.setParentClass(this);
        this.classes.add(cls);
        this.classesArray = null;
    }

    public JavaClass[] getClasses() {
        return getNestedClasses();
    }

    public JavaClass[] getNestedClasses() {
        if (this.classesArray == null) {
            this.classesArray = new JavaClass[this.classes.size()];
            this.classes.toArray(this.classesArray);
        }
        return this.classesArray;
    }

    @Override // org.hamcrest.generator.qdox.model.JavaClassParent
    public JavaClass getNestedClassByName(String name) {
        JavaClass[] classes = getNestedClasses();
        int separatorIndex = name.indexOf(46);
        String directInnerClassName = separatorIndex > 0 ? name.substring(0, separatorIndex) : name;
        for (JavaClass jClass : classes) {
            if (jClass.getName().equals(directInnerClassName)) {
                if (separatorIndex > 0) {
                    return jClass.getNestedClassByName(name.substring(separatorIndex + 1));
                } else {
                    return jClass;
                }
            }
        }
        return null;
    }

    public JavaClass[] getInnerClasses() {
        return getNestedClasses();
    }

    public JavaClass getInnerClassByName(String name) {
        return getNestedClassByName(name);
    }

    public boolean isA(String fullClassName) {
        Type type = new Type(fullClassName, 0, this);
        return asType().isA(type);
    }

    public boolean isA(JavaClass javaClass) {
        return asType().isA(javaClass.asType());
    }

    public BeanProperty[] getBeanProperties() {
        return getBeanProperties(false);
    }

    public BeanProperty[] getBeanProperties(boolean superclasses) {
        Map beanPropertyMap = getBeanPropertyMap(superclasses);
        Collection beanPropertyCollection = beanPropertyMap.values();
        return (BeanProperty[]) beanPropertyCollection.toArray(new BeanProperty[beanPropertyCollection.size()]);
    }

    private Map getBeanPropertyMap(boolean superclasses) {
        JavaMethod[] methods = getMethods(superclasses);
        Map beanPropertyMap = new OrderedMap();
        for (JavaMethod method : methods) {
            if (method.isPropertyAccessor()) {
                String propertyName = method.getPropertyName();
                BeanProperty beanProperty = getOrCreateProperty(beanPropertyMap, propertyName);
                beanProperty.setAccessor(method);
                beanProperty.setType(method.getPropertyType());
            } else if (method.isPropertyMutator()) {
                String propertyName2 = method.getPropertyName();
                BeanProperty beanProperty2 = getOrCreateProperty(beanPropertyMap, propertyName2);
                beanProperty2.setMutator(method);
                beanProperty2.setType(method.getPropertyType());
            }
        }
        return beanPropertyMap;
    }

    private BeanProperty getOrCreateProperty(Map beanPropertyMap, String propertyName) {
        BeanProperty result = (BeanProperty) beanPropertyMap.get(propertyName);
        if (result == null) {
            result = new BeanProperty(propertyName);
            beanPropertyMap.put(propertyName, result);
        }
        return result;
    }

    public BeanProperty getBeanProperty(String propertyName) {
        return getBeanProperty(propertyName, false);
    }

    public BeanProperty getBeanProperty(String propertyName, boolean superclasses) {
        return (BeanProperty) getBeanPropertyMap(superclasses).get(propertyName);
    }

    public JavaClass[] getDerivedClasses() {
        List result = new ArrayList();
        JavaClass[] classes = this.context.getClasses();
        for (JavaClass clazz : classes) {
            if (clazz.isA(this) && clazz != this) {
                result.add(clazz);
            }
        }
        return (JavaClass[]) result.toArray(new JavaClass[result.size()]);
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractInheritableJavaEntity
    public DocletTag[] getTagsByName(String name, boolean superclasses) {
        List result = new ArrayList();
        addTagsRecursive(result, this, name, superclasses);
        return (DocletTag[]) result.toArray(new DocletTag[result.size()]);
    }

    private void addTagsRecursive(List result, JavaClass javaClass, String name, boolean superclasses) {
        DocletTag[] tags = javaClass.getTagsByName(name);
        addNewTags(result, tags);
        if (superclasses) {
            JavaClass superclass = javaClass.getSuperJavaClass();
            if (superclass != null && superclass != javaClass) {
                addTagsRecursive(result, superclass, name, superclasses);
            }
            JavaClass[] implementz = javaClass.getImplementedInterfaces();
            for (int h = 0; h < implementz.length; h++) {
                if (implementz[h] != null) {
                    addTagsRecursive(result, implementz[h], name, superclasses);
                }
            }
        }
    }

    private void addNewTags(List list, DocletTag[] tags) {
        for (DocletTag superTag : tags) {
            if (!list.contains(superTag)) {
                list.add(superTag);
            }
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(Object o) {
        return getFullyQualifiedName().compareTo(((JavaClass) o).getFullyQualifiedName());
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (asType().isPrimitive() || Type.VOID.equals(asType())) {
            sb.append(asType().getValue());
        } else {
            sb.append(isInterface() ? "interface" : "class");
            sb.append(Instruction.argsep);
            sb.append(getFullyQualifiedName());
        }
        return sb.toString();
    }
}
