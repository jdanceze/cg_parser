package org.jf.dexlib2.writer;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.CharSequence;
import java.lang.Comparable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.Adler32;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.HiddenApiRestriction;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.base.BaseAnnotation;
import org.jf.dexlib2.base.BaseAnnotationElement;
import org.jf.dexlib2.builder.MutableMethodImplementation;
import org.jf.dexlib2.builder.instruction.BuilderInstruction31c;
import org.jf.dexlib2.dexbacked.raw.HeaderItem;
import org.jf.dexlib2.dexbacked.raw.ItemType;
import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.debug.LineNumber;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.VariableRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.ArrayPayload;
import org.jf.dexlib2.iface.instruction.formats.Instruction10t;
import org.jf.dexlib2.iface.instruction.formats.Instruction10x;
import org.jf.dexlib2.iface.instruction.formats.Instruction11n;
import org.jf.dexlib2.iface.instruction.formats.Instruction11x;
import org.jf.dexlib2.iface.instruction.formats.Instruction12x;
import org.jf.dexlib2.iface.instruction.formats.Instruction20bc;
import org.jf.dexlib2.iface.instruction.formats.Instruction20t;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.instruction.formats.Instruction21ih;
import org.jf.dexlib2.iface.instruction.formats.Instruction21lh;
import org.jf.dexlib2.iface.instruction.formats.Instruction21s;
import org.jf.dexlib2.iface.instruction.formats.Instruction21t;
import org.jf.dexlib2.iface.instruction.formats.Instruction22b;
import org.jf.dexlib2.iface.instruction.formats.Instruction22c;
import org.jf.dexlib2.iface.instruction.formats.Instruction22cs;
import org.jf.dexlib2.iface.instruction.formats.Instruction22s;
import org.jf.dexlib2.iface.instruction.formats.Instruction22t;
import org.jf.dexlib2.iface.instruction.formats.Instruction22x;
import org.jf.dexlib2.iface.instruction.formats.Instruction23x;
import org.jf.dexlib2.iface.instruction.formats.Instruction30t;
import org.jf.dexlib2.iface.instruction.formats.Instruction31c;
import org.jf.dexlib2.iface.instruction.formats.Instruction31i;
import org.jf.dexlib2.iface.instruction.formats.Instruction31t;
import org.jf.dexlib2.iface.instruction.formats.Instruction32x;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.instruction.formats.Instruction35mi;
import org.jf.dexlib2.iface.instruction.formats.Instruction35ms;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rmi;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rms;
import org.jf.dexlib2.iface.instruction.formats.Instruction45cc;
import org.jf.dexlib2.iface.instruction.formats.Instruction4rcc;
import org.jf.dexlib2.iface.instruction.formats.Instruction51l;
import org.jf.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import org.jf.dexlib2.iface.reference.CallSiteReference;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodHandleReference;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.util.InstructionUtil;
import org.jf.dexlib2.util.MethodUtil;
import org.jf.dexlib2.writer.AnnotationSection;
import org.jf.dexlib2.writer.AnnotationSetSection;
import org.jf.dexlib2.writer.CallSiteSection;
import org.jf.dexlib2.writer.ClassSection;
import org.jf.dexlib2.writer.EncodedArraySection;
import org.jf.dexlib2.writer.FieldSection;
import org.jf.dexlib2.writer.MethodHandleSection;
import org.jf.dexlib2.writer.MethodSection;
import org.jf.dexlib2.writer.ProtoSection;
import org.jf.dexlib2.writer.StringSection;
import org.jf.dexlib2.writer.TypeListSection;
import org.jf.dexlib2.writer.TypeSection;
import org.jf.dexlib2.writer.io.DeferredOutputStream;
import org.jf.dexlib2.writer.io.DeferredOutputStreamFactory;
import org.jf.dexlib2.writer.io.DexDataStore;
import org.jf.dexlib2.writer.io.MemoryDeferredOutputStream;
import org.jf.dexlib2.writer.util.TryListBuilder;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/DexWriter.class */
public abstract class DexWriter<StringKey extends CharSequence, StringRef extends StringReference, TypeKey extends CharSequence, TypeRef extends TypeReference, ProtoRefKey extends MethodProtoReference, FieldRefKey extends FieldReference, MethodRefKey extends MethodReference, ClassKey extends Comparable<? super ClassKey>, CallSiteKey extends CallSiteReference, MethodHandleKey extends MethodHandleReference, AnnotationKey extends Annotation, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedArrayKey, EncodedValue, AnnotationElement extends AnnotationElement, StringSectionType extends StringSection<StringKey, StringRef>, TypeSectionType extends TypeSection<StringKey, TypeKey, TypeRef>, ProtoSectionType extends ProtoSection<StringKey, TypeKey, ProtoRefKey, TypeListKey>, FieldSectionType extends FieldSection<StringKey, TypeKey, FieldRefKey, FieldKey>, MethodSectionType extends MethodSection<StringKey, TypeKey, ProtoRefKey, MethodRefKey, MethodKey>, ClassSectionType extends ClassSection<StringKey, TypeKey, TypeListKey, ClassKey, FieldKey, MethodKey, AnnotationSetKey, EncodedArrayKey>, CallSiteSectionType extends CallSiteSection<CallSiteKey, EncodedArrayKey>, MethodHandleSectionType extends MethodHandleSection<MethodHandleKey, FieldRefKey, MethodRefKey>, TypeListSectionType extends TypeListSection<TypeKey, TypeListKey>, AnnotationSectionType extends AnnotationSection<StringKey, TypeKey, AnnotationKey, AnnotationElement, EncodedValue>, AnnotationSetSectionType extends AnnotationSetSection<AnnotationKey, AnnotationSetKey>, EncodedArraySectionType extends EncodedArraySection<EncodedArrayKey, EncodedValue>> {
    public static final int NO_INDEX = -1;
    public static final int NO_OFFSET = 0;
    public static final int MAX_POOL_SIZE = 65536;
    protected final Opcodes opcodes;
    public final StringSectionType stringSection;
    public final TypeSectionType typeSection;
    public final ProtoSectionType protoSection;
    public final FieldSectionType fieldSection;
    public final MethodSectionType methodSection;
    public final ClassSectionType classSection;
    public final CallSiteSectionType callSiteSection;
    public final MethodHandleSectionType methodHandleSection;
    public final TypeListSectionType typeListSection;
    public final AnnotationSectionType annotationSection;
    public final AnnotationSetSectionType annotationSetSection;
    public final EncodedArraySectionType encodedArraySection;
    private final IndexSection<?>[] overflowableSections;
    private static Comparator<Map.Entry> toStringKeyComparator = new Comparator<Map.Entry>() { // from class: org.jf.dexlib2.writer.DexWriter.2
        @Override // java.util.Comparator
        public int compare(Map.Entry o1, Map.Entry o2) {
            return o1.getKey().toString().compareTo(o2.getKey().toString());
        }
    };
    protected int stringIndexSectionOffset = 0;
    protected int typeSectionOffset = 0;
    protected int protoSectionOffset = 0;
    protected int fieldSectionOffset = 0;
    protected int methodSectionOffset = 0;
    protected int classIndexSectionOffset = 0;
    protected int callSiteSectionOffset = 0;
    protected int methodHandleSectionOffset = 0;
    protected int stringDataSectionOffset = 0;
    protected int classDataSectionOffset = 0;
    protected int typeListSectionOffset = 0;
    protected int encodedArraySectionOffset = 0;
    protected int annotationSectionOffset = 0;
    protected int annotationSetSectionOffset = 0;
    protected int annotationSetRefSectionOffset = 0;
    protected int annotationDirectorySectionOffset = 0;
    protected int debugSectionOffset = 0;
    protected int codeSectionOffset = 0;
    protected int hiddenApiRestrictionsOffset = 0;
    protected int mapSectionOffset = 0;
    protected boolean hasHiddenApiRestrictions = false;
    protected int numAnnotationSetRefItems = 0;
    protected int numAnnotationDirectoryItems = 0;
    protected int numDebugInfoItems = 0;
    protected int numCodeItemItems = 0;
    protected int numClassDataItems = 0;
    private Comparator<Map.Entry<? extends CallSiteKey, Integer>> callSiteComparator = (Comparator<Map.Entry<? extends CallSiteKey, Integer>>) new Comparator<Map.Entry<? extends CallSiteKey, Integer>>() { // from class: org.jf.dexlib2.writer.DexWriter.1
        @Override // java.util.Comparator
        public /* bridge */ /* synthetic */ int compare(Object obj, Object obj2) {
            return compare((Map.Entry) ((Map.Entry) obj), (Map.Entry) ((Map.Entry) obj2));
        }

        public int compare(Map.Entry<? extends CallSiteKey, Integer> o1, Map.Entry<? extends CallSiteKey, Integer> o2) {
            int offset1 = DexWriter.this.encodedArraySection.getItemOffset(DexWriter.this.callSiteSection.getEncodedCallSite(o1.getKey()));
            int offset2 = DexWriter.this.encodedArraySection.getItemOffset(DexWriter.this.callSiteSection.getEncodedCallSite(o2.getKey()));
            return Ints.compare(offset1, offset2);
        }
    };

