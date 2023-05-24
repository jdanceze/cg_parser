package org.apache.tools.ant.taskdefs.email;

import java.io.File;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.mail.MailMessage;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/email/EmailTask.class */
public class EmailTask extends Task {
    private static final int SMTP_PORT = 25;
    public static final String AUTO = "auto";
    public static final String MIME = "mime";
    public static final String UU = "uu";
    public static final String PLAIN = "plain";
    private String messageFileInputEncoding;
    private String encoding = "auto";
    private String host = MailMessage.DEFAULT_HOST;
    private Integer port = null;
    private String subject = null;
    private Message message = null;
    private boolean failOnError = true;
    private boolean includeFileNames = false;
    private String messageMimeType = null;
    private EmailAddress from = null;
    private Vector<EmailAddress> replyToList = new Vector<>();
    private Vector<EmailAddress> toList = new Vector<>();
    private Vector<EmailAddress> ccList = new Vector<>();
    private Vector<EmailAddress> bccList = new Vector<>();
    private Vector<Header> headers = new Vector<>();
    private Path attachments = null;
    private String charset = null;
    private String user = null;
    private String password = null;
    private boolean ssl = false;
    private boolean starttls = false;
    private boolean ignoreInvalidRecipients = false;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/email/EmailTask$Encoding.class */
    public static class Encoding extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"auto", "mime", EmailTask.UU, EmailTask.PLAIN};
        }
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSSL(boolean ssl) {
        this.ssl = ssl;
    }

    public void setEnableStartTLS(boolean b) {
        this.starttls = b;
    }

    public void setEncoding(Encoding encoding) {
        this.encoding = encoding.getValue();
    }

    public void setMailport(int port) {
        this.port = Integer.valueOf(port);
    }

    public void setMailhost(String host) {
        this.host = host;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        if (this.message != null) {
            throw new BuildException("Only one message can be sent in an email");
        }
        this.message = new Message(message);
        this.message.setProject(getProject());
    }

    public void setMessageFile(File file) {
        if (this.message != null) {
            throw new BuildException("Only one message can be sent in an email");
        }
        this.message = new Message(file);
        this.message.setProject(getProject());
    }

    public void setMessageMimeType(String type) {
        this.messageMimeType = type;
    }

    public void addMessage(Message message) throws BuildException {
        if (this.message != null) {
            throw new BuildException("Only one message can be sent in an email");
        }
        this.message = message;
    }

    public void addFrom(EmailAddress address) {
        if (this.from != null) {
            throw new BuildException("Emails can only be from one address");
        }
        this.from = address;
    }

    public void setFrom(String address) {
        if (this.from != null) {
            throw new BuildException("Emails can only be from one address");
        }
        this.from = new EmailAddress(address);
    }

    public void addReplyTo(EmailAddress address) {
        this.replyToList.add(address);
    }

    public void setReplyTo(String address) {
        this.replyToList.add(new EmailAddress(address));
    }

    public void addTo(EmailAddress address) {
        this.toList.add(address);
    }

    public void setToList(String list) {
        StringTokenizer tokens = new StringTokenizer(list, ",");
        while (tokens.hasMoreTokens()) {
            this.toList.add(new EmailAddress(tokens.nextToken()));
        }
    }

    public void addCc(EmailAddress address) {
        this.ccList.add(address);
    }

    public void setCcList(String list) {
        StringTokenizer tokens = new StringTokenizer(list, ",");
        while (tokens.hasMoreTokens()) {
            this.ccList.add(new EmailAddress(tokens.nextToken()));
        }
    }

    public void addBcc(EmailAddress address) {
        this.bccList.add(address);
    }

    public void setBccList(String list) {
        StringTokenizer tokens = new StringTokenizer(list, ",");
        while (tokens.hasMoreTokens()) {
            this.bccList.add(new EmailAddress(tokens.nextToken()));
        }
    }

    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }

    public void setFiles(String filenames) {
        StringTokenizer t = new StringTokenizer(filenames, ", ");
        while (t.hasMoreTokens()) {
            createAttachments().add(new FileResource(getProject().resolveFile(t.nextToken())));
        }
    }

    public void addFileset(FileSet fs) {
        createAttachments().add(fs);
    }

    public Path createAttachments() {
        if (this.attachments == null) {
            this.attachments = new Path(getProject());
        }
        return this.attachments.createPath();
    }

    public Header createHeader() {
        Header h = new Header();
        this.headers.add(h);
        return h;
    }

    public void setIncludefilenames(boolean includeFileNames) {
        this.includeFileNames = includeFileNames;
    }

    public boolean getIncludeFileNames() {
        return this.includeFileNames;
    }

    public void setIgnoreInvalidRecipients(boolean b) {
        this.ignoreInvalidRecipients = b;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        Message savedMessage = this.message;
        try {
            try {
                Mailer mailer = null;
                boolean autoFound = false;
                if ("mime".equals(this.encoding) || ("auto".equals(this.encoding) && 0 == 0)) {
                    try {
                        Class.forName("javax.activation.DataHandler");
                        Class.forName("javax.mail.internet.MimeMessage");
                        mailer = (Mailer) ClasspathUtils.newInstance("org.apache.tools.ant.taskdefs.email.MimeMailer", EmailTask.class.getClassLoader(), Mailer.class);
                        autoFound = true;
                        log("Using MIME mail", 3);
                    } catch (BuildException e) {
                        logBuildException("Failed to initialise MIME mail: ", e);
                    }
                }
                if (!autoFound && ((this.user != null || this.password != null) && (UU.equals(this.encoding) || PLAIN.equals(this.encoding)))) {
                    throw new BuildException("SMTP auth only possible with MIME mail");
                }
                if (!autoFound && ((this.ssl || this.starttls) && (UU.equals(this.encoding) || PLAIN.equals(this.encoding)))) {
                    throw new BuildException("SSL and STARTTLS only possible with MIME mail");
                }
                if (UU.equals(this.encoding) || ("auto".equals(this.encoding) && !autoFound)) {
                    try {
                        mailer = (Mailer) ClasspathUtils.newInstance("org.apache.tools.ant.taskdefs.email.UUMailer", EmailTask.class.getClassLoader(), Mailer.class);
                        autoFound = true;
                        log("Using UU mail", 3);
                    } catch (BuildException e2) {
                        logBuildException("Failed to initialise UU mail: ", e2);
                    }
                }
                if (PLAIN.equals(this.encoding) || ("auto".equals(this.encoding) && !autoFound)) {
                    mailer = new PlainMailer();
                    log("Using plain mail", 3);
                }
                if (mailer == null) {
                    throw new BuildException("Failed to initialise encoding: %s", this.encoding);
                }
                if (this.message == null) {
                    this.message = new Message();
                    this.message.setProject(getProject());
                }
                if (this.from == null || this.from.getAddress() == null) {
                    throw new BuildException("A from element is required");
                }
                if (this.toList.isEmpty() && this.ccList.isEmpty() && this.bccList.isEmpty()) {
                    throw new BuildException("At least one of to, cc or bcc must be supplied");
                }
                if (this.messageMimeType != null) {
                    if (this.message.isMimeTypeSpecified()) {
                        throw new BuildException("The mime type can only be specified in one location");
                    }
                    this.message.setMimeType(this.messageMimeType);
                }
                if (this.charset != null) {
                    if (this.message.getCharset() != null) {
                        throw new BuildException("The charset can only be specified in one location");
                    }
                    this.message.setCharset(this.charset);
                }
                this.message.setInputEncoding(this.messageFileInputEncoding);
                Vector<File> files = new Vector<>();
                if (this.attachments != null) {
                    Iterator<Resource> it = this.attachments.iterator();
                    while (it.hasNext()) {
                        Resource r = it.next();
                        files.add(((FileProvider) r.as(FileProvider.class)).getFile());
                    }
                }
                log("Sending email: " + this.subject, 2);
                log("From " + this.from, 3);
                log("ReplyTo " + this.replyToList, 3);
                log("To " + this.toList, 3);
                log("Cc " + this.ccList, 3);
                log("Bcc " + this.bccList, 3);
                mailer.setHost(this.host);
                if (this.port != null) {
                    mailer.setPort(this.port.intValue());
                    mailer.setPortExplicitlySpecified(true);
                } else {
                    mailer.setPort(25);
                    mailer.setPortExplicitlySpecified(false);
                }
                mailer.setUser(this.user);
                mailer.setPassword(this.password);
                mailer.setSSL(this.ssl);
                mailer.setEnableStartTLS(this.starttls);
                mailer.setMessage(this.message);
                mailer.setFrom(this.from);
                mailer.setReplyToList(this.replyToList);
                mailer.setToList(this.toList);
                mailer.setCcList(this.ccList);
                mailer.setBccList(this.bccList);
                mailer.setFiles(files);
                mailer.setSubject(this.subject);
                mailer.setTask(this);
                mailer.setIncludeFileNames(this.includeFileNames);
                mailer.setHeaders(this.headers);
                mailer.setIgnoreInvalidRecipients(this.ignoreInvalidRecipients);
                mailer.send();
                int count = files.size();
                log("Sent email with " + count + " attachment" + (count == 1 ? "" : "s"), 2);
                this.message = savedMessage;
            } catch (Throwable th) {
                this.message = savedMessage;
                throw th;
            }
        } catch (BuildException e3) {
            logBuildException("Failed to send email: ", e3);
            if (this.failOnError) {
                throw e3;
            }
            this.message = savedMessage;
        } catch (Exception e4) {
            log("Failed to send email: " + e4.getMessage(), 1);
            if (this.failOnError) {
                throw new BuildException(e4);
            }
            this.message = savedMessage;
        }
    }

    private void logBuildException(String reason, BuildException e) {
        Throwable t = e.getCause() == null ? e : e.getCause();
        log(reason + t.getMessage(), 1);
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setMessageFileInputEncoding(String encoding) {
        this.messageFileInputEncoding = encoding;
    }
}
