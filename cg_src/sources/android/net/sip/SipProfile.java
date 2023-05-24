package android.net.sip;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.text.ParseException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/sip/SipProfile.class */
public class SipProfile implements Parcelable, Serializable, Cloneable {
    public static final Parcelable.Creator<SipProfile> CREATOR = null;

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/net/sip/SipProfile$Builder.class */
    public static class Builder {
        public Builder(SipProfile profile) {
            throw new RuntimeException("Stub!");
        }

        public Builder(String uriString) throws ParseException {
            throw new RuntimeException("Stub!");
        }

        public Builder(String username, String serverDomain) throws ParseException {
            throw new RuntimeException("Stub!");
        }

        public Builder setAuthUserName(String name) {
            throw new RuntimeException("Stub!");
        }

        public Builder setProfileName(String name) {
            throw new RuntimeException("Stub!");
        }

        public Builder setPassword(String password) {
            throw new RuntimeException("Stub!");
        }

        public Builder setPort(int port) throws IllegalArgumentException {
            throw new RuntimeException("Stub!");
        }

        public Builder setProtocol(String protocol) throws IllegalArgumentException {
            throw new RuntimeException("Stub!");
        }

        public Builder setOutboundProxy(String outboundProxy) {
            throw new RuntimeException("Stub!");
        }

        public Builder setDisplayName(String displayName) {
            throw new RuntimeException("Stub!");
        }

        public Builder setSendKeepAlive(boolean flag) {
            throw new RuntimeException("Stub!");
        }

        public Builder setAutoRegistration(boolean flag) {
            throw new RuntimeException("Stub!");
        }

        public SipProfile build() {
            throw new RuntimeException("Stub!");
        }
    }

    SipProfile() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    public String getUriString() {
        throw new RuntimeException("Stub!");
    }

    public String getDisplayName() {
        throw new RuntimeException("Stub!");
    }

    public String getUserName() {
        throw new RuntimeException("Stub!");
    }

    public String getAuthUserName() {
        throw new RuntimeException("Stub!");
    }

    public String getPassword() {
        throw new RuntimeException("Stub!");
    }

    public String getSipDomain() {
        throw new RuntimeException("Stub!");
    }

    public int getPort() {
        throw new RuntimeException("Stub!");
    }

    public String getProtocol() {
        throw new RuntimeException("Stub!");
    }

    public String getProxyAddress() {
        throw new RuntimeException("Stub!");
    }

    public String getProfileName() {
        throw new RuntimeException("Stub!");
    }

    public boolean getSendKeepAlive() {
        throw new RuntimeException("Stub!");
    }

    public boolean getAutoRegistration() {
        throw new RuntimeException("Stub!");
    }
}
