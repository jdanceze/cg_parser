package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.CodeDecl;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.Formal;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.ClassType;
import polyglot.types.CodeInstance;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.ParsedClassType;
import polyglot.types.ProcedureInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.SubtypeSet;
import polyglot.util.TypedList;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/ConstructorDecl_c.class */
public class ConstructorDecl_c extends Term_c implements ConstructorDecl {
    protected Flags flags;
    protected String name;
    protected List formals;
    protected List throwTypes;
    protected Block body;
    protected ConstructorInstance ci;
    static Class class$polyglot$ast$Formal;
    static Class class$polyglot$ast$TypeNode;

    public ConstructorDecl_c(Position pos, Flags flags, String name, List formals, List throwTypes, Block body) {
        super(pos);
        Class cls;
        Class cls2;
        this.flags = flags;
        this.name = name;
        if (class$polyglot$ast$Formal == null) {
            cls = class$("polyglot.ast.Formal");
            class$polyglot$ast$Formal = cls;
        } else {
            cls = class$polyglot$ast$Formal;
        }
        this.formals = TypedList.copyAndCheck(formals, cls, true);
        if (class$polyglot$ast$TypeNode == null) {
            cls2 = class$("polyglot.ast.TypeNode");
            class$polyglot$ast$TypeNode = cls2;
        } else {
            cls2 = class$polyglot$ast$TypeNode;
        }
        this.throwTypes = TypedList.copyAndCheck(throwTypes, cls2, true);
        this.body = body;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ast.ConstructorDecl, polyglot.ast.ProcedureDecl
    public Flags flags() {
        return this.flags;
    }

    @Override // polyglot.ast.ConstructorDecl
    public ConstructorDecl flags(Flags flags) {
        ConstructorDecl_c n = (ConstructorDecl_c) copy();
        n.flags = flags;
        return n;
    }

    @Override // polyglot.ast.ConstructorDecl, polyglot.ast.ProcedureDecl
    public String name() {
        return this.name;
    }

    @Override // polyglot.ast.ConstructorDecl
    public ConstructorDecl name(String name) {
        ConstructorDecl_c n = (ConstructorDecl_c) copy();
        n.name = name;
        return n;
    }

    @Override // polyglot.ast.ConstructorDecl, polyglot.ast.ProcedureDecl
    public List formals() {
        return Collections.unmodifiableList(this.formals);
    }

    @Override // polyglot.ast.ConstructorDecl
    public ConstructorDecl formals(List formals) {
        Class cls;
        ConstructorDecl_c n = (ConstructorDecl_c) copy();
        if (class$polyglot$ast$Formal == null) {
            cls = class$("polyglot.ast.Formal");
            class$polyglot$ast$Formal = cls;
        } else {
            cls = class$polyglot$ast$Formal;
        }
        n.formals = TypedList.copyAndCheck(formals, cls, true);
        return n;
    }

    @Override // polyglot.ast.ConstructorDecl, polyglot.ast.ProcedureDecl
    public List throwTypes() {
        return Collections.unmodifiableList(this.throwTypes);
    }

    @Override // polyglot.ast.ConstructorDecl
    public ConstructorDecl throwTypes(List throwTypes) {
        Class cls;
        ConstructorDecl_c n = (ConstructorDecl_c) copy();
        if (class$polyglot$ast$TypeNode == null) {
            cls = class$("polyglot.ast.TypeNode");
            class$polyglot$ast$TypeNode = cls;
        } else {
            cls = class$polyglot$ast$TypeNode;
        }
        n.throwTypes = TypedList.copyAndCheck(throwTypes, cls, true);
        return n;
    }

    @Override // polyglot.ast.CodeDecl
    public Block body() {
        return this.body;
    }

    @Override // polyglot.ast.CodeDecl
    public CodeDecl body(Block body) {
        ConstructorDecl_c n = (ConstructorDecl_c) copy();
        n.body = body;
        return n;
    }

    @Override // polyglot.ast.ConstructorDecl
    public ConstructorInstance constructorInstance() {
        return this.ci;
    }

    @Override // polyglot.ast.ProcedureDecl
    public ProcedureInstance procedureInstance() {
        return this.ci;
    }

    @Override // polyglot.ast.CodeDecl
    public CodeInstance codeInstance() {
        return procedureInstance();
    }

    @Override // polyglot.ast.ConstructorDecl
    public ConstructorDecl constructorInstance(ConstructorInstance ci) {
        ConstructorDecl_c n = (ConstructorDecl_c) copy();
        n.ci = ci;
        return n;
    }

    protected ConstructorDecl_c reconstruct(List formals, List throwTypes, Block body) {
        Class cls;
        Class cls2;
        if (!CollectionUtil.equals(formals, this.formals) || !CollectionUtil.equals(throwTypes, this.throwTypes) || body != this.body) {
            ConstructorDecl_c n = (ConstructorDecl_c) copy();
            if (class$polyglot$ast$Formal == null) {
                cls = class$("polyglot.ast.Formal");
                class$polyglot$ast$Formal = cls;
            } else {
                cls = class$polyglot$ast$Formal;
            }
            n.formals = TypedList.copyAndCheck(formals, cls, true);
            if (class$polyglot$ast$TypeNode == null) {
                cls2 = class$("polyglot.ast.TypeNode");
                class$polyglot$ast$TypeNode = cls2;
            } else {
                cls2 = class$polyglot$ast$TypeNode;
            }
            n.throwTypes = TypedList.copyAndCheck(throwTypes, cls2, true);
            n.body = body;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        List formals = visitList(this.formals, v);
        List throwTypes = visitList(this.throwTypes, v);
        Block body = (Block) visitChild(this.body, v);
        return reconstruct(formals, throwTypes, body);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
        return tb.pushCode();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        TypeSystem ts = tb.typeSystem();
        List l = new ArrayList(this.formals.size());
        for (int i = 0; i < this.formals.size(); i++) {
            l.add(ts.unknownType(position()));
        }
        List m = new ArrayList(throwTypes().size());
        for (int i2 = 0; i2 < throwTypes().size(); i2++) {
            m.add(ts.unknownType(position()));
        }
        ConstructorInstance ci = ts.constructorInstance(position(), ts.Object(), Flags.NONE, l, m);
        return constructorInstance(ci);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
        if (ar.kind() == AmbiguityRemover.SUPER) {
            return ar.bypassChildren(this);
        }
        if (ar.kind() == AmbiguityRemover.SIGNATURES && this.body != null) {
            return ar.bypass(this.body);
        }
        return ar;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        if (ar.kind() == AmbiguityRemover.SIGNATURES) {
            Context c = ar.context();
            TypeSystem ts = ar.typeSystem();
            ParsedClassType ct = c.currentClassScope();
            ConstructorInstance ci = makeConstructorInstance(ct, ts);
            return constructorInstance(ci);
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor addMembersEnter(AddMemberVisitor am) {
        ParsedClassType ct = am.context().currentClassScope();
        ct.addConstructor(this.ci);
        return am.bypassChildren(this);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Context enterScope(Context c) {
        return c.pushCode(this.ci);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        Context c = tc.context();
        TypeSystem ts = tc.typeSystem();
        ClassType ct = c.currentClass();
        if (ct.flags().isInterface()) {
            throw new SemanticException("Cannot declare a constructor inside an interface.", position());
        }
        if (ct.isAnonymous()) {
            throw new SemanticException("Cannot declare a constructor inside an anonymous class.", position());
        }
        String ctName = ct.name();
        if (!ctName.equals(this.name)) {
            throw new SemanticException(new StringBuffer().append("Constructor name \"").append(this.name).append("\" does not match name of containing class \"").append(ctName).append("\".").toString(), position());
        }
        try {
            ts.checkConstructorFlags(flags());
            if (this.body == null && !flags().isNative()) {
                throw new SemanticException("Missing constructor body.", position());
            }
            if (this.body != null && flags().isNative()) {
                throw new SemanticException("A native constructor cannot have a body.", position());
            }
            for (TypeNode tn : throwTypes()) {
                Type t = tn.type();
                if (!t.isThrowable()) {
                    throw new SemanticException(new StringBuffer().append("Type \"").append(t).append("\" is not a subclass of \"").append(ts.Throwable()).append("\".").toString(), tn.position());
                }
            }
            return this;
        } catch (SemanticException e) {
            throw new SemanticException(e.getMessage(), position());
        }
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
        TypeSystem ts = ec.typeSystem();
        SubtypeSet s = ec.throwsSet();
        Iterator i = s.iterator();
        while (i.hasNext()) {
            Type t = (Type) i.next();
            boolean throwDeclared = false;
            if (!t.isUncheckedException()) {
                Iterator j = throwTypes().iterator();
                while (true) {
                    if (!j.hasNext()) {
                        break;
                    }
                    TypeNode tn = (TypeNode) j.next();
                    Type tj = tn.type();
                    if (ts.isSubtype(t, tj)) {
                        throwDeclared = true;
                        break;
                    }
                }
                if (!throwDeclared) {
                    ec.throwsSet().clear();
                    Position pos = ec.exceptionPosition(t);
                    throw new SemanticException(new StringBuffer().append("The exception \"").append(t).append("\" must either be caught or declared to be thrown.").toString(), pos == null ? position() : pos);
                }
            }
        }
        ec.throwsSet().clear();
        return super.exceptionCheck(ec);
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.flags.translate()).append(this.name).append("(...)").toString();
    }

    public void prettyPrintHeader(CodeWriter w, PrettyPrinter tr) {
        w.begin(0);
        w.write(flags().translate());
        w.write(this.name);
        w.write("(");
        w.begin(0);
        Iterator i = this.formals.iterator();
        while (i.hasNext()) {
            Formal f = (Formal) i.next();
            print(f, w, tr);
            if (i.hasNext()) {
                w.write(",");
                w.allowBreak(0, Instruction.argsep);
            }
        }
        w.end();
        w.write(")");
        if (!throwTypes().isEmpty()) {
            w.allowBreak(6);
            w.write("throws ");
            Iterator i2 = throwTypes().iterator();
            while (i2.hasNext()) {
                TypeNode tn = (TypeNode) i2.next();
                print(tn, w, tr);
                if (i2.hasNext()) {
                    w.write(",");
                    w.allowBreak(4, Instruction.argsep);
                }
            }
        }
        w.end();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        prettyPrintHeader(w, tr);
        if (this.body != null) {
            printSubStmt(this.body, w, tr);
        } else {
            w.write(";");
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        if (this.ci != null) {
            w.allowBreak(4, Instruction.argsep);
            w.begin(0);
            w.write(new StringBuffer().append("(instance ").append(this.ci).append(")").toString());
            w.end();
        }
    }

    protected ConstructorInstance makeConstructorInstance(ClassType ct, TypeSystem ts) throws SemanticException {
        List argTypes = new LinkedList();
        List excTypes = new LinkedList();
        for (Formal f : this.formals) {
            argTypes.add(f.declType());
        }
        for (TypeNode tn : throwTypes()) {
            excTypes.add(tn.type());
        }
        return ts.constructorInstance(position(), ct, this.flags, argTypes, excTypes);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return listEntry(formals(), body() == null ? this : body().entry());
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (body() == null) {
            v.visitCFGList(formals(), this);
        } else {
            v.visitCFGList(formals(), body().entry());
            v.visitCFG(body(), this);
        }
        return succs;
    }
}
