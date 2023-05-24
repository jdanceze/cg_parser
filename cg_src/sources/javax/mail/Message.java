package javax.mail;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Date;
import javax.mail.Flags;
import javax.mail.search.SearchTerm;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/Message.class */
public abstract class Message implements Part {
    protected int msgnum;
    protected boolean expunged;
    protected Folder folder;
    protected Session session;

    public abstract Address[] getFrom() throws MessagingException;

    public abstract void setFrom() throws MessagingException;

    public abstract void setFrom(Address address) throws MessagingException;

    public abstract void addFrom(Address[] addressArr) throws MessagingException;

    public abstract Address[] getRecipients(RecipientType recipientType) throws MessagingException;

    public abstract void setRecipients(RecipientType recipientType, Address[] addressArr) throws MessagingException;

    public abstract void addRecipients(RecipientType recipientType, Address[] addressArr) throws MessagingException;

    public abstract String getSubject() throws MessagingException;

    public abstract void setSubject(String str) throws MessagingException;

    public abstract Date getSentDate() throws MessagingException;

    public abstract void setSentDate(Date date) throws MessagingException;

    public abstract Date getReceivedDate() throws MessagingException;

    public abstract Flags getFlags() throws MessagingException;

    public abstract void setFlags(Flags flags, boolean z) throws MessagingException;

    public abstract Message reply(boolean z) throws MessagingException;

    public abstract void saveChanges() throws MessagingException;

    protected Message() {
        this.msgnum = 0;
        this.expunged = false;
        this.folder = null;
        this.session = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Message(Folder folder, int msgnum) {
        this.msgnum = 0;
        this.expunged = false;
        this.folder = null;
        this.session = null;
        this.folder = folder;
        this.msgnum = msgnum;
        this.session = folder.store.session;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Message(Session session) {
        this.msgnum = 0;
        this.expunged = false;
        this.folder = null;
        this.session = null;
        this.session = session;
    }

    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/Message$RecipientType.class */
    public static class RecipientType implements Serializable {
        public static final RecipientType TO = new RecipientType("To");
        public static final RecipientType CC = new RecipientType("Cc");
        public static final RecipientType BCC = new RecipientType("Bcc");
        protected String type;

        /* JADX INFO: Access modifiers changed from: protected */
        public RecipientType(String type) {
            this.type = type;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public Object readResolve() throws ObjectStreamException {
            if (this.type.equals("To")) {
                return TO;
            }
            if (this.type.equals("Cc")) {
                return CC;
            }
            if (this.type.equals("Bcc")) {
                return BCC;
            }
            throw new InvalidObjectException(new StringBuffer().append("Attempt to resolve unknown RecipientType: ").append(this.type).toString());
        }
    }

    public Address[] getAllRecipients() throws MessagingException {
        Address[] to = getRecipients(RecipientType.TO);
        Address[] cc = getRecipients(RecipientType.CC);
        Address[] bcc = getRecipients(RecipientType.BCC);
        if (cc == null && bcc == null) {
            return to;
        }
        int numRecip = (to != null ? to.length : 0) + (cc != null ? cc.length : 0) + (bcc != null ? bcc.length : 0);
        Address[] addresses = new Address[numRecip];
        int pos = 0;
        if (to != null) {
            System.arraycopy(to, 0, addresses, 0, to.length);
            pos = 0 + to.length;
        }
        if (cc != null) {
            System.arraycopy(cc, 0, addresses, pos, cc.length);
            pos += cc.length;
        }
        if (bcc != null) {
            System.arraycopy(bcc, 0, addresses, pos, bcc.length);
            int length = pos + bcc.length;
        }
        return addresses;
    }

    public void setRecipient(RecipientType type, Address address) throws MessagingException {
        Address[] a = {address};
        setRecipients(type, a);
    }

    public void addRecipient(RecipientType type, Address address) throws MessagingException {
        Address[] a = {address};
        addRecipients(type, a);
    }

    public Address[] getReplyTo() throws MessagingException {
        return getFrom();
    }

    public void setReplyTo(Address[] addresses) throws MessagingException {
        throw new MethodNotSupportedException("setReplyTo not supported");
    }

    public boolean isSet(Flags.Flag flag) throws MessagingException {
        return getFlags().contains(flag);
    }

    public void setFlag(Flags.Flag flag, boolean set) throws MessagingException {
        Flags f = new Flags(flag);
        setFlags(f, set);
    }

    public int getMessageNumber() {
        return this.msgnum;
    }

    protected void setMessageNumber(int msgnum) {
        this.msgnum = msgnum;
    }

    public Folder getFolder() {
        return this.folder;
    }

    public boolean isExpunged() {
        return this.expunged;
    }

    protected void setExpunged(boolean expunged) {
        this.expunged = expunged;
    }

    public boolean match(SearchTerm term) throws MessagingException {
        return term.match(this);
    }
}
