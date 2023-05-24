package javax.mail;

import java.util.Vector;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/Transport.class */
public abstract class Transport extends Service {
    private Vector transportListeners;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: javax.mail.Transport.send0(javax.mail.Message, javax.mail.Address[]):void, file: gencallgraphv3.jar:j2ee.jar:javax/mail/Transport.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:107)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0xa8
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    private static void send0(javax.mail.Message r0, javax.mail.Address[] r1) throws javax.mail.MessagingException {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: javax.mail.Transport.send0(javax.mail.Message, javax.mail.Address[]):void, file: gencallgraphv3.jar:j2ee.jar:javax/mail/Transport.class
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.Transport.send0(javax.mail.Message, javax.mail.Address[]):void");
    }

    public abstract void sendMessage(Message message, Address[] addressArr) throws MessagingException;

    public Transport(Session session, URLName urlname) {
        super(session, urlname);
        this.transportListeners = null;
    }

    public static void send(Message msg) throws MessagingException {
        msg.saveChanges();
        send0(msg, msg.getAllRecipients());
    }

    public static void send(Message msg, Address[] addresses) throws MessagingException {
        msg.saveChanges();
        send0(msg, addresses);
    }

    public synchronized void addTransportListener(TransportListener l) {
        if (this.transportListeners == null) {
            this.transportListeners = new Vector();
        }
        this.transportListeners.addElement(l);
    }

    public synchronized void removeTransportListener(TransportListener l) {
        if (this.transportListeners != null) {
            this.transportListeners.removeElement(l);
        }
    }

    protected void notifyTransportListeners(int type, Address[] validSent, Address[] validUnsent, Address[] invalid, Message msg) {
        if (this.transportListeners == null) {
            return;
        }
        TransportEvent e = new TransportEvent(this, type, validSent, validUnsent, invalid, msg);
        queueEvent(e, this.transportListeners);
    }
}
