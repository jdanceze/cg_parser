package soot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.options.Options;
import soot.util.NumberedString;
/* loaded from: gencallgraphv3.jar:soot/SootMethodRefImpl.class */
public class SootMethodRefImpl implements SootMethodRef {
    private static final Logger logger = LoggerFactory.getLogger(SootMethodRefImpl.class);
    private final SootClass declaringClass;
    private final String name;
    private final List<Type> parameterTypes;
    private final Type returnType;
    private final boolean isStatic;
    private SootMethod resolveCache = null;
    private NumberedString subsig;

    public SootMethodRefImpl(SootClass declaringClass, String name, List<Type> parameterTypes, Type returnType, boolean isStatic) {
        if (declaringClass == null) {
            throw new IllegalArgumentException("Attempt to create SootMethodRef with null class");
        }
        if (name == null) {
            throw new IllegalArgumentException("Attempt to create SootMethodRef with null name");
        }
        if (returnType == null) {
            throw new IllegalArgumentException("Attempt to create SootMethodRef with null returnType (Method: " + name + " at declaring class: " + declaringClass.getName() + ")");
        }
        this.declaringClass = declaringClass;
        this.name = name;
        this.parameterTypes = createParameterTypeList(parameterTypes);
        this.returnType = returnType;
        this.isStatic = isStatic;
    }

    protected List<Type> createParameterTypeList(List<Type> parameterTypes) {
        if (parameterTypes == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList(parameterTypes));
    }

    @Override // soot.SootMethodRef
    public SootClass declaringClass() {
        return getDeclaringClass();
    }

    @Override // soot.SootMethodInterface
    public SootClass getDeclaringClass() {
        return this.declaringClass;
    }

    @Override // soot.SootMethodRef
    public String name() {
        return getName();
    }

    @Override // soot.SootMethodInterface
    public String getName() {
        return this.name;
    }

    @Override // soot.SootMethodRef
    public List<Type> parameterTypes() {
        return getParameterTypes();
    }

    @Override // soot.SootMethodInterface
    public List<Type> getParameterTypes() {
        return this.parameterTypes;
    }

    @Override // soot.SootMethodRef
    public Type returnType() {
        return getReturnType();
    }

    @Override // soot.SootMethodInterface
    public Type getReturnType() {
        return this.returnType;
    }

    @Override // soot.SootMethodInterface
    public boolean isStatic() {
        return this.isStatic;
    }

    @Override // soot.SootMethodRef
    public NumberedString getSubSignature() {
        if (this.subsig != null) {
            return this.subsig;
        }
        if (this.resolveCache != null) {
            this.subsig = this.resolveCache.getNumberedSubSignature();
            return this.subsig;
        }
        this.subsig = Scene.v().getSubSigNumberer().findOrAdd(SootMethod.getSubSignature(this.name, this.parameterTypes, this.returnType));
        return this.subsig;
    }

    @Override // soot.SootMethodInterface
    public String getSignature() {
        return SootMethod.getSignature(this.declaringClass, this.name, this.parameterTypes, this.returnType);
    }

    @Override // soot.SootMethodRef
    public Type parameterType(int i) {
        return getParameterType(i);
    }

    @Override // soot.SootMethodInterface
    public Type getParameterType(int i) {
        return this.parameterTypes.get(i);
    }

    /* loaded from: gencallgraphv3.jar:soot/SootMethodRefImpl$ClassResolutionFailedException.class */
    public class ClassResolutionFailedException extends ResolutionFailedException {
        private static final long serialVersionUID = 5430199603403917938L;

        public ClassResolutionFailedException() {
            super("Class " + SootMethodRefImpl.this.declaringClass + " doesn't have method " + SootMethodRefImpl.this.name + "(" + (SootMethodRefImpl.this.parameterTypes == null ? "" : SootMethodRefImpl.this.parameterTypes) + ") : " + SootMethodRefImpl.this.returnType + "; failed to resolve in superclasses and interfaces");
        }

        @Override // java.lang.Throwable
        public String toString() {
            StringBuilder ret = new StringBuilder(super.toString());
            SootMethodRefImpl.this.resolve(ret);
            return ret.toString();
        }
    }

    @Override // soot.SootMethodRef
    public SootMethod resolve() {
        SootMethod cached = this.resolveCache;
        if (cached == null || !cached.isValidResolve(this)) {
            cached = resolve(null);
            this.resolveCache = cached;
        }
        return cached;
    }

    @Override // soot.SootMethodRef
    public SootMethod tryResolve() {
        return tryResolve(null);
    }

    private void checkStatic(SootMethod method) {
        int opt = Options.v().wrong_staticness();
        if ((opt == 1 || opt == 4) && method.isStatic() != isStatic() && !method.isPhantom()) {
            throw new ResolutionFailedException("Resolved " + this + " to " + method + " which has wrong static-ness");
        }
    }

