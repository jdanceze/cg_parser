package org.apache.tools.ant.taskdefs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.launch.Launcher;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/AbstractCvsTask.class */
public abstract class AbstractCvsTask extends Task {
    public static final int DEFAULT_COMPRESSION_LEVEL = 3;
    private static final int MAXIMUM_COMRESSION_LEVEL = 9;
    private String cvsRoot;
    private String cvsRsh;
    private String cvsPackage;
    private String tag;
    private static final String DEFAULT_COMMAND = "checkout";
    private File dest;
    private File output;
    private File error;
    private ExecuteStreamHandler executeStreamHandler;
    private OutputStream outputStream;
    private OutputStream errorStream;
    private Commandline cmd = new Commandline();
    private List<Module> modules = new ArrayList();
    private List<Commandline> commandlines = new Vector();
    private String command = null;
    private boolean quiet = false;
    private boolean reallyquiet = false;
    private int compression = 0;
    private boolean noexec = false;
    private int port = 0;
    private File passFile = null;
    private boolean append = false;
    private boolean failOnError = false;

    public void setExecuteStreamHandler(ExecuteStreamHandler handler) {
        this.executeStreamHandler = handler;
    }

    protected ExecuteStreamHandler getExecuteStreamHandler() {
        if (this.executeStreamHandler == null) {
            setExecuteStreamHandler(new PumpStreamHandler(getOutputStream(), getErrorStream()));
        }
        return this.executeStreamHandler;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    protected OutputStream getOutputStream() {
        if (this.outputStream == null) {
            if (this.output != null) {
                try {
                    setOutputStream(new PrintStream(new BufferedOutputStream(FileUtils.newOutputStream(Paths.get(this.output.getPath(), new String[0]), this.append))));
                } catch (IOException e) {
                    throw new BuildException(e, getLocation());
                }
            } else {
                setOutputStream(new LogOutputStream((Task) this, 2));
            }
        }
        return this.outputStream;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setErrorStream(OutputStream errorStream) {
        this.errorStream = errorStream;
    }

    protected OutputStream getErrorStream() {
        if (this.errorStream == null) {
            if (this.error != null) {
                try {
                    setErrorStream(new PrintStream(new BufferedOutputStream(FileUtils.newOutputStream(Paths.get(this.error.getPath(), new String[0]), this.append))));
                } catch (IOException e) {
                    throw new BuildException(e, getLocation());
                }
            } else {
                setErrorStream(new LogOutputStream((Task) this, 1));
            }
        }
        return this.errorStream;
    }

    protected void runCommand(Commandline toExecute) throws BuildException {
        Environment env = new Environment();
        if (this.port > 0) {
            Environment.Variable var = new Environment.Variable();
            var.setKey("CVS_CLIENT_PORT");
            var.setValue(String.valueOf(this.port));
            env.addVariable(var);
            Environment.Variable var2 = new Environment.Variable();
            var2.setKey("CVS_PSERVER_PORT");
            var2.setValue(String.valueOf(this.port));
            env.addVariable(var2);
        }
        if (this.passFile == null) {
            File defaultPassFile = new File(System.getProperty("cygwin.user.home", System.getProperty(Launcher.USER_HOMEDIR)) + File.separatorChar + ".cvspass");
            if (defaultPassFile.exists()) {
                setPassfile(defaultPassFile);
            }
        }
        if (this.passFile != null) {
            if (this.passFile.isFile() && this.passFile.canRead()) {
                Environment.Variable var3 = new Environment.Variable();
                var3.setKey("CVS_PASSFILE");
                var3.setValue(String.valueOf(this.passFile));
                env.addVariable(var3);
                log("Using cvs passfile: " + String.valueOf(this.passFile), 3);
            } else if (!this.passFile.canRead()) {
                log("cvs passfile: " + String.valueOf(this.passFile) + " ignored as it is not readable", 1);
            } else {
                log("cvs passfile: " + String.valueOf(this.passFile) + " ignored as it is not a file", 1);
            }
        }
        if (this.cvsRsh != null) {
            Environment.Variable var4 = new Environment.Variable();
            var4.setKey("CVS_RSH");
            var4.setValue(String.valueOf(this.cvsRsh));
            env.addVariable(var4);
        }
        Execute exe = new Execute(getExecuteStreamHandler(), null);
        exe.setAntRun(getProject());
        if (this.dest == null) {
            this.dest = getProject().getBaseDir();
        }
        if (!this.dest.exists()) {
            this.dest.mkdirs();
        }
        exe.setWorkingDirectory(this.dest);
        exe.setCommandline(toExecute.getCommandline());
        exe.setEnvironment(env.getVariables());
        try {
            String actualCommandLine = executeToString(exe);
            log(actualCommandLine, 3);
            int retCode = exe.execute();
            log("retCode=" + retCode, 4);
            if (this.failOnError && Execute.isFailure(retCode)) {
                throw new BuildException(String.format("cvs exited with error code %s%nCommand line was [%s]", Integer.valueOf(retCode), actualCommandLine), getLocation());
            }
        } catch (IOException e) {
            if (this.failOnError) {
                throw new BuildException(e, getLocation());
            }
            log("Caught exception: " + e.getMessage(), 1);
        } catch (BuildException e2) {
            if (this.failOnError) {
                throw e2;
            }
            Throwable t = e2.getCause();
            if (t == null) {
                t = e2;
            }
            log("Caught exception: " + t.getMessage(), 1);
        } catch (Exception e3) {
            if (this.failOnError) {
                throw new BuildException(e3, getLocation());
            }
            log("Caught exception: " + e3.getMessage(), 1);
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        String savedCommand = getCommand();
        if (getCommand() == null && this.commandlines.isEmpty()) {
            setCommand("checkout");
        }
        String c = getCommand();
        Commandline cloned = null;
        if (c != null) {
            cloned = (Commandline) this.cmd.clone();
            cloned.createArgument(true).setLine(c);
            addConfiguredCommandline(cloned, true);
        }
        try {
            this.commandlines.forEach(this::runCommand);
            if (cloned != null) {
                removeCommandline(cloned);
            }
            setCommand(savedCommand);
            FileUtils.close(this.outputStream);
            FileUtils.close(this.errorStream);
        } catch (Throwable th) {
            if (cloned != null) {
                removeCommandline(cloned);
            }
            setCommand(savedCommand);
            FileUtils.close(this.outputStream);
            FileUtils.close(this.errorStream);
            throw th;
        }
    }

    private String executeToString(Execute execute) {
        String cmdLine = Commandline.describeCommand(execute.getCommandline());
        StringBuilder buf = removeCvsPassword(cmdLine);
        String[] variableArray = execute.getEnvironment();
        if (variableArray != null) {
            buf.append((String) Arrays.stream(variableArray).map(variable -> {
                return String.format("%n\t%s", variable);
            }).collect(Collectors.joining("", String.format("%n%nenvironment:%n", new Object[0]), "")));
        }
        return buf.toString();
    }

    private StringBuilder removeCvsPassword(String cmdLine) {
        StringBuilder buf = new StringBuilder(cmdLine);
        int start = cmdLine.indexOf("-d:");
        if (start >= 0) {
            int stop = cmdLine.indexOf(64, start);
            int startproto = cmdLine.indexOf(58, start);
            int startuser = cmdLine.indexOf(58, startproto + 1);
            int startpass = cmdLine.indexOf(58, startuser + 1);
            if (stop >= 0 && startpass > startproto && startpass < stop) {
                for (int i = startpass + 1; i < stop; i++) {
                    buf.replace(i, i + 1, "*");
                }
            }
        }
        return buf;
    }

    public void setCvsRoot(String root) {
        if (root != null && root.trim().isEmpty()) {
            root = null;
        }
        this.cvsRoot = root;
    }

    public String getCvsRoot() {
        return this.cvsRoot;
    }

    public void setCvsRsh(String rsh) {
        if (rsh != null && rsh.trim().isEmpty()) {
            rsh = null;
        }
        this.cvsRsh = rsh;
    }

    public String getCvsRsh() {
        return this.cvsRsh;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public void setPassfile(File passFile) {
        this.passFile = passFile;
    }

    public File getPassFile() {
        return this.passFile;
    }

    public void setDest(File dest) {
        this.dest = dest;
    }

    public File getDest() {
        return this.dest;
    }

    public void setPackage(String p) {
        this.cvsPackage = p;
    }

    public String getPackage() {
        return this.cvsPackage;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String p) {
        if (p != null && !p.trim().isEmpty()) {
            this.tag = p;
            addCommandArgument("-r" + p);
        }
    }

    public void addCommandArgument(String arg) {
        addCommandArgument(this.cmd, arg);
    }

    public void addCommandArgument(Commandline c, String arg) {
        c.createArgument().setValue(arg);
    }

    public void setDate(String p) {
        if (p != null && !p.trim().isEmpty()) {
            addCommandArgument(MSVSSConstants.FLAG_CODEDIFF);
            addCommandArgument(p);
        }
    }

    public void setCommand(String c) {
        this.command = c;
    }

    public String getCommand() {
        return this.command;
    }

    public void setQuiet(boolean q) {
        this.quiet = q;
    }

    public void setReallyquiet(boolean q) {
        this.reallyquiet = q;
    }

    public void setNoexec(boolean ne) {
        this.noexec = ne;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public void setError(File error) {
        this.error = error;
    }

    public void setAppend(boolean value) {
        this.append = value;
    }

    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    protected void configureCommandline(Commandline c) {
        if (c == null) {
            return;
        }
        c.setExecutable("cvs");
        if (this.cvsPackage != null) {
            c.createArgument().setLine(this.cvsPackage);
        }
        for (Module m : this.modules) {
            c.createArgument().setValue(m.getName());
        }
        if (this.compression > 0 && this.compression <= 9) {
            c.createArgument(true).setValue("-z" + this.compression);
        }
        if (this.quiet && !this.reallyquiet) {
            c.createArgument(true).setValue("-q");
        }
        if (this.reallyquiet) {
            c.createArgument(true).setValue("-Q");
        }
        if (this.noexec) {
            c.createArgument(true).setValue("-n");
        }
        if (this.cvsRoot != null) {
            c.createArgument(true).setLine("-d" + this.cvsRoot);
        }
    }

    protected void removeCommandline(Commandline c) {
        this.commandlines.remove(c);
    }

    public void addConfiguredCommandline(Commandline c) {
        addConfiguredCommandline(c, false);
    }

    public void addConfiguredCommandline(Commandline c, boolean insertAtStart) {
        if (c == null) {
            return;
        }
        configureCommandline(c);
        if (insertAtStart) {
            this.commandlines.add(0, c);
        } else {
            this.commandlines.add(c);
        }
    }

    public void setCompressionLevel(int level) {
        this.compression = level;
    }

    public void setCompression(boolean usecomp) {
        setCompressionLevel(usecomp ? 3 : 0);
    }

    public void addModule(Module m) {
        this.modules.add(m);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public List<Module> getModules() {
        return new ArrayList(this.modules);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/AbstractCvsTask$Module.class */
    public static final class Module {
        private String name;

        public void setName(String s) {
            this.name = s;
        }

        public String getName() {
            return this.name;
        }
    }
}
