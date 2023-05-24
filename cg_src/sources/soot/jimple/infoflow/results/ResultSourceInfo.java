package soot.jimple.infoflow.results;

import java.util.Arrays;
import java.util.List;
import soot.jimple.Stmt;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.tagkit.LineNumberTag;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/ResultSourceInfo.class */
public class ResultSourceInfo extends AbstractResultSourceSinkInfo {
    private final Stmt[] path;
    private final AccessPath[] pathAPs;
    private final Stmt[] pathCallSites;
    private transient boolean pathAgnosticResults;

    public ResultSourceInfo() {
        this.pathAgnosticResults = true;
        this.path = null;
        this.pathAPs = null;
        this.pathCallSites = null;
    }

    public ResultSourceInfo(ISourceSinkDefinition definition, AccessPath source, Stmt context, boolean pathAgnosticResults) {
        super(definition, source, context);
        this.pathAgnosticResults = true;
        this.path = null;
        this.pathAPs = null;
        this.pathCallSites = null;
        this.pathAgnosticResults = pathAgnosticResults;
    }

    public ResultSourceInfo(ISourceSinkDefinition definition, AccessPath source, Stmt context, Object userData, List<Stmt> path, List<AccessPath> pathAPs, List<Stmt> pathCallSites, boolean pathAgnosticResults) {
        super(definition, source, context, userData);
        this.pathAgnosticResults = true;
        this.path = (path == null || path.isEmpty()) ? null : (Stmt[]) path.toArray(new Stmt[path.size()]);
        this.pathAPs = (pathAPs == null || pathAPs.isEmpty()) ? null : (AccessPath[]) pathAPs.toArray(new AccessPath[pathAPs.size()]);
        this.pathCallSites = (pathCallSites == null || pathCallSites.isEmpty()) ? null : (Stmt[]) pathCallSites.toArray(new Stmt[pathCallSites.size()]);
        this.pathAgnosticResults = pathAgnosticResults;
    }

    public Stmt[] getPath() {
        return this.path;
    }

    public AccessPath[] getPathAccessPaths() {
        return this.pathAPs;
    }

    public Stmt[] getPathCallSites() {
        return this.pathCallSites;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.stmt.toString());
        if (this.stmt.hasTag(LineNumberTag.NAME)) {
            sb.append(" on line ").append(((LineNumberTag) this.stmt.getTag(LineNumberTag.NAME)).getLineNumber());
        }
        return sb.toString();
    }

    @Override // soot.jimple.infoflow.results.AbstractResultSourceSinkInfo
    public int hashCode() {
        int result = super.hashCode();
        if (!this.pathAgnosticResults) {
            if (this.path != null) {
                result += 31 * Arrays.hashCode(this.path);
            }
            if (this.pathAPs != null) {
                result += 31 * Arrays.hashCode(this.pathAPs);
            }
            if (this.pathCallSites != null) {
                result += 31 * Arrays.hashCode(this.pathCallSites);
            }
        }
        return result;
    }

    @Override // soot.jimple.infoflow.results.AbstractResultSourceSinkInfo
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        ResultSourceInfo other = (ResultSourceInfo) obj;
        if (!this.pathAgnosticResults) {
            if (!Arrays.equals(this.path, other.path) || !Arrays.equals(this.pathAPs, other.pathAPs) || !Arrays.equals(this.pathCallSites, other.pathCallSites)) {
                return false;
            }
            return true;
        }
        return true;
    }
}
