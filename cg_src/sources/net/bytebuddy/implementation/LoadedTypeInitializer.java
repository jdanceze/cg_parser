package net.bytebuddy.implementation;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.utility.JavaModule;
import net.bytebuddy.utility.privilege.SetAccessibleAction;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/LoadedTypeInitializer.class */
public interface LoadedTypeInitializer {
    void onLoad(Class<?> cls);

    boolean isAlive();

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/LoadedTypeInitializer$NoOp.class */
    public enum NoOp implements LoadedTypeInitializer {
        INSTANCE;

        @Override // net.bytebuddy.implementation.LoadedTypeInitializer
        public void onLoad(Class<?> type) {
        }

        @Override // net.bytebuddy.implementation.LoadedTypeInitializer
        public boolean isAlive() {
            return false;
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/LoadedTypeInitializer$ForStaticField.class */
    public static class ForStaticField implements LoadedTypeInitializer, Serializable {
        private static final long serialVersionUID = 1;
        private static final Object STATIC_FIELD = null;
        private final String fieldName;
        private final Object value;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.fieldName.equals(((ForStaticField) obj).fieldName) && this.value.equals(((ForStaticField) obj).value);
        }

        public int hashCode() {
            return (((17 * 31) + this.fieldName.hashCode()) * 31) + this.value.hashCode();
        }

        public ForStaticField(String fieldName, Object value) {
            this.fieldName = fieldName;
            this.value = value;
        }

        @Override // net.bytebuddy.implementation.LoadedTypeInitializer
        public void onLoad(Class<?> type) {
            try {
                Field field = type.getDeclaredField(this.fieldName);
                if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || (JavaModule.isSupported() && !JavaModule.ofType(type).isExported(new TypeDescription.ForLoadedType(type).getPackage(), JavaModule.ofType(ForStaticField.class)))) {
                    AccessController.doPrivileged(new SetAccessibleAction(field));
                }
                field.set(STATIC_FIELD, this.value);
            } catch (IllegalAccessException exception) {
                throw new IllegalArgumentException("Cannot access " + this.fieldName + " from " + type, exception);
            } catch (NoSuchFieldException exception2) {
                throw new IllegalStateException("There is no field " + this.fieldName + " defined on " + type, exception2);
            }
        }

        @Override // net.bytebuddy.implementation.LoadedTypeInitializer
        public boolean isAlive() {
            return true;
        }
    }

    @SuppressFBWarnings(value = {"SE_BAD_FIELD"}, justification = "Serialization is considered opt-in for a rare use case")
    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/LoadedTypeInitializer$Compound.class */
    public static class Compound implements LoadedTypeInitializer, Serializable {
        private static final long serialVersionUID = 1;
        private final List<LoadedTypeInitializer> loadedTypeInitializers;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.loadedTypeInitializers.equals(((Compound) obj).loadedTypeInitializers);
        }

        public int hashCode() {
            return (17 * 31) + this.loadedTypeInitializers.hashCode();
        }

        public Compound(LoadedTypeInitializer... loadedTypeInitializer) {
            this(Arrays.asList(loadedTypeInitializer));
        }

        public Compound(List<? extends LoadedTypeInitializer> loadedTypeInitializers) {
            this.loadedTypeInitializers = new ArrayList();
            for (LoadedTypeInitializer loadedTypeInitializer : loadedTypeInitializers) {
                if (loadedTypeInitializer instanceof Compound) {
                    this.loadedTypeInitializers.addAll(((Compound) loadedTypeInitializer).loadedTypeInitializers);
                } else if (!(loadedTypeInitializer instanceof NoOp)) {
                    this.loadedTypeInitializers.add(loadedTypeInitializer);
                }
            }
        }

        @Override // net.bytebuddy.implementation.LoadedTypeInitializer
        public void onLoad(Class<?> type) {
            for (LoadedTypeInitializer loadedTypeInitializer : this.loadedTypeInitializers) {
                loadedTypeInitializer.onLoad(type);
            }
        }

        @Override // net.bytebuddy.implementation.LoadedTypeInitializer
        public boolean isAlive() {
            for (LoadedTypeInitializer loadedTypeInitializer : this.loadedTypeInitializers) {
                if (loadedTypeInitializer.isAlive()) {
                    return true;
                }
            }
            return false;
        }
    }
}
