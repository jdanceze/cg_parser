package soot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.jimple.SpecialInvokeExpr;
import soot.util.ArraySet;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/Hierarchy.class */
public class Hierarchy {
    protected Map<SootClass, List<SootClass>> classToSubclasses;
    protected Map<SootClass, List<SootClass>> interfaceToSubinterfaces;
    protected Map<SootClass, List<SootClass>> interfaceToSuperinterfaces;
    protected Map<SootClass, List<SootClass>> classToDirSubclasses;
    protected Map<SootClass, List<SootClass>> interfaceToDirSubinterfaces;
    protected Map<SootClass, List<SootClass>> interfaceToDirSuperinterfaces;
    protected Map<SootClass, List<SootClass>> interfaceToDirImplementers;
    final Scene sc = Scene.v();
    final int state = this.sc.getState();

    public Hierarchy() {
        Chain<SootClass> allClasses = this.sc.getClasses();
        int mapSize = (allClasses.size() * 2) + 1;
        this.classToSubclasses = new HashMap(mapSize, 0.7f);
        this.interfaceToSubinterfaces = new HashMap(mapSize, 0.7f);
        this.interfaceToSuperinterfaces = new HashMap(mapSize, 0.7f);
        this.classToDirSubclasses = new HashMap(mapSize, 0.7f);
        this.interfaceToDirSubinterfaces = new HashMap(mapSize, 0.7f);
        this.interfaceToDirSuperinterfaces = new HashMap(mapSize, 0.7f);
        this.interfaceToDirImplementers = new HashMap(mapSize, 0.7f);
        initializeHierarchy(allClasses);
    }

    protected void initializeHierarchy(Chain<SootClass> allClasses) {
        List<SootClass> l;
        List<SootClass> l2;
        for (SootClass c : allClasses) {
            if (c.resolvingLevel() >= 1) {
                if (c.isInterface()) {
                    this.interfaceToDirSubinterfaces.put(c, new ArrayList());
                    this.interfaceToDirSuperinterfaces.put(c, new ArrayList());
                    this.interfaceToDirImplementers.put(c, new ArrayList());
                } else {
                    this.classToDirSubclasses.put(c, new ArrayList());
                }
            }
        }
        for (SootClass c2 : allClasses) {
            if (c2.resolvingLevel() >= 1 && c2.hasSuperclass()) {
                if (c2.isInterface()) {
                    List<SootClass> l22 = this.interfaceToDirSuperinterfaces.get(c2);
                    for (SootClass i : c2.getInterfaces()) {
                        if (c2.resolvingLevel() >= 1) {
                            List<SootClass> l3 = this.interfaceToDirSubinterfaces.get(i);
                            if (l3 != null) {
                                l3.add(c2);
                            }
                            if (l22 != null) {
                                l22.add(i);
                            }
                        }
                    }
                } else {
                    List<SootClass> l4 = this.classToDirSubclasses.get(c2.getSuperclass());
                    if (l4 != null) {
                        l4.add(c2);
                    }
                    for (SootClass i2 : c2.getInterfaces()) {
                        if (c2.resolvingLevel() >= 1 && (l2 = this.interfaceToDirImplementers.get(i2)) != null) {
                            l2.add(c2);
                        }
                    }
                }
            }
        }
        for (SootClass c3 : allClasses) {
            if (c3.resolvingLevel() >= 1) {
                if (c3.isInterface()) {
                    Set<SootClass> s = new ArraySet<>();
                    for (SootClass c0 : this.interfaceToDirImplementers.get(c3)) {
                        if (c3.resolvingLevel() >= 1) {
                            s.addAll(getSubclassesOfIncluding(c0));
                        }
                    }
                    this.interfaceToDirImplementers.put(c3, new ArrayList(s));
                } else if (c3.hasSuperclass() && (l = this.classToDirSubclasses.get(c3)) != null) {
                    this.classToDirSubclasses.put(c3, new ArrayList(l));
                }
            }
        }
    }

    protected void checkState() {
        if (this.state != this.sc.getState()) {
            throw new ConcurrentModificationException("Scene changed for Hierarchy!");
        }
    }

    public List<SootClass> getSubclassesOfIncluding(SootClass c) {
        c.checkLevel(1);
        if (c.isInterface()) {
            throw new RuntimeException("class needed!");
        }
        List<SootClass> subclasses = getSubclassesOf(c);
        List<SootClass> result = new ArrayList<>(subclasses.size() + 1);
        result.addAll(subclasses);
        result.add(c);
        return Collections.unmodifiableList(result);
    }

