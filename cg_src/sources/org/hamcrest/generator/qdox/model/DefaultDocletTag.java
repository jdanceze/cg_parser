package org.hamcrest.generator.qdox.model;

import java.util.Map;
import org.hamcrest.generator.qdox.model.util.TagParser;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/DefaultDocletTag.class */
public class DefaultDocletTag implements DocletTag {
    private final String name;
    private final String value;
    private final int lineNumber;
    private String[] parameters;
    private Map namedParameters;
    private AbstractBaseJavaEntity context;

    public DefaultDocletTag(String name, String value, AbstractBaseJavaEntity context, int lineNumber) {
        this.name = name;
        this.value = value;
        this.context = context;
        this.lineNumber = lineNumber;
    }

    public DefaultDocletTag(String name, String value) {
        this(name, value, null, 0);
    }

    @Override // org.hamcrest.generator.qdox.model.DocletTag
    public String getName() {
        return this.name;
    }

    @Override // org.hamcrest.generator.qdox.model.DocletTag
    public String getValue() {
        return this.value;
    }

    @Override // org.hamcrest.generator.qdox.model.DocletTag
    public String[] getParameters() {
        if (this.parameters == null) {
            this.parameters = TagParser.parseParameters(this.value);
        }
        return this.parameters;
    }

    @Override // org.hamcrest.generator.qdox.model.DocletTag
    public Map getNamedParameterMap() {
        if (this.namedParameters == null) {
            this.namedParameters = TagParser.parseNamedParameters(this.value);
        }
        return this.namedParameters;
    }

    @Override // org.hamcrest.generator.qdox.model.DocletTag
    public String getNamedParameter(String key) {
        return (String) getNamedParameterMap().get(key);
    }

    @Override // org.hamcrest.generator.qdox.model.DocletTag
    public final AbstractBaseJavaEntity getContext() {
        return this.context;
    }

    @Override // org.hamcrest.generator.qdox.model.DocletTag
    public int getLineNumber() {
        return this.lineNumber;
    }
}
