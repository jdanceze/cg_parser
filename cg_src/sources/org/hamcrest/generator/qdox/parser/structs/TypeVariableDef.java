package org.hamcrest.generator.qdox.parser.structs;

import java.util.List;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/structs/TypeVariableDef.class */
public class TypeVariableDef {
    public String name;
    public List bounds;

    public TypeVariableDef(String name) {
        this.name = name;
    }

    public TypeVariableDef(String name, List bounds) {
        this.name = name;
        this.bounds = bounds;
    }
}
