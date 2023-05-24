package android.os;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:android-4.1.1.4.jar:android/os/AsyncTask.class
 */
/* loaded from: gencallgraphv3.jar:android/os/AsyncTask.class */
public abstract class AsyncTask<Params, Progress, Result> {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/os/AsyncTask$Status.class */
    public enum Status {
        FINISHED,
        PENDING,
        RUNNING
    }

    protected Result doInBackground(Params... paramsArr) {
        return null;
    }

    protected void onPostExecute(Result result) {
    }

    protected void onPreExecute() {
    }

    protected void onProgressUpdate(Progress... progressArr) {
    }

    public final AsyncTask<Params, Progress, Result> execute(Params... paramsArr) {
        return null;
    }
}
