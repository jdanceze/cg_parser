package soot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.MagicNumberFileFilter;
import org.apache.tools.ant.launch.Launcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxb.android.axml.AxmlReader;
import pxb.android.axml.AxmlVisitor;
import pxb.android.axml.NodeVisitor;
import soot.Singletons;
import soot.dexpler.DalvikThrowAnalysis;
import soot.dotnet.exceptiontoolkits.DotnetThrowAnalysis;
import soot.dotnet.members.DotnetMethod;
import soot.dotnet.types.DotnetBasicTypes;
import soot.javaToJimple.DefaultLocalGenerator;
import soot.jimple.Jimple;
import soot.jimple.spark.internal.ClientAccessibilityOracle;
import soot.jimple.spark.internal.PublicAndProtectedAccessibility;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.ContextSensitiveCallGraph;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.jimple.toolkits.pointer.DumbPointerAnalysis;
import soot.jimple.toolkits.pointer.SideEffectAnalysis;
import soot.jimple.toolkits.scalar.DefaultLocalCreation;
import soot.jimple.toolkits.scalar.LocalCreation;
import soot.options.Options;
import soot.toolkits.exceptions.PedanticThrowAnalysis;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.exceptions.UnitThrowAnalysis;
import soot.util.ArrayNumberer;
import soot.util.Chain;
import soot.util.HashChain;
import soot.util.IterableNumberer;
import soot.util.MapNumberer;
import soot.util.Numberer;
import soot.util.StringNumberer;
import soot.util.WeakMapNumberer;
/* loaded from: gencallgraphv3.jar:soot/Scene.class */
public class Scene {
    private static final int defaultSdkVersion = 15;
    protected IterableNumberer<SootClass> classNumberer;
    protected Numberer<SparkField> fieldNumberer;
    protected IterableNumberer<SootMethod> methodNumberer;
    protected IterableNumberer<Local> localNumberer;
    protected Numberer<Context> contextNumberer;
    protected Hierarchy activeHierarchy;
    protected FastHierarchy activeFastHierarchy;
    protected SideEffectAnalysis activeSideEffectAnalysis;
    protected PointsToAnalysis activePointsToAnalysis;
    protected CallGraph activeCallGraph;
    protected ReachableMethods reachableMethods;
    protected List<SootMethod> entryPoints;
    protected ContextSensitiveCallGraph cscg;
    protected ClientAccessibilityOracle accessibilityOracle;
    protected String sootClassPath;
    protected List<SootClass> dynamicClasses;
    protected LinkedList<String> excludedPackages;
    protected SootClass mainClass;
    private ThrowAnalysis defaultThrowAnalysis;
    private List<String> pkgList;
    private AndroidVersionInfo androidSDKVersionInfo;
    private static final Logger logger = LoggerFactory.getLogger(Scene.class);
    private static final Pattern arrayPattern = Pattern.compile("([^\\[\\]]*)(.*)");
    protected final Map<String, RefType> nameToClass = new ConcurrentHashMap();
    protected final ArrayNumberer<Kind> kindNumberer = new ArrayNumberer<>(new Kind[]{Kind.INVALID, Kind.STATIC, Kind.VIRTUAL, Kind.INTERFACE, Kind.SPECIAL, Kind.CLINIT, Kind.THREAD, Kind.EXECUTOR, Kind.ASYNCTASK, Kind.FINALIZE, Kind.INVOKE_FINALIZE, Kind.PRIVILEGED, Kind.NEWINSTANCE});
    protected final Set<String> reservedNames = new HashSet();
    protected final Set<String>[] basicclasses = new Set[4];
    protected Chain<SootClass> classes = new HashChain();
    protected Chain<SootClass> applicationClasses = new HashChain();
    protected Chain<SootClass> libraryClasses = new HashChain();
    protected Chain<SootClass> phantomClasses = new HashChain();
    protected IterableNumberer<Type> typeNumberer = new ArrayNumberer();
    protected Numberer<Unit> unitNumberer = new MapNumberer();
    protected StringNumberer subSigNumberer = new StringNumberer();
    protected boolean allowsPhantomRefs = false;
    protected boolean incrementalBuild = false;
    protected boolean doneResolving = false;
    private int stateCount = 0;
    private final Map<String, Integer> maxAPIs = new HashMap();
    private int androidAPIVersion = -1;

    public Scene(Singletons.Global g) {
        setReservedNames();
        String scp = System.getProperty("soot.class.path");
        if (scp != null) {
            setSootClassPath(scp);
        }
        if (Options.v().weak_map_structures()) {
            this.classNumberer = new WeakMapNumberer();
            this.fieldNumberer = new WeakMapNumberer();
            this.methodNumberer = new WeakMapNumberer();
            this.localNumberer = new WeakMapNumberer();
        } else {
            this.classNumberer = new ArrayNumberer();
            this.fieldNumberer = new ArrayNumberer();
            this.methodNumberer = new ArrayNumberer();
            this.localNumberer = new ArrayNumberer();
        }
        if (Options.v().src_prec() == 7) {
            addSootBasicDotnetClasses();
            return;
        }
        addSootBasicClasses();
        determineExcludedPackages();
    }

    public static Scene v() {
        if (ModuleUtil.module_mode()) {
            return G.v().soot_ModuleScene();
        }
        return G.v().soot_Scene();
    }

    private void determineExcludedPackages() {
        LinkedList<String> excludedPackages;
        int fmt;
        Options options = Options.v();
        List<String> exclude = options.exclude();
        if (exclude == null) {
            excludedPackages = new LinkedList<>();
        } else {
            excludedPackages = new LinkedList<>(exclude);
        }
        if (!options.include_all() && (fmt = options.output_format()) != 10 && fmt != 11) {
            excludedPackages.add("java.*");
            excludedPackages.add("sun.*");
            excludedPackages.add("javax.*");
            excludedPackages.add("com.sun.*");
            excludedPackages.add("com.ibm.*");
            excludedPackages.add("org.xml.*");
            excludedPackages.add("org.w3c.*");
            excludedPackages.add("apple.awt.*");
            excludedPackages.add("com.apple.*");
        }
        this.excludedPackages = excludedPackages;
    }

    public void setMainClass(SootClass m) {
        this.mainClass = m;
        if (!m.declaresMethod(getSubSigNumberer().findOrAdd(JavaMethods.SIG_MAIN)) && !m.declaresMethod(getSubSigNumberer().findOrAdd(DotnetMethod.MAIN_METHOD_SIGNATURE))) {
            throw new RuntimeException("Main-class has no main method!");
        }
    }

    public Set<String> getReservedNames() {
        return this.reservedNames;
    }

