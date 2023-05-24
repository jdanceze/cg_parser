package org.jf.dexlib2.writer.builder;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseAnnotationEncodedValue;
import org.jf.dexlib2.base.value.BaseArrayEncodedValue;
import org.jf.dexlib2.base.value.BaseBooleanEncodedValue;
import org.jf.dexlib2.base.value.BaseEnumEncodedValue;
import org.jf.dexlib2.base.value.BaseFieldEncodedValue;
import org.jf.dexlib2.base.value.BaseMethodEncodedValue;
import org.jf.dexlib2.base.value.BaseMethodHandleEncodedValue;
import org.jf.dexlib2.base.value.BaseMethodTypeEncodedValue;
import org.jf.dexlib2.base.value.BaseNullEncodedValue;
import org.jf.dexlib2.base.value.BaseStringEncodedValue;
import org.jf.dexlib2.base.value.BaseTypeEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableByteEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableCharEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableDoubleEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableFloatEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableIntEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableLongEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableShortEncodedValue;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues.class */
public abstract class BuilderEncodedValues {

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderEncodedValue.class */
    public interface BuilderEncodedValue extends EncodedValue {
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderAnnotationEncodedValue.class */
    public static class BuilderAnnotationEncodedValue extends BaseAnnotationEncodedValue implements BuilderEncodedValue {
        @Nonnull
        final BuilderTypeReference typeReference;
        @Nonnull
        final Set<? extends BuilderAnnotationElement> elements;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderAnnotationEncodedValue(@Nonnull BuilderTypeReference typeReference, @Nonnull Set<? extends BuilderAnnotationElement> elements) {
            this.typeReference = typeReference;
            this.elements = elements;
        }

        @Override // org.jf.dexlib2.iface.value.AnnotationEncodedValue, org.jf.dexlib2.iface.BasicAnnotation
        @Nonnull
        public String getType() {
            return this.typeReference.getType();
        }

