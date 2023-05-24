package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.FastHierarchy;
import soot.G;
import soot.JavaBasicTypes;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
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
import soot.jbco.util.HierarchyUtils;
import soot.jbco.util.Rand;
import soot.jimple.InvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/jbco/jimpleTransformations/MethodRenamer.class */
public class MethodRenamer extends SceneTransformer implements IJbcoTransform {
    public static final String name = "wjtp.jbco_mr";
    private final Map<SootClass, Map<String, String>> classToRenamingMap = new HashMap();
    private final NameGenerator nameGenerator;
    private static final Logger logger = LoggerFactory.getLogger(MethodRenamer.class);
    private static final String MAIN_METHOD_SUB_SIGNATURE = SootMethod.getSubSignature("main", Collections.singletonList(ArrayType.v(RefType.v("java.lang.String"), 1)), VoidType.v());
    private static final Function<SootClass, Map<String, String>> RENAMING_MAP_CREATOR = key -> {
        return new HashMap();
    };

    public MethodRenamer(Singletons.Global global) {
        if (global == null) {
            throw new NullPointerException("Cannot instantiate MethodRenamer with null Singletons.Global");
        }
        this.nameGenerator = new JunkNameGenerator();
    }

    public static MethodRenamer v() {
        return G.v().soot_jbco_jimpleTransformations_MethodRenamer();
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
        Integer newNames = (Integer) this.classToRenamingMap.values().stream().map((v0) -> {
            return v0.values();
        }).flatMap((v0) -> {
            return v0.stream();
        }).collect(Collectors.collectingAndThen(Collectors.toSet(), (v0) -> {
            return v0.size();
        }));
        logger.info("{} methods were renamed.", newNames);
    }

