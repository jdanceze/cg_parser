package soot.jbco;

import java.io.PrintStream;
import soot.G;
/* loaded from: gencallgraphv3.jar:soot/jbco/IJbcoTransform.class */
public interface IJbcoTransform {
    @Deprecated
    public static final PrintStream out = G.v().out;
    @Deprecated
    public static final boolean output;
    @Deprecated
    public static final boolean debug;

    String getName();

    String[] getDependencies();

    void outputSummary();

    static {
        output = G.v().soot_options_Options().verbose() || Main.jbcoVerbose;
        debug = Main.jbcoDebug;
    }

    default boolean isVerbose() {
        return G.v().soot_options_Options().verbose() || Main.jbcoVerbose;
    }

    default boolean isDebugEnabled() {
        return Main.jbcoDebug;
    }
}
