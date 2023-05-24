package org.hamcrest.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.core.IsEqual;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/beans/SamePropertyValuesAs.class */
public class SamePropertyValuesAs<T> extends TypeSafeDiagnosingMatcher<T> {
    private final T expectedBean;
    private final Set<String> propertyNames;
    private final List<PropertyMatcher> propertyMatchers;

    public SamePropertyValuesAs(T expectedBean) {
        PropertyDescriptor[] descriptors = PropertyUtil.propertyDescriptorsFor(expectedBean, Object.class);
        this.expectedBean = expectedBean;
        this.propertyNames = propertyNamesFrom(descriptors);
        this.propertyMatchers = propertyMatchersFor(expectedBean, descriptors);
    }

    @Override // org.hamcrest.TypeSafeDiagnosingMatcher
    public boolean matchesSafely(T bean, Description mismatch) {
        return isCompatibleType(bean, mismatch) && hasNoExtraProperties(bean, mismatch) && hasMatchingValues(bean, mismatch);
    }

    @Override // org.hamcrest.SelfDescribing
    public void describeTo(Description description) {
        description.appendText("same property values as " + this.expectedBean.getClass().getSimpleName()).appendList(" [", ", ", "]", this.propertyMatchers);
    }

    private boolean isCompatibleType(T item, Description mismatchDescription) {
        if (!this.expectedBean.getClass().isAssignableFrom(item.getClass())) {
            mismatchDescription.appendText("is incompatible type: " + item.getClass().getSimpleName());
            return false;
        }
        return true;
    }

    private boolean hasNoExtraProperties(T item, Description mismatchDescription) {
        Set<String> actualPropertyNames = propertyNamesFrom(PropertyUtil.propertyDescriptorsFor(item, Object.class));
        actualPropertyNames.removeAll(this.propertyNames);
        if (!actualPropertyNames.isEmpty()) {
            mismatchDescription.appendText("has extra properties called " + actualPropertyNames);
            return false;
        }
        return true;
    }

    private boolean hasMatchingValues(T item, Description mismatchDescription) {
        for (PropertyMatcher propertyMatcher : this.propertyMatchers) {
            if (!propertyMatcher.matches(item)) {
                propertyMatcher.describeMismatch(item, mismatchDescription);
                return false;
            }
        }
        return true;
    }

    private static <T> List<PropertyMatcher> propertyMatchersFor(T bean, PropertyDescriptor[] descriptors) {
        List<PropertyMatcher> result = new ArrayList<>(descriptors.length);
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            result.add(new PropertyMatcher(propertyDescriptor, bean));
        }
        return result;
    }

    private static Set<String> propertyNamesFrom(PropertyDescriptor[] descriptors) {
        HashSet<String> result = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            result.add(propertyDescriptor.getDisplayName());
        }
        return result;
    }

    /* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/beans/SamePropertyValuesAs$PropertyMatcher.class */
    public static class PropertyMatcher extends DiagnosingMatcher<Object> {
        private final Method readMethod;
        private final Matcher<Object> matcher;
        private final String propertyName;

        public PropertyMatcher(PropertyDescriptor descriptor, Object expectedObject) {
            this.propertyName = descriptor.getDisplayName();
            this.readMethod = descriptor.getReadMethod();
            this.matcher = IsEqual.equalTo(SamePropertyValuesAs.readProperty(this.readMethod, expectedObject));
        }

        @Override // org.hamcrest.DiagnosingMatcher
        public boolean matches(Object actual, Description mismatch) {
            Object actualValue = SamePropertyValuesAs.readProperty(this.readMethod, actual);
            if (!this.matcher.matches(actualValue)) {
                mismatch.appendText(this.propertyName + Instruction.argsep);
                this.matcher.describeMismatch(actualValue, mismatch);
                return false;
            }
            return true;
        }

        @Override // org.hamcrest.SelfDescribing
        public void describeTo(Description description) {
            description.appendText(this.propertyName + ": ").appendDescriptionOf(this.matcher);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object readProperty(Method method, Object target) {
        try {
            return method.invoke(target, PropertyUtil.NO_ARGUMENTS);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not invoke " + method + " on " + target, e);
        }
    }

    @Factory
    public static <T> Matcher<T> samePropertyValuesAs(T expectedBean) {
        return new SamePropertyValuesAs(expectedBean);
    }
}
