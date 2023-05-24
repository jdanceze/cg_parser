package soot.jimple.toolkits.annotation.purity;

import java.util.Date;
import java.util.Iterator;
import org.apache.tools.ant.types.selectors.DepthSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.RefLikeType;
import soot.SootMethod;
import soot.Type;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.PurityOptions;
import soot.tagkit.GenericAttribute;
import soot.tagkit.StringTag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.util.dot.DotGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/PurityInterproceduralAnalysis.class */
public class PurityInterproceduralAnalysis extends AbstractInterproceduralAnalysis<PurityGraphBox> {
    private static final Logger logger = LoggerFactory.getLogger(PurityInterproceduralAnalysis.class);
    private static final String[][] pureMethods = {new String[]{"java.lang.", "valueOf"}, new String[]{"java.", "equals"}, new String[]{"javax.", "equals"}, new String[]{"sun.", "equals"}, new String[]{"java.", "compare"}, new String[]{"javax.", "compare"}, new String[]{"sun.", "compare"}, new String[]{"java.", "getClass"}, new String[]{"javax.", "getClass"}, new String[]{"sun.", "getClass"}, new String[]{"java.", "hashCode"}, new String[]{"javax.", "hashCode"}, new String[]{"sun.", "hashCode"}, new String[]{"java.", "toString"}, new String[]{"javax.", "toString"}, new String[]{"sun.", "toString"}, new String[]{"java.", "valueOf"}, new String[]{"javax.", "valueOf"}, new String[]{"sun.", "valueOf"}, new String[]{"java.", "compareTo"}, new String[]{"javax.", "compareTo"}, new String[]{"sun.", "compareTo"}, new String[]{"java.lang.System", "identityHashCode"}, new String[]{"java.", "<clinit>"}, new String[]{"javax.", "<clinit>"}, new String[]{"sun.", "<clinit>"}, new String[]{"java.lang.Math", "abs"}, new String[]{"java.lang.Math", "acos"}, new String[]{"java.lang.Math", "asin"}, new String[]{"java.lang.Math", "atan"}, new String[]{"java.lang.Math", "atan2"}, new String[]{"java.lang.Math", "ceil"}, new String[]{"java.lang.Math", "cos"}, new String[]{"java.lang.Math", "exp"}, new String[]{"java.lang.Math", "floor"}, new String[]{"java.lang.Math", "IEEEremainder"}, new String[]{"java.lang.Math", "log"}, new String[]{"java.lang.Math", DepthSelector.MAX_KEY}, new String[]{"java.lang.Math", DepthSelector.MIN_KEY}, new String[]{"java.lang.Math", "pow"}, new String[]{"java.lang.Math", "rint"}, new String[]{"java.lang.Math", "round"}, new String[]{"java.lang.Math", "sin"}, new String[]{"java.lang.Math", "sqrt"}, new String[]{"java.lang.Math", "tan"}, new String[]{"java.lang.Throwable", "<init>"}, new String[]{"java.lang.StringIndexOutOfBoundsException", "<init>"}};
    private static final String[][] impureMethods = {new String[]{"java.io.", "<init>"}, new String[]{"java.io.", "close"}, new String[]{"java.io.", "read"}, new String[]{"java.io.", "write"}, new String[]{"java.io.", "flush"}, new String[]{"java.io.", "flushBuffer"}, new String[]{"java.io.", "print"}, new String[]{"java.io.", "println"}, new String[]{"java.lang.Runtime", "exit"}};
    private static final String[][] alterMethods = {new String[]{"java.lang.System", "arraycopy"}, new String[]{"java.lang.FloatingDecimal", "dtoa"}, new String[]{"java.lang.FloatingDecimal", "developLongDigits"}, new String[]{"java.lang.FloatingDecimal", "big5pow"}, new String[]{"java.lang.FloatingDecimal", "getChars"}, new String[]{"java.lang.FloatingDecimal", "roundup"}};

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/purity/PurityInterproceduralAnalysis$Filter.class */
    private static class Filter implements SootMethodFilter {
        private Filter() {
        }

        /* synthetic */ Filter(Filter filter) {
            this();
        }

