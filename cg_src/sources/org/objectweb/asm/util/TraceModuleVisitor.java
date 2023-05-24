package org.objectweb.asm.util;

import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/TraceModuleVisitor.class */
public final class TraceModuleVisitor extends ModuleVisitor {
    public final Printer p;

    public TraceModuleVisitor(Printer printer) {
        this(null, printer);
    }

    public TraceModuleVisitor(ModuleVisitor moduleVisitor, Printer printer) {
        super(Opcodes.ASM9, moduleVisitor);
        this.p = printer;
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitMainClass(String mainClass) {
        this.p.visitMainClass(mainClass);
        super.visitMainClass(mainClass);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitPackage(String packaze) {
        this.p.visitPackage(packaze);
        super.visitPackage(packaze);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitRequire(String module, int access, String version) {
        this.p.visitRequire(module, access, version);
        super.visitRequire(module, access, version);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitExport(String packaze, int access, String... modules) {
        this.p.visitExport(packaze, access, modules);
        super.visitExport(packaze, access, modules);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitOpen(String packaze, int access, String... modules) {
        this.p.visitOpen(packaze, access, modules);
        super.visitOpen(packaze, access, modules);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitUse(String use) {
        this.p.visitUse(use);
        super.visitUse(use);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitProvide(String service, String... providers) {
        this.p.visitProvide(service, providers);
        super.visitProvide(service, providers);
    }

    @Override // org.objectweb.asm.ModuleVisitor
    public void visitEnd() {
        this.p.visitModuleEnd();
        super.visitEnd();
    }
}
