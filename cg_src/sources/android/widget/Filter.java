package android.widget;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/Filter.class */
public abstract class Filter {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/Filter$FilterListener.class */
    public interface FilterListener {
        void onFilterComplete(int i);
    }

    protected abstract FilterResults performFiltering(CharSequence charSequence);

    protected abstract void publishResults(CharSequence charSequence, FilterResults filterResults);

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/Filter$FilterResults.class */
    protected static class FilterResults {
        public Object values;
        public int count;

        public FilterResults() {
            throw new RuntimeException("Stub!");
        }
    }

    public Filter() {
        throw new RuntimeException("Stub!");
    }

    public final void filter(CharSequence constraint) {
        throw new RuntimeException("Stub!");
    }

    public final void filter(CharSequence constraint, FilterListener listener) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence convertResultToString(Object resultValue) {
        throw new RuntimeException("Stub!");
    }
}
