package javax.enterprise.deploy.shared;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/shared/ModuleType.class */
public class ModuleType {
    private int value;
    public static final ModuleType EAR = new ModuleType(0);
    public static final ModuleType EJB = new ModuleType(1);
    public static final ModuleType CAR = new ModuleType(2);
    public static final ModuleType RAR = new ModuleType(3);
    public static final ModuleType WAR = new ModuleType(4);
    private static final String[] stringTable = {"ear", "ejb", "car", "rar", "war"};
    private static final ModuleType[] enumValueTable = {EAR, EJB, CAR, RAR, WAR};
    private static final String[] moduleExtension = {".ear", ".jar", ".jar", ".rar", ".war"};

    protected ModuleType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    protected String[] getStringTable() {
        return stringTable;
    }

    protected ModuleType[] getEnumValueTable() {
        return enumValueTable;
    }

    public String getModuleExtension() {
        return moduleExtension[getValue()];
    }

    public static ModuleType getModuleType(int value) {
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
