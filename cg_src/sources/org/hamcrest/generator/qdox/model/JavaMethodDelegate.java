package org.hamcrest.generator.qdox.model;

import java.util.List;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/JavaMethodDelegate.class */
public class JavaMethodDelegate extends JavaMethod {
    private JavaClass callingClass;
    private JavaMethod originalMethod;

    public JavaMethodDelegate(JavaClass callingClass, JavaMethod originalMethod) {
        this.callingClass = callingClass;
        this.originalMethod = originalMethod;
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public Type getReturnType(boolean resolve) {
        Type returnType = this.originalMethod.getReturnType(resolve, this.callingClass);
        return returnType.resolve(this.originalMethod.getParentClass(), this.callingClass);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public Type getReturnType(boolean resolve, JavaClass _callingClass) {
        return super.getReturnType(resolve, this.callingClass);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public Type[] getParameterTypes(boolean resolve) {
        Type[] parameterTypes = this.originalMethod.getParameterTypes(resolve, this.callingClass);
        for (int paramIndex = 0; paramIndex < parameterTypes.length; paramIndex++) {
            parameterTypes[paramIndex] = parameterTypes[paramIndex].resolve(this.originalMethod.getParentClass(), this.callingClass);
        }
        return parameterTypes;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public Type[] getParameterTypes(boolean resolve, JavaClass _callingClass) {
        return super.getParameterTypes(resolve, this.callingClass);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public void addParameter(JavaParameter javaParameter) {
        this.originalMethod.addParameter(javaParameter);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod, java.lang.Comparable
    public int compareTo(Object o) {
        return this.originalMethod.compareTo(o);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public boolean equals(Object obj) {
        return this.originalMethod.equals(obj);
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public Annotation[] getAnnotations() {
        return this.originalMethod.getAnnotations();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod, org.hamcrest.generator.qdox.model.Member
    public String getCallSignature() {
        return this.originalMethod.getCallSignature();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public String getCodeBlock() {
        return this.originalMethod.getCodeBlock();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public String getComment() {
        return this.originalMethod.getComment();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod, org.hamcrest.generator.qdox.model.Member
    public String getDeclarationSignature(boolean withModifiers) {
        return this.originalMethod.getDeclarationSignature(withModifiers);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public Type[] getExceptions() {
        return this.originalMethod.getExceptions();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public Type getGenericReturnType() {
        return this.originalMethod.getGenericReturnType();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public int getLineNumber() {
        return this.originalMethod.getLineNumber();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public String[] getModifiers() {
        return this.originalMethod.getModifiers();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public String getName() {
        return this.originalMethod.getName();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public String getNamedParameter(String tagName, String parameterName) {
        return this.originalMethod.getNamedParameter(tagName, parameterName);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public JavaParameter getParameterByName(String name) {
        return this.originalMethod.getParameterByName(name);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public JavaParameter[] getParameters() {
        return this.originalMethod.getParameters();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public Type[] getParameterTypes() {
        return this.originalMethod.getParameterTypes();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public JavaClassParent getParent() {
        return this.originalMethod.getParent();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity, org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public JavaClass getParentClass() {
        return this.originalMethod.getParentClass();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public String getPropertyName() {
        return this.originalMethod.getPropertyName();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public Type getPropertyType() {
        return this.originalMethod.getPropertyType();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public Type getReturns() {
        return this.originalMethod.getReturns();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public Type getReturnType() {
        return getReturnType(false);
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public JavaSource getSource() {
        return this.originalMethod.getSource();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public String getSourceCode() {
        return this.originalMethod.getSourceCode();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractInheritableJavaEntity
    public DocletTag getTagByName(String name, boolean inherited) {
        return this.originalMethod.getTagByName(name, inherited);
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public DocletTag getTagByName(String name) {
        return this.originalMethod.getTagByName(name);
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public DocletTag[] getTags() {
        return this.originalMethod.getTags();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod, org.hamcrest.generator.qdox.model.AbstractInheritableJavaEntity
    public DocletTag[] getTagsByName(String name, boolean inherited) {
        return this.originalMethod.getTagsByName(name, inherited);
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public DocletTag[] getTagsByName(String name) {
        return this.originalMethod.getTagsByName(name);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public TypeVariable[] getTypeParameters() {
        return this.originalMethod.getTypeParameters();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public int hashCode() {
        return this.originalMethod.hashCode();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public boolean isAbstract() {
        return this.originalMethod.isAbstract();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public boolean isConstructor() {
        return this.originalMethod.isConstructor();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public boolean isFinal() {
        return this.originalMethod.isFinal();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public boolean isNative() {
        return this.originalMethod.isNative();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public boolean isPrivate() {
        return this.originalMethod.isPrivate();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public boolean isPropertyAccessor() {
        return this.originalMethod.isPropertyAccessor();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public boolean isPropertyMutator() {
        return this.originalMethod.isPropertyMutator();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public boolean isProtected() {
        return this.originalMethod.isProtected();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod, org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public boolean isPublic() {
        return this.originalMethod.isPublic();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public boolean isStatic() {
        return this.originalMethod.isStatic();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public boolean isStrictfp() {
        return this.originalMethod.isStrictfp();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public boolean isSynchronized() {
        return this.originalMethod.isSynchronized();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public boolean isTransient() {
        return this.originalMethod.isTransient();
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public boolean isVarArgs() {
        return this.originalMethod.isVarArgs();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public boolean isVolatile() {
        return this.originalMethod.isVolatile();
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public void setAnnotations(Annotation[] annotations) {
        this.originalMethod.setAnnotations(annotations);
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public void setComment(String comment) {
        this.originalMethod.setComment(comment);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public void setConstructor(boolean constructor) {
        this.originalMethod.setConstructor(constructor);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public void setExceptions(Type[] exceptions) {
        this.originalMethod.setExceptions(exceptions);
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public void setLineNumber(int lineNumber) {
        this.originalMethod.setLineNumber(lineNumber);
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public void setModifiers(String[] modifiers) {
        this.originalMethod.setModifiers(modifiers);
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public void setName(String name) {
        this.originalMethod.setName(name);
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity
    public void setParent(JavaClassParent parent) {
        this.originalMethod.setParent(parent);
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public void setParentClass(JavaClass parentClass) {
        this.originalMethod.setParentClass(parentClass);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public void setReturns(Type returns) {
        this.originalMethod.setReturns(returns);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public void setSourceCode(String sourceCode) {
        this.originalMethod.setSourceCode(sourceCode);
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public void setTags(List tagList) {
        this.originalMethod.setTags(tagList);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public void setTypeParameters(TypeVariable[] typeParameters) {
        this.originalMethod.setTypeParameters(typeParameters);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public boolean signatureMatches(String name, Type[] parameterTypes, boolean varArg) {
        return this.originalMethod.signatureMatches(name, parameterTypes, varArg);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public boolean signatureMatches(String name, Type[] parameterTypes) {
        return this.originalMethod.signatureMatches(name, parameterTypes);
    }

    @Override // org.hamcrest.generator.qdox.model.JavaMethod
    public String toString() {
        return this.originalMethod.toString();
    }
}