    @Nonnull
    protected abstract DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, CallSiteKey, MethodHandleKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedArrayKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, CallSiteSectionType, MethodHandleSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType, EncodedArraySectionType>.SectionProvider getSectionProvider();

    protected abstract void writeEncodedValue(@Nonnull DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, CallSiteKey, MethodHandleKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedArrayKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, CallSiteSectionType, MethodHandleSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType, EncodedArraySectionType>.InternalEncodedValueWriter internalEncodedValueWriter, @Nonnull EncodedValue encodedvalue) throws IOException;

    /* JADX INFO: Access modifiers changed from: protected */
    public DexWriter(Opcodes opcodes) {
        this.opcodes = opcodes;
        DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, CallSiteKey, MethodHandleKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedArrayKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, CallSiteSectionType, MethodHandleSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType, EncodedArraySectionType>.SectionProvider sectionProvider = getSectionProvider();
        this.stringSection = (StringSectionType) sectionProvider.getStringSection();
        this.typeSection = (TypeSectionType) sectionProvider.getTypeSection();
        this.protoSection = (ProtoSectionType) sectionProvider.getProtoSection();
        this.fieldSection = (FieldSectionType) sectionProvider.getFieldSection();
        this.methodSection = (MethodSectionType) sectionProvider.getMethodSection();
        this.classSection = (ClassSectionType) sectionProvider.getClassSection();
        this.callSiteSection = (CallSiteSectionType) sectionProvider.getCallSiteSection();
        this.methodHandleSection = (MethodHandleSectionType) sectionProvider.getMethodHandleSection();
        this.typeListSection = (TypeListSectionType) sectionProvider.getTypeListSection();
        this.annotationSection = (AnnotationSectionType) sectionProvider.getAnnotationSection();
        this.annotationSetSection = (AnnotationSetSectionType) sectionProvider.getAnnotationSetSection();
        this.encodedArraySection = (EncodedArraySectionType) sectionProvider.getEncodedArraySection();
        this.overflowableSections = new IndexSection[]{this.typeSection, this.protoSection, this.fieldSection, this.methodSection, this.callSiteSection, this.methodHandleSection};
    }

