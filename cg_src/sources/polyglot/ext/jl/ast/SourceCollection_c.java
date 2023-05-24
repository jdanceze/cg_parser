package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Node;
import polyglot.ast.SourceCollection;
import polyglot.ast.SourceFile;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/SourceCollection_c.class */
public class SourceCollection_c extends Node_c implements SourceCollection {
    protected List sources;
    static Class class$polyglot$ast$SourceFile;

    public SourceCollection_c(Position pos, List sources) {
        super(pos);
        Class cls;
        if (class$polyglot$ast$SourceFile == null) {
            cls = class$("polyglot.ast.SourceFile");
            class$polyglot$ast$SourceFile = cls;
        } else {
            cls = class$polyglot$ast$SourceFile;
        }
        this.sources = TypedList.copyAndCheck(sources, cls, true);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return this.sources.toString();
    }

    @Override // polyglot.ast.SourceCollection
    public List sources() {
        return this.sources;
    }

    @Override // polyglot.ast.SourceCollection
    public SourceCollection sources(List sources) {
        Class cls;
        SourceCollection_c n = (SourceCollection_c) copy();
        if (class$polyglot$ast$SourceFile == null) {
            cls = class$("polyglot.ast.SourceFile");
            class$polyglot$ast$SourceFile = cls;
        } else {
            cls = class$polyglot$ast$SourceFile;
        }
        n.sources = TypedList.copyAndCheck(sources, cls, true);
        return n;
    }

    protected SourceCollection_c reconstruct(List sources) {
        Class cls;
        if (!CollectionUtil.equals(sources, this.sources)) {
            SourceCollection_c n = (SourceCollection_c) copy();
            if (class$polyglot$ast$SourceFile == null) {
                cls = class$("polyglot.ast.SourceFile");
                class$polyglot$ast$SourceFile = cls;
            } else {
                cls = class$polyglot$ast$SourceFile;
            }
            n.sources = TypedList.copyAndCheck(sources, cls, true);
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        List sources = visitList(this.sources, v);
        return reconstruct(sources);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        for (SourceFile s : this.sources) {
            print(s, w, tr);
            w.newline(0);
        }
    }
}
