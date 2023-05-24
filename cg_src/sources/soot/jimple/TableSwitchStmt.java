package soot.jimple;

import java.util.List;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/jimple/TableSwitchStmt.class */
public interface TableSwitchStmt extends SwitchStmt {
    void setLowIndex(int i);

    void setHighIndex(int i);

    int getLowIndex();

    int getHighIndex();

    void setTargets(List<? extends Unit> list);
}
