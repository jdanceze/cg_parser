package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Call;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Precedence;
import polyglot.ast.ProcedureCall;
import polyglot.ast.Receiver;
import polyglot.ast.Special;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.MethodInstance;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Call_c.class */
public class Call_c extends Expr_c implements Call {
    protected Receiver target;
    protected String name;
    protected List arguments;
    protected MethodInstance mi;
    protected boolean targetImplicit;
    static Class class$polyglot$ast$Expr;

    public Call_c(Position pos, Receiver target, String name, List arguments) {
        super(pos);
        Class cls;
        this.target = target;
        this.name = name;
        if (class$polyglot$ast$Expr == null) {
            cls = class$("polyglot.ast.Expr");
            class$polyglot$ast$Expr = cls;
        } else {
            cls = class$polyglot$ast$Expr;
        }
        this.arguments = TypedList.copyAndCheck(arguments, cls, true);
        this.targetImplicit = target == null;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return Precedence.LITERAL;
    }

    @Override // polyglot.ast.Call
    public Receiver target() {
        return this.target;
    }

    @Override // polyglot.ast.Call
    public Call target(Receiver target) {
        Call_c n = (Call_c) copy();
        n.target = target;
        return n;
    }

    @Override // polyglot.ast.Call
    public String name() {
        return this.name;
    }

    @Override // polyglot.ast.Call
    public Call name(String name) {
        Call_c n = (Call_c) copy();
        n.name = name;
        return n;
    }

    @Override // polyglot.ast.ProcedureCall
    public ProcedureInstance procedureInstance() {
        return methodInstance();
    }

    @Override // polyglot.ast.Call
    public MethodInstance methodInstance() {
        return this.mi;
    }

    @Override // polyglot.ast.Call
    public Call methodInstance(MethodInstance mi) {
        Call_c n = (Call_c) copy();
        n.mi = mi;
        return n;
    }

    @Override // polyglot.ast.Call
    public boolean isTargetImplicit() {
        return this.targetImplicit;
    }

    @Override // polyglot.ast.Call
    public Call targetImplicit(boolean targetImplicit) {
        if (targetImplicit == this.targetImplicit) {
            return this;
        }
        Call_c n = (Call_c) copy();
        n.targetImplicit = targetImplicit;
        return n;
    }

    @Override // polyglot.ast.Call, polyglot.ast.ProcedureCall
    public List arguments() {
        return this.arguments;
    }

    @Override // polyglot.ast.Call, polyglot.ast.ProcedureCall
    public ProcedureCall arguments(List arguments) {
        Class cls;
        Call_c n = (Call_c) copy();
        if (class$polyglot$ast$Expr == null) {
            cls = class$("polyglot.ast.Expr");
            class$polyglot$ast$Expr = cls;
        } else {
            cls = class$polyglot$ast$Expr;
        }
        n.arguments = TypedList.copyAndCheck(arguments, cls, true);
        return n;
    }

