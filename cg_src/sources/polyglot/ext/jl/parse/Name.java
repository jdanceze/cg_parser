package polyglot.ext.jl.parse;

import polyglot.ast.Expr;
import polyglot.ast.NodeFactory;
import polyglot.ast.PackageNode;
import polyglot.ast.Prefix;
import polyglot.ast.QualifierNode;
import polyglot.ast.Receiver;
import polyglot.ast.TypeNode;
import polyglot.parse.BaseParser;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/parse/Name.class */
public class Name {
    public final Name prefix;
    public final String name;
    public final Position pos;
    NodeFactory nf;
    TypeSystem ts;

    public Name(BaseParser parser, Position pos, String name) {
        this(parser, pos, null, name);
    }

    public Name(BaseParser parser, Position pos, Name prefix, String name) {
        this.nf = parser.nf;
        this.ts = parser.ts;
        this.pos = pos;
        this.prefix = prefix;
        this.name = name;
    }

    public Expr toExpr() {
        if (this.prefix == null) {
            return this.nf.AmbExpr(this.pos, this.name);
        }
        return this.nf.Field(this.pos, this.prefix.toReceiver(), this.name);
    }

    public Receiver toReceiver() {
        if (this.prefix == null) {
            return this.nf.AmbReceiver(this.pos, this.name);
        }
        return this.nf.AmbReceiver(this.pos, this.prefix.toPrefix(), this.name);
    }

    public Prefix toPrefix() {
        if (this.prefix == null) {
            return this.nf.AmbPrefix(this.pos, this.name);
        }
        return this.nf.AmbPrefix(this.pos, this.prefix.toPrefix(), this.name);
    }

    public QualifierNode toQualifier() {
        if (this.prefix == null) {
            return this.nf.AmbQualifierNode(this.pos, this.name);
        }
        return this.nf.AmbQualifierNode(this.pos, this.prefix.toQualifier(), this.name);
    }

    public PackageNode toPackage() {
        if (this.prefix == null) {
            return this.nf.PackageNode(this.pos, this.ts.createPackage(null, this.name));
        }
        return this.nf.PackageNode(this.pos, this.ts.createPackage(this.prefix.toPackage().package_(), this.name));
    }

    public TypeNode toType() {
        if (this.prefix == null) {
            return this.nf.AmbTypeNode(this.pos, this.name);
        }
        return this.nf.AmbTypeNode(this.pos, this.prefix.toQualifier(), this.name);
    }

    public String toString() {
        if (this.prefix == null) {
            return this.name;
        }
        return new StringBuffer().append(this.prefix.toString()).append(".").append(this.name).toString();
    }
}
