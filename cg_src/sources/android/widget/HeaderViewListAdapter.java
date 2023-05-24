package android.widget;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/HeaderViewListAdapter.class */
public class HeaderViewListAdapter implements WrapperListAdapter, Filterable {
    public HeaderViewListAdapter(ArrayList<ListView.FixedViewInfo> headerViewInfos, ArrayList<ListView.FixedViewInfo> footerViewInfos, ListAdapter adapter) {
        throw new RuntimeException("Stub!");
    }

    public int getHeadersCount() {
        throw new RuntimeException("Stub!");
    }

    public int getFootersCount() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public boolean isEmpty() {
        throw new RuntimeException("Stub!");
    }

    public boolean removeHeader(View v) {
        throw new RuntimeException("Stub!");
    }

    public boolean removeFooter(View v) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public int getCount() {
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

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public boolean hasStableIds() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
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
    public void registerDataSetObserver(DataSetObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public void unregisterDataSetObserver(DataSetObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.WrapperListAdapter
    public ListAdapter getWrappedAdapter() {
        throw new RuntimeException("Stub!");
    }
}
