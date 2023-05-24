package soot;

import java.util.Map;
import soot.jimple.JimpleBody;
import soot.options.JBOptions;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/JimpleBodyPack.class */
public class JimpleBodyPack extends BodyPack {
    public JimpleBodyPack() {
        super("jb");
    }

    private void applyPhaseOptions(JimpleBody b, Map<String, String> opts) {
        JBOptions options = new JBOptions(opts);
        if (options.use_original_names()) {
            PhaseOptions.v().setPhaseOptionIfUnset("jb.lns", "only-stack-locals");
        }
        PackManager pacman = PackManager.v();
        boolean time = Options.v().time();
        if (time) {
            Timers.v().splitTimer.start();
        }
        pacman.getTransform("jb.tt").apply(b);
        pacman.getTransform("jb.dtr").apply(b);
        pacman.getTransform("jb.uce").apply(b);
        pacman.getTransform("jb.ls").apply(b);
        pacman.getTransform("jb.sils").apply(b);
        if (time) {
            Timers.v().splitTimer.end();
        }
        pacman.getTransform("jb.a").apply(b);
        pacman.getTransform("jb.ule").apply(b);
        if (time) {
            Timers.v().assignTimer.start();
        }
        pacman.getTransform("jb.tr").apply(b);
        if (time) {
            Timers.v().assignTimer.end();
        }
        if (options.use_original_names()) {
            pacman.getTransform("jb.ulp").apply(b);
        }
        pacman.getTransform("jb.lns").apply(b);
        pacman.getTransform("jb.cp").apply(b);
        pacman.getTransform("jb.dae").apply(b);
        pacman.getTransform("jb.cp-ule").apply(b);
        pacman.getTransform("jb.lp").apply(b);
        pacman.getTransform("jb.ne").apply(b);
        pacman.getTransform("jb.uce").apply(b);
        if (options.stabilize_local_names()) {
            PhaseOptions.v().setPhaseOption("jb.lns", "sort-locals:true");
            pacman.getTransform("jb.lns").apply(b);
        }
        if (time) {
            Timers.v().stmtCount += b.getUnits().size();
        }
    }

    @Override // soot.BodyPack, soot.Pack
    protected void internalApply(Body b) {
        applyPhaseOptions((JimpleBody) b, PhaseOptions.v().getPhaseOptions(getPhaseName()));
    }
}