    public List<SootClass> getSubclassesOf(SootClass c) {
        c.checkLevel(1);
        if (c.isInterface()) {
            throw new RuntimeException("class needed!");
        }
        checkState();
        List<SootClass> retVal = this.classToSubclasses.get(c);
        if (retVal != null) {
            return retVal;
        }
        ArrayList<SootClass> l = new ArrayList<>();
        for (SootClass cls : this.classToDirSubclasses.get(c)) {
            if (cls.resolvingLevel() >= 1) {
                l.addAll(getSubclassesOfIncluding(cls));
            }
        }
        l.trimToSize();
        List<SootClass> retVal2 = Collections.unmodifiableList(l);
        this.classToSubclasses.put(c, retVal2);
        return retVal2;
    }

    public List<SootClass> getSuperclassesOfIncluding(SootClass sootClass) {
        List<SootClass> superclasses = getSuperclassesOf(sootClass);
        List<SootClass> result = new ArrayList<>(superclasses.size() + 1);
        result.add(sootClass);
        result.addAll(superclasses);
        return Collections.unmodifiableList(result);
    }

    public List<SootClass> getSuperclassesOf(SootClass sootClass) {
        sootClass.checkLevel(1);
        if (sootClass.isInterface()) {
            throw new IllegalArgumentException(String.valueOf(sootClass.getName()) + " is an interface, but class is expected");
        }
        checkState();
        List<SootClass> superclasses = new ArrayList<>();
        SootClass sootClass2 = sootClass;
        while (true) {
            SootClass current = sootClass2;
            if (current.hasSuperclass()) {
                SootClass superclass = current.getSuperclass();
                superclasses.add(superclass);
                sootClass2 = superclass;
            } else {
                return Collections.unmodifiableList(superclasses);
            }
        }
    }

    public List<SootClass> getSubinterfacesOfIncluding(SootClass sootClass) {
        List<SootClass> subinterfaces = getSubinterfacesOf(sootClass);
        List<SootClass> result = new ArrayList<>(subinterfaces.size() + 1);
        result.addAll(subinterfaces);
        result.add(sootClass);
        return Collections.unmodifiableList(result);
    }

