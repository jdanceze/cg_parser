package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction20bc;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.instruction.formats.Instruction22c;
import org.jf.dexlib2.iface.instruction.formats.Instruction31c;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/InstructionRewriter.class */
public class InstructionRewriter implements Rewriter<Instruction> {
    @Nonnull
    protected final Rewriters rewriters;

    public InstructionRewriter(@Nonnull Rewriters rewriters) {
        this.rewriters = rewriters;
    }

    @Override // org.jf.dexlib2.rewriter.Rewriter
    @Nonnull
    public Instruction rewrite(@Nonnull Instruction instruction) {
        if (instruction instanceof ReferenceInstruction) {
            switch (instruction.getOpcode().format) {
                case Format20bc:
                    return new RewrittenInstruction20bc((Instruction20bc) instruction);
                case Format21c:
                    return new RewrittenInstruction21c((Instruction21c) instruction);
                case Format22c:
                    return new RewrittenInstruction22c((Instruction22c) instruction);
                case Format31c:
                    return new RewrittenInstruction31c((Instruction31c) instruction);
                case Format35c:
                    return new RewrittenInstruction35c((Instruction35c) instruction);
                case Format3rc:
                    return new RewrittenInstruction3rc((Instruction3rc) instruction);
                default:
                    throw new IllegalArgumentException();
            }
        }
        return instruction;
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/InstructionRewriter$BaseRewrittenReferenceInstruction.class */
    protected class BaseRewrittenReferenceInstruction<T extends ReferenceInstruction> implements ReferenceInstruction {
        @Nonnull
        protected T instruction;

        protected BaseRewrittenReferenceInstruction(@Nonnull T instruction) {
            this.instruction = instruction;
        }

        @Override // org.jf.dexlib2.iface.instruction.ReferenceInstruction
        @Nonnull
        public Reference getReference() {
            switch (this.instruction.getReferenceType()) {
                case 0:
                    return this.instruction.getReference();
                case 1:
                    return RewriterUtils.rewriteTypeReference(InstructionRewriter.this.rewriters.getTypeRewriter(), (TypeReference) this.instruction.getReference());
                case 2:
                    return InstructionRewriter.this.rewriters.getFieldReferenceRewriter().rewrite((FieldReference) this.instruction.getReference());
                case 3:
                    return InstructionRewriter.this.rewriters.getMethodReferenceRewriter().rewrite((MethodReference) this.instruction.getReference());
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override // org.jf.dexlib2.iface.instruction.ReferenceInstruction
        public int getReferenceType() {
            return this.instruction.getReferenceType();
        }

        @Override // org.jf.dexlib2.iface.instruction.Instruction
        public Opcode getOpcode() {
            return this.instruction.getOpcode();
        }

        @Override // org.jf.dexlib2.iface.instruction.Instruction
        public int getCodeUnits() {
            return this.instruction.getCodeUnits();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/InstructionRewriter$RewrittenInstruction20bc.class */
    public class RewrittenInstruction20bc extends BaseRewrittenReferenceInstruction<Instruction20bc> implements Instruction20bc {
        public RewrittenInstruction20bc(@Nonnull Instruction20bc instruction) {
            super(instruction);
        }

        @Override // org.jf.dexlib2.iface.instruction.VerificationErrorInstruction
        public int getVerificationError() {
            return ((Instruction20bc) this.instruction).getVerificationError();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/InstructionRewriter$RewrittenInstruction21c.class */
    public class RewrittenInstruction21c extends BaseRewrittenReferenceInstruction<Instruction21c> implements Instruction21c {
        public RewrittenInstruction21c(@Nonnull Instruction21c instruction) {
            super(instruction);
        }

        @Override // org.jf.dexlib2.iface.instruction.OneRegisterInstruction
        public int getRegisterA() {
            return ((Instruction21c) this.instruction).getRegisterA();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/InstructionRewriter$RewrittenInstruction22c.class */
    public class RewrittenInstruction22c extends BaseRewrittenReferenceInstruction<Instruction22c> implements Instruction22c {
        public RewrittenInstruction22c(@Nonnull Instruction22c instruction) {
            super(instruction);
        }

        @Override // org.jf.dexlib2.iface.instruction.OneRegisterInstruction
        public int getRegisterA() {
            return ((Instruction22c) this.instruction).getRegisterA();
        }

        @Override // org.jf.dexlib2.iface.instruction.TwoRegisterInstruction
        public int getRegisterB() {
            return ((Instruction22c) this.instruction).getRegisterB();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/InstructionRewriter$RewrittenInstruction31c.class */
    public class RewrittenInstruction31c extends BaseRewrittenReferenceInstruction<Instruction31c> implements Instruction31c {
        public RewrittenInstruction31c(@Nonnull Instruction31c instruction) {
            super(instruction);
        }

        @Override // org.jf.dexlib2.iface.instruction.OneRegisterInstruction
        public int getRegisterA() {
            return ((Instruction31c) this.instruction).getRegisterA();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/InstructionRewriter$RewrittenInstruction35c.class */
    public class RewrittenInstruction35c extends BaseRewrittenReferenceInstruction<Instruction35c> implements Instruction35c {
        public RewrittenInstruction35c(@Nonnull Instruction35c instruction) {
            super(instruction);
        }

        @Override // org.jf.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterC() {
            return ((Instruction35c) this.instruction).getRegisterC();
        }

        @Override // org.jf.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterE() {
            return ((Instruction35c) this.instruction).getRegisterE();
        }

        @Override // org.jf.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterG() {
            return ((Instruction35c) this.instruction).getRegisterG();
        }

        @Override // org.jf.dexlib2.iface.instruction.VariableRegisterInstruction
        public int getRegisterCount() {
            return ((Instruction35c) this.instruction).getRegisterCount();
        }

        @Override // org.jf.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterD() {
            return ((Instruction35c) this.instruction).getRegisterD();
        }

        @Override // org.jf.dexlib2.iface.instruction.FiveRegisterInstruction
        public int getRegisterF() {
            return ((Instruction35c) this.instruction).getRegisterF();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/rewriter/InstructionRewriter$RewrittenInstruction3rc.class */
    public class RewrittenInstruction3rc extends BaseRewrittenReferenceInstruction<Instruction3rc> implements Instruction3rc {
        public RewrittenInstruction3rc(@Nonnull Instruction3rc instruction) {
            super(instruction);
        }

        @Override // org.jf.dexlib2.iface.instruction.RegisterRangeInstruction
        public int getStartRegister() {
            return ((Instruction3rc) this.instruction).getStartRegister();
        }

        @Override // org.jf.dexlib2.iface.instruction.VariableRegisterInstruction
        public int getRegisterCount() {
            return ((Instruction3rc) this.instruction).getRegisterCount();
        }
    }
}
