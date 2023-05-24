package soot;

import com.google.common.base.Optional;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.tools.ant.launch.Launcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ModuleUtil;
import soot.Singletons;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.Jimple;
import soot.options.Options;
import soot.util.Chain;
import soot.util.HashChain;
/* loaded from: gencallgraphv3.jar:soot/ModuleScene.class */
public class ModuleScene extends Scene {
    private static final Logger logger = LoggerFactory.getLogger(Scene.class);
    private final Map<String, Map<String, RefType>> nameToClass;
    private String modulePath;

    public ModuleScene(Singletons.Global g) {
        super(g);
        this.nameToClass = new HashMap();
        this.modulePath = null;
        String smp = System.getProperty("soot.module.path");
        if (smp != null) {
            setSootModulePath(smp);
        }
        addBasicClass("java.lang.invoke.StringConcatFactory");
    }

    public static ModuleScene v() {
        return G.v().soot_ModuleScene();
    }

    @Override // soot.Scene
    public SootMethod getMainMethod() {
        if (!hasMainClass()) {
            throw new RuntimeException("There is no main class set!");
        }
        SootMethod mainMethod = this.mainClass.getMethodUnsafe("main", Collections.singletonList(ArrayType.v(ModuleRefType.v("java.lang.String", Optional.of("java.base")), 1)), VoidType.v());
        if (mainMethod == null) {
            throw new RuntimeException("Main class declares no main method!");
        }
        return mainMethod;
    }

    @Override // soot.Scene
    public void extendSootClassPath(String newPathElement) {
        extendSootModulePath(newPathElement);
    }

    public void extendSootModulePath(String newPathElement) {
        this.modulePath = String.valueOf(this.modulePath) + File.pathSeparatorChar + newPathElement;
        ModulePathSourceLocator.v().extendClassPath(newPathElement);
    }

    @Override // soot.Scene
    public String getSootClassPath() {
        return getSootModulePath();
    }

    @Override // soot.Scene
    public void setSootClassPath(String p) {
        setSootModulePath(p);
    }

    public String getSootModulePath() {
        if (this.modulePath == null) {
            String cp = Options.v().soot_modulepath();
            if (cp == null || cp.isEmpty()) {
                cp = defaultJavaModulePath();
            } else if (Options.v().prepend_classpath()) {
                cp = String.valueOf(cp) + File.pathSeparatorChar + defaultJavaModulePath();
            }
            List<String> dirs = Options.v().process_dir();
            if (!dirs.isEmpty()) {
                StringBuilder pds = new StringBuilder();
                for (String path : dirs) {
                    if (!cp.contains(path)) {
                        pds.append(path).append(File.pathSeparatorChar);
                    }
                }
                cp = pds.append(cp).toString();
            }
            this.modulePath = cp;
        }
        return this.modulePath;
    }

    public void setSootModulePath(String p) {
        ModulePathSourceLocator.v().invalidateClassPath();
        this.modulePath = p;
    }

    private String defaultJavaModulePath() {
        StringBuilder sb = new StringBuilder();
        File rtJar = Paths.get(System.getProperty("java.home"), Launcher.ANT_PRIVATELIB, "jrt-fs.jar").toFile();
        if ((rtJar.exists() && rtJar.isFile()) || !Options.v().soot_modulepath().isEmpty()) {
            sb.append(ModulePathSourceLocator.DUMMY_CLASSPATH_JDK9_FS);
            return sb.toString();
        }
        throw new RuntimeException("Error: cannot find jrt-fs.jar.");
    }

    @Override // soot.Scene
    protected void addClassSilent(SootClass c) {
        if (c.isInScene()) {
            throw new RuntimeException("already managed: " + c.getName());
        }
        String className = c.getName();
        if (containsClass(className, Optional.fromNullable(c.moduleName))) {
            throw new RuntimeException("duplicate class: " + className);
        }
        this.classes.add(c);
        Map<String, RefType> map = this.nameToClass.get(className);
        if (map == null) {
            Map<String, Map<String, RefType>> map2 = this.nameToClass;
            Map<String, RefType> hashMap = new HashMap<>();
            map = hashMap;
            map2.put(className, hashMap);
        }
        map.put(c.moduleName, c.getType());
        c.getType().setSootClass(c);
        c.setInScene(true);
        if (!c.isPhantom) {
            modifyHierarchy();
        }
    }

