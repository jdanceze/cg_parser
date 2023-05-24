package soot;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import soot.Singletons;
import soot.coffi.Instruction;
import soot.options.CGOptions;
import soot.options.Options;
import soot.toolkits.astmetrics.ClassData;
/* loaded from: gencallgraphv3.jar:soot/Main.class */
public class Main {
    public static final String versionString;
    private Date start;
    private long startNano;
    private long finishNano;
    public String[] cmdLineArgs = new String[0];

    public Main(Singletons.Global g) {
    }

    public static Main v() {
        return G.v().soot_Main();
    }

    static {
        versionString = Main.class.getPackage().getImplementationVersion() == null ? "trunk" : Main.class.getPackage().getImplementationVersion();
    }

    private void printVersion() {
        System.out.println("Soot version " + versionString);
        System.out.println("Copyright (C) 1997-2010 Raja Vallee-Rai and others.");
        System.out.println("All rights reserved.");
        System.out.println();
        System.out.println("Contributions are copyright (C) 1997-2010 by their respective contributors.");
        System.out.println("See the file 'credits' for a list of contributors.");
        System.out.println("See individual source files for details.");
        System.out.println();
        System.out.println("Soot comes with ABSOLUTELY NO WARRANTY.  Soot is free software,");
        System.out.println("and you are welcome to redistribute it under certain conditions.");
        System.out.println("See the accompanying file 'COPYING-LESSER.txt' for details.");
        System.out.println("Visit the Soot website:");
        System.out.println("  http://www.sable.mcgill.ca/soot/");
        System.out.println("For a list of command line options, enter:");
        System.out.println("  java soot.Main --help");
    }

    private void processCmdLine(String[] args) {
        Options opts = Options.v();
        if (!opts.parse(args)) {
            throw new OptionsParseException("Option parse error");
        }
        if (PackManager.v().onlyStandardPacks()) {
            for (Pack pack : PackManager.v().allPacks()) {
                opts.warnForeignPhase(pack.getPhaseName());
                Iterator<Transform> it = pack.iterator();
                while (it.hasNext()) {
                    Transform tr = it.next();
                    opts.warnForeignPhase(tr.getPhaseName());
                }
            }
        }
        opts.warnNonexistentPhase();
        if (opts.help()) {
            System.out.println(opts.getUsage());
            throw new CompilationDeathException(1);
        } else if (opts.phase_list()) {
            System.out.println(opts.getPhaseList());
            throw new CompilationDeathException(1);
        } else if (!opts.phase_help().isEmpty()) {
            for (String phase : opts.phase_help()) {
                System.out.println(opts.getPhaseHelp(phase));
            }
            throw new CompilationDeathException(1);
        } else if ((!opts.unfriendly_mode() && args.length == 0) || opts.version()) {
            printVersion();
            throw new CompilationDeathException(1);
        } else {
            if (opts.on_the_fly()) {
                opts.set_whole_program(true);
                PhaseOptions.v().setPhaseOption("cg", "off");
            }
            postCmdLineCheck();
        }
    }

    private void postCmdLineCheck() {
        if (Options.v().classes().isEmpty() && Options.v().process_dir().isEmpty()) {
            throw new CompilationDeathException(0, "No input classes specified!");
        }
    }

