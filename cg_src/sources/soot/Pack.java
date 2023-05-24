package soot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.coffi.Instruction;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/Pack.class */
public abstract class Pack implements HasPhaseOptions, Iterable<Transform> {
    private final List<Transform> opts = new ArrayList();
    private final String name;

    @Override // soot.HasPhaseOptions
    public String getPhaseName() {
        return this.name;
    }

    public Pack(String name) {
        this.name = name;
    }

    @Override // java.lang.Iterable
    public Iterator<Transform> iterator() {
        return this.opts.iterator();
    }

    public void add(Transform t) {
        if (!t.getPhaseName().startsWith(String.valueOf(getPhaseName()) + ".")) {
            throw new RuntimeException("Transforms in pack '" + getPhaseName() + "' must have a phase name that starts with '" + getPhaseName() + ".'.");
        }
        PhaseOptions.v().getPM().notifyAddPack();
        if (get(t.getPhaseName()) != null) {
            throw new RuntimeException("Phase " + t.getPhaseName() + " already in pack");
        }
        this.opts.add(t);
    }

    public void insertAfter(Transform t, String phaseName) {
        PhaseOptions.v().getPM().notifyAddPack();
        for (Transform tr : this.opts) {
            if (tr.getPhaseName().equals(phaseName)) {
                this.opts.add(this.opts.indexOf(tr) + 1, t);
                return;
            }
        }
        throw new RuntimeException("phase " + phaseName + " not found!");
    }

    public void insertBefore(Transform t, String phaseName) {
        PhaseOptions.v().getPM().notifyAddPack();
        for (Transform tr : this.opts) {
            if (tr.getPhaseName().equals(phaseName)) {
                this.opts.add(this.opts.indexOf(tr), t);
                return;
            }
        }
        throw new RuntimeException("phase " + phaseName + " not found!");
    }

    public Transform get(String phaseName) {
        for (Transform tr : this.opts) {
            if (tr.getPhaseName().equals(phaseName)) {
                return tr;
            }
        }
        return null;
    }

    public boolean remove(String phaseName) {
        for (Transform tr : this.opts) {
            if (tr.getPhaseName().equals(phaseName)) {
                this.opts.remove(tr);
                return true;
            }
        }
        return false;
    }

    protected void internalApply() {
        throw new RuntimeException("wrong type of pack");
    }

    protected void internalApply(Body b) {
        throw new RuntimeException("wrong type of pack");
    }

    public final void apply() {
        Map<String, String> options = PhaseOptions.v().getPhaseOptions(this);
        if (!PhaseOptions.getBoolean(options, "enabled")) {
            return;
        }
        internalApply();
    }

    public final void apply(Body b) {
        Map<String, String> options = PhaseOptions.v().getPhaseOptions(this);
        if (!PhaseOptions.getBoolean(options, "enabled")) {
            return;
        }
        internalApply(b);
    }

    @Override // soot.HasPhaseOptions
    public String getDeclaredOptions() {
        return Options.getDeclaredOptionsForPhase(getPhaseName());
    }

    @Override // soot.HasPhaseOptions
    public String getDefaultOptions() {
        return Options.getDefaultOptionsForPhase(getPhaseName());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append(" { ");
        for (int i = 0; i < this.opts.size(); i++) {
            sb.append(this.opts.get(i).getPhaseName());
            if (i < this.opts.size() - 1) {
                sb.append(",");
            }
            sb.append(Instruction.argsep);
        }
        sb.append(" }");
        return sb.toString();
    }
}
