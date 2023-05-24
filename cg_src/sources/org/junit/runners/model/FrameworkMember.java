package org.junit.runners.model;

import java.lang.reflect.Modifier;
import java.util.List;
import org.junit.runners.model.FrameworkMember;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/model/FrameworkMember.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/model/FrameworkMember.class */
public abstract class FrameworkMember<T extends FrameworkMember<T>> implements Annotatable {
    abstract boolean isShadowedBy(T t);

    abstract boolean isBridgeMethod();

    protected abstract int getModifiers();

    public abstract String getName();

    public abstract Class<?> getType();

    public abstract Class<?> getDeclaringClass();

    /* JADX INFO: Access modifiers changed from: package-private */
    public T handlePossibleBridgeMethod(List<T> members) {
        for (int i = members.size() - 1; i >= 0; i--) {
            T otherMember = members.get(i);
            if (isShadowedBy(otherMember)) {
                if (otherMember.isBridgeMethod()) {
                    members.remove(i);
                    return otherMember;
                } else {
                    return null;
                }
            }
        }
        return this;
    }

    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    public boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }
}
