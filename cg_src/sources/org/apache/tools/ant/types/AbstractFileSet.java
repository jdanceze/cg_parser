package org.apache.tools.ant.types;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.FileScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.selectors.AndSelector;
import org.apache.tools.ant.types.selectors.ContainsRegexpSelector;
import org.apache.tools.ant.types.selectors.ContainsSelector;
import org.apache.tools.ant.types.selectors.DateSelector;
import org.apache.tools.ant.types.selectors.DependSelector;
import org.apache.tools.ant.types.selectors.DepthSelector;
import org.apache.tools.ant.types.selectors.DifferentSelector;
import org.apache.tools.ant.types.selectors.ExecutableSelector;
import org.apache.tools.ant.types.selectors.ExtendSelector;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.apache.tools.ant.types.selectors.MajoritySelector;
import org.apache.tools.ant.types.selectors.NoneSelector;
import org.apache.tools.ant.types.selectors.NotSelector;
import org.apache.tools.ant.types.selectors.OrSelector;
import org.apache.tools.ant.types.selectors.OwnedBySelector;
import org.apache.tools.ant.types.selectors.PosixGroupSelector;
import org.apache.tools.ant.types.selectors.PosixPermissionsSelector;
import org.apache.tools.ant.types.selectors.PresentSelector;
import org.apache.tools.ant.types.selectors.ReadableSelector;
import org.apache.tools.ant.types.selectors.SelectSelector;
import org.apache.tools.ant.types.selectors.SelectorContainer;
import org.apache.tools.ant.types.selectors.SelectorScanner;
import org.apache.tools.ant.types.selectors.SizeSelector;
import org.apache.tools.ant.types.selectors.SymlinkSelector;
import org.apache.tools.ant.types.selectors.TypeSelector;
import org.apache.tools.ant.types.selectors.WritableSelector;
import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/AbstractFileSet.class */
public abstract class AbstractFileSet extends DataType implements Cloneable, SelectorContainer {
    private PatternSet defaultPatterns;
    private List<PatternSet> additionalPatterns;
    private List<FileSelector> selectors;
    private File dir;
    private boolean fileAttributeUsed;
    private boolean useDefaultExcludes;
    private boolean caseSensitive;
    private boolean followSymlinks;
    private boolean errorOnMissingDir;
    private int maxLevelsOfSymlinks;
    private DirectoryScanner directoryScanner;

    public AbstractFileSet() {
        this.defaultPatterns = new PatternSet();
        this.additionalPatterns = new ArrayList();
        this.selectors = new ArrayList();
        this.useDefaultExcludes = true;
        this.caseSensitive = true;
        this.followSymlinks = true;
        this.errorOnMissingDir = true;
        this.maxLevelsOfSymlinks = 5;
        this.directoryScanner = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractFileSet(AbstractFileSet fileset) {
        this.defaultPatterns = new PatternSet();
        this.additionalPatterns = new ArrayList();
        this.selectors = new ArrayList();
        this.useDefaultExcludes = true;
        this.caseSensitive = true;
        this.followSymlinks = true;
        this.errorOnMissingDir = true;
        this.maxLevelsOfSymlinks = 5;
        this.directoryScanner = null;
        this.dir = fileset.dir;
        this.defaultPatterns = fileset.defaultPatterns;
        this.additionalPatterns = fileset.additionalPatterns;
        this.selectors = fileset.selectors;
        this.useDefaultExcludes = fileset.useDefaultExcludes;
        this.caseSensitive = fileset.caseSensitive;
        this.followSymlinks = fileset.followSymlinks;
        this.errorOnMissingDir = fileset.errorOnMissingDir;
        this.maxLevelsOfSymlinks = fileset.maxLevelsOfSymlinks;
        setProject(fileset.getProject());
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference r) throws BuildException {
        if (this.dir != null || this.defaultPatterns.hasPatterns(getProject())) {
            throw tooManyAttributes();
        }
        if (!this.additionalPatterns.isEmpty()) {
            throw noChildrenAllowed();
        }
        if (!this.selectors.isEmpty()) {
            throw noChildrenAllowed();
        }
        super.setRefid(r);
    }

    public synchronized void setDir(File dir) throws BuildException {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (this.fileAttributeUsed && !getDir().equals(dir)) {
            throw dirAndFileAreMutuallyExclusive();
        }
        this.dir = dir;
        this.directoryScanner = null;
    }

    public File getDir() {
        return getDir(getProject());
    }

    public synchronized File getDir(Project p) {
        if (isReference()) {
            return getRef(p).getDir(p);
        }
        dieOnCircularReference();
        return this.dir;
    }

    public synchronized PatternSet createPatternSet() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        PatternSet patterns = new PatternSet();
        this.additionalPatterns.add(patterns);
        this.directoryScanner = null;
        return patterns;
    }

