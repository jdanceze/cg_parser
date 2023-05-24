package polyglot.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import polyglot.main.Report;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/Stats.class */
public class Stats {
    protected ExtensionInfo ext;
    protected Map passTimes = new HashMap();
    protected List keys = new ArrayList(20);

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/Stats$Times.class */
    public static class Times {
        long inclusive;
        long exclusive;

        protected Times() {
        }
    }

    public Stats(ExtensionInfo ext) {
        this.ext = ext;
    }

    public void resetPassTimes(Object key) {
        this.passTimes.remove(key);
    }

    public long passTime(Object key, boolean inclusive) {
        Times t = (Times) this.passTimes.get(key);
        if (t == null) {
            return 0L;
        }
        return inclusive ? t.inclusive : t.exclusive;
    }

    public void accumPassTimes(Object key, long in, long ex) {
        Times t = (Times) this.passTimes.get(key);
        if (t == null) {
            this.keys.add(key);
            t = new Times();
            this.passTimes.put(key, t);
        }
        t.inclusive += in;
        t.exclusive += ex;
    }

    public void report() {
        if (Report.should_report("time", 1)) {
            Report.report(1, new StringBuffer().append("\nStatistics for ").append(this.ext.compilerName()).append(" (").append(this.ext.getClass().getName()).append(")").toString());
            Report.report(1, "Pass Inclusive Exclusive");
            Report.report(1, "---- --------- ---------");
            for (Object key : this.keys) {
                Times t = (Times) this.passTimes.get(key);
                Report.report(1, new StringBuffer().append(key.toString()).append(Instruction.argsep).append(t.inclusive).append(Instruction.argsep).append(t.exclusive).toString());
            }
        }
    }
}
