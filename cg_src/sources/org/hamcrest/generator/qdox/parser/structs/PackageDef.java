package org.hamcrest.generator.qdox.parser.structs;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/structs/PackageDef.class */
public class PackageDef extends LocatedDef {
    public String name;

    public PackageDef(String name) {
        this.name = "";
        this.name = name;
    }

    public PackageDef(String name, int lineNumber) {
        this.name = "";
        this.name = name;
        this.lineNumber = lineNumber;
    }

    public boolean equals(Object obj) {
        PackageDef packageDef = (PackageDef) obj;
        return packageDef.name.equals(this.name);
    }
}
