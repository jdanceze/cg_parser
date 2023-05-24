package polyglot.ast;

import polyglot.types.Package;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/PackageNode.class */
public interface PackageNode extends Node, Prefix, QualifierNode {
    Package package_();

    PackageNode package_(Package r1);
}
