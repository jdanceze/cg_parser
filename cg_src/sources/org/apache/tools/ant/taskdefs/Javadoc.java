package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.stream.Collectors;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.optional.sos.SOSCmd;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;
import polyglot.main.Report;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javadoc.class */
public class Javadoc extends Task {
    private String noqualifier;
    private static final String LOAD_FRAME = "function loadFrames() {";
    private static final int LOAD_FRAME_LEN = LOAD_FRAME.length();
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    static final String[] SCOPE_ELEMENTS = {"overview", "packages", Report.types, "constructors", "methods", "fields"};
    private final Commandline cmd = new Commandline();
    private boolean failOnError = false;
    private boolean failOnWarning = false;
    private Path sourcePath = null;
    private File destDir = null;
    private final List<SourceFile> sourceFiles = new Vector();
    private final List<PackageName> packageNames = new Vector();
    private final List<PackageName> excludePackageNames = new Vector(1);
    private final List<PackageName> moduleNames = new ArrayList();
    private boolean author = true;
    private boolean version = true;
    private DocletInfo doclet = null;
    private Path classpath = null;
    private Path bootclasspath = null;
    private Path modulePath = null;
    private Path moduleSourcePath = null;
    private String group = null;
    private String packageList = null;
    private final List<LinkArgument> links = new Vector();
    private final List<GroupArgument> groups = new Vector();
    private final List<Object> tags = new Vector();
    private boolean useDefaultExcludes = true;
    private Html doctitle = null;
    private Html header = null;
    private Html footer = null;
    private Html bottom = null;
    private boolean useExternalFile = false;
    private String source = null;
    private boolean linksource = false;
    private boolean breakiterator = false;
    private boolean includeNoSourcePackages = false;
    private String executable = null;
    private boolean docFilesSubDirs = false;
    private String excludeDocFilesSubDir = null;
    private String docEncoding = null;
    private boolean postProcessGeneratedJavadocs = true;
    private final ResourceCollectionContainer nestedSourceFiles = new ResourceCollectionContainer();
    private final List<DirSet> packageSets = new Vector();

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javadoc$DocletParam.class */
    public class DocletParam {
        private String name;
        private String value;

