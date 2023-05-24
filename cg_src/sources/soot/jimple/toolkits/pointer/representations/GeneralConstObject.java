package soot.jimple.toolkits.pointer.representations;

import soot.G;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/representations/GeneralConstObject.class */
public class GeneralConstObject extends ConstantObject {
    private Type type;
    private String name;
    private int id;

    public GeneralConstObject(Type t, String n) {
        this.type = t;
        this.name = n;
        G v = G.v();
        int i = v.GeneralConstObject_counter;
        v.GeneralConstObject_counter = i + 1;
        this.id = i;
    }

    @Override // soot.jimple.toolkits.pointer.representations.AbstractObject
    public Type getType() {
        return this.type;
    }

    @Override // soot.jimple.toolkits.pointer.representations.ConstantObject, soot.jimple.toolkits.pointer.representations.AbstractObject
    public String toString() {
        return this.name;
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(Object other) {
        return this == other;
    }
}
