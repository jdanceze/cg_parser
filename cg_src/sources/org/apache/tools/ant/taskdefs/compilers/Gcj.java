package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/compilers/Gcj.class */
public class Gcj extends DefaultCompilerAdapter {
    private static final String[] CONFLICT_WITH_DASH_C = {"-o", "--main=", MSVSSConstants.FLAG_CODEDIFF, "-fjni", MSVSSConstants.FLAG_LABEL};

    @Override // org.apache.tools.ant.taskdefs.compilers.CompilerAdapter
    public boolean execute() throws BuildException {
        this.attributes.log("Using gcj compiler", 3);
        Commandline cmd = setupGCJCommand();
        int firstFileName = cmd.size();
        logAndAddFilesToCompile(cmd);
        return executeExternalCompile(cmd.getCommandline(), firstFileName) == 0;
    }

    protected Commandline setupGCJCommand() {
        Commandline cmd = new Commandline();
        Path classpath = new Path(this.project);
        Path p = getBootClassPath();
        if (!p.isEmpty()) {
            classpath.append(p);
        }
        if (this.extdirs != null || this.includeJavaRuntime) {
            classpath.addExtdirs(this.extdirs);
        }
        classpath.append(getCompileClasspath());
        if (this.compileSourcepath != null) {
            classpath.append(this.compileSourcepath);
        } else {
            classpath.append(this.src);
        }
        String exec = getJavac().getExecutable();
        cmd.setExecutable(exec == null ? "gcj" : exec);
        if (this.destDir != null) {
            cmd.createArgument().setValue("-d");
            cmd.createArgument().setFile(this.destDir);
            if (!this.destDir.exists() && !this.destDir.mkdirs() && !this.destDir.isDirectory()) {
                throw new BuildException("Can't make output directories. Maybe permission is wrong.");
            }
        }
        cmd.createArgument().setValue("-classpath");
        cmd.createArgument().setPath(classpath);
        if (this.encoding != null) {
            cmd.createArgument().setValue("--encoding=" + this.encoding);
        }
        if (this.debug) {
            cmd.createArgument().setValue("-g1");
        }
        if (this.optimize) {
            cmd.createArgument().setValue(MSVSSConstants.FLAG_OUTPUT);
        }
        if (!isNativeBuild()) {
            cmd.createArgument().setValue(MSVSSConstants.FLAG_COMMENT);
        }
        if (this.attributes.getSource() != null) {
            String source = this.attributes.getSource();
            cmd.createArgument().setValue("-fsource=" + source);
        }
        if (this.attributes.getTarget() != null) {
            String target = this.attributes.getTarget();
            cmd.createArgument().setValue("-ftarget=" + target);
        }
        addCurrentCompilerArgs(cmd);
        return cmd;
    }

    public boolean isNativeBuild() {
        boolean nativeBuild = false;
        String[] additionalArguments = getJavac().getCurrentCompilerArgs();
        for (int argsLength = 0; !nativeBuild && argsLength < additionalArguments.length; argsLength++) {
            for (int conflictLength = 0; !nativeBuild && conflictLength < CONFLICT_WITH_DASH_C.length; conflictLength++) {
                nativeBuild = additionalArguments[argsLength].startsWith(CONFLICT_WITH_DASH_C[conflictLength]);
            }
        }
        return nativeBuild;
    }
}