        public DocletParam() {
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javadoc$ExtensionInfo.class */
    public static class ExtensionInfo extends ProjectComponent {
        private String name;
        private Path path;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setPath(Path path) {
            if (this.path == null) {
                this.path = path;
            } else {
                this.path.append(path);
            }
        }

        public Path getPath() {
            return this.path;
        }

        public Path createPath() {
            if (this.path == null) {
                this.path = new Path(getProject());
            }
            return this.path.createPath();
        }

        public void setPathRef(Reference r) {
            createPath().setRefid(r);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javadoc$DocletInfo.class */
    public class DocletInfo extends ExtensionInfo {
        private final List<DocletParam> params = new Vector();

        public DocletInfo() {
        }

        public DocletParam createParam() {
            DocletParam param = new DocletParam();
            this.params.add(param);
            return param;
        }

        public Enumeration<DocletParam> getParams() {
            return Collections.enumeration(this.params);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javadoc$PackageName.class */
    public static class PackageName {
        private String name;

        public void setName(String name) {
            this.name = name.trim();
        }

        public String getName() {
            return this.name;
        }

        public String toString() {
            return getName();
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javadoc$SourceFile.class */
    public static class SourceFile {
        private File file;

        public SourceFile() {
        }

        public SourceFile(File file) {
            this.file = file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public File getFile() {
            return this.file;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javadoc$Html.class */
    public static class Html {
        private final StringBuffer text = new StringBuffer();

        public void addText(String t) {
            this.text.append(t);
        }

        public String getText() {
            return this.text.substring(0);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javadoc$AccessType.class */
    public static class AccessType extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{Jimple.PROTECTED, Jimple.PUBLIC, "package", Jimple.PRIVATE};
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javadoc$ResourceCollectionContainer.class */
    public class ResourceCollectionContainer implements Iterable<ResourceCollection> {
        private final List<ResourceCollection> rcs = new ArrayList();

        public ResourceCollectionContainer() {
        }

        public void add(ResourceCollection rc) {
            this.rcs.add(rc);
        }

        @Override // java.lang.Iterable
        public Iterator<ResourceCollection> iterator() {
            return this.rcs.iterator();
        }
    }

    private void addArgIf(boolean b, String arg) {
        if (b) {
            this.cmd.createArgument().setValue(arg);
        }
    }

    private void addArgIfNotEmpty(String key, String value) {
        if (value == null || value.isEmpty()) {
            log("Warning: Leaving out empty argument '" + key + "'", 1);
            return;
        }
        this.cmd.createArgument().setValue(key);
        this.cmd.createArgument().setValue(value);
    }

    public void setUseExternalFile(boolean b) {
        this.useExternalFile = b;
    }

    public void setDefaultexcludes(boolean useDefaultExcludes) {
        this.useDefaultExcludes = useDefaultExcludes;
    }

    public void setMaxmemory(String max) {
        this.cmd.createArgument().setValue("-J-Xmx" + max);
    }

    public void setAdditionalparam(String add) {
        this.cmd.createArgument().setLine(add);
    }

    public Commandline.Argument createArg() {
        return this.cmd.createArgument();
    }

    public void setSourcepath(Path src) {
        if (this.sourcePath == null) {
            this.sourcePath = src;
        } else {
            this.sourcePath.append(src);
        }
    }

    public Path createSourcepath() {
        if (this.sourcePath == null) {
            this.sourcePath = new Path(getProject());
        }
        return this.sourcePath.createPath();
    }

    public void setSourcepathRef(Reference r) {
        createSourcepath().setRefid(r);
    }

    public void setModulePath(Path mp) {
        if (this.modulePath == null) {
            this.modulePath = mp;
        } else {
            this.modulePath.append(mp);
        }
    }

    public Path createModulePath() {
        if (this.modulePath == null) {
            this.modulePath = new Path(getProject());
        }
        return this.modulePath.createPath();
    }

    public void setModulePathref(Reference r) {
        createModulePath().setRefid(r);
    }

    public void setModuleSourcePath(Path mp) {
        if (this.moduleSourcePath == null) {
            this.moduleSourcePath = mp;
        } else {
            this.moduleSourcePath.append(mp);
        }
    }

    public Path createModuleSourcePath() {
        if (this.moduleSourcePath == null) {
            this.moduleSourcePath = new Path(getProject());
        }
        return this.moduleSourcePath.createPath();
    }

    public void setModuleSourcePathref(Reference r) {
        createModuleSourcePath().setRefid(r);
    }

    public void setDestdir(File dir) {
        this.destDir = dir;
        this.cmd.createArgument().setValue("-d");
        this.cmd.createArgument().setFile(this.destDir);
    }

    public void setSourcefiles(String src) {
        StringTokenizer tok = new StringTokenizer(src, ",");
        while (tok.hasMoreTokens()) {
            String f = tok.nextToken();
            SourceFile sf = new SourceFile();
            sf.setFile(getProject().resolveFile(f.trim()));
            addSource(sf);
        }
    }

    public void addSource(SourceFile sf) {
        this.sourceFiles.add(sf);
    }

    public void setPackagenames(String packages) {
        StringTokenizer tok = new StringTokenizer(packages, ",");
        while (tok.hasMoreTokens()) {
            String p = tok.nextToken();
            PackageName pn = new PackageName();
            pn.setName(p);
            addPackage(pn);
        }
    }

    public void setModulenames(String modules) {
        String[] split;
        for (String m : modules.split(",")) {
            PackageName mn = new PackageName();
            mn.setName(m);
            addModule(mn);
        }
    }

    public void addPackage(PackageName pn) {
        this.packageNames.add(pn);
    }

    public void addModule(PackageName mn) {
        this.moduleNames.add(mn);
    }

    public void setExcludePackageNames(String packages) {
        StringTokenizer tok = new StringTokenizer(packages, ",");
        while (tok.hasMoreTokens()) {
            String p = tok.nextToken();
            PackageName pn = new PackageName();
            pn.setName(p);
            addExcludePackage(pn);
        }
    }

    public void addExcludePackage(PackageName pn) {
        this.excludePackageNames.add(pn);
    }

    public void setOverview(File f) {
        this.cmd.createArgument().setValue("-overview");
        this.cmd.createArgument().setFile(f);
    }

    public void setPublic(boolean b) {
        addArgIf(b, "-public");
    }

    public void setProtected(boolean b) {
        addArgIf(b, "-protected");
    }

    public void setPackage(boolean b) {
        addArgIf(b, "-package");
    }

    public void setPrivate(boolean b) {
        addArgIf(b, "-private");
    }

    public void setAccess(AccessType at) {
        this.cmd.createArgument().setValue(HelpFormatter.DEFAULT_OPT_PREFIX + at.getValue());
    }

    public void setDoclet(String docletName) {
        if (this.doclet == null) {
            this.doclet = new DocletInfo();
            this.doclet.setProject(getProject());
        }
        this.doclet.setName(docletName);
    }

    public void setDocletPath(Path docletPath) {
        if (this.doclet == null) {
            this.doclet = new DocletInfo();
            this.doclet.setProject(getProject());
        }
        this.doclet.setPath(docletPath);
    }

    public void setDocletPathRef(Reference r) {
        if (this.doclet == null) {
            this.doclet = new DocletInfo();
            this.doclet.setProject(getProject());
        }
        this.doclet.createPath().setRefid(r);
    }

    public DocletInfo createDoclet() {
        if (this.doclet == null) {
            this.doclet = new DocletInfo();
        }
        return this.doclet;
    }

    public void addTaglet(ExtensionInfo tagletInfo) {
        this.tags.add(tagletInfo);
    }

    public void setOld(boolean b) {
        log("Javadoc 1.4 doesn't support the -1.1 switch anymore", 1);
    }

    public void setClasspath(Path path) {
        if (this.classpath == null) {
            this.classpath = path;
        } else {
            this.classpath.append(path);
        }
    }

    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        return this.classpath.createPath();
    }

    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    public void setBootclasspath(Path path) {
        if (this.bootclasspath == null) {
            this.bootclasspath = path;
        } else {
            this.bootclasspath.append(path);
        }
    }

    public Path createBootclasspath() {
        if (this.bootclasspath == null) {
            this.bootclasspath = new Path(getProject());
        }
        return this.bootclasspath.createPath();
    }

    public void setBootClasspathRef(Reference r) {
        createBootclasspath().setRefid(r);
    }

    @Deprecated
    public void setExtdirs(String path) {
        this.cmd.createArgument().setValue("-extdirs");
        this.cmd.createArgument().setValue(path);
    }

    public void setExtdirs(Path path) {
        this.cmd.createArgument().setValue("-extdirs");
        this.cmd.createArgument().setPath(path);
    }

    public void setVerbose(boolean b) {
        addArgIf(b, SOSCmd.FLAG_VERBOSE);
    }

    public void setLocale(String locale) {
        this.cmd.createArgument(true).setValue(locale);
        this.cmd.createArgument(true).setValue("-locale");
    }

    public void setEncoding(String enc) {
        this.cmd.createArgument().setValue("-encoding");
        this.cmd.createArgument().setValue(enc);
    }

    public void setVersion(boolean b) {
        this.version = b;
    }

    public void setUse(boolean b) {
        addArgIf(b, "-use");
    }

    public void setAuthor(boolean b) {
        this.author = b;
    }

    public void setSplitindex(boolean b) {
        addArgIf(b, "-splitindex");
    }

    public void setWindowtitle(String title) {
        addArgIfNotEmpty("-windowtitle", title);
    }

    public void setDoctitle(String doctitle) {
        Html h = new Html();
        h.addText(doctitle);
        addDoctitle(h);
    }

    public void addDoctitle(Html text) {
        this.doctitle = text;
    }

    public void setHeader(String header) {
        Html h = new Html();
        h.addText(header);
        addHeader(h);
    }

    public void addHeader(Html text) {
        this.header = text;
    }

    public void setFooter(String footer) {
        Html h = new Html();
        h.addText(footer);
        addFooter(h);
    }

    public void addFooter(Html text) {
        this.footer = text;
    }

    public void setBottom(String bottom) {
        Html h = new Html();
        h.addText(bottom);
        addBottom(h);
    }

    public void addBottom(Html text) {
        this.bottom = text;
    }

    public void setLinkoffline(String src) {
        LinkArgument le = createLink();
        le.setOffline(true);
        if (src.trim().isEmpty()) {
            throw new BuildException("The linkoffline attribute must include a URL and a package-list file location separated by a space");
        }
        StringTokenizer tok = new StringTokenizer(src, Instruction.argsep, false);
        le.setHref(tok.nextToken());
        if (!tok.hasMoreTokens()) {
            throw new BuildException("The linkoffline attribute must include a URL and a package-list file location separated by a space");
        }
        le.setPackagelistLoc(getProject().resolveFile(tok.nextToken()));
    }

    public void setGroup(String src) {
        this.group = src;
    }

    public void setLink(String src) {
        createLink().setHref(src);
    }

    public void setNodeprecated(boolean b) {
        addArgIf(b, "-nodeprecated");
    }

    public void setNodeprecatedlist(boolean b) {
        addArgIf(b, "-nodeprecatedlist");
    }

    public void setNotree(boolean b) {
        addArgIf(b, "-notree");
    }

    public void setNoindex(boolean b) {
        addArgIf(b, "-noindex");
    }

    public void setNohelp(boolean b) {
        addArgIf(b, "-nohelp");
    }

    public void setNonavbar(boolean b) {
        addArgIf(b, "-nonavbar");
    }

    public void setSerialwarn(boolean b) {
        addArgIf(b, "-serialwarn");
    }

    public void setStylesheetfile(File f) {
        this.cmd.createArgument().setValue("-stylesheetfile");
        this.cmd.createArgument().setFile(f);
    }

    public void setHelpfile(File f) {
        this.cmd.createArgument().setValue("-helpfile");
        this.cmd.createArgument().setFile(f);
    }

    public void setDocencoding(String enc) {
        this.cmd.createArgument().setValue("-docencoding");
        this.cmd.createArgument().setValue(enc);
        this.docEncoding = enc;
    }

    public void setPackageList(String src) {
        this.packageList = src;
    }

    public LinkArgument createLink() {
        LinkArgument la = new LinkArgument();
        this.links.add(la);
        return la;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javadoc$LinkArgument.class */
    public class LinkArgument {
        private String href;
        private File packagelistLoc;
        private URL packagelistURL;
        private boolean offline = false;
        private boolean resolveLink = false;

        public LinkArgument() {
        }

        public void setHref(String hr) {
            this.href = hr;
        }

        public String getHref() {
            return this.href;
        }

        public void setPackagelistLoc(File src) {
            this.packagelistLoc = src;
        }

        public File getPackagelistLoc() {
            return this.packagelistLoc;
        }

        public void setPackagelistURL(URL src) {
            this.packagelistURL = src;
        }

        public URL getPackagelistURL() {
            return this.packagelistURL;
        }

        public void setOffline(boolean offline) {
            this.offline = offline;
        }

        public boolean isLinkOffline() {
            return this.offline;
        }

        public void setResolveLink(boolean resolve) {
            this.resolveLink = resolve;
        }

        public boolean shouldResolveLink() {
            return this.resolveLink;
        }
    }

    public TagArgument createTag() {
        TagArgument ta = new TagArgument();
        this.tags.add(ta);
        return ta;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javadoc$TagArgument.class */
    public class TagArgument extends FileSet {
        private String name = null;
        private boolean enabled = true;
        private String scope = "a";

        public TagArgument() {
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setScope(String verboseScope) throws BuildException {
            String verboseScope2 = verboseScope.toLowerCase(Locale.ENGLISH);
            boolean[] elements = new boolean[Javadoc.SCOPE_ELEMENTS.length];
            boolean gotAll = false;
            boolean gotNotAll = false;
            StringTokenizer tok = new StringTokenizer(verboseScope2, ",");
            while (tok.hasMoreTokens()) {
                String next = tok.nextToken().trim();
                if ("all".equals(next)) {
                    if (gotAll) {
                        getProject().log("Repeated tag scope element: all", 3);
                    }
                    gotAll = true;
                } else {
                    int i = 0;
                    while (i < Javadoc.SCOPE_ELEMENTS.length && !Javadoc.SCOPE_ELEMENTS[i].equals(next)) {
                        i++;
                    }
                    if (i == Javadoc.SCOPE_ELEMENTS.length) {
                        throw new BuildException("Unrecognised scope element: %s", next);
                    }
                    if (elements[i]) {
                        getProject().log("Repeated tag scope element: " + next, 3);
                    }
                    elements[i] = true;
                    gotNotAll = true;
                }
            }
            if (gotNotAll && gotAll) {
                throw new BuildException("Mixture of \"all\" and other scope elements in tag parameter.");
            }
            if (!gotNotAll && !gotAll) {
                throw new BuildException("No scope elements specified in tag parameter.");
            }
            if (gotAll) {
                this.scope = "a";
                return;
            }
            StringBuilder buff = new StringBuilder(elements.length);
            for (int i2 = 0; i2 < elements.length; i2++) {
                if (elements[i2]) {
                    buff.append(Javadoc.SCOPE_ELEMENTS[i2].charAt(0));
                }
            }
            this.scope = buff.toString();
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getParameter() throws BuildException {
            if (this.name == null || this.name.isEmpty()) {
                throw new BuildException("No name specified for custom tag.");
            }
            if (getDescription() != null) {
                return this.name + ":" + (this.enabled ? "" : "X") + this.scope + ":" + getDescription();
            } else if (this.enabled && "a".equals(this.scope)) {
                return this.name;
            } else {
                return this.name + ":" + (this.enabled ? "" : "X") + this.scope;
            }
        }
    }

    public GroupArgument createGroup() {
        GroupArgument ga = new GroupArgument();
        this.groups.add(ga);
        return ga;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javadoc$GroupArgument.class */
    public class GroupArgument {
        private Html title;
        private final List<PackageName> packages = new Vector();

        public GroupArgument() {
        }

        public void setTitle(String src) {
            Html h = new Html();
            h.addText(src);
            addTitle(h);
        }

        public void addTitle(Html text) {
            this.title = text;
        }

        public String getTitle() {
            if (this.title != null) {
                return this.title.getText();
            }
            return null;
        }

        public void setPackages(String src) {
            StringTokenizer tok = new StringTokenizer(src, ",");
            while (tok.hasMoreTokens()) {
                String p = tok.nextToken();
                PackageName pn = new PackageName();
                pn.setName(p);
                addPackage(pn);
            }
        }

        public void addPackage(PackageName pn) {
            this.packages.add(pn);
        }

        public String getPackages() {
            return (String) this.packages.stream().map((v0) -> {
                return v0.toString();
            }).collect(Collectors.joining(":"));
        }
    }

    public void setCharset(String src) {
        addArgIfNotEmpty("-charset", src);
    }

    public void setFailonerror(boolean b) {
        this.failOnError = b;
    }

    public void setFailonwarning(boolean b) {
        this.failOnWarning = b;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    public void addPackageset(DirSet packageSet) {
        this.packageSets.add(packageSet);
    }

    public void addFileset(FileSet fs) {
        createSourceFiles().add(fs);
    }

    public ResourceCollectionContainer createSourceFiles() {
        return this.nestedSourceFiles;
    }

    public void setLinksource(boolean b) {
        this.linksource = b;
    }

    public void setBreakiterator(boolean b) {
        this.breakiterator = b;
    }

    public void setNoqualifier(String noqualifier) {
        this.noqualifier = noqualifier;
    }

    public void setIncludeNoSourcePackages(boolean b) {
        this.includeNoSourcePackages = b;
    }

    public void setDocFilesSubDirs(boolean b) {
        this.docFilesSubDirs = b;
    }

    public void setExcludeDocFilesSubDir(String s) {
        this.excludeDocFilesSubDir = s;
    }

    public void setPostProcessGeneratedJavadocs(boolean b) {
        this.postProcessGeneratedJavadocs = b;
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        checkTaskName();
        List<String> packagesToDoc = new Vector<>();
        Path sourceDirs = new Path(getProject());
        checkPackageAndSourcePath();
        if (this.sourcePath != null) {
            sourceDirs.addExisting(this.sourcePath);
        }
        parsePackages(packagesToDoc, sourceDirs);
        checkPackages(packagesToDoc, sourceDirs);
        List<SourceFile> sourceFilesToDoc = new ArrayList<>(this.sourceFiles);
        addSourceFiles(sourceFilesToDoc);
        checkPackagesToDoc(packagesToDoc, sourceFilesToDoc);
        log("Generating Javadoc", 2);
        Commandline toExecute = (Commandline) this.cmd.clone();
        if (this.executable != null) {
            toExecute.setExecutable(this.executable);
        } else {
            toExecute.setExecutable(JavaEnvUtils.getJdkExecutable("javadoc"));
        }
        generalJavadocArguments(toExecute);
        doSourcePath(toExecute, sourceDirs);
        doDoclet(toExecute);
        doBootPath(toExecute);
        doLinks(toExecute);
        doGroup(toExecute);
        doGroups(toExecute);
        doDocFilesSubDirs(toExecute);
        doModuleArguments(toExecute);
        doTags(toExecute);
        doSource(toExecute);
        doLinkSource(toExecute);
        doNoqualifier(toExecute);
        if (this.breakiterator) {
            toExecute.createArgument().setValue("-breakiterator");
        }
        if (this.useExternalFile) {
            writeExternalArgs(toExecute);
        }
        File tmpList = null;
        FileWriter wr = null;
        try {
            try {
                BufferedWriter srcListWriter = null;
                if (this.useExternalFile) {
                    tmpList = FILE_UTILS.createTempFile(getProject(), "javadoc", "", null, true, true);
                    toExecute.createArgument().setValue("@" + tmpList.getAbsolutePath());
                    wr = new FileWriter(tmpList.getAbsolutePath(), true);
                    srcListWriter = new BufferedWriter(wr);
                }
                doSourceAndPackageNames(toExecute, packagesToDoc, sourceFilesToDoc, this.useExternalFile, tmpList, srcListWriter);
                if (this.useExternalFile) {
                    srcListWriter.flush();
                }
                if (this.packageList != null) {
                    toExecute.createArgument().setValue("@" + this.packageList);
                }
                log(toExecute.describeCommand(), 3);
                log("Javadoc execution", 2);
                JavadocOutputStream out = new JavadocOutputStream(2);
                JavadocOutputStream err = new JavadocOutputStream(1);
                Execute exe = new Execute(new PumpStreamHandler(out, err));
                exe.setAntRun(getProject());
                exe.setWorkingDirectory(null);
                try {
                    try {
                        exe.setCommandline(toExecute.getCommandline());
                        int ret = exe.execute();
                        if (ret != 0 && this.failOnError) {
                            throw new BuildException("Javadoc returned " + ret, getLocation());
                        }
                        if (out.sawWarnings() && this.failOnWarning) {
                            throw new BuildException("Javadoc issued warnings.", getLocation());
                        }
                        postProcessGeneratedJavadocs();
                        if (tmpList != null) {
                            tmpList.delete();
                        }
                        out.logFlush();
                        err.logFlush();
                        FileUtils.close((OutputStream) out);
                        FileUtils.close((OutputStream) err);
                    } catch (Throwable th) {
                        if (tmpList != null) {
                            tmpList.delete();
                        }
                        out.logFlush();
                        err.logFlush();
                        FileUtils.close((OutputStream) out);
                        FileUtils.close((OutputStream) err);
                        throw th;
                    }
                } catch (IOException e) {
                    throw new BuildException("Javadoc failed: " + e, e, getLocation());
                }
            } catch (IOException e2) {
                if (tmpList != null) {
                    tmpList.delete();
                }
                throw new BuildException("Error creating temporary file", e2, getLocation());
            }
        } finally {
            FileUtils.close((Writer) wr);
        }
    }

    private void checkTaskName() {
        if ("javadoc2".equals(getTaskType())) {
            log("Warning: the task name <javadoc2> is deprecated. Use <javadoc> instead.", 1);
        }
    }

    private void checkPackageAndSourcePath() {
        if (this.packageList != null && this.sourcePath == null) {
            throw new BuildException("sourcePath attribute must be set when specifying packagelist.");
        }
    }

    private void checkPackages(List<String> packagesToDoc, Path sourceDirs) {
        if (!packagesToDoc.isEmpty() && sourceDirs.isEmpty()) {
            throw new BuildException("sourcePath attribute must be set when specifying package names.");
        }
    }

    private void checkPackagesToDoc(List<String> packagesToDoc, List<SourceFile> sourceFilesToDoc) {
        if (this.packageList == null && packagesToDoc.isEmpty() && sourceFilesToDoc.isEmpty() && this.moduleNames.isEmpty()) {
            throw new BuildException("No source files, no packages and no modules have been specified.");
        }
    }

    private void doSourcePath(Commandline toExecute, Path sourceDirs) {
        if (!sourceDirs.isEmpty()) {
            toExecute.createArgument().setValue("-sourcepath");
            toExecute.createArgument().setPath(sourceDirs);
        }
    }

    private void generalJavadocArguments(Commandline toExecute) {
        if (this.doctitle != null) {
            toExecute.createArgument().setValue("-doctitle");
            toExecute.createArgument().setValue(expand(this.doctitle.getText()));
        }
        if (this.header != null) {
            toExecute.createArgument().setValue("-header");
            toExecute.createArgument().setValue(expand(this.header.getText()));
        }
        if (this.footer != null) {
            toExecute.createArgument().setValue("-footer");
            toExecute.createArgument().setValue(expand(this.footer.getText()));
        }
        if (this.bottom != null) {
            toExecute.createArgument().setValue("-bottom");
            toExecute.createArgument().setValue(expand(this.bottom.getText()));
        }
        if (this.classpath == null) {
            this.classpath = new Path(getProject()).concatSystemClasspath("last");
        } else {
            this.classpath = this.classpath.concatSystemClasspath(Definer.OnError.POLICY_IGNORE);
        }
        if (this.classpath.size() > 0) {
            toExecute.createArgument().setValue("-classpath");
            toExecute.createArgument().setPath(this.classpath);
        }
        if (this.version && this.doclet == null) {
            toExecute.createArgument().setValue("-version");
        }
        if (this.author && this.doclet == null) {
            toExecute.createArgument().setValue("-author");
        }
        if (this.doclet == null && this.destDir == null) {
            throw new BuildException("destdir attribute must be set!");
        }
    }

    private void doDoclet(Commandline toExecute) {
        if (this.doclet != null) {
            if (this.doclet.getName() == null) {
                throw new BuildException("The doclet name must be specified.", getLocation());
            }
            toExecute.createArgument().setValue("-doclet");
            toExecute.createArgument().setValue(this.doclet.getName());
            if (this.doclet.getPath() != null) {
                Path docletPath = this.doclet.getPath().concatSystemClasspath(Definer.OnError.POLICY_IGNORE);
                if (docletPath.size() != 0) {
                    toExecute.createArgument().setValue("-docletpath");
                    toExecute.createArgument().setPath(docletPath);
                }
            }
            Iterator it = Collections.list(this.doclet.getParams()).iterator();
            while (it.hasNext()) {
                DocletParam param = (DocletParam) it.next();
                if (param.getName() == null) {
                    throw new BuildException("Doclet parameters must have a name");
                }
                toExecute.createArgument().setValue(param.getName());
                if (param.getValue() != null) {
                    toExecute.createArgument().setValue(param.getValue());
                }
            }
        }
    }

    private void writeExternalArgs(Commandline toExecute) {
        File optionsTmpFile = null;
        try {
            optionsTmpFile = FILE_UTILS.createTempFile(getProject(), "javadocOptions", "", null, true, true);
            String[] listOpt = toExecute.getArguments();
            toExecute.clearArgs();
            toExecute.createArgument().setValue("@" + optionsTmpFile.getAbsolutePath());
            BufferedWriter optionsListWriter = new BufferedWriter(new FileWriter(optionsTmpFile.getAbsolutePath(), true));
            for (String opt : listOpt) {
                if (opt.startsWith("-J-")) {
                    toExecute.createArgument().setValue(opt);
                } else if (opt.startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
                    optionsListWriter.write(opt);
                    optionsListWriter.write(Instruction.argsep);
                } else {
                    optionsListWriter.write(quoteString(opt));
                    optionsListWriter.newLine();
                }
            }
            optionsListWriter.close();
        } catch (IOException ex) {
            if (optionsTmpFile != null) {
                optionsTmpFile.delete();
            }
            throw new BuildException("Error creating or writing temporary file for javadoc options", ex, getLocation());
        }
    }

    private void doBootPath(Commandline toExecute) {
        Path bcp = new Path(getProject());
        if (this.bootclasspath != null) {
            bcp.append(this.bootclasspath);
        }
        Path bcp2 = bcp.concatSystemBootClasspath(Definer.OnError.POLICY_IGNORE);
        if (bcp2.size() > 0) {
            toExecute.createArgument().setValue("-bootclasspath");
            toExecute.createArgument().setPath(bcp2);
        }
    }

    private void doLinks(Commandline toExecute) {
        for (LinkArgument la : this.links) {
            if (la.getHref() == null || la.getHref().isEmpty()) {
                log("No href was given for the link - skipping", 3);
            } else {
                String link = null;
                if (la.shouldResolveLink()) {
                    File hrefAsFile = getProject().resolveFile(la.getHref());
                    if (hrefAsFile.exists()) {
                        try {
                            link = FILE_UTILS.getFileURL(hrefAsFile).toExternalForm();
                        } catch (MalformedURLException e) {
                            log("Warning: link location was invalid " + hrefAsFile, 1);
                        }
                    }
                }
                if (link == null) {
                    try {
                        URL base = new URL("file://.");
                        new URL(base, la.getHref());
                        link = la.getHref();
                    } catch (MalformedURLException e2) {
                        log("Link href \"" + la.getHref() + "\" is not a valid url - skipping link", 1);
                    }
                }
                if (la.isLinkOffline()) {
                    File packageListLocation = la.getPackagelistLoc();
                    URL packageListURL = la.getPackagelistURL();
                    if (packageListLocation == null && packageListURL == null) {
                        throw new BuildException("The package list location for link " + la.getHref() + " must be provided because the link is offline");
                    }
                    if (packageListLocation != null) {
                        File packageListFile = new File(packageListLocation, "package-list");
                        if (packageListFile.exists()) {
                            try {
                                packageListURL = FILE_UTILS.getFileURL(packageListLocation);
                            } catch (MalformedURLException e3) {
                                log("Warning: Package list location was invalid " + packageListLocation, 1);
                            }
                        } else {
                            log("Warning: No package list was found at " + packageListLocation, 3);
                        }
                    }
                    if (packageListURL != null) {
                        toExecute.createArgument().setValue("-linkoffline");
                        toExecute.createArgument().setValue(link);
                        toExecute.createArgument().setValue(packageListURL.toExternalForm());
                    }
                } else {
                    toExecute.createArgument().setValue("-link");
                    toExecute.createArgument().setValue(link);
                }
            }
        }
    }

    private void doGroup(Commandline toExecute) {
        if (this.group != null) {
            StringTokenizer tok = new StringTokenizer(this.group, ",", false);
            while (tok.hasMoreTokens()) {
                String grp = tok.nextToken().trim();
                int space = grp.indexOf(32);
                if (space > 0) {
                    String name = grp.substring(0, space);
                    String pkgList = grp.substring(space + 1);
                    toExecute.createArgument().setValue("-group");
                    toExecute.createArgument().setValue(name);
                    toExecute.createArgument().setValue(pkgList);
                }
            }
        }
    }

    private void doGroups(Commandline toExecute) {
        for (GroupArgument ga : this.groups) {
            String title = ga.getTitle();
            String packages = ga.getPackages();
            if (title == null || packages == null) {
                throw new BuildException("The title and packages must be specified for group elements.");
            }
            toExecute.createArgument().setValue("-group");
            toExecute.createArgument().setValue(expand(title));
            toExecute.createArgument().setValue(packages);
        }
    }

    private void doNoqualifier(Commandline toExecute) {
        if (this.noqualifier != null && this.doclet == null) {
            toExecute.createArgument().setValue("-noqualifier");
            toExecute.createArgument().setValue(this.noqualifier);
        }
    }

    private void doLinkSource(Commandline toExecute) {
        if (this.linksource && this.doclet == null) {
            toExecute.createArgument().setValue("-linksource");
        }
    }

    private void doSource(Commandline toExecute) {
        String sourceArg = this.source != null ? this.source : getProject().getProperty(MagicNames.BUILD_JAVAC_SOURCE);
        if (sourceArg != null) {
            toExecute.createArgument().setValue("-source");
            toExecute.createArgument().setValue(sourceArg);
        }
    }

    private void doTags(Commandline toExecute) {
        String[] includedFiles;
        for (Object element : this.tags) {
            if (element instanceof TagArgument) {
                TagArgument ta = (TagArgument) element;
                File tagDir = ta.getDir(getProject());
                if (tagDir == null) {
                    toExecute.createArgument().setValue("-tag");
                    toExecute.createArgument().setValue(ta.getParameter());
                } else {
                    DirectoryScanner tagDefScanner = ta.getDirectoryScanner(getProject());
                    for (String file : tagDefScanner.getIncludedFiles()) {
                        File tagDefFile = new File(tagDir, file);
                        try {
                            BufferedReader in = new BufferedReader(new FileReader(tagDefFile));
                            in.lines().forEach(line -> {
                                toExecute.createArgument().setValue("-tag");
                                toExecute.createArgument().setValue(line);
                            });
                            in.close();
                        } catch (IOException ioe) {
                            throw new BuildException("Couldn't read tag file from " + tagDefFile.getAbsolutePath(), ioe);
                        }
                    }
                    continue;
                }
            } else {
                ExtensionInfo tagletInfo = (ExtensionInfo) element;
                toExecute.createArgument().setValue("-taglet");
                toExecute.createArgument().setValue(tagletInfo.getName());
                if (tagletInfo.getPath() != null) {
                    Path tagletPath = tagletInfo.getPath().concatSystemClasspath(Definer.OnError.POLICY_IGNORE);
                    if (!tagletPath.isEmpty()) {
                        toExecute.createArgument().setValue("-tagletpath");
                        toExecute.createArgument().setPath(tagletPath);
                    }
                }
            }
        }
    }

    private void doDocFilesSubDirs(Commandline toExecute) {
        if (this.docFilesSubDirs) {
            toExecute.createArgument().setValue("-docfilessubdirs");
            if (this.excludeDocFilesSubDir != null && !this.excludeDocFilesSubDir.trim().isEmpty()) {
                toExecute.createArgument().setValue("-excludedocfilessubdir");
                toExecute.createArgument().setValue(this.excludeDocFilesSubDir);
            }
        }
    }

    private void doSourceAndPackageNames(Commandline toExecute, List<String> packagesToDoc, List<SourceFile> sourceFilesToDoc, boolean useExternalFile, File tmpList, BufferedWriter srcListWriter) throws IOException {
        for (String packageName : packagesToDoc) {
            if (useExternalFile) {
                srcListWriter.write(packageName);
                srcListWriter.newLine();
            } else {
                toExecute.createArgument().setValue(packageName);
            }
        }
        for (SourceFile sf : sourceFilesToDoc) {
            String sourceFileName = sf.getFile().getAbsolutePath();
            if (useExternalFile) {
                if (sourceFileName.contains(Instruction.argsep)) {
                    String name = sourceFileName;
                    if (File.separatorChar == '\\') {
                        name = sourceFileName.replace(File.separatorChar, '/');
                    }
                    srcListWriter.write("\"" + name + "\"");
                } else {
                    srcListWriter.write(sourceFileName);
                }
                srcListWriter.newLine();
            } else {
                toExecute.createArgument().setValue(sourceFileName);
            }
        }
    }

    private String quoteString(String str) {
        if (!containsWhitespace(str) && !str.contains("'") && !str.contains("\"")) {
            return str;
        }
        if (!str.contains("'")) {
            return quoteString(str, '\'');
        }
        return quoteString(str, '\"');
    }

    private boolean containsWhitespace(String s) {
        char[] charArray;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                return true;
            }
        }
        return false;
    }

    private String quoteString(String str, char delim) {
        char[] charArray;
        boolean z;
        StringBuilder buf = new StringBuilder(str.length() * 2);
        buf.append(delim);
        boolean lastCharWasCR = false;
        for (char c : str.toCharArray()) {
            if (c == delim) {
                buf.append('\\').append(c);
                z = false;
            } else {
                switch (c) {
                    case '\n':
                        if (!lastCharWasCR) {
                            buf.append("\\\n");
                        } else {
                            buf.append("\n");
                        }
                        z = false;
                        continue;
                    case '\r':
                        buf.append("\\\r");
                        z = true;
                        continue;
                    case '\\':
                        buf.append("\\\\");
                        z = false;
                        continue;
                    default:
                        buf.append(c);
                        z = false;
                        continue;
                }
            }
            lastCharWasCR = z;
        }
        buf.append(delim);
        return buf.toString();
    }

    private void addSourceFiles(List<SourceFile> sf) {
        Iterator<ResourceCollection> it = this.nestedSourceFiles.iterator();
        while (it.hasNext()) {
            ResourceCollection rc = it.next();
            if (!rc.isFilesystemOnly()) {
                throw new BuildException("only file system based resources are supported by javadoc");
            }
            if (rc instanceof FileSet) {
                FileSet fs = (FileSet) rc;
                if (!fs.hasPatterns() && !fs.hasSelectors()) {
                    FileSet fs2 = (FileSet) fs.clone();
                    fs2.createInclude().setName("**/*.java");
                    if (this.includeNoSourcePackages) {
                        fs2.createInclude().setName("**/package.html");
                    }
                    rc = fs2;
                }
            }
            for (Resource r : rc) {
                sf.add(new SourceFile(((FileProvider) r.as(FileProvider.class)).getFile()));
            }
        }
    }

    private void parsePackages(List<String> pn, Path sp) {
        String[] includedDirectories;
        String[] list;
        Set<String> addedPackages = new HashSet<>();
        List<DirSet> dirSets = new ArrayList<>(this.packageSets);
        if (this.sourcePath != null) {
            PatternSet ps = new PatternSet();
            ps.setProject(getProject());
            if (this.packageNames.isEmpty()) {
                ps.createInclude().setName(SelectorUtils.DEEP_TREE_MATCH);
            } else {
                this.packageNames.stream().map((v0) -> {
                    return v0.getName();
                }).map(s -> {
                    return s.replace('.', '/').replaceFirst("\\*$", SelectorUtils.DEEP_TREE_MATCH);
                }).forEach(pkg -> {
                    ps.createInclude().setName(pkg);
                });
            }
            this.excludePackageNames.stream().map((v0) -> {
                return v0.getName();
            }).map(s2 -> {
                return s2.replace('.', '/').replaceFirst("\\*$", SelectorUtils.DEEP_TREE_MATCH);
            }).forEach(pkg2 -> {
                ps.createExclude().setName(pkg2);
            });
            for (String pathElement : this.sourcePath.list()) {
                File dir = new File(pathElement);
                if (dir.isDirectory()) {
                    DirSet ds = new DirSet();
                    ds.setProject(getProject());
                    ds.setDefaultexcludes(this.useDefaultExcludes);
                    ds.setDir(dir);
                    ds.createPatternSet().addConfiguredPatternset(ps);
                    dirSets.add(ds);
                } else {
                    log("Skipping " + pathElement + " since it is no directory.", 1);
                }
            }
        }
        for (DirSet ds2 : dirSets) {
            File baseDir = ds2.getDir(getProject());
            log("scanning " + baseDir + " for packages.", 4);
            DirectoryScanner dsc = ds2.getDirectoryScanner(getProject());
            boolean containsPackages = false;
            for (String dir2 : dsc.getIncludedDirectories()) {
                File pd = new File(baseDir, dir2);
                String[] files = pd.list(directory, name -> {
                    return name.endsWith(".java") || (this.includeNoSourcePackages && name.equals("package.html"));
                });
                if (files.length > 0) {
                    if (dir2.isEmpty()) {
                        log(baseDir + " contains source files in the default package, you must specify them as source files not packages.", 1);
                    } else {
                        containsPackages = true;
                        String packageName = dir2.replace(File.separatorChar, '.');
                        if (!addedPackages.contains(packageName)) {
                            addedPackages.add(packageName);
                            pn.add(packageName);
                        }
                    }
                }
            }
            if (containsPackages) {
                sp.createPathElement().setLocation(baseDir);
            } else {
                log(baseDir + " doesn't contain any packages, dropping it.", 3);
            }
        }
    }

    private void postProcessGeneratedJavadocs() throws IOException {
        String[] includedFiles;
        if (!this.postProcessGeneratedJavadocs) {
            return;
        }
        if (this.destDir != null && !this.destDir.isDirectory()) {
            log("No javadoc created, no need to post-process anything", 3);
            return;
        }
        InputStream in = Javadoc.class.getResourceAsStream("javadoc-frame-injections-fix.txt");
        if (in == null) {
            throw new FileNotFoundException("Missing resource 'javadoc-frame-injections-fix.txt' in classpath.");
        }
        try {
            String fixData = fixLineFeeds(FileUtils.readFully(new InputStreamReader(in, StandardCharsets.US_ASCII))).trim();
            DirectoryScanner ds = new DirectoryScanner();
            ds.setBasedir(this.destDir);
            ds.setCaseSensitive(false);
            ds.setIncludes(new String[]{"**/index.html", "**/index.htm", "**/toc.html", "**/toc.htm"});
            ds.addDefaultExcludes();
            ds.scan();
            int patched = 0;
            for (String f : ds.getIncludedFiles()) {
                patched += postProcess(new File(this.destDir, f), fixData);
            }
            if (patched > 0) {
                log("Patched " + patched + " link injection vulnerable javadocs", 2);
            }
        } finally {
            FileUtils.close(in);
        }
    }

    private int postProcess(File file, String fixData) throws IOException {
        String enc = this.docEncoding != null ? this.docEncoding : FILE_UTILS.getDefaultEncoding();
        InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath(), new OpenOption[0]), enc);
        try {
            String fileContents = fixLineFeeds(FileUtils.safeReadFully(reader));
            reader.close();
            if (!fileContents.contains("function validURL(url) {")) {
                String patchedFileContents = patchContent(fileContents, fixData);
                if (!patchedFileContents.equals(fileContents)) {
                    OutputStreamWriter w = new OutputStreamWriter(Files.newOutputStream(file.toPath(), new OpenOption[0]), enc);
                    try {
                        w.write(patchedFileContents);
                        w.close();
                        w.close();
                        return 1;
                    } catch (Throwable th) {
                        try {
                            w.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
                return 0;
            }
            return 0;
        } catch (Throwable th3) {
            try {
                reader.close();
            } catch (Throwable th4) {
                th3.addSuppressed(th4);
            }
            throw th3;
        }
    }

    private String fixLineFeeds(String orig) {
        return orig.replace("\r\n", "\n").replace("\n", System.lineSeparator());
    }

    private String patchContent(String fileContents, String fixData) {
        int start = fileContents.indexOf(LOAD_FRAME);
        if (start >= 0) {
            return fileContents.substring(0, start) + fixData + fileContents.substring(start + LOAD_FRAME_LEN);
        }
        return fileContents;
    }

    private void doModuleArguments(Commandline toExecute) {
        if (!this.moduleNames.isEmpty()) {
            toExecute.createArgument().setValue("--module");
            toExecute.createArgument().setValue((String) this.moduleNames.stream().map((v0) -> {
                return v0.getName();
            }).collect(Collectors.joining(",")));
        }
        if (this.modulePath != null) {
            toExecute.createArgument().setValue("--module-path");
            toExecute.createArgument().setPath(this.modulePath);
        }
        if (this.moduleSourcePath != null) {
            toExecute.createArgument().setValue("--module-source-path");
            toExecute.createArgument().setPath(this.moduleSourcePath);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Javadoc$JavadocOutputStream.class */
    private class JavadocOutputStream extends LogOutputStream {
        private String queuedLine;
        private boolean sawWarnings;

        JavadocOutputStream(int level) {
            super((Task) Javadoc.this, level);
            this.queuedLine = null;
            this.sawWarnings = false;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.apache.tools.ant.taskdefs.LogOutputStream
        public void processLine(String line, int messageLevel) {
            if (line.contains("warning")) {
                this.sawWarnings = true;
            }
            if (messageLevel == 2 && line.startsWith("Generating ")) {
                if (this.queuedLine != null) {
                    super.processLine(this.queuedLine, 3);
                }
                this.queuedLine = line;
                return;
            }
            if (this.queuedLine != null) {
                if (line.startsWith("Building ")) {
                    super.processLine(this.queuedLine, 3);
                } else {
                    super.processLine(this.queuedLine, 2);
                }
                this.queuedLine = null;
            }
            super.processLine(line, messageLevel);
        }

        protected void logFlush() {
            if (this.queuedLine != null) {
                super.processLine(this.queuedLine, 3);
                this.queuedLine = null;
            }
        }

        public boolean sawWarnings() {
            return this.sawWarnings;
        }
    }

    protected String expand(String content) {
        return getProject().replaceProperties(content);
    }
}
