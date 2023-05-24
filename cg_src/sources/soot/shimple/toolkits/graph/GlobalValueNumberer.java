package soot.shimple.toolkits.graph;

import soot.Local;
/* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/graph/GlobalValueNumberer.class */
public interface GlobalValueNumberer {
    int getGlobalValueNumber(Local local);

    boolean areEqual(Local local, Local local2);
}
