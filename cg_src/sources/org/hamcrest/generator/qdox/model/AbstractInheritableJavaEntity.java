package org.hamcrest.generator.qdox.model;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/AbstractInheritableJavaEntity.class */
public abstract class AbstractInheritableJavaEntity extends AbstractJavaEntity {
    public abstract DocletTag[] getTagsByName(String str, boolean z);

    public DocletTag getTagByName(String name, boolean inherited) {
        DocletTag[] tags = getTagsByName(name, inherited);
        if (tags.length > 0) {
            return tags[0];
        }
        return null;
    }
}
