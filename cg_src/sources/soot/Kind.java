package soot;

import soot.util.Numberable;
/* loaded from: gencallgraphv3.jar:soot/Kind.class */
public final class Kind implements Numberable {
    public static final Kind INVALID = new Kind("INVALID");
    public static final Kind STATIC = new Kind("STATIC");
    public static final Kind VIRTUAL = new Kind("VIRTUAL");
    public static final Kind INTERFACE = new Kind("INTERFACE");
    public static final Kind SPECIAL = new Kind("SPECIAL");
    public static final Kind CLINIT = new Kind("CLINIT");
    public static final Kind GENERIC_FAKE = new Kind("GENERIC_FAKE");
    public static final Kind THREAD = new Kind("THREAD");
    public static final Kind EXECUTOR = new Kind("EXECUTOR");
    public static final Kind ASYNCTASK = new Kind("ASYNCTASK");
    public static final Kind FINALIZE = new Kind("FINALIZE");
    public static final Kind HANDLER = new Kind("HANDLER");
    public static final Kind INVOKE_FINALIZE = new Kind("INVOKE_FINALIZE");
    public static final Kind PRIVILEGED = new Kind("PRIVILEGED");
    public static final Kind NEWINSTANCE = new Kind("NEWINSTANCE");
    public static final Kind REFL_INVOKE = new Kind("REFL_METHOD_INVOKE");
    public static final Kind REFL_CONSTR_NEWINSTANCE = new Kind("REFL_CONSTRUCTOR_NEWINSTANCE");
    public static final Kind REFL_CLASS_NEWINSTANCE = new Kind("REFL_CLASS_NEWINSTANCE");
    private final String name;
    private int num;

    private Kind(String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    @Override // soot.util.Numberable
    public int getNumber() {
        return this.num;
    }

    @Override // soot.util.Numberable
    public void setNumber(int num) {
        this.num = num;
    }

    public String toString() {
        return name();
    }

    public boolean passesParameters() {
        return passesParameters(this);
    }

    public boolean isFake() {
        return isFake(this);
    }

    public boolean isExplicit() {
        return isExplicit(this);
    }

    public boolean isInstance() {
        return isInstance(this);
    }

    public boolean isVirtual() {
        return isVirtual(this);
    }

    public boolean isSpecial() {
        return isSpecial(this);
    }

    public boolean isClinit() {
        return isClinit(this);
    }

    public boolean isStatic() {
        return isStatic(this);
    }

    public boolean isThread() {
        return isThread(this);
    }

    public boolean isExecutor() {
        return isExecutor(this);
    }

    public boolean isAsyncTask() {
        return isAsyncTask(this);
    }

    public boolean isPrivileged() {
        return isPrivileged(this);
    }

    public boolean isReflection() {
        return isReflection(this);
    }

    public boolean isReflInvoke() {
        return isReflInvoke(this);
    }

    public static boolean passesParameters(Kind k) {
        return isExplicit(k) || k == THREAD || k == EXECUTOR || k == ASYNCTASK || k == FINALIZE || k == PRIVILEGED || k == NEWINSTANCE || k == INVOKE_FINALIZE || k == REFL_INVOKE || k == REFL_CONSTR_NEWINSTANCE || k == REFL_CLASS_NEWINSTANCE;
    }

    public static boolean isFake(Kind k) {
        return k == THREAD || k == EXECUTOR || k == ASYNCTASK || k == PRIVILEGED || k == HANDLER || k == GENERIC_FAKE;
    }

    public static boolean isExplicit(Kind k) {
        return isInstance(k) || isStatic(k);
    }

    public static boolean isInstance(Kind k) {
        return k == VIRTUAL || k == INTERFACE || k == SPECIAL;
    }

    public static boolean isVirtual(Kind k) {
        return k == VIRTUAL;
    }

    public static boolean isSpecial(Kind k) {
        return k == SPECIAL;
    }

    public static boolean isClinit(Kind k) {
        return k == CLINIT;
    }

    public static boolean isStatic(Kind k) {
        return k == STATIC;
    }

    public static boolean isThread(Kind k) {
        return k == THREAD;
    }

    public static boolean isExecutor(Kind k) {
        return k == EXECUTOR;
    }

    public static boolean isAsyncTask(Kind k) {
        return k == ASYNCTASK;
    }

    public static boolean isPrivileged(Kind k) {
        return k == PRIVILEGED;
    }

    public static boolean isReflection(Kind k) {
        return k == REFL_CLASS_NEWINSTANCE || k == REFL_CONSTR_NEWINSTANCE || k == REFL_INVOKE;
    }

    public static boolean isReflInvoke(Kind k) {
        return k == REFL_INVOKE;
    }
}