    public Map<String, String> getRenamingMap(String className) {
        return (Map) this.classToRenamingMap.entrySet().stream().filter(entry -> {
            return ((SootClass) entry.getKey()).getName().equals(className);
        }).flatMap(entry2 -> {
            return ((Map) entry2.getValue()).entrySet().stream();
        }).collect(Collectors.toMap((v0) -> {
            return v0.getKey();
        }, (v0) -> {
            return v0.getValue();
        }));
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        Body body;
        if (isVerbose()) {
            logger.info("Transforming method names...");
        }
        BodyBuilder.retrieveAllBodies();
        BodyBuilder.retrieveAllNames();
        Scene.v().releaseActiveHierarchy();
        for (SootClass applicationClass : Scene.v().getApplicationClasses()) {
            List<String> fieldNames = (List) applicationClass.getFields().stream().map((v0) -> {
                return v0.getName();
            }).collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
            List<String> leftFieldNames = new ArrayList<>(fieldNames);
            List<SootMethod> methods = new ArrayList<>(applicationClass.getMethods());
            for (SootMethod method : methods) {
                if (isRenamingAllowed(method)) {
                    Set<SootClass> declaringClasses = getDeclaringClasses(applicationClass, method);
                    if (declaringClasses.isEmpty()) {
                        throw new IllegalStateException("Cannot find classes that declare " + method.getSignature() + ".");
                    }
                    Optional<SootClass> libraryClass = declaringClasses.stream().filter((v0) -> {
                        return v0.isLibraryClass();
                    }).findAny();
                    if (libraryClass.isPresent()) {
                        if (isVerbose()) {
                            logger.info("Skipping renaming {} method as it overrides library one from {}.", method.getSignature(), libraryClass.get().getName());
                        }
                    } else {
                        Set<SootClass> union = uniteWithApplicationParents(applicationClass, declaringClasses);
                        String newName = getNewName(union, method.getName());
                        if (newName == null) {
                            if (leftFieldNames.isEmpty()) {
                                newName = getNewName();
                            } else {
                                int randomIndex = Rand.getInt(leftFieldNames.size());
                                String randomFieldName = leftFieldNames.remove(randomIndex);
                                if (isNotUnique(randomFieldName) || fieldNames.contains(randomFieldName)) {
                                    newName = getNewName();
                                } else {
                                    newName = randomFieldName;
                                }
                            }
                        }
                        for (SootClass declaringClass : union) {
                            this.classToRenamingMap.computeIfAbsent(declaringClass, RENAMING_MAP_CREATOR).put(method.getName(), newName);
                        }
                    }
                }
            }
        }
        for (SootClass applicationClass2 : Scene.v().getApplicationClasses()) {
            List<SootMethod> methods2 = new ArrayList<>(applicationClass2.getMethods());
            for (SootMethod method2 : methods2) {
                String newName2 = getNewName(Collections.singleton(applicationClass2), method2.getName());
                if (newName2 != null) {
                    if (isVerbose()) {
                        logger.info("Method \"{}\" is being renamed to \"{}\".", method2.getSignature(), newName2);
                    }
                    method2.setName(newName2);
                }
            }
        }
        for (SootClass applicationClass3 : Scene.v().getApplicationClasses()) {
            List<SootMethod> methods3 = new ArrayList<>(applicationClass3.getMethods());
            for (SootMethod method3 : methods3) {
                if (method3.isConcrete() && !method3.getDeclaringClass().isLibraryClass() && (body = getActiveBodySafely(method3)) != null) {
                    Iterator<Unit> it = body.getUnits().iterator();
                    while (it.hasNext()) {
                        Unit unit = it.next();
                        for (ValueBox valueBox : unit.getUseBoxes()) {
                            Value v = valueBox.getValue();
                            if (v instanceof InvokeExpr) {
                                InvokeExpr invokeExpr = (InvokeExpr) v;
                                SootMethodRef methodRef = invokeExpr.getMethodRef();
                                Set<SootClass> parents = getParents(methodRef.getDeclaringClass());
                                Optional<SootClass> declaringLibraryClass = findDeclaringLibraryClass(parents, methodRef);
                                if (declaringLibraryClass.isPresent()) {
                                    if (isVerbose()) {
                                        logger.info("Skipping replacing method call \"{}\" in \"{}\" as it is overrides one  from library {}.", methodRef.getSignature(), method3.getSignature(), declaringLibraryClass.get().getName());
                                    }
                                } else {
                                    String newName3 = getNewName(parents, methodRef.getName());
                                    if (newName3 != null) {
                                        SootMethodRef newMethodRef = Scene.v().makeMethodRef(methodRef.getDeclaringClass(), newName3, methodRef.getParameterTypes(), methodRef.getReturnType(), methodRef.isStatic());
                                        invokeExpr.setMethodRef(newMethodRef);
                                        if (isVerbose()) {
                                            logger.info("Method call \"{}\" is being replaced with \"{}\" in {}.", methodRef.getSignature(), newMethodRef.getSignature(), method3.getSignature());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Scene.v().releaseActiveHierarchy();
        Scene.v().setFastHierarchy(new FastHierarchy());
        if (isVerbose()) {
            logger.info("Transforming method names is completed.");
        }
    }

    public String getNewName() {
        int size = 5;
        int tries = 0;
        String generateName = this.nameGenerator.generateName(5);
        while (true) {
            String newName = generateName;
            if (isNotUnique(newName) || BodyBuilder.nameList.contains(newName)) {
                int i = tries;
                tries++;
                if (i > size) {
                    size++;
                    tries = 0;
                }
                generateName = this.nameGenerator.generateName(size);
            } else {
                BodyBuilder.nameList.add(newName);
                return newName;
            }
        }
    }

    private boolean isRenamingAllowed(SootMethod method) {
        if (Main.getWeight(name, method.getSignature()) == 0) {
            return false;
        }
        String subSignature = method.getSubSignature();
        if (MAIN_METHOD_SUB_SIGNATURE.equals(subSignature) && method.isPublic() && method.isStatic()) {
            if (isVerbose()) {
                logger.info("Skipping renaming \"{}\" method as it is main one.", subSignature);
                return false;
            }
            return false;
        } else if (method.getName().equals("<init>") || method.getName().equals("<clinit>")) {
            if (isVerbose()) {
                logger.info("Skipping renaming \"{}\" method as it is constructor or static initializer.", subSignature);
                return false;
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean isNotUnique(String methodName) {
        Stream flatMap = this.classToRenamingMap.values().stream().map((v0) -> {
            return v0.values();
        }).flatMap((v0) -> {
            return v0.stream();
        });
        methodName.getClass();
        return flatMap.anyMatch((v1) -> {
            return r1.equals(v1);
        });
    }

    private Set<SootClass> uniteWithApplicationParents(SootClass applicationClass, Collection<SootClass> classes) {
        Set<SootClass> parents = getApplicationParents(applicationClass);
        Set<SootClass> result = new HashSet<>(parents.size() + classes.size());
        result.addAll(parents);
        result.addAll(classes);
        return result;
    }

    private Optional<SootClass> findDeclaringLibraryClass(Collection<SootClass> classes, SootMethodRef methodRef) {
        return classes.stream().filter((v0) -> {
            return v0.isLibraryClass();
        }).filter(sootClass -> {
            return isDeclared(methodRef, r6.getName(), r6.getParameterTypes());
        }).findAny();
    }

    private Set<SootClass> getDeclaringClasses(SootClass applicationClass, SootMethod method) {
        return (Set) getTree(applicationClass).stream().filter(sootClass -> {
            return isDeclared(method, r6.getName(), r6.getParameterTypes());
        }).collect(Collectors.toSet());
    }

    private Set<SootClass> getTree(SootClass applicationClass) {
        int count;
        Set<SootClass> children = getChildrenOfIncluding(getParentsOfIncluding(applicationClass));
        do {
            count = children.size();
            children.addAll(getChildrenOfIncluding(getParentsOfIncluding(children)));
        } while (count < children.size());
        return children;
    }

    private Set<SootClass> getParents(SootClass applicationClass) {
        int count;
        Set<SootClass> parents = new HashSet<>(getParentsOfIncluding(applicationClass));
        do {
            count = parents.size();
            parents.addAll(getParentsOfIncluding(parents));
        } while (count < parents.size());
        return parents;
    }

    private Set<SootClass> getApplicationParents(SootClass applicationClass) {
        return (Set) getParents(applicationClass).stream().filter(parent -> {
            return !parent.isLibraryClass();
        }).collect(Collectors.toSet());
    }

    private List<SootClass> getParentsOfIncluding(SootClass applicationClass) {
        List<SootClass> result = HierarchyUtils.getAllInterfacesOf(applicationClass);
        result.addAll(applicationClass.getInterfaces());
        if (!applicationClass.isInterface() && applicationClass.hasSuperclass()) {
            result.add(applicationClass.getSuperclass());
        }
        result.addAll(applicationClass.isInterface() ? Scene.v().getActiveHierarchy().getSuperinterfacesOfIncluding(applicationClass) : Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(applicationClass));
        return result;
    }

    private Set<SootClass> getChildrenOfIncluding(Collection<SootClass> classes) {
        return (Set) Stream.concat(classes.stream().filter(c -> {
            return !c.getName().equals(JavaBasicTypes.JAVA_LANG_OBJECT);
        }).map(c2 -> {
            return c2.isInterface() ? Scene.v().getActiveHierarchy().getImplementersOf(c2) : Scene.v().getActiveHierarchy().getSubclassesOf(c2);
        }).flatMap((v0) -> {
            return v0.stream();
        }), classes.stream()).collect(Collectors.toSet());
    }

    private Set<SootClass> getParentsOfIncluding(Collection<SootClass> classes) {
        Set<SootClass> parents = new HashSet<>(classes);
        for (SootClass clazz : classes) {
            parents.addAll(clazz.getInterfaces());
            if (!clazz.isInterface() && clazz.hasSuperclass()) {
                parents.add(clazz.getSuperclass());
            }
            parents.addAll(clazz.isInterface() ? Scene.v().getActiveHierarchy().getSuperinterfacesOfIncluding(clazz) : Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(clazz));
        }
        return parents;
    }

    private String getNewName(Collection<SootClass> classes, String name2) {
        Set<String> names = (Set) this.classToRenamingMap.entrySet().stream().filter(entry -> {
            return classes.contains(entry.getKey());
        }).map((v0) -> {
            return v0.getValue();
        }).map((v0) -> {
            return v0.entrySet();
        }).flatMap((v0) -> {
            return v0.stream();
        }).filter(entry2 -> {
            return ((String) entry2.getKey()).equals(name2);
        }).map((v0) -> {
            return v0.getValue();
        }).collect(Collectors.toSet());
        if (names.size() > 1) {
            logger.warn("Found {} names for method \"{}\": {}.", Integer.valueOf(names.size()), name2, String.join(", ", names));
        }
        if (names.isEmpty()) {
            return null;
        }
        return names.iterator().next();
    }

    private boolean isDeclared(SootClass sootClass, String methodName, List<Type> parameterTypes) {
        for (SootMethod declared : sootClass.getMethods()) {
            if (declared.getName().equals(methodName) && declared.getParameterCount() == parameterTypes.size()) {
                return true;
            }
        }
        return false;
    }

    private static Body getActiveBodySafely(SootMethod method) {
        try {
            return method.getActiveBody();
        } catch (Exception exception) {
            logger.warn("Getting Body from SootMethod {} caused exception that was suppressed.", (Throwable) exception);
            return null;
        }
    }
}
