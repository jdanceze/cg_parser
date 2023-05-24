package soot.jimple.infoflow.results;

import soot.jimple.Stmt;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
import soot.tagkit.LineNumberTag;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/ResultSinkInfo.class */
public class ResultSinkInfo extends AbstractResultSourceSinkInfo {
    public ResultSinkInfo() {
    }

    public ResultSinkInfo(ISourceSinkDefinition definition, AccessPath sink, Stmt context) {
        super(definition, sink, context);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.stmt == null ? this.accessPath.toString() : this.stmt.toString());
        if (this.stmt != null && this.stmt.hasTag(LineNumberTag.NAME)) {
            sb.append(" on line ").append(((LineNumberTag) this.stmt.getTag(LineNumberTag.NAME)).getLineNumber());
        }
        return sb.toString();
    }
}
