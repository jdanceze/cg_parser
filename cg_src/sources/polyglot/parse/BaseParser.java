package polyglot.parse;

import java.io.IOException;
import java.util.List;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;
import polyglot.ast.AmbExpr;
import polyglot.ast.AmbPrefix;
import polyglot.ast.AmbReceiver;
import polyglot.ast.AmbTypeNode;
import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Prefix;
import polyglot.ast.QualifierNode;
import polyglot.ast.Receiver;
import polyglot.ast.TypeNode;
import polyglot.lex.Lexer;
import polyglot.lex.Token;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/parse/BaseParser.class */
public abstract class BaseParser extends lr_parser {
    public final Lexer lexer;
    public final ErrorQueue eq;
    public final TypeSystem ts;
    public final NodeFactory nf;
    protected Position prev_pos = Position.COMPILER_GENERATED;
    protected Position position = Position.COMPILER_GENERATED;

    public BaseParser(Lexer l, TypeSystem t, NodeFactory n, ErrorQueue q) {
        this.lexer = l;
        this.eq = q;
        this.ts = t;
        this.nf = n;
    }

    public Symbol nextSymbol() throws IOException {
        Token t = this.lexer.nextToken();
        this.position = this.prev_pos;
        this.prev_pos = t.getPosition();
        return t.symbol();
    }

    public Position position() {
        return this.position;
    }

    @Override // java_cup.runtime.lr_parser
    public void report_fatal_error(String message, Object info) throws Exception {
        report_error(message, info);
        die();
    }

    public void die(String msg, Position pos) throws Exception {
        report_fatal_error(msg, pos);
    }

    public void die(Position pos) throws Exception {
        report_fatal_error("Syntax error.", pos);
    }

    public void die() throws Exception {
        done_parsing();
        throw new Exception();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Position posForObject(Object o) {
        if (o instanceof Node) {
            return pos((Node) o);
        }
        if (o instanceof Token) {
            return pos((Token) o);
        }
        if (o instanceof Type) {
            return pos((Type) o);
        }
        if (o instanceof List) {
            return pos((List) o);
        }
        if (o instanceof VarDeclarator) {
            return pos((VarDeclarator) o);
        }
        return null;
    }

    public Position pos(Object first, Object last) {
        return pos(first, last, first);
    }

    public Position pos(Object first, Object last, Object noEndDefault) {
        Position fpos = posForObject(first);
        Position epos = posForObject(last);
        if (fpos != null && epos != null) {
            if (epos.endColumn() != -2) {
                return new Position(fpos, epos);
            }
            return posForObject(noEndDefault);
        }
        return null;
    }

    public Position pos(Token t) {
        if (t == null) {
            return null;
        }
        return t.getPosition();
    }

    public Position pos(Type n) {
        if (n == null) {
            return null;
        }
        return n.position();
    }

    public Position pos(List l) {
        if (l == null || l.isEmpty()) {
            return null;
        }
        return pos(l.get(0), l.get(l.size() - 1));
    }

    public Position pos(VarDeclarator n) {
        if (n == null) {
            return null;
        }
        return n.pos;
    }

    public Position pos(Node n) {
        if (n == null) {
            return null;
        }
        return n.position();
    }

    public TypeNode array(TypeNode n, int dims) throws Exception {
        if (dims > 0) {
            if (n instanceof CanonicalTypeNode) {
                Type t = ((CanonicalTypeNode) n).type();
                return this.nf.CanonicalTypeNode(pos(n), this.ts.arrayOf(t, dims));
            }
            return this.nf.ArrayTypeNode(pos(n), array(n, dims - 1));
        }
        return n;
    }

    protected QualifierNode prefixToQualifier(Prefix p) throws Exception {
        if (p instanceof TypeNode) {
            return typeToQualifier((TypeNode) p);
        }
        if (p instanceof Expr) {
            return exprToQualifier((Expr) p);
        }
        if (p instanceof AmbReceiver) {
            AmbReceiver a = (AmbReceiver) p;
            if (a.prefix() != null) {
                return this.nf.AmbQualifierNode(pos(p), prefixToQualifier(a.prefix()), a.name());
            }
            return this.nf.AmbQualifierNode(pos(p), a.name());
        } else if (p instanceof AmbPrefix) {
            AmbPrefix a2 = (AmbPrefix) p;
            if (a2.prefix() != null) {
                return this.nf.AmbQualifierNode(pos(p), prefixToQualifier(a2.prefix()), a2.name());
            }
            return this.nf.AmbQualifierNode(pos(p), a2.name());
        } else {
            die(pos(p));
            return null;
        }
    }

    protected QualifierNode typeToQualifier(TypeNode t) throws Exception {
        if (t instanceof AmbTypeNode) {
            AmbTypeNode a = (AmbTypeNode) t;
            if (a.qualifier() != null) {
                return this.nf.AmbQualifierNode(pos(t), a.qual(), a.name());
            }
            return this.nf.AmbQualifierNode(pos(t), a.name());
        }
        die(pos(t));
        return null;
    }

    protected QualifierNode exprToQualifier(Expr e) throws Exception {
        if (e instanceof AmbExpr) {
            AmbExpr a = (AmbExpr) e;
            return this.nf.AmbQualifierNode(pos(e), a.name());
        } else if (e instanceof Field) {
            Field f = (Field) e;
            Receiver r = f.target();
            return this.nf.AmbQualifierNode(pos(e), prefixToQualifier(r), f.name());
        } else {
            die(pos(e));
            return null;
        }
    }

    public TypeNode exprToType(Expr e) throws Exception {
        if (e instanceof AmbExpr) {
            AmbExpr a = (AmbExpr) e;
            return this.nf.AmbTypeNode(pos(e), a.name());
        } else if (e instanceof Field) {
            Field f = (Field) e;
            Receiver r = f.target();
            return this.nf.AmbTypeNode(pos(e), prefixToQualifier(r), f.name());
        } else {
            die(pos(e));
            return null;
        }
    }
}
