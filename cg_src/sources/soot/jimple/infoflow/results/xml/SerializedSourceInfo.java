package soot.jimple.infoflow.results.xml;

import java.util.ArrayList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/xml/SerializedSourceInfo.class */
public class SerializedSourceInfo extends AbstractSerializedSourceSink {
    private List<SerializedPathElement> propagationPath;
    private final String methodSourceSinkDefinition;

    @Override // soot.jimple.infoflow.results.xml.AbstractSerializedSourceSink
    public /* bridge */ /* synthetic */ String getStatement() {
        return super.getStatement();
    }

    @Override // soot.jimple.infoflow.results.xml.AbstractSerializedSourceSink
    public /* bridge */ /* synthetic */ SerializedAccessPath getAccessPath() {
        return super.getAccessPath();
    }

    @Override // soot.jimple.infoflow.results.xml.AbstractSerializedSourceSink
    public /* bridge */ /* synthetic */ String getMethod() {
        return super.getMethod();
    }

    SerializedSourceInfo(SerializedAccessPath accessPath, String statement, String method, String methodSourceSinkDefinition) {
        this(accessPath, statement, method, null, methodSourceSinkDefinition);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SerializedSourceInfo(SerializedAccessPath accessPath, String statement, String method, List<SerializedPathElement> propagationPath, String methodSourceSinkDefinition) {
        super(accessPath, statement, method);
        this.propagationPath = null;
        this.propagationPath = propagationPath;
        this.methodSourceSinkDefinition = methodSourceSinkDefinition;
    }

    void addPathElement(SerializedPathElement pathElement) {
        if (this.propagationPath == null) {
            this.propagationPath = new ArrayList();
        }
        this.propagationPath.add(pathElement);
    }

    @Override // soot.jimple.infoflow.results.xml.AbstractSerializedSourceSink
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.propagationPath == null ? 0 : this.propagationPath.hashCode());
    }

    @Override // soot.jimple.infoflow.results.xml.AbstractSerializedSourceSink
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        SerializedSourceInfo other = (SerializedSourceInfo) obj;
        if (this.propagationPath == null) {
            if (other.propagationPath != null) {
                return false;
            }
            return true;
        } else if (!this.propagationPath.equals(other.propagationPath)) {
            return false;
        } else {
            return true;
        }
    }

    public List<SerializedPathElement> getPropagationPath() {
        return this.propagationPath;
    }

    public String getMethodSourceSinkDefinition() {
        return this.methodSourceSinkDefinition;
    }
}
