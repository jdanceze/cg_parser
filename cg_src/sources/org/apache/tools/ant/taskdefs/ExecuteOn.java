package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.AbstractFileSet;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.ResourceUtils;
import org.apache.tools.ant.util.SourceFileScanner;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/ExecuteOn.class */
public class ExecuteOn extends ExecTask {
    protected Vector<AbstractFileSet> filesets = new Vector<>();
    private Union resources = null;
    private boolean relative = false;
    private boolean parallel = false;
    private boolean forwardSlash = false;
    protected String type = "file";
    protected Commandline.Marker srcFilePos = null;
    private boolean skipEmpty = false;
    protected Commandline.Marker targetFilePos = null;
    protected Mapper mapperElement = null;
    protected FileNameMapper mapper = null;
    protected File destDir = null;
    private int maxParallel = -1;
    private boolean addSourceFile = true;
    private boolean verbose = false;
    private boolean ignoreMissing = true;
    private boolean force = false;
    protected boolean srcIsFirst = true;

    public void addFileset(FileSet set) {
        this.filesets.addElement(set);
    }

    public void addDirset(DirSet set) {
        this.filesets.addElement(set);
    }

    public void addFilelist(FileList list) {
        add(list);
    }

    public void add(ResourceCollection rc) {
        if (this.resources == null) {
            this.resources = new Union();
        }
        this.resources.add(rc);
    }

