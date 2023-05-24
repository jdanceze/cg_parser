package soot.jimple.infoflow.results.xml;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/xml/SerializedSinkInfo.class */
public class SerializedSinkInfo extends AbstractSerializedSourceSink {
    private final String methodSourceSinkDefinition;

    @Override // soot.jimple.infoflow.results.xml.AbstractSerializedSourceSink
    public /* bridge */ /* synthetic */ String getStatement() {
        return super.getStatement();
    }

    @Override // soot.jimple.infoflow.results.xml.AbstractSerializedSourceSink
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // soot.jimple.infoflow.results.xml.AbstractSerializedSourceSink
    public /* bridge */ /* synthetic */ SerializedAccessPath getAccessPath() {
        return super.getAccessPath();
    }

    @Override // soot.jimple.infoflow.results.xml.AbstractSerializedSourceSink
    public /* bridge */ /* synthetic */ String getMethod() {
        return super.getMethod();
    }

    @Override // soot.jimple.infoflow.results.xml.AbstractSerializedSourceSink
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SerializedSinkInfo(SerializedAccessPath accessPath, String statement, String method, String methodSourceSinkDefinition) {
        super(accessPath, statement, method);
        this.methodSourceSinkDefinition = methodSourceSinkDefinition;
    }

    public String getMethodSourceSinkDefinition() {
        return this.methodSourceSinkDefinition;
    }
}
