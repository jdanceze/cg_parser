package javax.mail.internet;

import com.google.common.net.HttpHeaders;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.LineOutputStream;
import com.sun.mail.util.SharedByteArrayInputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/MimeMessage.class */
public class MimeMessage extends Message implements MimePart {
    protected DataHandler dh;
    protected byte[] content;
    protected InputStream contentStream;
    protected InternetHeaders headers;
    protected Flags flags;
    protected boolean modified;
    protected boolean saved;
    private boolean strict;
    private static MailDateFormat mailDateFormat = new MailDateFormat();
    private static final Flags answeredFlag = new Flags(Flags.Flag.ANSWERED);

    public MimeMessage(Session session) {
        super(session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.modified = true;
        this.headers = new InternetHeaders();
        this.flags = new Flags();
        initStrict();
    }

    public MimeMessage(Session session, InputStream is) throws MessagingException {
        super(session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.flags = new Flags();
        initStrict();
        parse(is);
        this.saved = true;
    }

    public MimeMessage(MimeMessage source) throws MessagingException {
        super(source.session);
        ByteArrayOutputStream bos;
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.flags = source.getFlags();
        int size = source.getSize();
        if (size > 0) {
            bos = new ByteArrayOutputStream(size);
        } else {
            bos = new ByteArrayOutputStream();
        }
        try {
            this.strict = source.strict;
            source.writeTo(bos);
            bos.close();
            SharedByteArrayInputStream bis = new SharedByteArrayInputStream(bos.toByteArray());
            parse(bis);
            bis.close();
            this.saved = true;
        } catch (IOException ex) {
            throw new MessagingException("IOException while copying message", ex);
        }
    }

    protected MimeMessage(Folder folder, int msgnum) {
        super(folder, msgnum);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.headers = new InternetHeaders();
        this.flags = new Flags();
        this.saved = true;
        initStrict();
    }

    protected MimeMessage(Folder folder, InputStream is, int msgnum) throws MessagingException {
        this(folder, msgnum);
        initStrict();
        parse(is);
    }

    protected MimeMessage(Folder folder, InternetHeaders headers, byte[] content, int msgnum) throws MessagingException {
        this(folder, msgnum);
        this.headers = headers;
        this.content = content;
        initStrict();
    }

    private void initStrict() {
        if (this.session != null) {
            String s = this.session.getProperty("mail.mime.address.strict");
            this.strict = s == null || !s.equalsIgnoreCase("false");
        }
    }

    protected void parse(InputStream is) throws MessagingException {
        if (!(is instanceof ByteArrayInputStream) && !(is instanceof BufferedInputStream) && !(is instanceof SharedInputStream)) {
            is = new BufferedInputStream(is);
        }
        this.headers = createInternetHeaders(is);
        if (is instanceof SharedInputStream) {
            SharedInputStream sis = (SharedInputStream) is;
            this.contentStream = sis.newStream(sis.getPosition(), -1L);
        } else {
            try {
                this.content = ASCIIUtility.getBytes(is);
            } catch (IOException ioex) {
                throw new MessagingException("IOException", ioex);
            }
        }
        this.modified = false;
    }

    @Override // javax.mail.Message
    public Address[] getFrom() throws MessagingException {
        Address[] a = getAddressHeader("From");
        if (a == null) {
            a = getAddressHeader("Sender");
        }
        return a;
    }

    @Override // javax.mail.Message
    public void setFrom(Address address) throws MessagingException {
        if (address == null) {
            removeHeader("From");
        } else {
            setHeader("From", address.toString());
        }
    }

    @Override // javax.mail.Message
    public void setFrom() throws MessagingException {
        InternetAddress me = InternetAddress.getLocalAddress(this.session);
        if (me != null) {
            setFrom(me);
            return;
        }
        throw new MessagingException("No From address");
    }

    @Override // javax.mail.Message
    public void addFrom(Address[] addresses) throws MessagingException {
        addAddressHeader("From", addresses);
    }

    public Address getSender() throws MessagingException {
        Address[] a = getAddressHeader("Sender");
        if (a == null) {
            return null;
        }
        return a[0];
    }

    public void setSender(Address address) throws MessagingException {
        if (address == null) {
            removeHeader("Sender");
        } else {
            setHeader("Sender", address.toString());
        }
    }

    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/MimeMessage$RecipientType.class */
    public static class RecipientType extends Message.RecipientType {
        public static final RecipientType NEWSGROUPS = new RecipientType("Newsgroups");

        protected RecipientType(String type) {
            super(type);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // javax.mail.Message.RecipientType
        public Object readResolve() throws ObjectStreamException {
            if (this.type.equals("Newsgroups")) {
                return NEWSGROUPS;
            }
            return super.readResolve();
        }
    }

    @Override // javax.mail.Message
    public Address[] getRecipients(Message.RecipientType type) throws MessagingException {
        if (type == RecipientType.NEWSGROUPS) {
            String s = getHeader("Newsgroups", ",");
            if (s == null) {
                return null;
            }
            return NewsAddress.parse(s);
        }
        return getAddressHeader(getHeaderName(type));
    }

    @Override // javax.mail.Message
    public Address[] getAllRecipients() throws MessagingException {
        Address[] all = super.getAllRecipients();
        Address[] ng = getRecipients(RecipientType.NEWSGROUPS);
        if (ng == null) {
            return all;
        }
        int numRecip = (all != null ? all.length : 0) + (ng != null ? ng.length : 0);
        Address[] addresses = new Address[numRecip];
        int pos = 0;
        if (all != null) {
            System.arraycopy(all, 0, addresses, 0, all.length);
            pos = 0 + all.length;
        }
        if (ng != null) {
            System.arraycopy(ng, 0, addresses, pos, ng.length);
            int length = pos + ng.length;
        }
        return addresses;
    }

    @Override // javax.mail.Message
    public void setRecipients(Message.RecipientType type, Address[] addresses) throws MessagingException {
        if (type == RecipientType.NEWSGROUPS) {
            if (addresses == null || addresses.length == 0) {
                removeHeader("Newsgroups");
                return;
            } else {
                setHeader("Newsgroups", NewsAddress.toString(addresses));
                return;
            }
        }
        setAddressHeader(getHeaderName(type), addresses);
    }

    public void setRecipients(Message.RecipientType type, String addresses) throws MessagingException {
        if (type == RecipientType.NEWSGROUPS) {
            if (addresses == null || addresses.length() == 0) {
                removeHeader("Newsgroups");
                return;
            } else {
                setHeader("Newsgroups", addresses);
                return;
            }
        }
        setAddressHeader(getHeaderName(type), InternetAddress.parse(addresses));
    }

    @Override // javax.mail.Message
    public void addRecipients(Message.RecipientType type, Address[] addresses) throws MessagingException {
        if (type == RecipientType.NEWSGROUPS) {
            String s = NewsAddress.toString(addresses);
            if (s != null) {
                addHeader("Newsgroups", s);
                return;
            }
            return;
        }
        addAddressHeader(getHeaderName(type), addresses);
    }

    public void addRecipients(Message.RecipientType type, String addresses) throws MessagingException {
        if (type == RecipientType.NEWSGROUPS) {
            if (addresses != null && addresses.length() != 0) {
                addHeader("Newsgroups", addresses);
                return;
            }
            return;
        }
        addAddressHeader(getHeaderName(type), InternetAddress.parse(addresses));
    }

    @Override // javax.mail.Message
    public Address[] getReplyTo() throws MessagingException {
        Address[] a = getAddressHeader("Reply-To");
        if (a == null) {
            a = getFrom();
        }
        return a;
    }

    @Override // javax.mail.Message
    public void setReplyTo(Address[] addresses) throws MessagingException {
        setAddressHeader("Reply-To", addresses);
    }

    private Address[] getAddressHeader(String name) throws MessagingException {
        String s = getHeader(name, ",");
        if (s == null) {
            return null;
        }
        return InternetAddress.parseHeader(s, this.strict);
    }

    private void setAddressHeader(String name, Address[] addresses) throws MessagingException {
        String s = InternetAddress.toString(addresses);
        if (s == null) {
            removeHeader(name);
        } else {
            setHeader(name, s);
        }
    }

    private void addAddressHeader(String name, Address[] addresses) throws MessagingException {
        String s = InternetAddress.toString(addresses);
        if (s == null) {
            return;
        }
        addHeader(name, s);
    }

    @Override // javax.mail.Message
    public String getSubject() throws MessagingException {
        String rawvalue = getHeader("Subject", null);
        if (rawvalue == null) {
            return null;
        }
        try {
            return MimeUtility.decodeText(MimeUtility.unfold(rawvalue));
        } catch (UnsupportedEncodingException e) {
            return rawvalue;
        }
    }

    @Override // javax.mail.Message
    public void setSubject(String subject) throws MessagingException {
        setSubject(subject, null);
    }

    public void setSubject(String subject, String charset) throws MessagingException {
        if (subject == null) {
            removeHeader("Subject");
        }
        try {
            setHeader("Subject", MimeUtility.fold(9, MimeUtility.encodeText(subject, charset, null)));
        } catch (UnsupportedEncodingException uex) {
            throw new MessagingException("Encoding error", uex);
        }
    }

    @Override // javax.mail.Message
    public Date getSentDate() throws MessagingException {
        Date parse;
        String s = getHeader("Date", null);
        if (s != null) {
            try {
                synchronized (mailDateFormat) {
                    parse = mailDateFormat.parse(s);
                }
                return parse;
            } catch (java.text.ParseException e) {
                return null;
            }
        }
        return null;
    }

    @Override // javax.mail.Message
    public void setSentDate(Date d) throws MessagingException {
        if (d == null) {
            removeHeader("Date");
            return;
        }
        synchronized (mailDateFormat) {
            setHeader("Date", mailDateFormat.format(d));
        }
    }

    @Override // javax.mail.Message
    public Date getReceivedDate() throws MessagingException {
        return null;
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
            return "text/plain";
        }
        return s;
    }

    @Override // javax.mail.Part
    public boolean isMimeType(String mimeType) throws MessagingException {
        return MimeBodyPart.isMimeType(this, mimeType);
    }

    @Override // javax.mail.Part
    public String getDisposition() throws MessagingException {
        return MimeBodyPart.getDisposition(this);
    }

    @Override // javax.mail.Part
    public void setDisposition(String disposition) throws MessagingException {
        MimeBodyPart.setDisposition(this, disposition);
    }

    @Override // javax.mail.internet.MimePart
    public String getEncoding() throws MessagingException {
        return MimeBodyPart.getEncoding(this);
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

    @Override // javax.mail.Part
    public String getDescription() throws MessagingException {
        return MimeBodyPart.getDescription(this);
    }

    @Override // javax.mail.Part
    public void setDescription(String description) throws MessagingException {
        setDescription(description, null);
    }

    public void setDescription(String description, String charset) throws MessagingException {
        MimeBodyPart.setDescription(this, description, charset);
    }

    @Override // javax.mail.internet.MimePart
    public String[] getContentLanguage() throws MessagingException {
        return MimeBodyPart.getContentLanguage(this);
    }

    @Override // javax.mail.internet.MimePart
    public void setContentLanguage(String[] languages) throws MessagingException {
        MimeBodyPart.setContentLanguage(this, languages);
    }

    public String getMessageID() throws MessagingException {
        return getHeader("Message-ID", null);
    }

    @Override // javax.mail.Part
    public String getFileName() throws MessagingException {
        return MimeBodyPart.getFileName(this);
    }

    @Override // javax.mail.Part
    public void setFileName(String filename) throws MessagingException {
        MimeBodyPart.setFileName(this, filename);
    }

    private String getHeaderName(Message.RecipientType type) throws MessagingException {
        String headerName;
        if (type == Message.RecipientType.TO) {
            headerName = "To";
        } else if (type == Message.RecipientType.CC) {
            headerName = "Cc";
        } else if (type == Message.RecipientType.BCC) {
            headerName = "Bcc";
        } else if (type == RecipientType.NEWSGROUPS) {
            headerName = "Newsgroups";
        } else {
            throw new MessagingException("Invalid Recipient Type");
        }
        return headerName;
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
            return new SharedByteArrayInputStream(this.content);
        }
        throw new MessagingException("No content");
    }

    public InputStream getRawInputStream() throws MessagingException {
        return getContentStream();
    }

    @Override // javax.mail.Part
    public synchronized DataHandler getDataHandler() throws MessagingException {
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
        MimeBodyPart.invalidateContentHeaders(this);
    }

    @Override // javax.mail.Part
    public void setContent(Object o, String type) throws MessagingException {
        setDataHandler(new DataHandler(o, type));
    }

    @Override // javax.mail.Part, javax.mail.internet.MimePart
    public void setText(String text) throws MessagingException {
        setText(text, null);
    }

    @Override // javax.mail.internet.MimePart
    public void setText(String text, String charset) throws MessagingException {
        MimeBodyPart.setText(this, text, charset);
    }

    @Override // javax.mail.Part
    public void setContent(Multipart mp) throws MessagingException {
        setDataHandler(new DataHandler(mp, mp.getContentType()));
        mp.setParent(this);
    }

    @Override // javax.mail.Message
    public Message reply(boolean replyToAll) throws MessagingException {
        MimeMessage reply = new MimeMessage(this.session);
        String subject = getHeader("Subject", null);
        if (subject != null) {
            if (!subject.regionMatches(true, 0, "Re: ", 0, 4)) {
                subject = new StringBuffer().append("Re: ").append(subject).toString();
            }
            reply.setHeader("Subject", subject);
        }
        Address[] a = getReplyTo();
        reply.setRecipients(Message.RecipientType.TO, a);
        if (replyToAll) {
            Vector v = new Vector();
            InternetAddress me = InternetAddress.getLocalAddress(this.session);
            if (me != null) {
                v.addElement(me);
            }
            String alternates = this.session.getProperty("mail.alternates");
            if (alternates != null) {
                eliminateDuplicates(v, InternetAddress.parse(alternates, false));
            }
            String replyallccStr = this.session.getProperty("mail.replyallcc");
            boolean replyallcc = replyallccStr != null && replyallccStr.equalsIgnoreCase("true");
            eliminateDuplicates(v, a);
            Address[] a2 = eliminateDuplicates(v, getRecipients(Message.RecipientType.TO));
            if (a2 != null && a2.length > 0) {
                if (replyallcc) {
                    reply.addRecipients(Message.RecipientType.CC, a2);
                } else {
                    reply.addRecipients(Message.RecipientType.TO, a2);
                }
            }
            Address[] a3 = eliminateDuplicates(v, getRecipients(Message.RecipientType.CC));
            if (a3 != null && a3.length > 0) {
                reply.addRecipients(Message.RecipientType.CC, a3);
            }
            Address[] a4 = getRecipients(RecipientType.NEWSGROUPS);
            if (a4 != null && a4.length > 0) {
                reply.setRecipients(RecipientType.NEWSGROUPS, a4);
            }
        }
        String msgId = getHeader("Message-Id", null);
        if (msgId != null) {
            reply.setHeader("In-Reply-To", msgId);
        }
        try {
            setFlags(answeredFlag, true);
        } catch (MessagingException e) {
        }
        return reply;
    }

    private Address[] eliminateDuplicates(Vector v, Address[] addrs) {
        Address[] a;
        if (addrs == null) {
            return null;
        }
        int gone = 0;
        for (int i = 0; i < addrs.length; i++) {
            boolean found = false;
            int j = 0;
            while (true) {
                if (j < v.size()) {
                    if (!((InternetAddress) v.elementAt(j)).equals(addrs[i])) {
                        j++;
                    } else {
                        found = true;
                        gone++;
                        addrs[i] = null;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!found) {
                v.addElement(addrs[i]);
            }
        }
        if (gone != 0) {
            if (addrs instanceof InternetAddress[]) {
                a = new InternetAddress[addrs.length - gone];
            } else {
                a = new Address[addrs.length - gone];
            }
            int j2 = 0;
            for (int i2 = 0; i2 < addrs.length; i2++) {
                if (addrs[i2] != null) {
                    int i3 = j2;
                    j2++;
                    a[i3] = addrs[i2];
                }
            }
            addrs = a;
        }
        return addrs;
    }

    @Override // javax.mail.Part
    public void writeTo(OutputStream os) throws IOException, MessagingException {
        writeTo(os, null);
    }

    public void writeTo(OutputStream os, String[] ignoreList) throws IOException, MessagingException {
        if (!this.saved) {
            saveChanges();
        }
        if (this.modified) {
            MimeBodyPart.writeTo(this, os, ignoreList);
            return;
        }
        Enumeration hdrLines = getNonMatchingHeaderLines(ignoreList);
        LineOutputStream los = new LineOutputStream(os);
        while (hdrLines.hasMoreElements()) {
            los.writeln((String) hdrLines.nextElement());
        }
        los.writeln();
        if (this.content == null) {
            InputStream is = getContentStream();
            byte[] buf = new byte[8192];
            while (true) {
                int read = is.read(buf);
                if (read <= 0) {
                    break;
                }
                os.write(buf, 0, read);
            }
            is.close();
        } else {
            os.write(this.content);
        }
        os.flush();
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

    @Override // javax.mail.Message
    public synchronized Flags getFlags() throws MessagingException {
        return (Flags) this.flags.clone();
    }

    @Override // javax.mail.Message
    public synchronized boolean isSet(Flags.Flag flag) throws MessagingException {
        return this.flags.contains(flag);
    }

    @Override // javax.mail.Message
    public synchronized void setFlags(Flags flag, boolean set) throws MessagingException {
        if (set) {
            this.flags.add(flag);
        } else {
            this.flags.remove(flag);
        }
    }

    @Override // javax.mail.Message
    public void saveChanges() throws MessagingException {
        this.modified = true;
        this.saved = true;
        updateHeaders();
    }

    protected void updateHeaders() throws MessagingException {
        MimeBodyPart.updateHeaders(this);
        setHeader("Mime-Version", "1.0");
        setHeader("Message-ID", new StringBuffer().append("<").append(UniqueValue.getUniqueMessageIDValue(this.session)).append(">").toString());
    }

    protected InternetHeaders createInternetHeaders(InputStream is) throws MessagingException {
        return new InternetHeaders(is);
    }
}
