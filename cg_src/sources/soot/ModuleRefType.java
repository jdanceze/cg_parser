package soot;

import com.google.common.base.Optional;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ModuleUtil;
import soot.Singletons;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/ModuleRefType.class */
public class ModuleRefType extends RefType {
    private static final Logger logger = LoggerFactory.getLogger(ModuleRefType.class);
    private String moduleName;

    public ModuleRefType(Singletons.Global g) {
        super(g);
    }

    protected ModuleRefType(String className, String moduleName) {
        super(className);
        this.moduleName = moduleName;
    }

    public static RefType v(String className) {
        ModuleUtil.ModuleClassNameWrapper wrapper = ModuleUtil.v().makeWrapper(className);
        return v(wrapper.getClassName(), wrapper.getModuleNameOptional());
    }

    public static RefType v(String className, Optional<String> moduleName) {
        boolean isPresent = moduleName.isPresent();
        String module = isPresent ? ModuleUtil.v().declaringModule(className, moduleName.get()) : null;
        if (!isPresent && Options.v().verbose()) {
            logger.warn("ModuleRefType called with empty module for: " + className);
        }
        RefType rt = ModuleScene.v().getRefTypeUnsafe(className, Optional.fromNullable(module));
        if (rt == null) {
            rt = new ModuleRefType(className, isPresent ? module : null);
            ModuleScene.v().addRefType(rt);
        }
        return rt;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    @Override // soot.RefType
    public SootClass getSootClass() {
        if (this.sootClass == null) {
            super.setSootClass(SootModuleResolver.v().makeClassRef(getClassName(), Optional.fromNullable(this.moduleName)));
        }
        return super.getSootClass();
    }

    @Override // soot.RefType, soot.Type
    public Type merge(Type other, Scene cm) {
        if (UnknownType.v().equals(other) || equals(other)) {
            return this;
        }
        if (!(other instanceof RefType)) {
            throw new RuntimeException("illegal type merge: " + this + " and " + other);
        }
        ModuleScene cmMod = (ModuleScene) cm;
        SootClass javalangObject = cm.getObjectType().getSootClass();
        LinkedList<SootClass> thisHierarchy = new LinkedList<>();
        LinkedList<SootClass> otherHierarchy = new LinkedList<>();
        SootClass sootClass = cmMod.getSootClass(getClassName(), Optional.fromNullable(this.moduleName));
        while (true) {
            SootClass sc = sootClass;
            thisHierarchy.addFirst(sc);
            if (sc == javalangObject) {
                break;
            }
            sootClass = sc.hasSuperclass() ? sc.getSuperclass() : javalangObject;
        }
        SootClass sootClass2 = cmMod.getSootClass(((RefType) other).getClassName(), Optional.fromNullable(this.moduleName));
        while (true) {
            SootClass sc2 = sootClass2;
            otherHierarchy.addFirst(sc2);
            if (sc2 == javalangObject) {
                break;
            }
            sootClass2 = sc2.hasSuperclass() ? sc2.getSuperclass() : javalangObject;
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

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0072, code lost:
        if (r0.equals(soot.JavaBasicTypes.JAVA_LANG_OBJECT) == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x007e, code lost:
        if (r0.equals(soot.JavaBasicTypes.JAVA_IO_SERIALIZABLE) == false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x008e, code lost:
        return v(soot.JavaBasicTypes.JAVA_LANG_OBJECT, com.google.common.base.Optional.of("java.base"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x005a, code lost:
        if (r0.equals("java.lang.Cloneable") == false) goto L20;
     */
    @Override // soot.RefType, soot.RefLikeType
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public soot.Type getArrayElementType() {
        /*
            r4 = this;
            r0 = r4
            java.lang.String r0 = r0.getClassName()
            soot.Scene r1 = soot.Scene.v()
            soot.RefType r1 = r1.getObjectType()
            java.lang.String r1 = r1.toString()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L20
            soot.Scene r0 = soot.Scene.v()
            soot.RefType r0 = r0.getObjectType()
            java.lang.String r0 = r0.toString()
            soot.RefType r0 = v(r0)
            return r0
        L20:
            r0 = r4
            java.lang.String r0 = r0.getClassName()
            r1 = r0
            r5 = r1
            int r0 = r0.hashCode()
            switch(r0) {
                case -2034166429: goto L54;
                case -1630038566: goto L60;
                case 1063877011: goto L6c;
                case 1832181019: goto L78;
                default: goto L96;
            }
        L54:
            r0 = r5
            java.lang.String r1 = "java.lang.Cloneable"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L84
            goto L96
        L60:
            r0 = r5
            java.lang.String r1 = "System.Array"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L8f
            goto L96
        L6c:
            r0 = r5
            java.lang.String r1 = "java.lang.Object"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L84
            goto L96
        L78:
            r0 = r5
            java.lang.String r1 = "java.io.Serializable"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L84
            goto L96
        L84:
            java.lang.String r0 = "java.lang.Object"
            java.lang.String r1 = "java.base"
            com.google.common.base.Optional r1 = com.google.common.base.Optional.of(r1)
            soot.RefType r0 = v(r0, r1)
            return r0
        L8f:
            java.lang.String r0 = "System.Object"
            soot.RefType r0 = v(r0)
            return r0
        L96:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            r1 = r0
            java.lang.String r2 = "Attempt to get array base type of a non-array"
            r1.<init>(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.ModuleRefType.getArrayElementType():soot.Type");
    }
}
