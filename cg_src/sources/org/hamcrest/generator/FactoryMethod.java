package org.hamcrest.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/FactoryMethod.class */
public class FactoryMethod {
    private final String matcherClass;
    private final String factoryMethod;
    private final String returnType;
    private String generifiedType;
    private List<Parameter> parameters = new ArrayList();
    private List<String> exceptions = new ArrayList();
    private List<String> genericTypeParameters = new ArrayList();
    private String javaDoc;

    public FactoryMethod(String matcherClass, String factoryMethod, String returnType) {
        this.matcherClass = matcherClass;
        this.factoryMethod = factoryMethod;
        this.returnType = returnType;
    }

    public String getMatcherClass() {
        return this.matcherClass;
    }

    public String getReturnType() {
        return this.returnType;
    }

    public String getName() {
        return this.factoryMethod;
    }

    public void setGenerifiedType(String generifiedType) {
        this.generifiedType = generifiedType;
    }

    public String getGenerifiedType() {
        return this.generifiedType;
    }

    public void addParameter(String type, String name) {
        this.parameters.add(new Parameter(type, name));
    }

    public List<Parameter> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    public void addException(String exception) {
        this.exceptions.add(exception);
    }

    public List<String> getExceptions() {
        return Collections.unmodifiableList(this.exceptions);
    }

    public void addGenericTypeParameter(String genericTypeParameter) {
        this.genericTypeParameters.add(genericTypeParameter);
    }

    public List<String> getGenericTypeParameters() {
        return Collections.unmodifiableList(this.genericTypeParameters);
    }

    public void setJavaDoc(String javaDoc) {
        this.javaDoc = javaDoc;
    }

    public String getJavaDoc() {
        return this.javaDoc;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            FactoryMethod other = (FactoryMethod) obj;
            if (this.exceptions == null) {
                if (other.exceptions != null) {
                    return false;
                }
            } else if (!this.exceptions.equals(other.exceptions)) {
                return false;
            }
            if (this.factoryMethod == null) {
                if (other.factoryMethod != null) {
                    return false;
                }
            } else if (!this.factoryMethod.equals(other.factoryMethod)) {
                return false;
            }
            if (this.genericTypeParameters == null) {
                if (other.genericTypeParameters != null) {
                    return false;
                }
            } else if (!this.genericTypeParameters.equals(other.genericTypeParameters)) {
                return false;
            }
            if (this.generifiedType == null) {
                if (other.generifiedType != null) {
                    return false;
                }
            } else if (!this.generifiedType.equals(other.generifiedType)) {
                return false;
            }
            if (this.javaDoc == null) {
                if (other.javaDoc != null) {
                    return false;
                }
            } else if (!this.javaDoc.equals(other.javaDoc)) {
                return false;
            }
            if (this.matcherClass == null) {
                if (other.matcherClass != null) {
                    return false;
                }
            } else if (!this.matcherClass.equals(other.matcherClass)) {
                return false;
            }
            return this.parameters == null ? other.parameters == null : this.parameters.equals(other.parameters);
        }
        return false;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.exceptions == null ? 0 : this.exceptions.hashCode());
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + (this.factoryMethod == null ? 0 : this.factoryMethod.hashCode()))) + (this.genericTypeParameters == null ? 0 : this.genericTypeParameters.hashCode()))) + (this.generifiedType == null ? 0 : this.generifiedType.hashCode()))) + (this.javaDoc == null ? 0 : this.javaDoc.hashCode()))) + (this.matcherClass == null ? 0 : this.matcherClass.hashCode()))) + (this.parameters == null ? 0 : this.parameters.hashCode());
    }

    public String toString() {
        return "{FactoryMethod: \n  matcherClass = " + this.matcherClass + "\n  factoryMethod = " + this.factoryMethod + "\n  generifiedType = " + this.generifiedType + "\n  parameters = " + this.parameters + "\n  exceptions = " + this.exceptions + "\n  genericTypeParameters = " + this.genericTypeParameters + "\n  javaDoc = " + this.javaDoc + "\n}";
    }

    /* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/FactoryMethod$Parameter.class */
    public static class Parameter {
        private final String type;
        private String name;

        public Parameter(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return this.type;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String toString() {
            return this.type + Instruction.argsep + this.name;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.name == null ? 0 : this.name.hashCode());
            return (31 * result) + (this.type == null ? 0 : this.type.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                Parameter other = (Parameter) obj;
                if (this.name == null) {
                    if (other.name != null) {
                        return false;
                    }
                } else if (!this.name.equals(other.name)) {
                    return false;
                }
                return this.type == null ? other.type == null : this.type.equals(other.type);
            }
            return false;
        }
    }
}
