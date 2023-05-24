package org.hamcrest.generator.qdox.parser.structs;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/structs/TagDef.class */
public class TagDef extends LocatedDef {
    public String name;
    public String text;

    public TagDef(String name, String text, int lineNumber) {
        this.name = name;
        this.text = text;
        this.lineNumber = lineNumber;
    }

    public TagDef(String name, String text) {
        this(name, text, -1);
    }

    public boolean equals(Object obj) {
        TagDef tagDef = (TagDef) obj;
        return tagDef.name.equals(this.name) && tagDef.text.equals(this.text) && tagDef.lineNumber == this.lineNumber;
    }

    public int hashCode() {
        return this.name.hashCode() + this.text.hashCode() + this.lineNumber;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append('@');
        result.append(this.name);
        result.append(" => \"");
        result.append(this.text);
        result.append("\" @ line ");
        result.append(this.lineNumber);
        return result.toString();
    }
}
