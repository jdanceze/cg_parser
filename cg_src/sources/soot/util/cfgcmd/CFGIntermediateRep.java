package soot.util.cfgcmd;

import soot.Body;
import soot.baf.Baf;
import soot.grimp.Grimp;
import soot.jimple.JimpleBody;
import soot.shimple.Shimple;
import soot.util.cfgcmd.CFGOptionMatcher;
/* loaded from: gencallgraphv3.jar:soot/util/cfgcmd/CFGIntermediateRep.class */
public abstract class CFGIntermediateRep extends CFGOptionMatcher.CFGOption {
    public static final CFGIntermediateRep JIMPLE_IR = new CFGIntermediateRep("jimple") { // from class: soot.util.cfgcmd.CFGIntermediateRep.1
        @Override // soot.util.cfgcmd.CFGIntermediateRep
        public Body getBody(JimpleBody b) {
            return b;
        }
    };
    public static final CFGIntermediateRep BAF_IR = new CFGIntermediateRep("baf") { // from class: soot.util.cfgcmd.CFGIntermediateRep.2
        @Override // soot.util.cfgcmd.CFGIntermediateRep
        public Body getBody(JimpleBody b) {
            return Baf.v().newBody(b);
        }
    };
    public static final CFGIntermediateRep GRIMP_IR = new CFGIntermediateRep("grimp") { // from class: soot.util.cfgcmd.CFGIntermediateRep.3
        @Override // soot.util.cfgcmd.CFGIntermediateRep
        public Body getBody(JimpleBody b) {
            return Grimp.v().newBody(b, "gb");
        }
    };
    public static final CFGIntermediateRep SHIMPLE_IR = new CFGIntermediateRep(Shimple.PHASE) { // from class: soot.util.cfgcmd.CFGIntermediateRep.4
        @Override // soot.util.cfgcmd.CFGIntermediateRep
        public Body getBody(JimpleBody b) {
            return Shimple.v().newBody(b);
        }
    };
    public static final CFGIntermediateRep VIA_SHIMPLE_JIMPLE_IR = new CFGIntermediateRep("viaShimpleJimple") { // from class: soot.util.cfgcmd.CFGIntermediateRep.5
        @Override // soot.util.cfgcmd.CFGIntermediateRep
        public Body getBody(JimpleBody b) {
            return Shimple.v().newJimpleBody(Shimple.v().newBody(b));
        }
    };
    private static final CFGOptionMatcher irOptions = new CFGOptionMatcher(new CFGIntermediateRep[]{JIMPLE_IR, BAF_IR, GRIMP_IR, SHIMPLE_IR, VIA_SHIMPLE_JIMPLE_IR});

    public abstract Body getBody(JimpleBody jimpleBody);

    /* synthetic */ CFGIntermediateRep(String str, CFGIntermediateRep cFGIntermediateRep) {
        this(str);
    }

    private CFGIntermediateRep(String name) {
        super(name);
    }

    public static CFGIntermediateRep getIR(String name) {
        return (CFGIntermediateRep) irOptions.match(name);
    }

    public static String help(int initialIndent, int rightMargin, int hangingIndent) {
        return irOptions.help(initialIndent, rightMargin, hangingIndent);
    }
}
