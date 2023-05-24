package javax.mail;

import java.util.Vector;
import javax.mail.event.FolderEvent;
import javax.mail.event.FolderListener;
import javax.mail.event.StoreEvent;
import javax.mail.event.StoreListener;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/Store.class */
public abstract class Store extends Service {
    private volatile Vector storeListeners;
    private volatile Vector folderListeners;

    public abstract Folder getDefaultFolder() throws MessagingException;

    public abstract Folder getFolder(String str) throws MessagingException;

    public abstract Folder getFolder(URLName uRLName) throws MessagingException;

    protected Store(Session session, URLName urlname) {
        super(session, urlname);
        this.storeListeners = null;
        this.folderListeners = null;
    }

    public Folder[] getPersonalNamespaces() throws MessagingException {
        return new Folder[]{getDefaultFolder()};
    }

    public Folder[] getUserNamespaces(String user) throws MessagingException {
        return new Folder[0];
    }

    public Folder[] getSharedNamespaces() throws MessagingException {
        return new Folder[0];
    }

    public synchronized void addStoreListener(StoreListener l) {
        if (this.storeListeners == null) {
            this.storeListeners = new Vector();
        }
        this.storeListeners.addElement(l);
    }

    public synchronized void removeStoreListener(StoreListener l) {
        if (this.storeListeners != null) {
            this.storeListeners.removeElement(l);
        }
    }

    protected void notifyStoreListeners(int type, String message) {
        if (this.storeListeners == null) {
            return;
        }
        StoreEvent e = new StoreEvent(this, type, message);
        queueEvent(e, this.storeListeners);
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

    /* JADX INFO: Access modifiers changed from: protected */
    public void notifyFolderListeners(int type, Folder folder) {
        if (this.folderListeners == null) {
            return;
        }
        FolderEvent e = new FolderEvent(this, folder, type);
        queueEvent(e, this.folderListeners);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void notifyFolderRenamedListeners(Folder oldF, Folder newF) {
        if (this.folderListeners == null) {
            return;
        }
        FolderEvent e = new FolderEvent(this, oldF, newF, 3);
        queueEvent(e, this.folderListeners);
    }
}
