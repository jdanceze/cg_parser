package javax.mail;

import javax.mail.FetchProfile;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/UIDFolder.class */
public interface UIDFolder {
    public static final long LASTUID = -1;

    long getUIDValidity() throws MessagingException;

    Message getMessageByUID(long j) throws MessagingException;

    Message[] getMessagesByUID(long j, long j2) throws MessagingException;

    Message[] getMessagesByUID(long[] jArr) throws MessagingException;

    long getUID(Message message) throws MessagingException;

    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/UIDFolder$FetchProfileItem.class */
    public static class FetchProfileItem extends FetchProfile.Item {
        public static final FetchProfileItem UID = new FetchProfileItem("UID");

        protected FetchProfileItem(String name) {
            super(name);
        }
    }
}
