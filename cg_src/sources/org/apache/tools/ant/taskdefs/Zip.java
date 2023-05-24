package org.apache.tools.ant.taskdefs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.Vector;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.FileScanner;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.types.ZipScanner;
import org.apache.tools.ant.types.resources.ArchiveResource;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.types.resources.ZipResource;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.util.DateUtils;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.ant.util.MergingMapper;
import org.apache.tools.ant.util.ResourceUtils;
import org.apache.tools.zip.Zip64Mode;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipExtraField;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Zip.class */
public class Zip extends MatchingTask {
    private static final int BUFFER_SIZE = 8192;
    private static final int ZIP_FILE_TIMESTAMP_GRANULARITY = 2000;
    private static final int ROUNDUP_MILLIS = 1999;
    protected File zipFile;
    private ZipScanner zs;
    private File baseDir;
    private String encoding;
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private static final long EMPTY_CRC = new CRC32().getValue();
    private static final ResourceSelector MISSING_SELECTOR = target -> {
        return !target.isExists();
    };
    private static final ResourceUtils.ResourceSelectorProvider MISSING_DIR_PROVIDER = sr -> {
        return MISSING_SELECTOR;
    };
    private static final ThreadLocal<Boolean> HAVE_NON_FILE_SET_RESOURCES_TO_ADD = ThreadLocal.withInitial(() -> {
        return Boolean.FALSE;
    });
    private static final ThreadLocal<ZipExtraField[]> CURRENT_ZIP_EXTRA = new ThreadLocal<>();
    protected Hashtable<String, String> entries = new Hashtable<>();
    private final List<FileSet> groupfilesets = new Vector();
    private final List<ZipFileSet> filesetsFromGroupfilesets = new Vector();
    protected String duplicate = "add";
    private boolean doCompress = true;
    private boolean doUpdate = false;
    private boolean savedDoUpdate = false;
    private boolean doFilesonly = false;
    protected String archiveType = "zip";
    protected String emptyBehavior = MSVSSConstants.WRITABLE_SKIP;
    private final List<ResourceCollection> resources = new Vector();
    protected Hashtable<String, String> addedDirs = new Hashtable<>();
    private final List<String> addedFiles = new Vector();
    private String fixedModTime = null;
    private long modTimeMillis = 0;
    protected boolean doubleFilePass = false;
    protected boolean skipWriting = false;
    private boolean updatedFile = false;
    private boolean addingNewFiles = false;
    private boolean keepCompression = false;
    private boolean roundUp = true;
    private String comment = "";
    private int level = -1;
    private boolean preserve0Permissions = false;
    private boolean useLanguageEncodingFlag = true;
    private UnicodeExtraField createUnicodeExtraFields = UnicodeExtraField.NEVER;
    private boolean fallBackToUTF8 = false;
    private Zip64ModeAttribute zip64Mode = Zip64ModeAttribute.AS_NEEDED;

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean isFirstPass() {
        return !this.doubleFilePass || this.skipWriting;
    }

    @Deprecated
    public void setZipfile(File zipFile) {
        setDestFile(zipFile);
    }

    @Deprecated
    public void setFile(File file) {
        setDestFile(file);
    }

    public void setDestFile(File destFile) {
        this.zipFile = destFile;
    }

    public File getDestFile() {
        return this.zipFile;
    }

    public void setBasedir(File baseDir) {
        this.baseDir = baseDir;
    }

    public void setCompress(boolean c) {
        this.doCompress = c;
    }

    public boolean isCompress() {
        return this.doCompress;
    }

    public void setFilesonly(boolean f) {
        this.doFilesonly = f;
    }

    public void setUpdate(boolean c) {
        this.doUpdate = c;
        this.savedDoUpdate = c;
    }

    public boolean isInUpdateMode() {
        return this.doUpdate;
    }

    public void addFileset(FileSet set) {
        add(set);
    }

    public void addZipfileset(ZipFileSet set) {
        add(set);
    }

    public void add(ResourceCollection a) {
        this.resources.add(a);
    }

    public void addZipGroupFileset(FileSet set) {
        this.groupfilesets.add(set);
    }

