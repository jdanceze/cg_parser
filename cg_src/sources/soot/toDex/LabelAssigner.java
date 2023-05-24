package soot.toDex;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.builder.MethodImplementationBuilder;
import soot.jimple.Stmt;
import soot.toDex.instructions.AbstractPayload;
import soot.toDex.instructions.SwitchPayload;
/* loaded from: gencallgraphv3.jar:soot/toDex/LabelAssigner.class */
public class LabelAssigner {
    private final MethodImplementationBuilder builder;
    private int lastLabelId = 0;
    private Map<Stmt, Label> stmtToLabel = new HashMap();
    private Map<Stmt, String> stmtToLabelName = new HashMap();
    private Map<AbstractPayload, Label> payloadToLabel = new HashMap();
    private Map<AbstractPayload, String> payloadToLabelName = new HashMap();

    public LabelAssigner(MethodImplementationBuilder builder) {
        this.builder = builder;
    }

    public Label getOrCreateLabel(Stmt stmt) {
        if (stmt == null) {
            throw new RuntimeException("Cannot create label for NULL statement");
        }
        Label lbl = this.stmtToLabel.get(stmt);
        if (lbl == null) {
            StringBuilder sb = new StringBuilder("l");
            int i = this.lastLabelId;
            this.lastLabelId = i + 1;
            String labelName = sb.append(i).toString();
            lbl = this.builder.getLabel(labelName);
            this.stmtToLabel.put(stmt, lbl);
            this.stmtToLabelName.put(stmt, labelName);
        }
        return lbl;
    }

    public Label getOrCreateLabel(AbstractPayload payload) {
        if (payload == null) {
            throw new RuntimeException("Cannot create label for NULL payload");
        }
        Label lbl = this.payloadToLabel.get(payload);
        if (lbl == null) {
            StringBuilder sb = new StringBuilder("l");
            int i = this.lastLabelId;
            this.lastLabelId = i + 1;
            String labelName = sb.append(i).toString();
            lbl = this.builder.getLabel(labelName);
            this.payloadToLabel.put(payload, lbl);
            this.payloadToLabelName.put(payload, labelName);
        }
        return lbl;
    }

    public Label getLabel(Stmt stmt) {
        Label lbl = getLabelUnsafe(stmt);
        if (lbl == null) {
            throw new RuntimeException("Statement has no label: " + stmt);
        }
        return lbl;
    }

    public Label getLabelUnsafe(Stmt stmt) {
        return this.stmtToLabel.get(stmt);
    }

    public Label getLabel(SwitchPayload payload) {
        Label lbl = this.payloadToLabel.get(payload);
        if (lbl == null) {
            throw new RuntimeException("Switch payload has no label: " + payload);
        }
        return lbl;
    }

    public String getLabelName(Stmt stmt) {
        return this.stmtToLabelName.get(stmt);
    }

    public String getLabelName(AbstractPayload payload) {
        return this.payloadToLabelName.get(payload);
    }

    public Label getLabelAtAddress(int address) {
        for (Label lb : this.stmtToLabel.values()) {
            if (lb.isPlaced() && lb.getCodeAddress() == address) {
                return lb;
            }
        }
        return null;
    }

    public Collection<Label> getAllLabels() {
        return this.stmtToLabel.values();
    }
}
