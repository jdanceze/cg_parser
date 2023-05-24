package android.hardware.usb;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/usb/UsbDeviceConnection.class */
public class UsbDeviceConnection {
    UsbDeviceConnection() {
        throw new RuntimeException("Stub!");
    }

    public void close() {
        throw new RuntimeException("Stub!");
    }

    public int getFileDescriptor() {
        throw new RuntimeException("Stub!");
    }

    public byte[] getRawDescriptors() {
        throw new RuntimeException("Stub!");
    }

    public boolean claimInterface(UsbInterface intf, boolean force) {
        throw new RuntimeException("Stub!");
    }

    public boolean releaseInterface(UsbInterface intf) {
        throw new RuntimeException("Stub!");
    }

    public int controlTransfer(int requestType, int request, int value, int index, byte[] buffer, int length, int timeout) {
        throw new RuntimeException("Stub!");
    }

    public int bulkTransfer(UsbEndpoint endpoint, byte[] buffer, int length, int timeout) {
        throw new RuntimeException("Stub!");
    }

    public UsbRequest requestWait() {
        throw new RuntimeException("Stub!");
    }

    public String getSerial() {
        throw new RuntimeException("Stub!");
    }
}
