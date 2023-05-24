package org.apache.tools.ant.taskdefs.modules;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.spi.ToolProvider;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ModuleVersion;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.MergingMapper;
import org.apache.tools.ant.util.ResourceUtils;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Jmod.class */
public class Jmod extends Task {
    private File jmodFile;
    private Path classpath;
    private Path modulePath;
    private Path commandPath;
    private Path configPath;
    private Path headerPath;
    private Path legalPath;
    private Path nativeLibPath;
    private Path manPath;
    private String version;
    private ModuleVersion moduleVersion;
    private String mainClass;
    private String platform;
    private String hashModulesPattern;
    private boolean resolveByDefault = true;
    private final List<ResolutionWarningSpec> moduleWarnings = new ArrayList();

    public File getDestFile() {
        return this.jmodFile;
    }

    public void setDestFile(File file) {
        this.jmodFile = file;
    }

    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        return this.classpath.createPath();
    }

    public Path getClasspath() {
        return this.classpath;
    }

    public void setClasspath(Path path) {
        if (this.classpath == null) {
            this.classpath = path;
        } else {
            this.classpath.append(path);
        }
    }

    public void setClasspathRef(Reference ref) {
        createClasspath().setRefid(ref);
    }

    public Path createModulePath() {
        if (this.modulePath == null) {
            this.modulePath = new Path(getProject());
        }
        return this.modulePath.createPath();
    }

    public Path getModulePath() {
        return this.modulePath;
    }

    public void setModulePath(Path path) {
        if (this.modulePath == null) {
            this.modulePath = path;
        } else {
            this.modulePath.append(path);
        }
    }

    public void setModulePathRef(Reference ref) {
        createModulePath().setRefid(ref);
    }

    public Path createCommandPath() {
        if (this.commandPath == null) {
            this.commandPath = new Path(getProject());
        }
        return this.commandPath.createPath();
    }

    public Path getCommandPath() {
        return this.commandPath;
    }

    public void setCommandPath(Path path) {
        if (this.commandPath == null) {
            this.commandPath = path;
        } else {
            this.commandPath.append(path);
        }
    }

    public void setCommandPathRef(Reference ref) {
        createCommandPath().setRefid(ref);
    }

    public Path createConfigPath() {
        if (this.configPath == null) {
            this.configPath = new Path(getProject());
        }
        return this.configPath.createPath();
    }

    public Path getConfigPath() {
        return this.configPath;
    }

    public void setConfigPath(Path path) {
        if (this.configPath == null) {
            this.configPath = path;
        } else {
            this.configPath.append(path);
        }
    }

    public void setConfigPathRef(Reference ref) {
        createConfigPath().setRefid(ref);
    }

    public Path createHeaderPath() {
        if (this.headerPath == null) {
            this.headerPath = new Path(getProject());
        }
        return this.headerPath.createPath();
    }

    public Path getHeaderPath() {
        return this.headerPath;
    }

    public void setHeaderPath(Path path) {
        if (this.headerPath == null) {
            this.headerPath = path;
        } else {
            this.headerPath.append(path);
        }
    }

    public void setHeaderPathRef(Reference ref) {
        createHeaderPath().setRefid(ref);
    }

    public Path createLegalPath() {
        if (this.legalPath == null) {
            this.legalPath = new Path(getProject());
        }
        return this.legalPath.createPath();
    }

    public Path getLegalPath() {
        return this.legalPath;
    }

    public void setLegalPath(Path path) {
        if (this.legalPath == null) {
            this.legalPath = path;
        } else {
            this.legalPath.append(path);
        }
    }

    public void setLegalPathRef(Reference ref) {
        createLegalPath().setRefid(ref);
    }

    public Path createNativeLibPath() {
        if (this.nativeLibPath == null) {
            this.nativeLibPath = new Path(getProject());
        }
        return this.nativeLibPath.createPath();
    }

    public Path getNativeLibPath() {
        return this.nativeLibPath;
    }

    public void setNativeLibPath(Path path) {
        if (this.nativeLibPath == null) {
            this.nativeLibPath = path;
        } else {
            this.nativeLibPath.append(path);
        }
    }

    public void setNativeLibPathRef(Reference ref) {
        createNativeLibPath().setRefid(ref);
    }

    public Path createManPath() {
        if (this.manPath == null) {
            this.manPath = new Path(getProject());
        }
        return this.manPath.createPath();
    }

    public Path getManPath() {
        return this.manPath;
    }

    public void setManPath(Path path) {
        if (this.manPath == null) {
            this.manPath = path;
        } else {
            this.manPath.append(path);
        }
    }

    public void setManPathRef(Reference ref) {
        createManPath().setRefid(ref);
    }

    public ModuleVersion createVersion() {
        if (this.moduleVersion != null) {
            throw new BuildException("No more than one <moduleVersion> element is allowed.", getLocation());
        }
        this.moduleVersion = new ModuleVersion();
        return this.moduleVersion;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMainClass() {
        return this.mainClass;
    }

    public void setMainClass(String className) {
        this.mainClass = className;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getHashModulesPattern() {
        return this.hashModulesPattern;
    }

    public void setHashModulesPattern(String pattern) {
        this.hashModulesPattern = pattern;
    }

    public boolean getResolveByDefault() {
        return this.resolveByDefault;
    }

    public void setResolveByDefault(boolean resolve) {
        this.resolveByDefault = resolve;
    }

    public ResolutionWarningSpec createModuleWarning() {
        ResolutionWarningSpec warningSpec = new ResolutionWarningSpec();
        this.moduleWarnings.add(warningSpec);
        return warningSpec;
    }

    public void setModuleWarnings(String warningList) {
        String[] split;
        for (String warning : warningList.split(",")) {
            this.moduleWarnings.add(new ResolutionWarningSpec(warning));
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Jmod$ResolutionWarningReason.class */
    public static class ResolutionWarningReason extends EnumeratedAttribute {
        public static final String DEPRECATED = "deprecated";
        public static final String LEAVING = "leaving";
        public static final String INCUBATING = "incubating";
        private static final Map<String, String> VALUES_TO_OPTIONS;

        static {
            Map<String, String> map = new LinkedHashMap<>();
            map.put(DEPRECATED, DEPRECATED);
            map.put(LEAVING, "deprecated-for-removal");
            map.put(INCUBATING, INCUBATING);
            VALUES_TO_OPTIONS = Collections.unmodifiableMap(map);
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return (String[]) VALUES_TO_OPTIONS.keySet().toArray(new String[0]);
        }

        String toCommandLineOption() {
            return VALUES_TO_OPTIONS.get(getValue());
        }

        public static ResolutionWarningReason valueOf(String s) {
            return (ResolutionWarningReason) getInstance(ResolutionWarningReason.class, s);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Jmod$ResolutionWarningSpec.class */
    public class ResolutionWarningSpec {
        private ResolutionWarningReason reason;

        public ResolutionWarningSpec() {
        }

        public ResolutionWarningSpec(String reason) {
            setReason(ResolutionWarningReason.valueOf(reason));
        }

        public ResolutionWarningReason getReason() {
            return this.reason;
        }

        public void setReason(ResolutionWarningReason reason) {
            this.reason = reason;
        }

        public void validate() {
            if (this.reason == null) {
                throw new BuildException("reason attribute is required", Jmod.this.getLocation());
            }
        }
    }

    private static boolean isRegularFile(Resource resource) {
        return resource.isExists() && !resource.isDirectory();
    }

    private void checkDirPaths() {
        if (this.modulePath != null && this.modulePath.stream().anyMatch(Jmod::isRegularFile)) {
            throw new BuildException("ModulePath must contain only directories.", getLocation());
        }
        if (this.commandPath != null && this.commandPath.stream().anyMatch(Jmod::isRegularFile)) {
            throw new BuildException("CommandPath must contain only directories.", getLocation());
        }
        if (this.configPath != null && this.configPath.stream().anyMatch(Jmod::isRegularFile)) {
            throw new BuildException("ConfigPath must contain only directories.", getLocation());
        }
        if (this.headerPath != null && this.headerPath.stream().anyMatch(Jmod::isRegularFile)) {
            throw new BuildException("HeaderPath must contain only directories.", getLocation());
        }
        if (this.legalPath != null && this.legalPath.stream().anyMatch(Jmod::isRegularFile)) {
            throw new BuildException("LegalPath must contain only directories.", getLocation());
        }
        if (this.nativeLibPath != null && this.nativeLibPath.stream().anyMatch(Jmod::isRegularFile)) {
            throw new BuildException("NativeLibPath must contain only directories.", getLocation());
        }
        if (this.manPath != null && this.manPath.stream().anyMatch(Jmod::isRegularFile)) {
            throw new BuildException("ManPath must contain only directories.", getLocation());
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        String[] list;
        if (this.jmodFile == null) {
            throw new BuildException("Destination file is required.", getLocation());
        }
        if (this.classpath == null) {
            throw new BuildException("Classpath is required.", getLocation());
        }
        if (this.classpath.stream().noneMatch((v0) -> {
            return v0.isExists();
        })) {
            throw new BuildException("Classpath must contain at least one entry which exists.", getLocation());
        }
        if (this.version != null && this.moduleVersion != null) {
            throw new BuildException("version attribute and nested <version> element cannot both be present.", getLocation());
        }
        if (this.hashModulesPattern != null && !this.hashModulesPattern.isEmpty() && this.modulePath == null) {
            throw new BuildException("hashModulesPattern requires a module path, since it will generate hashes of the other modules which depend on the module being created.", getLocation());
        }
        checkDirPaths();
        Path[] dependentPaths = {this.classpath, this.modulePath, this.commandPath, this.configPath, this.headerPath, this.legalPath, this.nativeLibPath, this.manPath};
        Union allResources = new Union(getProject());
        for (Path path : dependentPaths) {
            if (path != null) {
                for (String entry : path.list()) {
                    File entryFile = new File(entry);
                    if (entryFile.isDirectory()) {
                        log("Will compare timestamp of all files in \"" + entryFile + "\" with timestamp of " + this.jmodFile, 3);
                        FileSet fileSet = new FileSet();
                        fileSet.setDir(entryFile);
                        allResources.add(fileSet);
                    } else {
                        log("Will compare timestamp of \"" + entryFile + "\" with timestamp of " + this.jmodFile, 3);
                        allResources.add(new FileResource(entryFile));
                    }
                }
            }
        }
        ResourceCollection outOfDate = ResourceUtils.selectOutOfDateSources(this, allResources, new MergingMapper(this.jmodFile.toString()), getProject(), FileUtils.getFileUtils().getFileTimestampGranularity());
        if (outOfDate.isEmpty()) {
            log("Skipping jmod creation, since \"" + this.jmodFile + "\" is already newer than all files in paths.", 3);
            return;
        }
        Collection<String> args = buildJmodArgs();
        try {
            log("Deleting " + this.jmodFile + " if it exists.", 3);
            Files.deleteIfExists(this.jmodFile.toPath());
            ToolProvider jmod = (ToolProvider) ToolProvider.findFirst("jmod").orElseThrow(() -> {
                return new BuildException("jmod tool not found in JDK.", getLocation());
            });
            log("Executing: jmod " + String.join(Instruction.argsep, args), 3);
            ByteArrayOutputStream stdout = new ByteArrayOutputStream();
            ByteArrayOutputStream stderr = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(stdout);
            try {
                PrintStream err = new PrintStream(stderr);
                int exitCode = jmod.run(out, err, (String[]) args.toArray(new String[0]));
                err.close();
                out.close();
                if (exitCode != 0) {
                    StringBuilder message = new StringBuilder();
                    message.append("jmod failed (exit code ").append(exitCode).append(")");
                    if (stdout.size() > 0) {
                        message.append(", output is: ").append(stdout);
                    }
                    if (stderr.size() > 0) {
                        message.append(", error output is: ").append(stderr);
                    }
                    throw new BuildException(message.toString(), getLocation());
                }
                log("Created " + this.jmodFile.getAbsolutePath(), 2);
            } catch (Throwable th) {
                try {
                    out.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException e) {
            throw new BuildException("Could not remove old file \"" + this.jmodFile + "\": " + e, e, getLocation());
        }
    }

    private Collection<String> buildJmodArgs() {
        Collection<String> args = new ArrayList<>();
        args.add("create");
        args.add("--class-path");
        args.add(this.classpath.toString());
        if (this.modulePath != null && !this.modulePath.isEmpty()) {
            args.add("--module-path");
            args.add(this.modulePath.toString());
        }
        if (this.commandPath != null && !this.commandPath.isEmpty()) {
            args.add("--cmds");
            args.add(this.commandPath.toString());
        }
        if (this.configPath != null && !this.configPath.isEmpty()) {
            args.add("--config");
            args.add(this.configPath.toString());
        }
        if (this.headerPath != null && !this.headerPath.isEmpty()) {
            args.add("--header-files");
            args.add(this.headerPath.toString());
        }
        if (this.legalPath != null && !this.legalPath.isEmpty()) {
            args.add("--legal-notices");
            args.add(this.legalPath.toString());
        }
        if (this.nativeLibPath != null && !this.nativeLibPath.isEmpty()) {
            args.add("--libs");
            args.add(this.nativeLibPath.toString());
        }
        if (this.manPath != null && !this.manPath.isEmpty()) {
            args.add("--man-pages");
            args.add(this.manPath.toString());
        }
        String versionStr = this.moduleVersion != null ? this.moduleVersion.toModuleVersionString() : this.version;
        if (versionStr != null && !versionStr.isEmpty()) {
            args.add("--module-version");
            args.add(versionStr);
        }
        if (this.mainClass != null && !this.mainClass.isEmpty()) {
            args.add("--main-class");
            args.add(this.mainClass);
        }
        if (this.platform != null && !this.platform.isEmpty()) {
            args.add("--target-platform");
            args.add(this.platform);
        }
        if (this.hashModulesPattern != null && !this.hashModulesPattern.isEmpty()) {
            args.add("--hash-modules");
            args.add(this.hashModulesPattern);
        }
        if (!this.resolveByDefault) {
            args.add("--do-not-resolve-by-default");
        }
        for (ResolutionWarningSpec moduleWarning : this.moduleWarnings) {
            moduleWarning.validate();
            args.add("--warn-if-resolved");
            args.add(moduleWarning.getReason().toCommandLineOption());
        }
        args.add(this.jmodFile.toString());
        return args;
    }
}
