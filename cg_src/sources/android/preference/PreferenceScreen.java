package android.preference;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/preference/PreferenceScreen.class */
public final class PreferenceScreen extends PreferenceGroup implements AdapterView.OnItemClickListener, DialogInterface.OnDismissListener {
    PreferenceScreen() {
        super(null, null);
        throw new RuntimeException("Stub!");
    }

    public ListAdapter getRootAdapter() {
        throw new RuntimeException("Stub!");
    }

    protected ListAdapter onCreateRootAdapter() {
        throw new RuntimeException("Stub!");
    }

    public void bind(ListView listView) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected void onClick() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        throw new RuntimeException("Stub!");
    }

    public Dialog getDialog() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.PreferenceGroup
    protected boolean isOnSameScreenAsChildren() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected Parcelable onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.preference.Preference
    protected void onRestoreInstanceState(Parcelable state) {
        throw new RuntimeException("Stub!");
    }
}
