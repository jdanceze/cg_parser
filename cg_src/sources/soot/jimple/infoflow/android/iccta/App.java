package soot.jimple.infoflow.android.iccta;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import soot.jimple.infoflow.android.iccta.Ic3Data;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/App.class */
public class App {
    private Set<LoggingPoint> loggingPoints;
    private int analysisTime;
    private String analysisName;
    private String appName;
    private Object metadata;
    private boolean seal;
    private List<Ic3Data.Application.Component> componentsList;

    public App(String analysisName, String appName, Object metadata) {
        this.loggingPoints = new HashSet();
        this.analysisName = analysisName;
        this.appName = appName;
        this.metadata = metadata;
    }

    public App(String analysisName, String appName) {
        this(analysisName, appName, null);
    }

    public String getAppName() {
        return this.appName;
    }

    public String getAnalysisName() {
        return this.analysisName;
    }

    public Set<LoggingPoint> getLoggingPoints() {
        if (this.seal) {
            return Collections.unmodifiableSet(this.loggingPoints);
        }
        return this.loggingPoints;
    }

    public void setLoggingPoints(Set<LoggingPoint> loggingPoints) {
        this.loggingPoints = loggingPoints;
    }

    public int getAnalysisTime() {
        return this.analysisTime;
    }

    public void setAnalysisTime(int analysisTime) {
        this.analysisTime = analysisTime;
    }

    public void dump() {
        for (LoggingPoint loggingPoint : this.loggingPoints) {
            System.out.println("----------------------------");
            System.out.println(String.valueOf(loggingPoint.getCallerMethodSignature()) + "/" + loggingPoint.getCalleeMethodSignature());
            for (Intent intent : loggingPoint.getIntents()) {
                System.out.println("  Component: " + intent.getComponent());
                System.out.println("  Categories: " + intent.getCategories());
                System.out.println("  Action: " + intent.getAction());
            }
        }
        System.out.println("Analysis time: " + this.analysisTime);
    }

    public int getResultCount() {
        int c = 0;
        for (LoggingPoint lp : this.loggingPoints) {
            c += lp.getIntents().size();
        }
        return c;
    }

    public Object getMetadata() {
        return this.metadata;
    }

    public int getSatisfiedLPs() {
        int satisfied = 0;
        for (LoggingPoint c : this.loggingPoints) {
            if (!c.getIntents().isEmpty()) {
                satisfied++;
            }
        }
        return satisfied;
    }

    public void seal() {
        this.seal = true;
        for (LoggingPoint p : getLoggingPoints()) {
            p.seal();
        }
    }

    public Set<Intent> getIntents() {
        Set<Intent> intents = new HashSet<>();
        for (LoggingPoint p : getLoggingPoints()) {
            intents.addAll(p.getIntents());
        }
        return intents;
    }

    public void setComponentList(List<Ic3Data.Application.Component> componentsList) {
        for (Ic3Data.Application.Component c : componentsList) {
            c.setApp(this);
        }
        this.componentsList = componentsList;
    }

    public List<Ic3Data.Application.Component> getComponentList() {
        return this.componentsList;
    }
}
