package org.apache.tools.ant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.tools.ant.util.DateUtils;
import org.apache.tools.ant.util.StringUtils;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/DefaultLogger.class */
public class DefaultLogger implements BuildLogger {
    public static final int LEFT_COLUMN_SIZE = 12;
    protected PrintStream out;
    protected PrintStream err;
    @Deprecated
    protected static final String lSep = StringUtils.LINE_SEP;
    protected int msgOutputLevel = 0;
    private long startTime = System.currentTimeMillis();
    protected boolean emacsMode = false;

    @Override // org.apache.tools.ant.BuildLogger
    public void setMessageOutputLevel(int level) {
        this.msgOutputLevel = level;
    }

    @Override // org.apache.tools.ant.BuildLogger
    public void setOutputPrintStream(PrintStream output) {
        this.out = new PrintStream((OutputStream) output, true);
    }

    @Override // org.apache.tools.ant.BuildLogger
    public void setErrorPrintStream(PrintStream err) {
        this.err = new PrintStream((OutputStream) err, true);
    }

    @Override // org.apache.tools.ant.BuildLogger
    public void setEmacsMode(boolean emacsMode) {
        this.emacsMode = emacsMode;
    }

    @Override // org.apache.tools.ant.BuildListener
    public void buildStarted(BuildEvent event) {
        this.startTime = System.currentTimeMillis();
    }

    static void throwableMessage(StringBuffer m, Throwable error, boolean verbose) {
        Throwable cause;
        while ((error instanceof BuildException) && (cause = error.getCause()) != null) {
            String msg1 = error.toString();
            String msg2 = cause.toString();
            if (!msg1.endsWith(msg2)) {
                break;
            }
            m.append((CharSequence) msg1, 0, msg1.length() - msg2.length());
            error = cause;
        }
        if (verbose || !(error instanceof BuildException)) {
            m.append(StringUtils.getStackTrace(error));
        } else {
            m.append(String.format("%s%n", error));
        }
    }

    @Override // org.apache.tools.ant.BuildListener
    public void buildFinished(BuildEvent event) {
        Throwable error = event.getException();
        StringBuffer message = new StringBuffer();
        if (error == null) {
            message.append(String.format("%n%s", getBuildSuccessfulMessage()));
        } else {
            message.append(String.format("%n%s%n", getBuildFailedMessage()));
            throwableMessage(message, error, 3 <= this.msgOutputLevel);
        }
        message.append(String.format("%nTotal time: %s", formatTime(System.currentTimeMillis() - this.startTime)));
        String msg = message.toString();
        if (error == null) {
            printMessage(msg, this.out, 3);
        } else {
            printMessage(msg, this.err, 0);
        }
        log(msg);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getBuildFailedMessage() {
        return "BUILD FAILED";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getBuildSuccessfulMessage() {
        return "BUILD SUCCESSFUL";
    }

    @Override // org.apache.tools.ant.BuildListener
    public void targetStarted(BuildEvent event) {
        if (2 <= this.msgOutputLevel && !event.getTarget().getName().isEmpty()) {
            String msg = String.format("%n%s:", event.getTarget().getName());
            printMessage(msg, this.out, event.getPriority());
            log(msg);
        }
    }

    @Override // org.apache.tools.ant.BuildListener
    public void targetFinished(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void taskStarted(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void taskFinished(BuildEvent event) {
    }

    @Override // org.apache.tools.ant.BuildListener
    public void messageLogged(BuildEvent event) {
        int priority = event.getPriority();
        if (priority <= this.msgOutputLevel) {
            StringBuilder message = new StringBuilder();
            if (event.getTask() == null || this.emacsMode) {
                message.append(event.getMessage());
            } else {
                String name = event.getTask().getTaskName();
                String label = "[" + name + "] ";
                int size = 12 - label.length();
                String prefix = size > 0 ? ((String) Stream.generate(() -> {
                    return Instruction.argsep;
                }).limit(size).collect(Collectors.joining())) + label : label;
                try {
                    BufferedReader r = new BufferedReader(new StringReader(event.getMessage()));
                    message.append((String) r.lines().collect(Collectors.joining(System.lineSeparator() + prefix, prefix, "")));
                    r.close();
                } catch (IOException e) {
                    message.append(label).append(event.getMessage());
                }
            }
            Throwable ex = event.getException();
            if (4 <= this.msgOutputLevel && ex != null) {
                message.append(String.format("%n%s: ", ex.getClass().getSimpleName())).append(StringUtils.getStackTrace(ex));
            }
            String msg = message.toString();
            if (priority != 0) {
                printMessage(msg, this.out, priority);
            } else {
                printMessage(msg, this.err, priority);
            }
            log(msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String formatTime(long millis) {
        return DateUtils.formatElapsedTime(millis);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void printMessage(String message, PrintStream stream, int priority) {
        stream.println(message);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void log(String message) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getTimestamp() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat formatter = DateFormat.getDateTimeInstance(3, 3);
        return formatter.format(date);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String extractProjectName(BuildEvent event) {
        Project project = event.getProject();
        if (project != null) {
            return project.getName();
        }
        return null;
    }
}
