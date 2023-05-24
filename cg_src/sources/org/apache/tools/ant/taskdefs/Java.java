package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ExitException;
import org.apache.tools.ant.ExitStatusException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Assertions;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Permissions;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.types.RedirectorElement;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.KeepAliveInputStream;
import org.apache.tools.ant.util.StringUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Java.class */
public class Java extends Task {
    private static final String TIMEOUT_MESSAGE = "Timeout: killed the sub-process";
    private String inputString;
    private File input;
    private File output;
    private File error;
    protected RedirectorElement redirectorElement;
    private String resultProperty;
    private CommandlineJava cmdl = new CommandlineJava();
    private Environment env = new Environment();
    private boolean fork = false;
    private boolean newEnvironment = false;
    private File dir = null;
    private boolean failOnError = false;
    private Long timeout = null;
    protected Redirector redirector = new Redirector((Task) this);
    private Permissions perm = null;
    private boolean spawn = false;
    private boolean incompatibleWithSpawn = false;

    public Java() {
    }

    public Java(Task owner) {
        bindToOwner(owner);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        File savedDir = this.dir;
        Permissions savedPermissions = this.perm;
        try {
            checkConfiguration();
            int err = executeJava();
            if (err != 0) {
                if (this.failOnError) {
                    throw new ExitStatusException("Java returned: " + err, err, getLocation());
                }
                log("Java Result: " + err, 0);
            }
            maybeSetResultPropertyValue(err);
            this.dir = savedDir;
            this.perm = savedPermissions;
        } catch (Throwable th) {
            this.dir = savedDir;
            this.perm = savedPermissions;
            throw th;
        }
    }

    public int executeJava() throws BuildException {
        return executeJava(getCommandLine());
    }

    protected void checkConfiguration() throws BuildException {
        String classname = getCommandLine().getClassname();
        String module = getCommandLine().getModule();
        String sourceFile = getCommandLine().getSourceFile();
        if (classname == null && getCommandLine().getJar() == null && module == null && sourceFile == null) {
            throw new BuildException("Classname must not be null.");
        }
        if (!this.fork && getCommandLine().getJar() != null) {
            throw new BuildException("Cannot execute a jar in non-forked mode. Please set fork='true'. ");
        }
        if (!this.fork && getCommandLine().getModule() != null) {
            throw new BuildException("Cannot execute a module in non-forked mode. Please set fork='true'. ");
        }
        if (!this.fork && sourceFile != null) {
            throw new BuildException("Cannot execute sourcefile in non-forked mode. Please set fork='true'");
        }
        if (this.spawn && !this.fork) {
            throw new BuildException("Cannot spawn a java process in non-forked mode. Please set fork='true'. ");
        }
        if (getCommandLine().getClasspath() != null && getCommandLine().getJar() != null) {
            log("When using 'jar' attribute classpath-settings are ignored. See the manual for more information.", 3);
        }
        if (this.spawn && this.incompatibleWithSpawn) {
            getProject().log("spawn does not allow attributes related to input, output, error, result", 0);
            getProject().log("spawn also does not allow timeout", 0);
            getProject().log("finally, spawn is not compatible with a nested I/O <redirector>", 0);
            throw new BuildException("You have used an attribute or nested element which is not compatible with spawn");
        }
        if (getCommandLine().getAssertions() != null && !this.fork) {
            log("Assertion statements are currently ignored in non-forked mode");
        }
        if (this.fork) {
            if (this.perm != null) {
                log("Permissions can not be set this way in forked mode.", 1);
            }
            log(getCommandLine().describeCommand(), 3);
        } else {
            if (getCommandLine().getVmCommand().size() > 1) {
                log("JVM args ignored when same JVM is used.", 1);
            }
            if (this.dir != null) {
                log("Working directory ignored when same JVM is used.", 1);
            }
            if (this.newEnvironment || null != this.env.getVariables()) {
                log("Changes to environment variables are ignored when same JVM is used.", 1);
            }
            if (getCommandLine().getBootclasspath() != null) {
                log("bootclasspath ignored when same JVM is used.", 1);
            }
            if (this.perm == null) {
                this.perm = new Permissions(true);
                log("running " + getCommandLine().getClassname() + " with default permissions (exit forbidden)", 3);
            }
            log("Running in same VM " + getCommandLine().describeJavaCommand(), 3);
        }
        setupRedirector();
    }

