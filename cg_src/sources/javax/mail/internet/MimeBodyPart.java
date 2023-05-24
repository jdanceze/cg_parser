package javax.mail.internet;

import com.google.common.net.HttpHeaders;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.LineOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.HeaderTokenizer;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/MimeBodyPart.class */
public class MimeBodyPart extends BodyPart implements MimePart {
    private static boolean setDefaultTextCharset;
    protected DataHandler dh;
    protected byte[] content;
    protected InputStream contentStream;
    protected InternetHeaders headers;

    static {
        setDefaultTextCharset = true;
        try {
            String s = System.getProperty("mail.mime.setdefaulttextcharset");
            setDefaultTextCharset = s == null || !s.equalsIgnoreCase("false");
        } catch (SecurityException e) {
        }
    }

    public MimeBodyPart() {
        this.headers = new InternetHeaders();
    }

    public MimeBodyPart(InputStream is) throws MessagingException {
        if (!(is instanceof ByteArrayInputStream) && !(is instanceof BufferedInputStream) && !(is instanceof SharedInputStream)) {
            is = new BufferedInputStream(is);
        }
        this.headers = new InternetHeaders(is);
        if (is instanceof SharedInputStream) {
            SharedInputStream sis = (SharedInputStream) is;
            this.contentStream = sis.newStream(sis.getPosition(), -1L);
            return;
        }
        try {
            this.content = ASCIIUtility.getBytes(is);
        } catch (IOException ioex) {
            throw new MessagingException("Error reading input stream", ioex);
        }
    }

    public MimeBodyPart(InternetHeaders headers, byte[] content) throws MessagingException {
        this.headers = headers;
        this.content = content;
    }

    @Override // javax.mail.Part
    public int getSize() throws MessagingException {
        if (this.content != null) {
            return this.content.length;
        }
        if (this.contentStream != null) {
            try {
                int size = this.contentStream.available();
                if (size > 0) {
                    return size;
                }
                return -1;
            } catch (IOException e) {
                return -1;
            }
        }
        return -1;
    }

    @Override // javax.mail.Part
    public int getLineCount() throws MessagingException {
        return -1;
    }

    @Override // javax.mail.Part
    public String getContentType() throws MessagingException {
        String s = getHeader("Content-Type", null);
        if (s == null) {
            s = "text/plain";
        }
        return s;
    }

    @Override // javax.mail.Part
    public boolean isMimeType(String mimeType) throws MessagingException {
        return isMimeType(this, mimeType);
    }

    @Override // javax.mail.Part
    public String getDisposition() throws MessagingException {
        return getDisposition(this);
    }

    @Override // javax.mail.Part
    public void setDisposition(String disposition) throws MessagingException {
        setDisposition(this, disposition);
    }

    @Override // javax.mail.internet.MimePart
    public String getEncoding() throws MessagingException {
        return getEncoding(this);
    }

    @Override // javax.mail.internet.MimePart
    public String getContentID() throws MessagingException {
        return getHeader("Content-Id", null);
    }

    public void setContentID(String cid) throws MessagingException {
        if (cid == null) {
            removeHeader("Content-ID");
        } else {
            setHeader("Content-ID", cid);
        }
    }

    @Override // javax.mail.internet.MimePart
    public String getContentMD5() throws MessagingException {
        return getHeader(HttpHeaders.CONTENT_MD5, null);
    }

    @Override // javax.mail.internet.MimePart
    public void setContentMD5(String md5) throws MessagingException {
        setHeader(HttpHeaders.CONTENT_MD5, md5);
    }

    @Override // javax.mail.internet.MimePart
    public String[] getContentLanguage() throws MessagingException {
        return getContentLanguage(this);
    }

    @Override // javax.mail.internet.MimePart
    public void setContentLanguage(String[] languages) throws MessagingException {
        setContentLanguage(this, languages);
    }

    @Override // javax.mail.Part
    public String getDescription() throws MessagingException {
        return getDescription(this);
    }

    @Override // javax.mail.Part
    public void setDescription(String description) throws MessagingException {
        setDescription(description, null);
    }

