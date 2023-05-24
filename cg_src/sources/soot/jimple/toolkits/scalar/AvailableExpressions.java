package soot.jimple.toolkits.scalar;

import java.util.List;
import soot.EquivalentValue;
import soot.Unit;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/AvailableExpressions.class */
public interface AvailableExpressions {
    List<UnitValueBoxPair> getAvailablePairsBefore(Unit unit);

    List<UnitValueBoxPair> getAvailablePairsAfter(Unit unit);

    Chain<EquivalentValue> getAvailableEquivsBefore(Unit unit);

    Chain<EquivalentValue> getAvailableEquivsAfter(Unit unit);
}
