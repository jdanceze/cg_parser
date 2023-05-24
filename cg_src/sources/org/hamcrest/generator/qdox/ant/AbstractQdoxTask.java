package org.hamcrest.generator.qdox.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.hamcrest.generator.qdox.JavaDocBuilder;
import org.hamcrest.generator.qdox.model.DefaultDocletTagFactory;
import org.hamcrest.generator.qdox.model.DocletTagFactory;
import org.hamcrest.generator.qdox.model.JavaClass;
import org.hamcrest.generator.qdox.model.JavaSource;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/ant/AbstractQdoxTask.class */
public abstract class AbstractQdoxTask extends Task {
    private Vector filesets = new Vector();
    protected HashMap fileMap = new HashMap();
    protected ArrayList allSources = new ArrayList();
    protected ArrayList allClasses = new ArrayList();

    public void addFileset(FileSet set) {
        this.filesets.addElement(set);
    }

    protected void buildFileMap() {
        for (int i = 0; i < this.filesets.size(); i++) {
            FileSet fs = (FileSet) this.filesets.elementAt(i);
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            String[] srcFiles = ds.getIncludedFiles();
            buildFileMap(fs.getDir(getProject()), srcFiles);
        }
    }

    protected void buildFileMap(File directory, String[] sourceFiles) {
        for (String str : sourceFiles) {
            File src = new File(directory, str);
            this.fileMap.put(src.getAbsolutePath(), src);
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        validateAttributes();
        buildFileMap();
        JavaDocBuilder builder = new JavaDocBuilder(createDocletTagFactory());
        builder.getClassLibrary().addClassLoader(getClass().getClassLoader());
        mergeBuilderSources(builder);
        JavaSource[] sources = builder.getSources();
        processSources(sources);
    }

    protected DocletTagFactory createDocletTagFactory() {
        return new DefaultDocletTagFactory();
    }

    private void mergeBuilderSources(JavaDocBuilder builder) {
        for (String sourceFile : this.fileMap.keySet()) {
            builder.addSourceTree((File) this.fileMap.get(sourceFile));
        }
    }

    protected void processSources(JavaSource[] sources) {
        for (JavaSource source : sources) {
            this.allSources.add(source);
            JavaClass[] classes = source.getClasses();
            processClasses(classes);
        }
    }

    protected void processClasses(JavaClass[] classes) {
        for (JavaClass clazz : classes) {
            this.allClasses.add(clazz);
        }
    }

    protected void validateAttributes() throws BuildException {
        if (this.filesets.size() == 0) {
            throw new BuildException("Specify at least one source fileset.");
        }
    }
}
