package org.jf.dexlib2.builder;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.debug.BuilderEndLocal;
import org.jf.dexlib2.builder.debug.BuilderEpilogueBegin;
import org.jf.dexlib2.builder.debug.BuilderLineNumber;
import org.jf.dexlib2.builder.debug.BuilderPrologueEnd;
import org.jf.dexlib2.builder.debug.BuilderRestartLocal;
import org.jf.dexlib2.builder.debug.BuilderSetSourceFile;
import org.jf.dexlib2.builder.debug.BuilderStartLocal;
import org.jf.dexlib2.builder.instruction.BuilderArrayPayload;
import org.jf.dexlib2.builder.instruction.BuilderInstruction10t;
import org.jf.dexlib2.builder.instruction.BuilderInstruction10x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction11n;
import org.jf.dexlib2.builder.instruction.BuilderInstruction11x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction12x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction20bc;
import org.jf.dexlib2.builder.instruction.BuilderInstruction20t;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21c;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21ih;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21lh;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21s;
import org.jf.dexlib2.builder.instruction.BuilderInstruction21t;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22b;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22c;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22cs;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22s;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22t;
import org.jf.dexlib2.builder.instruction.BuilderInstruction22x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction23x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction30t;
import org.jf.dexlib2.builder.instruction.BuilderInstruction31c;
import org.jf.dexlib2.builder.instruction.BuilderInstruction31i;
import org.jf.dexlib2.builder.instruction.BuilderInstruction31t;
import org.jf.dexlib2.builder.instruction.BuilderInstruction32x;
import org.jf.dexlib2.builder.instruction.BuilderInstruction35c;
import org.jf.dexlib2.builder.instruction.BuilderInstruction35mi;
import org.jf.dexlib2.builder.instruction.BuilderInstruction35ms;
import org.jf.dexlib2.builder.instruction.BuilderInstruction3rc;
import org.jf.dexlib2.builder.instruction.BuilderInstruction3rmi;
import org.jf.dexlib2.builder.instruction.BuilderInstruction3rms;
import org.jf.dexlib2.builder.instruction.BuilderInstruction51l;
import org.jf.dexlib2.builder.instruction.BuilderPackedSwitchPayload;
import org.jf.dexlib2.builder.instruction.BuilderSparseSwitchPayload;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.debug.EndLocal;
import org.jf.dexlib2.iface.debug.LineNumber;
import org.jf.dexlib2.iface.debug.RestartLocal;
import org.jf.dexlib2.iface.debug.SetSourceFile;
import org.jf.dexlib2.iface.debug.StartLocal;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.SwitchElement;
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
import org.jf.dexlib2.iface.instruction.formats.Instruction51l;
import org.jf.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/MutableMethodImplementation.class */
public class MutableMethodImplementation implements MethodImplementation {
    private final int registerCount;
    final ArrayList<MethodLocation> instructionList;
    private final ArrayList<BuilderTryBlock> tryBlocks;
    private boolean fixInstructions;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/MutableMethodImplementation$Task.class */
    private interface Task {
        void perform();
    }

    static {
        $assertionsDisabled = !MutableMethodImplementation.class.desiredAssertionStatus();
    }

