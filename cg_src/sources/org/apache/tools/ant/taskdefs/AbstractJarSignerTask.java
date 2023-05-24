package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.filters.LineContainsRegExp;
import org.apache.tools.ant.taskdefs.optional.sos.SOSCmd;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.RedirectorElement;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.util.JavaEnvUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/AbstractJarSignerTask.class */
public abstract class AbstractJarSignerTask extends Task {
    public static final String ERROR_NO_SOURCE = "jar must be set through jar attribute or nested filesets";
    protected static final String JARSIGNER_COMMAND = "jarsigner";
    protected File jar;
    protected String alias;
    protected String keystore;
    protected String storepass;
    protected String storetype;
    protected String keypass;
    protected boolean verbose;
    protected String maxMemory;
    private RedirectorElement redirector;
    private String executable;
    private String providerName;
    private String providerClass;
    private String providerArg;
    protected boolean strict = false;
    protected Vector<FileSet> filesets = new Vector<>();
    private Environment sysProperties = new Environment();
    private Path path = null;
    private List<Commandline.Argument> additionalArgs = new ArrayList();

    public void setMaxmemory(String max) {
        this.maxMemory = max;
    }

    public void setJar(File jar) {
        this.jar = jar;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public void setStorepass(String storepass) {
        this.storepass = storepass;
    }

    public void setStoretype(String storetype) {
        this.storetype = storetype;
    }

    public void setKeypass(String keypass) {
        this.keypass = keypass;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public void addFileset(FileSet set) {
        this.filesets.addElement(set);
    }

    public void addSysproperty(Environment.Variable sysp) {
        this.sysProperties.addVariable(sysp);
    }

    public Path createPath() {
        if (this.path == null) {
            this.path = new Path(getProject());
        }
        return this.path.createPath();
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public void setProviderClass(String providerClass) {
        this.providerClass = providerClass;
    }

    public void setProviderArg(String providerArg) {
        this.providerArg = providerArg;
    }

    public void addArg(Commandline.Argument arg) {
        this.additionalArgs.add(arg);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void beginExecution() {
        this.redirector = createRedirector();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void endExecution() {
        this.redirector = null;
    }

    private RedirectorElement createRedirector() {
        RedirectorElement result = new RedirectorElement();
        if (this.storepass != null) {
            StringBuilder input = new StringBuilder(this.storepass).append('\n');
            if (this.keypass != null) {
                input.append(this.keypass).append('\n');
            }
            result.setInputString(input.toString());
            result.setLogInputString(false);
            LineContainsRegExp filter = new LineContainsRegExp();
            RegularExpression rx = new RegularExpression();
            rx.setPattern("^(Enter Passphrase for keystore: |Enter key password for .+: )$");
            filter.addConfiguredRegexp(rx);
            filter.setNegate(true);
            result.createErrorFilterChain().addLineContainsRegExp(filter);
        }
        return result;
    }

    public RedirectorElement getRedirector() {
        return this.redirector;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setCommonOptions(ExecTask cmd) {
        if (this.maxMemory != null) {
            addValue(cmd, "-J-Xmx" + this.maxMemory);
        }
        if (this.verbose) {
            addValue(cmd, SOSCmd.FLAG_VERBOSE);
        }
        if (this.strict) {
            addValue(cmd, "-strict");
        }
        Iterator<Environment.Variable> it = this.sysProperties.getVariablesVector().iterator();
        while (it.hasNext()) {
            Environment.Variable variable = it.next();
            declareSysProperty(cmd, variable);
        }
        for (Commandline.Argument arg : this.additionalArgs) {
            addArgument(cmd, arg);
        }
    }

    protected void declareSysProperty(ExecTask cmd, Environment.Variable property) throws BuildException {
        addValue(cmd, "-J-D" + property.getContent());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void bindToKeystore(ExecTask cmd) {
        String loc;
        if (null != this.keystore) {
            addValue(cmd, "-keystore");
            File keystoreFile = getProject().resolveFile(this.keystore);
            if (keystoreFile.exists()) {
                loc = keystoreFile.getPath();
            } else {
                loc = this.keystore;
            }
            addValue(cmd, loc);
        }
        if (null != this.storetype) {
            addValue(cmd, "-storetype");
            addValue(cmd, this.storetype);
        }
        if (null != this.providerName) {
            addValue(cmd, "-providerName");
            addValue(cmd, this.providerName);
        }
        if (null != this.providerClass) {
            addValue(cmd, "-providerClass");
            addValue(cmd, this.providerClass);
            if (null != this.providerArg) {
                addValue(cmd, "-providerArg");
                addValue(cmd, this.providerArg);
            }
        } else if (null != this.providerArg) {
            log("Ignoring providerArg as providerClass has not been set");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ExecTask createJarSigner() {
        ExecTask cmd = new ExecTask(this);
        if (this.executable == null) {
            cmd.setExecutable(JavaEnvUtils.getJdkExecutable(JARSIGNER_COMMAND));
        } else {
            cmd.setExecutable(this.executable);
        }
        cmd.setTaskType(JARSIGNER_COMMAND);
        cmd.setFailonerror(true);
        cmd.addConfiguredRedirector(this.redirector);
        return cmd;
    }

    protected Vector<FileSet> createUnifiedSources() {
        Vector<FileSet> sources = new Vector<>(this.filesets);
        if (this.jar != null) {
            FileSet sourceJar = new FileSet();
            sourceJar.setProject(getProject());
            sourceJar.setFile(this.jar);
            sources.add(sourceJar);
        }
        return sources;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Path createUnifiedSourcePath() {
        Path p = this.path == null ? new Path(getProject()) : (Path) this.path.clone();
        Iterator<FileSet> it = createUnifiedSources().iterator();
        while (it.hasNext()) {
            FileSet fileSet = it.next();
            p.add(fileSet);
        }
        return p;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasResources() {
        return (this.path == null && this.filesets.isEmpty()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addValue(ExecTask cmd, String value) {
        cmd.createArg().setValue(value);
    }

    protected void addArgument(ExecTask cmd, Commandline.Argument arg) {
        cmd.createArg().copyFrom(arg);
    }
}
