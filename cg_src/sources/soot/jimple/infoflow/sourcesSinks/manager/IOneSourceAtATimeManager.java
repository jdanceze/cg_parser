package soot.jimple.infoflow.sourcesSinks.manager;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/sourcesSinks/manager/IOneSourceAtATimeManager.class */
public interface IOneSourceAtATimeManager {
    void setOneSourceAtATimeEnabled(boolean z);

    boolean isOneSourceAtATimeEnabled();

    void resetCurrentSource();

    void nextSource();

    boolean hasNextSource();
}
