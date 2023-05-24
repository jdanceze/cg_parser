package javax.mail;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/ReadOnlyFolderException.class */
public class ReadOnlyFolderException extends MessagingException {
    private transient Folder folder;

    public ReadOnlyFolderException(Folder folder) {
        this(folder, null);
    }

    public ReadOnlyFolderException(Folder folder, String message) {
        super(message);
        this.folder = folder;
    }

    public Folder getFolder() {
        return this.folder;
    }
}
