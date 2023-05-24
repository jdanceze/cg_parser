package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.IdentityUnit;
import soot.Local;
import soot.PhaseOptions;
import soot.Singletons;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.GroupIntPair;
import soot.options.Options;
import soot.util.Chain;
import soot.util.DeterministicHashMap;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/LocalPacker.class */
public class LocalPacker extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(LocalPacker.class);

    public LocalPacker(Singletons.Global g) {
    }

    public static LocalPacker v() {
        return G.v().soot_toolkits_scalar_LocalPacker();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "] Packing locals...");
        }
        Chain<Local> bodyLocalsRef = body.getLocals();
        int origLocalCount = bodyLocalsRef.size();
        if (origLocalCount < 1) {
            return;
        }
        Map<Local, Type> localToGroup = new DeterministicHashMap<>((origLocalCount * 2) + 1, 0.7f);
        Map<Type, Integer> groupToColorCount = new HashMap<>((origLocalCount * 2) + 1, 0.7f);
        Map<Local, Integer> localToColor = new HashMap<>((origLocalCount * 2) + 1, 0.7f);
        for (Local l : bodyLocalsRef) {
            Type g = l.getType();
            localToGroup.put(l, g);
            groupToColorCount.putIfAbsent(g, 0);
        }
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit s = it.next();
            if (s instanceof IdentityUnit) {
                Value leftOp = ((IdentityUnit) s).getLeftOp();
                if (leftOp instanceof Local) {
                    Local l2 = (Local) leftOp;
                    Type group = localToGroup.get(l2);
                    Integer count = groupToColorCount.get(group);
                    localToColor.put(l2, count);
                    groupToColorCount.put(group, Integer.valueOf(count.intValue() + 1));
                }
            }
        }
        if (PhaseOptions.getBoolean(options, "unsplit-original-locals")) {
            FastColorer.unsplitAssignColorsToLocals(body, localToGroup, localToColor, groupToColorCount);
        } else {
            FastColorer.assignColorsToLocals(body, localToGroup, localToColor, groupToColorCount);
        }
        Map<Local, Local> localToNewLocal = new HashMap<>((origLocalCount * 2) + 1, 0.7f);
        Map<GroupIntPair, Local> groupIntToLocal = new HashMap<>((origLocalCount * 2) + 1, 0.7f);
        List<Local> originalLocals = new ArrayList<>(bodyLocalsRef);
        bodyLocalsRef.clear();
        Set<String> usedLocalNames = new HashSet<>();
        for (Local original : originalLocals) {
            Type group2 = localToGroup.get(original);
            GroupIntPair pair = new GroupIntPair(group2, localToColor.get(original).intValue());
            Local newLocal = groupIntToLocal.get(pair);
            if (newLocal == null) {
                newLocal = (Local) original.clone();
                newLocal.setType(group2);
                String name = newLocal.getName();
                if (name != null) {
                    int signIndex = name.indexOf(35);
                    if (signIndex >= 0) {
                        String newName = name.substring(0, signIndex);
                        if (usedLocalNames.add(newName)) {
                            newLocal.setName(newName);
                        }
                    } else {
                        usedLocalNames.add(name);
                    }
                }
                groupIntToLocal.put(pair, newLocal);
                bodyLocalsRef.add(newLocal);
            }
            localToNewLocal.put(original, newLocal);
        }
        Iterator<Unit> it2 = body.getUnits().iterator();
        while (it2.hasNext()) {
            Unit s2 = it2.next();
            for (ValueBox box : s2.getUseBoxes()) {
                Value val = box.getValue();
                if (val instanceof Local) {
                    box.setValue(localToNewLocal.get((Local) val));
                }
            }
            for (ValueBox box2 : s2.getDefBoxes()) {
                Value val2 = box2.getValue();
                if (val2 instanceof Local) {
                    box2.setValue(localToNewLocal.get((Local) val2));
                }
            }
        }
    }
}
