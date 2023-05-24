package soot;

import com.google.common.base.Optional;
import java.util.ArrayDeque;
import soot.Singletons;
import soot.dotnet.types.DotnetBasicTypes;
import soot.options.Options;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/RefType.class */
public class RefType extends RefLikeType implements Comparable<RefType> {
    private String className;
    private AnySubType anySubType;
    protected volatile SootClass sootClass;

    public RefType(Singletons.Global g) {
        this.className = "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RefType(String className) {
        if (!className.isEmpty()) {
            if (className.charAt(0) == '[') {
                throw new RuntimeException("Attempt to create RefType whose name starts with [ --> " + className);
            }
            if (className.indexOf(47) >= 0) {
                throw new RuntimeException("Attempt to create RefType containing a / --> " + className);
            }
            if (className.indexOf(59) >= 0) {
                throw new RuntimeException("Attempt to create RefType containing a ; --> " + className);
            }
        }
        this.className = className;
    }

    public static RefType v() {
        if (ModuleUtil.module_mode()) {
            return G.v().soot_ModuleRefType();
        }
        return G.v().soot_RefType();
    }

    public static RefType v(String className) {
        if (ModuleUtil.module_mode()) {
            return ModuleRefType.v(className);
        }
        return Scene.v().getOrAddRefType(className);
    }

    public static RefType v(SootClass c) {
        if (ModuleUtil.module_mode()) {
            return ModuleRefType.v(c.getName(), Optional.fromNullable(c.moduleName));
        }
        return v(c.getName());
    }

    public String getClassName() {
        return this.className;
    }

    @Override // java.lang.Comparable
    public int compareTo(RefType t) {
        return toString().compareTo(t.toString());
    }

    public SootClass getSootClass() {
        if (this.sootClass == null) {
            this.sootClass = SootResolver.v().makeClassRef(this.className);
        }
        return this.sootClass;
    }

    public boolean hasSootClass() {
        return this.sootClass != null;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setSootClass(SootClass sootClass) {
        this.sootClass = sootClass;
    }

    public boolean equals(Object t) {
        return (t instanceof RefType) && this.className.equals(((RefType) t).className);
    }

    @Override // soot.Type
    public String toString() {
        return this.className;
    }

    @Override // soot.Type
    public String toQuotedString() {
        return Scene.v().quotedNameOf(this.className);
    }

    public int hashCode() {
        return this.className.hashCode();
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseRefType(this);
    }

    @Override // soot.Type
    public Type merge(Type other, Scene cm) {
        if (other.equals(UnknownType.v()) || equals(other)) {
            return this;
        }
        if (!(other instanceof RefType)) {
            throw new RuntimeException("illegal type merge: " + this + " and " + other);
        }
        SootClass javalangObject = cm.getObjectType().getSootClass();
        ArrayDeque<SootClass> thisHierarchy = new ArrayDeque<>();
        ArrayDeque<SootClass> otherHierarchy = new ArrayDeque<>();
        SootClass sc = cm.getSootClass(this.className);
        while (sc != null) {
            thisHierarchy.addFirst(sc);
            if (sc == javalangObject) {
                break;
            }
            sc = sc.getSuperclassUnsafe();
            if (sc == null) {
                sc = javalangObject;
            }
        }
        SootClass sc2 = cm.getSootClass(((RefType) other).className);
        while (sc2 != null) {
            otherHierarchy.addFirst(sc2);
            if (sc2 == javalangObject) {
                break;
            }
            sc2 = sc2.getSuperclassUnsafe();
            if (sc2 == null) {
                sc2 = javalangObject;
            }
        }
        SootClass commonClass = null;
        while (!otherHierarchy.isEmpty() && !thisHierarchy.isEmpty() && otherHierarchy.getFirst() == thisHierarchy.getFirst()) {
            commonClass = otherHierarchy.removeFirst();
            thisHierarchy.removeFirst();
        }
        if (commonClass == null) {
            throw new RuntimeException("Could not find a common superclass for " + this + " and " + other);
        }
        return commonClass.getType();
    }

    @Override // soot.RefLikeType
    public Type getArrayElementType() {
        if (Options.v().src_prec() == 7 && (DotnetBasicTypes.SYSTEM_OBJECT.equals(this.className) || DotnetBasicTypes.SYSTEM_ICLONEABLE.equals(this.className))) {
            return Scene.v().getObjectType();
        }
        if (JavaBasicTypes.JAVA_LANG_OBJECT.equals(this.className) || JavaBasicTypes.JAVA_IO_SERIALIZABLE.equals(this.className) || JavaBasicTypes.JAVA_LANG_CLONABLE.equals(this.className)) {
            return Scene.v().getObjectType();
        }
        throw new RuntimeException("Attempt to get array base type of a non-array");
    }

    public AnySubType getAnySubType() {
        return this.anySubType;
    }

    public void setAnySubType(AnySubType anySubType) {
        this.anySubType = anySubType;
    }

    @Override // soot.Type
    public boolean isAllowedInFinalCode() {
        return true;
    }
}
