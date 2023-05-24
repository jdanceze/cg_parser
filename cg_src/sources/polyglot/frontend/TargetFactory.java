package polyglot.frontend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import polyglot.main.Report;
import polyglot.util.InternalCompilerError;
import polyglot.util.UnicodeWriter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/TargetFactory.class */
public class TargetFactory {
    File outputDirectory;
    String outputExtension;
    boolean outputStdout;

    public TargetFactory(File outDir, String outExt, boolean so) {
        this.outputDirectory = outDir;
        this.outputExtension = outExt;
        this.outputStdout = so;
    }

    public File getOutputDirectory() {
        return this.outputDirectory;
    }

    public Writer outputWriter(String packageName, String className, Source source) throws IOException {
        return outputWriter(outputFile(packageName, className, source));
    }

    public Writer outputWriter(File outputFile) throws IOException {
        if (Report.should_report(Report.frontend, 2)) {
            Report.report(2, new StringBuffer().append("Opening ").append(outputFile).append(" for output.").toString());
        }
        if (this.outputStdout) {
            return new UnicodeWriter(new PrintWriter(System.out));
        }
        if (!outputFile.getParentFile().exists()) {
            File parent = outputFile.getParentFile();
            parent.mkdirs();
        }
        return new UnicodeWriter(new FileWriter(outputFile));
    }

    public File outputFile(String packageName, Source source) {
        String name = new File(source.name()).getName();
        return outputFile(packageName, name.substring(0, name.lastIndexOf(46)), source);
    }

    public File outputFile(String packageName, String className, Source source) {
        if (this.outputDirectory == null) {
            throw new InternalCompilerError("Output directory not set.");
        }
        if (packageName == null) {
            packageName = "";
        }
        File outputFile = new File(this.outputDirectory, new StringBuffer().append(packageName.replace('.', File.separatorChar)).append(File.separatorChar).append(className).append(".").append(this.outputExtension).toString());
        if (source != null && outputFile.getPath().equals(source.path())) {
            throw new InternalCompilerError("The output file is the same as the source file");
        }
        return outputFile;
    }

    public String headerNameForFileName(String filename) {
        String s;
        int dotIdx = filename.lastIndexOf(".");
        if (dotIdx < 0) {
            s = new StringBuffer().append(filename).append(".h").toString();
        } else {
            s = new StringBuffer().append(filename.substring(0, dotIdx + 1)).append("h").toString();
        }
        return s;
    }
}
