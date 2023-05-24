package soot.asm;

import com.google.common.base.Optional;
import org.objectweb.asm.ModuleVisitor;
import soot.RefType;
import soot.SootClass;
import soot.SootModuleInfo;
import soot.SootModuleResolver;
/* loaded from: gencallgraphv3.jar:soot/asm/SootModuleInfoBuilder.class */
public class SootModuleInfoBuilder extends ModuleVisitor {
    private final SootClassBuilder scb;
    private final SootModuleInfo klass;
    private final String name;

    public SootModuleInfoBuilder(String name, SootModuleInfo klass, SootClassBuilder scb) {
        super(524288);
        this.klass = klass;
        this.name = name;
        this.scb = scb;
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitRequire(String module, int access, String version) {
        SootClass moduleInfo = SootModuleResolver.v().makeClassRef(SootModuleInfo.MODULE_INFO, Optional.of(module));
        this.klass.getRequiredModules().put((SootModuleInfo) moduleInfo, Integer.valueOf(access));
        this.scb.addDep(RefType.v(moduleInfo));
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitExport(String packaze, int access, String... modules) {
        if (packaze != null) {
            this.klass.addExportedPackage(packaze, modules);
        }
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitOpen(String packaze, int access, String... modules) {
        if (packaze != null) {
            this.klass.addOpenedPackage(packaze, modules);
        }
    }
}