    private static <T extends Comparable<? super T>> Comparator<Map.Entry<? extends T, ?>> comparableKeyComparator() {
        return (Comparator<Map.Entry<? extends T, ?>>) new Comparator<Map.Entry<? extends T, ?>>() { // from class: org.jf.dexlib2.writer.DexWriter.3
            /* JADX WARN: Unknown type variable: T in type: java.util.Map$Entry<? extends T, ?> */
            @Override // java.util.Comparator
            public int compare(Map.Entry<? extends T, ?> o1, Map.Entry<? extends T, ?> o2) {
                return ((Comparable) o1.getKey()).compareTo(o2.getKey());
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/DexWriter$InternalEncodedValueWriter.class */
    public class InternalEncodedValueWriter extends EncodedValueWriter<StringKey, TypeKey, FieldRefKey, MethodRefKey, AnnotationElement, ProtoRefKey, MethodHandleKey, EncodedValue> {
        private InternalEncodedValueWriter(@Nonnull DexDataWriter writer) {
            super(writer, DexWriter.this.stringSection, DexWriter.this.typeSection, DexWriter.this.fieldSection, DexWriter.this.methodSection, DexWriter.this.protoSection, DexWriter.this.methodHandleSection, DexWriter.this.annotationSection);
        }

        @Override // org.jf.dexlib2.writer.EncodedValueWriter
        protected void writeEncodedValue(@Nonnull EncodedValue encodedValue) throws IOException {
            DexWriter.this.writeEncodedValue(this, encodedValue);
        }
    }

    private int getDataSectionOffset() {
        return 112 + (this.stringSection.getItemCount() * 4) + (this.typeSection.getItemCount() * 4) + (this.protoSection.getItemCount() * 12) + (this.fieldSection.getItemCount() * 8) + (this.methodSection.getItemCount() * 8) + (this.classSection.getItemCount() * 32) + (this.callSiteSection.getItemCount() * 4) + (this.methodHandleSection.getItemCount() * 8);
    }

    @Nonnull
    public List<String> getMethodReferences() {
        List<String> methodReferences = Lists.newArrayList();
        for (Map.Entry<? extends MethodRefKey, Integer> methodReference : this.methodSection.getItems()) {
            methodReferences.add(DexFormatter.INSTANCE.getMethodDescriptor((MethodReference) methodReference.getKey()));
        }
        return methodReferences;
    }

    @Nonnull
    public List<String> getFieldReferences() {
        List<String> fieldReferences = Lists.newArrayList();
        for (Map.Entry<? extends FieldRefKey, Integer> fieldReference : this.fieldSection.getItems()) {
            fieldReferences.add(DexFormatter.INSTANCE.getFieldDescriptor((FieldReference) fieldReference.getKey()));
        }
        return fieldReferences;
    }

    @Nonnull
    public List<String> getTypeReferences() {
        List<String> classReferences = Lists.newArrayList();
        for (Map.Entry<? extends TypeKey, Integer> typeReference : this.typeSection.getItems()) {
            classReferences.add(((CharSequence) typeReference.getKey()).toString());
        }
        return classReferences;
    }

    public boolean hasOverflowed() {
        return hasOverflowed(65536);
    }

    public boolean hasOverflowed(int maxPoolSize) {
        IndexSection[] indexSectionArr;
        for (IndexSection section : this.overflowableSections) {
            if (section.getItemCount() > maxPoolSize) {
                return true;
            }
        }
        return false;
    }

    public void writeTo(@Nonnull DexDataStore dest) throws IOException {
        writeTo(dest, MemoryDeferredOutputStream.getFactory());
    }

    public void writeTo(@Nonnull DexDataStore dest, @Nonnull DeferredOutputStreamFactory tempFactory) throws IOException {
        try {
            int dataSectionOffset = getDataSectionOffset();
            DexDataWriter headerWriter = outputAt(dest, 0);
            DexDataWriter indexWriter = outputAt(dest, 112);
            DexDataWriter offsetWriter = outputAt(dest, dataSectionOffset);
            writeStrings(indexWriter, offsetWriter);
            writeTypes(indexWriter);
            writeTypeLists(offsetWriter);
            writeProtos(indexWriter);
            writeFields(indexWriter);
            writeMethods(indexWriter);
            DexDataWriter callSiteWriter = outputAt(dest, indexWriter.getPosition() + (this.classSection.getItemCount() * 32) + (this.callSiteSection.getItemCount() * 4));
            try {
                writeMethodHandles(callSiteWriter);
                callSiteWriter.close();
                writeEncodedArrays(offsetWriter);
                callSiteWriter = outputAt(dest, indexWriter.getPosition() + (this.classSection.getItemCount() * 32));
                try {
                    writeCallSites(callSiteWriter);
                    callSiteWriter.close();
                    writeAnnotations(offsetWriter);
                    writeAnnotationSets(offsetWriter);
                    writeAnnotationSetRefs(offsetWriter);
                    writeAnnotationDirectories(offsetWriter);
                    writeDebugAndCodeItems(offsetWriter, tempFactory.makeDeferredOutputStream());
                    writeClasses(dest, indexWriter, offsetWriter);
                    writeMapItem(offsetWriter);
                    writeHeader(headerWriter, dataSectionOffset, offsetWriter.getPosition());
                    headerWriter.close();
                    indexWriter.close();
                    offsetWriter.close();
                    updateSignature(dest);
                    updateChecksum(dest);
                    dest.close();
                } finally {
                }
            } finally {
            }
        } catch (Throwable th) {
            dest.close();
            throw th;
        }
    }

    private void updateSignature(@Nonnull DexDataStore dataStore) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[4096];
            InputStream input = dataStore.readAt(32);
            int read = input.read(buffer);
            while (true) {
                int bytesRead = read;
                if (bytesRead < 0) {
                    break;
                }
                md.update(buffer, 0, bytesRead);
                read = input.read(buffer);
            }
            byte[] signature = md.digest();
            if (signature.length != 20) {
                throw new RuntimeException("unexpected digest write: " + signature.length + " bytes");
            }
            OutputStream output = dataStore.outputAt(12);
            output.write(signature);
            output.close();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void updateChecksum(@Nonnull DexDataStore dataStore) throws IOException {
        Adler32 a32 = new Adler32();
        byte[] buffer = new byte[4096];
        InputStream input = dataStore.readAt(12);
        int read = input.read(buffer);
        while (true) {
            int bytesRead = read;
            if (bytesRead >= 0) {
                a32.update(buffer, 0, bytesRead);
                read = input.read(buffer);
            } else {
                OutputStream output = dataStore.outputAt(8);
                DexDataWriter.writeInt(output, (int) a32.getValue());
                output.close();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static DexDataWriter outputAt(DexDataStore dataStore, int filePosition) throws IOException {
        return new DexDataWriter(dataStore.outputAt(filePosition), filePosition);
    }

    private void writeStrings(@Nonnull DexDataWriter indexWriter, @Nonnull DexDataWriter offsetWriter) throws IOException {
        this.stringIndexSectionOffset = indexWriter.getPosition();
        this.stringDataSectionOffset = offsetWriter.getPosition();
        int index = 0;
        List<Map.Entry<? extends StringKey, Integer>> stringEntries = Lists.newArrayList(this.stringSection.getItems());
        Collections.sort(stringEntries, toStringKeyComparator);
        for (Map.Entry<? extends StringKey, Integer> entry : stringEntries) {
            int i = index;
            index++;
            entry.setValue(Integer.valueOf(i));
            indexWriter.writeInt(offsetWriter.getPosition());
            String stringValue = ((CharSequence) entry.getKey()).toString();
            offsetWriter.writeUleb128(stringValue.length());
            offsetWriter.writeString(stringValue);
            offsetWriter.write(0);
        }
    }

    private void writeTypes(@Nonnull DexDataWriter writer) throws IOException {
        this.typeSectionOffset = writer.getPosition();
        int index = 0;
        List<Map.Entry<? extends TypeKey, Integer>> typeEntries = Lists.newArrayList(this.typeSection.getItems());
        Collections.sort(typeEntries, toStringKeyComparator);
        for (Map.Entry<? extends TypeKey, Integer> entry : typeEntries) {
            int i = index;
            index++;
            entry.setValue(Integer.valueOf(i));
            writer.writeInt(this.stringSection.getItemIndex((CharSequence) this.typeSection.getString((CharSequence) entry.getKey())));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeProtos(@Nonnull DexDataWriter writer) throws IOException {
        this.protoSectionOffset = writer.getPosition();
        int index = 0;
        List<Map.Entry<? extends ProtoRefKey, Integer>> protoEntries = Lists.newArrayList(this.protoSection.getItems());
        Collections.sort(protoEntries, comparableKeyComparator());
        for (Map.Entry<? extends ProtoRefKey, Integer> entry : protoEntries) {
            int i = index;
            index++;
            entry.setValue(Integer.valueOf(i));
            MethodProtoReference methodProtoReference = (MethodProtoReference) entry.getKey();
            writer.writeInt(this.stringSection.getItemIndex((CharSequence) this.protoSection.getShorty(methodProtoReference)));
            writer.writeInt(this.typeSection.getItemIndex((CharSequence) this.protoSection.getReturnType(methodProtoReference)));
            writer.writeInt(this.typeListSection.getNullableItemOffset(this.protoSection.getParameters(methodProtoReference)));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeFields(@Nonnull DexDataWriter writer) throws IOException {
        this.fieldSectionOffset = writer.getPosition();
        int index = 0;
        List<Map.Entry<? extends FieldRefKey, Integer>> fieldEntries = Lists.newArrayList(this.fieldSection.getItems());
        Collections.sort(fieldEntries, comparableKeyComparator());
        for (Map.Entry<? extends FieldRefKey, Integer> entry : fieldEntries) {
            int i = index;
            index++;
            entry.setValue(Integer.valueOf(i));
            FieldReference fieldReference = (FieldReference) entry.getKey();
            writer.writeUshort(this.typeSection.getItemIndex((CharSequence) this.fieldSection.getDefiningClass(fieldReference)));
            writer.writeUshort(this.typeSection.getItemIndex((CharSequence) this.fieldSection.getFieldType(fieldReference)));
            writer.writeInt(this.stringSection.getItemIndex((CharSequence) this.fieldSection.getName(fieldReference)));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeMethods(@Nonnull DexDataWriter writer) throws IOException {
        this.methodSectionOffset = writer.getPosition();
        int index = 0;
        List<Map.Entry<? extends MethodRefKey, Integer>> methodEntries = Lists.newArrayList(this.methodSection.getItems());
        Collections.sort(methodEntries, comparableKeyComparator());
        for (Map.Entry<? extends MethodRefKey, Integer> entry : methodEntries) {
            int i = index;
            index++;
            entry.setValue(Integer.valueOf(i));
            MethodReference methodReference = (MethodReference) entry.getKey();
            writer.writeUshort(this.typeSection.getItemIndex((CharSequence) this.methodSection.getDefiningClass(methodReference)));
            writer.writeUshort(this.protoSection.getItemIndex(this.methodSection.getPrototype(methodReference)));
            writer.writeInt(this.stringSection.getItemIndex((CharSequence) this.methodSection.getName(methodReference)));
        }
    }

    private void writeClasses(@Nonnull DexDataStore dataStore, @Nonnull DexDataWriter indexWriter, @Nonnull DexDataWriter offsetWriter) throws IOException {
        this.classIndexSectionOffset = indexWriter.getPosition();
        this.classDataSectionOffset = offsetWriter.getPosition();
        List<Map.Entry<? extends ClassKey, Integer>> classEntries = Lists.newArrayList(this.classSection.getItems());
        Collections.sort(classEntries, comparableKeyComparator());
        int index = 0;
        for (Map.Entry<? extends ClassKey, Integer> key : classEntries) {
            index = writeClass(indexWriter, offsetWriter, index, key);
        }
        if (!shouldWriteHiddenApiRestrictions()) {
            return;
        }
        this.hiddenApiRestrictionsOffset = offsetWriter.getPosition();
        RestrictionsWriter restrictionsWriter = new RestrictionsWriter(dataStore, offsetWriter, classEntries.size());
        try {
            for (Map.Entry<? extends ClassKey, Integer> key2 : classEntries) {
                for (FieldKey fieldKey : this.classSection.getSortedStaticFields((Comparable) key2.getKey())) {
                    restrictionsWriter.writeRestriction(this.classSection.getFieldHiddenApiRestrictions(fieldKey));
                }
                for (FieldKey fieldKey2 : this.classSection.getSortedInstanceFields((Comparable) key2.getKey())) {
                    restrictionsWriter.writeRestriction(this.classSection.getFieldHiddenApiRestrictions(fieldKey2));
                }
                for (MethodKey methodKey : this.classSection.getSortedDirectMethods((Comparable) key2.getKey())) {
                    restrictionsWriter.writeRestriction(this.classSection.getMethodHiddenApiRestrictions(methodKey));
                }
                for (MethodKey methodKey2 : this.classSection.getSortedVirtualMethods((Comparable) key2.getKey())) {
                    restrictionsWriter.writeRestriction(this.classSection.getMethodHiddenApiRestrictions(methodKey2));
                }
                restrictionsWriter.finishClass();
            }
        } finally {
            restrictionsWriter.close();
        }
    }

    private boolean shouldWriteHiddenApiRestrictions() {
        return this.hasHiddenApiRestrictions && this.opcodes.api >= 29;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/DexWriter$RestrictionsWriter.class */
    public static class RestrictionsWriter {
        private final int startOffset;
        private final DexDataStore dataStore;
        private final DexDataWriter offsetsWriter;
        private final DexDataWriter restrictionsWriter;
        private boolean writeRestrictionsForClass = false;
        private int pendingBlankEntries = 0;

        public RestrictionsWriter(DexDataStore dataStore, DexDataWriter offsetWriter, int numClasses) throws IOException {
            this.startOffset = offsetWriter.getPosition();
            this.dataStore = dataStore;
            this.restrictionsWriter = offsetWriter;
            int offsetsSize = numClasses * 4;
            this.restrictionsWriter.writeInt(0);
            this.offsetsWriter = DexWriter.outputAt(dataStore, this.restrictionsWriter.getPosition());
            for (int i = 0; i < offsetsSize; i++) {
                this.restrictionsWriter.write(0);
            }
            this.restrictionsWriter.flush();
        }

        public void finishClass() throws IOException {
            if (!this.writeRestrictionsForClass) {
                this.offsetsWriter.writeInt(0);
            }
            this.writeRestrictionsForClass = false;
            this.pendingBlankEntries = 0;
        }

        private void addBlankEntry() throws IOException {
            if (this.writeRestrictionsForClass) {
                this.restrictionsWriter.writeUleb128(HiddenApiRestriction.WHITELIST.getValue());
            } else {
                this.pendingBlankEntries++;
            }
        }

        public void writeRestriction(@Nonnull Set<HiddenApiRestriction> hiddenApiRestrictions) throws IOException {
            if (hiddenApiRestrictions.isEmpty()) {
                addBlankEntry();
                return;
            }
            if (!this.writeRestrictionsForClass) {
                this.writeRestrictionsForClass = true;
                this.offsetsWriter.writeInt(this.restrictionsWriter.getPosition() - this.startOffset);
                for (int i = 0; i < this.pendingBlankEntries; i++) {
                    this.restrictionsWriter.writeUleb128(HiddenApiRestriction.WHITELIST.getValue());
                }
                this.pendingBlankEntries = 0;
            }
            this.restrictionsWriter.writeUleb128(HiddenApiRestriction.combineFlags(hiddenApiRestrictions));
        }

        public void close() throws IOException {
            DexDataWriter writer = null;
            this.offsetsWriter.close();
            try {
                writer = DexWriter.outputAt(this.dataStore, this.startOffset);
                writer.writeInt(this.restrictionsWriter.getPosition() - this.startOffset);
                if (writer != null) {
                    writer.close();
                }
            } catch (Throwable th) {
                if (writer != null) {
                    writer.close();
                }
                throw th;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int writeClass(@Nonnull DexDataWriter indexWriter, @Nonnull DexDataWriter offsetWriter, int nextIndex, @Nullable Map.Entry<? extends ClassKey, Integer> entry) throws IOException {
        if (entry == null) {
            return nextIndex;
        }
        if (entry.getValue().intValue() != -1) {
            return nextIndex;
        }
        ClassKey key = entry.getKey();
        entry.setValue(0);
        Map.Entry<? extends ClassKey, Integer> superEntry = this.classSection.getClassEntryByType(this.classSection.getSuperclass(key));
        int nextIndex2 = writeClass(indexWriter, offsetWriter, nextIndex, superEntry);
        for (CharSequence charSequence : this.typeListSection.getTypes(this.classSection.getInterfaces(key))) {
            Map.Entry<? extends ClassKey, Integer> interfaceEntry = this.classSection.getClassEntryByType(charSequence);
            nextIndex2 = writeClass(indexWriter, offsetWriter, nextIndex2, interfaceEntry);
        }
        int i = nextIndex2;
        int nextIndex3 = nextIndex2 + 1;
        entry.setValue(Integer.valueOf(i));
        indexWriter.writeInt(this.typeSection.getItemIndex(this.classSection.getType(key)));
        indexWriter.writeInt(this.classSection.getAccessFlags(key));
        indexWriter.writeInt(this.typeSection.getNullableItemIndex(this.classSection.getSuperclass(key)));
        indexWriter.writeInt(this.typeListSection.getNullableItemOffset(this.classSection.getInterfaces(key)));
        indexWriter.writeInt(this.stringSection.getNullableItemIndex(this.classSection.getSourceFile(key)));
        indexWriter.writeInt(this.classSection.getAnnotationDirectoryOffset(key));
        Collection<? extends FieldKey> staticFields = this.classSection.getSortedStaticFields(key);
        Collection<? extends FieldKey> instanceFields = this.classSection.getSortedInstanceFields(key);
        Collection<? extends MethodKey> directMethods = this.classSection.getSortedDirectMethods(key);
        Collection<? extends MethodKey> virtualMethods = this.classSection.getSortedVirtualMethods(key);
        boolean classHasData = staticFields.size() > 0 || instanceFields.size() > 0 || directMethods.size() > 0 || virtualMethods.size() > 0;
        if (classHasData) {
            indexWriter.writeInt(offsetWriter.getPosition());
        } else {
            indexWriter.writeInt(0);
        }
        Object staticInitializers = this.classSection.getStaticInitializers(key);
        if (staticInitializers != null) {
            indexWriter.writeInt(this.encodedArraySection.getItemOffset(staticInitializers));
        } else {
            indexWriter.writeInt(0);
        }
        if (classHasData) {
            this.numClassDataItems++;
            offsetWriter.writeUleb128(staticFields.size());
            offsetWriter.writeUleb128(instanceFields.size());
            offsetWriter.writeUleb128(directMethods.size());
            offsetWriter.writeUleb128(virtualMethods.size());
            writeEncodedFields(offsetWriter, staticFields);
            writeEncodedFields(offsetWriter, instanceFields);
            writeEncodedMethods(offsetWriter, directMethods);
            writeEncodedMethods(offsetWriter, virtualMethods);
        }
        return nextIndex3;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeCallSites(DexDataWriter writer) throws IOException {
        this.callSiteSectionOffset = writer.getPosition();
        List<Map.Entry<? extends CallSiteKey, Integer>> callSiteEntries = Lists.newArrayList(this.callSiteSection.getItems());
        Collections.sort(callSiteEntries, this.callSiteComparator);
        int index = 0;
        for (Map.Entry<? extends CallSiteKey, Integer> callSite : callSiteEntries) {
            int i = index;
            index++;
            callSite.setValue(Integer.valueOf(i));
            writer.writeInt(this.encodedArraySection.getItemOffset(this.callSiteSection.getEncodedCallSite((CallSiteReference) callSite.getKey())));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeMethodHandles(DexDataWriter writer) throws IOException {
        int itemIndex;
        this.methodHandleSectionOffset = writer.getPosition();
        int index = 0;
        for (Map.Entry<? extends MethodHandleKey, Integer> entry : this.methodHandleSection.getItems()) {
            int i = index;
            index++;
            entry.setValue(Integer.valueOf(i));
            MethodHandleReference methodHandleReference = (MethodHandleReference) entry.getKey();
            writer.writeUshort(methodHandleReference.getMethodHandleType());
            writer.writeUshort(0);
            switch (methodHandleReference.getMethodHandleType()) {
                case 0:
                case 1:
                case 2:
                case 3:
                    itemIndex = this.fieldSection.getItemIndex(this.methodHandleSection.getFieldReference(methodHandleReference));
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    itemIndex = this.methodSection.getItemIndex(this.methodHandleSection.getMethodReference(methodHandleReference));
                    break;
                default:
                    throw new ExceptionWithContext("Invalid method handle type: %d", Integer.valueOf(methodHandleReference.getMethodHandleType()));
            }
            int memberIndex = itemIndex;
            writer.writeUshort(memberIndex);
            writer.writeUshort(0);
        }
    }

    private void writeEncodedFields(@Nonnull DexDataWriter writer, @Nonnull Collection<? extends FieldKey> fields) throws IOException {
        int prevIndex = 0;
        for (FieldKey key : fields) {
            int index = this.fieldSection.getFieldIndex(key);
            if (!this.classSection.getFieldHiddenApiRestrictions(key).isEmpty()) {
                this.hasHiddenApiRestrictions = true;
            }
            writer.writeUleb128(index - prevIndex);
            writer.writeUleb128(this.classSection.getFieldAccessFlags(key));
            prevIndex = index;
        }
    }

    private void writeEncodedMethods(@Nonnull DexDataWriter writer, @Nonnull Collection<? extends MethodKey> methods) throws IOException {
        int prevIndex = 0;
        for (MethodKey key : methods) {
            int index = this.methodSection.getMethodIndex(key);
            if (!this.classSection.getMethodHiddenApiRestrictions(key).isEmpty()) {
                this.hasHiddenApiRestrictions = true;
            }
            writer.writeUleb128(index - prevIndex);
            writer.writeUleb128(this.classSection.getMethodAccessFlags(key));
            writer.writeUleb128(this.classSection.getCodeItemOffset(key));
            prevIndex = index;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeTypeLists(@Nonnull DexDataWriter writer) throws IOException {
        writer.align();
        this.typeListSectionOffset = writer.getPosition();
        for (Map.Entry<? extends TypeListKey, Integer> entry : this.typeListSection.getItems()) {
            writer.align();
            entry.setValue(Integer.valueOf(writer.getPosition()));
            Collection<? extends TypeKey> types = this.typeListSection.getTypes(entry.getKey());
            writer.writeInt(types.size());
            Iterator<? extends TypeKey> it = types.iterator();
            while (it.hasNext()) {
                writer.writeUshort(this.typeSection.getItemIndex((CharSequence) it.next()));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeEncodedArrays(@Nonnull DexDataWriter writer) throws IOException {
        DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, CallSiteKey, MethodHandleKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedArrayKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, CallSiteSectionType, MethodHandleSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType, EncodedArraySectionType>.InternalEncodedValueWriter encodedValueWriter = new InternalEncodedValueWriter(writer);
        this.encodedArraySectionOffset = writer.getPosition();
        for (Map.Entry<? extends EncodedArrayKey, Integer> entry : this.encodedArraySection.getItems()) {
            entry.setValue(Integer.valueOf(writer.getPosition()));
            List<? extends EncodedValue> encodedArray = this.encodedArraySection.getEncodedValueList(entry.getKey());
            writer.writeUleb128(encodedArray.size());
            for (EncodedValue value : encodedArray) {
                writeEncodedValue(encodedValueWriter, value);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeAnnotations(@Nonnull DexDataWriter writer) throws IOException {
        DexWriter<StringKey, StringRef, TypeKey, TypeRef, ProtoRefKey, FieldRefKey, MethodRefKey, ClassKey, CallSiteKey, MethodHandleKey, AnnotationKey, AnnotationSetKey, TypeListKey, FieldKey, MethodKey, EncodedArrayKey, EncodedValue, AnnotationElement, StringSectionType, TypeSectionType, ProtoSectionType, FieldSectionType, MethodSectionType, ClassSectionType, CallSiteSectionType, MethodHandleSectionType, TypeListSectionType, AnnotationSectionType, AnnotationSetSectionType, EncodedArraySectionType>.InternalEncodedValueWriter encodedValueWriter = new InternalEncodedValueWriter(writer);
        this.annotationSectionOffset = writer.getPosition();
        for (Map.Entry<? extends AnnotationKey, Integer> entry : this.annotationSection.getItems()) {
            entry.setValue(Integer.valueOf(writer.getPosition()));
            Annotation annotation = (Annotation) entry.getKey();
            writer.writeUbyte(this.annotationSection.getVisibility(annotation));
            writer.writeUleb128(this.typeSection.getItemIndex((CharSequence) this.annotationSection.getType(annotation)));
            Collection<? extends AnnotationElement> elements = Ordering.from(BaseAnnotationElement.BY_NAME).immutableSortedCopy(this.annotationSection.getElements(annotation));
            writer.writeUleb128(elements.size());
            Iterator<? extends AnnotationElement> it = elements.iterator();
            while (it.hasNext()) {
                AnnotationElement annotationElement = (AnnotationElement) it.next();
                writer.writeUleb128(this.stringSection.getItemIndex((CharSequence) this.annotationSection.getElementName(annotationElement)));
                writeEncodedValue(encodedValueWriter, this.annotationSection.getElementValue(annotationElement));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeAnnotationSets(@Nonnull DexDataWriter writer) throws IOException {
        writer.align();
        this.annotationSetSectionOffset = writer.getPosition();
        if (shouldCreateEmptyAnnotationSet()) {
            writer.writeInt(0);
        }
        for (Map.Entry<? extends AnnotationSetKey, Integer> entry : this.annotationSetSection.getItems()) {
            Collection<? extends AnnotationKey> annotations = Ordering.from(BaseAnnotation.BY_TYPE).immutableSortedCopy(this.annotationSetSection.getAnnotations(entry.getKey()));
            writer.align();
            entry.setValue(Integer.valueOf(writer.getPosition()));
            writer.writeInt(annotations.size());
            Iterator<? extends AnnotationKey> it = annotations.iterator();
            while (it.hasNext()) {
                writer.writeInt(this.annotationSection.getItemOffset((Annotation) it.next()));
            }
        }
    }

    private void writeAnnotationSetRefs(@Nonnull DexDataWriter writer) throws IOException {
        writer.align();
        this.annotationSetRefSectionOffset = writer.getPosition();
        HashMap<List<? extends AnnotationSetKey>, Integer> internedItems = Maps.newHashMap();
        for (Comparable comparable : this.classSection.getSortedClasses()) {
            for (MethodKey methodKey : this.classSection.getSortedMethods(comparable)) {
                List<? extends AnnotationSetKey> parameterAnnotations = this.classSection.getParameterAnnotations(methodKey);
                if (parameterAnnotations != null) {
                    Integer prev = internedItems.get(parameterAnnotations);
                    if (prev != null) {
                        this.classSection.setAnnotationSetRefListOffset(methodKey, prev.intValue());
                    } else {
                        writer.align();
                        int position = writer.getPosition();
                        this.classSection.setAnnotationSetRefListOffset(methodKey, position);
                        internedItems.put(parameterAnnotations, Integer.valueOf(position));
                        this.numAnnotationSetRefItems++;
                        writer.writeInt(parameterAnnotations.size());
                        for (AnnotationSetKey annotationSetKey : parameterAnnotations) {
                            if (this.annotationSetSection.getAnnotations(annotationSetKey).size() > 0) {
                                writer.writeInt(this.annotationSetSection.getItemOffset(annotationSetKey));
                            } else if (shouldCreateEmptyAnnotationSet()) {
                                writer.writeInt(this.annotationSetSectionOffset);
                            } else {
                                writer.writeInt(0);
                            }
                        }
                    }
                }
            }
        }
    }

    private void writeAnnotationDirectories(@Nonnull DexDataWriter writer) throws IOException {
        writer.align();
        this.annotationDirectorySectionOffset = writer.getPosition();
        HashMap newHashMap = Maps.newHashMap();
        ByteBuffer tempBuffer = ByteBuffer.allocate(65536);
        tempBuffer.order(ByteOrder.LITTLE_ENDIAN);
        for (Comparable comparable : this.classSection.getSortedClasses()) {
            Collection<? extends FieldKey> fields = this.classSection.getSortedFields(comparable);
            Collection<? extends MethodKey> methods = this.classSection.getSortedMethods(comparable);
            int maxSize = (fields.size() * 8) + (methods.size() * 16);
            if (maxSize > tempBuffer.capacity()) {
                tempBuffer = ByteBuffer.allocate(maxSize);
                tempBuffer.order(ByteOrder.LITTLE_ENDIAN);
            }
            tempBuffer.clear();
            int fieldAnnotations = 0;
            int methodAnnotations = 0;
            int parameterAnnotations = 0;
            for (FieldKey field : fields) {
                Object fieldAnnotations2 = this.classSection.getFieldAnnotations(field);
                if (fieldAnnotations2 != null) {
                    fieldAnnotations++;
                    tempBuffer.putInt(this.fieldSection.getFieldIndex(field));
                    tempBuffer.putInt(this.annotationSetSection.getItemOffset(fieldAnnotations2));
                }
            }
            for (MethodKey method : methods) {
                Object methodAnnotations2 = this.classSection.getMethodAnnotations(method);
                if (methodAnnotations2 != null) {
                    methodAnnotations++;
                    tempBuffer.putInt(this.methodSection.getMethodIndex(method));
                    tempBuffer.putInt(this.annotationSetSection.getItemOffset(methodAnnotations2));
                }
            }
            for (MethodKey method2 : methods) {
                int offset = this.classSection.getAnnotationSetRefListOffset(method2);
                if (offset != 0) {
                    parameterAnnotations++;
                    tempBuffer.putInt(this.methodSection.getMethodIndex(method2));
                    tempBuffer.putInt(offset);
                }
            }
            Object classAnnotations = this.classSection.getClassAnnotations(comparable);
            if (fieldAnnotations == 0 && methodAnnotations == 0 && parameterAnnotations == 0) {
                if (classAnnotations != null) {
                    Integer directoryOffset = (Integer) newHashMap.get(classAnnotations);
                    if (directoryOffset != null) {
                        this.classSection.setAnnotationDirectoryOffset(comparable, directoryOffset.intValue());
                    } else {
                        newHashMap.put(classAnnotations, Integer.valueOf(writer.getPosition()));
                    }
                }
            }
            this.numAnnotationDirectoryItems++;
            this.classSection.setAnnotationDirectoryOffset(comparable, writer.getPosition());
            writer.writeInt(this.annotationSetSection.getNullableItemOffset(classAnnotations));
            writer.writeInt(fieldAnnotations);
            writer.writeInt(methodAnnotations);
            writer.writeInt(parameterAnnotations);
            writer.write(tempBuffer.array(), 0, tempBuffer.position());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/DexWriter$CodeItemOffset.class */
    public static class CodeItemOffset<MethodKey> {
        @Nonnull
        MethodKey method;
        int codeOffset;

        private CodeItemOffset(@Nonnull MethodKey method, int codeOffset) {
            this.codeOffset = codeOffset;
            this.method = method;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void writeDebugAndCodeItems(@Nonnull DexDataWriter offsetWriter, @Nonnull DeferredOutputStream temp) throws IOException {
        ByteArrayOutputStream ehBuf = new ByteArrayOutputStream();
        this.debugSectionOffset = offsetWriter.getPosition();
        DebugWriter<StringKey, TypeKey> debugWriter = new DebugWriter<>(this.stringSection, this.typeSection, offsetWriter);
        DexDataWriter codeWriter = new DexDataWriter(temp, 0);
        List<CodeItemOffset<MethodKey>> codeOffsets = Lists.newArrayList();
        for (Comparable comparable : this.classSection.getSortedClasses()) {
            Collection<? extends MethodKey> directMethods = this.classSection.getSortedDirectMethods(comparable);
            Collection<? extends MethodKey> virtualMethods = this.classSection.getSortedVirtualMethods(comparable);
            Iterable<MethodKey> methods = Iterables.concat(directMethods, virtualMethods);
            for (MethodKey methodKey : methods) {
                List<? extends TryBlock<? extends ExceptionHandler>> tryBlocks = this.classSection.getTryBlocks(methodKey);
                Iterable<? extends Instruction> instructions = this.classSection.getInstructions(methodKey);
                Iterable<? extends DebugItem> debugItems = this.classSection.getDebugItems(methodKey);
                List<? extends TryBlock<? extends ExceptionHandler>> tryBlocks2 = tryBlocks;
                Iterable<? extends Instruction> instructions2 = instructions;
                if (instructions != null) {
                    tryBlocks2 = tryBlocks;
                    instructions2 = instructions;
                    if (this.stringSection.hasJumboIndexes()) {
                        boolean needsFix = false;
                        Iterator<? extends Instruction> it = instructions.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            Instruction instruction = it.next();
                            if (instruction.getOpcode() == Opcode.CONST_STRING && this.stringSection.getItemIndex((StringReference) ((ReferenceInstruction) instruction).getReference()) >= 65536) {
                                needsFix = true;
                                break;
                            }
                        }
                        tryBlocks2 = tryBlocks;
                        instructions2 = instructions;
                        if (needsFix) {
                            MutableMethodImplementation mutableMethodImplementation = this.classSection.makeMutableMethodImplementation(methodKey);
                            fixInstructions(mutableMethodImplementation);
                            instructions2 = mutableMethodImplementation.getInstructions();
                            tryBlocks2 = mutableMethodImplementation.getTryBlocks();
                            debugItems = mutableMethodImplementation.getDebugItems();
                        }
                    }
                }
                int debugItemOffset = writeDebugItem(offsetWriter, debugWriter, this.classSection.getParameterNames(methodKey), debugItems);
                try {
                    int codeItemOffset = writeCodeItem(codeWriter, ehBuf, methodKey, tryBlocks2, instructions2, debugItemOffset);
                    if (codeItemOffset != -1) {
                        codeOffsets.add(new CodeItemOffset<>(methodKey, codeItemOffset));
                    }
                } catch (RuntimeException ex) {
                    throw new ExceptionWithContext(ex, "Exception occurred while writing code_item for method %s", this.methodSection.getMethodReference(methodKey));
                }
            }
        }
        offsetWriter.align();
        this.codeSectionOffset = offsetWriter.getPosition();
        codeWriter.close();
        temp.writeTo(offsetWriter);
        temp.close();
        for (CodeItemOffset<MethodKey> codeOffset : codeOffsets) {
            this.classSection.setCodeItemOffset(codeOffset.method, this.codeSectionOffset + codeOffset.codeOffset);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void fixInstructions(@Nonnull MutableMethodImplementation methodImplementation) {
        List<? extends Instruction> instructions = methodImplementation.getInstructions();
        for (int i = 0; i < instructions.size(); i++) {
            Instruction instruction = instructions.get(i);
            if (instruction.getOpcode() == Opcode.CONST_STRING && this.stringSection.getItemIndex((StringReference) ((ReferenceInstruction) instruction).getReference()) >= 65536) {
                methodImplementation.replaceInstruction(i, new BuilderInstruction31c(Opcode.CONST_STRING_JUMBO, ((OneRegisterInstruction) instruction).getRegisterA(), ((ReferenceInstruction) instruction).getReference()));
            }
        }
    }

    private int writeDebugItem(@Nonnull DexDataWriter writer, @Nonnull DebugWriter<StringKey, TypeKey> debugWriter, @Nullable Iterable<? extends StringKey> parameterNames, @Nullable Iterable<? extends DebugItem> debugItems) throws IOException {
        int parameterCount = 0;
        int lastNamedParameterIndex = -1;
        if (parameterNames != null) {
            parameterCount = Iterables.size(parameterNames);
            int index = 0;
            for (StringKey parameterName : parameterNames) {
                if (parameterName != null) {
                    lastNamedParameterIndex = index;
                }
                index++;
            }
        }
        if (lastNamedParameterIndex == -1 && (debugItems == null || Iterables.isEmpty(debugItems))) {
            return 0;
        }
        this.numDebugInfoItems++;
        int debugItemOffset = writer.getPosition();
        int startingLineNumber = 0;
        if (debugItems != null) {
            Iterator<? extends DebugItem> it = debugItems.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                DebugItem debugItem = it.next();
                if (debugItem instanceof LineNumber) {
                    startingLineNumber = ((LineNumber) debugItem).getLineNumber();
                    break;
                }
            }
        }
        writer.writeUleb128(startingLineNumber);
        writer.writeUleb128(parameterCount);
        if (parameterNames != null) {
            int index2 = 0;
            for (StringKey parameterName2 : parameterNames) {
                if (index2 == parameterCount) {
                    break;
                }
                index2++;
                writer.writeUleb128(this.stringSection.getNullableItemIndex(parameterName2) + 1);
            }
        }
        if (debugItems != null) {
            debugWriter.reset(startingLineNumber);
            for (DebugItem debugItem2 : debugItems) {
                this.classSection.writeDebugItem(debugWriter, debugItem2);
            }
        }
        writer.write(0);
        return debugItemOffset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int writeCodeItem(@Nonnull DexDataWriter writer, @Nonnull ByteArrayOutputStream ehBuf, @Nonnull MethodKey methodKey, @Nonnull List<? extends TryBlock<? extends ExceptionHandler>> tryBlocks, @Nullable Iterable<? extends Instruction> instructions, int debugItemOffset) throws IOException {
        int paramCount;
        if (instructions == null && debugItemOffset == 0) {
            return -1;
        }
        this.numCodeItemItems++;
        writer.align();
        int codeItemOffset = writer.getPosition();
        writer.writeUshort(this.classSection.getRegisterCount(methodKey));
        boolean isStatic = AccessFlags.STATIC.isSet(this.classSection.getMethodAccessFlags(methodKey));
        Collection<? extends TypeKey> parameters = this.typeListSection.getTypes(this.protoSection.getParameters(this.methodSection.getPrototype(methodKey)));
        writer.writeUshort(MethodUtil.getParameterRegisterCount(parameters, isStatic));
        if (instructions != null) {
            List<? extends TryBlock<? extends ExceptionHandler>> tryBlocks2 = TryListBuilder.massageTryBlocks(tryBlocks);
            int outParamCount = 0;
            int codeUnitCount = 0;
            for (Instruction instruction : instructions) {
                codeUnitCount += instruction.getCodeUnits();
                if (instruction.getOpcode().referenceType == 3) {
                    ReferenceInstruction refInsn = (ReferenceInstruction) instruction;
                    MethodReference methodRef = (MethodReference) refInsn.getReference();
                    Opcode opcode = instruction.getOpcode();
                    if (InstructionUtil.isInvokePolymorphic(opcode)) {
                        paramCount = ((VariableRegisterInstruction) instruction).getRegisterCount();
                    } else {
                        paramCount = MethodUtil.getParameterRegisterCount(methodRef, InstructionUtil.isInvokeStatic(opcode));
                    }
                    if (paramCount > outParamCount) {
                        outParamCount = paramCount;
                    }
                }
            }
            writer.writeUshort(outParamCount);
            writer.writeUshort(tryBlocks2.size());
            writer.writeInt(debugItemOffset);
            InstructionWriter instructionWriter = InstructionWriter.makeInstructionWriter(this.opcodes, writer, this.stringSection, this.typeSection, this.fieldSection, this.methodSection, this.protoSection, this.methodHandleSection, this.callSiteSection);
            writer.writeInt(codeUnitCount);
            int codeOffset = 0;
            for (Instruction instruction2 : instructions) {
                try {
                    switch (instruction2.getOpcode().format) {
                        case Format10t:
                            instructionWriter.write((Instruction10t) instruction2);
                            break;
                        case Format10x:
                            instructionWriter.write((Instruction10x) instruction2);
                            break;
                        case Format11n:
                            instructionWriter.write((Instruction11n) instruction2);
                            break;
                        case Format11x:
                            instructionWriter.write((Instruction11x) instruction2);
                            break;
                        case Format12x:
                            instructionWriter.write((Instruction12x) instruction2);
                            break;
                        case Format20bc:
                            instructionWriter.write((Instruction20bc) instruction2);
                            break;
                        case Format20t:
                            instructionWriter.write((Instruction20t) instruction2);
                            break;
                        case Format21c:
                            instructionWriter.write((Instruction21c) instruction2);
                            break;
                        case Format21ih:
                            instructionWriter.write((Instruction21ih) instruction2);
                            break;
                        case Format21lh:
                            instructionWriter.write((Instruction21lh) instruction2);
                            break;
                        case Format21s:
                            instructionWriter.write((Instruction21s) instruction2);
                            break;
                        case Format21t:
                            instructionWriter.write((Instruction21t) instruction2);
                            break;
                        case Format22b:
                            instructionWriter.write((Instruction22b) instruction2);
                            break;
                        case Format22c:
                            instructionWriter.write((Instruction22c) instruction2);
                            break;
                        case Format22cs:
                            instructionWriter.write((Instruction22cs) instruction2);
                            break;
                        case Format22s:
                            instructionWriter.write((Instruction22s) instruction2);
                            break;
                        case Format22t:
                            instructionWriter.write((Instruction22t) instruction2);
                            break;
                        case Format22x:
                            instructionWriter.write((Instruction22x) instruction2);
                            break;
                        case Format23x:
                            instructionWriter.write((Instruction23x) instruction2);
                            break;
                        case Format30t:
                            instructionWriter.write((Instruction30t) instruction2);
                            break;
                        case Format31c:
                            instructionWriter.write((Instruction31c) instruction2);
                            break;
                        case Format31i:
                            instructionWriter.write((Instruction31i) instruction2);
                            break;
                        case Format31t:
                            instructionWriter.write((Instruction31t) instruction2);
                            break;
                        case Format32x:
                            instructionWriter.write((Instruction32x) instruction2);
                            break;
                        case Format35c:
                            instructionWriter.write((Instruction35c) instruction2);
                            break;
                        case Format35mi:
                            instructionWriter.write((Instruction35mi) instruction2);
                            break;
                        case Format35ms:
                            instructionWriter.write((Instruction35ms) instruction2);
                            break;
                        case Format3rc:
                            instructionWriter.write((Instruction3rc) instruction2);
                            break;
                        case Format3rmi:
                            instructionWriter.write((Instruction3rmi) instruction2);
                            break;
                        case Format3rms:
                            instructionWriter.write((Instruction3rms) instruction2);
                            break;
                        case Format45cc:
                            instructionWriter.write((Instruction45cc) instruction2);
                            break;
                        case Format4rcc:
                            instructionWriter.write((Instruction4rcc) instruction2);
                            break;
                        case Format51l:
                            instructionWriter.write((Instruction51l) instruction2);
                            break;
                        case ArrayPayload:
                            instructionWriter.write((ArrayPayload) instruction2);
                            break;
                        case PackedSwitchPayload:
                            instructionWriter.write((PackedSwitchPayload) instruction2);
                            break;
                        case SparseSwitchPayload:
                            instructionWriter.write((SparseSwitchPayload) instruction2);
                            break;
                        default:
                            throw new ExceptionWithContext("Unsupported instruction format: %s", instruction2.getOpcode().format);
                    }
                    codeOffset += instruction2.getCodeUnits();
                } catch (RuntimeException ex) {
                    throw new ExceptionWithContext(ex, "Error while writing instruction at code offset 0x%x", Integer.valueOf(codeOffset));
                }
            }
            if (tryBlocks2.size() > 0) {
                writer.align();
                Map<List<? extends ExceptionHandler>, Integer> exceptionHandlerOffsetMap = Maps.newHashMap();
                Iterator<? extends TryBlock<? extends ExceptionHandler>> it = tryBlocks2.iterator();
                while (it.hasNext()) {
                    exceptionHandlerOffsetMap.put(((TryBlock) it.next()).getExceptionHandlers(), 0);
                }
                DexDataWriter.writeUleb128(ehBuf, exceptionHandlerOffsetMap.size());
                Iterator<? extends TryBlock<? extends ExceptionHandler>> it2 = tryBlocks2.iterator();
                while (it2.hasNext()) {
                    TryBlock tryBlock = (TryBlock) it2.next();
                    int startAddress = tryBlock.getStartCodeAddress();
                    int endAddress = startAddress + tryBlock.getCodeUnitCount();
                    int tbCodeUnitCount = endAddress - startAddress;
                    writer.writeInt(startAddress);
                    writer.writeUshort(tbCodeUnitCount);
                    if (tryBlock.getExceptionHandlers().size() == 0) {
                        throw new ExceptionWithContext("No exception handlers for the try block!", new Object[0]);
                    }
                    Integer offset = exceptionHandlerOffsetMap.get(tryBlock.getExceptionHandlers());
                    if (offset.intValue() != 0) {
                        writer.writeUshort(offset.intValue());
                    } else {
                        Integer offset2 = Integer.valueOf(ehBuf.size());
                        writer.writeUshort(offset2.intValue());
                        exceptionHandlerOffsetMap.put(tryBlock.getExceptionHandlers(), offset2);
                        int ehSize = tryBlock.getExceptionHandlers().size();
                        ExceptionHandler ehLast = (ExceptionHandler) tryBlock.getExceptionHandlers().get(ehSize - 1);
                        if (ehLast.getExceptionType() == null) {
                            ehSize = (ehSize * (-1)) + 1;
                        }
                        DexDataWriter.writeSleb128(ehBuf, ehSize);
                        for (ExceptionHandler eh : tryBlock.getExceptionHandlers()) {
                            CharSequence exceptionType = this.classSection.getExceptionType(eh);
                            int codeAddress = eh.getHandlerCodeAddress();
                            if (exceptionType != null) {
                                DexDataWriter.writeUleb128(ehBuf, this.typeSection.getItemIndex(exceptionType));
                                DexDataWriter.writeUleb128(ehBuf, codeAddress);
                            } else {
                                DexDataWriter.writeUleb128(ehBuf, codeAddress);
                            }
                        }
                    }
                }
                if (ehBuf.size() > 0) {
                    ehBuf.writeTo(writer);
                    ehBuf.reset();
                }
            }
        } else {
            writer.writeUshort(0);
            writer.writeUshort(0);
            writer.writeInt(debugItemOffset);
            writer.writeInt(0);
        }
        return codeItemOffset;
    }

    private int calcNumItems() {
        int numItems = 0 + 1;
        if (this.stringSection.getItems().size() > 0) {
            numItems += 2;
        }
        if (this.typeSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.protoSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.fieldSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.methodSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.callSiteSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.methodHandleSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.typeListSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.encodedArraySection.getItems().size() > 0) {
            numItems++;
        }
        if (this.annotationSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.annotationSetSection.getItems().size() > 0 || shouldCreateEmptyAnnotationSet()) {
            numItems++;
        }
        if (this.numAnnotationSetRefItems > 0) {
            numItems++;
        }
        if (this.numAnnotationDirectoryItems > 0) {
            numItems++;
        }
        if (this.numDebugInfoItems > 0) {
            numItems++;
        }
        if (this.numCodeItemItems > 0) {
            numItems++;
        }
        if (this.classSection.getItems().size() > 0) {
            numItems++;
        }
        if (this.numClassDataItems > 0) {
            numItems++;
        }
        if (shouldWriteHiddenApiRestrictions()) {
            numItems++;
        }
        return numItems + 1;
    }

    private void writeMapItem(@Nonnull DexDataWriter writer) throws IOException {
        writer.align();
        this.mapSectionOffset = writer.getPosition();
        int numItems = calcNumItems();
        writer.writeInt(numItems);
        writeMapItem(writer, 0, 1, 0);
        writeMapItem(writer, 1, this.stringSection.getItems().size(), this.stringIndexSectionOffset);
        writeMapItem(writer, 2, this.typeSection.getItems().size(), this.typeSectionOffset);
        writeMapItem(writer, 3, this.protoSection.getItems().size(), this.protoSectionOffset);
        writeMapItem(writer, 4, this.fieldSection.getItems().size(), this.fieldSectionOffset);
        writeMapItem(writer, 5, this.methodSection.getItems().size(), this.methodSectionOffset);
        writeMapItem(writer, 6, this.classSection.getItems().size(), this.classIndexSectionOffset);
        writeMapItem(writer, 7, this.callSiteSection.getItems().size(), this.callSiteSectionOffset);
        writeMapItem(writer, 8, this.methodHandleSection.getItems().size(), this.methodHandleSectionOffset);
        writeMapItem(writer, 8194, this.stringSection.getItems().size(), this.stringDataSectionOffset);
        writeMapItem(writer, 4097, this.typeListSection.getItems().size(), this.typeListSectionOffset);
        writeMapItem(writer, ItemType.ENCODED_ARRAY_ITEM, this.encodedArraySection.getItems().size(), this.encodedArraySectionOffset);
        writeMapItem(writer, ItemType.ANNOTATION_ITEM, this.annotationSection.getItems().size(), this.annotationSectionOffset);
        writeMapItem(writer, 4099, this.annotationSetSection.getItems().size() + (shouldCreateEmptyAnnotationSet() ? 1 : 0), this.annotationSetSectionOffset);
        writeMapItem(writer, 4098, this.numAnnotationSetRefItems, this.annotationSetRefSectionOffset);
        writeMapItem(writer, ItemType.ANNOTATION_DIRECTORY_ITEM, this.numAnnotationDirectoryItems, this.annotationDirectorySectionOffset);
        writeMapItem(writer, ItemType.DEBUG_INFO_ITEM, this.numDebugInfoItems, this.debugSectionOffset);
        writeMapItem(writer, ItemType.CODE_ITEM, this.numCodeItemItems, this.codeSectionOffset);
        writeMapItem(writer, 8192, this.numClassDataItems, this.classDataSectionOffset);
        if (shouldWriteHiddenApiRestrictions()) {
            writeMapItem(writer, ItemType.HIDDENAPI_CLASS_DATA_ITEM, 1, this.hiddenApiRestrictionsOffset);
        }
        writeMapItem(writer, 4096, 1, this.mapSectionOffset);
    }

    private void writeMapItem(@Nonnull DexDataWriter writer, int type, int size, int offset) throws IOException {
        if (size > 0) {
            writer.writeUshort(type);
            writer.writeUshort(0);
            writer.writeInt(size);
            writer.writeInt(offset);
        }
    }

    private void writeHeader(@Nonnull DexDataWriter writer, int dataOffset, int fileSize) throws IOException {
        writer.write(HeaderItem.getMagicForApi(this.opcodes.api));
        writer.writeInt(0);
        writer.write(new byte[20]);
        writer.writeInt(fileSize);
        writer.writeInt(112);
        writer.writeInt(HeaderItem.LITTLE_ENDIAN_TAG);
        writer.writeInt(0);
        writer.writeInt(0);
        writer.writeInt(this.mapSectionOffset);
        writeSectionInfo(writer, this.stringSection.getItems().size(), this.stringIndexSectionOffset);
        writeSectionInfo(writer, this.typeSection.getItems().size(), this.typeSectionOffset);
        writeSectionInfo(writer, this.protoSection.getItems().size(), this.protoSectionOffset);
        writeSectionInfo(writer, this.fieldSection.getItems().size(), this.fieldSectionOffset);
        writeSectionInfo(writer, this.methodSection.getItems().size(), this.methodSectionOffset);
        writeSectionInfo(writer, this.classSection.getItems().size(), this.classIndexSectionOffset);
        writer.writeInt(fileSize - dataOffset);
        writer.writeInt(dataOffset);
    }

    private void writeSectionInfo(DexDataWriter writer, int numItems, int offset) throws IOException {
        writer.writeInt(numItems);
        if (numItems > 0) {
            writer.writeInt(offset);
        } else {
            writer.writeInt(0);
        }
    }

    private boolean shouldCreateEmptyAnnotationSet() {
        return this.opcodes.api < 17;
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/DexWriter$SectionProvider.class */
    public abstract class SectionProvider {
        @Nonnull
        public abstract StringSectionType getStringSection();

        @Nonnull
        public abstract TypeSectionType getTypeSection();

        @Nonnull
        public abstract ProtoSectionType getProtoSection();

        @Nonnull
        public abstract FieldSectionType getFieldSection();

        @Nonnull
        public abstract MethodSectionType getMethodSection();

        @Nonnull
        public abstract ClassSectionType getClassSection();

        @Nonnull
        public abstract CallSiteSectionType getCallSiteSection();

        @Nonnull
        public abstract MethodHandleSectionType getMethodHandleSection();

        @Nonnull
        public abstract TypeListSectionType getTypeListSection();

        @Nonnull
        public abstract AnnotationSectionType getAnnotationSection();

        @Nonnull
        public abstract AnnotationSetSectionType getAnnotationSetSection();

        @Nonnull
        public abstract EncodedArraySectionType getEncodedArraySection();

        public SectionProvider() {
        }
    }
}
