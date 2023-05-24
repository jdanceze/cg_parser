package soot.coffi;
/* loaded from: gencallgraphv3.jar:soot/coffi/exception_table_entry.class */
class exception_table_entry {
    public int start_pc;
    public int end_pc;
    public int handler_pc;
    public int catch_type;
    public Instruction start_inst;
    public Instruction end_inst;
    public Instruction handler_inst;
    public BasicBlock b;
}
