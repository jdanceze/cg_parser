package soot.dava.toolkits.base.finders;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import soot.dava.internal.asg.AugmentedStmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/finders/SwitchNode.class */
public class SwitchNode implements Comparable {
    private AugmentedStmt as;
    private TreeSet<Object> indexSet;
    private IterableSet body;
    private final LinkedList preds = new LinkedList();
    private final LinkedList succs = new LinkedList();
    private int score = -1;

    public SwitchNode(AugmentedStmt as, TreeSet<Object> indexSet, IterableSet body) {
        this.as = as;
        this.indexSet = indexSet;
        this.body = body;
    }

    public int get_Score() {
        if (this.score == -1) {
            this.score = 0;
            if (this.preds.size() < 2) {
                Iterator sit = this.succs.iterator();
                while (sit.hasNext()) {
                    SwitchNode ssn = (SwitchNode) sit.next();
                    int curScore = ssn.get_Score();
                    if (this.score < curScore) {
                        this.score = curScore;
                    }
                }
                this.score++;
            }
        }
        return this.score;
    }

    public List get_Preds() {
        return this.preds;
    }

    public List get_Succs() {
        return this.succs;
    }

    public AugmentedStmt get_AugStmt() {
        return this.as;
    }

    public TreeSet<Object> get_IndexSet() {
        return new TreeSet<>((SortedSet<Object>) this.indexSet);
    }

    public IterableSet get_Body() {
        return this.body;
    }

    public SwitchNode reset() {
        this.preds.clear();
        this.succs.clear();
        return this;
    }

    public void setup_Graph(HashMap<AugmentedStmt, SwitchNode> binding) {
        Iterator rit = this.as.bsuccs.get(0).get_Reachers().iterator();
        while (rit.hasNext()) {
            SwitchNode pred = binding.get(rit.next());
            if (pred != null) {
                if (!this.preds.contains(pred)) {
                    this.preds.add(pred);
                }
                if (!pred.succs.contains(this)) {
                    pred.succs.add(this);
                }
            }
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(Object o) {
        if (o == this) {
            return 0;
        }
        if (this.indexSet.last() instanceof String) {
            return 1;
        }
        if (o instanceof String) {
            return -1;
        }
        if (o instanceof Integer) {
            return ((Integer) this.indexSet.last()).intValue() - ((Integer) o).intValue();
        }
        if (o instanceof TreeSet) {
            TreeSet other = (TreeSet) o;
            if (other.last() instanceof String) {
                return -1;
            }
            return ((Integer) this.indexSet.last()).intValue() - ((Integer) other.last()).intValue();
        }
        SwitchNode other2 = (SwitchNode) o;
        if (other2.indexSet.last() instanceof String) {
            return -1;
        }
        return ((Integer) this.indexSet.last()).intValue() - ((Integer) other2.indexSet.last()).intValue();
    }
}
