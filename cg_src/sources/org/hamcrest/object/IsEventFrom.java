package org.hamcrest.object;

import java.util.EventObject;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/object/IsEventFrom.class */
public class IsEventFrom extends TypeSafeDiagnosingMatcher<EventObject> {
    private final Class<?> eventClass;
    private final Object source;

    public IsEventFrom(Class<?> eventClass, Object source) {
        this.eventClass = eventClass;
        this.source = source;
    }

    @Override // org.hamcrest.TypeSafeDiagnosingMatcher
    public boolean matchesSafely(EventObject item, Description mismatchDescription) {
        if (!this.eventClass.isInstance(item)) {
            mismatchDescription.appendText("item type was " + item.getClass().getName());
            return false;
        } else if (!eventHasSameSource(item)) {
            mismatchDescription.appendText("source was ").appendValue(item.getSource());
            return false;
        } else {
            return true;
        }
    }

    private boolean eventHasSameSource(EventObject ev) {
        return ev.getSource() == this.source;
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("an event of type ").appendText(this.eventClass.getName()).appendText(" from ").appendValue(this.source);
    }

    @Factory
    public static Matcher<EventObject> eventFrom(Class<? extends EventObject> eventClass, Object source) {
        return new IsEventFrom(eventClass, source);
    }

    @Factory
    public static Matcher<EventObject> eventFrom(Object source) {
        return eventFrom(EventObject.class, source);
    }
}