        @Override // org.jf.dexlib2.iface.value.AnnotationEncodedValue, org.jf.dexlib2.iface.BasicAnnotation
        @Nonnull
        public Set<? extends BuilderAnnotationElement> getElements() {
            return this.elements;
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderArrayEncodedValue.class */
    public static class BuilderArrayEncodedValue extends BaseArrayEncodedValue implements BuilderEncodedValue {
        @Nonnull
        final List<? extends BuilderEncodedValue> elements;
        int offset = 0;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderArrayEncodedValue(@Nonnull List<? extends BuilderEncodedValue> elements) {
            this.elements = elements;
        }

        @Override // org.jf.dexlib2.iface.value.ArrayEncodedValue
        @Nonnull
        public List<? extends EncodedValue> getValue() {
            return this.elements;
        }
    }

    @Nonnull
    public static BuilderEncodedValue defaultValueForType(String type) {
        switch (type.charAt(0)) {
            case 'B':
                return new BuilderByteEncodedValue((byte) 0);
            case 'C':
                return new BuilderCharEncodedValue((char) 0);
            case 'D':
                return new BuilderDoubleEncodedValue(Const.default_value_double);
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new ExceptionWithContext("Unrecognized type: %s", type);
            case 'F':
                return new BuilderFloatEncodedValue(0.0f);
            case 'I':
                return new BuilderIntEncodedValue(0);
            case 'J':
                return new BuilderLongEncodedValue(0L);
            case 'L':
            case '[':
                return BuilderNullEncodedValue.INSTANCE;
            case 'S':
                return new BuilderShortEncodedValue((short) 0);
            case 'Z':
                return BuilderBooleanEncodedValue.FALSE_VALUE;
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderBooleanEncodedValue.class */
    public static class BuilderBooleanEncodedValue extends BaseBooleanEncodedValue implements BuilderEncodedValue {
        public static final BuilderBooleanEncodedValue TRUE_VALUE = new BuilderBooleanEncodedValue(true);
        public static final BuilderBooleanEncodedValue FALSE_VALUE = new BuilderBooleanEncodedValue(false);
        private final boolean value;

        private BuilderBooleanEncodedValue(boolean value) {
            this.value = value;
        }

        @Override // org.jf.dexlib2.iface.value.BooleanEncodedValue
        public boolean getValue() {
            return this.value;
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderByteEncodedValue.class */
    public static class BuilderByteEncodedValue extends ImmutableByteEncodedValue implements BuilderEncodedValue {
        public BuilderByteEncodedValue(byte value) {
            super(value);
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderCharEncodedValue.class */
    public static class BuilderCharEncodedValue extends ImmutableCharEncodedValue implements BuilderEncodedValue {
        public BuilderCharEncodedValue(char value) {
            super(value);
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderDoubleEncodedValue.class */
    public static class BuilderDoubleEncodedValue extends ImmutableDoubleEncodedValue implements BuilderEncodedValue {
        public BuilderDoubleEncodedValue(double value) {
            super(value);
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderEnumEncodedValue.class */
    public static class BuilderEnumEncodedValue extends BaseEnumEncodedValue implements BuilderEncodedValue {
        @Nonnull
        final BuilderFieldReference enumReference;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderEnumEncodedValue(@Nonnull BuilderFieldReference enumReference) {
            this.enumReference = enumReference;
        }

        @Override // org.jf.dexlib2.iface.value.EnumEncodedValue
        @Nonnull
        public BuilderFieldReference getValue() {
            return this.enumReference;
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderFieldEncodedValue.class */
    public static class BuilderFieldEncodedValue extends BaseFieldEncodedValue implements BuilderEncodedValue {
        @Nonnull
        final BuilderFieldReference fieldReference;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderFieldEncodedValue(@Nonnull BuilderFieldReference fieldReference) {
            this.fieldReference = fieldReference;
        }

        @Override // org.jf.dexlib2.iface.value.FieldEncodedValue
        @Nonnull
        public BuilderFieldReference getValue() {
            return this.fieldReference;
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderFloatEncodedValue.class */
    public static class BuilderFloatEncodedValue extends ImmutableFloatEncodedValue implements BuilderEncodedValue {
        public BuilderFloatEncodedValue(float value) {
            super(value);
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderIntEncodedValue.class */
    public static class BuilderIntEncodedValue extends ImmutableIntEncodedValue implements BuilderEncodedValue {
        public BuilderIntEncodedValue(int value) {
            super(value);
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderLongEncodedValue.class */
    public static class BuilderLongEncodedValue extends ImmutableLongEncodedValue implements BuilderEncodedValue {
        public BuilderLongEncodedValue(long value) {
            super(value);
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderMethodEncodedValue.class */
    public static class BuilderMethodEncodedValue extends BaseMethodEncodedValue implements BuilderEncodedValue {
        @Nonnull
        final BuilderMethodReference methodReference;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderMethodEncodedValue(@Nonnull BuilderMethodReference methodReference) {
            this.methodReference = methodReference;
        }

        @Override // org.jf.dexlib2.iface.value.MethodEncodedValue
        public BuilderMethodReference getValue() {
            return this.methodReference;
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderNullEncodedValue.class */
    public static class BuilderNullEncodedValue extends BaseNullEncodedValue implements BuilderEncodedValue {
        public static final BuilderNullEncodedValue INSTANCE = new BuilderNullEncodedValue();

        private BuilderNullEncodedValue() {
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderShortEncodedValue.class */
    public static class BuilderShortEncodedValue extends ImmutableShortEncodedValue implements BuilderEncodedValue {
        public BuilderShortEncodedValue(short value) {
            super(value);
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderStringEncodedValue.class */
    public static class BuilderStringEncodedValue extends BaseStringEncodedValue implements BuilderEncodedValue {
        @Nonnull
        final BuilderStringReference stringReference;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderStringEncodedValue(@Nonnull BuilderStringReference stringReference) {
            this.stringReference = stringReference;
        }

        @Override // org.jf.dexlib2.iface.value.StringEncodedValue
        @Nonnull
        public String getValue() {
            return this.stringReference.getString();
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderTypeEncodedValue.class */
    public static class BuilderTypeEncodedValue extends BaseTypeEncodedValue implements BuilderEncodedValue {
        @Nonnull
        final BuilderTypeReference typeReference;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BuilderTypeEncodedValue(@Nonnull BuilderTypeReference typeReference) {
            this.typeReference = typeReference;
        }

        @Override // org.jf.dexlib2.iface.value.TypeEncodedValue
        @Nonnull
        public String getValue() {
            return this.typeReference.getType();
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderMethodTypeEncodedValue.class */
    public static class BuilderMethodTypeEncodedValue extends BaseMethodTypeEncodedValue implements BuilderEncodedValue {
        @Nonnull
        final BuilderMethodProtoReference methodProtoReference;

        public BuilderMethodTypeEncodedValue(@Nonnull BuilderMethodProtoReference methodProtoReference) {
            this.methodProtoReference = methodProtoReference;
        }

        @Override // org.jf.dexlib2.iface.value.MethodTypeEncodedValue
        @Nonnull
        public BuilderMethodProtoReference getValue() {
            return this.methodProtoReference;
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/builder/BuilderEncodedValues$BuilderMethodHandleEncodedValue.class */
    public static class BuilderMethodHandleEncodedValue extends BaseMethodHandleEncodedValue implements BuilderEncodedValue {
        @Nonnull
        final BuilderMethodHandleReference methodHandleReference;

        public BuilderMethodHandleEncodedValue(@Nonnull BuilderMethodHandleReference methodHandleReference) {
            this.methodHandleReference = methodHandleReference;
        }

        @Override // org.jf.dexlib2.iface.value.MethodHandleEncodedValue
        @Nonnull
        public BuilderMethodHandleReference getValue() {
            return this.methodHandleReference;
        }
    }
}
