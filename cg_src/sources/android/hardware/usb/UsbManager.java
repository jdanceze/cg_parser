package android.hardware.usb;

import android.app.PendingIntent;
import android.os.ParcelFileDescriptor;
import java.util.HashMap;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/usb/UsbManager.class */
public class UsbManager {
    public static final String ACTION_USB_DEVICE_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    public static final String ACTION_USB_DEVICE_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    public static final String ACTION_USB_ACCESSORY_ATTACHED = "android.hardware.usb.action.USB_ACCESSORY_ATTACHED";
    public static final String ACTION_USB_ACCESSORY_DETACHED = "android.hardware.usb.action.USB_ACCESSORY_DETACHED";
    public static final String EXTRA_DEVICE = "device";
    public static final String EXTRA_ACCESSORY = "accessory";
    public static final String EXTRA_PERMISSION_GRANTED = "permission";

    UsbManager() {
        throw new RuntimeException("Stub!");
    }

    public HashMap<String, UsbDevice> getDeviceList() {
        throw new RuntimeException("Stub!");
    }

    public UsbDeviceConnection openDevice(UsbDevice device) {
        throw new RuntimeException("Stub!");
    }

    public UsbAccessory[] getAccessoryList() {
        throw new RuntimeException("Stub!");
    }

    public ParcelFileDescriptor openAccessory(UsbAccessory accessory) {
        throw new RuntimeException("Stub!");
    }

    public boolean hasPermission(UsbDevice device) {
        throw new RuntimeException("Stub!");
    }

    public boolean hasPermission(UsbAccessory accessory) {
        throw new RuntimeException("Stub!");
    }

    public void requestPermission(UsbDevice device, PendingIntent pi) {
        throw new RuntimeException("Stub!");
    }

    public void requestPermission(UsbAccessory accessory, PendingIntent pi) {
        throw new RuntimeException("Stub!");
    }
}