    @Override // soot.Scene
    public boolean containsClass(String className) {
        ModuleUtil.ModuleClassNameWrapper wrapper = ModuleUtil.v().makeWrapper(className);
        return containsClass(wrapper.getClassName(), wrapper.getModuleNameOptional());
    }

    public boolean containsClass(String className, Optional<String> moduleName) {
        RefType type = null;
        Map<String, RefType> map = this.nameToClass.get(className);
        if (map != null && !map.isEmpty()) {
            if (moduleName.isPresent()) {
                type = map.get(ModuleUtil.v().declaringModule(className, moduleName.get()));
            } else {
                type = map.values().iterator().next();
                if (Options.v().verbose() && ModuleUtil.module_mode()) {
                    logger.warn("containsClass called with empty module for: " + className);
                }
            }
        }
        return type != null && type.hasSootClass() && type.getSootClass().isInScene();
    }

    @Override // soot.Scene
    public boolean containsType(String className) {
        return this.nameToClass.containsKey(className);
    }

    public SootClass tryLoadClass(String className, int desiredLevel, Optional<String> moduleName) {
        setPhantomRefs(true);
        ClassSource source = ModulePathSourceLocator.v().getClassSource(className, moduleName);
        try {
            if (!getPhantomRefs() && source == null) {
                setPhantomRefs(false);
                if (source == null) {
                    return null;
                }
                source.close();
                return null;
            }
            SootClass toReturn = SootModuleResolver.v().resolveClass(className, desiredLevel, moduleName);
            setPhantomRefs(false);
            return toReturn;
        } finally {
            if (source != null) {
                source.close();
            }
        }
    }

    @Override // soot.Scene
    public SootClass loadClassAndSupport(String className) {
        ModuleUtil.ModuleClassNameWrapper wrapper = ModuleUtil.v().makeWrapper(className);
        return loadClassAndSupport(wrapper.getClassName(), wrapper.getModuleNameOptional());
    }

    public SootClass loadClassAndSupport(String className, Optional<String> moduleName) {
        SootClass ret = loadClass(className, 2, moduleName);
        if (!ret.isPhantom()) {
            ret = loadClass(className, 3, moduleName);
        }
        return ret;
    }

    @Override // soot.Scene
    public SootClass loadClass(String className, int desiredLevel) {
        ModuleUtil.ModuleClassNameWrapper wrapper = ModuleUtil.v().makeWrapper(className);
        return loadClass(wrapper.getClassName(), desiredLevel, wrapper.getModuleNameOptional());
    }

    public SootClass loadClass(String className, int desiredLevel, Optional<String> moduleName) {
        setPhantomRefs(true);
        SootClass toReturn = SootModuleResolver.v().resolveClass(className, desiredLevel, moduleName);
        setPhantomRefs(false);
        return toReturn;
    }

