package soot;

import com.google.common.base.Optional;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.JavaClassProvider;
import soot.ModuleUtil;
import soot.Singletons;
import soot.SourceLocator;
import soot.asm.AsmModuleClassProvider;
/* loaded from: gencallgraphv3.jar:soot/ModulePathSourceLocator.class */
public class ModulePathSourceLocator extends SourceLocator {
    private static final Logger logger;
    public static final String DUMMY_CLASSPATH_JDK9_FS = "VIRTUAL_FS_FOR_JDK";
    private final HashMap<String, Path> moduleNameToPath;
    private Set<String> classesToLoad;
    private List<String> modulePath;
    private int nextPathEntry;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$SourceLocator$ClassSourceType;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ModulePathSourceLocator.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(ModulePathSourceLocator.class);
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$SourceLocator$ClassSourceType() {
        int[] iArr = $SWITCH_TABLE$soot$SourceLocator$ClassSourceType;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[SourceLocator.ClassSourceType.valuesCustom().length];
        try {
            iArr2[SourceLocator.ClassSourceType.apk.ordinal()] = 3;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[SourceLocator.ClassSourceType.dex.ordinal()] = 4;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[SourceLocator.ClassSourceType.directory.ordinal()] = 5;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[SourceLocator.ClassSourceType.dll.ordinal()] = 9;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[SourceLocator.ClassSourceType.exe.ordinal()] = 8;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[SourceLocator.ClassSourceType.jar.ordinal()] = 1;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[SourceLocator.ClassSourceType.jrt.ordinal()] = 6;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[SourceLocator.ClassSourceType.unknown.ordinal()] = 7;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            iArr2[SourceLocator.ClassSourceType.zip.ordinal()] = 2;
        } catch (NoSuchFieldError unused9) {
        }
        $SWITCH_TABLE$soot$SourceLocator$ClassSourceType = iArr2;
        return iArr2;
    }

    public ModulePathSourceLocator(Singletons.Global g) {
        super(g);
        this.moduleNameToPath = new HashMap<>();
        this.nextPathEntry = 0;
    }

    public static ModulePathSourceLocator v() {
        return G.v().soot_ModulePathSourceLocator();
    }

    @Override // soot.SourceLocator
    public ClassSource getClassSource(String className) {
        ModuleUtil.ModuleClassNameWrapper wrapper = ModuleUtil.v().makeWrapper(className);
        return getClassSource(wrapper.getClassName(), wrapper.getModuleNameOptional());
    }

    public ClassSource getClassSource(String className, Optional<String> moduleName) {
        ClassSource ret;
        if (this.classesToLoad == null) {
            Set<String> classesToLoad = new HashSet<>(ModuleScene.v().getBasicClasses());
            for (SootClass c : ModuleScene.v().getApplicationClasses()) {
                classesToLoad.add(c.getName());
            }
            this.classesToLoad = classesToLoad;
        }
        if (this.modulePath == null) {
            this.modulePath = explodeModulePath(ModuleScene.v().getSootModulePath());
        }
        if (this.classProviders == null) {
            setupClassProviders();
        }
        JavaClassProvider.JarException ex = null;
        String searchFor = moduleName.isPresent() ? String.valueOf(moduleName.get()) + ':' + className : className;
        for (ClassProvider cp : this.classProviders) {
            try {
                ret = cp.find(searchFor);
            } catch (JavaClassProvider.JarException e) {
                ex = e;
            }
            if (ret != null) {
                return ret;
            }
        }
        if (ex != null) {
            throw ex;
        }
        return null;
    }

    public static List<String> explodeModulePath(String classPath) {
        List<String> ret = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(classPath, File.pathSeparator);
        while (tokenizer.hasMoreTokens()) {
            String originalDir = tokenizer.nextToken();
            try {
                String canonicalDir = new File(originalDir).getCanonicalPath();
                if (DUMMY_CLASSPATH_JDK9_FS.equals(originalDir)) {
                    canonicalDir = "jrt:/";
                }
                ret.add(canonicalDir);
            } catch (IOException e) {
                throw new CompilationDeathException("Couldn't resolve classpath entry " + originalDir + ": " + e);
            }
        }
        return ret;
    }

