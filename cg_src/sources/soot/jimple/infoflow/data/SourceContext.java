package soot.jimple.infoflow.data;

import soot.jimple.Stmt;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/SourceContext.class */
public class SourceContext implements Cloneable {
    protected final ISourceSinkDefinition definition;
    protected final AccessPath accessPath;
    protected final Stmt stmt;
    protected final Object userData;
    private int hashCode;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SourceContext.class.desiredAssertionStatus();
    }

    public SourceContext(ISourceSinkDefinition definition, AccessPath accessPath, Stmt stmt) {
        this(definition, accessPath, stmt, null);
    }

    public SourceContext(ISourceSinkDefinition definition, AccessPath accessPath, Stmt stmt, Object userData) {
        this.hashCode = 0;
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
        if (this.hashCode != 0) {
            return this.hashCode;
        }
        int result = (31 * 1) + (this.definition == null ? 0 : this.definition.hashCode());
        this.hashCode = (31 * ((31 * ((31 * result) + (this.stmt == null ? 0 : this.stmt.hashCode()))) + (this.accessPath == null ? 0 : this.accessPath.hashCode()))) + (this.userData == null ? 0 : this.userData.hashCode());
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SourceContext other = (SourceContext) obj;
        if (this.hashCode != 0 && other.hashCode != 0 && this.hashCode != other.hashCode) {
            return false;
        }
        if (this.definition == null) {
            if (other.definition != null) {
                return false;
            }
        } else if (!this.definition.equals(other.definition)) {
            return false;
        }
        if (this.stmt == null) {
            if (other.stmt != null) {
                return false;
            }
        } else if (!this.stmt.equals(other.stmt)) {
            return false;
        }
        if (this.accessPath == null) {
            if (other.accessPath != null) {
                return false;
            }
        } else if (!this.accessPath.equals(other.accessPath)) {
            return false;
        }
        if (this.userData == null) {
            if (other.userData != null) {
                return false;
            }
            return true;
        } else if (!this.userData.equals(other.userData)) {
            return false;
        } else {
            return true;
        }
    }

    @Override // 
    /* renamed from: clone */
    public SourceContext mo2740clone() {
        SourceContext sc = new SourceContext(this.definition, this.accessPath, this.stmt, this.userData);
        if ($assertionsDisabled || sc.equals(this)) {
            return sc;
        }
        throw new AssertionError();
    }

    public String toString() {
        return String.valueOf(this.accessPath.toString()) + (this.stmt == null ? "" : " in " + this.stmt.toString());
    }
}