    public Type getType(String arg, Optional<String> moduleName) {
        String type = arg.replaceAll("([^\\[\\]]*)(.*)", "$1");
        Type result = getRefTypeUnsafe(type, moduleName);
        if (result == null) {
            switch (type.hashCode()) {
                case -1325958191:
                    if (type.equals("double")) {
                        result = DoubleType.v();
                        break;
                    }
                    throw new RuntimeException("unknown type: '" + type + "'");
                case 104431:
                    if (type.equals("int")) {
                        result = IntType.v();
                        break;
                    }
                    throw new RuntimeException("unknown type: '" + type + "'");
                case 3039496:
                    if (type.equals("byte")) {
                        result = ByteType.v();
                        break;
                    }
                    throw new RuntimeException("unknown type: '" + type + "'");
                case 3052374:
                    if (type.equals("char")) {
                        result = CharType.v();
                        break;
                    }
                    throw new RuntimeException("unknown type: '" + type + "'");
                case 3327612:
                    if (type.equals("long")) {
                        result = LongType.v();
                        break;
                    }
                    throw new RuntimeException("unknown type: '" + type + "'");
                case 3625364:
                    if (type.equals(Jimple.VOID)) {
                        result = VoidType.v();
                        break;
                    }
                    throw new RuntimeException("unknown type: '" + type + "'");
                case 64711720:
                    if (type.equals("boolean")) {
                        result = BooleanType.v();
                        break;
                    }
                    throw new RuntimeException("unknown type: '" + type + "'");
                case 97526364:
                    if (type.equals(Jimple.FLOAT)) {
                        result = FloatType.v();
                        break;
                    }
                    throw new RuntimeException("unknown type: '" + type + "'");
                case 109413500:
                    if (type.equals("short")) {
                        result = ShortType.v();
                        break;
                    }
                    throw new RuntimeException("unknown type: '" + type + "'");
                default:
                    throw new RuntimeException("unknown type: '" + type + "'");
            }
        }
        int arrayCount = arg.contains("[") ? arg.replaceAll("([^\\[\\]]*)(.*)", "$2").length() / 2 : 0;
        return arrayCount == 0 ? result : ArrayType.v(result, arrayCount);
    }

    public RefType getRefType(String className, Optional<String> moduleName) {
        RefType refType = getRefTypeUnsafe(className, moduleName);
        if (refType == null) {
            throw new IllegalStateException("RefType " + className + " not loaded. If you tried to get the RefType of a library class, did you call loadNecessaryClasses()? Otherwise please check Soot's classpath.");
        }
        return refType;
    }

    @Override // soot.Scene
    public RefType getRefType(String className) {
        ModuleUtil.ModuleClassNameWrapper wrapper = ModuleUtil.v().makeWrapper(className);
        return getRefType(wrapper.getClassName(), wrapper.getModuleNameOptional());
    }

    @Override // soot.Scene
    public RefType getObjectType() {
        if (Options.v().src_prec() == 7) {
            return getRefType(DotnetBasicTypes.SYSTEM_OBJECT);
        }
        return getRefType(JavaBasicTypes.JAVA_LANG_OBJECT, Optional.of("java.base"));
    }

    public RefType getRefTypeUnsafe(String className, Optional<String> moduleName) {
        RefType refType = null;
        Map<String, RefType> map = this.nameToClass.get(className);
        if (map != null && !map.isEmpty()) {
            if (moduleName.isPresent()) {
                refType = map.get(moduleName.get());
            } else {
                refType = map.values().iterator().next();
                if (Options.v().verbose() && ModuleUtil.module_mode()) {
                    logger.warn("getRefTypeUnsafe called with empty module for: " + className);
                }
            }
        }
        return refType;
    }

    @Override // soot.Scene
    public RefType getRefTypeUnsafe(String className) {
        ModuleUtil.ModuleClassNameWrapper wrapper = ModuleUtil.v().makeWrapper(className);
        return getRefTypeUnsafe(wrapper.getClassName(), wrapper.getModuleNameOptional());
    }

    public void addRefType(RefType type) {
        String className = type.getClassName();
        Map<String, RefType> map = this.nameToClass.get(className);
        if (map == null) {
            Map<String, Map<String, RefType>> map2 = this.nameToClass;
            Map<String, RefType> hashMap = new HashMap<>();
            map = hashMap;
            map2.put(className, hashMap);
        }
        map.put(((ModuleRefType) type).getModuleName(), type);
    }

    @Override // soot.Scene
    public SootClass getSootClassUnsafe(String className) {
        ModuleUtil.ModuleClassNameWrapper wrapper = ModuleUtil.v().makeWrapper(className);
        return getSootClassUnsafe(wrapper.getClassName(), wrapper.getModuleNameOptional());
    }

