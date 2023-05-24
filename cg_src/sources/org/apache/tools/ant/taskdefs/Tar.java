package org.apache.tools.ant.taskdefs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.ArchiveFileSet;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.ArchiveResource;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.TarResource;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.MergingMapper;
import org.apache.tools.ant.util.ResourceUtils;
import org.apache.tools.ant.util.SourceFileScanner;
import org.apache.tools.bzip2.CBZip2OutputStream;
import org.apache.tools.tar.TarConstants;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarOutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Tar.class */
public class Tar extends MatchingTask {
    private static final int BUFFER_SIZE = 8192;
    @Deprecated
    public static final String WARN = "warn";
    @Deprecated
    public static final String FAIL = "fail";
    @Deprecated
    public static final String TRUNCATE = "truncate";
    @Deprecated
    public static final String GNU = "gnu";
    @Deprecated
    public static final String OMIT = "omit";
    File tarFile;
    File baseDir;
    private TarLongFileMode longFileMode = new TarLongFileMode();
    Vector<TarFileSet> filesets = new Vector<>();
    private final List<ResourceCollection> resourceCollections = new Vector();
    private boolean longWarningGiven = false;
    private TarCompressionMethod compression = new TarCompressionMethod();
    private String encoding;

    public TarFileSet createTarFileSet() {
        TarFileSet fs = new TarFileSet();
        fs.setProject(getProject());
        this.filesets.addElement(fs);
        return fs;
    }

    public void add(ResourceCollection res) {
        this.resourceCollections.add(res);
    }

    @Deprecated
    public void setTarfile(File tarFile) {
        this.tarFile = tarFile;
    }

    public void setDestFile(File destFile) {
        this.tarFile = destFile;
    }

    public void setBasedir(File baseDir) {
        this.baseDir = baseDir;
    }

    @Deprecated
    public void setLongfile(String mode) {
        log("DEPRECATED - The setLongfile(String) method has been deprecated. Use setLongfile(Tar.TarLongFileMode) instead.");
        this.longFileMode = new TarLongFileMode();
        this.longFileMode.setValue(mode);
    }

    public void setLongfile(TarLongFileMode mode) {
        this.longFileMode = mode;
    }

