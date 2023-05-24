package soot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import soot.Singletons;
import soot.dotnet.members.DotnetMethod;
import soot.options.Options;
import soot.util.NumberedString;
import soot.util.StringNumberer;
/* loaded from: gencallgraphv3.jar:soot/EntryPoints.class */
public class EntryPoints {
    final NumberedString sigMain;
    final NumberedString sigFinalize;
    final NumberedString sigExit;
    final NumberedString sigClinit;
    final NumberedString sigInit;
    final NumberedString sigStart;
    final NumberedString sigRun;
    final NumberedString sigObjRun;
    final NumberedString sigForName;

    public EntryPoints(Singletons.Global g) {
        StringNumberer subSigNumberer = Scene.v().getSubSigNumberer();
        if (Options.v().src_prec() == 7) {
            this.sigMain = subSigNumberer.findOrAdd(DotnetMethod.MAIN_METHOD_SIGNATURE);
            this.sigFinalize = subSigNumberer.findOrAdd("void Finalize()");
        } else {
            this.sigMain = subSigNumberer.findOrAdd(JavaMethods.SIG_MAIN);
            this.sigFinalize = subSigNumberer.findOrAdd(JavaMethods.SIG_FINALIZE);
        }
        this.sigExit = subSigNumberer.findOrAdd(JavaMethods.SIG_EXIT);
        this.sigClinit = subSigNumberer.findOrAdd(JavaMethods.SIG_CLINIT);
        this.sigInit = subSigNumberer.findOrAdd(JavaMethods.SIG_INIT);
        this.sigStart = subSigNumberer.findOrAdd(JavaMethods.SIG_START);
        this.sigRun = subSigNumberer.findOrAdd(JavaMethods.SIG_RUN);
        this.sigObjRun = subSigNumberer.findOrAdd(JavaMethods.SIG_OBJ_RUN);
        this.sigForName = subSigNumberer.findOrAdd(JavaMethods.SIG_FOR_NAME);
    }

    public static EntryPoints v() {
        return G.v().soot_EntryPoints();
    }

    protected void addMethod(List<SootMethod> set, SootClass cls, NumberedString methodSubSig) {
        SootMethod sm = cls.getMethodUnsafe(methodSubSig);
        if (sm != null) {
            set.add(sm);
        }
    }

    protected void addMethod(List<SootMethod> set, String methodSig) {
        Scene sc = Scene.v();
        if (sc.containsMethod(methodSig)) {
            set.add(sc.getMethod(methodSig));
        }
    }

    public List<SootMethod> application() {
        List<SootMethod> ret = new ArrayList<>();
        Scene sc = Scene.v();
        if (sc.hasMainClass()) {
            SootClass mainClass = sc.getMainClass();
            addMethod(ret, mainClass, this.sigMain);
            for (SootMethod clinit : clinitsOf(mainClass)) {
                ret.add(clinit);
            }
        }
        return ret;
    }

    public List<SootMethod> implicit() {
        List<SootMethod> ret = new ArrayList<>();
        if (Options.v().src_prec() == 7) {
            return ret;
        }
        addMethod(ret, JavaMethods.INITIALIZE_SYSTEM_CLASS);
        addMethod(ret, JavaMethods.THREAD_GROUP_INIT);
        addMethod(ret, JavaMethods.THREAD_EXIT);
        addMethod(ret, JavaMethods.THREADGROUP_UNCAUGHT_EXCEPTION);
        addMethod(ret, JavaMethods.CLASSLOADER_INIT);
        addMethod(ret, JavaMethods.CLASSLOADER_LOAD_CLASS_INTERNAL);
        addMethod(ret, JavaMethods.CLASSLOADER_CHECK_PACKAGE_ACC);
        addMethod(ret, JavaMethods.CLASSLOADER_ADD_CLASS);
        addMethod(ret, JavaMethods.CLASSLOADER_FIND_NATIVE);
        addMethod(ret, JavaMethods.PRIV_ACTION_EXC_INIT);
        addMethod(ret, JavaMethods.RUN_FINALIZE);
        addMethod(ret, JavaMethods.THREAD_INIT_RUNNABLE);
        addMethod(ret, JavaMethods.THREAD_INIT_STRING);
        return ret;
    }

    public List<SootMethod> all() {
        List<SootMethod> ret = new ArrayList<>();
        ret.addAll(application());
        ret.addAll(implicit());
        return ret;
    }

    public List<SootMethod> clinits() {
        List<SootMethod> ret = new ArrayList<>();
        for (SootClass cl : Scene.v().getClasses()) {
            addMethod(ret, cl, this.sigClinit);
        }
        return ret;
    }

    public List<SootMethod> inits() {
        List<SootMethod> ret = new ArrayList<>();
        for (SootClass cl : Scene.v().getClasses()) {
            addMethod(ret, cl, this.sigInit);
        }
        return ret;
    }

    public List<SootMethod> allInits() {
        List<SootMethod> ret = new ArrayList<>();
        for (SootClass cl : Scene.v().getClasses()) {
            for (SootMethod m : cl.getMethods()) {
                if ("<init>".equals(m.getName())) {
                    ret.add(m);
                }
            }
        }
        return ret;
    }

    public List<SootMethod> methodsOfApplicationClasses() {
        List<SootMethod> ret = new ArrayList<>();
        for (SootClass cl : Scene.v().getApplicationClasses()) {
            for (SootMethod m : cl.getMethods()) {
                if (m.isConcrete()) {
                    ret.add(m);
                }
            }
        }
        return ret;
    }

    public List<SootMethod> mainsOfApplicationClasses() {
        List<SootMethod> ret = new ArrayList<>();
        for (SootClass cl : Scene.v().getApplicationClasses()) {
            SootMethod m = Options.v().src_prec() == 7 ? cl.getMethodUnsafe(DotnetMethod.MAIN_METHOD_SIGNATURE) : cl.getMethodUnsafe(JavaMethods.SIG_MAIN);
            if (m != null && m.isConcrete()) {
                ret.add(m);
            }
        }
        return ret;
    }

    public Iterable<SootMethod> clinitsOf(SootClass cl) {
        SootMethod init = cl.getMethodUnsafe(this.sigClinit);
        SootClass superclassUnsafe = cl.getSuperclassUnsafe();
        while (true) {
            SootClass superClass = superclassUnsafe;
            if (init != null || superClass == null) {
                break;
            }
            init = superClass.getMethodUnsafe(this.sigClinit);
            superclassUnsafe = superClass.getSuperclassUnsafe();
        }
        if (init == null) {
            return Collections.emptyList();
        }
        final SootMethod initStart = init;
        return new Iterable<SootMethod>() { // from class: soot.EntryPoints.1
            @Override // java.lang.Iterable
            public Iterator<SootMethod> iterator() {
                return new Iterator<SootMethod>(initStart) { // from class: soot.EntryPoints.1.1
                    SootMethod current;

                    {
                        this.current = r5;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public SootMethod next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        SootMethod n = this.current;
                        this.current = null;
                        SootClass declaringClass = n.getDeclaringClass();
                        while (true) {
                            SootClass currentClass = declaringClass;
                            SootClass superClass2 = currentClass.getSuperclassUnsafe();
                            if (superClass2 == null) {
                                break;
                            }
                            SootMethod m = superClass2.getMethodUnsafe(EntryPoints.this.sigClinit);
                            if (m != null) {
                                this.current = m;
                                break;
                            }
                            declaringClass = superClass2;
                        }
                        return n;
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.current != null;
                    }
                };
            }
        };
    }
}
