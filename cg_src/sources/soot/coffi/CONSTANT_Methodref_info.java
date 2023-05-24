package soot.coffi;

import java.util.ArrayList;
import java.util.List;
import soot.Scene;
import soot.Type;
import soot.Value;
import soot.jimple.Jimple;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/CONSTANT_Methodref_info.class */
public class CONSTANT_Methodref_info extends cp_info implements ICONSTANT_Methodref_info {
    public int class_index;
    public int name_and_type_index;

    @Override // soot.coffi.cp_info
    public int size() {
        return 5;
    }

    @Override // soot.coffi.cp_info
    public String toString(cp_info[] constant_pool) {
        CONSTANT_Class_info cc = (CONSTANT_Class_info) constant_pool[this.class_index];
        CONSTANT_NameAndType_info cn = (CONSTANT_NameAndType_info) constant_pool[this.name_and_type_index];
        return String.valueOf(cc.toString(constant_pool)) + "." + cn.toString(constant_pool);
    }

    @Override // soot.coffi.cp_info
    public String typeName() {
        return "methodref";
    }

    @Override // soot.coffi.cp_info
    public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
        if (this.tag != cp.tag) {
            return this.tag - cp.tag;
        }
        CONSTANT_Methodref_info cu = (CONSTANT_Methodref_info) cp;
        int i = constant_pool[this.class_index].compareTo(constant_pool, cp_constant_pool[cu.class_index], cp_constant_pool);
        if (i != 0) {
            return i;
        }
        return constant_pool[this.name_and_type_index].compareTo(constant_pool, cp_constant_pool[cu.name_and_type_index], cp_constant_pool);
    }

    @Override // soot.coffi.cp_info
    public Value createJimpleConstantValue(cp_info[] constant_pool) {
        CONSTANT_Class_info cc = (CONSTANT_Class_info) constant_pool[this.class_index];
        CONSTANT_NameAndType_info cn = (CONSTANT_NameAndType_info) constant_pool[this.name_and_type_index];
        String className = cc.toString(constant_pool);
        String nameAndType = cn.toString(constant_pool);
        String name = nameAndType.substring(0, nameAndType.indexOf(":"));
        String typeName = nameAndType.substring(nameAndType.indexOf(":") + 1);
        Type[] types = Util.v().jimpleTypesOfFieldOrMethodDescriptor(typeName);
        List parameterTypes = new ArrayList();
        for (int k = 0; k < types.length - 1; k++) {
            parameterTypes.add(types[k]);
        }
        Type returnType = types[types.length - 1];
        return Jimple.v().newStaticInvokeExpr(Scene.v().makeMethodRef(Scene.v().getSootClass(className), name, parameterTypes, returnType, true));
    }

    @Override // soot.coffi.ICONSTANT_Methodref_info
    public int getClassIndex() {
        return this.class_index;
    }

    @Override // soot.coffi.ICONSTANT_Methodref_info
    public int getNameAndTypeIndex() {
        return this.name_and_type_index;
    }
}
