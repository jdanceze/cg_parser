package android.app;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TimePicker;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/TimePickerDialog.class */
public class TimePickerDialog extends AlertDialog implements DialogInterface.OnClickListener, TimePicker.OnTimeChangedListener {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/TimePickerDialog$OnTimeSetListener.class */
    public interface OnTimeSetListener {
        void onTimeSet(TimePicker timePicker, int i, int i2);
    }

    public TimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
        super(null, false, null);
        throw new RuntimeException("Stub!");
    }

    public TimePickerDialog(Context context, int theme, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
        super(null, false, null);
        throw new RuntimeException("Stub!");
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        throw new RuntimeException("Stub!");
    }

    public void updateTime(int hourOfDay, int minutOfHour) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TimePicker.OnTimeChangedListener
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
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
