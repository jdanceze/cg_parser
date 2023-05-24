package org.apache.tools.ant;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.tools.ant.util.DOMElementWriter;
import org.apache.tools.ant.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import polyglot.main.Report;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/XmlLogger.class */
public class XmlLogger implements BuildLogger {
    private PrintStream outStream;
    private static DocumentBuilder builder = getDocumentBuilder();
    private static final String BUILD_TAG = "build";
    private static final String TARGET_TAG = "target";
    private static final String TASK_TAG = "task";
    private static final String MESSAGE_TAG = "message";
    private static final String NAME_ATTR = "name";
    private static final String TIME_ATTR = "time";
    private static final String PRIORITY_ATTR = "priority";
    private static final String LOCATION_ATTR = "location";
    private static final String ERROR_ATTR = "error";
    private static final String STACKTRACE_TAG = "stacktrace";
    private int msgOutputLevel = 4;
    private Document doc = builder.newDocument();
    private Map<Task, TimedElement> tasks = new Hashtable();
    private Map<Target, TimedElement> targets = new Hashtable();
    private Map<Thread, Stack<TimedElement>> threadStacks = new Hashtable();
    private TimedElement buildElement = null;

    private static DocumentBuilder getDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Exception exc) {
            throw new ExceptionInInitializerError(exc);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/XmlLogger$TimedElement.class */
    public static class TimedElement {
        private long startTime;
        private Element element;

        private TimedElement() {
        }

        public String toString() {
            return this.element.getTagName() + ":" + this.element.getAttribute("name");
        }
    }

    @Override // org.apache.tools.ant.BuildListener
    public void buildStarted(BuildEvent event) {
        this.buildElement = new TimedElement();
        this.buildElement.startTime = System.currentTimeMillis();
        this.buildElement.element = this.doc.createElement(BUILD_TAG);
    }

    @Override // org.apache.tools.ant.BuildListener
    public void buildFinished(BuildEvent event) {
        long totalTime = System.currentTimeMillis() - this.buildElement.startTime;
        this.buildElement.element.setAttribute("time", DefaultLogger.formatTime(totalTime));
        if (event.getException() != null) {
            this.buildElement.element.setAttribute(ERROR_ATTR, event.getException().toString());
            Throwable t = event.getException();
            Text errText = this.doc.createCDATASection(StringUtils.getStackTrace(t));
            Element stacktrace = this.doc.createElement(STACKTRACE_TAG);
            stacktrace.appendChild(errText);
            synchronizedAppend(this.buildElement.element, stacktrace);
        }
        String outFilename = getProperty(event, "XmlLogger.file", "log.xml");
        String xslUri = getProperty(event, "ant.XmlLogger.stylesheet.uri", "log.xsl");
        try {
            OutputStream stream = this.outStream == null ? Files.newOutputStream(Paths.get(outFilename, new String[0]), new OpenOption[0]) : this.outStream;
            Writer out = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
            try {
                out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                if (!xslUri.isEmpty()) {
                    out.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + xslUri + "\"?>\n\n");
                }
                new DOMElementWriter().write(this.buildElement.element, out, 0, "\t");
                out.flush();
                out.close();
                if (stream != null) {
                    stream.close();
                }
                this.buildElement = null;
            } catch (Throwable th) {
                try {
                    out.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (IOException exc) {
            throw new BuildException("Unable to write log file", exc);
        }
    }

    private String getProperty(BuildEvent event, String propertyName, String defaultValue) {
        String rv = defaultValue;
        if (event != null && event.getProject() != null && event.getProject().getProperty(propertyName) != null) {
            rv = event.getProject().getProperty(propertyName);
        }
        return rv;
    }

    private Stack<TimedElement> getStack() {
        return this.threadStacks.computeIfAbsent(Thread.currentThread(), k -> {
            return new Stack();
        });
    }

    @Override // org.apache.tools.ant.BuildListener
    public void targetStarted(BuildEvent event) {
        Target target = event.getTarget();
        TimedElement targetElement = new TimedElement();
        targetElement.startTime = System.currentTimeMillis();
        targetElement.element = this.doc.createElement("target");
        targetElement.element.setAttribute("name", target.getName());
        this.targets.put(target, targetElement);
        getStack().push(targetElement);
    }

    @Override // org.apache.tools.ant.BuildListener
    public void targetFinished(BuildEvent event) {
        Target target = event.getTarget();
        TimedElement targetElement = this.targets.get(target);
        if (targetElement != null) {
            long totalTime = System.currentTimeMillis() - targetElement.startTime;
            targetElement.element.setAttribute("time", DefaultLogger.formatTime(totalTime));
            TimedElement parentElement = null;
            Stack<TimedElement> threadStack = getStack();
            if (!threadStack.empty()) {
                TimedElement poppedStack = threadStack.pop();
                if (poppedStack != targetElement) {
                    throw new RuntimeException("Mismatch - popped element = " + poppedStack + " finished target element = " + targetElement);
                }
                if (!threadStack.empty()) {
                    parentElement = threadStack.peek();
                }
            }
            if (parentElement == null) {
                synchronizedAppend(this.buildElement.element, targetElement.element);
            } else {
                synchronizedAppend(parentElement.element, targetElement.element);
            }
        }
        this.targets.remove(target);
    }

    @Override // org.apache.tools.ant.BuildListener
    public void taskStarted(BuildEvent event) {
        TimedElement taskElement = new TimedElement();
        taskElement.startTime = System.currentTimeMillis();
        taskElement.element = this.doc.createElement(TASK_TAG);
        Task task = event.getTask();
        String name = event.getTask().getTaskName();
        if (name == null) {
            name = "";
        }
        taskElement.element.setAttribute("name", name);
        taskElement.element.setAttribute("location", event.getTask().getLocation().toString());
        this.tasks.put(task, taskElement);
        getStack().push(taskElement);
    }

    @Override // org.apache.tools.ant.BuildListener
    public void taskFinished(BuildEvent event) {
        TimedElement poppedStack;
        Task task = event.getTask();
        TimedElement taskElement = this.tasks.get(task);
        if (taskElement != null) {
            long totalTime = System.currentTimeMillis() - taskElement.startTime;
            taskElement.element.setAttribute("time", DefaultLogger.formatTime(totalTime));
            Target target = task.getOwningTarget();
            TimedElement targetElement = null;
            if (target != null) {
                targetElement = this.targets.get(target);
            }
            if (targetElement == null) {
                synchronizedAppend(this.buildElement.element, taskElement.element);
            } else {
                synchronizedAppend(targetElement.element, taskElement.element);
            }
            Stack<TimedElement> threadStack = getStack();
            if (!threadStack.empty() && (poppedStack = threadStack.pop()) != taskElement) {
                throw new RuntimeException("Mismatch - popped element = " + poppedStack + " finished task element = " + taskElement);
            }
            this.tasks.remove(task);
            return;
        }
        throw new RuntimeException("Unknown task " + task + " not in " + this.tasks);
    }

    private TimedElement getTaskElement(Task task) {
        TimedElement element = this.tasks.get(task);
        if (element != null) {
            return element;
        }
        Set<Task> knownTasks = new HashSet<>(this.tasks.keySet());
        for (Task t : knownTasks) {
            if ((t instanceof UnknownElement) && ((UnknownElement) t).getTask() == task) {
                return this.tasks.get(t);
            }
        }
        return null;
    }

    @Override // org.apache.tools.ant.BuildListener
    public void messageLogged(BuildEvent event) {
        String name;
        int priority = event.getPriority();
        if (priority > this.msgOutputLevel) {
            return;
        }
        Element messageElement = this.doc.createElement(MESSAGE_TAG);
        switch (priority) {
            case 0:
                name = ERROR_ATTR;
                break;
            case 1:
                name = "warn";
                break;
            case 2:
                name = "info";
                break;
            default:
                name = Report.debug;
                break;
        }
        messageElement.setAttribute("priority", name);
        Throwable ex = event.getException();
        if (4 <= this.msgOutputLevel && ex != null) {
            Node errText = this.doc.createCDATASection(StringUtils.getStackTrace(ex));
            Element stacktrace = this.doc.createElement(STACKTRACE_TAG);
            stacktrace.appendChild(errText);
            synchronizedAppend(this.buildElement.element, stacktrace);
        }
        Text messageText = this.doc.createCDATASection(event.getMessage());
        messageElement.appendChild(messageText);
        TimedElement parentElement = null;
        Task task = event.getTask();
        Target target = event.getTarget();
        if (task != null) {
            parentElement = getTaskElement(task);
        }
        if (parentElement == null && target != null) {
            parentElement = this.targets.get(target);
        }
        if (parentElement != null) {
            synchronizedAppend(parentElement.element, messageElement);
        } else {
            synchronizedAppend(this.buildElement.element, messageElement);
        }
    }

    @Override // org.apache.tools.ant.BuildLogger
    public void setMessageOutputLevel(int level) {
        this.msgOutputLevel = level;
    }

    @Override // org.apache.tools.ant.BuildLogger
    public void setOutputPrintStream(PrintStream output) {
        this.outStream = new PrintStream((OutputStream) output, true);
    }

    @Override // org.apache.tools.ant.BuildLogger
    public void setEmacsMode(boolean emacsMode) {
    }

    @Override // org.apache.tools.ant.BuildLogger
    public void setErrorPrintStream(PrintStream err) {
    }

    private void synchronizedAppend(Node parent, Node child) {
        synchronized (parent) {
            parent.appendChild(child);
        }
    }
}
