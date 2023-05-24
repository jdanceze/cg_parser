package soot.jimple.infoflow.sourcesSinks.manager;

import java.util.Collections;
import java.util.Set;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/manager/SourceInfo.class */
public class SourceInfo extends AbstractSourceSinkInfo {
    protected final Set<AccessPath> accessPaths;

    @Override // soot.jimple.infoflow.sourcesSinks.manager.AbstractSourceSinkInfo
    public /* bridge */ /* synthetic */ Object getUserData() {
        return super.getUserData();
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.AbstractSourceSinkInfo
    public /* bridge */ /* synthetic */ ISourceSinkDefinition getDefinition() {
        return super.getDefinition();
    }

    public SourceInfo(ISourceSinkDefinition definition, AccessPath ap) {
        this(definition, Collections.singleton(ap), (Object) null);
    }

    public SourceInfo(ISourceSinkDefinition definition, AccessPath ap, Object userData) {
        this(definition, Collections.singleton(ap), userData);
    }

    public SourceInfo(ISourceSinkDefinition definition, Set<AccessPath> bundle) {
        this(definition, bundle, (Object) null);
    }

    public SourceInfo(ISourceSinkDefinition definition, Set<AccessPath> bundle, Object userData) {
        super(definition, userData);
        this.accessPaths = bundle;
    }

    public Set<AccessPath> getAccessPaths() {
        return this.accessPaths;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.AbstractSourceSinkInfo
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.accessPaths == null ? 0 : this.accessPaths.hashCode());
    }

    @Override // soot.jimple.infoflow.sourcesSinks.manager.AbstractSourceSinkInfo
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        SourceInfo other = (SourceInfo) obj;
        if (this.accessPaths == null) {
            if (other.accessPaths != null) {
                return false;
            }
            return true;
        } else if (!this.accessPaths.equals(other.accessPaths)) {
            return false;
        } else {
            return true;
        }
    }
}
