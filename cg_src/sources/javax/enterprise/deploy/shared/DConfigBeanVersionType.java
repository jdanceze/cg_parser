package javax.enterprise.deploy.shared;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/shared/DConfigBeanVersionType.class */
public class DConfigBeanVersionType {
    private int value;
    public static final DConfigBeanVersionType V1_3 = new DConfigBeanVersionType(0);
    public static final DConfigBeanVersionType V1_3_1 = new DConfigBeanVersionType(1);
    public static final DConfigBeanVersionType V1_4 = new DConfigBeanVersionType(2);
    private static final String[] stringTable = {"V1_3", "V1_3_1", "V1_4"};
    private static final DConfigBeanVersionType[] enumValueTable = {V1_3, V1_3_1, V1_4};

    protected DConfigBeanVersionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    protected String[] getStringTable() {
        return stringTable;
    }

    protected DConfigBeanVersionType[] getEnumValueTable() {
        return enumValueTable;
    }

    public static DConfigBeanVersionType getDConfigBeanVersionType(int value) {
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
