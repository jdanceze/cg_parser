package soot;

import soot.coffi.Instruction;
import soot.jimple.paddle.PaddleField;
import soot.jimple.spark.pag.SparkField;
import soot.options.Options;
import soot.tagkit.AbstractHost;
import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/SootField.class */
public class SootField extends AbstractHost implements ClassMember, SparkField, Numberable, PaddleField {
    protected String name;
    protected Type type;
    protected int modifiers;
    protected boolean isDeclared;
    protected SootClass declaringClass;
    protected boolean isPhantom;
    protected volatile String sig;
    protected volatile String subSig;
    private int number;

    public SootField(String name, Type type, int modifiers) {
        this.isDeclared = false;
        this.isPhantom = false;
        this.number = 0;
        if (name == null || type == null) {
            throw new RuntimeException("A SootField cannot have a null name or type.");
        }
        this.name = name;
        this.type = type;
        this.modifiers = modifiers;
    }

    public SootField(String name, Type type) {
        this(name, type, 0);
    }

    public int equivHashCode() {
        return (this.type.hashCode() * 101) + (this.modifiers * 17) + this.name.hashCode();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v8 */
    public String getSignature() {
        if (this.sig == null) {
            ?? r0 = this;
            synchronized (r0) {
                if (this.sig == null) {
                    this.sig = getSignature(getDeclaringClass(), getSubSignature());
                }
                r0 = r0;
            }
        }
        return this.sig;
    }

    public static String getSignature(SootClass cl, String name, Type type) {
        return getSignature(cl, getSubSignature(name, type));
    }

    public static String getSignature(SootClass cl, String subSignature) {
        StringBuilder buffer = new StringBuilder();
        buffer.append('<').append(Scene.v().quotedNameOf(cl.getName())).append(": ");
        buffer.append(subSignature).append('>');
        return buffer.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v8 */
    public String getSubSignature() {
        if (this.subSig == null) {
            ?? r0 = this;
            synchronized (r0) {
                if (this.subSig == null) {
                    this.subSig = getSubSignature(getName(), getType());
                }
                r0 = r0;
            }
        }
        return this.subSig;
    }

    protected static String getSubSignature(String name, Type type) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(type.toQuotedString()).append(' ').append(Scene.v().quotedNameOf(name));
        return buffer.toString();
    }

    @Override // soot.ClassMember
    public SootClass getDeclaringClass() {
        if (!this.isDeclared) {
            throw new RuntimeException("not declared: " + getName() + Instruction.argsep + getType());
        }
        return this.declaringClass;
    }

    public synchronized void setDeclaringClass(SootClass sc) {
        if (sc != null && (this.type instanceof RefLikeType)) {
            Scene.v().getFieldNumberer().add(this);
        }
        this.declaringClass = sc;
        this.sig = null;
    }

    @Override // soot.ClassMember
    public boolean isPhantom() {
        return this.isPhantom;
    }

    @Override // soot.ClassMember
    public void setPhantom(boolean value) {
        if (value) {
            if (!Scene.v().allowsPhantomRefs()) {
                throw new RuntimeException("Phantom refs not allowed");
            }
            if (!Options.v().allow_phantom_elms() && this.declaringClass != null && !this.declaringClass.isPhantom()) {
                throw new RuntimeException("Declaring class would have to be phantom");
            }
        }
        this.isPhantom = value;
    }

    @Override // soot.ClassMember
    public boolean isDeclared() {
        return this.isDeclared;
    }

    public void setDeclared(boolean isDeclared) {
        this.isDeclared = isDeclared;
    }

    public String getName() {
        return this.name;
    }

    public synchronized void setName(String name) {
        if (name != null) {
            this.name = name;
            this.sig = null;
            this.subSig = null;
        }
    }

    @Override // soot.jimple.spark.pag.SparkField
    public Type getType() {
        return this.type;
    }

    public synchronized void setType(Type t) {
        if (t != null) {
            this.type = t;
            this.sig = null;
            this.subSig = null;
        }
    }

    @Override // soot.ClassMember
    public boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    @Override // soot.ClassMember
    public boolean isProtected() {
        return Modifier.isProtected(getModifiers());
    }

    @Override // soot.ClassMember
    public boolean isPrivate() {
        return Modifier.isPrivate(getModifiers());
    }

    @Override // soot.ClassMember
    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    public boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    @Override // soot.ClassMember
    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    @Override // soot.ClassMember
    public int getModifiers() {
        return this.modifiers;
    }

    public String toString() {
        return getSignature();
    }

    private String getOriginalStyleDeclaration() {
        String qualifiers = (String.valueOf(Modifier.toString(this.modifiers)) + ' ' + this.type.toQuotedString()).trim();
        if (qualifiers.isEmpty()) {
            return Scene.v().quotedNameOf(this.name);
        }
        return String.valueOf(qualifiers) + ' ' + Scene.v().quotedNameOf(this.name);
    }

    public String getDeclaration() {
        return getOriginalStyleDeclaration();
    }

    @Override // soot.util.Numberable
    public final int getNumber() {
        return this.number;
    }

    @Override // soot.util.Numberable
    public final void setNumber(int number) {
        this.number = number;
    }

    public SootFieldRef makeRef() {
        return Scene.v().makeFieldRef(this.declaringClass, this.name, this.type, isStatic());
    }
}