    protected int executeJava(CommandlineJava commandLine) {
        try {
            if (this.fork) {
                if (this.spawn) {
                    spawn(commandLine.getCommandline());
                    return 0;
                }
                return fork(commandLine.getCommandline());
            }
            try {
                run(commandLine);
                return 0;
            } catch (ExitException ex) {
                return ex.getStatus();
            }
        } catch (ThreadDeath t) {
            throw t;
        } catch (BuildException e) {
            if (e.getLocation() == null && getLocation() != null) {
                e.setLocation(getLocation());
            }
            if (this.failOnError) {
                throw e;
            }
            if (TIMEOUT_MESSAGE.equals(e.getMessage())) {
                log(TIMEOUT_MESSAGE);
                return -1;
            }
            log(e);
            return -1;
        } catch (Throwable t2) {
            if (this.failOnError) {
                throw new BuildException(t2, getLocation());
            }
            log(t2);
            return -1;
        }
    }

    public void setSpawn(boolean spawn) {
        this.spawn = spawn;
    }

    public void setClasspath(Path s) {
        createClasspath().append(s);
    }

    public Path createClasspath() {
        return getCommandLine().createClasspath(getProject()).createPath();
    }

    public Path createBootclasspath() {
        return getCommandLine().createBootclasspath(getProject()).createPath();
    }

    public void setModulepath(Path mp) {
        createModulepath().append(mp);
    }

    public Path createModulepath() {
        return getCommandLine().createModulepath(getProject()).createPath();
    }

    public void setModulepathRef(Reference r) {
        createModulepath().setRefid(r);
    }

    public Path createUpgrademodulepath() {
        return getCommandLine().createUpgrademodulepath(getProject()).createPath();
    }

    public Permissions createPermissions() {
        this.perm = this.perm == null ? new Permissions() : this.perm;
        return this.perm;
    }

    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    public void setJar(File jarfile) throws BuildException {
        if (getCommandLine().getClassname() != null || getCommandLine().getModule() != null || getCommandLine().getSourceFile() != null) {
            throw new BuildException("Cannot use combination of 'jar', 'sourcefile', 'classname', 'module' attributes in same command");
        }
        getCommandLine().setJar(jarfile.getAbsolutePath());
    }

    public void setClassname(String s) throws BuildException {
        if (getCommandLine().getJar() != null || getCommandLine().getSourceFile() != null) {
            throw new BuildException("Cannot use combination of 'jar', 'classname', sourcefile attributes in same command");
        }
        getCommandLine().setClassname(s);
    }

    public void setModule(String module) throws BuildException {
        if (getCommandLine().getJar() != null || getCommandLine().getSourceFile() != null) {
            throw new BuildException("Cannot use combination of 'jar', 'module', sourcefile attributes in same command");
        }
        getCommandLine().setModule(module);
    }

    public void setSourceFile(String sourceFile) throws BuildException {
        String jar = getCommandLine().getJar();
        String className = getCommandLine().getClassname();
        String module = getCommandLine().getModule();
        if (jar != null || className != null || module != null) {
            throw new BuildException("Cannot use 'sourcefile' in combination with 'jar' or 'module' or 'classname'");
        }
        getCommandLine().setSourceFile(sourceFile);
    }

    public void setArgs(String s) {
        log("The args attribute is deprecated. Please use nested arg elements.", 1);
        getCommandLine().createArgument().setLine(s);
    }

    public void setCloneVm(boolean cloneVm) {
        getCommandLine().setCloneVm(cloneVm);
    }

    public Commandline.Argument createArg() {
        return getCommandLine().createArgument();
    }

    public void setResultProperty(String resultProperty) {
        this.resultProperty = resultProperty;
        this.incompatibleWithSpawn = true;
    }

