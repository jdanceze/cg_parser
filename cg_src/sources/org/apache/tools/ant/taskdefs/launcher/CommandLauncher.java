package org.apache.tools.ant.taskdefs.launcher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/launcher/CommandLauncher.class */
public class CommandLauncher {
    protected static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private static CommandLauncher vmLauncher;
    private static CommandLauncher shellLauncher;

    static {
        vmLauncher = null;
        shellLauncher = null;
        if (!Os.isFamily(Os.FAMILY_OS2)) {
            vmLauncher = new Java13CommandLauncher();
        }
        if (Os.isFamily(Os.FAMILY_MAC) && !Os.isFamily(Os.FAMILY_UNIX)) {
            shellLauncher = new MacCommandLauncher(new CommandLauncher());
        } else if (Os.isFamily(Os.FAMILY_OS2)) {
            shellLauncher = new OS2CommandLauncher(new CommandLauncher());
        } else if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            CommandLauncher baseLauncher = new CommandLauncher();
            if (!Os.isFamily(Os.FAMILY_9X)) {
                shellLauncher = new WinNTCommandLauncher(baseLauncher);
            } else {
                shellLauncher = new ScriptCommandLauncher("bin/antRun.bat", baseLauncher);
            }
        } else if (Os.isFamily(Os.FAMILY_NETWARE)) {
            shellLauncher = new PerlScriptCommandLauncher("bin/antRun.pl", new CommandLauncher());
        } else if (Os.isFamily(Os.FAMILY_VMS)) {
            shellLauncher = new VmsCommandLauncher();
        } else {
            shellLauncher = new ScriptCommandLauncher("bin/antRun", new CommandLauncher());
        }
    }

    public Process exec(Project project, String[] cmd, String[] env) throws IOException {
        if (project != null) {
            project.log("Execute:CommandLauncher: " + Commandline.describeCommand(cmd), 4);
        }
        return Runtime.getRuntime().exec(cmd, env);
    }

    public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
        if (workingDir == null) {
            return exec(project, cmd, env);
        }
        throw new IOException("Cannot execute a process in different directory under this JVM");
    }

    public static CommandLauncher getShellLauncher(Project project) {
        CommandLauncher launcher = extractLauncher(MagicNames.ANT_SHELL_LAUNCHER_REF_ID, project);
        if (launcher == null) {
            launcher = shellLauncher;
        }
        return launcher;
    }

    public static CommandLauncher getVMLauncher(Project project) {
        CommandLauncher launcher = extractLauncher(MagicNames.ANT_VM_LAUNCHER_REF_ID, project);
        if (launcher == null) {
            launcher = vmLauncher;
        }
        return launcher;
    }

    private static CommandLauncher extractLauncher(String referenceName, Project project) {
        return (CommandLauncher) Optional.ofNullable(project).map(p -> {
            return (CommandLauncher) p.getReference(referenceName);
        }).orElseGet(() -> {
            return getSystemLauncher(referenceName);
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static CommandLauncher getSystemLauncher(String launcherRefId) {
        String launcherClass = System.getProperty(launcherRefId);
        if (launcherClass != null) {
            try {
                return (CommandLauncher) Class.forName(launcherClass).asSubclass(CommandLauncher.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                System.err.println("Could not instantiate launcher class " + launcherClass + ": " + e.getMessage());
                return null;
            }
        }
        return null;
    }

    public static void setVMLauncher(Project project, CommandLauncher launcher) {
        if (project != null) {
            project.addReference(MagicNames.ANT_VM_LAUNCHER_REF_ID, launcher);
        }
    }

    public static void setShellLauncher(Project project, CommandLauncher launcher) {
        if (project != null) {
            project.addReference(MagicNames.ANT_SHELL_LAUNCHER_REF_ID, launcher);
        }
    }
}
