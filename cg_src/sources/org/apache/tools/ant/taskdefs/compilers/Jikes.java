package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.optional.sos.SOSCmd;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/compilers/Jikes.class */
public class Jikes extends DefaultCompilerAdapter {
    @Override // org.apache.tools.ant.taskdefs.compilers.CompilerAdapter
    public boolean execute() throws BuildException {
        Path sourcepath;
        this.attributes.log("Using jikes compiler", 3);
        Commandline cmd = new Commandline();
        if (this.compileSourcepath != null) {
            sourcepath = this.compileSourcepath;
        } else {
            sourcepath = this.src;
        }
        if (!sourcepath.isEmpty()) {
            cmd.createArgument().setValue("-sourcepath");
            cmd.createArgument().setPath(sourcepath);
        }
        Path classpath = new Path(this.project);
        if (this.bootclasspath == null || this.bootclasspath.isEmpty()) {
            this.includeJavaRuntime = true;
        }
        classpath.append(getCompileClasspath());
        String jikesPath = System.getProperty("jikes.class.path");
        if (jikesPath != null) {
            classpath.append(new Path(this.project, jikesPath));
        }
        if (this.extdirs != null && !this.extdirs.isEmpty()) {
            cmd.createArgument().setValue("-extdirs");
            cmd.createArgument().setPath(this.extdirs);
        }
        String exec = getJavac().getExecutable();
        cmd.setExecutable(exec == null ? "jikes" : exec);
        if (this.deprecation) {
            cmd.createArgument().setValue("-deprecation");
        }
        if (this.destDir != null) {
            cmd.createArgument().setValue("-d");
            cmd.createArgument().setFile(this.destDir);
        }
        cmd.createArgument().setValue("-classpath");
        cmd.createArgument().setPath(classpath);
        if (this.encoding != null) {
            cmd.createArgument().setValue("-encoding");
            cmd.createArgument().setValue(this.encoding);
        }
        if (this.debug) {
            String debugLevel = this.attributes.getDebugLevel();
            if (debugLevel != null) {
                cmd.createArgument().setValue("-g:" + debugLevel);
            } else {
                cmd.createArgument().setValue("-g");
            }
        } else {
            cmd.createArgument().setValue("-g:none");
        }
        if (this.optimize) {
            cmd.createArgument().setValue(MSVSSConstants.FLAG_OUTPUT);
        }
        if (this.verbose) {
            cmd.createArgument().setValue(SOSCmd.FLAG_VERBOSE);
        }
        if (this.depend) {
            cmd.createArgument().setValue("-depend");
        }
        if (this.target != null) {
            cmd.createArgument().setValue("-target");
            cmd.createArgument().setValue(this.target);
        }
        addPropertyParams(cmd);
        if (this.attributes.getSource() != null) {
            cmd.createArgument().setValue("-source");
            String source = this.attributes.getSource();
            if ("1.1".equals(source) || "1.2".equals(source)) {
                this.attributes.log("Jikes doesn't support '-source " + source + "', will use '-source 1.3' instead");
                cmd.createArgument().setValue("1.3");
            } else {
                cmd.createArgument().setValue(source);
            }
        }
        addCurrentCompilerArgs(cmd);
        int firstFileName = cmd.size();
        Path boot = getBootClassPath();
        if (!boot.isEmpty()) {
            cmd.createArgument().setValue("-bootclasspath");
            cmd.createArgument().setPath(boot);
        }
        logAndAddFilesToCompile(cmd);
        return executeExternalCompile(cmd.getCommandline(), firstFileName) == 0;
    }

    private void addPropertyParams(Commandline cmd) {
        String emacsProperty = this.project.getProperty("build.compiler.emacs");
        if (emacsProperty != null && Project.toBoolean(emacsProperty)) {
            cmd.createArgument().setValue("+E");
        }
        String warningsProperty = this.project.getProperty("build.compiler.warnings");
        if (warningsProperty != null) {
            this.attributes.log("!! the build.compiler.warnings property is deprecated. !!", 1);
            this.attributes.log("!! Use the nowarn attribute instead. !!", 1);
            if (!Project.toBoolean(warningsProperty)) {
                cmd.createArgument().setValue("-nowarn");
            }
        }
        if (this.attributes.getNowarn()) {
            cmd.createArgument().setValue("-nowarn");
        }
        String pedanticProperty = this.project.getProperty("build.compiler.pedantic");
        if (pedanticProperty != null && Project.toBoolean(pedanticProperty)) {
            cmd.createArgument().setValue("+P");
        }
        String fullDependProperty = this.project.getProperty("build.compiler.fulldepend");
        if (fullDependProperty != null && Project.toBoolean(fullDependProperty)) {
            cmd.createArgument().setValue("+F");
        }
    }
}
