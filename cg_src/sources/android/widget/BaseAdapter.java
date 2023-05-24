package android.widget;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/BaseAdapter.class */
public abstract class BaseAdapter implements ListAdapter, SpinnerAdapter {
    public BaseAdapter() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public boolean hasStableIds() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public void registerDataSetObserver(DataSetObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public void unregisterDataSetObserver(DataSetObserver observer) {
        throw new RuntimeException("Stub!");
    }

    public void notifyDataSetChanged() {
        throw new RuntimeException("Stub!");
    }

    public void notifyDataSetInvalidated() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ListAdapter
    public boolean areAllItemsEnabled() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.ListAdapter
    public boolean isEnabled(int position) {
        throw new RuntimeException("Stub!");
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public int getItemViewType(int position) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public int getViewTypeCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public boolean isEmpty() {
        throw new RuntimeException("Stub!");
    }
}
