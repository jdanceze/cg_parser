package soot.jimple.toolkits.pointer.representations;

import soot.AnySubType;
import soot.ArrayType;
import soot.G;
import soot.PhaseOptions;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.Type;
import soot.options.CGOptions;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/representations/TypeConstants.class */
public class TypeConstants {
    public Type OBJECTCLASS;
    public Type STRINGCLASS;
    public Type CLASSLOADERCLASS;
    public Type PROCESSCLASS;
    public Type THREADCLASS;
    public Type CLASSCLASS;
    public Type FIELDCLASS;
    public Type METHODCLASS;
    public Type CONSTRUCTORCLASS;
    public Type FILESYSTEMCLASS;
    public Type PRIVILEGEDACTIONEXCEPTION;
    public Type ACCESSCONTROLCONTEXT;
    public Type ARRAYFIELDS;
    public Type ARRAYMETHODS;
    public Type ARRAYCONSTRUCTORS;
    public Type ARRAYCLASSES;

    public static TypeConstants v() {
        return G.v().soot_jimple_toolkits_pointer_representations_TypeConstants();
    }

    public TypeConstants(Singletons.Global g) {
        int jdkver = new CGOptions(PhaseOptions.v().getPhaseOptions("cg")).jdkver();
        this.OBJECTCLASS = Scene.v().getObjectType();
        this.STRINGCLASS = RefType.v("java.lang.String");
        this.CLASSLOADERCLASS = AnySubType.v(RefType.v("java.lang.ClassLoader"));
        this.PROCESSCLASS = AnySubType.v(RefType.v("java.lang.Process"));
        this.THREADCLASS = AnySubType.v(RefType.v("java.lang.Thread"));
        this.CLASSCLASS = RefType.v("java.lang.Class");
        this.FIELDCLASS = RefType.v("java.lang.reflect.Field");
        this.METHODCLASS = RefType.v("java.lang.reflect.Method");
        this.CONSTRUCTORCLASS = RefType.v("java.lang.reflect.Constructor");
        if (jdkver >= 2) {
            this.FILESYSTEMCLASS = AnySubType.v(RefType.v("java.io.FileSystem"));
        }
        if (jdkver >= 2) {
            this.PRIVILEGEDACTIONEXCEPTION = AnySubType.v(RefType.v("java.security.PrivilegedActionException"));
            this.ACCESSCONTROLCONTEXT = RefType.v("java.security.AccessControlContext");
        }
        this.ARRAYFIELDS = ArrayType.v(RefType.v("java.lang.reflect.Field"), 1);
        this.ARRAYMETHODS = ArrayType.v(RefType.v("java.lang.reflect.Method"), 1);
        this.ARRAYCONSTRUCTORS = ArrayType.v(RefType.v("java.lang.reflect.Constructor"), 1);
        this.ARRAYCLASSES = ArrayType.v(RefType.v("java.lang.Class"), 1);
    }
}
