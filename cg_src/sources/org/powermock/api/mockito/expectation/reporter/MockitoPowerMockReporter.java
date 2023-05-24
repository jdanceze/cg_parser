package org.powermock.api.mockito.expectation.reporter;

import org.powermock.api.mockito.ClassNotPreparedException;
import org.powermock.core.reporter.PowerMockReporter;
import org.powermock.utils.StringJoiner;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/expectation/reporter/MockitoPowerMockReporter.class */
public class MockitoPowerMockReporter implements PowerMockReporter {
    @Override // org.powermock.core.reporter.PowerMockReporter
    public <T> void classNotPrepared(Class<T> type) {
        throw new ClassNotPreparedException(StringJoiner.join(String.format("The class %s not prepared for test.", type.getName()), "To prepare this class, add class to the '@PrepareForTest' annotation.", "In case if you don't use this annotation, add the annotation on class or  method level. "));
    }
}
