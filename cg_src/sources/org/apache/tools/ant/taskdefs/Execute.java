package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.taskdefs.launcher.CommandLauncher;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Execute.class */
public class Execute {
    private static final int ONE_SECOND = 1000;
    public static final int INVALID = Integer.MAX_VALUE;
    private static String antWorkingDirectory = System.getProperty("user.dir");
    private static Map<String, String> procEnvironment = null;
    private static ProcessDestroyer processDestroyer = new ProcessDestroyer();
    private static boolean environmentCaseInSensitive;
    private String[] cmdl;
    private String[] env;
    private int exitValue;
    private ExecuteStreamHandler streamHandler;
    private final ExecuteWatchdog watchdog;
    private File workingDirectory;
    private Project project;
    private boolean newEnvironment;
    private boolean useVMLauncher;

    static {
        environmentCaseInSensitive = false;
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            environmentCaseInSensitive = true;
        }
    }

    @Deprecated
    public void setSpawn(boolean spawn) {
    }

    public static synchronized Map<String, String> getEnvironmentVariables() {
        BufferedReader in;
        if (procEnvironment != null) {
            return procEnvironment;
        }
        if (!Os.isFamily(Os.FAMILY_VMS)) {
            try {
                procEnvironment = System.getenv();
                return procEnvironment;
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        procEnvironment = new LinkedHashMap();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Execute exe = new Execute(new PumpStreamHandler(out));
            exe.setCommandline(getProcEnvCommand());
            exe.setNewenvironment(true);
            int retval = exe.execute();
            if (retval != 0) {
            }
            in = new BufferedReader(new StringReader(toString(out)));
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        if (Os.isFamily(Os.FAMILY_VMS)) {
            procEnvironment = getVMSLogicals(in);
            return procEnvironment;
        }
        StringBuilder var = null;
        while (true) {
            String line = in.readLine();
            if (line == null) {
                break;
            } else if (line.contains("=")) {
                if (var != null) {
                    int eq = var.toString().indexOf(61);
                    procEnvironment.put(var.substring(0, eq), var.substring(eq + 1));
                }
                var = new StringBuilder(line);
            } else if (var == null) {
                var = new StringBuilder(System.lineSeparator() + line);
            } else {
                var.append(System.lineSeparator()).append(line);
            }
        }
        if (var != null) {
            int eq2 = var.toString().indexOf(61);
            procEnvironment.put(var.substring(0, eq2), var.substring(eq2 + 1));
        }
        return procEnvironment;
    }

    @Deprecated
    public static synchronized Vector<String> getProcEnvironment() {
        Vector<String> v = new Vector<>();
        getEnvironmentVariables().forEach(key, value -> {
            v.add(key + "=" + value);
        });
        return v;
    }

    private static String[] getProcEnvCommand() {
        if (Os.isFamily(Os.FAMILY_OS2)) {
            return new String[]{"cmd", "/c", "set"};
        }
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            return Os.isFamily(Os.FAMILY_9X) ? new String[]{"command.com", "/c", "set"} : new String[]{"cmd", "/c", "set"};
        } else if (Os.isFamily(Os.FAMILY_ZOS) || Os.isFamily(Os.FAMILY_UNIX)) {
            String[] cmd = new String[1];
            if (new File("/bin/env").canRead()) {
                cmd[0] = "/bin/env";
            } else if (new File("/usr/bin/env").canRead()) {
                cmd[0] = "/usr/bin/env";
            } else {
                cmd[0] = "env";
            }
            return cmd;
        } else if (Os.isFamily(Os.FAMILY_NETWARE) || Os.isFamily(Os.FAMILY_OS400)) {
            return new String[]{"env"};
        } else {
            if (Os.isFamily(Os.FAMILY_VMS)) {
                return new String[]{"show", "logical"};
            }
            return null;
        }
    }

    public static String toString(ByteArrayOutputStream bos) {
        if (Os.isFamily(Os.FAMILY_ZOS)) {
            try {
                return bos.toString("Cp1047");
            } catch (UnsupportedEncodingException e) {
            }
        } else if (Os.isFamily(Os.FAMILY_OS400)) {
            try {
                return bos.toString("Cp500");
            } catch (UnsupportedEncodingException e2) {
            }
        }
        return bos.toString();
    }

    public Execute() {
        this(new PumpStreamHandler(), null);
    }

    public Execute(ExecuteStreamHandler streamHandler) {
        this(streamHandler, null);
    }

    public Execute(ExecuteStreamHandler streamHandler, ExecuteWatchdog watchdog) {
        this.cmdl = null;
        this.env = null;
        this.exitValue = Integer.MAX_VALUE;
        this.workingDirectory = null;
        this.project = null;
        this.newEnvironment = false;
        this.useVMLauncher = true;
        setStreamHandler(streamHandler);
        this.watchdog = watchdog;
        if (Os.isFamily(Os.FAMILY_VMS)) {
            this.useVMLauncher = false;
        }
    }

    public void setStreamHandler(ExecuteStreamHandler streamHandler) {
        this.streamHandler = streamHandler;
    }

    public String[] getCommandline() {
        return this.cmdl;
    }

    public void setCommandline(String[] commandline) {
        this.cmdl = commandline;
    }

    public void setNewenvironment(boolean newenv) {
        this.newEnvironment = newenv;
    }

    public String[] getEnvironment() {
        return (this.env == null || this.newEnvironment) ? this.env : patchEnvironment();
    }

    public void setEnvironment(String[] env) {
        this.env = env;
    }

    public void setWorkingDirectory(File wd) {
        this.workingDirectory = wd;
    }

    public File getWorkingDirectory() {
        return this.workingDirectory == null ? new File(antWorkingDirectory) : this.workingDirectory;
    }

    public void setAntRun(Project project) throws BuildException {
        this.project = project;
    }

    public void setVMLauncher(boolean useVMLauncher) {
        this.useVMLauncher = useVMLauncher;
    }

    public static Process launch(Project project, String[] command, String[] env, File dir, boolean useVM) throws IOException {
        if (dir != null && !dir.exists()) {
            throw new BuildException("%s doesn't exist.", dir);
        }
        CommandLauncher vmLauncher = CommandLauncher.getVMLauncher(project);
        CommandLauncher launcher = (!useVM || vmLauncher == null) ? CommandLauncher.getShellLauncher(project) : vmLauncher;
        return launcher.exec(project, command, env, dir);
    }

    public int execute() throws IOException {
        if (this.workingDirectory != null && !this.workingDirectory.exists()) {
            throw new BuildException("%s doesn't exist.", this.workingDirectory);
        }
        Process process = launch(this.project, getCommandline(), getEnvironment(), this.workingDirectory, this.useVMLauncher);
        try {
            this.streamHandler.setProcessInputStream(process.getOutputStream());
            this.streamHandler.setProcessOutputStream(process.getInputStream());
            this.streamHandler.setProcessErrorStream(process.getErrorStream());
            this.streamHandler.start();
            try {
                try {
                    processDestroyer.add(process);
                    if (this.watchdog != null) {
                        this.watchdog.start(process);
                    }
                    waitFor(process);
                    if (this.watchdog != null) {
                        this.watchdog.stop();
                    }
                    this.streamHandler.stop();
                    closeStreams(process);
                    if (this.watchdog != null) {
                        this.watchdog.checkException();
                    }
                    int exitValue = getExitValue();
                    processDestroyer.remove(process);
                    return exitValue;
                } catch (Throwable th) {
                    processDestroyer.remove(process);
                    throw th;
                }
            } catch (ThreadDeath t) {
                process.destroy();
                throw t;
            }
        } catch (IOException e) {
            process.destroy();
            throw e;
        }
    }

    public void spawn() throws IOException {
        if (this.workingDirectory != null && !this.workingDirectory.exists()) {
            throw new BuildException("%s doesn't exist.", this.workingDirectory);
        }
        Process process = launch(this.project, getCommandline(), getEnvironment(), this.workingDirectory, this.useVMLauncher);
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                this.project.log("interruption in the sleep after having spawned a process", 3);
            }
        }
        OutputStream dummyOut = new OutputStream() { // from class: org.apache.tools.ant.taskdefs.Execute.1
            @Override // java.io.OutputStream
            public void write(int b) throws IOException {
            }
        };
        ExecuteStreamHandler handler = new PumpStreamHandler(dummyOut);
        handler.setProcessErrorStream(process.getErrorStream());
        handler.setProcessOutputStream(process.getInputStream());
        handler.start();
        process.getOutputStream().close();
        this.project.log("spawned process " + process.toString(), 3);
    }

    protected void waitFor(Process process) {
        try {
            process.waitFor();
            setExitValue(process.exitValue());
        } catch (InterruptedException e) {
            process.destroy();
        }
    }

    protected void setExitValue(int value) {
        this.exitValue = value;
    }

    public int getExitValue() {
        return this.exitValue;
    }

    public static boolean isFailure(int exitValue) {
        return Os.isFamily(Os.FAMILY_VMS) ? exitValue % 2 == 0 : exitValue != 0;
    }

    public boolean isFailure() {
        return isFailure(getExitValue());
    }

    public boolean killedProcess() {
        return this.watchdog != null && this.watchdog.killedProcess();
    }

    private String[] patchEnvironment() {
        String[] strArr;
        if (Os.isFamily(Os.FAMILY_VMS)) {
            return this.env;
        }
        Map<String, String> osEnv = new LinkedHashMap<>(getEnvironmentVariables());
        for (String keyValue : this.env) {
            String key = keyValue.substring(0, keyValue.indexOf(61));
            if (osEnv.remove(key) == null && environmentCaseInSensitive) {
                Iterator<String> it = osEnv.keySet().iterator();
                while (true) {
                    if (it.hasNext()) {
                        String osEnvItem = it.next();
                        if (osEnvItem.equalsIgnoreCase(key)) {
                            key = osEnvItem;
                            break;
                        }
                    }
                }
            }
            osEnv.put(key, keyValue.substring(key.length() + 1));
        }
        return (String[]) osEnv.entrySet().stream().map(e -> {
            return ((String) e.getKey()) + "=" + ((String) e.getValue());
        }).toArray(x$0 -> {
            return new String[x$0];
        });
    }

    public static void runCommand(Task task, String... cmdline) throws BuildException {
        try {
            task.log(Commandline.describeCommand(cmdline), 3);
            Execute exe = new Execute(new LogStreamHandler(task, 2, 0));
            exe.setAntRun(task.getProject());
            exe.setCommandline(cmdline);
            int retval = exe.execute();
            if (isFailure(retval)) {
                throw new BuildException(cmdline[0] + " failed with return code " + retval, task.getLocation());
            }
        } catch (IOException exc) {
            throw new BuildException("Could not launch " + cmdline[0] + ": " + exc, task.getLocation());
        }
    }

    public static void closeStreams(Process process) {
        FileUtils.close(process.getInputStream());
        FileUtils.close(process.getOutputStream());
        FileUtils.close(process.getErrorStream());
    }

    private static Map<String, String> getVMSLogicals(BufferedReader in) throws IOException {
        Map<String, String> logicals = new HashMap<>();
        String logName = null;
        String logValue = null;
        while (true) {
            String line = in.readLine();
            if (line == null) {
                break;
            } else if (line.startsWith("\t=")) {
                if (logName != null) {
                    logValue = logValue + "," + line.substring(4, line.length() - 1);
                }
            } else if (line.startsWith("  \"")) {
                if (logName != null) {
                    logicals.put(logName, logValue);
                }
                int eqIndex = line.indexOf(61);
                String newLogName = line.substring(3, eqIndex - 2);
                if (logicals.containsKey(newLogName)) {
                    logName = null;
                } else {
                    logName = newLogName;
                    logValue = line.substring(eqIndex + 3, line.length() - 1);
                }
            }
        }
        if (logName != null) {
            logicals.put(logName, logValue);
        }
        return logicals;
    }
}
