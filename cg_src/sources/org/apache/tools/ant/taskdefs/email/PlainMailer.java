package org.apache.tools.ant.taskdefs.email;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.mail.MailMessage;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/email/PlainMailer.class */
class PlainMailer extends Mailer {
    @Override // org.apache.tools.ant.taskdefs.email.Mailer
    public void send() {
        try {
            MailMessage mailMessage = new MailMessage(this.host, this.port);
            mailMessage.from(this.from.toString());
            boolean atLeastOneRcptReached = false;
            Stream map = this.replyToList.stream().map((v0) -> {
                return v0.toString();
            });
            Objects.requireNonNull(mailMessage);
            map.forEach(this::replyto);
            Iterator<EmailAddress> it = this.toList.iterator();
            while (it.hasNext()) {
                EmailAddress to = it.next();
                try {
                    mailMessage.to(to.toString());
                    atLeastOneRcptReached = true;
                } catch (IOException ex) {
                    badRecipient(to, ex);
                }
            }
            Iterator<EmailAddress> it2 = this.ccList.iterator();
            while (it2.hasNext()) {
                EmailAddress cc = it2.next();
                try {
                    mailMessage.cc(cc.toString());
                    atLeastOneRcptReached = true;
                } catch (IOException ex2) {
                    badRecipient(cc, ex2);
                }
            }
            Iterator<EmailAddress> it3 = this.bccList.iterator();
            while (it3.hasNext()) {
                EmailAddress bcc = it3.next();
                try {
                    mailMessage.bcc(bcc.toString());
                    atLeastOneRcptReached = true;
                } catch (IOException ex3) {
                    badRecipient(bcc, ex3);
                }
            }
            if (!atLeastOneRcptReached) {
                throw new BuildException("Couldn't reach any recipient");
            }
            if (this.subject != null) {
                mailMessage.setSubject(this.subject);
            }
            mailMessage.setHeader("Date", getDate());
            if (this.message.getCharset() != null) {
                mailMessage.setHeader("Content-Type", this.message.getMimeType() + "; charset=\"" + this.message.getCharset() + "\"");
            } else {
                mailMessage.setHeader("Content-Type", this.message.getMimeType());
            }
            if (this.headers != null) {
                Iterator<Header> it4 = this.headers.iterator();
                while (it4.hasNext()) {
                    Header h = it4.next();
                    mailMessage.setHeader(h.getName(), h.getValue());
                }
            }
            PrintStream out = mailMessage.getPrintStream();
            this.message.print(out);
            if (this.files != null) {
                Iterator<File> it5 = this.files.iterator();
                while (it5.hasNext()) {
                    File f = it5.next();
                    attach(f, out);
                }
            }
            mailMessage.sendAndClose();
        } catch (IOException ioe) {
            throw new BuildException("IO error sending mail", ioe);
        }
    }

    protected void attach(File file, PrintStream out) throws IOException {
        if (!file.exists() || !file.canRead()) {
            throw new BuildException("File \"%s\" does not exist or is not readable.", file.getAbsolutePath());
        }
        if (this.includeFileNames) {
            out.println();
            String filename = file.getName();
            int filenamelength = filename.length();
            out.println(filename);
            for (int star = 0; star < filenamelength; star++) {
                out.print('=');
            }
            out.println();
        }
        byte[] buf = new byte[1024];
        InputStream finstr = Files.newInputStream(file.toPath(), new OpenOption[0]);
        try {
            BufferedInputStream in = new BufferedInputStream(finstr, buf.length);
            while (true) {
                int length = in.read(buf);
                if (length == -1) {
                    break;
                }
                out.write(buf, 0, length);
            }
            in.close();
            if (finstr != null) {
                finstr.close();
            }
        } catch (Throwable th) {
            if (finstr != null) {
                try {
                    finstr.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private void badRecipient(EmailAddress rcpt, IOException reason) {
        String msg = "Failed to send mail to " + rcpt;
        if (shouldIgnoreInvalidRecipients()) {
            String msg2 = msg + " because of :" + reason.getMessage();
            if (this.task != null) {
                this.task.log(msg2, 1);
                return;
            } else {
                System.err.println(msg2);
                return;
            }
        }
        throw new BuildException(msg, reason);
    }
}
