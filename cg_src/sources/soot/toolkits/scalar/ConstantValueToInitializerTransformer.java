package soot.toolkits.scalar;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/ConstantValueToInitializerTransformer.class */
public class ConstantValueToInitializerTransformer extends SceneTransformer {
    public static ConstantValueToInitializerTransformer v() {
        return new ConstantValueToInitializerTransformer();
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        for (SootClass sc : Scene.v().getClasses()) {
            transformClass(sc);
        }
    }

    public void transformClass(SootClass sc) {
        Jimple jimp = Jimple.v();
        SootMethod smInit = null;
        Set<SootField> alreadyInitialized = new HashSet<>();
        for (SootField sf : sc.getFields()) {
            if (!alreadyInitialized.contains(sf)) {
                for (Tag t : sf.getTags()) {
                    Constant constant = null;
                    if (t instanceof DoubleConstantValueTag) {
                        double value = ((DoubleConstantValueTag) t).getDoubleValue();
                        constant = DoubleConstant.v(value);
                    } else if (t instanceof FloatConstantValueTag) {
                        float value2 = ((FloatConstantValueTag) t).getFloatValue();
                        constant = FloatConstant.v(value2);
                    } else if (t instanceof IntegerConstantValueTag) {
                        int value3 = ((IntegerConstantValueTag) t).getIntValue();
                        constant = IntConstant.v(value3);
                    } else if (t instanceof LongConstantValueTag) {
                        long value4 = ((LongConstantValueTag) t).getLongValue();
                        constant = LongConstant.v(value4);
                    } else if (t instanceof StringConstantValueTag) {
                        String value5 = ((StringConstantValueTag) t).getStringValue();
                        constant = StringConstant.v(value5);
                    }
                    if (constant != null) {
                        if (sf.isStatic()) {
                            AssignStmt newAssignStmt = jimp.newAssignStmt(jimp.newStaticFieldRef(sf.makeRef()), constant);
                            if (smInit == null) {
                                smInit = getOrCreateInitializer(sc, alreadyInitialized);
                            }
                            if (smInit != null) {
                                smInit.getActiveBody().getUnits().addFirst((UnitPatchingChain) newAssignStmt);
                            }
                        } else {
                            for (SootMethod m : sc.getMethods()) {
                                if (m.isConstructor()) {
                                    Body body = m.retrieveActiveBody();
                                    UnitPatchingChain units = body.getUnits();
                                    Local thisLocal = null;
                                    Iterator<Unit> it = units.iterator();
                                    while (true) {
                                        if (!it.hasNext()) {
                                            break;
                                        }
                                        Unit u = it.next();
                                        if (u instanceof Stmt) {
                                            Stmt s = (Stmt) u;
                                            if (s.containsInvokeExpr()) {
                                                InvokeExpr expr = s.getInvokeExpr();
                                                if (expr instanceof SpecialInvokeExpr) {
                                                    if (expr.getMethod().getDeclaringClass() != sc) {
                                                        if (0 == 0) {
                                                            thisLocal = body.getThisLocal();
                                                        }
                                                        units.insertAfter(jimp.newAssignStmt(jimp.newInstanceFieldRef(thisLocal, sf.makeRef()), constant), (AssignStmt) s);
                                                    }
                                                }
                                            } else {
                                                continue;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (smInit != null) {
            Chain<Unit> units2 = smInit.getActiveBody().getUnits();
            if (units2.isEmpty() || !(units2.getLast() instanceof ReturnVoidStmt)) {
                units2.add(jimp.newReturnVoidStmt());
            }
        }
    }

    private SootMethod getOrCreateInitializer(SootClass sc, Set<SootField> alreadyInitialized) {
        SootMethod smInit = sc.getMethodByNameUnsafe("<clinit>");
        if (smInit == null) {
            smInit = Scene.v().makeSootMethod("<clinit>", Collections.emptyList(), VoidType.v());
            smInit.setActiveBody(Jimple.v().newBody(smInit));
            sc.addMethod(smInit);
            smInit.setModifiers(9);
        } else if (smInit.isPhantom()) {
            return null;
        } else {
            Iterator<Unit> it = smInit.retrieveActiveBody().getUnits().iterator();
            while (it.hasNext()) {
                Unit u = it.next();
                Stmt s = (Stmt) u;
                for (ValueBox vb : s.getDefBoxes()) {
                    Value value = vb.getValue();
                    if (value instanceof FieldRef) {
                        alreadyInitialized.add(((FieldRef) value).getField());
                    }
                }
            }
        }
        return smInit;
    }
}
