package org.powermock.api.mockito.internal.mockcreation;

import java.lang.reflect.Method;
import org.mockito.MockSettings;
/* loaded from: gencallgraphv3.jar:powermock-api-mockito2-2.0.9.jar:org/powermock/api/mockito/internal/mockcreation/MockCreator.class */
public interface MockCreator {
    <T> T createMock(Class<T> cls, boolean z, boolean z2, Object obj, MockSettings mockSettings, Method... methodArr);
}
