package soot.jimple.toolkits.ide;

import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import java.util.Map;
import soot.PackManager;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.jimple.toolkits.ide.exampleproblems.IFDSPossibleTypes;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/Main.class */
public class Main {
    public static void main(String[] args) {
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", new SceneTransformer() { // from class: soot.jimple.toolkits.ide.Main.1
            @Override // soot.SceneTransformer
            protected void internalTransform(String phaseName, Map options) {
                IFDSTabulationProblem<Unit, ?, SootMethod, InterproceduralCFG<Unit, SootMethod>> problem = new IFDSPossibleTypes(new JimpleBasedInterproceduralCFG());
                JimpleIFDSSolver<?, InterproceduralCFG<Unit, SootMethod>> solver = new JimpleIFDSSolver<>(problem);
                solver.solve();
            }
        }));
        soot.Main.main(args);
    }
}