    protected Call_c reconstruct(Receiver target, List arguments) {
        Class cls;
        if (target != this.target || !CollectionUtil.equals(arguments, this.arguments)) {
            Call_c n = (Call_c) copy();
            n.target = target;
            if (class$polyglot$ast$Expr == null) {
                cls = class$("polyglot.ast.Expr");
                class$polyglot$ast$Expr = cls;
            } else {
                cls = class$polyglot$ast$Expr;
            }
            n.arguments = TypedList.copyAndCheck(arguments, cls, true);
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Receiver target = (Receiver) visitChild(this.target, v);
        List arguments = visitList(this.arguments, v);
        return reconstruct(target, arguments);
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        Call_c n = (Call_c) super.buildTypes(tb);
        TypeSystem ts = tb.typeSystem();
        List l = new ArrayList(this.arguments.size());
        for (int i = 0; i < this.arguments.size(); i++) {
            l.add(ts.unknownType(position()));
        }
        MethodInstance mi = ts.methodInstance(position(), ts.Object(), Flags.NONE, ts.unknownType(position()), this.name, l, Collections.EMPTY_LIST);
        return n.methodInstance(mi);
    }

    protected Node typeCheckNullTarget(TypeChecker tc, List argTypes) throws SemanticException {
        Receiver r;
        TypeSystem ts = tc.typeSystem();
        NodeFactory nf = tc.nodeFactory();
        Context c = tc.context();
        MethodInstance mi = c.findMethod(this.name, argTypes);
        if (mi.flags().isStatic()) {
            r = nf.CanonicalTypeNode(position(), mi.container()).type(mi.container());
        } else {
            ClassType scope = c.findMethodScope(this.name);
            if (!ts.equals(scope, c.currentClass())) {
                r = nf.This(position(), nf.CanonicalTypeNode(position(), scope)).type(scope);
            } else {
                r = nf.This(position()).type(scope);
            }
        }
        return targetImplicit(true).target((Receiver) r.typeCheck(tc)).del().typeCheck(tc);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        Context c = tc.context();
        List argTypes = new ArrayList(this.arguments.size());
        for (Expr e : this.arguments) {
            argTypes.add(e.type());
        }
        if (this.target == null) {
            return typeCheckNullTarget(tc, argTypes);
        }
        ReferenceType targetType = findTargetType();
        MethodInstance mi = ts.findMethod(targetType, this.name, argTypes, c.currentClass());
        boolean staticContext = this.target instanceof TypeNode;
        if (staticContext && !mi.flags().isStatic()) {
            throw new SemanticException(new StringBuffer().append("Cannot call non-static method ").append(this.name).append(" of ").append(targetType).append(" in static ").append("context.").toString(), position());
        }
        if ((this.target instanceof Special) && ((Special) this.target).kind() == Special.SUPER && mi.flags().isAbstract()) {
            throw new SemanticException("Cannot call an abstract method of the super class", position());
        }
        Call_c call = (Call_c) methodInstance(mi).type(mi.returnType());
        call.checkConsistency(c);
        return call;
    }

    public ReferenceType findTargetType() throws SemanticException {
        Type t = this.target.type();
        if (t.isReference()) {
            return t.toReference();
        }
        if (this.target instanceof Expr) {
            throw new SemanticException(new StringBuffer().append("Cannot invoke method \"").append(this.name).append("\" on ").append("an expression of non-reference type ").append(t).append(".").toString(), this.target.position());
        }
        if (this.target instanceof TypeNode) {
            throw new SemanticException(new StringBuffer().append("Cannot invoke static method \"").append(this.name).append("\" on non-reference type ").append(t).append(".").toString(), this.target.position());
        }
        throw new SemanticException("Receiver of method invocation must be a reference type.", this.target.position());
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        if (child == this.target) {
            return this.mi.container();
        }
        Iterator i = this.arguments.iterator();
        Iterator j = this.mi.formalTypes().iterator();
        while (i.hasNext() && j.hasNext()) {
            Expr e = (Expr) i.next();
            Type t = (Type) j.next();
            if (e == child) {
                return t;
            }
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.targetImplicit ? "" : new StringBuffer().append(this.target.toString()).append(".").toString());
        sb.append(this.name);
        sb.append("(");
        int count = 0;
        Iterator i = this.arguments.iterator();
        while (true) {
            if (!i.hasNext()) {
                break;
            }
            int i2 = count;
            count++;
            if (i2 > 2) {
                sb.append("...");
                break;
            }
            Expr n = (Expr) i.next();
            sb.append(n.toString());
            if (i.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if (!this.targetImplicit) {
            if (this.target instanceof Expr) {
                printSubExpr((Expr) this.target, w, tr);
                w.write(".");
            } else if (this.target != null) {
                print(this.target, w, tr);
                w.write(".");
            }
        }
        w.write(new StringBuffer().append(this.name).append("(").toString());
        w.begin(0);
        Iterator i = this.arguments.iterator();
        while (i.hasNext()) {
            Expr e = (Expr) i.next();
            print(e, w, tr);
            if (i.hasNext()) {
                w.write(",");
                w.allowBreak(0, Instruction.argsep);
            }
        }
        w.end();
        w.write(")");
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        if (this.mi != null) {
            w.allowBreak(4, Instruction.argsep);
            w.begin(0);
            w.write(new StringBuffer().append("(instance ").append(this.mi).append(")").toString());
            w.end();
        }
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(name ").append(this.name).append(")").toString());
        w.end();
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(arguments ").append(this.arguments).append(")").toString());
        w.end();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        if (this.target instanceof Expr) {
            return ((Expr) this.target).entry();
        }
        return listEntry(this.arguments, this);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (this.target instanceof Expr) {
            Expr t = (Expr) this.target;
            v.visitCFG(t, listEntry(this.arguments, this));
        }
        v.visitCFGList(this.arguments, this);
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
        if (this.mi == null) {
            throw new InternalCompilerError(position(), "Null method instance after type check.");
        }
        return super.exceptionCheck(ec);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public List throwTypes(TypeSystem ts) {
        List l = new LinkedList();
        l.addAll(this.mi.throwTypes());
        l.addAll(ts.uncheckedExceptions());
        if ((this.target instanceof Expr) && !(this.target instanceof Special)) {
            l.add(ts.NullPointerException());
        }
        return l;
    }

    protected void checkConsistency(Context c) throws SemanticException {
        if (this.targetImplicit) {
            c.findMethod(this.name, this.mi.formalTypes());
        }
    }
}
