package polyglot.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import polyglot.frontend.Compiler;
import polyglot.frontend.ExtensionInfo;
import polyglot.util.ErrorQueue;
import polyglot.util.StdErrorQueue;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/main/Main.class */
public class Main {
    private Set source;
    public static final String verbose = "verbose";
    private static Collection timeTopics = new ArrayList(1);

    protected ExtensionInfo getExtensionInfo(List args) throws TerminationException {
        ExtensionInfo ext = null;
        Iterator i = args.iterator();
        while (i.hasNext()) {
            String s = (String) i.next();
            if (s.equals("-ext") || s.equals("-extension")) {
                if (ext != null) {
                    throw new TerminationException("only one extension can be specified");
                }
                i.remove();
                if (!i.hasNext()) {
                    throw new TerminationException("missing argument");
                }
                String extName = (String) i.next();
                i.remove();
                ext = loadExtension(new StringBuffer().append("polyglot.ext.").append(extName).append(".ExtensionInfo").toString());
            } else if (!s.equals("-extclass")) {
                continue;
            } else if (ext != null) {
                throw new TerminationException("only one extension can be specified");
            } else {
                i.remove();
                if (!i.hasNext()) {
                    throw new TerminationException("missing argument");
                }
                String extClass = (String) i.next();
                i.remove();
                ext = loadExtension(extClass);
            }
        }
        if (ext != null) {
            return ext;
        }
        return loadExtension("polyglot.ext.jl.ExtensionInfo");
    }

    public void start(String[] argv) throws TerminationException {
        start(argv, null);
    }

    public void start(String[] argv, ErrorQueue eq) throws TerminationException {
        this.source = new HashSet();
        List args = explodeOptions(argv);
        ExtensionInfo ext = getExtensionInfo(args);
        Options options = ext.getOptions();
        Options.global = options;
        try {
            String[] argv2 = (String[]) args.toArray(new String[0]);
            options.parseCommandLine(argv2, this.source);
            if (eq == null) {
                eq = new StdErrorQueue(System.err, options.error_count, ext.compilerName());
            }
            Compiler compiler = new Compiler(ext, eq);
            long time0 = System.currentTimeMillis();
            if (!compiler.compile(this.source)) {
                throw new TerminationException(1);
            }
            if (Report.should_report("verbose", 1)) {
                Report.report(1, new StringBuffer().append("Output files: ").append(compiler.outputFiles()).toString());
            }
            long start_time = System.currentTimeMillis();
            if (!invokePostCompiler(options, compiler, eq)) {
                throw new TerminationException(1);
            }
            if (Report.should_report("verbose", 1)) {
                reportTime(new StringBuffer().append("Finished compiling Java output files. time=").append(System.currentTimeMillis() - start_time).toString(), 1);
                reportTime(new StringBuffer().append("Total time=").append(System.currentTimeMillis() - time0).toString(), 1);
            }
        } catch (UsageError ue) {
            PrintStream out = ue.exitCode == 0 ? System.out : System.err;
            if (ue.getMessage() != null && ue.getMessage().length() > 0) {
                out.println(new StringBuffer().append(ext.compilerName()).append(": ").append(ue.getMessage()).toString());
            }
            options.usage(out);
            throw new TerminationException(ue.exitCode);
        }
    }

    protected boolean invokePostCompiler(Options options, Compiler compiler, ErrorQueue eq) {
        if (options.post_compiler != null && !options.output_stdout) {
            Runtime runtime = Runtime.getRuntime();
            StringBuffer outputFiles = new StringBuffer();
            for (String str : compiler.outputFiles()) {
                outputFiles.append(str);
                outputFiles.append(Instruction.argsep);
            }
            String command = new StringBuffer().append(options.post_compiler).append(" -classpath ").append(options.constructPostCompilerClasspath()).append(Instruction.argsep).append(outputFiles.toString()).toString();
            if (Report.should_report("verbose", 1)) {
                Report.report(1, new StringBuffer().append("Executing post-compiler ").append(command).toString());
            }
            try {
                Process proc = runtime.exec(command);
                InputStreamReader err = new InputStreamReader(proc.getErrorStream());
                char[] c = new char[72];
                StringBuffer sb = new StringBuffer();
                while (true) {
                    int len = err.read(c);
                    if (len <= 0) {
                        break;
                    }
                    sb.append(String.valueOf(c, 0, len));
                }
                if (sb.length() != 0) {
                    eq.enqueue(6, sb.toString());
                }
                err.close();
                proc.waitFor();
                if (!options.keep_output_files) {
                    String command2 = new StringBuffer().append("rm ").append(outputFiles.toString()).toString();
                    runtime.exec(command2);
                }
                if (proc.exitValue() > 0) {
                    eq.enqueue(6, new StringBuffer().append("Non-zero return code: ").append(proc.exitValue()).toString());
                    return false;
                }
                return true;
            } catch (Exception e) {
                eq.enqueue(6, e.getMessage());
                return false;
            }
        }
        return true;
    }

    private List explodeOptions(String[] args) throws TerminationException {
        LinkedList ll = new LinkedList();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("@")) {
                String fn = args[i].substring(1);
                try {
                    BufferedReader lr = new BufferedReader(new FileReader(fn));
                    LinkedList newArgs = new LinkedList();
                    while (true) {
                        String l = lr.readLine();
                        if (l == null) {
                            break;
                        }
                        StringTokenizer st = new StringTokenizer(l, Instruction.argsep);
                        while (st.hasMoreTokens()) {
                            newArgs.add(st.nextToken());
                        }
                    }
                    lr.close();
                    ll.addAll(newArgs);
                } catch (IOException e) {
                    throw new TerminationException(new StringBuffer().append("cmdline parser: couldn't read args file ").append(fn).toString());
                }
            } else {
                ll.add(args[i]);
            }
        }
        return ll;
    }

    public static final void main(String[] args) {
        try {
            new Main().start(args);
        } catch (TerminationException te) {
            if (te.getMessage() != null) {
                (te.exitCode == 0 ? System.out : System.err).println(te.getMessage());
            }
            System.exit(te.exitCode);
        }
    }

    static final ExtensionInfo loadExtension(String ext) throws TerminationException {
        if (ext != null && !ext.equals("")) {
            try {
                Class extClass = Class.forName(ext);
                try {
                    return (ExtensionInfo) extClass.newInstance();
                } catch (ClassCastException e) {
                    throw new TerminationException(new StringBuffer().append(ext).append(" is not a valid polyglot extension:").append(" extension class ").append(ext).append(" exists but is not a subclass of ExtensionInfo").toString());
                } catch (Exception e2) {
                    throw new TerminationException(new StringBuffer().append("Extension ").append(ext).append(" could not be loaded: could not instantiate ").append(ext).append(".").toString());
                }
            } catch (ClassNotFoundException e3) {
                throw new TerminationException(new StringBuffer().append("Extension ").append(ext).append(" not found: could not find class ").append(ext).append(".").append(e3.getMessage()).toString());
            }
        }
        return null;
    }

    static {
        timeTopics.add("time");
    }

    private static void reportTime(String msg, int level) {
        Report.report(level, msg);
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/main/Main$TerminationException.class */
    public static class TerminationException extends RuntimeException {
        public final int exitCode;

        public TerminationException(String msg) {
            this(msg, 1);
        }

        public TerminationException(int exit) {
            this.exitCode = exit;
        }

        public TerminationException(String msg, int exit) {
            super(msg);
            this.exitCode = exit;
        }
    }
}
