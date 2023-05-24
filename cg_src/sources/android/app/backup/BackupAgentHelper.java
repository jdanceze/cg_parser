package android.app.backup;

import android.os.ParcelFileDescriptor;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/app/backup/BackupAgentHelper.class */
public class BackupAgentHelper extends BackupAgent {
    public BackupAgentHelper() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.backup.BackupAgent
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.app.backup.BackupAgent
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public void addHelper(String keyPrefix, BackupHelper helper) {
        throw new RuntimeException("Stub!");
    }
}
