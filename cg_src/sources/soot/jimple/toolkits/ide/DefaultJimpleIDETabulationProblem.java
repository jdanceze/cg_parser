package soot.jimple.toolkits.ide;

import heros.InterproceduralCFG;
import heros.template.DefaultIDETabulationProblem;
import soot.SootMethod;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/DefaultJimpleIDETabulationProblem.class */
public abstract class DefaultJimpleIDETabulationProblem<D, V, I extends InterproceduralCFG<Unit, SootMethod>> extends DefaultIDETabulationProblem<Unit, D, SootMethod, V, I> {
    public DefaultJimpleIDETabulationProblem(I icfg) {
        super(icfg);
    }
}
