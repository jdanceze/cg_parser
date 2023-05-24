package org.apache.tools.ant.types.optional.depend;

import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.StreamUtils;
import org.apache.tools.ant.util.depend.DependencyAnalyzer;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/optional/depend/DependScanner.class */
public class DependScanner extends DirectoryScanner {
    public static final String DEFAULT_ANALYZER_CLASS = "org.apache.tools.ant.util.depend.bcel.FullAnalyzer";
    private Vector<String> rootClasses;
    private Vector<String> included;
    private Vector<File> additionalBaseDirs = new Vector<>();
    private DirectoryScanner parentScanner;

    public DependScanner(DirectoryScanner parentScanner) {
        this.parentScanner = parentScanner;
    }

    public synchronized void setRootClasses(Vector<String> rootClasses) {
        this.rootClasses = rootClasses;
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public String[] getIncludedFiles() {
        return (String[]) this.included.toArray(new String[getIncludedFilesCount()]);
    }

    @Override // org.apache.tools.ant.DirectoryScanner
    public synchronized int getIncludedFilesCount() {
        if (this.included == null) {
            throw new IllegalStateException();
        }
        return this.included.size();
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public synchronized void scan() throws IllegalStateException {
        this.included = new Vector<>();
        try {
            DependencyAnalyzer analyzer = (DependencyAnalyzer) Class.forName("org.apache.tools.ant.util.depend.bcel.FullAnalyzer").asSubclass(DependencyAnalyzer.class).newInstance();
            analyzer.addClassPath(new Path(null, this.basedir.getPath()));
            Stream map = this.additionalBaseDirs.stream().map((v0) -> {
                return v0.getPath();
            }).map(p -> {
                return new Path(null, p);
            });
            Objects.requireNonNull(analyzer);
            map.forEach(this::addClassPath);
            Vector<String> vector = this.rootClasses;
            Objects.requireNonNull(analyzer);
            vector.forEach(this::addRootClass);
            Set<String> parentSet = (Set) Stream.of((Object[]) this.parentScanner.getIncludedFiles()).collect(Collectors.toSet());
            StreamUtils.enumerationAsStream(analyzer.getClassDependencies()).map(cName -> {
                return cName.replace('.', File.separatorChar) + ".class";
            }).filter(fName -> {
                return new File(this.basedir, parentSet).exists() && parentSet.contains(parentSet);
            }).forEach(fName2 -> {
                this.included.addElement(fName2);
            });
        } catch (Exception e) {
            throw new BuildException("Unable to load dependency analyzer: org.apache.tools.ant.util.depend.bcel.FullAnalyzer", e);
        }
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public void addDefaultExcludes() {
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public String[] getExcludedDirectories() {
        return null;
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public String[] getExcludedFiles() {
        return null;
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public String[] getIncludedDirectories() {
        return new String[0];
    }

    @Override // org.apache.tools.ant.DirectoryScanner
    public int getIncludedDirsCount() {
        return 0;
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public String[] getNotIncludedDirectories() {
        return null;
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public String[] getNotIncludedFiles() {
        return null;
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public void setExcludes(String[] excludes) {
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public void setIncludes(String[] includes) {
    }

    @Override // org.apache.tools.ant.DirectoryScanner, org.apache.tools.ant.FileScanner
    public void setCaseSensitive(boolean isCaseSensitive) {
    }

    public void addBasedir(File baseDir) {
        this.additionalBaseDirs.addElement(baseDir);
    }
}
