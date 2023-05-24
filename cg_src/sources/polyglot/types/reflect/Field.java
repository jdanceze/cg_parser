package polyglot.types.reflect;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import polyglot.types.ClassType;
import polyglot.types.FieldInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/reflect/Field.class */
public class Field {
    DataInputStream in;
    ClassFile clazz;
    int modifiers;
    int name;
    int type;
    Attribute[] attrs;
    ConstantValue constantValue;
    boolean synthetic;

    boolean isString(Type t) {
        return t.isClass() && t.toClass().isTopLevel() && t.toClass().fullName().equals("java.lang.String");
    }

    public int modifiers() {
        return this.modifiers;
    }

    public FieldInstance fieldInstance(TypeSystem ts, ClassType ct) {
        String name = (String) this.clazz.constants[this.name].value();
        String type = (String) this.clazz.constants[this.type].value();
        FieldInstance fi = ts.fieldInstance(ct.position(), ct, ts.flagsForBits(this.modifiers), this.clazz.typeForString(ts, type), name);
        Constant c = constantValue();
        if (c != null) {
            Object o = null;
            try {
                switch (c.tag()) {
                    case 3:
                        o = new Integer(getInt());
                        break;
                    case 4:
                        o = new Float(getFloat());
                        break;
                    case 5:
                        o = new Long(getLong());
                        break;
                    case 6:
                        o = new Double(getDouble());
                        break;
                    case 8:
                        o = getString();
                        break;
                }
                if (o != null) {
                    return fi.constantValue(o);
                }
            } catch (SemanticException e) {
                throw new ClassFormatError("Unexpected constant pool entry.");
            }
        }
        return fi;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSynthetic() {
        return this.synthetic;
    }

    Constant constantValue() {
        if (this.constantValue != null) {
            int index = this.constantValue.index;
            return this.clazz.constants[index];
        }
        return null;
    }

    int getInt() throws SemanticException {
        Constant c = constantValue();
        if (c != null && c.tag() == 3) {
            Integer v = (Integer) c.value();
            return v.intValue();
        }
        throw new SemanticException("Could not find expected constant pool entry with tag INTEGER.");
    }

    float getFloat() throws SemanticException {
        Constant c = constantValue();
        if (c != null && c.tag() == 4) {
            Float v = (Float) c.value();
            return v.floatValue();
        }
        throw new SemanticException("Could not find expected constant pool entry with tag FLOAT.");
    }

    double getDouble() throws SemanticException {
        Constant c = constantValue();
        if (c != null && c.tag() == 6) {
            Double v = (Double) c.value();
            return v.doubleValue();
        }
        throw new SemanticException("Could not find expected constant pool entry with tag DOUBLE.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getLong() throws SemanticException {
        Constant c = constantValue();
        if (c != null && c.tag() == 5) {
            Long v = (Long) c.value();
            return v.longValue();
        }
        throw new SemanticException("Could not find expected constant pool entry with tag LONG.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getString() throws SemanticException {
        Constant c = constantValue();
        if (c != null && c.tag() == 8) {
            Integer i = (Integer) c.value();
            Constant c2 = this.clazz.constants[i.intValue()];
            if (c2 != null && c2.tag() == 1) {
                String v = (String) c2.value();
                return v;
            }
        }
        throw new SemanticException("Could not find expected constant pool entry with tag STRING or UTF8.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String name() {
        return (String) this.clazz.constants[this.name].value();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Field(DataInputStream in, ClassFile clazz) throws IOException {
        this.clazz = clazz;
        this.in = in;
    }

    public void initialize() throws IOException {
        this.modifiers = this.in.readUnsignedShort();
        this.name = this.in.readUnsignedShort();
        this.type = this.in.readUnsignedShort();
        int numAttributes = this.in.readUnsignedShort();
        this.attrs = new Attribute[numAttributes];
        for (int i = 0; i < numAttributes; i++) {
            int nameIndex = this.in.readUnsignedShort();
            int length = this.in.readInt();
            Constant name = this.clazz.constants[nameIndex];
            if (name != null) {
                if ("ConstantValue".equals(name.value())) {
                    this.constantValue = new ConstantValue(this.in, nameIndex, length);
                    this.attrs[i] = this.constantValue;
                }
                if ("Synthetic".equals(name.value())) {
                    this.synthetic = true;
                }
            }
            if (this.attrs[i] == null) {
                long n = this.in.skip(length);
                if (n != length) {
                    throw new EOFException();
                }
            }
        }
    }
}
