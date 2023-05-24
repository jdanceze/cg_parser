package android.app;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/DatePickerDialog.class */
public class DatePickerDialog extends AlertDialog implements DialogInterface.OnClickListener, DatePicker.OnDateChangedListener {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/DatePickerDialog$OnDateSetListener.class */
    public interface OnDateSetListener {
        void onDateSet(DatePicker datePicker, int i, int i2, int i3);
    }

    public DatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(null, false, null);
        throw new RuntimeException("Stub!");
    }

    public DatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(null, false, null);
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.DatePicker.OnDateChangedListener
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        throw new RuntimeException("Stub!");
    }

    public DatePicker getDatePicker() {
        throw new RuntimeException("Stub!");
    }

    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Dialog
    protected void onStop() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Dialog
    public Bundle onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.Dialog
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        throw new RuntimeException("Stub!");
    }
}
