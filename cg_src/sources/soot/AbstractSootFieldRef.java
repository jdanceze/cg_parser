package soot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/AbstractSootFieldRef.class */
public class AbstractSootFieldRef implements SootFieldRef {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSootFieldRef.class);
    private final SootClass declaringClass;
    private final String name;
    private final Type type;
    private final boolean isStatic;

    public AbstractSootFieldRef(SootClass declaringClass, String name, Type type, boolean isStatic) {
        this.declaringClass = declaringClass;
        this.name = name;
        this.type = type;
        this.isStatic = isStatic;
        if (declaringClass == null) {
            throw new RuntimeException("Attempt to create SootFieldRef with null class");
        }
        if (name == null) {
            throw new RuntimeException("Attempt to create SootFieldRef with null name");
        }
        if (type == null) {
            throw new RuntimeException("Attempt to create SootFieldRef with null type");
        }
    }

    @Override // soot.SootFieldRef
    public SootClass declaringClass() {
        return this.declaringClass;
    }

    @Override // soot.SootFieldRef
    public String name() {
        return this.name;
    }

    @Override // soot.SootFieldRef
    public Type type() {
        return this.type;
    }

    @Override // soot.SootFieldRef
    public boolean isStatic() {
        return this.isStatic;
    }

    @Override // soot.SootFieldRef
    public String getSignature() {
        return SootField.getSignature(this.declaringClass, this.name, this.type);
    }

    /* loaded from: gencallgraphv3.jar:soot/AbstractSootFieldRef$FieldResolutionFailedException.class */
    public class FieldResolutionFailedException extends ResolutionFailedException {
        private static final long serialVersionUID = -4657113720516199499L;

        public FieldResolutionFailedException() {
            super("Class " + AbstractSootFieldRef.this.declaringClass + " doesn't have field " + AbstractSootFieldRef.this.name + " : " + AbstractSootFieldRef.this.type + "; failed to resolve in superclasses and interfaces");
        }

        @Override // java.lang.Throwable
        public String toString() {
            StringBuffer ret = new StringBuffer();
            ret.append(super.toString());
            AbstractSootFieldRef.this.resolve(ret);
            return ret.toString();
        }
    }

    @Override // soot.SootFieldRef
    public SootField resolve() {
        return resolve(null);
    }

    private SootField checkStatic(SootField ret) {
        if ((Options.v().wrong_staticness() == 1 || Options.v().wrong_staticness() == 4) && ret.isStatic() != isStatic() && !ret.isPhantom()) {
            throw new ResolutionFailedException("Resolved " + this + " to " + ret + " which has wrong static-ness");
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x013e, code lost:
        if (r7.hasSuperclass() == false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x014f, code lost:
        if (soot.options.Options.v().allow_phantom_refs() == false) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0152, code lost:
        r0 = soot.Scene.v();
        r1 = r5.name;
        r2 = r5.type;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0161, code lost:
        if (r5.isStatic == false) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0164, code lost:
        r3 = 8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0169, code lost:
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x016a, code lost:
        r0 = r0.makeSootField(r1, r2, r3);
        r0.setPhantom(true);
        r0 = r5.declaringClass;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x017a, code lost:
        monitor-enter(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x017b, code lost:
        r0 = r5.declaringClass.getFieldByNameUnsafe(r5.name);
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x018a, code lost:
        if (r0 == null) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0199, code lost:
        if (r0.getType().equals(r5.type) == false) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x019c, code lost:
        r0 = checkStatic(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01a4, code lost:
        monitor-exit(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x01a5, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x01a6, code lost:
        r0 = handleFieldTypeMismatch(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x01ae, code lost:
        monitor-exit(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x01af, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01b0, code lost:
        r5.declaringClass.addField(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x01bb, code lost:
        monitor-exit(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01bc, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x01c2, code lost:
        if (r6 != null) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x01c5, code lost:
        r0 = new soot.AbstractSootFieldRef.FieldResolutionFailedException(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x01d4, code lost:
        if (soot.options.Options.v().ignore_resolution_errors() == false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x01d7, code lost:
        soot.AbstractSootFieldRef.logger.debug(r0.getMessage());
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x01f4, code lost:
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x01f5, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:?, code lost:
        return null;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v34, types: [java.lang.Throwable, soot.SootClass] */
    /* JADX WARN: Type inference failed for: r7v0, types: [java.lang.Throwable, java.lang.Object, soot.SootClass] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public soot.SootField resolve(java.lang.StringBuffer r6) {
        /*
            Method dump skipped, instructions count: 503
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.AbstractSootFieldRef.resolve(java.lang.StringBuffer):soot.SootField");
    }

    protected SootField handleFieldTypeMismatch(SootField clField) {
        switch (Options.v().field_type_mismatches()) {
            case 1:
                throw new ConflictingFieldRefException(clField, this.type);
            case 2:
                return checkStatic(clField);
            case 3:
                return null;
            default:
                throw new RuntimeException(String.format("Unsupported option for handling field type mismatches: %d", Integer.valueOf(Options.v().field_type_mismatches())));
        }
    }

    public String toString() {
        return getSignature();
    }

    public int hashCode() {
        int result = (31 * 1) + (this.declaringClass == null ? 0 : this.declaringClass.hashCode());
        return (31 * ((31 * ((31 * result) + (this.isStatic ? 1231 : 1237))) + (this.name == null ? 0 : this.name.hashCode()))) + (this.type == null ? 0 : this.type.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractSootFieldRef other = (AbstractSootFieldRef) obj;
        if (this.declaringClass == null) {
            if (other.declaringClass != null) {
                return false;
            }
        } else if (!this.declaringClass.equals(other.declaringClass)) {
            return false;
        }
        if (this.isStatic != other.isStatic) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
            return true;
        } else if (!this.type.equals(other.type)) {
            return false;
        } else {
            return true;
        }
    }
}
