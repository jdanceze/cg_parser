package soot.baf;
/* loaded from: gencallgraphv3.jar:soot/baf/TableSwitchInst.class */
public interface TableSwitchInst extends SwitchInst {
    int getLowIndex();

    void setLowIndex(int i);

    int getHighIndex();

    void setHighIndex(int i);
}
