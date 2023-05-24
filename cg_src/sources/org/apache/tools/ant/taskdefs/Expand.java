package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Expand.class */
public class Expand extends Task {
    public static final String NATIVE_ENCODING = "native-encoding";
    public static final String ERROR_MULTIPLE_MAPPERS = "Cannot define more than one mapper";
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private static final int BUFFER_SIZE = 1024;
    private File dest;
    private File source;
    private boolean overwrite;
    private Mapper mapperElement;
    private List<PatternSet> patternsets;
    private Union resources;
    private boolean resourcesSpecified;
    private boolean failOnEmptyArchive;
    private boolean stripAbsolutePathSpec;
    private boolean scanForUnicodeExtraFields;
    private Boolean allowFilesToEscapeDest;
    private String encoding;

    public Expand() {
        this("UTF8");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Expand(String encoding) {
        this.overwrite = true;
        this.mapperElement = null;
        this.patternsets = new Vector();
        this.resources = new Union();
        this.resourcesSpecified = false;
        this.failOnEmptyArchive = false;
        this.stripAbsolutePathSpec = true;
        this.scanForUnicodeExtraFields = true;
        this.allowFilesToEscapeDest = null;
        this.encoding = encoding;
    }

    public void setFailOnEmptyArchive(boolean b) {
        this.failOnEmptyArchive = b;
    }

    public boolean getFailOnEmptyArchive() {
        return this.failOnEmptyArchive;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if ("expand".equals(getTaskType())) {
            log("!! expand is deprecated. Use unzip instead. !!");
        }
        if (this.source == null && !this.resourcesSpecified) {
            throw new BuildException("src attribute and/or resources must be specified");
        }
        if (this.dest == null) {
            throw new BuildException("Dest attribute must be specified");
        }
        if (this.dest.exists() && !this.dest.isDirectory()) {
            throw new BuildException("Dest must be a directory.", getLocation());
        }
        if (this.source != null) {
            if (this.source.isDirectory()) {
                throw new BuildException("Src must not be a directory. Use nested filesets instead.", getLocation());
            }
            if (!this.source.exists()) {
                throw new BuildException("src '" + this.source + "' doesn't exist.");
            }
            if (!this.source.canRead()) {
                throw new BuildException("src '" + this.source + "' cannot be read.");
            }
            expandFile(FILE_UTILS, this.source, this.dest);
        }
        Iterator<Resource> it = this.resources.iterator();
        while (it.hasNext()) {
            Resource r = it.next();
            if (!r.isExists()) {
                log("Skipping '" + r.getName() + "' because it doesn't exist.");
            } else {
                FileProvider fp = (FileProvider) r.as(FileProvider.class);
                if (fp != null) {
                    expandFile(FILE_UTILS, fp.getFile(), this.dest);
                } else {
                    expandResource(r, this.dest);
                }
            }
        }
    }

    protected void expandFile(FileUtils fileUtils, File srcF, File dir) {
        log("Expanding: " + srcF + " into " + dir, 2);
        FileNameMapper mapper = getMapper();
        if (!srcF.exists()) {
            throw new BuildException("Unable to expand " + srcF + " as the file does not exist", getLocation());
        }
        try {
            ZipFile zf = new ZipFile(srcF, this.encoding, this.scanForUnicodeExtraFields);
            boolean empty = true;
            Enumeration<ZipEntry> entries = zf.getEntries();
            while (entries.hasMoreElements()) {
                ZipEntry ze = entries.nextElement();
                empty = false;
                InputStream is = null;
                log("extracting " + ze.getName(), 4);
                try {
                    InputStream inputStream = zf.getInputStream(ze);
                    is = inputStream;
                    extractFile(fileUtils, srcF, dir, inputStream, ze.getName(), new Date(ze.getTime()), ze.isDirectory(), mapper);
                    FileUtils.close(is);
                } catch (Throwable th) {
                    FileUtils.close(is);
                    throw th;
                }
            }
            if (empty && getFailOnEmptyArchive()) {
                throw new BuildException("archive '%s' is empty", srcF);
            }
            log("expand complete", 3);
            zf.close();
        } catch (IOException ioe) {
            throw new BuildException("Error while expanding " + srcF.getPath() + "\n" + ioe.toString(), ioe);
        }
    }

    protected void expandResource(Resource srcR, File dir) {
        throw new BuildException("only filesystem based resources are supported by this task.");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FileNameMapper getMapper() {
        if (this.mapperElement != null) {
            return this.mapperElement.getImplementation();
        }
        return new IdentityMapper();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void extractFile(FileUtils fileUtils, File srcF, File dir, InputStream compressedInputStream, String entryName, Date entryDate, boolean isDirectory, FileNameMapper mapper) throws IOException {
        String[] strArr;
        boolean entryNameStartsWithPathSpec = !entryName.isEmpty() && (entryName.charAt(0) == File.separatorChar || entryName.charAt(0) == '/' || entryName.charAt(0) == '\\');
        if (this.stripAbsolutePathSpec && entryNameStartsWithPathSpec) {
            log("stripped absolute path spec from " + entryName, 3);
            entryName = entryName.substring(1);
        }
        boolean allowedOutsideOfDest = Boolean.TRUE == getAllowFilesToEscapeDest() || (null == getAllowFilesToEscapeDest() && !this.stripAbsolutePathSpec && entryNameStartsWithPathSpec);
        if (this.patternsets != null && !this.patternsets.isEmpty()) {
            String name = entryName.replace('/', File.separatorChar).replace('\\', File.separatorChar);
            Set<String> includePatterns = new HashSet<>();
            Set<String> excludePatterns = new HashSet<>();
            for (PatternSet p : this.patternsets) {
                String[] incls = p.getIncludePatterns(getProject());
                if (incls == null || incls.length == 0) {
                    incls = new String[]{SelectorUtils.DEEP_TREE_MATCH};
                }
                for (String incl : incls) {
                    String pattern = incl.replace('/', File.separatorChar).replace('\\', File.separatorChar);
                    if (pattern.endsWith(File.separator)) {
                        pattern = pattern + SelectorUtils.DEEP_TREE_MATCH;
                    }
                    includePatterns.add(pattern);
                }
                String[] excls = p.getExcludePatterns(getProject());
                if (excls != null) {
                    for (String excl : excls) {
                        String pattern2 = excl.replace('/', File.separatorChar).replace('\\', File.separatorChar);
                        if (pattern2.endsWith(File.separator)) {
                            pattern2 = pattern2 + SelectorUtils.DEEP_TREE_MATCH;
                        }
                        excludePatterns.add(pattern2);
                    }
                }
            }
            boolean included = false;
            Iterator<String> it = includePatterns.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String pattern3 = it.next();
                if (SelectorUtils.matchPath(pattern3, name)) {
                    included = true;
                    break;
                }
            }
            Iterator<String> it2 = excludePatterns.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                String pattern4 = it2.next();
                if (SelectorUtils.matchPath(pattern4, name)) {
                    included = false;
                    break;
                }
            }
            if (!included) {
                log("skipping " + entryName + " as it is excluded or not included.", 3);
                return;
            }
        }
        String[] mappedNames = mapper.mapFileName(entryName);
        if (mappedNames == null || mappedNames.length == 0) {
            mappedNames = new String[]{entryName};
        }
        File f = fileUtils.resolveFile(dir, mappedNames[0]);
        if (!allowedOutsideOfDest && !fileUtils.isLeadingPath(dir, f, true)) {
            log("skipping " + entryName + " as its target " + f.getCanonicalPath() + " is outside of " + dir.getCanonicalPath() + ".", 3);
            return;
        }
        try {
            if (!this.overwrite && f.exists() && f.lastModified() >= entryDate.getTime()) {
                log("Skipping " + f + " as it is up-to-date", 4);
                return;
            }
            log("expanding " + entryName + " to " + f, 3);
            File dirF = f.getParentFile();
            if (dirF != null) {
                dirF.mkdirs();
            }
            if (isDirectory) {
                f.mkdirs();
            } else {
                byte[] buffer = new byte[1024];
                OutputStream fos = Files.newOutputStream(f.toPath(), new OpenOption[0]);
                while (true) {
                    int length = compressedInputStream.read(buffer);
                    if (length < 0) {
                        break;
                    }
                    fos.write(buffer, 0, length);
                }
                if (fos != null) {
                    fos.close();
                }
            }
            fileUtils.setFileLastModified(f, entryDate.getTime());
        } catch (FileNotFoundException ex) {
            log("Unable to expand to file " + f.getPath(), ex, 1);
        }
    }

    public void setDest(File d) {
        this.dest = d;
    }

    public void setSrc(File s) {
        this.source = s;
    }

    public void setOverwrite(boolean b) {
        this.overwrite = b;
    }

    public void addPatternset(PatternSet set) {
        this.patternsets.add(set);
    }

    public void addFileset(FileSet set) {
        add(set);
    }

    public void add(ResourceCollection rc) {
        this.resourcesSpecified = true;
        this.resources.add(rc);
    }

    public Mapper createMapper() throws BuildException {
        if (this.mapperElement != null) {
            throw new BuildException(ERROR_MULTIPLE_MAPPERS, getLocation());
        }
        this.mapperElement = new Mapper(getProject());
        return this.mapperElement;
    }

    public void add(FileNameMapper fileNameMapper) {
        createMapper().add(fileNameMapper);
    }

    public void setEncoding(String encoding) {
        internalSetEncoding(encoding);
    }

    protected void internalSetEncoding(String encoding) {
        if (NATIVE_ENCODING.equals(encoding)) {
            encoding = null;
        }
        this.encoding = encoding;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setStripAbsolutePathSpec(boolean b) {
        this.stripAbsolutePathSpec = b;
    }

    public void setScanForUnicodeExtraFields(boolean b) {
        internalSetScanForUnicodeExtraFields(b);
    }

    protected void internalSetScanForUnicodeExtraFields(boolean b) {
        this.scanForUnicodeExtraFields = b;
    }

    public boolean getScanForUnicodeExtraFields() {
        return this.scanForUnicodeExtraFields;
    }

    public void setAllowFilesToEscapeDest(boolean b) {
        this.allowFilesToEscapeDest = Boolean.valueOf(b);
    }

    public Boolean getAllowFilesToEscapeDest() {
        return this.allowFilesToEscapeDest;
    }
}
