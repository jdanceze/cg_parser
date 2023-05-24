package soot.toDex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;
/* loaded from: gencallgraphv3.jar:soot/toDex/DexArrayInitDetector.class */
public class DexArrayInitDetector {
    private Map<Unit, List<Value>> arrayInitToFillValues = new HashMap();
    private Set<Unit> ignoreUnits = new HashSet();
    private int minimumArrayLength = -1;

    public void constructArrayInitializations(Body body) {
        Unit arrayInitStmt = null;
        List<Value> arrayValues = null;
        Set<Unit> curIgnoreUnits = null;
        int arraySize = 0;
        Value concernedArray = null;
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (!(u instanceof AssignStmt)) {
                arrayValues = null;
            } else {
                AssignStmt assignStmt = (AssignStmt) u;
                if (assignStmt.getRightOp() instanceof NewArrayExpr) {
                    NewArrayExpr newArrayExp = (NewArrayExpr) assignStmt.getRightOp();
                    if (newArrayExp.getSize() instanceof IntConstant) {
                        IntConstant intConst = (IntConstant) newArrayExp.getSize();
                        if (this.minimumArrayLength == -1 || intConst.value >= this.minimumArrayLength) {
                            arrayValues = new ArrayList<>();
                            arraySize = intConst.value;
                            curIgnoreUnits = new HashSet<>();
                            concernedArray = assignStmt.getLeftOp();
                        }
                    } else {
                        arrayValues = null;
                    }
                } else if ((assignStmt.getLeftOp() instanceof ArrayRef) && (assignStmt.getRightOp() instanceof IntConstant) && arrayValues != null) {
                    ArrayRef aref = (ArrayRef) assignStmt.getLeftOp();
                    if (aref.getBase() != concernedArray) {
                        arrayValues = null;
                    } else if (aref.getIndex() instanceof IntConstant) {
                        IntConstant intConst2 = (IntConstant) aref.getIndex();
                        if (intConst2.value == arrayValues.size()) {
                            arrayValues.add(assignStmt.getRightOp());
                            if (intConst2.value == 0) {
                                arrayInitStmt = u;
                            } else if (intConst2.value == arraySize - 1) {
                                curIgnoreUnits.add(u);
                                checkAndSave(arrayInitStmt, arrayValues, arraySize, curIgnoreUnits);
                                arrayValues = null;
                            } else {
                                curIgnoreUnits.add(u);
                            }
                        } else {
                            arrayValues = null;
                        }
                    } else {
                        arrayValues = null;
                    }
                } else {
                    arrayValues = null;
                }
            }
        }
    }

    public void setMinimumArrayLength(int l) {
        this.minimumArrayLength = l;
    }

    private void checkAndSave(Unit arrayInitStmt, List<Value> arrayValues, int arraySize, Set<Unit> curIgnoreUnits) {
        if (arrayValues != null && arrayValues.size() == arraySize && arrayInitStmt != null) {
            this.arrayInitToFillValues.put(arrayInitStmt, arrayValues);
            this.ignoreUnits.addAll(curIgnoreUnits);
        }
    }

    public List<Value> getValuesForArrayInit(Unit arrayInit) {
        return this.arrayInitToFillValues.get(arrayInit);
    }

    public Set<Unit> getIgnoreUnits() {
        return this.ignoreUnits;
    }

    public void fixTraps(Body activeBody) {
        Iterator<Trap> trapIt = activeBody.getTraps().iterator();
        while (trapIt.hasNext()) {
            Trap t = trapIt.next();
            Unit beginUnit = t.getBeginUnit();
            Unit endUnit = t.getEndUnit();
            while (this.ignoreUnits.contains(beginUnit)) {
                beginUnit = activeBody.getUnits().getPredOf((UnitPatchingChain) beginUnit);
                if (beginUnit == endUnit) {
                    trapIt.remove();
                    break;
                } else if (this.arrayInitToFillValues.containsKey(beginUnit)) {
                    break;
                }
            }
            while (true) {
                if (this.ignoreUnits.contains(endUnit)) {
                    endUnit = activeBody.getUnits().getSuccOf((UnitPatchingChain) endUnit);
                    if (beginUnit == endUnit) {
                        trapIt.remove();
                        break;
                    }
                } else {
                    t.setBeginUnit(beginUnit);
                    t.setEndUnit(endUnit);
                    break;
                }
            }
        }
    }
}
