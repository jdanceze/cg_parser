package org.powermock.core.testlisteners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import org.powermock.core.spi.support.AbstractPowerMockTestListenerBase;
import org.powermock.core.spi.testresult.TestMethodResult;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.internal.TypeUtils;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/testlisteners/FieldDefaulter.class */
public class FieldDefaulter extends AbstractPowerMockTestListenerBase {
    @Override // org.powermock.core.spi.support.AbstractPowerMockTestListenerBase, org.powermock.core.spi.PowerMockTestListener
    public void afterTestMethod(Object testInstance, Method method, Object[] arguments, TestMethodResult testResult) throws Exception {
        Set<Field> allFields = Whitebox.getAllInstanceFields(testInstance);
        for (Field field : allFields) {
            field.set(testInstance, TypeUtils.getDefaultValue(field.getType()));
        }
    }
}
