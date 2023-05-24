package org.hamcrest.generator.qdox.model;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import soot.JavaBasicTypes;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/JavaMethod.class */
public class JavaMethod extends AbstractInheritableJavaEntity implements Member {
    private TypeVariable[] typeParameters = TypeVariable.EMPTY_ARRAY;
    private Type returns = Type.VOID;
    private List parameters = new LinkedList();
    private JavaParameter[] parametersArray = JavaParameter.EMPTY_ARRAY;
    private Type[] exceptions = Type.EMPTY_ARRAY;
    private boolean constructor;
    private String sourceCode;
    private boolean varArgs;

    public JavaMethod() {
    }

    public JavaMethod(String name) {
        setName(name);
    }

    public JavaMethod(Type returns, String name) {
        setReturns(returns);
        setName(name);
    }

    public Type getReturns() {
        return this.returns;
    }

    public JavaParameter[] getParameters() {
        if (this.parametersArray == null) {
            this.parametersArray = new JavaParameter[this.parameters.size()];
            this.parameters.toArray(this.parametersArray);
        }
        return this.parametersArray;
    }

    public JavaParameter getParameterByName(String name) {
        JavaParameter[] parameters = getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getName().equals(name)) {
                return parameters[i];
            }
        }
        return null;
    }

    public Type[] getExceptions() {
        return this.exceptions;
    }

    public boolean isConstructor() {
        return this.constructor;
    }

    public boolean isVarArgs() {
        return this.varArgs;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    protected void writeBody(IndentBuffer result) {
        writeBody(result, true, true, true);
    }

    protected void writeBody(IndentBuffer result, boolean withModifiers, boolean isDeclaration, boolean isPrettyPrint) {
        if (withModifiers) {
            writeAccessibilityModifier(result);
            writeNonAccessibilityModifiers(result);
        }
        if (!this.constructor && isDeclaration) {
            result.write(this.returns.toString());
            result.write(' ');
        }
        result.write(this.name);
        result.write('(');
        for (int i = 0; i < getParameters().length; i++) {
            JavaParameter parameter = this.parametersArray[i];
            if (i > 0) {
                result.write(", ");
            }
            if (isDeclaration) {
                result.write(parameter.getType().toString());
                if (parameter.isVarArgs()) {
                    result.write("...");
                }
                result.write(' ');
            }
            result.write(parameter.getName());
        }
        result.write(')');
        if (isDeclaration && this.exceptions.length > 0) {
            result.write(" throws ");
            for (int i2 = 0; i2 < this.exceptions.length; i2++) {
                if (i2 > 0) {
                    result.write(", ");
                }
                result.write(this.exceptions[i2].getValue());
            }
        }
        if (isPrettyPrint) {
            if (this.sourceCode != null && this.sourceCode.length() > 0) {
                result.write(" {");
                result.newline();
                result.write(this.sourceCode);
                result.write("}");
                result.newline();
                return;
            }
            result.write(';');
            result.newline();
        }
    }

    private String getSignature(boolean withModifiers, boolean isDeclaration) {
        IndentBuffer result = new IndentBuffer();
        writeBody(result, withModifiers, isDeclaration, false);
        return result.toString();
    }

    @Override // org.hamcrest.generator.qdox.model.Member
    public String getDeclarationSignature(boolean withModifiers) {
        return getSignature(withModifiers, true);
    }

    @Override // org.hamcrest.generator.qdox.model.Member
    public String getCallSignature() {
        return getSignature(false, false);
    }

    public void setReturns(Type returns) {
        this.returns = returns;
    }

    public void addParameter(JavaParameter javaParameter) {
        javaParameter.setParentMethod(this);
        this.parameters.add(javaParameter);
        this.parametersArray = null;
        this.varArgs = javaParameter.isVarArgs();
    }

    public void setExceptions(Type[] exceptions) {
        this.exceptions = exceptions;
    }

    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        JavaMethod m = (JavaMethod) obj;
        if (m.isConstructor() != isConstructor()) {
            return false;
        }
        if (m.getName() == null) {
            return getName() == null;
        } else if (m.getName().equals(getName())) {
            if (m.getReturns() == null) {
                return getReturns() == null;
            } else if (m.getReturns().equals(getReturns())) {
                JavaParameter[] myParams = getParameters();
                JavaParameter[] otherParams = m.getParameters();
                if (otherParams.length != myParams.length) {
                    return false;
                }
                for (int i = 0; i < myParams.length; i++) {
                    if (!otherParams[i].equals(myParams[i])) {
                        return false;
                    }
                }
                return this.varArgs == m.varArgs;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean signatureMatches(String name, Type[] parameterTypes) {
        return signatureMatches(name, parameterTypes, false);
    }

    public boolean signatureMatches(String name, Type[] parameterTypes, boolean varArg) {
        if (name.equals(this.name)) {
            Type[] parameterTypes2 = parameterTypes == null ? new Type[0] : parameterTypes;
            if (parameterTypes2.length != getParameters().length) {
                return false;
            }
            for (int i = 0; i < this.parametersArray.length; i++) {
                if (!this.parametersArray[i].getType().equals(parameterTypes2[i])) {
                    return false;
                }
            }
            return this.varArgs == varArg;
        }
        return false;
    }

    public int hashCode() {
        int hashCode = this.name.hashCode();
        if (this.returns != null) {
            hashCode *= this.returns.hashCode();
        }
        return hashCode * getParameters().length;
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractJavaEntity
    public boolean isPublic() {
        return super.isPublic() || (getParentClass() != null && getParentClass().isInterface());
    }

    public boolean isPropertyAccessor() {
        if (!isStatic() && getParameters().length == 0) {
            return getName().startsWith("is") ? getName().length() > 2 && Character.isUpperCase(getName().charAt(2)) : getName().startsWith("get") && getName().length() > 3 && Character.isUpperCase(getName().charAt(3));
        }
        return false;
    }

    public boolean isPropertyMutator() {
        return !isStatic() && getParameters().length == 1 && getName().startsWith("set") && getName().length() > 3 && Character.isUpperCase(getName().charAt(3));
    }

    public Type getPropertyType() {
        if (isPropertyAccessor()) {
            return getReturns();
        }
        if (isPropertyMutator()) {
            return getParameters()[0].getType();
        }
        return null;
    }

    public String getPropertyName() {
        int start;
        if (getName().startsWith("get") || getName().startsWith("set")) {
            start = 3;
        } else if (getName().startsWith("is")) {
            start = 2;
        } else {
            return null;
        }
        return Introspector.decapitalize(getName().substring(start));
    }

    @Override // org.hamcrest.generator.qdox.model.AbstractInheritableJavaEntity
    public DocletTag[] getTagsByName(String name, boolean inherited) {
        JavaClass clazz = getParentClass();
        JavaParameter[] params = getParameters();
        Type[] types = new Type[params.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = params[i].getType();
        }
        JavaMethod[] methods = clazz.getMethodsBySignature(getName(), types, true);
        List result = new ArrayList();
        for (JavaMethod method : methods) {
            DocletTag[] tags = method.getTagsByName(name);
            for (DocletTag tag : tags) {
                if (!result.contains(tag)) {
                    result.add(tag);
                }
            }
        }
        return (DocletTag[]) result.toArray(new DocletTag[result.size()]);
    }

    @Override // java.lang.Comparable
    public int compareTo(Object o) {
        return getDeclarationSignature(false).compareTo(((JavaMethod) o).getDeclarationSignature(false));
    }

    public String getSourceCode() {
        return this.sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public void setTypeParameters(TypeVariable[] typeParameters) {
        this.typeParameters = typeParameters;
    }

    public TypeVariable[] getTypeParameters() {
        return this.typeParameters;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        if (isPrivate()) {
            result.append("private ");
        } else if (isProtected()) {
            result.append("protected ");
        } else if (isPublic()) {
            result.append("public ");
        }
        if (isAbstract()) {
            result.append("abstract ");
        }
        if (isStatic()) {
            result.append("static ");
        }
        if (isFinal()) {
            result.append("final ");
        }
        if (isSynchronized()) {
            result.append("synchronized ");
        }
        if (isNative()) {
            result.append("native ");
        }
        result.append(new StringBuffer().append(getReturns().getValue()).append(Instruction.argsep).toString());
        if (getParentClass() != null) {
            result.append(new StringBuffer().append(getParentClass().getFullyQualifiedName()).append(".").toString());
        }
        result.append(getName());
        result.append("(");
        for (int paramIndex = 0; paramIndex < getParameters().length; paramIndex++) {
            if (paramIndex > 0) {
                result.append(",");
            }
            String typeValue = getParameters()[paramIndex].getType().getResolvedValue(getTypeParameters());
            result.append(typeValue);
        }
        result.append(")");
        int i = 0;
        while (i < this.exceptions.length) {
            result.append(i == 0 ? " throws " : ",");
            result.append(this.exceptions[i].getValue());
            i++;
        }
        return result.toString();
    }

    public Type getGenericReturnType() {
        return this.returns;
    }

    public Type getReturnType() {
        return getReturnType(false);
    }

    public Type getReturnType(boolean resolve) {
        return getReturnType(resolve, getParentClass());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Type getReturnType(boolean resolve, JavaClass callingClass) {
        Type result = getReturns().resolve(getParentClass(), callingClass);
        if (!resolve && !this.returns.getFullyQualifiedName().equals(result.getFullyQualifiedName())) {
            result = new Type(JavaBasicTypes.JAVA_LANG_OBJECT);
        }
        return result;
    }

    public Type[] getParameterTypes() {
        return getParameterTypes(false);
    }

    public Type[] getParameterTypes(boolean resolve) {
        return getParameterTypes(resolve, getParentClass());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Type[] getParameterTypes(boolean resolve, JavaClass callingClass) {
        Type[] result = new Type[getParameters().length];
        for (int paramIndex = 0; paramIndex < getParameters().length; paramIndex++) {
            Type curType = getParameters()[paramIndex].getType().resolve(getParentClass(), callingClass);
            if (!resolve && !this.returns.getFullyQualifiedName().equals(curType.getFullyQualifiedName())) {
                result[paramIndex] = new Type(JavaBasicTypes.JAVA_LANG_OBJECT);
            } else {
                result[paramIndex] = curType;
            }
        }
        return result;
    }
}
