package net.bytebuddy.description.enumeration;

import java.util.ArrayList;
import java.util.List;
import net.bytebuddy.build.CachedReturnPlugin;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/enumeration/EnumerationDescription.class */
public interface EnumerationDescription extends NamedElement {
    String getValue();

    TypeDescription getEnumerationType();

    <T extends Enum<T>> T load(Class<T> cls);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/enumeration/EnumerationDescription$AbstractBase.class */
    public static abstract class AbstractBase implements EnumerationDescription {
        private transient /* synthetic */ int hashCode_uOZcVtzI;

        @Override // net.bytebuddy.description.NamedElement
        public String getActualName() {
            return getValue();
        }

        @CachedReturnPlugin.Enhance
        public int hashCode() {
            int hashCode = this.hashCode_uOZcVtzI != 0 ? 0 : getValue().hashCode() + (31 * getEnumerationType().hashCode());
            if (hashCode == 0) {
                hashCode = this.hashCode_uOZcVtzI;
            } else {
                this.hashCode_uOZcVtzI = hashCode;
            }
            return hashCode;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof EnumerationDescription)) {
                return false;
            }
            EnumerationDescription enumerationDescription = (EnumerationDescription) other;
            return getEnumerationType().equals(enumerationDescription.getEnumerationType()) && getValue().equals(enumerationDescription.getValue());
        }

        public String toString() {
            return getValue();
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/enumeration/EnumerationDescription$ForLoadedEnumeration.class */
    public static class ForLoadedEnumeration extends AbstractBase {
        private final Enum<?> value;

        public ForLoadedEnumeration(Enum<?> value) {
            this.value = value;
        }

        public static List<EnumerationDescription> asList(Enum<?>[] enumerations) {
            List<EnumerationDescription> result = new ArrayList<>(enumerations.length);
            for (Enum<?> enumeration : enumerations) {
                result.add(new ForLoadedEnumeration(enumeration));
            }
            return result;
        }

        @Override // net.bytebuddy.description.enumeration.EnumerationDescription
        public String getValue() {
            return this.value.name();
        }

        @Override // net.bytebuddy.description.enumeration.EnumerationDescription
        public TypeDescription getEnumerationType() {
            return TypeDescription.ForLoadedType.of(this.value.getDeclaringClass());
        }

        @Override // net.bytebuddy.description.enumeration.EnumerationDescription
        public <T extends Enum<T>> T load(Class<T> type) {
            return this.value.getDeclaringClass() == type ? (T) this.value : (T) Enum.valueOf(type, this.value.name());
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/description/enumeration/EnumerationDescription$Latent.class */
    public static class Latent extends AbstractBase {
        private final TypeDescription enumerationType;
        private final String value;

        public Latent(TypeDescription enumerationType, String value) {
            this.enumerationType = enumerationType;
            this.value = value;
        }

        @Override // net.bytebuddy.description.enumeration.EnumerationDescription
        public String getValue() {
            return this.value;
        }

        @Override // net.bytebuddy.description.enumeration.EnumerationDescription
        public TypeDescription getEnumerationType() {
            return this.enumerationType;
        }

        @Override // net.bytebuddy.description.enumeration.EnumerationDescription
        public <T extends Enum<T>> T load(Class<T> type) {
            if (!this.enumerationType.represents(type)) {
                throw new IllegalArgumentException(type + " does not represent " + this.enumerationType);
            }
            return (T) Enum.valueOf(type, this.value);
        }
    }
}
