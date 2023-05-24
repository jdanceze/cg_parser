package android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SimpleAdapter.class */
public class SimpleAdapter extends BaseAdapter implements Filterable {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SimpleAdapter$ViewBinder.class */
    public interface ViewBinder {
        boolean setViewValue(View view, Object obj, String str);
    }

    public SimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Adapter
    public int getCount() {
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

    public ViewBinder getViewBinder() {
        throw new RuntimeException("Stub!");
    }

    public void setViewBinder(ViewBinder viewBinder) {
        throw new RuntimeException("Stub!");
    }

    public void setViewImage(ImageView v, int value) {
        throw new RuntimeException("Stub!");
    }

    public void setViewImage(ImageView v, String value) {
        throw new RuntimeException("Stub!");
    }

    public void setViewText(TextView v, String text) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        throw new RuntimeException("Stub!");
    }
}
