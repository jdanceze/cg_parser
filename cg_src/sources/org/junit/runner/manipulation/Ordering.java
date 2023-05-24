package org.junit.runner.manipulation;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.runner.Description;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/manipulation/Ordering.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/manipulation/Ordering.class */
public abstract class Ordering {
    private static final String CONSTRUCTOR_ERROR_FORMAT = "Ordering class %s should have a public constructor with signature %s(Ordering.Context context)";

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/manipulation/Ordering$Factory.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/manipulation/Ordering$Factory.class */
    public interface Factory {
        Ordering create(Context context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract List<Description> orderItems(Collection<Description> collection);

    public static Ordering shuffledBy(final Random random) {
        return new Ordering() { // from class: org.junit.runner.manipulation.Ordering.1
            @Override // org.junit.runner.manipulation.Ordering
            boolean validateOrderingIsCorrect() {
                return false;
            }

            @Override // org.junit.runner.manipulation.Ordering
            protected List<Description> orderItems(Collection<Description> descriptions) {
                List<Description> shuffled = new ArrayList<>(descriptions);
                Collections.shuffle(shuffled, random);
                return shuffled;
            }
        };
    }

    public static Ordering definedBy(Class<? extends Factory> factoryClass, Description annotatedTestClass) throws InvalidOrderingException {
        if (factoryClass == null) {
            throw new NullPointerException("factoryClass cannot be null");
        }
        if (annotatedTestClass == null) {
            throw new NullPointerException("annotatedTestClass cannot be null");
        }
        try {
            Constructor<? extends Factory> constructor = factoryClass.getConstructor(new Class[0]);
            Factory factory = constructor.newInstance(new Object[0]);
            return definedBy(factory, annotatedTestClass);
        } catch (NoSuchMethodException e) {
            throw new InvalidOrderingException(String.format("Ordering class %s should have a public constructor with signature %s(Ordering.Context context)", getClassName(factoryClass), factoryClass.getSimpleName()));
        } catch (Exception e2) {
            throw new InvalidOrderingException("Could not create ordering for " + annotatedTestClass, e2);
        }
    }

    public static Ordering definedBy(Factory factory, Description annotatedTestClass) throws InvalidOrderingException {
        if (factory == null) {
            throw new NullPointerException("factory cannot be null");
        }
        if (annotatedTestClass == null) {
            throw new NullPointerException("annotatedTestClass cannot be null");
        }
        return factory.create(new Context(annotatedTestClass));
    }

    private static String getClassName(Class<?> clazz) {
        String name = clazz.getCanonicalName();
        if (name == null) {
            return clazz.getName();
        }
        return name;
    }

    public void apply(Object target) throws InvalidOrderingException {
        if (target instanceof Orderable) {
            Orderable orderable = (Orderable) target;
            orderable.order(new Orderer(this));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean validateOrderingIsCorrect() {
        return true;
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/manipulation/Ordering$Context.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/manipulation/Ordering$Context.class */
    public static class Context {
        private final Description description;

        public Description getTarget() {
            return this.description;
        }

        private Context(Description description) {
            this.description = description;
        }
    }
}
