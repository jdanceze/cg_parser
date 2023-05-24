package android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ArrayAdapter.class */
public class ArrayAdapter<T> extends BaseAdapter implements Filterable {
    public ArrayAdapter(Context context, int textViewResourceId) {
        throw new RuntimeException("Stub!");
    }

    public ArrayAdapter(Context context, int resource, int textViewResourceId) {
        throw new RuntimeException("Stub!");
    }

    public ArrayAdapter(Context context, int textViewResourceId, T[] objects) {
        throw new RuntimeException("Stub!");
    }

    public ArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        throw new RuntimeException("Stub!");
    }

    public ArrayAdapter(Context context, int textViewResourceId, List<T> objects) {
        throw new RuntimeException("Stub!");
    }

    public ArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        throw new RuntimeException("Stub!");
    }

    public void add(T object) {
        throw new RuntimeException("Stub!");
    }

    public void addAll(Collection<? extends T> collection) {
        throw new RuntimeException("Stub!");
    }

    public void addAll(T... items) {
        throw new RuntimeException("Stub!");
    }

    public void insert(T object, int index) {
        throw new RuntimeException("Stub!");
    }

    public void remove(T object) {
        throw new RuntimeException("Stub!");
    }

    public void clear() {
        throw new RuntimeException("Stub!");
    }

    public void sort(Comparator<? super T> comparator) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        throw new RuntimeException("Stub!");
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        throw new RuntimeException("Stub!");
    }

    public Context getContext() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public int getCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public T getItem(int position) {
        throw new RuntimeException("Stub!");
    }

    public int getPosition(T item) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }

    public void setDropDownViewResource(int resource) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.BaseAdapter, android.widget.SpinnerAdapter
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }

    public static ArrayAdapter<CharSequence> createFromResource(Context context, int textArrayResId, int textViewResId) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        throw new RuntimeException("Stub!");
    }
}
