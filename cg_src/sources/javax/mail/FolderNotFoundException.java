package javax.mail;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/FolderNotFoundException.class */
public class FolderNotFoundException extends MessagingException {
    private transient Folder folder;

    public FolderNotFoundException() {
    }

    public FolderNotFoundException(Folder folder) {
        this.folder = folder;
    }

    public FolderNotFoundException(Folder folder, String s) {
        super(s);
        this.folder = folder;
    }

    public FolderNotFoundException(String s, Folder folder) {
        super(s);
        this.folder = folder;
    }

    public Folder getFolder() {
        return this.folder;
    }
}
