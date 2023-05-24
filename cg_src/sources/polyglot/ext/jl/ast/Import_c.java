package polyglot.ext.jl.ast;

import polyglot.ast.Import;
import polyglot.ast.Node;
import polyglot.main.Options;
import polyglot.types.ImportTable;
import polyglot.types.Named;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.util.StringUtil;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Import_c.class */
public class Import_c extends Node_c implements Import {
    protected Import.Kind kind;
    protected String name;

    public Import_c(Position pos, Import.Kind kind, String name) {
        super(pos);
        this.name = name;
        this.kind = kind;
    }

    @Override // polyglot.ast.Import
    public String name() {
        return this.name;
    }

    @Override // polyglot.ast.Import
    public Import name(String name) {
        Import_c n = (Import_c) copy();
        n.name = name;
        return n;
    }

    @Override // polyglot.ast.Import
    public Import.Kind kind() {
        return this.kind;
    }

    @Override // polyglot.ast.Import
    public Import kind(Import.Kind kind) {
        Import_c n = (Import_c) copy();
        n.kind = kind;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        ImportTable it = tb.importTable();
        if (this.kind == Import.CLASS) {
            it.addClassImport(this.name);
        } else if (this.kind == Import.PACKAGE) {
            it.addPackageImport(this.name);
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        if (this.kind == Import.PACKAGE && tc.typeSystem().packageExists(this.name)) {
            return this;
        }
        String pkgName = StringUtil.getFirstComponent(this.name);
        if (!tc.typeSystem().packageExists(pkgName)) {
            throw new SemanticException(new StringBuffer().append("Package \"").append(pkgName).append("\" not found.").toString(), position());
        }
        Named nt = tc.typeSystem().forName(this.name);
        if (nt instanceof Type) {
            Type t = (Type) nt;
            if (t.isClass()) {
                tc.typeSystem().classAccessibleFromPackage(t.toClass(), tc.context().package_());
            }
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append("import ").append(this.name).append(this.kind == Import.PACKAGE ? ".*" : "").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if (!Options.global.fully_qualified_names) {
            w.write("import ");
            w.write(this.name);
            if (this.kind == Import.PACKAGE) {
                w.write(".*");
            }
            w.write(";");
            w.newline(0);
        }
    }
}
