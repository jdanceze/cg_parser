package polyglot.ext.jl.qq;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java_cup.runtime.Symbol;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.SourceFile;
import polyglot.ast.Stmt;
import polyglot.ast.TypeNode;
import polyglot.ext.jl.Topics;
import polyglot.frontend.ExtensionInfo;
import polyglot.lex.Lexer;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/qq/QQ.class */
public class QQ {
    protected ExtensionInfo ext;
    protected Position pos;
    protected static final int EXPR = 0;
    protected static final int STMT = 1;
    protected static final int TYPE = 2;
    protected static final int MEMB = 3;
    protected static final int DECL = 4;
    protected static final int FILE = 5;

    public QQ(ExtensionInfo ext) {
        this(ext, Position.COMPILER_GENERATED);
    }

    public QQ(ExtensionInfo ext, Position pos) {
        this.ext = ext;
        this.pos = pos;
    }

    private List list() {
        return Collections.EMPTY_LIST;
    }

    private List list(Object o1) {
        return list(new Object[]{o1});
    }

    private List list(Object o1, Object o2) {
        return list(new Object[]{o1, o2});
    }

    private List list(Object o1, Object o2, Object o3) {
        return list(new Object[]{o1, o2, o3});
    }

    private List list(Object o1, Object o2, Object o3, Object o4) {
        return list(new Object[]{o1, o2, o3, o4});
    }

    private List list(Object o1, Object o2, Object o3, Object o4, Object o5) {
        return list(new Object[]{o1, o2, o3, o4, o5});
    }

