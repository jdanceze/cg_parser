package soot.javaToJimple.jj.ast;

import java.util.List;
import polyglot.ast.Call;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ext.jl.ast.Expr_c;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/jj/ast/JjAccessField_c.class */
public class JjAccessField_c extends Expr_c implements Expr {
    private Call getMeth;
    private Call setMeth;
    private Field field;

    public JjAccessField_c(Position pos, Call getMeth, Call setMeth, Field field) {
        super(pos);
        this.getMeth = getMeth;
        this.setMeth = setMeth;
        this.field = field;
    }

    public Call getMeth() {
        return this.getMeth;
    }

    public Call setMeth() {
        return this.setMeth;
    }

    public Field field() {
        return this.field;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return this.field + Instruction.argsep + this.getMeth + Instruction.argsep + this.setMeth;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.field.entry();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        visitChild(this.field, v);
        visitChild(this.getMeth, v);
        visitChild(this.setMeth, v);
        return this;
    }
}
