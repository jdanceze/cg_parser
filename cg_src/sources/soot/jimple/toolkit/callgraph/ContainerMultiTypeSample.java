package soot.jimple.toolkit.callgraph;

import java.util.HashSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkit/callgraph/ContainerMultiTypeSample.class */
public class ContainerMultiTypeSample {
    Helper helper;
    int i;
    Set<Helper> helpers = new HashSet();

    public void target() {
        for (Helper helper : this.helpers) {
            helper.handle();
        }
    }

    public ContainerMultiTypeSample() {
        this.helpers.add(new AHelper());
    }
}
