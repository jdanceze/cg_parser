package soot.jimple.infoflow.results;

import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory;
import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/DataFlowResult.class */
public class DataFlowResult {
    private final ResultSourceInfo source;
    private final ResultSinkInfo sink;

    public DataFlowResult(ResultSourceInfo source, ResultSinkInfo sink) {
        this.source = source;
        this.sink = sink;
    }

    public ResultSourceInfo getSource() {
        return this.source;
    }

    public ResultSinkInfo getSink() {
        return this.sink;
    }

    public String getSourceCategoryID() {
        ISourceSinkCategory sourceCat;
        ISourceSinkDefinition sourceDef = this.source.getDefinition();
        if (sourceDef != null && (sourceCat = sourceDef.getCategory()) != null) {
            return sourceCat.getID();
        }
        return null;
    }

    public String getSinkCategoryID() {
        ISourceSinkCategory sinkCat;
        ISourceSinkDefinition sinkDef = this.sink.getDefinition();
        if (sinkDef != null && (sinkCat = sinkDef.getCategory()) != null) {
            return sinkCat.getID();
        }
        return null;
    }

    public String toString() {
        return this.source.toString() + " -> " + this.sink.toString();
    }

    public int hashCode() {
        int result = (31 * 1) + (this.sink == null ? 0 : this.sink.hashCode());
        return (31 * result) + (this.source == null ? 0 : this.source.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DataFlowResult other = (DataFlowResult) obj;
        if (this.sink == null) {
            if (other.sink != null) {
                return false;
            }
        } else if (!this.sink.equals(other.sink)) {
            return false;
        }
        if (this.source == null) {
            if (other.source != null) {
                return false;
            }
            return true;
        } else if (!this.source.equals(other.source)) {
            return false;
        } else {
            return true;
        }
    }
}
