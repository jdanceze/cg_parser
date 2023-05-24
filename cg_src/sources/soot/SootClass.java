package soot;

import com.google.common.base.Optional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.baf.BafBody;
import soot.dava.toolkits.base.misc.PackageNamer;
import soot.options.Options;
import soot.tagkit.AbstractHost;
import soot.util.Chain;
import soot.util.EmptyChain;
import soot.util.HashChain;
import soot.util.Numberable;
import soot.util.NumberedString;
import soot.util.SmallNumberedMap;
import soot.validation.ClassFlagsValidator;
import soot.validation.ClassValidator;
import soot.validation.MethodDeclarationValidator;
import soot.validation.OuterClassValidator;
import soot.validation.ValidationException;
/* loaded from: gencallgraphv3.jar:soot/SootClass.class */
public class SootClass extends AbstractHost implements Numberable {
    private static final Logger logger;
    public static final String INVOKEDYNAMIC_DUMMY_CLASS_NAME = "soot.dummy.InvokeDynamic";
    public static final int DANGLING = 0;
    public static final int HIERARCHY = 1;
    public static final int SIGNATURES = 2;
    public static final int BODIES = 3;
    protected String name;
    protected String shortName;
    protected String fixedShortName;
    protected String packageName;
    protected String fixedPackageName;
    protected int modifiers;
    protected Chain<SootField> fields;
    protected SmallNumberedMap<NumberedString, SootMethod> subSigToMethods;
    protected List<SootMethod> methodList;
    protected Chain<SootClass> interfaces;
    protected boolean isInScene;
    protected SootClass superClass;
    protected SootClass outerClass;
    protected boolean isPhantom;
    public final String moduleName;
    protected SootModuleInfo moduleInformation;
    private RefType refType;
    private volatile int resolvingLevel;
    protected volatile int number;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SootClass.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(SootClass.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/SootClass$LazyValidatorsSingleton.class */
    public static class LazyValidatorsSingleton {
        static final ClassValidator[] V = {OuterClassValidator.v(), MethodDeclarationValidator.v(), ClassFlagsValidator.v()};

        private LazyValidatorsSingleton() {
        }
    }

    public SootClass(String name, int modifiers) {
        this(name, modifiers, null);
    }

    public SootClass(String name, String moduleName) {
        this(name, 0, moduleName);
    }

    public SootClass(String name) {
        this(name, 0, null);
    }

    public SootClass(String name, int modifiers, String moduleName) {
        this.resolvingLevel = 0;
        this.number = 0;
        if (name.length() == 0) {
            throw new RuntimeException("Class must not be empty!");
        }
        if (name.length() > 0 && name.charAt(0) == '[') {
            throw new RuntimeException("Attempt to make a class whose name starts with [");
        }
        this.moduleName = moduleName;
        setName(name);
        this.modifiers = modifiers;
        initializeRefType(name, moduleName);
        if (Options.v().debug_resolver()) {
            logger.debug("created " + name + " with modifiers " + modifiers);
        }
        setResolvingLevel(3);
    }

    protected void initializeRefType(String name, String moduleName) {
        if (ModuleUtil.module_mode()) {
            this.refType = ModuleRefType.v(name, Optional.fromNullable(this.moduleName));
        } else {
            this.refType = RefType.v(name);
        }
        this.refType.setSootClass(this);
    }

    protected static String levelToString(int level) {
        switch (level) {
            case 0:
                return "DANGLING";
            case 1:
                return "HIERARCHY";
            case 2:
                return "SIGNATURES";
            case 3:
                return "BODIES";
            default:
                throw new RuntimeException("unknown resolving level");
        }
    }

    public void checkLevel(int level) {
        if (resolvingLevel() >= level || !Scene.v().doneResolving() || Options.v().ignore_resolving_levels()) {
            return;
        }
        checkLevelIgnoreResolving(level);
    }

    public void checkLevelIgnoreResolving(int level) {
        int currentLevel = resolvingLevel();
        if (currentLevel < level) {
            String hint = "\nIf you are extending Soot, try to add the following call before calling soot.Main.main(..):\nScene.v().addBasicClass(" + getName() + "," + levelToString(level) + ");\nOtherwise, try whole-program mode (-w).";
            throw new RuntimeException("This operation requires resolving level " + levelToString(level) + " but " + this.name + " is at resolving level " + levelToString(currentLevel) + hint);
        }
    }

    public int resolvingLevel() {
        return this.resolvingLevel;
    }

    public void setResolvingLevel(int newLevel) {
        this.resolvingLevel = newLevel;
    }

    public boolean isInScene() {
        return this.isInScene;
    }

    public void setInScene(boolean isInScene) {
        this.isInScene = isInScene;
        Scene.v().getClassNumberer().add(this);
    }

    public int getFieldCount() {
        checkLevel(2);
        if (this.fields == null) {
            return 0;
        }
        return this.fields.size();
    }

    public Chain<SootField> getFields() {
        checkLevel(2);
        return this.fields == null ? EmptyChain.v() : this.fields;
    }

    public void addField(SootField f) {
        checkLevel(2);
        if (f.isDeclared()) {
            throw new RuntimeException("already declared: " + f.getName());
        }
        if (declaresField(f.getName(), f.getType())) {
            throw new RuntimeException("Field already exists : " + f.getName() + " of type " + f.getType());
        }
        if (this.fields == null) {
            this.fields = new HashChain();
        }
        f.setDeclared(true);
        f.setDeclaringClass(this);
        this.fields.add(f);
    }

    public void removeField(SootField f) {
        checkLevel(2);
        if (!f.isDeclared() || f.getDeclaringClass() != this) {
            throw new RuntimeException("did not declare: " + f.getName());
        }
        if (this.fields != null) {
            this.fields.remove(f);
        }
        f.setDeclared(false);
        f.setDeclaringClass(null);
    }

    public SootField getField(String name, Type type) {
        SootField sf = getFieldUnsafe(name, type);
        if (sf == null) {
            throw new RuntimeException("No field " + name + " in class " + getName());
        }
        return sf;
    }

    public SootField getFieldUnsafe(String name, Type type) {
        checkLevel(2);
        if (this.fields != null) {
            for (SootField field : this.fields.getElementsUnsorted()) {
                if (name.equals(field.getName()) && type.equals(field.getType())) {
                    return field;
                }
            }
            return null;
        }
        return null;
    }

    public SootField getFieldByName(String name) {
        SootField foundField = getFieldByNameUnsafe(name);
        if (foundField == null) {
            throw new RuntimeException("No field " + name + " in class " + getName());
        }
        return foundField;
    }

    public SootField getFieldByNameUnsafe(String name) {
        if ($assertionsDisabled || name != null) {
            checkLevel(2);
            SootField foundField = null;
            if (this.fields != null) {
                for (SootField field : this.fields.getElementsUnsorted()) {
                    if (name.equals(field.getName())) {
                        if (foundField == null) {
                            foundField = field;
                        } else {
                            throw new AmbiguousFieldException(name, this.name);
                        }
                    }
                }
            }
            return foundField;
        }
        throw new AssertionError();
    }

    public SootField getField(String subsignature) {
        SootField sf = getFieldUnsafe(subsignature);
        if (sf == null) {
            throw new RuntimeException("No field " + subsignature + " in class " + getName());
        }
        return sf;
    }

    public SootField getFieldUnsafe(String subsignature) {
        checkLevel(2);
        if (this.fields != null) {
            for (SootField field : this.fields.getElementsUnsorted()) {
                if (subsignature.equals(field.getSubSignature())) {
                    return field;
                }
            }
            return null;
        }
        return null;
    }

    public boolean declaresField(String subsignature) {
        return getFieldUnsafe(subsignature) != null;
    }

    public SootMethod getMethod(NumberedString subsignature) {
        SootMethod ret = getMethodUnsafe(subsignature);
        if (ret == null) {
            throw new RuntimeException("No method " + subsignature + " in class " + getName());
        }
        return ret;
    }

    public SootMethod getMethodUnsafe(NumberedString subsignature) {
        checkLevel(2);
        if (this.subSigToMethods != null) {
            return this.subSigToMethods.get(subsignature);
        }
        return null;
    }

    public boolean declaresMethod(NumberedString subsignature) {
        return getMethodUnsafe(subsignature) != null;
    }

    public SootMethod getMethod(String subsignature) {
        NumberedString numberedString = Scene.v().getSubSigNumberer().find(subsignature);
        if (numberedString == null) {
            throw new RuntimeException("No method " + subsignature + " in class " + getName());
        }
        return getMethod(numberedString);
    }

    public SootMethod getMethodUnsafe(String subsignature) {
        NumberedString numberedString = Scene.v().getSubSigNumberer().find(subsignature);
        if (numberedString == null) {
            return null;
        }
        return getMethodUnsafe(numberedString);
    }

    public boolean declaresMethod(String subsignature) {
        NumberedString numberedString = Scene.v().getSubSigNumberer().find(subsignature);
        if (numberedString == null) {
            return false;
        }
        return declaresMethod(numberedString);
    }

    public boolean declaresFieldByName(String name) {
        checkLevel(2);
        if (this.fields != null) {
            for (SootField field : this.fields) {
                if (name.equals(field.getName())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean declaresField(String name, Type type) {
        checkLevel(2);
        if (this.fields != null) {
            for (SootField field : this.fields) {
                if (name.equals(field.getName()) && type.equals(field.getType())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public int getMethodCount() {
        checkLevel(2);
        if (this.subSigToMethods == null) {
            return 0;
        }
        return this.subSigToMethods.nonNullSize();
    }

    public Iterator<SootMethod> methodIterator() {
        checkLevel(2);
        if (this.methodList == null) {
            return Collections.emptyIterator();
        }
        return new Iterator<SootMethod>() { // from class: soot.SootClass.1
            final Iterator<SootMethod> internalIterator;
            private SootMethod currentMethod;

            {
                this.internalIterator = SootClass.this.methodList.iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.internalIterator.hasNext();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public SootMethod next() {
                this.currentMethod = this.internalIterator.next();
                return this.currentMethod;
            }

            @Override // java.util.Iterator
            public void remove() {
                this.internalIterator.remove();
                SootClass.this.subSigToMethods.put(this.currentMethod.getNumberedSubSignature(), null);
                this.currentMethod.setDeclared(false);
            }
        };
    }

    public List<SootMethod> getMethods() {
        checkLevel(2);
        return this.methodList != null ? this.methodList : Collections.emptyList();
    }

    public SootMethod getMethod(String name, List<Type> parameterTypes, Type returnType) {
        SootMethod sm = getMethodUnsafe(name, parameterTypes, returnType);
        if (sm != null) {
            return sm;
        }
        throw new RuntimeException("Class " + getName() + " doesn't have method \"" + SootMethod.getSubSignature(name, parameterTypes, returnType) + "\"");
    }

    public SootMethod getMethodUnsafe(String name, List<Type> parameterTypes, Type returnType) {
        checkLevel(2);
        if (this.methodList != null) {
            Iterator it = new ArrayList(this.methodList).iterator();
            while (it.hasNext()) {
                SootMethod method = (SootMethod) it.next();
                if (name.equals(method.getName()) && returnType.equals(method.getReturnType()) && parameterTypes.equals(method.getParameterTypes())) {
                    return method;
                }
            }
            return null;
        }
        return null;
    }

    public SootMethod getMethod(String name, List<Type> parameterTypes) {
        checkLevel(2);
        if (this.methodList != null) {
            SootMethod foundMethod = null;
            for (SootMethod method : this.methodList) {
                if (name.equals(method.getName()) && parameterTypes.equals(method.getParameterTypes())) {
                    if (foundMethod == null) {
                        foundMethod = method;
                    } else {
                        throw new AmbiguousMethodException(name, this.name);
                    }
                }
            }
            if (foundMethod != null) {
                return foundMethod;
            }
        }
        throw new RuntimeException("couldn't find method " + name + "(" + parameterTypes + ") in " + this);
    }

    public SootMethod getMethodByNameUnsafe(String name) {
        checkLevel(2);
        SootMethod foundMethod = null;
        if (this.methodList != null) {
            for (SootMethod method : this.methodList) {
                if (name.equals(method.getName())) {
                    if (foundMethod == null) {
                        foundMethod = method;
                    } else {
                        throw new AmbiguousMethodException(name, this.name);
                    }
                }
            }
        }
        return foundMethod;
    }

    public SootMethod getMethodByName(String name) {
        SootMethod foundMethod = getMethodByNameUnsafe(name);
        if (foundMethod == null) {
            throw new RuntimeException("couldn't find method " + name + "(*) in " + this);
        }
        return foundMethod;
    }

    public boolean declaresMethod(String name, List<Type> parameterTypes) {
        checkLevel(2);
        if (this.methodList != null) {
            for (SootMethod method : this.methodList) {
                if (name.equals(method.getName()) && parameterTypes.equals(method.getParameterTypes())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean declaresMethod(String name, List<Type> parameterTypes, Type returnType) {
        checkLevel(2);
        if (this.methodList != null) {
            for (SootMethod method : this.methodList) {
                if (name.equals(method.getName()) && returnType.equals(method.getReturnType()) && parameterTypes.equals(method.getParameterTypes())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean declaresMethodByName(String name) {
        checkLevel(2);
        if (this.methodList != null) {
            for (SootMethod method : this.methodList) {
                if (name.equals(method.getName())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public void addMethod(SootMethod m) {
        checkLevel(2);
        if (m.isDeclared()) {
            throw new RuntimeException("already declared: " + m.getName());
        }
        if (this.methodList == null) {
            this.methodList = Collections.synchronizedList(new ArrayList());
            this.subSigToMethods = new SmallNumberedMap<>();
        }
        if (this.subSigToMethods.get(m.getNumberedSubSignature()) != null) {
            throw new RuntimeException("Attempting to add method " + m.getSubSignature() + " to class " + this + ", but the class already has a method with that signature.");
        }
        this.subSigToMethods.put(m.getNumberedSubSignature(), m);
        this.methodList.add(m);
        m.setDeclared(true);
        m.setDeclaringClass(this);
    }

    public synchronized SootMethod getOrAddMethod(SootMethod m) {
        checkLevel(2);
        if (m.isDeclared()) {
            throw new RuntimeException("already declared: " + m.getName());
        }
        if (this.methodList == null) {
            this.methodList = Collections.synchronizedList(new ArrayList());
            this.subSigToMethods = new SmallNumberedMap<>();
        }
        SootMethod old = this.subSigToMethods.get(m.getNumberedSubSignature());
        if (old != null) {
            return old;
        }
        this.subSigToMethods.put(m.getNumberedSubSignature(), m);
        this.methodList.add(m);
        m.setDeclared(true);
        m.setDeclaringClass(this);
        return m;
    }

    public synchronized SootField getOrAddField(SootField f) {
        checkLevel(2);
        if (f.isDeclared()) {
            throw new RuntimeException("already declared: " + f.getName());
        }
        SootField old = getFieldUnsafe(f.getName(), f.getType());
        if (old != null) {
            return old;
        }
        if (this.fields == null) {
            this.fields = new HashChain();
        }
        f.setDeclared(true);
        f.setDeclaringClass(this);
        this.fields.add(f);
        return f;
    }

    public void removeMethod(SootMethod m) {
        checkLevel(2);
        if (!m.isDeclared() || m.getDeclaringClass() != this) {
            throw new RuntimeException("incorrect declarer for remove: " + m.getName());
        }
        if (this.subSigToMethods.get(m.getNumberedSubSignature()) == null) {
            throw new RuntimeException("Attempt to remove method " + m.getSubSignature() + " which is not in class " + this);
        }
        this.subSigToMethods.put(m.getNumberedSubSignature(), null);
        this.methodList.remove(m);
        m.setDeclared(false);
        m.setDeclaringClass(null);
        Scene scene = Scene.v();
        scene.getMethodNumberer().remove(m);
        scene.modifyHierarchy();
    }

    public int getModifiers() {
        return this.modifiers;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public int getInterfaceCount() {
        checkLevel(1);
        if (this.interfaces == null) {
            return 0;
        }
        return this.interfaces.size();
    }

    public Chain<SootClass> getInterfaces() {
        checkLevel(1);
        return this.interfaces == null ? EmptyChain.v() : this.interfaces;
    }

    public boolean implementsInterface(String name) {
        checkLevel(1);
        if (this.interfaces != null) {
            for (SootClass sc : this.interfaces) {
                if (name.equals(sc.getName())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public void addInterface(SootClass interfaceClass) {
        if (implementsInterface(interfaceClass.getName())) {
            throw new RuntimeException("duplicate interface on class " + getName() + ": " + interfaceClass.getName());
        }
        if (this.interfaces == null) {
            this.interfaces = new HashChain(4);
        }
        this.interfaces.add(interfaceClass);
    }

    public void removeInterface(SootClass interfaceClass) {
        if (!implementsInterface(interfaceClass.getName())) {
            throw new RuntimeException("no such interface on class " + getName() + ": " + interfaceClass.getName());
        }
        this.interfaces.remove(interfaceClass);
        if (this.interfaces.isEmpty()) {
            this.interfaces = null;
        }
    }

    public boolean hasSuperclass() {
        checkLevel(1);
        return this.superClass != null;
    }

    public SootClass getSuperclass() {
        checkLevel(1);
        if (this.superClass == null && !isPhantom() && !Options.v().ignore_resolution_errors()) {
            throw new RuntimeException("no superclass for " + getName());
        }
        return this.superClass;
    }

    public SootClass getSuperclassUnsafe() {
        checkLevel(1);
        return this.superClass;
    }

    public void setSuperclass(SootClass c) {
        checkLevel(1);
        this.superClass = c;
    }

    public boolean hasOuterClass() {
        checkLevel(1);
        return this.outerClass != null;
    }

    public SootClass getOuterClass() {
        checkLevel(1);
        if (this.outerClass == null) {
            throw new RuntimeException("no outer class");
        }
        return this.outerClass;
    }

    public SootClass getOuterClassUnsafe() {
        checkLevel(1);
        return this.outerClass;
    }

    public void setOuterClass(SootClass c) {
        checkLevel(1);
        this.outerClass = c;
    }

    public boolean isInnerClass() {
        return hasOuterClass();
    }

    public String getName() {
        return this.name;
    }

    public String getJavaStyleName() {
        if (PackageNamer.v().has_FixedNames()) {
            if (this.fixedShortName == null) {
                this.fixedShortName = PackageNamer.v().get_FixedClassName(this.name);
            }
            if (!PackageNamer.v().use_ShortName(getJavaPackageName(), this.fixedShortName)) {
                return String.valueOf(getJavaPackageName()) + '.' + this.fixedShortName;
            }
            return this.fixedShortName;
        }
        return this.shortName;
    }

    public String getShortJavaStyleName() {
        if (PackageNamer.v().has_FixedNames()) {
            if (this.fixedShortName == null) {
                this.fixedShortName = PackageNamer.v().get_FixedClassName(this.name);
            }
            return this.fixedShortName;
        }
        return this.shortName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getJavaPackageName() {
        if (PackageNamer.v().has_FixedNames()) {
            if (this.fixedPackageName == null) {
                this.fixedPackageName = PackageNamer.v().get_FixedPackageName(this.packageName);
            }
            return this.fixedPackageName;
        }
        return this.packageName;
    }

    public void setName(String name) {
        this.name = name.intern();
        int index = name.lastIndexOf(46);
        if (index > 0) {
            this.shortName = name.substring(index + 1);
            this.packageName = name.substring(0, index);
        } else {
            this.shortName = name;
            this.packageName = "";
        }
        this.fixedShortName = null;
        this.fixedPackageName = null;
    }

    public boolean isInterface() {
        checkLevel(1);
        return Modifier.isInterface(getModifiers());
    }

    public boolean isEnum() {
        checkLevel(1);
        return Modifier.isEnum(getModifiers());
    }

    public boolean isSynchronized() {
        checkLevel(1);
        return Modifier.isSynchronized(getModifiers());
    }

    public boolean isConcrete() {
        return (isInterface() || isAbstract()) ? false : true;
    }

    public boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    public boolean containsBafBody() {
        Iterator<SootMethod> methodIt = methodIterator();
        while (methodIt.hasNext()) {
            SootMethod m = methodIt.next();
            if (m.hasActiveBody() && (m.getActiveBody() instanceof BafBody)) {
                return true;
            }
        }
        return false;
    }

    public void setRefType(RefType refType) {
        this.refType = refType;
    }

    public boolean hasRefType() {
        return this.refType != null;
    }

    public RefType getType() {
        return this.refType;
    }

    public String toString() {
        return getName();
    }

    public void renameFieldsAndMethods(boolean privateOnly) {
        checkLevel(2);
        int fieldCount = 0;
        for (SootField f : getFields()) {
            if (!privateOnly || Modifier.isPrivate(f.getModifiers())) {
                int i = fieldCount;
                fieldCount++;
                f.setName("__field" + i);
            }
        }
        int methodCount = 0;
        Iterator<SootMethod> methodIt = methodIterator();
        while (methodIt.hasNext()) {
            SootMethod m = methodIt.next();
            if (!privateOnly || Modifier.isPrivate(m.getModifiers())) {
                int i2 = methodCount;
                methodCount++;
                m.setName("__method" + i2);
            }
        }
    }

    public boolean isApplicationClass() {
        return Scene.v().getApplicationClasses().contains(this);
    }

    public void setApplicationClass() {
        if (isApplicationClass()) {
            return;
        }
        Chain<SootClass> c = Scene.v().getContainingChain(this);
        if (c != null) {
            c.remove(this);
        }
        Scene.v().getApplicationClasses().add(this);
        this.isPhantom = false;
    }

    public boolean isLibraryClass() {
        return Scene.v().getLibraryClasses().contains(this);
    }

    public void setLibraryClass() {
        if (isLibraryClass()) {
            return;
        }
        Chain<SootClass> c = Scene.v().getContainingChain(this);
        if (c != null) {
            c.remove(this);
        }
        Scene.v().getLibraryClasses().add(this);
        this.isPhantom = false;
    }

    public boolean isJavaLibraryClass() {
        return this.name.startsWith("java.") || this.name.startsWith("sun.") || this.name.startsWith("javax.") || this.name.startsWith("com.sun.") || this.name.startsWith("org.omg.") || this.name.startsWith("org.xml.") || this.name.startsWith("org.w3c.dom");
    }

    public boolean isPhantomClass() {
        return Scene.v().getPhantomClasses().contains(this);
    }

    public void setPhantomClass() {
        Chain<SootClass> c = Scene.v().getContainingChain(this);
        if (c != null) {
            c.remove(this);
        }
        Scene.v().getPhantomClasses().add(this);
        this.isPhantom = true;
    }

    public boolean isPhantom() {
        return this.isPhantom;
    }

    public boolean isPrivate() {
        return Modifier.isPrivate(getModifiers());
    }

    public boolean isProtected() {
        return Modifier.isProtected(getModifiers());
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(getModifiers());
    }

    public boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    @Override // soot.util.Numberable
    public final int getNumber() {
        return this.number;
    }

    @Override // soot.util.Numberable
    public void setNumber(int number) {
        this.number = number;
    }

    public void rename(String newName) {
        this.name = newName;
        if (this.refType != null) {
            this.refType.setClassName(this.name);
        } else if (ModuleUtil.module_mode()) {
            this.refType = ModuleScene.v().getOrAddRefType(this.name, Optional.fromNullable(this.moduleName));
        } else {
            this.refType = Scene.v().getOrAddRefType(this.name);
        }
    }

    public void validate() {
        List<ValidationException> exceptionList = new ArrayList<>();
        validate(exceptionList);
        if (!exceptionList.isEmpty()) {
            throw exceptionList.get(0);
        }
    }

    public void validate(List<ValidationException> exceptionList) {
        ClassValidator[] classValidatorArr;
        boolean runAllValidators = Options.v().debug() || Options.v().validate();
        for (ClassValidator validator : LazyValidatorsSingleton.V) {
            if (runAllValidators || validator.isBasicValidator()) {
                validator.validate(this, exceptionList);
            }
        }
    }

    public String getFilePath() {
        if (ModuleUtil.module_mode()) {
            return String.valueOf(this.moduleName) + ':' + getName();
        }
        return getName();
    }

    public SootModuleInfo getModuleInformation() {
        return this.moduleInformation;
    }

    public void setModuleInformation(SootModuleInfo moduleInformation) {
        this.moduleInformation = moduleInformation;
    }

    public boolean isExportedByModule() {
        if (getModuleInformation() == null && ModuleUtil.module_mode()) {
            Scene.v().forceResolve(getName(), 3);
        }
        SootModuleInfo moduleInfo = getModuleInformation();
        if (moduleInfo == null) {
            return true;
        }
        return moduleInfo.exportsPackagePublic(getJavaPackageName());
    }

    public boolean isExportedByModule(String toModule) {
        if (getModuleInformation() == null && ModuleUtil.module_mode()) {
            ModuleScene.v().forceResolve(getName(), 3, Optional.of(this.moduleName));
        }
        return getModuleInformation().exportsPackage(getJavaPackageName(), toModule);
    }

    public boolean isOpenedByModule() {
        if (getModuleInformation() == null && ModuleUtil.module_mode()) {
            Scene.v().forceResolve(getName(), 3);
        }
        SootModuleInfo moduleInfo = getModuleInformation();
        if (moduleInfo == null) {
            return true;
        }
        return moduleInfo.openPackagePublic(getJavaPackageName());
    }

    public Collection<SootMethod> getMethodsByNameAndParamCount(String name, int paramCount) {
        List<SootMethod> result = null;
        for (SootMethod m : getMethods()) {
            if (m.getParameterCount() == paramCount && m.getName().equals(name)) {
                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(m);
            }
        }
        if (result == null) {
            return Collections.emptyList();
        }
        return result;
    }
}