    public void setDescription(String description, String charset) throws MessagingException {
        setDescription(this, description, charset);
    }

    @Override // javax.mail.Part
    public String getFileName() throws MessagingException {
        return getFileName(this);
    }

    @Override // javax.mail.Part
    public void setFileName(String filename) throws MessagingException {
        setFileName(this, filename);
    }

    @Override // javax.mail.Part
    public InputStream getInputStream() throws IOException, MessagingException {
        return getDataHandler().getInputStream();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public InputStream getContentStream() throws MessagingException {
        if (this.contentStream != null) {
            return ((SharedInputStream) this.contentStream).newStream(0L, -1L);
        }
        if (this.content != null) {
            return new ByteArrayInputStream(this.content);
        }
        throw new MessagingException("No content");
    }

    public InputStream getRawInputStream() throws MessagingException {
        return getContentStream();
    }

    @Override // javax.mail.Part
    public DataHandler getDataHandler() throws MessagingException {
        if (this.dh == null) {
            this.dh = new DataHandler(new MimePartDataSource(this));
        }
        return this.dh;
    }

    @Override // javax.mail.Part
    public Object getContent() throws IOException, MessagingException {
        return getDataHandler().getContent();
    }

    @Override // javax.mail.Part
    public void setDataHandler(DataHandler dh) throws MessagingException {
        this.dh = dh;
        invalidateContentHeaders(this);
    }

    @Override // javax.mail.Part
    public void setContent(Object o, String type) throws MessagingException {
        if (o instanceof Multipart) {
            setContent((Multipart) o);
        } else {
            setDataHandler(new DataHandler(o, type));
        }
    }

    @Override // javax.mail.Part, javax.mail.internet.MimePart
    public void setText(String text) throws MessagingException {
        setText(text, null);
    }

    @Override // javax.mail.internet.MimePart
    public void setText(String text, String charset) throws MessagingException {
        setText(this, text, charset);
    }

    @Override // javax.mail.Part
    public void setContent(Multipart mp) throws MessagingException {
        setDataHandler(new DataHandler(mp, mp.getContentType()));
        mp.setParent(this);
    }

    @Override // javax.mail.Part
    public void writeTo(OutputStream os) throws IOException, MessagingException {
        writeTo(this, os, null);
    }

    @Override // javax.mail.Part
    public String[] getHeader(String name) throws MessagingException {
        return this.headers.getHeader(name);
    }

    @Override // javax.mail.internet.MimePart
    public String getHeader(String name, String delimiter) throws MessagingException {
        return this.headers.getHeader(name, delimiter);
    }

    @Override // javax.mail.Part
    public void setHeader(String name, String value) throws MessagingException {
        this.headers.setHeader(name, value);
    }

    @Override // javax.mail.Part
    public void addHeader(String name, String value) throws MessagingException {
        this.headers.addHeader(name, value);
    }

    @Override // javax.mail.Part
    public void removeHeader(String name) throws MessagingException {
        this.headers.removeHeader(name);
    }

    @Override // javax.mail.Part
    public Enumeration getAllHeaders() throws MessagingException {
        return this.headers.getAllHeaders();
    }

    @Override // javax.mail.Part
    public Enumeration getMatchingHeaders(String[] names) throws MessagingException {
        return this.headers.getMatchingHeaders(names);
    }

    @Override // javax.mail.Part
    public Enumeration getNonMatchingHeaders(String[] names) throws MessagingException {
        return this.headers.getNonMatchingHeaders(names);
    }

    @Override // javax.mail.internet.MimePart
    public void addHeaderLine(String line) throws MessagingException {
        this.headers.addHeaderLine(line);
    }

    @Override // javax.mail.internet.MimePart
    public Enumeration getAllHeaderLines() throws MessagingException {
        return this.headers.getAllHeaderLines();
    }

    @Override // javax.mail.internet.MimePart
    public Enumeration getMatchingHeaderLines(String[] names) throws MessagingException {
        return this.headers.getMatchingHeaderLines(names);
    }

    @Override // javax.mail.internet.MimePart
    public Enumeration getNonMatchingHeaderLines(String[] names) throws MessagingException {
        return this.headers.getNonMatchingHeaderLines(names);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateHeaders() throws MessagingException {
        updateHeaders(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isMimeType(MimePart part, String mimeType) throws MessagingException {
        try {
            ContentType ct = new ContentType(part.getContentType());
            return ct.match(mimeType);
        } catch (ParseException e) {
            return part.getContentType().equalsIgnoreCase(mimeType);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setText(MimePart part, String text, String charset) throws MessagingException {
        if (charset == null) {
            if (MimeUtility.checkAscii(text) != 1) {
                charset = MimeUtility.getDefaultMIMECharset();
            } else {
                charset = "us-ascii";
            }
        }
        part.setContent(text, new StringBuffer().append("text/plain; charset=").append(MimeUtility.quote(charset, HeaderTokenizer.MIME)).toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getDisposition(MimePart part) throws MessagingException {
        String s = part.getHeader(HttpHeaders.CONTENT_DISPOSITION, null);
        if (s == null) {
            return null;
        }
        ContentDisposition cd = new ContentDisposition(s);
        return cd.getDisposition();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setDisposition(MimePart part, String disposition) throws MessagingException {
        if (disposition == null) {
            part.removeHeader(HttpHeaders.CONTENT_DISPOSITION);
            return;
        }
        String s = part.getHeader(HttpHeaders.CONTENT_DISPOSITION, null);
        if (s != null) {
            ContentDisposition cd = new ContentDisposition(s);
            cd.setDisposition(disposition);
            disposition = cd.toString();
        }
        part.setHeader(HttpHeaders.CONTENT_DISPOSITION, disposition);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getDescription(MimePart part) throws MessagingException {
        String rawvalue = part.getHeader("Content-Description", null);
        if (rawvalue == null) {
            return null;
        }
        try {
            return MimeUtility.decodeText(MimeUtility.unfold(rawvalue));
        } catch (UnsupportedEncodingException e) {
            return rawvalue;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setDescription(MimePart part, String description, String charset) throws MessagingException {
        if (description == null) {
            part.removeHeader("Content-Description");
            return;
        }
        try {
            part.setHeader("Content-Description", MimeUtility.fold(21, MimeUtility.encodeText(description, charset, null)));
        } catch (UnsupportedEncodingException uex) {
            throw new MessagingException("Encoding error", uex);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getFileName(MimePart part) throws MessagingException {
        String s;
        String filename = null;
        String s2 = part.getHeader(HttpHeaders.CONTENT_DISPOSITION, null);
        if (s2 != null) {
            ContentDisposition cd = new ContentDisposition(s2);
            filename = cd.getParameter("filename");
        }
        if (filename == null && (s = part.getHeader("Content-Type", null)) != null) {
            try {
                ContentType ct = new ContentType(s);
                filename = ct.getParameter("name");
            } catch (ParseException e) {
            }
        }
        return filename;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setFileName(MimePart part, String name) throws MessagingException {
        String s = part.getHeader(HttpHeaders.CONTENT_DISPOSITION, null);
        ContentDisposition cd = new ContentDisposition(s == null ? Part.ATTACHMENT : s);
        cd.setParameter("filename", name);
        part.setHeader(HttpHeaders.CONTENT_DISPOSITION, cd.toString());
        String s2 = part.getHeader("Content-Type", null);
        if (s2 != null) {
            try {
                ContentType cType = new ContentType(s2);
                cType.setParameter("name", name);
                part.setHeader("Content-Type", cType.toString());
            } catch (ParseException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] getContentLanguage(MimePart part) throws MessagingException {
        String s = part.getHeader(HttpHeaders.CONTENT_LANGUAGE, null);
        if (s == null) {
            return null;
        }
        HeaderTokenizer h = new HeaderTokenizer(s, HeaderTokenizer.MIME);
        Vector v = new Vector();
        while (true) {
            HeaderTokenizer.Token tk = h.next();
            int tkType = tk.getType();
            if (tkType == -4) {
                break;
            } else if (tkType == -1) {
                v.addElement(tk.getValue());
            }
        }
        if (v.size() == 0) {
            return null;
        }
        String[] language = new String[v.size()];
        v.copyInto(language);
        return language;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setContentLanguage(MimePart part, String[] languages) throws MessagingException {
        StringBuffer sb = new StringBuffer(languages[0]);
        for (int i = 1; i < languages.length; i++) {
            sb.append(',').append(languages[i]);
        }
        part.setHeader(HttpHeaders.CONTENT_LANGUAGE, sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getEncoding(MimePart part) throws MessagingException {
        HeaderTokenizer.Token tk;
        int tkType;
        String s = part.getHeader("Content-Transfer-Encoding", null);
        if (s == null) {
            return null;
        }
        String s2 = s.trim();
        if (s2.equalsIgnoreCase("7bit") || s2.equalsIgnoreCase("8bit") || s2.equalsIgnoreCase("quoted-printable") || s2.equalsIgnoreCase("base64")) {
            return s2;
        }
        HeaderTokenizer h = new HeaderTokenizer(s2, HeaderTokenizer.MIME);
        do {
            tk = h.next();
            tkType = tk.getType();
            if (tkType == -4) {
                return s2;
            }
        } while (tkType != -1);
        return tk.getValue();
    }

    static void setEncoding(MimePart part, String encoding) throws MessagingException {
        part.setHeader("Content-Transfer-Encoding", encoding);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void updateHeaders(MimePart part) throws MessagingException {
        String charset;
        DataHandler dh = part.getDataHandler();
        if (dh == null) {
            return;
        }
        try {
            String type = dh.getContentType();
            boolean composite = false;
            boolean needCTHeader = part.getHeader("Content-Type") == null;
            ContentType cType = new ContentType(type);
            if (cType.match("multipart/*")) {
                composite = true;
                Object o = dh.getContent();
                ((MimeMultipart) o).updateHeaders();
            } else if (cType.match("message/rfc822")) {
                composite = true;
            }
            if (!composite) {
                if (part.getHeader("Content-Transfer-Encoding") == null) {
                    setEncoding(part, MimeUtility.getEncoding(dh));
                }
                if (needCTHeader && setDefaultTextCharset && cType.match("text/*") && cType.getParameter("charset") == null) {
                    String enc = part.getEncoding();
                    if (enc != null && enc.equalsIgnoreCase("7bit")) {
                        charset = "us-ascii";
                    } else {
                        charset = MimeUtility.getDefaultMIMECharset();
                    }
                    cType.setParameter("charset", charset);
                    type = cType.toString();
                }
            }
            if (needCTHeader) {
                String s = part.getHeader(HttpHeaders.CONTENT_DISPOSITION, null);
                if (s != null) {
                    ContentDisposition cd = new ContentDisposition(s);
                    String filename = cd.getParameter("filename");
                    if (filename != null) {
                        cType.setParameter("name", filename);
                        type = cType.toString();
                    }
                }
                part.setHeader("Content-Type", type);
            }
        } catch (IOException ex) {
            throw new MessagingException("IOException updating headers", ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void invalidateContentHeaders(MimePart part) throws MessagingException {
        part.removeHeader("Content-Type");
        part.removeHeader("Content-Transfer-Encoding");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void writeTo(MimePart part, OutputStream os, String[] ignoreList) throws IOException, MessagingException {
        LineOutputStream los;
        if (os instanceof LineOutputStream) {
            los = (LineOutputStream) os;
        } else {
            los = new LineOutputStream(os);
        }
        Enumeration hdrLines = part.getNonMatchingHeaderLines(ignoreList);
        while (hdrLines.hasMoreElements()) {
            los.writeln((String) hdrLines.nextElement());
        }
        los.writeln();
        OutputStream os2 = MimeUtility.encode(os, part.getEncoding());
        part.getDataHandler().writeTo(os2);
        os2.flush();
    }
}
