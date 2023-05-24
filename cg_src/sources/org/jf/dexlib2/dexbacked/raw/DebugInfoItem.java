package org.jf.dexlib2.dexbacked.raw;

import android.widget.ExpandableListView;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.util.AnnotatedBytes;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/DebugInfoItem.class */
public class DebugInfoItem {
    @Nonnull
    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new SectionAnnotator(annotator, mapItem) { // from class: org.jf.dexlib2.dexbacked.raw.DebugInfoItem.1
            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            @Nonnull
            public String getItemName() {
                return "debug_info_item";
            }

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            public void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
                DexReader reader = this.dexFile.getBuffer().readerAt(out.getCursor());
                int lineStart = reader.readBigUleb128();
                out.annotateTo(reader.getOffset(), "line_start = %d", Long.valueOf(lineStart & ExpandableListView.PACKED_POSITION_VALUE_NULL));
                int parametersSize = reader.readSmallUleb128();
                out.annotateTo(reader.getOffset(), "parameters_size = %d", Integer.valueOf(parametersSize));
                if (parametersSize > 0) {
                    out.annotate(0, "parameters:", new Object[0]);
                    out.indent();
                    for (int i = 0; i < parametersSize; i++) {
                        int paramaterIndex = reader.readSmallUleb128() - 1;
                        out.annotateTo(reader.getOffset(), "%s", StringIdItem.getOptionalReferenceAnnotation(this.dexFile, paramaterIndex, true));
                    }
                    out.deindent();
                }
                out.annotate(0, "debug opcodes:", new Object[0]);
                out.indent();
                int codeAddress = 0;
                int lineNumber = lineStart;
                while (true) {
                    int opcode = reader.readUbyte();
                    switch (opcode) {
                        case 0:
                            out.annotateTo(reader.getOffset(), "DBG_END_SEQUENCE", new Object[0]);
                            out.deindent();
                            return;
                        case 1:
                            out.annotateTo(reader.getOffset(), "DBG_ADVANCE_PC", new Object[0]);
                            out.indent();
                            int addressDiff = reader.readSmallUleb128();
                            codeAddress += addressDiff;
                            out.annotateTo(reader.getOffset(), "addr_diff = +0x%x: 0x%x", Integer.valueOf(addressDiff), Integer.valueOf(codeAddress));
                            out.deindent();
                            break;
                        case 2:
                            out.annotateTo(reader.getOffset(), "DBG_ADVANCE_LINE", new Object[0]);
                            out.indent();
                            int lineDiff = reader.readSleb128();
                            lineNumber += lineDiff;
                            out.annotateTo(reader.getOffset(), "line_diff = +%d: %d", Integer.valueOf(Math.abs(lineDiff)), Integer.valueOf(lineNumber));
                            out.deindent();
                            break;
                        case 3:
                            out.annotateTo(reader.getOffset(), "DBG_START_LOCAL", new Object[0]);
                            out.indent();
                            int registerNum = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "register_num = v%d", Integer.valueOf(registerNum));
                            int nameIndex = reader.readSmallUleb128() - 1;
                            out.annotateTo(reader.getOffset(), "name_idx = %s", StringIdItem.getOptionalReferenceAnnotation(this.dexFile, nameIndex, true));
                            int typeIndex = reader.readSmallUleb128() - 1;
                            out.annotateTo(reader.getOffset(), "type_idx = %s", TypeIdItem.getOptionalReferenceAnnotation(this.dexFile, typeIndex));
                            out.deindent();
                            break;
                        case 4:
                            out.annotateTo(reader.getOffset(), "DBG_START_LOCAL_EXTENDED", new Object[0]);
                            out.indent();
                            int registerNum2 = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "register_num = v%d", Integer.valueOf(registerNum2));
                            int nameIndex2 = reader.readSmallUleb128() - 1;
                            out.annotateTo(reader.getOffset(), "name_idx = %s", StringIdItem.getOptionalReferenceAnnotation(this.dexFile, nameIndex2, true));
                            int typeIndex2 = reader.readSmallUleb128() - 1;
                            out.annotateTo(reader.getOffset(), "type_idx = %s", TypeIdItem.getOptionalReferenceAnnotation(this.dexFile, typeIndex2));
                            int sigIndex = reader.readSmallUleb128() - 1;
                            out.annotateTo(reader.getOffset(), "sig_idx = %s", StringIdItem.getOptionalReferenceAnnotation(this.dexFile, sigIndex, true));
                            out.deindent();
                            break;
                        case 5:
                            out.annotateTo(reader.getOffset(), "DBG_END_LOCAL", new Object[0]);
                            out.indent();
                            int registerNum3 = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "register_num = v%d", Integer.valueOf(registerNum3));
                            out.deindent();
                            break;
                        case 6:
                            out.annotateTo(reader.getOffset(), "DBG_RESTART_LOCAL", new Object[0]);
                            out.indent();
                            int registerNum4 = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "register_num = v%d", Integer.valueOf(registerNum4));
                            out.deindent();
                            break;
                        case 7:
                            out.annotateTo(reader.getOffset(), "DBG_SET_PROLOGUE_END", new Object[0]);
                            break;
                        case 8:
                            out.annotateTo(reader.getOffset(), "DBG_SET_EPILOGUE_BEGIN", new Object[0]);
                            break;
                        case 9:
                            out.annotateTo(reader.getOffset(), "DBG_SET_FILE", new Object[0]);
                            out.indent();
                            int nameIdx = reader.readSmallUleb128() - 1;
                            out.annotateTo(reader.getOffset(), "name_idx = %s", StringIdItem.getOptionalReferenceAnnotation(this.dexFile, nameIdx));
                            out.deindent();
                            break;
                        default:
                            int adjusted = opcode - 10;
                            int addressDiff2 = adjusted / 15;
                            int lineDiff2 = (adjusted % 15) - 4;
                            codeAddress += addressDiff2;
                            lineNumber += lineDiff2;
                            out.annotateTo(reader.getOffset(), "address_diff = +0x%x:0x%x, line_diff = +%d:%d, ", Integer.valueOf(addressDiff2), Integer.valueOf(codeAddress), Integer.valueOf(lineDiff2), Integer.valueOf(lineNumber));
                            break;
                    }
                }
            }
        };
    }
}
