package org.objectweb.asm.tree;

import java.util.List;
import org.objectweb.asm.ModuleVisitor;
/* loaded from: gencallgraphv3.jar:asm-tree-9.4.jar:org/objectweb/asm/tree/ModuleProvideNode.class */
public class ModuleProvideNode {
    public String service;
    public List<String> providers;

    public ModuleProvideNode(String service, List<String> providers) {
        this.service = service;
        this.providers = providers;
    }

    public void accept(ModuleVisitor moduleVisitor) {
        moduleVisitor.visitProvide(this.service, (String[]) this.providers.toArray(new String[0]));
    }
}
