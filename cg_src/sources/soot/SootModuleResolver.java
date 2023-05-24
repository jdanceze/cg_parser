package soot;

import com.google.common.base.Optional;
import soot.ModuleUtil;
import soot.Singletons;
import soot.SootResolver;
/* loaded from: gencallgraphv3.jar:soot/SootModuleResolver.class */
public class SootModuleResolver extends SootResolver {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SootModuleResolver.class.desiredAssertionStatus();
    }

    public SootModuleResolver(Singletons.Global g) {
        super(g);
    }

    public static SootModuleResolver v() {
        return G.v().soot_SootModuleResolver();
    }

    public SootClass makeClassRef(String className, Optional<String> moduleName) {
        SootClass newClass;
        String className2 = Scene.unescapeName(className);
        String module = null;
        if (moduleName.isPresent()) {
            module = ModuleUtil.v().declaringModule(className2, moduleName.get());
        }
        ModuleScene modScene = ModuleScene.v();
        if (modScene.containsClass(className2, Optional.fromNullable(module))) {
            return modScene.getSootClass(className2, Optional.fromNullable(module));
        }
        if (className2.endsWith(SootModuleInfo.MODULE_INFO)) {
            newClass = new SootModuleInfo(className2, module);
        } else {
            newClass = modScene.makeSootClass(className2, module);
        }
        newClass.setResolvingLevel(0);
        modScene.addClass(newClass);
        return newClass;
    }

    @Override // soot.SootResolver
    public SootClass makeClassRef(String className) {
        ModuleUtil.ModuleClassNameWrapper wrapper = ModuleUtil.v().makeWrapper(className);
        return makeClassRef(wrapper.getClassName(), wrapper.getModuleNameOptional());
    }

    public SootClass resolveClass(String className, int desiredLevel, Optional<String> moduleName) {
        SootClass resolvedClass = null;
        try {
            resolvedClass = makeClassRef(className, moduleName);
            addToResolveWorklist(resolvedClass, desiredLevel);
            processResolveWorklist();
            return resolvedClass;
        } catch (SootResolver.SootClassNotFoundException e) {
            if (resolvedClass != null) {
                if (!$assertionsDisabled && resolvedClass.resolvingLevel() != 0) {
                    throw new AssertionError();
                }
                ModuleScene.v().removeClass(resolvedClass);
            }
            throw e;
        }
    }

    @Override // soot.SootResolver
    public SootClass resolveClass(String className, int desiredLevel) {
        ModuleUtil.ModuleClassNameWrapper wrapper = ModuleUtil.v().makeWrapper(className);
        return resolveClass(wrapper.getClassName(), desiredLevel, wrapper.getModuleNameOptional());
    }
}
