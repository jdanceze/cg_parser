package soot.jimple.infoflow.android.iccta;
/* compiled from: App.java */
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/EmptyIntent.class */
class EmptyIntent extends Intent {
    public EmptyIntent(App app, LoggingPoint point) {
        super(app, point);
    }

    @Override // soot.jimple.infoflow.android.iccta.Intent
    public String toString() {
        return "Not found";
    }
}
