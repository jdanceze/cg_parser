package org.hamcrest.generator.qdox.parser.structs;

import java.util.List;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/structs/TypeDef.class */
public class TypeDef {
    public String name;
    public int dimensions;
    public List actualArgumentTypes;

    public TypeDef(String name, int dimensions) {
        this.name = name;
        this.dimensions = dimensions;
    }

    public TypeDef(String name) {
        this(name, 0);
    }

    public boolean equals(Object obj) {
        TypeDef typeDef = (TypeDef) obj;
        return typeDef.name.equals(this.name) && typeDef.dimensions == this.dimensions && (typeDef.actualArgumentTypes == null ? this.actualArgumentTypes == null : typeDef.actualArgumentTypes.equals(this.actualArgumentTypes));
    }

    public int hashCode() {
        return this.name.hashCode() + this.dimensions + (this.actualArgumentTypes == null ? 0 : this.actualArgumentTypes.hashCode());
    }
}
