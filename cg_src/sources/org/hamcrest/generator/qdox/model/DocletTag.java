package org.hamcrest.generator.qdox.model;

import java.io.Serializable;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/DocletTag.class */
public interface DocletTag extends Serializable {
    String getName();

    String getValue();

    String[] getParameters();

    String getNamedParameter(String str);

    Map getNamedParameterMap();

    int getLineNumber();

    AbstractBaseJavaEntity getContext();
}
