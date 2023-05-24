package javax.mail.event;

import java.util.EventListener;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/event/FolderListener.class */
public interface FolderListener extends EventListener {
    void folderCreated(FolderEvent folderEvent);

    void folderDeleted(FolderEvent folderEvent);

    void folderRenamed(FolderEvent folderEvent);
}
