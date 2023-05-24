package android.app.backup;

import android.content.Context;
import android.os.ParcelFileDescriptor;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/backup/FileBackupHelper.class */
public class FileBackupHelper extends FileBackupHelperBase implements BackupHelper {
    @Override // android.app.backup.FileBackupHelperBase, android.app.backup.BackupHelper
    public /* bridge */ /* synthetic */ void writeNewStateDescription(ParcelFileDescriptor x0) {
        super.writeNewStateDescription(x0);
    }

    public FileBackupHelper(Context context, String... files) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.backup.BackupHelper
    public void performBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.backup.BackupHelper
    public void restoreEntity(BackupDataInputStream data) {
        throw new RuntimeException("Stub!");
    }
}
