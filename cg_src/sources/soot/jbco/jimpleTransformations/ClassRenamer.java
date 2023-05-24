package soot.jbco.jimpleTransformations;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.FastHierarchy;
import soot.G;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.name.JunkNameGenerator;
import soot.jbco.name.NameGenerator;
import soot.jbco.util.BodyBuilder;
import soot.jimple.CastExpr;
import soot.jimple.ClassConstant;
import soot.jimple.Expr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.Ref;
import soot.tagkit.SourceFileTag;
/* loaded from: gencallgraphv3.jar:soot/jbco/jimpleTransformations/ClassRenamer.class */
public class ClassRenamer extends SceneTransformer implements IJbcoTransform {
    private static final Logger logger = LoggerFactory.getLogger(ClassRenamer.class);
    public static final String name = "wjtp.jbco_cr";
    private final NameGenerator nameGenerator;
    private boolean removePackages = false;
    private boolean renamePackages = false;
    private final Map<String, String> oldToNewPackageNames = new HashMap();
    private final Map<String, String> oldToNewClassNames = new HashMap();
    private final Map<String, SootClass> newNameToClass = new HashMap();
    private final Object classNamesMapLock = new Object();
    private final Object packageNamesMapLock = new Object();

    public ClassRenamer(Singletons.Global global) {
        if (global == null) {
            throw new NullPointerException("Cannot instantiate ClassRenamer with null Singletons.Global");
        }
        this.nameGenerator = new JunkNameGenerator();
    }

    public static ClassRenamer v() {
        return G.v().soot_jbco_jimpleTransformations_ClassRenamer();
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
        StringBuilder stringBuilder = new StringBuilder("ClassName mapping:").append(System.lineSeparator());
        this.oldToNewClassNames.forEach(oldName, newName -> {
            stringBuilder.append(oldName).append(" -> ").append(newName).append(System.lineSeparator());
        });
        logger.info(stringBuilder.toString());
    }

    public boolean isRemovePackages() {
        return this.removePackages;
    }

    public void setRemovePackages(boolean removePackages) {
        this.removePackages = removePackages;
    }

    public boolean isRenamePackages() {
        return this.renamePackages;
    }

    public void setRenamePackages(boolean renamePackages) {
        this.renamePackages = renamePackages;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Throwable, java.lang.Object] */
    public void addClassNameMapping(String classNameSource, String classNameTarget) {
        synchronized (this.classNamesMapLock) {
            if (!this.oldToNewClassNames.containsKey(classNameSource) && !this.oldToNewClassNames.containsValue(classNameTarget) && !BodyBuilder.nameList.contains(classNameTarget)) {
                this.oldToNewClassNames.put(classNameSource, classNameTarget);
                BodyBuilder.nameList.add(classNameTarget);
                return;
            }
            throw new IllegalStateException("Cannot generate unique name: too long for JVM.");
        }
    }

