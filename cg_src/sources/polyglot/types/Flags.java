package polyglot.types;

import java.io.Serializable;
import polyglot.util.InternalCompilerError;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/Flags.class */
public class Flags implements Serializable {
    static final int[] print_order = new int[64];
    static int next_bit = 0;
    static final String[] flag_names = new String[64];
    static final String[] c_body_flag_names = new String[64];
    static final String[] c_head_flag_names = new String[64];
    public static final Flags NONE = new Flags(0);
    public static final Flags PUBLIC = createFlag(Jimple.PUBLIC, "", "", null);
    public static final Flags PRIVATE = createFlag(Jimple.PRIVATE, "", "", null);
    public static final Flags PROTECTED = createFlag(Jimple.PROTECTED, "", "", null);
    public static final Flags STATIC = createFlag(Jimple.STATIC, Jimple.STATIC, "", null);
    public static final Flags FINAL = createFlag(Jimple.FINAL, "", "", null);
    public static final Flags SYNCHRONIZED = createFlag(Jimple.SYNCHRONIZED, "", "", null);
    public static final Flags TRANSIENT = createFlag(Jimple.TRANSIENT, "", "", null);
    public static final Flags NATIVE = createFlag(Jimple.NATIVE, "", "", null);
    public static final Flags INTERFACE = createFlag("interface", "", "", null);
    public static final Flags ABSTRACT = createFlag(Jimple.ABSTRACT, "", "", null);
    public static final Flags VOLATILE = createFlag(Jimple.VOLATILE, "", "", null);
    public static final Flags STRICTFP = createFlag(Jimple.STRICTFP, "", "", null);
    protected static final Flags ACCESS_FLAGS = PUBLIC.set(PRIVATE).set(PROTECTED);
    protected long bits;

    public static Flags createFlag(String name, Flags after) {
        return createFlag(name, "", "", after);
    }

    public static Flags createFlag(String name, String cHeadName, String cBodyName, Flags after) {
        if (next_bit >= flag_names.length) {
            throw new InternalCompilerError("too many flags");
        }
        if (print_order[next_bit] != 0) {
            throw new InternalCompilerError("print_order and next_bit inconsistent");
        }
        if (flag_names[next_bit] != null) {
            throw new InternalCompilerError("flag_names and next_bit inconsistent");
        }
        int bit = next_bit;
        next_bit = bit + 1;
        flag_names[bit] = name;
        c_head_flag_names[bit] = cHeadName;
        c_body_flag_names[bit] = cBodyName;
        if (after == null) {
            print_order[bit] = bit;
        } else {
            for (int i = bit; i > 0 && (after.bits & print_order[i]) == 0; i--) {
                print_order[i] = print_order[i - 1];
                print_order[i - 1] = bit;
            }
        }
        return new Flags(1 << bit);
    }

    protected Flags(long bits) {
        this.bits = bits;
    }

    public Flags set(Flags other) {
        return new Flags(this.bits | other.bits);
    }

    public Flags clear(Flags other) {
        return new Flags(this.bits & (other.bits ^ (-1)));
    }

    public Flags retain(Flags other) {
        return new Flags(this.bits & other.bits);
    }

    public boolean intersects(Flags other) {
        return (this.bits & other.bits) != 0;
    }

    public boolean contains(Flags other) {
        return (this.bits & other.bits) == other.bits;
    }

    public Flags Public() {
        return set(PUBLIC);
    }

    public Flags clearPublic() {
        return clear(PUBLIC);
    }

    public boolean isPublic() {
        return contains(PUBLIC);
    }

    public Flags Private() {
        return set(PRIVATE);
    }

    public Flags clearPrivate() {
        return clear(PRIVATE);
    }

    public boolean isPrivate() {
        return contains(PRIVATE);
    }

    public Flags Protected() {
        return set(PROTECTED);
    }

    public Flags clearProtected() {
        return clear(PROTECTED);
    }

    public boolean isProtected() {
        return contains(PROTECTED);
    }

    public Flags Package() {
        return clear(ACCESS_FLAGS);
    }

    public boolean isPackage() {
        return !intersects(ACCESS_FLAGS);
    }

    public Flags Static() {
        return set(STATIC);
    }

    public Flags clearStatic() {
        return clear(STATIC);
    }

    public boolean isStatic() {
        return contains(STATIC);
    }

    public Flags Final() {
        return set(FINAL);
    }

    public Flags clearFinal() {
        return clear(FINAL);
    }

    public boolean isFinal() {
        return contains(FINAL);
    }

    public Flags Synchronized() {
        return set(SYNCHRONIZED);
    }

    public Flags clearSynchronized() {
        return clear(SYNCHRONIZED);
    }

    public boolean isSynchronized() {
        return contains(SYNCHRONIZED);
    }

    public Flags Transient() {
        return set(TRANSIENT);
    }

    public Flags clearTransient() {
        return clear(TRANSIENT);
    }

    public boolean isTransient() {
        return contains(TRANSIENT);
    }

    public Flags Native() {
        return set(NATIVE);
    }

    public Flags clearNative() {
        return clear(NATIVE);
    }

    public boolean isNative() {
        return contains(NATIVE);
    }

    public Flags Interface() {
        return set(INTERFACE);
    }

    public Flags clearInterface() {
        return clear(INTERFACE);
    }

    public boolean isInterface() {
        return contains(INTERFACE);
    }

    public Flags Abstract() {
        return set(ABSTRACT);
    }

    public Flags clearAbstract() {
        return clear(ABSTRACT);
    }

    public boolean isAbstract() {
        return contains(ABSTRACT);
    }

    public Flags Volatile() {
        return set(VOLATILE);
    }

    public Flags clearVolatile() {
        return clear(VOLATILE);
    }

    public boolean isVolatile() {
        return contains(VOLATILE);
    }

    public Flags StrictFP() {
        return set(STRICTFP);
    }

    public Flags clearStrictFP() {
        return clear(STRICTFP);
    }

    public boolean isStrictFP() {
        return contains(STRICTFP);
    }

    public boolean moreRestrictiveThan(Flags f) {
        if (isPrivate() && (f.isProtected() || f.isPackage() || f.isPublic())) {
            return true;
        }
        if (isPackage() && (f.isProtected() || f.isPublic())) {
            return true;
        }
        if (isProtected() && f.isPublic()) {
            return true;
        }
        return false;
    }

    public String toString() {
        return translate().trim();
    }

    public String translate() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < next_bit; i++) {
            int bit = print_order[i];
            if ((this.bits & (1 << bit)) != 0) {
                sb.append(flag_names[bit]);
                sb.append(Instruction.argsep);
            }
        }
        return sb.toString();
    }

    public String translateCBody() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < next_bit; i++) {
            int bit = print_order[i];
            if ((this.bits & (1 << bit)) != 0) {
                sb.append(c_body_flag_names[bit]);
                sb.append(Instruction.argsep);
            }
        }
        return sb.toString();
    }

    public String translateCHead() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < next_bit; i++) {
            int bit = print_order[i];
            if ((this.bits & (1 << bit)) != 0) {
                sb.append(c_head_flag_names[bit]);
                sb.append(Instruction.argsep);
            }
        }
        return sb.toString();
    }

    public int hashCode() {
        return ((int) ((this.bits >> 32) | this.bits)) * 37;
    }

    public boolean equals(Object o) {
        return (o instanceof Flags) && this.bits == ((Flags) o).bits;
    }
}
