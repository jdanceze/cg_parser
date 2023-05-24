package javax.mail;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/FolderClosedException.class */
public class FolderClosedException extends MessagingException {
    private transient Folder folder;

    public FolderClosedException(Folder folder) {
        this(folder, null);
    }

    public FolderClosedException(Folder folder, String message) {
        super(message);
        this.folder = folder;
    }

    public Folder getFolder() {
        return this.folder;
    }
}
