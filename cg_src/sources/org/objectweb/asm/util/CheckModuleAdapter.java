package org.objectweb.asm.util;

import java.util.HashSet;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/CheckModuleAdapter.class */
public class CheckModuleAdapter extends ModuleVisitor {
    private final boolean isOpen;
    private final NameSet requiredModules;
    private final NameSet exportedPackages;
    private final NameSet openedPackages;
    private final NameSet usedServices;
    private final NameSet providedServices;
    int classVersion;
    private boolean visitEndCalled;

    public CheckModuleAdapter(ModuleVisitor moduleVisitor, boolean isOpen) {
        this(Opcodes.ASM9, moduleVisitor, isOpen);
        if (getClass() != CheckModuleAdapter.class) {
            throw new IllegalStateException();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CheckModuleAdapter(int api, ModuleVisitor moduleVisitor, boolean isOpen) {
        super(api, moduleVisitor);
        this.requiredModules = new NameSet("Modules requires");
        this.exportedPackages = new NameSet("Module exports");
        this.openedPackages = new NameSet("Module opens");
        this.usedServices = new NameSet("Module uses");
        this.providedServices = new NameSet("Module provides");
        this.isOpen = isOpen;
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitMainClass(String mainClass) {
        CheckMethodAdapter.checkInternalName(53, mainClass, "module main class");
        super.visitMainClass(mainClass);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitPackage(String packaze) {
        CheckMethodAdapter.checkInternalName(53, packaze, "module package");
        super.visitPackage(packaze);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitRequire(String module, int access, String version) {
        checkVisitEndNotCalled();
        CheckClassAdapter.checkFullyQualifiedName(53, module, "required module");
        this.requiredModules.checkNameNotAlreadyDeclared(module);
        CheckClassAdapter.checkAccess(access, 36960);
        if (this.classVersion >= 54 && module.equals("java.base") && (access & 96) != 0) {
            throw new IllegalArgumentException("Invalid access flags: " + access + " java.base can not be declared ACC_TRANSITIVE or ACC_STATIC_PHASE");
        }
        super.visitRequire(module, access, version);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitExport(String packaze, int access, String... modules) {
        checkVisitEndNotCalled();
        CheckMethodAdapter.checkInternalName(53, packaze, "package name");
        this.exportedPackages.checkNameNotAlreadyDeclared(packaze);
        CheckClassAdapter.checkAccess(access, 36864);
        if (modules != null) {
            for (String module : modules) {
                CheckClassAdapter.checkFullyQualifiedName(53, module, "module export to");
            }
        }
        super.visitExport(packaze, access, modules);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitOpen(String packaze, int access, String... modules) {
        checkVisitEndNotCalled();
        if (this.isOpen) {
            throw new UnsupportedOperationException("An open module can not use open directive");
        }
        CheckMethodAdapter.checkInternalName(53, packaze, "package name");
        this.openedPackages.checkNameNotAlreadyDeclared(packaze);
        CheckClassAdapter.checkAccess(access, 36864);
        if (modules != null) {
            for (String module : modules) {
                CheckClassAdapter.checkFullyQualifiedName(53, module, "module open to");
            }
        }
        super.visitOpen(packaze, access, modules);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitUse(String service) {
        checkVisitEndNotCalled();
        CheckMethodAdapter.checkInternalName(53, service, "service");
        this.usedServices.checkNameNotAlreadyDeclared(service);
        super.visitUse(service);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitProvide(String service, String... providers) {
        checkVisitEndNotCalled();
        CheckMethodAdapter.checkInternalName(53, service, "service");
        this.providedServices.checkNameNotAlreadyDeclared(service);
        if (providers == null || providers.length == 0) {
            throw new IllegalArgumentException("Providers cannot be null or empty");
        }
        for (String provider : providers) {
            CheckMethodAdapter.checkInternalName(53, provider, "provider");
        }
        super.visitProvide(service, providers);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitEnd() {
        checkVisitEndNotCalled();
        this.visitEndCalled = true;
        super.visitEnd();
    }

    private void checkVisitEndNotCalled() {
        if (this.visitEndCalled) {
            throw new IllegalStateException("Cannot call a visit method after visitEnd has been called");
        }
    }

    /* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/CheckModuleAdapter$NameSet.class */
    private static class NameSet {
        private final String type;
        private final HashSet<String> names = new HashSet<>();

        NameSet(String type) {
            this.type = type;
        }

        void checkNameNotAlreadyDeclared(String name) {
            if (!this.names.add(name)) {
                throw new IllegalArgumentException(this.type + " '" + name + "' already declared");
            }
        }
    }
}
