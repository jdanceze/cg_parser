package org.junit.runner;

import junit.framework.Test;
import junit.runner.Version;
import org.junit.internal.JUnitSystem;
import org.junit.internal.RealSystem;
import org.junit.internal.TextListener;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/JUnitCore.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/JUnitCore.class */
public class JUnitCore {
    private final RunNotifier notifier = new RunNotifier();

    public static void main(String... args) {
        Result result = new JUnitCore().runMain(new RealSystem(), args);
        System.exit(result.wasSuccessful() ? 0 : 1);
    }

    public static Result runClasses(Class<?>... classes) {
        return runClasses(defaultComputer(), classes);
    }

    public static Result runClasses(Computer computer, Class<?>... classes) {
        return new JUnitCore().run(computer, classes);
    }

    Result runMain(JUnitSystem system, String... args) {
        system.out().println("JUnit version " + Version.id());
        JUnitCommandLineParseResult jUnitCommandLineParseResult = JUnitCommandLineParseResult.parse(args);
        RunListener listener = new TextListener(system);
        addListener(listener);
        return run(jUnitCommandLineParseResult.createRequest(defaultComputer()));
    }

    public String getVersion() {
        return Version.id();
    }

    public Result run(Class<?>... classes) {
        return run(defaultComputer(), classes);
    }

    public Result run(Computer computer, Class<?>... classes) {
        return run(Request.classes(computer, classes));
    }

    public Result run(Request request) {
        return run(request.getRunner());
    }

    public Result run(Test test) {
        return run(new JUnit38ClassRunner(test));
    }

    public Result run(Runner runner) {
        Result result = new Result();
        RunListener listener = result.createListener();
        this.notifier.addFirstListener(listener);
        try {
            this.notifier.fireTestRunStarted(runner.getDescription());
            runner.run(this.notifier);
            this.notifier.fireTestRunFinished(result);
            removeListener(listener);
            return result;
        } catch (Throwable th) {
            removeListener(listener);
            throw th;
        }
    }

    public void addListener(RunListener listener) {
        this.notifier.addListener(listener);
    }

    public void removeListener(RunListener listener) {
        this.notifier.removeListener(listener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Computer defaultComputer() {
        return new Computer();
    }
}
