package org.hamcrest.generator.qdox.model;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/BeanProperty.class */
public class BeanProperty {
    private final String name;
    private JavaMethod accessor;
    private JavaMethod mutator;
    private Type type;

    public BeanProperty(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public JavaMethod getAccessor() {
        return this.accessor;
    }

    public void setAccessor(JavaMethod accessor) {
        this.accessor = accessor;
    }

    public JavaMethod getMutator() {
        return this.mutator;
    }

    public void setMutator(JavaMethod mutator) {
        this.mutator = mutator;
    }
}
