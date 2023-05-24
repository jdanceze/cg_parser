package org.jf.dexlib2.writer.pool;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.reference.CallSiteReference;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodHandleReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.iface.value.AnnotationEncodedValue;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.BooleanEncodedValue;
import org.jf.dexlib2.iface.value.ByteEncodedValue;
import org.jf.dexlib2.iface.value.CharEncodedValue;
import org.jf.dexlib2.iface.value.DoubleEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.EnumEncodedValue;
import org.jf.dexlib2.iface.value.FieldEncodedValue;
import org.jf.dexlib2.iface.value.FloatEncodedValue;
import org.jf.dexlib2.iface.value.IntEncodedValue;
import org.jf.dexlib2.iface.value.LongEncodedValue;
import org.jf.dexlib2.iface.value.MethodEncodedValue;
import org.jf.dexlib2.iface.value.MethodHandleEncodedValue;
import org.jf.dexlib2.iface.value.MethodTypeEncodedValue;
import org.jf.dexlib2.iface.value.ShortEncodedValue;
import org.jf.dexlib2.iface.value.StringEncodedValue;
import org.jf.dexlib2.iface.value.TypeEncodedValue;
import org.jf.dexlib2.writer.DexWriter;
import org.jf.dexlib2.writer.io.DexDataStore;
import org.jf.dexlib2.writer.io.FileDataStore;
import org.jf.dexlib2.writer.pool.TypeListPool;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/DexPool.class */
public class DexPool extends DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, CallSiteReference, MethodHandleReference, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, ArrayEncodedValue, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, CallSitePool, MethodHandlePool, TypeListPool, AnnotationPool, AnnotationSetPool, EncodedArrayPool> {
    private final BasePool<?, ?>[] sections;

    @Override // org.jf.dexlib2.writer.DexWriter
    protected /* bridge */ /* synthetic */ void writeEncodedValue(@Nonnull DexWriter.InternalEncodedValueWriter internalEncodedValueWriter, @Nonnull EncodedValue encodedValue) throws IOException {
        writeEncodedValue2((DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, CallSiteReference, MethodHandleReference, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, ArrayEncodedValue, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, CallSitePool, MethodHandlePool, TypeListPool, AnnotationPool, AnnotationSetPool, EncodedArrayPool>.InternalEncodedValueWriter) internalEncodedValueWriter, encodedValue);
    }

    public DexPool(Opcodes opcodes) {
        super(opcodes);
        this.sections = new BasePool[]{(BasePool) this.stringSection, (BasePool) this.typeSection, (BasePool) this.protoSection, (BasePool) this.fieldSection, (BasePool) this.methodSection, (BasePool) this.classSection, (BasePool) this.callSiteSection, (BasePool) this.methodHandleSection, (BasePool) this.typeListSection, (BasePool) this.annotationSection, (BasePool) this.annotationSetSection, (BasePool) this.encodedArraySection};
    }

    @Override // org.jf.dexlib2.writer.DexWriter
    @Nonnull
    protected DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, CallSiteReference, MethodHandleReference, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, ArrayEncodedValue, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, CallSitePool, MethodHandlePool, TypeListPool, AnnotationPool, AnnotationSetPool, EncodedArrayPool>.SectionProvider getSectionProvider() {
        return new DexPoolSectionProvider();
    }

    public static void writeTo(@Nonnull DexDataStore dataStore, @Nonnull DexFile input) throws IOException {
        DexPool dexPool = new DexPool(input.getOpcodes());
        for (ClassDef classDef : input.getClasses()) {
            dexPool.internClass(classDef);
        }
        dexPool.writeTo(dataStore);
    }

    public static void writeTo(@Nonnull String path, @Nonnull DexFile input) throws IOException {
        DexPool dexPool = new DexPool(input.getOpcodes());
        for (ClassDef classDef : input.getClasses()) {
            dexPool.internClass(classDef);
        }
        dexPool.writeTo(new FileDataStore(new File(path)));
    }

    public void internClass(ClassDef classDef) {
        ((ClassPool) this.classSection).intern(classDef);
    }

    public void mark() {
        Markable[] markableArr;
        for (Markable section : this.sections) {
            section.mark();
        }
    }

    public void reset() {
        Markable[] markableArr;
        for (Markable section : this.sections) {
            section.reset();
        }
    }

