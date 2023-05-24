package org.apache.tools.ant.taskdefs.launcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/launcher/VmsCommandLauncher.class */
public class VmsCommandLauncher extends Java13CommandLauncher {
    @Override // org.apache.tools.ant.taskdefs.launcher.CommandLauncher
    public Process exec(Project project, String[] cmd, String[] env) throws IOException {
        File cmdFile = createCommandFile(project, cmd, env);
        Process p = super.exec(project, new String[]{cmdFile.getPath()}, env);
        deleteAfter(cmdFile, p);
        return p;
    }

    @Override // org.apache.tools.ant.taskdefs.launcher.Java13CommandLauncher, org.apache.tools.ant.taskdefs.launcher.CommandLauncher
    public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
        File cmdFile = createCommandFile(project, cmd, env);
        Process p = super.exec(project, new String[]{cmdFile.getPath()}, env, workingDir);
        deleteAfter(cmdFile, p);
        return p;
    }

    private File createCommandFile(Project project, String[] cmd, String[] env) throws IOException {
        File script = FILE_UTILS.createTempFile(project, "ANT", ".COM", null, true, true);
        BufferedWriter out = new BufferedWriter(new FileWriter(script));
        if (env != null) {
            try {
                for (String variable : env) {
                    int eqIndex = variable.indexOf(61);
                    if (eqIndex != -1) {
                        out.write("$ DEFINE/NOLOG ");
                        out.write(variable.substring(0, eqIndex));
                        out.write(" \"");
                        out.write(variable.substring(eqIndex + 1));
                        out.write(34);
                        out.newLine();
                    }
                }
            } catch (Throwable th) {
                try {
                    out.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        out.write("$ " + cmd[0]);
        for (int i = 1; i < cmd.length; i++) {
            out.write(" -");
            out.newLine();
            out.write(cmd[i]);
        }
        out.close();
        return script;
    }

    private void deleteAfter(File f, Process p) {
        new Thread(() -> {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
            }
            FileUtils.delete(f);
        }).start();
    }
}
