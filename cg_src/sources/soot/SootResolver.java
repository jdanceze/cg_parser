package soot;

import beaver.Parser;
import com.google.common.base.Optional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.JastAddJ.BytecodeParser;
import soot.JastAddJ.CompilationUnit;
import soot.JastAddJ.JastAddJavaParser;
import soot.JastAddJ.JavaParser;
import soot.JastAddJ.Program;
import soot.Singletons;
import soot.javaToJimple.IInitialResolver;
import soot.options.Options;
import soot.util.ConcurrentHashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/SootResolver.class */
public class SootResolver {
    private static final Logger logger;
    protected MultiMap<SootClass, Type> classToTypesSignature = new ConcurrentHashMultiMap();
    protected MultiMap<SootClass, Type> classToTypesHierarchy = new ConcurrentHashMultiMap();
    private final Deque<SootClass>[] worklist = new Deque[4];
    private Program program = null;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SootResolver.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(SootResolver.class);
    }

    public SootResolver(Singletons.Global g) {
        this.worklist[1] = new ArrayDeque();
        this.worklist[2] = new ArrayDeque();
        this.worklist[3] = new ArrayDeque();
    }

    protected void initializeProgram() {
        if (Options.v().src_prec() != 6) {
            this.program = new Program();
            this.program.state().reset();
            this.program.initBytecodeReader(new BytecodeParser());
            this.program.initJavaParser(new JavaParser() { // from class: soot.SootResolver.1
                @Override // soot.JastAddJ.JavaParser
                public CompilationUnit parse(InputStream is, String fileName) throws IOException, Parser.Exception {
                    return new JastAddJavaParser().parse(is, fileName);
                }
            });
            soot.JastAddJ.Options options = this.program.options();
            options.initOptions();
            options.addKeyValueOption("-classpath");
            options.setValueForOption(Scene.v().getSootClassPath(), "-classpath");
            switch (Options.v().src_prec()) {
                case 1:
                    this.program.setSrcPrec(2);
                    break;
                case 2:
                    this.program.setSrcPrec(2);
                    break;
                case 4:
                    this.program.setSrcPrec(1);
                    break;
            }
            this.program.initPaths();
        }
    }

    public static SootResolver v() {
        if (ModuleUtil.module_mode()) {
            return G.v().soot_SootModuleResolver();
        }
        return G.v().soot_SootResolver();
    }

    protected boolean resolveEverything() {
        Options opts = Options.v();
        if (opts.on_the_fly()) {
            return false;
        }
        return opts.whole_program() || opts.whole_shimple() || opts.full_resolver() || opts.output_format() == 15;
    }

    public SootClass makeClassRef(String className) {
        SootClass newClass;
        if (className.length() == 0) {
            throw new RuntimeException("Classname must not be empty!");
        }
        Scene scene = Scene.v();
        if (scene.containsClass(className)) {
            return scene.getSootClass(className);
        }
        if (className.endsWith(SootModuleInfo.MODULE_INFO)) {
            newClass = new SootModuleInfo(className, null);
        } else {
            newClass = new SootClass(className);
        }
        newClass.setResolvingLevel(0);
        scene.addClass(newClass);
        return newClass;
    }

    public SootClass resolveClass(String className, int desiredLevel) {
        SootClass resolvedClass = null;
        try {
            resolvedClass = makeClassRef(className);
            addToResolveWorklist(resolvedClass, desiredLevel);
            processResolveWorklist();
            return resolvedClass;
        } catch (SootClassNotFoundException e) {
            if (resolvedClass != null) {
                if (!$assertionsDisabled && resolvedClass.resolvingLevel() != 0) {
                    throw new AssertionError();
                }
                Scene.v().removeClass(resolvedClass);
            }
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void processResolveWorklist() {
        Scene scene = Scene.v();
        boolean resolveEverything = resolveEverything();
        boolean no_bodies_for_excluded = Options.v().no_bodies_for_excluded();
        for (int i = 3; i >= 1; i--) {
            Deque<SootClass> currWorklist = this.worklist[i];
            while (!currWorklist.isEmpty()) {
                SootClass sc = currWorklist.pop();
                if (resolveEverything) {
                    boolean onlySignatures = sc.isPhantom() || (no_bodies_for_excluded && scene.isExcluded(sc) && !scene.isBasicClass(sc.getName()));
                    if (onlySignatures) {
                        bringToSignatures(sc);
                        sc.setPhantomClass();
                        for (SootMethod m : sc.getMethods()) {
                            m.setPhantom(true);
                        }
                        for (SootField f : sc.getFields()) {
                            f.setPhantom(true);
                        }
                    } else {
                        bringToBodies(sc);
                    }
                } else {
                    switch (i) {
                        case 1:
                            bringToHierarchy(sc);
                            break;
                        case 2:
                            bringToSignatures(sc);
                            break;
                        case 3:
                            bringToBodies(sc);
                            break;
                    }
                }
            }
            this.worklist[i] = new ArrayDeque(0);
        }
    }

    protected void addToResolveWorklist(Type type, int level) {
        if (type instanceof RefType) {
            addToResolveWorklist(((RefType) type).getSootClass(), level);
        } else if (type instanceof ArrayType) {
            addToResolveWorklist(((ArrayType) type).baseType, level);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addToResolveWorklist(SootClass sc, int desiredLevel) {
        if (sc.resolvingLevel() >= desiredLevel) {
            return;
        }
        this.worklist[desiredLevel].add(sc);
    }

    protected void bringToHierarchy(SootClass sc) {
        if (sc.resolvingLevel() >= 1) {
            return;
        }
        if (Options.v().debug_resolver()) {
            logger.debug("bringing to HIERARCHY: " + sc);
        }
        sc.setResolvingLevel(1);
        bringToHierarchyUnchecked(sc);
    }

    protected void bringToHierarchyUnchecked(SootClass sc) {
        ClassSource is;
        String className = sc.getName();
        if (ModuleUtil.module_mode()) {
            is = ModulePathSourceLocator.v().getClassSource(className, Optional.fromNullable(sc.moduleName));
        } else {
            is = SourceLocator.v().getClassSource(className);
        }
        boolean modelAsPhantomRef = is == null;
        try {
            if (modelAsPhantomRef) {
                if (!Scene.v().allowsPhantomRefs()) {
                    String suffix = "";
                    if (JavaBasicTypes.JAVA_LANG_OBJECT.equals(className)) {
                        suffix = " Try adding rt.jar to Soot's classpath, e.g.:\njava -cp sootclasses.jar soot.Main -cp .:/path/to/jdk/jre/lib/rt.jar <other options>";
                    } else if ("javax.crypto.Cipher".equals(className)) {
                        suffix = " Try adding jce.jar to Soot's classpath, e.g.:\njava -cp sootclasses.jar soot.Main -cp .:/path/to/jdk/jre/lib/rt.jar:/path/to/jdk/jre/lib/jce.jar <other options>";
                    }
                    throw new SootClassNotFoundException("couldn't find class: " + className + " (is your soot-class-path set properly?)" + suffix);
                }
                sc.setPhantomClass();
            } else {
                IInitialResolver.Dependencies dependencies = is.resolve(sc);
                if (!dependencies.typesToSignature.isEmpty()) {
                    this.classToTypesSignature.putAll(sc, dependencies.typesToSignature);
                }
                if (!dependencies.typesToHierarchy.isEmpty()) {
                    this.classToTypesHierarchy.putAll(sc, dependencies.typesToHierarchy);
                }
            }
            reResolveHierarchy(sc, 1);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public void reResolveHierarchy(SootClass sc, int level) {
        SootClass superClass = sc.getSuperclassUnsafe();
        if (superClass != null) {
            addToResolveWorklist(superClass, level);
        }
        SootClass outerClass = sc.getOuterClassUnsafe();
        if (outerClass != null) {
            addToResolveWorklist(outerClass, level);
        }
        for (SootClass iface : sc.getInterfaces()) {
            addToResolveWorklist(iface, level);
        }
    }

    protected void bringToSignatures(SootClass sc) {
        if (sc.resolvingLevel() >= 2) {
            return;
        }
        bringToHierarchy(sc);
        if (Options.v().debug_resolver()) {
            logger.debug("bringing to SIGNATURES: " + sc);
        }
        sc.setResolvingLevel(2);
        bringToSignaturesUnchecked(sc);
    }

    protected void bringToSignaturesUnchecked(SootClass sc) {
        for (SootField f : sc.getFields()) {
            addToResolveWorklist(f.getType(), 1);
        }
        for (SootMethod m : sc.getMethods()) {
            addToResolveWorklist(m.getReturnType(), 1);
            for (Type ptype : m.getParameterTypes()) {
                addToResolveWorklist(ptype, 1);
            }
            List<SootClass> exceptions = m.getExceptionsUnsafe();
            if (exceptions != null) {
                for (SootClass exception : exceptions) {
                    addToResolveWorklist(exception, 1);
                }
            }
        }
        reResolveHierarchy(sc, 2);
    }

    protected void bringToBodies(SootClass sc) {
        if (sc.resolvingLevel() >= 3) {
            return;
        }
        bringToSignatures(sc);
        if (Options.v().debug_resolver()) {
            logger.debug("bringing to BODIES: " + sc);
        }
        sc.setResolvingLevel(3);
        bringToBodiesUnchecked(sc);
    }

    protected void bringToBodiesUnchecked(SootClass sc) {
        Collection<Type> references = this.classToTypesHierarchy.get(sc);
        if (references != null) {
            for (Type t : references) {
                addToResolveWorklist(t, 1);
            }
        }
        Collection<Type> references2 = this.classToTypesSignature.get(sc);
        if (references2 != null) {
            for (Type t2 : references2) {
                addToResolveWorklist(t2, 2);
            }
        }
    }

    public void reResolve(SootClass cl, int newResolvingLevel) {
        int resolvingLevel = cl.resolvingLevel();
        if (resolvingLevel >= newResolvingLevel) {
            return;
        }
        reResolveHierarchy(cl, 1);
        cl.setResolvingLevel(newResolvingLevel);
        addToResolveWorklist(cl, resolvingLevel);
        processResolveWorklist();
    }

    public void reResolve(SootClass cl) {
        reResolve(cl, 1);
    }

    public Program getProgram() {
        if (this.program == null) {
            initializeProgram();
        }
        return this.program;
    }

    /* loaded from: gencallgraphv3.jar:soot/SootResolver$SootClassNotFoundException.class */
    public class SootClassNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1563461446590293827L;

        public SootClassNotFoundException(String s) {
            super(s);
        }
    }
}
