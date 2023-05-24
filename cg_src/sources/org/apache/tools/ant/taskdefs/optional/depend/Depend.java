package org.apache.tools.ant.taskdefs.optional.depend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.taskdefs.rmic.DefaultRmicAdapter;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.depend.DependencyAnalyzer;
import soot.dava.internal.AST.ASTNode;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/Depend.class */
public class Depend extends MatchingTask {
    private static final int ONE_SECOND = 1000;
    private Path srcPath;
    private Path destPath;
    private File cache;
    private Map<String, Map<String, ClassFileInfo>> affectedClassMap;
    private Map<String, ClassFileInfo> classFileInfoMap;
    private Map<String, Set<File>> classpathDependencies;
    private Map<String, String> outOfDateClasses;
    private boolean closure = false;
    private boolean warnOnRmiStubs = true;
    private boolean dump = false;
    private Path dependClasspath;
    private static final String CACHE_FILE_NAME = "dependencies.txt";
    private static final String CLASSNAME_PREPEND = "||:";

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/Depend$ClassFileInfo.class */
    public static class ClassFileInfo {
        private File absoluteFile;
        private String className;
        private File sourceFile;
        private boolean isUserWarned;

        private ClassFileInfo() {
            this.isUserWarned = false;
        }
    }

    public void setClasspath(Path classpath) {
        if (this.dependClasspath == null) {
            this.dependClasspath = classpath;
        } else {
            this.dependClasspath.append(classpath);
        }
    }

    public Path getClasspath() {
        return this.dependClasspath;
    }

    public Path createClasspath() {
        if (this.dependClasspath == null) {
            this.dependClasspath = new Path(getProject());
        }
        return this.dependClasspath.createPath();
    }

    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    public void setWarnOnRmiStubs(boolean warnOnRmiStubs) {
        this.warnOnRmiStubs = warnOnRmiStubs;
    }