    public Map<String, String> getClassNameMapping(BiPredicate<String, String> predicate) {
        if (predicate == null) {
            return new HashMap(this.oldToNewClassNames);
        }
        return (Map) this.oldToNewClassNames.entrySet().stream().filter(entry -> {
            return predicate.test((String) entry.getKey(), (String) entry.getValue());
        }).collect(Collectors.toMap((v0) -> {
            return v0.getKey();
        }, (v0) -> {
            return v0.getValue();
        }));
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        if (isVerbose()) {
            logger.info("Transforming Class Names...");
        }
        BodyBuilder.retrieveAllBodies();
        BodyBuilder.retrieveAllNames();
        SootClass mainClass = getMainClassSafely();
        for (SootClass applicationClass : Scene.v().getApplicationClasses()) {
            String fullyQualifiedName = applicationClass.getName();
            if (!applicationClass.equals(mainClass) && !this.oldToNewClassNames.containsValue(fullyQualifiedName) && Main.getWeight(phaseName, fullyQualifiedName) != 0) {
                String newClassName = getOrAddNewName(getPackageName(fullyQualifiedName), getClassName(fullyQualifiedName));
                applicationClass.setName(newClassName);
                RefType crt = RefType.v(newClassName);
                crt.setSootClass(applicationClass);
                applicationClass.setRefType(crt);
                applicationClass.setResolvingLevel(3);
                SourceFileTag sourceFileTag = (SourceFileTag) applicationClass.getTag(SourceFileTag.NAME);
                if (sourceFileTag == null) {
                    logger.info("Adding SourceFileTag for class {}", fullyQualifiedName);
                    sourceFileTag = new SourceFileTag();
                    applicationClass.addTag(sourceFileTag);
                }
                sourceFileTag.setSourceFile(newClassName);
                this.newNameToClass.put(newClassName, applicationClass);
                if (isVerbose()) {
                    logger.info("Renaming {} to {}", fullyQualifiedName, newClassName);
                }
            }
        }
        Scene.v().releaseActiveHierarchy();
        Scene.v().setFastHierarchy(new FastHierarchy());
        if (isVerbose()) {
            logger.info("Updating bytecode class references");
        }
        for (SootClass sootClass : Scene.v().getApplicationClasses()) {
            for (SootMethod sootMethod : sootClass.getMethods()) {
                if (sootMethod.isConcrete()) {
                    if (isVerbose()) {
                        logger.info(sootMethod.getSignature());
                    }
                    try {
                        Body aBody = sootMethod.getActiveBody();
                        Iterator<Unit> it = aBody.getUnits().iterator();
                        while (it.hasNext()) {
                            Unit u = it.next();
                            for (ValueBox vb : u.getUseAndDefBoxes()) {
                                Value v = vb.getValue();
                                if (v instanceof ClassConstant) {
                                    ClassConstant constant = (ClassConstant) v;
                                    RefType type = (RefType) constant.toSootType();
                                    RefType updatedType = type.getSootClass().getType();
                                    vb.setValue(ClassConstant.fromType(updatedType));
                                } else if (v instanceof Expr) {
                                    if (v instanceof CastExpr) {
                                        CastExpr castExpr = (CastExpr) v;
                                        updateType(castExpr.getCastType());
                                    } else if (v instanceof InstanceOfExpr) {
                                        InstanceOfExpr instanceOfExpr = (InstanceOfExpr) v;
                                        updateType(instanceOfExpr.getCheckType());
                                    }
                                } else if (v instanceof Ref) {
                                    updateType(v.getType());
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        Scene.v().releaseActiveHierarchy();
        Scene.v().setFastHierarchy(new FastHierarchy());
    }

    private void updateType(Type type) {
        if (type instanceof RefType) {
            RefType rt = (RefType) type;
            if (!rt.getSootClass().isLibraryClass() && this.oldToNewClassNames.containsKey(rt.getClassName())) {
                rt.setSootClass(this.newNameToClass.get(this.oldToNewClassNames.get(rt.getClassName())));
                rt.setClassName(this.oldToNewClassNames.get(rt.getClassName()));
            }
        } else if (type instanceof ArrayType) {
            ArrayType at = (ArrayType) type;
            if (at.baseType instanceof RefType) {
                RefType rt2 = (RefType) at.baseType;
                if (!rt2.getSootClass().isLibraryClass() && this.oldToNewClassNames.containsKey(rt2.getClassName())) {
                    rt2.setSootClass(this.newNameToClass.get(this.oldToNewClassNames.get(rt2.getClassName())));
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable, java.lang.Object] */
    public String getOrAddNewName(String packageName, String className) {
        String oldFqn;
        int size = 5;
        int tries = 0;
        String newFqn = "";
        if (className != null) {
            newFqn = this.removePackages ? className : packageName == null ? className : String.valueOf(packageName) + '.' + className;
            if (this.oldToNewClassNames.containsKey(newFqn)) {
                return this.oldToNewClassNames.get(newFqn);
            }
        }
        synchronized (this.classNamesMapLock) {
            if (this.oldToNewClassNames.containsKey(newFqn)) {
                return this.oldToNewClassNames.get(newFqn);
            }
            while (newFqn.length() < 21845) {
                String name2 = this.nameGenerator.generateName(size);
                newFqn = name2;
                if (!this.removePackages) {
                    String newPackage = this.renamePackages ? getOrAddNewPackageName(packageName) : packageName;
                    newFqn = newPackage == null ? name2 : String.valueOf(newPackage) + '.' + name2;
                }
                if (className == null) {
                    oldFqn = newFqn;
                } else {
                    oldFqn = String.valueOf(packageName == null ? "" : String.valueOf(packageName) + '.') + className;
                }
                if (!this.oldToNewClassNames.containsKey(oldFqn) && !this.oldToNewClassNames.containsValue(newFqn) && !BodyBuilder.nameList.contains(newFqn)) {
                    addClassNameMapping(oldFqn, newFqn);
                    return newFqn;
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

    public static String getPackageName(String fullyQualifiedClassName) {
        int idx;
        if (fullyQualifiedClassName == null || fullyQualifiedClassName.isEmpty() || (idx = fullyQualifiedClassName.lastIndexOf(46)) < 1) {
            return null;
        }
        return fullyQualifiedClassName.substring(0, idx);
    }

    public static String getClassName(String fullyQualifiedClassName) {
        if (fullyQualifiedClassName == null || fullyQualifiedClassName.isEmpty()) {
            return null;
        }
        int idx = fullyQualifiedClassName.lastIndexOf(46);
        if (idx < 0) {
            return fullyQualifiedClassName;
        }
        if (idx < fullyQualifiedClassName.length() - 1) {
            return fullyQualifiedClassName.substring(idx + 1);
        }
        return null;
    }

    private static SootClass getMainClassSafely() {
        if (Scene.v().hasMainClass()) {
            return Scene.v().getMainClass();
        }
        return null;
    }

    private String getOrAddNewPackageName(String packageName) {
        if (packageName == null || packageName.isEmpty()) {
            return getNewPackageNamePart("");
        }
        String[] packageNameParts = packageName.split("\\.");
        StringBuilder newPackageName = new StringBuilder((int) (5 * (packageNameParts.length + 1) * 1.5d));
        for (int i = 0; i < packageNameParts.length; i++) {
            newPackageName.append(getNewPackageNamePart(packageNameParts[i]));
            if (i < packageNameParts.length - 1) {
                newPackageName.append('.');
            }
        }
        return newPackageName.toString();
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [java.lang.Throwable, java.lang.Object] */
    private String getNewPackageNamePart(String oldPackageNamePart) {
        if (oldPackageNamePart != null && this.oldToNewPackageNames.containsKey(oldPackageNamePart)) {
            return this.oldToNewPackageNames.get(oldPackageNamePart);
        }
        int size = 5;
        int tries = 0;
        String newPackageNamePart = "";
        while (newPackageNamePart.length() < 21845) {
            synchronized (this.packageNamesMapLock) {
                if (this.oldToNewPackageNames.containsValue(newPackageNamePart)) {
                    return this.oldToNewPackageNames.get(newPackageNamePart);
                }
                newPackageNamePart = this.nameGenerator.generateName(size);
                if (!this.oldToNewPackageNames.containsValue(newPackageNamePart)) {
                    String key = oldPackageNamePart == null ? newPackageNamePart : oldPackageNamePart;
                    this.oldToNewPackageNames.put(key, newPackageNamePart);
                    return newPackageNamePart;
                }
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
