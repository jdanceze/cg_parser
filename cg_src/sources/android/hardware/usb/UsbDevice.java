package android.hardware.usb;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/hardware/usb/UsbDevice.class */
public class UsbDevice implements Parcelable {
    public static final Parcelable.Creator<UsbDevice> CREATOR = null;

    UsbDevice() {
        throw new RuntimeException("Stub!");
    }

    public String getDeviceName() {
        throw new RuntimeException("Stub!");
    }

    public int getDeviceId() {
        throw new RuntimeException("Stub!");
    }

    public int getVendorId() {
        throw new RuntimeException("Stub!");
    }

    public int getProductId() {
        throw new RuntimeException("Stub!");
    }

    public int getDeviceClass() {
        throw new RuntimeException("Stub!");
    }

    public int getDeviceSubclass() {
        throw new RuntimeException("Stub!");
    }

    public int getDeviceProtocol() {
        throw new RuntimeException("Stub!");
    }

    public int getInterfaceCount() {
        throw new RuntimeException("Stub!");
    }

    public UsbInterface getInterface(int index) {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object o) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        throw new RuntimeException("Stub!");
    }

    public static int getDeviceId(String name) {
        throw new RuntimeException("Stub!");
    }

    public static String getDeviceName(int id) {
        throw new RuntimeException("Stub!");
    }
}
