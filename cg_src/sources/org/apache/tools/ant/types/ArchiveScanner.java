package org.apache.tools.ant.types;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.resource.spi.work.WorkManager;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.FileResourceIterator;
import org.apache.tools.ant.types.selectors.SelectorUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/ArchiveScanner.class */
public abstract class ArchiveScanner extends DirectoryScanner {
    protected File srcFile;
    private Resource src;
    private Resource lastScannedResource;
    private String encoding;
    private Map<String, Resource> fileEntries = new TreeMap();
    private Map<String, Resource> dirEntries = new TreeMap();
    private Map<String, Resource> matchFileEntries = new TreeMap();
    private Map<String, Resource> matchDirEntries = new TreeMap();
    private boolean errorOnMissingArchive = true;

    protected abstract void fillMapsFromArchive(Resource resource, String str, Map<String, Resource> map, Map<String, Resource> map2, Map<String, Resource> map3, Map<String, Resource> map4);

    public void setErrorOnMissingArchive(boolean errorOnMissingArchive) {
        this.errorOnMissingArchive = errorOnMissingArchive;
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public void scan() {
        if (this.src != null) {
            if (!this.src.isExists() && !this.errorOnMissingArchive) {
                return;
            }
            super.scan();
        }
    }

    public void setSrc(File srcFile) {
        setSrc(new FileResource(srcFile));
    }

    public void setSrc(Resource src) {
        this.src = src;
        FileProvider fp = (FileProvider) src.as(FileProvider.class);
        if (fp != null) {
            this.srcFile = fp.getFile();
        }
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public String[] getIncludedFiles() {
        if (this.src == null) {
            return super.getIncludedFiles();
        }
        scanme();
        return (String[]) this.matchFileEntries.keySet().toArray(new String[this.matchFileEntries.size()]);
    }

    @Override // org.apache.tools.ant.DirectoryScanner
    public int getIncludedFilesCount() {
        if (this.src == null) {
            return super.getIncludedFilesCount();
        }
        scanme();
        return this.matchFileEntries.size();
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public String[] getIncludedDirectories() {
        if (this.src == null) {
            return super.getIncludedDirectories();
        }
        scanme();
        return (String[]) this.matchDirEntries.keySet().toArray(new String[this.matchDirEntries.size()]);
    }

    @Override // org.apache.tools.ant.DirectoryScanner
    public int getIncludedDirsCount() {
        if (this.src == null) {
            return super.getIncludedDirsCount();
        }
        scanme();
        return this.matchDirEntries.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Iterator<Resource> getResourceFiles(Project project) {
        if (this.src == null) {
            return new FileResourceIterator(project, getBasedir(), getIncludedFiles());
        }
        scanme();
        return this.matchFileEntries.values().iterator();
    }

    Iterator<Resource> getResourceDirectories(Project project) {
        if (this.src == null) {
            return new FileResourceIterator(project, getBasedir(), getIncludedDirectories());
        }
        scanme();
        return this.matchDirEntries.values().iterator();
    }

    public void init() {
        if (this.includes == null) {
            this.includes = new String[1];
            this.includes[0] = SelectorUtils.DEEP_TREE_MATCH;
        }
        if (this.excludes == null) {
            this.excludes = new String[0];
        }
    }

    public boolean match(String path) {
        String vpath = path;
        if (!path.isEmpty()) {
            vpath = path.replace('/', File.separatorChar).replace('\\', File.separatorChar);
            if (vpath.charAt(0) == File.separatorChar) {
                vpath = vpath.substring(1);
            }
        }
        return isIncluded(vpath) && !isExcluded(vpath);
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.types.ResourceFactory
    public Resource getResource(String name) {
        if (this.src == null) {
            return super.getResource(name);
        }
        if (name.isEmpty()) {
            return new Resource("", true, WorkManager.INDEFINITE, true);
        }
        scanme();
        if (this.fileEntries.containsKey(name)) {
            return this.fileEntries.get(name);
        }
        String name2 = trimSeparator(name);
        if (this.dirEntries.containsKey(name2)) {
            return this.dirEntries.get(name2);
        }
        return new Resource(name2);
    }

    private void scanme() {
        if (!this.src.isExists() && !this.errorOnMissingArchive) {
            return;
        }
        Resource thisresource = new Resource(this.src.getName(), this.src.isExists(), this.src.getLastModified());
        if (this.lastScannedResource != null && this.lastScannedResource.getName().equals(thisresource.getName()) && this.lastScannedResource.getLastModified() == thisresource.getLastModified()) {
            return;
        }
        init();
        this.fileEntries.clear();
        this.dirEntries.clear();
        this.matchFileEntries.clear();
        this.matchDirEntries.clear();
        fillMapsFromArchive(this.src, this.encoding, this.fileEntries, this.matchFileEntries, this.dirEntries, this.matchDirEntries);
        this.lastScannedResource = thisresource;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static final String trimSeparator(String s) {
        return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
    }
}
