package org.powermock.modules.junit4;

import java.lang.annotation.Annotation;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.powermock.modules.junit4.common.internal.PowerMockJUnitRunnerDelegate;
import org.powermock.modules.junit4.common.internal.impl.AbstractCommonPowerMockRunner;
import org.powermock.modules.junit4.common.internal.impl.JUnitVersion;
import org.powermock.modules.junit4.internal.impl.DelegatingPowerMockRunner;
import org.powermock.modules.junit4.internal.impl.PowerMockJUnit44RunnerDelegateImpl;
import org.powermock.modules.junit4.internal.impl.PowerMockJUnit47RunnerDelegateImpl;
import org.powermock.modules.junit4.internal.impl.PowerMockJUnit49RunnerDelegateImpl;
import org.powermock.reflect.Whitebox;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-2.0.9.jar:org/powermock/modules/junit4/PowerMockRunner.class */
public class PowerMockRunner extends AbstractCommonPowerMockRunner {
    public PowerMockRunner(Class<?> klass) throws Exception {
        super(klass, getRunnerDelegateImplClass(klass));
    }

    private static Class<? extends PowerMockJUnitRunnerDelegate> getRunnerDelegateImplClass(Class<?> klass) {
        if (klass.isAnnotationPresent(PowerMockRunnerDelegate.class) || Boolean.getBoolean("powermock.implicitDelegateAnnotation")) {
            return DelegatingPowerMockRunner.class;
        }
        Class<? extends PowerMockJUnitRunnerDelegate> concreteClass = PowerMockJUnit44RunnerDelegateImpl.class;
        if (JUnitVersion.isGreaterThanOrEqualTo("4.9")) {
            concreteClass = PowerMockJUnit49RunnerDelegateImpl.class;
        } else if (JUnitVersion.isGreaterThanOrEqualTo("4.7")) {
            concreteClass = PowerMockJUnit47RunnerDelegateImpl.class;
        }
        return concreteClass;
    }

    @Override // org.powermock.modules.junit4.common.internal.impl.AbstractCommonPowerMockRunner, org.junit.runner.Runner
    public void run(RunNotifier notifier) {
        Description description = getDescription();
        try {
            super.run(notifier);
            Whitebox.setInternalState((Object) description, "fAnnotations", (Object[]) new Annotation[0]);
        } catch (Throwable th) {
            Whitebox.setInternalState((Object) description, "fAnnotations", (Object[]) new Annotation[0]);
            throw th;
        }
    }
}