    public void setRelative(boolean relative) {
        this.relative = relative;
    }

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }

    public void setType(FileDirBoth type) {
        this.type = type.getValue();
    }

    public void setSkipEmptyFilesets(boolean skip) {
        this.skipEmpty = skip;
    }

    public void setDest(File destDir) {
        this.destDir = destDir;
    }

    public void setForwardslash(boolean forwardSlash) {
        this.forwardSlash = forwardSlash;
    }

    public void setMaxParallel(int max) {
        this.maxParallel = max;
    }

    public void setAddsourcefile(boolean b) {
        this.addSourceFile = b;
    }

    public void setVerbose(boolean b) {
        this.verbose = b;
    }

    public void setIgnoremissing(boolean b) {
        this.ignoreMissing = b;
    }

    public void setForce(boolean b) {
        this.force = b;
    }

    public Commandline.Marker createSrcfile() {
        if (this.srcFilePos != null) {
            throw new BuildException(getTaskType() + " doesn't support multiple srcfile elements.", getLocation());
        }
        this.srcFilePos = this.cmdl.createMarker();
        return this.srcFilePos;
    }

    public Commandline.Marker createTargetfile() {
        if (this.targetFilePos != null) {
            throw new BuildException(getTaskType() + " doesn't support multiple targetfile elements.", getLocation());
        }
        this.targetFilePos = this.cmdl.createMarker();
        this.srcIsFirst = this.srcFilePos != null;
        return this.targetFilePos;
    }

    public Mapper createMapper() throws BuildException {
        if (this.mapperElement != null) {
            throw new BuildException(Expand.ERROR_MULTIPLE_MAPPERS, getLocation());
        }
        this.mapperElement = new Mapper(getProject());
        return this.mapperElement;
    }

    public void add(FileNameMapper fileNameMapper) {
        createMapper().add(fileNameMapper);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.ExecTask
    public void checkConfiguration() {
        if ("execon".equals(getTaskName())) {
            log("!! execon is deprecated. Use apply instead. !!");
        }
        super.checkConfiguration();
        if (this.filesets.isEmpty() && this.resources == null) {
            throw new BuildException("no resources specified", getLocation());
        }
        if (this.targetFilePos != null && this.mapperElement == null) {
            throw new BuildException("targetfile specified without mapper", getLocation());
        }
        if (this.destDir != null && this.mapperElement == null) {
            throw new BuildException("dest specified without mapper", getLocation());
        }
        if (this.mapperElement != null) {
            this.mapper = this.mapperElement.getImplementation();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.ExecTask
    public ExecuteStreamHandler createHandler() throws BuildException {
        return this.redirectorElement == null ? super.createHandler() : new PumpStreamHandler();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.ExecTask
    public void setupRedirector() {
        super.setupRedirector();
        this.redirector.setAppendProperties(true);
    }

    @Override // org.apache.tools.ant.taskdefs.ExecTask
    protected void runExec(Execute exe) throws BuildException {
        String[] dirs;
        String[] files;
        int totalFiles = 0;
        int totalDirs = 0;
        boolean haveExecuted = false;
        try {
            try {
                Vector<String> fileNames = new Vector<>();
                Vector<File> baseDirs = new Vector<>();
                Iterator<AbstractFileSet> it = this.filesets.iterator();
                while (it.hasNext()) {
                    AbstractFileSet fs = it.next();
                    String currentType = this.type;
                    if ((fs instanceof DirSet) && !"dir".equals(this.type)) {
                        log("Found a nested dirset but type is " + this.type + ". Temporarily switching to type=\"dir\" on the assumption that you really did mean <dirset> not <fileset>.", 4);
                        currentType = "dir";
                    }
                    File base = fs.getDir(getProject());
                    DirectoryScanner ds = fs.getDirectoryScanner(getProject());
                    if (!"dir".equals(currentType)) {
                        for (String value : getFiles(base, ds)) {
                            totalFiles++;
                            fileNames.add(value);
                            baseDirs.add(base);
                        }
                    }
                    if (!"file".equals(currentType)) {
                        for (String value2 : getDirs(base, ds)) {
                            totalDirs++;
                            fileNames.add(value2);
                            baseDirs.add(base);
                        }
                    }
                    if (fileNames.isEmpty() && this.skipEmpty) {
                        logSkippingFileset(currentType, ds, base);
                    } else if (!this.parallel) {
                        Iterator<String> it2 = fileNames.iterator();
                        while (it2.hasNext()) {
                            String srcFile = it2.next();
                            String[] command = getCommandline(srcFile, base);
                            log(Commandline.describeCommand(command), 3);
                            exe.setCommandline(command);
                            if (this.redirectorElement != null) {
                                setupRedirector();
                                this.redirectorElement.configure(this.redirector, srcFile);
                            }
                            if (this.redirectorElement != null || haveExecuted) {
                                exe.setStreamHandler(this.redirector.createHandler());
                            }
                            runExecute(exe);
                            haveExecuted = true;
                        }
                        fileNames.clear();
                        baseDirs.clear();
                    }
                }
                if (this.resources != null) {
                    Iterator<Resource> it3 = this.resources.iterator();
                    while (it3.hasNext()) {
                        Resource res = it3.next();
                        if (res.isExists() || !this.ignoreMissing) {
                            File base2 = null;
                            String name = res.getName();
                            FileProvider fp = (FileProvider) res.as(FileProvider.class);
                            if (fp != null) {
                                FileResource fr = ResourceUtils.asFileResource(fp);
                                base2 = fr.getBaseDir();
                                if (base2 == null) {
                                    name = fr.getFile().getAbsolutePath();
                                }
                            }
                            if (restrict(new String[]{name}, base2).length != 0) {
                                if ((!res.isDirectory() || !res.isExists()) && !"dir".equals(this.type)) {
                                    totalFiles++;
                                } else if (res.isDirectory() && !"file".equals(this.type)) {
                                    totalDirs++;
                                }
                                baseDirs.add(base2);
                                fileNames.add(name);
                                if (!this.parallel) {
                                    String[] command2 = getCommandline(name, base2);
                                    log(Commandline.describeCommand(command2), 3);
                                    exe.setCommandline(command2);
                                    if (this.redirectorElement != null) {
                                        setupRedirector();
                                        this.redirectorElement.configure(this.redirector, name);
                                    }
                                    if (this.redirectorElement != null || haveExecuted) {
                                        exe.setStreamHandler(this.redirector.createHandler());
                                    }
                                    runExecute(exe);
                                    haveExecuted = true;
                                    fileNames.clear();
                                    baseDirs.clear();
                                }
                            }
                        }
                    }
                }
                if (this.parallel && (!fileNames.isEmpty() || !this.skipEmpty)) {
                    runParallel(exe, fileNames, baseDirs);
                    haveExecuted = true;
                }
                if (haveExecuted) {
                    log("Applied " + this.cmdl.getExecutable() + " to " + totalFiles + " file" + (totalFiles != 1 ? "s" : "") + " and " + totalDirs + " director" + (totalDirs != 1 ? "ies" : "y") + ".", this.verbose ? 2 : 3);
                }
            } catch (IOException e) {
                throw new BuildException("Execute failed: " + e, e, getLocation());
            }
        } finally {
            logFlush();
            this.redirector.setAppendProperties(false);
            this.redirector.setProperties();
        }
    }

    private void logSkippingFileset(String currentType, DirectoryScanner ds, File base) {
        int includedCount = (!"dir".equals(currentType) ? ds.getIncludedFilesCount() : 0) + (!"file".equals(currentType) ? ds.getIncludedDirsCount() : 0);
        log("Skipping fileset for directory " + base + ". It is " + (includedCount > 0 ? "up to date." : "empty."), this.verbose ? 2 : 3);
    }

    protected String[] getCommandline(String[] srcFiles, File[] baseDirs) {
        String src;
        String name;
        char fileSeparator = File.separatorChar;
        List<String> targets = new ArrayList<>();
        if (this.targetFilePos != null) {
            Set<String> addedFiles = new HashSet<>();
            for (String srcFile : srcFiles) {
                String[] subTargets = this.mapper.mapFileName(srcFile);
                if (subTargets != null) {
                    for (String subTarget : subTargets) {
                        if (this.relative) {
                            name = subTarget;
                        } else {
                            name = new File(this.destDir, subTarget).getAbsolutePath();
                        }
                        if (this.forwardSlash && fileSeparator != '/') {
                            name = name.replace(fileSeparator, '/');
                        }
                        if (!addedFiles.contains(name)) {
                            targets.add(name);
                            addedFiles.add(name);
                        }
                    }
                }
            }
        }
        String[] targetFiles = (String[]) targets.toArray(new String[targets.size()]);
        if (!this.addSourceFile) {
            srcFiles = new String[0];
        }
        String[] orig = this.cmdl.getCommandline();
        String[] result = new String[orig.length + srcFiles.length + targetFiles.length];
        int srcIndex = orig.length;
        if (this.srcFilePos != null) {
            srcIndex = this.srcFilePos.getPosition();
        }
        if (this.targetFilePos != null) {
            int targetIndex = this.targetFilePos.getPosition();
            if (srcIndex < targetIndex || (srcIndex == targetIndex && this.srcIsFirst)) {
                System.arraycopy(orig, 0, result, 0, srcIndex);
                System.arraycopy(orig, srcIndex, result, srcIndex + srcFiles.length, targetIndex - srcIndex);
                insertTargetFiles(targetFiles, result, targetIndex + srcFiles.length, this.targetFilePos.getPrefix(), this.targetFilePos.getSuffix());
                System.arraycopy(orig, targetIndex, result, targetIndex + srcFiles.length + targetFiles.length, orig.length - targetIndex);
            } else {
                System.arraycopy(orig, 0, result, 0, targetIndex);
                insertTargetFiles(targetFiles, result, targetIndex, this.targetFilePos.getPrefix(), this.targetFilePos.getSuffix());
                System.arraycopy(orig, targetIndex, result, targetIndex + targetFiles.length, srcIndex - targetIndex);
                System.arraycopy(orig, srcIndex, result, srcIndex + srcFiles.length + targetFiles.length, orig.length - srcIndex);
                srcIndex += targetFiles.length;
            }
        } else {
            System.arraycopy(orig, 0, result, 0, srcIndex);
            System.arraycopy(orig, srcIndex, result, srcIndex + srcFiles.length, orig.length - srcIndex);
        }
        for (int i = 0; i < srcFiles.length; i++) {
            if (this.relative) {
                src = srcFiles[i];
            } else {
                src = new File(baseDirs[i], srcFiles[i]).getAbsolutePath();
            }
            if (this.forwardSlash && fileSeparator != '/') {
                src = src.replace(fileSeparator, '/');
            }
            if (this.srcFilePos != null && (!this.srcFilePos.getPrefix().isEmpty() || !this.srcFilePos.getSuffix().isEmpty())) {
                src = this.srcFilePos.getPrefix() + src + this.srcFilePos.getSuffix();
            }
            result[srcIndex + i] = src;
        }
        return result;
    }

    protected String[] getCommandline(String srcFile, File baseDir) {
        return getCommandline(new String[]{srcFile}, new File[]{baseDir});
    }

    protected String[] getFiles(File baseDir, DirectoryScanner ds) {
        return restrict(ds.getIncludedFiles(), baseDir);
    }

    protected String[] getDirs(File baseDir, DirectoryScanner ds) {
        return restrict(ds.getIncludedDirectories(), baseDir);
    }

    protected String[] getFilesAndDirs(FileList list) {
        return restrict(list.getFiles(getProject()), list.getDir(getProject()));
    }

    private String[] restrict(String[] s, File baseDir) {
        return (this.mapper == null || this.force) ? s : new SourceFileScanner(this).restrict(s, baseDir, this.destDir, this.mapper);
    }

    protected void runParallel(Execute exe, Vector<String> fileNames, Vector<File> baseDirs) throws IOException, BuildException {
        String[] s = (String[]) fileNames.toArray(new String[fileNames.size()]);
        File[] b = (File[]) baseDirs.toArray(new File[baseDirs.size()]);
        if (this.maxParallel <= 0 || s.length == 0) {
            String[] command = getCommandline(s, b);
            log(Commandline.describeCommand(command), 3);
            exe.setCommandline(command);
            if (this.redirectorElement != null) {
                setupRedirector();
                this.redirectorElement.configure(this.redirector, null);
                exe.setStreamHandler(this.redirector.createHandler());
            }
            runExecute(exe);
            return;
        }
        int stillToDo = fileNames.size();
        int i = 0;
        while (true) {
            int currentOffset = i;
            if (stillToDo > 0) {
                int currentAmount = Math.min(stillToDo, this.maxParallel);
                String[] cs = new String[currentAmount];
                System.arraycopy(s, currentOffset, cs, 0, currentAmount);
                File[] cb = new File[currentAmount];
                System.arraycopy(b, currentOffset, cb, 0, currentAmount);
                String[] command2 = getCommandline(cs, cb);
                log(Commandline.describeCommand(command2), 3);
                exe.setCommandline(command2);
                if (this.redirectorElement != null) {
                    setupRedirector();
                    this.redirectorElement.configure(this.redirector, null);
                }
                if (this.redirectorElement != null || currentOffset > 0) {
                    exe.setStreamHandler(this.redirector.createHandler());
                }
                runExecute(exe);
                stillToDo -= currentAmount;
                i = currentOffset + currentAmount;
            } else {
                return;
            }
        }
    }

    private static void insertTargetFiles(String[] targetFiles, String[] arguments, int insertPosition, String prefix, String suffix) {
        if (prefix.isEmpty() && suffix.isEmpty()) {
            System.arraycopy(targetFiles, 0, arguments, insertPosition, targetFiles.length);
            return;
        }
        for (int i = 0; i < targetFiles.length; i++) {
            arguments[insertPosition + i] = prefix + targetFiles[i] + suffix;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/ExecuteOn$FileDirBoth.class */
    public static class FileDirBoth extends EnumeratedAttribute {
        public static final String FILE = "file";
        public static final String DIR = "dir";

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"file", "dir", "both"};
        }
    }
}
