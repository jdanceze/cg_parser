package org.hamcrest.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import org.hamcrest.Condition;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/beans/HasPropertyWithValue.class */
public class HasPropertyWithValue<T> extends TypeSafeDiagnosingMatcher<T> {
    private static final Condition.Step<PropertyDescriptor, Method> WITH_READ_METHOD = withReadMethod();
    private final String propertyName;
    private final Matcher<Object> valueMatcher;

    public HasPropertyWithValue(String propertyName, Matcher<?> valueMatcher) {
        this.propertyName = propertyName;
        this.valueMatcher = nastyGenericsWorkaround(valueMatcher);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.hamcrest.TypeSafeDiagnosingMatcher
    public boolean matchesSafely(T bean, Description mismatch) {
        return propertyOn(bean, mismatch).and(WITH_READ_METHOD).and(withPropertyValue(bean)).matching(this.valueMatcher, "property '" + this.propertyName + "' ");
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("hasProperty(").appendValue(this.propertyName).appendText(", ").appendDescriptionOf(this.valueMatcher).appendText(")");
    }

    private Condition<PropertyDescriptor> propertyOn(T bean, Description mismatch) {
        PropertyDescriptor property = PropertyUtil.getPropertyDescriptor(this.propertyName, bean);
        if (property == null) {
            mismatch.appendText("No property \"" + this.propertyName + "\"");
            return Condition.notMatched();
        }
        return Condition.matched(property, mismatch);
    }

    private Condition.Step<Method, Object> withPropertyValue(final T bean) {
        return new Condition.Step<Method, Object>() { // from class: org.hamcrest.beans.HasPropertyWithValue.1
            @Override // org.hamcrest.Condition.Step
            public Condition<Object> apply(Method readMethod, Description mismatch) {
                try {
                    return Condition.matched(readMethod.invoke(bean, PropertyUtil.NO_ARGUMENTS), mismatch);
                } catch (Exception e) {
                    mismatch.appendText(e.getMessage());
                    return Condition.notMatched();
                }
            }
        };
    }

    private static Matcher<Object> nastyGenericsWorkaround(Matcher<?> valueMatcher) {
        return valueMatcher;
    }

    private static Condition.Step<PropertyDescriptor, Method> withReadMethod() {
        return new Condition.Step<PropertyDescriptor, Method>() { // from class: org.hamcrest.beans.HasPropertyWithValue.2
            @Override // org.hamcrest.Condition.Step
            public Condition<Method> apply(PropertyDescriptor property, Description mismatch) {
                Method readMethod = property.getReadMethod();
                if (null == readMethod) {
                    mismatch.appendText("property \"" + property.getName() + "\" is not readable");
                    return Condition.notMatched();
                }
                return Condition.matched(readMethod, mismatch);
            }
        };
    }

    @Factory
    public static <T> Matcher<T> hasProperty(String propertyName, Matcher<?> valueMatcher) {
        return new HasPropertyWithValue(propertyName, valueMatcher);
    }
}
