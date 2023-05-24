package org.mockito.internal.debugging;

import java.io.PrintStream;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/debugging/VerboseMockInvocationLogger.class */
public class VerboseMockInvocationLogger implements InvocationListener {
    final PrintStream printStream;
    private int mockInvocationsCounter;

    public VerboseMockInvocationLogger() {
        this(System.out);
    }

    public VerboseMockInvocationLogger(PrintStream printStream) {
        this.mockInvocationsCounter = 0;
        this.printStream = printStream;
    }

    @Override // org.mockito.listeners.InvocationListener
    public void reportInvocation(MethodInvocationReport methodInvocationReport) {
        printHeader();
        printStubInfo(methodInvocationReport);
        printInvocation(methodInvocationReport.getInvocation());
        printReturnedValueOrThrowable(methodInvocationReport);
        printFooter();
    }

    private void printReturnedValueOrThrowable(MethodInvocationReport methodInvocationReport) {
        String str;
        String str2;
        if (methodInvocationReport.threwException()) {
            if (methodInvocationReport.getThrowable().getMessage() == null) {
                str2 = "";
            } else {
                str2 = " with message " + methodInvocationReport.getThrowable().getMessage();
            }
            String message = str2;
            printlnIndented("has thrown: " + methodInvocationReport.getThrowable().getClass() + message);
            return;
        }
        if (methodInvocationReport.getReturnedValue() == null) {
            str = "";
        } else {
            str = " (" + methodInvocationReport.getReturnedValue().getClass().getName() + ")";
        }
        String type = str;
        printlnIndented("has returned: \"" + methodInvocationReport.getReturnedValue() + "\"" + type);
    }

    private void printStubInfo(MethodInvocationReport methodInvocationReport) {
        if (methodInvocationReport.getLocationOfStubbing() != null) {
            printlnIndented("stubbed: " + methodInvocationReport.getLocationOfStubbing());
        }
    }

    private void printHeader() {
        this.mockInvocationsCounter++;
        this.printStream.println("############ Logging method invocation #" + this.mockInvocationsCounter + " on mock/spy ########");
    }

    private void printInvocation(DescribedInvocation invocation) {
        this.printStream.println(invocation.toString());
        printlnIndented("invoked: " + invocation.getLocation().toString());
    }

    private void printFooter() {
        this.printStream.println("");
    }

    private void printlnIndented(String message) {
        this.printStream.println("   " + message);
    }
}
