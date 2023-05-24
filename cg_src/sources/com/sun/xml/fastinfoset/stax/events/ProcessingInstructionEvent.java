package com.sun.xml.fastinfoset.stax.events;

import javax.xml.stream.events.ProcessingInstruction;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/stax/events/ProcessingInstructionEvent.class */
public class ProcessingInstructionEvent extends EventBase implements ProcessingInstruction {
    private String targetName;
    private String _data;

    public ProcessingInstructionEvent() {
        init();
    }

    public ProcessingInstructionEvent(String targetName, String data) {
        this.targetName = targetName;
        this._data = data;
        init();
    }

    protected void init() {
        setEventType(3);
    }

    public String getTarget() {
        return this.targetName;
    }

    public void setTarget(String targetName) {
        this.targetName = targetName;
    }

    public void setData(String data) {
        this._data = data;
    }

    public String getData() {
        return this._data;
    }

    public String toString() {
        if (this._data != null && this.targetName != null) {
            return "<?" + this.targetName + Instruction.argsep + this._data + "?>";
        }
        if (this.targetName != null) {
            return "<?" + this.targetName + "?>";
        }
        if (this._data != null) {
            return "<?" + this._data + "?>";
        }
        return "<??>";
    }
}
