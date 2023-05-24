package soot.jbco.jimpleTransformations;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Hierarchy;
import soot.NullType;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jbco.IJbcoTransform;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
import soot.jimple.ClassConstant;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.StringConstant;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jbco/jimpleTransformations/CollectConstants.class */
public class CollectConstants extends SceneTransformer implements IJbcoTransform {
    public static final String name = "wjtp.jbco_cc";
    private final Map<Type, List<Constant>> typeToConstants = new HashMap();
    private int constants = 0;
    private int updatedConstants = 0;
    private static final Logger logger = LoggerFactory.getLogger(FieldRenamer.class);
    public static HashMap<Constant, SootField> constantsToFields = new HashMap<>();

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return new String[]{name};
    }

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
        logger.info("Found {} constants, updated {} ones", Integer.valueOf(this.constants), Integer.valueOf(this.updatedConstants));
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        SootClass randomClass;
        if (isVerbose()) {
            logger.info("Collecting Constant Data");
        }
        BodyBuilder.retrieveAllNames();
        Chain<SootClass> applicationClasses = Scene.v().getApplicationClasses();
        for (SootClass applicationClass : applicationClasses) {
            for (SootMethod method : applicationClass.getMethods()) {
                if (method.hasActiveBody() && !method.getName().contains("<clinit>")) {
                    for (ValueBox useBox : method.getActiveBody().getUseBoxes()) {
                        Value value = useBox.getValue();
                        if (value instanceof Constant) {
                            Constant constant = (Constant) value;
                            List<Constant> constants = this.typeToConstants.computeIfAbsent(constant.getType(), t -> {
                                return new ArrayList();
                            });
                            if (!constants.contains(constant)) {
                                this.constants++;
                                constants.add(constant);
                            }
                        }
                    }
                }
            }
        }
        int count = 0;
        String name2 = "newConstantJbcoName";
        SootClass[] classes = (SootClass[]) applicationClasses.toArray(new SootClass[applicationClasses.size()]);
        for (Type type : this.typeToConstants.keySet()) {
            if (!(type instanceof NullType)) {
                for (Constant constant2 : this.typeToConstants.get(type)) {
                    name2 = String.valueOf(name2) + "_";
                    do {
                        randomClass = classes[Rand.getInt(classes.length)];
                    } while (!isSuitableClassToAddFieldConstant(randomClass, constant2));
                    SootField newField = Scene.v().makeSootField(FieldRenamer.v().getOrAddNewName(name2), type, 9);
                    randomClass.addField(newField);
                    constantsToFields.put(constant2, newField);
                    addInitializingValue(randomClass, newField, constant2);
                    count++;
                }
            }
        }
        this.updatedConstants += count;
    }

    private boolean isSuitableClassToAddFieldConstant(SootClass sc, Constant constant) {
        if (sc.isInterface()) {
            return false;
        }
        if (constant instanceof ClassConstant) {
            ClassConstant classConstant = (ClassConstant) constant;
            RefType type = (RefType) classConstant.toSootType();
            SootClass classFromConstant = type.getSootClass();
            Hierarchy hierarchy = Scene.v().getActiveHierarchy();
            return hierarchy.isVisible(sc, classFromConstant);
        }
        return true;
    }

    private void addInitializingValue(SootClass sc, SootField f, Constant constant) {
        Body b;
        if (constant instanceof NullConstant) {
            return;
        }
        if (constant instanceof IntConstant) {
            if (((IntConstant) constant).value == 0) {
                return;
            }
        } else if (constant instanceof LongConstant) {
            if (((LongConstant) constant).value == 0) {
                return;
            }
        } else if (constant instanceof StringConstant) {
            if (((StringConstant) constant).value == null) {
                return;
            }
        } else if (constant instanceof DoubleConstant) {
            if (((DoubleConstant) constant).value == Const.default_value_double) {
                return;
            }
        } else if ((constant instanceof FloatConstant) && ((FloatConstant) constant).value == 0.0f) {
            return;
        }
        boolean newInit = false;
        if (!sc.declaresMethodByName("<clinit>")) {
            SootMethod m = Scene.v().makeSootMethod("<clinit>", Collections.emptyList(), VoidType.v(), 8);
            sc.addMethod(m);
            b = Jimple.v().newBody(m);
            m.setActiveBody(b);
            newInit = true;
        } else {
            SootMethod m2 = sc.getMethodByName("<clinit>");
            if (!m2.hasActiveBody()) {
                b = Jimple.v().newBody(m2);
                m2.setActiveBody(b);
                newInit = true;
            } else {
                b = m2.getActiveBody();
            }
        }
        PatchingChain<Unit> units = b.getUnits();
        units.addFirst((PatchingChain<Unit>) Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(f.makeRef()), constant));
        if (newInit) {
            units.addLast((PatchingChain<Unit>) Jimple.v().newReturnVoidStmt());
        }
    }
}
