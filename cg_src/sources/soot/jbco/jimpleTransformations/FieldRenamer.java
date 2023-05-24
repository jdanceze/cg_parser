package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BooleanType;
import soot.G;
import soot.IntegerType;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.name.JunkNameGenerator;
import soot.jbco.name.NameGenerator;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
import soot.jimple.FieldRef;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.tagkit.SignatureTag;
/* loaded from: gencallgraphv3.jar:soot/jbco/jimpleTransformations/FieldRenamer.class */
public class FieldRenamer extends SceneTransformer implements IJbcoTransform {
    public static final String name = "wjtp.jbco_fr";
    private final NameGenerator nameGenerator;
    private final Map<String, String> oldToNewFieldNames = new HashMap();
    private final Map<SootClass, SootField> opaquePredicate1ByClass = new HashMap();
    private final Map<SootClass, SootField> opaquePredicate2ByClass = new HashMap();
    private SootField[][] opaquePairs = null;
    private final Set<String> skipFields = new HashSet();
    private boolean renameFields = false;
    private final Object fieldNamesLock = new Object();
    private static final Logger logger = LoggerFactory.getLogger(FieldRenamer.class);
    private static final String BOOLEAN_CLASS_NAME = Boolean.class.getName();
    private static final SootField[] EMPTY_ARRAY = new SootField[0];
    public static int[] handedOutPairs = null;
    public static int[] handedOutRunPairs = null;

    public FieldRenamer(Singletons.Global global) {
        if (global == null) {
            throw new NullPointerException("Cannot instantiate FieldRenamer with null Singletons.Global");
        }
        this.nameGenerator = new JunkNameGenerator();
    }

