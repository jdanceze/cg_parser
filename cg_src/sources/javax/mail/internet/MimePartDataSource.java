package javax.mail.internet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownServiceException;
import javax.activation.DataSource;
import javax.mail.MessageAware;
import javax.mail.MessageContext;
import javax.mail.MessagingException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/MimePartDataSource.class */
public class MimePartDataSource implements DataSource, MessageAware {
    private MimePart part;
    private MessageContext context;

    public MimePartDataSource(MimePart part) {
        this.part = part;
    }

    @Override // javax.activation.DataSource
    public InputStream getInputStream() throws IOException {
        InputStream is;
        try {
            if (this.part instanceof MimeBodyPart) {
                is = ((MimeBodyPart) this.part).getContentStream();
            } else if (this.part instanceof MimeMessage) {
                is = ((MimeMessage) this.part).getContentStream();
            } else {
                throw new MessagingException("Unknown part");
            }
            String encoding = this.part.getEncoding();
            if (encoding != null) {
                return MimeUtility.decode(is, encoding);
            }
            return is;
        } catch (MessagingException mex) {
            throw new IOException(mex.getMessage());
        }
    }

    @Override // javax.activation.DataSource
    public OutputStream getOutputStream() throws IOException {
        throw new UnknownServiceException();
    }

    @Override // javax.activation.DataSource
    public String getContentType() {
        try {
            return this.part.getContentType();
        } catch (MessagingException e) {
            return null;
        }
    }

    @Override // javax.activation.DataSource
    public String getName() {
        try {
            if (this.part instanceof MimeBodyPart) {
                return ((MimeBodyPart) this.part).getFileName();
            }
            return "";
        } catch (MessagingException e) {
            return "";
        }
    }

    @Override // javax.mail.MessageAware
    public synchronized MessageContext getMessageContext() {
        if (this.context == null) {
            this.context = new MessageContext(this.part);
        }
        return this.context;
    }
}