    @Override // soot.SourceLocator
    public void additionalClassLoader(ClassLoader c) {
        this.additionalClassLoaders.add(c);
    }

    @Override // soot.SourceLocator
    public List<String> classPath() {
        return this.modulePath;
    }

    @Override // soot.SourceLocator
    public void invalidateClassPath() {
        this.modulePath = null;
        super.invalidateClassPath();
    }

    @Override // soot.SourceLocator
    public List<String> sourcePath() {
        List<String> sourcePath = this.sourcePath;
        if (sourcePath == null) {
            sourcePath = new ArrayList<>();
            for (String dir : this.modulePath) {
                SourceLocator.ClassSourceType cst = getClassSourceType(dir);
                if (cst != SourceLocator.ClassSourceType.apk && cst != SourceLocator.ClassSourceType.jar && cst != SourceLocator.ClassSourceType.zip) {
                    sourcePath.add(dir);
                }
            }
            this.sourcePath = sourcePath;
        }
        return sourcePath;
    }

    @Override // soot.SourceLocator
    public List<String> getClassesUnder(String aPath) {
        List<String> classes = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : getClassUnderModulePath(aPath).entrySet()) {
            for (String className : entry.getValue()) {
                classes.add(String.valueOf(entry.getKey()) + ':' + className);
            }
        }
        return classes;
    }

    public Map<String, List<String>> getClassUnderModulePath(String aPath) {
        Path path;
        switch ($SWITCH_TABLE$soot$SourceLocator$ClassSourceType()[getClassSourceType(aPath).ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            default:
                path = Paths.get(aPath, new String[0]);
                break;
            case 6:
                path = getRootModulesPathOfJDK();
                break;
            case 7:
                path = null;
                break;
        }
        if (this.classProviders == null) {
            setupClassProviders();
        }
        if (path == null) {
            throw new RuntimeException("[Error] The path " + aPath + "is not a valid path.");
        }
        BasicFileAttributes attrs = null;
        try {
            attrs = Files.readAttributes(path, BasicFileAttributes.class, new LinkOption[0]);
        } catch (IOException e) {
            logger.debug(e.getMessage(), (Throwable) e);
        }
        if ($assertionsDisabled || attrs != null) {
            Map<String, List<String>> mapModuleClasses = new HashMap<>();
            if (attrs.isDirectory()) {
                if (!Files.exists(path.resolve(SootModuleInfo.MODULE_INFO_FILE), new LinkOption[0])) {
                    mapModuleClasses.putAll(discoverModulesIn(path));
                } else {
                    mapModuleClasses.putAll(buildModuleForExplodedModule(path));
                }
            } else if (attrs.isRegularFile() && path.getFileName().toString().endsWith(".jar")) {
                mapModuleClasses.putAll(buildModuleForJar(path));
            }
            return mapModuleClasses;
        }
        throw new AssertionError();
    }

    public static Path getRootModulesPathOfJDK() {
        Path p = Paths.get(URI.create("jrt:/"));
        if (p.endsWith("modules")) {
            return p;
        }
        return p.resolve("modules");
    }

    /* JADX WARN: Finally extract failed */
    private Map<String, List<String>> discoverModulesIn(Path path) {
        Map<String, List<String>> mapModuleClasses = new HashMap<>();
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(path);
            try {
                for (Path entry : stream) {
                    try {
                        BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class, new LinkOption[0]);
                        if (attrs.isDirectory()) {
                            if (Files.exists(entry.resolve(SootModuleInfo.MODULE_INFO_FILE), new LinkOption[0])) {
                                mapModuleClasses.putAll(buildModuleForExplodedModule(entry));
                            }
                        } else if (attrs.isRegularFile() && entry.getFileName().toString().endsWith(".jar")) {
                            mapModuleClasses.putAll(buildModuleForJar(entry));
                        }
                    } catch (NoSuchFileException e) {
                    }
                }
                if (stream != null) {
                    stream.close();
                }
            } catch (Throwable th) {
                if (stream != null) {
                    stream.close();
                }
                throw th;
            }
        } catch (IOException e2) {
            logger.debug(e2.getMessage(), (Throwable) e2);
        }
        return mapModuleClasses;
    }

    private Map<String, List<String>> buildModuleForJar(Path jar) {
        SootModuleInfo moduleInfo;
        Map<String, List<String>> moduleClassMap = new HashMap<>();
        try {
            FileSystem zipFileSystem = FileSystems.newFileSystem(jar, getClass().getClassLoader());
            try {
                Path mi = zipFileSystem.getPath(SootModuleInfo.MODULE_INFO_FILE, new String[0]);
                if (Files.exists(mi, new LinkOption[0])) {
                    FoundFile foundFile = new FoundFile(mi);
                    for (ClassProvider cp : this.classProviders) {
                        if (cp instanceof AsmModuleClassProvider) {
                            String moduleName = ((AsmModuleClassProvider) cp).getModuleName(foundFile);
                            SootModuleInfo moduleInfo2 = (SootModuleInfo) SootModuleResolver.v().makeClassRef(SootModuleInfo.MODULE_INFO, Optional.of(moduleName));
                            this.moduleNameToPath.put(moduleName, jar);
                            List<String> classesInJar = super.getClassesUnder(jar.toAbsolutePath().toString());
                            for (String foundClass : classesInJar) {
                                int index = foundClass.lastIndexOf(46);
                                if (index > 0) {
                                    moduleInfo2.addModulePackage(foundClass.substring(0, index));
                                }
                            }
                            moduleClassMap.put(moduleName, classesInJar);
                        }
                    }
                } else {
                    String moduleName2 = createModuleNameForAutomaticModule(jar.getFileName().toString());
                    if (!ModuleScene.v().containsClass(SootModuleInfo.MODULE_INFO, Optional.of(moduleName2))) {
                        moduleInfo = new SootModuleInfo(SootModuleInfo.MODULE_INFO, moduleName2, true);
                        Scene.v().addClass(moduleInfo);
                        moduleInfo.setApplicationClass();
                    } else {
                        moduleInfo = (SootModuleInfo) ModuleScene.v().getSootClass(SootModuleInfo.MODULE_INFO, Optional.of(moduleName2));
                        if (moduleInfo.resolvingLevel() != 0) {
                            return moduleClassMap;
                        }
                    }
                    List<String> classesInJar2 = super.getClassesUnder(jar.toAbsolutePath().toString());
                    for (String foundClass2 : classesInJar2) {
                        int index2 = foundClass2.lastIndexOf(46);
                        if (index2 > 0) {
                            moduleInfo.addModulePackage(foundClass2.substring(0, index2));
                        }
                    }
                    moduleInfo.setResolvingLevel(3);
                    moduleInfo.setAutomaticModule(true);
                    this.moduleNameToPath.put(moduleName2, jar);
                    moduleClassMap.put(moduleName2, classesInJar2);
                }
                if (zipFileSystem != null) {
                    zipFileSystem.close();
                }
            } finally {
                if (zipFileSystem != null) {
                    zipFileSystem.close();
                }
            }
        } catch (IOException e) {
            logger.debug(e.getMessage(), (Throwable) e);
        }
        return moduleClassMap;
    }

    private String createModuleNameForAutomaticModule(String filename) {
        int i = filename.lastIndexOf(File.separatorChar);
        if (i != -1) {
            filename = filename.substring(i + 1);
        }
        String moduleName = filename.substring(0, filename.length() - 4);
        Matcher matcher = Patterns.VERSION.matcher(moduleName);
        if (matcher.find()) {
            moduleName = moduleName.substring(0, matcher.start());
        }
        String moduleName2 = Patterns.REPEATING_DOTS.matcher(Patterns.ALPHA_NUM.matcher(moduleName).replaceAll(".")).replaceAll(".");
        if (moduleName2.length() > 0 && moduleName2.charAt(0) == '.') {
            moduleName2 = Patterns.LEADING_DOTS.matcher(moduleName2).replaceAll("");
        }
        int len = moduleName2.length();
        if (len > 0 && moduleName2.charAt(len - 1) == '.') {
            moduleName2 = Patterns.TRAILING_DOTS.matcher(moduleName2).replaceAll("");
        }
        return moduleName2;
    }

    private Map<String, List<String>> buildModuleForExplodedModule(Path dir) {
        Map<String, List<String>> moduleClassesMap = new HashMap<>();
        Path mi = dir.resolve(SootModuleInfo.MODULE_INFO_FILE);
        for (ClassProvider cp : this.classProviders) {
            if (cp instanceof AsmModuleClassProvider) {
                String moduleName = ((AsmModuleClassProvider) cp).getModuleName(new FoundFile(mi));
                SootModuleInfo moduleInfo = (SootModuleInfo) SootModuleResolver.v().makeClassRef(SootModuleInfo.MODULE_INFO, Optional.of(moduleName));
                this.moduleNameToPath.put(moduleName, dir);
                List<String> classes = getClassesUnderDirectory(dir);
                for (String foundClass : classes) {
                    int index = foundClass.lastIndexOf(46);
                    if (index > 0) {
                        moduleInfo.addModulePackage(foundClass.substring(0, index));
                    }
                }
                moduleClassesMap.put(moduleName, classes);
            }
        }
        return moduleClassesMap;
    }

    @Override // soot.SourceLocator
    public Set<String> classesInDynamicPackage(String str) {
        HashSet<String> set = new HashSet<>(0);
        StringTokenizer strtok = new StringTokenizer(ModuleScene.v().getSootModulePath(), File.pathSeparator);
        while (strtok.hasMoreTokens()) {
            String path = strtok.nextToken();
            for (String filename : super.getClassesUnder(path)) {
                if (filename.startsWith(str)) {
                    set.add(filename);
                }
            }
            StringBuilder sb = new StringBuilder(path);
            sb.append(File.pathSeparatorChar);
            StringTokenizer tok = new StringTokenizer(str, ".");
            while (tok.hasMoreTokens()) {
                sb.append(tok.nextToken());
                if (tok.hasMoreTokens()) {
                    sb.append(File.pathSeparatorChar);
                }
            }
            for (String string : super.getClassesUnder(sb.toString())) {
                set.add(String.valueOf(str) + '.' + string);
            }
        }
        return set;
    }

    @Override // soot.SourceLocator
    public IFoundFile lookupInClassPath(String fileName) {
        return lookUpInModulePath(fileName);
    }

    private SourceLocator.ClassSourceType getClassSourceType(Path path) {
        if (path.toUri().toString().startsWith("jrt:/")) {
            return SourceLocator.ClassSourceType.jrt;
        }
        return super.getClassSourceType(path.toAbsolutePath().toString());
    }

    @Override // soot.SourceLocator
    protected SourceLocator.ClassSourceType getClassSourceType(String path) {
        if (path.startsWith("jrt:/")) {
            return SourceLocator.ClassSourceType.jrt;
        }
        return super.getClassSourceType(path);
    }

    public IFoundFile lookUpInModulePath(String fileName) {
        String[] moduleAndClassName = fileName.split(":");
        String className = moduleAndClassName[moduleAndClassName.length - 1];
        String moduleName = moduleAndClassName[0];
        if (className.isEmpty() || moduleName.isEmpty()) {
            throw new RuntimeException("No module given!");
        }
        Path foundModulePath = discoverModule(moduleName);
        if (foundModulePath == null) {
            return null;
        }
        String uriString = foundModulePath.toUri().toString();
        String dir = uriString.startsWith("jrt:/") ? uriString : foundModulePath.toAbsolutePath().toString();
        SourceLocator.ClassSourceType cst = getClassSourceType(foundModulePath);
        if (cst != null) {
            switch ($SWITCH_TABLE$soot$SourceLocator$ClassSourceType()[cst.ordinal()]) {
                case 1:
                case 2:
                    return lookupInArchive(dir, className);
                case 3:
                case 4:
                default:
                    return null;
                case 5:
                    return lookupInDir(dir, className);
                case 6:
                    return lookUpInVirtualFileSystem(dir, className);
            }
        }
        return null;
    }

    private Path discoverModule(String moduleName) {
        Path pathToModule = this.moduleNameToPath.get(moduleName);
        if (pathToModule != null) {
            return pathToModule;
        }
        while (this.nextPathEntry < this.modulePath.size()) {
            getClassUnderModulePath(this.modulePath.get(this.nextPathEntry));
            this.nextPathEntry++;
            Path pathToModule2 = this.moduleNameToPath.get(moduleName);
            if (pathToModule2 != null) {
                return pathToModule2;
            }
        }
        return null;
    }

    @Override // soot.SourceLocator
    protected IFoundFile lookupInDir(String dir, String fileName) {
        Path dirPath = Paths.get(dir, new String[0]);
        Path foundFile = dirPath.resolve(fileName);
        if (foundFile != null && Files.isRegularFile(foundFile, new LinkOption[0])) {
            return new FoundFile(foundFile);
        }
        return null;
    }

    @Override // soot.SourceLocator
    protected IFoundFile lookupInArchive(String archivePath, String fileName) {
        Path archive = Paths.get(archivePath, new String[0]);
        try {
            FileSystem zipFileSystem = FileSystems.newFileSystem(archive, getClass().getClassLoader());
            try {
                Path entry = zipFileSystem.getPath(fileName, new String[0]);
                if (entry == null || !Files.isRegularFile(entry, new LinkOption[0])) {
                }
                FoundFile foundFile = new FoundFile(archive.toAbsolutePath().toString(), fileName);
                if (zipFileSystem != null) {
                    zipFileSystem.close();
                }
                return foundFile;
            } finally {
                if (zipFileSystem != null) {
                    zipFileSystem.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Caught IOException " + e + " looking in archive file " + archivePath + " for file " + fileName);
        }
    }

    public IFoundFile lookUpInVirtualFileSystem(String archivePath, String fileName) {
        Path foundFile = Paths.get(URI.create(archivePath)).resolve(fileName);
        if (foundFile != null && Files.isRegularFile(foundFile, new LinkOption[0])) {
            return new FoundFile(foundFile);
        }
        return null;
    }

    @Override // soot.SourceLocator
    protected void setupClassProviders() {
        LinkedList<ClassProvider> classProviders = new LinkedList<>();
        classProviders.add(new AsmModuleClassProvider());
        this.classProviders = classProviders;
    }

    private List<String> getClassesUnderDirectory(final Path aPath) {
        SourceLocator.ClassSourceType cst = getClassSourceType(aPath);
        if (cst != SourceLocator.ClassSourceType.directory && cst != SourceLocator.ClassSourceType.jrt) {
            throw new RuntimeException("Invalid class source type");
        }
        final List<String> classes = new ArrayList<>();
        FileVisitor<Path> fileVisitor = new FileVisitor<Path>() { // from class: soot.ModulePathSourceLocator.1
            @Override // java.nio.file.FileVisitor
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override // java.nio.file.FileVisitor
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String fileName = aPath.relativize(file).toString().replace(file.getFileSystem().getSeparator(), ".");
                if (fileName.endsWith(".class")) {
                    classes.add(fileName.substring(0, fileName.lastIndexOf(".class")));
                } else if (fileName.endsWith(".jimple")) {
                    classes.add(fileName.substring(0, fileName.lastIndexOf(".jimple")));
                } else if (fileName.endsWith(".java")) {
                    classes.add(fileName.substring(0, fileName.lastIndexOf(".java")));
                }
                return FileVisitResult.CONTINUE;
            }

            @Override // java.nio.file.FileVisitor
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override // java.nio.file.FileVisitor
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(aPath, fileVisitor);
        } catch (IOException e) {
            logger.debug(e.getMessage(), (Throwable) e);
        }
        return classes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/ModulePathSourceLocator$Patterns.class */
    public static class Patterns {
        static final Pattern VERSION = Pattern.compile("-(\\d+(\\.|$))");
        static final Pattern ALPHA_NUM = Pattern.compile("[^A-Za-z0-9]");
        static final Pattern REPEATING_DOTS = Pattern.compile("(\\.)(\\1)+");
        static final Pattern LEADING_DOTS = Pattern.compile("^\\.");
        static final Pattern TRAILING_DOTS = Pattern.compile("\\.$");

        private Patterns() {
        }
    }
}
