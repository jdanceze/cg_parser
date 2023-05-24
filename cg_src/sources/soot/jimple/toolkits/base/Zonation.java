package soot.jimple.toolkits.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.resource.spi.work.WorkException;
import soot.Trap;
import soot.Unit;
import soot.jimple.StmtBody;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/base/Zonation.class */
public class Zonation {
    private final Map<Unit, Zone> unitToZone;
    private int zoneCount;

    public Zonation(StmtBody body) {
        Chain<Unit> units = body.getUnits();
        this.zoneCount = 0;
        this.unitToZone = new HashMap((units.size() * 2) + 1, 0.7f);
        Map<Unit, List<Trap>> unitToTrapBoundaries = new HashMap<>();
        for (Trap t : body.getTraps()) {
            addTrapBoundary(t.getBeginUnit(), t, unitToTrapBoundaries);
            addTrapBoundary(t.getEndUnit(), t, unitToTrapBoundaries);
        }
        Map<List<Trap>, Zone> trapListToZone = new HashMap<>(10, 0.7f);
        List<Trap> currentTraps = new ArrayList<>();
        Zone currentZone = new Zone(WorkException.UNDEFINED);
        trapListToZone.put(new ArrayList<>(), currentZone);
        for (Unit u : units) {
            List<Trap> trapBoundaries = unitToTrapBoundaries.get(u);
            if (trapBoundaries != null && !trapBoundaries.isEmpty()) {
                for (Trap trap : trapBoundaries) {
                    if (currentTraps.contains(trap)) {
                        currentTraps.remove(trap);
                    } else {
                        currentTraps.add(trap);
                    }
                }
                if (trapListToZone.containsKey(currentTraps)) {
                    currentZone = trapListToZone.get(currentTraps);
                } else {
                    this.zoneCount++;
                    currentZone = new Zone(Integer.toString(this.zoneCount));
                    trapListToZone.put(currentTraps, currentZone);
                }
            }
            this.unitToZone.put(u, currentZone);
        }
    }

    private void addTrapBoundary(Unit unit, Trap t, Map<Unit, List<Trap>> unitToTrapBoundaries) {
        List<Trap> boundary = unitToTrapBoundaries.get(unit);
        if (boundary == null) {
            boundary = new ArrayList<>();
            unitToTrapBoundaries.put(unit, boundary);
        }
        boundary.add(t);
    }

    public Zone getZoneOf(Unit u) {
        Zone z = this.unitToZone.get(u);
        if (z == null) {
            throw new RuntimeException("null zone!");
        }
        return z;
    }

    public int getZoneCount() {
        return this.zoneCount;
    }
}
