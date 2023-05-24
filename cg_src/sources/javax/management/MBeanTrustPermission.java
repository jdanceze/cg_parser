package javax.management;

import java.security.BasicPermission;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanTrustPermission.class */
public class MBeanTrustPermission extends BasicPermission {
    private static final long serialVersionUID = -2952178077029018140L;

    public MBeanTrustPermission(String str) {
        this(str, null);
    }

    public MBeanTrustPermission(String str, String str2) {
        super(str, str2);
        if (str2 != null && str2.length() > 0) {
            throw new IllegalArgumentException(new StringBuffer().append("MBeanTrustPermission actions must be null: ").append(str2).toString());
        }
        if (!str.equals("register") && !str.equals("*")) {
            throw new IllegalArgumentException(new StringBuffer().append("MBeanTrustPermission: Unknown target name [").append(str).append("]").toString());
        }
    }
}
