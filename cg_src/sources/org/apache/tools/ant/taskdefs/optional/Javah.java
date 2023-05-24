package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.optional.javah.JavahAdapter;
import org.apache.tools.ant.taskdefs.optional.javah.JavahAdapterFactory;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.facade.FacadeTaskHelper;
import org.apache.tools.ant.util.facade.ImplementationSpecificArgument;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/Javah.class */
public class Javah extends Task {
    private String cls;
    private File destDir;
    private Path bootclasspath;
    private FacadeTaskHelper facade;
    private List<ClassArgument> classes = new Vector(2);
    private Path classpath = null;
    private File outputFile = null;
    private boolean verbose = false;
    private boolean force = false;
    private boolean old = false;
    private boolean stubs = false;
    private Vector<FileSet> files = new Vector<>();
    private JavahAdapter nestedAdapter = null;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/Javah$Settings.class */
    private enum Settings {
        cls,
        files,
        classes
    }

    public Javah() {
        this.facade = null;
        this.facade = new FacadeTaskHelper(JavahAdapterFactory.getDefault());
    }

    public void setClass(String cls) {
        this.cls = cls;
    }

    public ClassArgument createClass() {
        ClassArgument ga = new ClassArgument();
        this.classes.add(ga);
        return ga;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/Javah$ClassArgument.class */
    public class ClassArgument {
        private String name;

        public ClassArgument() {
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public void addFileSet(FileSet fs) {
        this.files.add(fs);
    }

    public String[] getClasses() {
        Stream<String> stream = Stream.concat(this.files.stream().map(fs -> {
            return fs.getDirectoryScanner(getProject()).getIncludedFiles();
        }).flatMap((v0) -> {
            return Stream.of(v0);
        }).map(s -> {
            return s.replace('\\', '.').replace('/', '.').replaceFirst("\\.class$", "");
        }), this.classes.stream().map((v0) -> {
            return v0.getName();
        }));
        if (this.cls != null) {
            stream = Stream.concat(Stream.of((Object[]) this.cls.split(",")).map((v0) -> {
                return v0.trim();
            }), stream);
        }
        return (String[]) stream.toArray(x$0 -> {
            return new String[x$0];
        });
    }

    public void setDestdir(File destDir) {
        this.destDir = destDir;
    }

    public File getDestdir() {
        return this.destDir;
    }

    public void setClasspath(Path src) {
        if (this.classpath == null) {
            this.classpath = src;
        } else {
            this.classpath.append(src);
        }
    }

    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        return this.classpath.createPath();
    }

    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    public Path getClasspath() {
        return this.classpath;
    }

    public void setBootclasspath(Path src) {
        if (this.bootclasspath == null) {
            this.bootclasspath = src;
        } else {
            this.bootclasspath.append(src);
        }
    }

    public Path createBootclasspath() {
        if (this.bootclasspath == null) {
            this.bootclasspath = new Path(getProject());
        }
        return this.bootclasspath.createPath();
    }

    public void setBootClasspathRef(Reference r) {
        createBootclasspath().setRefid(r);
    }

    public Path getBootclasspath() {
        return this.bootclasspath;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public File getOutputfile() {
        return this.outputFile;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public boolean getForce() {
        return this.force;
    }

    public void setOld(boolean old) {
        this.old = old;
    }

    public boolean getOld() {
        return this.old;
    }

    public void setStubs(boolean stubs) {
        this.stubs = stubs;
    }

    public boolean getStubs() {
        return this.stubs;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean getVerbose() {
        return this.verbose;
    }

    public void setImplementation(String impl) {
        if ("default".equals(impl)) {
            this.facade.setImplementation(JavahAdapterFactory.getDefault());
        } else {
            this.facade.setImplementation(impl);
        }
    }

    public ImplementationSpecificArgument createArg() {
        ImplementationSpecificArgument arg = new ImplementationSpecificArgument();
        this.facade.addImplementationArgument(arg);
        return arg;
    }

    public String[] getCurrentArgs() {
        return this.facade.getArgs();
    }

    public Path createImplementationClasspath() {
        return this.facade.getImplementationClasspath(getProject());
    }

    public void add(JavahAdapter adapter) {
        if (this.nestedAdapter != null) {
            throw new BuildException("Can't have more than one javah adapter");
        }
        this.nestedAdapter = adapter;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        Set<Settings> settings = EnumSet.noneOf(Settings.class);
        if (this.cls != null) {
            settings.add(Settings.cls);
        }
        if (!this.classes.isEmpty()) {
            settings.add(Settings.classes);
        }
        if (!this.files.isEmpty()) {
            settings.add(Settings.files);
        }
        if (settings.size() > 1) {
            throw new BuildException("Exactly one of " + Settings.values() + " attributes is required", getLocation());
        }
        if (this.destDir != null) {
            if (!this.destDir.isDirectory()) {
                throw new BuildException("destination directory \"" + this.destDir + "\" does not exist or is not a directory", getLocation());
            }
            if (this.outputFile != null) {
                throw new BuildException("destdir and outputFile are mutually exclusive", getLocation());
            }
        }
        if (this.classpath == null) {
            this.classpath = new Path(getProject()).concatSystemClasspath("last");
        } else {
            this.classpath = this.classpath.concatSystemClasspath(Definer.OnError.POLICY_IGNORE);
        }
        JavahAdapter ad = this.nestedAdapter != null ? this.nestedAdapter : JavahAdapterFactory.getAdapter(this.facade.getImplementation(), this, createImplementationClasspath());
        if (!ad.compile(this)) {
            throw new BuildException("compilation failed");
        }
    }

    public void logAndAddFiles(Commandline cmd) {
        logAndAddFilesToCompile(cmd);
    }

    protected void logAndAddFilesToCompile(Commandline cmd) {
        log("Compilation " + cmd.describeArguments(), 3);
        String[] c = getClasses();
        StringBuilder message = new StringBuilder("Class");
        if (c.length > 1) {
            message.append("es");
        }
        message.append(String.format(" to be compiled:%n", new Object[0]));
        for (String element : c) {
            cmd.createArgument().setValue(element);
            message.append(String.format("    %s%n", element));
        }
        log(message.toString(), 3);
    }
}
