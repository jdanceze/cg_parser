package soot.jimple.toolkit.callgraph;

import android.os.AsyncTask;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkit/callgraph/AsyncTaskTestMainSample.class */
public class AsyncTaskTestMainSample {
    AsyncTask task;

    public void target1() {
        this.task = new LongOperation().execute("stub");
    }
}
