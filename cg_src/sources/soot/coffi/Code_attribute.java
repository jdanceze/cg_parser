package soot.coffi;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/coffi/Code_attribute.class */
public class Code_attribute extends attribute_info {
    public int max_stack;
    public int max_locals;
    public long code_length;
    public byte[] code;
    public int exception_table_length;
    public exception_table_entry[] exception_table;
    int attributes_count;
    attribute_info[] attributes;

    public LocalVariableTable_attribute findLocalVariableTable() {
        for (int i = 0; i < this.attributes_count; i++) {
            if (this.attributes[i] instanceof LocalVariableTable_attribute) {
                return (LocalVariableTable_attribute) this.attributes[i];
            }
        }
        return null;
    }

    public LocalVariableTypeTable_attribute findLocalVariableTypeTable() {
        for (int i = 0; i < this.attributes_count; i++) {
            if (this.attributes[i] instanceof LocalVariableTypeTable_attribute) {
                return (LocalVariableTypeTable_attribute) this.attributes[i];
            }
        }
        return null;
    }
}
