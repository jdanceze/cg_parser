package soot.dava.internal.asg;

import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.dava.internal.SET.SETNode;
import soot.jimple.Stmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/asg/AugmentedStmt.class */
public class AugmentedStmt {
    private static final Logger logger = LoggerFactory.getLogger(AugmentedStmt.class);
    public List<AugmentedStmt> bpreds;
    public List<AugmentedStmt> bsuccs;
    public List<AugmentedStmt> cpreds;
    public List<AugmentedStmt> csuccs;
    public SETNode myNode;
    private final IterableSet<AugmentedStmt> dominators = new IterableSet<>();
    private IterableSet<AugmentedStmt> reachers = new IterableSet<>();
    private Stmt s;

    public AugmentedStmt(Stmt s) {
        this.s = s;
        reset_PredsSuccs();
    }

    public void set_Stmt(Stmt s) {
        this.s = s;
    }

    public boolean add_BPred(AugmentedStmt bpred) {
        if (!add_CPred(bpred)) {
            return false;
        }
        if (this.bpreds.contains(bpred)) {
            this.cpreds.remove(bpred);
            return false;
        }
        this.bpreds.add(bpred);
        return true;
    }

    public boolean add_BSucc(AugmentedStmt bsucc) {
        if (!add_CSucc(bsucc)) {
            return false;
        }
        if (this.bsuccs.contains(bsucc)) {
            this.csuccs.remove(bsucc);
            return false;
        }
        this.bsuccs.add(bsucc);
        return true;
    }

    public boolean add_CPred(AugmentedStmt cpred) {
        if (!this.cpreds.contains(cpred)) {
            this.cpreds.add(cpred);
            return true;
        }
        return false;
    }

    public boolean add_CSucc(AugmentedStmt csucc) {
        if (!this.csuccs.contains(csucc)) {
            this.csuccs.add(csucc);
            return true;
        }
        return false;
    }

    public boolean remove_BPred(AugmentedStmt bpred) {
        if (!remove_CPred(bpred)) {
            return false;
        }
        if (this.bpreds.contains(bpred)) {
            this.bpreds.remove(bpred);
            return true;
        }
        this.cpreds.add(bpred);
        return false;
    }

    public boolean remove_BSucc(AugmentedStmt bsucc) {
        if (!remove_CSucc(bsucc)) {
            return false;
        }
        if (this.bsuccs.contains(bsucc)) {
            this.bsuccs.remove(bsucc);
            return true;
        }
        this.csuccs.add(bsucc);
        return false;
    }

    public boolean remove_CPred(AugmentedStmt cpred) {
        if (this.cpreds.contains(cpred)) {
            this.cpreds.remove(cpred);
            return true;
        }
        return false;
    }

    public boolean remove_CSucc(AugmentedStmt csucc) {
        if (this.csuccs.contains(csucc)) {
            this.csuccs.remove(csucc);
            return true;
        }
        return false;
    }

    public Stmt get_Stmt() {
        return this.s;
    }

    public IterableSet<AugmentedStmt> get_Dominators() {
        return this.dominators;
    }

    public IterableSet<AugmentedStmt> get_Reachers() {
        return this.reachers;
    }

    public void set_Reachability(IterableSet<AugmentedStmt> reachers) {
        this.reachers = reachers;
    }

    public void dump() {
        logger.debug(toString());
    }

    public String toString() {
        return "(" + this.s.toString() + " @ " + hashCode() + ")";
    }

    public void reset_PredsSuccs() {
        this.bpreds = new LinkedList();
        this.bsuccs = new LinkedList();
        this.cpreds = new LinkedList();
        this.csuccs = new LinkedList();
    }

    public Object clone() {
        return new AugmentedStmt((Stmt) this.s.clone());
    }
}
