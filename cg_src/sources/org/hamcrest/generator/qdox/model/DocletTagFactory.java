package org.hamcrest.generator.qdox.model;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/DocletTagFactory.class */
public interface DocletTagFactory extends Serializable {
    DocletTag createDocletTag(String str, String str2, AbstractBaseJavaEntity abstractBaseJavaEntity, int i);

    DocletTag createDocletTag(String str, String str2);
}
