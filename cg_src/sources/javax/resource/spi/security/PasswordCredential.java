package javax.resource.spi.security;

import java.io.Serializable;
import javax.resource.spi.ManagedConnectionFactory;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/security/PasswordCredential.class */
public final class PasswordCredential implements Serializable {
    private String userName;
    private char[] password;
    private ManagedConnectionFactory mcf;

    public PasswordCredential(String userName, char[] password) {
        this.userName = userName;
        this.password = (char[]) password.clone();
    }

    public String getUserName() {
        return this.userName;
    }

    public char[] getPassword() {
        return this.password;
    }

    public ManagedConnectionFactory getManagedConnectionFactory() {
        return this.mcf;
    }

    public void setManagedConnectionFactory(ManagedConnectionFactory mcf) {
        this.mcf = mcf;
    }

    public boolean equals(Object other) {
        if (!(other instanceof PasswordCredential)) {
            return false;
        }
        PasswordCredential pc = (PasswordCredential) other;
        if (!this.userName.equals(pc.userName) || this.password.length != pc.password.length) {
            return false;
        }
        for (int i = 0; i < this.password.length; i++) {
            if (this.password[i] != pc.password[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        String s = this.userName;
        return new StringBuffer().append(s).append(new String(this.password)).toString().hashCode();
    }
}
