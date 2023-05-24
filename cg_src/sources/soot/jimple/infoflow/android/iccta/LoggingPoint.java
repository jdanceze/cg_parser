package soot.jimple.infoflow.android.iccta;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: App.java */
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/LoggingPoint.class */
public class LoggingPoint {
    private String callerMethodSignature;
    private String calleeMethodSignature;
    private int stmtSequence;
    private Set<Intent> intents = new HashSet();
    int id;
    private App app;
    private boolean sealed;
    public String extraInformation;

    public LoggingPoint(App app) {
        this.app = app;
    }

    public void seal() {
        this.sealed = true;
        for (Intent i : getIntents()) {
            i.seal();
        }
    }

    public String getCallerMethodSignature() {
        return this.callerMethodSignature;
    }

    public void setCallerMethodSignature(String callerMethodSignature) {
        this.callerMethodSignature = callerMethodSignature;
    }

    public String getCalleeMethodSignature() {
        return this.calleeMethodSignature;
    }

    public void setCalleeMethodSignature(String calleeMethodSignature) {
        this.calleeMethodSignature = calleeMethodSignature;
    }

    public int getStmtSequence() {
        return this.stmtSequence;
    }

    public void setStmtSequence(int stmtSequence) {
        this.stmtSequence = stmtSequence;
    }

    public Set<Intent> getIntents() {
        if (this.sealed) {
            return Collections.unmodifiableSet(this.intents);
        }
        return this.intents;
    }

    public void setIntents(Set<Intent> intents) {
        if (this.sealed) {
            throw new IllegalStateException();
        }
        this.intents = intents;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int hashCode() {
        return 1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LoggingPoint other = (LoggingPoint) obj;
        if (this.calleeMethodSignature == null) {
            if (other.calleeMethodSignature != null) {
                return false;
            }
        } else if (!this.calleeMethodSignature.equals(other.calleeMethodSignature)) {
            return false;
        }
        if (this.callerMethodSignature == null) {
            if (other.callerMethodSignature != null) {
                return false;
            }
        } else if (!this.callerMethodSignature.equals(other.callerMethodSignature)) {
            return false;
        }
        if (this.stmtSequence != other.stmtSequence) {
            return false;
        }
        if (this.app != other.app || this.id == other.id) {
            return true;
        }
        return false;
    }

    public String toString() {
        return String.valueOf(this.id);
    }

    public boolean equalsSimilar(LoggingPoint pointDest) {
        String shortenedA = getCalleeMethodSignature().substring(getCalleeMethodSignature().indexOf(":"));
        String shortenedB = pointDest.getCalleeMethodSignature().substring(pointDest.getCalleeMethodSignature().indexOf(":"));
        boolean b = getCallerMethodSignature().equals(pointDest.getCallerMethodSignature()) && shortenedA.equals(shortenedB);
        return b;
    }

    public boolean hasResults() {
        boolean noResult = getIntents().isEmpty() || (getIntents().size() == 1 && (getIntents().iterator().next() instanceof EmptyIntent));
        return !noResult;
    }
}