    protected SootMethod tryResolve(StringBuilder trace) {
        SootClass classToAddTo;
        SootMethod resolved = Scene.v().getOrMakeFastHierarchy().resolveMethod(this.declaringClass, this.declaringClass, this.name, this.parameterTypes, this.returnType, true, getSubSignature());
        if (resolved != null) {
            checkStatic(resolved);
            return resolved;
        }
        if (Scene.v().allowsPhantomRefs()) {
            SootClass sootClass = this.declaringClass;
            while (true) {
                SootClass selectedClass = sootClass;
                if (selectedClass == null) {
                    break;
                } else if (selectedClass.isPhantom()) {
                    SootMethod phantomMethod = Scene.v().makeSootMethod(this.name, this.parameterTypes, this.returnType, isStatic() ? 8 : 0);
                    phantomMethod.setPhantom(true);
                    SootMethod phantomMethod2 = selectedClass.getOrAddMethod(phantomMethod);
                    checkStatic(phantomMethod2);
                    return phantomMethod2;
                } else {
                    sootClass = selectedClass.getSuperclassUnsafe();
                }
            }
        }
        if (Scene.v().allowsPhantomRefs() && Options.v().ignore_resolution_errors()) {
            SootClass sootClass2 = this.declaringClass;
            while (true) {
                classToAddTo = sootClass2;
                if (classToAddTo == null || classToAddTo.isPhantom()) {
                    break;
                }
                sootClass2 = classToAddTo.getSuperclassUnsafe();
            }
            if (classToAddTo == null) {
                classToAddTo = this.declaringClass;
            }
            SootMethod method = Scene.v().makeSootMethod(this.name, this.parameterTypes, this.returnType, isStatic() ? 8 : 0);
            method.setPhantom(true);
            SootMethod method2 = classToAddTo.getOrAddMethod(method);
            checkStatic(method2);
            return method2;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SootMethod resolve(StringBuilder trace) {
        SootMethod resolved = tryResolve(trace);
        if (resolved != null) {
            return resolved;
        }
        if (Options.v().allow_phantom_refs() || SootClass.INVOKEDYNAMIC_DUMMY_CLASS_NAME.equals(this.declaringClass.getName())) {
            return createUnresolvedErrorMethod(this.declaringClass);
        }
        if (trace == null) {
            ClassResolutionFailedException e = new ClassResolutionFailedException();
            if (Options.v().ignore_resolution_errors()) {
                logger.debug(e.getMessage());
                return null;
            }
            throw e;
        }
        return null;
    }

    private SootMethod createUnresolvedErrorMethod(SootClass declaringClass) {
        Jimple jimp = Jimple.v();
        SootMethod m = Scene.v().makeSootMethod(this.name, this.parameterTypes, this.returnType, isStatic() ? 8 : 0);
        int modifiers = 1;
        if (isStatic()) {
            modifiers = 1 | 8;
        }
        m.setModifiers(modifiers);
        JimpleBody body = jimp.newBody(m);
        m.setActiveBody(body);
        LocalGenerator lg = Scene.v().createLocalGenerator(body);
        body.insertIdentityStmts(declaringClass);
        RefType runtimeExceptionType = RefType.v("java.lang.Error");
        if (Options.v().src_prec() == 7) {
            runtimeExceptionType = RefType.v(DotnetBasicTypes.SYSTEM_EXCEPTION);
        }
        Local exceptionLocal = lg.generateLocal(runtimeExceptionType);
        AssignStmt assignStmt = jimp.newAssignStmt(exceptionLocal, jimp.newNewExpr(runtimeExceptionType));
        body.getUnits().add((UnitPatchingChain) assignStmt);
        SootMethodRef cref = Scene.v().makeConstructorRef(runtimeExceptionType.getSootClass(), Collections.singletonList(RefType.v("java.lang.String")));
        if (Options.v().src_prec() == 7) {
            cref = Scene.v().makeConstructorRef(runtimeExceptionType.getSootClass(), Collections.singletonList(RefType.v(DotnetBasicTypes.SYSTEM_STRING)));
        }
        SpecialInvokeExpr constructorInvokeExpr = jimp.newSpecialInvokeExpr(exceptionLocal, cref, StringConstant.v("Unresolved compilation error: Method " + getSignature() + " does not exist!"));
        InvokeStmt initStmt = jimp.newInvokeStmt(constructorInvokeExpr);
        body.getUnits().insertAfter(initStmt, (InvokeStmt) assignStmt);
        body.getUnits().insertAfter(jimp.newThrowStmt(exceptionLocal), (ThrowStmt) initStmt);
        return declaringClass.getOrAddMethod(m);
    }

    public String toString() {
        return getSignature();
    }

    public int hashCode() {
        int result = (31 * 1) + (this.declaringClass == null ? 0 : this.declaringClass.hashCode());
        return (31 * ((31 * ((31 * ((31 * result) + (this.isStatic ? 1231 : 1237))) + (this.name == null ? 0 : this.name.hashCode()))) + (this.parameterTypes == null ? 0 : this.parameterTypes.hashCode()))) + (this.returnType == null ? 0 : this.returnType.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SootMethodRefImpl other = (SootMethodRefImpl) obj;
        if (this.isStatic != other.isStatic) {
            return false;
        }
        if (this.declaringClass == null) {
            if (other.declaringClass != null) {
                return false;
            }
        } else if (!this.declaringClass.equals(other.declaringClass)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.parameterTypes == null) {
            if (other.parameterTypes != null) {
                return false;
            }
        } else if (!this.parameterTypes.equals(other.parameterTypes)) {
            return false;
        }
        if (this.returnType == null) {
            if (other.returnType != null) {
                return false;
            }
            return true;
        } else if (!this.returnType.equals(other.returnType)) {
            return false;
        } else {
            return true;
        }
    }
}
