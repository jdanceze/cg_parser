package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import java.util.stream.Collectors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.selectors.AbstractSelectorContainer;
import org.apache.tools.ant.types.selectors.FileSelector;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/Files.class */
public class Files extends AbstractSelectorContainer implements ResourceCollection {
    private PatternSet defaultPatterns;
    private Vector<PatternSet> additionalPatterns;
    private boolean useDefaultExcludes;
    private boolean caseSensitive;
    private boolean followSymlinks;
    private DirectoryScanner ds;

    public Files() {
        this.defaultPatterns = new PatternSet();
        this.additionalPatterns = new Vector<>();
        this.useDefaultExcludes = true;
        this.caseSensitive = true;
        this.followSymlinks = true;
        this.ds = null;
    }

    protected Files(Files f) {
        this.defaultPatterns = new PatternSet();
        this.additionalPatterns = new Vector<>();
        this.useDefaultExcludes = true;
        this.caseSensitive = true;
        this.followSymlinks = true;
        this.ds = null;
        this.defaultPatterns = f.defaultPatterns;
        this.additionalPatterns = f.additionalPatterns;
        this.useDefaultExcludes = f.useDefaultExcludes;
        this.caseSensitive = f.caseSensitive;
        this.followSymlinks = f.followSymlinks;
        this.ds = f.ds;
        setProject(f.getProject());
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) throws BuildException {
        if (hasPatterns(this.defaultPatterns)) {
            throw tooManyAttributes();
        }
        if (!this.additionalPatterns.isEmpty()) {
            throw noChildrenAllowed();
        }
        if (hasSelectors()) {
            throw noChildrenAllowed();
        }
        super.setRefid(r);
    }

