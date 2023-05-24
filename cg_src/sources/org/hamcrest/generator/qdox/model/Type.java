package org.hamcrest.generator.qdox.model;

import java.io.Serializable;
import org.hamcrest.generator.qdox.JavaClassContext;
import org.hamcrest.generator.qdox.parser.structs.TypeDef;
import org.hamcrest.generator.qdox.parser.structs.WildcardTypeDef;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/Type.class */
public class Type implements Comparable, Serializable {
    public static final Type[] EMPTY_ARRAY = new Type[0];
    public static final Type VOID = new Type(Jimple.VOID);
    private String name;
    private JavaClassParent context;
    private String fullName;
    private int dimensions;
    private Type[] actualArgumentTypes;

    public Type(String fullName, String name, int dimensions, JavaClassParent context) {
        this.fullName = fullName;
        this.name = name;
        this.dimensions = dimensions;
        this.context = context;
    }

    public Type(String fullName, TypeDef typeDef, int dimensions, JavaClassParent context) {
        this.fullName = fullName;
        this.name = typeDef.name;
        this.dimensions = typeDef.dimensions + dimensions;
        if (typeDef.actualArgumentTypes != null && !typeDef.actualArgumentTypes.isEmpty()) {
            this.actualArgumentTypes = new Type[typeDef.actualArgumentTypes.size()];
            for (int index = 0; index < typeDef.actualArgumentTypes.size(); index++) {
                this.actualArgumentTypes[index] = createUnresolved((TypeDef) typeDef.actualArgumentTypes.get(index), context);
            }
        }
        this.context = context;
    }

    public Type(String fullName, int dimensions, JavaClassParent context) {
        this(fullName, (String) null, dimensions, context);
    }

    public Type(String fullName, int dimensions) {
        this(fullName, dimensions, null);
    }

    public Type(String fullName) {
        this(fullName, 0);
    }

    public static Type createUnresolved(String name, int dimensions, JavaClassParent context) {
        return new Type((String) null, name, dimensions, context);
    }

    public static Type createUnresolved(TypeDef typeDef, int dimensions, JavaClassParent context) {
        return new Type((String) null, typeDef, dimensions, context);
    }

    public static Type createUnresolved(TypeDef typeDef, JavaClassParent context) {
        if (typeDef instanceof WildcardTypeDef) {
            return new WildcardType((WildcardTypeDef) typeDef, context);
        }
        return new Type((String) null, typeDef, 0, context);
    }

    public JavaClassParent getJavaClassParent() {
        return this.context;
    }

    public String getFullQualifiedName() {
        return getFullyQualifiedName();
    }

    public String getFullyQualifiedName() {
        return isResolved() ? this.fullName : this.name;
    }

    public String getValue() {
        String fqn = getFullyQualifiedName();
        return fqn == null ? "" : fqn.replaceAll("\\$", ".");
    }

    public String getGenericValue() {
        StringBuffer result = new StringBuffer(getValue());
        if (this.actualArgumentTypes != null && this.actualArgumentTypes.length > 0) {
            result.append("<");
            for (int index = 0; index < this.actualArgumentTypes.length; index++) {
                result.append(this.actualArgumentTypes[index].getGenericValue());
                if (index + 1 != this.actualArgumentTypes.length) {
                    result.append(",");
                }
            }
            result.append(">");
        }
        for (int i = 0; i < this.dimensions; i++) {
            result.append("[]");
        }
        return result.toString();
    }

