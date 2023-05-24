package soot;

import java.util.Map;
import soot.javaToJimple.jj.Topics;
import soot.jimple.JimpleBody;
import soot.options.JJOptions;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/JavaToJimpleBodyPack.class */
public class JavaToJimpleBodyPack extends BodyPack {
    public JavaToJimpleBodyPack() {
        super(Topics.jj);
    }

    private void applyPhaseOptions(JimpleBody b, Map<String, String> opts) {
        JJOptions options = new JJOptions(opts);
        if (options.use_original_names()) {
            PhaseOptions.v().setPhaseOptionIfUnset("jj.lns", "only-stack-locals");
        }
        PackManager pacman = PackManager.v();
        boolean time = Options.v().time();
        if (time) {
            Timers.v().splitTimer.start();
        }
        pacman.getTransform("jj.ls").apply(b);
        if (time) {
            Timers.v().splitTimer.end();
        }
        pacman.getTransform("jj.a").apply(b);
        pacman.getTransform("jj.ule").apply(b);
        pacman.getTransform("jj.ne").apply(b);
        if (time) {
            Timers.v().assignTimer.start();
        }
        pacman.getTransform("jj.tr").apply(b);
        if (time) {
            Timers.v().assignTimer.end();
        }
        if (options.use_original_names()) {
            pacman.getTransform("jj.ulp").apply(b);
        }
        pacman.getTransform("jj.lns").apply(b);
        pacman.getTransform("jj.cp").apply(b);
        pacman.getTransform("jj.dae").apply(b);
        pacman.getTransform("jj.cp-ule").apply(b);
        pacman.getTransform("jj.lp").apply(b);
        pacman.getTransform("jj.uce").apply(b);
        if (time) {
            Timers.v().stmtCount += b.getUnits().size();
        }
    }

    @Override // soot.BodyPack, soot.Pack
    protected void internalApply(Body b) {
        applyPhaseOptions((JimpleBody) b, PhaseOptions.v().getPhaseOptions(getPhaseName()));
    }
}
