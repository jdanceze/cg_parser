package soot.jimple.toolkits.annotation.logic;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/logic/Loop.class */
public class Loop {
    protected final Stmt header;
    protected final Stmt backJump;
    protected final List<Stmt> loopStatements;
    protected final UnitGraph g;
    protected Collection<Stmt> loopExits;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Loop.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Loop(Stmt head, List<Stmt> loopStatements, UnitGraph g) {
        this.header = head;
        this.g = g;
        loopStatements.remove(head);
        loopStatements.add(0, head);
        this.backJump = loopStatements.get(loopStatements.size() - 1);
        if (!$assertionsDisabled && !g.getSuccsOf((Unit) this.backJump).contains(head)) {
            throw new AssertionError();
        }
        this.loopStatements = loopStatements;
    }

    public Stmt getHead() {
        return this.header;
    }

    public Stmt getBackJumpStmt() {
        return this.backJump;
    }

    public List<Stmt> getLoopStatements() {
        return this.loopStatements;
    }

    public Collection<Stmt> getLoopExits() {
        if (this.loopExits == null) {
            this.loopExits = new HashSet();
            for (Stmt s : this.loopStatements) {
                for (Unit succ : this.g.getSuccsOf((Unit) s)) {
                    if (!this.loopStatements.contains(succ)) {
                        this.loopExits.add(s);
                    }
                }
            }
        }
        return this.loopExits;
    }

    public Collection<Stmt> targetsOfLoopExit(Stmt loopExit) {
        if ($assertionsDisabled || getLoopExits().contains(loopExit)) {
            List<Unit> succs = this.g.getSuccsOf((Unit) loopExit);
            Collection<Stmt> res = new HashSet<>();
            for (Unit u : succs) {
                Stmt s = (Stmt) u;
                res.add(s);
            }
            res.removeAll(this.loopStatements);
            return res;
        }
        throw new AssertionError();
    }

    public boolean loopsForever() {
        return getLoopExits().isEmpty();
    }

    public boolean hasSingleExit() {
        return getLoopExits().size() == 1;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.header == null ? 0 : this.header.hashCode());
        return (31 * result) + (this.loopStatements == null ? 0 : this.loopStatements.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Loop other = (Loop) obj;
        if (this.header == null) {
            if (other.header != null) {
                return false;
            }
        } else if (!this.header.equals(other.header)) {
            return false;
        }
        if (this.loopStatements == null) {
            if (other.loopStatements != null) {
                return false;
            }
            return true;
        } else if (!this.loopStatements.equals(other.loopStatements)) {
            return false;
        } else {
            return true;
        }
    }
}