        @Override // soot.jimple.toolkits.annotation.purity.SootMethodFilter
        public boolean want(SootMethod method) {
            String[][] strArr;
            String[][] strArr2;
            String[][] strArr3;
            String c = method.getDeclaringClass().toString();
            String m = method.getName();
            for (String[] element : PurityInterproceduralAnalysis.pureMethods) {
                if (m.equals(element[1]) && c.startsWith(element[0])) {
                    return false;
                }
            }
            for (String[] element2 : PurityInterproceduralAnalysis.impureMethods) {
                if (m.equals(element2[1]) && c.startsWith(element2[0])) {
                    return false;
                }
            }
            for (String[] element3 : PurityInterproceduralAnalysis.alterMethods) {
                if (m.equals(element3[1]) && c.startsWith(element3[0])) {
                    return false;
                }
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PurityInterproceduralAnalysis(CallGraph cg, Iterator<SootMethod> heads, PurityOptions opts) {
        super(cg, new Filter(null), heads, opts.dump_cg());
        String s;
        String s2;
        if (opts.dump_cg()) {
            logger.debug("[AM] Dumping empty .dot call-graph");
            drawAsOneDot("EmptyCallGraph");
        }
        Date start = new Date();
        logger.debug("[AM] Analysis began");
        doAnalysis(opts.verbose());
        logger.debug("[AM] Analysis finished");
        Date finish = new Date();
        long runtime = finish.getTime() - start.getTime();
        logger.debug("[AM] run time: " + (runtime / 1000.0d) + " s");
        if (opts.dump_cg()) {
            logger.debug("[AM] Dumping annotated .dot call-graph");
            drawAsOneDot("CallGraph");
        }
        if (opts.dump_summaries()) {
            logger.debug("[AM] Dumping .dot summaries of analysed methods");
            drawAsManyDot("Summary_", false);
        }
        if (opts.dump_intra()) {
            logger.debug("[AM] Dumping .dot full intra-procedural method analyses");
            Iterator<SootMethod> it = getAnalysedMethods();
            while (it.hasNext()) {
                SootMethod method = it.next();
                if (opts.verbose()) {
                    logger.debug("  |- " + method);
                }
                ExceptionalUnitGraph graph = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(method.retrieveActiveBody());
                PurityIntraproceduralAnalysis r = new PurityIntraproceduralAnalysis(graph, this);
                r.drawAsOneDot("Intra_", method.toString());
                r.copyResult(new PurityGraphBox());
            }
        }
        logger.debug("[AM] Annotate methods. ");
        Iterator<SootMethod> it2 = getAnalysedMethods();
        while (it2.hasNext()) {
            SootMethod m = it2.next();
            PurityGraphBox b = getSummaryFor(m);
            boolean isPure = m.toString().contains("<init>") ? b.g.isPureConstructor() : b.g.isPure();
            m.addTag(new StringTag("purity: " + (isPure ? "pure" : "impure")));
            if (isPure && opts.annotate()) {
                m.addTag(new GenericAttribute("Pure", new byte[0]));
            }
            if (opts.print()) {
                logger.debug("  |- method " + m.toString() + " is " + (isPure ? "pure" : "impure"));
            }
            if (!m.isStatic()) {
                switch (b.g.thisStatus()) {
                    case 0:
                        s2 = "read/write";
                        break;
                    case 1:
                        s2 = "read-only";
                        break;
                    case 2:
                        s2 = "Safe";
                        break;
                    default:
                        s2 = "unknown";
                        break;
                }
                m.addTag(new StringTag("this: " + s2));
                if (opts.print()) {
                    logger.debug("  |   |- this is " + s2);
                }
            }
            int i = 0;
            for (Type t : m.getParameterTypes()) {
                if (t instanceof RefLikeType) {
                    switch (b.g.paramStatus(i)) {
                        case 0:
                            s = "read/write";
                            break;
                        case 1:
                            s = "read-only";
                            break;
                        case 2:
                            s = "safe";
                            break;
                        default:
                            s = "unknown";
                            break;
                    }
                    m.addTag(new StringTag("param" + i + ": " + s));
                    if (opts.print()) {
                        logger.debug("  |   |- param " + i + " is " + s);
                    }
                }
                i++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.jimple.toolkits.annotation.purity.AbstractInterproceduralAnalysis
    public PurityGraphBox newInitialSummary() {
        return new PurityGraphBox();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.toolkits.annotation.purity.AbstractInterproceduralAnalysis
    public void merge(PurityGraphBox in1, PurityGraphBox in2, PurityGraphBox out) {
        if (out != in1) {
            out.g = new PurityGraph(in1.g);
        }
        out.g.union(in2.g);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.toolkits.annotation.purity.AbstractInterproceduralAnalysis
    public void copy(PurityGraphBox source, PurityGraphBox dest) {
        dest.g = new PurityGraph(source.g);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.toolkits.annotation.purity.AbstractInterproceduralAnalysis
    public void analyseMethod(SootMethod method, PurityGraphBox dst) {
        new PurityIntraproceduralAnalysis(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(method.retrieveActiveBody()), this).copyResult(dst);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.jimple.toolkits.annotation.purity.AbstractInterproceduralAnalysis
    public PurityGraphBox summaryOfUnanalysedMethod(SootMethod method) {
        String[][] strArr;
        String[][] strArr2;
        PurityGraphBox b = new PurityGraphBox();
        String c = method.getDeclaringClass().toString();
        String m = method.getName();
        b.g = PurityGraph.conservativeGraph(method, true);
        for (String[] element : pureMethods) {
            if (m.equals(element[1]) && c.startsWith(element[0])) {
                b.g = PurityGraph.freshGraph(method);
            }
        }
        for (String[] element2 : alterMethods) {
            if (m.equals(element2[1]) && c.startsWith(element2[0])) {
                b.g = PurityGraph.conservativeGraph(method, false);
            }
        }
        return b;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.toolkits.annotation.purity.AbstractInterproceduralAnalysis
    public void applySummary(PurityGraphBox src, Stmt stmt, PurityGraphBox summary, PurityGraphBox dst) {
        Local ret = null;
        if (stmt instanceof AssignStmt) {
            Local v = (Local) ((AssignStmt) stmt).getLeftOp();
            if (v.getType() instanceof RefLikeType) {
                ret = v;
            }
        }
        Local obj = null;
        InvokeExpr e = stmt.getInvokeExpr();
        if (!(e instanceof StaticInvokeExpr)) {
            obj = (Local) ((InstanceInvokeExpr) e).getBase();
        }
        PurityGraph g = new PurityGraph(src.g);
        g.methodCall(summary.g, obj, e.getArgs(), ret);
        dst.g = g;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.toolkits.annotation.purity.AbstractInterproceduralAnalysis
    public void fillDotGraph(String prefix, PurityGraphBox o, DotGraph out) {
        o.g.fillDotGraph(prefix, out);
    }
}
