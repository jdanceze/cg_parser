package org.mockito.internal.creation.bytebuddy;

import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/creation/bytebuddy/ClassCreatingMockMaker.class */
interface ClassCreatingMockMaker extends MockMaker {
    <T> Class<? extends T> createMockType(MockCreationSettings<T> mockCreationSettings);
}