    protected String getGenericValue(TypeVariable[] typeVariableList) {
        StringBuffer result = new StringBuffer(getResolvedValue(typeVariableList));
        if (this.actualArgumentTypes != null && this.actualArgumentTypes.length > 0) {
            for (int index = 0; index < this.actualArgumentTypes.length; index++) {
                result.append(this.actualArgumentTypes[index].getResolvedGenericValue(typeVariableList));
                if (index + 1 != this.actualArgumentTypes.length) {
                    result.append(",");
                }
            }
        }
        return result.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getResolvedValue(TypeVariable[] typeParameters) {
        String result = getValue();
        int typeIndex = 0;
        while (true) {
            if (typeIndex >= typeParameters.length) {
                break;
            } else if (!typeParameters[typeIndex].getName().equals(getValue())) {
                typeIndex++;
            } else {
                result = typeParameters[typeIndex].getValue();
                break;
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getResolvedGenericValue(TypeVariable[] typeParameters) {
        String result = getGenericValue(typeParameters);
        int typeIndex = 0;
        while (true) {
            if (typeIndex >= typeParameters.length) {
                break;
            } else if (!typeParameters[typeIndex].getName().equals(getValue())) {
                typeIndex++;
            } else {
                result = typeParameters[typeIndex].getGenericValue();
                break;
            }
        }
        return result;
    }

    public boolean isResolved() {
        if (this.fullName == null && this.context != null) {
            this.fullName = this.context.resolveType(this.name);
        }
        return this.fullName != null;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object o) {
        if (!(o instanceof Type)) {
            return 0;
        }
        return getValue().compareTo(((Type) o).getValue());
    }

    public boolean isArray() {
        return this.dimensions > 0;
    }

    public int getDimensions() {
        return this.dimensions;
    }

    public Type[] getActualTypeArguments() {
        return this.actualArgumentTypes;
    }

    public String toString() {
        if (this.dimensions == 0) {
            return getValue();
        }
        StringBuffer buff = new StringBuffer(getValue());
        for (int i = 0; i < this.dimensions; i++) {
            buff.append("[]");
        }
        String result = buff.toString();
        return result;
    }

    public String toGenericString() {
        if (this.dimensions == 0) {
            return getGenericValue();
        }
        StringBuffer buff = new StringBuffer(getGenericValue());
        for (int i = 0; i < this.dimensions; i++) {
            buff.append("[]");
        }
        String result = buff.toString();
        return result;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Type t = (Type) obj;
        return getValue().equals(t.getValue()) && t.getDimensions() == getDimensions();
    }

    public int hashCode() {
        return getValue().hashCode();
    }

    public JavaClass getJavaClass() {
        JavaClass result = null;
        JavaClassParent javaClassParent = getJavaClassParent();
        if (javaClassParent != null) {
            result = javaClassParent.getNestedClassByName(getFullyQualifiedName());
            if (result == null) {
                JavaClassContext context = javaClassParent.getJavaClassContext();
                if (context.getClassLibrary() != null) {
                    result = context.getClassByName(getFullyQualifiedName());
                }
            }
        }
        return result;
    }

    public boolean isA(Type type) {
        if (equals(type)) {
            return true;
        }
        JavaClass javaClass = getJavaClass();
        if (javaClass != null) {
            Type[] implementz = javaClass.getImplements();
            for (Type type2 : implementz) {
                if (type2.isA(type)) {
                    return true;
                }
            }
            Type supertype = javaClass.getSuperClass();
            if (supertype != null && supertype.isA(type)) {
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isPrimitive() {
        String value = getValue();
        if (value == null || value.length() == 0 || value.indexOf(46) > -1) {
            return false;
        }
        return Jimple.VOID.equals(value) || "boolean".equals(value) || "byte".equals(value) || "char".equals(value) || "short".equals(value) || "int".equals(value) || "long".equals(value) || Jimple.FLOAT.equals(value) || "double".equals(value);
    }

    public boolean isVoid() {
        return Jimple.VOID.equals(getValue());
    }

    protected int getTypeVariableIndex(JavaClass superClass) {
        TypeVariable[] typeVariables = superClass.getTypeParameters();
        for (int typeIndex = 0; typeIndex < typeVariables.length; typeIndex++) {
            if (typeVariables[typeIndex].getFullyQualifiedName().equals(getFullyQualifiedName())) {
                return typeIndex;
            }
        }
        return -1;
    }

    protected Type resolve(JavaClass parentClass) {
        return resolve(parentClass, parentClass);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Type resolve(JavaClass parentClass, JavaClass subclass) {
        Type result = this;
        int typeIndex = getTypeVariableIndex(parentClass);
        if (typeIndex >= 0) {
            String fqn = parentClass.getFullyQualifiedName();
            if (subclass.getSuperClass() != null && fqn.equals(subclass.getSuperClass().getFullyQualifiedName())) {
                result = subclass.getSuperClass().getActualTypeArguments()[typeIndex];
            } else if (subclass.getImplementedInterfaces() != null) {
                int i = 0;
                while (true) {
                    if (i < subclass.getImplementedInterfaces().length) {
                        if (!fqn.equals(subclass.getImplements()[i].getFullyQualifiedName())) {
                            i++;
                        } else {
                            result = subclass.getImplements()[i].getActualTypeArguments()[typeIndex].resolve(subclass.getImplementedInterfaces()[i]);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        if (this.actualArgumentTypes != null) {
            result = new Type(this.fullName, this.name, this.dimensions, this.context);
            result.actualArgumentTypes = new Type[this.actualArgumentTypes.length];
            for (int i2 = 0; i2 < getActualTypeArguments().length; i2++) {
                result.actualArgumentTypes[i2] = this.actualArgumentTypes[i2].resolve(parentClass, subclass);
            }
        }
        return result;
    }
}
