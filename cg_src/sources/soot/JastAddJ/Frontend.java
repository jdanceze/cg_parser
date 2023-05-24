package soot.JastAddJ;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.tools.ant.taskdefs.optional.sos.SOSCmd;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Frontend.class */
public class Frontend {
    private static final Logger logger = LoggerFactory.getLogger(Frontend.class);
    protected Program program = new Program();

    protected Frontend() {
        this.program.state().reset();
    }

    public boolean process(String[] args, BytecodeReader reader, JavaParser parser) {
        this.program.initBytecodeReader(reader);
        this.program.initJavaParser(parser);
        initOptions();
        processArgs(args);
        Collection<String> files = this.program.options().files();
        if (this.program.options().hasOption("-version")) {
            printVersion();
            return true;
        } else if (this.program.options().hasOption("-help") || files.isEmpty()) {
            printUsage();
            return true;
        } else {
            try {
                for (String name : files) {
                    if (!new File(name).exists()) {
                        System.err.println("WARNING: file \"" + name + "\" does not exist");
                    }
                    this.program.addSourceFile(name);
                }
                Iterator iter = this.program.compilationUnitIterator();
                while (iter.hasNext()) {
                    CompilationUnit unit = (CompilationUnit) iter.next();
                    if (unit.fromSource()) {
                        Collection errors = unit.parseErrors();
                        Collection warnings = new LinkedList();
                        if (errors.isEmpty() || this.program.options().hasOption("-recover")) {
                            unit.errorCheck(errors, warnings);
                        }
                        if (!errors.isEmpty()) {
                            processErrors(errors, unit);
                            return false;
                        }
                        if (!warnings.isEmpty()) {
                            processWarnings(warnings, unit);
                        }
                        processNoErrors(unit);
                    }
                }
                return true;
            } catch (Throwable t) {
                System.err.println("Errors:");
                System.err.println("Fatal exception:");
                logger.error(t.getMessage(), t);
                return false;
            }
        }
    }

    protected void initOptions() {
        Options options = this.program.options();
        options.initOptions();
        options.addKeyOption("-version");
        options.addKeyOption("-print");
        options.addKeyOption("-g");
        options.addKeyOption("-g:none");
        options.addKeyOption("-g:lines,vars,source");
        options.addKeyOption("-nowarn");
        options.addKeyOption(SOSCmd.FLAG_VERBOSE);
        options.addKeyOption("-deprecation");
        options.addKeyValueOption("-classpath");
        options.addKeyValueOption("-cp");
        options.addKeyValueOption("-sourcepath");
        options.addKeyValueOption("-bootclasspath");
        options.addKeyValueOption("-extdirs");
        options.addKeyValueOption("-d");
        options.addKeyValueOption("-encoding");
        options.addKeyValueOption("-source");
        options.addKeyValueOption("-target");
        options.addKeyOption("-help");
        options.addKeyOption(MSVSSConstants.FLAG_OUTPUT);
        options.addKeyOption("-J-Xmx128M");
        options.addKeyOption("-recover");
    }

    protected void processArgs(String[] args) {
        this.program.options().addOptions(args);
    }

    protected void processErrors(Collection errors, CompilationUnit unit) {
        System.err.println("Errors:");
        for (Object obj : errors) {
            System.err.println(obj);
        }
    }

    protected void processWarnings(Collection warnings, CompilationUnit unit) {
        System.err.println("Warnings:");
        for (Object obj : warnings) {
            System.err.println(obj);
        }
    }

    protected void processNoErrors(CompilationUnit unit) {
    }

    protected void printUsage() {
        printLongVersion();
        System.out.println("\n" + name() + "\n\nUsage: java " + name() + " <options> <source files>\n  -verbose                  Output messages about what the compiler is doing\n  -classpath <path>         Specify where to find user class files\n  -sourcepath <path>        Specify where to find input source files\n  -bootclasspath <path>     Override location of bootstrap class files\n  -extdirs <dirs>           Override location of installed extensions\n  -d <directory>            Specify where to place generated class files\n  -help                     Print a synopsis of standard options\n  -version                  Print version information\n");
    }

    protected void printLongVersion() {
        System.out.println(String.valueOf(name()) + Instruction.argsep + url() + " Version " + version());
    }

    protected void printVersion() {
        System.out.println(String.valueOf(name()) + Instruction.argsep + version());
    }

    protected String name() {
        return "Java1.4Frontend";
    }

    protected String url() {
        return "(http://jastadd.cs.lth.se)";
    }

    protected String version() {
        return "R20070504";
    }
}
