package org.objectweb.asm.tree;

import java.util.List;
import org.objectweb.asm.ModuleVisitor;
/* loaded from: gencallgraphv3.jar:asm-tree-9.4.jar:org/objectweb/asm/tree/ModuleOpenNode.class */
public class ModuleOpenNode {
    public String packaze;
    public int access;
    public List<String> modules;

    public ModuleOpenNode(String packaze, int access, List<String> modules) {
        this.packaze = packaze;
        this.access = access;
        this.modules = modules;
    }

    public void accept(ModuleVisitor moduleVisitor) {
        moduleVisitor.visitOpen(this.packaze, this.access, this.modules == null ? null : (String[]) this.modules.toArray(new String[0]));
    }
}
