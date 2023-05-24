package org.jf.dexlib2.dexbacked.raw;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.cli.HelpFormatter;
import org.jf.dexlib2.VerificationError;
import org.jf.dexlib2.dexbacked.CDexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.instruction.DexBackedInstruction;
import org.jf.dexlib2.dexbacked.raw.util.DexAnnotator;
import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.instruction.FieldOffsetInstruction;
import org.jf.dexlib2.iface.instruction.InlineIndexInstruction;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.NarrowLiteralInstruction;
import org.jf.dexlib2.iface.instruction.OffsetInstruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.ThreeRegisterInstruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import org.jf.dexlib2.iface.instruction.VerificationErrorInstruction;
import org.jf.dexlib2.iface.instruction.VtableIndexInstruction;
import org.jf.dexlib2.iface.instruction.WideLiteralInstruction;
import org.jf.dexlib2.iface.instruction.formats.ArrayPayload;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.util.AnnotatedBytes;
import org.jf.util.ExceptionWithContext;
import org.jf.util.NumberUtils;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/CodeItem.class */
public class CodeItem {
    public static final int REGISTERS_OFFSET = 0;
    public static final int INS_OFFSET = 2;
    public static final int OUTS_OFFSET = 4;
    public static final int TRIES_SIZE_OFFSET = 6;
    public static final int DEBUG_INFO_OFFSET = 8;
    public static final int INSTRUCTION_COUNT_OFFSET = 12;
    public static final int INSTRUCTION_START_OFFSET = 16;
    public static int CDEX_TRIES_SIZE_SHIFT = 0;
    public static int CDEX_OUTS_COUNT_SHIFT = 4;
    public static int CDEX_INS_COUNT_SHIFT = 8;
    public static int CDEX_REGISTER_COUNT_SHIFT = 12;
    public static int CDEX_INSTRUCTIONS_SIZE_AND_PREHEADER_FLAGS_OFFSET = 2;
    public static int CDEX_INSTRUCTIONS_SIZE_SHIFT = 5;
    public static int CDEX_PREHEADER_FLAGS_MASK = 31;
    public static int CDEX_PREHEADER_FLAG_REGISTER_COUNT = 1;
    public static int CDEX_PREHEADER_FLAG_INS_COUNT = 2;
    public static int CDEX_PREHEADER_FLAG_OUTS_COUNT = 4;
    public static int CDEX_PREHEADER_FLAG_TRIES_COUNT = 8;
    public static int CDEX_PREHEADER_FLAG_INSTRUCTIONS_SIZE = 16;

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/CodeItem$TryItem.class */
    public static class TryItem {
        public static final int ITEM_SIZE = 8;
        public static final int START_ADDRESS_OFFSET = 0;
        public static final int CODE_UNIT_COUNT_OFFSET = 4;
        public static final int HANDLER_OFFSET = 6;
    }

