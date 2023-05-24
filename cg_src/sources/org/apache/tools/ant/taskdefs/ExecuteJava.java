package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Permissions;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.TimeoutObserver;
import org.apache.tools.ant.util.Watchdog;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/ExecuteJava.class */
public class ExecuteJava implements Runnable, TimeoutObserver {
    private Commandline javaCommand = null;
    private Path classpath = null;
    private CommandlineJava.SysProperties sysProperties = null;
    private Permissions perm = null;
    private Method main = null;
    private Long timeout = null;
    private volatile Throwable caught = null;
    private volatile boolean timedOut = false;
    private boolean done = false;
    private Thread thread = null;

    public void setJavaCommand(Commandline javaCommand) {
        this.javaCommand = javaCommand;
    }

    public void setClasspath(Path p) {
        this.classpath = p;
    }

    public void setSystemProperties(CommandlineJava.SysProperties s) {
        this.sysProperties = s;
    }

    public void setPermissions(Permissions permissions) {
        this.perm = permissions;
    }

    @Deprecated
    public void setOutput(PrintStream out) {
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public void execute(Project project) throws BuildException {
        Class<?> target;
        String classname = this.javaCommand.getExecutable();
        AntClassLoader loader = null;
        try {
            try {
                if (this.sysProperties != null) {
                    this.sysProperties.setSystem();
                }
                try {
                    if (this.classpath == null) {
                        target = Class.forName(classname);
                    } else {
                        loader = project.createClassLoader(this.classpath);
                        loader.setParent(project.getCoreLoader());
                        loader.setParentFirst(false);
                        loader.addJavaLibraries();
                        loader.setIsolated(true);
                        loader.setThreadContextLoader();
                        loader.forceLoadClass(classname);
                        target = Class.forName(classname, true, loader);
                    }
                    this.main = target.getMethod("main", String[].class);
                    if (this.main == null) {
                        throw new BuildException("Could not find main() method in %s", classname);
                    }
                    if ((this.main.getModifiers() & 8) == 0) {
                        throw new BuildException("main() method in %s is not declared static", classname);
                    }
                    if (this.timeout == null) {
                        run();
                    } else {
                        this.thread = new Thread(this, "ExecuteJava");
                        Task currentThreadTask = project.getThreadTask(Thread.currentThread());
                        project.registerThreadTask(this.thread, currentThreadTask);
                        this.thread.setDaemon(true);
                        Watchdog w = new Watchdog(this.timeout.longValue());
                        w.addTimeoutObserver(this);
                        synchronized (this) {
                            this.thread.start();
                            w.start();
                            while (!this.done) {
                                try {
                                    wait();
                                } catch (InterruptedException e) {
                                }
                            }
                            if (this.timedOut) {
                                project.log("Timeout: sub-process interrupted", 1);
                            } else {
                                this.thread = null;
                                w.stop();
                            }
                        }
                    }
                    if (this.caught != null) {
                        throw this.caught;
                    }
                    if (loader != null) {
                        loader.resetThreadContextLoader();
                        loader.cleanup();
                    }
                    if (this.sysProperties != null) {
                        this.sysProperties.restoreSystem();
                    }
                } catch (ClassNotFoundException e2) {
                    throw new BuildException("Could not find %s. Make sure you have it in your classpath", classname);
                }
            } catch (SecurityException | ThreadDeath | BuildException e3) {
                throw e3;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                loader.resetThreadContextLoader();
                loader.cleanup();
            }
            if (this.sysProperties != null) {
                this.sysProperties.restoreSystem();
            }
            throw th;
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        Object[] argument = {this.javaCommand.getArguments()};
        try {
            try {
                if (this.perm != null) {
                    this.perm.setSecurityManager();
                }
                this.main.invoke(null, argument);
                if (this.perm != null) {
                    this.perm.restoreSecurityManager();
                }
                synchronized (this) {
                    this.done = true;
                    notifyAll();
                }
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                if (!(t instanceof InterruptedException)) {
                    this.caught = t;
                }
                if (this.perm != null) {
                    this.perm.restoreSecurityManager();
                }
                synchronized (this) {
                    this.done = true;
                    notifyAll();
                }
            } catch (Throwable t2) {
                this.caught = t2;
                if (this.perm != null) {
                    this.perm.restoreSecurityManager();
                }
                synchronized (this) {
                    this.done = true;
                    notifyAll();
                }
            }
        } catch (Throwable th) {
            if (this.perm != null) {
                this.perm.restoreSecurityManager();
            }
            synchronized (this) {
                this.done = true;
                notifyAll();
                throw th;
            }
        }
    }

    @Override // org.apache.tools.ant.util.TimeoutObserver
    public synchronized void timeoutOccured(Watchdog w) {
        if (this.thread != null) {
            this.timedOut = true;
            this.thread.interrupt();
        }
        this.done = true;
        notifyAll();
    }

    public synchronized boolean killedProcess() {
        return this.timedOut;
    }

    public int fork(ProjectComponent pc) throws BuildException {
        String[] arguments;
        ExecuteWatchdog executeWatchdog;
        CommandlineJava cmdl = new CommandlineJava();
        cmdl.setClassname(this.javaCommand.getExecutable());
        for (String arg : this.javaCommand.getArguments()) {
            cmdl.createArgument().setValue(arg);
        }
        if (this.classpath != null) {
            cmdl.createClasspath(pc.getProject()).append(this.classpath);
        }
        if (this.sysProperties != null) {
            cmdl.addSysproperties(this.sysProperties);
        }
        Redirector redirector = new Redirector(pc);
        ExecuteStreamHandler createHandler = redirector.createHandler();
        if (this.timeout == null) {
            executeWatchdog = null;
        } else {
            executeWatchdog = new ExecuteWatchdog(this.timeout.longValue());
        }
        Execute exe = new Execute(createHandler, executeWatchdog);
        exe.setAntRun(pc.getProject());
        if (Os.isFamily(Os.FAMILY_VMS)) {
            setupCommandLineForVMS(exe, cmdl.getCommandline());
        } else {
            exe.setCommandline(cmdl.getCommandline());
        }
        try {
            try {
                int rc = exe.execute();
                redirector.complete();
                this.timedOut = exe.killedProcess();
                return rc;
            } catch (IOException e) {
                throw new BuildException(e);
            }
        } catch (Throwable th) {
            this.timedOut = exe.killedProcess();
            throw th;
        }
    }

    public static void setupCommandLineForVMS(Execute exe, String[] command) {
        exe.setVMLauncher(true);
        try {
            String[] args = new String[command.length - 1];
            System.arraycopy(command, 1, args, 0, command.length - 1);
            File vmsJavaOptionFile = JavaEnvUtils.createVmsJavaOptionFile(args);
            vmsJavaOptionFile.deleteOnExit();
            String[] vmsCmd = {command[0], MSVSSConstants.FLAG_VERSION, vmsJavaOptionFile.getPath()};
            exe.setCommandline(vmsCmd);
        } catch (IOException e) {
            throw new BuildException("Failed to create a temporary file for \"-V\" switch");
        }
    }
}
