package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.AmbTypeNode;
import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.ClassBody;
import polyglot.ast.Expr;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Precedence;
import polyglot.ast.ProcedureCall;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.ParsedClassType;
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
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/New_c.class */
public class New_c extends Expr_c implements New {
    protected Expr qualifier;
    protected TypeNode tn;
    protected List arguments;
    protected ClassBody body;
    protected ConstructorInstance ci;
    protected ParsedClassType anonType;
    static Class class$polyglot$ast$Expr;

    public New_c(Position pos, Expr qualifier, TypeNode tn, List arguments, ClassBody body) {
        super(pos);
        Class cls;
        this.qualifier = qualifier;
        this.tn = tn;
        if (class$polyglot$ast$Expr == null) {
            cls = class$("polyglot.ast.Expr");
            class$polyglot$ast$Expr = cls;
        } else {
            cls = class$polyglot$ast$Expr;
        }
        this.arguments = TypedList.copyAndCheck(arguments, cls, true);
        this.body = body;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ast.New
    public Expr qualifier() {
        return this.qualifier;
    }

    @Override // polyglot.ast.New
    public New qualifier(Expr qualifier) {
        New_c n = (New_c) copy();
        n.qualifier = qualifier;
        return n;
    }

    @Override // polyglot.ast.New
    public TypeNode objectType() {
        return this.tn;
    }

    @Override // polyglot.ast.New
    public New objectType(TypeNode tn) {
        New_c n = (New_c) copy();
        n.tn = tn;
        return n;
    }

    @Override // polyglot.ast.New
    public ParsedClassType anonType() {
        return this.anonType;
    }

    @Override // polyglot.ast.New
    public New anonType(ParsedClassType anonType) {
        New_c n = (New_c) copy();
        n.anonType = anonType;
        return n;
    }

    @Override // polyglot.ast.ProcedureCall
    public ProcedureInstance procedureInstance() {
        return constructorInstance();
    }

    @Override // polyglot.ast.New
    public ConstructorInstance constructorInstance() {
        return this.ci;
    }

    @Override // polyglot.ast.New
    public New constructorInstance(ConstructorInstance ci) {
        New_c n = (New_c) copy();
        n.ci = ci;
        return n;
    }

    @Override // polyglot.ast.New, polyglot.ast.ProcedureCall
    public List arguments() {
        return this.arguments;
    }

    @Override // polyglot.ast.New, polyglot.ast.ProcedureCall
    public ProcedureCall arguments(List arguments) {
        Class cls;
        New_c n = (New_c) copy();
        if (class$polyglot$ast$Expr == null) {
            cls = class$("polyglot.ast.Expr");
            class$polyglot$ast$Expr = cls;
        } else {
            cls = class$polyglot$ast$Expr;
        }
        n.arguments = TypedList.copyAndCheck(arguments, cls, true);
        return n;
    }

    @Override // polyglot.ast.New
    public ClassBody body() {
        return this.body;
    }

    @Override // polyglot.ast.New
    public New body(ClassBody body) {
        New_c n = (New_c) copy();
        n.body = body;
        return n;
    }

    protected New_c reconstruct(Expr qualifier, TypeNode tn, List arguments, ClassBody body) {
        Class cls;
        if (qualifier != this.qualifier || tn != this.tn || !CollectionUtil.equals(arguments, this.arguments) || body != this.body) {
            New_c n = (New_c) copy();
            n.tn = tn;
            n.qualifier = qualifier;
            if (class$polyglot$ast$Expr == null) {
                cls = class$("polyglot.ast.Expr");
                class$polyglot$ast$Expr = cls;
            } else {
                cls = class$polyglot$ast$Expr;
            }
            n.arguments = TypedList.copyAndCheck(arguments, cls, true);
            n.body = body;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr qualifier = (Expr) visitChild(this.qualifier, v);
        TypeNode tn = (TypeNode) visitChild(this.tn, v);
        List arguments = visitList(this.arguments, v);
        ClassBody body = (ClassBody) visitChild(this.body, v);
        return reconstruct(qualifier, tn, arguments, body);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Context enterScope(Node child, Context c) {
        if (child == this.body && this.anonType != null && this.body != null) {
            c = c.pushClass(this.anonType, this.anonType);
        }
        return super.enterScope(child, c);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
        if (this.body != null) {
            return tb.bypass(this.body);
        }
        return tb;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        New_c n = this;
        if (n.body() != null) {
            TypeBuilder bodyTB = ((TypeBuilder) tb.visitChildren()).pushAnonClass(position());
            New_c n2 = (New_c) n.body((ClassBody) n.body().visit(bodyTB));
            ParsedClassType type = bodyTB.currentClass();
            n = (New_c) n2.anonType(type);
        }
        TypeSystem ts = tb.typeSystem();
        List l = new ArrayList(n.arguments.size());
        for (int i = 0; i < n.arguments.size(); i++) {
            l.add(ts.unknownType(position()));
        }
        ConstructorInstance ci = ts.constructorInstance(position(), ts.Object(), Flags.NONE, l, Collections.EMPTY_LIST);
        return ((New_c) n.constructorInstance(ci)).type(ts.unknownType(position()));
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
        if (this.qualifier != null) {
            ar = (AmbiguityRemover) ar.bypass(this.tn);
        }
        if (this.body != null) {
            ar = (AmbiguityRemover) ar.bypass(this.body);
        }
        return ar;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        Expr q;
        ClassType mt;
        if (ar.kind() != AmbiguityRemover.ALL) {
            return this;
        }
        if (this.qualifier == null) {
            ClassType ct = this.tn.type().toClass();
            if (!ct.isMember() || ct.flags().isStatic()) {
                return this;
            }
            NodeFactory nf = ar.nodeFactory();
            TypeSystem ts = ar.typeSystem();
            Context c = ar.context();
            Type outer = null;
            String name = ct.name();
            ClassType t = c.currentClass();
            if (t == this.anonType) {
                t = t.outer();
            }
            while (true) {
                if (t == null) {
                    break;
                }
                try {
                    t = ts.staticTarget(t).toClass();
                    mt = ts.findMemberClass(t, name, c.currentClass());
                } catch (SemanticException e) {
                }
                if (ts.equals(mt, ct)) {
                    outer = t;
                    break;
                }
                t = t.outer();
            }
            if (outer == null) {
                throw new SemanticException(new StringBuffer().append("Could not find non-static member class \"").append(name).append("\".").toString(), position());
            }
            if (outer.equals(c.currentClass())) {
                q = nf.This(position());
            } else {
                q = nf.This(position(), nf.CanonicalTypeNode(position(), outer));
            }
            return qualifier(q);
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor typeCheckEnter(TypeChecker tc) throws SemanticException {
        if (this.qualifier != null) {
            tc = (TypeChecker) tc.bypass(this.tn);
        }
        if (this.body != null) {
            tc = (TypeChecker) tc.bypass(this.body);
        }
        return tc;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        New_c n = this;
        if (this.qualifier != null) {
            Type qt = this.qualifier.type();
            if (!qt.isClass()) {
                throw new SemanticException("Cannot instantiate member class of a non-class type.", this.qualifier.position());
            }
            TypeNode tn = disambiguateTypeNode(tc, qt.toClass());
            ClassType ct = tn.type().toClass();
            if (!ct.isInnerClass()) {
                throw new SemanticException(new StringBuffer().append("Cannot provide a containing instance for non-inner class ").append(ct.fullName()).append(".").toString(), this.qualifier.position());
            }
            n = (New_c) n.objectType(tn);
        } else {
            ClassType ct2 = this.tn.type().toClass();
            if (ct2.isMember()) {
                ClassType classType = ct2;
                while (true) {
                    ClassType t = classType;
                    if (!t.isMember()) {
                        break;
                    } else if (t.flags().isStatic()) {
                        classType = t.outer();
                    } else {
                        throw new SemanticException(new StringBuffer().append("Cannot allocate non-static member class \"").append(t).append("\".").toString(), position());
                    }
                }
            }
        }
        return n.typeCheckEpilogue(tc);
    }

    protected Node typeCheckEpilogue(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        List argTypes = new ArrayList(this.arguments.size());
        for (Expr e : this.arguments) {
            argTypes.add(e.type());
        }
        ClassType ct = this.tn.type().toClass();
        if (this.body == null) {
            if (ct.flags().isInterface()) {
                throw new SemanticException("Cannot instantiate an interface.", position());
            }
            if (ct.flags().isAbstract()) {
                throw new SemanticException("Cannot instantiate an abstract class.", position());
            }
        } else if (ct.flags().isFinal()) {
            throw new SemanticException("Cannot create an anonymous subclass of a final class.", position());
        } else {
            if (ct.flags().isInterface() && !this.arguments.isEmpty()) {
                throw new SemanticException("Cannot pass arguments to an anonymous class that implements an interface.", ((Expr) this.arguments.get(0)).position());
            }
        }
        if (!ct.flags().isInterface()) {
            Context c = tc.context();
            if (this.body != null) {
                this.anonType.superType(ct);
                c = c.pushClass(this.anonType, this.anonType);
            }
            this.ci = ts.findConstructor(ct, argTypes, c.currentClass());
        } else {
            this.ci = ts.defaultConstructor(position(), ct);
        }
        New_c n = (New_c) constructorInstance(this.ci).type(ct);
        if (n.body == null) {
            return n;
        }
        if (!ct.flags().isInterface()) {
            this.anonType.superType(ct);
        } else {
            this.anonType.superType(ts.Object());
            this.anonType.addInterface(ct);
        }
        this.anonType.inStaticContext(tc.context().inStaticContext());
        New_c n2 = (New_c) n.type(this.anonType);
        ClassBody body = n2.typeCheckBody(tc, ct);
        return n2.body(body);
    }

    protected TypeNode partialDisambTypeNode(TypeNode tn, TypeChecker tc, ClassType outer) throws SemanticException {
        if (tn instanceof CanonicalTypeNode) {
            return tn;
        }
        if ((tn instanceof AmbTypeNode) && ((AmbTypeNode) tn).qual() == null) {
            String name = ((AmbTypeNode) tn).name();
            TypeSystem ts = tc.typeSystem();
            NodeFactory nf = tc.nodeFactory();
            Context c = tc.context();
            ClassType ct = ts.findMemberClass(outer, name, c.currentClass());
            return nf.CanonicalTypeNode(tn.position(), ct);
        }
        throw new SemanticException("Cannot instantiate an member class.", tn.position());
    }

    protected TypeNode disambiguateTypeNode(TypeChecker tc, ClassType ct) throws SemanticException {
        TypeNode tn = partialDisambTypeNode(this.tn, tc, ct);
        if (tn instanceof CanonicalTypeNode) {
            return tn;
        }
        Job sj = tc.job().spawn(tc.context(), tn, Pass.CLEAN_SUPER, Pass.DISAM_ALL);
        if (!sj.status()) {
            if (!sj.reportedErrors()) {
                throw new SemanticException("Could not disambiguate type.", this.tn.position());
            }
            throw new SemanticException();
        }
        return (TypeNode) visitChild((TypeNode) sj.ast(), tc);
    }

    protected ClassBody typeCheckBody(TypeChecker tc, ClassType superType) throws SemanticException {
        Context bodyCtxt = tc.context().pushClass(this.anonType, this.anonType);
        Job sj = tc.job().spawn(bodyCtxt, this.body, Pass.CLEAN_SUPER, Pass.DISAM_ALL);
        if (!sj.status()) {
            if (!sj.reportedErrors()) {
                throw new SemanticException(new StringBuffer().append("Could not disambiguate body of anonymous ").append(superType.flags().isInterface() ? "implementor" : "subclass").append(" of \"").append(superType).append("\".").toString());
            }
            throw new SemanticException();
        }
        ClassBody b = (ClassBody) sj.ast();
        TypeChecker bodyTC = (TypeChecker) tc.context(bodyCtxt);
        ClassBody b2 = (ClassBody) visitChild(b, bodyTC.visitChildren());
        bodyTC.typeSystem().checkClassConformance(anonType());
        return b2;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        if (child == this.qualifier) {
            ReferenceType t = this.ci.container();
            if (t.isClass() && t.toClass().isMember()) {
                return t.toClass().container();
            }
            return child.type();
        }
        Iterator i = this.arguments.iterator();
        Iterator j = this.ci.formalTypes().iterator();
        while (i.hasNext() && j.hasNext()) {
            Expr e = (Expr) i.next();
            Type t2 = (Type) j.next();
            if (e == child) {
                return t2;
            }
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
        if (this.ci == null) {
            throw new InternalCompilerError(position(), "Null constructor instance after type check.");
        }
        for (Type t : this.ci.throwTypes()) {
            ec.throwsException(t, position());
        }
        return super.exceptionCheck(ec);
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return Precedence.LITERAL;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.qualifier != null ? new StringBuffer().append(this.qualifier.toString()).append(".").toString() : "").append("new ").append(this.tn).append("(...)").append(this.body != null ? new StringBuffer().append(Instruction.argsep).append(this.body).toString() : "").toString();
    }

    protected void printQualifier(CodeWriter w, PrettyPrinter tr) {
        if (this.qualifier != null) {
            print(this.qualifier, w, tr);
            w.write(".");
        }
    }

    protected void printArgs(CodeWriter w, PrettyPrinter tr) {
        w.write("(");
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
        w.write(")");
    }

    protected void printBody(CodeWriter w, PrettyPrinter tr) {
        if (this.body != null) {
            w.write(" {");
            print(this.body, w, tr);
            w.write("}");
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        printQualifier(w, tr);
        w.write("new ");
        print(this.tn, w, tr);
        printArgs(w, tr);
        printBody(w, tr);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void translate(CodeWriter w, Translator tr) {
        w.write("new ");
        if (this.qualifier != null) {
            ClassType ct = this.tn.type().toClass();
            if (!ct.isMember()) {
                throw new InternalCompilerError("Cannot qualify a non-member class.", position());
            }
            tr.setOuterClass(ct.outer());
            print(this.tn, w, tr);
            tr.setOuterClass(null);
        } else {
            print(this.tn, w, tr);
        }
        printArgs(w, tr);
        printBody(w, tr);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        if (this.qualifier != null) {
            return this.qualifier.entry();
        }
        Term afterArgs = this;
        if (body() != null) {
            afterArgs = body();
        }
        return listEntry(this.arguments, afterArgs);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        Term afterArgs = this;
        if (body() != null) {
            afterArgs = body();
        }
        if (this.qualifier != null) {
            v.visitCFG(this.qualifier, listEntry(this.arguments, afterArgs));
        }
        v.visitCFGList(this.arguments, afterArgs);
        if (body() != null) {
            v.visitCFG(body(), this);
        }
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
