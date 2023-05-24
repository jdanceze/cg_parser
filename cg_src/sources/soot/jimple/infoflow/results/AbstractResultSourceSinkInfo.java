package soot.jimple.infoflow.results;

import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/AbstractResultSourceSinkInfo.class */
public abstract class AbstractResultSourceSinkInfo {
    protected final ISourceSinkDefinition definition;
    protected final AccessPath accessPath;
    protected final Stmt stmt;
    protected final Object userData;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AbstractResultSourceSinkInfo.class.desiredAssertionStatus();
    }

    public AbstractResultSourceSinkInfo() {
        this.stmt = null;
        this.definition = null;
        this.accessPath = null;
        this.userData = null;
    }

    public AbstractResultSourceSinkInfo(ISourceSinkDefinition definition, AccessPath accessPath, Stmt stmt) {
        this(definition, accessPath, stmt, null);
    }

    public AbstractResultSourceSinkInfo(ISourceSinkDefinition definition, AccessPath accessPath, Stmt stmt, Object userData) {
        if (!$assertionsDisabled && accessPath == null) {
            throw new AssertionError();
        }
        this.definition = definition;
        this.accessPath = accessPath;
        this.stmt = stmt;
        this.userData = userData;
    }

    public ISourceSinkDefinition getDefinition() {
        return this.definition;
    }

    public AccessPath getAccessPath() {
        return this.accessPath;
    }

    public Stmt getStmt() {
        return this.stmt;
    }

    public Object getUserData() {
        return this.userData;
    }

    public int hashCode() {
        int result = InfoflowConfiguration.getOneResultPerAccessPath() ? 31 * this.accessPath.hashCode() : 0;
        return (31 * ((31 * ((31 * result) + (this.definition == null ? 0 : this.definition.hashCode()))) + (this.stmt == null ? 0 : this.stmt.hashCode()))) + (this.userData == null ? 0 : this.userData.hashCode());
    }

    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractResultSourceSinkInfo si = (AbstractResultSourceSinkInfo) o;
        if (InfoflowConfiguration.getOneResultPerAccessPath() && !this.accessPath.equals(si.accessPath)) {
            return false;
        }
        if (this.definition == null) {
            if (si.definition != null) {
                return false;
            }
        } else if (!this.definition.equals(si.definition)) {
            return false;
        }
        if (this.stmt == null) {
            if (si.stmt != null) {
                return false;
            }
        } else if (!this.stmt.equals(si.stmt)) {
            return false;
        }
        if (this.userData == null) {
            if (si.userData != null) {
                return false;
            }
            return true;
        } else if (!this.userData.equals(si.userData)) {
            return false;
        } else {
            return true;
        }
    }
}
