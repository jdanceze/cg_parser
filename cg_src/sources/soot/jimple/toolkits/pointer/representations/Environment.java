package soot.jimple.toolkits.pointer.representations;

import soot.G;
import soot.Singletons;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/representations/Environment.class */
public class Environment {
    private final ConstantObject clsloaders = new GeneralConstObject(TypeConstants.v().CLASSLOADERCLASS, "classloader");
    private final ConstantObject processes = new GeneralConstObject(TypeConstants.v().PROCESSCLASS, "process");
    private final ConstantObject threads = new GeneralConstObject(TypeConstants.v().THREADCLASS, "thread");
    private final ConstantObject filesystem = new GeneralConstObject(TypeConstants.v().FILESYSTEMCLASS, "filesystem");
    private final ConstantObject classobject = new GeneralConstObject(TypeConstants.v().CLASSCLASS, "unknownclass");
    private final ConstantObject stringobject = new GeneralConstObject(TypeConstants.v().STRINGCLASS, "unknownstring");
    private final ConstantObject fieldobject = new GeneralConstObject(TypeConstants.v().FIELDCLASS, "field");
    private final ConstantObject methodobject = new GeneralConstObject(TypeConstants.v().METHODCLASS, "method");
    private final ConstantObject constructorobject = new GeneralConstObject(TypeConstants.v().CONSTRUCTORCLASS, "constructor");
    private final ConstantObject privilegedActionException = new GeneralConstObject(TypeConstants.v().PRIVILEGEDACTIONEXCEPTION, "constructor");
    private final ConstantObject accessControlContext = new GeneralConstObject(TypeConstants.v().ACCESSCONTROLCONTEXT, "exception");
    private final ConstantObject arrayFields = new GeneralConstObject(TypeConstants.v().ARRAYFIELDS, "field");
    private final ConstantObject arrayMethods = new GeneralConstObject(TypeConstants.v().ARRAYMETHODS, "method");
    private final ConstantObject arrayConstructors = new GeneralConstObject(TypeConstants.v().ARRAYCONSTRUCTORS, "ctor");
    private final ConstantObject arrayClasses = new GeneralConstObject(TypeConstants.v().ARRAYCLASSES, "classes");

    public Environment(Singletons.Global g) {
    }

    public static Environment v() {
        return G.v().soot_jimple_toolkits_pointer_representations_Environment();
    }

    public ConstantObject getClassLoaderObject() {
        return this.clsloaders;
    }

    public ConstantObject getProcessObject() {
        return this.processes;
    }

    public ConstantObject getThreadObject() {
        return this.threads;
    }

    public ConstantObject getClassObject() {
        return this.classobject;
    }

    public ConstantObject getStringObject() {
        return this.stringobject;
    }

    public ConstantObject getFieldObject() {
        return this.fieldobject;
    }

    public ConstantObject getMethodObject() {
        return this.methodobject;
    }

    public ConstantObject getConstructorObject() {
        return this.constructorobject;
    }

    public ConstantObject getFileSystemObject() {
        return this.filesystem;
    }

    public ConstantObject getPrivilegedActionExceptionObject() {
        return this.privilegedActionException;
    }

    public ConstantObject getAccessControlContext() {
        return this.accessControlContext;
    }

    public ConstantObject getArrayConstructor() {
        return this.arrayConstructors;
    }

    public AbstractObject getArrayFields() {
        return this.arrayFields;
    }

    public AbstractObject getArrayMethods() {
        return this.arrayMethods;
    }

    public AbstractObject getArrayClasses() {
        return this.arrayClasses;
    }
}
