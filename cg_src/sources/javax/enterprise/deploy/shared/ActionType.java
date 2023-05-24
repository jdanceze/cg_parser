package javax.enterprise.deploy.shared;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/shared/ActionType.class */
public class ActionType {
    private int value;
    public static final ActionType EXECUTE = new ActionType(0);
    public static final ActionType CANCEL = new ActionType(1);
    public static final ActionType STOP = new ActionType(2);
    private static final String[] stringTable = {"execute", "cancel", "stop"};
    private static final ActionType[] enumValueTable = {EXECUTE, CANCEL, STOP};

    protected ActionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    protected String[] getStringTable() {
        return stringTable;
    }

    protected ActionType[] getEnumValueTable() {
        return enumValueTable;
    }

    public static ActionType getActionType(int value) {
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
