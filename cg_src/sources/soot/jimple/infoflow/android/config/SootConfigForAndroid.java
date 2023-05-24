package soot.jimple.infoflow.android.config;

import java.util.LinkedList;
import java.util.List;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.config.IInfoflowConfig;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/config/SootConfigForAndroid.class */
public class SootConfigForAndroid implements IInfoflowConfig {
    @Override // soot.jimple.infoflow.config.IInfoflowConfig
    public void setSootOptions(Options options, InfoflowConfiguration config) {
        List<String> excludeList = new LinkedList<>();
        excludeList.add("java.*");
        excludeList.add("javax.*");
        excludeList.add("sun.*");
        excludeList.add("android.*");
        excludeList.add("androidx.*");
        excludeList.add("org.apache.*");
        excludeList.add("org.eclipse.*");
        excludeList.add("soot.*");
        options.set_exclude(excludeList);
        Options.v().set_no_bodies_for_excluded(true);
    }
}
