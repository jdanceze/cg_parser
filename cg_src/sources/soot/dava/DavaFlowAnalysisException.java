package soot.dava;
/* loaded from: gencallgraphv3.jar:soot/dava/DavaFlowAnalysisException.class */
public class DavaFlowAnalysisException extends DecompilationException {
    public DavaFlowAnalysisException() {
    }

    public DavaFlowAnalysisException(String message) {
        System.out.println("There was an Error During the Structural Flow Analysis in Dava");
        System.out.println(message);
        report();
    }
}
