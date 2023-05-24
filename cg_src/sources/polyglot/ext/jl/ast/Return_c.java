package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.List;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Return;
import polyglot.ast.Term;
import polyglot.types.CodeInstance;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.InitializerInstance;
import polyglot.types.MethodInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Return_c.class */
public class Return_c extends Stmt_c implements Return {
    protected Expr expr;

    public Return_c(Position pos, Expr expr) {
        super(pos);
        this.expr = expr;
    }

    @Override // polyglot.ast.Return
    public Expr expr() {
        return this.expr;
    }

    @Override // polyglot.ast.Return
    public Return expr(Expr expr) {
        Return_c n = (Return_c) copy();
        n.expr = expr;
        return n;
    }

    protected Return_c reconstruct(Expr expr) {
        if (expr != this.expr) {
            Return_c n = (Return_c) copy();
            n.expr = expr;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr expr = (Expr) visitChild(this.expr, v);
        return reconstruct(expr);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        Context c = tc.context();
        CodeInstance ci = c.currentCode();
        if (ci instanceof InitializerInstance) {
            throw new SemanticException("Cannot return from an initializer block.", position());
        }
        if (ci instanceof ConstructorInstance) {
            if (this.expr != null) {
                throw new SemanticException(new StringBuffer().append("Cannot return a value from ").append(ci).append(".").toString(), position());
            }
            return this;
        } else if (ci instanceof MethodInstance) {
            MethodInstance mi = (MethodInstance) ci;
            if (mi.returnType().isVoid()) {
                if (this.expr != null) {
                    throw new SemanticException(new StringBuffer().append("Cannot return a value from ").append(mi).append(".").toString(), position());
                }
                return this;
            } else if (this.expr == null) {
                throw new SemanticException(new StringBuffer().append("Must return a value from ").append(mi).append(".").toString(), position());
            } else {
                if (ts.isImplicitCastValid(this.expr.type(), mi.returnType())) {
                    return this;
                }
                if (ts.numericConversionValid(mi.returnType(), this.expr.constantValue())) {
                    return this;
                }
                throw new SemanticException(new StringBuffer().append("Cannot return expression of type ").append(this.expr.type()).append(" from ").append(mi).append(".").toString(), this.expr.position());
            }
        } else {
            throw new InternalCompilerError("Unrecognized code type.");
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        if (child == this.expr) {
            Context c = av.context();
            CodeInstance ci = c.currentCode();
            if (ci instanceof MethodInstance) {
                MethodInstance mi = (MethodInstance) ci;
                TypeSystem ts = av.typeSystem();
                if (ts.numericConversionValid(mi.returnType(), child.constantValue())) {
                    return child.type();
                }
                return mi.returnType();
            }
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append("return").append(this.expr != null ? new StringBuffer().append(Instruction.argsep).append(this.expr).toString() : "").append(";").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("return");
        if (this.expr != null) {
            w.write(Instruction.argsep);
            print(this.expr, w, tr);
        }
        w.write(";");
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.expr != null ? this.expr.entry() : this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (this.expr != null) {
            v.visitCFG(this.expr, this);
        }
        v.visitReturn(this);
        return Collections.EMPTY_LIST;
    }
}