    public static void main(String[] args) {
        try {
            v().run(args);
        } catch (OutOfMemoryError e) {
            System.err.println("Soot has run out of the memory allocated to it by the Java VM.");
            System.err.println("To allocate more memory to Soot, use the -Xmx switch to Java.");
            System.err.println("For example (for 2GB): java -Xmx2g soot.Main ...");
            throw e;
        } catch (StackOverflowError e2) {
            System.err.println("Soot has run out of stack memory.");
            System.err.println("To allocate more stack memory to Soot, use the -Xss switch to Java.");
            System.err.println("For example (for 2MB): java -Xss2m soot.Main ...");
            throw e2;
        } catch (OptionsParseException e3) {
        } catch (RuntimeException e4) {
            e4.printStackTrace();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            e4.printStackTrace(new PrintStream(bos));
            String stackStraceString = bos.toString();
            String body = "Steps to reproduce:\n1.) ...\n\nFiles used to reproduce: \n...\n\nSoot version: <pre>" + ((Object) escape(versionString)) + "</pre>\n\nCommand line:\n<pre>" + ((Object) escape(String.join(Instruction.argsep, args))) + "</pre>\n\nMax Memory:\n<pre>" + ((Object) escape(String.valueOf(Runtime.getRuntime().maxMemory() / FileUtils.ONE_MB) + "MB")) + "</pre>\n\nStack trace:\n<pre>" + ((Object) escape(stackStraceString)) + "</pre>";
            String title = String.valueOf(e4.getClass().getName()) + " when ...";
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("\n\nOuuups... something went wrong! Sorry about that.\n");
                sb.append("Follow these steps to fix the problem:\n");
                sb.append("1.) Are you sure you used the right command line?\n");
                sb.append("    Click here to double-check:\n");
                sb.append("    https://github.com/soot-oss/soot/wiki/Options-and-JavaDoc\n");
                sb.append('\n');
                sb.append("2.) Not sure whether it's a bug? Feel free to discuss\n");
                sb.append("    the issue on the Soot mailing list:\n");
                sb.append("    https://github.com/soot-oss/soot/wiki/Getting-help\n");
                sb.append('\n');
                sb.append("3.) Sure it's a bug? Click this link to report it.\n");
                sb.append("    https://github.com/soot-oss/soot/issues/new?title=").append(URLEncoder.encode(title, "UTF-8"));
                sb.append("&body=").append(URLEncoder.encode(body, "UTF-8")).append('\n');
                sb.append("    Please be as precise as possible when giving us\n");
                sb.append("    information on how to reproduce the problem. Thanks!");
                System.err.println(sb);
                System.exit(1);
            } catch (UnsupportedEncodingException e5) {
                System.exit(1);
            }
        }
    }

    private static CharSequence escape(CharSequence s) {
        int end = s.length();
        int start = 0;
        StringBuilder sb = new StringBuilder(32 + (end - 0));
        for (int i = 0; i < end; i++) {
            int c = s.charAt(i);
            switch (c) {
                case 34:
                case 38:
                case 39:
                case 60:
                case 62:
                    sb.append(s, start, i);
                    sb.append("&#");
                    sb.append(c);
                    sb.append(';');
                    start = i + 1;
                    break;
            }
        }
        return sb.append(s, start, end);
    }

    public void run(String[] args) {
        this.cmdLineArgs = args;
        this.start = new Date();
        this.startNano = System.nanoTime();
        try {
            Timers.v().totalTimer.start();
            processCmdLine(this.cmdLineArgs);
            autoSetOptions();
            System.out.println("Soot started on " + this.start);
            Scene.v().loadNecessaryClasses();
            if (Options.v().ast_metrics()) {
                Throwable th = null;
                try {
                    try {
                        OutputStream streamOut = new FileOutputStream("../astMetrics.xml");
                        try {
                            PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
                            writerOut.println("<?xml version='1.0'?>");
                            writerOut.println("<ASTMetrics>");
                            Iterator<ClassData> it = G.v().ASTMetricsData.iterator();
                            while (it.hasNext()) {
                                ClassData cData = it.next();
                                writerOut.println(cData);
                            }
                            writerOut.println("</ASTMetrics>");
                            writerOut.flush();
                            if (streamOut != null) {
                                streamOut.close();
                                return;
                            }
                            return;
                        } catch (Throwable th2) {
                            if (streamOut != null) {
                                streamOut.close();
                            }
                            throw th2;
                        }
                    } catch (Throwable th3) {
                        if (0 == 0) {
                            th = th3;
                        } else if (null != th3) {
                            th.addSuppressed(th3);
                        }
                        throw th;
                    }
                } catch (IOException e) {
                    throw new CompilationDeathException("Cannot output file astMetrics", e);
                }
            }
            PackManager.v().runPacks();
            if (!Options.v().oaat()) {
                PackManager.v().writeOutput();
            }
            Timers.v().totalTimer.end();
            if (Options.v().time()) {
                Timers.v().printProfilingInformation();
            }
            this.finishNano = System.nanoTime();
            System.out.println("Soot finished on " + new Date());
            long runtime = (this.finishNano - this.startNano) / 1000000;
            System.out.println("Soot has run for " + (runtime / 60000) + " min. " + ((runtime % 60000) / 1000) + " sec.");
        } catch (CompilationDeathException e2) {
            Timers.v().totalTimer.end();
            if (e2.getStatus() != 1) {
                throw e2;
            }
        }
    }

    public void autoSetOptions() {
        Options opts = Options.v();
        if (opts.no_bodies_for_excluded()) {
            opts.set_allow_phantom_refs(true);
        }
        CGOptions cgOptions = new CGOptions(PhaseOptions.v().getPhaseOptions("cg"));
        String log = cgOptions.reflection_log();
        if (log != null && !log.isEmpty()) {
            opts.set_allow_phantom_refs(true);
        }
        if (opts.allow_phantom_refs()) {
            opts.set_wrong_staticness(3);
        }
        if (opts.java_version() != 1) {
            opts.set_derive_java_version(false);
        }
    }
}
