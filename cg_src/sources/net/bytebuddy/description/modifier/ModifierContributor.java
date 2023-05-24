package net.bytebuddy.description.modifier;

import java.util.Arrays;
import java.util.Collection;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/modifier/ModifierContributor.class */
public interface ModifierContributor {
    public static final int EMPTY_MASK = 0;

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/modifier/ModifierContributor$ForField.class */
    public interface ForField extends ModifierContributor {
        public static final int MASK = 151775;
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/modifier/ModifierContributor$ForMethod.class */
    public interface ForMethod extends ModifierContributor {
        public static final int MASK = 7679;
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/modifier/ModifierContributor$ForParameter.class */
    public interface ForParameter extends ModifierContributor {
        public static final int MASK = 36880;
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/modifier/ModifierContributor$ForType.class */
    public interface ForType extends ModifierContributor {
        public static final int MASK = 161311;
    }

    int getMask();

    int getRange();

    boolean isDefault();

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/modifier/ModifierContributor$Resolver.class */
    public static class Resolver<T extends ModifierContributor> {
        private final Collection<? extends T> modifierContributors;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.modifierContributors.equals(((Resolver) obj).modifierContributors);
        }

        public int hashCode() {
            return (17 * 31) + this.modifierContributors.hashCode();
        }

        protected Resolver(Collection<? extends T> modifierContributors) {
            this.modifierContributors = modifierContributors;
        }

        public static Resolver<ForType> of(ForType... modifierContributor) {
            return of(Arrays.asList(modifierContributor));
        }

        public static Resolver<ForField> of(ForField... modifierContributor) {
            return of(Arrays.asList(modifierContributor));
        }

        public static Resolver<ForMethod> of(ForMethod... modifierContributor) {
            return of(Arrays.asList(modifierContributor));
        }

        public static Resolver<ForParameter> of(ForParameter... modifierContributor) {
            return of(Arrays.asList(modifierContributor));
        }

        public static <S extends ModifierContributor> Resolver<S> of(Collection<? extends S> modifierContributors) {
            return new Resolver<>(modifierContributors);
        }

        public int resolve() {
            return resolve(0);
        }

        public int resolve(int modifiers) {
            for (T modifierContributor : this.modifierContributors) {
                modifiers = (modifiers & (modifierContributor.getRange() ^ (-1))) | modifierContributor.getMask();
            }
            return modifiers;
        }
    }
}
