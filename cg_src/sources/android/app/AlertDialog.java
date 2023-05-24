package android.app;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/AlertDialog.class */
public class AlertDialog extends Dialog implements DialogInterface {
    public static final int THEME_TRADITIONAL = 1;
    public static final int THEME_HOLO_DARK = 2;
    public static final int THEME_HOLO_LIGHT = 3;
    public static final int THEME_DEVICE_DEFAULT_DARK = 4;
    public static final int THEME_DEVICE_DEFAULT_LIGHT = 5;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/AlertDialog$Builder.class */
    public static class Builder {
        public Builder(Context context) {
            throw new RuntimeException("Stub!");
        }

        public Builder(Context context, int theme) {
            throw new RuntimeException("Stub!");
        }

        public Context getContext() {
            throw new RuntimeException("Stub!");
        }

        public Builder setTitle(int titleId) {
            throw new RuntimeException("Stub!");
        }

        public Builder setTitle(CharSequence title) {
            throw new RuntimeException("Stub!");
        }

        public Builder setCustomTitle(View customTitleView) {
            throw new RuntimeException("Stub!");
        }

        public Builder setMessage(int messageId) {
            throw new RuntimeException("Stub!");
        }

        public Builder setMessage(CharSequence message) {
            throw new RuntimeException("Stub!");
        }

        public Builder setIcon(int iconId) {
            throw new RuntimeException("Stub!");
        }

        public Builder setIcon(Drawable icon) {
            throw new RuntimeException("Stub!");
        }

        public Builder setIconAttribute(int attrId) {
            throw new RuntimeException("Stub!");
        }

        public Builder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setNegativeButton(int textId, DialogInterface.OnClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setNeutralButton(int textId, DialogInterface.OnClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setNeutralButton(CharSequence text, DialogInterface.OnClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setCancelable(boolean cancelable) {
            throw new RuntimeException("Stub!");
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setItems(int itemsId, DialogInterface.OnClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setItems(CharSequence[] items, DialogInterface.OnClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setAdapter(ListAdapter adapter, DialogInterface.OnClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setCursor(Cursor cursor, DialogInterface.OnClickListener listener, String labelColumn) {
            throw new RuntimeException("Stub!");
        }

        public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, DialogInterface.OnMultiChoiceClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setSingleChoiceItems(int itemsId, int checkedItem, DialogInterface.OnClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, DialogInterface.OnClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, DialogInterface.OnClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem, DialogInterface.OnClickListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
            throw new RuntimeException("Stub!");
        }

        public Builder setView(View view) {
            throw new RuntimeException("Stub!");
        }

        public Builder setInverseBackgroundForced(boolean useInverseBackground) {
            throw new RuntimeException("Stub!");
        }

        public AlertDialog create() {
            throw new RuntimeException("Stub!");
        }

        public AlertDialog show() {
            throw new RuntimeException("Stub!");
        }
    }

    protected AlertDialog(Context context) {
        super(null, false, null);
        throw new RuntimeException("Stub!");
    }

    protected AlertDialog(Context context, int theme) {
        super(null, false, null);
        throw new RuntimeException("Stub!");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AlertDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(null, false, null);
        throw new RuntimeException("Stub!");
    }

    public Button getButton(int whichButton) {
        throw new RuntimeException("Stub!");
    }

    public ListView getListView() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Dialog
    public void setTitle(CharSequence title) {
        throw new RuntimeException("Stub!");
    }

    public void setCustomTitle(View customTitleView) {
        throw new RuntimeException("Stub!");
    }

    public void setMessage(CharSequence message) {
        throw new RuntimeException("Stub!");
    }

    public void setView(View view) {
        throw new RuntimeException("Stub!");
    }

    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        throw new RuntimeException("Stub!");
    }

    public void setButton(int whichButton, CharSequence text, Message msg) {
        throw new RuntimeException("Stub!");
    }

    public void setButton(int whichButton, CharSequence text, DialogInterface.OnClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setButton(CharSequence text, Message msg) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setButton2(CharSequence text, Message msg) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setButton3(CharSequence text, Message msg) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setButton(CharSequence text, DialogInterface.OnClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setButton2(CharSequence text, DialogInterface.OnClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public void setButton3(CharSequence text, DialogInterface.OnClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setIcon(int resId) {
        throw new RuntimeException("Stub!");
    }

    public void setIcon(Drawable icon) {
        throw new RuntimeException("Stub!");
    }

    public void setIconAttribute(int attrId) {
        throw new RuntimeException("Stub!");
    }

    public void setInverseBackgroundForced(boolean forceInverseBackground) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }
}
