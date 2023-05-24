package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Locale;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import soot.coffi.Instruction;
@Deprecated
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Exec.class */
public class Exec extends Task {
    private String os;
    private String out;
    private File dir;
    private String command;
    protected PrintWriter fos = null;
    private boolean failOnError = false;

    public Exec() {
        System.err.println("As of Ant 1.2 released in October 2000, the Exec class");
        System.err.println("is considered to be dead code by the Ant developers and is unmaintained.");
        System.err.println("Don't use it!");
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        run(this.command);
    }

    protected int run(String command) throws BuildException {
        int err = -1;
        String myos = System.getProperty("os.name");
        log("Myos = " + myos, 3);
        if (this.os != null && !this.os.contains(myos)) {
            log("Not found in " + this.os, 3);
            return 0;
        }
        if (this.dir == null) {
            this.dir = getProject().getBaseDir();
        }
        if (myos.toLowerCase(Locale.ENGLISH).contains(Os.FAMILY_WINDOWS)) {
            if (!this.dir.equals(getProject().resolveFile("."))) {
                if (myos.toLowerCase(Locale.ENGLISH).contains("nt")) {
                    command = "cmd /c cd " + this.dir + " && " + command;
                } else {
                    String ant = getProject().getProperty("ant.home");
                    if (ant == null) {
                        throw new BuildException("Property 'ant.home' not found", getLocation());
                    }
                    String antRun = getProject().resolveFile(ant + "/bin/antRun.bat").toString();
                    command = antRun + Instruction.argsep + this.dir + Instruction.argsep + command;
                }
            }
        } else {
            String ant2 = getProject().getProperty("ant.home");
            if (ant2 == null) {
                throw new BuildException("Property 'ant.home' not found", getLocation());
            }
            String antRun2 = getProject().resolveFile(ant2 + "/bin/antRun").toString();
            command = antRun2 + Instruction.argsep + this.dir + Instruction.argsep + command;
        }
        try {
            log(command, 3);
            Process proc = Runtime.getRuntime().exec(command);
            if (this.out != null) {
                this.fos = new PrintWriter(new FileWriter(this.out));
                log("Output redirected to " + this.out, 3);
            }
            StreamPumper inputPumper = new StreamPumper(proc.getInputStream(), 2);
            StreamPumper errorPumper = new StreamPumper(proc.getErrorStream(), 1);
            inputPumper.start();
            errorPumper.start();
            proc.waitFor();
            inputPumper.join();
            errorPumper.join();
            proc.destroy();
            logFlush();
            err = proc.exitValue();
            if (err != 0) {
                if (this.failOnError) {
                    throw new BuildException("Exec returned: " + err, getLocation());
                }
                log("Result: " + err, 0);
            }
        } catch (IOException ioe) {
            throw new BuildException("Error exec: " + command, ioe, getLocation());
        } catch (InterruptedException e) {
        }
        return err;
    }

    public void setDir(String d) {
        this.dir = getProject().resolveFile(d);
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setOutput(String out) {
        this.out = out;
    }

    public void setFailonerror(boolean fail) {
        this.failOnError = fail;
    }

    protected void outputLog(String line, int messageLevel) {
        if (this.fos == null) {
            log(line, messageLevel);
        } else {
            this.fos.println(line);
        }
    }

    protected void logFlush() {
        if (this.fos != null) {
            this.fos.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Exec$StreamPumper.class */
    public class StreamPumper extends Thread {
        private BufferedReader din;
        private int messageLevel;
        private boolean endOfStream = false;
        private static final int SLEEP_TIME = 5;

        public StreamPumper(InputStream is, int messageLevel) {
            this.din = new BufferedReader(new InputStreamReader(is));
            this.messageLevel = messageLevel;
        }

        public void pumpStream() throws IOException {
            if (!this.endOfStream) {
                String line = this.din.readLine();
                if (line != null) {
                    Exec.this.outputLog(line, this.messageLevel);
                } else {
                    this.endOfStream = true;
                }
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!this.endOfStream) {
                try {
                    try {
                        pumpStream();
                        sleep(5L);
                    } catch (IOException e) {
                        return;
                    }
                } catch (InterruptedException e2) {
                }
            }
            this.din.close();
        }
    }
}
