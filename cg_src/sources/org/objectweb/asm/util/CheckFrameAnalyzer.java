package org.objectweb.asm.util;

import java.util.Iterator;
import java.util.List;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Interpreter;
import org.objectweb.asm.tree.analysis.Value;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/CheckFrameAnalyzer.class */
class CheckFrameAnalyzer<V extends Value> extends Analyzer<V> {
    private final Interpreter<V> interpreter;
    private InsnList insnList;
    private int currentLocals;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CheckFrameAnalyzer(Interpreter<V> interpreter) {
        super(interpreter);
        this.interpreter = interpreter;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.objectweb.asm.tree.analysis.Analyzer
    protected void init(String owner, MethodNode method) throws AnalyzerException {
        Type catchType;
        this.insnList = method.instructions;
        this.currentLocals = Type.getArgumentsAndReturnSizes(method.desc) >> 2;
        Frame[] frames = getFrames();
        Frame frame = frames[0];
        expandFrames(owner, method, frame);
        for (int insnIndex = 0; insnIndex < this.insnList.size(); insnIndex++) {
            Frame frame2 = frames[insnIndex];
            try {
                AbstractInsnNode insnNode = method.instructions.get(insnIndex);
                int insnOpcode = insnNode.getOpcode();
                int insnType = insnNode.getType();
                if (insnType == 8 || insnType == 15 || insnType == 14) {
                    checkFrame(insnIndex + 1, frame2, false);
                } else {
                    frame.init(frame2).execute(insnNode, this.interpreter);
                    if (insnNode instanceof JumpInsnNode) {
                        if (insnOpcode == 168) {
                            throw new AnalyzerException(insnNode, "JSR instructions are unsupported");
                        }
                        JumpInsnNode jumpInsn = (JumpInsnNode) insnNode;
                        checkFrame(this.insnList.indexOf(jumpInsn.label), frame, true);
                        if (insnOpcode == 167) {
                            endControlFlow(insnIndex);
                        } else {
                            checkFrame(insnIndex + 1, frame, false);
                        }
                    } else if (insnNode instanceof LookupSwitchInsnNode) {
                        LookupSwitchInsnNode lookupSwitchInsn = (LookupSwitchInsnNode) insnNode;
                        checkFrame(this.insnList.indexOf(lookupSwitchInsn.dflt), frame, true);
                        for (int i = 0; i < lookupSwitchInsn.labels.size(); i++) {
                            LabelNode label = lookupSwitchInsn.labels.get(i);
                            int targetInsnIndex = this.insnList.indexOf(label);
                            frame.initJumpTarget(insnOpcode, label);
                            checkFrame(targetInsnIndex, frame, true);
                        }
                        endControlFlow(insnIndex);
                    } else if (insnNode instanceof TableSwitchInsnNode) {
                        TableSwitchInsnNode tableSwitchInsn = (TableSwitchInsnNode) insnNode;
                        int targetInsnIndex2 = this.insnList.indexOf(tableSwitchInsn.dflt);
                        frame.initJumpTarget(insnOpcode, tableSwitchInsn.dflt);
                        checkFrame(targetInsnIndex2, frame, true);
                        newControlFlowEdge(insnIndex, targetInsnIndex2);
                        for (int i2 = 0; i2 < tableSwitchInsn.labels.size(); i2++) {
                            LabelNode label2 = tableSwitchInsn.labels.get(i2);
                            frame.initJumpTarget(insnOpcode, label2);
                            checkFrame(this.insnList.indexOf(label2), frame, true);
                        }
                        endControlFlow(insnIndex);
                    } else if (insnOpcode == 169) {
                        throw new AnalyzerException(insnNode, "RET instructions are unsupported");
                    } else {
                        if (insnOpcode != 191 && (insnOpcode < 172 || insnOpcode > 177)) {
                            checkFrame(insnIndex + 1, frame, false);
                        } else {
                            endControlFlow(insnIndex);
                        }
                    }
                }
                List<TryCatchBlockNode> insnHandlers = getHandlers(insnIndex);
                if (insnHandlers != null) {
                    for (TryCatchBlockNode tryCatchBlock : insnHandlers) {
                        if (tryCatchBlock.type == null) {
                            catchType = Type.getObjectType("java/lang/Throwable");
                        } else {
                            catchType = Type.getObjectType(tryCatchBlock.type);
                        }
                        Frame<V> handler = newFrame(frame2);
                        handler.clearStack();
                        handler.push(this.interpreter.newExceptionValue(tryCatchBlock, handler, catchType));
                        checkFrame(this.insnList.indexOf(tryCatchBlock.handler), handler, true);
                    }
                }
                if (!hasNextJvmInsnOrFrame(insnIndex)) {
                    return;
                }
            } catch (RuntimeException e) {
                throw new AnalyzerException(null, "Error at instruction " + insnIndex + ": " + e.getMessage(), e);
            } catch (AnalyzerException e2) {
                throw new AnalyzerException(e2.node, "Error at instruction " + insnIndex + ": " + e2.getMessage(), e2);
            }
        }
    }

    private void expandFrames(String owner, MethodNode method, Frame<V> initialFrame) throws AnalyzerException {
        int lastJvmOrFrameInsnIndex = -1;
        Frame<V> currentFrame = initialFrame;
        int currentInsnIndex = 0;
        Iterator<AbstractInsnNode> iterator2 = method.instructions.iterator2();
        while (iterator2.hasNext()) {
            AbstractInsnNode insnNode = iterator2.next();
            if (insnNode instanceof FrameNode) {
                try {
                    currentFrame = expandFrame(owner, currentFrame, (FrameNode) insnNode);
                    for (int index = lastJvmOrFrameInsnIndex + 1; index <= currentInsnIndex; index++) {
                        getFrames()[index] = currentFrame;
                    }
                } catch (AnalyzerException e) {
                    throw new AnalyzerException(e.node, "Error at instruction " + currentInsnIndex + ": " + e.getMessage(), e);
                }
            }
            if (isJvmInsnNode(insnNode) || (insnNode instanceof FrameNode)) {
                lastJvmOrFrameInsnIndex = currentInsnIndex;
            }
            currentInsnIndex++;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0149 A[LOOP:2: B:40:0x013f->B:42:0x0149, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0165  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x016b  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0189 A[LOOP:3: B:48:0x017f->B:50:0x0189, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private org.objectweb.asm.tree.analysis.Frame<V> expandFrame(java.lang.String r7, org.objectweb.asm.tree.analysis.Frame<V> r8, org.objectweb.asm.tree.FrameNode r9) throws org.objectweb.asm.tree.analysis.AnalyzerException {
        /*
            Method dump skipped, instructions count: 421
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.objectweb.asm.util.CheckFrameAnalyzer.expandFrame(java.lang.String, org.objectweb.asm.tree.analysis.Frame, org.objectweb.asm.tree.FrameNode):org.objectweb.asm.tree.analysis.Frame");
    }

    private V newFrameValue(String owner, FrameNode frameNode, Object type) throws AnalyzerException {
        AbstractInsnNode referencedNode;
        if (type == Opcodes.TOP) {
            return this.interpreter.newValue(null);
        }
        if (type == Opcodes.INTEGER) {
            return this.interpreter.newValue(Type.INT_TYPE);
        }
        if (type == Opcodes.FLOAT) {
            return this.interpreter.newValue(Type.FLOAT_TYPE);
        }
        if (type == Opcodes.LONG) {
            return this.interpreter.newValue(Type.LONG_TYPE);
        }
        if (type == Opcodes.DOUBLE) {
            return this.interpreter.newValue(Type.DOUBLE_TYPE);
        }
        if (type == Opcodes.NULL) {
            return this.interpreter.newOperation(new InsnNode(1));
        }
        if (type == Opcodes.UNINITIALIZED_THIS) {
            return this.interpreter.newValue(Type.getObjectType(owner));
        }
        if (type instanceof String) {
            return this.interpreter.newValue(Type.getObjectType((String) type));
        }
        if (type instanceof LabelNode) {
            AbstractInsnNode abstractInsnNode = (LabelNode) type;
            while (true) {
                referencedNode = abstractInsnNode;
                if (referencedNode == null || isJvmInsnNode(referencedNode)) {
                    break;
                }
                abstractInsnNode = referencedNode.getNext();
            }
            if (referencedNode == null || referencedNode.getOpcode() != 187) {
                throw new AnalyzerException(frameNode, "LabelNode does not designate a NEW instruction");
            }
            return this.interpreter.newValue(Type.getObjectType(((TypeInsnNode) referencedNode).desc));
        }
        throw new AnalyzerException(frameNode, "Illegal stack map frame value " + type);
    }

    private void checkFrame(int insnIndex, Frame<V> frame, boolean requireFrame) throws AnalyzerException {
        Frame<V> oldFrame = getFrames()[insnIndex];
        if (oldFrame == null) {
            if (requireFrame) {
                throw new AnalyzerException(null, "Expected stack map frame at instruction " + insnIndex);
            }
            getFrames()[insnIndex] = newFrame(frame);
            return;
        }
        String error = checkMerge(frame, oldFrame);
        if (error != null) {
            throw new AnalyzerException(null, "Stack map frame incompatible with frame at instruction " + insnIndex + " (" + error + ")");
        }
    }

    private String checkMerge(Frame<V> srcFrame, Frame<V> dstFrame) {
        int numLocals = srcFrame.getLocals();
        if (numLocals != dstFrame.getLocals()) {
            throw new AssertionError();
        }
        for (int i = 0; i < numLocals; i++) {
            V v = this.interpreter.merge(srcFrame.getLocal(i), dstFrame.getLocal(i));
            if (!v.equals(dstFrame.getLocal(i))) {
                return "incompatible types at local " + i + ": " + srcFrame.getLocal(i) + " and " + dstFrame.getLocal(i);
            }
        }
        int numStack = srcFrame.getStackSize();
        if (numStack != dstFrame.getStackSize()) {
            return "incompatible stack heights";
        }
        for (int i2 = 0; i2 < numStack; i2++) {
            V v2 = this.interpreter.merge(srcFrame.getStack(i2), dstFrame.getStack(i2));
            if (!v2.equals(dstFrame.getStack(i2))) {
                return "incompatible types at stack item " + i2 + ": " + srcFrame.getStack(i2) + " and " + dstFrame.getStack(i2);
            }
        }
        return null;
    }

    private void endControlFlow(int insnIndex) throws AnalyzerException {
        if (hasNextJvmInsnOrFrame(insnIndex) && getFrames()[insnIndex + 1] == null) {
            throw new AnalyzerException(null, "Expected stack map frame at instruction " + (insnIndex + 1));
        }
    }

    private boolean hasNextJvmInsnOrFrame(int insnIndex) {
        AbstractInsnNode next = this.insnList.get(insnIndex).getNext();
        while (true) {
            AbstractInsnNode insn = next;
            if (insn != null) {
                if (isJvmInsnNode(insn) || (insn instanceof FrameNode)) {
                    return true;
                }
                next = insn.getNext();
            } else {
                return false;
            }
        }
    }

    private static boolean isJvmInsnNode(AbstractInsnNode insnNode) {
        return insnNode.getOpcode() >= 0;
    }
}
