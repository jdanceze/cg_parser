package soot.shimple;

import java.util.List;
import soot.Unit;
import soot.Value;
import soot.toolkits.graph.Block;
import soot.toolkits.scalar.ValueUnitPair;
/* loaded from: gencallgraphv3.jar:soot/shimple/PhiExpr.class */
public interface PhiExpr extends ShimpleExpr {
    List<ValueUnitPair> getArgs();

    List<Value> getValues();

    List<Unit> getPreds();

    int getArgCount();

    ValueUnitPair getArgBox(int i);

    Value getValue(int i);

    Unit getPred(int i);

    int getArgIndex(Unit unit);

    ValueUnitPair getArgBox(Unit unit);

    Value getValue(Unit unit);

    int getArgIndex(Block block);

    ValueUnitPair getArgBox(Block block);

    Value getValue(Block block);

    boolean setArg(int i, Value value, Unit unit);

    boolean setArg(int i, Value value, Block block);

    boolean setValue(int i, Value value);

    boolean setValue(Unit unit, Value value);

    boolean setValue(Block block, Value value);

    boolean setPred(int i, Unit unit);

    boolean setPred(int i, Block block);

    boolean removeArg(int i);

    boolean removeArg(Unit unit);

    boolean removeArg(Block block);

    boolean removeArg(ValueUnitPair valueUnitPair);

    boolean addArg(Value value, Block block);

    boolean addArg(Value value, Unit unit);

    void setBlockId(int i);

    int getBlockId();
}
