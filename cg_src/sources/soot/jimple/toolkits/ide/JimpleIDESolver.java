package soot.jimple.toolkits.ide;

import com.google.common.collect.Table;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IDESolver;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/JimpleIDESolver.class */
public class JimpleIDESolver<D, V, I extends InterproceduralCFG<Unit, SootMethod>> extends IDESolver<Unit, D, SootMethod, V, I> {
    private static final Logger logger = LoggerFactory.getLogger(JimpleIDESolver.class);
    private final boolean DUMP_RESULTS;

    public JimpleIDESolver(IDETabulationProblem<Unit, D, SootMethod, V, I> problem) {
        this(problem, false);
    }

    public JimpleIDESolver(IDETabulationProblem<Unit, D, SootMethod, V, I> problem, boolean dumpResults) {
        super(problem);
        this.DUMP_RESULTS = dumpResults;
    }

    @Override // heros.solver.IDESolver
    public void solve() {
        super.solve();
        if (this.DUMP_RESULTS) {
            dumpResults();
        }
    }

    public void dumpResults() {
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream("ideSolverDump" + System.currentTimeMillis() + ".csv"));
            List<String> res = new ArrayList<>();
            for (Table.Cell<Unit, D, V> entry : this.val.cellSet()) {
                SootMethod methodOf = (SootMethod) this.icfg.getMethodOf(entry.getRowKey());
                PatchingChain<Unit> units = methodOf.getActiveBody().getUnits();
                int i = 0;
                Iterator<Unit> it = units.iterator();
                while (it.hasNext()) {
                    Unit unit = it.next();
                    if (unit == entry.getRowKey()) {
                        break;
                    }
                    i++;
                }
                res.add(methodOf + ";" + entry.getRowKey() + "@" + i + ";" + entry.getColumnKey() + ";" + entry.getValue());
            }
            Collections.sort(res);
            for (String string : res) {
                out.println(string);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), (Throwable) e);
        }
    }
}
