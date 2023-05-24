package junit.textui;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.Enumeration;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestFailure;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.runner.BaseTestRunner;
import soot.coffi.Instruction;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:junit/textui/ResultPrinter.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:junit/textui/ResultPrinter.class */
public class ResultPrinter implements TestListener {
    PrintStream fWriter;
    int fColumn = 0;

    public ResultPrinter(PrintStream writer) {
        this.fWriter = writer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void print(TestResult result, long runTime) {
        printHeader(runTime);
        printErrors(result);
        printFailures(result);
        printFooter(result);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void printWaitPrompt() {
        getWriter().println();
        getWriter().println("<RETURN> to continue");
    }

    protected void printHeader(long runTime) {
        getWriter().println();
        getWriter().println("Time: " + elapsedTimeAsString(runTime));
    }

    protected void printErrors(TestResult result) {
        printDefects(result.errors(), result.errorCount(), "error");
    }

    protected void printFailures(TestResult result) {
        printDefects(result.failures(), result.failureCount(), "failure");
    }

    protected void printDefects(Enumeration<TestFailure> booBoos, int count, String type) {
        if (count == 0) {
            return;
        }
        if (count == 1) {
            getWriter().println("There was " + count + Instruction.argsep + type + ":");
        } else {
            getWriter().println("There were " + count + Instruction.argsep + type + "s:");
        }
        int i = 1;
        while (booBoos.hasMoreElements()) {
            printDefect(booBoos.nextElement(), i);
            i++;
        }
    }

    public void printDefect(TestFailure booBoo, int count) {
        printDefectHeader(booBoo, count);
        printDefectTrace(booBoo);
    }

    protected void printDefectHeader(TestFailure booBoo, int count) {
        getWriter().print(count + ") " + booBoo.failedTest());
    }

    protected void printDefectTrace(TestFailure booBoo) {
        getWriter().print(BaseTestRunner.getFilteredTrace(booBoo.trace()));
    }

    protected void printFooter(TestResult result) {
        if (result.wasSuccessful()) {
            getWriter().println();
            getWriter().print("OK");
            getWriter().println(" (" + result.runCount() + " test" + (result.runCount() == 1 ? "" : "s") + ")");
        } else {
            getWriter().println();
            getWriter().println("FAILURES!!!");
            getWriter().println("Tests run: " + result.runCount() + ",  Failures: " + result.failureCount() + ",  Errors: " + result.errorCount());
        }
        getWriter().println();
    }

    protected String elapsedTimeAsString(long runTime) {
        return NumberFormat.getInstance().format(runTime / 1000.0d);
    }

    public PrintStream getWriter() {
        return this.fWriter;
    }

    @Override // junit.framework.TestListener
    public void addError(Test test, Throwable e) {
        getWriter().print("E");
    }

    @Override // junit.framework.TestListener
    public void addFailure(Test test, AssertionFailedError t) {
        getWriter().print("F");
    }

    @Override // junit.framework.TestListener
    public void endTest(Test test) {
    }

    @Override // junit.framework.TestListener
    public void startTest(Test test) {
        getWriter().print(".");
        int i = this.fColumn;
        this.fColumn = i + 1;
        if (i >= 40) {
            getWriter().println();
            this.fColumn = 0;
        }
    }
}
