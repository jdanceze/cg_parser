package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/FieldReader.class */
public class FieldReader {
    final Object target;
    final Field field;
    final MemberAccessor accessor = Plugins.getMemberAccessor();

    public FieldReader(Object target, Field field) {
        this.target = target;
        this.field = field;
    }

    public boolean isNull() {
        return read() == null;
    }

    public Object read() {
        try {
            return this.accessor.get(this.field, this.target);
        } catch (Exception e) {
            throw new MockitoException("Cannot read state from field: " + this.field + ", on instance: " + this.target);
        }
    }
}
