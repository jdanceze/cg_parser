package polyglot.ext.jl.ast;

import polyglot.ast.PackageNode;
import polyglot.types.Package;
import polyglot.types.Qualifier;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/PackageNode_c.class */
public class PackageNode_c extends Node_c implements PackageNode {
    protected Package package_;

    public PackageNode_c(Position pos, Package package_) {
        super(pos);
        this.package_ = package_;
    }

    @Override // polyglot.ast.QualifierNode
    public Qualifier qualifier() {
        return this.package_;
    }

    @Override // polyglot.ast.PackageNode
    public Package package_() {
        return this.package_;
    }

    @Override // polyglot.ast.PackageNode
    public PackageNode package_(Package package_) {
        PackageNode_c n = (PackageNode_c) copy();
        n.package_ = package_;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if (this.package_ == null) {
            w.write("<unknown-package>");
        } else {
            w.write(this.package_.toString());
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void translate(CodeWriter w, Translator tr) {
        w.write(tr.typeSystem().translatePackage(tr.context(), this.package_));
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return this.package_.toString();
    }
}
