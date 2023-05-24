package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ConstructorCall;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.main.Report;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.Named;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/ClassDecl_c.class */
public class ClassDecl_c extends Term_c implements ClassDecl {
    protected Flags flags;
    protected String name;
    protected TypeNode superClass;
    protected List interfaces;
    protected ClassBody body;
    protected ParsedClassType type;
    static Class class$polyglot$ast$TypeNode;

    public ClassDecl_c(Position pos, Flags flags, String name, TypeNode superClass, List interfaces, ClassBody body) {
        super(pos);
        Class cls;
        this.flags = flags;
        this.name = name;
        this.superClass = superClass;
        if (class$polyglot$ast$TypeNode == null) {
            cls = class$("polyglot.ast.TypeNode");
            class$polyglot$ast$TypeNode = cls;
        } else {
            cls = class$polyglot$ast$TypeNode;
        }
        this.interfaces = TypedList.copyAndCheck(interfaces, cls, true);
        this.body = body;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ast.TopLevelDecl
    public Named declaration() {
        return type();
    }

    @Override // polyglot.ast.ClassDecl
    public ParsedClassType type() {
        return this.type;
    }

    @Override // polyglot.ast.ClassDecl
    public ClassDecl type(ParsedClassType type) {
        ClassDecl_c n = (ClassDecl_c) copy();
        n.type = type;
        return n;
    }

    @Override // polyglot.ast.ClassDecl, polyglot.ast.TopLevelDecl
    public Flags flags() {
        return this.flags;
    }

    @Override // polyglot.ast.ClassDecl
    public ClassDecl flags(Flags flags) {
        ClassDecl_c n = (ClassDecl_c) copy();
        n.flags = flags;
        return n;
    }

    @Override // polyglot.ast.ClassDecl, polyglot.ast.TopLevelDecl
    public String name() {
        return this.name;
    }

    @Override // polyglot.ast.ClassDecl
    public ClassDecl name(String name) {
        ClassDecl_c n = (ClassDecl_c) copy();
        n.name = name;
        return n;
    }

    @Override // polyglot.ast.ClassDecl
    public TypeNode superClass() {
        return this.superClass;
    }

    @Override // polyglot.ast.ClassDecl
    public ClassDecl superClass(TypeNode superClass) {
        ClassDecl_c n = (ClassDecl_c) copy();
        n.superClass = superClass;
        return n;
    }

    @Override // polyglot.ast.ClassDecl
    public List interfaces() {
        return this.interfaces;
    }

    @Override // polyglot.ast.ClassDecl
    public ClassDecl interfaces(List interfaces) {
        Class cls;
        ClassDecl_c n = (ClassDecl_c) copy();
        if (class$polyglot$ast$TypeNode == null) {
            cls = class$("polyglot.ast.TypeNode");
            class$polyglot$ast$TypeNode = cls;
        } else {
            cls = class$polyglot$ast$TypeNode;
        }
        n.interfaces = TypedList.copyAndCheck(interfaces, cls, true);
        return n;
    }

    @Override // polyglot.ast.ClassDecl
    public ClassBody body() {
        return this.body;
    }

    @Override // polyglot.ast.ClassDecl
    public ClassDecl body(ClassBody body) {
        ClassDecl_c n = (ClassDecl_c) copy();
        n.body = body;
        return n;
    }

    protected ClassDecl_c reconstruct(TypeNode superClass, List interfaces, ClassBody body) {
        Class cls;
        if (superClass != this.superClass || !CollectionUtil.equals(interfaces, this.interfaces) || body != this.body) {
            ClassDecl_c n = (ClassDecl_c) copy();
            n.superClass = superClass;
            if (class$polyglot$ast$TypeNode == null) {
                cls = class$("polyglot.ast.TypeNode");
                class$polyglot$ast$TypeNode = cls;
            } else {
                cls = class$polyglot$ast$TypeNode;
            }
            n.interfaces = TypedList.copyAndCheck(interfaces, cls, true);
            n.body = body;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return body().entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFG(body(), this);
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        TypeNode superClass = (TypeNode) visitChild(this.superClass, v);
        List interfaces = visitList(this.interfaces, v);
        ClassBody body = (ClassBody) visitChild(this.body, v);
        return reconstruct(superClass, interfaces, body);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
        TypeBuilder tb2 = tb.pushClass(position(), this.flags, this.name);
        ParsedClassType ct = tb2.currentClass();
        if (ct.isMember() && ct.outer().flags().isInterface()) {
            ct.flags(ct.flags().Public().Static());
        }
        if (ct.isMember() && ct.flags().isInterface()) {
            ct.flags(ct.flags().Static());
        }
        if (ct.flags().isInterface()) {
            ct.flags(ct.flags().Abstract());
        }
        return tb2;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        ParsedClassType type = tb.currentClass();
        if (type != null) {
            return type(type).flags(type.flags());
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Context enterScope(Node child, Context c) {
        if (child == this.body) {
            TypeSystem ts = c.typeSystem();
            c = c.pushClass(this.type, ts.staticTarget(this.type).toClass());
        }
        return super.enterScope(child, c);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
        if (ar.kind() == AmbiguityRemover.SUPER) {
            return ar.bypass(this.body);
        }
        return ar;
    }

    protected void disambiguateSuperType(AmbiguityRemover ar) throws SemanticException {
        TypeSystem ts = ar.typeSystem();
        if (this.superClass != null) {
            Type t = this.superClass.type();
            if (!t.isCanonical()) {
                throw new SemanticException(new StringBuffer().append("Could not disambiguate super class of ").append(this.type).append(".").toString(), this.superClass.position());
            }
            if (!t.isClass() || t.toClass().flags().isInterface()) {
                throw new SemanticException(new StringBuffer().append("Super class ").append(t).append(" of ").append(this.type).append(" is not a class.").toString(), this.superClass.position());
            }
            if (Report.should_report(Report.types, 3)) {
                Report.report(3, new StringBuffer().append("setting super type of ").append(this.type).append(" to ").append(t).toString());
            }
            this.type.superType(t);
            ts.checkCycles(t.toReference());
        } else if (ts.Object() != this.type && !ts.Object().fullName().equals(this.type.fullName())) {
            this.type.superType(ts.Object());
        } else {
            this.type.superType(null);
        }
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        if (ar.kind() == AmbiguityRemover.SIGNATURES) {
            Context ctxt = ar.context();
            type().inStaticContext(ctxt.inStaticContext());
        }
        if (ar.kind() == AmbiguityRemover.SIGNATURES) {
            ar.addSuperDependencies(type());
        }
        if (ar.kind() != AmbiguityRemover.SUPER) {
            return this;
        }
        TypeSystem ts = ar.typeSystem();
        if (Report.should_report(Report.types, 2)) {
            Report.report(2, new StringBuffer().append("Cleaning ").append(this.type).append(".").toString());
        }
        disambiguateSuperType(ar);
        for (TypeNode tn : this.interfaces) {
            Type t = tn.type();
            if (!t.isCanonical()) {
                throw new SemanticException(new StringBuffer().append("Could not disambiguate super class of ").append(this.type).append(".").toString(), tn.position());
            }
            if (!t.isClass() || !t.toClass().flags().isInterface()) {
                throw new SemanticException(new StringBuffer().append("Interface ").append(t).append(" of ").append(this.type).append(" is not an interface.").toString(), tn.position());
            }
            if (Report.should_report(Report.types, 3)) {
                Report.report(3, new StringBuffer().append("adding interface of ").append(this.type).append(" to ").append(t).toString());
            }
            if (!this.type.interfaces().contains(t)) {
                this.type.addInterface(t);
            }
            ts.checkCycles(t.toReference());
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node addMembers(AddMemberVisitor tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        NodeFactory nf = tc.nodeFactory();
        return addDefaultConstructorIfNeeded(ts, nf);
    }

    protected Node addDefaultConstructorIfNeeded(TypeSystem ts, NodeFactory nf) {
        if (defaultConstructorNeeded()) {
            return addDefaultConstructor(ts, nf);
        }
        return this;
    }

    protected boolean defaultConstructorNeeded() {
        if (flags().isInterface()) {
            return false;
        }
        return type().constructors().isEmpty();
    }

    protected Node addDefaultConstructor(TypeSystem ts, NodeFactory nf) {
        Block block;
        ConstructorInstance ci = ts.defaultConstructor(position(), this.type);
        this.type.addConstructor(ci);
        if (this.type.superType() instanceof ClassType) {
            ConstructorInstance sci = ts.defaultConstructor(position(), (ClassType) this.type.superType());
            ConstructorCall cc = nf.SuperCall(position(), Collections.EMPTY_LIST);
            block = nf.Block(position(), cc.constructorInstance(sci));
        } else {
            block = nf.Block(position());
        }
        ConstructorDecl cd = nf.ConstructorDecl(position(), Flags.PUBLIC, this.name, Collections.EMPTY_LIST, Collections.EMPTY_LIST, block);
        return body(this.body.addMember(cd.constructorInstance(ci)));
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x008a, code lost:
        if (type().isLocal() == false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x008d, code lost:
        r0 = r6.context();
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x009c, code lost:
        if (r0.isLocal(r5.name) == false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x009f, code lost:
        r0 = r0.find(r5.name);
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00b0, code lost:
        if ((r0 instanceof polyglot.types.Type) == false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00b3, code lost:
        r0 = (polyglot.types.Type) r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00c1, code lost:
        if (r0.isClass() == false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00d0, code lost:
        if (r0.toClass().isLocal() == false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0103, code lost:
        throw new polyglot.types.SemanticException(new java.lang.StringBuffer().append("Cannot declare local class \"").append(r5.type).append("\" within the same ").append("method, constructor or initializer as another ").append("local class of the same name.").toString(), position());
     */
    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public polyglot.ast.Node typeCheck(polyglot.visit.TypeChecker r6) throws polyglot.types.SemanticException {
        /*
            Method dump skipped, instructions count: 631
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: polyglot.ext.jl.ast.ClassDecl_c.typeCheck(polyglot.visit.TypeChecker):polyglot.ast.Node");
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.flags.clearInterface().translate()).append(this.flags.isInterface() ? "interface " : "class ").append(this.name).append(Instruction.argsep).append(this.body).toString();
    }

    public void prettyPrintHeader(CodeWriter w, PrettyPrinter tr) {
        if (this.flags.isInterface()) {
            w.write(this.flags.clearInterface().clearAbstract().translate());
        } else {
            w.write(this.flags.translate());
        }
        if (this.flags.isInterface()) {
            w.write("interface ");
        } else {
            w.write("class ");
        }
        w.write(this.name);
        if (superClass() != null) {
            w.write(" extends ");
            print(superClass(), w, tr);
        }
        if (!this.interfaces.isEmpty()) {
            if (this.flags.isInterface()) {
                w.write(" extends ");
            } else {
                w.write(" implements ");
            }
            Iterator i = interfaces().iterator();
            while (i.hasNext()) {
                TypeNode tn = (TypeNode) i.next();
                print(tn, w, tr);
                if (i.hasNext()) {
                    w.write(", ");
                }
            }
        }
        w.write(" {");
    }

    public void prettyPrintFooter(CodeWriter w, PrettyPrinter tr) {
        w.write("}");
        w.newline(0);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        prettyPrintHeader(w, tr);
        print(body(), w, tr);
        prettyPrintFooter(w, tr);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(name ").append(this.name).append(")").toString());
        w.end();
        if (this.type != null) {
            w.allowBreak(4, Instruction.argsep);
            w.begin(0);
            w.write(new StringBuffer().append("(type ").append(this.type).append(")").toString());
            w.end();
        }
    }
}
