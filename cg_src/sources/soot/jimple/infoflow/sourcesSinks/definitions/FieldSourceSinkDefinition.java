package soot.jimple.infoflow.sourcesSinks.definitions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/FieldSourceSinkDefinition.class */
public class FieldSourceSinkDefinition extends AbstractSourceSinkDefinition implements IAccessPathBasedSourceSinkDefinition {
    protected final String fieldSignature;
    protected Set<AccessPathTuple> accessPaths;

    public FieldSourceSinkDefinition(String fieldSignature) {
        this(fieldSignature, null);
    }

    public FieldSourceSinkDefinition(String fieldSignature, Set<AccessPathTuple> accessPaths) {
        this.fieldSignature = fieldSignature;
        this.accessPaths = accessPaths;
    }

    public String getFieldSignature() {
        return this.fieldSignature;
    }

    public Set<AccessPathTuple> getAccessPaths() {
        return this.accessPaths;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition, soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public FieldSourceSinkDefinition getSourceOnlyDefinition() {
        Set<AccessPathTuple> sources = null;
        if (this.accessPaths != null) {
            sources = new HashSet<>(this.accessPaths.size());
            for (AccessPathTuple apt : this.accessPaths) {
                if (apt.getSourceSinkType().isSource()) {
                    sources.add(apt);
                }
            }
        }
        return buildNewDefinition(sources);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition, soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public FieldSourceSinkDefinition getSinkOnlyDefinition() {
        Set<AccessPathTuple> sinks = null;
        if (this.accessPaths != null) {
            sinks = new HashSet<>(this.accessPaths.size());
            for (AccessPathTuple apt : this.accessPaths) {
                if (apt.getSourceSinkType().isSink()) {
                    sinks.add(apt);
                }
            }
        }
        return buildNewDefinition(sinks);
    }

    protected FieldSourceSinkDefinition buildNewDefinition(Set<AccessPathTuple> accessPaths) {
        return buildNewDefinition(this.fieldSignature, accessPaths);
    }

    protected FieldSourceSinkDefinition buildNewDefinition(String fieldSignature, Set<AccessPathTuple> accessPaths) {
        FieldSourceSinkDefinition fssd = new FieldSourceSinkDefinition(fieldSignature, accessPaths);
        fssd.setCategory(this.category);
        return fssd;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition
    public void merge(ISourceSinkDefinition other) {
        if (other instanceof FieldSourceSinkDefinition) {
            FieldSourceSinkDefinition otherField = (FieldSourceSinkDefinition) other;
            if (otherField.accessPaths != null && !otherField.accessPaths.isEmpty()) {
                if (this.accessPaths == null) {
                    this.accessPaths = new HashSet();
                }
                for (AccessPathTuple apt : otherField.accessPaths) {
                    this.accessPaths.add(apt);
                }
            }
        }
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition, soot.jimple.infoflow.sourcesSinks.definitions.IAccessPathBasedSourceSinkDefinition
    public boolean isEmpty() {
        return this.accessPaths == null || this.accessPaths.isEmpty();
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
        FieldSourceSinkDefinition def = buildNewDefinition(this.fieldSignature, filteredAPs);
        def.category = this.category;
        return def;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.AbstractSourceSinkDefinition
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * result) + (this.accessPaths == null ? 0 : this.accessPaths.hashCode()))) + (this.fieldSignature == null ? 0 : this.fieldSignature.hashCode());
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.AbstractSourceSinkDefinition
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        FieldSourceSinkDefinition other = (FieldSourceSinkDefinition) obj;
        if (this.accessPaths == null) {
            if (other.accessPaths != null) {
                return false;
            }
        } else if (!this.accessPaths.equals(other.accessPaths)) {
            return false;
        }
        if (this.fieldSignature == null) {
            if (other.fieldSignature != null) {
                return false;
            }
            return true;
        } else if (!this.fieldSignature.equals(other.fieldSignature)) {
            return false;
        } else {
            return true;
        }
    }
}