    public List<SootClass> getSubinterfacesOf(SootClass sootClass) {
        sootClass.checkLevel(1);
        if (!sootClass.isInterface()) {
            throw new IllegalArgumentException(String.valueOf(sootClass.getName()) + " is a class, but interface is expected");
        }
        checkState();
        List<SootClass> retVal = this.interfaceToSubinterfaces.get(sootClass);
        if (retVal != null) {
            return retVal;
        }
        List<SootClass> directSubInterfaces = this.interfaceToDirSubinterfaces.get(sootClass);
        if (directSubInterfaces == null || directSubInterfaces.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<SootClass> l = new ArrayList<>();
        for (SootClass si : directSubInterfaces) {
            l.addAll(getSubinterfacesOfIncluding(si));
        }
        l.trimToSize();
        List<SootClass> retVal2 = Collections.unmodifiableList(l);
        this.interfaceToSubinterfaces.put(sootClass, retVal2);
        return retVal2;
    }

    public List<SootClass> getSuperinterfacesOfIncluding(SootClass c) {
        c.checkLevel(1);
        if (!c.isInterface()) {
            throw new RuntimeException("interface needed!");
        }
        List<SootClass> superinterfaces = getSuperinterfacesOf(c);
        List<SootClass> result = new ArrayList<>(superinterfaces.size() + 1);
        result.addAll(superinterfaces);
        result.add(c);
        return Collections.unmodifiableList(result);
    }

    public List<SootClass> getSuperinterfacesOf(SootClass c) {
        c.checkLevel(1);
        if (!c.isInterface()) {
            throw new RuntimeException("interface needed!");
        }
        checkState();
        List<SootClass> retVal = this.interfaceToSuperinterfaces.get(c);
        if (retVal != null) {
            return retVal;
        }
        ArrayList<SootClass> l = new ArrayList<>();
        for (SootClass si : this.interfaceToDirSuperinterfaces.get(c)) {
            l.addAll(getSuperinterfacesOfIncluding(si));
        }
        l.trimToSize();
        List<SootClass> retVal2 = Collections.unmodifiableList(l);
        this.interfaceToSuperinterfaces.put(c, retVal2);
        return retVal2;
    }

    public List<SootClass> getDirectSuperclassesOf(SootClass c) {
        throw new RuntimeException("Not implemented yet!");
    }

    public List<SootClass> getDirectSubclassesOf(SootClass c) {
        c.checkLevel(1);
        if (c.isInterface()) {
            throw new RuntimeException("class needed!");
        }
        checkState();
        return Collections.unmodifiableList(this.classToDirSubclasses.get(c));
    }

    public List<SootClass> getDirectSubclassesOfIncluding(SootClass c) {
        c.checkLevel(1);
        if (c.isInterface()) {
            throw new RuntimeException("class needed!");
        }
        checkState();
        List<SootClass> subclasses = this.classToDirSubclasses.get(c);
        List<SootClass> l = new ArrayList<>(subclasses.size() + 1);
        l.addAll(subclasses);
        l.add(c);
        return Collections.unmodifiableList(l);
    }

    public List<SootClass> getDirectSuperinterfacesOf(SootClass c) {
        throw new RuntimeException("Not implemented yet!");
    }

    public List<SootClass> getDirectSubinterfacesOf(SootClass c) {
        c.checkLevel(1);
        if (!c.isInterface()) {
            throw new RuntimeException("interface needed!");
        }
        checkState();
        return Collections.unmodifiableList(this.interfaceToDirSubinterfaces.get(c));
    }

    public List<SootClass> getDirectSubinterfacesOfIncluding(SootClass c) {
        c.checkLevel(1);
        if (!c.isInterface()) {
            throw new RuntimeException("interface needed!");
        }
        checkState();
        List<SootClass> subinterfaces = this.interfaceToDirSubinterfaces.get(c);
        List<SootClass> l = new ArrayList<>(subinterfaces.size() + 1);
        l.addAll(subinterfaces);
        l.add(c);
        return Collections.unmodifiableList(l);
    }

    public List<SootClass> getDirectImplementersOf(SootClass i) {
        i.checkLevel(1);
        if (!i.isInterface()) {
            throw new RuntimeException("interface needed; got " + i);
        }
        checkState();
        return Collections.unmodifiableList(this.interfaceToDirImplementers.get(i));
    }

    public List<SootClass> getImplementersOf(SootClass i) {
        i.checkLevel(1);
        if (!i.isInterface()) {
            throw new RuntimeException("interface needed; got " + i);
        }
        checkState();
        ArraySet<SootClass> set = new ArraySet<>();
        for (SootClass c : getSubinterfacesOfIncluding(i)) {
            set.addAll(getDirectImplementersOf(c));
        }
        return Collections.unmodifiableList(new ArrayList(set));
    }

    public boolean isClassSubclassOf(SootClass child, SootClass possibleParent) {
        child.checkLevel(1);
        possibleParent.checkLevel(1);
        List<SootClass> parentClasses = getSuperclassesOf(child);
        if (parentClasses.contains(possibleParent)) {
            return true;
        }
        for (SootClass sc : parentClasses) {
            if (sc.isPhantom()) {
                return true;
            }
        }
        return false;
    }

    public boolean isClassSubclassOfIncluding(SootClass child, SootClass possibleParent) {
        child.checkLevel(1);
        possibleParent.checkLevel(1);
        List<SootClass> parentClasses = getSuperclassesOfIncluding(child);
        if (parentClasses.contains(possibleParent)) {
            return true;
        }
        for (SootClass sc : parentClasses) {
            if (sc.isPhantom()) {
                return true;
            }
        }
        return false;
    }

    public boolean isClassDirectSubclassOf(SootClass c, SootClass c2) {
        throw new RuntimeException("Not implemented yet!");
    }

    public boolean isClassSuperclassOf(SootClass parent, SootClass possibleChild) {
        parent.checkLevel(1);
        possibleChild.checkLevel(1);
        return getSubclassesOf(parent).contains(possibleChild);
    }

    public boolean isClassSuperclassOfIncluding(SootClass parent, SootClass possibleChild) {
        parent.checkLevel(1);
        possibleChild.checkLevel(1);
        return getSubclassesOfIncluding(parent).contains(possibleChild);
    }

    public boolean isInterfaceSubinterfaceOf(SootClass child, SootClass possibleParent) {
        child.checkLevel(1);
        possibleParent.checkLevel(1);
        return getSubinterfacesOf(possibleParent).contains(child);
    }

    public boolean isInterfaceDirectSubinterfaceOf(SootClass child, SootClass possibleParent) {
        child.checkLevel(1);
        possibleParent.checkLevel(1);
        return getDirectSubinterfacesOf(possibleParent).contains(child);
    }

    public boolean isInterfaceSuperinterfaceOf(SootClass parent, SootClass possibleChild) {
        parent.checkLevel(1);
        possibleChild.checkLevel(1);
        return getSuperinterfacesOf(possibleChild).contains(parent);
    }

    public boolean isInterfaceDirectSuperinterfaceOf(SootClass parent, SootClass possibleChild) {
        parent.checkLevel(1);
        possibleChild.checkLevel(1);
        return getDirectSuperinterfacesOf(possibleChild).contains(parent);
    }

    public SootClass getLeastCommonSuperclassOf(SootClass c1, SootClass c2) {
        c1.checkLevel(1);
        c2.checkLevel(1);
        throw new RuntimeException("Not implemented yet!");
    }

    public boolean isVisible(SootClass from, SootClass check) {
        if (check.isPublic()) {
            return true;
        }
        if (check.isProtected() || check.isPrivate()) {
            return false;
        }
        return from.getJavaPackageName().equals(check.getJavaPackageName());
    }

    public boolean isVisible(SootClass from, ClassMember m) {
        from.checkLevel(1);
        SootClass declaringClass = m.getDeclaringClass();
        declaringClass.checkLevel(1);
        if (!isVisible(from, declaringClass)) {
            return false;
        }
        if (m.isPublic()) {
            return true;
        }
        if (m.isPrivate()) {
            return from.equals(declaringClass);
        }
        if (m.isProtected()) {
            return isClassSubclassOfIncluding(from, declaringClass) || from.getJavaPackageName().equals(declaringClass.getJavaPackageName());
        }
        return from.getJavaPackageName().equals(declaringClass.getJavaPackageName());
    }

    public SootMethod resolveConcreteDispatch(SootClass concreteType, SootMethod m) {
        concreteType.checkLevel(1);
        m.getDeclaringClass().checkLevel(1);
        checkState();
        if (concreteType.isInterface()) {
            throw new RuntimeException("class needed!");
        }
        String methodSig = m.getSubSignature();
        for (SootClass c : getSuperclassesOfIncluding(concreteType)) {
            SootMethod sm = c.getMethodUnsafe(methodSig);
            if (sm != null && isVisible(c, m)) {
                return sm;
            }
        }
        throw new RuntimeException("could not resolve concrete dispatch!\nType: " + concreteType + "\nMethod: " + m);
    }

    public List<SootMethod> resolveConcreteDispatch(List<Type> classes, SootMethod m) {
        m.getDeclaringClass().checkLevel(1);
        checkState();
        Set<SootMethod> s = new ArraySet<>();
        for (Type cls : classes) {
            if (cls instanceof RefType) {
                s.add(resolveConcreteDispatch(((RefType) cls).getSootClass(), m));
            } else if (cls instanceof ArrayType) {
                s.add(resolveConcreteDispatch(Scene.v().getObjectType().getSootClass(), m));
            } else {
                throw new RuntimeException("Unable to resolve concrete dispatch of type " + cls);
            }
        }
        return Collections.unmodifiableList(new ArrayList(s));
    }

    public List<SootMethod> resolveAbstractDispatch(SootClass c, SootMethod m) {
        Collection<SootClass> classes;
        c.checkLevel(1);
        m.getDeclaringClass().checkLevel(1);
        checkState();
        if (c.isInterface()) {
            classes = new HashSet<>();
            for (SootClass sootClass : getImplementersOf(c)) {
                classes.addAll(getSubclassesOfIncluding(sootClass));
            }
        } else {
            classes = getSubclassesOfIncluding(c);
        }
        Set<SootMethod> s = new ArraySet<>();
        for (SootClass cl : classes) {
            if (!Modifier.isAbstract(cl.getModifiers())) {
                s.add(resolveConcreteDispatch(cl, m));
            }
        }
        return Collections.unmodifiableList(new ArrayList(s));
    }

    public List<SootMethod> resolveAbstractDispatch(List<SootClass> classes, SootMethod m) {
        m.getDeclaringClass().checkLevel(1);
        Set<SootMethod> s = new ArraySet<>();
        for (SootClass sootClass : classes) {
            s.addAll(resolveAbstractDispatch(sootClass, m));
        }
        return Collections.unmodifiableList(new ArrayList(s));
    }

    public SootMethod resolveSpecialDispatch(SpecialInvokeExpr ie, SootMethod container) {
        SootClass containerClass = container.getDeclaringClass();
        containerClass.checkLevel(1);
        SootMethod target = ie.getMethod();
        SootClass targetClass = target.getDeclaringClass();
        targetClass.checkLevel(1);
        if ("<init>".equals(target.getName()) || target.isPrivate()) {
            return target;
        }
        if (isClassSubclassOf(targetClass, containerClass)) {
            return resolveConcreteDispatch(containerClass, target);
        }
        return target;
    }
}
