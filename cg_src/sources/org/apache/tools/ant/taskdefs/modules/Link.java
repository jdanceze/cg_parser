package org.apache.tools.ant.taskdefs.modules;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.spi.ToolProvider;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.resource.spi.work.WorkException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.LogLevel;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.util.CompositeMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.MergingMapper;
import org.apache.tools.ant.util.ResourceUtils;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Link.class */
public class Link extends Task {
    private static final String INVALID_LAUNCHER_STRING = "Launcher command must take the form name=module or name=module/mainclass";
    private Path modulePath;
    private boolean bindServices;
    private boolean ignoreSigning;
    private LogLevel verboseLevel;
    private File outputDir;
    private Endianness endianness;
    private CompressionLevel compressionLevel;
    private Compression compression;
    private boolean checkDuplicateLegal;
    private VMType vmType;
    private final List<ModuleSpec> modules = new ArrayList();
    private final List<ModuleSpec> observableModules = new ArrayList();
    private final List<Launcher> launchers = new ArrayList();
    private final List<LocaleSpec> locales = new ArrayList();
    private final List<PatternListEntry> ordering = new ArrayList();
    private final List<PatternListEntry> excludedFiles = new ArrayList();
    private final List<PatternListEntry> excludedResources = new ArrayList();
    private boolean includeHeaders = true;
    private boolean includeManPages = true;
    private boolean includeNativeCommands = true;
    private boolean debug = true;
    private final List<ReleaseInfo> releaseInfo = new ArrayList();

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

    public ModuleSpec createModule() {
        ModuleSpec module = new ModuleSpec();
        this.modules.add(module);
        return module;
    }

    public void setModules(String moduleList) {
        String[] split;
        for (String moduleName : moduleList.split(",")) {
            this.modules.add(new ModuleSpec(moduleName));
        }
    }

    public ModuleSpec createObservableModule() {
        ModuleSpec module = new ModuleSpec();
        this.observableModules.add(module);
        return module;
    }

    public void setObservableModules(String moduleList) {
        String[] split;
        for (String moduleName : moduleList.split(",")) {
            this.observableModules.add(new ModuleSpec(moduleName));
        }
    }

    public Launcher createLauncher() {
        Launcher command = new Launcher();
        this.launchers.add(command);
        return command;
    }

    public void setLaunchers(String launcherList) {
        String[] split;
        for (String launcherSpec : launcherList.split(",")) {
            this.launchers.add(new Launcher(launcherSpec));
        }
    }

    public LocaleSpec createLocale() {
        LocaleSpec locale = new LocaleSpec();
        this.locales.add(locale);
        return locale;
    }

    public void setLocales(String localeList) {
        String[] split;
        for (String localeName : localeList.split(",")) {
            this.locales.add(new LocaleSpec(localeName));
        }
    }

    public PatternListEntry createExcludeFiles() {
        PatternListEntry entry = new PatternListEntry();
        this.excludedFiles.add(entry);
        return entry;
    }

    public void setExcludeFiles(String patternList) {
        String[] split;
        for (String pattern : patternList.split(",")) {
            this.excludedFiles.add(new PatternListEntry(pattern));
        }
    }

    public PatternListEntry createExcludeResources() {
        PatternListEntry entry = new PatternListEntry();
        this.excludedResources.add(entry);
        return entry;
    }

    public void setExcludeResources(String patternList) {
        String[] split;
        for (String pattern : patternList.split(",")) {
            this.excludedResources.add(new PatternListEntry(pattern));
        }
    }

    public PatternListEntry createResourceOrder() {
        PatternListEntry order = new PatternListEntry();
        this.ordering.add(order);
        return order;
    }

    public void setResourceOrder(String patternList) {
        String[] split;
        List<PatternListEntry> orderList = new ArrayList<>();
        for (String pattern : patternList.split(",")) {
            orderList.add(new PatternListEntry(pattern));
        }
        this.ordering.addAll(0, orderList);
    }

    public boolean getBindServices() {
        return this.bindServices;
    }

    public void setBindServices(boolean bind) {
        this.bindServices = bind;
    }

    public boolean getIgnoreSigning() {
        return this.ignoreSigning;
    }

    public void setIgnoreSigning(boolean ignore) {
        this.ignoreSigning = ignore;
    }

    public boolean getIncludeHeaders() {
        return this.includeHeaders;
    }

    public void setIncludeHeaders(boolean include) {
        this.includeHeaders = include;
    }

    public boolean getIncludeManPages() {
        return this.includeManPages;
    }

