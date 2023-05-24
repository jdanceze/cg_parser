package org.mockito.internal.configuration.injection;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.mockito.internal.util.Checks;
import org.mockito.internal.util.collections.Sets;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/MockInjection.class */
public class MockInjection {
    public static OngoingMockInjection onField(Field field, Object ofInstance) {
        return new OngoingMockInjection(field, ofInstance);
    }

    public static OngoingMockInjection onFields(Set<Field> fields, Object ofInstance) {
        return new OngoingMockInjection(fields, ofInstance);
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/MockInjection$OngoingMockInjection.class */
    public static class OngoingMockInjection {
        private final Set<Field> fields;
        private final Set<Object> mocks;
        private final Object fieldOwner;
        private final MockInjectionStrategy injectionStrategies;
        private final MockInjectionStrategy postInjectionStrategies;

        private OngoingMockInjection(Field field, Object fieldOwner) {
            this(Collections.singleton(field), fieldOwner);
        }

        private OngoingMockInjection(Set<Field> fields, Object fieldOwner) {
            this.fields = new HashSet();
            this.mocks = Sets.newMockSafeHashSet(new Object[0]);
            this.injectionStrategies = MockInjectionStrategy.nop();
            this.postInjectionStrategies = MockInjectionStrategy.nop();
            this.fieldOwner = Checks.checkNotNull(fieldOwner, "fieldOwner");
            this.fields.addAll((Collection) Checks.checkItemsNotNull(fields, "fields"));
        }

        public OngoingMockInjection withMocks(Set<Object> mocks) {
            this.mocks.addAll((Collection) Checks.checkNotNull(mocks, "mocks"));
            return this;
        }

        public OngoingMockInjection tryConstructorInjection() {
            this.injectionStrategies.thenTry(new ConstructorInjection());
            return this;
        }

        public OngoingMockInjection tryPropertyOrFieldInjection() {
            this.injectionStrategies.thenTry(new PropertyAndSetterInjection());
            return this;
        }

        public OngoingMockInjection handleSpyAnnotation() {
            this.postInjectionStrategies.thenTry(new SpyOnInjectedFieldsHandler());
            return this;
        }

        public void apply() {
            for (Field field : this.fields) {
                this.injectionStrategies.process(field, this.fieldOwner, this.mocks);
                this.postInjectionStrategies.process(field, this.fieldOwner, this.mocks);
            }
        }
    }
}
