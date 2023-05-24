package org.junit.internal;

import java.lang.reflect.Array;
import java.util.Arrays;
import org.junit.Assert;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/ComparisonCriteria.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/ComparisonCriteria.class */
public abstract class ComparisonCriteria {
    private static final Object END_OF_ARRAY_SENTINEL = objectWithToString("end of array");

    protected abstract void assertElementsEqual(Object obj, Object obj2);

    public void arrayEquals(String message, Object expecteds, Object actuals) throws ArrayComparisonFailure {
        arrayEquals(message, expecteds, actuals, true);
    }

    private void arrayEquals(String message, Object expecteds, Object actuals, boolean outer) throws ArrayComparisonFailure {
        if (expecteds == actuals || Arrays.deepEquals(new Object[]{expecteds}, new Object[]{actuals})) {
            return;
        }
        String header = message == null ? "" : message + ": ";
        String exceptionMessage = outer ? header : "";
        if (expecteds == null) {
            Assert.fail(exceptionMessage + "expected array was null");
        }
        if (actuals == null) {
            Assert.fail(exceptionMessage + "actual array was null");
        }
        int actualsLength = Array.getLength(actuals);
        int expectedsLength = Array.getLength(expecteds);
        if (actualsLength != expectedsLength) {
            header = header + "array lengths differed, expected.length=" + expectedsLength + " actual.length=" + actualsLength + "; ";
        }
        int prefixLength = Math.min(actualsLength, expectedsLength);
        for (int i = 0; i < prefixLength; i++) {
            Object expected = Array.get(expecteds, i);
            Object actual = Array.get(actuals, i);
            if (isArray(expected) && isArray(actual)) {
                try {
                    arrayEquals(message, expected, actual, false);
                } catch (ArrayComparisonFailure e) {
                    e.addDimension(i);
                    throw e;
                } catch (AssertionError e2) {
                    throw new ArrayComparisonFailure(header, e2, i);
                }
            } else {
                try {
                    assertElementsEqual(expected, actual);
                } catch (AssertionError e3) {
                    throw new ArrayComparisonFailure(header, e3, i);
                }
            }
        }
        if (actualsLength != expectedsLength) {
            try {
                Assert.assertEquals(getToStringableArrayElement(expecteds, expectedsLength, prefixLength), getToStringableArrayElement(actuals, actualsLength, prefixLength));
            } catch (AssertionError e4) {
                throw new ArrayComparisonFailure(header, e4, prefixLength);
            }
        }
    }

    private Object getToStringableArrayElement(Object array, int length, int index) {
        if (index < length) {
            Object element = Array.get(array, index);
            if (isArray(element)) {
                return objectWithToString(componentTypeName(element.getClass()) + "[" + Array.getLength(element) + "]");
            }
            return element;
        }
        return END_OF_ARRAY_SENTINEL;
    }

    private static Object objectWithToString(final String string) {
        return new Object() { // from class: org.junit.internal.ComparisonCriteria.1
            public String toString() {
                return string;
            }
        };
    }

    private String componentTypeName(Class<?> arrayClass) {
        Class<?> componentType = arrayClass.getComponentType();
        if (componentType.isArray()) {
            return componentTypeName(componentType) + "[]";
        }
        return componentType.getName();
    }

    private boolean isArray(Object expected) {
        return expected != null && expected.getClass().isArray();
    }
}
