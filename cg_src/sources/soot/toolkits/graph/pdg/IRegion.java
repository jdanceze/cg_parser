package soot.toolkits.graph.pdg;

import java.util.List;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.UnitGraph;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/IRegion.class */
public interface IRegion {
    SootMethod getSootMethod();

    SootClass getSootClass();

    UnitGraph getUnitGraph();

    List<Unit> getUnits();

    List<Unit> getUnits(Unit unit, Unit unit2);

    List<Block> getBlocks();

    Unit getLast();

    Unit getFirst();

    int getID();

    boolean occursBefore(Unit unit, Unit unit2);

    void setParent(IRegion iRegion);

    IRegion getParent();

    void addChildRegion(IRegion iRegion);

    List<IRegion> getChildRegions();
}
