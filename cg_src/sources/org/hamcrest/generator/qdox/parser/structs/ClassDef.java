package org.hamcrest.generator.qdox.parser.structs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/structs/ClassDef.class */
public class ClassDef extends LocatedDef {
    public static final String CLASS = "class";
    public static final String INTERFACE = "interface";
    public static final String ENUM = "enum";
    public static final String ANNOTATION_TYPE = "@interface";
    public String name = "";
    public Set modifiers = new HashSet();
    public List typeParams = new ArrayList();
    public Set extendz = new HashSet();
    public Set implementz = new HashSet();
    public String type = "class";

    public boolean equals(Object obj) {
        ClassDef classDef = (ClassDef) obj;
        return classDef.name.equals(this.name) && classDef.type == this.type && classDef.typeParams.equals(this.typeParams) && classDef.modifiers.equals(this.modifiers) && classDef.extendz.equals(this.extendz) && classDef.implementz.equals(this.implementz);
    }

    public int hashCode() {
        return this.name.hashCode() + this.type.hashCode() + this.typeParams.hashCode() + this.modifiers.hashCode() + this.extendz.hashCode() + this.implementz.hashCode();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(this.modifiers);
        result.append(Instruction.argsep);
        result.append(this.type);
        result.append(Instruction.argsep);
        result.append(this.name);
        result.append(" extends ");
        result.append(this.extendz);
        result.append(" implements ");
        result.append(this.implementz);
        return result.toString();
    }
}
