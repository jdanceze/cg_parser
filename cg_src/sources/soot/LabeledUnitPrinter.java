package soot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/LabeledUnitPrinter.class */
public abstract class LabeledUnitPrinter extends AbstractUnitPrinter {
    protected Map<Unit, String> labels;
    protected Map<Unit, String> references;
    protected String labelIndent = "     ";

    public LabeledUnitPrinter(Body b) {
        createLabelMaps(b);
    }

    public Map<Unit, String> labels() {
        return this.labels;
    }

    public Map<Unit, String> references() {
        return this.references;
    }

    @Override // soot.UnitPrinter
    public void unitRef(Unit u, boolean branchTarget) {
        String oldIndent = getIndent();
        if (branchTarget) {
            setIndent(this.labelIndent);
            handleIndent();
            setIndent(oldIndent);
            String label = this.labels.get(u);
            if (label == null || "<unnamed>".equals(label)) {
                label = "[?= " + u + "]";
            }
            this.output.append(label);
            return;
        }
        String ref = this.references.get(u);
        if (this.startOfLine) {
            String newIndent = "(" + ref + ")" + this.indent.substring(ref.length() + 2);
            setIndent(newIndent);
            handleIndent();
            setIndent(oldIndent);
            return;
        }
        this.output.append(ref);
    }

    private void createLabelMaps(Body body) {
        Chain<Unit> units = body.getUnits();
        this.labels = new HashMap((units.size() * 2) + 1, 0.7f);
        this.references = new HashMap((units.size() * 2) + 1, 0.7f);
        Set<Unit> labelStmts = new HashSet<>();
        Set<Unit> refStmts = new HashSet<>();
        for (UnitBox box : body.getAllUnitBoxes()) {
            Unit stmt = box.getUnit();
            if (box.isBranchTarget()) {
                labelStmts.add(stmt);
            } else {
                refStmts.add(stmt);
            }
        }
        int maxDigits = 1 + ((int) Math.log10(labelStmts.size()));
        String formatString = "label%0" + maxDigits + "d";
        int labelCount = 0;
        int refCount = 0;
        for (Unit s : units) {
            if (labelStmts.contains(s)) {
                labelCount++;
                this.labels.put(s, String.format(formatString, Integer.valueOf(labelCount)));
            }
            if (refStmts.contains(s)) {
                int i = refCount;
                refCount++;
                this.references.put(s, Integer.toString(i));
            }
        }
    }
}
