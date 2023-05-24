package org.mockito.internal.util.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.bytebuddy.ClassFileVersion;
import org.mockito.plugins.MemberAccessor;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/util/reflection/ModuleMemberAccessor.class */
public class ModuleMemberAccessor implements MemberAccessor {
    private final MemberAccessor delegate;

    public ModuleMemberAccessor() {
        if (ClassFileVersion.ofThisVm().isAtLeast(ClassFileVersion.JAVA_V9)) {
            this.delegate = new InstrumentationMemberAccessor();
        } else {
            this.delegate = new ReflectionMemberAccessor();
        }
    }

    @Override // org.mockito.plugins.MemberAccessor
    public Object newInstance(Constructor<?> constructor, Object... arguments) throws InstantiationException, InvocationTargetException, IllegalAccessException {
        return this.delegate.newInstance(constructor, arguments);
    }

    @Override // org.mockito.plugins.MemberAccessor
    public Object newInstance(Constructor<?> constructor, MemberAccessor.OnConstruction onConstruction, Object... arguments) throws InstantiationException, InvocationTargetException, IllegalAccessException {
        return this.delegate.newInstance(constructor, onConstruction, arguments);
    }

    @Override // org.mockito.plugins.MemberAccessor
    public Object invoke(Method method, Object target, Object... arguments) throws InvocationTargetException, IllegalAccessException {
        return this.delegate.invoke(method, target, arguments);
    }

    @Override // org.mockito.plugins.MemberAccessor
    public Object get(Field field, Object target) throws IllegalAccessException {
        return this.delegate.get(field, target);
    }

    @Override // org.mockito.plugins.MemberAccessor
    public void set(Field field, Object target, Object value) throws IllegalAccessException {
        this.delegate.set(field, target, value);
    }
}
