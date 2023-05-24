package polyglot.main;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.ant.launch.Launcher;
import org.apache.tools.ant.taskdefs.optional.sos.SOSCmd;
import polyglot.frontend.ExtensionInfo;
import polyglot.main.Main;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/main/Options.class */
public class Options {
    public static Options global;
    protected ExtensionInfo extension;
    public Collection source_path;
    public File output_directory;
    public String default_classpath;
    public String classpath;
    public String post_compiler;
    public int error_count = 100;
    public String bootclasspath = null;
    public boolean assertions = false;
    public String[] source_ext = null;
    public String output_ext = "java";
    public boolean output_stdout = false;
    public int output_width = 120;
    public boolean fully_qualified_names = false;
    public boolean serialize_type_info = true;
    public Set dump_ast = new HashSet();
    public Set print_ast = new HashSet();
    public Set disable_passes = new HashSet();
    public boolean keep_output_files = true;
    protected int USAGE_SCREEN_WIDTH = 76;
    protected int USAGE_FLAG_WIDTH = 27;
    protected int USAGE_SUBSECTION_INDENT = 8;

    public boolean cppBackend() {
        return false;
    }

    public Options(ExtensionInfo extension) {
        this.extension = null;
        this.extension = extension;
        setDefaultValues();
    }

    public void setDefaultValues() {
        String default_bootpath = System.getProperty("sun.boot.class.path");
        if (default_bootpath == null) {
            default_bootpath = new StringBuffer().append(System.getProperty("java.home")).append(File.separator).append("jre").append(File.separator).append(Launcher.ANT_PRIVATELIB).append(File.separator).append("rt.jar").toString();
        }
        this.default_classpath = new StringBuffer().append(System.getProperty("java.class.path")).append(File.pathSeparator).append(default_bootpath).toString();
        this.classpath = this.default_classpath;
        String java_home = System.getProperty("java.home");
        String current_dir = System.getProperty("user.dir");
        this.source_path = new LinkedList();
        this.source_path.add(new File(current_dir));
        this.output_directory = new File(current_dir);
        this.post_compiler = new StringBuffer().append(java_home).append(File.separator).append("..").append(File.separator).append("bin").append(File.separator).append("javac").toString();
        if (!new File(this.post_compiler).exists()) {
            this.post_compiler = new StringBuffer().append(java_home).append(File.separator).append("bin").append(File.separator).append("javac").toString();
            if (!new File(this.post_compiler).exists()) {
                this.post_compiler = "javac";
            }
        }
    }

    public void parseCommandLine(String[] args, Set source) throws UsageError {
        if (args.length < 1) {
            throw new UsageError("No command line arguments given");
        }
        int i = 0;
        while (i < args.length) {
            try {
                int ni = parseCommand(args, i, source);
                if (ni == i) {
                    throw new UsageError(new StringBuffer().append("illegal option -- ").append(args[i]).toString());
                }
                i = ni;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new UsageError("missing argument");
            }
        }
        if (source.size() < 1) {
            throw new UsageError("must specify at least one source file");
        }
    }

