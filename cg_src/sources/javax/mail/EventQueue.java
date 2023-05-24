package javax.mail;

import java.util.Vector;
import javax.mail.event.MailEvent;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/EventQueue.class */
class EventQueue implements Runnable {
    private QueueElement head = null;
    private QueueElement tail = null;
    private Thread qThread = new Thread(this, "JavaMail-EventQueue");

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/EventQueue$QueueElement.class */
    public class QueueElement {
        QueueElement next = null;
        QueueElement prev = null;
        MailEvent event;
        Vector vector;
        private final EventQueue this$0;

        QueueElement(EventQueue this$0, MailEvent event, Vector vector) {
            this.this$0 = this$0;
            this.event = null;
            this.vector = null;
            this.event = event;
            this.vector = vector;
        }
    }

    public EventQueue() {
        this.qThread.setDaemon(true);
        this.qThread.start();
    }

    public synchronized void enqueue(MailEvent event, Vector vector) {
        QueueElement newElt = new QueueElement(this, event, vector);
        if (this.head == null) {
            this.head = newElt;
            this.tail = newElt;
        } else {
            newElt.next = this.head;
            this.head.prev = newElt;
            this.head = newElt;
        }
        notify();
    }

    private synchronized QueueElement dequeue() throws InterruptedException {
        while (this.tail == null) {
            wait();
        }
        QueueElement elt = this.tail;
        this.tail = elt.prev;
        if (this.tail == null) {
            this.head = null;
        } else {
            this.tail.next = null;
        }
        elt.next = null;
        elt.prev = null;
        return elt;
    }

    @Override // java.lang.Runnable
    public void run() {
        while (true) {
            try {
                QueueElement dequeue = dequeue();
                if (dequeue != null) {
                    MailEvent e = dequeue.event;
                    Vector v = dequeue.vector;
                    for (int i = 0; i < v.size(); i++) {
                        e.dispatch(v.elementAt(i));
                    }
                } else {
                    return;
                }
            } catch (InterruptedException e2) {
                return;
            }
        }
    }

    void stop() {
        if (this.qThread != null) {
            this.qThread.interrupt();
            this.qThread = null;
        }
    }
}
