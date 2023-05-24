package org.apache.tools.ant.listener;

import android.accounts.AccountManager;
import android.hardware.Camera;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.http.cookie.ClientCookie;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.email.EmailAddress;
import org.apache.tools.ant.taskdefs.email.Mailer;
import org.apache.tools.ant.taskdefs.email.Message;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.DateUtils;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.mail.MailMessage;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/listener/MailLogger.class */
public class MailLogger extends DefaultLogger {
    private static final String DEFAULT_MIME_TYPE = "text/plain";
    private StringBuffer buffer = new StringBuffer();

    @Override // org.apache.tools.ant.DefaultLogger, org.apache.tools.ant.BuildListener
    public void buildFinished(BuildEvent event) {
        super.buildFinished(event);
        Project project = event.getProject();
        Map<String, Object> properties = project.getProperties();
        Properties fileProperties = new Properties();
        String filename = (String) properties.get("MailLogger.properties.file");
        if (filename != null) {
            InputStream is = null;
            try {
                is = Files.newInputStream(Paths.get(filename, new String[0]), new OpenOption[0]);
                fileProperties.load(is);
                FileUtils.close(is);
            } catch (IOException e) {
                FileUtils.close(is);
            } catch (Throwable th) {
                FileUtils.close(is);
                throw th;
            }
        }
        fileProperties.stringPropertyNames().forEach(key -> {
            properties.put(key, project.replaceProperties(fileProperties.getProperty(key)));
        });
        boolean success = event.getException() == null;
        String prefix = success ? "success" : "failure";
        try {
            boolean notify = Project.toBoolean(getValue(properties, prefix + ".notify", Camera.Parameters.FLASH_MODE_ON));
            if (!notify) {
                return;
            }
            Values values = new Values().mailhost(getValue(properties, "mailhost", MailMessage.DEFAULT_HOST)).port(Integer.parseInt(getValue(properties, ClientCookie.PORT_ATTR, String.valueOf(25)))).user(getValue(properties, "user", "")).password(getValue(properties, AccountManager.KEY_PASSWORD, "")).ssl(Project.toBoolean(getValue(properties, "ssl", "off"))).starttls(Project.toBoolean(getValue(properties, "starttls.enable", "off"))).from(getValue(properties, "from", null)).replytoList(getValue(properties, "replyto", "")).toList(getValue(properties, prefix + ".to", null)).toCcList(getValue(properties, prefix + ".cc", "")).toBccList(getValue(properties, prefix + ".bcc", "")).mimeType(getValue(properties, "mimeType", "text/plain")).charset(getValue(properties, "charset", "")).body(getValue(properties, prefix + ".body", "")).subject(getValue(properties, prefix + ".subject", success ? "Build Success" : "Build Failure"));
            if (values.user().isEmpty() && values.password().isEmpty() && !values.ssl() && !values.starttls()) {
                sendMail(values, this.buffer.substring(0));
            } else {
                sendMimeMail(event.getProject(), values, this.buffer.substring(0));
            }
        } catch (Exception e2) {
            System.out.println("MailLogger failed to send e-mail!");
            e2.printStackTrace(System.err);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/listener/MailLogger$Values.class */
    public static class Values {
        private String mailhost;
        private int port;
        private String user;
        private String password;
        private boolean ssl;
        private String from;
        private String replytoList;
        private String toList;
        private String toCcList;
        private String toBccList;
        private String subject;
        private String charset;
        private String mimeType;
        private String body;
        private boolean starttls;

        private Values() {
        }

        public String mailhost() {
            return this.mailhost;
        }

        public Values mailhost(String mailhost) {
            this.mailhost = mailhost;
            return this;
        }

        public int port() {
            return this.port;
        }

        public Values port(int port) {
            this.port = port;
            return this;
        }

        public String user() {
            return this.user;
        }

        public Values user(String user) {
            this.user = user;
            return this;
        }

        public String password() {
            return this.password;
        }

        public Values password(String password) {
            this.password = password;
            return this;
        }

        public boolean ssl() {
            return this.ssl;
        }

        public Values ssl(boolean ssl) {
            this.ssl = ssl;
            return this;
        }

        public String from() {
            return this.from;
        }

        public Values from(String from) {
            this.from = from;
            return this;
        }

        public String replytoList() {
            return this.replytoList;
        }

        public Values replytoList(String replytoList) {
            this.replytoList = replytoList;
            return this;
        }

        public String toList() {
            return this.toList;
        }

        public Values toList(String toList) {
            this.toList = toList;
            return this;
        }

        public String toCcList() {
            return this.toCcList;
        }

        public Values toCcList(String toCcList) {
            this.toCcList = toCcList;
            return this;
        }

        public String toBccList() {
            return this.toBccList;
        }

        public Values toBccList(String toBccList) {
            this.toBccList = toBccList;
            return this;
        }

        public String subject() {
            return this.subject;
        }

        public Values subject(String subject) {
            this.subject = subject;
            return this;
        }

        public String charset() {
            return this.charset;
        }

        public Values charset(String charset) {
            this.charset = charset;
            return this;
        }

        public String mimeType() {
            return this.mimeType;
        }

        public Values mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public String body() {
            return this.body;
        }

        public Values body(String body) {
            this.body = body;
            return this;
        }

        public boolean starttls() {
            return this.starttls;
        }

        public Values starttls(boolean starttls) {
            this.starttls = starttls;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.DefaultLogger
    public void log(String message) {
        this.buffer.append(message).append(System.lineSeparator());
    }

    private String getValue(Map<String, Object> properties, String name, String defaultValue) {
        String propertyName = "MailLogger." + name;
        String value = (String) properties.get(propertyName);
        if (value == null) {
            value = defaultValue;
        }
        if (value == null) {
            throw new RuntimeException("Missing required parameter: " + propertyName);
        }
        return value;
    }

    private void sendMail(Values values, String message) throws IOException {
        MailMessage mailMessage = new MailMessage(values.mailhost(), values.port());
        mailMessage.setHeader("Date", DateUtils.getDateForHeader());
        mailMessage.from(values.from());
        if (!values.replytoList().isEmpty()) {
            StringTokenizer t = new StringTokenizer(values.replytoList(), ", ", false);
            while (t.hasMoreTokens()) {
                mailMessage.replyto(t.nextToken());
            }
        }
        StringTokenizer t2 = new StringTokenizer(values.toList(), ", ", false);
        while (t2.hasMoreTokens()) {
            mailMessage.to(t2.nextToken());
        }
        mailMessage.setSubject(values.subject());
        if (values.charset().isEmpty()) {
            mailMessage.setHeader("Content-Type", values.mimeType());
        } else {
            mailMessage.setHeader("Content-Type", values.mimeType() + "; charset=\"" + values.charset() + "\"");
        }
        PrintStream ps = mailMessage.getPrintStream();
        ps.println(values.body().isEmpty() ? message : values.body());
        mailMessage.sendAndClose();
    }

    private void sendMimeMail(Project project, Values values, String message) {
        try {
            Mailer mailer = (Mailer) ClasspathUtils.newInstance("org.apache.tools.ant.taskdefs.email.MimeMailer", MailLogger.class.getClassLoader(), Mailer.class);
            Vector<EmailAddress> replyToList = splitEmailAddresses(values.replytoList());
            mailer.setHost(values.mailhost());
            mailer.setPort(values.port());
            mailer.setUser(values.user());
            mailer.setPassword(values.password());
            mailer.setSSL(values.ssl());
            mailer.setEnableStartTLS(values.starttls());
            Message mymessage = new Message(!values.body().isEmpty() ? values.body() : message);
            mymessage.setProject(project);
            mymessage.setMimeType(values.mimeType());
            if (!values.charset().isEmpty()) {
                mymessage.setCharset(values.charset());
            }
            mailer.setMessage(mymessage);
            mailer.setFrom(new EmailAddress(values.from()));
            mailer.setReplyToList(replyToList);
            Vector<EmailAddress> toList = splitEmailAddresses(values.toList());
            mailer.setToList(toList);
            Vector<EmailAddress> toCcList = splitEmailAddresses(values.toCcList());
            mailer.setCcList(toCcList);
            Vector<EmailAddress> toBccList = splitEmailAddresses(values.toBccList());
            mailer.setBccList(toBccList);
            mailer.setFiles(new Vector<>());
            mailer.setSubject(values.subject());
            mailer.setHeaders(new Vector<>());
            mailer.send();
        } catch (BuildException e) {
            Throwable t = e.getCause() == null ? e : e.getCause();
            log("Failed to initialise MIME mail: " + t.getMessage());
        }
    }

    private Vector<EmailAddress> splitEmailAddresses(String listString) {
        return (Vector) Stream.of((Object[]) listString.split(",")).map(EmailAddress::new).collect(Collectors.toCollection(Vector::new));
    }
}