    public MutableMethodImplementation(@Nonnull MethodImplementation methodImplementation) {
        this.instructionList = Lists.newArrayList(new MethodLocation(null, 0, 0));
        this.tryBlocks = Lists.newArrayList();
        this.fixInstructions = true;
        this.registerCount = methodImplementation.getRegisterCount();
        int codeAddress = 0;
        int index = 0;
        for (Instruction instruction : methodImplementation.getInstructions()) {
            codeAddress += instruction.getCodeUnits();
            index++;
            this.instructionList.add(new MethodLocation(null, codeAddress, index));
        }
        final int[] codeAddressToIndex = new int[codeAddress + 1];
        Arrays.fill(codeAddressToIndex, -1);
        for (int i = 0; i < this.instructionList.size(); i++) {
            codeAddressToIndex[this.instructionList.get(i).codeAddress] = i;
        }
        List<Task> switchPayloadTasks = Lists.newArrayList();
        int index2 = 0;
        for (final Instruction instruction2 : methodImplementation.getInstructions()) {
            final MethodLocation location = this.instructionList.get(index2);
            Opcode opcode = instruction2.getOpcode();
            if (opcode == Opcode.PACKED_SWITCH_PAYLOAD || opcode == Opcode.SPARSE_SWITCH_PAYLOAD) {
                switchPayloadTasks.add(new Task() { // from class: org.jf.dexlib2.builder.MutableMethodImplementation.1
                    @Override // org.jf.dexlib2.builder.MutableMethodImplementation.Task
                    public void perform() {
                        MutableMethodImplementation.this.convertAndSetInstruction(location, codeAddressToIndex, instruction2);
                    }
                });
            } else {
                convertAndSetInstruction(location, codeAddressToIndex, instruction2);
            }
            index2++;
        }
        for (Task switchPayloadTask : switchPayloadTasks) {
            switchPayloadTask.perform();
        }
        for (DebugItem debugItem : methodImplementation.getDebugItems()) {
            int debugCodeAddress = debugItem.getCodeAddress();
            int locationIndex = mapCodeAddressToIndex(codeAddressToIndex, debugCodeAddress);
            MethodLocation debugLocation = this.instructionList.get(locationIndex);
            BuilderDebugItem builderDebugItem = convertDebugItem(debugItem);
            debugLocation.getDebugItems().add(builderDebugItem);
            builderDebugItem.location = debugLocation;
        }
        for (TryBlock<? extends ExceptionHandler> tryBlock : methodImplementation.getTryBlocks()) {
            Label startLabel = newLabel(codeAddressToIndex, tryBlock.getStartCodeAddress());
            Label endLabel = newLabel(codeAddressToIndex, tryBlock.getStartCodeAddress() + tryBlock.getCodeUnitCount());
            Iterator<? extends Object> it = tryBlock.getExceptionHandlers().iterator();
            while (it.hasNext()) {
                ExceptionHandler exceptionHandler = (ExceptionHandler) it.next();
                this.tryBlocks.add(new BuilderTryBlock(startLabel, endLabel, exceptionHandler.getExceptionTypeReference(), newLabel(codeAddressToIndex, exceptionHandler.getHandlerCodeAddress())));
            }
        }
    }

    public MutableMethodImplementation(int registerCount) {
        this.instructionList = Lists.newArrayList(new MethodLocation(null, 0, 0));
        this.tryBlocks = Lists.newArrayList();
        this.fixInstructions = true;
        this.registerCount = registerCount;
    }

    @Override // org.jf.dexlib2.iface.MethodImplementation
    public int getRegisterCount() {
        return this.registerCount;
    }

    @Override // org.jf.dexlib2.iface.MethodImplementation
    @Nonnull
    public List<BuilderInstruction> getInstructions() {
        if (this.fixInstructions) {
            fixInstructions();
        }
        return new AbstractList<BuilderInstruction>() { // from class: org.jf.dexlib2.builder.MutableMethodImplementation.2
            @Override // java.util.AbstractList, java.util.List
            public BuilderInstruction get(int i) {
                if (i < size()) {
                    if (MutableMethodImplementation.this.fixInstructions) {
                        MutableMethodImplementation.this.fixInstructions();
                    }
                    return MutableMethodImplementation.this.instructionList.get(i).instruction;
                }
                throw new IndexOutOfBoundsException();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                if (MutableMethodImplementation.this.fixInstructions) {
                    MutableMethodImplementation.this.fixInstructions();
                }
                return MutableMethodImplementation.this.instructionList.size() - 1;
            }
        };
    }

    @Override // org.jf.dexlib2.iface.MethodImplementation
    @Nonnull
    public List<BuilderTryBlock> getTryBlocks() {
        if (this.fixInstructions) {
            fixInstructions();
        }
        return Collections.unmodifiableList(this.tryBlocks);
    }

