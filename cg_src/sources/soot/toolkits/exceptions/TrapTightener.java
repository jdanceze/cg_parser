package soot.toolkits.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.toolkits.graph.ExceptionalUnitGraph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/TrapTightener.class */
public final class TrapTightener extends TrapTransformer {
    private static final Logger logger = LoggerFactory.getLogger(TrapTightener.class);
    protected ThrowAnalysis throwAnalysis;

    public TrapTightener(Singletons.Global g) {
        this.throwAnalysis = null;
    }

    public static TrapTightener v() {
        return G.v().soot_toolkits_exceptions_TrapTightener();
    }

    public TrapTightener(ThrowAnalysis ta) {
        this.throwAnalysis = null;
        this.throwAnalysis = ta;
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x0137, code lost:
        r20 = r21;
     */
    @Override // soot.BodyTransformer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void internalTransform(soot.Body r6, java.lang.String r7, java.util.Map<java.lang.String, java.lang.String> r8) {
        /*
            Method dump skipped, instructions count: 419
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.toolkits.exceptions.TrapTightener.internalTransform(soot.Body, java.lang.String, java.util.Map):void");
    }

    protected boolean mightThrowTo(ExceptionalUnitGraph g, Unit u, Trap t) {
        for (ExceptionalUnitGraph.ExceptionDest dest : g.getExceptionDests(u)) {
            if (dest.getTrap() == t) {
                return true;
            }
        }
        return false;
    }
}