    public synchronized PatternSet.NameEntry createInclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.directoryScanner = null;
        return this.defaultPatterns.createInclude();
    }

    public synchronized PatternSet.NameEntry createIncludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.directoryScanner = null;
        return this.defaultPatterns.createIncludesFile();
    }

    public synchronized PatternSet.NameEntry createExclude() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.directoryScanner = null;
        return this.defaultPatterns.createExclude();
    }

    public synchronized PatternSet.NameEntry createExcludesFile() {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.directoryScanner = null;
        return this.defaultPatterns.createExcludesFile();
    }

    public synchronized void setFile(File file) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (this.fileAttributeUsed) {
            if (getDir().equals(file.getParentFile())) {
                String[] includes = this.defaultPatterns.getIncludePatterns(getProject());
                if (includes.length == 1 && includes[0].equals(file.getName())) {
                    return;
                }
            }
            throw new BuildException("setFile cannot be called twice with different arguments");
        } else if (getDir() != null) {
            throw dirAndFileAreMutuallyExclusive();
        } else {
            setDir(file.getParentFile());
            this.fileAttributeUsed = true;
            createInclude().setName(file.getName());
        }
    }

    public synchronized void setIncludes(String includes) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.defaultPatterns.setIncludes(includes);
        this.directoryScanner = null;
    }

    public synchronized void appendIncludes(String[] includes) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (includes != null) {
            for (String include : includes) {
                this.defaultPatterns.createInclude().setName(include);
            }
            this.directoryScanner = null;
        }
    }

    public synchronized void setExcludes(String excludes) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.defaultPatterns.setExcludes(excludes);
        this.directoryScanner = null;
    }

    public synchronized void appendExcludes(String[] excludes) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        if (excludes != null) {
            for (String exclude : excludes) {
                this.defaultPatterns.createExclude().setName(exclude);
            }
            this.directoryScanner = null;
        }
    }

    public synchronized void setIncludesfile(File incl) throws BuildException {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.defaultPatterns.setIncludesfile(incl);
        this.directoryScanner = null;
    }

    public synchronized void setExcludesfile(File excl) throws BuildException {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.defaultPatterns.setExcludesfile(excl);
        this.directoryScanner = null;
    }

    public synchronized void setDefaultexcludes(boolean useDefaultExcludes) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.useDefaultExcludes = useDefaultExcludes;
        this.directoryScanner = null;
    }

    public synchronized boolean getDefaultexcludes() {
        if (isReference()) {
            return getRef(getProject()).getDefaultexcludes();
        }
        dieOnCircularReference();
        return this.useDefaultExcludes;
    }

    public synchronized void setCaseSensitive(boolean caseSensitive) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.caseSensitive = caseSensitive;
        this.directoryScanner = null;
    }

    public synchronized boolean isCaseSensitive() {
        if (isReference()) {
            return getRef(getProject()).isCaseSensitive();
        }
        dieOnCircularReference();
        return this.caseSensitive;
    }

    public synchronized void setFollowSymlinks(boolean followSymlinks) {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.followSymlinks = followSymlinks;
        this.directoryScanner = null;
    }

    public synchronized boolean isFollowSymlinks() {
        if (isReference()) {
            return getRef(getProject()).isCaseSensitive();
        }
        dieOnCircularReference();
        return this.followSymlinks;
    }

    public void setMaxLevelsOfSymlinks(int max) {
        this.maxLevelsOfSymlinks = max;
    }

    public int getMaxLevelsOfSymlinks() {
        return this.maxLevelsOfSymlinks;
    }

    public void setErrorOnMissingDir(boolean errorOnMissingDir) {
        this.errorOnMissingDir = errorOnMissingDir;
    }

    public boolean getErrorOnMissingDir() {
        return this.errorOnMissingDir;
    }

    public DirectoryScanner getDirectoryScanner() {
        return getDirectoryScanner(getProject());
    }

    public DirectoryScanner getDirectoryScanner(Project p) {
        DirectoryScanner ds;
        if (isReference()) {
            return getRef(p).getDirectoryScanner(p);
        }
        dieOnCircularReference();
        synchronized (this) {
            if (this.directoryScanner != null && p == getProject()) {
                ds = this.directoryScanner;
            } else if (this.dir == null) {
                throw new BuildException("No directory specified for %s.", getDataTypeName());
            } else {
                if (!this.dir.exists() && this.errorOnMissingDir) {
                    throw new BuildException(this.dir.getAbsolutePath() + DirectoryScanner.DOES_NOT_EXIST_POSTFIX);
                }
                if (!this.dir.isDirectory() && this.dir.exists()) {
                    throw new BuildException("%s is not a directory.", this.dir.getAbsolutePath());
                }
                ds = new DirectoryScanner();
                setupDirectoryScanner(ds, p);
                ds.setFollowSymlinks(this.followSymlinks);
                ds.setErrorOnMissingDir(this.errorOnMissingDir);
                ds.setMaxLevelsOfSymlinks(this.maxLevelsOfSymlinks);
                this.directoryScanner = p == getProject() ? ds : this.directoryScanner;
            }
        }
        ds.scan();
        return ds;
    }

    public void setupDirectoryScanner(FileScanner ds) {
        setupDirectoryScanner(ds, getProject());
    }

    public synchronized void setupDirectoryScanner(FileScanner ds, Project p) {
        if (isReference()) {
            getRef(p).setupDirectoryScanner(ds, p);
            return;
        }
        dieOnCircularReference(p);
        if (ds == null) {
            throw new IllegalArgumentException("ds cannot be null");
        }
        ds.setBasedir(this.dir);
        PatternSet ps = mergePatterns(p);
        p.log(getDataTypeName() + ": Setup scanner in dir " + this.dir + " with " + ps, 4);
        ds.setIncludes(ps.getIncludePatterns(p));
        ds.setExcludes(ps.getExcludePatterns(p));
        if (ds instanceof SelectorScanner) {
            SelectorScanner ss = (SelectorScanner) ds;
            ss.setSelectors(getSelectors(p));
        }
        if (this.useDefaultExcludes) {
            ds.addDefaultExcludes();
        }
        ds.setCaseSensitive(this.caseSensitive);
    }

    protected AbstractFileSet getRef(Project p) {
        return (AbstractFileSet) getCheckedRef(AbstractFileSet.class, getDataTypeName(), p);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public synchronized boolean hasSelectors() {
        if (isReference()) {
            return getRef(getProject()).hasSelectors();
        }
        dieOnCircularReference();
        return !this.selectors.isEmpty();
    }

    public synchronized boolean hasPatterns() {
        if (isReference() && getProject() != null) {
            return getRef(getProject()).hasPatterns();
        }
        dieOnCircularReference();
        return this.defaultPatterns.hasPatterns(getProject()) || this.additionalPatterns.stream().anyMatch(ps -> {
            return ps.hasPatterns(getProject());
        });
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public synchronized int selectorCount() {
        if (isReference()) {
            return getRef(getProject()).selectorCount();
        }
        dieOnCircularReference();
        return this.selectors.size();
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public synchronized FileSelector[] getSelectors(Project p) {
        if (isReference()) {
            return getRef(getProject()).getSelectors(p);
        }
        dieOnCircularReference(p);
        return (FileSelector[]) this.selectors.toArray(new FileSelector[this.selectors.size()]);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public synchronized Enumeration<FileSelector> selectorElements() {
        if (isReference()) {
            return getRef(getProject()).selectorElements();
        }
        dieOnCircularReference();
        return Collections.enumeration(this.selectors);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public synchronized void appendSelector(FileSelector selector) {
        if (isReference()) {
            throw noChildrenAllowed();
        }
        this.selectors.add(selector);
        this.directoryScanner = null;
        setChecked(false);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addSelector(SelectSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addAnd(AndSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addOr(OrSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addNot(NotSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addNone(NoneSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addMajority(MajoritySelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addDate(DateSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addSize(SizeSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addDifferent(DifferentSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addFilename(FilenameSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addType(TypeSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addCustom(ExtendSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addContains(ContainsSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addPresent(PresentSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addDepth(DepthSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addDepend(DependSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addContainsRegexp(ContainsRegexpSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void addModified(ModifiedSelector selector) {
        appendSelector(selector);
    }

    public void addReadable(ReadableSelector r) {
        appendSelector(r);
    }

    public void addWritable(WritableSelector w) {
        appendSelector(w);
    }

    public void addExecutable(ExecutableSelector e) {
        appendSelector(e);
    }

    public void addSymlink(SymlinkSelector e) {
        appendSelector(e);
    }

    public void addOwnedBy(OwnedBySelector o) {
        appendSelector(o);
    }

    public void addPosixGroup(PosixGroupSelector o) {
        appendSelector(o);
    }

    public void addPosixPermissions(PosixPermissionsSelector o) {
        appendSelector(o);
    }

    @Override // org.apache.tools.ant.types.selectors.SelectorContainer
    public void add(FileSelector selector) {
        appendSelector(selector);
    }

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        if (isReference()) {
            return getRef(getProject()).toString();
        }
        dieOnCircularReference();
        return String.join(";", getDirectoryScanner().getIncludedFiles());
    }

    @Override // org.apache.tools.ant.types.DataType, org.apache.tools.ant.ProjectComponent
    public synchronized Object clone() {
        if (isReference()) {
            return getRef(getProject()).clone();
        }
        try {
            AbstractFileSet fs = (AbstractFileSet) super.clone();
            fs.defaultPatterns = (PatternSet) this.defaultPatterns.clone();
            Stream<R> map = this.additionalPatterns.stream().map((v0) -> {
                return v0.clone();
            });
            Objects.requireNonNull(PatternSet.class);
            fs.additionalPatterns = (List) map.map(this::cast).collect(Collectors.toList());
            fs.selectors = new ArrayList(this.selectors);
            return fs;
        } catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }

    public String[] mergeIncludes(Project p) {
        return mergePatterns(p).getIncludePatterns(p);
    }

    public String[] mergeExcludes(Project p) {
        return mergePatterns(p).getExcludePatterns(p);
    }

    public synchronized PatternSet mergePatterns(Project p) {
        if (isReference()) {
            return getRef(p).mergePatterns(p);
        }
        dieOnCircularReference();
        PatternSet ps = (PatternSet) this.defaultPatterns.clone();
        this.additionalPatterns.forEach(pat -> {
            ps.append(pat, p);
        });
        return ps;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
            return;
        }
        Stream<FileSelector> stream = this.selectors.stream();
        Objects.requireNonNull(DataType.class);
        Stream<FileSelector> filter = stream.filter((v1) -> {
            return r1.isInstance(v1);
        });
        Objects.requireNonNull(DataType.class);
        filter.map((v1) -> {
            return r1.cast(v1);
        }).forEach(type -> {
            pushAndInvokeCircularReferenceCheck(type, stk, p);
        });
        this.additionalPatterns.forEach(ps -> {
            pushAndInvokeCircularReferenceCheck(ps, stk, p);
        });
        setChecked(true);
    }

    private BuildException dirAndFileAreMutuallyExclusive() {
        return new BuildException("you can only specify one of the dir and file attributes");
    }
}