    public void setCompression(TarCompressionMethod mode) {
        this.compression = mode;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.tarFile == null) {
            throw new BuildException("tarfile attribute must be set!", getLocation());
        }
        if (this.tarFile.exists() && this.tarFile.isDirectory()) {
            throw new BuildException("tarfile is a directory!", getLocation());
        }
        if (this.tarFile.exists() && !this.tarFile.canWrite()) {
            throw new BuildException("Can not write to the specified tarfile!", getLocation());
        }
        Vector<TarFileSet> savedFileSets = new Vector<>(this.filesets);
        try {
            if (this.baseDir != null) {
                if (!this.baseDir.exists()) {
                    throw new BuildException("basedir does not exist!", getLocation());
                }
                TarFileSet mainFileSet = new TarFileSet(this.fileset);
                mainFileSet.setDir(this.baseDir);
                this.filesets.addElement(mainFileSet);
            }
            if (this.filesets.isEmpty() && this.resourceCollections.isEmpty()) {
                throw new BuildException("You must supply either a basedir attribute or some nested resource collections.", getLocation());
            }
            boolean upToDate = true;
            Iterator<TarFileSet> it = this.filesets.iterator();
            while (it.hasNext()) {
                TarFileSet tfs = it.next();
                upToDate &= check(tfs);
            }
            for (ResourceCollection rcol : this.resourceCollections) {
                upToDate &= check(rcol);
            }
            if (upToDate) {
                log("Nothing to do: " + this.tarFile.getAbsolutePath() + " is up to date.", 2);
                this.filesets = savedFileSets;
                return;
            }
            File parent = this.tarFile.getParentFile();
            if (parent != null && !parent.isDirectory() && !parent.mkdirs() && !parent.isDirectory()) {
                throw new BuildException("Failed to create missing parent directory for %s", this.tarFile);
            }
            log("Building tar: " + this.tarFile.getAbsolutePath(), 2);
            try {
                TarOutputStream tOut = new TarOutputStream(this.compression.compress(new BufferedOutputStream(Files.newOutputStream(this.tarFile.toPath(), new OpenOption[0]))), this.encoding);
                try {
                    tOut.setDebug(true);
                    if (this.longFileMode.isTruncateMode()) {
                        tOut.setLongFileMode(1);
                    } else if (this.longFileMode.isFailMode() || this.longFileMode.isOmitMode()) {
                        tOut.setLongFileMode(0);
                    } else if (this.longFileMode.isPosixMode()) {
                        tOut.setLongFileMode(3);
                    } else {
                        tOut.setLongFileMode(2);
                    }
                    this.longWarningGiven = false;
                    Iterator<TarFileSet> it2 = this.filesets.iterator();
                    while (it2.hasNext()) {
                        TarFileSet tfs2 = it2.next();
                        tar(tfs2, tOut);
                    }
                    for (ResourceCollection rcol2 : this.resourceCollections) {
                        tar(rcol2, tOut);
                    }
                    tOut.close();
                } catch (Throwable th) {
                    try {
                        tOut.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException ioe) {
                String msg = "Problem creating TAR: " + ioe.getMessage();
                throw new BuildException(msg, ioe, getLocation());
            }
        } finally {
            this.filesets = savedFileSets;
        }
    }

    protected void tarFile(File file, TarOutputStream tOut, String vPath, TarFileSet tarFileSet) throws IOException {
        if (file.equals(this.tarFile)) {
            return;
        }
        tarResource(new FileResource(file), tOut, vPath, tarFileSet);
    }

    /* JADX WARN: Finally extract failed */
    protected void tarResource(Resource r, TarOutputStream tOut, String vPath, TarFileSet tarFileSet) throws IOException {
        if (!r.isExists()) {
            return;
        }
        boolean preserveLeadingSlashes = false;
        if (tarFileSet != null) {
            String fullpath = tarFileSet.getFullpath(getProject());
            if (fullpath.isEmpty()) {
                if (vPath.isEmpty()) {
                    return;
                }
                String prefix = tarFileSet.getPrefix(getProject());
                if (!prefix.isEmpty() && !prefix.endsWith("/")) {
                    prefix = prefix + "/";
                }
                vPath = prefix + vPath;
            } else {
                vPath = fullpath;
            }
            preserveLeadingSlashes = tarFileSet.getPreserveLeadingSlashes();
            if (vPath.startsWith("/") && !preserveLeadingSlashes) {
                int l = vPath.length();
                if (l <= 1) {
                    return;
                }
                vPath = vPath.substring(1, l);
            }
        }
        if (r.isDirectory() && !vPath.endsWith("/")) {
            vPath = vPath + "/";
        }
        if (vPath.length() >= 100) {
            if (this.longFileMode.isOmitMode()) {
                log("Omitting: " + vPath, 2);
                return;
            } else if (this.longFileMode.isWarnMode()) {
                log("Entry: " + vPath + " longer than 100 characters.", 1);
                if (!this.longWarningGiven) {
                    log("Resulting tar file can only be processed successfully by GNU compatible tar commands", 1);
                    this.longWarningGiven = true;
                }
            } else if (this.longFileMode.isFailMode()) {
                throw new BuildException("Entry: " + vPath + " longer than 100characters.", getLocation());
            }
        }
        TarEntry te = new TarEntry(vPath, preserveLeadingSlashes);
        te.setModTime(r.getLastModified());
        if (r instanceof ArchiveResource) {
            ArchiveResource ar = (ArchiveResource) r;
            te.setMode(ar.getMode());
            if (r instanceof TarResource) {
                TarResource tr = (TarResource) r;
                te.setUserName(tr.getUserName());
                te.setUserId(tr.getLongUid());
                te.setGroupName(tr.getGroup());
                te.setGroupId(tr.getLongGid());
            }
        }
        if (!r.isDirectory()) {
            if (r.size() > TarConstants.MAXSIZE) {
                throw new BuildException("Resource: " + r + " larger than " + TarConstants.MAXSIZE + " bytes.");
            }
            te.setSize(r.getSize());
            if (tarFileSet != null && tarFileSet.hasFileModeBeenSet()) {
                te.setMode(tarFileSet.getMode());
            }
        } else if (tarFileSet != null && tarFileSet.hasDirModeBeenSet()) {
            te.setMode(tarFileSet.getDirMode(getProject()));
        }
        if (tarFileSet != null) {
            if (tarFileSet.hasUserNameBeenSet()) {
                te.setUserName(tarFileSet.getUserName());
            }
            if (tarFileSet.hasGroupBeenSet()) {
                te.setGroupName(tarFileSet.getGroup());
            }
            if (tarFileSet.hasUserIdBeenSet()) {
                te.setUserId(tarFileSet.getUid());
            }
            if (tarFileSet.hasGroupIdBeenSet()) {
                te.setGroupId(tarFileSet.getGid());
            }
        }
        InputStream in = null;
        try {
            tOut.putNextEntry(te);
            if (!r.isDirectory()) {
                in = r.getInputStream();
                byte[] buffer = new byte[8192];
                int count = 0;
                do {
                    tOut.write(buffer, 0, count);
                    count = in.read(buffer, 0, buffer.length);
                } while (count != -1);
                tOut.closeEntry();
                FileUtils.close(in);
            }
            tOut.closeEntry();
            FileUtils.close(in);
        } catch (Throwable th) {
            FileUtils.close(in);
            throw th;
        }
    }

    @Deprecated
    protected boolean archiveIsUpToDate(String[] files) {
        return archiveIsUpToDate(files, this.baseDir);
    }

    protected boolean archiveIsUpToDate(String[] files, File dir) {
        SourceFileScanner sfs = new SourceFileScanner(this);
        MergingMapper mm = new MergingMapper();
        mm.setTo(this.tarFile.getAbsolutePath());
        return sfs.restrict(files, dir, null, mm).length == 0;
    }

    protected boolean archiveIsUpToDate(Resource r) {
        return SelectorUtils.isOutOfDate(new FileResource(this.tarFile), r, FileUtils.getFileUtils().getFileTimestampGranularity());
    }

    protected boolean supportsNonFileResources() {
        return getClass().equals(Tar.class);
    }

    protected boolean check(ResourceCollection rc) {
        boolean upToDate = true;
        if (isFileFileSet(rc)) {
            FileSet fs = (FileSet) rc;
            upToDate = check(fs.getDir(getProject()), getFileNames(fs));
        } else if (!rc.isFilesystemOnly() && !supportsNonFileResources()) {
            throw new BuildException("only filesystem resources are supported");
        } else {
            if (rc.isFilesystemOnly()) {
                Set<File> basedirs = new HashSet<>();
                Map<File, List<String>> basedirToFilesMap = new HashMap<>();
                for (Resource res : rc) {
                    FileResource r = ResourceUtils.asFileResource((FileProvider) res.as(FileProvider.class));
                    File base = r.getBaseDir();
                    if (base == null) {
                        base = Copy.NULL_FILE_PLACEHOLDER;
                    }
                    basedirs.add(base);
                    List<String> files = basedirToFilesMap.computeIfAbsent(base, k -> {
                        return new Vector();
                    });
                    if (base == Copy.NULL_FILE_PLACEHOLDER) {
                        files.add(r.getFile().getAbsolutePath());
                    } else {
                        files.add(r.getName());
                    }
                }
                Iterator<File> it = basedirs.iterator();
                while (it.hasNext()) {
                    File base2 = it.next();
                    File tmpBase = base2 == Copy.NULL_FILE_PLACEHOLDER ? null : base2;
                    upToDate &= check(tmpBase, basedirToFilesMap.get(base2));
                }
            } else {
                Iterator<Resource> it2 = rc.iterator();
                while (it2.hasNext()) {
                    upToDate = archiveIsUpToDate(it2.next());
                }
            }
        }
        return upToDate;
    }

    protected boolean check(File basedir, String[] files) {
        boolean upToDate = true;
        if (!archiveIsUpToDate(files, basedir)) {
            upToDate = false;
        }
        for (String file : files) {
            if (this.tarFile.equals(new File(basedir, file))) {
                throw new BuildException("A tar file cannot include itself", getLocation());
            }
        }
        return upToDate;
    }

    protected boolean check(File basedir, Collection<String> files) {
        return check(basedir, (String[]) files.toArray(new String[files.size()]));
    }

    protected void tar(ResourceCollection rc, TarOutputStream tOut) throws IOException {
        String[] fileNames;
        ArchiveFileSet afs = null;
        if (rc instanceof ArchiveFileSet) {
            afs = (ArchiveFileSet) rc;
        }
        if (afs != null && afs.size() > 1 && !afs.getFullpath(getProject()).isEmpty()) {
            throw new BuildException("fullpath attribute may only be specified for filesets that specify a single file.");
        }
        TarFileSet tfs = asTarFileSet(afs);
        if (isFileFileSet(rc)) {
            FileSet fs = (FileSet) rc;
            for (String file : getFileNames(fs)) {
                File f = new File(fs.getDir(getProject()), file);
                String name = file.replace(File.separatorChar, '/');
                tarFile(f, tOut, name, tfs);
            }
        } else if (rc.isFilesystemOnly()) {
            Iterator<Resource> it = rc.iterator();
            while (it.hasNext()) {
                File f2 = ((FileProvider) it.next().as(FileProvider.class)).getFile();
                tarFile(f2, tOut, f2.getName(), tfs);
            }
        } else {
            for (Resource r : rc) {
                tarResource(r, tOut, r.getName(), tfs);
            }
        }
    }

    protected static boolean isFileFileSet(ResourceCollection rc) {
        return (rc instanceof FileSet) && rc.isFilesystemOnly();
    }

    protected static String[] getFileNames(FileSet fs) {
        DirectoryScanner ds = fs.getDirectoryScanner(fs.getProject());
        String[] directories = ds.getIncludedDirectories();
        String[] filesPerSe = ds.getIncludedFiles();
        String[] files = new String[directories.length + filesPerSe.length];
        System.arraycopy(directories, 0, files, 0, directories.length);
        System.arraycopy(filesPerSe, 0, files, directories.length, filesPerSe.length);
        return files;
    }

    protected TarFileSet asTarFileSet(ArchiveFileSet archiveFileSet) {
        TarFileSet tfs;
        if (archiveFileSet instanceof TarFileSet) {
            tfs = (TarFileSet) archiveFileSet;
        } else {
            tfs = new TarFileSet();
            tfs.setProject(getProject());
            if (archiveFileSet != null) {
                tfs.setPrefix(archiveFileSet.getPrefix(getProject()));
                tfs.setFullpath(archiveFileSet.getFullpath(getProject()));
                if (archiveFileSet.hasFileModeBeenSet()) {
                    tfs.integerSetFileMode(archiveFileSet.getFileMode(getProject()));
                }
                if (archiveFileSet.hasDirModeBeenSet()) {
                    tfs.integerSetDirMode(archiveFileSet.getDirMode(getProject()));
                }
                if (archiveFileSet instanceof org.apache.tools.ant.types.TarFileSet) {
                    org.apache.tools.ant.types.TarFileSet t = (org.apache.tools.ant.types.TarFileSet) archiveFileSet;
                    if (t.hasUserNameBeenSet()) {
                        tfs.setUserName(t.getUserName());
                    }
                    if (t.hasGroupBeenSet()) {
                        tfs.setGroup(t.getGroup());
                    }
                    if (t.hasUserIdBeenSet()) {
                        tfs.setUid(t.getUid());
                    }
                    if (t.hasGroupIdBeenSet()) {
                        tfs.setGid(t.getGid());
                    }
                }
            }
        }
        return tfs;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Tar$TarFileSet.class */
    public static class TarFileSet extends org.apache.tools.ant.types.TarFileSet {
        private String[] files;
        private boolean preserveLeadingSlashes;

        public TarFileSet(FileSet fileset) {
            super(fileset);
            this.files = null;
            this.preserveLeadingSlashes = false;
        }

        public TarFileSet() {
            this.files = null;
            this.preserveLeadingSlashes = false;
        }

        public String[] getFiles(Project p) {
            if (this.files == null) {
                this.files = Tar.getFileNames(this);
            }
            return this.files;
        }

        public void setMode(String octalString) {
            setFileMode(octalString);
        }

        public int getMode() {
            return getFileMode(getProject());
        }

        public void setPreserveLeadingSlashes(boolean b) {
            this.preserveLeadingSlashes = b;
        }

        public boolean getPreserveLeadingSlashes() {
            return this.preserveLeadingSlashes;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Tar$TarLongFileMode.class */
    public static class TarLongFileMode extends EnumeratedAttribute {
        public static final String WARN = "warn";
        public static final String FAIL = "fail";
        public static final String TRUNCATE = "truncate";
        public static final String GNU = "gnu";
        public static final String OMIT = "omit";
        public static final String POSIX = "posix";
        private static final String[] VALID_MODES = {"warn", "fail", "truncate", "gnu", POSIX, "omit"};

        public TarLongFileMode() {
            setValue("warn");
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return VALID_MODES;
        }

        public boolean isTruncateMode() {
            return "truncate".equalsIgnoreCase(getValue());
        }

        public boolean isWarnMode() {
            return "warn".equalsIgnoreCase(getValue());
        }

        public boolean isGnuMode() {
            return "gnu".equalsIgnoreCase(getValue());
        }

        public boolean isFailMode() {
            return "fail".equalsIgnoreCase(getValue());
        }

        public boolean isOmitMode() {
            return "omit".equalsIgnoreCase(getValue());
        }

        public boolean isPosixMode() {
            return POSIX.equalsIgnoreCase(getValue());
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Tar$TarCompressionMethod.class */
    public static final class TarCompressionMethod extends EnumeratedAttribute {
        private static final String NONE = "none";
        private static final String GZIP = "gzip";
        private static final String BZIP2 = "bzip2";
        private static final String XZ = "xz";

        public TarCompressionMethod() {
            setValue("none");
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"none", GZIP, BZIP2, XZ};
        }

        /* JADX INFO: Access modifiers changed from: private */
        public OutputStream compress(OutputStream ostream) throws IOException {
            String v = getValue();
            if (GZIP.equals(v)) {
                return new GZIPOutputStream(ostream);
            }
            if (XZ.equals(v)) {
                return newXZOutputStream(ostream);
            }
            if (BZIP2.equals(v)) {
                ostream.write(66);
                ostream.write(90);
                return new CBZip2OutputStream(ostream);
            }
            return ostream;
        }

        private static OutputStream newXZOutputStream(OutputStream ostream) throws BuildException {
            try {
                Class<?> fClazz = Class.forName("org.tukaani.xz.FilterOptions");
                Class<?> oClazz = Class.forName("org.tukaani.xz.LZMA2Options");
                return (OutputStream) Class.forName("org.tukaani.xz.XZOutputStream").asSubclass(OutputStream.class).getConstructor(OutputStream.class, fClazz).newInstance(ostream, oClazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
            } catch (ClassNotFoundException ex) {
                throw new BuildException("xz compression requires the XZ for Java library", ex);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex2) {
                throw new BuildException("failed to create XZOutputStream", ex2);
            }
        }
    }
}
