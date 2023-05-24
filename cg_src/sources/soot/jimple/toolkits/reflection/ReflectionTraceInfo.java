package soot.jimple.toolkits.reflection;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.tagkit.Host;
import soot.tagkit.LineNumberTag;
import soot.tagkit.SourceLnPosTag;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/reflection/ReflectionTraceInfo.class */
public class ReflectionTraceInfo {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTraceInfo.class);
    protected final Map<SootMethod, Set<String>> classForNameReceivers = new LinkedHashMap();
    protected final Map<SootMethod, Set<String>> classNewInstanceReceivers = new LinkedHashMap();
    protected final Map<SootMethod, Set<String>> constructorNewInstanceReceivers = new LinkedHashMap();
    protected final Map<SootMethod, Set<String>> methodInvokeReceivers = new LinkedHashMap();
    protected final Map<SootMethod, Set<String>> fieldSetReceivers = new LinkedHashMap();
    protected final Map<SootMethod, Set<String>> fieldGetReceivers = new LinkedHashMap();

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/reflection/ReflectionTraceInfo$Kind.class */
    public enum Kind {
        ClassForName,
        ClassNewInstance,
        ConstructorNewInstance,
        MethodInvoke,
        FieldSet,
        FieldGet;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Kind[] valuesCustom() {
            Kind[] valuesCustom = values();
            int length = valuesCustom.length;
            Kind[] kindArr = new Kind[length];
            System.arraycopy(valuesCustom, 0, kindArr, 0, length);
            return kindArr;
        }
    }

    /* JADX WARN: Finally extract failed */
    public ReflectionTraceInfo(String logFile) {
        if (logFile == null) {
            throw new InternalError("Trace based refection model enabled but no trace file given!?");
        }
        Throwable th = null;
        try {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
                try {
                    Scene sc = Scene.v();
                    Set<String> ignoredKinds = new HashSet<>();
                    while (true) {
                        String line = reader.readLine();
                        if (line != null) {
                            if (!line.isEmpty()) {
                                String[] portions = line.split(";", -1);
                                String kind = portions[0];
                                String target = portions[1];
                                String source = portions[2];
                                int lineNumber = portions[3].length() == 0 ? -1 : Integer.parseInt(portions[3]);
                                for (SootMethod sourceMethod : inferSource(source, lineNumber)) {
                                    switch (kind.hashCode()) {
                                        case -1805075963:
                                            if (kind.equals("Method.invoke")) {
                                                if (!sc.containsMethod(target)) {
                                                    throw new RuntimeException("Unknown method for signature: " + target);
                                                }
                                                Set<String> receiverNames = this.methodInvokeReceivers.get(sourceMethod);
                                                if (receiverNames == null) {
                                                    Map<SootMethod, Set<String>> map = this.methodInvokeReceivers;
                                                    Set<String> linkedHashSet = new LinkedHashSet<>();
                                                    receiverNames = linkedHashSet;
                                                    map.put(sourceMethod, linkedHashSet);
                                                }
                                                receiverNames.add(target);
                                                break;
                                            } else {
                                                ignoredKinds.add(kind);
                                                break;
                                            }
                                        case -1770300376:
                                            if (kind.equals("Field.get*")) {
                                                if (!sc.containsField(target)) {
                                                    throw new RuntimeException("Unknown method for signature: " + target);
                                                }
                                                Set<String> receiverNames2 = this.fieldGetReceivers.get(sourceMethod);
                                                if (receiverNames2 == null) {
                                                    Map<SootMethod, Set<String>> map2 = this.fieldGetReceivers;
                                                    Set<String> linkedHashSet2 = new LinkedHashSet<>();
                                                    receiverNames2 = linkedHashSet2;
                                                    map2.put(sourceMethod, linkedHashSet2);
                                                }
                                                receiverNames2.add(target);
                                                break;
                                            } else {
                                                ignoredKinds.add(kind);
                                                break;
                                            }
                                        case -1769942884:
                                            if (kind.equals("Field.set*")) {
                                                if (!sc.containsField(target)) {
                                                    throw new RuntimeException("Unknown method for signature: " + target);
                                                }
                                                Set<String> receiverNames3 = this.fieldSetReceivers.get(sourceMethod);
                                                if (receiverNames3 == null) {
                                                    Map<SootMethod, Set<String>> map3 = this.fieldSetReceivers;
                                                    Set<String> linkedHashSet3 = new LinkedHashSet<>();
                                                    receiverNames3 = linkedHashSet3;
                                                    map3.put(sourceMethod, linkedHashSet3);
                                                }
                                                receiverNames3.add(target);
                                                break;
                                            } else {
                                                ignoredKinds.add(kind);
                                                break;
                                            }
                                        case -1267449279:
                                            if (kind.equals("Constructor.newInstance")) {
                                                if (!sc.containsMethod(target)) {
                                                    throw new RuntimeException("Unknown method for signature: " + target);
                                                }
                                                Set<String> receiverNames4 = this.constructorNewInstanceReceivers.get(sourceMethod);
                                                if (receiverNames4 == null) {
                                                    Map<SootMethod, Set<String>> map4 = this.constructorNewInstanceReceivers;
                                                    Set<String> linkedHashSet4 = new LinkedHashSet<>();
                                                    receiverNames4 = linkedHashSet4;
                                                    map4.put(sourceMethod, linkedHashSet4);
                                                }
                                                receiverNames4.add(target);
                                                break;
                                            } else {
                                                ignoredKinds.add(kind);
                                                break;
                                            }
                                        case -1148571969:
                                            if (kind.equals("Class.newInstance")) {
                                                Set<String> receiverNames5 = this.classNewInstanceReceivers.get(sourceMethod);
                                                if (receiverNames5 == null) {
                                                    Map<SootMethod, Set<String>> map5 = this.classNewInstanceReceivers;
                                                    Set<String> linkedHashSet5 = new LinkedHashSet<>();
                                                    receiverNames5 = linkedHashSet5;
                                                    map5.put(sourceMethod, linkedHashSet5);
                                                }
                                                receiverNames5.add(target);
                                                break;
                                            } else {
                                                ignoredKinds.add(kind);
                                                break;
                                            }
                                        case -274276258:
                                            if (kind.equals("Class.forName")) {
                                                Set<String> receiverNames6 = this.classForNameReceivers.get(sourceMethod);
                                                if (receiverNames6 == null) {
                                                    Map<SootMethod, Set<String>> map6 = this.classForNameReceivers;
                                                    Set<String> linkedHashSet6 = new LinkedHashSet<>();
                                                    receiverNames6 = linkedHashSet6;
                                                    map6.put(sourceMethod, linkedHashSet6);
                                                }
                                                receiverNames6.add(target);
                                                break;
                                            } else {
                                                ignoredKinds.add(kind);
                                                break;
                                            }
                                        default:
                                            ignoredKinds.add(kind);
                                            break;
                                    }
                                }
                                continue;
                            }
                        } else {
                            if (!ignoredKinds.isEmpty()) {
                                logger.debug("Encountered reflective calls entries of the following kinds that\ncannot currently be handled:");
                                for (String kind2 : ignoredKinds) {
                                    logger.debug(kind2);
                                }
                            }
                            if (reader != null) {
                                reader.close();
                                return;
                            }
                            return;
                        }
                    }
                } catch (Throwable th2) {
                    if (reader != null) {
                        reader.close();
                    }
                    throw th2;
                }
            } catch (Throwable th3) {
                if (0 == 0) {
                    th = th3;
                } else if (null != th3) {
                    th.addSuppressed(th3);
                }
                throw th;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Trace file not found.", e);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    private Set<SootMethod> inferSource(String source, int lineNumber) {
        int dotIndex = source.lastIndexOf(46);
        String className = source.substring(0, dotIndex);
        String methodName = source.substring(dotIndex + 1);
        Scene scene = Scene.v();
        if (!scene.containsClass(className)) {
            scene.addBasicClass(className, 3);
            scene.loadBasicClasses();
            if (!scene.containsClass(className)) {
                throw new RuntimeException("Trace file refers to unknown class: " + className);
            }
        }
        Set<SootMethod> methodsWithRightName = new LinkedHashSet<>();
        for (SootMethod m : scene.getSootClass(className).getMethods()) {
            if (m.isConcrete() && m.getName().equals(methodName)) {
                methodsWithRightName.add(m);
            }
        }
        if (methodsWithRightName.isEmpty()) {
            throw new RuntimeException("Trace file refers to unknown method with name " + methodName + " in Class " + className);
        }
        if (methodsWithRightName.size() == 1) {
            return Collections.singleton(methodsWithRightName.iterator().next());
        }
        for (SootMethod sootMethod : methodsWithRightName) {
            if (coversLineNumber(lineNumber, sootMethod)) {
                return Collections.singleton(sootMethod);
            }
            if (sootMethod.isConcrete()) {
                if (!sootMethod.hasActiveBody()) {
                    sootMethod.retrieveActiveBody();
                }
                Body body = sootMethod.getActiveBody();
                if (coversLineNumber(lineNumber, body)) {
                    return Collections.singleton(sootMethod);
                }
                Iterator<Unit> it = body.getUnits().iterator();
                while (it.hasNext()) {
                    Unit u = it.next();
                    if (coversLineNumber(lineNumber, u)) {
                        return Collections.singleton(sootMethod);
                    }
                }
                continue;
            }
        }
        return methodsWithRightName;
    }

    private boolean coversLineNumber(int lineNumber, Host host) {
        SourceLnPosTag tag = (SourceLnPosTag) host.getTag(SourceLnPosTag.NAME);
        if (tag != null && tag.startLn() <= lineNumber && tag.endLn() >= lineNumber) {
            return true;
        }
        LineNumberTag tag2 = (LineNumberTag) host.getTag(LineNumberTag.NAME);
        if (tag2 != null && tag2.getLineNumber() == lineNumber) {
            return true;
        }
        return false;
    }

    public Set<String> classForNameClassNames(SootMethod container) {
        Set<String> ret = this.classForNameReceivers.get(container);
        return ret != null ? ret : Collections.emptySet();
    }

    public Set<SootClass> classForNameClasses(SootMethod container) {
        Set<SootClass> result = new LinkedHashSet<>();
        for (String className : classForNameClassNames(container)) {
            result.add(Scene.v().getSootClass(className));
        }
        return result;
    }

    public Set<String> classNewInstanceClassNames(SootMethod container) {
        Set<String> ret = this.classNewInstanceReceivers.get(container);
        return ret != null ? ret : Collections.emptySet();
    }

    public Set<SootClass> classNewInstanceClasses(SootMethod container) {
        Set<SootClass> result = new LinkedHashSet<>();
        for (String className : classNewInstanceClassNames(container)) {
            result.add(Scene.v().getSootClass(className));
        }
        return result;
    }

    public Set<String> constructorNewInstanceSignatures(SootMethod container) {
        Set<String> ret = this.constructorNewInstanceReceivers.get(container);
        return ret != null ? ret : Collections.emptySet();
    }

    public Set<SootMethod> constructorNewInstanceConstructors(SootMethod container) {
        Set<SootMethod> result = new LinkedHashSet<>();
        for (String signature : constructorNewInstanceSignatures(container)) {
            result.add(Scene.v().getMethod(signature));
        }
        return result;
    }

    public Set<String> methodInvokeSignatures(SootMethod container) {
        Set<String> ret = this.methodInvokeReceivers.get(container);
        return ret != null ? ret : Collections.emptySet();
    }

    public Set<SootMethod> methodInvokeMethods(SootMethod container) {
        Set<SootMethod> result = new LinkedHashSet<>();
        for (String signature : methodInvokeSignatures(container)) {
            result.add(Scene.v().getMethod(signature));
        }
        return result;
    }

    public Set<SootMethod> methodsContainingReflectiveCalls() {
        Set<SootMethod> res = new LinkedHashSet<>();
        res.addAll(this.classForNameReceivers.keySet());
        res.addAll(this.classNewInstanceReceivers.keySet());
        res.addAll(this.constructorNewInstanceReceivers.keySet());
        res.addAll(this.methodInvokeReceivers.keySet());
        return res;
    }

    public Set<String> fieldSetSignatures(SootMethod container) {
        Set<String> ret = this.fieldSetReceivers.get(container);
        return ret != null ? ret : Collections.emptySet();
    }

    public Set<String> fieldGetSignatures(SootMethod container) {
        Set<String> ret = this.fieldGetReceivers.get(container);
        return ret != null ? ret : Collections.emptySet();
    }
}