    /* renamed from: writeEncodedValue  reason: avoid collision after fix types in other method */
    protected void writeEncodedValue2(@Nonnull DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, CallSiteReference, MethodHandleReference, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, ArrayEncodedValue, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, CallSitePool, MethodHandlePool, TypeListPool, AnnotationPool, AnnotationSetPool, EncodedArrayPool>.InternalEncodedValueWriter writer, @Nonnull EncodedValue encodedValue) throws IOException {
        switch (encodedValue.getValueType()) {
            case 0:
                writer.writeByte(((ByteEncodedValue) encodedValue).getValue());
                return;
            case 1:
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 18:
            case 19:
            case 20:
            default:
                throw new ExceptionWithContext("Unrecognized value type: %d", Integer.valueOf(encodedValue.getValueType()));
            case 2:
                writer.writeShort(((ShortEncodedValue) encodedValue).getValue());
                return;
            case 3:
                writer.writeChar(((CharEncodedValue) encodedValue).getValue());
                return;
            case 4:
                writer.writeInt(((IntEncodedValue) encodedValue).getValue());
                return;
            case 6:
                writer.writeLong(((LongEncodedValue) encodedValue).getValue());
                return;
            case 16:
                writer.writeFloat(((FloatEncodedValue) encodedValue).getValue());
                return;
            case 17:
                writer.writeDouble(((DoubleEncodedValue) encodedValue).getValue());
                return;
            case 21:
                writer.writeMethodType(((MethodTypeEncodedValue) encodedValue).getValue());
                return;
            case 22:
                writer.writeMethodHandle(((MethodHandleEncodedValue) encodedValue).getValue());
                return;
            case 23:
                writer.writeString(((StringEncodedValue) encodedValue).getValue());
                return;
            case 24:
                writer.writeType(((TypeEncodedValue) encodedValue).getValue());
                return;
            case 25:
                writer.writeField(((FieldEncodedValue) encodedValue).getValue());
                return;
            case 26:
                writer.writeMethod(((MethodEncodedValue) encodedValue).getValue());
                return;
            case 27:
                writer.writeEnum(((EnumEncodedValue) encodedValue).getValue());
                return;
            case 28:
                ArrayEncodedValue arrayEncodedValue = (ArrayEncodedValue) encodedValue;
                writer.writeArray(arrayEncodedValue.getValue());
                return;
            case 29:
                AnnotationEncodedValue annotationEncodedValue = (AnnotationEncodedValue) encodedValue;
                writer.writeAnnotation(annotationEncodedValue.getType(), annotationEncodedValue.getElements());
                return;
            case 30:
                writer.writeNull();
                return;
            case 31:
                writer.writeBoolean(((BooleanEncodedValue) encodedValue).getValue());
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void internEncodedValue(@Nonnull EncodedValue encodedValue) {
        switch (encodedValue.getValueType()) {
            case 21:
                ((ProtoPool) this.protoSection).intern(((MethodTypeEncodedValue) encodedValue).getValue());
                return;
            case 22:
                ((MethodHandlePool) this.methodHandleSection).intern(((MethodHandleEncodedValue) encodedValue).getValue());
                return;
            case 23:
                ((StringPool) this.stringSection).intern(((StringEncodedValue) encodedValue).getValue());
                return;
            case 24:
                ((TypePool) this.typeSection).intern(((TypeEncodedValue) encodedValue).getValue());
                return;
            case 25:
                ((FieldPool) this.fieldSection).intern(((FieldEncodedValue) encodedValue).getValue());
                return;
            case 26:
                ((MethodPool) this.methodSection).intern(((MethodEncodedValue) encodedValue).getValue());
                return;
            case 27:
                ((FieldPool) this.fieldSection).intern(((EnumEncodedValue) encodedValue).getValue());
                return;
            case 28:
                for (EncodedValue element : ((ArrayEncodedValue) encodedValue).getValue()) {
                    internEncodedValue(element);
                }
                return;
            case 29:
                AnnotationEncodedValue annotationEncodedValue = (AnnotationEncodedValue) encodedValue;
                ((TypePool) this.typeSection).intern(annotationEncodedValue.getType());
                for (AnnotationElement element2 : annotationEncodedValue.getElements()) {
                    ((StringPool) this.stringSection).intern(element2.getName());
                    internEncodedValue(element2.getValue());
                }
                return;
            default:
                return;
        }
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/pool/DexPool$DexPoolSectionProvider.class */
    protected class DexPoolSectionProvider extends DexWriter<CharSequence, StringReference, CharSequence, TypeReference, MethodProtoReference, FieldReference, MethodReference, PoolClassDef, CallSiteReference, MethodHandleReference, Annotation, Set<? extends Annotation>, TypeListPool.Key<? extends Collection<? extends CharSequence>>, Field, PoolMethod, ArrayEncodedValue, EncodedValue, AnnotationElement, StringPool, TypePool, ProtoPool, FieldPool, MethodPool, ClassPool, CallSitePool, MethodHandlePool, TypeListPool, AnnotationPool, AnnotationSetPool, EncodedArrayPool>.SectionProvider {
        protected DexPoolSectionProvider() {
            super();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public StringPool getStringSection() {
            return new StringPool(DexPool.this);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public TypePool getTypeSection() {
            return new TypePool(DexPool.this);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public ProtoPool getProtoSection() {
            return new ProtoPool(DexPool.this);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public FieldPool getFieldSection() {
            return new FieldPool(DexPool.this);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public MethodPool getMethodSection() {
            return new MethodPool(DexPool.this);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public ClassPool getClassSection() {
            return new ClassPool(DexPool.this);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public CallSitePool getCallSiteSection() {
            return new CallSitePool(DexPool.this);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public MethodHandlePool getMethodHandleSection() {
            return new MethodHandlePool(DexPool.this);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public TypeListPool getTypeListSection() {
            return new TypeListPool(DexPool.this);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public AnnotationPool getAnnotationSection() {
            return new AnnotationPool(DexPool.this);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public AnnotationSetPool getAnnotationSetSection() {
            return new AnnotationSetPool(DexPool.this);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.jf.dexlib2.writer.DexWriter.SectionProvider
        @Nonnull
        public EncodedArrayPool getEncodedArraySection() {
            return new EncodedArrayPool(DexPool.this);
        }
    }
}
