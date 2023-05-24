package soot.jimple.infoflow.data;

import soot.jimple.Stmt;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/AbstractionAtSink.class */
public class AbstractionAtSink {
    private final ISourceSinkDefinition sinkDefinition;
    private final Abstraction abstraction;
    private final Stmt sinkStmt;

    public AbstractionAtSink(ISourceSinkDefinition sinkDefinition, Abstraction abstraction, Stmt sinkStmt) {
        this.sinkDefinition = sinkDefinition;
        this.abstraction = abstraction;
        this.sinkStmt = sinkStmt;
    }

    public ISourceSinkDefinition getSinkDefinition() {
        return this.sinkDefinition;
    }

    public Abstraction getAbstraction() {
        return this.abstraction;
    }

    public Stmt getSinkStmt() {
        return this.sinkStmt;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.abstraction == null ? 0 : this.abstraction.hashCode());
        return (31 * ((31 * result) + (this.sinkDefinition == null ? 0 : this.sinkDefinition.hashCode()))) + (this.sinkStmt == null ? 0 : this.sinkStmt.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractionAtSink other = (AbstractionAtSink) obj;
        if (this.abstraction == null) {
            if (other.abstraction != null) {
                return false;
            }
        } else if (!this.abstraction.equals(other.abstraction)) {
            return false;
        }
        if (this.sinkDefinition == null) {
            if (other.sinkDefinition != null) {
                return false;
            }
        } else if (!this.sinkDefinition.equals(other.sinkDefinition)) {
            return false;
        }
        if (this.sinkStmt == null) {
            if (other.sinkStmt != null) {
                return false;
            }
            return true;
        } else if (!this.sinkStmt.equals(other.sinkStmt)) {
            return false;
        } else {
            return true;
        }
    }

    public String toString() {
        return this.abstraction + " at " + this.sinkStmt;
    }
}
