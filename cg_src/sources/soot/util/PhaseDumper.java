package soot.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.Printer;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalGraph;
import soot.util.cfgcmd.CFGToDotGraph;
import soot.util.dot.DotGraph;
/* loaded from: gencallgraphv3.jar:soot/util/PhaseDumper.class */
public class PhaseDumper {
    private static final Logger logger = LoggerFactory.getLogger(PhaseDumper.class);
    private static final String ALL_WILDCARD = "ALL";
    private List<String> bodyDumpingPhases;
    private List<String> cfgDumpingPhases;
    private final PhaseStack phaseStack = new PhaseStack();
    private boolean alreadyDumping = false;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/util/PhaseDumper$PhaseStack.class */
    public class PhaseStack extends ArrayList<String> {
        private static final int initialCapacity = 4;
        private static final String EMPTY_STACK_PHASE_NAME = "NOPHASE";

        PhaseStack() {
            super(4);
        }

        String currentPhase() {
            if (isEmpty()) {
                return EMPTY_STACK_PHASE_NAME;
            }
            return get(size() - 1);
        }

        String pop() {
            return remove(size() - 1);
        }

        String push(String phaseName) {
            add(phaseName);
            return phaseName;
        }
    }

    public PhaseDumper(Singletons.Global g) {
        this.bodyDumpingPhases = null;
        this.cfgDumpingPhases = null;
        List<String> bodyPhases = Options.v().dump_body();
        if (!bodyPhases.isEmpty()) {
            this.bodyDumpingPhases = bodyPhases;
        }
        List<String> cfgPhases = Options.v().dump_cfg();
        if (!cfgPhases.isEmpty()) {
            this.cfgDumpingPhases = cfgPhases;
        }
    }

    public static PhaseDumper v() {
        return G.v().soot_util_PhaseDumper();
    }

    private boolean isBodyDumpingPhase(String phaseName) {
        if (this.bodyDumpingPhases != null) {
            return this.bodyDumpingPhases.contains(phaseName) || this.bodyDumpingPhases.contains(ALL_WILDCARD);
        }
        return false;
    }

    private boolean isCFGDumpingPhase(String phaseName) {
        if (this.cfgDumpingPhases == null) {
            return false;
        }
        if (this.cfgDumpingPhases.contains(ALL_WILDCARD)) {
            return true;
        }
        while (!this.cfgDumpingPhases.contains(phaseName)) {
            int lastDot = phaseName.lastIndexOf(46);
            if (lastDot >= 0) {
                phaseName = phaseName.substring(0, lastDot);
            } else {
                return false;
            }
        }
        return true;
    }

