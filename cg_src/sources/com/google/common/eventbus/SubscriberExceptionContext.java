package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.Method;
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/eventbus/SubscriberExceptionContext.class */
public class SubscriberExceptionContext {
    private final EventBus eventBus;
    private final Object event;
    private final Object subscriber;
    private final Method subscriberMethod;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SubscriberExceptionContext(EventBus eventBus, Object event, Object subscriber, Method subscriberMethod) {
        this.eventBus = (EventBus) Preconditions.checkNotNull(eventBus);
        this.event = Preconditions.checkNotNull(event);
        this.subscriber = Preconditions.checkNotNull(subscriber);
        this.subscriberMethod = (Method) Preconditions.checkNotNull(subscriberMethod);
    }

    public EventBus getEventBus() {
        return this.eventBus;
    }

    public Object getEvent() {
        return this.event;
    }

    public Object getSubscriber() {
        return this.subscriber;
    }

    public Method getSubscriberMethod() {
        return this.subscriberMethod;
    }
}
