package org.junit.runner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.internal.Classes;
import org.junit.runner.FilterFactory;
import org.junit.runner.manipulation.Filter;
import org.junit.runners.model.InitializationError;
/* JADX INFO: Access modifiers changed from: package-private */
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/JUnitCommandLineParseResult.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/JUnitCommandLineParseResult.class */
public class JUnitCommandLineParseResult {
    private final List<String> filterSpecs = new ArrayList();
    private final List<Class<?>> classes = new ArrayList();
    private final List<Throwable> parserErrors = new ArrayList();

    JUnitCommandLineParseResult() {
    }

    public List<String> getFilterSpecs() {
        return Collections.unmodifiableList(this.filterSpecs);
    }

    public List<Class<?>> getClasses() {
        return Collections.unmodifiableList(this.classes);
    }

    public static JUnitCommandLineParseResult parse(String[] args) {
        JUnitCommandLineParseResult result = new JUnitCommandLineParseResult();
        result.parseArgs(args);
        return result;
    }

    private void parseArgs(String[] args) {
        parseParameters(parseOptions(args));
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x00d8, code lost:
        return new java.lang.String[0];
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    java.lang.String[] parseOptions(java.lang.String... r7) {
        /*
            Method dump skipped, instructions count: 217
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.junit.runner.JUnitCommandLineParseResult.parseOptions(java.lang.String[]):java.lang.String[]");
    }

    private String[] copyArray(String[] args, int from, int to) {
        String[] result = new String[to - from];
        for (int j = from; j != to; j++) {
            result[j - from] = args[j];
        }
        return result;
    }

    void parseParameters(String[] args) {
        for (String arg : args) {
            try {
                this.classes.add(Classes.getClass(arg));
            } catch (ClassNotFoundException e) {
                this.parserErrors.add(new IllegalArgumentException("Could not find class [" + arg + "]", e));
            }
        }
    }

    private Request errorReport(Throwable cause) {
        return Request.errorReport(JUnitCommandLineParseResult.class, cause);
    }

    public Request createRequest(Computer computer) {
        if (this.parserErrors.isEmpty()) {
            Request request = Request.classes(computer, (Class[]) this.classes.toArray(new Class[this.classes.size()]));
            return applyFilterSpecs(request);
        }
        return errorReport(new InitializationError(this.parserErrors));
    }

    private Request applyFilterSpecs(Request request) {
        try {
            for (String filterSpec : this.filterSpecs) {
                Filter filter = FilterFactories.createFilterFromFilterSpec(request, filterSpec);
                request = request.filterWith(filter);
            }
            return request;
        } catch (FilterFactory.FilterNotCreatedException e) {
            return errorReport(e);
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/JUnitCommandLineParseResult$CommandLineParserError.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/JUnitCommandLineParseResult$CommandLineParserError.class */
    public static class CommandLineParserError extends Exception {
        private static final long serialVersionUID = 1;

        public CommandLineParserError(String message) {
            super(message);
        }
    }
}
