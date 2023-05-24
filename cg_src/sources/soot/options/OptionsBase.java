package soot.options;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import soot.Pack;
import soot.PackManager;
import soot.PhaseOptions;
import soot.Transform;
import soot.plugins.internal.PluginLoader;
/* loaded from: gencallgraphv3.jar:soot/options/OptionsBase.class */
abstract class OptionsBase {
    private final Deque<String> options = new LinkedList();
    protected LinkedList<String> classes = new LinkedList<>();

    private String pad(int initial, String opts, int tab, String desc) {
        int i;
        StringBuilder b = new StringBuilder();
        for (int i2 = 0; i2 < initial; i2++) {
            b.append(' ');
        }
        b.append(opts);
        if (tab <= opts.length()) {
            b.append('\n');
            i = 0;
        } else {
            i = opts.length() + initial;
        }
        while (i <= tab) {
            b.append(' ');
            i++;
        }
        StringTokenizer t = new StringTokenizer(desc);
        while (t.hasMoreTokens()) {
            String s = t.nextToken();
            if (i + s.length() > 78) {
                b.append('\n');
                i = 0;
                while (i <= tab) {
                    b.append(' ');
                    i++;
                }
            }
            b.append(s);
            b.append(' ');
            i += s.length() + 1;
        }
        b.append('\n');
        return b.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String padOpt(String opts, String desc) {
        return pad(1, opts, 30, desc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String padVal(String vals, String desc) {
        return pad(4, vals, 32, desc);
    }

    protected String getPhaseUsage() {
        StringBuilder b = new StringBuilder();
        b.append("\nPhases and phase options:\n");
        for (Pack p : PackManager.v().allPacks()) {
            b.append(padOpt(p.getPhaseName(), p.getDeclaredOptions()));
            Iterator<Transform> it = p.iterator();
            while (it.hasNext()) {
                Transform ph = it.next();
                b.append(padVal(ph.getPhaseName(), ph.getDeclaredOptions()));
            }
        }
        return b.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void pushOption(String option) {
        this.options.push(option);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasMoreOptions() {
        return !this.options.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String nextOption() {
        return this.options.removeFirst();
    }

    public LinkedList<String> classes() {
        return this.classes;
    }

    public boolean setPhaseOption(String phase, String option) {
        return PhaseOptions.v().processPhaseOptions(phase, option);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean loadPluginConfiguration(String file) {
        return PluginLoader.load(file);
    }
}
