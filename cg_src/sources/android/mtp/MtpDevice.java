package android.mtp;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/mtp/MtpDevice.class */
public final class MtpDevice {
    public MtpDevice(UsbDevice device) {
        throw new RuntimeException("Stub!");
    }

    public boolean open(UsbDeviceConnection connection) {
        throw new RuntimeException("Stub!");
    }

    public void close() {
        throw new RuntimeException("Stub!");
    }

    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }

    public String getDeviceName() {
        throw new RuntimeException("Stub!");
    }

    public int getDeviceId() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public MtpDeviceInfo getDeviceInfo() {
        throw new RuntimeException("Stub!");
    }

    public int[] getStorageIds() {
        throw new RuntimeException("Stub!");
    }

    public int[] getObjectHandles(int storageId, int format, int objectHandle) {
        throw new RuntimeException("Stub!");
    }

    public byte[] getObject(int objectHandle, int objectSize) {
        throw new RuntimeException("Stub!");
    }

    public byte[] getThumbnail(int objectHandle) {
        throw new RuntimeException("Stub!");
    }

    public MtpStorageInfo getStorageInfo(int storageId) {
        throw new RuntimeException("Stub!");
    }

    public MtpObjectInfo getObjectInfo(int objectHandle) {
        throw new RuntimeException("Stub!");
    }

    public boolean deleteObject(int objectHandle) {
        throw new RuntimeException("Stub!");
    }

    public long getParent(int objectHandle) {
        throw new RuntimeException("Stub!");
    }

    public long getStorageId(int objectHandle) {
        throw new RuntimeException("Stub!");
    }

    public boolean importFile(int objectHandle, String destPath) {
        throw new RuntimeException("Stub!");
    }
}
