package org.apache.tools.ant.taskdefs.compilers;

import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/compilers/JavacExternal.class */
public class JavacExternal extends DefaultCompilerAdapter {
    @Override // org.apache.tools.ant.taskdefs.compilers.CompilerAdapter
    public boolean execute() throws BuildException {
        int firstFileName;
        this.attributes.log("Using external javac compiler", 3);
        Commandline cmd = new Commandline();
        cmd.setExecutable(getJavac().getJavacExecutable());
        if (assumeJava1_3Plus()) {
            setupModernJavacCommandlineSwitches(cmd);
        } else {
            setupJavacCommandlineSwitches(cmd, true);
        }
        int openVmsFirstFileName = assumeJava1_2Plus() ? cmd.size() : -1;
        logAndAddFilesToCompile(cmd);
        if (Os.isFamily(Os.FAMILY_VMS)) {
            return execOnVMS(cmd, openVmsFirstFileName);
        }
        String[] commandLine = cmd.getCommandline();
        if (assumeJava1_2Plus()) {
            firstFileName = moveArgFileEligibleOptionsToEnd(commandLine);
        } else {
            firstFileName = -1;
        }
        return executeExternalCompile(commandLine, firstFileName, true) == 0;
    }

    private int moveArgFileEligibleOptionsToEnd(String[] commandLine) {
        int nonArgFileOptionIdx = 1;
        while (nonArgFileOptionIdx < commandLine.length && !isArgFileEligible(commandLine[nonArgFileOptionIdx])) {
            nonArgFileOptionIdx++;
        }
        for (int i = nonArgFileOptionIdx + 1; i < commandLine.length; i++) {
            if (!isArgFileEligible(commandLine[i])) {
                String option = commandLine[i];
                for (int j = i - 1; j >= nonArgFileOptionIdx; j--) {
                    commandLine[j + 1] = commandLine[j];
                }
                commandLine[nonArgFileOptionIdx] = option;
                nonArgFileOptionIdx++;
            }
        }
        return nonArgFileOptionIdx;
    }

    private static boolean isArgFileEligible(String option) {
        return (option.startsWith("-J") || option.startsWith("@")) ? false : true;
    }

    private boolean execOnVMS(Commandline cmd, int firstFileName) {
        boolean vmsFile = null;
        try {
            try {
                boolean vmsFile2 = JavaEnvUtils.createVmsJavaOptionFile(cmd.getArguments());
                String[] commandLine = {cmd.getExecutable(), MSVSSConstants.FLAG_VERSION, vmsFile2.getPath()};
                return 0 == executeExternalCompile(commandLine, firstFileName, true);
            } catch (IOException e) {
                throw new BuildException("Failed to create a temporary file for \"-V\" switch");
            }
        } finally {
            FileUtils.delete(vmsFile);
        }
    }
}
