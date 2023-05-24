package org.apache.tools.ant.taskdefs.email;

import java.io.File;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.DateUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/email/Mailer.class */
public abstract class Mailer {
    protected Message message;
    protected EmailAddress from;
    protected Task task;
    protected String host = null;
    protected int port = -1;
    protected String user = null;
    protected String password = null;
    protected boolean SSL = false;
    protected Vector<EmailAddress> replyToList = null;
    protected Vector<EmailAddress> toList = null;
    protected Vector<EmailAddress> ccList = null;
    protected Vector<EmailAddress> bccList = null;
    protected Vector<File> files = null;
    protected String subject = null;
    protected boolean includeFileNames = false;
    protected Vector<Header> headers = null;
    private boolean ignoreInvalidRecipients = false;
    private boolean starttls = false;
    private boolean portExplicitlySpecified = false;

    public abstract void send() throws BuildException;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPortExplicitlySpecified(boolean explicit) {
        this.portExplicitlySpecified = explicit;
    }

    protected boolean isPortExplicitlySpecified() {
        return this.portExplicitlySpecified;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSSL(boolean ssl) {
        this.SSL = ssl;
    }

    public void setEnableStartTLS(boolean b) {
        this.starttls = b;
    }

    protected boolean isStartTLSEnabled() {
        return this.starttls;
    }

    public void setMessage(Message m) {
        this.message = m;
    }

    public void setFrom(EmailAddress from) {
        this.from = from;
    }

    public void setReplyToList(Vector<EmailAddress> list) {
        this.replyToList = list;
    }

    public void setToList(Vector<EmailAddress> list) {
        this.toList = list;
    }

    public void setCcList(Vector<EmailAddress> list) {
        this.ccList = list;
    }

    public void setBccList(Vector<EmailAddress> list) {
        this.bccList = list;
    }

    public void setFiles(Vector<File> files) {
        this.files = files;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setIncludeFileNames(boolean b) {
        this.includeFileNames = b;
    }

    public void setHeaders(Vector<Header> v) {
        this.headers = v;
    }

    public void setIgnoreInvalidRecipients(boolean b) {
        this.ignoreInvalidRecipients = b;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean shouldIgnoreInvalidRecipients() {
        return this.ignoreInvalidRecipients;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String getDate() {
        return DateUtils.getDateForHeader();
    }
}
