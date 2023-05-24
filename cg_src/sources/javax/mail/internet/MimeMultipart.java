package javax.mail.internet;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.LineOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessageAware;
import javax.mail.MessageContext;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.MultipartDataSource;
import org.apache.commons.cli.HelpFormatter;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/MimeMultipart.class */
public class MimeMultipart extends Multipart {
    protected DataSource ds;
    protected boolean parsed;

    public MimeMultipart() {
        this("mixed");
    }

    public MimeMultipart(String subtype) {
        this.ds = null;
        this.parsed = true;
        String boundary = UniqueValue.getUniqueBoundaryValue();
        ContentType cType = new ContentType("multipart", subtype, null);
        cType.setParameter("boundary", boundary);
        this.contentType = cType.toString();
    }

    public MimeMultipart(DataSource ds) throws MessagingException {
        this.ds = null;
        this.parsed = true;
        if (ds instanceof MessageAware) {
            MessageContext mc = ((MessageAware) ds).getMessageContext();
            setParent(mc.getPart());
        }
        if (ds instanceof MultipartDataSource) {
            setMultipartDataSource((MultipartDataSource) ds);
            return;
        }
        this.parsed = false;
        this.ds = ds;
        this.contentType = ds.getContentType();
    }

    public synchronized void setSubType(String subtype) throws MessagingException {
        ContentType cType = new ContentType(this.contentType);
        cType.setSubType(subtype);
        this.contentType = cType.toString();
    }

    @Override // javax.mail.Multipart
    public synchronized int getCount() throws MessagingException {
        parse();
        return super.getCount();
    }

    @Override // javax.mail.Multipart
    public synchronized BodyPart getBodyPart(int index) throws MessagingException {
        parse();
        return super.getBodyPart(index);
    }

    public synchronized BodyPart getBodyPart(String CID) throws MessagingException {
        parse();
        int count = getCount();
        for (int i = 0; i < count; i++) {
            MimeBodyPart part = (MimeBodyPart) getBodyPart(i);
            String s = part.getContentID();
            if (s != null && s.equals(CID)) {
                return part;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateHeaders() throws MessagingException {
        for (int i = 0; i < this.parts.size(); i++) {
            ((MimeBodyPart) this.parts.elementAt(i)).updateHeaders();
        }
    }

    @Override // javax.mail.Multipart
    public void writeTo(OutputStream os) throws IOException, MessagingException {
        parse();
        String boundary = new StringBuffer().append(HelpFormatter.DEFAULT_LONG_OPT_PREFIX).append(new ContentType(this.contentType).getParameter("boundary")).toString();
        LineOutputStream los = new LineOutputStream(os);
        for (int i = 0; i < this.parts.size(); i++) {
            los.writeln(boundary);
            ((MimeBodyPart) this.parts.elementAt(i)).writeTo(os);
            los.writeln();
        }
        los.writeln(new StringBuffer().append(boundary).append(HelpFormatter.DEFAULT_LONG_OPT_PREFIX).toString());
    }

    protected synchronized void parse() throws MessagingException {
        String line;
        MimeBodyPart part;
        String line2;
        char c;
        if (this.parsed) {
            return;
        }
        SharedInputStream sin = null;
        long start = 0;
        long end = 0;
        try {
            InputStream in = this.ds.getInputStream();
            if (!(in instanceof ByteArrayInputStream) && !(in instanceof BufferedInputStream) && !(in instanceof SharedInputStream)) {
                in = new BufferedInputStream(in);
            }
            if (in instanceof SharedInputStream) {
                sin = (SharedInputStream) in;
            }
            ContentType cType = new ContentType(this.contentType);
            String boundary = new StringBuffer().append(HelpFormatter.DEFAULT_LONG_OPT_PREFIX).append(cType.getParameter("boundary")).toString();
            byte[] bndbytes = ASCIIUtility.getBytes(boundary);
            int bl = bndbytes.length;
            try {
                LineInputStream lin = new LineInputStream(in);
                do {
                    String readLine = lin.readLine();
                    line = readLine;
                    if (readLine == null) {
                        break;
                    }
                    int i = line.length() - 1;
                    while (i >= 0 && ((c = line.charAt(i)) == ' ' || c == '\t')) {
                        i--;
                    }
                    line = line.substring(0, i + 1);
                } while (!line.equals(boundary));
                if (line == null) {
                    throw new MessagingException("Missing start boundary");
                }
                boolean done = false;
                while (!done) {
                    InternetHeaders headers = null;
                    if (sin != null) {
                        start = sin.getPosition();
                        do {
                            line2 = lin.readLine();
                            if (line2 == null) {
                                break;
                            }
                        } while (line2.length() > 0);
                        if (line2 == null) {
                            break;
                        }
                    } else {
                        headers = createInternetHeaders(in);
                    }
                    if (!in.markSupported()) {
                        throw new MessagingException("Stream doesn't support mark");
                    }
                    ByteArrayOutputStream buf = null;
                    if (sin == null) {
                        buf = new ByteArrayOutputStream();
                    }
                    boolean bol = true;
                    int eol1 = -1;
                    int eol2 = -1;
                    while (true) {
                        if (bol) {
                            in.mark(bl + 4 + 1000);
                            int i2 = 0;
                            while (i2 < bl && in.read() == bndbytes[i2]) {
                                i2++;
                            }
                            if (i2 == bl) {
                                int b2 = in.read();
                                if (b2 == 45 && in.read() == 45) {
                                    done = true;
                                    break;
                                }
                                while (true) {
                                    if (b2 != 32 && b2 != 9) {
                                        break;
                                    }
                                    b2 = in.read();
                                }
                                if (b2 == 10) {
                                    break;
                                } else if (b2 == 13) {
                                    in.mark(1);
                                    if (in.read() != 10) {
                                        in.reset();
                                    }
                                }
                            }
                            in.reset();
                            if (buf != null && eol1 != -1) {
                                buf.write(eol1);
                                if (eol2 != -1) {
                                    buf.write(eol2);
                                }
                                eol2 = -1;
                                eol1 = -1;
                            }
                        }
                        int b = in.read();
                        if (b < 0) {
                            done = true;
                            break;
                        } else if (b == 13 || b == 10) {
                            bol = true;
                            if (sin != null) {
                                end = sin.getPosition() - 1;
                            }
                            eol1 = b;
                            if (b == 13) {
                                in.mark(1);
                                int b3 = in.read();
                                if (b3 == 10) {
                                    eol2 = b3;
                                } else {
                                    in.reset();
                                }
                            }
                        } else {
                            bol = false;
                            if (buf != null) {
                                buf.write(b);
                            }
                        }
                    }
                    if (sin != null) {
                        part = createMimeBodyPart(sin.newStream(start, end));
                    } else {
                        part = createMimeBodyPart(headers, buf.toByteArray());
                    }
                    addBodyPart(part);
                }
                this.parsed = true;
            } catch (IOException ioex) {
                throw new MessagingException("IO Error", ioex);
            }
        } catch (Exception e) {
            throw new MessagingException("No inputstream from datasource");
        }
    }

    protected InternetHeaders createInternetHeaders(InputStream is) throws MessagingException {
        return new InternetHeaders(is);
    }

    protected MimeBodyPart createMimeBodyPart(InternetHeaders headers, byte[] content) throws MessagingException {
        return new MimeBodyPart(headers, content);
    }

    protected MimeBodyPart createMimeBodyPart(InputStream is) throws MessagingException {
        return new MimeBodyPart(is);
    }
}
