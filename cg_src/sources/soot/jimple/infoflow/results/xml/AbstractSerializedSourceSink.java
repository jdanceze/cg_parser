package soot.jimple.infoflow.results.xml;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/xml/AbstractSerializedSourceSink.class */
class AbstractSerializedSourceSink {
    private final SerializedAccessPath accessPath;
    private final String statement;
    private final String method;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractSerializedSourceSink(SerializedAccessPath ap, String statement, String method) {
        this.accessPath = ap;
        this.statement = statement;
        this.method = method;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.accessPath == null ? 0 : this.accessPath.hashCode());
        return (31 * ((31 * result) + (this.statement == null ? 0 : this.statement.hashCode()))) + (this.method == null ? 0 : this.method.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractSerializedSourceSink other = (AbstractSerializedSourceSink) obj;
        if (this.accessPath == null) {
            if (other.accessPath != null) {
                return false;
            }
        } else if (!this.accessPath.equals(other.accessPath)) {
            return false;
        }
        if (this.statement == null) {
            if (other.statement != null) {
                return false;
            }
        } else if (!this.statement.equals(other.statement)) {
            return false;
        }
        if (this.method == null) {
            if (other.method != null) {
                return false;
            }
            return true;
        } else if (!this.method.equals(other.method)) {
            return false;
        } else {
            return true;
        }
    }

    public SerializedAccessPath getAccessPath() {
        return this.accessPath;
    }

    public String getStatement() {
        return this.statement;
    }

    public String getMethod() {
        return this.method;
    }
}
