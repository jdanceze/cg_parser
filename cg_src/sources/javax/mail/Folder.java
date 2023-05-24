package javax.mail;

import java.util.Vector;
import javax.mail.Flags;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.FolderEvent;
import javax.mail.event.FolderListener;
import javax.mail.event.MailEvent;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.search.SearchTerm;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/Folder.class */
public abstract class Folder {
    protected Store store;
    public static final int HOLDS_MESSAGES = 1;
    public static final int HOLDS_FOLDERS = 2;
    public static final int READ_ONLY = 1;
    public static final int READ_WRITE = 2;
    private EventQueue q;
    protected int mode = -1;
    private volatile Vector connectionListeners = null;
    private volatile Vector folderListeners = null;
    private volatile Vector messageCountListeners = null;
    private volatile Vector messageChangedListeners = null;
    private Object qLock = new Object();

    public abstract String getName();

    public abstract String getFullName();

    public abstract Folder getParent() throws MessagingException;

    public abstract boolean exists() throws MessagingException;

    public abstract Folder[] list(String str) throws MessagingException;

    public abstract char getSeparator() throws MessagingException;

    public abstract int getType() throws MessagingException;

    public abstract boolean create(int i) throws MessagingException;

    public abstract boolean hasNewMessages() throws MessagingException;

    public abstract Folder getFolder(String str) throws MessagingException;

    public abstract boolean delete(boolean z) throws MessagingException;

    public abstract boolean renameTo(Folder folder) throws MessagingException;

    public abstract void open(int i) throws MessagingException;

    public abstract void close(boolean z) throws MessagingException;

    public abstract boolean isOpen();

    public abstract Flags getPermanentFlags();

    public abstract int getMessageCount() throws MessagingException;

    public abstract Message getMessage(int i) throws MessagingException;

    public abstract void appendMessages(Message[] messageArr) throws MessagingException;

    public abstract Message[] expunge() throws MessagingException;

    protected Folder(Store store) {
        this.store = store;
    }

    public URLName getURLName() throws MessagingException {
        URLName storeURL = getStore().getURLName();
        String fullname = getFullName();
        StringBuffer encodedName = new StringBuffer();
        getSeparator();
        if (fullname != null) {
            encodedName.append(fullname);
        }
        return new URLName(storeURL.getProtocol(), storeURL.getHost(), storeURL.getPort(), encodedName.toString(), storeURL.getUsername(), null);
    }

    public Store getStore() {
        return this.store;
    }

    public Folder[] listSubscribed(String pattern) throws MessagingException {
        return list(pattern);
    }

    public Folder[] list() throws MessagingException {
        return list("%");
    }

    public Folder[] listSubscribed() throws MessagingException {
        return listSubscribed("%");
    }

    public boolean isSubscribed() {
        return true;
    }

    public void setSubscribed(boolean subscribe) throws MessagingException {
        throw new MethodNotSupportedException();
    }

    public int getMode() {
        if (!isOpen()) {
            throw new IllegalStateException("Folder not open");
        }
        return this.mode;
    }

    public synchronized int getNewMessageCount() throws MessagingException {
        if (!isOpen()) {
            return -1;
        }
        int newmsgs = 0;
        int total = getMessageCount();
        for (int i = 1; i <= total; i++) {
            try {
                if (getMessage(i).isSet(Flags.Flag.RECENT)) {
                    newmsgs++;
                }
            } catch (MessageRemovedException e) {
            }
        }
        return newmsgs;
    }

    public synchronized int getUnreadMessageCount() throws MessagingException {
        if (!isOpen()) {
            return -1;
        }
        int unread = 0;
        int total = getMessageCount();
        for (int i = 1; i <= total; i++) {
            try {
                if (!getMessage(i).isSet(Flags.Flag.SEEN)) {
                    unread++;
                }
            } catch (MessageRemovedException e) {
            }
        }
        return unread;
    }

    public synchronized int getDeletedMessageCount() throws MessagingException {
        if (!isOpen()) {
            return -1;
        }
        int deleted = 0;
        int total = getMessageCount();
        for (int i = 1; i <= total; i++) {
            try {
                if (!getMessage(i).isSet(Flags.Flag.DELETED)) {
                    deleted++;
                }
            } catch (MessageRemovedException e) {
            }
        }
        return deleted;
    }

    public synchronized Message[] getMessages(int start, int end) throws MessagingException {
        Message[] msgs = new Message[(end - start) + 1];
        for (int i = start; i <= end; i++) {
            msgs[i - start] = getMessage(i);
        }
        return msgs;
    }

    public synchronized Message[] getMessages(int[] msgnums) throws MessagingException {
        int len = msgnums.length;
        Message[] msgs = new Message[len];
        for (int i = 0; i < len; i++) {
            msgs[i] = getMessage(msgnums[i]);
        }
        return msgs;
    }

    public synchronized Message[] getMessages() throws MessagingException {
        if (!isOpen()) {
            throw new IllegalStateException("Folder not open");
        }
        int total = getMessageCount();
        Message[] msgs = new Message[total];
        for (int i = 1; i <= total; i++) {
            msgs[i - 1] = getMessage(i);
        }
        return msgs;
    }

    public void fetch(Message[] msgs, FetchProfile fp) throws MessagingException {
    }

    public synchronized void setFlags(Message[] msgs, Flags flag, boolean value) throws MessagingException {
        for (Message message : msgs) {
            try {
                message.setFlags(flag, value);
            } catch (MessageRemovedException e) {
            }
        }
    }

