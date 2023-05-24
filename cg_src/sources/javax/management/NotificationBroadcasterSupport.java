package javax.management;

import com.sun.jmx.trace.Trace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/NotificationBroadcasterSupport.class */
public class NotificationBroadcasterSupport implements NotificationEmitter {
    private List listenerList = Collections.EMPTY_LIST;
    static Class class$javax$management$NotificationBroadcasterSupport;

    @Override // javax.management.NotificationBroadcaster
    public void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) {
        if (notificationListener == null) {
            throw new IllegalArgumentException("Listener can't be null");
        }
        synchronized (this) {
            ArrayList arrayList = new ArrayList(this.listenerList.size() + 1);
            arrayList.addAll(this.listenerList);
            arrayList.add(new ListenerInfo(this, notificationListener, notificationFilter, obj));
            this.listenerList = arrayList;
        }
    }

    @Override // javax.management.NotificationBroadcaster
    public void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException {
        synchronized (this) {
            ArrayList arrayList = new ArrayList(this.listenerList);
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                if (((ListenerInfo) arrayList.get(size)).listener == notificationListener) {
                    arrayList.remove(size);
                }
            }
            if (arrayList.size() == this.listenerList.size()) {
                throw new ListenerNotFoundException("Listener not registered");
            }
            this.listenerList = arrayList;
        }
    }

    @Override // javax.management.NotificationEmitter
    public void removeNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException {
        boolean z = false;
        synchronized (this) {
            ArrayList arrayList = new ArrayList(this.listenerList);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ListenerInfo listenerInfo = (ListenerInfo) arrayList.get(i);
                if (listenerInfo.listener == notificationListener) {
                    z = true;
                    if (listenerInfo.filter == notificationFilter && listenerInfo.handback == obj) {
                        arrayList.remove(i);
                        this.listenerList = arrayList;
                        return;
                    }
                }
            }
            if (z) {
                throw new ListenerNotFoundException("Listener not registered with this filter and handback");
            }
            throw new ListenerNotFoundException("Listener not registered");
        }
    }

    @Override // javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        return new MBeanNotificationInfo[0];
    }

    public void sendNotification(Notification notification) {
        List list;
        if (notification == null) {
            return;
        }
        synchronized (this) {
            list = this.listenerList;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ListenerInfo listenerInfo = (ListenerInfo) list.get(i);
            if (listenerInfo.filter == null || listenerInfo.filter.isNotificationEnabled(notification)) {
                try {
                    handleNotification(listenerInfo.listener, notification, listenerInfo.handback);
                } catch (Exception e) {
                    trace("sendNotification", new StringBuffer().append("exception from listener: ").append(e).toString());
                }
            }
        }
    }

    protected void handleNotification(NotificationListener notificationListener, Notification notification, Object obj) {
        notificationListener.handleNotification(notification, obj);
    }

    private static void trace(String str, String str2) {
        Class cls;
        if (Trace.isSelected(1, 16)) {
            if (class$javax$management$NotificationBroadcasterSupport == null) {
                cls = class$("javax.management.NotificationBroadcasterSupport");
                class$javax$management$NotificationBroadcasterSupport = cls;
            } else {
                cls = class$javax$management$NotificationBroadcasterSupport;
            }
            Trace.send(1, 16, cls.getName(), str, str2);
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/NotificationBroadcasterSupport$ListenerInfo.class */
    public class ListenerInfo {
        public NotificationListener listener;
        NotificationFilter filter;
        Object handback;
        private final NotificationBroadcasterSupport this$0;

        public ListenerInfo(NotificationBroadcasterSupport notificationBroadcasterSupport, NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) {
            this.this$0 = notificationBroadcasterSupport;
            this.listener = notificationListener;
            this.filter = notificationFilter;
            this.handback = obj;
        }
    }
}
