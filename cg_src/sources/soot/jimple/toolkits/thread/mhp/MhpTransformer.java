package soot.jimple.toolkits.thread.mhp;

import java.util.Map;
import soot.G;
import soot.SceneTransformer;
import soot.Singletons;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/mhp/MhpTransformer.class */
public class MhpTransformer extends SceneTransformer {
    MhpTester mhpTester;

    public MhpTransformer(Singletons.Global g) {
    }

    public static MhpTransformer v() {
        return G.v().soot_jimple_toolkits_thread_mhp_MhpTransformer();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map options) {
        getMhpTester().printMhpSummary();
    }

    public MhpTester getMhpTester() {
        if (this.mhpTester == null) {
            this.mhpTester = new SynchObliviousMhpAnalysis();
        }
        return this.mhpTester;
    }

    public void setMhpTester(MhpTester mhpTester) {
        this.mhpTester = mhpTester;
    }
}
