package org.jf.dexlib2.writer;

import java.io.IOException;
import java.lang.CharSequence;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.HiddenApiRestriction;
import org.jf.dexlib2.builder.MutableMethodImplementation;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.instruction.Instruction;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/ClassSection.class */
public interface ClassSection<StringKey extends CharSequence, TypeKey extends CharSequence, TypeListKey, ClassKey, FieldKey, MethodKey, AnnotationSetKey, EncodedArrayKey> extends IndexSection<ClassKey> {
    @Nonnull
    Collection<? extends ClassKey> getSortedClasses();

    @Nullable
    Map.Entry<? extends ClassKey, Integer> getClassEntryByType(@Nullable TypeKey typekey);

    @Nonnull
    TypeKey getType(@Nonnull ClassKey classkey);

    int getAccessFlags(@Nonnull ClassKey classkey);

    @Nullable
    TypeKey getSuperclass(@Nonnull ClassKey classkey);

    @Nullable
    TypeListKey getInterfaces(@Nonnull ClassKey classkey);

    @Nullable
    StringKey getSourceFile(@Nonnull ClassKey classkey);

    @Nullable
    EncodedArrayKey getStaticInitializers(@Nonnull ClassKey classkey);

    @Nonnull
    Collection<? extends FieldKey> getSortedStaticFields(@Nonnull ClassKey classkey);

    @Nonnull
    Collection<? extends FieldKey> getSortedInstanceFields(@Nonnull ClassKey classkey);

    @Nonnull
    Collection<? extends FieldKey> getSortedFields(@Nonnull ClassKey classkey);

    @Nonnull
    Collection<? extends MethodKey> getSortedDirectMethods(@Nonnull ClassKey classkey);

    @Nonnull
    Collection<? extends MethodKey> getSortedVirtualMethods(@Nonnull ClassKey classkey);

    @Nonnull
    Collection<? extends MethodKey> getSortedMethods(@Nonnull ClassKey classkey);

    int getFieldAccessFlags(@Nonnull FieldKey fieldkey);

    int getMethodAccessFlags(@Nonnull MethodKey methodkey);

    @Nonnull
    Set<HiddenApiRestriction> getFieldHiddenApiRestrictions(@Nonnull FieldKey fieldkey);

    @Nonnull
    Set<HiddenApiRestriction> getMethodHiddenApiRestrictions(@Nonnull MethodKey methodkey);

    @Nullable
    AnnotationSetKey getClassAnnotations(@Nonnull ClassKey classkey);

    @Nullable
    AnnotationSetKey getFieldAnnotations(@Nonnull FieldKey fieldkey);

    @Nullable
    AnnotationSetKey getMethodAnnotations(@Nonnull MethodKey methodkey);

    @Nullable
    List<? extends AnnotationSetKey> getParameterAnnotations(@Nonnull MethodKey methodkey);

    @Nullable
    Iterable<? extends DebugItem> getDebugItems(@Nonnull MethodKey methodkey);

    @Nullable
    Iterable<? extends StringKey> getParameterNames(@Nonnull MethodKey methodkey);

    int getRegisterCount(@Nonnull MethodKey methodkey);

    @Nullable
    Iterable<? extends Instruction> getInstructions(@Nonnull MethodKey methodkey);

    @Nonnull
    List<? extends TryBlock<? extends ExceptionHandler>> getTryBlocks(@Nonnull MethodKey methodkey);

    @Nullable
    TypeKey getExceptionType(@Nonnull ExceptionHandler exceptionHandler);

    @Nonnull
    MutableMethodImplementation makeMutableMethodImplementation(@Nonnull MethodKey methodkey);

    void setAnnotationDirectoryOffset(@Nonnull ClassKey classkey, int i);

    int getAnnotationDirectoryOffset(@Nonnull ClassKey classkey);

    void setAnnotationSetRefListOffset(@Nonnull MethodKey methodkey, int i);

    int getAnnotationSetRefListOffset(@Nonnull MethodKey methodkey);

    void setCodeItemOffset(@Nonnull MethodKey methodkey, int i);

    int getCodeItemOffset(@Nonnull MethodKey methodkey);

    void writeDebugItem(@Nonnull DebugWriter<StringKey, TypeKey> debugWriter, DebugItem debugItem) throws IOException;
}
