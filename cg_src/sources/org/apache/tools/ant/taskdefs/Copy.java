package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.FlatFileNameMapper;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.ant.util.LinkedHashtable;
import org.apache.tools.ant.util.ResourceUtils;
import org.apache.tools.ant.util.SourceFileScanner;
import org.apache.tools.ant.util.StringUtils;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Copy.class */
public class Copy extends Task {
    private static final String MSG_WHEN_COPYING_EMPTY_RC_TO_FILE = "Cannot perform operation from directory to file.";
    @Deprecated
    static final String LINE_SEPARATOR = StringUtils.LINE_SEP;
    static final File NULL_FILE_PLACEHOLDER = new File("/NULL_FILE");
    private long granularity;
    protected File file = null;
    protected File destFile = null;
    protected File destDir = null;
    protected Vector<ResourceCollection> rcs = new Vector<>();
    protected Vector<ResourceCollection> filesets = this.rcs;
    private boolean enableMultipleMappings = false;
    protected boolean filtering = false;
    protected boolean preserveLastModified = false;
    protected boolean forceOverwrite = false;
    protected boolean flatten = false;
    protected int verbosity = 3;
    protected boolean includeEmpty = true;
    protected boolean failonerror = true;
    protected Hashtable<String, String[]> fileCopyMap = new LinkedHashtable();
    protected Hashtable<String, String[]> dirCopyMap = new LinkedHashtable();
    protected Hashtable<File, File> completeDirMap = new LinkedHashtable();
    protected Mapper mapperElement = null;
    private final Vector<FilterChain> filterChains = new Vector<>();
    private final Vector<FilterSet> filterSets = new Vector<>();
    private String inputEncoding = null;
    private String outputEncoding = null;
    private boolean force = false;
    private boolean quiet = false;
    private Resource singleResource = null;
    protected FileUtils fileUtils = FileUtils.getFileUtils();

