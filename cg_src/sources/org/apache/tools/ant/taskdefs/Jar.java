package org.apache.tools.ant.taskdefs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.Manifest;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.types.ArchiveFileSet;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.types.spi.Service;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.StreamUtils;
import org.apache.tools.zip.JarMarker;
import org.apache.tools.zip.ZipExtraField;
import org.apache.tools.zip.ZipOutputStream;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Jar.class */
public class Jar extends Zip {
    private static final String INDEX_NAME = "META-INF/INDEX.LIST";
    private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
    private Manifest configuredManifest;
    private Manifest savedConfiguredManifest;
    private Manifest filesetManifest;
    private Manifest originalManifest;
    private FilesetManifestConfig filesetManifestConfig;
    private Manifest manifest;
    private String manifestEncoding;
    private File manifestFile;
    private List<String> rootEntries;
    private Path indexJars;
    private static final ZipExtraField[] JAR_MARKER = {JarMarker.getInstance()};
    private List<Service> serviceList = new ArrayList();
    private boolean mergeManifestsMain = true;
    private boolean index = false;
    private boolean indexMetaInf = false;
    private boolean createEmpty = false;
    private FileNameMapper indexJarsMapper = null;
    private StrictMode strict = new StrictMode(Definer.OnError.POLICY_IGNORE);
    private boolean mergeClassPaths = false;
    private boolean flattenClassPaths = false;

    public Jar() {
        this.archiveType = "jar";
        this.emptyBehavior = "create";
        setEncoding("UTF8");
        setZip64Mode(Zip.Zip64ModeAttribute.NEVER);
        this.rootEntries = new Vector();
    }

    @Override // org.apache.tools.ant.taskdefs.Zip
    public void setWhenempty(Zip.WhenEmpty we) {
        log("JARs are never empty, they contain at least a manifest file", 1);
    }

    public void setWhenmanifestonly(Zip.WhenEmpty we) {
        this.emptyBehavior = we.getValue();
    }

    public void setStrict(StrictMode strict) {
        this.strict = strict;
    }

    @Deprecated
    public void setJarfile(File jarFile) {
        setDestFile(jarFile);
    }

    public void setIndex(boolean flag) {
        this.index = flag;
    }

    public void setIndexMetaInf(boolean flag) {
        this.indexMetaInf = flag;
    }

    public void setManifestEncoding(String manifestEncoding) {
        this.manifestEncoding = manifestEncoding;
    }

    public void addConfiguredManifest(Manifest newManifest) throws ManifestException {
        if (this.configuredManifest == null) {
            this.configuredManifest = newManifest;
        } else {
            this.configuredManifest.merge(newManifest, false, this.mergeClassPaths);
        }
        this.savedConfiguredManifest = this.configuredManifest;
    }

    public void setManifest(File manifestFile) {
        if (!manifestFile.exists()) {
            throw new BuildException("Manifest file: " + manifestFile + DirectoryScanner.DOES_NOT_EXIST_POSTFIX, getLocation());
        }
        this.manifestFile = manifestFile;
    }

    private Manifest getManifest(File manifestFile) {
        try {
            InputStreamReader isr = new InputStreamReader(Files.newInputStream(manifestFile.toPath(), new OpenOption[0]), getManifestCharset());
            Manifest manifest = getManifest(isr);
            isr.close();
            return manifest;
        } catch (IOException e) {
            throw new BuildException("Unable to read manifest file: " + manifestFile + " (" + e.getMessage() + ")", e);
        }
    }

