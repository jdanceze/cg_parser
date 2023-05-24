package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import polyglot.ast.Import;
import polyglot.ast.Node;
import polyglot.ast.PackageNode;
import polyglot.ast.SourceFile;
import polyglot.ast.TopLevelDecl;
import polyglot.frontend.Source;
import polyglot.types.Context;
import polyglot.types.ImportTable;
import polyglot.types.Named;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/SourceFile_c.class */
public class SourceFile_c extends Node_c implements SourceFile {
    protected PackageNode package_;
    protected List imports;
    protected List decls;
    protected ImportTable importTable;
    protected Source source;
    static Class class$polyglot$ast$Import;
    static Class class$polyglot$ast$TopLevelDecl;

    public SourceFile_c(Position pos, PackageNode package_, List imports, List decls) {
        super(pos);
        Class cls;
        Class cls2;
        this.package_ = package_;
        if (class$polyglot$ast$Import == null) {
            cls = class$("polyglot.ast.Import");
            class$polyglot$ast$Import = cls;
        } else {
            cls = class$polyglot$ast$Import;
        }
        this.imports = TypedList.copyAndCheck(imports, cls, true);
        if (class$polyglot$ast$TopLevelDecl == null) {
            cls2 = class$("polyglot.ast.TopLevelDecl");
            class$polyglot$ast$TopLevelDecl = cls2;
        } else {
            cls2 = class$polyglot$ast$TopLevelDecl;
        }
        this.decls = TypedList.copyAndCheck(decls, cls2, true);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ast.SourceFile
    public Source source() {
        return this.source;
    }

    @Override // polyglot.ast.SourceFile
    public SourceFile source(Source source) {
        SourceFile_c n = (SourceFile_c) copy();
        n.source = source;
        return n;
    }

    @Override // polyglot.ast.SourceFile
    public PackageNode package_() {
        return this.package_;
    }

    @Override // polyglot.ast.SourceFile
    public SourceFile package_(PackageNode package_) {
        SourceFile_c n = (SourceFile_c) copy();
        n.package_ = package_;
        return n;
    }

    @Override // polyglot.ast.SourceFile
    public List imports() {
        return Collections.unmodifiableList(this.imports);
    }

    @Override // polyglot.ast.SourceFile
    public SourceFile imports(List imports) {
        Class cls;
        SourceFile_c n = (SourceFile_c) copy();
        if (class$polyglot$ast$Import == null) {
            cls = class$("polyglot.ast.Import");
            class$polyglot$ast$Import = cls;
        } else {
            cls = class$polyglot$ast$Import;
        }
        n.imports = TypedList.copyAndCheck(imports, cls, true);
        return n;
    }

    @Override // polyglot.ast.SourceFile
    public List decls() {
        return Collections.unmodifiableList(this.decls);
    }

    @Override // polyglot.ast.SourceFile
    public SourceFile decls(List decls) {
        Class cls;
        SourceFile_c n = (SourceFile_c) copy();
        if (class$polyglot$ast$TopLevelDecl == null) {
            cls = class$("polyglot.ast.TopLevelDecl");
            class$polyglot$ast$TopLevelDecl = cls;
        } else {
            cls = class$polyglot$ast$TopLevelDecl;
        }
        n.decls = TypedList.copyAndCheck(decls, cls, true);
        return n;
    }

    @Override // polyglot.ast.SourceFile
    public ImportTable importTable() {
        return this.importTable;
    }

    @Override // polyglot.ast.SourceFile
    public SourceFile importTable(ImportTable importTable) {
        SourceFile_c n = (SourceFile_c) copy();
        n.importTable = importTable;
        return n;
    }

    protected SourceFile_c reconstruct(PackageNode package_, List imports, List decls) {
        Class cls;
        Class cls2;
        if (package_ != this.package_ || !CollectionUtil.equals(imports, this.imports) || !CollectionUtil.equals(decls, this.decls)) {
            SourceFile_c n = (SourceFile_c) copy();
            n.package_ = package_;
            if (class$polyglot$ast$Import == null) {
                cls = class$("polyglot.ast.Import");
                class$polyglot$ast$Import = cls;
            } else {
                cls = class$polyglot$ast$Import;
            }
            n.imports = TypedList.copyAndCheck(imports, cls, true);
            if (class$polyglot$ast$TopLevelDecl == null) {
                cls2 = class$("polyglot.ast.TopLevelDecl");
                class$polyglot$ast$TopLevelDecl = cls2;
            } else {
                cls2 = class$polyglot$ast$TopLevelDecl;
            }
            n.decls = TypedList.copyAndCheck(decls, cls2, true);
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        PackageNode package_ = (PackageNode) visitChild(this.package_, v);
        List imports = visitList(this.imports, v);
        List decls = visitList(this.decls, v);
        return reconstruct(package_, imports, decls);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
        ImportTable it;
        TypeSystem ts = tb.typeSystem();
        if (this.package_ != null) {
            it = ts.importTable(this.source.name(), this.package_.package_());
        } else {
            it = ts.importTable(this.source.name(), null);
        }
        tb.setImportTable(it);
        return tb;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        ImportTable it = tb.importTable();
        tb.setImportTable(null);
        return importTable(it);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Context enterScope(Context c) {
        Context c2 = c.pushSource(this.importTable).pushBlock();
        for (TopLevelDecl d : this.decls) {
            Named n = d.declaration();
            c2.addNamed(n);
        }
        return c2;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        Set names = new HashSet();
        boolean hasPublic = false;
        for (TopLevelDecl d : this.decls) {
            String s = d.name();
            if (names.contains(s)) {
                throw new SemanticException(new StringBuffer().append("Duplicate declaration: \"").append(s).append("\".").toString(), d.position());
            }
            names.add(s);
            if (d.flags().isPublic()) {
                if (hasPublic) {
                    throw new SemanticException("The source contains more than one public declaration.", d.position());
                }
                hasPublic = true;
            }
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append("<<<< ").append(this.source).append(" >>>>").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write(new StringBuffer().append("<<<< ").append(this.source).append(" >>>>").toString());
        w.newline(0);
        if (this.package_ != null) {
            w.write("package ");
            print(this.package_, w, tr);
            w.write(";");
            w.newline(0);
            w.newline(0);
        }
        for (Import im : this.imports) {
            print(im, w, tr);
        }
        if (!this.imports.isEmpty()) {
            w.newline(0);
        }
        for (TopLevelDecl d : this.decls) {
            print(d, w, tr);
        }
    }
}
