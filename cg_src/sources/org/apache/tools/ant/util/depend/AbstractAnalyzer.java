package org.apache.tools.ant.util.depend;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipFile;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.VectorSet;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/depend/AbstractAnalyzer.class */
public abstract class AbstractAnalyzer implements DependencyAnalyzer {
    public static final int MAX_LOOPS = 1000;
    private Vector<File> fileDependencies;
    private Vector<String> classDependencies;
    private Path sourcePath = new Path(null);
    private Path classPath = new Path(null);
    private final Vector<String> rootClasses = new VectorSet();
    private boolean determined = false;
    private boolean closure = true;

    protected abstract void determineDependencies(Vector<File> vector, Vector<String> vector2);

    protected abstract boolean supportsFileDependencies();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractAnalyzer() {
        reset();
    }

    @Override // org.apache.tools.ant.util.depend.DependencyAnalyzer
    public void setClosure(boolean closure) {
        this.closure = closure;
    }

    @Override // org.apache.tools.ant.util.depend.DependencyAnalyzer
    public Enumeration<File> getFileDependencies() {
        if (!supportsFileDependencies()) {
            throw new BuildException("File dependencies are not supported by this analyzer");
        }
        if (!this.determined) {
            determineDependencies(this.fileDependencies, this.classDependencies);
        }
        return this.fileDependencies.elements();
    }

    @Override // org.apache.tools.ant.util.depend.DependencyAnalyzer
    public Enumeration<String> getClassDependencies() {
        if (!this.determined) {
            determineDependencies(this.fileDependencies, this.classDependencies);
        }
        return this.classDependencies.elements();
    }

    @Override // org.apache.tools.ant.util.depend.DependencyAnalyzer
    public File getClassContainer(String classname) throws IOException {
        String classLocation = classname.replace('.', '/') + ".class";
        return getResourceContainer(classLocation, this.classPath.list());
    }

    @Override // org.apache.tools.ant.util.depend.DependencyAnalyzer
    public File getSourceContainer(String classname) throws IOException {
        String sourceLocation = classname.replace('.', '/') + ".java";
        return getResourceContainer(sourceLocation, this.sourcePath.list());
    }

    @Override // org.apache.tools.ant.util.depend.DependencyAnalyzer
    public void addSourcePath(Path sourcePath) {
        if (sourcePath == null) {
            return;
        }
        this.sourcePath.append(sourcePath);
        this.sourcePath.setProject(sourcePath.getProject());
    }

    @Override // org.apache.tools.ant.util.depend.DependencyAnalyzer
    public void addClassPath(Path classPath) {
        if (classPath == null) {
            return;
        }
        this.classPath.append(classPath);
        this.classPath.setProject(classPath.getProject());
    }

    @Override // org.apache.tools.ant.util.depend.DependencyAnalyzer
    public void addRootClass(String className) {
        if (className != null && !this.rootClasses.contains(className)) {
            this.rootClasses.addElement(className);
        }
    }

    @Override // org.apache.tools.ant.util.depend.DependencyAnalyzer
    public void config(String name, Object info) {
    }

    @Override // org.apache.tools.ant.util.depend.DependencyAnalyzer
    public void reset() {
        this.rootClasses.removeAllElements();
        this.determined = false;
        this.fileDependencies = new Vector<>();
        this.classDependencies = new Vector<>();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Enumeration<String> getRootClasses() {
        return this.rootClasses.elements();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isClosureRequired() {
        return this.closure;
    }

    private File getResourceContainer(String resourceLocation, String[] paths) throws IOException {
        for (String path : paths) {
            File element = new File(path);
            if (element.exists()) {
                if (element.isDirectory()) {
                    File resource = new File(element, resourceLocation);
                    if (resource.exists()) {
                        return resource;
                    }
                } else {
                    ZipFile zipFile = new ZipFile(element);
                    try {
                        if (zipFile.getEntry(resourceLocation) != null) {
                            zipFile.close();
                            return element;
                        }
                        zipFile.close();
                    } catch (Throwable th) {
                        try {
                            zipFile.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                }
            }
        }
        return null;
    }
}