    private static File makeDirectoryIfMissing(Body b) throws IOException {
        File dir = new File(SourceLocator.v().getOutputDir() + File.separatorChar + b.getMethod().getDeclaringClass().getName() + File.separatorChar + b.getMethod().getSubSignature().replace('<', '[').replace('>', ']'));
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                throw new IOException(String.valueOf(dir.getPath()) + " exists but is not a directory.");
            }
        } else if (!dir.mkdirs()) {
            throw new IOException("Unable to mkdirs " + dir.getPath());
        }
        return dir;
    }

    private static PrintWriter openBodyFile(Body b, String baseName) throws IOException {
        File dir = makeDirectoryIfMissing(b);
        String filePath = String.valueOf(dir.toString()) + File.separatorChar + baseName;
        return new PrintWriter(new FileOutputStream(filePath));
    }

    private static String nextGraphFileName(Body b, String baseName) throws IOException {
        File file;
        File dir = makeDirectoryIfMissing(b);
        String prefix = String.valueOf(dir.toString()) + File.separatorChar + baseName;
        int fileNumber = 0;
        do {
            file = new File(String.valueOf(prefix) + fileNumber + DotGraph.DOT_EXTENSION);
            fileNumber++;
        } while (file.exists());
        return file.toString();
    }

    private static void deleteOldGraphFiles(Body b, final String phaseName) {
        try {
            File dir = makeDirectoryIfMissing(b);
            File[] toDelete = dir.listFiles(new FilenameFilter() { // from class: soot.util.PhaseDumper.1
                @Override // java.io.FilenameFilter
                public boolean accept(File dir2, String name) {
                    return name.startsWith(phaseName) && name.endsWith(DotGraph.DOT_EXTENSION);
                }
            });
            if (toDelete != null) {
                for (File element : toDelete) {
                    element.delete();
                }
            }
        } catch (IOException e) {
            logger.debug("PhaseDumper.dumpBody() caught: " + e.toString());
            logger.error(e.getMessage(), (Throwable) e);
        }
    }

    public void dumpBody(Body b, String baseName) {
        Printer printer = Printer.v();
        this.alreadyDumping = true;
        try {
            Throwable th = null;
            try {
                PrintWriter out = openBodyFile(b, baseName);
                try {
                    printer.setOption(1);
                    printer.printTo(b, out);
                    if (out != null) {
                        out.close();
                    }
                } catch (Throwable th2) {
                    if (out != null) {
                        out.close();
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
            logger.debug("PhaseDumper.dumpBody() caught: " + e.toString());
            logger.error(e.getMessage(), (Throwable) e);
        } finally {
            this.alreadyDumping = false;
        }
    }

    private void dumpAllBodies(String baseName, boolean deleteGraphFiles) {
        for (SootClass cls : Scene.v().getClasses(3)) {
            for (SootMethod method : cls.getMethods()) {
                if (method.hasActiveBody()) {
                    Body body = method.getActiveBody();
                    if (deleteGraphFiles) {
                        deleteOldGraphFiles(body, baseName);
                    }
                    dumpBody(body, baseName);
                }
            }
        }
    }

    public void dumpBefore(Body b, String phaseName) {
        this.phaseStack.push(phaseName);
        if (isBodyDumpingPhase(phaseName)) {
            deleteOldGraphFiles(b, phaseName);
            dumpBody(b, String.valueOf(phaseName) + ".in");
        }
    }

    public void dumpAfter(Body b, String phaseName) {
        String poppedPhaseName = this.phaseStack.pop();
        if (poppedPhaseName != phaseName) {
            throw new IllegalArgumentException("dumpAfter(" + phaseName + ") when poppedPhaseName == " + poppedPhaseName);
        }
        if (isBodyDumpingPhase(phaseName)) {
            dumpBody(b, String.valueOf(phaseName) + ".out");
        }
    }

    public void dumpBefore(String phaseName) {
        this.phaseStack.push(phaseName);
        if (isBodyDumpingPhase(phaseName)) {
            dumpAllBodies(String.valueOf(phaseName) + ".in", true);
        }
    }

    public void dumpAfter(String phaseName) {
        String poppedPhaseName = this.phaseStack.pop();
        if (poppedPhaseName != phaseName) {
            throw new IllegalArgumentException("dumpAfter(" + phaseName + ") when poppedPhaseName == " + poppedPhaseName);
        }
        if (isBodyDumpingPhase(phaseName)) {
            dumpAllBodies(String.valueOf(phaseName) + ".out", false);
        }
    }

    public <N> void dumpGraph(DirectedGraph<N> g, Body b) {
        dumpGraph(g, b, false);
    }

    public <N> void dumpGraph(DirectedGraph<N> g, Body b, boolean skipPhaseCheck) {
        if (!this.alreadyDumping) {
            try {
                this.alreadyDumping = true;
                String phaseName = this.phaseStack.currentPhase();
                if (skipPhaseCheck || isCFGDumpingPhase(phaseName)) {
                    try {
                        String outputFile = nextGraphFileName(b, String.valueOf(phaseName) + '-' + getClassIdent(g) + '-');
                        CFGToDotGraph drawer = new CFGToDotGraph();
                        drawer.drawCFG(g, b).plot(outputFile);
                    } catch (IOException e) {
                        logger.debug("PhaseDumper.dumpBody() caught: " + e.toString());
                        logger.error(e.getMessage(), (Throwable) e);
                    }
                }
            } finally {
                this.alreadyDumping = false;
            }
        }
    }

    public <N> void dumpGraph(ExceptionalGraph<N> g) {
        dumpGraph((ExceptionalGraph) g, false);
    }

    public <N> void dumpGraph(ExceptionalGraph<N> g, boolean skipPhaseCheck) {
        if (!this.alreadyDumping) {
            try {
                this.alreadyDumping = true;
                String phaseName = this.phaseStack.currentPhase();
                if (skipPhaseCheck || isCFGDumpingPhase(phaseName)) {
                    try {
                        String outputFile = nextGraphFileName(g.getBody(), String.valueOf(phaseName) + '-' + getClassIdent(g) + '-');
                        CFGToDotGraph drawer = new CFGToDotGraph();
                        drawer.setShowExceptions(Options.v().show_exception_dests());
                        drawer.drawCFG(g).plot(outputFile);
                    } catch (IOException e) {
                        logger.debug("PhaseDumper.dumpBody() caught: " + e.toString());
                        logger.error(e.getMessage(), (Throwable) e);
                    }
                }
            } finally {
                this.alreadyDumping = false;
            }
        }
    }

    private String getClassIdent(Object obj) {
        String qualifiedName = obj.getClass().getName();
        return qualifiedName.substring(qualifiedName.lastIndexOf(46) + 1);
    }

    public void printCurrentStackTrace() {
        IOException e = new IOException("FAKE");
        logger.error(e.getMessage(), (Throwable) e);
    }
}