    protected int parseCommand(String[] args, int index, Set source) throws UsageError, Main.TerminationException {
        int i = index;
        if (args[i].equals("-h") || args[i].equals("-help") || args[i].equals("--help")) {
            throw new UsageError("", 0);
        }
        if (args[i].equals("-version")) {
            StringBuffer sb = new StringBuffer();
            if (this.extension != null) {
                sb.append(new StringBuffer().append(this.extension.compilerName()).append(" version ").append(this.extension.version()).append("\n").toString());
            }
            sb.append(new StringBuffer().append("Polyglot compiler toolkit version ").append(new polyglot.ext.jl.Version()).toString());
            throw new Main.TerminationException(sb.toString(), 0);
        }
        if (args[i].equals("-d")) {
            int i2 = i + 1;
            this.output_directory = new File(args[i2]);
            i = i2 + 1;
        } else if (args[i].equals("-classpath") || args[i].equals("-cp")) {
            int i3 = i + 1;
            this.classpath = new StringBuffer().append(args[i3]).append(System.getProperty("path.separator")).append(this.default_classpath).toString();
            i = i3 + 1;
        } else if (args[i].equals("-bootclasspath")) {
            int i4 = i + 1;
            this.bootclasspath = args[i4];
            i = i4 + 1;
        } else if (args[i].equals("-sourcepath")) {
            int i5 = i + 1;
            StringTokenizer st = new StringTokenizer(args[i5], File.pathSeparator);
            while (st.hasMoreTokens()) {
                File f = new File(st.nextToken());
                if (f != null && !this.source_path.contains(f)) {
                    this.source_path.add(f);
                }
            }
            i = i5 + 1;
        } else if (args[i].equals("-assert")) {
            i++;
            this.assertions = true;
        } else if (args[i].equals("-fqcn")) {
            i++;
            this.fully_qualified_names = true;
        } else if (args[i].equals("-c")) {
            this.post_compiler = null;
            i++;
        } else if (args[i].equals("-errors")) {
            int i6 = i + 1;
            try {
                this.error_count = Integer.parseInt(args[i6]);
            } catch (NumberFormatException e) {
            }
            i = i6 + 1;
        } else if (args[i].equals("-w")) {
            int i7 = i + 1;
            try {
                this.output_width = Integer.parseInt(args[i7]);
            } catch (NumberFormatException e2) {
            }
            i = i7 + 1;
        } else if (args[i].equals("-post")) {
            int i8 = i + 1;
            this.post_compiler = args[i8];
            i = i8 + 1;
        } else if (args[i].equals("-stdout")) {
            i++;
            this.output_stdout = true;
        } else if (args[i].equals("-sx")) {
            int i9 = i + 1;
            if (this.source_ext == null) {
                this.source_ext = new String[]{args[i9]};
            } else {
                String[] s = new String[this.source_ext.length + 1];
                System.arraycopy(this.source_ext, 0, s, 0, this.source_ext.length);
                s[s.length - 1] = args[i9];
                this.source_ext = s;
            }
            i = i9 + 1;
        } else if (args[i].equals("-ox")) {
            int i10 = i + 1;
            this.output_ext = args[i10];
            i = i10 + 1;
        } else if (args[i].equals("-noserial")) {
            i++;
            this.serialize_type_info = false;
        } else if (args[i].equals("-dump")) {
            int i11 = i + 1;
            String pass_name = args[i11];
            this.dump_ast.add(pass_name);
            i = i11 + 1;
        } else if (args[i].equals("-print")) {
            int i12 = i + 1;
            String pass_name2 = args[i12];
            this.print_ast.add(pass_name2);
            i = i12 + 1;
        } else if (args[i].equals("-disable")) {
            int i13 = i + 1;
            String pass_name3 = args[i13];
            this.disable_passes.add(pass_name3);
            i = i13 + 1;
        } else if (args[i].equals("-nooutput")) {
            i++;
            this.keep_output_files = false;
            this.output_width = 1000;
        } else if (args[i].equals("-v") || args[i].equals(SOSCmd.FLAG_VERBOSE)) {
            i++;
            Report.addTopic("verbose", 1);
        } else if (args[i].equals("-report")) {
            int i14 = i + 1;
            String str = args[i14];
            StringTokenizer st2 = new StringTokenizer(args[i14], "=");
            int level = 0;
            String topic = st2.hasMoreTokens() ? st2.nextToken() : "";
            if (st2.hasMoreTokens()) {
                try {
                    level = Integer.parseInt(st2.nextToken());
                } catch (NumberFormatException e3) {
                }
            }
            Report.addTopic(topic, level);
            i = i14 + 1;
        } else if (!args[i].startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
            source.add(args[i]);
            File f2 = new File(args[i]).getParentFile();
            if (f2 != null && !this.source_path.contains(f2)) {
                this.source_path.add(f2);
            }
            i++;
        }
        return i;
    }

