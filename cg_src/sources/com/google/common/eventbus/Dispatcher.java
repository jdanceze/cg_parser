package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/eventbus/Dispatcher.class */
public abstract class Dispatcher {
    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void dispatch(Object obj, Iterator<Subscriber> it);

    Dispatcher() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Dispatcher perThreadDispatchQueue() {
        return new PerThreadQueuedDispatcher();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Dispatcher legacyAsync() {
        return new LegacyAsyncDispatcher();
    }

    static Dispatcher immediate() {
        return ImmediateDispatcher.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/eventbus/Dispatcher$PerThreadQueuedDispatcher.class */
    public static final class PerThreadQueuedDispatcher extends Dispatcher {
        private final ThreadLocal<Queue<Event>> queue;
        private final ThreadLocal<Boolean> dispatching;

        private PerThreadQueuedDispatcher() {
            this.queue = new ThreadLocal<Queue<Event>>() { // from class: com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.1
                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.lang.ThreadLocal
                public Queue<Event> initialValue() {
                    return Queues.newArrayDeque();
                }
            };
            this.dispatching = new ThreadLocal<Boolean>() { // from class: com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.2
                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.lang.ThreadLocal
                public Boolean initialValue() {
                    return false;
                }
            };
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x0075 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:7:0x0050 A[Catch: all -> 0x0086, LOOP:1: B:7:0x0050->B:9:0x005d, LOOP_START, TryCatch #0 {all -> 0x0086, blocks: (B:5:0x0041, B:7:0x0050, B:9:0x005d), top: B:17:0x0041 }] */
        @Override // com.google.common.eventbus.Dispatcher
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        void dispatch(java.lang.Object r8, java.util.Iterator<com.google.common.eventbus.Subscriber> r9) {
            /*
                r7 = this;
                r0 = r8
                java.lang.Object r0 = com.google.common.base.Preconditions.checkNotNull(r0)
                r0 = r9
                java.lang.Object r0 = com.google.common.base.Preconditions.checkNotNull(r0)
                r0 = r7
                java.lang.ThreadLocal<java.util.Queue<com.google.common.eventbus.Dispatcher$PerThreadQueuedDispatcher$Event>> r0 = r0.queue
                java.lang.Object r0 = r0.get()
                java.util.Queue r0 = (java.util.Queue) r0
                r10 = r0
                r0 = r10
                com.google.common.eventbus.Dispatcher$PerThreadQueuedDispatcher$Event r1 = new com.google.common.eventbus.Dispatcher$PerThreadQueuedDispatcher$Event
                r2 = r1
                r3 = r8
                r4 = r9
                r5 = 0
                r2.<init>(r3, r4)
                boolean r0 = r0.offer(r1)
                r0 = r7
                java.lang.ThreadLocal<java.lang.Boolean> r0 = r0.dispatching
                java.lang.Object r0 = r0.get()
                java.lang.Boolean r0 = (java.lang.Boolean) r0
                boolean r0 = r0.booleanValue()
                if (r0 != 0) goto L99
                r0 = r7
                java.lang.ThreadLocal<java.lang.Boolean> r0 = r0.dispatching
                r1 = 1
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)
                r0.set(r1)
            L41:
                r0 = r10
                java.lang.Object r0 = r0.poll()     // Catch: java.lang.Throwable -> L86
                com.google.common.eventbus.Dispatcher$PerThreadQueuedDispatcher$Event r0 = (com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.Event) r0     // Catch: java.lang.Throwable -> L86
                r1 = r0
                r11 = r1
                if (r0 == 0) goto L75
            L50:
                r0 = r11
                java.util.Iterator r0 = com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.Event.access$400(r0)     // Catch: java.lang.Throwable -> L86
                boolean r0 = r0.hasNext()     // Catch: java.lang.Throwable -> L86
                if (r0 == 0) goto L41
                r0 = r11
                java.util.Iterator r0 = com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.Event.access$400(r0)     // Catch: java.lang.Throwable -> L86
                java.lang.Object r0 = r0.next()     // Catch: java.lang.Throwable -> L86
                com.google.common.eventbus.Subscriber r0 = (com.google.common.eventbus.Subscriber) r0     // Catch: java.lang.Throwable -> L86
                r1 = r11
                java.lang.Object r1 = com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.Event.access$500(r1)     // Catch: java.lang.Throwable -> L86
                r0.dispatchEvent(r1)     // Catch: java.lang.Throwable -> L86
                goto L50
            L75:
                r0 = r7
                java.lang.ThreadLocal<java.lang.Boolean> r0 = r0.dispatching
                r0.remove()
                r0 = r7
                java.lang.ThreadLocal<java.util.Queue<com.google.common.eventbus.Dispatcher$PerThreadQueuedDispatcher$Event>> r0 = r0.queue
                r0.remove()
                goto L99
            L86:
                r12 = move-exception
                r0 = r7
                java.lang.ThreadLocal<java.lang.Boolean> r0 = r0.dispatching
                r0.remove()
                r0 = r7
                java.lang.ThreadLocal<java.util.Queue<com.google.common.eventbus.Dispatcher$PerThreadQueuedDispatcher$Event>> r0 = r0.queue
                r0.remove()
                r0 = r12
                throw r0
            L99:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.eventbus.Dispatcher.PerThreadQueuedDispatcher.dispatch(java.lang.Object, java.util.Iterator):void");
        }

        /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/eventbus/Dispatcher$PerThreadQueuedDispatcher$Event.class */
        private static final class Event {
            private final Object event;
            private final Iterator<Subscriber> subscribers;

            private Event(Object event, Iterator<Subscriber> subscribers) {
                this.event = event;
                this.subscribers = subscribers;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/eventbus/Dispatcher$LegacyAsyncDispatcher.class */
    private static final class LegacyAsyncDispatcher extends Dispatcher {
        private final ConcurrentLinkedQueue<EventWithSubscriber> queue;

        private LegacyAsyncDispatcher() {
            this.queue = Queues.newConcurrentLinkedQueue();
        }

        @Override // com.google.common.eventbus.Dispatcher
        void dispatch(Object event, Iterator<Subscriber> subscribers) {
            Preconditions.checkNotNull(event);
            while (subscribers.hasNext()) {
                this.queue.add(new EventWithSubscriber(event, subscribers.next()));
            }
            while (true) {
                EventWithSubscriber e = this.queue.poll();
                if (e == null) {
                    return;
                }
                e.subscriber.dispatchEvent(e.event);
            }
        }

        /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/eventbus/Dispatcher$LegacyAsyncDispatcher$EventWithSubscriber.class */
        private static final class EventWithSubscriber {
            private final Object event;
            private final Subscriber subscriber;

            private EventWithSubscriber(Object event, Subscriber subscriber) {
                this.event = event;
                this.subscriber = subscriber;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/eventbus/Dispatcher$ImmediateDispatcher.class */
    private static final class ImmediateDispatcher extends Dispatcher {
        private static final ImmediateDispatcher INSTANCE = new ImmediateDispatcher();

        private ImmediateDispatcher() {
        }

        @Override // com.google.common.eventbus.Dispatcher
        void dispatch(Object event, Iterator<Subscriber> subscribers) {
            Preconditions.checkNotNull(event);
            while (subscribers.hasNext()) {
                subscribers.next().dispatchEvent(event);
            }
        }
    }
}
