package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.dexpler.DexBody;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/PseudoInstruction.class */
public abstract class PseudoInstruction extends DexlibAbstractInstruction {
    protected int dataFirstByte;
    protected int dataLastByte;
    protected int dataSize;
    protected byte[] data;
    protected boolean loaded;

    public abstract void computeDataOffsets(DexBody dexBody);

    public PseudoInstruction(Instruction instruction, int codeAddress) {
        super(instruction, codeAddress);
        this.dataFirstByte = -1;
        this.dataLastByte = -1;
        this.dataSize = -1;
        this.data = null;
        this.loaded = false;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public byte[] getData() {
        return this.data;
    }

    protected void setData(byte[] data) {
        this.data = data;
    }

    public int getDataFirstByte() {
        if (this.dataFirstByte == -1) {
            throw new RuntimeException("Error: dataFirstByte was not set!");
        }
        return this.dataFirstByte;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setDataFirstByte(int dataFirstByte) {
        this.dataFirstByte = dataFirstByte;
    }

    public int getDataLastByte() {
        if (this.dataLastByte == -1) {
            throw new RuntimeException("Error: dataLastByte was not set!");
        }
        return this.dataLastByte;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setDataLastByte(int dataLastByte) {
        this.dataLastByte = dataLastByte;
    }

    public int getDataSize() {
        if (this.dataSize == -1) {
            throw new RuntimeException("Error: dataFirstByte was not set!");
        }
        return this.dataSize;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }
}
