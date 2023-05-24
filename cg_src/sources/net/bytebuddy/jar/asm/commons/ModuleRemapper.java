package net.bytebuddy.jar.asm.commons;

import net.bytebuddy.jar.asm.ModuleVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/jar/asm/commons/ModuleRemapper.class */
public class ModuleRemapper extends ModuleVisitor {
    protected final Remapper remapper;

    public ModuleRemapper(ModuleVisitor moduleVisitor, Remapper remapper) {
        this(524288, moduleVisitor, remapper);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ModuleRemapper(int api, ModuleVisitor moduleVisitor, Remapper remapper) {
        super(api, moduleVisitor);
        this.remapper = remapper;
    }

    @Override // net.bytebuddy.jar.asm.ModuleVisitor
    public void visitMainClass(String mainClass) {
        super.visitMainClass(this.remapper.mapType(mainClass));
    }

    @Override // net.bytebuddy.jar.asm.ModuleVisitor
    public void visitPackage(String packaze) {
        super.visitPackage(this.remapper.mapPackageName(packaze));
    }

    @Override // net.bytebuddy.jar.asm.ModuleVisitor
    public void visitRequire(String module, int access, String version) {
        super.visitRequire(this.remapper.mapModuleName(module), access, version);
    }

    @Override // net.bytebuddy.jar.asm.ModuleVisitor
    public void visitExport(String packaze, int access, String... modules) {
        String[] remappedModules = null;
        if (modules != null) {
            remappedModules = new String[modules.length];
            for (int i = 0; i < modules.length; i++) {
                remappedModules[i] = this.remapper.mapModuleName(modules[i]);
            }
        }
        super.visitExport(this.remapper.mapPackageName(packaze), access, remappedModules);
    }

    @Override // net.bytebuddy.jar.asm.ModuleVisitor
    public void visitOpen(String packaze, int access, String... modules) {
        String[] remappedModules = null;
        if (modules != null) {
            remappedModules = new String[modules.length];
            for (int i = 0; i < modules.length; i++) {
                remappedModules[i] = this.remapper.mapModuleName(modules[i]);
            }
        }
        super.visitOpen(this.remapper.mapPackageName(packaze), access, remappedModules);
    }

    @Override // net.bytebuddy.jar.asm.ModuleVisitor
    public void visitUse(String service) {
        super.visitUse(this.remapper.mapType(service));
    }

    @Override // net.bytebuddy.jar.asm.ModuleVisitor
    public void visitProvide(String service, String... providers) {
        String[] remappedProviders = new String[providers.length];
        for (int i = 0; i < providers.length; i++) {
            remappedProviders[i] = this.remapper.mapType(providers[i]);
        }
        super.visitProvide(this.remapper.mapType(service), remappedProviders);
    }
}
