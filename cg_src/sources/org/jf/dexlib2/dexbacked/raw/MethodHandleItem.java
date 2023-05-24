package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.MethodHandleType;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/MethodHandleItem.class */
public class MethodHandleItem {
    public static final int ITEM_SIZE = 8;
    public static final int METHOD_HANDLE_TYPE_OFFSET = 0;
    public static final int MEMBER_ID_OFFSET = 4;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: org.jf.dexlib2.dexbacked.raw.MethodHandleItem.1
            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "method_handle_item";
            }

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                String fieldOrMethodDescriptor;
                int methodHandleType = this.dexFile.getBuffer().readUshort(out.getCursor());
                out.annotate(2, "type = %s", MethodHandleType.toString(methodHandleType));
                out.annotate(2, "unused", new Object[0]);
                int fieldOrMethodId = this.dexFile.getBuffer().readUshort(out.getCursor());
                switch (methodHandleType) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        fieldOrMethodDescriptor = FieldIdItem.getReferenceAnnotation(this.dexFile, fieldOrMethodId);
                        break;
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        fieldOrMethodDescriptor = MethodIdItem.getReferenceAnnotation(this.dexFile, fieldOrMethodId);
                        break;
                    default:
                        throw new ExceptionWithContext("Invalid method handle type: %d", Integer.valueOf(methodHandleType));
                }
                out.annotate(2, "field_or_method_id = %s", fieldOrMethodDescriptor);
                out.annotate(2, "unused", new Object[0]);
            }
        };
    }
}
