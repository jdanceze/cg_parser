package soot.coffi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/LocalVariableTable_attribute.class */
public class LocalVariableTable_attribute extends attribute_info {
    private static final Logger logger = LoggerFactory.getLogger(LocalVariableTable_attribute.class);
    public int local_variable_table_length;
    public local_variable_table_entry[] local_variable_table;

    public String getLocalVariableName(cp_info[] constant_pool, int idx) {
        return getLocalVariableName(constant_pool, idx, -1);
    }

    public String getLocalVariableName(cp_info[] constant_pool, int idx, int code) {
        for (int i = 0; i < this.local_variable_table_length; i++) {
            local_variable_table_entry e = this.local_variable_table[i];
            if (e.index == idx && (code == -1 || (code >= e.start_pc && code < e.start_pc + e.length))) {
                if (constant_pool[e.name_index] instanceof CONSTANT_Utf8_info) {
                    String n = ((CONSTANT_Utf8_info) constant_pool[e.name_index]).convert();
                    if (Util.v().isValidJimpleName(n)) {
                        return n;
                    }
                    return null;
                } else {
                    throw new RuntimeException("What? A local variable table name_index isn't a UTF8 entry?");
                }
            }
        }
        return null;
    }

    public String getLocalVariableDescriptor(cp_info[] constant_pool, int idx, int code) {
        for (int i = 0; i < this.local_variable_table_length; i++) {
            local_variable_table_entry e = this.local_variable_table[i];
            if (e.index == idx && (code == -1 || (code >= e.start_pc && code < e.start_pc + e.length))) {
                if (constant_pool[e.descriptor_index] instanceof CONSTANT_Utf8_info) {
                    String n = ((CONSTANT_Utf8_info) constant_pool[e.descriptor_index]).convert();
                    return n;
                } else {
                    throw new RuntimeException("What? A local variable table name_index isn't a UTF8 entry?");
                }
            }
        }
        return null;
    }

    public String getEntryName(cp_info[] constant_pool, int entryIndex) {
        try {
            local_variable_table_entry e = this.local_variable_table[entryIndex];
            if (constant_pool[e.name_index] instanceof CONSTANT_Utf8_info) {
                String n = ((CONSTANT_Utf8_info) constant_pool[e.name_index]).convert();
                if (Util.v().isValidJimpleName(n)) {
                    return n;
                }
                return null;
            }
            throw new RuntimeException("name_index not addressing an UTF8 entry.");
        } catch (ArrayIndexOutOfBoundsException e2) {
            return null;
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < this.local_variable_table_length; i++) {
            buffer.append(String.valueOf(this.local_variable_table[i].toString()) + "\n");
        }
        return buffer.toString();
    }
}