    public static FieldRenamer v() {
        return G.v().soot_jbco_jimpleTransformations_FieldRenamer();
    }

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
        logger.info("Processed field mapping:");
        this.oldToNewFieldNames.forEach(oldName, newName -> {
            logger.info("{} -> {}", oldName, newName);
        });
    }

    public boolean isRenameFields() {
        return this.renameFields;
    }

    public void setRenameFields(boolean renameFields) {
        this.renameFields = renameFields;
    }

    public void setSkipFields(Collection<String> fields) {
        if (!this.skipFields.isEmpty()) {
            this.skipFields.clear();
        }
        if (fields != null && !fields.isEmpty()) {
            this.skipFields.addAll(fields);
        }
    }

    public Set<String> getSkipFields() {
        return this.skipFields;
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        if (isVerbose()) {
            logger.info(this.renameFields ? "Transforming Field Names and Adding Opaque Predicates..." : "Adding Opaques...");
        }
        RefType booleanWrapperRefType = Scene.v().getRefType(BOOLEAN_CLASS_NAME);
        BodyBuilder.retrieveAllBodies();
        BodyBuilder.retrieveAllNames();
        for (SootClass applicationClass : Scene.v().getApplicationClasses()) {
            String className = applicationClass.getName();
            if (className.contains(".")) {
                className = className.substring(className.lastIndexOf(".") + 1);
            }
            this.oldToNewFieldNames.put(className, className);
            if (this.renameFields) {
                if (isVerbose()) {
                    logger.info("Class [{}]", applicationClass.getName());
                }
                for (SootField field : applicationClass.getFields()) {
                    if (Main.getWeight(phaseName, field.getSignature()) > 0) {
                        renameField(applicationClass, field);
                        field.removeTag(SignatureTag.NAME);
                    }
                }
            }
            if (!applicationClass.isInterface()) {
                String opaquePredicate = getOrAddNewName(null);
                Type type = Rand.getInt() % 2 == 0 ? BooleanType.v() : booleanWrapperRefType;
                SootField opaquePredicateField = Scene.v().makeSootField(opaquePredicate, type, 9);
                renameField(applicationClass, opaquePredicateField);
                this.opaquePredicate1ByClass.put(applicationClass, opaquePredicateField);
                applicationClass.addField(opaquePredicateField);
                setBooleanTo(applicationClass, opaquePredicateField, true);
                String opaquePredicate2 = getOrAddNewName(null);
                Type type2 = type == BooleanType.v() ? booleanWrapperRefType : BooleanType.v();
                SootField opaquePredicateField2 = Scene.v().makeSootField(opaquePredicate2, type2, 9);
                renameField(applicationClass, opaquePredicateField2);
                this.opaquePredicate2ByClass.put(applicationClass, opaquePredicateField2);
                applicationClass.addField(opaquePredicateField2);
                if (type2 == booleanWrapperRefType) {
                    setBooleanTo(applicationClass, opaquePredicateField2, false);
                }
            }
        }
        buildOpaquePairings();
        if (!this.renameFields) {
            return;
        }
        if (isVerbose()) {
            logger.info("Updating field references in bytecode");
        }
        for (SootClass applicationClass2 : Scene.v().getApplicationClasses()) {
            for (SootMethod method : applicationClass2.getMethods()) {
                if (method.isConcrete()) {
                    if (!method.hasActiveBody()) {
                        method.retrieveActiveBody();
                    }
                    Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
                    while (it.hasNext()) {
                        Unit unit = it.next();
                        for (ValueBox box : unit.getUseAndDefBoxes()) {
                            Value value = box.getValue();
                            if (value instanceof FieldRef) {
                                FieldRef fieldRef = (FieldRef) value;
                                SootFieldRef sootFieldRef = fieldRef.getFieldRef();
                                if (sootFieldRef.declaringClass().isLibraryClass()) {
                                    continue;
                                } else {
                                    String oldName = sootFieldRef.name();
                                    String fullyQualifiedName = String.valueOf(sootFieldRef.declaringClass().getName()) + '.' + oldName;
                                    if (this.skipFields.contains(fullyQualifiedName)) {
                                        continue;
                                    } else {
                                        String newName = this.oldToNewFieldNames.get(oldName);
                                        if (!this.oldToNewFieldNames.containsKey(oldName)) {
                                            newName = oldName;
                                        } else if (newName == null) {
                                            throw new IllegalStateException("Found incorrect field mapping [" + fullyQualifiedName + "] -> [null].");
                                        } else {
                                            if (newName.equals(oldName)) {
                                                logger.warn("The new name of the field \"{}\" is equal to the old one. Check if it is a mistake.", fullyQualifiedName);
                                            }
                                        }
                                        SootFieldRef sootFieldRef2 = Scene.v().makeFieldRef(sootFieldRef.declaringClass(), newName, sootFieldRef.type(), sootFieldRef.isStatic());
                                        fieldRef.setFieldRef(sootFieldRef2);
                                        try {
                                            sootFieldRef2.resolve();
                                        } catch (Exception exception) {
                                            logger.error("Cannot rename field \"" + oldName + "\" to \"" + newName + "\" due to error.", (Throwable) exception);
                                            logger.info("Fields of {}: {}", sootFieldRef2.declaringClass().getName(), sootFieldRef2.declaringClass().getFields());
                                            throw new RuntimeException(exception);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    continue;
                }
            }
        }
    }

    protected void setBooleanTo(SootClass sootClass, SootField field, boolean value) {
        Body body;
        if (!value && (field.getType() instanceof IntegerType) && Rand.getInt() % 2 > 0) {
            return;
        }
        RefType booleanWrapperRefType = Scene.v().getRefType(BOOLEAN_CLASS_NAME);
        boolean addStaticInitializer = !sootClass.declaresMethodByName("<clinit>");
        if (addStaticInitializer) {
            SootMethod staticInitializerMethod = Scene.v().makeSootMethod("<clinit>", Collections.emptyList(), VoidType.v(), 8);
            sootClass.addMethod(staticInitializerMethod);
            body = Jimple.v().newBody(staticInitializerMethod);
            staticInitializerMethod.setActiveBody(body);
        } else {
            body = sootClass.getMethodByName("<clinit>").getActiveBody();
        }
        PatchingChain<Unit> units = body.getUnits();
        if (field.getType() instanceof IntegerType) {
            units.addFirst((PatchingChain<Unit>) Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(field.makeRef()), IntConstant.v(value ? 1 : 0)));
        } else {
            Local bool = Jimple.v().newLocal("boolLcl", booleanWrapperRefType);
            body.getLocals().add(bool);
            SootMethod booleanWrapperConstructor = booleanWrapperRefType.getSootClass().getMethod("void <init>(boolean)");
            units.addFirst((PatchingChain<Unit>) Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(field.makeRef()), bool));
            units.addFirst((PatchingChain<Unit>) Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(bool, booleanWrapperConstructor.makeRef(), IntConstant.v(value ? 1 : 0))));
            units.addFirst((PatchingChain<Unit>) Jimple.v().newAssignStmt(bool, Jimple.v().newNewExpr(booleanWrapperRefType)));
        }
        if (addStaticInitializer) {
            units.addLast((PatchingChain<Unit>) Jimple.v().newReturnVoidStmt());
        }
    }

    protected void renameField(SootClass sootClass, SootField field) {
        String fullyQualifiedName = String.valueOf(sootClass.getName()) + "." + field.getName();
        String newName = getOrAddNewName(field.getName());
        if (isVerbose()) {
            logger.info("Changing {} to {}", fullyQualifiedName, newName);
        }
        field.setName(newName);
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable, java.lang.Object] */
    public String getOrAddNewName(String originalName) {
        int size = 5;
        int tries = 0;
        String newName = "";
        if (originalName != null) {
            newName = originalName;
            if (this.oldToNewFieldNames.containsKey(newName)) {
                return this.oldToNewFieldNames.get(originalName);
            }
        }
        synchronized (this.fieldNamesLock) {
            if (this.oldToNewFieldNames.containsKey(newName)) {
                return this.oldToNewFieldNames.get(newName);
            }
            while (newName.length() < 21845) {
                newName = this.nameGenerator.generateName(size);
                String key = originalName == null ? newName : originalName;
                if (!this.oldToNewFieldNames.containsKey(key) && !this.oldToNewFieldNames.containsValue(newName) && !BodyBuilder.nameList.contains(newName)) {
                    this.oldToNewFieldNames.put(key, newName);
                    BodyBuilder.nameList.add(newName);
                    return newName;
                }
                int i = tries;
                tries++;
                if (i > size) {
                    size++;
                    tries = 0;
                }
            }
            throw new IllegalStateException("Cannot generate unique package name part: too long for JVM.");
        }
    }

    public SootField[] getRandomOpaques() {
        int[] iArr;
        if (handedOutPairs == null) {
            handedOutPairs = new int[this.opaquePairs.length];
        }
        int lowValue = 99999;
        List<Integer> available = new ArrayList<>();
        for (int element : handedOutPairs) {
            if (lowValue > element) {
                lowValue = element;
            }
        }
        for (int i = 0; i < handedOutPairs.length; i++) {
            if (handedOutPairs[i] == lowValue) {
                available.add(Integer.valueOf(i));
            }
        }
        int index = available.get(Rand.getInt(available.size())).intValue();
        int[] iArr2 = handedOutPairs;
        iArr2[index] = iArr2[index] + 1;
        return this.opaquePairs[index];
    }

    public int getRandomOpaquesForRunnable() {
        int[] iArr;
        if (handedOutRunPairs == null) {
            handedOutRunPairs = new int[this.opaquePairs.length];
        }
        int lowValue = 99999;
        List<Integer> available = new ArrayList<>();
        for (int element : handedOutRunPairs) {
            if (lowValue > element) {
                lowValue = element;
            }
        }
        if (lowValue > 2) {
            return -1;
        }
        for (int i = 0; i < handedOutRunPairs.length; i++) {
            if (handedOutRunPairs[i] == lowValue) {
                available.add(Integer.valueOf(i));
            }
        }
        return available.get(Rand.getInt(available.size())).intValue();
    }

    public static void updateOpaqueRunnableCount(int i) {
        int[] iArr = handedOutRunPairs;
        iArr[i] = iArr[i] + 1;
    }

    private void buildOpaquePairings() {
        SootField[] fields1 = (SootField[]) this.opaquePredicate1ByClass.values().toArray(EMPTY_ARRAY);
        SootField[] fields2 = (SootField[]) this.opaquePredicate2ByClass.values().toArray(EMPTY_ARRAY);
        int length = fields1.length;
        for (int i = 0; i < fields1.length * 2 && fields1.length > 1; i++) {
            swap(fields1);
            swap(fields2);
        }
        this.opaquePairs = new SootField[length][2];
        for (int i2 = 0; i2 < length; i2++) {
            SootField[] sootFieldArr = new SootField[2];
            sootFieldArr[0] = fields1[i2];
            sootFieldArr[1] = fields2[i2];
            this.opaquePairs[i2] = sootFieldArr;
        }
    }

    private static <T> void swap(T[] tArr) {
        int a = Rand.getInt(tArr.length);
        int i = Rand.getInt(tArr.length);
        while (true) {
            int b = i;
            if (a == b) {
                i = Rand.getInt(tArr.length);
            } else {
                T t = tArr[a];
                tArr[a] = tArr[b];
                tArr[b] = t;
                return;
            }
        }
    }
}