    public static SectionAnnotator makeAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        if (annotator.dexFile instanceof CDexBackedDexFile) {
            return makeAnnotatorForCDex(annotator, mapItem);
        }
        return makeAnnotatorForDex(annotator, mapItem);
    }

    @Nonnull
    private static SectionAnnotator makeAnnotatorForDex(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new CodeItemAnnotator(annotator, mapItem);
    }

    @Nonnull
    private static SectionAnnotator makeAnnotatorForCDex(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
        return new CodeItemAnnotator(annotator, mapItem) { // from class: org.jf.dexlib2.dexbacked.raw.CodeItem.1
            private List<Integer> sortedItems;

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            public void annotateSection(@Nonnull AnnotatedBytes out) {
                this.sortedItems = new ArrayList(this.itemIdentities.keySet());
                this.sortedItems.sort((v0, v1) -> {
                    return v0.compareTo(v1);
                });
                out.moveTo(this.sectionOffset);
                annotateSectionInner(out, this.itemIdentities.size());
            }

            @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
            protected int getItemOffset(int itemIndex, int currentOffset) {
                return this.sortedItems.get(itemIndex).intValue();
            }

            @Override // org.jf.dexlib2.dexbacked.raw.CodeItem.CodeItemAnnotator
            protected CodeItemAnnotator.PreInstructionInfo annotatePreInstructionFields(@Nonnull AnnotatedBytes out, @Nonnull DexReader reader, @Nullable String itemIdentity) {
                int sizeFields = reader.readUshort();
                int triesCount = (sizeFields >> CodeItem.CDEX_TRIES_SIZE_SHIFT) & 15;
                int outsCount = (sizeFields >> CodeItem.CDEX_OUTS_COUNT_SHIFT) & 15;
                int insCount = (sizeFields >> CodeItem.CDEX_INS_COUNT_SHIFT) & 15;
                int registerCount = (sizeFields >> CodeItem.CDEX_REGISTER_COUNT_SHIFT) & 15;
                int startOffset = out.getCursor();
                out.annotate(2, "tries_size = %d", Integer.valueOf(triesCount));
                out.annotate(0, "outs_size = %d", Integer.valueOf(outsCount));
                out.annotate(0, "ins_size = %d", Integer.valueOf(insCount));
                out.annotate(0, "registers_size = %d", Integer.valueOf(registerCount));
                int instructionsSizeAndPreheaderFlags = reader.readUshort();
                int instructionsSize = instructionsSizeAndPreheaderFlags >> CodeItem.CDEX_INSTRUCTIONS_SIZE_SHIFT;
                out.annotate(2, "insns_size = %d", Integer.valueOf(instructionsSize));
                int instructionsStartOffset = out.getCursor();
                int preheaderOffset = startOffset;
                int totalTriesCount = triesCount;
                int totalInstructionsSize = instructionsSize;
                if ((instructionsSizeAndPreheaderFlags & CodeItem.CDEX_PREHEADER_FLAGS_MASK) != 0) {
                    int preheaderCount = Integer.bitCount(instructionsSizeAndPreheaderFlags & CodeItem.CDEX_PREHEADER_FLAGS_MASK);
                    if ((instructionsSizeAndPreheaderFlags & CodeItem.CDEX_PREHEADER_FLAG_INSTRUCTIONS_SIZE) != 0) {
                        preheaderCount++;
                    }
                    out.moveTo(startOffset - (2 * preheaderCount));
                    out.deindent();
                    out.annotate(0, "[preheader for next code_item]", new Object[0]);
                    out.indent();
                    out.moveTo(instructionsStartOffset);
                }
                if ((instructionsSizeAndPreheaderFlags & CodeItem.CDEX_PREHEADER_FLAG_INSTRUCTIONS_SIZE) != 0) {
                    out.annotate(0, "insns_size_preheader_flag=1", new Object[0]);
                    int preheaderOffset2 = preheaderOffset - 2;
                    reader.setOffset(preheaderOffset2);
                    int extraInstructionsSize = reader.readUshort();
                    preheaderOffset = preheaderOffset2 - 2;
                    reader.setOffset(preheaderOffset);
                    int extraInstructionsSize2 = extraInstructionsSize + reader.readUshort();
                    out.moveTo(preheaderOffset);
                    totalInstructionsSize += extraInstructionsSize2;
                    out.annotate(2, "insns_size = %d + %d = %d", Integer.valueOf(instructionsSize), Integer.valueOf(extraInstructionsSize2), Integer.valueOf(instructionsSize + extraInstructionsSize2));
                    out.moveTo(instructionsStartOffset);
                }
                if ((instructionsSizeAndPreheaderFlags & CodeItem.CDEX_PREHEADER_FLAG_REGISTER_COUNT) != 0) {
                    out.annotate(0, "registers_size_preheader_flag=1", new Object[0]);
                    preheaderOffset -= 2;
                    out.moveTo(preheaderOffset);
                    reader.setOffset(preheaderOffset);
                    int extraRegisterCount = reader.readUshort();
                    out.annotate(2, "registers_size = %d + %d = %d", Integer.valueOf(registerCount), Integer.valueOf(extraRegisterCount), Integer.valueOf(registerCount + extraRegisterCount));
                    out.moveTo(instructionsStartOffset);
                }
                if ((instructionsSizeAndPreheaderFlags & CodeItem.CDEX_PREHEADER_FLAG_INS_COUNT) != 0) {
                    out.annotate(0, "ins_size_preheader_flag=1", new Object[0]);
                    preheaderOffset -= 2;
                    out.moveTo(preheaderOffset);
                    reader.setOffset(preheaderOffset);
                    int extraInsCount = reader.readUshort();
                    out.annotate(2, "ins_size = %d + %d = %d", Integer.valueOf(insCount), Integer.valueOf(extraInsCount), Integer.valueOf(insCount + extraInsCount));
                    out.moveTo(instructionsStartOffset);
                }
                if ((instructionsSizeAndPreheaderFlags & CodeItem.CDEX_PREHEADER_FLAG_OUTS_COUNT) != 0) {
                    out.annotate(0, "outs_size_preheader_flag=1", new Object[0]);
                    preheaderOffset -= 2;
                    out.moveTo(preheaderOffset);
                    reader.setOffset(preheaderOffset);
                    int extraOutsCount = reader.readUshort();
                    out.annotate(2, "outs_size = %d + %d = %d", Integer.valueOf(outsCount), Integer.valueOf(extraOutsCount), Integer.valueOf(outsCount + extraOutsCount));
                    out.moveTo(instructionsStartOffset);
                }
                if ((instructionsSizeAndPreheaderFlags & CodeItem.CDEX_PREHEADER_FLAG_TRIES_COUNT) != 0) {
                    out.annotate(0, "tries_size_preheader_flag=1", new Object[0]);
                    int preheaderOffset3 = preheaderOffset - 2;
                    out.moveTo(preheaderOffset3);
                    reader.setOffset(preheaderOffset3);
                    int extraTriesCount = reader.readUshort();
                    totalTriesCount += extraTriesCount;
                    out.annotate(2, "tries_size = %d + %d = %d", Integer.valueOf(triesCount), Integer.valueOf(extraTriesCount), Integer.valueOf(triesCount + extraTriesCount));
                    out.moveTo(instructionsStartOffset);
                }
                reader.setOffset(instructionsStartOffset);
                return new CodeItemAnnotator.PreInstructionInfo(totalTriesCount, totalInstructionsSize);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/CodeItem$CodeItemAnnotator.class */
    public static class CodeItemAnnotator extends SectionAnnotator {
        private SectionAnnotator debugInfoAnnotator;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !CodeItem.class.desiredAssertionStatus();
        }

        public CodeItemAnnotator(@Nonnull DexAnnotator annotator, @Nonnull MapItem mapItem) {
            super(annotator, mapItem);
        }

        @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
        @Nonnull
        public String getItemName() {
            return "code_item";
        }

        @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
        public int getItemAlignment() {
            return 4;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/raw/CodeItem$CodeItemAnnotator$PreInstructionInfo.class */
        public class PreInstructionInfo {
            public int triesCount;
            public int instructionSize;

            public PreInstructionInfo(int triesCount, int instructionSize) {
                this.triesCount = triesCount;
                this.instructionSize = instructionSize;
            }
        }

        protected PreInstructionInfo annotatePreInstructionFields(@Nonnull AnnotatedBytes out, @Nonnull DexReader reader, @Nullable String itemIdentity) {
            int registers = reader.readUshort();
            out.annotate(2, "registers_size = %d", Integer.valueOf(registers));
            int inSize = reader.readUshort();
            out.annotate(2, "ins_size = %d", Integer.valueOf(inSize));
            int outSize = reader.readUshort();
            out.annotate(2, "outs_size = %d", Integer.valueOf(outSize));
            int triesCount = reader.readUshort();
            out.annotate(2, "tries_size = %d", Integer.valueOf(triesCount));
            int debugInfoOffset = reader.readInt();
            out.annotate(4, "debug_info_off = 0x%x", Integer.valueOf(debugInfoOffset));
            if (debugInfoOffset > 0) {
                addDebugInfoIdentity(debugInfoOffset, itemIdentity);
            }
            int instructionSize = reader.readSmallUint();
            out.annotate(4, "insns_size = 0x%x", Integer.valueOf(instructionSize));
            return new PreInstructionInfo(triesCount, instructionSize);
        }

        protected void annotateInstructions(@Nonnull AnnotatedBytes out, @Nonnull DexReader reader, int instructionSize) {
            out.annotate(0, "instructions:", new Object[0]);
            out.indent();
            out.setLimit(out.getCursor(), out.getCursor() + (instructionSize * 2));
            int end = reader.getOffset() + (instructionSize * 2);
            while (reader.getOffset() < end) {
                try {
                    try {
                        Instruction instruction = DexBackedInstruction.readFrom(this.dexFile, reader);
                        if (reader.getOffset() > end) {
                            out.annotateTo(end, "truncated instruction", new Object[0]);
                            reader.setOffset(end);
                        } else {
                            switch (instruction.getOpcode().format) {
                                case Format10x:
                                    annotateInstruction10x(out, instruction);
                                    break;
                                case Format35c:
                                    annotateInstruction35c(out, (Instruction35c) instruction);
                                    break;
                                case Format3rc:
                                    annotateInstruction3rc(out, (Instruction3rc) instruction);
                                    break;
                                case ArrayPayload:
                                    annotateArrayPayload(out, (ArrayPayload) instruction);
                                    break;
                                case PackedSwitchPayload:
                                    annotatePackedSwitchPayload(out, (PackedSwitchPayload) instruction);
                                    break;
                                case SparseSwitchPayload:
                                    annotateSparseSwitchPayload(out, (SparseSwitchPayload) instruction);
                                    break;
                                default:
                                    annotateDefaultInstruction(out, instruction);
                                    break;
                            }
                        }
                        if (!$assertionsDisabled && reader.getOffset() != out.getCursor()) {
                            throw new AssertionError();
                        }
                    } catch (ExceptionWithContext ex) {
                        ex.printStackTrace(System.err);
                        out.annotate(0, "annotation error: %s", ex.getMessage());
                        out.moveTo(end);
                        reader.setOffset(end);
                        out.clearLimit();
                        out.deindent();
                        return;
                    }
                } finally {
                    out.clearLimit();
                    out.deindent();
                }
            }
        }

        protected void annotatePostInstructionFields(@Nonnull AnnotatedBytes out, @Nonnull DexReader reader, int triesCount) {
            if (triesCount > 0) {
                if (reader.getOffset() % 4 != 0) {
                    reader.readUshort();
                    out.annotate(2, "padding", new Object[0]);
                }
                out.annotate(0, "try_items:", new Object[0]);
                out.indent();
                for (int i = 0; i < triesCount; i++) {
                    try {
                        out.annotate(0, "try_item[%d]:", Integer.valueOf(i));
                        out.indent();
                        int startAddr = reader.readSmallUint();
                        out.annotate(4, "start_addr = 0x%x", Integer.valueOf(startAddr));
                        int instructionCount = reader.readUshort();
                        out.annotate(2, "insn_count = 0x%x", Integer.valueOf(instructionCount));
                        int handlerOffset = reader.readUshort();
                        out.annotate(2, "handler_off = 0x%x", Integer.valueOf(handlerOffset));
                        out.deindent();
                    } finally {
                    }
                }
                out.deindent();
                int handlerListCount = reader.readSmallUleb128();
                out.annotate(0, "encoded_catch_handler_list:", new Object[0]);
                out.annotateTo(reader.getOffset(), "size = %d", Integer.valueOf(handlerListCount));
                out.indent();
                for (int i2 = 0; i2 < handlerListCount; i2++) {
                    try {
                        out.annotate(0, "encoded_catch_handler[%d]", Integer.valueOf(i2));
                        out.indent();
                        int handlerCount = reader.readSleb128();
                        out.annotateTo(reader.getOffset(), "size = %d", Integer.valueOf(handlerCount));
                        boolean hasCatchAll = handlerCount <= 0;
                        int handlerCount2 = Math.abs(handlerCount);
                        if (handlerCount2 != 0) {
                            out.annotate(0, "handlers:", new Object[0]);
                            out.indent();
                            for (int j = 0; j < handlerCount2; j++) {
                                try {
                                    out.annotate(0, "encoded_type_addr_pair[%d]", Integer.valueOf(i2));
                                    out.indent();
                                    try {
                                        int typeIndex = reader.readSmallUleb128();
                                        out.annotateTo(reader.getOffset(), TypeIdItem.getReferenceAnnotation(this.dexFile, typeIndex), new Object[0]);
                                        int handlerAddress = reader.readSmallUleb128();
                                        out.annotateTo(reader.getOffset(), "addr = 0x%x", Integer.valueOf(handlerAddress));
                                        out.deindent();
                                    } catch (Throwable th) {
                                        out.deindent();
                                        throw th;
                                    }
                                } finally {
                                }
                            }
                            out.deindent();
                        }
                        if (hasCatchAll) {
                            int catchAllAddress = reader.readSmallUleb128();
                            out.annotateTo(reader.getOffset(), "catch_all_addr = 0x%x", Integer.valueOf(catchAllAddress));
                        }
                        out.deindent();
                    } finally {
                        out.deindent();
                    }
                }
                out.deindent();
            }
        }

        @Override // org.jf.dexlib2.dexbacked.raw.SectionAnnotator
        public void annotateItem(@Nonnull AnnotatedBytes out, int itemIndex, @Nullable String itemIdentity) {
            try {
                DexReader reader = this.dexFile.getBuffer().readerAt(out.getCursor());
                PreInstructionInfo info = annotatePreInstructionFields(out, reader, itemIdentity);
                annotateInstructions(out, reader, info.instructionSize);
                annotatePostInstructionFields(out, reader, info.triesCount);
            } catch (ExceptionWithContext ex) {
                out.annotate(0, "annotation error: %s", ex.getMessage());
            }
        }

        private String formatRegister(int registerNum) {
            return String.format("v%d", Integer.valueOf(registerNum));
        }

        private void annotateInstruction10x(@Nonnull AnnotatedBytes out, @Nonnull Instruction instruction) {
            out.annotate(2, instruction.getOpcode().name, new Object[0]);
        }

        private void annotateInstruction35c(@Nonnull AnnotatedBytes out, @Nonnull Instruction35c instruction) {
            List<String> args = Lists.newArrayList();
            int registerCount = instruction.getRegisterCount();
            if (registerCount == 1) {
                args.add(formatRegister(instruction.getRegisterC()));
            } else if (registerCount == 2) {
                args.add(formatRegister(instruction.getRegisterC()));
                args.add(formatRegister(instruction.getRegisterD()));
            } else if (registerCount == 3) {
                args.add(formatRegister(instruction.getRegisterC()));
                args.add(formatRegister(instruction.getRegisterD()));
                args.add(formatRegister(instruction.getRegisterE()));
            } else if (registerCount == 4) {
                args.add(formatRegister(instruction.getRegisterC()));
                args.add(formatRegister(instruction.getRegisterD()));
                args.add(formatRegister(instruction.getRegisterE()));
                args.add(formatRegister(instruction.getRegisterF()));
            } else if (registerCount == 5) {
                args.add(formatRegister(instruction.getRegisterC()));
                args.add(formatRegister(instruction.getRegisterD()));
                args.add(formatRegister(instruction.getRegisterE()));
                args.add(formatRegister(instruction.getRegisterF()));
                args.add(formatRegister(instruction.getRegisterG()));
            }
            out.annotate(6, String.format("%s {%s}, %s", instruction.getOpcode().name, Joiner.on(", ").join(args), instruction.getReference()), new Object[0]);
        }

        private void annotateInstruction3rc(@Nonnull AnnotatedBytes out, @Nonnull Instruction3rc instruction) {
            int startRegister = instruction.getStartRegister();
            int endRegister = (startRegister + instruction.getRegisterCount()) - 1;
            out.annotate(6, String.format("%s {%s .. %s}, %s", instruction.getOpcode().name, formatRegister(startRegister), formatRegister(endRegister), instruction.getReference()), new Object[0]);
        }

        private void annotateDefaultInstruction(@Nonnull AnnotatedBytes out, @Nonnull Instruction instruction) {
            String referenceString;
            List<String> args = Lists.newArrayList();
            if (instruction instanceof OneRegisterInstruction) {
                args.add(formatRegister(((OneRegisterInstruction) instruction).getRegisterA()));
                if (instruction instanceof TwoRegisterInstruction) {
                    args.add(formatRegister(((TwoRegisterInstruction) instruction).getRegisterB()));
                    if (instruction instanceof ThreeRegisterInstruction) {
                        args.add(formatRegister(((ThreeRegisterInstruction) instruction).getRegisterC()));
                    }
                }
            } else if (instruction instanceof VerificationErrorInstruction) {
                String verificationError = VerificationError.getVerificationErrorName(((VerificationErrorInstruction) instruction).getVerificationError());
                if (verificationError != null) {
                    args.add(verificationError);
                } else {
                    args.add("invalid verification error type");
                }
            }
            if (instruction instanceof ReferenceInstruction) {
                ReferenceInstruction referenceInstruction = (ReferenceInstruction) instruction;
                Reference reference = ((ReferenceInstruction) instruction).getReference();
                if (referenceInstruction.getReferenceType() == 0) {
                    referenceString = DexFormatter.INSTANCE.getQuotedString((StringReference) reference);
                } else {
                    referenceString = referenceInstruction.getReference().toString();
                }
                args.add(referenceString);
            } else if (instruction instanceof OffsetInstruction) {
                int offset = ((OffsetInstruction) instruction).getCodeOffset();
                String sign = offset >= 0 ? "+" : HelpFormatter.DEFAULT_OPT_PREFIX;
                args.add(String.format("%s0x%x", sign, Integer.valueOf(Math.abs(offset))));
            } else if (instruction instanceof NarrowLiteralInstruction) {
                int value = ((NarrowLiteralInstruction) instruction).getNarrowLiteral();
                if (NumberUtils.isLikelyFloat(value)) {
                    args.add(String.format("%d # %f", Integer.valueOf(value), Float.valueOf(Float.intBitsToFloat(value))));
                } else {
                    args.add(String.format("%d", Integer.valueOf(value)));
                }
            } else if (instruction instanceof WideLiteralInstruction) {
                long value2 = ((WideLiteralInstruction) instruction).getWideLiteral();
                if (NumberUtils.isLikelyDouble(value2)) {
                    args.add(String.format("%d # %f", Long.valueOf(value2), Double.valueOf(Double.longBitsToDouble(value2))));
                } else {
                    args.add(String.format("%d", Long.valueOf(value2)));
                }
            } else if (instruction instanceof FieldOffsetInstruction) {
                int fieldOffset = ((FieldOffsetInstruction) instruction).getFieldOffset();
                args.add(String.format("field@0x%x", Integer.valueOf(fieldOffset)));
            } else if (instruction instanceof VtableIndexInstruction) {
                int vtableIndex = ((VtableIndexInstruction) instruction).getVtableIndex();
                args.add(String.format("vtable@%d", Integer.valueOf(vtableIndex)));
            } else if (instruction instanceof InlineIndexInstruction) {
                int inlineIndex = ((InlineIndexInstruction) instruction).getInlineIndex();
                args.add(String.format("inline@%d", Integer.valueOf(inlineIndex)));
            }
            out.annotate(instruction.getCodeUnits() * 2, "%s %s", instruction.getOpcode().name, Joiner.on(", ").join(args));
        }

        private void annotateArrayPayload(@Nonnull AnnotatedBytes out, @Nonnull ArrayPayload instruction) {
            List<Number> elements = instruction.getArrayElements();
            int elementWidth = instruction.getElementWidth();
            out.annotate(2, instruction.getOpcode().name, new Object[0]);
            out.indent();
            out.annotate(2, "element_width = %d", Integer.valueOf(elementWidth));
            out.annotate(4, "size = %d", Integer.valueOf(elements.size()));
            if (elements.size() > 0) {
                out.annotate(0, "elements:", new Object[0]);
            }
            out.indent();
            if (elements.size() > 0) {
                for (int i = 0; i < elements.size(); i++) {
                    if (elementWidth == 8) {
                        long value = elements.get(i).longValue();
                        if (NumberUtils.isLikelyDouble(value)) {
                            out.annotate(elementWidth, "element[%d] = %d # %f", Integer.valueOf(i), Long.valueOf(value), Double.valueOf(Double.longBitsToDouble(value)));
                        } else {
                            out.annotate(elementWidth, "element[%d] = %d", Integer.valueOf(i), Long.valueOf(value));
                        }
                    } else {
                        int value2 = elements.get(i).intValue();
                        if (NumberUtils.isLikelyFloat(value2)) {
                            out.annotate(elementWidth, "element[%d] = %d # %f", Integer.valueOf(i), Integer.valueOf(value2), Float.valueOf(Float.intBitsToFloat(value2)));
                        } else {
                            out.annotate(elementWidth, "element[%d] = %d", Integer.valueOf(i), Integer.valueOf(value2));
                        }
                    }
                }
            }
            if (out.getCursor() % 2 != 0) {
                out.annotate(1, "padding", new Object[0]);
            }
            out.deindent();
            out.deindent();
        }

        private void annotatePackedSwitchPayload(@Nonnull AnnotatedBytes out, @Nonnull PackedSwitchPayload instruction) {
            List<? extends SwitchElement> elements = instruction.getSwitchElements();
            out.annotate(2, instruction.getOpcode().name, new Object[0]);
            out.indent();
            out.annotate(2, "size = %d", Integer.valueOf(elements.size()));
            if (elements.size() == 0) {
                out.annotate(4, "first_key", new Object[0]);
            } else {
                out.annotate(4, "first_key = %d", Integer.valueOf(elements.get(0).getKey()));
                out.annotate(0, "targets:", new Object[0]);
                out.indent();
                for (int i = 0; i < elements.size(); i++) {
                    out.annotate(4, "target[%d] = %d", Integer.valueOf(i), Integer.valueOf(elements.get(i).getOffset()));
                }
                out.deindent();
            }
            out.deindent();
        }

        private void annotateSparseSwitchPayload(@Nonnull AnnotatedBytes out, @Nonnull SparseSwitchPayload instruction) {
            List<? extends SwitchElement> elements = instruction.getSwitchElements();
            out.annotate(2, instruction.getOpcode().name, new Object[0]);
            out.indent();
            out.annotate(2, "size = %d", Integer.valueOf(elements.size()));
            if (elements.size() > 0) {
                out.annotate(0, "keys:", new Object[0]);
                out.indent();
                for (int i = 0; i < elements.size(); i++) {
                    out.annotate(4, "key[%d] = %d", Integer.valueOf(i), Integer.valueOf(elements.get(i).getKey()));
                }
                out.deindent();
                out.annotate(0, "targets:", new Object[0]);
                out.indent();
                for (int i2 = 0; i2 < elements.size(); i2++) {
                    out.annotate(4, "target[%d] = %d", Integer.valueOf(i2), Integer.valueOf(elements.get(i2).getOffset()));
                }
                out.deindent();
            }
            out.deindent();
        }

        private void addDebugInfoIdentity(int debugInfoOffset, String methodString) {
            if (this.debugInfoAnnotator != null) {
                this.debugInfoAnnotator.setItemIdentity(debugInfoOffset, methodString);
            }
        }
    }
}