    protected void maybeSetResultPropertyValue(int result) {
        String res = Integer.toString(result);
        if (this.resultProperty != null) {
            getProject().setNewProperty(this.resultProperty, res);
        }
    }

    public void setFork(boolean s) {
        this.fork = s;
    }

    public void setJvmargs(String s) {
        log("The jvmargs attribute is deprecated. Please use nested jvmarg elements.", 1);
        getCommandLine().createVmArgument().setLine(s);
    }

    public Commandline.Argument createJvmarg() {
        return getCommandLine().createVmArgument();
    }

    public void setJvm(String s) {
        getCommandLine().setVm(s);
    }

    public void addSysproperty(Environment.Variable sysp) {
        getCommandLine().addSysproperty(sysp);
    }

    public void addSyspropertyset(PropertySet sysp) {
        getCommandLine().addSyspropertyset(sysp);
    }

    public void setFailonerror(boolean fail) {
        this.failOnError = fail;
        this.incompatibleWithSpawn |= fail;
    }

    public void setDir(File d) {
        this.dir = d;
    }

    public void setOutput(File out) {
        this.output = out;
        this.incompatibleWithSpawn = true;
    }

    public void setInput(File input) {
        if (this.inputString != null) {
            throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
        }
        this.input = input;
        this.incompatibleWithSpawn = true;
    }

    public void setInputString(String inputString) {
        if (this.input != null) {
            throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
        }
        this.inputString = inputString;
        this.incompatibleWithSpawn = true;
    }

    public void setLogError(boolean logError) {
        this.redirector.setLogError(logError);
        this.incompatibleWithSpawn |= logError;
    }

    public void setError(File error) {
        this.error = error;
        this.incompatibleWithSpawn = true;
    }

    public void setOutputproperty(String outputProp) {
        this.redirector.setOutputProperty(outputProp);
        this.incompatibleWithSpawn = true;
    }

    public void setErrorProperty(String errorProperty) {
        this.redirector.setErrorProperty(errorProperty);
        this.incompatibleWithSpawn = true;
    }

    public void setMaxmemory(String max) {
        getCommandLine().setMaxmemory(max);
    }

    public void setJVMVersion(String value) {
        getCommandLine().setVmversion(value);
    }

    public void addEnv(Environment.Variable var) {
        this.env.addVariable(var);
    }

    public void setNewenvironment(boolean newenv) {
        this.newEnvironment = newenv;
    }

    public void setAppend(boolean append) {
        this.redirector.setAppend(append);
        this.incompatibleWithSpawn |= append;
    }

    public void setDiscardOutput(boolean discard) {
        this.redirector.setDiscardOutput(discard);
    }

    public void setDiscardError(boolean discard) {
        this.redirector.setDiscardError(discard);
    }

    public void setTimeout(Long value) {
        this.timeout = value;
        this.incompatibleWithSpawn |= this.timeout != null;
    }

    public void addAssertions(Assertions asserts) {
        if (getCommandLine().getAssertions() != null) {
            throw new BuildException("Only one assertion declaration is allowed");
        }
        getCommandLine().setAssertions(asserts);
    }