    public String quotedNameOf(String s) {
        String[] split;
        boolean found = s.indexOf(45) > -1;
        if (!found) {
            Iterator<String> it = this.reservedNames.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String token = it.next();
                if (s.contains(token)) {
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            return s;
        }
        StringBuilder res = new StringBuilder(s.length());
        for (String part : s.split("\\.")) {
            if (res.length() > 0) {
                res.append('.');
            }
            if ((!part.isEmpty() && part.charAt(0) == '-') || this.reservedNames.contains(part)) {
                res.append('\'').append(part).append('\'');
            } else {
                res.append(part);
            }
        }
        return res.toString();
    }

    public static String unescapeName(String s) {
        String[] split;
        if (s.indexOf(39) < 0) {
            return s;
        }
        StringBuilder res = new StringBuilder(s.length());
        for (String part : s.split("\\.")) {
            if (res.length() > 0) {
                res.append('.');
            }
            int len = part.length();
            if (len > 1 && part.charAt(0) == '\'' && part.charAt(len - 1) == '\'') {
                res.append(part.substring(1, len - 1));
            } else {
                res.append(part);
            }
        }
        return res.toString();
    }

    public boolean hasMainClass() {
        if (this.mainClass == null) {
            setMainClassFromOptions();
        }
        return this.mainClass != null;
    }

    public SootClass getMainClass() {
        if (!hasMainClass()) {
            throw new RuntimeException("There is no main class set!");
        }
        return this.mainClass;
    }

    public SootMethod getMainMethod() {
        SootMethod methodUnsafe;
        if (!hasMainClass()) {
            throw new RuntimeException("There is no main class set!");
        }
        if (Options.v().src_prec() != 7) {
            methodUnsafe = this.mainClass.getMethodUnsafe("main", Collections.singletonList(ArrayType.v(RefType.v("java.lang.String"), 1)), VoidType.v());
        } else {
            methodUnsafe = this.mainClass.getMethodUnsafe("Main", Collections.singletonList(ArrayType.v(RefType.v(DotnetBasicTypes.SYSTEM_STRING), 1)), VoidType.v());
        }
        SootMethod mainMethod = methodUnsafe;
        if (mainMethod == null) {
            throw new RuntimeException("Main class declares no main method!");
        }
        return mainMethod;
    }

    public void setSootClassPath(String p) {
        this.sootClassPath = p;
        SourceLocator.v().invalidateClassPath();
    }

    public void extendSootClassPath(String newPathElement) {
        this.sootClassPath = String.valueOf(this.sootClassPath) + File.pathSeparatorChar + newPathElement;
        SourceLocator.v().extendClassPath(newPathElement);
    }

    public String getSootClassPath() {
        if (this.sootClassPath == null) {
            String cp = Options.v().soot_classpath();
            if (cp == null || cp.isEmpty()) {
                cp = defaultClassPath();
            } else if (Options.v().prepend_classpath()) {
                cp = String.valueOf(cp) + File.pathSeparatorChar + defaultClassPath();
            }
            List<String> dirs = new LinkedList<>();
            dirs.addAll(Options.v().process_dir());
            List<String> jarDirs = Options.v().process_jar_dir();
            if (!jarDirs.isEmpty()) {
                for (String jarDirName : jarDirs) {
                    File jarDir = new File(jarDirName);
                    File[] contents = jarDir.listFiles();
                    for (File f : contents) {
                        if (f.getAbsolutePath().endsWith(".jar")) {
                            dirs.add(f.getAbsolutePath());
                        }
                    }
                }
            }
            if (!dirs.isEmpty()) {
                StringBuilder pds = new StringBuilder();
                for (String path : dirs) {
                    if (!cp.contains(path)) {
                        pds.append(path.replaceAll("(?<!\\\\)" + Pattern.quote(File.pathSeparator), "\\\\" + File.pathSeparator)).append(File.pathSeparatorChar);
                    }
                }
                cp = pds.append(cp).toString();
            }
            this.sootClassPath = cp;
        }
        return this.sootClassPath;
    }

    private int getMaxAPIAvailable(String dir) {
        Integer mapi = this.maxAPIs.get(dir);
        if (mapi != null) {
            return mapi.intValue();
        }
        File d = new File(dir);
        if (!d.exists()) {
            throw new AndroidPlatformException(String.format("The Android platform directory you have specified (%s) does not exist. Please check.", dir));
        }
        File[] files = d.listFiles();
        if (files == null) {
            return -1;
        }
        int maxApi = -1;
        for (File f : files) {
            String name = f.getName();
            if (f.isDirectory() && name.startsWith("android-")) {
                try {
                    int v = Integer.decode(name.split("android-")[1]).intValue();
                    if (v > maxApi) {
                        maxApi = v;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        this.maxAPIs.put(dir, Integer.valueOf(maxApi));
        return maxApi;
    }

    public String getAndroidJarPath(String jars, String apk) {
        int APIVersion = getAndroidAPIVersion(jars, apk);
        String jarPath = String.valueOf(jars) + File.separatorChar + "android-" + APIVersion + File.separatorChar + "android.jar";
        File f = new File(jarPath);
        if (!f.isFile()) {
            throw new AndroidPlatformException(String.format("error: target android.jar %s does not exist.", jarPath));
        }
        return jarPath;
    }

    public int getAndroidAPIVersion() {
        if (this.androidAPIVersion > 0) {
            return this.androidAPIVersion;
        }
        if (Options.v().android_api_version() > 0) {
            return Options.v().android_api_version();
        }
        return 15;
    }

    private int getAndroidAPIVersion(String jars, String apk) {
        if (this.androidAPIVersion > 0) {
            return this.androidAPIVersion;
        }
        File jarsF = new File(jars);
        if (!jarsF.exists()) {
            throw new AndroidPlatformException(String.format("Android platform directory '%s' does not exist!", jarsF.getAbsolutePath()));
        }
        if (apk != null && !new File(apk).exists()) {
            throw new RuntimeException("file '" + apk + "' does not exist!");
        }
        this.androidAPIVersion = 15;
        if (Options.v().android_api_version() > 0) {
            this.androidAPIVersion = Options.v().android_api_version();
        } else if (apk != null && apk.toLowerCase().endsWith(".apk")) {
            this.androidAPIVersion = getTargetSDKVersion(apk, jars);
        }
        int maxAPI = getMaxAPIAvailable(jars);
        if (maxAPI > 0 && this.androidAPIVersion > maxAPI) {
            this.androidAPIVersion = maxAPI;
        }
        while (this.androidAPIVersion < maxAPI) {
            String jarPath = String.valueOf(jars) + File.separatorChar + "android-" + this.androidAPIVersion + File.separatorChar + "android.jar";
            if (new File(jarPath).exists()) {
                break;
            }
            this.androidAPIVersion++;
        }
        return this.androidAPIVersion;
    }

    /* loaded from: gencallgraphv3.jar:soot/Scene$AndroidVersionInfo.class */
    public static class AndroidVersionInfo {
        public int sdkTargetVersion = -1;
        public int minSdkVersion = -1;
        public int platformBuildVersionCode = -1;

        /* JADX INFO: Access modifiers changed from: private */
        public static AndroidVersionInfo get(InputStream manifestIS) {
            AndroidVersionInfo versionInfo = new AndroidVersionInfo();
            AxmlVisitor axmlVisitor = new AxmlVisitor() { // from class: soot.Scene.AndroidVersionInfo.1
                private String nodeName = null;

                @Override // pxb.android.axml.NodeVisitor
                public void attr(String ns, String name, int resourceId, int type, Object obj) {
                    super.attr(ns, name, resourceId, type, obj);
                    if (this.nodeName != null && name != null) {
                        if (this.nodeName.equals("manifest")) {
                            if (name.equals("platformBuildVersionCode")) {
                                AndroidVersionInfo.this.platformBuildVersionCode = Integer.valueOf(new StringBuilder().append(obj).toString()).intValue();
                            }
                        } else if (this.nodeName.equals("uses-sdk")) {
                            if (name.equals("targetSdkVersion") || (name.isEmpty() && resourceId == 16843376)) {
                                AndroidVersionInfo.this.sdkTargetVersion = Integer.valueOf(String.valueOf(obj)).intValue();
                            } else if (name.equals("minSdkVersion") || (name.isEmpty() && resourceId == 16843276)) {
                                AndroidVersionInfo.this.minSdkVersion = Integer.valueOf(String.valueOf(obj)).intValue();
                            }
                        }
                    }
                }

                @Override // pxb.android.axml.NodeVisitor
                public NodeVisitor child(String ns, String name) {
                    this.nodeName = name;
                    return this;
                }
            };
            try {
                AxmlReader xmlReader = new AxmlReader(IOUtils.toByteArray(manifestIS));
                xmlReader.accept(axmlVisitor);
            } catch (Exception e) {
                Scene.logger.error(e.getMessage(), (Throwable) e);
            }
            return versionInfo;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0031, code lost:
        r10 = r9.getInputStream(r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int getTargetSDKVersion(java.lang.String r7, java.lang.String r8) {
        /*
            Method dump skipped, instructions count: 560
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.Scene.getTargetSDKVersion(java.lang.String, java.lang.String):int");
    }

    public AndroidVersionInfo getAndroidSDKVersionInfo() {
        return this.androidSDKVersionInfo;
    }

    public String defaultClassPath() {
        if (Options.v().src_prec() != 5) {
            Iterator<String> it = Options.v().process_dir().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String entry = it.next();
                if (entry.toLowerCase().endsWith(".apk")) {
                    System.err.println("APK file on process dir, but chosen src-prec does not support loading APKs");
                    break;
                }
            }
            String path = defaultJavaClassPath();
            if (path == null) {
                throw new RuntimeException("Error: cannot find rt.jar.");
            }
            return path;
        }
        return defaultAndroidClassPath();
    }

    private String defaultAndroidClassPath() {
        String androidJars = Options.v().android_jars();
        String forceAndroidJar = Options.v().force_android_jar();
        if ((androidJars == null || androidJars.isEmpty()) && (forceAndroidJar == null || forceAndroidJar.isEmpty())) {
            throw new RuntimeException("You are analyzing an Android application but did not define android.jar. Options -android-jars or -force-android-jar should be used.");
        }
        String jarPath = "";
        if (forceAndroidJar != null && !forceAndroidJar.isEmpty()) {
            jarPath = forceAndroidJar;
            if (Options.v().android_api_version() > 0) {
                this.androidAPIVersion = Options.v().android_api_version();
            } else if (forceAndroidJar.contains("android-")) {
                Pattern pt = Pattern.compile("\\" + File.separatorChar + "android-(\\d+)\\" + File.separatorChar);
                Matcher m = pt.matcher(forceAndroidJar);
                if (m.find()) {
                    this.androidAPIVersion = Integer.valueOf(m.group(1)).intValue();
                }
            } else {
                this.androidAPIVersion = 15;
            }
        } else if (androidJars != null && !androidJars.isEmpty()) {
            List<String> classPathEntries = new ArrayList<>(Arrays.asList(Options.v().soot_classpath().split(File.pathSeparator)));
            classPathEntries.addAll(Options.v().process_dir());
            String targetApk = "";
            Set<String> targetDexs = new HashSet<>();
            for (String entry : classPathEntries) {
                if (isApk(new File(entry))) {
                    if (targetApk != null && !targetApk.isEmpty()) {
                        throw new RuntimeException("only one Android application can be analyzed when using option -android-jars.");
                    }
                    targetApk = entry;
                }
                if (entry.toLowerCase().endsWith(".dex")) {
                    targetDexs.add(entry);
                }
            }
            if (targetApk == null || targetApk.isEmpty()) {
                if (targetDexs.isEmpty()) {
                    throw new RuntimeException("no apk file given");
                }
                jarPath = getAndroidJarPath(androidJars, null);
            } else {
                jarPath = getAndroidJarPath(androidJars, targetApk);
            }
        }
        if (jarPath.isEmpty()) {
            throw new RuntimeException("android.jar not found.");
        }
        File f = new File(jarPath);
        if (!f.exists()) {
            throw new RuntimeException("file '" + jarPath + "' does not exist!");
        }
        logger.debug("Using '" + jarPath + "' as android.jar");
        return jarPath;
    }

    public static boolean isApk(File apk) {
        MagicNumberFileFilter apkFilter = new MagicNumberFileFilter(new byte[]{80, 75});
        if (!apkFilter.accept(apk)) {
            return false;
        }
        try {
            ZipFile zf = new ZipFile(apk);
            try {
                Enumeration<? extends ZipEntry> en = zf.entries();
                while (en.hasMoreElements()) {
                    ZipEntry z = en.nextElement();
                    if ("classes.dex".equals(z.getName())) {
                    }
                }
                if (zf != null) {
                    zf.close();
                    return false;
                }
                return false;
            } finally {
                if (zf != null) {
                    zf.close();
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), (Throwable) e);
            return false;
        }
    }

    public static boolean isJavaGEQ9(String version) {
        try {
            int idx = version.indexOf(45);
            if (idx > 0) {
                version = version.substring(0, idx);
            }
            String[] elements = version.split("\\.");
            Integer firstVersionDigest = Integer.valueOf(elements[0]);
            if (firstVersionDigest.intValue() >= 9) {
                return true;
            }
            if (firstVersionDigest.intValue() != 1 || elements.length <= 1) {
                throw new IllegalArgumentException(String.format("Unknown Version number schema %s", version));
            }
            return Integer.valueOf(elements[1]).intValue() >= 9;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(String.format("Unknown Version number schema %s", version), ex);
        }
    }

    public static String defaultJavaClassPath() {
        String javaHome = System.getProperty("java.home");
        StringBuilder sb = new StringBuilder();
        if ("Mac OS X".equals(System.getProperty("os.name"))) {
            String prefix = String.valueOf(javaHome) + File.separatorChar + ".." + File.separatorChar + "Classes" + File.separatorChar;
            File classesJar = new File(String.valueOf(prefix) + "classes.jar");
            if (classesJar.exists()) {
                sb.append(classesJar.getAbsolutePath()).append(File.pathSeparatorChar);
            }
            File uiJar = new File(String.valueOf(prefix) + "ui.jar");
            if (uiJar.exists()) {
                sb.append(uiJar.getAbsolutePath()).append(File.pathSeparatorChar);
            }
        }
        boolean javaGEQ9 = isJavaGEQ9(System.getProperty("java.version"));
        if (javaGEQ9) {
            sb.append(ModulePathSourceLocator.DUMMY_CLASSPATH_JDK9_FS);
            v().addBasicClass("java.lang.invoke.StringConcatFactory");
        } else {
            File rtJar = new File(String.valueOf(javaHome) + File.separatorChar + Launcher.ANT_PRIVATELIB + File.separatorChar + "rt.jar");
            if (rtJar.exists() && rtJar.isFile()) {
                sb.append(rtJar.getAbsolutePath());
            } else {
                File rtJar2 = new File(String.valueOf(javaHome) + File.separatorChar + "jre" + File.separatorChar + Launcher.ANT_PRIVATELIB + File.separatorChar + "rt.jar");
                if (rtJar2.exists() && rtJar2.isFile()) {
                    sb.append(rtJar2.getAbsolutePath());
                } else {
                    return null;
                }
            }
        }
        if (!javaGEQ9 && (Options.v().whole_program() || Options.v().whole_shimple() || Options.v().output_format() == 15)) {
            sb.append(File.pathSeparatorChar).append(javaHome).append(File.separatorChar).append(Launcher.ANT_PRIVATELIB).append(File.separatorChar).append("jce.jar");
        }
        return sb.toString();
    }

    public int getState() {
        return this.stateCount;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void modifyHierarchy() {
        this.stateCount++;
        this.activeHierarchy = null;
        this.activeFastHierarchy = null;
        this.activeSideEffectAnalysis = null;
        this.activePointsToAnalysis = null;
    }

    public void addClass(SootClass c) {
        addClassSilent(c);
        c.setLibraryClass();
        modifyHierarchy();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void addClassSilent(SootClass c) {
        synchronized (c) {
            if (c.isInScene()) {
                throw new RuntimeException("already managed: " + c.getName());
            }
            if (containsClass(c.getName())) {
                throw new RuntimeException("duplicate class: " + c.getName());
            }
            this.classes.add(c);
            c.getType().setSootClass(c);
            c.setInScene(true);
            if (!c.isPhantom) {
                modifyHierarchy();
            }
            this.nameToClass.computeIfAbsent(c.getName(), k -> {
                return c.getType();
            });
        }
    }

    public void removeClass(SootClass c) {
        if (!c.isInScene()) {
            throw new RuntimeException();
        }
        this.classes.remove(c);
        if (c.isLibraryClass()) {
            this.libraryClasses.remove(c);
        } else if (c.isPhantomClass()) {
            this.phantomClasses.remove(c);
        } else if (c.isApplicationClass()) {
            this.applicationClasses.remove(c);
        }
        c.getType().setSootClass(null);
        c.setInScene(false);
        modifyHierarchy();
    }

    public boolean containsClass(String className) {
        RefType type = this.nameToClass.get(className);
        return type != null && type.hasSootClass() && type.getSootClass().isInScene();
    }

    public boolean containsType(String className) {
        return this.nameToClass.containsKey(className);
    }

    private static int signatureSeparatorIndex(String sig) {
        int len = sig.length();
        if (len < 3 || sig.charAt(0) != '<' || sig.charAt(len - 1) != '>') {
            throw new RuntimeException("oops " + sig);
        }
        int index = sig.indexOf(58);
        if (index < 0) {
            throw new RuntimeException("oops " + sig);
        }
        return index;
    }

    private static String sepIndexToClass(String sig, int index) {
        return unescapeName(sig.substring(1, index));
    }

    private static String sepIndexToSubsignature(String sig, int index) {
        return sig.substring(index + 2, sig.length() - 1);
    }

    public static String signatureToClass(String sig) {
        return sepIndexToClass(sig, signatureSeparatorIndex(sig));
    }

    public static String signatureToSubsignature(String sig) {
        return sepIndexToSubsignature(sig, signatureSeparatorIndex(sig));
    }

    public SootField grabField(String fieldSignature) {
        int index = signatureSeparatorIndex(fieldSignature);
        String cname = sepIndexToClass(fieldSignature, index);
        if (!containsClass(cname)) {
            return null;
        }
        String fname = sepIndexToSubsignature(fieldSignature, index);
        return getSootClass(cname).getFieldUnsafe(fname);
    }

    public boolean containsField(String fieldSignature) {
        return grabField(fieldSignature) != null;
    }

    public SootMethod grabMethod(String methodSignature) {
        int index = signatureSeparatorIndex(methodSignature);
        String cname = sepIndexToClass(methodSignature, index);
        if (!containsClass(cname)) {
            return null;
        }
        String mname = sepIndexToSubsignature(methodSignature, index);
        return getSootClass(cname).getMethodUnsafe(mname);
    }

    public boolean containsMethod(String methodSignature) {
        return grabMethod(methodSignature) != null;
    }

    public SootField getField(String fieldSignature) {
        SootField f = grabField(fieldSignature);
        if (f != null) {
            return f;
        }
        throw new RuntimeException("tried to get nonexistent field " + fieldSignature);
    }

    public SootMethod getMethod(String methodSignature) {
        SootMethod m = grabMethod(methodSignature);
        if (m != null) {
            return m;
        }
        throw new RuntimeException("tried to get nonexistent method " + methodSignature);
    }

    public SootClass tryLoadClass(String className, int desiredLevel) {
        setPhantomRefs(true);
        ClassSource source = SourceLocator.v().getClassSource(className);
        try {
            if (!getPhantomRefs() && source == null) {
                setPhantomRefs(false);
                if (source == null) {
                    return null;
                }
                source.close();
                return null;
            }
            SootClass toReturn = SootResolver.v().resolveClass(className, desiredLevel);
            setPhantomRefs(false);
            return toReturn;
        } finally {
            if (source != null) {
                source.close();
            }
        }
    }

    public SootClass loadClassAndSupport(String className) {
        SootClass ret = loadClass(className, 2);
        if (!ret.isPhantom()) {
            ret = loadClass(className, 3);
        }
        return ret;
    }

    public SootClass loadClass(String className, int desiredLevel) {
        setPhantomRefs(true);
        SootClass toReturn = SootResolver.v().resolveClass(className, desiredLevel);
        setPhantomRefs(false);
        return toReturn;
    }

    public Type getType(String arg) {
        Type t = getTypeUnsafe(arg, false);
        if (t == null) {
            throw new RuntimeException("Unknown Type: '" + t + "'");
        }
        return t;
    }

    public Type getTypeUnsafe(String arg) {
        return getTypeUnsafe(arg, true);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0162  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public soot.Type getTypeUnsafe(java.lang.String r5, boolean r6) {
        /*
            Method dump skipped, instructions count: 397
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.Scene.getTypeUnsafe(java.lang.String, boolean):soot.Type");
    }

    public RefType getRefType(String className) {
        RefType refType = getRefTypeUnsafe(className);
        if (refType == null) {
            throw new IllegalStateException("RefType " + className + " not loaded. If you tried to get the RefType of a library class, did you call loadNecessaryClasses()? Otherwise please check Soot's classpath.");
        }
        return refType;
    }

    public RefType getRefTypeUnsafe(String className) {
        return this.nameToClass.get(className);
    }

    public RefType getObjectType() {
        if (Options.v().src_prec() == 7) {
            return getRefType(DotnetBasicTypes.SYSTEM_OBJECT);
        }
        return getRefType(JavaBasicTypes.JAVA_LANG_OBJECT);
    }

    public RefType getBaseExceptionType() {
        if (Options.v().src_prec() == 7) {
            return getRefType(DotnetBasicTypes.SYSTEM_EXCEPTION);
        }
        return getRefType("java.lang.Throwable");
    }

    public SootClass getSootClassUnsafe(String className) {
        return getSootClassUnsafe(className, true);
    }

    /* JADX WARN: Type inference failed for: r0v10, types: [java.lang.Throwable, soot.RefType] */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Throwable, soot.RefType] */
    public SootClass getSootClassUnsafe(String className, boolean phantomNonExist) {
        SootClass tsc;
        RefType refType = this.nameToClass.get(className);
        if (refType != 0) {
            synchronized (refType) {
                if (refType.hasSootClass() && (tsc = refType.getSootClass()) != null) {
                    return tsc;
                }
            }
        }
        if ((phantomNonExist && allowsPhantomRefs()) || SootClass.INVOKEDYNAMIC_DUMMY_CLASS_NAME.equals(className)) {
            ?? orAddRefType = getOrAddRefType(className);
            synchronized (orAddRefType) {
                if (orAddRefType.hasSootClass()) {
                    return orAddRefType.getSootClass();
                }
                SootClass c = new SootClass(className);
                c.isPhantom = true;
                addClassSilent(c);
                c.setPhantomClass();
                return c;
            }
        }
        return null;
    }

    public SootClass getSootClass(String className) {
        SootClass sc = getSootClassUnsafe(className);
        if (sc != null) {
            return sc;
        }
        throw new RuntimeException(String.valueOf(System.lineSeparator()) + "Aborting: can't find classfile " + className);
    }

    public Chain<SootClass> getClasses() {
        return this.classes;
    }

    public Chain<SootClass> getApplicationClasses() {
        return this.applicationClasses;
    }

    public Chain<SootClass> getLibraryClasses() {
        return this.libraryClasses;
    }

    public Chain<SootClass> getPhantomClasses() {
        return this.phantomClasses;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Chain<SootClass> getContainingChain(SootClass c) {
        if (c.isApplicationClass()) {
            return getApplicationClasses();
        }
        if (c.isLibraryClass()) {
            return getLibraryClasses();
        }
        if (c.isPhantomClass()) {
            return getPhantomClasses();
        }
        return null;
    }

    public SideEffectAnalysis getSideEffectAnalysis() {
        SideEffectAnalysis temp = this.activeSideEffectAnalysis;
        if (temp == null) {
            temp = new SideEffectAnalysis(getPointsToAnalysis(), getCallGraph());
            this.activeSideEffectAnalysis = temp;
        }
        return temp;
    }

    public void setSideEffectAnalysis(SideEffectAnalysis sea) {
        this.activeSideEffectAnalysis = sea;
    }

    public boolean hasSideEffectAnalysis() {
        return this.activeSideEffectAnalysis != null;
    }

    public void releaseSideEffectAnalysis() {
        this.activeSideEffectAnalysis = null;
    }

    public PointsToAnalysis getPointsToAnalysis() {
        PointsToAnalysis temp = this.activePointsToAnalysis;
        if (temp == null) {
            return DumbPointerAnalysis.v();
        }
        return temp;
    }

    public void setPointsToAnalysis(PointsToAnalysis pa) {
        this.activePointsToAnalysis = pa;
    }

    public boolean hasPointsToAnalysis() {
        return this.activePointsToAnalysis != null;
    }

    public void releasePointsToAnalysis() {
        this.activePointsToAnalysis = null;
    }

    public ClientAccessibilityOracle getClientAccessibilityOracle() {
        ClientAccessibilityOracle temp = this.accessibilityOracle;
        if (temp == null) {
            return PublicAndProtectedAccessibility.v();
        }
        return temp;
    }

    public boolean hasClientAccessibilityOracle() {
        return this.accessibilityOracle != null;
    }

    public void setClientAccessibilityOracle(ClientAccessibilityOracle oracle) {
        this.accessibilityOracle = oracle;
    }

    public void releaseClientAccessibilityOracle() {
        this.accessibilityOracle = null;
    }

    public synchronized FastHierarchy getOrMakeFastHierarchy() {
        FastHierarchy temp = this.activeFastHierarchy;
        if (temp == null) {
            temp = new FastHierarchy();
            this.activeFastHierarchy = temp;
        }
        return temp;
    }

    public synchronized FastHierarchy getFastHierarchy() {
        FastHierarchy temp = this.activeFastHierarchy;
        if (temp == null) {
            throw new RuntimeException("no active FastHierarchy present for scene");
        }
        return temp;
    }

    public synchronized void setFastHierarchy(FastHierarchy hierarchy) {
        this.activeFastHierarchy = hierarchy;
    }

    public synchronized boolean hasFastHierarchy() {
        return this.activeFastHierarchy != null;
    }

    public synchronized void releaseFastHierarchy() {
        this.activeFastHierarchy = null;
    }

    public synchronized Hierarchy getActiveHierarchy() {
        Hierarchy temp = this.activeHierarchy;
        if (temp == null) {
            temp = new Hierarchy();
            this.activeHierarchy = temp;
        }
        return temp;
    }

    public synchronized void setActiveHierarchy(Hierarchy hierarchy) {
        this.activeHierarchy = hierarchy;
    }

    public synchronized boolean hasActiveHierarchy() {
        return this.activeHierarchy != null;
    }

    public synchronized void releaseActiveHierarchy() {
        this.activeHierarchy = null;
    }

    public boolean hasCustomEntryPoints() {
        return this.entryPoints != null;
    }

    public List<SootMethod> getEntryPoints() {
        List<SootMethod> temp = this.entryPoints;
        if (temp == null) {
            temp = EntryPoints.v().all();
            this.entryPoints = temp;
        }
        return temp;
    }

    public void setEntryPoints(List<SootMethod> entryPoints) {
        this.entryPoints = entryPoints;
    }

    public ContextSensitiveCallGraph getContextSensitiveCallGraph() {
        ContextSensitiveCallGraph temp = this.cscg;
        if (temp == null) {
            throw new RuntimeException("No context-sensitive call graph present in Scene. You can bulid one with Paddle.");
        }
        return temp;
    }

    public void setContextSensitiveCallGraph(ContextSensitiveCallGraph cscg) {
        this.cscg = cscg;
    }

    public CallGraph getCallGraph() {
        CallGraph temp = this.activeCallGraph;
        if (temp == null) {
            throw new RuntimeException("No call graph present in Scene. Maybe you want Whole Program mode (-w).");
        }
        return temp;
    }

    public void setCallGraph(CallGraph cg) {
        this.reachableMethods = null;
        this.activeCallGraph = cg;
    }

    public boolean hasCallGraph() {
        return this.activeCallGraph != null;
    }

    public void releaseCallGraph() {
        this.activeCallGraph = null;
        this.reachableMethods = null;
    }

    public ReachableMethods getReachableMethods() {
        ReachableMethods temp = this.reachableMethods;
        if (temp == null) {
            temp = new ReachableMethods(getCallGraph(), new ArrayList(getEntryPoints()));
            this.reachableMethods = temp;
        }
        temp.update();
        return temp;
    }

    public void setReachableMethods(ReachableMethods rm) {
        this.reachableMethods = rm;
    }

    public boolean hasReachableMethods() {
        return this.reachableMethods != null;
    }

    public void releaseReachableMethods() {
        this.reachableMethods = null;
    }

    public boolean getPhantomRefs() {
        return Options.v().allow_phantom_refs();
    }

    public void setPhantomRefs(boolean value) {
        this.allowsPhantomRefs = value;
    }

    public boolean allowsPhantomRefs() {
        return getPhantomRefs();
    }

    public Numberer<Kind> kindNumberer() {
        return this.kindNumberer;
    }

    public IterableNumberer<Type> getTypeNumberer() {
        return this.typeNumberer;
    }

    public IterableNumberer<SootMethod> getMethodNumberer() {
        return this.methodNumberer;
    }

    public Numberer<Context> getContextNumberer() {
        return this.contextNumberer;
    }

    public Numberer<Unit> getUnitNumberer() {
        return this.unitNumberer;
    }

    public Numberer<SparkField> getFieldNumberer() {
        return this.fieldNumberer;
    }

    public IterableNumberer<SootClass> getClassNumberer() {
        return this.classNumberer;
    }

    public StringNumberer getSubSigNumberer() {
        return this.subSigNumberer;
    }

    public IterableNumberer<Local> getLocalNumberer() {
        return this.localNumberer;
    }

    public void setContextNumberer(Numberer<Context> n) {
        if (this.contextNumberer != null) {
            throw new RuntimeException("Attempt to set context numberer when it is already set.");
        }
        this.contextNumberer = n;
    }

    public ThrowAnalysis getDefaultThrowAnalysis() {
        if (this.defaultThrowAnalysis == null) {
            switch (Options.v().throw_analysis()) {
                case 1:
                    this.defaultThrowAnalysis = PedanticThrowAnalysis.v();
                    break;
                case 2:
                    this.defaultThrowAnalysis = UnitThrowAnalysis.v();
                    break;
                case 3:
                    this.defaultThrowAnalysis = DalvikThrowAnalysis.v();
                    break;
                case 4:
                    this.defaultThrowAnalysis = DotnetThrowAnalysis.v();
                    break;
                case 5:
                    if (Options.v().src_prec() != 5) {
                        if (Options.v().src_prec() == 7) {
                            this.defaultThrowAnalysis = DotnetThrowAnalysis.v();
                            break;
                        } else {
                            this.defaultThrowAnalysis = UnitThrowAnalysis.v();
                            break;
                        }
                    } else {
                        this.defaultThrowAnalysis = DalvikThrowAnalysis.v();
                        break;
                    }
                default:
                    throw new IllegalStateException("Options.v().throw_analysis() == " + Options.v().throw_analysis());
            }
        }
        return this.defaultThrowAnalysis;
    }

    public void setDefaultThrowAnalysis(ThrowAnalysis ta) {
        this.defaultThrowAnalysis = ta;
    }

    private void setReservedNames() {
        Set<String> rn = this.reservedNames;
        rn.add(Jimple.NEWARRAY);
        rn.add(Jimple.NEWMULTIARRAY);
        rn.add(Jimple.NOP);
        rn.add(Jimple.RET);
        rn.add(Jimple.SPECIALINVOKE);
        rn.add(Jimple.STATICINVOKE);
        rn.add(Jimple.TABLESWITCH);
        rn.add(Jimple.VIRTUALINVOKE);
        rn.add(Jimple.NULL_TYPE);
        rn.add("unknown");
        rn.add(Jimple.CMP);
        rn.add(Jimple.CMPG);
        rn.add(Jimple.CMPL);
        rn.add(Jimple.ENTERMONITOR);
        rn.add(Jimple.EXITMONITOR);
        rn.add(Jimple.INTERFACEINVOKE);
        rn.add(Jimple.LENGTHOF);
        rn.add(Jimple.LOOKUPSWITCH);
        rn.add(Jimple.NEG);
        rn.add(Jimple.IF);
        rn.add(Jimple.ABSTRACT);
        rn.add(Jimple.ANNOTATION);
        rn.add("boolean");
        rn.add(Jimple.BREAK);
        rn.add("byte");
        rn.add(Jimple.CASE);
        rn.add(Jimple.CATCH);
        rn.add("char");
        rn.add("class");
        rn.add("enum");
        rn.add(Jimple.FINAL);
        rn.add(Jimple.NATIVE);
        rn.add(Jimple.PUBLIC);
        rn.add(Jimple.PROTECTED);
        rn.add(Jimple.PRIVATE);
        rn.add(Jimple.STATIC);
        rn.add(Jimple.SYNCHRONIZED);
        rn.add(Jimple.TRANSIENT);
        rn.add(Jimple.VOLATILE);
        rn.add("interface");
        rn.add(Jimple.VOID);
        rn.add("short");
        rn.add("int");
        rn.add("long");
        rn.add(Jimple.FLOAT);
        rn.add("double");
        rn.add(Jimple.EXTENDS);
        rn.add(Jimple.IMPLEMENTS);
        rn.add(Jimple.BREAKPOINT);
        rn.add("default");
        rn.add(Jimple.GOTO);
        rn.add(Jimple.INSTANCEOF);
        rn.add("new");
        rn.add("return");
        rn.add(Jimple.THROW);
        rn.add(Jimple.THROWS);
        rn.add(Jimple.NULL);
        rn.add("from");
        rn.add("to");
        rn.add(Jimple.WITH);
        rn.add(Jimple.CLS);
        rn.add(Jimple.DYNAMICINVOKE);
        rn.add(Jimple.STRICTFP);
    }

    private void addSootBasicClasses() {
        this.basicclasses[1] = new HashSet();
        this.basicclasses[2] = new HashSet();
        this.basicclasses[3] = new HashSet();
        addBasicClass(JavaBasicTypes.JAVA_LANG_OBJECT);
        addBasicClass("java.lang.Class", 2);
        addBasicClass("java.lang.Void", 2);
        addBasicClass(JavaBasicTypes.JAVA_LANG_BOOLEAN, 2);
        addBasicClass(JavaBasicTypes.JAVA_LANG_BYTE, 2);
        addBasicClass(JavaBasicTypes.JAVA_LANG_CHARACTER, 2);
        addBasicClass(JavaBasicTypes.JAVA_LANG_SHORT, 2);
        addBasicClass(JavaBasicTypes.JAVA_LANG_INTEGER, 2);
        addBasicClass(JavaBasicTypes.JAVA_LANG_LONG, 2);
        addBasicClass(JavaBasicTypes.JAVA_LANG_FLOAT, 2);
        addBasicClass(JavaBasicTypes.JAVA_LANG_DOUBLE, 2);
        addBasicClass("java.lang.Number", 2);
        addBasicClass("java.lang.String");
        addBasicClass("java.lang.StringBuffer", 2);
        addBasicClass("java.lang.Enum", 2);
        addBasicClass("java.lang.Error");
        addBasicClass("java.lang.AssertionError", 2);
        addBasicClass("java.lang.Throwable", 2);
        addBasicClass("java.lang.Exception", 2);
        addBasicClass("java.lang.NoClassDefFoundError", 2);
        addBasicClass("java.lang.ReflectiveOperationException", 2);
        addBasicClass("java.lang.ExceptionInInitializerError");
        addBasicClass("java.lang.RuntimeException");
        addBasicClass("java.lang.ClassNotFoundException");
        addBasicClass("java.lang.ArithmeticException");
        addBasicClass("java.lang.ArrayStoreException");
        addBasicClass("java.lang.ClassCastException");
        addBasicClass("java.lang.IllegalMonitorStateException");
        addBasicClass("java.lang.IndexOutOfBoundsException");
        addBasicClass("java.lang.ArrayIndexOutOfBoundsException");
        addBasicClass("java.lang.NegativeArraySizeException");
        addBasicClass("java.lang.NullPointerException", 2);
        addBasicClass("java.lang.InstantiationError");
        addBasicClass("java.lang.InternalError");
        addBasicClass("java.lang.OutOfMemoryError");
        addBasicClass("java.lang.StackOverflowError");
        addBasicClass("java.lang.UnknownError");
        addBasicClass("java.lang.ThreadDeath");
        addBasicClass("java.lang.ClassCircularityError");
        addBasicClass("java.lang.ClassFormatError");
        addBasicClass("java.lang.IllegalAccessError");
        addBasicClass("java.lang.IncompatibleClassChangeError");
        addBasicClass("java.lang.LinkageError");
        addBasicClass("java.lang.VerifyError");
        addBasicClass("java.lang.NoSuchFieldError");
        addBasicClass("java.lang.AbstractMethodError");
        addBasicClass("java.lang.NoSuchMethodError");
        addBasicClass("java.lang.UnsatisfiedLinkError");
        addBasicClass("java.lang.Thread");
        addBasicClass("java.lang.Runnable");
        addBasicClass("java.lang.Cloneable");
        addBasicClass(JavaBasicTypes.JAVA_IO_SERIALIZABLE);
        addBasicClass("java.lang.ref.Finalizer");
        addBasicClass("java.lang.invoke.LambdaMetafactory");
    }

    private void addSootBasicDotnetClasses() {
        this.basicclasses[1] = new HashSet();
        this.basicclasses[2] = new HashSet();
        this.basicclasses[3] = new HashSet();
        addBasicClass(DotnetBasicTypes.SYSTEM_OBJECT, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_VOID, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_BOOLEAN, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_BYTE, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_CHAR, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_INT16, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_INT32, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_INT64, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_SINGLE, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_DOUBLE, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_STRING, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_ENUM, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_TYPE, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_SBYTE, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_DECIMAL, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_INTPTR, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_UINTPTR, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_UINTPTR, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_UINT16, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_UINT32, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_UINT64, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_EXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_ACCESSVIOLATIONEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_AGGREGATEEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_APPDOMAINUNLOADEDEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_APPLICATIONEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_ARGUMENTEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_ARGUMENTNULLEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_ARGUMENTOUTOFRANGEEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_ARITHMETICEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_ARRAYTYPEMISMATCHEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_BADIMAGEFORMATEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_CANNOTUNLOADAPPDOMAINEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_CONTEXTMARSHALEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_DATAMISALIGNEDEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_DIVIDEBYZEROEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_DLLNOTFOUNDEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_DUPLICATEWAITOBJECTEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_ENTRYPOINTNOTFOUNDEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_EXECUTIONENGINEEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_FIELDACCESSEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_FORMATEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_INDEXOUTOFRANGEEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_INSUFFICIENTEXECUTIONSTACKEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_INSUFFICIENTMEMORYEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_INVALIDCASTEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_INVALIDOPERATIONEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_INVALIDPROGRAMEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_INVALIDTIMEZONEEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_MISSINGFIELDEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_MISSINGMETHODEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_NULLREFERENCEEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_OUTOFMEMORYEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_OVERFLOWEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_SYSTEMEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_TYPEACCESSEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_TYPEINITIALIZATIONEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_TYPELOADEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_TYPEUNLOADEDEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_UNAUTHORIZEDACCESSEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_URIFORMATEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_SECURITYEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_METHODACCESSEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_VERIFICATIONEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_STACKOVERFLOWEXCEPTION, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_THREADING, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_SERIALIZEABLEATTRIBUTE, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_CONSOLE, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_RUNTIMEFIELDHANDLE, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_RUNTIMEMETHODHANDLE, 2);
        addBasicClass(DotnetBasicTypes.SYSTEM_RUNTIMETYPEHANDLE, 2);
        addBasicClass(DotnetBasicTypes.FAKE_LDFTN, 2);
    }

    public void addBasicClass(String name) {
        addBasicClass(name, 1);
    }

    public void addBasicClass(String name, int level) {
        this.basicclasses[level].add(name);
    }

    public void loadBasicClasses() {
        addReflectionTraceClasses();
        int loadedClasses = 0;
        for (int i = 3; i >= 1; i--) {
            for (String name : this.basicclasses[i]) {
                SootClass basicClass = tryLoadClass(name, i);
                if (basicClass != null && !basicClass.isPhantom()) {
                    loadedClasses++;
                }
            }
        }
        if (loadedClasses == 0) {
            throw new RuntimeException("None of the basic classes could be loaded! Check your Soot class path!");
        }
    }

    public Set<String> getBasicClasses() {
        Set<String> all = new HashSet<>();
        for (int i = 3; i >= 1; i--) {
            all.addAll(this.basicclasses[i]);
        }
        return all;
    }

    public boolean isBasicClass(String className) {
        for (int i = 3; i >= 1; i--) {
            if (this.basicclasses[i].contains(className)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0306 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x033b A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void addReflectionTraceClasses() {
        /*
            Method dump skipped, instructions count: 974
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.Scene.addReflectionTraceClasses():void");
    }

    public Collection<SootClass> dynamicClasses() {
        List<SootClass> temp = this.dynamicClasses;
        if (temp == null) {
            throw new IllegalStateException("Have to call loadDynamicClasses() first!");
        }
        return temp;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void loadNecessaryClass(String name) {
        loadClassAndSupport(name).setApplicationClass();
    }

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
                for (String cl : SourceLocator.v().getClassesUnder(path)) {
                    SootClass theClass = loadClassAndSupport(cl);
                    if (!theClass.isPhantom) {
                        theClass.setApplicationClass();
                    }
                }
            }
        }
        prepareClasses();
        setDoneResolving();
    }

    public void loadDynamicClasses() {
        ArrayList<SootClass> dynamicClasses = new ArrayList<>();
        Options opts = Options.v();
        HashSet<String> temp = new HashSet<>(opts.dynamic_class());
        SourceLocator sloc = SourceLocator.v();
        for (String path : opts.dynamic_dir()) {
            temp.addAll(sloc.getClassesUnder(path));
        }
        for (String pkg : opts.dynamic_package()) {
            temp.addAll(sloc.classesInDynamicPackage(pkg));
        }
        Iterator<String> it = temp.iterator();
        while (it.hasNext()) {
            String className = it.next();
            dynamicClasses.add(loadClassAndSupport(className));
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
                                loadClassAndSupport(s.getName());
                            }
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    public boolean isExcluded(SootClass sc) {
        return isExcluded(sc.getName());
    }

    public boolean isExcluded(String className) {
        if (this.excludedPackages == null) {
            return false;
        }
        Iterator<String> it = this.excludedPackages.iterator();
        while (it.hasNext()) {
            String pkg = it.next();
            if (!className.equals(pkg)) {
                if (pkg.endsWith(".*") || pkg.endsWith("$*")) {
                    if (className.startsWith(pkg.substring(0, pkg.length() - 1))) {
                    }
                }
            }
            return !isIncluded(className);
        }
        return false;
    }

    public boolean isIncluded(SootClass sc) {
        return isIncluded(sc.getName());
    }

    public boolean isIncluded(String className) {
        for (String pkg : Options.v().include()) {
            if (!className.equals(pkg)) {
                if (pkg.endsWith(".*") || pkg.endsWith("$*")) {
                    if (className.startsWith(pkg.substring(0, pkg.length() - 1))) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public void setPkgList(List<String> list) {
        this.pkgList = list;
    }

    public List<String> getPkgList() {
        return this.pkgList;
    }

    public SootMethodRef makeMethodRef(SootClass declaringClass, String name, List<Type> parameterTypes, Type returnType, boolean isStatic) {
        if (PolymorphicMethodRef.handlesClass(declaringClass)) {
            return new PolymorphicMethodRef(declaringClass, name, parameterTypes, returnType, isStatic);
        }
        return new SootMethodRefImpl(declaringClass, name, parameterTypes, returnType, isStatic);
    }

    public SootMethodRef makeConstructorRef(SootClass declaringClass, List<Type> parameterTypes) {
        return makeMethodRef(declaringClass, "<init>", parameterTypes, VoidType.v(), false);
    }

    public SootFieldRef makeFieldRef(SootClass declaringClass, String name, Type type, boolean isStatic) {
        return new AbstractSootFieldRef(declaringClass, name, type, isStatic);
    }

    public List<SootClass> getClasses(int desiredLevel) {
        List<SootClass> ret = new ArrayList<>();
        for (SootClass cl : getClasses()) {
            if (cl.resolvingLevel() >= desiredLevel) {
                ret.add(cl);
            }
        }
        return ret;
    }

    public boolean doneResolving() {
        return this.doneResolving;
    }

    public void setDoneResolving() {
        this.doneResolving = true;
    }

    void setResolving(boolean value) {
        this.doneResolving = value;
    }

    public void setMainClassFromOptions() {
        if (this.mainClass == null) {
            String optsMain = Options.v().main_class();
            if (optsMain != null && !optsMain.isEmpty()) {
                setMainClass(getSootClass(optsMain));
                return;
            }
            List<Type> mainArgs = Collections.singletonList(ArrayType.v(Options.v().src_prec() == 7 ? RefType.v(DotnetBasicTypes.SYSTEM_STRING) : RefType.v("java.lang.String"), 1));
            Iterator it = Options.v().classes().iterator();
            while (it.hasNext()) {
                String next = (String) it.next();
                SootClass c = getSootClass(next);
                boolean declaresMethod = Options.v().src_prec() != 7 ? c.declaresMethod("main", mainArgs, VoidType.v()) : c.declaresMethod("Main", mainArgs, VoidType.v());
                if (declaresMethod) {
                    logger.debug("No main class given. Inferred '" + c.getName() + "' as main class.");
                    setMainClass(c);
                    return;
                }
            }
            for (SootClass c2 : getApplicationClasses()) {
                boolean declaresMethod2 = Options.v().src_prec() != 7 ? c2.declaresMethod("main", mainArgs, VoidType.v()) : c2.declaresMethod("Main", mainArgs, VoidType.v());
                if (declaresMethod2) {
                    logger.debug("No main class given. Inferred '" + c2.getName() + "' as main class.");
                    setMainClass(c2);
                    return;
                }
            }
        }
    }

    public boolean isIncrementalBuild() {
        return this.incrementalBuild;
    }

    public void initiateIncrementalBuild() {
        this.incrementalBuild = true;
    }

    public void incrementalBuildFinished() {
        this.incrementalBuild = false;
    }

    public SootClass forceResolve(String className, int level) {
        boolean tmp = this.doneResolving;
        this.doneResolving = false;
        try {
            SootClass c = SootResolver.v().resolveClass(className, level);
            return c;
        } finally {
            this.doneResolving = tmp;
        }
    }

    public SootClass makeSootClass(String name) {
        return new SootClass(name);
    }

    public SootClass makeSootClass(String name, int modifiers) {
        return new SootClass(name, modifiers);
    }

    public SootMethod makeSootMethod(String name, List<Type> parameterTypes, Type returnType) {
        return new SootMethod(name, parameterTypes, returnType);
    }

    public SootMethod makeSootMethod(String name, List<Type> parameterTypes, Type returnType, int modifiers) {
        return new SootMethod(name, parameterTypes, returnType, modifiers);
    }

    public SootMethod makeSootMethod(String name, List<Type> parameterTypes, Type returnType, int modifiers, List<SootClass> thrownExceptions) {
        return new SootMethod(name, parameterTypes, returnType, modifiers, thrownExceptions);
    }

    public SootField makeSootField(String name, Type type, int modifiers) {
        return new SootField(name, type, modifiers);
    }

    public SootField makeSootField(String name, Type type) {
        return new SootField(name, type);
    }

    public RefType getOrAddRefType(String refTypeName) {
        return this.nameToClass.computeIfAbsent(refTypeName, k -> {
            return new RefType(k);
        });
    }

    public CallGraph internalMakeCallGraph() {
        return new CallGraph();
    }

    public LocalGenerator createLocalGenerator(Body stmtBody) {
        return new DefaultLocalGenerator(stmtBody);
    }

    public LocalCreation createLocalCreation(Chain<Local> locals) {
        return new DefaultLocalCreation(locals);
    }

    public LocalCreation createLocalCreation(Chain<Local> locals, String prefix) {
        return new DefaultLocalCreation(locals, prefix);
    }
}
