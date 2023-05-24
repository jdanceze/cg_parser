package com.google.common.eventbus;
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/eventbus/SubscriberExceptionHandler.class */
public interface SubscriberExceptionHandler {
    void handleException(Throwable th, SubscriberExceptionContext subscriberExceptionContext);
}
