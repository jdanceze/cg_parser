package soot.jimple.infoflow.sourcesSinks.definitions;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/definitions/AbstractSourceSinkDefinition.class */
public abstract class AbstractSourceSinkDefinition implements ISourceSinkDefinition {
    protected ISourceSinkCategory category;

    public AbstractSourceSinkDefinition() {
    }

    public AbstractSourceSinkDefinition(ISourceSinkCategory category) {
        this.category = category;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition
    public void setCategory(ISourceSinkCategory category) {
        this.category = category;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition
    public ISourceSinkCategory getCategory() {
        return this.category;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.category == null ? 0 : this.category.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractSourceSinkDefinition other = (AbstractSourceSinkDefinition) obj;
        if (this.category == null) {
            if (other.category != null) {
                return false;
            }
            return true;
        } else if (!this.category.equals(other.category)) {
            return false;
        } else {
            return true;
        }
    }
}
