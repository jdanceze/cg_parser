package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Chmod.class */
public class Chmod extends ExecuteOn {
    private FileSet defaultSet = new FileSet();
    private boolean defaultSetDefined = false;
    private boolean havePerm = false;

    public Chmod() {
        super.setExecutable("chmod");
        super.setParallel(true);
        super.setSkipEmptyFilesets(true);
    }

    @Override // org.apache.tools.ant.ProjectComponent
    public void setProject(Project project) {
        super.setProject(project);
        this.defaultSet.setProject(project);
    }

    public void setFile(File src) {
        FileSet fs = new FileSet();
        fs.setFile(src);
        addFileset(fs);
    }

    @Override // org.apache.tools.ant.taskdefs.ExecTask
    public void setDir(File src) {
        this.defaultSet.setDir(src);
    }

    public void setPerm(String perm) {
        createArg().setValue(perm);
        this.havePerm = true;
    }

    public PatternSet.NameEntry createInclude() {
        this.defaultSetDefined = true;
        return this.defaultSet.createInclude();
    }

    public PatternSet.NameEntry createExclude() {
        this.defaultSetDefined = true;
        return this.defaultSet.createExclude();
    }

    public PatternSet createPatternSet() {
        this.defaultSetDefined = true;
        return this.defaultSet.createPatternSet();
    }

    public void setIncludes(String includes) {
        this.defaultSetDefined = true;
        this.defaultSet.setIncludes(includes);
    }

    public void setExcludes(String excludes) {
        this.defaultSetDefined = true;
        this.defaultSet.setExcludes(excludes);
    }

    public void setDefaultexcludes(boolean useDefaultExcludes) {
        this.defaultSetDefined = true;
        this.defaultSet.setDefaultexcludes(useDefaultExcludes);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.ExecuteOn, org.apache.tools.ant.taskdefs.ExecTask
    public void checkConfiguration() {
        if (!this.havePerm) {
            throw new BuildException("Required attribute perm not set in chmod", getLocation());
        }
        if (this.defaultSetDefined && this.defaultSet.getDir(getProject()) != null) {
            addFileset(this.defaultSet);
        }
        super.checkConfiguration();
    }

    @Override // org.apache.tools.ant.taskdefs.ExecTask, org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.defaultSetDefined || this.defaultSet.getDir(getProject()) == null) {
            try {
                super.execute();
            } finally {
                if (this.defaultSetDefined && this.defaultSet.getDir(getProject()) != null) {
                    this.filesets.removeElement(this.defaultSet);
                }
            }
        } else if (isValidOs()) {
            Execute execute = prepareExec();
            Commandline cloned = (Commandline) this.cmdl.clone();
            cloned.createArgument().setValue(this.defaultSet.getDir(getProject()).getPath());
            try {
                try {
                    execute.setCommandline(cloned.getCommandline());
                    runExecute(execute);
                    logFlush();
                } catch (IOException e) {
                    throw new BuildException("Execute failed: " + e, e, getLocation());
                }
            } catch (Throwable th) {
                logFlush();
                throw th;
            }
        }
    }

    @Override // org.apache.tools.ant.taskdefs.ExecTask
    public void setExecutable(String e) {
        throw new BuildException(getTaskType() + " doesn't support the executable attribute", getLocation());
    }

    @Override // org.apache.tools.ant.taskdefs.ExecTask
    public void setCommand(Commandline cmdl) {
        throw new BuildException(getTaskType() + " doesn't support the command attribute", getLocation());
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteOn
    public void setSkipEmptyFilesets(boolean skip) {
        throw new BuildException(getTaskType() + " doesn't support the skipemptyfileset attribute", getLocation());
    }

    @Override // org.apache.tools.ant.taskdefs.ExecuteOn
    public void setAddsourcefile(boolean b) {
        throw new BuildException(getTaskType() + " doesn't support the addsourcefile attribute", getLocation());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.ExecTask
    public boolean isValidOs() {
        return (getOs() == null && getOsFamily() == null) ? Os.isFamily(Os.FAMILY_UNIX) : super.isValidOs();
    }
}
