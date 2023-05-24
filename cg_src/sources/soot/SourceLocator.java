package soot;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.ForwardingLoadingCache;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.jf.dexlib2.iface.DexFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.JavaClassProvider;
import soot.Singletons;
import soot.asm.AsmClassProvider;
import soot.asm.AsmJava9ClassProvider;
import soot.dexpler.DexFileProvider;
import soot.dotnet.AssemblyFile;
import soot.dotnet.DotnetClassProvider;
import soot.options.Options;
import soot.util.SharedCloseable;
/* loaded from: gencallgraphv3.jar:soot/SourceLocator.class */
public class SourceLocator {
    private static final Logger logger = LoggerFactory.getLogger(SourceLocator.class);
    protected List<ClassProvider> classProviders;
    protected List<String> classPath;
    protected List<String> sourcePath;
    private Set<String> dexClassPathExtensions;
    private Map<String, File> dexClassIndex;
    private static final int PATH_CACHE_CAPACITY = 100000;
    protected final Set<ClassLoader> additionalClassLoaders = new HashSet();
    protected boolean java9Mode = false;
    final SharedZipFileCacheWrapper archivePathToZip = new SharedZipFileCacheWrapper(5, 100000);
    protected final LoadingCache<String, ClassSourceType> pathToSourceType = CacheBuilder.newBuilder().initialCapacity(5).maximumSize(100000).concurrencyLevel(Runtime.getRuntime().availableProcessors()).build(new CacheLoader<String, ClassSourceType>() { // from class: soot.SourceLocator.1
        @Override // com.google.common.cache.CacheLoader
        public ClassSourceType load(String path) throws Exception {
            File f = new File(path);
            if (!f.exists() && !Options.v().ignore_classpath_errors()) {
                throw new Exception("Error: The path '" + path + "' does not exist.");
            }
            if (!f.canRead() && !Options.v().ignore_classpath_errors()) {
                throw new Exception("Error: The path '" + path + "' exists but is not readable.");
            }
            if (f.isFile()) {
                String substring = path.substring(path.length() - 4);
                switch (substring.hashCode()) {
                    case 1469737:
                        if (substring.equals(".dex")) {
                            return ClassSourceType.dex;
                        }
                        break;
                    case 1469942:
                        if (substring.equals(".dll")) {
                            return ClassSourceType.dll;
                        }
                        break;
                    case 1471268:
                        if (substring.equals(".exe")) {
                            return ClassSourceType.exe;
                        }
                        break;
                    case 1475373:
                        if (substring.equals(".jar")) {
                            return ClassSourceType.jar;
                        }
                        break;
                    case 1490995:
                        if (substring.equals(".zip")) {
                            return ClassSourceType.zip;
                        }
                        break;
                }
                return Scene.isApk(new File(path)) ? ClassSourceType.apk : ClassSourceType.unknown;
            } else if (f.isDirectory()) {
                return ClassSourceType.directory;
            } else {
                throw new Exception("Error: The path '" + path + "' is neither file nor directory.");
            }
        }
    });
    protected final LoadingCache<String, Set<String>> archivePathToEntriesCache = CacheBuilder.newBuilder().initialCapacity(5).maximumSize(100000).softValues().concurrencyLevel(Runtime.getRuntime().availableProcessors()).build(new CacheLoader<String, Set<String>>() { // from class: soot.SourceLocator.2
        @Override // com.google.common.cache.CacheLoader
        public Set<String> load(String archivePath) throws Exception {
            Throwable th = null;
            try {
                SharedCloseable<ZipFile> archive = SourceLocator.this.archivePathToZip.getRef(archivePath);
                Set<String> ret = new HashSet<>();
                Enumeration<? extends ZipEntry> it = archive.get().entries();
                while (it.hasMoreElements()) {
                    ret.add(it.nextElement().getName());
                }
                Set<String> unmodifiableSet = Collections.unmodifiableSet(ret);
                if (archive != null) {
                    archive.close();
                }
                return unmodifiableSet;
            } catch (Throwable th2) {
                if (0 == 0) {
                    th = th2;
                } else if (null != th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    });

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/SourceLocator$ClassSourceType.class */
    public enum ClassSourceType {
        jar,
        zip,
        apk,
        dex,
        directory,
        jrt,
        unknown,
        exe,
        dll;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static ClassSourceType[] valuesCustom() {
            ClassSourceType[] valuesCustom = values();
            int length = valuesCustom.length;
            ClassSourceType[] classSourceTypeArr = new ClassSourceType[length];
            System.arraycopy(valuesCustom, 0, classSourceTypeArr, 0, length);
            return classSourceTypeArr;
        }
    }

    public SourceLocator(Singletons.Global g) {
    }

    public static SourceLocator v() {
        return ModuleUtil.module_mode() ? G.v().soot_ModulePathSourceLocator() : G.v().soot_SourceLocator();
    }

    public static void ensureDirectoryExists(File dir) {
        if (dir != null && !dir.exists()) {
            try {
                dir.mkdirs();
            } catch (SecurityException e) {
                logger.debug("Unable to create " + dir);
                throw new CompilationDeathException(0);
            }
        }
    }

    public static List<String> explodeClassPath(String classPath) {
        String[] split;
        List<String> ret = new ArrayList<>();
        String regex = "(?<!\\\\)" + Pattern.quote(File.pathSeparator);
        for (String originalDir : classPath.split(regex)) {
            if (!originalDir.isEmpty()) {
                try {
                    originalDir = originalDir.replaceAll("\\\\" + Pattern.quote(File.pathSeparator), File.pathSeparator);
                    String canonicalDir = new File(originalDir).getCanonicalPath();
                    if (ModulePathSourceLocator.DUMMY_CLASSPATH_JDK9_FS.equals(originalDir)) {
                        v().java9Mode = true;
                    } else {
                        ret.add(canonicalDir);
                    }
                } catch (IOException e) {
                    throw new CompilationDeathException("Couldn't resolve classpath entry " + originalDir + ": " + e);
                }
            }
        }
        return Collections.unmodifiableList(ret);
    }

    public ClassSource getClassSource(String className) {
        ClassLoader cl;
        String fileName;
        InputStream stream;
        ClassSource ret;
        ClassSource ret2;
        if (this.classPath == null) {
            this.classPath = explodeClassPath(Scene.v().getSootClassPath());
        }
        if (this.classProviders == null) {
            setupClassProviders();
        }
        JavaClassProvider.JarException ex = null;
        for (ClassProvider cp : this.classProviders) {
            try {
                ret2 = cp.find(className);
            } catch (JavaClassProvider.JarException e) {
                ex = e;
            }
            if (ret2 != null) {
                return ret2;
            }
        }
        if (ex != null) {
            throw ex;
        }
        for (final ClassLoader cl2 : this.additionalClassLoaders) {
            try {
                ret = new ClassProvider() { // from class: soot.SourceLocator.3
                    @Override // soot.ClassProvider
                    public ClassSource find(String className2) {
                        String fileName2 = String.valueOf(className2.replace('.', '/')) + ".class";
                        InputStream stream2 = cl2.getResourceAsStream(fileName2);
                        if (stream2 == null) {
                            return null;
                        }
                        return new CoffiClassSource(className2, stream2, fileName2);
                    }
                }.find(className);
            } catch (JavaClassProvider.JarException e2) {
                ex = e2;
            }
            if (ret != null) {
                return ret;
            }
        }
        if (ex != null) {
            throw ex;
        }
        if (className.startsWith("soot.rtlib.tamiflex.") && (cl = getClass().getClassLoader()) != null && (stream = cl.getResourceAsStream((String.valueOf(className.replace('.', '/')) + ".class"))) != null) {
            return new CoffiClassSource(className, stream, fileName);
        }
        return null;
    }

    public void additionalClassLoader(ClassLoader c) {
        this.additionalClassLoaders.add(c);
    }

    protected void setupClassProviders() {
        List<ClassProvider> classProviders = new LinkedList<>();
        if (this.java9Mode) {
            classProviders.add(new AsmJava9ClassProvider());
        }
        ClassProvider classFileClassProvider = Options.v().coffi() ? new CoffiClassProvider() : new AsmClassProvider();
        switch (Options.v().src_prec()) {
            case 1:
                classProviders.add(classFileClassProvider);
                classProviders.add(new JimpleClassProvider());
                classProviders.add(new JavaClassProvider());
                break;
            case 2:
                classProviders.add(classFileClassProvider);
                break;
            case 3:
                classProviders.add(new JimpleClassProvider());
                classProviders.add(classFileClassProvider);
                classProviders.add(new JavaClassProvider());
                break;
            case 4:
                classProviders.add(new JavaClassProvider());
                classProviders.add(classFileClassProvider);
                classProviders.add(new JimpleClassProvider());
                break;
            case 5:
                classProviders.add(new DexClassProvider());
                classProviders.add(classFileClassProvider);
                classProviders.add(new JavaClassProvider());
                classProviders.add(new JimpleClassProvider());
                break;
            case 6:
                classProviders.add(new DexClassProvider());
                classProviders.add(classFileClassProvider);
                classProviders.add(new JimpleClassProvider());
                break;
            case 7:
                classProviders.add(new DotnetClassProvider());
                classProviders.add(new JimpleClassProvider());
                break;
            default:
                throw new RuntimeException("Other source precedences are not currently supported.");
        }
        this.classProviders = classProviders;
    }

    public void setClassProviders(List<ClassProvider> classProviders) {
        this.classProviders = classProviders;
    }

    public List<String> classPath() {
        return this.classPath;
    }

    public void invalidateClassPath() {
        this.classPath = null;
        this.dexClassIndex = null;
    }

    public List<String> sourcePath() {
        List<String> sourcePath = this.sourcePath;
        if (sourcePath == null) {
            sourcePath = new ArrayList<>();
            for (String dir : this.classPath) {
                ClassSourceType cst = getClassSourceType(dir);
                if (cst != ClassSourceType.apk && cst != ClassSourceType.jar && cst != ClassSourceType.zip) {
                    sourcePath.add(dir);
                }
            }
            this.sourcePath = sourcePath;
        }
        return sourcePath;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ClassSourceType getClassSourceType(String path) {
        try {
            return this.pathToSourceType.get(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getClassesUnder(String aPath) {
        return getClassesUnder(aPath, "");
    }

    /* JADX WARN: Finally extract failed */
    public List<String> getClassesUnder(String aPath, String prefix) {
        File[] fileArr;
        File[] fileArr2;
        AssemblyFile assemblyFile;
        if (ModulePathSourceLocator.DUMMY_CLASSPATH_JDK9_FS.equals(aPath)) {
            ArrayList<String> foundClasses = new ArrayList<>();
            for (List<String> classesInModule : ModulePathSourceLocator.v().getClassUnderModulePath("jrt:/").values()) {
                foundClasses.addAll(classesInModule);
            }
            return foundClasses;
        }
        List<String> classes = new ArrayList<>();
        ClassSourceType cst = getClassSourceType(aPath);
        if (cst == ClassSourceType.apk || cst == ClassSourceType.dex) {
            try {
                for (DexFileProvider.DexContainer<? extends DexFile> dex : DexFileProvider.v().getDexFromSource(new File(aPath))) {
                    classes.addAll(DexClassProvider.classesOfDex(dex.getBase().getDexFile()));
                }
            } catch (IOException e) {
                throw new CompilationDeathException("Error reading dex source", e);
            }
        } else if (cst == ClassSourceType.jar || cst == ClassSourceType.zip) {
            try {
                SharedCloseable<ZipFile> archive = this.archivePathToZip.getRef(aPath);
                try {
                    Enumeration<? extends ZipEntry> entries = archive.get().entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        String entryName = entry.getName();
                        if (entryName.endsWith(".class") || entryName.endsWith(".jimple")) {
                            classes.add(String.valueOf(prefix) + entryName.substring(0, entryName.lastIndexOf(46)).replace('/', '.'));
                        }
                    }
                    if (archive != null) {
                        archive.close();
                    }
                    try {
                        for (DexFileProvider.DexContainer<? extends DexFile> dex2 : DexFileProvider.v().getDexFromSource(new File(aPath))) {
                            classes.addAll(DexClassProvider.classesOfDex(dex2.getBase().getDexFile()));
                        }
                    } catch (IOException e2) {
                        logger.debug(e2.getMessage());
                    } catch (CompilationDeathException e3) {
                    }
                } catch (Throwable th) {
                    if (archive != null) {
                        archive.close();
                    }
                    throw th;
                }
            } catch (Throwable e4) {
                throw new CompilationDeathException("Error reading archive '" + aPath + "'", e4);
            }
        } else if ((Options.v().src_prec() == 7 && cst == ClassSourceType.directory) || cst == ClassSourceType.dll || cst == ClassSourceType.exe) {
            if (Strings.isNullOrEmpty(Options.v().dotnet_nativehost_path())) {
                throw new RuntimeException("Dotnet NativeHost Path is not set! Use -dotnet-nativehost-path Soot parameter!");
            }
            File file = new File(aPath);
            File[] files = new File[1];
            if (cst == ClassSourceType.directory) {
                File[] fileList = file.listFiles();
                if (fileList == null) {
                    return classes;
                }
                files = fileList;
            } else {
                files[0] = new File(aPath);
            }
            for (File element : files) {
                if (element.isDirectory()) {
                    classes.addAll(getClassesUnder(String.valueOf(aPath) + File.separatorChar + element.getName()));
                } else {
                    String fileName = element.getName();
                    if (fileName.endsWith(".dll") || fileName.endsWith(".exe")) {
                        try {
                            Map<String, File> classContainerIndex = v().dexClassIndex();
                            String canonicalPath = element.getCanonicalPath();
                            if (classContainerIndex.containsKey(canonicalPath)) {
                                assemblyFile = (AssemblyFile) classContainerIndex.get(canonicalPath);
                            } else {
                                assemblyFile = new AssemblyFile(canonicalPath);
                                if (!assemblyFile.isAssembly()) {
                                }
                            }
                            List<String> allClassNames = assemblyFile.getAllTypeNames();
                            if (allClassNames != null) {
                                classes.addAll(allClassNames);
                            }
                        } catch (IOException e5) {
                            logger.debug(e5.getMessage());
                        }
                    }
                }
            }
        } else if (cst == ClassSourceType.directory) {
            File file2 = new File(aPath);
            File[] files2 = file2.listFiles();
            if (files2 == null) {
                files2 = new File[]{file2};
            }
            for (File element2 : files2) {
                if (element2.isDirectory()) {
                    classes.addAll(getClassesUnder(String.valueOf(aPath) + File.separatorChar + element2.getName(), String.valueOf(prefix) + element2.getName() + '.'));
                } else {
                    String fileName2 = element2.getName();
                    if (fileName2.endsWith(".class")) {
                        classes.add(String.valueOf(prefix) + fileName2.substring(0, fileName2.lastIndexOf(".class")));
                    } else if (fileName2.endsWith(".jimple")) {
                        classes.add(String.valueOf(prefix) + fileName2.substring(0, fileName2.lastIndexOf(".jimple")));
                    } else if (fileName2.endsWith(".java")) {
                        classes.add(String.valueOf(prefix) + fileName2.substring(0, fileName2.lastIndexOf(".java")));
                    } else if (fileName2.endsWith(".dex")) {
                        try {
                            for (DexFileProvider.DexContainer<? extends DexFile> dex3 : DexFileProvider.v().getDexFromSource(element2)) {
                                classes.addAll(DexClassProvider.classesOfDex(dex3.getBase().getDexFile()));
                            }
                        } catch (IOException e6) {
                            logger.debug(e6.getMessage());
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException("Invalid class source type " + cst + " for " + aPath);
        }
        return classes;
    }

    public String getFileNameFor(SootClass c, int rep) {
        if (rep == 12) {
            return null;
        }
        StringBuilder b = new StringBuilder();
        if (!Options.v().output_jar()) {
            b.append(getOutputDir());
        }
        if (b.length() > 0 && b.charAt(b.length() - 1) != File.separatorChar) {
            b.append(File.separatorChar);
        }
        switch (rep) {
            case 14:
                b.append(c.getName().replace('.', File.separatorChar));
                break;
            case 15:
                return getDavaFilenameFor(c, b);
            case 16:
                b.append(c.getName().replace('.', '_'));
                b.append("_Maker");
                break;
            default:
                if ((rep == 1 || rep == 3) && Options.v().hierarchy_dirs()) {
                    b.append(c.getName().replace('.', File.separatorChar));
                    break;
                } else {
                    b.append(c.getName());
                    break;
                }
                break;
        }
        b.append(getExtensionFor(rep));
        return b.toString();
    }

    private String getDavaFilenameFor(SootClass c, StringBuilder b) {
        b.append("dava").append(File.separatorChar);
        ensureDirectoryExists(new File(String.valueOf(b.toString()) + "classes"));
        b.append("src").append(File.separatorChar);
        String fixedPackageName = c.getJavaPackageName();
        if (!fixedPackageName.isEmpty()) {
            b.append(fixedPackageName.replace('.', File.separatorChar)).append(File.separatorChar);
        }
        ensureDirectoryExists(new File(b.toString()));
        b.append(c.getShortJavaStyleName()).append(".java");
        return b.toString();
    }

    public Set<String> classesInDynamicPackage(String str) {
        HashSet<String> set = new HashSet<>(0);
        StringTokenizer strtok = new StringTokenizer(Scene.v().getSootClassPath(), File.pathSeparator);
        while (strtok.hasMoreTokens()) {
            String path = strtok.nextToken();
            if (getClassSourceType(path) == ClassSourceType.directory) {
                for (String filename : getClassesUnder(path)) {
                    if (filename.startsWith(str)) {
                        set.add(filename);
                    }
                }
                StringBuilder sb = new StringBuilder(path);
                sb.append(File.separatorChar);
                StringTokenizer tok = new StringTokenizer(str, ".");
                while (tok.hasMoreTokens()) {
                    sb.append(tok.nextToken());
                    if (tok.hasMoreTokens()) {
                        sb.append(File.separatorChar);
                    }
                }
                for (String string : getClassesUnder(sb.toString())) {
                    set.add(String.valueOf(str) + '.' + string);
                }
            }
        }
        return set;
    }

    public String getExtensionFor(int rep) {
        switch (rep) {
            case 1:
                return ".jimple";
            case 2:
                return ".jimp";
            case 3:
                return ".shimple";
            case 4:
                return ".shimp";
            case 5:
                return ".baf";
            case 6:
                return ".b";
            case 7:
                return ".grimple";
            case 8:
                return ".grimp";
            case 9:
                return ".xml";
            case 10:
            case 11:
            case 12:
            default:
                throw new RuntimeException();
            case 13:
                return ".jasmin";
            case 14:
                return ".class";
            case 15:
                return ".java";
            case 16:
                return ".java";
            case 17:
                return ".asm";
        }
    }

    public String getOutputDir() {
        File dir;
        String output_dir = Options.v().output_dir();
        if (output_dir.isEmpty()) {
            dir = new File("sootOutput");
        } else {
            dir = new File(output_dir);
            if (dir.getPath().endsWith(".jar")) {
                dir = dir.getParentFile();
                if (dir == null) {
                    dir = new File("");
                }
            }
        }
        ensureDirectoryExists(dir);
        return dir.getPath();
    }

    public String getOutputJarName() {
        File dir;
        if (!Options.v().output_jar()) {
            return "";
        }
        String output_dir = Options.v().output_dir();
        if (output_dir.isEmpty()) {
            dir = new File("sootOutput/out.jar");
        } else {
            dir = new File(output_dir);
            if (!dir.getPath().endsWith(".jar")) {
                dir = new File(dir.getPath(), "out.jar");
            }
        }
        ensureDirectoryExists(dir.getParentFile());
        return dir.getPath();
    }

    public IFoundFile lookupInClassPath(String fileName) {
        for (String dir : this.classPath) {
            IFoundFile ret = null;
            ClassSourceType cst = getClassSourceType(dir);
            if (cst == ClassSourceType.zip || cst == ClassSourceType.jar) {
                ret = lookupInArchive(dir, fileName);
            } else if (cst == ClassSourceType.directory) {
                ret = lookupInDir(dir, fileName);
            }
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    protected IFoundFile lookupInDir(String dir, String fileName) {
        File f = new File(dir, fileName);
        if (f.exists() && f.canRead()) {
            return new FoundFile(f);
        }
        return null;
    }

    protected IFoundFile lookupInArchive(String archivePath, String fileName) {
        try {
            Set<String> entryNames = this.archivePathToEntriesCache.get(archivePath);
            if (entryNames.contains(fileName)) {
                return new FoundFile(archivePath, fileName);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error: Failed to retrieve the archive entries list for the archive at path '" + archivePath + "'.", e);
        }
    }

    public String getSourceForClass(String className) {
        int i = className.indexOf(36);
        if (i > -1) {
            return className.substring(0, i);
        }
        return className;
    }

    public Map<String, File> dexClassIndex() {
        return this.dexClassIndex;
    }

    public void setDexClassIndex(Map<String, File> index) {
        this.dexClassIndex = index;
    }

    public void extendClassPath(String newPathElement) {
        this.classPath = null;
        if (newPathElement.endsWith(".dex") || newPathElement.endsWith(".apk") || newPathElement.endsWith(".exe") || newPathElement.endsWith(".dll")) {
            Set<String> dexClassPathExtensions = this.dexClassPathExtensions;
            if (dexClassPathExtensions == null) {
                Set<String> dexClassPathExtensions2 = new HashSet<>();
                this.dexClassPathExtensions = dexClassPathExtensions2;
            }
            this.dexClassPathExtensions.add(newPathElement);
        }
    }

    public Set<String> getDexClassPathExtensions() {
        return this.dexClassPathExtensions;
    }

    public void clearDexClassPathExtensions() {
        this.dexClassPathExtensions = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/SourceLocator$SharedZipFileCacheWrapper.class */
    public static class SharedZipFileCacheWrapper {
        private final SharedResourceCache<String, ZipFile> cache;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: gencallgraphv3.jar:soot/SourceLocator$SharedZipFileCacheWrapper$SharedResourceCache.class */
        public static class SharedResourceCache<K, V extends Closeable> extends ForwardingLoadingCache<K, SharedCloseable<V>> {
            private final LoadingCache<K, SharedCloseable<V>> delegate;
            private final DelayedRemovalListener<K, SharedCloseable<V>> removalListener = new DelayedRemovalListener<>(null);

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.cache.ForwardingLoadingCache, com.google.common.cache.LoadingCache
            public /* bridge */ /* synthetic */ Object get(Object obj) throws ExecutionException {
                return get((SharedResourceCache<K, V>) obj);
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* loaded from: gencallgraphv3.jar:soot/SourceLocator$SharedZipFileCacheWrapper$SharedResourceCache$DelayedRemovalListener.class */
            public static class DelayedRemovalListener<K, V extends SharedCloseable<?>> implements RemovalListener<K, V> {
                private static final BiFunction<Object, Integer, Integer> INC;
                private static final BiFunction<Object, Integer, Integer> DEC;
                private final ConcurrentHashMap<K, Integer> delayed;
                private final Queue<RemovalNotification<K, V>> delayQueue;
                static final /* synthetic */ boolean $assertionsDisabled;

                static {
                    $assertionsDisabled = !SourceLocator.class.desiredAssertionStatus();
                    INC = new BiFunction<Object, Integer, Integer>() { // from class: soot.SourceLocator.SharedZipFileCacheWrapper.SharedResourceCache.DelayedRemovalListener.1
                        @Override // java.util.function.BiFunction
                        public Integer apply(Object t, Integer u) {
                            return Integer.valueOf(u == null ? 1 : u.intValue() + 1);
                        }
                    };
                    DEC = new BiFunction<Object, Integer, Integer>() { // from class: soot.SourceLocator.SharedZipFileCacheWrapper.SharedResourceCache.DelayedRemovalListener.2
                        @Override // java.util.function.BiFunction
                        public Integer apply(Object t, Integer u) {
                            if (u.intValue() == 1) {
                                return null;
                            }
                            return Integer.valueOf(u.intValue() - 1);
                        }
                    };
                }

                private DelayedRemovalListener() {
                    this.delayed = new ConcurrentHashMap<>();
                    this.delayQueue = new ConcurrentLinkedQueue();
                }

                /* synthetic */ DelayedRemovalListener(DelayedRemovalListener delayedRemovalListener) {
                    this();
                }

                @Override // com.google.common.cache.RemovalListener
                public void onRemoval(RemovalNotification<K, V> rn) {
                    process();
                    removeOrEnqueue(rn, this.delayQueue);
                }

                public void delay(K key) {
                    Integer val = this.delayed.compute(key, INC);
                    if ($assertionsDisabled) {
                        return;
                    }
                    if (val == null || val.intValue() <= 0) {
                        throw new AssertionError();
                    }
                }

                public void release(K key) {
                    Integer val = this.delayed.compute(key, DEC);
                    if (!$assertionsDisabled && val != null && val.intValue() <= 0) {
                        throw new AssertionError();
                    }
                    process();
                }

                private void process() {
                    Queue<RemovalNotification<K, V>> delayFurther = new LinkedList<>();
                    while (true) {
                        RemovalNotification<K, V> rn = this.delayQueue.poll();
                        if (rn != null) {
                            removeOrEnqueue(rn, delayFurther);
                        } else {
                            this.delayQueue.addAll(delayFurther);
                            return;
                        }
                    }
                }

                private void removeOrEnqueue(RemovalNotification<K, V> rn, Queue<RemovalNotification<K, V>> q) {
                    if (this.delayed.containsKey(rn.getKey())) {
                        q.offer(rn);
                        return;
                    }
                    V val = rn.getValue();
                    if (!$assertionsDisabled && val == null) {
                        throw new AssertionError();
                    }
                    val.release();
                }
            }

            public SharedResourceCache(int initSize, int maxSize, final CacheLoader<K, V> loader) {
                this.delegate = CacheBuilder.newBuilder().initialCapacity(initSize).maximumSize(maxSize).concurrencyLevel(Runtime.getRuntime().availableProcessors()).expireAfterAccess(15L, TimeUnit.SECONDS).removalListener(this.removalListener).build(new CacheLoader<K, SharedCloseable<V>>() { // from class: soot.SourceLocator.SharedZipFileCacheWrapper.SharedResourceCache.1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // com.google.common.cache.CacheLoader
                    public /* bridge */ /* synthetic */ Object load(Object obj) throws Exception {
                        return load((AnonymousClass1) obj);
                    }

                    @Override // com.google.common.cache.CacheLoader
                    public SharedCloseable<V> load(K key) throws Exception {
                        return new SharedCloseable<>((Closeable) loader.load(key));
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.cache.ForwardingLoadingCache, com.google.common.cache.ForwardingCache, com.google.common.collect.ForwardingObject
            public final LoadingCache<K, SharedCloseable<V>> delegate() {
                return this.delegate;
            }

            @Override // com.google.common.cache.ForwardingLoadingCache, com.google.common.cache.LoadingCache
            public final SharedCloseable<V> get(K key) throws ExecutionException {
                this.removalListener.delay(key);
                try {
                    return ((SharedCloseable) super.get((SharedResourceCache<K, V>) key)).acquire();
                } finally {
                    this.removalListener.release(key);
                }
            }
        }

        public SharedZipFileCacheWrapper(int initSize, int maxSize) {
            this.cache = new SharedResourceCache<>(initSize, maxSize, new CacheLoader<String, ZipFile>() { // from class: soot.SourceLocator.SharedZipFileCacheWrapper.1
                @Override // com.google.common.cache.CacheLoader
                public ZipFile load(String archivePath) throws Exception {
                    return new ZipFile(archivePath);
                }
            });
        }

        public SharedCloseable<ZipFile> getRef(String archivePath) throws ExecutionException {
            return this.cache.get((SharedResourceCache<String, ZipFile>) archivePath);
        }
    }
}
