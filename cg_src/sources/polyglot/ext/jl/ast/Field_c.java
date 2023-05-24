package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.List;
import polyglot.ast.AmbReceiver;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Receiver;
import polyglot.ast.Special;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.Context;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.types.VarInstance;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Field_c.class */
public class Field_c extends Expr_c implements Field {
    protected Receiver target;
    protected String name;
    protected FieldInstance fi;
    protected boolean targetImplicit;

    public Field_c(Position pos, Receiver target, String name) {
        super(pos);
        this.target = target;
        this.name = name;
        this.targetImplicit = false;
        if (target == null) {
            throw new InternalCompilerError("Cannot create a field with a null target.  Use AmbExpr or prefix with the appropriate type node or this.");
        }
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return Precedence.LITERAL;
    }

    @Override // polyglot.ast.Field
    public Receiver target() {
        return this.target;
    }

    @Override // polyglot.ast.Field
    public Field target(Receiver target) {
        Field_c n = (Field_c) copy();
        n.target = target;
        return n;
    }

    @Override // polyglot.ast.Field
    public String name() {
        return this.name;
    }

    @Override // polyglot.ast.Field
    public Field name(String name) {
        Field_c n = (Field_c) copy();
        n.name = name;
        return n;
    }

    @Override // polyglot.ast.Variable
    public Flags flags() {
        return this.fi.flags();
    }

    @Override // polyglot.ast.Field
    public FieldInstance fieldInstance() {
        return this.fi;
    }

    @Override // polyglot.ast.Field
    public Field fieldInstance(FieldInstance fi) {
        Field_c n = (Field_c) copy();
        n.fi = fi;
        return n;
    }

    @Override // polyglot.ast.Field
    public boolean isTargetImplicit() {
        return this.targetImplicit;
    }

    @Override // polyglot.ast.Field
    public Field targetImplicit(boolean implicit) {
        Field_c n = (Field_c) copy();
        n.targetImplicit = implicit;
        return n;
    }

    protected Field_c reconstruct(Receiver target) {
        if (target != this.target) {
            Field_c n = (Field_c) copy();
            n.target = target;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Receiver target = (Receiver) visitChild(this.target, v);
        return reconstruct(target);
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        Field_c n = (Field_c) super.buildTypes(tb);
        TypeSystem ts = tb.typeSystem();
        FieldInstance fi = ts.fieldInstance(position(), ts.Object(), Flags.NONE, ts.unknownType(position()), this.name);
        return n.fieldInstance(fi);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        Context c = tc.context();
        TypeSystem ts = tc.typeSystem();
        if (!this.target.type().isReference()) {
            throw new SemanticException(new StringBuffer().append("Cannot access field \"").append(this.name).append("\" ").append(this.target instanceof Expr ? "on an expression " : "").append("of non-reference type \"").append(this.target.type()).append("\".").toString(), this.target.position());
        }
        FieldInstance fi = ts.findField(this.target.type().toReference(), this.name, c.currentClass());
        if (fi == null) {
            throw new InternalCompilerError(new StringBuffer().append("Cannot access field on node of type ").append(this.target.getClass().getName()).append(".").toString());
        }
        Field_c f = (Field_c) fieldInstance(fi).type(fi.type());
        f.checkConsistency(c);
        return f;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        if (child == this.target) {
            return this.fi.container();
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if (!this.targetImplicit) {
            if (this.target instanceof Expr) {
                printSubExpr((Expr) this.target, w, tr);
            } else if ((this.target instanceof TypeNode) || (this.target instanceof AmbReceiver)) {
                print(this.target, w, tr);
            }
            w.write(".");
        }
        w.write(this.name);
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(name \"").append(this.name).append("\")").toString());
        w.end();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        if (this.target instanceof Expr) {
            return ((Expr) this.target).entry();
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (this.target instanceof Expr) {
            v.visitCFG((Expr) this.target, this);
        }
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append((this.target == null || this.targetImplicit) ? "" : new StringBuffer().append(this.target).append(".").toString()).append(this.name).toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public List throwTypes(TypeSystem ts) {
        if ((this.target instanceof Expr) && !(this.target instanceof Special)) {
            return Collections.singletonList(ts.NullPointerException());
        }
        return Collections.EMPTY_LIST;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public boolean isConstant() {
        if (this.fi != null) {
            if ((this.target instanceof TypeNode) || ((this.target instanceof Special) && this.targetImplicit)) {
                return this.fi.isConstant();
            }
            return false;
        }
        return false;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Object constantValue() {
        if (isConstant()) {
            return this.fi.constantValue();
        }
        return null;
    }

    protected void checkConsistency(Context c) {
        if (this.targetImplicit) {
            VarInstance vi = c.findVariableSilent(this.name);
            if (vi instanceof FieldInstance) {
                FieldInstance rfi = (FieldInstance) vi;
                if (c.typeSystem().equals(rfi, this.fi)) {
                    return;
                }
            }
            throw new InternalCompilerError(new StringBuffer().append("Field ").append(this).append(" has an ").append("implicit target, but the name ").append(this.name).append(" resolves to ").append(vi).append(" instead of ").append(this.target).toString(), position());
        }
    }
}
