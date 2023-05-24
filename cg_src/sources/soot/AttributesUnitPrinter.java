package soot;

import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.tagkit.ColorTag;
import soot.tagkit.Host;
import soot.tagkit.JimpleLineNumberTag;
import soot.tagkit.PositionTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/AttributesUnitPrinter.class */
public class AttributesUnitPrinter {
    private static final Logger logger = LoggerFactory.getLogger(AttributesUnitPrinter.class);
    private Stack<Integer> startOffsets;
    private int endOffset;
    private int startStmtOffset;
    private int startLn;
    private int currentLn;
    private int lastNewline;
    private UnitPrinter printer;

    public AttributesUnitPrinter(int currentLnNum) {
        this.currentLn = currentLnNum;
    }

    public void startUnit(Unit u) {
        this.startLn = this.currentLn;
        this.startStmtOffset = outputLength() - this.lastNewline;
    }

    public void endUnit(Unit u) {
        int endStmtOffset = outputLength() - this.lastNewline;
        if (hasTag(u)) {
            u.addTag(new JimpleLineNumberTag(this.startLn, this.currentLn));
        }
        if (hasColorTag(u)) {
            u.addTag(new PositionTag(this.startStmtOffset, endStmtOffset));
        }
    }

    public void startValueBox(ValueBox u) {
        if (this.startOffsets == null) {
            this.startOffsets = new Stack<>();
        }
        this.startOffsets.push(Integer.valueOf(outputLength() - this.lastNewline));
    }

    public void endValueBox(ValueBox u) {
        this.endOffset = outputLength() - this.lastNewline;
        if (hasColorTag(u)) {
            u.addTag(new PositionTag(this.startOffsets.pop().intValue(), this.endOffset));
        }
    }

    private boolean hasTag(Host h) {
        if (h instanceof Unit) {
            for (ValueBox box : ((Unit) h).getUseAndDefBoxes()) {
                if (hasTag(box)) {
                    return true;
                }
            }
        }
        return !h.getTags().isEmpty();
    }

    private boolean hasColorTag(Host h) {
        for (Tag t : h.getTags()) {
            if (t instanceof ColorTag) {
                return true;
            }
        }
        return false;
    }

    public void setEndLn(int ln) {
        this.currentLn = ln;
    }

    public int getEndLn() {
        return this.currentLn;
    }

    public void newline() {
        this.currentLn++;
        this.lastNewline = outputLength();
    }

    private int outputLength() {
        return this.printer.output().length();
    }

    public void setUnitPrinter(UnitPrinter up) {
        this.printer = up;
    }
}
