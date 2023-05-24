package soot.coffi;

import java.io.PrintStream;
import soot.ArrayType;
import soot.RefType;
import soot.Scene;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/coffi/TypeArray.class */
class TypeArray {
    private Type[] types;

    private TypeArray() {
    }

    public static TypeArray v(int size) {
        TypeArray newArray = new TypeArray();
        newArray.types = new Type[size];
        for (int i = 0; i < size; i++) {
            newArray.types[i] = UnusuableType.v();
        }
        return newArray;
    }

    public Type get(int index) {
        return this.types[index];
    }

    public TypeArray set(int index, Type type) {
        TypeArray newArray = new TypeArray();
        newArray.types = (Type[]) this.types.clone();
        newArray.types[index] = type;
        return newArray;
    }

    public boolean equals(Object obj) {
        Type[] typeArr;
        if (obj instanceof TypeArray) {
            TypeArray other = (TypeArray) obj;
            if (this.types.length != other.types.length) {
                return false;
            }
            for (Type element : this.types) {
                if (!element.equals(element)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public TypeArray merge(TypeArray otherArray) {
        TypeArray newArray = new TypeArray();
        if (this.types.length != otherArray.types.length) {
            throw new RuntimeException("Merging of type arrays failed; unequal array length");
        }
        newArray.types = new Type[this.types.length];
        for (int i = 0; i < this.types.length; i++) {
            if (this.types[i].equals(otherArray.types[i])) {
                newArray.types[i] = this.types[i];
            } else if (((this.types[i] instanceof ArrayType) || (this.types[i] instanceof RefType)) && ((otherArray.types[i] instanceof ArrayType) || (otherArray.types[i] instanceof RefType))) {
                newArray.types[i] = Scene.v().getObjectType();
            } else {
                newArray.types[i] = UnusuableType.v();
            }
        }
        return newArray;
    }

    public void print(PrintStream out) {
        for (int i = 0; i < this.types.length; i++) {
            out.println(String.valueOf(i) + ": " + this.types[i].toString());
        }
    }
}