    private Manifest getManifestFromJar(File jarFile) throws IOException {
        ZipFile zf = new ZipFile(jarFile);
        try {
            ZipEntry ze = (ZipEntry) StreamUtils.enumerationAsStream(zf.entries()).filter(entry -> {
                return MANIFEST_NAME.equalsIgnoreCase(entry.getName());
            }).findFirst().orElse(null);
            if (ze != null) {
                InputStreamReader isr = new InputStreamReader(zf.getInputStream(ze), StandardCharsets.UTF_8);
                Manifest manifest = getManifest(isr);
                isr.close();
                zf.close();
                return manifest;
            }
            zf.close();
            return null;
        } catch (Throwable th) {
            try {
                zf.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private Manifest getManifest(Reader r) {
        try {
            return new Manifest(r);
        } catch (IOException e) {
            throw new BuildException("Unable to read manifest file (" + e.getMessage() + ")", e);
        } catch (ManifestException e2) {
            log("Manifest is invalid: " + e2.getMessage(), 0);
            throw new BuildException("Invalid Manifest: " + this.manifestFile, e2, getLocation());
        }
    }

    private boolean jarHasIndex(File jarFile) throws IOException {
        ZipFile zf = new ZipFile(jarFile);
        try {
            boolean anyMatch = StreamUtils.enumerationAsStream(zf.entries()).anyMatch(ze -> {
                return INDEX_NAME.equalsIgnoreCase(ze.getName());
            });
            zf.close();
            return anyMatch;
        } catch (Throwable th) {
            try {
                zf.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public void setFilesetmanifest(FilesetManifestConfig config) {
        this.filesetManifestConfig = config;
        this.mergeManifestsMain = config != null && "merge".equals(config.getValue());
        if (this.filesetManifestConfig != null && !MSVSSConstants.WRITABLE_SKIP.equals(this.filesetManifestConfig.getValue())) {
            this.doubleFilePass = true;
        }
    }

    public void addMetainf(ZipFileSet fs) {
        fs.setPrefix("META-INF/");
        super.addFileset(fs);
    }

    public void addConfiguredIndexJars(Path p) {
        if (this.indexJars == null) {
            this.indexJars = new Path(getProject());
        }
        this.indexJars.append(p);
    }

    public void addConfiguredIndexJarsMapper(Mapper mapper) {
        if (this.indexJarsMapper != null) {
            throw new BuildException("Cannot define more than one indexjar-mapper", getLocation());
        }
        this.indexJarsMapper = mapper.getImplementation();
    }

    public FileNameMapper getIndexJarsMapper() {
        return this.indexJarsMapper;
    }

    public void addConfiguredService(Service service) {
        service.check();
        this.serviceList.add(service);
    }

    private void writeServices(ZipOutputStream zOut) throws IOException {
        for (Service service : this.serviceList) {
            InputStream is = service.getAsStream();
            try {
                super.zipFile(is, zOut, "META-INF/services/" + service.getType(), System.currentTimeMillis(), null, 33188);
                if (is != null) {
                    is.close();
                }
            } catch (Throwable th) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    public void setMergeClassPathAttributes(boolean b) {
        this.mergeClassPaths = b;
    }

    public void setFlattenAttributes(boolean b) {
        this.flattenClassPaths = b;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.Zip
    public void initZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
        if (!this.skipWriting) {
            Manifest jarManifest = createManifest();
            writeManifest(zOut, jarManifest);
            writeServices(zOut);
        }
    }

    private Manifest createManifest() throws BuildException {
        Manifest finalManifest;
        try {
            if (this.manifest == null && this.manifestFile != null) {
                this.manifest = getManifest(this.manifestFile);
            }
            boolean mergeFileSetFirst = !this.mergeManifestsMain && this.filesetManifest != null && this.configuredManifest == null && this.manifest == null;
            if (mergeFileSetFirst) {
                finalManifest = new Manifest();
                finalManifest.merge(this.filesetManifest, false, this.mergeClassPaths);
                finalManifest.merge(Manifest.getDefaultManifest(), true, this.mergeClassPaths);
            } else {
                finalManifest = Manifest.getDefaultManifest();
            }
            if (isInUpdateMode()) {
                finalManifest.merge(this.originalManifest, false, this.mergeClassPaths);
            }
            if (!mergeFileSetFirst) {
                finalManifest.merge(this.filesetManifest, false, this.mergeClassPaths);
            }
            finalManifest.merge(this.configuredManifest, !this.mergeManifestsMain, this.mergeClassPaths);
            finalManifest.merge(this.manifest, !this.mergeManifestsMain, this.mergeClassPaths);
            return finalManifest;
        } catch (ManifestException e) {
            log("Manifest is invalid: " + e.getMessage(), 0);
            throw new BuildException("Invalid Manifest", e, getLocation());
        }
    }

    private void writeManifest(ZipOutputStream zOut, Manifest manifest) throws IOException {
        StreamUtils.enumerationAsStream(manifest.getWarnings()).forEach(warning -> {
            log("Manifest warning: " + warning, 1);
        });
        zipDir((Resource) null, zOut, "META-INF/", 16877, JAR_MARKER);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(baos, Manifest.JAR_CHARSET);
        PrintWriter writer = new PrintWriter(osw);
        manifest.write(writer, this.flattenClassPaths);
        if (writer.checkError()) {
            throw new IOException("Encountered an error writing the manifest");
        }
        writer.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try {
            super.zipFile(bais, zOut, MANIFEST_NAME, System.currentTimeMillis(), null, 33188);
            FileUtils.close((InputStream) bais);
            super.initZipOutputStream(zOut);
        } catch (Throwable th) {
            FileUtils.close((InputStream) bais);
            throw th;
        }
    }

    @Override // org.apache.tools.ant.taskdefs.Zip
    protected void finalizeZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
        if (this.index) {
            createIndexList(zOut);
        }
    }

    private void createIndexList(ZipOutputStream zOut) throws IOException {
        String[] list;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8));
        writer.println("JarIndex-Version: 1.0");
        writer.println();
        writer.println(this.zipFile.getName());
        writeIndexLikeList(new ArrayList<>(this.addedDirs.keySet()), this.rootEntries, writer);
        writer.println();
        if (this.indexJars != null) {
            FileNameMapper mapper = this.indexJarsMapper;
            if (mapper == null) {
                mapper = createDefaultIndexJarsMapper();
            }
            for (String indexJarEntry : this.indexJars.list()) {
                String[] names = mapper.mapFileName(indexJarEntry);
                if (names != null && names.length > 0) {
                    ArrayList<String> dirs = new ArrayList<>();
                    ArrayList<String> files = new ArrayList<>();
                    grabFilesAndDirs(indexJarEntry, dirs, files);
                    if (dirs.size() + files.size() > 0) {
                        writer.println(names[0]);
                        writeIndexLikeList(dirs, files, writer);
                        writer.println();
                    }
                }
            }
        }
        if (writer.checkError()) {
            throw new IOException("Encountered an error writing jar index");
        }
        writer.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try {
            super.zipFile(bais, zOut, INDEX_NAME, System.currentTimeMillis(), null, 33188);
            bais.close();
        } catch (Throwable th) {
            try {
                bais.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private FileNameMapper createDefaultIndexJarsMapper() {
        Manifest mf = createManifest();
        Manifest.Attribute classpath = mf.getMainSection().getAttribute(Manifest.ATTRIBUTE_CLASSPATH);
        String[] cpEntries = null;
        if (classpath != null && classpath.getValue() != null) {
            StringTokenizer tok = new StringTokenizer(classpath.getValue(), Instruction.argsep);
            cpEntries = new String[tok.countTokens()];
            int c = 0;
            while (tok.hasMoreTokens()) {
                int i = c;
                c++;
                cpEntries[i] = tok.nextToken();
            }
        }
        return new IndexJarsFilenameMapper(cpEntries);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.Zip
    public void zipFile(InputStream is, ZipOutputStream zOut, String vPath, long lastModified, File fromArchive, int mode) throws IOException {
        if (MANIFEST_NAME.equalsIgnoreCase(vPath)) {
            if (isFirstPass()) {
                filesetManifest(fromArchive, is);
            }
        } else if (INDEX_NAME.equalsIgnoreCase(vPath) && this.index) {
            logWhenWriting("Warning: selected " + this.archiveType + " files include a " + INDEX_NAME + " which will be replaced by a newly generated one.", 1);
        } else {
            if (this.index && !vPath.contains("/")) {
                this.rootEntries.add(vPath);
            }
            super.zipFile(is, zOut, vPath, lastModified, fromArchive, mode);
        }
    }

    private void filesetManifest(File file, InputStream is) throws IOException {
        InputStreamReader isr;
        Manifest newManifest;
        if (this.manifestFile != null && this.manifestFile.equals(file)) {
            log("Found manifest " + file, 3);
            if (is == null) {
                this.manifest = getManifest(file);
                return;
            }
            isr = new InputStreamReader(is, getManifestCharset());
            try {
                this.manifest = getManifest(isr);
                isr.close();
            } finally {
            }
        } else if (this.filesetManifestConfig != null && !MSVSSConstants.WRITABLE_SKIP.equals(this.filesetManifestConfig.getValue())) {
            logWhenWriting("Found manifest to merge in file " + file, 3);
            try {
                if (is == null) {
                    newManifest = getManifest(file);
                } else {
                    isr = new InputStreamReader(is, getManifestCharset());
                    try {
                        newManifest = getManifest(isr);
                        isr.close();
                    } finally {
                    }
                }
                if (this.filesetManifest == null) {
                    this.filesetManifest = newManifest;
                } else {
                    this.filesetManifest.merge(newManifest, false, this.mergeClassPaths);
                }
            } catch (UnsupportedEncodingException e) {
                throw new BuildException("Unsupported encoding while reading manifest: " + e.getMessage(), e);
            } catch (ManifestException e2) {
                log("Manifest in file " + file + " is invalid: " + e2.getMessage(), 0);
                throw new BuildException("Invalid Manifest", e2, getLocation());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.Zip
    public Zip.ArchiveState getResourcesToAdd(ResourceCollection[] rcs, File zipFile, boolean needsUpdate) throws BuildException {
        if (this.skipWriting) {
            Resource[][] manifests = grabManifests(rcs);
            int count = 0;
            for (Resource[] mf : manifests) {
                count += mf.length;
            }
            log("found a total of " + count + " manifests in " + manifests.length + " resource collections", 3);
            return new Zip.ArchiveState(true, manifests);
        }
        if (zipFile.exists()) {
            try {
                this.originalManifest = getManifestFromJar(zipFile);
                if (this.originalManifest == null) {
                    log("Updating jar since the current jar has no manifest", 3);
                    needsUpdate = true;
                } else {
                    Manifest mf2 = createManifest();
                    if (!mf2.equals(this.originalManifest)) {
                        log("Updating jar since jar manifest has changed", 3);
                        needsUpdate = true;
                    }
                }
            } catch (Throwable t) {
                log("error while reading original manifest in file: " + zipFile.toString() + " due to " + t.getMessage(), 1);
                needsUpdate = true;
            }
        } else {
            needsUpdate = true;
        }
        this.createEmpty = needsUpdate;
        if (!needsUpdate && this.index) {
            try {
                needsUpdate = !jarHasIndex(zipFile);
            } catch (IOException e) {
                needsUpdate = true;
            }
        }
        return super.getResourcesToAdd(rcs, zipFile, needsUpdate);
    }

    @Override // org.apache.tools.ant.taskdefs.Zip
    protected boolean createEmptyZip(File zipFile) throws BuildException {
        if (!this.createEmpty) {
            return true;
        }
        if (MSVSSConstants.WRITABLE_SKIP.equals(this.emptyBehavior)) {
            if (!this.skipWriting) {
                log("Warning: skipping " + this.archiveType + " archive " + zipFile + " because no files were included.", 1);
                return true;
            }
            return true;
        } else if ("fail".equals(this.emptyBehavior)) {
            throw new BuildException("Cannot create " + this.archiveType + " archive " + zipFile + ": no files were included.", getLocation());
        } else {
            if (!this.skipWriting) {
                log("Building MANIFEST-only jar: " + getDestFile().getAbsolutePath());
                try {
                    try {
                        ZipOutputStream zOut = new ZipOutputStream(getDestFile());
                        try {
                            zOut.setEncoding(getEncoding());
                            zOut.setUseZip64(getZip64Mode().getMode());
                            if (isCompress()) {
                                zOut.setMethod(8);
                            } else {
                                zOut.setMethod(0);
                            }
                            initZipOutputStream(zOut);
                            finalizeZipOutputStream(zOut);
                            zOut.close();
                            return true;
                        } catch (Throwable th) {
                            try {
                                zOut.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                        }
                    } finally {
                        this.createEmpty = false;
                    }
                } catch (IOException ioe) {
                    throw new BuildException("Could not create almost empty JAR archive (" + ioe.getMessage() + ")", ioe, getLocation());
                }
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.Zip
    public void cleanUp() {
        super.cleanUp();
        checkJarSpec();
        if (!this.doubleFilePass || !this.skipWriting) {
            this.manifest = null;
            this.configuredManifest = this.savedConfiguredManifest;
            this.filesetManifest = null;
            this.originalManifest = null;
        }
        this.rootEntries.clear();
    }

    private void checkJarSpec() {
        Manifest.Section mainSection;
        StringBuilder message = new StringBuilder();
        if (this.configuredManifest == null) {
            mainSection = null;
        } else {
            mainSection = this.configuredManifest.getMainSection();
        }
        Manifest.Section mainSection2 = mainSection;
        if (mainSection2 == null) {
            message.append("No Implementation-Title set.");
            message.append("No Implementation-Version set.");
            message.append("No Implementation-Vendor set.");
        } else {
            if (mainSection2.getAttribute("Implementation-Title") == null) {
                message.append("No Implementation-Title set.");
            }
            if (mainSection2.getAttribute("Implementation-Version") == null) {
                message.append("No Implementation-Version set.");
            }
            if (mainSection2.getAttribute("Implementation-Vendor") == null) {
                message.append("No Implementation-Vendor set.");
            }
        }
        if (message.length() > 0) {
            message.append(String.format("%nLocation: %s%n", getLocation()));
            if ("fail".equalsIgnoreCase(this.strict.getValue())) {
                throw new BuildException(message.toString(), getLocation());
            }
            logWhenWriting(message.toString(), this.strict.getLogLevel());
        }
    }

    @Override // org.apache.tools.ant.taskdefs.Zip
    public void reset() {
        super.reset();
        this.emptyBehavior = "create";
        this.configuredManifest = null;
        this.filesetManifestConfig = null;
        this.mergeManifestsMain = false;
        this.manifestFile = null;
        this.index = false;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Jar$FilesetManifestConfig.class */
    public static class FilesetManifestConfig extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{MSVSSConstants.WRITABLE_SKIP, "merge", "mergewithoutmain"};
        }
    }

    protected final void writeIndexLikeList(List<String> dirs, List<String> files, PrintWriter writer) {
        Collections.sort(dirs);
        Collections.sort(files);
        for (String dir : dirs) {
            String dir2 = dir.replace('\\', '/');
            if (dir2.startsWith("./")) {
                dir2 = dir2.substring(2);
            }
            while (dir2.startsWith("/")) {
                dir2 = dir2.substring(1);
            }
            int pos = dir2.lastIndexOf(47);
            if (pos != -1) {
                dir2 = dir2.substring(0, pos);
            }
            if (this.indexMetaInf || !dir2.startsWith("META-INF")) {
                writer.println(dir2);
            }
        }
        Objects.requireNonNull(writer);
        files.forEach(this::println);
    }

    protected static String findJarName(String fileName, String[] classpath) {
        if (classpath == null) {
            return new File(fileName).getName();
        }
        String fileName2 = fileName.replace(File.separatorChar, '/');
        SortedMap<String, String> matches = new TreeMap<>(Comparator.comparingInt(s -> {
            if (s == null) {
                return 0;
            }
            return s.length();
        }).reversed());
        for (String element : classpath) {
            String str = element;
            while (true) {
                String candidate = str;
                if (fileName2.endsWith(candidate)) {
                    matches.put(candidate, element);
                    break;
                }
                int slash = candidate.indexOf(47);
                if (slash < 0) {
                    break;
                }
                str = candidate.substring(slash + 1);
            }
        }
        if (matches.isEmpty()) {
            return null;
        }
        return matches.get(matches.firstKey());
    }

    protected static void grabFilesAndDirs(String file, List<String> dirs, List<String> files) throws IOException {
        org.apache.tools.zip.ZipFile zf = new org.apache.tools.zip.ZipFile(file, "utf-8");
        try {
            Set<String> dirSet = new HashSet<>();
            StreamUtils.enumerationAsStream(zf.getEntries()).forEach(ze -> {
                String name = ze.getName();
                if (ze.isDirectory()) {
                    dirSet.add(name);
                } else if (!name.contains("/")) {
                    files.add(name);
                } else {
                    dirSet.add(name.substring(0, name.lastIndexOf(47) + 1));
                }
            });
            dirs.addAll(dirSet);
            zf.close();
        } catch (Throwable th) {
            try {
                zf.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [org.apache.tools.ant.types.Resource[], org.apache.tools.ant.types.Resource[][]] */
    private Resource[][] grabManifests(ResourceCollection[] rcs) {
        Resource[][] resources;
        ?? r0 = new Resource[rcs.length];
        for (int i = 0; i < rcs.length; i++) {
            if (rcs[i] instanceof FileSet) {
                resources = grabResources(new FileSet[]{(FileSet) rcs[i]});
            } else {
                resources = grabNonFileSetResources(new ResourceCollection[]{rcs[i]});
            }
            int j = 0;
            while (true) {
                if (j >= resources[0].length) {
                    break;
                }
                String name = resources[0][j].getName().replace('\\', '/');
                if (rcs[i] instanceof ArchiveFileSet) {
                    ArchiveFileSet afs = (ArchiveFileSet) rcs[i];
                    if (!afs.getFullpath(getProject()).isEmpty()) {
                        name = afs.getFullpath(getProject());
                    } else if (!afs.getPrefix(getProject()).isEmpty()) {
                        String prefix = afs.getPrefix(getProject());
                        if (!prefix.endsWith("/") && !prefix.endsWith("\\")) {
                            prefix = prefix + "/";
                        }
                        name = prefix + name;
                    }
                }
                if (!MANIFEST_NAME.equalsIgnoreCase(name)) {
                    j++;
                } else {
                    Resource[] resourceArr = new Resource[1];
                    resourceArr[0] = resources[0][j];
                    r0[i] = resourceArr;
                    break;
                }
            }
            if (r0[i] == 0) {
                r0[i] = new Resource[0];
            }
        }
        return r0;
    }

    private Charset getManifestCharset() {
        if (this.manifestEncoding == null) {
            return Charset.defaultCharset();
        }
        try {
            return Charset.forName(this.manifestEncoding);
        } catch (IllegalArgumentException e) {
            throw new BuildException("Unsupported encoding while reading manifest: " + e.getMessage(), e);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Jar$StrictMode.class */
    public static class StrictMode extends EnumeratedAttribute {
        public StrictMode() {
        }

        public StrictMode(String value) {
            setValue(value);
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"fail", "warn", Definer.OnError.POLICY_IGNORE};
        }

        public int getLogLevel() {
            return Definer.OnError.POLICY_IGNORE.equals(getValue()) ? 3 : 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Jar$IndexJarsFilenameMapper.class */
    public static class IndexJarsFilenameMapper implements FileNameMapper {
        private String[] classpath;

        IndexJarsFilenameMapper(String[] classpath) {
            this.classpath = classpath;
        }

        @Override // org.apache.tools.ant.util.FileNameMapper
        public void setFrom(String from) {
        }

        @Override // org.apache.tools.ant.util.FileNameMapper
        public void setTo(String to) {
        }

        @Override // org.apache.tools.ant.util.FileNameMapper
        public String[] mapFileName(String sourceFileName) {
            String result = Jar.findJarName(sourceFileName, this.classpath);
            if (result == null) {
                return null;
            }
            return new String[]{result};
        }
    }
}
