package soot.coffi;

import java.io.PrintStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.JavaBasicTypes;
import soot.RefType;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/coffi/TypeStack.class */
class TypeStack {
    private static final Logger logger = LoggerFactory.getLogger(TypeStack.class);
    private Type[] types;

    private TypeStack() {
    }

    public Object clone() {
        TypeStack newTypeStack = new TypeStack();
        newTypeStack.types = (Type[]) this.types.clone();
        return newTypeStack;
    }

    public static TypeStack v() {
        TypeStack typeStack = new TypeStack();
        typeStack.types = new Type[0];
        return typeStack;
    }

    public TypeStack pop() {
        TypeStack newStack = new TypeStack();
        newStack.types = new Type[this.types.length - 1];
        System.arraycopy(this.types, 0, newStack.types, 0, this.types.length - 1);
        return newStack;
    }

    public TypeStack push(Type type) {
        TypeStack newStack = new TypeStack();
        newStack.types = new Type[this.types.length + 1];
        System.arraycopy(this.types, 0, newStack.types, 0, this.types.length);
        newStack.types[this.types.length] = type;
        return newStack;
    }

    public Type get(int index) {
        return this.types[index];
    }

    public int topIndex() {
        return this.types.length - 1;
    }

    public Type top() {
        if (this.types.length == 0) {
            throw new RuntimeException("TypeStack is empty");
        }
        return this.types[this.types.length - 1];
    }

    public boolean equals(Object object) {
        Type[] typeArr;
        if (object instanceof TypeStack) {
            TypeStack otherStack = (TypeStack) object;
            if (otherStack.types.length != this.types.length) {
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

    public TypeStack merge(TypeStack other) {
        if (this.types.length != other.types.length) {
            throw new RuntimeException("TypeStack merging failed; unequal stack lengths: " + this.types.length + " and " + other.types.length);
        }
        TypeStack newStack = new TypeStack();
        newStack.types = new Type[other.types.length];
        for (int i = 0; i < this.types.length; i++) {
            if (this.types[i].equals(other.types[i])) {
                newStack.types[i] = this.types[i];
            } else if ((!(this.types[i] instanceof ArrayType) && !(this.types[i] instanceof RefType)) || (!(other.types[i] instanceof RefType) && !(other.types[i] instanceof ArrayType))) {
                throw new RuntimeException("TypeStack merging failed; incompatible types " + this.types[i] + " and " + other.types[i]);
            } else {
                newStack.types[i] = RefType.v(JavaBasicTypes.JAVA_LANG_OBJECT);
            }
        }
        return newStack;
    }

    public void print(PrintStream out) {
        for (int i = this.types.length - 1; i >= 0; i--) {
            out.println(String.valueOf(i) + ": " + this.types[i].toString());
        }
        if (this.types.length == 0) {
            out.println("<empty>");
        }
    }
}
