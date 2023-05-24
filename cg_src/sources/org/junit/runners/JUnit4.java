package org.junit.runners;

import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/JUnit4.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/JUnit4.class */
public final class JUnit4 extends BlockJUnit4ClassRunner {
    public JUnit4(Class<?> klass) throws InitializationError {
        super(new TestClass(klass));
    }
}