    public synchronized PatternSet createPatternSet() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        PatternSet patterns = new PatternSet();
        this.additionalPatterns.addElement(patterns);
        this.ds = null;
        setChecked(false);
        return patterns;
    }

    public synchronized PatternSet.NameEntry createInclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.ds = null;
        return this.defaultPatterns.createInclude();
    }

    public synchronized PatternSet.NameEntry createIncludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.ds = null;
        return this.defaultPatterns.createIncludesFile();
    }

    public synchronized PatternSet.NameEntry createExclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.ds = null;
        return this.defaultPatterns.createExclude();
    }

    public synchronized PatternSet.NameEntry createExcludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.ds = null;
        return this.defaultPatterns.createExcludesFile();
    }

    public synchronized void setIncludes(String includes) {
        checkAttributesAllowed();
        this.defaultPatterns.setIncludes(includes);
        this.ds = null;
    }

    public synchronized void appendIncludes(String[] includes) {
        checkAttributesAllowed();
        if (includes != null) {
            for (String include : includes) {
                this.defaultPatterns.createInclude().setName(include);
            }
            this.ds = null;
        }
    }

    public synchronized void setExcludes(String excludes) {
        checkAttributesAllowed();
        this.defaultPatterns.setExcludes(excludes);
        this.ds = null;
    }

    public synchronized void appendExcludes(String[] excludes) {
        checkAttributesAllowed();
        if (excludes != null) {
            for (String exclude : excludes) {
                this.defaultPatterns.createExclude().setName(exclude);
            }
            this.ds = null;
        }
    }

    public synchronized void setIncludesfile(File incl) throws BuildException {
        checkAttributesAllowed();
        this.defaultPatterns.setIncludesfile(incl);
        this.ds = null;
    }

    public synchronized void setExcludesfile(File excl) throws BuildException {
        checkAttributesAllowed();
        this.defaultPatterns.setExcludesfile(excl);
        this.ds = null;
    }

    public synchronized void setDefaultexcludes(boolean useDefaultExcludes) {
        checkAttributesAllowed();
        this.useDefaultExcludes = useDefaultExcludes;
        this.ds = null;
    }

    public synchronized boolean getDefaultexcludes() {
        return isReference() ? getRef().getDefaultexcludes() : this.useDefaultExcludes;
    }

    public synchronized void setCaseSensitive(boolean caseSensitive) {
        checkAttributesAllowed();
        this.caseSensitive = caseSensitive;
        this.ds = null;
    }

    public synchronized boolean isCaseSensitive() {
        return isReference() ? getRef().isCaseSensitive() : this.caseSensitive;
    }

    public synchronized void setFollowSymlinks(boolean followSymlinks) {
        checkAttributesAllowed();
        this.followSymlinks = followSymlinks;
        this.ds = null;
    }

    public synchronized boolean isFollowSymlinks() {
        return isReference() ? getRef().isFollowSymlinks() : this.followSymlinks;
    }

    @Override // java.lang.Iterable
    public synchronized Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        ensureDirectoryScannerSetup();
        this.ds.scan();
        int fct = this.ds.getIncludedFilesCount();
        int dct = this.ds.getIncludedDirsCount();
        if (fct + dct == 0) {
            return Collections.emptyIterator();
        }
        FileResourceIterator result = new FileResourceIterator(getProject());
        if (fct > 0) {
            result.addFiles(this.ds.getIncludedFiles());
        }
        if (dct > 0) {
            result.addFiles(this.ds.getIncludedDirectories());
        }
        return result;
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public synchronized int size() {
        if (isReference()) {
            return getRef().size();
        }
        ensureDirectoryScannerSetup();
        this.ds.scan();
        return this.ds.getIncludedFilesCount() + this.ds.getIncludedDirsCount();
    }

    public synchronized boolean hasPatterns() {
        if (isReference()) {
            return getRef().hasPatterns();
        }
        dieOnCircularReference();
        return hasPatterns(this.defaultPatterns) || this.additionalPatterns.stream().anyMatch(this::hasPatterns);
    }

    @Override // org.apache.tools.ant.types.selectors.AbstractSelectorContainer, org.apache.tools.ant.types.selectors.SelectorContainer
    public synchronized void appendSelector(FileSelector selector) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        super.appendSelector(selector);
        this.ds = null;
    }

    @Override // org.apache.tools.ant.types.selectors.AbstractSelectorContainer, org.apache.tools.ant.types.DataType
    public String toString() {
        if (isReference()) {
            return getRef().toString();
        }
        return isEmpty() ? "" : (String) stream().map((v0) -> {
            return v0.toString();
        }).collect(Collectors.joining(File.pathSeparator));
    }

    @Override // org.apache.tools.ant.types.selectors.AbstractSelectorContainer, org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public synchronized Object clone() {
        if (isReference()) {
            return getRef().clone();
        }
        Files f = (Files) super.clone();
        f.defaultPatterns = (PatternSet) this.defaultPatterns.clone();
        f.additionalPatterns = new Vector<>(this.additionalPatterns.size());
        Iterator<PatternSet> it = this.additionalPatterns.iterator();
        while (it.hasNext()) {
            PatternSet ps = it.next();
            f.additionalPatterns.add((PatternSet) ps.clone());
        }
        return f;
    }

    public String[] mergeIncludes(Project p) {
        return mergePatterns(p).getIncludePatterns(p);
    }

    public String[] mergeExcludes(Project p) {
        return mergePatterns(p).getExcludePatterns(p);
    }

    public synchronized PatternSet mergePatterns(Project p) {
        if (isReference()) {
            return getRef().mergePatterns(p);
        }
        dieOnCircularReference();
        PatternSet ps = new PatternSet();
        ps.append(this.defaultPatterns, p);
        this.additionalPatterns.forEach(pat -> {
            ps.append(pat, p);
        });
        return ps;
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public boolean isFilesystemOnly() {
        return true;
    }

    protected Files getRef() {
        return (Files) getCheckedRef(Files.class);
    }

    private synchronized void ensureDirectoryScannerSetup() {
        dieOnCircularReference();
        if (this.ds == null) {
            this.ds = new DirectoryScanner();
            PatternSet ps = mergePatterns(getProject());
            this.ds.setIncludes(ps.getIncludePatterns(getProject()));
            this.ds.setExcludes(ps.getExcludePatterns(getProject()));
            this.ds.setSelectors(getSelectors(getProject()));
            if (this.useDefaultExcludes) {
                this.ds.addDefaultExcludes();
            }
            this.ds.setCaseSensitive(this.caseSensitive);
            this.ds.setFollowSymlinks(this.followSymlinks);
        }
    }

    private boolean hasPatterns(PatternSet ps) {
        String[] includePatterns = ps.getIncludePatterns(getProject());
        String[] excludePatterns = ps.getExcludePatterns(getProject());
        return (includePatterns != null && includePatterns.length > 0) || (excludePatterns != null && excludePatterns.length > 0);
    }
}
