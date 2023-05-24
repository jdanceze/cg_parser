package javax.management.relation;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/relation/RoleStatus.class */
public class RoleStatus {
    public static final int NO_ROLE_WITH_NAME = 1;
    public static final int ROLE_NOT_READABLE = 2;
    public static final int ROLE_NOT_WRITABLE = 3;
    public static final int LESS_THAN_MIN_ROLE_DEGREE = 4;
    public static final int MORE_THAN_MAX_ROLE_DEGREE = 5;
    public static final int REF_MBEAN_OF_INCORRECT_CLASS = 6;
    public static final int REF_MBEAN_NOT_REGISTERED = 7;

    public static boolean isRoleStatus(int i) {
        if (i != 1 && i != 2 && i != 3 && i != 4 && i != 5 && i != 6 && i != 7) {
            return false;
        }
        return true;
    }
}
