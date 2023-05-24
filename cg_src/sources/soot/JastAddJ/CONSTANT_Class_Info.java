package soot.JastAddJ;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/CONSTANT_Class_Info.class */
public class CONSTANT_Class_Info extends CONSTANT_Info {
    public int name_index;

    public CONSTANT_Class_Info(BytecodeParser parser) {
        super(parser);
        this.name_index = this.p.u2();
    }

    public String toString() {
        return "ClassInfo: " + name();
    }

    public String name() {
        String name = ((CONSTANT_Utf8_Info) this.p.constantPool[this.name_index]).string();
        return name.replace('/', '.');
    }

    public String simpleName() {
        String name = name();
        int pos = name.lastIndexOf(46);
        return name.substring(pos + 1, name.length());
    }

    public String packageDecl() {
        String name = name();
        int pos = name.lastIndexOf(46);
        if (pos == -1) {
            return "";
        }
        return name.substring(0, pos);
    }

    public Access access() {
        String name = name();
        int pos = name.lastIndexOf(46);
        String typeName = name.substring(pos + 1, name.length());
        String packageName = pos == -1 ? "" : name.substring(0, pos);
        if (typeName.indexOf(36) != -1) {
            return new BytecodeTypeAccess(packageName, typeName);
        }
        return new TypeAccess(packageName, typeName);
    }
}