    public SootClass getSootClassUnsafe(String className, Optional<String> moduleName) {
        SootClass tsc;
        RefType type = null;
        Map<String, RefType> map = this.nameToClass.get(className);
        if (map != null && !map.isEmpty()) {
            if (moduleName.isPresent()) {
                type = map.get(ModuleUtil.v().declaringModule(className, moduleName.get()));
            } else {
                type = map.values().iterator().next();
                if (Options.v().verbose() && ModuleUtil.module_mode()) {
                    logger.warn("getSootClassUnsafe called with empty for: " + className);
                }
            }
        }
        if (type != null && (tsc = type.getSootClass()) != null) {
            return tsc;
        }
        if (allowsPhantomRefs() || SootClass.INVOKEDYNAMIC_DUMMY_CLASS_NAME.equals(className)) {
            SootClass c = new SootClass(className);
            addClassSilent(c);
            c.setPhantomClass();
            return c;
        }
        return null;
    }

    @Override // soot.Scene
    public SootClass getSootClass(String className) {
        ModuleUtil.ModuleClassNameWrapper wrapper = ModuleUtil.v().makeWrapper(className);
        return getSootClass(wrapper.getClassName(), wrapper.getModuleNameOptional());
    }

    public SootClass getSootClass(String className, Optional<String> moduleName) {
        SootClass sc = getSootClassUnsafe(className, moduleName);
        if (sc != null) {
            return sc;
        }
        throw new RuntimeException(String.valueOf(System.lineSeparator()) + "Aborting: can't find classfile " + className);
    }

    @Override // soot.Scene
    public void loadBasicClasses() {
        addReflectionTraceClasses();
        ModuleUtil modU = ModuleUtil.v();
        int loadedClasses = 0;
        for (int i = 3; i >= 1; i--) {
            for (String name : this.basicclasses[i]) {
                ModuleUtil.ModuleClassNameWrapper wrapper = modU.makeWrapper(name);
                SootClass sootClass = tryLoadClass(wrapper.getClassName(), i, wrapper.getModuleNameOptional());
                if (sootClass != null && !sootClass.isPhantom()) {
                    loadedClasses++;
                }
            }
        }
        if (loadedClasses == 0) {
            throw new RuntimeException("None of the basic classes could be loaded! Check your Soot class path!");
        }
    }

