package org.hamcrest.generator.qdox.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.hamcrest.generator.qdox.JavaDocBuilder;
import org.hamcrest.generator.qdox.directorywalker.DirectoryScanner;
import org.hamcrest.generator.qdox.directorywalker.FileVisitor;
import org.hamcrest.generator.qdox.directorywalker.SuffixFilter;
import org.hamcrest.generator.qdox.parser.ParseException;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/tools/QDoxTester.class */
public class QDoxTester {
    private final Reporter reporter;
    static Class class$com$thoughtworks$qdox$tools$QDoxTester;

    /* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/tools/QDoxTester$Reporter.class */
    public interface Reporter {
        void success(String str);

        void parseFailure(String str, int i, int i2, String str2);

        void error(String str, Throwable th);
    }

    public QDoxTester(Reporter reporter) {
        this.reporter = reporter;
    }

    public void checkZipOrJarFile(File file) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            InputStream inputStream = zipFile.getInputStream(zipEntry);
            try {
                verify(new StringBuffer().append(file.getName()).append("!").append(zipEntry.getName()).toString(), inputStream);
                inputStream.close();
            } catch (Throwable th) {
                inputStream.close();
                throw th;
            }
        }
    }

    public void checkDirectory(File dir) throws IOException {
        DirectoryScanner directoryScanner = new DirectoryScanner(dir);
        directoryScanner.addFilter(new SuffixFilter(".java"));
        directoryScanner.scan(new FileVisitor(this) { // from class: org.hamcrest.generator.qdox.tools.QDoxTester.1
            private final QDoxTester this$0;

            {
                this.this$0 = this;
            }

            @Override // org.hamcrest.generator.qdox.directorywalker.FileVisitor
            public void visitFile(File file) {
                try {
                    this.this$0.checkJavaFile(file);
                } catch (IOException e) {
                }
            }
        });
    }

    public void checkJavaFile(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        try {
            verify(file.getName(), inputStream);
            inputStream.close();
        } catch (Throwable th) {
            inputStream.close();
            throw th;
        }
    }

    private void verify(String id, InputStream inputStream) {
        try {
            JavaDocBuilder javaDocBuilder = new JavaDocBuilder();
            javaDocBuilder.addSource(new BufferedReader(new InputStreamReader(inputStream)));
            this.reporter.success(id);
        } catch (ParseException parseException) {
            this.reporter.parseFailure(id, parseException.getLine(), parseException.getColumn(), parseException.getMessage());
        } catch (Exception otherException) {
            this.reporter.error(id, otherException);
        }
    }

    public static void main(String[] args) throws IOException {
        Class cls;
        if (args.length == 0) {
            System.err.println("Tool that verifies that QDox can parse some Java source.");
            System.err.println();
            PrintStream printStream = System.err;
            StringBuffer append = new StringBuffer().append("Usage: java ");
            if (class$com$thoughtworks$qdox$tools$QDoxTester == null) {
                cls = class$("org.hamcrest.generator.qdox.tools.QDoxTester");
                class$com$thoughtworks$qdox$tools$QDoxTester = cls;
            } else {
                cls = class$com$thoughtworks$qdox$tools$QDoxTester;
            }
            printStream.println(append.append(cls.getName()).append(" src1 [src2] [src3]...").toString());
            System.err.println();
            System.err.println("Each src can be a single .java file, or a directory/zip/jar containing multiple source files");
            System.exit(-1);
        }
        ConsoleReporter reporter = new ConsoleReporter(System.out);
        QDoxTester qDoxTester = new QDoxTester(reporter);
        for (String str : args) {
            File file = new File(str);
            if (file.isDirectory()) {
                qDoxTester.checkDirectory(file);
            } else if (file.getName().endsWith(".java")) {
                qDoxTester.checkJavaFile(file);
            } else if (file.getName().endsWith(".jar") || file.getName().endsWith(".zip")) {
                qDoxTester.checkZipOrJarFile(file);
            } else {
                System.err.println(new StringBuffer().append("Unknown input <").append(file.getName()).append(">. Should be zip, jar, java or directory").toString());
            }
        }
        reporter.writeSummary();
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    /* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/tools/QDoxTester$ConsoleReporter.class */
    private static class ConsoleReporter implements Reporter {
        private final PrintStream out;
        private int success;
        private int failure;
        private int error;
        private int dotsWrittenThisLine;

        public ConsoleReporter(PrintStream out) {
            this.out = out;
        }

        @Override // org.hamcrest.generator.qdox.tools.QDoxTester.Reporter
        public void success(String id) {
            this.success++;
            int i = this.dotsWrittenThisLine + 1;
            this.dotsWrittenThisLine = i;
            if (i > 80) {
                newLine();
            }
            this.out.print('.');
        }

        private void newLine() {
            this.dotsWrittenThisLine = 0;
            this.out.println();
            this.out.flush();
        }

        @Override // org.hamcrest.generator.qdox.tools.QDoxTester.Reporter
        public void parseFailure(String id, int line, int column, String reason) {
            newLine();
            this.out.println(new StringBuffer().append("* ").append(id).toString());
            this.out.println(new StringBuffer().append("  [").append(line).append(":").append(column).append("] ").append(reason).toString());
            this.failure++;
        }

        @Override // org.hamcrest.generator.qdox.tools.QDoxTester.Reporter
        public void error(String id, Throwable throwable) {
            newLine();
            this.out.println(new StringBuffer().append("* ").append(id).toString());
            throwable.printStackTrace(this.out);
            this.error++;
        }

        public void writeSummary() {
            newLine();
            this.out.println("-- Summary --------------");
            this.out.println(new StringBuffer().append("Success: ").append(this.success).toString());
            this.out.println(new StringBuffer().append("Failure: ").append(this.failure).toString());
            this.out.println(new StringBuffer().append("Error  : ").append(this.error).toString());
            this.out.println(new StringBuffer().append("Total  : ").append(this.success + this.failure + this.error).toString());
            this.out.println("-------------------------");
        }
    }
}
