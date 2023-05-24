package org.junit.experimental.runners;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.junit.runners.Suite;
import org.junit.runners.model.RunnerBuilder;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/runners/Enclosed.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/runners/Enclosed.class */
public class Enclosed extends Suite {
    public Enclosed(Class<?> klass, RunnerBuilder builder) throws Throwable {
        super(builder, klass, filterAbstractClasses(klass.getClasses()));
    }

    private static Class<?>[] filterAbstractClasses(Class<?>[] classes) {
        List<Class<?>> filteredList = new ArrayList<>(classes.length);
        for (Class<?> clazz : classes) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                filteredList.add(clazz);
            }
        }
        return (Class[]) filteredList.toArray(new Class[filteredList.size()]);
    }
}
