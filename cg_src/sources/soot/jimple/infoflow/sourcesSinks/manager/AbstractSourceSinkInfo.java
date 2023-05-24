package soot.jimple.infoflow.sourcesSinks.manager;

import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/manager/AbstractSourceSinkInfo.class */
public abstract class AbstractSourceSinkInfo {
    protected final ISourceSinkDefinition definition;
    protected final Object userData;

    public AbstractSourceSinkInfo(ISourceSinkDefinition definition) {
        this(definition, null);
    }

    public AbstractSourceSinkInfo(ISourceSinkDefinition definition, Object userData) {
        this.definition = definition;
        this.userData = userData;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.definition == null ? 0 : this.definition.hashCode());
        return (31 * result) + (this.userData == null ? 0 : this.userData.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SourceInfo other = (SourceInfo) obj;
        if (this.definition == null) {
            if (other.definition != null) {
                return false;
            }
        } else if (!this.definition.equals(other.definition)) {
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

    public Object getUserData() {
        return this.userData;
    }

    public ISourceSinkDefinition getDefinition() {
        return this.definition;
    }
}