    public void setIncludeManPages(boolean include) {
        this.includeManPages = include;
    }

    public boolean getIncludeNativeCommands() {
        return this.includeNativeCommands;
    }

    public void setIncludeNativeCommands(boolean include) {
        this.includeNativeCommands = include;
    }

    public boolean getDebug() {
        return this.debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public LogLevel getVerboseLevel() {
        return this.verboseLevel;
    }

    public void setVerboseLevel(LogLevel level) {
        this.verboseLevel = level;
    }

    public File getDestDir() {
        return this.outputDir;
    }

    public void setDestDir(File dir) {
        this.outputDir = dir;
    }

    public CompressionLevel getCompress() {
        return this.compressionLevel;
    }

    public void setCompress(CompressionLevel level) {
        this.compressionLevel = level;
    }

    public Compression createCompress() {
        if (this.compression != null) {
            throw new BuildException("Only one nested compression element is permitted.", getLocation());
        }
        this.compression = new Compression();
        return this.compression;
    }

    public Endianness getEndianness() {
        return this.endianness;
    }

    public void setEndianness(Endianness endianness) {
        this.endianness = endianness;
    }

    public boolean getCheckDuplicateLegal() {
        return this.checkDuplicateLegal;
    }

    public void setCheckDuplicateLegal(boolean check) {
        this.checkDuplicateLegal = check;
    }

    public VMType getVmType() {
        return this.vmType;
    }

    public void setVmType(VMType type) {
        this.vmType = type;
    }

    public ReleaseInfo createReleaseInfo() {
        ReleaseInfo info = new ReleaseInfo();
        this.releaseInfo.add(info);
        return info;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Link$ModuleSpec.class */
    public class ModuleSpec {
        private String name;

        public ModuleSpec() {
        }

        public ModuleSpec(String name) {
            setName(name);
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void validate() {
            if (this.name == null) {
                throw new BuildException("name is required for module.", Link.this.getLocation());
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Link$LocaleSpec.class */
    public class LocaleSpec {
        private String name;

        public LocaleSpec() {
        }

        public LocaleSpec(String name) {
            setName(name);
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void validate() {
            if (this.name == null) {
                throw new BuildException("name is required for locale.", Link.this.getLocation());
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Link$PatternListEntry.class */
    public class PatternListEntry {
        private String pattern;
        private File file;

        public PatternListEntry() {
        }

        public PatternListEntry(String pattern) {
            if (pattern.startsWith("@")) {
                setListFile(new File(pattern.substring(1)));
            } else {
                setPattern(pattern);
            }
        }

        public String getPattern() {
            return this.pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public File getListFile() {
            return this.file;
        }

        public void setListFile(File file) {
            this.file = file;
        }

        public void validate() {
            if ((this.pattern == null && this.file == null) || (this.pattern != null && this.file != null)) {
                throw new BuildException("Each entry in a pattern list must specify exactly one of pattern or file.", Link.this.getLocation());
            }
        }

        public String toOptionValue() {
            return this.pattern != null ? this.pattern : "@" + this.file;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Link$Launcher.class */
    public class Launcher {
        private String name;
        private String module;
        private String mainClass;

        public Launcher() {
        }

        public Launcher(String textSpec) {
            Objects.requireNonNull(textSpec, "Text cannot be null");
            int equals = textSpec.lastIndexOf(61);
            if (equals < 1) {
                throw new BuildException(Link.INVALID_LAUNCHER_STRING);
            }
            setName(textSpec.substring(0, equals));
            int slash = textSpec.indexOf(47, equals);
            if (slash < 0) {
                setModule(textSpec.substring(equals + 1));
            } else if (slash > equals + 1 && slash < textSpec.length() - 1) {
                setModule(textSpec.substring(equals + 1, slash));
                setMainClass(textSpec.substring(slash + 1));
            } else {
                throw new BuildException(Link.INVALID_LAUNCHER_STRING);
            }
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getModule() {
            return this.module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public String getMainClass() {
            return this.mainClass;
        }

        public void setMainClass(String className) {
            this.mainClass = className;
        }

        public void validate() {
            if (this.name == null || this.name.isEmpty()) {
                throw new BuildException("Launcher must have a name", Link.this.getLocation());
            }
            if (this.module == null || this.module.isEmpty()) {
                throw new BuildException("Launcher must have specify a module", Link.this.getLocation());
            }
        }

        public String toString() {
            if (this.mainClass != null) {
                return this.name + "=" + this.module + "/" + this.mainClass;
            }
            return this.name + "=" + this.module;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Link$Endianness.class */
    public static class Endianness extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"little", "big"};
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Link$VMType.class */
    public static class VMType extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"client", "server", "minimal", "all"};
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Link$CompressionLevel.class */
    public static class CompressionLevel extends EnumeratedAttribute {
        private static final Map<String, String> KEYWORDS;

        static {
            Map<String, String> map = new LinkedHashMap<>();
            map.put(WorkException.UNDEFINED, WorkException.UNDEFINED);
            map.put(WorkException.START_TIMED_OUT, WorkException.START_TIMED_OUT);
            map.put(WorkException.TX_CONCURRENT_WORK_DISALLOWED, WorkException.TX_CONCURRENT_WORK_DISALLOWED);
            map.put("none", WorkException.UNDEFINED);
            map.put("strings", WorkException.START_TIMED_OUT);
            map.put("zip", WorkException.TX_CONCURRENT_WORK_DISALLOWED);
            KEYWORDS = Collections.unmodifiableMap(map);
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return (String[]) KEYWORDS.keySet().toArray(new String[0]);
        }

        String toCommandLineOption() {
            return KEYWORDS.get(getValue());
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Link$Compression.class */
    public class Compression {
        private CompressionLevel level;
        private final List<PatternListEntry> patterns = new ArrayList();

        public Compression() {
        }

        public CompressionLevel getLevel() {
            return this.level;
        }

        public void setLevel(CompressionLevel level) {
            this.level = level;
        }

        public PatternListEntry createFiles() {
            PatternListEntry pattern = new PatternListEntry();
            this.patterns.add(pattern);
            return pattern;
        }

        public void setFiles(String patternList) {
            String[] split;
            this.patterns.clear();
            for (String pattern : patternList.split(",")) {
                this.patterns.add(new PatternListEntry(pattern));
            }
        }

        public void validate() {
            if (this.level == null) {
                throw new BuildException("Compression level must be specified.", Link.this.getLocation());
            }
            this.patterns.forEach((v0) -> {
                v0.validate();
            });
        }

        public String toCommandLineOption() {
            StringBuilder option = new StringBuilder(this.level.toCommandLineOption());
            if (!this.patterns.isEmpty()) {
                String separator = ":filter=";
                for (PatternListEntry entry : this.patterns) {
                    option.append(separator).append(entry.toOptionValue());
                    separator = ",";
                }
            }
            return option.toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Link$ReleaseInfoKey.class */
    public class ReleaseInfoKey {
        private String key;

        public ReleaseInfoKey() {
        }

        public ReleaseInfoKey(String key) {
            setKey(key);
        }

        public String getKey() {
            return this.key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void validate() {
            if (this.key == null) {
                throw new BuildException("Release info key must define a 'key' attribute.", Link.this.getLocation());
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Link$ReleaseInfoEntry.class */
    public class ReleaseInfoEntry {
        private String key;
        private String value;
        private File file;
        private String charset = StandardCharsets.ISO_8859_1.name();

        public ReleaseInfoEntry() {
        }

        public ReleaseInfoEntry(String key, String value) {
            setKey(key);
            setValue(value);
        }

        public String getKey() {
            return this.key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public File getFile() {
            return this.file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getCharset() {
            return this.charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public void validate() {
            if (this.file == null && (this.key == null || this.value == null)) {
                throw new BuildException("Release info must define 'key' and 'value' attributes, or a 'file' attribute.", Link.this.getLocation());
            }
            if (this.file != null && (this.key != null || this.value != null)) {
                throw new BuildException("Release info cannot define both a file attribute and key/value attributes.", Link.this.getLocation());
            }
            if (this.charset == null) {
                throw new BuildException("Charset cannot be null.", Link.this.getLocation());
            }
            try {
                Charset.forName(this.charset);
            } catch (IllegalArgumentException e) {
                throw new BuildException(e, Link.this.getLocation());
            }
        }

        public Properties toProperties() {
            Properties props = new Properties();
            if (this.file != null) {
                try {
                    Reader reader = Files.newBufferedReader(this.file.toPath(), Charset.forName(this.charset));
                    props.load(reader);
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    throw new BuildException("Cannot read release info file \"" + this.file + "\": " + e, e, Link.this.getLocation());
                }
            } else {
                props.setProperty(this.key, this.value);
            }
            return props;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/modules/Link$ReleaseInfo.class */
    public class ReleaseInfo {
        private File file;
        private final List<ReleaseInfoEntry> propertiesToAdd = new ArrayList();
        private final List<ReleaseInfoKey> propertiesToDelete = new ArrayList();

        public ReleaseInfo() {
        }

        public File getFile() {
            return this.file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public ReleaseInfoEntry createAdd() {
            ReleaseInfoEntry property = new ReleaseInfoEntry();
            this.propertiesToAdd.add(property);
            return property;
        }

        public ReleaseInfoKey createDelete() {
            ReleaseInfoKey key = new ReleaseInfoKey();
            this.propertiesToDelete.add(key);
            return key;
        }

        public void setDelete(String keyList) {
            String[] split;
            for (String key : keyList.split(",")) {
                this.propertiesToDelete.add(new ReleaseInfoKey(key));
            }
        }

        public void validate() {
            this.propertiesToAdd.forEach((v0) -> {
                v0.validate();
            });
            this.propertiesToDelete.forEach((v0) -> {
                v0.validate();
            });
        }

        public Collection<String> toCommandLineOptions() {
            Collection<String> options = new ArrayList<>();
            if (this.file != null) {
                options.add("--release-info=" + this.file);
            }
            if (!this.propertiesToAdd.isEmpty()) {
                StringBuilder option = new StringBuilder("--release-info=add");
                for (ReleaseInfoEntry entry : this.propertiesToAdd) {
                    Properties props = entry.toProperties();
                    for (String key : props.stringPropertyNames()) {
                        option.append(":").append(key).append("=");
                        option.append(props.getProperty(key));
                    }
                }
                options.add(option.toString());
            }
            if (!this.propertiesToDelete.isEmpty()) {
                StringBuilder option2 = new StringBuilder("--release-info=del:keys=");
                String separator = "";
                for (ReleaseInfoKey key2 : this.propertiesToDelete) {
                    option2.append(separator).append(key2.getKey());
                    separator = ",";
                }
                options.add(option2.toString());
            }
            return options;
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.outputDir == null) {
            throw new BuildException("Destination directory is required.", getLocation());
        }
        if (this.modulePath == null || this.modulePath.isEmpty()) {
            throw new BuildException("Module path is required.", getLocation());
        }
        if (this.modules.isEmpty()) {
            throw new BuildException("At least one module must be specified.", getLocation());
        }
        if (this.outputDir.exists()) {
            CompositeMapper imageMapper = new CompositeMapper();
            try {
                Stream<java.nio.file.Path> imageTree = Files.walk(this.outputDir.toPath(), new FileVisitOption[0]);
                imageTree.forEach(p -> {
                    imageMapper.add(new MergingMapper(p.toString()));
                });
                ResourceCollection outOfDate = ResourceUtils.selectOutOfDateSources(this, this.modulePath, imageMapper, getProject(), FileUtils.getFileUtils().getFileTimestampGranularity());
                if (outOfDate.isEmpty()) {
                    log("Skipping image creation, since \"" + this.outputDir + "\" is already newer than all constituent modules.", 3);
                    if (imageTree != null) {
                        imageTree.close();
                        return;
                    }
                    return;
                } else if (imageTree != null) {
                    imageTree.close();
                }
            } catch (IOException e) {
                throw new BuildException("Could not scan \"" + this.outputDir + "\" for being up-to-date: " + e, e, getLocation());
            }
        }
        this.modules.forEach((v0) -> {
            v0.validate();
        });
        this.observableModules.forEach((v0) -> {
            v0.validate();
        });
        this.launchers.forEach((v0) -> {
            v0.validate();
        });
        this.locales.forEach((v0) -> {
            v0.validate();
        });
        this.ordering.forEach((v0) -> {
            v0.validate();
        });
        this.excludedFiles.forEach((v0) -> {
            v0.validate();
        });
        this.excludedResources.forEach((v0) -> {
            v0.validate();
        });
        Collection<String> args = buildJlinkArgs();
        ToolProvider jlink = (ToolProvider) ToolProvider.findFirst("jlink").orElseThrow(() -> {
            return new BuildException("jlink tool not found in JDK.", getLocation());
        });
        if (this.outputDir.exists()) {
            log("Deleting existing " + this.outputDir, 3);
            deleteTree(this.outputDir.toPath());
        }
        log("Executing: jlink " + String.join(Instruction.argsep, args), 3);
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(stdout);
        try {
            PrintStream err = new PrintStream(stderr);
            int exitCode = jlink.run(out, err, (String[]) args.toArray(new String[0]));
            err.close();
            out.close();
            if (exitCode != 0) {
                StringBuilder message = new StringBuilder();
                message.append("jlink failed (exit code ").append(exitCode).append(")");
                if (stdout.size() > 0) {
                    message.append(", output is: ").append(stdout);
                }
                if (stderr.size() > 0) {
                    message.append(", error output is: ").append(stderr);
                }
                throw new BuildException(message.toString(), getLocation());
            }
            if (this.verboseLevel != null) {
                int level = this.verboseLevel.getLevel();
                if (stdout.size() > 0) {
                    log(stdout.toString(), level);
                }
                if (stderr.size() > 0) {
                    log(stderr.toString(), level);
                }
            }
            log("Created " + this.outputDir.getAbsolutePath(), 2);
        } catch (Throwable th) {
            try {
                out.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private void deleteTree(java.nio.file.Path dir) {
        try {
            Files.walkFileTree(dir, new SimpleFileVisitor<java.nio.file.Path>() { // from class: org.apache.tools.ant.taskdefs.modules.Link.1
                @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
                public FileVisitResult visitFile(java.nio.file.Path file, BasicFileAttributes attr) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
                public FileVisitResult postVisitDirectory(java.nio.file.Path dir2, IOException e) throws IOException {
                    if (e == null) {
                        Files.delete(dir2);
                    }
                    return super.postVisitDirectory((AnonymousClass1) dir2, e);
                }
            });
        } catch (IOException e) {
            throw new BuildException("Could not delete \"" + dir + "\": " + e, e, getLocation());
        }
    }

    private Collection<String> buildJlinkArgs() {
        Collection<String> args = new ArrayList<>();
        args.add("--output");
        args.add(this.outputDir.toString());
        args.add("--module-path");
        args.add(this.modulePath.toString());
        args.add("--add-modules");
        args.add((String) this.modules.stream().map((v0) -> {
            return v0.getName();
        }).collect(Collectors.joining(",")));
        if (!this.observableModules.isEmpty()) {
            args.add("--limit-modules");
            args.add((String) this.observableModules.stream().map((v0) -> {
                return v0.getName();
            }).collect(Collectors.joining(",")));
        }
        if (!this.locales.isEmpty()) {
            args.add("--include-locales=" + ((String) this.locales.stream().map((v0) -> {
                return v0.getName();
            }).collect(Collectors.joining(","))));
        }
        for (Launcher launcher : this.launchers) {
            args.add("--launcher");
            args.add(launcher.toString());
        }
        if (!this.ordering.isEmpty()) {
            args.add("--order-resources=" + ((String) this.ordering.stream().map((v0) -> {
                return v0.toOptionValue();
            }).collect(Collectors.joining(","))));
        }
        if (!this.excludedFiles.isEmpty()) {
            args.add("--exclude-files=" + ((String) this.excludedFiles.stream().map((v0) -> {
                return v0.toOptionValue();
            }).collect(Collectors.joining(","))));
        }
        if (!this.excludedResources.isEmpty()) {
            args.add("--exclude-resources=" + ((String) this.excludedResources.stream().map((v0) -> {
                return v0.toOptionValue();
            }).collect(Collectors.joining(","))));
        }
        if (this.bindServices) {
            args.add("--bind-services");
        }
        if (this.ignoreSigning) {
            args.add("--ignore-signing-information");
        }
        if (!this.includeHeaders) {
            args.add("--no-header-files");
        }
        if (!this.includeManPages) {
            args.add("--no-man-pages");
        }
        if (!this.includeNativeCommands) {
            args.add("--strip-native-commands");
        }
        if (!this.debug) {
            args.add("--strip-debug");
        }
        if (this.verboseLevel != null) {
            args.add("--verbose");
        }
        if (this.endianness != null) {
            args.add("--endian");
            args.add(this.endianness.getValue());
        }
        if (this.compressionLevel != null) {
            if (this.compression != null) {
                throw new BuildException("compressionLevel attribute and <compression> child element cannot both be present.", getLocation());
            }
            args.add("--compress=" + this.compressionLevel.toCommandLineOption());
        }
        if (this.compression != null) {
            this.compression.validate();
            args.add("--compress=" + this.compression.toCommandLineOption());
        }
        if (this.vmType != null) {
            args.add("--vm=" + this.vmType.getValue());
        }
        if (this.checkDuplicateLegal) {
            args.add("--dedup-legal-notices=error-if-not-same-content");
        }
        for (ReleaseInfo info : this.releaseInfo) {
            info.validate();
            args.addAll(info.toCommandLineOptions());
        }
        return args;
    }
}
