package javax.enterprise.deploy.shared;

import org.apache.tools.ant.taskdefs.optional.j2ee.HotDeploymentTool;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/shared/CommandType.class */
public class CommandType {
    private int value;
    public static final CommandType DISTRIBUTE = new CommandType(0);
    public static final CommandType START = new CommandType(1);
    public static final CommandType STOP = new CommandType(2);
    public static final CommandType UNDEPLOY = new CommandType(3);
    public static final CommandType REDEPLOY = new CommandType(4);
    private static final String[] stringTable = {"distribute", "start", "stop", HotDeploymentTool.ACTION_UNDEPLOY, "redeploy"};
    private static final CommandType[] enumValueTable = {DISTRIBUTE, START, STOP, UNDEPLOY, REDEPLOY};

    protected CommandType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    protected String[] getStringTable() {
        return stringTable;
    }

    protected CommandType[] getEnumValueTable() {
        return enumValueTable;
    }

    public static CommandType getCommandType(int value) {
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