    @Override // org.jf.dexlib2.iface.MethodImplementation
    @Nonnull
    public Iterable<? extends DebugItem> getDebugItems() {
        if (this.fixInstructions) {
            fixInstructions();
        }
        return Iterables.concat(Iterables.transform(this.instructionList, new Function<MethodLocation, Iterable<? extends DebugItem>>() { // from class: org.jf.dexlib2.builder.MutableMethodImplementation.3
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !MutableMethodImplementation.class.desiredAssertionStatus();
            }

            @Override // com.google.common.base.Function
            @Nullable
            public Iterable<? extends DebugItem> apply(@Nullable MethodLocation input) {
                if ($assertionsDisabled || input != null) {
                    if (MutableMethodImplementation.this.fixInstructions) {
                        throw new IllegalStateException("This iterator was invalidated by a change to this MutableMethodImplementation.");
                    }
                    return input.getDebugItems();
                }
                throw new AssertionError();
            }
        }));
    }

    public void addCatch(@Nullable TypeReference type, @Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
        this.tryBlocks.add(new BuilderTryBlock(from, to, type, handler));
    }

    public void addCatch(@Nullable String type, @Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
        this.tryBlocks.add(new BuilderTryBlock(from, to, type, handler));
    }

    public void addCatch(@Nonnull Label from, @Nonnull Label to, @Nonnull Label handler) {
        this.tryBlocks.add(new BuilderTryBlock(from, to, handler));
    }

    public void addInstruction(int index, BuilderInstruction instruction) {
        if (index >= this.instructionList.size()) {
            throw new IndexOutOfBoundsException();
        }
        if (index == this.instructionList.size() - 1) {
            addInstruction(instruction);
            return;
        }
        int codeAddress = this.instructionList.get(index).getCodeAddress();
        MethodLocation newLoc = new MethodLocation(instruction, codeAddress, index);
        this.instructionList.add(index, newLoc);
        instruction.location = newLoc;
        int codeAddress2 = codeAddress + instruction.getCodeUnits();
        for (int i = index + 1; i < this.instructionList.size(); i++) {
            MethodLocation location = this.instructionList.get(i);
            location.index++;
            location.codeAddress = codeAddress2;
            if (location.instruction != null) {
                codeAddress2 += location.instruction.getCodeUnits();
            } else if (!$assertionsDisabled && i != this.instructionList.size() - 1) {
                throw new AssertionError();
            }
        }
        this.fixInstructions = true;
    }

    public void addInstruction(@Nonnull BuilderInstruction instruction) {
        MethodLocation last = this.instructionList.get(this.instructionList.size() - 1);
        last.instruction = instruction;
        instruction.location = last;
        int nextCodeAddress = last.codeAddress + instruction.getCodeUnits();
        this.instructionList.add(new MethodLocation(null, nextCodeAddress, this.instructionList.size()));
        this.fixInstructions = true;
    }

    public void replaceInstruction(int index, @Nonnull BuilderInstruction replacementInstruction) {
        if (index >= this.instructionList.size() - 1) {
            throw new IndexOutOfBoundsException();
        }
        MethodLocation replaceLocation = this.instructionList.get(index);
        replacementInstruction.location = replaceLocation;
        BuilderInstruction old = replaceLocation.instruction;
        if (!$assertionsDisabled && old == null) {
            throw new AssertionError();
        }
        old.location = null;
        replaceLocation.instruction = replacementInstruction;
        int codeAddress = replaceLocation.codeAddress + replaceLocation.instruction.getCodeUnits();
        for (int i = index + 1; i < this.instructionList.size(); i++) {
            MethodLocation location = this.instructionList.get(i);
            location.codeAddress = codeAddress;
            Instruction instruction = location.getInstruction();
            if (instruction != null) {
                codeAddress += instruction.getCodeUnits();
            } else if (!$assertionsDisabled && i != this.instructionList.size() - 1) {
                throw new AssertionError();
            }
        }
        this.fixInstructions = true;
    }

    public void removeInstruction(int index) {
        if (index >= this.instructionList.size() - 1) {
            throw new IndexOutOfBoundsException();
        }
        MethodLocation toRemove = this.instructionList.get(index);
        toRemove.instruction = null;
        MethodLocation next = this.instructionList.get(index + 1);
        toRemove.mergeInto(next);
        this.instructionList.remove(index);
        int codeAddress = toRemove.codeAddress;
        for (int i = index; i < this.instructionList.size(); i++) {
            MethodLocation location = this.instructionList.get(i);
            location.index = i;
            location.codeAddress = codeAddress;
            Instruction instruction = location.getInstruction();
            if (instruction != null) {
                codeAddress += instruction.getCodeUnits();
            } else if (!$assertionsDisabled && i != this.instructionList.size() - 1) {
                throw new AssertionError();
            }
        }
        this.fixInstructions = true;
    }

    public void swapInstructions(int index1, int index2) {
        if (index1 >= this.instructionList.size() - 1 || index2 >= this.instructionList.size() - 1) {
            throw new IndexOutOfBoundsException();
        }
        MethodLocation first = this.instructionList.get(index1);
        MethodLocation second = this.instructionList.get(index2);
        if (!$assertionsDisabled && first.instruction == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && second.instruction == null) {
            throw new AssertionError();
        }
        first.instruction.location = second;
        second.instruction.location = first;
        BuilderInstruction tmp = second.instruction;
        second.instruction = first.instruction;
        first.instruction = tmp;
        if (index2 < index1) {
            index2 = index1;
            index1 = index2;
        }
        int codeAddress = first.codeAddress + first.instruction.getCodeUnits();
        for (int i = index1 + 1; i <= index2; i++) {
            MethodLocation location = this.instructionList.get(i);
            location.codeAddress = codeAddress;
            Instruction instruction = location.instruction;
            if (!$assertionsDisabled && instruction == null) {
                throw new AssertionError();
            }
            codeAddress += location.instruction.getCodeUnits();
        }
        this.fixInstructions = true;
    }

    @Nullable
    private BuilderInstruction getFirstNonNop(int startIndex) {
        for (int i = startIndex; i < this.instructionList.size() - 1; i++) {
            BuilderInstruction instruction = this.instructionList.get(i).instruction;
            if (!$assertionsDisabled && instruction == null) {
                throw new AssertionError();
            }
            if (instruction.getOpcode() != Opcode.NOP) {
                return instruction;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:100:0x02c5, code lost:
        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x000c, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void fixInstructions() {
        /*
            Method dump skipped, instructions count: 725
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jf.dexlib2.builder.MutableMethodImplementation.fixInstructions():void");
    }

    private int mapCodeAddressToIndex(@Nonnull int[] codeAddressToIndex, int codeAddress) {
        while (true) {
            if (codeAddress >= codeAddressToIndex.length) {
                codeAddress = codeAddressToIndex.length - 1;
            }
            int index = codeAddressToIndex[codeAddress];
            if (index < 0) {
                codeAddress--;
            } else {
                return index;
            }
        }
    }

    private int mapCodeAddressToIndex(int codeAddress) {
        int index = (int) (codeAddress / 1.9f);
        if (index >= this.instructionList.size()) {
            index = this.instructionList.size() - 1;
        }
        MethodLocation guessedLocation = this.instructionList.get(index);
        if (guessedLocation.codeAddress == codeAddress) {
            return index;
        }
        if (guessedLocation.codeAddress > codeAddress) {
            do {
                index--;
            } while (this.instructionList.get(index).codeAddress > codeAddress);
            return index;
        }
        do {
            index++;
            if (index >= this.instructionList.size()) {
                break;
            }
        } while (this.instructionList.get(index).codeAddress <= codeAddress);
        return index - 1;
    }

    @Nonnull
    public Label newLabelForAddress(int codeAddress) {
        if (codeAddress < 0 || codeAddress > this.instructionList.get(this.instructionList.size() - 1).codeAddress) {
            throw new IndexOutOfBoundsException(String.format("codeAddress %d out of bounds", Integer.valueOf(codeAddress)));
        }
        MethodLocation referent = this.instructionList.get(mapCodeAddressToIndex(codeAddress));
        return referent.addNewLabel();
    }

    @Nonnull
    public Label newLabelForIndex(int instructionIndex) {
        if (instructionIndex < 0 || instructionIndex >= this.instructionList.size()) {
            throw new IndexOutOfBoundsException(String.format("instruction index %d out of bounds", Integer.valueOf(instructionIndex)));
        }
        MethodLocation referent = this.instructionList.get(instructionIndex);
        return referent.addNewLabel();
    }

    @Nonnull
    private Label newLabel(@Nonnull int[] codeAddressToIndex, int codeAddress) {
        MethodLocation referent = this.instructionList.get(mapCodeAddressToIndex(codeAddressToIndex, codeAddress));
        return referent.addNewLabel();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/MutableMethodImplementation$SwitchPayloadReferenceLabel.class */
    public static class SwitchPayloadReferenceLabel extends Label {
        @Nonnull
        public MethodLocation switchLocation;

        private SwitchPayloadReferenceLabel() {
        }
    }

    @Nonnull
    public Label newSwitchPayloadReferenceLabel(@Nonnull MethodLocation switchLocation, @Nonnull int[] codeAddressToIndex, int codeAddress) {
        MethodLocation referent = this.instructionList.get(mapCodeAddressToIndex(codeAddressToIndex, codeAddress));
        SwitchPayloadReferenceLabel label = new SwitchPayloadReferenceLabel();
        label.switchLocation = switchLocation;
        referent.getLabels().add(label);
        return label;
    }

    private void setInstruction(@Nonnull MethodLocation location, @Nonnull BuilderInstruction instruction) {
        location.instruction = instruction;
        instruction.location = location;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void convertAndSetInstruction(@Nonnull MethodLocation location, int[] codeAddressToIndex, @Nonnull Instruction instruction) {
        switch (instruction.getOpcode().format) {
            case Format10t:
                setInstruction(location, newBuilderInstruction10t(location.codeAddress, codeAddressToIndex, (Instruction10t) instruction));
                return;
            case Format10x:
                setInstruction(location, newBuilderInstruction10x((Instruction10x) instruction));
                return;
            case Format11n:
                setInstruction(location, newBuilderInstruction11n((Instruction11n) instruction));
                return;
            case Format11x:
                setInstruction(location, newBuilderInstruction11x((Instruction11x) instruction));
                return;
            case Format12x:
                setInstruction(location, newBuilderInstruction12x((Instruction12x) instruction));
                return;
            case Format20bc:
                setInstruction(location, newBuilderInstruction20bc((Instruction20bc) instruction));
                return;
            case Format20t:
                setInstruction(location, newBuilderInstruction20t(location.codeAddress, codeAddressToIndex, (Instruction20t) instruction));
                return;
            case Format21c:
                setInstruction(location, newBuilderInstruction21c((Instruction21c) instruction));
                return;
            case Format21ih:
                setInstruction(location, newBuilderInstruction21ih((Instruction21ih) instruction));
                return;
            case Format21lh:
                setInstruction(location, newBuilderInstruction21lh((Instruction21lh) instruction));
                return;
            case Format21s:
                setInstruction(location, newBuilderInstruction21s((Instruction21s) instruction));
                return;
            case Format21t:
                setInstruction(location, newBuilderInstruction21t(location.codeAddress, codeAddressToIndex, (Instruction21t) instruction));
                return;
            case Format22b:
                setInstruction(location, newBuilderInstruction22b((Instruction22b) instruction));
                return;
            case Format22c:
                setInstruction(location, newBuilderInstruction22c((Instruction22c) instruction));
                return;
            case Format22cs:
                setInstruction(location, newBuilderInstruction22cs((Instruction22cs) instruction));
                return;
            case Format22s:
                setInstruction(location, newBuilderInstruction22s((Instruction22s) instruction));
                return;
            case Format22t:
                setInstruction(location, newBuilderInstruction22t(location.codeAddress, codeAddressToIndex, (Instruction22t) instruction));
                return;
            case Format22x:
                setInstruction(location, newBuilderInstruction22x((Instruction22x) instruction));
                return;
            case Format23x:
                setInstruction(location, newBuilderInstruction23x((Instruction23x) instruction));
                return;
            case Format30t:
                setInstruction(location, newBuilderInstruction30t(location.codeAddress, codeAddressToIndex, (Instruction30t) instruction));
                return;
            case Format31c:
                setInstruction(location, newBuilderInstruction31c((Instruction31c) instruction));
                return;
            case Format31i:
                setInstruction(location, newBuilderInstruction31i((Instruction31i) instruction));
                return;
            case Format31t:
                setInstruction(location, newBuilderInstruction31t(location, codeAddressToIndex, (Instruction31t) instruction));
                return;
            case Format32x:
                setInstruction(location, newBuilderInstruction32x((Instruction32x) instruction));
                return;
            case Format35c:
                setInstruction(location, newBuilderInstruction35c((Instruction35c) instruction));
                return;
            case Format35mi:
                setInstruction(location, newBuilderInstruction35mi((Instruction35mi) instruction));
                return;
            case Format35ms:
                setInstruction(location, newBuilderInstruction35ms((Instruction35ms) instruction));
                return;
            case Format3rc:
                setInstruction(location, newBuilderInstruction3rc((Instruction3rc) instruction));
                return;
            case Format3rmi:
                setInstruction(location, newBuilderInstruction3rmi((Instruction3rmi) instruction));
                return;
            case Format3rms:
                setInstruction(location, newBuilderInstruction3rms((Instruction3rms) instruction));
                return;
            case Format51l:
                setInstruction(location, newBuilderInstruction51l((Instruction51l) instruction));
                return;
            case PackedSwitchPayload:
                setInstruction(location, newBuilderPackedSwitchPayload(location, codeAddressToIndex, (PackedSwitchPayload) instruction));
                return;
            case SparseSwitchPayload:
                setInstruction(location, newBuilderSparseSwitchPayload(location, codeAddressToIndex, (SparseSwitchPayload) instruction));
                return;
            case ArrayPayload:
                setInstruction(location, newBuilderArrayPayload((ArrayPayload) instruction));
                return;
            default:
                throw new ExceptionWithContext("Instruction format %s not supported", instruction.getOpcode().format);
        }
    }

    @Nonnull
    private BuilderInstruction10t newBuilderInstruction10t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction10t instruction) {
        return new BuilderInstruction10t(instruction.getOpcode(), newLabel(codeAddressToIndex, codeAddress + instruction.getCodeOffset()));
    }

    @Nonnull
    private BuilderInstruction10x newBuilderInstruction10x(@Nonnull Instruction10x instruction) {
        return new BuilderInstruction10x(instruction.getOpcode());
    }

    @Nonnull
    private BuilderInstruction11n newBuilderInstruction11n(@Nonnull Instruction11n instruction) {
        return new BuilderInstruction11n(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
    }

    @Nonnull
    private BuilderInstruction11x newBuilderInstruction11x(@Nonnull Instruction11x instruction) {
        return new BuilderInstruction11x(instruction.getOpcode(), instruction.getRegisterA());
    }

    @Nonnull
    private BuilderInstruction12x newBuilderInstruction12x(@Nonnull Instruction12x instruction) {
        return new BuilderInstruction12x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB());
    }

    @Nonnull
    private BuilderInstruction20bc newBuilderInstruction20bc(@Nonnull Instruction20bc instruction) {
        return new BuilderInstruction20bc(instruction.getOpcode(), instruction.getVerificationError(), instruction.getReference());
    }

    @Nonnull
    private BuilderInstruction20t newBuilderInstruction20t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction20t instruction) {
        return new BuilderInstruction20t(instruction.getOpcode(), newLabel(codeAddressToIndex, codeAddress + instruction.getCodeOffset()));
    }

    @Nonnull
    private BuilderInstruction21c newBuilderInstruction21c(@Nonnull Instruction21c instruction) {
        return new BuilderInstruction21c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getReference());
    }

    @Nonnull
    private BuilderInstruction21ih newBuilderInstruction21ih(@Nonnull Instruction21ih instruction) {
        return new BuilderInstruction21ih(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
    }

    @Nonnull
    private BuilderInstruction21lh newBuilderInstruction21lh(@Nonnull Instruction21lh instruction) {
        return new BuilderInstruction21lh(instruction.getOpcode(), instruction.getRegisterA(), instruction.getWideLiteral());
    }

    @Nonnull
    private BuilderInstruction21s newBuilderInstruction21s(@Nonnull Instruction21s instruction) {
        return new BuilderInstruction21s(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
    }

    @Nonnull
    private BuilderInstruction21t newBuilderInstruction21t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction21t instruction) {
        return new BuilderInstruction21t(instruction.getOpcode(), instruction.getRegisterA(), newLabel(codeAddressToIndex, codeAddress + instruction.getCodeOffset()));
    }

    @Nonnull
    private BuilderInstruction22b newBuilderInstruction22b(@Nonnull Instruction22b instruction) {
        return new BuilderInstruction22b(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getNarrowLiteral());
    }

    @Nonnull
    private BuilderInstruction22c newBuilderInstruction22c(@Nonnull Instruction22c instruction) {
        return new BuilderInstruction22c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getReference());
    }

    @Nonnull
    private BuilderInstruction22cs newBuilderInstruction22cs(@Nonnull Instruction22cs instruction) {
        return new BuilderInstruction22cs(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getFieldOffset());
    }

    @Nonnull
    private BuilderInstruction22s newBuilderInstruction22s(@Nonnull Instruction22s instruction) {
        return new BuilderInstruction22s(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getNarrowLiteral());
    }

    @Nonnull
    private BuilderInstruction22t newBuilderInstruction22t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction22t instruction) {
        return new BuilderInstruction22t(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), newLabel(codeAddressToIndex, codeAddress + instruction.getCodeOffset()));
    }

    @Nonnull
    private BuilderInstruction22x newBuilderInstruction22x(@Nonnull Instruction22x instruction) {
        return new BuilderInstruction22x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB());
    }

    @Nonnull
    private BuilderInstruction23x newBuilderInstruction23x(@Nonnull Instruction23x instruction) {
        return new BuilderInstruction23x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB(), instruction.getRegisterC());
    }

    @Nonnull
    private BuilderInstruction30t newBuilderInstruction30t(int codeAddress, int[] codeAddressToIndex, @Nonnull Instruction30t instruction) {
        return new BuilderInstruction30t(instruction.getOpcode(), newLabel(codeAddressToIndex, codeAddress + instruction.getCodeOffset()));
    }

    @Nonnull
    private BuilderInstruction31c newBuilderInstruction31c(@Nonnull Instruction31c instruction) {
        return new BuilderInstruction31c(instruction.getOpcode(), instruction.getRegisterA(), instruction.getReference());
    }

    @Nonnull
    private BuilderInstruction31i newBuilderInstruction31i(@Nonnull Instruction31i instruction) {
        return new BuilderInstruction31i(instruction.getOpcode(), instruction.getRegisterA(), instruction.getNarrowLiteral());
    }

    @Nonnull
    private BuilderInstruction31t newBuilderInstruction31t(@Nonnull MethodLocation location, int[] codeAddressToIndex, @Nonnull Instruction31t instruction) {
        Label newLabel;
        int codeAddress = location.getCodeAddress();
        if (instruction.getOpcode() != Opcode.FILL_ARRAY_DATA) {
            newLabel = newSwitchPayloadReferenceLabel(location, codeAddressToIndex, codeAddress + instruction.getCodeOffset());
        } else {
            newLabel = newLabel(codeAddressToIndex, codeAddress + instruction.getCodeOffset());
        }
        return new BuilderInstruction31t(instruction.getOpcode(), instruction.getRegisterA(), newLabel);
    }

    @Nonnull
    private BuilderInstruction32x newBuilderInstruction32x(@Nonnull Instruction32x instruction) {
        return new BuilderInstruction32x(instruction.getOpcode(), instruction.getRegisterA(), instruction.getRegisterB());
    }

    @Nonnull
    private BuilderInstruction35c newBuilderInstruction35c(@Nonnull Instruction35c instruction) {
        return new BuilderInstruction35c(instruction.getOpcode(), instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getReference());
    }

    @Nonnull
    private BuilderInstruction35mi newBuilderInstruction35mi(@Nonnull Instruction35mi instruction) {
        return new BuilderInstruction35mi(instruction.getOpcode(), instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getInlineIndex());
    }

    @Nonnull
    private BuilderInstruction35ms newBuilderInstruction35ms(@Nonnull Instruction35ms instruction) {
        return new BuilderInstruction35ms(instruction.getOpcode(), instruction.getRegisterCount(), instruction.getRegisterC(), instruction.getRegisterD(), instruction.getRegisterE(), instruction.getRegisterF(), instruction.getRegisterG(), instruction.getVtableIndex());
    }

    @Nonnull
    private BuilderInstruction3rc newBuilderInstruction3rc(@Nonnull Instruction3rc instruction) {
        return new BuilderInstruction3rc(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getReference());
    }

    @Nonnull
    private BuilderInstruction3rmi newBuilderInstruction3rmi(@Nonnull Instruction3rmi instruction) {
        return new BuilderInstruction3rmi(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getInlineIndex());
    }

    @Nonnull
    private BuilderInstruction3rms newBuilderInstruction3rms(@Nonnull Instruction3rms instruction) {
        return new BuilderInstruction3rms(instruction.getOpcode(), instruction.getStartRegister(), instruction.getRegisterCount(), instruction.getVtableIndex());
    }

    @Nonnull
    private BuilderInstruction51l newBuilderInstruction51l(@Nonnull Instruction51l instruction) {
        return new BuilderInstruction51l(instruction.getOpcode(), instruction.getRegisterA(), instruction.getWideLiteral());
    }

    @Nullable
    private MethodLocation findSwitchForPayload(@Nonnull MethodLocation payloadLocation) {
        MethodLocation location = payloadLocation;
        MethodLocation switchLocation = null;
        do {
            for (Label label : location.getLabels()) {
                if (label instanceof SwitchPayloadReferenceLabel) {
                    if (switchLocation != null) {
                        throw new IllegalStateException("Multiple switch instructions refer to the same payload. This is not currently supported. Please file a bug :)");
                    }
                    switchLocation = ((SwitchPayloadReferenceLabel) label).switchLocation;
                }
            }
            if (location.index == 0) {
                return switchLocation;
            }
            location = this.instructionList.get(location.index - 1);
            if (location.instruction == null) {
                break;
            }
        } while (location.instruction.getOpcode() == Opcode.NOP);
        return switchLocation;
    }

    @Nonnull
    private BuilderPackedSwitchPayload newBuilderPackedSwitchPayload(@Nonnull MethodLocation location, @Nonnull int[] codeAddressToIndex, @Nonnull PackedSwitchPayload instruction) {
        int baseAddress;
        List<? extends SwitchElement> switchElements = instruction.getSwitchElements();
        if (switchElements.size() == 0) {
            return new BuilderPackedSwitchPayload(0, null);
        }
        MethodLocation switchLocation = findSwitchForPayload(location);
        if (switchLocation == null) {
            baseAddress = 0;
        } else {
            baseAddress = switchLocation.codeAddress;
        }
        List<Label> labels = Lists.newArrayList();
        for (SwitchElement element : switchElements) {
            labels.add(newLabel(codeAddressToIndex, element.getOffset() + baseAddress));
        }
        return new BuilderPackedSwitchPayload(switchElements.get(0).getKey(), labels);
    }

    @Nonnull
    private BuilderSparseSwitchPayload newBuilderSparseSwitchPayload(@Nonnull MethodLocation location, @Nonnull int[] codeAddressToIndex, @Nonnull SparseSwitchPayload instruction) {
        int baseAddress;
        List<? extends SwitchElement> switchElements = instruction.getSwitchElements();
        if (switchElements.size() == 0) {
            return new BuilderSparseSwitchPayload(null);
        }
        MethodLocation switchLocation = findSwitchForPayload(location);
        if (switchLocation == null) {
            baseAddress = 0;
        } else {
            baseAddress = switchLocation.codeAddress;
        }
        List<SwitchLabelElement> labelElements = Lists.newArrayList();
        for (SwitchElement element : switchElements) {
            labelElements.add(new SwitchLabelElement(element.getKey(), newLabel(codeAddressToIndex, element.getOffset() + baseAddress)));
        }
        return new BuilderSparseSwitchPayload(labelElements);
    }

    @Nonnull
    private BuilderArrayPayload newBuilderArrayPayload(@Nonnull ArrayPayload instruction) {
        return new BuilderArrayPayload(instruction.getElementWidth(), instruction.getArrayElements());
    }

    @Nonnull
    private BuilderDebugItem convertDebugItem(@Nonnull DebugItem debugItem) {
        switch (debugItem.getDebugItemType()) {
            case 3:
                StartLocal startLocal = (StartLocal) debugItem;
                return new BuilderStartLocal(startLocal.getRegister(), startLocal.getNameReference(), startLocal.getTypeReference(), startLocal.getSignatureReference());
            case 4:
            default:
                throw new ExceptionWithContext("Invalid debug item type: " + debugItem.getDebugItemType(), new Object[0]);
            case 5:
                EndLocal endLocal = (EndLocal) debugItem;
                return new BuilderEndLocal(endLocal.getRegister());
            case 6:
                RestartLocal restartLocal = (RestartLocal) debugItem;
                return new BuilderRestartLocal(restartLocal.getRegister());
            case 7:
                return new BuilderPrologueEnd();
            case 8:
                return new BuilderEpilogueBegin();
            case 9:
                SetSourceFile setSourceFile = (SetSourceFile) debugItem;
                return new BuilderSetSourceFile(setSourceFile.getSourceFileReference());
            case 10:
                LineNumber lineNumber = (LineNumber) debugItem;
                return new BuilderLineNumber(lineNumber.getLineNumber());
        }
    }
}