    private List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        return list(new Object[]{o1, o2, o3, o4, o5, o6});
    }

    private List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        return list(new Object[]{o1, o2, o3, o4, o5, o6, o7});
    }

    private List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
        return list(new Object[]{o1, o2, o3, o4, o5, o6, o7, o8});
    }

    private List list(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
        return list(new Object[]{o1, o2, o3, o4, o5, o6, o7, o8, o9});
    }

    private List list(Object[] os) {
        return Arrays.asList(os);
    }

    public SourceFile parseFile(String fmt) {
        return (SourceFile) parse(fmt, list(), 5);
    }

    public SourceFile parseFile(String fmt, Object o1) {
        return (SourceFile) parse(fmt, list(o1), 5);
    }

    public SourceFile parseFile(String fmt, Object o1, Object o2) {
        return (SourceFile) parse(fmt, list(o1, o2), 5);
    }

    public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3) {
        return (SourceFile) parse(fmt, list(o1, o2, o3), 5);
    }

    public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3, Object o4) {
        return (SourceFile) parse(fmt, list(o1, o2, o3, o4), 5);
    }

    public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5) {
        return (SourceFile) parse(fmt, list(o1, o2, o3, o4, o5), 5);
    }

    public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        return (SourceFile) parse(fmt, list(o1, o2, o3, o4, o5, o6), 5);
    }

    public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        return (SourceFile) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7), 5);
    }

    public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
        return (SourceFile) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7, o8), 5);
    }

    public SourceFile parseFile(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
        return (SourceFile) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7, o8, o9), 5);
    }

    public SourceFile parseFile(String fmt, Object[] os) {
        return (SourceFile) parse(fmt, list(os), 5);
    }

    public SourceFile parseFile(String fmt, List subst) {
        return (SourceFile) parse(fmt, subst, 5);
    }

    public ClassDecl parseDecl(String fmt) {
        return (ClassDecl) parse(fmt, list(), 4);
    }

    public ClassDecl parseDecl(String fmt, Object o1) {
        return (ClassDecl) parse(fmt, list(o1), 4);
    }

    public ClassDecl parseDecl(String fmt, Object o1, Object o2) {
        return (ClassDecl) parse(fmt, list(o1, o2), 4);
    }

    public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3) {
        return (ClassDecl) parse(fmt, list(o1, o2, o3), 4);
    }

    public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3, Object o4) {
        return (ClassDecl) parse(fmt, list(o1, o2, o3, o4), 4);
    }

    public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5) {
        return (ClassDecl) parse(fmt, list(o1, o2, o3, o4, o5), 4);
    }

    public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        return (ClassDecl) parse(fmt, list(o1, o2, o3, o4, o5, o6), 4);
    }

    public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        return (ClassDecl) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7), 4);
    }

    public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
        return (ClassDecl) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7, o8), 4);
    }

    public ClassDecl parseDecl(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
        return (ClassDecl) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7, o8, o9), 4);
    }

    public ClassDecl parseDecl(String fmt, Object[] os) {
        return (ClassDecl) parse(fmt, list(os), 4);
    }

    public ClassDecl parseDecl(String fmt, List subst) {
        return (ClassDecl) parse(fmt, subst, 4);
    }

    public ClassMember parseMember(String fmt) {
        return (ClassMember) parse(fmt, list(), 3);
    }

    public ClassMember parseMember(String fmt, Object o1) {
        return (ClassMember) parse(fmt, list(o1), 3);
    }

    public ClassMember parseMember(String fmt, Object o1, Object o2) {
        return (ClassMember) parse(fmt, list(o1, o2), 3);
    }

    public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3) {
        return (ClassMember) parse(fmt, list(o1, o2, o3), 3);
    }

    public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3, Object o4) {
        return (ClassMember) parse(fmt, list(o1, o2, o3, o4), 3);
    }

    public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5) {
        return (ClassMember) parse(fmt, list(o1, o2, o3, o4, o5), 3);
    }

    public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        return (ClassMember) parse(fmt, list(o1, o2, o3, o4, o5, o6), 3);
    }

    public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        return (ClassMember) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7), 3);
    }

    public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
        return (ClassMember) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7, o8), 3);
    }

    public ClassMember parseMember(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
        return (ClassMember) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7, o8, o9), 3);
    }

    public ClassMember parseMember(String fmt, Object[] os) {
        return (ClassMember) parse(fmt, list(os), 3);
    }

    public ClassMember parseMember(String fmt, List subst) {
        return (ClassMember) parse(fmt, subst, 3);
    }

    public Expr parseExpr(String fmt) {
        return (Expr) parse(fmt, list(), 0);
    }

    public Expr parseExpr(String fmt, Object o1) {
        return (Expr) parse(fmt, list(o1), 0);
    }

    public Expr parseExpr(String fmt, Object o1, Object o2) {
        return (Expr) parse(fmt, list(o1, o2), 0);
    }

    public Expr parseExpr(String fmt, Object o1, Object o2, Object o3) {
        return (Expr) parse(fmt, list(o1, o2, o3), 0);
    }

    public Expr parseExpr(String fmt, Object o1, Object o2, Object o3, Object o4) {
        return (Expr) parse(fmt, list(o1, o2, o3, o4), 0);
    }

    public Expr parseExpr(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5) {
        return (Expr) parse(fmt, list(o1, o2, o3, o4, o5), 0);
    }

    public Expr parseExpr(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        return (Expr) parse(fmt, list(o1, o2, o3, o4, o5, o6), 0);
    }

    public Expr parseExpr(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        return (Expr) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7), 0);
    }

    public Expr parseExpr(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
        return (Expr) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7, o8), 0);
    }

    public Expr parseExpr(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
        return (Expr) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7, o8, o9), 0);
    }

    public Expr parseExpr(String fmt, Object[] os) {
        return (Expr) parse(fmt, list(os), 0);
    }

    public Expr parseExpr(String fmt, List subst) {
        return (Expr) parse(fmt, subst, 0);
    }

    public Stmt parseStmt(String fmt) {
        return (Stmt) parse(fmt, list(), 1);
    }

    public Stmt parseStmt(String fmt, Object o1) {
        return (Stmt) parse(fmt, list(o1), 1);
    }

    public Stmt parseStmt(String fmt, Object o1, Object o2) {
        return (Stmt) parse(fmt, list(o1, o2), 1);
    }

    public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3) {
        return (Stmt) parse(fmt, list(o1, o2, o3), 1);
    }

    public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3, Object o4) {
        return (Stmt) parse(fmt, list(o1, o2, o3, o4), 1);
    }

    public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5) {
        return (Stmt) parse(fmt, list(o1, o2, o3, o4, o5), 1);
    }

    public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        return (Stmt) parse(fmt, list(o1, o2, o3, o4, o5, o6), 1);
    }

    public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        return (Stmt) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7), 1);
    }

    public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
        return (Stmt) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7, o8), 1);
    }

    public Stmt parseStmt(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
        return (Stmt) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7, o8, o9), 1);
    }

    public Stmt parseStmt(String fmt, Object[] os) {
        return (Stmt) parse(fmt, list(os), 1);
    }

    public Stmt parseStmt(String fmt, List subst) {
        return (Stmt) parse(fmt, subst, 1);
    }

    public TypeNode parseType(String fmt) {
        return (TypeNode) parse(fmt, list(), 2);
    }

    public TypeNode parseType(String fmt, Object o1) {
        return (TypeNode) parse(fmt, list(o1), 2);
    }

    public TypeNode parseType(String fmt, Object o1, Object o2) {
        return (TypeNode) parse(fmt, list(o1, o2), 2);
    }

    public TypeNode parseType(String fmt, Object o1, Object o2, Object o3) {
        return (TypeNode) parse(fmt, list(o1, o2, o3), 2);
    }

    public TypeNode parseType(String fmt, Object o1, Object o2, Object o3, Object o4) {
        return (TypeNode) parse(fmt, list(o1, o2, o3, o4), 2);
    }

    public TypeNode parseType(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5) {
        return (TypeNode) parse(fmt, list(o1, o2, o3, o4, o5), 2);
    }

    public TypeNode parseType(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        return (TypeNode) parse(fmt, list(o1, o2, o3, o4, o5, o6), 2);
    }

    public TypeNode parseType(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        return (TypeNode) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7), 2);
    }

    public TypeNode parseType(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
        return (TypeNode) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7, o8), 2);
    }

    public TypeNode parseType(String fmt, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9) {
        return (TypeNode) parse(fmt, list(o1, o2, o3, o4, o5, o6, o7, o8, o9), 2);
    }

    public TypeNode parseType(String fmt, Object[] os) {
        return (TypeNode) parse(fmt, list(os), 2);
    }

    public TypeNode parseType(String fmt, List subst) {
        return (TypeNode) parse(fmt, subst, 2);
    }

    protected Lexer lexer(String fmt, Position pos, List subst) {
        return new Lexer_c(fmt, pos, subst);
    }

    protected QQParser parser(Lexer lexer, TypeSystem ts, NodeFactory nf, ErrorQueue eq) {
        return new Grm(lexer, ts, nf, eq);
    }

    protected Node parse(String fmt, List subst, int kind) {
        Symbol qq_file;
        TypeSystem ts = this.ext.typeSystem();
        NodeFactory nf = this.ext.nodeFactory();
        ErrorQueue eq = this.ext.compiler().errorQueue();
        ListIterator i = subst.listIterator();
        while (i.hasNext()) {
            Object o = i.next();
            if (o instanceof Type) {
                Type t = (Type) o;
                i.set(nf.CanonicalTypeNode(t.position(), t));
            } else if (o instanceof List) {
                List l = (List) o;
                ListIterator j = l.listIterator();
                while (j.hasNext()) {
                    Object p = j.next();
                    if (p instanceof Type) {
                        Type t2 = (Type) p;
                        j.set(nf.CanonicalTypeNode(t2.position(), t2));
                    }
                }
            }
        }
        Position pos = this.pos;
        if (pos == Position.COMPILER_GENERATED) {
            pos = Position.compilerGenerated(3);
        }
        Lexer lexer = lexer(fmt, pos, subst);
        QQParser grm = parser(lexer, ts, nf, eq);
        if (Report.should_report(Topics.qq, 1)) {
            Report.report(1, new StringBuffer().append("qq: ").append(fmt).toString());
            Report.report(1, new StringBuffer().append("subst: ").append(subst).toString());
        }
        try {
            switch (kind) {
                case 0:
                    qq_file = grm.qq_expr();
                    break;
                case 1:
                    qq_file = grm.qq_stmt();
                    break;
                case 2:
                    qq_file = grm.qq_type();
                    break;
                case 3:
                    qq_file = grm.qq_member();
                    break;
                case 4:
                    qq_file = grm.qq_decl();
                    break;
                case 5:
                    qq_file = grm.qq_file();
                    break;
                default:
                    throw new QQError(new StringBuffer().append("bad quasi-quoting kind: ").append(kind).toString(), pos);
            }
            if (qq_file != null && (qq_file.value instanceof Node)) {
                Node n = (Node) qq_file.value;
                if (Report.should_report(Topics.qq, 1)) {
                    Report.report(1, new StringBuffer().append("result: ").append(n).toString());
                }
                return n;
            }
            throw new QQError(new StringBuffer().append("Unable to parse: \"").append(fmt).append("\".").toString(), pos);
        } catch (IOException e) {
            throw new QQError(new StringBuffer().append("Unable to parse: \"").append(fmt).append("\".").toString(), pos);
        } catch (RuntimeException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new QQError(new StringBuffer().append("Unable to parse: \"").append(fmt).append("\"; ").append(e3.getMessage()).toString(), pos);
        }
    }
}
