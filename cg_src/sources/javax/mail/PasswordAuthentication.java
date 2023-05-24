package javax.mail;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/PasswordAuthentication.class */
public final class PasswordAuthentication {
    private String userName;
    private String password;

    public PasswordAuthentication(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }
}