    public void usage(PrintStream out) {
        out.println(new StringBuffer().append(HelpFormatter.DEFAULT_SYNTAX_PREFIX).append(this.extension.compilerName()).append(" [options] ").append("<source-file>.").append(this.extension.fileExtensions()[0]).append(" ...").toString());
        out.println("where [options] includes:");
        usageForFlag(out, "@<file>", "read options from <file>");
        usageForFlag(out, "-d <directory>", "output directory");
        usageForFlag(out, "-assert", "recognize the assert keyword");
        usageForFlag(out, "-sourcepath <path>", "source path");
        usageForFlag(out, "-bootclasspath <path>", "path for bootstrap class files");
        usageForFlag(out, "-ext <extension>", "use language extension");
        usageForFlag(out, "-extclass <ext-class>", "use language extension");
        usageForFlag(out, "-fqcn", "use fully-qualified class names");
        usageForFlag(out, "-sx <ext>", "set source extension");
        usageForFlag(out, "-ox <ext>", "set output extension");
        usageForFlag(out, "-errors <num>", "set the maximum number of errors");
        usageForFlag(out, "-w <num>", "set the maximum width of the .java output files");
        usageForFlag(out, "-dump <pass>", "dump the ast after pass <pass>");
        usageForFlag(out, "-print <pass>", "pretty-print the ast after pass <pass>");
        usageForFlag(out, "-disable <pass>", "disable pass <pass>");
        usageForFlag(out, "-noserial", "disable class serialization");
        usageForFlag(out, "-nooutput", "delete output files after compilation");
        usageForFlag(out, "-c", "compile only to .java");
        usageForFlag(out, "-post <compiler>", "run javac-like compiler after translation");
        usageForFlag(out, "-v -verbose", "print verbose debugging information");
        usageForFlag(out, "-report <topic>=<level>", "print verbose debugging information about topic at specified verbosity");
        StringBuffer allowedTopics = new StringBuffer("Allowed topics: ");
        Iterator iter = Report.topics.iterator();
        while (iter.hasNext()) {
            allowedTopics.append(iter.next().toString());
            if (iter.hasNext()) {
                allowedTopics.append(", ");
            }
        }
        usageSubsection(out, allowedTopics.toString());
        usageForFlag(out, "-version", "print version info");
        usageForFlag(out, "-h", "print this message");
    }

    protected void usageForFlag(PrintStream out, String flag, String description) {
        out.print("  ");
        out.print(flag);
        int cur = flag.length() + 2;
        if (cur < this.USAGE_FLAG_WIDTH) {
            printSpaces(out, this.USAGE_FLAG_WIDTH - cur);
        } else {
            out.println();
            printSpaces(out, this.USAGE_FLAG_WIDTH);
        }
        int cur2 = this.USAGE_FLAG_WIDTH;
        StringTokenizer st = new StringTokenizer(description);
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (cur2 + s.length() > this.USAGE_SCREEN_WIDTH) {
                out.println();
                printSpaces(out, this.USAGE_FLAG_WIDTH);
                cur2 = this.USAGE_FLAG_WIDTH;
            }
            out.print(s);
            cur2 += s.length();
            if (st.hasMoreTokens()) {
                if (cur2 + 1 > this.USAGE_SCREEN_WIDTH) {
                    out.println();
                    printSpaces(out, this.USAGE_FLAG_WIDTH);
                    cur2 = this.USAGE_FLAG_WIDTH;
                } else {
                    out.print(Instruction.argsep);
                    cur2++;
                }
            }
        }
        out.println();
    }

    protected void usageSubsection(PrintStream out, String text) {
        printSpaces(out, this.USAGE_SUBSECTION_INDENT);
        int cur = this.USAGE_SUBSECTION_INDENT;
        StringTokenizer st = new StringTokenizer(text);
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if (cur + s.length() > this.USAGE_SCREEN_WIDTH) {
                out.println();
                printSpaces(out, this.USAGE_SUBSECTION_INDENT);
                cur = this.USAGE_SUBSECTION_INDENT;
            }
            out.print(s);
            cur += s.length();
            if (st.hasMoreTokens()) {
                if (cur + 1 > this.USAGE_SCREEN_WIDTH) {
                    out.println();
                    printSpaces(out, this.USAGE_SUBSECTION_INDENT);
                    cur = this.USAGE_SUBSECTION_INDENT;
                } else {
                    out.print(' ');
                    cur++;
                }
            }
        }
        out.println();
    }

    protected static void printSpaces(PrintStream out, int n) {
        while (true) {
            int i = n;
            n = i - 1;
            if (i > 0) {
                out.print(' ');
            } else {
                return;
            }
        }
    }

    public String constructFullClasspath() {
        StringBuffer fullcp = new StringBuffer();
        if (this.bootclasspath != null) {
            fullcp.append(this.bootclasspath);
        }
        fullcp.append(this.classpath);
        return fullcp.toString();
    }

    public String constructPostCompilerClasspath() {
        return new StringBuffer().append(this.output_directory).append(File.pathSeparator).append(".").append(File.pathSeparator).append(System.getProperty("java.class.path")).toString();
    }
}