    public synchronized void setFlags(int start, int end, Flags flag, boolean value) throws MessagingException {
        for (int i = start; i <= end; i++) {
            try {
                Message msg = getMessage(i);
                msg.setFlags(flag, value);
            } catch (MessageRemovedException e) {
            }
        }
    }

    public synchronized void setFlags(int[] msgnums, Flags flag, boolean value) throws MessagingException {
        for (int i : msgnums) {
            try {
                Message msg = getMessage(i);
                msg.setFlags(flag, value);
            } catch (MessageRemovedException e) {
            }
        }
    }

    public void copyMessages(Message[] msgs, Folder folder) throws MessagingException {
        if (!folder.exists()) {
            throw new FolderNotFoundException(new StringBuffer().append(folder.getFullName()).append(" does not exist").toString(), folder);
        }
        folder.appendMessages(msgs);
    }

    public Message[] search(SearchTerm term) throws MessagingException {
        return search(term, getMessages());
    }

    public Message[] search(SearchTerm term, Message[] msgs) throws MessagingException {
        Vector matchedMsgs = new Vector();
        for (int i = 0; i < msgs.length; i++) {
            try {
                if (msgs[i].match(term)) {
                    matchedMsgs.addElement(msgs[i]);
                }
            } catch (MessageRemovedException e) {
            }
        }
        Message[] m = new Message[matchedMsgs.size()];
        matchedMsgs.copyInto(m);
        return m;
    }

    public synchronized void addConnectionListener(ConnectionListener l) {
        if (this.connectionListeners == null) {
            this.connectionListeners = new Vector();
        }
        this.connectionListeners.addElement(l);
    }

    public synchronized void removeConnectionListener(ConnectionListener l) {
        if (this.connectionListeners != null) {
            this.connectionListeners.removeElement(l);
        }
    }

    protected void notifyConnectionListeners(int type) {
        if (this.connectionListeners != null) {
            ConnectionEvent e = new ConnectionEvent(this, type);
            queueEvent(e, this.connectionListeners);
        }
        if (type == 3) {
            terminateQueue();
        }
    }

    public synchronized void addFolderListener(FolderListener l) {
        if (this.folderListeners == null) {
            this.folderListeners = new Vector();
        }
        this.folderListeners.addElement(l);
    }

    public synchronized void removeFolderListener(FolderListener l) {
        if (this.folderListeners != null) {
            this.folderListeners.removeElement(l);
        }
    }

    protected void notifyFolderListeners(int type) {
        if (this.folderListeners != null) {
            FolderEvent e = new FolderEvent(this, this, type);
            queueEvent(e, this.folderListeners);
        }
        this.store.notifyFolderListeners(type, this);
    }

    protected void notifyFolderRenamedListeners(Folder folder) {
        if (this.folderListeners != null) {
            FolderEvent e = new FolderEvent(this, this, folder, 3);
            queueEvent(e, this.folderListeners);
        }
        this.store.notifyFolderRenamedListeners(this, folder);
    }

    public synchronized void addMessageCountListener(MessageCountListener l) {
        if (this.messageCountListeners == null) {
            this.messageCountListeners = new Vector();
        }
        this.messageCountListeners.addElement(l);
    }

    public synchronized void removeMessageCountListener(MessageCountListener l) {
        if (this.messageCountListeners != null) {
            this.messageCountListeners.removeElement(l);
        }
    }

    protected void notifyMessageAddedListeners(Message[] msgs) {
        if (this.messageCountListeners == null) {
            return;
        }
        MessageCountEvent e = new MessageCountEvent(this, 1, false, msgs);
        queueEvent(e, this.messageCountListeners);
    }

    protected void notifyMessageRemovedListeners(boolean removed, Message[] msgs) {
        if (this.messageCountListeners == null) {
            return;
        }
        MessageCountEvent e = new MessageCountEvent(this, 2, removed, msgs);
        queueEvent(e, this.messageCountListeners);
    }

    public synchronized void addMessageChangedListener(MessageChangedListener l) {
        if (this.messageChangedListeners == null) {
            this.messageChangedListeners = new Vector();
        }
        this.messageChangedListeners.addElement(l);
    }

    public synchronized void removeMessageChangedListener(MessageChangedListener l) {
        if (this.messageChangedListeners != null) {
            this.messageChangedListeners.removeElement(l);
        }
    }

    protected void notifyMessageChangedListeners(int type, Message msg) {
        if (this.messageChangedListeners == null) {
            return;
        }
        MessageChangedEvent e = new MessageChangedEvent(this, type, msg);
        queueEvent(e, this.messageChangedListeners);
    }

    private void queueEvent(MailEvent event, Vector vector) {
        synchronized (this.qLock) {
            if (this.q == null) {
                this.q = new EventQueue();
            }
        }
        Vector v = (Vector) vector.clone();
        this.q.enqueue(event, v);
    }

    private void terminateQueue() {
        synchronized (this.qLock) {
            if (this.q != null) {
                Vector dummyListeners = new Vector();
                dummyListeners.setSize(1);
                this.q.enqueue(new MailEvent(this, new Object()) { // from class: javax.mail.Folder.1
                    private final Folder this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // javax.mail.event.MailEvent
                    public void dispatch(Object listener) {
                        Thread.currentThread().interrupt();
                    }
                }, dummyListeners);
                this.q = null;
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        terminateQueue();
    }

    public String toString() {
        String s = getFullName();
        if (s != null) {
            return s;
        }
        return super.toString();
    }
}
