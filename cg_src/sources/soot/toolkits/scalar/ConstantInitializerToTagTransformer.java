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
import soot.ConflictingFieldRefException;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.StaticFieldRef;
import soot.jimple.StringConstant;
import soot.tagkit.ConstantValueTag;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/ConstantInitializerToTagTransformer.class */
public class ConstantInitializerToTagTransformer extends SceneTransformer {
    private static final Logger logger = LoggerFactory.getLogger(ConstantInitializerToTagTransformer.class);
    private static final ConstantInitializerToTagTransformer INSTANCE = new ConstantInitializerToTagTransformer();

    public static ConstantInitializerToTagTransformer v() {
        return INSTANCE;
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        for (SootClass sc : Scene.v().getClasses()) {
            transformClass(sc, false);
        }
    }

    public void transformClass(SootClass sc, boolean removeAssignments) {
        SootMethod smInit = sc.getMethodByNameUnsafe("<clinit>");
        if (smInit == null || !smInit.isConcrete()) {
            return;
        }
        Set<SootField> nonConstantFields = new HashSet<>();
        Map<SootField, ConstantValueTag> newTags = new HashMap<>();
        Set<SootField> removeTagList = new HashSet<>();
        Iterator<Unit> itU = smInit.getActiveBody().getUnits().snapshotIterator();
        while (itU.hasNext()) {
            Unit u = itU.next();
            if (u instanceof AssignStmt) {
                AssignStmt assign = (AssignStmt) u;
                Value leftOp = assign.getLeftOp();
                if (leftOp instanceof StaticFieldRef) {
                    Value rightOp = assign.getRightOp();
                    if (rightOp instanceof Constant) {
                        try {
                            SootField field = ((StaticFieldRef) leftOp).getField();
                            if (field != null && !nonConstantFields.contains(field) && field.getDeclaringClass().equals(sc) && field.isStatic() && field.isFinal()) {
                                boolean found = false;
                                Iterator<Tag> it = field.getTags().iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    Tag t = it.next();
                                    if (t instanceof ConstantValueTag) {
                                        if (checkConstantValue((ConstantValueTag) t, (Constant) rightOp)) {
                                            if (removeAssignments) {
                                                itU.remove();
                                            }
                                        } else {
                                            logger.debug("WARNING: Constant value for field '" + field + "' mismatch between code (" + rightOp + ") and constant table (" + t + ")");
                                            removeTagList.add(field);
                                        }
                                        found = true;
                                    }
                                }
                                if (!found) {
                                    if (!checkConstantValue(newTags.get(field), (Constant) rightOp)) {
                                        nonConstantFields.add(field);
                                        newTags.remove(field);
                                        removeTagList.add(field);
                                    } else {
                                        ConstantValueTag newTag = createConstantTagFromValue((Constant) rightOp);
                                        if (newTag != null) {
                                            newTags.put(field, newTag);
                                        }
                                    }
                                }
                            }
                        } catch (ConflictingFieldRefException e) {
                        }
                    } else {
                        try {
                            SootField sf = ((StaticFieldRef) leftOp).getField();
                            if (sf != null) {
                                removeTagList.add(sf);
                            }
                        } catch (ConflictingFieldRefException e2) {
                        }
                    }
                }
            }
        }
        for (Map.Entry<SootField, ConstantValueTag> entry : newTags.entrySet()) {
            SootField field2 = entry.getKey();
            if (!removeTagList.contains(field2)) {
                field2.addTag(entry.getValue());
            }
        }
        if (removeAssignments && !newTags.isEmpty()) {
            Iterator<Unit> itU2 = smInit.getActiveBody().getUnits().snapshotIterator();
            while (itU2.hasNext()) {
                Unit u2 = itU2.next();
                if (u2 instanceof AssignStmt) {
                    Value leftOp2 = ((AssignStmt) u2).getLeftOp();
                    if (leftOp2 instanceof FieldRef) {
                        try {
                            SootField fld = ((FieldRef) leftOp2).getField();
                            if (fld != null && newTags.containsKey(fld)) {
                                itU2.remove();
                            }
                        } catch (ConflictingFieldRefException e3) {
                        }
                    }
                }
            }
        }
        for (SootField sf2 : removeTagList) {
            if (removeTagList.contains(sf2)) {
                List<Tag> toRemoveTagList = new ArrayList<>();
                for (Tag t2 : sf2.getTags()) {
                    if (t2 instanceof ConstantValueTag) {
                        toRemoveTagList.add(t2);
                    }
                }
                for (Tag t3 : toRemoveTagList) {
                    sf2.getTags().remove(t3);
                }
            }
        }
    }

    private ConstantValueTag createConstantTagFromValue(Constant rightOp) {
        if (rightOp instanceof DoubleConstant) {
            return new DoubleConstantValueTag(((DoubleConstant) rightOp).value);
        }
        if (rightOp instanceof FloatConstant) {
            return new FloatConstantValueTag(((FloatConstant) rightOp).value);
        }
        if (rightOp instanceof IntConstant) {
            return new IntegerConstantValueTag(((IntConstant) rightOp).value);
        }
        if (rightOp instanceof LongConstant) {
            return new LongConstantValueTag(((LongConstant) rightOp).value);
        }
        if (rightOp instanceof StringConstant) {
            return new StringConstantValueTag(((StringConstant) rightOp).value);
        }
        return null;
    }

    private boolean checkConstantValue(ConstantValueTag t, Constant rightOp) {
        if (t == null || rightOp == null) {
            return true;
        }
        if (t instanceof DoubleConstantValueTag) {
            return (rightOp instanceof DoubleConstant) && ((DoubleConstantValueTag) t).getDoubleValue() == ((DoubleConstant) rightOp).value;
        } else if (t instanceof FloatConstantValueTag) {
            return (rightOp instanceof FloatConstant) && ((FloatConstantValueTag) t).getFloatValue() == ((FloatConstant) rightOp).value;
        } else if (t instanceof IntegerConstantValueTag) {
            return (rightOp instanceof IntConstant) && ((IntegerConstantValueTag) t).getIntValue() == ((IntConstant) rightOp).value;
        } else if (t instanceof LongConstantValueTag) {
            return (rightOp instanceof LongConstant) && ((LongConstantValueTag) t).getLongValue() == ((LongConstant) rightOp).value;
        } else if (t instanceof StringConstantValueTag) {
            return (rightOp instanceof StringConstant) && ((StringConstantValueTag) t).getStringValue().equals(((StringConstant) rightOp).value);
        } else {
            return true;
        }
    }
}
