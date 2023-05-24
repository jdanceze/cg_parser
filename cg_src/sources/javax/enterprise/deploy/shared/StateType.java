package javax.enterprise.deploy.shared;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/shared/StateType.class */
public class StateType {
    private int value;
    public static final StateType RUNNING = new StateType(0);
    public static final StateType COMPLETED = new StateType(1);
    public static final StateType FAILED = new StateType(2);
    public static final StateType RELEASED = new StateType(3);
    private static final String[] stringTable = {"running", "completed", "failed", "released"};
    private static final StateType[] enumValueTable = {RUNNING, COMPLETED, FAILED, RELEASED};

    protected StateType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    protected String[] getStringTable() {
        return stringTable;
    }

    protected StateType[] getEnumValueTable() {
        return enumValueTable;
    }

    public static StateType getStateType(int value) {
        return enumValueTable[value];
    }

    public String toString() {
        String[] strTable = getStringTable();
        int index = this.value - getOffset();
        if (strTable != null && index >= 0 && index < strTable.length) {
            return strTable[index];
        }
        return Integer.toString(this.value);
    }

    protected int getOffset() {
        return 0;
    }
}
