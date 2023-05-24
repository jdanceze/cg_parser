package android.app.backup;

import android.os.ParcelFileDescriptor;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/backup/BackupHelper.class */
public interface BackupHelper {
    void performBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2);

    void restoreEntity(BackupDataInputStream backupDataInputStream);

    void writeNewStateDescription(ParcelFileDescriptor parcelFileDescriptor);
}