    public void addConfiguredRedirector(RedirectorElement redirectorElement) {
        if (this.redirectorElement != null) {
            throw new BuildException("cannot have > 1 nested redirectors");
        }
        this.redirectorElement = redirectorElement;
        this.incompatibleWithSpawn = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.Task
    public void handleOutput(String output) {
        if (this.redirector.getOutputStream() != null) {
            this.redirector.handleOutput(output);
        } else {
            super.handleOutput(output);
        }
    }

    @Override // org.apache.tools.ant.Task
    public int handleInput(byte[] buffer, int offset, int length) throws IOException {
        return this.redirector.handleInput(buffer, offset, length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.Task
    public void handleFlush(String output) {
        if (this.redirector.getOutputStream() != null) {
            this.redirector.handleFlush(output);
        } else {
            super.handleFlush(output);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.Task
    public void handleErrorOutput(String output) {
        if (this.redirector.getErrorStream() != null) {
            this.redirector.handleErrorOutput(output);
        } else {
            super.handleErrorOutput(output);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.Task
    public void handleErrorFlush(String output) {
        if (this.redirector.getErrorStream() != null) {
            this.redirector.handleErrorFlush(output);
        } else {
            super.handleErrorFlush(output);
        }
    }

    protected void setupRedirector() {
        this.redirector.setInput(this.input);
        this.redirector.setInputString(this.inputString);
        this.redirector.setOutput(this.output);
        this.redirector.setError(this.error);
        if (this.redirectorElement != null) {
            this.redirectorElement.configure(this.redirector);
        }
        if (!this.spawn && this.input == null && this.inputString == null) {
            this.redirector.setInputStream(new KeepAliveInputStream(getProject().getDefaultInputStream()));
        }
    }

    private void run(CommandlineJava command) throws BuildException {
        try {
            ExecuteJava exe = new ExecuteJava();
            exe.setJavaCommand(command.getJavaCommand());
            exe.setClasspath(command.getClasspath());
            exe.setSystemProperties(command.getSystemProperties());
            exe.setPermissions(this.perm);
            exe.setTimeout(this.timeout);
            this.redirector.createStreams();
            exe.execute(getProject());
            this.redirector.complete();
            if (exe.killedProcess()) {
                throw new BuildException(TIMEOUT_MESSAGE);
            }
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    private int fork(String[] command) throws BuildException {
        Execute exe = new Execute(this.redirector.createHandler(), createWatchdog());
        setupExecutable(exe, command);
        try {
            int rc = exe.execute();
            this.redirector.complete();
            if (exe.killedProcess()) {
                throw new BuildException(TIMEOUT_MESSAGE);
            }
            return rc;
        } catch (IOException e) {
            throw new BuildException(e, getLocation());
        }
    }

    private void spawn(String[] command) throws BuildException {
        Execute exe = new Execute();
        setupExecutable(exe, command);
        try {
            exe.spawn();
        } catch (IOException e) {
            throw new BuildException(e, getLocation());
        }
    }

    private void setupExecutable(Execute exe, String[] command) {
        exe.setAntRun(getProject());
        setupWorkingDir(exe);
        setupEnvironment(exe);
        setupCommandLine(exe, command);
    }

    private void setupEnvironment(Execute exe) {
        String[] environment = this.env.getVariables();
        if (environment != null) {
            for (String element : environment) {
                log("Setting environment variable: " + element, 3);
            }
        }
        exe.setNewenvironment(this.newEnvironment);
        exe.setEnvironment(environment);
    }

    private void setupWorkingDir(Execute exe) {
        if (this.dir == null) {
            this.dir = getProject().getBaseDir();
        } else if (!this.dir.isDirectory()) {
            throw new BuildException(this.dir.getAbsolutePath() + " is not a valid directory", getLocation());
        }
        exe.setWorkingDirectory(this.dir);
    }

    private void setupCommandLine(Execute exe, String[] command) {
        if (Os.isFamily(Os.FAMILY_VMS)) {
            setupCommandLineForVMS(exe, command);
        } else {
            exe.setCommandline(command);
        }
    }

    private void setupCommandLineForVMS(Execute exe, String[] command) {
        ExecuteJava.setupCommandLineForVMS(exe, command);
    }

    protected void run(String classname, Vector<String> args) throws BuildException {
        CommandlineJava cmdj = new CommandlineJava();
        cmdj.setClassname(classname);
        args.forEach(arg -> {
            cmdj.createArgument().setValue(arg);
        });
        run(cmdj);
    }

    public void clearArgs() {
        getCommandLine().clearJavaArgs();
    }

    protected ExecuteWatchdog createWatchdog() throws BuildException {
        if (this.timeout == null) {
            return null;
        }
        return new ExecuteWatchdog(this.timeout.longValue());
    }

    private void log(Throwable t) {
        log(StringUtils.getStackTrace(t), 0);
    }

    public CommandlineJava getCommandLine() {
        return this.cmdl;
    }

    public CommandlineJava.SysProperties getSysProperties() {
        return getCommandLine().getSystemProperties();
    }
}
