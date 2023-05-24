package net.bytebuddy.build;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/BuildLogger.class */
public interface BuildLogger {
    boolean isDebugEnabled();

    void debug(String str);

    void debug(String str, Throwable th);

    boolean isInfoEnabled();

    void info(String str);

    void info(String str, Throwable th);

    boolean isWarnEnabled();

    void warn(String str);

    void warn(String str, Throwable th);

    boolean isErrorEnabled();

    void error(String str);

    void error(String str, Throwable th);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/BuildLogger$NoOp.class */
    public enum NoOp implements BuildLogger {
        INSTANCE;

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isDebugEnabled() {
            return false;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void debug(String message) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void debug(String message, Throwable throwable) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isInfoEnabled() {
            return false;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void info(String message) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void info(String message, Throwable throwable) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isWarnEnabled() {
            return false;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void warn(String message) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void warn(String message, Throwable throwable) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isErrorEnabled() {
            return false;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void error(String message) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void error(String message, Throwable throwable) {
        }
    }

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/BuildLogger$Adapter.class */
    public static abstract class Adapter implements BuildLogger {
        @Override // net.bytebuddy.build.BuildLogger
        public boolean isDebugEnabled() {
            return false;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void debug(String message) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void debug(String message, Throwable throwable) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isInfoEnabled() {
            return false;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void info(String message) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void info(String message, Throwable throwable) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isWarnEnabled() {
            return false;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void warn(String message) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void warn(String message, Throwable throwable) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isErrorEnabled() {
            return false;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void error(String message) {
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void error(String message, Throwable throwable) {
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/BuildLogger$StreamWriting.class */
    public static class StreamWriting implements BuildLogger {
        private final PrintStream printStream;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.printStream.equals(((StreamWriting) obj).printStream);
        }

        public int hashCode() {
            return (17 * 31) + this.printStream.hashCode();
        }

        public StreamWriting(PrintStream printStream) {
            this.printStream = printStream;
        }

        public static BuildLogger toSystemOut() {
            return new StreamWriting(System.out);
        }

        public static BuildLogger toSystemError() {
            return new StreamWriting(System.err);
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isDebugEnabled() {
            return true;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void debug(String message) {
            this.printStream.print(message);
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void debug(String message, Throwable throwable) {
            synchronized (this.printStream) {
                this.printStream.print(message);
                throwable.printStackTrace(this.printStream);
            }
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isInfoEnabled() {
            return true;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void info(String message) {
            this.printStream.print(message);
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void info(String message, Throwable throwable) {
            synchronized (this.printStream) {
                this.printStream.print(message);
                throwable.printStackTrace(this.printStream);
            }
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isWarnEnabled() {
            return true;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void warn(String message) {
            this.printStream.print(message);
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void warn(String message, Throwable throwable) {
            synchronized (this.printStream) {
                this.printStream.print(message);
                throwable.printStackTrace(this.printStream);
            }
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isErrorEnabled() {
            return true;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void error(String message) {
            this.printStream.print(message);
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void error(String message, Throwable throwable) {
            synchronized (this.printStream) {
                this.printStream.print(message);
                throwable.printStackTrace(this.printStream);
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/build/BuildLogger$Compound.class */
    public static class Compound implements BuildLogger {
        private final List<BuildLogger> buildLoggers;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.buildLoggers.equals(((Compound) obj).buildLoggers);
        }

        public int hashCode() {
            return (17 * 31) + this.buildLoggers.hashCode();
        }

        public Compound(BuildLogger... buildLogger) {
            this(Arrays.asList(buildLogger));
        }

        public Compound(List<? extends BuildLogger> buildLoggers) {
            this.buildLoggers = new ArrayList();
            for (BuildLogger buildLogger : buildLoggers) {
                if (buildLogger instanceof Compound) {
                    this.buildLoggers.addAll(((Compound) buildLogger).buildLoggers);
                } else if (!(buildLogger instanceof NoOp)) {
                    this.buildLoggers.add(buildLogger);
                }
            }
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isDebugEnabled() {
            for (BuildLogger buildLogger : this.buildLoggers) {
                if (buildLogger.isDebugEnabled()) {
                    return true;
                }
            }
            return false;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void debug(String message) {
            for (BuildLogger buildLogger : this.buildLoggers) {
                if (buildLogger.isDebugEnabled()) {
                    buildLogger.debug(message);
                }
            }
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void debug(String message, Throwable throwable) {
            for (BuildLogger buildLogger : this.buildLoggers) {
                if (buildLogger.isDebugEnabled()) {
                    buildLogger.debug(message, throwable);
                }
            }
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isInfoEnabled() {
            for (BuildLogger buildLogger : this.buildLoggers) {
                if (buildLogger.isInfoEnabled()) {
                    return true;
                }
            }
            return false;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void info(String message) {
            for (BuildLogger buildLogger : this.buildLoggers) {
                if (buildLogger.isInfoEnabled()) {
                    buildLogger.info(message);
                }
            }
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void info(String message, Throwable throwable) {
            for (BuildLogger buildLogger : this.buildLoggers) {
                if (buildLogger.isInfoEnabled()) {
                    buildLogger.info(message, throwable);
                }
            }
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isWarnEnabled() {
            for (BuildLogger buildLogger : this.buildLoggers) {
                if (buildLogger.isWarnEnabled()) {
                    return true;
                }
            }
            return false;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void warn(String message) {
            for (BuildLogger buildLogger : this.buildLoggers) {
                if (buildLogger.isWarnEnabled()) {
                    buildLogger.warn(message);
                }
            }
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void warn(String message, Throwable throwable) {
            for (BuildLogger buildLogger : this.buildLoggers) {
                if (buildLogger.isWarnEnabled()) {
                    buildLogger.warn(message, throwable);
                }
            }
        }

        @Override // net.bytebuddy.build.BuildLogger
        public boolean isErrorEnabled() {
            for (BuildLogger buildLogger : this.buildLoggers) {
                if (buildLogger.isErrorEnabled()) {
                    return true;
                }
            }
            return false;
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void error(String message) {
            for (BuildLogger buildLogger : this.buildLoggers) {
                if (buildLogger.isErrorEnabled()) {
                    buildLogger.error(message);
                }
            }
        }

        @Override // net.bytebuddy.build.BuildLogger
        public void error(String message, Throwable throwable) {
            for (BuildLogger buildLogger : this.buildLoggers) {
                if (buildLogger.isErrorEnabled()) {
                    buildLogger.error(message, throwable);
                }
            }
        }
    }
}
