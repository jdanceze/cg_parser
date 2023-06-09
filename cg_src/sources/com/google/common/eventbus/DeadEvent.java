package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import soot.jimple.infoflow.rifl.RIFLConstants;
@Beta
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/eventbus/DeadEvent.class */
public class DeadEvent {
    private final Object source;
    private final Object event;

    public DeadEvent(Object source, Object event) {
        this.source = Preconditions.checkNotNull(source);
        this.event = Preconditions.checkNotNull(event);
    }

    public Object getSource() {
        return this.source;
    }

    public Object getEvent() {
        return this.event;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add(RIFLConstants.SOURCE_TAG, this.source).add("event", this.event).toString();
    }
}
