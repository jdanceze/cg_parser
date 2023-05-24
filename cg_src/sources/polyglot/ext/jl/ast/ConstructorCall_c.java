package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.ConstructorCall;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.ProcedureCall;
import polyglot.ast.Term;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.ProcedureInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/ConstructorCall_c.class */
public class ConstructorCall_c extends Stmt_c implements ConstructorCall {
    protected ConstructorCall.Kind kind;
    protected Expr qualifier;
    protected List arguments;
    protected ConstructorInstance ci;
    static Class class$polyglot$ast$Expr;

    public ConstructorCall_c(Position pos, ConstructorCall.Kind kind, Expr qualifier, List arguments) {
        super(pos);
        Class cls;
        this.kind = kind;
        this.qualifier = qualifier;
        if (class$polyglot$ast$Expr == null) {
            cls = class$("polyglot.ast.Expr");
            class$polyglot$ast$Expr = cls;
        } else {
            cls = class$polyglot$ast$Expr;
        }
        this.arguments = TypedList.copyAndCheck(arguments, cls, true);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ast.ConstructorCall
    public Expr qualifier() {
        return this.qualifier;
    }

    @Override // polyglot.ast.ConstructorCall
    public ConstructorCall qualifier(Expr qualifier) {
        ConstructorCall_c n = (ConstructorCall_c) copy();
        n.qualifier = qualifier;
        return n;
    }

    @Override // polyglot.ast.ConstructorCall
    public ConstructorCall.Kind kind() {
        return this.kind;
    }

    @Override // polyglot.ast.ConstructorCall
    public ConstructorCall kind(ConstructorCall.Kind kind) {
        ConstructorCall_c n = (ConstructorCall_c) copy();
        n.kind = kind;
        return n;
    }

    @Override // polyglot.ast.ConstructorCall, polyglot.ast.ProcedureCall
    public List arguments() {
        return Collections.unmodifiableList(this.arguments);
    }

    @Override // polyglot.ast.ConstructorCall, polyglot.ast.ProcedureCall
    public ProcedureCall arguments(List arguments) {
        Class cls;
        ConstructorCall_c n = (ConstructorCall_c) copy();
        if (class$polyglot$ast$Expr == null) {
            cls = class$("polyglot.ast.Expr");
            class$polyglot$ast$Expr = cls;
        } else {
            cls = class$polyglot$ast$Expr;
        }
        n.arguments = TypedList.copyAndCheck(arguments, cls, true);
        return n;
    }

    @Override // polyglot.ast.ProcedureCall
    public ProcedureInstance procedureInstance() {
        return constructorInstance();
    }

    @Override // polyglot.ast.ConstructorCall
    public ConstructorInstance constructorInstance() {
        return this.ci;
    }

    @Override // polyglot.ast.ConstructorCall
    public ConstructorCall constructorInstance(ConstructorInstance ci) {
        ConstructorCall_c n = (ConstructorCall_c) copy();
        n.ci = ci;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Context enterScope(Context c) {
        return c.pushStatic();
    }

    protected ConstructorCall_c reconstruct(Expr qualifier, List arguments) {
        Class cls;
        if (qualifier != this.qualifier || !CollectionUtil.equals(arguments, this.arguments)) {
            ConstructorCall_c n = (ConstructorCall_c) copy();
            n.qualifier = qualifier;
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
        Expr qualifier = (Expr) visitChild(this.qualifier, v);
        List arguments = visitList(this.arguments, v);
        return reconstruct(qualifier, arguments);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        TypeSystem ts = tb.typeSystem();
        if (this.kind == ConstructorCall.SUPER && tb.currentClass() == ts.Object()) {
            return tb.nodeFactory().Empty(position());
        }
        ConstructorCall_c n = (ConstructorCall_c) super.buildTypes(tb);
        List l = new ArrayList(this.arguments.size());
        for (int i = 0; i < this.arguments.size(); i++) {
            l.add(ts.unknownType(position()));
        }
        ConstructorInstance ci = ts.constructorInstance(position(), ts.Object(), Flags.NONE, l, Collections.EMPTY_LIST);
        return n.constructorInstance(ci);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        ClassType e;
        TypeSystem ts = tc.typeSystem();
        Context c = tc.context();
        ClassType ct = c.currentClass();
        Type superType = ct.superType();
        if (this.qualifier != null) {
            if (this.kind != ConstructorCall.SUPER) {
                throw new SemanticException("Can only qualify a \"super\"constructor invocation.", position());
            }
            if (!superType.isClass() || !superType.toClass().isInnerClass() || superType.toClass().inStaticContext()) {
                throw new SemanticException(new StringBuffer().append("The class \"").append(superType).append("\"").append(" is not an inner class, or was declared in a static ").append("context; a qualified constructor invocation cannot ").append("be used.").toString(), position());
            }
            Type qt = this.qualifier.type();
            if (!qt.isClass() || !qt.isSubtype(superType.toClass().outer())) {
                throw new SemanticException(new StringBuffer().append("The type of the qualifier \"").append(qt).append("\" does not match the immediately enclosing ").append("class  of the super class \"").append(superType.toClass().outer()).append("\".").toString(), this.qualifier.position());
            }
        }
        if (this.kind == ConstructorCall.SUPER) {
            if (!superType.isClass()) {
                throw new SemanticException(new StringBuffer().append("Super type of ").append(ct).append(" is not a class.").toString(), position());
            }
            if (this.qualifier == null && superType.isClass() && superType.toClass().isInnerClass()) {
                ClassType superContainer = superType.toClass().outer();
                ClassType classType = ct;
                while (true) {
                    e = classType;
                    if (e == null || (e.isSubtype(superContainer) && ct.hasEnclosingInstance(e))) {
                        break;
                    }
                    classType = e.outer();
                }
                if (e == null) {
                    throw new SemanticException(new StringBuffer().append(ct).append(" must have an enclosing instance").append(" that is a subtype of ").append(superContainer).toString(), position());
                }
                if (e == ct) {
                    throw new SemanticException(new StringBuffer().append(ct).append(" is a subtype of ").append(superContainer).append("; an enclosing instance that is a subtype of ").append(superContainer).append(" must be specified in the super constructor call.").toString(), position());
                }
            }
            ct = ct.superType().toClass();
        }
        List argTypes = new LinkedList();
        for (Expr e2 : this.arguments) {
            argTypes.add(e2.type());
        }
        ConstructorInstance ci = ts.findConstructor(ct, argTypes, c.currentClass());
        return constructorInstance(ci);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        TypeSystem ts = av.typeSystem();
        if (child == this.qualifier) {
            return ts.Object();
        }
        Iterator i = this.arguments.iterator();
        Iterator j = this.ci.formalTypes().iterator();
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
        return new StringBuffer().append(this.qualifier != null ? new StringBuffer().append(this.qualifier).append(".").toString() : "").append(this.kind).append("(...)").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if (this.qualifier != null) {
            print(this.qualifier, w, tr);
            w.write(".");
        }
        w.write(new StringBuffer().append(this.kind).append("(").toString());
        w.begin(0);
        Iterator i = this.arguments.iterator();
        while (i.hasNext()) {
            Expr e = (Expr) i.next();
            print(e, w, tr);
            if (i.hasNext()) {
                w.write(",");
                w.allowBreak(0);
            }
        }
        w.end();
        w.write(");");
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        if (this.qualifier != null) {
            return this.qualifier.entry();
        }
        return listEntry(this.arguments, this);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (this.qualifier != null) {
            v.visitCFG(this.qualifier, listEntry(this.arguments, this));
        }
        v.visitCFGList(this.arguments, this);
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public List throwTypes(TypeSystem ts) {
        List l = new LinkedList();
        l.addAll(this.ci.throwTypes());
        l.addAll(ts.uncheckedExceptions());
        return l;
    }
}