    @Override // soot.Scene
    public void loadNecessaryClasses() {
        loadBasicClasses();
        Options opts = Options.v();
        Iterator it = opts.classes().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            loadNecessaryClass(name);
        }
        loadDynamicClasses();
        if (opts.oaat()) {
            if (opts.process_dir().isEmpty()) {
                throw new IllegalArgumentException("If switch -oaat is used, then also -process-dir must be given.");
            }
        } else {
            for (String path : opts.process_dir()) {
                for (Map.Entry<String, List<String>> entry : ModulePathSourceLocator.v().getClassUnderModulePath(path).entrySet()) {
                    for (String cl : entry.getValue()) {
                        SootClass theClass = loadClassAndSupport(cl, Optional.fromNullable(entry.getKey()));
                        theClass.setApplicationClass();
                    }
                }
            }
        }
        prepareClasses();
        setDoneResolving();
    }

    @Override // soot.Scene
    public void loadDynamicClasses() {
        ArrayList<SootClass> dynamicClasses = new ArrayList<>();
        Options opts = Options.v();
        Map<String, List<String>> temp = new HashMap<>();
        temp.put(null, opts.dynamic_class());
        ModulePathSourceLocator msloc = ModulePathSourceLocator.v();
        for (String path : opts.dynamic_dir()) {
            temp.putAll(msloc.getClassUnderModulePath(path));
        }
        SourceLocator sloc = SourceLocator.v();
        for (String pkg : opts.dynamic_package()) {
            temp.get(null).addAll(sloc.classesInDynamicPackage(pkg));
        }
        for (Map.Entry<String, List<String>> entry : temp.entrySet()) {
            for (String className : entry.getValue()) {
                dynamicClasses.add(loadClassAndSupport(className, Optional.fromNullable(entry.getKey())));
            }
        }
        Iterator<SootClass> iterator = dynamicClasses.iterator();
        while (iterator.hasNext()) {
            SootClass c = iterator.next();
            if (!c.isConcrete()) {
                if (opts.verbose()) {
                    logger.warn("dynamic class " + c.getName() + " is abstract or an interface, and it will not be considered.");
                }
                iterator.remove();
            }
        }
        this.dynamicClasses = dynamicClasses;
    }

    @Override // soot.Scene
    protected void prepareClasses() {
        List<String> optionsClasses = Options.v().classes();
        Chain<SootClass> processedClasses = new HashChain<>();
        while (true) {
            Chain<SootClass> unprocessedClasses = new HashChain<>(getClasses());
            unprocessedClasses.removeAll(processedClasses);
            if (!unprocessedClasses.isEmpty()) {
                processedClasses.addAll(unprocessedClasses);
                for (SootClass s : unprocessedClasses) {
                    if (!s.isPhantom()) {
                        if (Options.v().app()) {
                            s.setApplicationClass();
                        }
                        if (optionsClasses.contains(s.getName())) {
                            s.setApplicationClass();
                        } else {
                            if (s.isApplicationClass() && isExcluded(s)) {
                                s.setLibraryClass();
                            }
                            if (isIncluded(s)) {
                                s.setApplicationClass();
                            }
                            if (s.isApplicationClass()) {
                                loadClassAndSupport(s.getName(), Optional.fromNullable(s.moduleName));
                            }
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    @Override // soot.Scene
    public void setMainClassFromOptions() {
        if (this.mainClass == null) {
            String optsMain = Options.v().main_class();
            if (optsMain != null && !optsMain.isEmpty()) {
                setMainClass(getSootClass(optsMain, null));
                return;
            }
            List<Type> mainArgs = Collections.singletonList(ArrayType.v(ModuleRefType.v("java.lang.String", Optional.of("java.base")), 1));
            Iterator it = Options.v().classes().iterator();
            while (it.hasNext()) {
                String s = (String) it.next();
                SootClass c = getSootClass(s, null);
                if (c.declaresMethod("main", mainArgs, VoidType.v())) {
                    logger.debug("No main class given. Inferred '" + c.getName() + "' as main class.");
                    setMainClass(c);
                    return;
                }
            }
            for (SootClass c2 : getApplicationClasses()) {
                if (c2.declaresMethod("main", mainArgs, VoidType.v())) {
                    logger.debug("No main class given. Inferred '" + c2.getName() + "' as main class.");
                    setMainClass(c2);
                    return;
                }
            }
        }
    }

    @Override // soot.Scene
    public SootClass forceResolve(String className, int level) {
        ModuleUtil.ModuleClassNameWrapper wrapper = ModuleUtil.v().makeWrapper(className);
        return forceResolve(wrapper.getClassName(), level, wrapper.getModuleNameOptional());
    }

    public SootClass forceResolve(String className, int level, Optional<String> moduleName) {
        boolean tmp = this.doneResolving;
        this.doneResolving = false;
        try {
            SootClass c = SootModuleResolver.v().resolveClass(className, level, moduleName);
            return c;
        } finally {
            this.doneResolving = tmp;
        }
    }

    @Override // soot.Scene
    public SootClass makeSootClass(String className) {
        ModuleUtil.ModuleClassNameWrapper wrapper = ModuleUtil.v().makeWrapper(className);
        return makeSootClass(wrapper.getClassName(), wrapper.getModuleName());
    }

    public SootClass makeSootClass(String name, String moduleName) {
        return new SootClass(name, moduleName);
    }

    public RefType getOrAddRefType(RefType tp) {
        RefType existing = getRefType(tp.getClassName(), Optional.fromNullable(((ModuleRefType) tp).getModuleName()));
        if (existing != null) {
            return existing;
        }
        addRefType(tp);
        return tp;
    }

    public RefType getOrAddRefType(String className, Optional<String> moduleName) {
        RefType existing = getRefType(className, moduleName);
        if (existing != null) {
            return existing;
        }
        RefType tp = ModuleRefType.v(className, moduleName);
        addRefType(tp);
        return tp;
    }
}
