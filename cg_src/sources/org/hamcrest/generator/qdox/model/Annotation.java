package org.hamcrest.generator.qdox.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import org.hamcrest.generator.qdox.model.annotation.AnnotationValue;
import org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/Annotation.class */
public class Annotation implements AnnotationValue, Serializable {
    private final Type type;
    private final int lineNumber;
    private final Map properties;
    private final Map namedParameters;
    private AbstractBaseJavaEntity context;

    public Annotation(Type type, AbstractBaseJavaEntity context, Map namedParameters, int lineNumber) {
        this.properties = new LinkedHashMap();
        this.namedParameters = new LinkedHashMap();
        this.type = type;
        this.context = context;
        this.lineNumber = lineNumber;
        if (this.properties != null) {
            for (Map.Entry entry : this.properties.entrySet()) {
                String name = (String) entry.getKey();
                AnnotationValue value = (AnnotationValue) entry.getValue();
                setProperty(name, value);
            }
        }
    }

    public Annotation(Type type, int line) {
        this(type, null, null, line);
    }

    public void setProperty(String name, AnnotationValue value) {
        this.properties.put(name, value);
        this.namedParameters.put(name, value.getParameterValue());
    }

    public Type getType() {
        return this.type;
    }

    public Object getNamedParameter(String key) {
        return this.namedParameters.get(key);
    }

    public Map getNamedParameterMap() {
        return this.namedParameters;
    }

    public final AbstractBaseJavaEntity getContext() {
        return this.context;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object accept(AnnotationVisitor visitor) {
        return visitor.visitAnnotation(this);
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object getParameterValue() {
        return this;
    }

    public Map getPropertyMap() {
        return this.properties;
    }

    public AnnotationValue getProperty(String name) {
        return (AnnotationValue) this.properties.get(name);
    }

    public void setContext(AbstractBaseJavaEntity context) {
        this.context = context;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append('@');
        result.append(this.type.getValue());
        result.append('(');
        if (!this.namedParameters.isEmpty()) {
            for (Object obj : this.namedParameters.entrySet()) {
                result.append(new StringBuffer().append(obj).append(",").toString());
            }
            result.deleteCharAt(result.length() - 1);
        }
        result.append(')');
        return result.toString();
    }
}