    private Map<String, List<String>> readCachedDependencies(File depFile) throws IOException {
        Map<String, List<String>> dependencyMap = new HashMap<>();
        int prependLength = CLASSNAME_PREPEND.length();
        BufferedReader in = new BufferedReader(new FileReader(depFile));
        List<String> dependencyList = null;
        while (true) {
            try {
                String line = in.readLine();
                if (line != null) {
                    if (line.startsWith(CLASSNAME_PREPEND)) {
                        String className = line.substring(prependLength);
                        dependencyList = dependencyMap.computeIfAbsent(className, k -> {
                            return new ArrayList();
                        });
                    } else if (dependencyList != null) {
                        dependencyList.add(line);
                    }
                } else {
                    in.close();
                    return dependencyMap;
                }
            } catch (Throwable th) {
                try {
                    in.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    }

    private void writeCachedDependencies(Map<String, List<String>> dependencyMap) throws IOException {
        if (this.cache != null) {
            this.cache.mkdirs();
            File depFile = new File(this.cache, CACHE_FILE_NAME);
            BufferedWriter pw = new BufferedWriter(new FileWriter(depFile));
            try {
                for (Map.Entry<String, List<String>> e : dependencyMap.entrySet()) {
                    pw.write(String.format("%s%s%n", CLASSNAME_PREPEND, e.getKey()));
                    for (String s : e.getValue()) {
                        pw.write(s);
                        pw.newLine();
                    }
                }
                pw.close();
            } catch (Throwable th) {
                try {
                    pw.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    }

    private Path getCheckClassPath() {
        Path p;
        if (this.dependClasspath == null) {
            return null;
        }
        Set<Resource> dependNotInDest = new LinkedHashSet<>();
        Path path = this.dependClasspath;
        Objects.requireNonNull(dependNotInDest);
        path.forEach((v1) -> {
            r1.add(v1);
        });
        Path path2 = this.destPath;
        Objects.requireNonNull(dependNotInDest);
        path2.forEach((v1) -> {
            r1.remove(v1);
        });
        if (dependNotInDest.isEmpty()) {
            p = null;
        } else {
            p = new Path(getProject());
            Objects.requireNonNull(p);
            dependNotInDest.forEach((v1) -> {
                r1.add(v1);
            });
        }
        log("Classpath without dest dir is " + p, 4);
        return p;
    }

    private void determineDependencies() throws IOException {
        this.affectedClassMap = new HashMap();
        this.classFileInfoMap = new HashMap();
        boolean cacheDirty = false;
        Map<String, List<String>> dependencyMap = new HashMap<>();
        boolean cacheFileExists = true;
        long cacheLastModified = Long.MAX_VALUE;
        if (this.cache != null) {
            File cacheFile = new File(this.cache, CACHE_FILE_NAME);
            cacheFileExists = cacheFile.exists();
            cacheLastModified = cacheFile.lastModified();
            if (cacheFileExists) {
                dependencyMap = readCachedDependencies(cacheFile);
            }
        }
        for (ClassFileInfo info : getClassFiles()) {
            log("Adding class info for " + info.className, 4);
            this.classFileInfoMap.put(info.className, info);
            List<String> dependencyList = null;
            if (this.cache != null && cacheFileExists && cacheLastModified > info.absoluteFile.lastModified()) {
                dependencyList = dependencyMap.get(info.className);
            }
            if (dependencyList == null) {
                DependencyAnalyzer analyzer = new AntAnalyzer();
                analyzer.addRootClass(info.className);
                analyzer.addClassPath(this.destPath);
                analyzer.setClosure(false);
                dependencyList = Collections.list(analyzer.getClassDependencies());
                dependencyList.forEach(o -> {
                    log("Class " + info.className + " depends on " + info, 4);
                });
                cacheDirty = true;
                dependencyMap.put(info.className, dependencyList);
            }
            for (String dependentClass : dependencyList) {
                this.affectedClassMap.computeIfAbsent(dependentClass, k -> {
                    return new HashMap();
                }).put(info.className, info);
                log(dependentClass + " affects " + info.className, 4);
            }
        }
        this.classpathDependencies = null;
        Path checkPath = getCheckClassPath();
        if (checkPath != null) {
            this.classpathDependencies = new HashMap();
            AntClassLoader loader = getProject().createClassLoader(checkPath);
            try {
                Map<String, Object> classpathFileCache = new HashMap<>();
                Object nullFileMarker = new Object();
                for (Map.Entry<String, List<String>> e : dependencyMap.entrySet()) {
                    String className = e.getKey();
                    log("Determining classpath dependencies for " + className, 4);
                    List<String> dependencyList2 = e.getValue();
                    Set<File> dependencies = new HashSet<>();
                    this.classpathDependencies.put(className, dependencies);
                    for (String dependency : dependencyList2) {
                        log("Looking for " + dependency, 4);
                        Object classpathFileObject = classpathFileCache.get(dependency);
                        if (classpathFileObject == null) {
                            classpathFileObject = nullFileMarker;
                            if (!dependency.startsWith("java.") && !dependency.startsWith("javax.")) {
                                URL classURL = loader.getResource(dependency.replace('.', '/') + ".class");
                                log("URL is " + classURL, 4);
                                if (classURL != null) {
                                    if ("jar".equals(classURL.getProtocol())) {
                                        String jarFilePath = classURL.getFile();
                                        int classMarker = jarFilePath.indexOf(33);
                                        String jarFilePath2 = jarFilePath.substring(0, classMarker);
                                        if (jarFilePath2.startsWith("file:")) {
                                            classpathFileObject = new File(FileUtils.getFileUtils().fromURI(jarFilePath2));
                                        } else {
                                            throw new IOException("Bizarre nested path in jar: protocol: " + jarFilePath2);
                                        }
                                    } else if ("file".equals(classURL.getProtocol())) {
                                        classpathFileObject = new File(FileUtils.getFileUtils().fromURI(classURL.toExternalForm()));
                                    }
                                    log("Class " + className + " depends on " + classpathFileObject + " due to " + dependency, 4);
                                }
                            } else {
                                log("Ignoring base classlib dependency " + dependency, 4);
                            }
                            classpathFileCache.put(dependency, classpathFileObject);
                        }
                        if (classpathFileObject != nullFileMarker) {
                            File jarFile = (File) classpathFileObject;
                            log("Adding a classpath dependency on " + jarFile, 4);
                            dependencies.add(jarFile);
                        }
                    }
                }
                if (loader != null) {
                    loader.close();
                }
            } catch (Throwable th) {
                if (loader != null) {
                    try {
                        loader.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } else {
            log("No classpath to check", 4);
        }
        if (this.cache != null && cacheDirty) {
            writeCachedDependencies(dependencyMap);
        }
    }

    private int deleteAllAffectedFiles() {
        int count = 0;
        for (String className : this.outOfDateClasses.keySet()) {
            count += deleteAffectedFiles(className);
            ClassFileInfo classInfo = this.classFileInfoMap.get(className);
            if (classInfo != null && classInfo.absoluteFile.exists()) {
                if (classInfo.sourceFile != null) {
                    classInfo.absoluteFile.delete();
                    count++;
                } else {
                    warnOutOfDateButNotDeleted(classInfo, className, className);
                }
            }
        }
        return count;
    }

    private int deleteAffectedFiles(String className) {
        int count = 0;
        Map<String, ClassFileInfo> affectedClasses = this.affectedClassMap.get(className);
        if (affectedClasses == null) {
            return 0;
        }
        for (Map.Entry<String, ClassFileInfo> e : affectedClasses.entrySet()) {
            String affectedClass = e.getKey();
            ClassFileInfo affectedClassInfo = e.getValue();
            if (affectedClassInfo.absoluteFile.exists()) {
                if (affectedClassInfo.sourceFile == null) {
                    warnOutOfDateButNotDeleted(affectedClassInfo, affectedClass, className);
                } else {
                    log("Deleting file " + affectedClassInfo.absoluteFile.getPath() + " since " + className + " out of date", 3);
                    affectedClassInfo.absoluteFile.delete();
                    count++;
                    if (this.closure) {
                        count += deleteAffectedFiles(affectedClass);
                    } else if (affectedClass.contains("$")) {
                        String topLevelClassName = affectedClass.substring(0, affectedClass.indexOf("$"));
                        log("Top level class = " + topLevelClassName, 3);
                        ClassFileInfo topLevelClassInfo = this.classFileInfoMap.get(topLevelClassName);
                        if (topLevelClassInfo != null && topLevelClassInfo.absoluteFile.exists()) {
                            log("Deleting file " + topLevelClassInfo.absoluteFile.getPath() + " since one of its inner classes was removed", 3);
                            topLevelClassInfo.absoluteFile.delete();
                            count++;
                            if (this.closure) {
                                count += deleteAffectedFiles(topLevelClassName);
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    private void warnOutOfDateButNotDeleted(ClassFileInfo affectedClassInfo, String affectedClass, String className) {
        if (affectedClassInfo.isUserWarned) {
            return;
        }
        int level = 1;
        if (!this.warnOnRmiStubs && isRmiStub(affectedClass, className)) {
            level = 3;
        }
        log("The class " + affectedClass + " in file " + affectedClassInfo.absoluteFile.getPath() + " is out of date due to " + className + " but has not been deleted because its source file could not be determined", level);
        affectedClassInfo.isUserWarned = true;
    }

    private boolean isRmiStub(String affectedClass, String className) {
        return isStub(affectedClass, className, DefaultRmicAdapter.RMI_STUB_SUFFIX) || isStub(affectedClass, className, DefaultRmicAdapter.RMI_SKEL_SUFFIX) || isStub(affectedClass, className, DefaultRmicAdapter.RMI_STUB_SUFFIX) || isStub(affectedClass, className, DefaultRmicAdapter.RMI_SKEL_SUFFIX);
    }

    private boolean isStub(String affectedClass, String baseClass, String suffix) {
        return (baseClass + suffix).equals(affectedClass);
    }

    private void dumpDependencies() {
        log("Reverse Dependency Dump for " + this.affectedClassMap.size() + " classes:", 4);
        this.affectedClassMap.forEach(className, affectedClasses -> {
            log(" Class " + className + " affects:", 4);
            affectedClasses.forEach(affectedClass, info -> {
                log(ASTNode.TAB + affectedClass + " in " + info.absoluteFile.getPath(), 4);
            });
        });
        if (this.classpathDependencies != null) {
            log("Classpath file dependencies (Forward):", 4);
            this.classpathDependencies.forEach(className2, dependencies -> {
                log(" Class " + className2 + " depends on:", 4);
                dependencies.forEach(f -> {
                    log(ASTNode.TAB + f.getPath(), 4);
                });
            });
        }
    }

    private void determineOutOfDateClasses() {
        ClassFileInfo info;
        this.outOfDateClasses = new HashMap();
        directories(this.srcPath).forEach(srcDir -> {
            DirectoryScanner ds = getDirectoryScanner(srcDir);
            scanDir(srcDir, ds.getIncludedFiles());
        });
        if (this.classpathDependencies == null) {
            return;
        }
        for (Map.Entry<String, Set<File>> e : this.classpathDependencies.entrySet()) {
            String className = e.getKey();
            if (!this.outOfDateClasses.containsKey(className) && (info = this.classFileInfoMap.get(className)) != null) {
                Iterator<File> it = e.getValue().iterator();
                while (true) {
                    if (it.hasNext()) {
                        File classpathFile = it.next();
                        if (classpathFile.lastModified() > info.absoluteFile.lastModified()) {
                            log("Class " + className + " is out of date with respect to " + classpathFile, 4);
                            this.outOfDateClasses.put(className, className);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        int summaryLogLevel;
        try {
            long start = System.currentTimeMillis();
            if (this.srcPath == null) {
                throw new BuildException("srcdir attribute must be set", getLocation());
            }
            if (!directories(this.srcPath).findAny().isPresent()) {
                throw new BuildException("srcdir attribute must be non-empty", getLocation());
            }
            if (this.destPath == null) {
                this.destPath = this.srcPath;
            }
            if (this.cache != null && this.cache.exists() && !this.cache.isDirectory()) {
                throw new BuildException("The cache, if specified, must point to a directory");
            }
            if (this.cache != null && !this.cache.exists()) {
                this.cache.mkdirs();
            }
            determineDependencies();
            if (this.dump) {
                dumpDependencies();
            }
            determineOutOfDateClasses();
            int count = deleteAllAffectedFiles();
            long duration = (System.currentTimeMillis() - start) / 1000;
            if (count > 0) {
                summaryLogLevel = 2;
            } else {
                summaryLogLevel = 4;
            }
            log("Deleted " + count + " out of date files in " + duration + " seconds", summaryLogLevel);
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    protected void scanDir(File srcDir, String[] files) {
        for (String f : files) {
            File srcFile = new File(srcDir, f);
            if (f.endsWith(".java")) {
                String filePath = srcFile.getPath();
                String className = ClassFileUtils.convertSlashName(filePath.substring(srcDir.getPath().length() + 1, filePath.length() - ".java".length()));
                ClassFileInfo info = this.classFileInfoMap.get(className);
                if (info == null) {
                    this.outOfDateClasses.put(className, className);
                } else if (srcFile.lastModified() > info.absoluteFile.lastModified()) {
                    this.outOfDateClasses.put(className, className);
                }
            }
        }
    }

    private List<ClassFileInfo> getClassFiles() {
        List<ClassFileInfo> classFileList = new ArrayList<>();
        directories(this.destPath).forEach(dir -> {
            addClassFiles(classFileList, classFileList, classFileList);
        });
        return classFileList;
    }

    private File findSourceFile(String classname, File sourceFileKnownToExist) {
        String sourceFilename;
        int innerIndex = classname.indexOf(36);
        if (innerIndex != -1) {
            sourceFilename = classname.substring(0, innerIndex) + ".java";
        } else {
            sourceFilename = classname + ".java";
        }
        String str = sourceFilename;
        return (File) directories(this.srcPath).map(d -> {
            return new File(d, str);
        }).filter(Predicate.isEqual(sourceFileKnownToExist).or((v0) -> {
            return v0.exists();
        })).findFirst().orElse(null);
    }

    private void addClassFiles(List<ClassFileInfo> classFileList, File dir, File root) {
        File[] children = dir.listFiles();
        if (children == null) {
            return;
        }
        int rootLength = root.getPath().length();
        File sourceFileKnownToExist = null;
        for (File file : children) {
            if (file.getName().endsWith(".class")) {
                ClassFileInfo info = new ClassFileInfo();
                info.absoluteFile = file;
                String relativeName = file.getPath().substring(rootLength + 1, file.getPath().length() - ".class".length());
                info.className = ClassFileUtils.convertSlashName(relativeName);
                File findSourceFile = findSourceFile(relativeName, sourceFileKnownToExist);
                sourceFileKnownToExist = findSourceFile;
                info.sourceFile = findSourceFile;
                classFileList.add(info);
            } else {
                addClassFiles(classFileList, file, root);
            }
        }
    }

    public void setSrcdir(Path srcPath) {
        this.srcPath = srcPath;
    }

    public void setDestDir(Path destPath) {
        this.destPath = destPath;
    }

    public void setCache(File cache) {
        this.cache = cache;
    }

    public void setClosure(boolean closure) {
        this.closure = closure;
    }

    public void setDump(boolean dump) {
        this.dump = dump;
    }

    private Stream<File> directories(ResourceCollection rc) {
        return rc.stream().map(r -> {
            return (FileProvider) r.as(FileProvider.class);
        }).filter((v0) -> {
            return Objects.nonNull(v0);
        }).map((v0) -> {
            return v0.getFile();
        }).filter((v0) -> {
            return v0.isDirectory();
        });
    }
}
