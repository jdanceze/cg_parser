package org.jf.dexlib2.builder;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.builder.debug.BuilderEndLocal;
import org.jf.dexlib2.builder.debug.BuilderEpilogueBegin;
import org.jf.dexlib2.builder.debug.BuilderLineNumber;
import org.jf.dexlib2.builder.debug.BuilderPrologueEnd;
import org.jf.dexlib2.builder.debug.BuilderRestartLocal;
import org.jf.dexlib2.builder.debug.BuilderSetSourceFile;
import org.jf.dexlib2.builder.debug.BuilderStartLocal;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/builder/MethodLocation.class */
public class MethodLocation {
    @Nullable
    BuilderInstruction instruction;
    int codeAddress;
    int index;
    private final LocatedItems<BuilderDebugItem> debugItems = new LocatedDebugItems();
    private final LocatedItems<Label> labels = new LocatedLabels();

    /* JADX INFO: Access modifiers changed from: package-private */
    public MethodLocation(@Nullable BuilderInstruction instruction, int codeAddress, int index) {
        this.instruction = instruction;
        this.codeAddress = codeAddress;
        this.index = index;
    }

    @Nullable
    public Instruction getInstruction() {
        return this.instruction;
    }

    public int getCodeAddress() {
        return this.codeAddress;
    }

    public int getIndex() {
        return this.index;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void mergeInto(@Nonnull MethodLocation nextLocation) {
        this.labels.mergeItemsIntoNext(nextLocation, nextLocation.labels);
        this.debugItems.mergeItemsIntoNext(nextLocation, nextLocation.debugItems);
    }

    @Nonnull
    public Set<Label> getLabels() {
        return this.labels.getModifiableItems(this);
    }

    @Nonnull
    public Label addNewLabel() {
        Label newLabel = new Label();
        getLabels().add(newLabel);
        return newLabel;
    }

    @Nonnull
    public Set<BuilderDebugItem> getDebugItems() {
        return this.debugItems.getModifiableItems(this);
    }

    public void addLineNumber(int lineNumber) {
        getDebugItems().add(new BuilderLineNumber(lineNumber));
    }

    public void addStartLocal(int registerNumber, @Nullable StringReference name, @Nullable TypeReference type, @Nullable StringReference signature) {
        getDebugItems().add(new BuilderStartLocal(registerNumber, name, type, signature));
    }

    public void addEndLocal(int registerNumber) {
        getDebugItems().add(new BuilderEndLocal(registerNumber));
    }

    public void addRestartLocal(int registerNumber) {
        getDebugItems().add(new BuilderRestartLocal(registerNumber));
    }

    public void addPrologue() {
        getDebugItems().add(new BuilderPrologueEnd());
    }

    public void addEpilogue() {
        getDebugItems().add(new BuilderEpilogueBegin());
    }

    public void addSetSourceFile(@Nullable StringReference sourceFile) {
        getDebugItems().add(new BuilderSetSourceFile(sourceFile));
    }
}
