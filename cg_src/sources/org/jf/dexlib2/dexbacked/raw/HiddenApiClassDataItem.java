package org.jf.dexlib2.dexbacked.raw;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.HiddenApiRestriction;
import org.jf.dexlib2.dexbacked.DexBuffer;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.util.AnnotatedBytes;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/HiddenApiClassDataItem.class */
public class HiddenApiClassDataItem {
    public static final int SIZE_OFFSET = 0;
    public static final int OFFSETS_LIST_OFFSET = 4;
    public static final int OFFSET_ITEM_SIZE = 4;

    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: org.jf.dexlib2.dexbacked.raw.HiddenApiClassDataItem.1
            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "hiddenapi_class_data_item";
            }

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            protected void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                int startOffset = out.getCursor();
                out.annotate(4, "size = 0x%x", Integer.valueOf(this.dexFile.getDataBuffer().readSmallUint(out.getCursor())));
                int index = 0;
                for (ClassDef classDef : this.dexFile.getClasses()) {
                    out.annotate(0, "[%d] %s", Integer.valueOf(index), classDef);
                    out.indent();
                    int offset = this.dexFile.getDataBuffer().readSmallUint(out.getCursor());
                    if (offset == 0) {
                        out.annotate(4, "offset = 0x%x", Integer.valueOf(offset));
                    } else {
                        out.annotate(4, "offset = 0x%x (absolute offset: 0x%x)", Integer.valueOf(offset), Integer.valueOf(startOffset + offset));
                    }
                    int nextOffset = out.getCursor();
                    if (offset > 0) {
                        out.deindent();
                        out.moveTo(startOffset + offset);
                        DexReader<? extends DexBuffer> reader = this.dexFile.getBuffer().readerAt(out.getCursor());
                        for (Field field : classDef.getStaticFields()) {
                            out.annotate(0, "%s:", field);
                            out.indent();
                            int restrictions = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "restriction = 0x%x: %s", Integer.valueOf(restrictions), HiddenApiRestriction.formatHiddenRestrictions(restrictions));
                            out.deindent();
                        }
                        for (Field field2 : classDef.getInstanceFields()) {
                            out.annotate(0, "%s:", field2);
                            out.indent();
                            int restrictions2 = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "restriction = 0x%x: %s", Integer.valueOf(restrictions2), HiddenApiRestriction.formatHiddenRestrictions(restrictions2));
                            out.deindent();
                        }
                        for (Method method : classDef.getDirectMethods()) {
                            out.annotate(0, "%s:", method);
                            out.indent();
                            int restrictions3 = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "restriction = 0x%x: %s", Integer.valueOf(restrictions3), HiddenApiRestriction.formatHiddenRestrictions(restrictions3));
                            out.deindent();
                        }
                        for (Method method2 : classDef.getVirtualMethods()) {
                            out.annotate(0, "%s:", method2);
                            out.indent();
                            int restrictions4 = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "restriction = 0x%x: %s", Integer.valueOf(restrictions4), HiddenApiRestriction.formatHiddenRestrictions(restrictions4));
                            out.deindent();
                        }
                        out.indent();
                    }
                    out.moveTo(nextOffset);
                    out.deindent();
                    index++;
                }
            }
        };
    }
}
