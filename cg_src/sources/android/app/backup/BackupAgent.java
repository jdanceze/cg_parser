package android.app.backup;

import android.content.ContextWrapper;
import android.os.ParcelFileDescriptor;
import java.io.File;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/backup/BackupAgent.class */
public abstract class BackupAgent extends ContextWrapper {
    public static final int TYPE_FILE = 1;
    public static final int TYPE_DIRECTORY = 2;

    public abstract void onBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) throws IOException;

    public abstract void onRestore(BackupDataInput backupDataInput, int i, ParcelFileDescriptor parcelFileDescriptor) throws IOException;

    public BackupAgent() {
        super(null);
        throw new RuntimeException("Stub!");
    }

    public void onCreate() {
        throw new RuntimeException("Stub!");
    }

    public void onDestroy() {
        throw new RuntimeException("Stub!");
    }

    public void onFullBackup(FullBackupDataOutput data) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public final void fullBackupFile(File file, FullBackupDataOutput output) {
        throw new RuntimeException("Stub!");
    }

    public void onRestoreFile(ParcelFileDescriptor data, long size, File destination, int type, long mode, long mtime) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
