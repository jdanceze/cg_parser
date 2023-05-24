package soot.jimple.infoflow.sourcesSinks.definitions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import soot.Local;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/StatementSourceSinkDefinition.class */
public class StatementSourceSinkDefinition extends AbstractSourceSinkDefinition implements IAccessPathBasedSourceSinkDefinition {
    private final Stmt stmt;
    private final Local local;
    private Set<AccessPathTuple> accessPaths;

    public StatementSourceSinkDefinition(Stmt stmt, Local local, Set<AccessPathTuple> accessPaths) {
        this.stmt = stmt;
        this.local = local;
        this.accessPaths = new HashSet(accessPaths);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition, soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public StatementSourceSinkDefinition getSourceOnlyDefinition() {
        Set<AccessPathTuple> newSet = null;
        if (this.accessPaths != null) {
            newSet = new HashSet<>(this.accessPaths.size());
            for (AccessPathTuple apt : this.accessPaths) {
                SourceSinkType ssType = apt.getSourceSinkType();
                if (ssType == SourceSinkType.Source) {
                    newSet.add(apt);
                } else if (ssType == SourceSinkType.Both) {
                    newSet.add(new AccessPathTuple(apt.getBaseType(), apt.getFields(), apt.getFieldTypes(), SourceSinkType.Source));
                }
            }
        }
        return buildNewDefinition(this.stmt, this.local, newSet);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition, soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public StatementSourceSinkDefinition getSinkOnlyDefinition() {
        Set<AccessPathTuple> newSet = null;
        if (this.accessPaths != null) {
            newSet = new HashSet<>(this.accessPaths.size());
            for (AccessPathTuple apt : this.accessPaths) {
                SourceSinkType ssType = apt.getSourceSinkType();
                if (ssType == SourceSinkType.Sink) {
                    newSet.add(apt);
                } else if (ssType == SourceSinkType.Both) {
                    newSet.add(new AccessPathTuple(apt.getBaseType(), apt.getFields(), apt.getFieldTypes(), SourceSinkType.Sink));
                }
            }
        }
        return buildNewDefinition(this.stmt, this.local, newSet);
    }

    public Stmt getStmt() {
        return this.stmt;
    }

    public Local getLocal() {
        return this.local;
    }

    public Set<AccessPathTuple> getAccessPaths() {
        return this.accessPaths;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition
    public void merge(ISourceSinkDefinition other) {
        if (other instanceof StatementSourceSinkDefinition) {
            StatementSourceSinkDefinition otherStmt = (StatementSourceSinkDefinition) other;
            if (otherStmt.accessPaths != null && !otherStmt.accessPaths.isEmpty()) {
                if (this.accessPaths == null) {
                    this.accessPaths = new HashSet();
                }
                for (AccessPathTuple apt : otherStmt.accessPaths) {
                    this.accessPaths.add(apt);
                }
            }
        }
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition, soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public boolean isEmpty() {
        return false;
    }

    public String toString() {
        return String.format("Local %s at %s", this.local, this.stmt);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public Set<AccessPathTuple> getAllAccessPaths() {
        return this.accessPaths;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public IAccessPathBasedSourceSinkDefinition filter(Collection<AccessPathTuple> toFilter) {
        Set<AccessPathTuple> filteredAPs = null;
        if (this.accessPaths != null && !this.accessPaths.isEmpty()) {
            filteredAPs = new HashSet<>(this.accessPaths.size());
            for (AccessPathTuple ap : this.accessPaths) {
                if (toFilter.contains(ap)) {
                    filteredAPs.add(ap);
                }
            }
        }
        StatementSourceSinkDefinition def = buildNewDefinition(this.stmt, this.local, filteredAPs);
        def.setCategory(this.category);
        return def;
    }

    protected StatementSourceSinkDefinition buildNewDefinition(Stmt stmt, Local local, Set<AccessPathTuple> accessPaths) {
        StatementSourceSinkDefinition sssd = new StatementSourceSinkDefinition(stmt, local, accessPaths);
        sssd.category = this.category;
        return sssd;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.AbstractSourceSinkDefinition
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * ((31 * result) + (this.accessPaths == null ? 0 : this.accessPaths.hashCode()))) + (this.local == null ? 0 : this.local.hashCode()))) + (this.stmt == null ? 0 : this.stmt.hashCode());
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.AbstractSourceSinkDefinition
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        StatementSourceSinkDefinition other = (StatementSourceSinkDefinition) obj;
        if (this.accessPaths == null) {
            if (other.accessPaths != null) {
                return false;
            }
        } else if (!this.accessPaths.equals(other.accessPaths)) {
            return false;
        }
        if (this.local == null) {
            if (other.local != null) {
                return false;
            }
        } else if (!this.local.equals(other.local)) {
            return false;
        }
        if (this.stmt == null) {
            if (other.stmt != null) {
                return false;
            }
            return true;
        } else if (!this.stmt.equals(other.stmt)) {
            return false;
        } else {
            return true;
        }
    }
}
