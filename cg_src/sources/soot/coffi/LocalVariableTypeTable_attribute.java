package soot.coffi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/LocalVariableTypeTable_attribute.class */
public class LocalVariableTypeTable_attribute extends attribute_info {
    private static final Logger logger = LoggerFactory.getLogger(LocalVariableTypeTable_attribute.class);
    public int local_variable_type_table_length;
    public local_variable_type_table_entry[] local_variable_type_table;

    public String getLocalVariableType(cp_info[] constant_pool, int idx) {
        return getLocalVariableType(constant_pool, idx, -1);
    }

    public String getLocalVariableType(cp_info[] constant_pool, int idx, int code) {
        for (int i = 0; i < this.local_variable_type_table_length; i++) {
            local_variable_type_table_entry e = this.local_variable_type_table[i];
            if (e.index == idx && (code == -1 || (code >= e.start_pc && code <= e.start_pc + e.length))) {
                if (constant_pool[e.signature_index] instanceof CONSTANT_Utf8_info) {
                    ((CONSTANT_Utf8_info) constant_pool[e.signature_index]).convert();
                } else {
                    throw new RuntimeException("What? A local variable type table signature_index isn't a UTF8 entry?");
                }
            }
        }
        return null;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < this.local_variable_type_table_length; i++) {
            buffer.append(String.valueOf(this.local_variable_type_table[i].toString()) + "\n");
        }
        return buffer.toString();
    }
}