    public Copy() {
        this.granularity = 0L;
        this.granularity = this.fileUtils.getFileTimestampGranularity();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FileUtils getFileUtils() {
        return this.fileUtils;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setTofile(File destFile) {
        this.destFile = destFile;
    }

    public void setTodir(File destDir) {
        this.destDir = destDir;
    }

    public FilterChain createFilterChain() {
        FilterChain filterChain = new FilterChain();
        this.filterChains.addElement(filterChain);
        return filterChain;
    }

    public FilterSet createFilterSet() {
        FilterSet filterSet = new FilterSet();
        this.filterSets.addElement(filterSet);
        return filterSet;
    }

    @Deprecated
    public void setPreserveLastModified(String preserve) {
        setPreserveLastModified(Project.toBoolean(preserve));
    }

    public void setPreserveLastModified(boolean preserve) {
        this.preserveLastModified = preserve;
    }

    public boolean getPreserveLastModified() {
        return this.preserveLastModified;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Vector<FilterSet> getFilterSets() {
        return this.filterSets;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Vector<FilterChain> getFilterChains() {
        return this.filterChains;
    }

    public void setFiltering(boolean filtering) {
        this.filtering = filtering;
    }

    public void setOverwrite(boolean overwrite) {
        this.forceOverwrite = overwrite;
    }

    public void setForce(boolean f) {
        this.force = f;
    }

    public boolean getForce() {
        return this.force;
    }

    public void setFlatten(boolean flatten) {
        this.flatten = flatten;
    }

    public void setVerbose(boolean verbose) {
        this.verbosity = verbose ? 2 : 3;
    }

    public void setIncludeEmptyDirs(boolean includeEmpty) {
        this.includeEmpty = includeEmpty;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public void setEnableMultipleMappings(boolean enableMultipleMappings) {
        this.enableMultipleMappings = enableMultipleMappings;
    }

    public boolean isEnableMultipleMapping() {
        return this.enableMultipleMappings;
    }

    public void setFailOnError(boolean failonerror) {
        this.failonerror = failonerror;
    }

    public void addFileset(FileSet set) {
        add(set);
    }

    public void add(ResourceCollection res) {
        this.rcs.add(res);
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

    public void setEncoding(String encoding) {
        this.inputEncoding = encoding;
        if (this.outputEncoding == null) {
            this.outputEncoding = encoding;
        }
    }

    public String getEncoding() {
        return this.inputEncoding;
    }

    public void setOutputEncoding(String encoding) {
        this.outputEncoding = encoding;
    }

    public String getOutputEncoding() {
        return this.outputEncoding;
    }

    public void setGranularity(long granularity) {
        this.granularity = granularity;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        File savedFile = this.file;
        File savedDestFile = this.destFile;
        File savedDestDir = this.destDir;
        ResourceCollection savedRc = null;
        if (this.file == null && this.destFile != null && this.rcs.size() == 1) {
            savedRc = this.rcs.elementAt(0);
        }
        try {
            try {
                validateAttributes();
                copySingleFile();
                Map<File, List<String>> filesByBasedir = new HashMap<>();
                Map<File, List<String>> dirsByBasedir = new HashMap<>();
                Set<File> baseDirs = new HashSet<>();
                List<Resource> nonFileResources = new ArrayList<>();
                Iterator<ResourceCollection> it = this.rcs.iterator();
                while (it.hasNext()) {
                    ResourceCollection rc = it.next();
                    if ((rc instanceof FileSet) && rc.isFilesystemOnly()) {
                        FileSet fs = (FileSet) rc;
                        try {
                            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
                            File fromDir = fs.getDir(getProject());
                            if (!this.flatten && this.mapperElement == null && ds.isEverythingIncluded() && !fs.hasPatterns()) {
                                this.completeDirMap.put(fromDir, this.destDir);
                            }
                            add(fromDir, ds.getIncludedFiles(), filesByBasedir);
                            add(fromDir, ds.getIncludedDirectories(), dirsByBasedir);
                            baseDirs.add(fromDir);
                        } catch (BuildException e) {
                            if (this.failonerror || !getMessage(e).endsWith(DirectoryScanner.DOES_NOT_EXIST_POSTFIX)) {
                                throw e;
                            }
                            if (!this.quiet) {
                                log("Warning: " + getMessage(e), 0);
                            }
                        }
                    } else if (!rc.isFilesystemOnly() && !supportsNonFileResources()) {
                        throw new BuildException("Only FileSystem resources are supported.");
                    } else {
                        for (Resource r : rc) {
                            if (!r.isExists()) {
                                String message = "Warning: Could not find resource " + r.toLongString() + " to copy.";
                                if (!this.failonerror) {
                                    if (!this.quiet) {
                                        log(message, 0);
                                    }
                                } else {
                                    throw new BuildException(message);
                                }
                            } else {
                                File baseDir = NULL_FILE_PLACEHOLDER;
                                String name = r.getName();
                                FileProvider fp = (FileProvider) r.as(FileProvider.class);
                                if (fp != null) {
                                    FileResource fr = ResourceUtils.asFileResource(fp);
                                    baseDir = getKeyFile(fr.getBaseDir());
                                    if (fr.getBaseDir() == null) {
                                        name = fr.getFile().getAbsolutePath();
                                    }
                                }
                                if (r.isDirectory() || fp != null) {
                                    add(baseDir, name, r.isDirectory() ? dirsByBasedir : filesByBasedir);
                                    baseDirs.add(baseDir);
                                } else {
                                    nonFileResources.add(r);
                                }
                            }
                        }
                    }
                }
                iterateOverBaseDirs(baseDirs, dirsByBasedir, filesByBasedir);
                try {
                    doFileOperations();
                } catch (BuildException e2) {
                    if (!this.failonerror) {
                        if (!this.quiet) {
                            log("Warning: " + getMessage(e2), 0);
                        }
                    } else {
                        throw e2;
                    }
                }
                if (!nonFileResources.isEmpty() || this.singleResource != null) {
                    Resource[] nonFiles = (Resource[]) nonFileResources.toArray(new Resource[nonFileResources.size()]);
                    Map<Resource, String[]> map = scan(nonFiles, this.destDir);
                    if (this.singleResource != null) {
                        map.put(this.singleResource, new String[]{this.destFile.getAbsolutePath()});
                    }
                    try {
                        doResourceOperations(map);
                    } catch (BuildException e3) {
                        if (!this.failonerror) {
                            if (!this.quiet) {
                                log("Warning: " + getMessage(e3), 0);
                            }
                        } else {
                            throw e3;
                        }
                    }
                }
                this.singleResource = null;
                this.file = savedFile;
                this.destFile = savedDestFile;
                this.destDir = savedDestDir;
                if (savedRc != null) {
                    this.rcs.insertElementAt(savedRc, 0);
                }
                this.fileCopyMap.clear();
                this.dirCopyMap.clear();
                this.completeDirMap.clear();
            } catch (BuildException e4) {
                if (this.failonerror || !getMessage(e4).equals(MSG_WHEN_COPYING_EMPTY_RC_TO_FILE)) {
                    throw e4;
                }
                log("Warning: " + getMessage(e4), 0);
                this.singleResource = null;
                this.file = savedFile;
                this.destFile = savedDestFile;
                this.destDir = savedDestDir;
                if (savedRc != null) {
                    this.rcs.insertElementAt(savedRc, 0);
                }
                this.fileCopyMap.clear();
                this.dirCopyMap.clear();
                this.completeDirMap.clear();
            }
        } catch (Throwable th) {
            this.singleResource = null;
            this.file = savedFile;
            this.destFile = savedDestFile;
            this.destDir = savedDestDir;
            if (savedRc != null) {
                this.rcs.insertElementAt(savedRc, 0);
            }
            this.fileCopyMap.clear();
            this.dirCopyMap.clear();
            this.completeDirMap.clear();
            throw th;
        }
    }

    private void copySingleFile() {
        if (this.file != null) {
            if (this.file.exists()) {
                if (this.destFile == null) {
                    this.destFile = new File(this.destDir, this.file.getName());
                }
                if (this.forceOverwrite || !this.destFile.exists() || this.file.lastModified() - this.granularity > this.destFile.lastModified()) {
                    this.fileCopyMap.put(this.file.getAbsolutePath(), new String[]{this.destFile.getAbsolutePath()});
                    return;
                } else {
                    log(this.file + " omitted as " + this.destFile + " is up to date.", 3);
                    return;
                }
            }
            String message = "Warning: Could not find file " + this.file.getAbsolutePath() + " to copy.";
            if (!this.failonerror) {
                if (!this.quiet) {
                    log(message, 0);
                    return;
                }
                return;
            }
            throw new BuildException(message);
        }
    }

    private void iterateOverBaseDirs(Set<File> baseDirs, Map<File, List<String>> dirsByBasedir, Map<File, List<String>> filesByBasedir) {
        Iterator<File> it = baseDirs.iterator();
        while (it.hasNext()) {
            File f = it.next();
            List<String> files = filesByBasedir.get(f);
            List<String> dirs = dirsByBasedir.get(f);
            String[] srcFiles = new String[0];
            if (files != null) {
                srcFiles = (String[]) files.toArray(srcFiles);
            }
            String[] srcDirs = new String[0];
            if (dirs != null) {
                srcDirs = (String[]) dirs.toArray(srcDirs);
            }
            scan(f == NULL_FILE_PLACEHOLDER ? null : f, this.destDir, srcFiles, srcDirs);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void validateAttributes() throws BuildException {
        if (this.file == null && this.rcs.isEmpty()) {
            throw new BuildException("Specify at least one source--a file or a resource collection.");
        }
        if (this.destFile != null && this.destDir != null) {
            throw new BuildException("Only one of tofile and todir may be set.");
        }
        if (this.destFile == null && this.destDir == null) {
            throw new BuildException("One of tofile or todir must be set.");
        }
        if (this.file != null && this.file.isDirectory()) {
            throw new BuildException("Use a resource collection to copy directories.");
        }
        if (this.destFile != null && !this.rcs.isEmpty()) {
            if (this.rcs.size() > 1) {
                throw new BuildException("Cannot concatenate multiple files into a single file.");
            }
            ResourceCollection rc = this.rcs.elementAt(0);
            if (!rc.isFilesystemOnly() && !supportsNonFileResources()) {
                throw new BuildException("Only FileSystem resources are supported.");
            }
            if (rc.isEmpty()) {
                throw new BuildException(MSG_WHEN_COPYING_EMPTY_RC_TO_FILE);
            }
            if (rc.size() == 1) {
                Resource res = rc.iterator().next();
                FileProvider r = (FileProvider) res.as(FileProvider.class);
                if (this.file == null) {
                    if (r != null) {
                        this.file = r.getFile();
                    } else {
                        this.singleResource = res;
                    }
                    this.rcs.removeElementAt(0);
                } else {
                    throw new BuildException("Cannot concatenate multiple files into a single file.");
                }
            } else {
                throw new BuildException("Cannot concatenate multiple files into a single file.");
            }
        }
        if (this.destFile != null) {
            this.destDir = this.destFile.getParentFile();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void scan(File fromDir, File toDir, String[] files, String[] dirs) {
        FileNameMapper mapper = getMapper();
        buildMap(fromDir, toDir, files, mapper, this.fileCopyMap);
        if (this.includeEmpty) {
            buildMap(fromDir, toDir, dirs, mapper, this.dirCopyMap);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map<Resource, String[]> scan(Resource[] fromResources, File toDir) {
        return buildMap(fromResources, toDir, getMapper());
    }

    protected void buildMap(File fromDir, File toDir, String[] names, FileNameMapper mapper, Hashtable<String, String[]> map) {
        String[] toCopy;
        String[] strArr;
        if (this.forceOverwrite) {
            List<String> v = new ArrayList<>();
            for (String name : names) {
                if (mapper.mapFileName(name) != null) {
                    v.add(name);
                }
            }
            toCopy = (String[]) v.toArray(new String[v.size()]);
        } else {
            SourceFileScanner ds = new SourceFileScanner(this);
            toCopy = ds.restrict(names, fromDir, toDir, mapper, this.granularity);
        }
        for (String name2 : toCopy) {
            File src = new File(fromDir, name2);
            String[] mappedFiles = mapper.mapFileName(name2);
            if (mappedFiles != null && mappedFiles.length != 0) {
                if (!this.enableMultipleMappings) {
                    map.put(src.getAbsolutePath(), new String[]{new File(toDir, mappedFiles[0]).getAbsolutePath()});
                } else {
                    for (int k = 0; k < mappedFiles.length; k++) {
                        mappedFiles[k] = new File(toDir, mappedFiles[k]).getAbsolutePath();
                    }
                    map.put(src.getAbsolutePath(), mappedFiles);
                }
            }
        }
    }

    protected Map<Resource, String[]> buildMap(Resource[] fromResources, File toDir, FileNameMapper mapper) {
        Resource[] toCopy;
        Resource[] resourceArr;
        Map<Resource, String[]> map = new HashMap<>();
        if (this.forceOverwrite) {
            List<Resource> v = new ArrayList<>();
            for (Resource rc : fromResources) {
                if (mapper.mapFileName(rc.getName()) != null) {
                    v.add(rc);
                }
            }
            toCopy = (Resource[]) v.toArray(new Resource[v.size()]);
        } else {
            toCopy = ResourceUtils.selectOutOfDateSources(this, fromResources, mapper, name -> {
                return new FileResource(toDir, name);
            }, this.granularity);
        }
        for (Resource rc2 : toCopy) {
            String[] mappedFiles = mapper.mapFileName(rc2.getName());
            if (mappedFiles == null || mappedFiles.length == 0) {
                throw new BuildException("Can't copy a resource without a name if the mapper doesn't provide one.");
            }
            if (!this.enableMultipleMappings) {
                map.put(rc2, new String[]{new File(toDir, mappedFiles[0]).getAbsolutePath()});
            } else {
                for (int k = 0; k < mappedFiles.length; k++) {
                    mappedFiles[k] = new File(toDir, mappedFiles[k]).getAbsolutePath();
                }
                map.put(rc2, mappedFiles);
            }
        }
        return map;
    }

    protected void doFileOperations() {
        String[] value;
        if (!this.fileCopyMap.isEmpty()) {
            log("Copying " + this.fileCopyMap.size() + " file" + (this.fileCopyMap.size() == 1 ? "" : "s") + " to " + this.destDir.getAbsolutePath());
            for (Map.Entry<String, String[]> e : this.fileCopyMap.entrySet()) {
                String fromFile = e.getKey();
                for (String toFile : e.getValue()) {
                    if (fromFile.equals(toFile)) {
                        log("Skipping self-copy of " + fromFile, this.verbosity);
                    } else {
                        try {
                            log("Copying " + fromFile + " to " + toFile, this.verbosity);
                            FilterSetCollection executionFilters = new FilterSetCollection();
                            if (this.filtering) {
                                executionFilters.addFilterSet(getProject().getGlobalFilterSet());
                            }
                            Iterator<FilterSet> it = this.filterSets.iterator();
                            while (it.hasNext()) {
                                FilterSet filterSet = it.next();
                                executionFilters.addFilterSet(filterSet);
                            }
                            this.fileUtils.copyFile(new File(fromFile), new File(toFile), executionFilters, this.filterChains, this.forceOverwrite, this.preserveLastModified, false, this.inputEncoding, this.outputEncoding, getProject(), getForce());
                        } catch (IOException ioe) {
                            String msg = "Failed to copy " + fromFile + " to " + toFile + " due to " + getDueTo(ioe);
                            File targetFile = new File(toFile);
                            if (!(ioe instanceof ResourceUtils.ReadOnlyTargetFileException) && targetFile.exists() && !targetFile.delete()) {
                                msg = msg + " and I couldn't delete the corrupt " + toFile;
                            }
                            if (this.failonerror) {
                                throw new BuildException(msg, ioe, getLocation());
                            }
                            log(msg, 0);
                        }
                    }
                }
            }
        }
        if (this.includeEmpty) {
            int createCount = 0;
            for (String[] dirs : this.dirCopyMap.values()) {
                for (String dir : dirs) {
                    File d = new File(dir);
                    if (!d.exists()) {
                        if (!d.mkdirs() && !d.isDirectory()) {
                            log("Unable to create directory " + d.getAbsolutePath(), 0);
                        } else {
                            createCount++;
                        }
                    }
                }
            }
            if (createCount > 0) {
                log("Copied " + this.dirCopyMap.size() + " empty director" + (this.dirCopyMap.size() == 1 ? "y" : "ies") + " to " + createCount + " empty director" + (createCount == 1 ? "y" : "ies") + " under " + this.destDir.getAbsolutePath());
            }
        }
    }

    protected void doResourceOperations(Map<Resource, String[]> map) {
        String[] value;
        if (!map.isEmpty()) {
            log("Copying " + map.size() + " resource" + (map.size() == 1 ? "" : "s") + " to " + this.destDir.getAbsolutePath());
            for (Map.Entry<Resource, String[]> e : map.entrySet()) {
                Resource fromResource = e.getKey();
                for (String toFile : e.getValue()) {
                    try {
                        log("Copying " + fromResource + " to " + toFile, this.verbosity);
                        FilterSetCollection executionFilters = new FilterSetCollection();
                        if (this.filtering) {
                            executionFilters.addFilterSet(getProject().getGlobalFilterSet());
                        }
                        Iterator<FilterSet> it = this.filterSets.iterator();
                        while (it.hasNext()) {
                            FilterSet filterSet = it.next();
                            executionFilters.addFilterSet(filterSet);
                        }
                        ResourceUtils.copyResource(fromResource, new FileResource(this.destDir, toFile), executionFilters, this.filterChains, this.forceOverwrite, this.preserveLastModified, false, this.inputEncoding, this.outputEncoding, getProject(), getForce());
                    } catch (IOException ioe) {
                        String msg = "Failed to copy " + fromResource + " to " + toFile + " due to " + getDueTo(ioe);
                        File targetFile = new File(toFile);
                        if (!(ioe instanceof ResourceUtils.ReadOnlyTargetFileException) && targetFile.exists() && !targetFile.delete()) {
                            msg = msg + " and I couldn't delete the corrupt " + toFile;
                        }
                        if (this.failonerror) {
                            throw new BuildException(msg, ioe, getLocation());
                        }
                        log(msg, 0);
                    }
                }
            }
        }
    }

    protected boolean supportsNonFileResources() {
        return getClass().equals(Copy.class);
    }

    private static void add(File baseDir, String[] names, Map<File, List<String>> m) {
        if (names != null) {
            List<String> l = m.computeIfAbsent(getKeyFile(baseDir), k -> {
                return new ArrayList(names.length);
            });
            l.addAll(Arrays.asList(names));
        }
    }

    private static void add(File baseDir, String name, Map<File, List<String>> m) {
        if (name != null) {
            add(baseDir, new String[]{name}, m);
        }
    }

    private static File getKeyFile(File f) {
        return f == null ? NULL_FILE_PLACEHOLDER : f;
    }

    private FileNameMapper getMapper() {
        FileNameMapper mapper;
        if (this.mapperElement != null) {
            mapper = this.mapperElement.getImplementation();
        } else if (this.flatten) {
            mapper = new FlatFileNameMapper();
        } else {
            mapper = new IdentityMapper();
        }
        return mapper;
    }

    private String getMessage(Exception ex) {
        return ex.getMessage() == null ? ex.toString() : ex.getMessage();
    }

    private String getDueTo(Exception ex) {
        boolean baseIOException = ex.getClass() == IOException.class;
        StringBuilder message = new StringBuilder();
        if (!baseIOException || ex.getMessage() == null) {
            message.append(ex.getClass().getName());
        }
        if (ex.getMessage() != null) {
            if (!baseIOException) {
                message.append(Instruction.argsep);
            }
            message.append(ex.getMessage());
        }
        if (ex.getClass().getName().contains("MalformedInput")) {
            Object[] objArr = new Object[1];
            objArr[0] = this.inputEncoding == null ? this.fileUtils.getDefaultEncoding() : this.inputEncoding;
            message.append(String.format("%nThis is normally due to the input file containing invalid%nbytes for the character encoding used : %s%n", objArr));
        }
        return message.toString();
    }
}