    public void setDuplicate(Duplicate df) {
        this.duplicate = df.getValue();
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Zip$WhenEmpty.class */
    public static class WhenEmpty extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"fail", MSVSSConstants.WRITABLE_SKIP, "create"};
        }
    }

    public void setWhenempty(WhenEmpty we) {
        this.emptyBehavior = we.getValue();
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setKeepCompression(boolean keep) {
        this.keepCompression = keep;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return this.comment;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }

    public void setRoundUp(boolean r) {
        this.roundUp = r;
    }

    public void setPreserve0Permissions(boolean b) {
        this.preserve0Permissions = b;
    }

    public boolean getPreserve0Permissions() {
        return this.preserve0Permissions;
    }

    public void setUseLanguageEncodingFlag(boolean b) {
        this.useLanguageEncodingFlag = b;
    }

    public boolean getUseLanguageEnodingFlag() {
        return this.useLanguageEncodingFlag;
    }

    public void setCreateUnicodeExtraFields(UnicodeExtraField b) {
        this.createUnicodeExtraFields = b;
    }

    public UnicodeExtraField getCreateUnicodeExtraFields() {
        return this.createUnicodeExtraFields;
    }

    public void setFallBackToUTF8(boolean b) {
        this.fallBackToUTF8 = b;
    }

    public boolean getFallBackToUTF8() {
        return this.fallBackToUTF8;
    }

    public void setZip64Mode(Zip64ModeAttribute b) {
        this.zip64Mode = b;
    }

    public Zip64ModeAttribute getZip64Mode() {
        return this.zip64Mode;
    }

    public void setModificationtime(String time) {
        this.fixedModTime = time;
    }

    public String getModificationtime() {
        return this.fixedModTime;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.doubleFilePass) {
            this.skipWriting = true;
            executeMain();
            this.skipWriting = false;
        }
        executeMain();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasUpdatedFile() {
        return this.updatedFile;
    }

    /* JADX WARN: Finally extract failed */
    public void executeMain() throws BuildException {
        checkAttributesAndElements();
        this.addingNewFiles = true;
        processDoUpdate();
        processGroupFilesets();
        List<ResourceCollection> vfss = new ArrayList<>();
        if (this.baseDir != null) {
            FileSet fs = (FileSet) getImplicitFileSet().clone();
            fs.setDir(this.baseDir);
            vfss.add(fs);
        }
        vfss.addAll(this.resources);
        ResourceCollection[] fss = (ResourceCollection[]) vfss.toArray(new ResourceCollection[vfss.size()]);
        try {
            try {
                ArchiveState state = getResourcesToAdd(fss, this.zipFile, false);
                if (state.isOutOfDate()) {
                    File parent = this.zipFile.getParentFile();
                    if (parent != null && !parent.isDirectory() && !parent.mkdirs() && !parent.isDirectory()) {
                        throw new BuildException("Failed to create missing parent directory for %s", this.zipFile);
                    }
                    this.updatedFile = true;
                    if (!this.zipFile.exists() && state.isWithoutAnyResources()) {
                        createEmptyZip(this.zipFile);
                        cleanUp();
                        return;
                    }
                    Resource[][] addThem = state.getResourcesToAdd();
                    renamedFile = this.doUpdate ? renameFile() : null;
                    String action = this.doUpdate ? "Updating " : "Building ";
                    if (!this.skipWriting) {
                        log(action + this.archiveType + ": " + this.zipFile.getAbsolutePath());
                    }
                    ZipOutputStream zOut = null;
                    try {
                        if (!this.skipWriting) {
                            zOut = new ZipOutputStream(this.zipFile);
                            zOut.setEncoding(this.encoding);
                            zOut.setUseLanguageEncodingFlag(this.useLanguageEncodingFlag);
                            zOut.setCreateUnicodeExtraFields(this.createUnicodeExtraFields.getPolicy());
                            zOut.setFallbackToUTF8(this.fallBackToUTF8);
                            zOut.setMethod(this.doCompress ? 8 : 0);
                            zOut.setLevel(this.level);
                            zOut.setUseZip64(this.zip64Mode.getMode());
                        }
                        initZipOutputStream(zOut);
                        for (int i = 0; i < fss.length; i++) {
                            if (addThem[i].length != 0) {
                                addResources(fss[i], addThem[i], zOut);
                            }
                        }
                        if (this.doUpdate) {
                            this.addingNewFiles = false;
                            ZipFileSet oldFiles = new ZipFileSet();
                            oldFiles.setProject(getProject());
                            oldFiles.setSrc(renamedFile);
                            oldFiles.setDefaultexcludes(false);
                            for (String addedFile : this.addedFiles) {
                                oldFiles.createExclude().setName(addedFile);
                            }
                            DirectoryScanner ds = oldFiles.getDirectoryScanner(getProject());
                            ((ZipScanner) ds).setEncoding(this.encoding);
                            Stream<String> includedResourceNames = Stream.of((Object[]) ds.getIncludedFiles());
                            if (!this.doFilesonly) {
                                includedResourceNames = Stream.concat(includedResourceNames, Stream.of((Object[]) ds.getIncludedDirectories()));
                            }
                            Objects.requireNonNull(ds);
                            Resource[] r = (Resource[]) includedResourceNames.map(this::getResource).toArray(x$0 -> {
                                return new Resource[x$0];
                            });
                            addResources((FileSet) oldFiles, r, zOut);
                        }
                        if (zOut != null) {
                            zOut.setComment(this.comment);
                        }
                        finalizeZipOutputStream(zOut);
                        if (this.doUpdate && !renamedFile.delete()) {
                            log("Warning: unable to delete temporary file " + renamedFile.getName(), 1);
                        }
                        closeZout(zOut, true);
                        cleanUp();
                    } catch (Throwable th) {
                        closeZout(zOut, false);
                        throw th;
                    }
                }
            } catch (IOException ioe) {
                String msg = "Problem creating " + this.archiveType + ": " + ioe.getMessage();
                if ((!this.doUpdate || 0 != 0) && !this.zipFile.delete()) {
                    msg = msg + " (and the archive is probably corrupt but I could not delete it)";
                }
                if (this.doUpdate && 0 != 0) {
                    try {
                        FILE_UTILS.rename(null, this.zipFile);
                    } catch (IOException e) {
                        msg = msg + " (and I couldn't rename the temporary file " + renamedFile.getName() + " back)";
                    }
                }
                throw new BuildException(msg, ioe, getLocation());
            }
        } finally {
            cleanUp();
        }
    }

    private File renameFile() {
        File renamedFile = FILE_UTILS.createTempFile(getProject(), "zip", ".tmp", this.zipFile.getParentFile(), true, false);
        try {
            FILE_UTILS.rename(this.zipFile, renamedFile);
            return renamedFile;
        } catch (IOException | SecurityException e) {
            throw new BuildException("Unable to rename old file (%s) to temporary file", this.zipFile.getAbsolutePath());
        }
    }

    private void closeZout(ZipOutputStream zOut, boolean success) throws IOException {
        if (zOut == null) {
            return;
        }
        try {
            zOut.close();
        } catch (IOException ex) {
            if (success) {
                throw ex;
            }
        }
    }

    private void checkAttributesAndElements() {
        if (this.baseDir == null && this.resources.isEmpty() && this.groupfilesets.isEmpty() && "zip".equals(this.archiveType)) {
            throw new BuildException("basedir attribute must be set, or at least one resource collection must be given!");
        }
        if (this.zipFile == null) {
            throw new BuildException("You must specify the %s file to create!", this.archiveType);
        }
        if (this.fixedModTime != null) {
            try {
                this.modTimeMillis = DateUtils.parseLenientDateTime(this.fixedModTime).getTime();
                if (this.roundUp) {
                    this.modTimeMillis += 1999;
                }
            } catch (ParseException e) {
                throw new BuildException("Failed to parse date string %s.", this.fixedModTime);
            }
        }
        if (this.zipFile.exists() && !this.zipFile.isFile()) {
            throw new BuildException("%s is not a file.", this.zipFile);
        }
        if (this.zipFile.exists() && !this.zipFile.canWrite()) {
            throw new BuildException("%s is read-only.", this.zipFile);
        }
    }

    private void processDoUpdate() {
        if (this.doUpdate && !this.zipFile.exists()) {
            this.doUpdate = false;
            logWhenWriting("ignoring update attribute as " + this.archiveType + " doesn't exist.", 4);
        }
    }

    private void processGroupFilesets() {
        String[] includedFiles;
        for (FileSet fs : this.groupfilesets) {
            logWhenWriting("Processing groupfileset ", 3);
            FileScanner scanner = fs.getDirectoryScanner(getProject());
            File basedir = scanner.getBasedir();
            for (String file : scanner.getIncludedFiles()) {
                logWhenWriting("Adding file " + file + " to fileset", 3);
                ZipFileSet zf = new ZipFileSet();
                zf.setProject(getProject());
                zf.setSrc(new File(basedir, file));
                add(zf);
                this.filesetsFromGroupfilesets.add(zf);
            }
        }
    }

    protected final boolean isAddingNewFiles() {
        return this.addingNewFiles;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x011d A[Catch: all -> 0x0228, TryCatch #0 {all -> 0x0228, blocks: (B:28:0x00d1, B:31:0x00ed, B:33:0x00f5, B:34:0x010b, B:37:0x011d, B:39:0x012c, B:41:0x013a, B:70:0x0215, B:44:0x0151, B:46:0x0159, B:51:0x0168, B:55:0x017f, B:54:0x0175, B:56:0x0195, B:58:0x01a7, B:61:0x01db, B:69:0x0212, B:68:0x0209, B:64:0x01e8, B:30:0x00dd), top: B:81:0x00d1 }] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0220 A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0237 A[ORIG_RETURN, RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final void addResources(org.apache.tools.ant.types.FileSet r11, org.apache.tools.ant.types.Resource[] r12, org.apache.tools.zip.ZipOutputStream r13) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 568
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.tools.ant.taskdefs.Zip.addResources(org.apache.tools.ant.types.FileSet, org.apache.tools.ant.types.Resource[], org.apache.tools.zip.ZipOutputStream):void");
    }

    private void addDirectoryResource(Resource r, String name, String prefix, File base, ZipOutputStream zOut, int defaultDirMode, int thisDirMode) throws IOException {
        if (!name.endsWith("/")) {
            name = name + "/";
        }
        int nextToLastSlash = name.lastIndexOf(47, name.length() - 2);
        if (nextToLastSlash != -1) {
            addParentDirs(base, name.substring(0, nextToLastSlash + 1), zOut, prefix, defaultDirMode);
        }
        zipDir(r, zOut, prefix + name, thisDirMode, r instanceof ZipResource ? ((ZipResource) r).getExtraFields() : null);
    }

    private int getUnixMode(Resource r, ZipFile zf, int defaultMode) {
        int unixMode = defaultMode;
        if (zf != null) {
            ZipEntry ze = zf.getEntry(r.getName());
            unixMode = ze.getUnixMode();
            if ((unixMode == 0 || unixMode == 16384) && !this.preserve0Permissions) {
                unixMode = defaultMode;
            }
        } else if (r instanceof ArchiveResource) {
            unixMode = ((ArchiveResource) r).getMode();
        }
        return unixMode;
    }

    private void addResource(Resource r, String name, String prefix, ZipOutputStream zOut, int mode, ZipFile zf, File fromArchive) throws IOException {
        if (zf != null) {
            ZipEntry ze = zf.getEntry(r.getName());
            if (ze != null) {
                boolean oldCompress = this.doCompress;
                if (this.keepCompression) {
                    this.doCompress = ze.getMethod() == 8;
                }
                try {
                    BufferedInputStream is = new BufferedInputStream(zf.getInputStream(ze));
                    zipFile(is, zOut, prefix + name, ze.getTime(), fromArchive, mode, ze.getExtraFields(true));
                    is.close();
                    return;
                } finally {
                    this.doCompress = oldCompress;
                }
            }
            return;
        }
        BufferedInputStream is2 = new BufferedInputStream(r.getInputStream());
        try {
            zipFile(is2, zOut, prefix + name, r.getLastModified(), fromArchive, mode, r instanceof ZipResource ? ((ZipResource) r).getExtraFields() : null);
            is2.close();
        } catch (Throwable th) {
            try {
                is2.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    protected final void addResources(ResourceCollection rc, Resource[] resources, ZipOutputStream zOut) throws IOException {
        if (rc instanceof FileSet) {
            addResources((FileSet) rc, resources, zOut);
            return;
        }
        for (Resource resource : resources) {
            String name = resource.getName();
            if (name != null) {
                String name2 = name.replace(File.separatorChar, '/');
                if (!name2.isEmpty() && (!resource.isDirectory() || !this.doFilesonly)) {
                    File base = null;
                    FileProvider fp = (FileProvider) resource.as(FileProvider.class);
                    if (fp != null) {
                        base = ResourceUtils.asFileResource(fp).getBaseDir();
                    }
                    if (resource.isDirectory()) {
                        addDirectoryResource(resource, name2, "", base, zOut, 16877, 16877);
                    } else {
                        addParentDirs(base, name2, zOut, "", 16877);
                        if (fp != null) {
                            File f = fp.getFile();
                            zipFile(f, zOut, name2, 33188);
                        } else {
                            addResource(resource, name2, "", zOut, 33188, null, null);
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
    }

    protected void finalizeZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
    }

    protected boolean createEmptyZip(File zipFile) throws BuildException {
        if (!this.skipWriting) {
            log("Note: creating empty " + this.archiveType + " archive " + zipFile, 2);
        }
        try {
            OutputStream os = Files.newOutputStream(zipFile.toPath(), new OpenOption[0]);
            byte[] empty = new byte[22];
            empty[0] = 80;
            empty[1] = 75;
            empty[2] = 5;
            empty[3] = 6;
            os.write(empty);
            if (os != null) {
                os.close();
            }
            return true;
        } catch (IOException ioe) {
            throw new BuildException("Could not create empty ZIP archive (" + ioe.getMessage() + ")", ioe, getLocation());
        }
    }

    private synchronized ZipScanner getZipScanner() {
        if (this.zs == null) {
            this.zs = new ZipScanner();
            this.zs.setEncoding(this.encoding);
            this.zs.setSrc(this.zipFile);
        }
        return this.zs;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r0v21, types: [org.apache.tools.ant.types.Resource[], org.apache.tools.ant.types.Resource[][]] */
    public ArchiveState getResourcesToAdd(ResourceCollection[] rcs, File zipFile, boolean needsUpdate) throws BuildException {
        List<FileSet> filesets = new ArrayList<>();
        List<ResourceCollection> rest = new ArrayList<>();
        for (ResourceCollection rc : rcs) {
            if (rc instanceof FileSet) {
                filesets.add((FileSet) rc);
            } else {
                rest.add(rc);
            }
        }
        ResourceCollection[] rc2 = (ResourceCollection[]) rest.toArray(new ResourceCollection[rest.size()]);
        ArchiveState as = getNonFileSetResourcesToAdd(rc2, zipFile, needsUpdate);
        FileSet[] fs = (FileSet[]) filesets.toArray(new FileSet[filesets.size()]);
        ArchiveState as2 = getResourcesToAdd(fs, zipFile, as.isOutOfDate());
        if (!as.isOutOfDate() && as2.isOutOfDate()) {
            as = getNonFileSetResourcesToAdd(rc2, zipFile, true);
        }
        ?? r0 = new Resource[rcs.length];
        int fsIndex = 0;
        int restIndex = 0;
        for (int i = 0; i < rcs.length; i++) {
            if (rcs[i] instanceof FileSet) {
                int i2 = fsIndex;
                fsIndex++;
                r0[i] = as2.getResourcesToAdd()[i2];
            } else {
                int i3 = restIndex;
                restIndex++;
                r0[i] = as.getResourcesToAdd()[i3];
            }
        }
        return new ArchiveState(as2.isOutOfDate(), r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v9, types: [org.apache.tools.ant.types.Resource[], org.apache.tools.ant.types.Resource[][]] */
    protected ArchiveState getResourcesToAdd(FileSet[] filesets, File zipFile, boolean needsUpdate) throws BuildException {
        Resource[][] initialResources = grabResources(filesets);
        if (isEmpty(initialResources)) {
            if (Boolean.FALSE.equals(HAVE_NON_FILE_SET_RESOURCES_TO_ADD.get())) {
                if (needsUpdate && this.doUpdate) {
                    return new ArchiveState(true, initialResources);
                }
                if (MSVSSConstants.WRITABLE_SKIP.equals(this.emptyBehavior)) {
                    if (this.doUpdate) {
                        logWhenWriting(this.archiveType + " archive " + zipFile + " not updated because no new files were included.", 3);
                    } else {
                        logWhenWriting("Warning: skipping " + this.archiveType + " archive " + zipFile + " because no files were included.", 1);
                    }
                } else if ("fail".equals(this.emptyBehavior)) {
                    throw new BuildException("Cannot create " + this.archiveType + " archive " + zipFile + ": no files were included.", getLocation());
                } else {
                    if (!zipFile.exists()) {
                        needsUpdate = true;
                    }
                }
            }
            return new ArchiveState(needsUpdate, initialResources);
        } else if (!zipFile.exists()) {
            return new ArchiveState(true, initialResources);
        } else {
            if (needsUpdate && !this.doUpdate) {
                return new ArchiveState(true, initialResources);
            }
            ?? r0 = new Resource[filesets.length];
            for (int i = 0; i < filesets.length; i++) {
                if (!(this.fileset instanceof ZipFileSet) || ((ZipFileSet) this.fileset).getSrc(getProject()) == null) {
                    File base = filesets[i].getDir(getProject());
                    for (int j = 0; j < initialResources[i].length; j++) {
                        File resourceAsFile = FILE_UTILS.resolveFile(base, initialResources[i][j].getName());
                        if (resourceAsFile.equals(zipFile)) {
                            throw new BuildException("A zip file cannot include itself", getLocation());
                        }
                    }
                    continue;
                }
            }
            for (int i2 = 0; i2 < filesets.length; i2++) {
                if (initialResources[i2].length == 0) {
                    r0[i2] = new Resource[0];
                } else {
                    FileNameMapper identityMapper = new IdentityMapper();
                    FileNameMapper myMapper = identityMapper;
                    if (filesets[i2] instanceof ZipFileSet) {
                        ZipFileSet zfs = (ZipFileSet) filesets[i2];
                        if (zfs.getFullpath(getProject()) == null || zfs.getFullpath(getProject()).isEmpty()) {
                            myMapper = identityMapper;
                            if (zfs.getPrefix(getProject()) != null) {
                                myMapper = identityMapper;
                                if (!zfs.getPrefix(getProject()).isEmpty()) {
                                    FileNameMapper gm = new GlobPatternMapper();
                                    gm.setFrom("*");
                                    String prefix = zfs.getPrefix(getProject());
                                    if (!prefix.endsWith("/") && !prefix.endsWith("\\")) {
                                        prefix = prefix + "/";
                                    }
                                    gm.setTo(prefix + "*");
                                    myMapper = gm;
                                }
                            }
                        } else {
                            FileNameMapper fm = new MergingMapper();
                            fm.setTo(zfs.getFullpath(getProject()));
                            myMapper = fm;
                        }
                    }
                    r0[i2] = selectOutOfDateResources(initialResources[i2], myMapper);
                    needsUpdate = needsUpdate || r0[i2].length > 0;
                    if (needsUpdate && !this.doUpdate) {
                        break;
                    }
                }
            }
            if (needsUpdate && !this.doUpdate) {
                return new ArchiveState(true, initialResources);
            }
            return new ArchiveState(needsUpdate, r0);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11, types: [org.apache.tools.ant.types.Resource[], org.apache.tools.ant.types.Resource[][]] */
    protected ArchiveState getNonFileSetResourcesToAdd(ResourceCollection[] rcs, File zipFile, boolean needsUpdate) throws BuildException {
        Resource[][] initialResources = grabNonFileSetResources(rcs);
        boolean empty = isEmpty(initialResources);
        HAVE_NON_FILE_SET_RESOURCES_TO_ADD.set(Boolean.valueOf(!empty));
        if (empty) {
            return new ArchiveState(needsUpdate, initialResources);
        }
        if (!zipFile.exists()) {
            return new ArchiveState(true, initialResources);
        }
        if (needsUpdate && !this.doUpdate) {
            return new ArchiveState(true, initialResources);
        }
        ?? r0 = new Resource[rcs.length];
        for (int i = 0; i < rcs.length; i++) {
            if (initialResources[i].length == 0) {
                r0[i] = new Resource[0];
            } else {
                for (int j = 0; j < initialResources[i].length; j++) {
                    FileProvider fp = (FileProvider) initialResources[i][j].as(FileProvider.class);
                    if (fp != null && zipFile.equals(fp.getFile())) {
                        throw new BuildException("A zip file cannot include itself", getLocation());
                    }
                }
                r0[i] = selectOutOfDateResources(initialResources[i], new IdentityMapper());
                needsUpdate = needsUpdate || r0[i].length > 0;
                if (needsUpdate && !this.doUpdate) {
                    break;
                }
            }
        }
        if (needsUpdate && !this.doUpdate) {
            return new ArchiveState(true, initialResources);
        }
        return new ArchiveState(needsUpdate, r0);
    }

    private Resource[] selectOutOfDateResources(Resource[] initial, FileNameMapper mapper) {
        Resource[] rs = selectFileResources(initial);
        Resource[] result = ResourceUtils.selectOutOfDateSources(this, rs, mapper, getZipScanner(), (long) FileUtils.FAT_FILE_TIMESTAMP_GRANULARITY);
        if (!this.doFilesonly) {
            Union u = new Union();
            u.addAll(Arrays.asList(selectDirectoryResources(initial)));
            ResourceCollection rc = ResourceUtils.selectSources(this, u, mapper, getZipScanner(), MISSING_DIR_PROVIDER);
            if (!rc.isEmpty()) {
                List<Resource> newer = new ArrayList<>();
                newer.addAll(Arrays.asList(((Union) rc).listResources()));
                newer.addAll(Arrays.asList(result));
                result = (Resource[]) newer.toArray(result);
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r0v2, types: [org.apache.tools.ant.types.Resource[], org.apache.tools.ant.types.Resource[][]] */
    public Resource[][] grabResources(FileSet[] filesets) {
        String[] includedFiles;
        String[] includedDirectories;
        ?? r0 = new Resource[filesets.length];
        for (int i = 0; i < filesets.length; i++) {
            boolean skipEmptyNames = true;
            if (filesets[i] instanceof ZipFileSet) {
                ZipFileSet zfs = (ZipFileSet) filesets[i];
                skipEmptyNames = zfs.getPrefix(getProject()).isEmpty() && zfs.getFullpath(getProject()).isEmpty();
            }
            DirectoryScanner rs = filesets[i].getDirectoryScanner(getProject());
            if (rs instanceof ZipScanner) {
                ((ZipScanner) rs).setEncoding(this.encoding);
            }
            List<Resource> resources = new Vector<>();
            if (!this.doFilesonly) {
                for (String d : rs.getIncludedDirectories()) {
                    if (!d.isEmpty() || !skipEmptyNames) {
                        resources.add(rs.getResource(d));
                    }
                }
            }
            for (String f : rs.getIncludedFiles()) {
                if (!f.isEmpty() || !skipEmptyNames) {
                    resources.add(rs.getResource(f));
                }
            }
            r0[i] = (Resource[]) resources.toArray(new Resource[resources.size()]);
        }
        return r0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r0v2, types: [org.apache.tools.ant.types.Resource[], org.apache.tools.ant.types.Resource[][]] */
    public Resource[][] grabNonFileSetResources(ResourceCollection[] rcs) {
        ?? r0 = new Resource[rcs.length];
        for (int i = 0; i < rcs.length; i++) {
            List<Resource> dirs = new ArrayList<>();
            ArrayList arrayList = new ArrayList();
            for (Resource r : rcs[i]) {
                if (r.isDirectory()) {
                    dirs.add(r);
                } else if (r.isExists()) {
                    arrayList.add(r);
                }
            }
            dirs.sort(Comparator.comparing((v0) -> {
                return v0.getName();
            }));
            List<Resource> rs = new ArrayList<>(dirs);
            rs.addAll(arrayList);
            r0[i] = (Resource[]) rs.toArray(new Resource[rs.size()]);
        }
        return r0;
    }

    protected void zipDir(File dir, ZipOutputStream zOut, String vPath, int mode) throws IOException {
        zipDir(dir, zOut, vPath, mode, (ZipExtraField[]) null);
    }

    protected void zipDir(File dir, ZipOutputStream zOut, String vPath, int mode, ZipExtraField[] extra) throws IOException {
        zipDir(dir == null ? null : new FileResource(dir), zOut, vPath, mode, extra);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void zipDir(Resource dir, ZipOutputStream zOut, String vPath, int mode, ZipExtraField[] extra) throws IOException {
        if (this.doFilesonly) {
            logWhenWriting("skipping directory " + vPath + " for file-only archive", 3);
        } else if (this.addedDirs.get(vPath) != null) {
        } else {
            logWhenWriting("adding directory " + vPath, 3);
            this.addedDirs.put(vPath, vPath);
            if (!this.skipWriting) {
                ZipEntry ze = new ZipEntry(vPath);
                int millisToAdd = this.roundUp ? 1999 : 0;
                if (this.fixedModTime != null) {
                    ze.setTime(this.modTimeMillis);
                } else if (dir != null && dir.isExists()) {
                    ze.setTime(dir.getLastModified() + millisToAdd);
                } else {
                    ze.setTime(System.currentTimeMillis() + millisToAdd);
                }
                ze.setSize(0L);
                ze.setMethod(0);
                ze.setCrc(EMPTY_CRC);
                ze.setUnixMode(mode);
                if (extra != null) {
                    ze.setExtraFields(extra);
                }
                zOut.putNextEntry(ze);
            }
        }
    }

    protected final ZipExtraField[] getCurrentExtraFields() {
        return CURRENT_ZIP_EXTRA.get();
    }

    protected final void setCurrentExtraFields(ZipExtraField[] extra) {
        CURRENT_ZIP_EXTRA.set(extra);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void zipFile(InputStream in, ZipOutputStream zOut, String vPath, long lastModified, File fromArchive, int mode) throws IOException {
        if (this.entries.containsKey(vPath)) {
            if ("preserve".equals(this.duplicate)) {
                logWhenWriting(vPath + " already added, skipping", 2);
                return;
            } else if ("fail".equals(this.duplicate)) {
                throw new BuildException("Duplicate file %s was found and the duplicate attribute is 'fail'.", vPath);
            } else {
                logWhenWriting("duplicate file " + vPath + " found, adding.", 3);
            }
        } else {
            logWhenWriting("adding entry " + vPath, 3);
        }
        this.entries.put(vPath, vPath);
        if (!this.skipWriting) {
            ZipEntry ze = new ZipEntry(vPath);
            ze.setTime(this.fixedModTime != null ? this.modTimeMillis : lastModified);
            ze.setMethod(this.doCompress ? 8 : 0);
            InputStream markableInputStream = in.markSupported() ? in : new BufferedInputStream(in);
            if (!zOut.isSeekable() && !this.doCompress) {
                long size = 0;
                CRC32 cal = new CRC32();
                markableInputStream.mark(Integer.MAX_VALUE);
                byte[] buffer = new byte[8192];
                int count = 0;
                do {
                    size += count;
                    cal.update(buffer, 0, count);
                    count = markableInputStream.read(buffer, 0, buffer.length);
                } while (count != -1);
                markableInputStream.reset();
                ze.setSize(size);
                ze.setCrc(cal.getValue());
            }
            ze.setUnixMode(mode);
            ZipExtraField[] extra = getCurrentExtraFields();
            if (extra != null) {
                ze.setExtraFields(extra);
            }
            zOut.putNextEntry(ze);
            byte[] buffer2 = new byte[8192];
            int count2 = 0;
            do {
                if (count2 != 0) {
                    zOut.write(buffer2, 0, count2);
                }
                count2 = markableInputStream.read(buffer2, 0, buffer2.length);
            } while (count2 != -1);
            this.addedFiles.add(vPath);
        }
        this.addedFiles.add(vPath);
    }

    protected final void zipFile(InputStream in, ZipOutputStream zOut, String vPath, long lastModified, File fromArchive, int mode, ZipExtraField[] extra) throws IOException {
        try {
            setCurrentExtraFields(extra);
            zipFile(in, zOut, vPath, lastModified, fromArchive, mode);
            setCurrentExtraFields(null);
        } catch (Throwable th) {
            setCurrentExtraFields(null);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void zipFile(File file, ZipOutputStream zOut, String vPath, int mode) throws IOException {
        if (file.equals(this.zipFile)) {
            throw new BuildException("A zip file cannot include itself", getLocation());
        }
        BufferedInputStream bIn = new BufferedInputStream(Files.newInputStream(file.toPath(), new OpenOption[0]));
        try {
            zipFile(bIn, zOut, vPath, file.lastModified() + (this.roundUp ? 1999 : 0), null, mode);
            bIn.close();
        } catch (Throwable th) {
            try {
                bIn.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    protected final void addParentDirs(File baseDir, String entry, ZipOutputStream zOut, String prefix, int dirMode) throws IOException {
        File file;
        if (!this.doFilesonly) {
            Stack<String> directories = new Stack<>();
            int slashPos = entry.length();
            while (true) {
                int lastIndexOf = entry.lastIndexOf(47, slashPos - 1);
                slashPos = lastIndexOf;
                if (lastIndexOf == -1) {
                    break;
                }
                String dir = entry.substring(0, slashPos + 1);
                if (this.addedDirs.get(prefix + dir) != null) {
                    break;
                }
                directories.push(dir);
            }
            while (!directories.isEmpty()) {
                String dir2 = directories.pop();
                if (baseDir != null) {
                    file = new File(baseDir, dir2);
                } else {
                    file = new File(dir2);
                }
                File f = file;
                zipDir(f, zOut, prefix + dir2, dirMode);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void cleanUp() {
        this.addedDirs.clear();
        this.addedFiles.clear();
        this.entries.clear();
        this.addingNewFiles = false;
        this.doUpdate = this.savedDoUpdate;
        this.resources.removeAll(this.filesetsFromGroupfilesets);
        this.filesetsFromGroupfilesets.clear();
        HAVE_NON_FILE_SET_RESOURCES_TO_ADD.set(Boolean.FALSE);
    }

    public void reset() {
        this.resources.clear();
        this.zipFile = null;
        this.baseDir = null;
        this.groupfilesets.clear();
        this.duplicate = "add";
        this.archiveType = "zip";
        this.doCompress = true;
        this.emptyBehavior = MSVSSConstants.WRITABLE_SKIP;
        this.doUpdate = false;
        this.doFilesonly = false;
        this.encoding = null;
    }

    protected static final boolean isEmpty(Resource[][] r) {
        for (Resource[] element : r) {
            if (element.length > 0) {
                return false;
            }
        }
        return true;
    }

    protected Resource[] selectFileResources(Resource[] orig) {
        return selectResources(orig, r -> {
            if (!r.isDirectory()) {
                return true;
            }
            if (this.doFilesonly) {
                logWhenWriting("Ignoring directory " + r.getName() + " as only files will be added.", 3);
                return false;
            }
            return false;
        });
    }

    protected Resource[] selectDirectoryResources(Resource[] orig) {
        return selectResources(orig, (v0) -> {
            return v0.isDirectory();
        });
    }

    protected Resource[] selectResources(Resource[] orig, ResourceSelector selector) {
        if (orig.length == 0) {
            return orig;
        }
        Stream of = Stream.of((Object[]) orig);
        Objects.requireNonNull(selector);
        Resource[] result = (Resource[]) of.filter(this::isSelected).toArray(x$0 -> {
            return new Resource[x$0];
        });
        return result.length == orig.length ? orig : result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void logWhenWriting(String msg, int level) {
        if (!this.skipWriting) {
            log(msg, level);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Zip$Duplicate.class */
    public static class Duplicate extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"add", "preserve", "fail"};
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Zip$ArchiveState.class */
    public static class ArchiveState {
        private final boolean outOfDate;
        private final Resource[][] resourcesToAdd;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ArchiveState(boolean state, Resource[][] r) {
            this.outOfDate = state;
            this.resourcesToAdd = r;
        }

        public boolean isOutOfDate() {
            return this.outOfDate;
        }

        public Resource[][] getResourcesToAdd() {
            return this.resourcesToAdd;
        }

        public boolean isWithoutAnyResources() {
            Resource[][] resourceArr;
            if (this.resourcesToAdd == null) {
                return true;
            }
            for (Resource[] element : this.resourcesToAdd) {
                if (element != null && element.length > 0) {
                    return false;
                }
            }
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Zip$UnicodeExtraField.class */
    public static final class UnicodeExtraField extends EnumeratedAttribute {
        private static final Map<String, ZipOutputStream.UnicodeExtraFieldPolicy> POLICIES = new HashMap();
        private static final String NEVER_KEY = "never";
        private static final String ALWAYS_KEY = "always";
        private static final String N_E_KEY = "not-encodeable";
        public static final UnicodeExtraField NEVER;

        static {
            POLICIES.put(NEVER_KEY, ZipOutputStream.UnicodeExtraFieldPolicy.NEVER);
            POLICIES.put(ALWAYS_KEY, ZipOutputStream.UnicodeExtraFieldPolicy.ALWAYS);
            POLICIES.put(N_E_KEY, ZipOutputStream.UnicodeExtraFieldPolicy.NOT_ENCODEABLE);
            NEVER = new UnicodeExtraField(NEVER_KEY);
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{NEVER_KEY, ALWAYS_KEY, N_E_KEY};
        }

        private UnicodeExtraField(String name) {
            setValue(name);
        }

        public UnicodeExtraField() {
        }

        public ZipOutputStream.UnicodeExtraFieldPolicy getPolicy() {
            return POLICIES.get(getValue());
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Zip$Zip64ModeAttribute.class */
    public static final class Zip64ModeAttribute extends EnumeratedAttribute {
        private static final Map<String, Zip64Mode> MODES = new HashMap();
        private static final String NEVER_KEY = "never";
        private static final String ALWAYS_KEY = "always";
        private static final String A_N_KEY = "as-needed";
        public static final Zip64ModeAttribute NEVER;
        public static final Zip64ModeAttribute AS_NEEDED;

        static {
            MODES.put(NEVER_KEY, Zip64Mode.Never);
            MODES.put(ALWAYS_KEY, Zip64Mode.Always);
            MODES.put(A_N_KEY, Zip64Mode.AsNeeded);
            NEVER = new Zip64ModeAttribute(NEVER_KEY);
            AS_NEEDED = new Zip64ModeAttribute(A_N_KEY);
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{NEVER_KEY, ALWAYS_KEY, A_N_KEY};
        }

        private Zip64ModeAttribute(String name) {
            setValue(name);
        }

        public Zip64ModeAttribute() {
        }

        public Zip64Mode getMode() {
            return MODES.get(getValue());
        }
    }
}
