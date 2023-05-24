package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.main.Report;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.MethodInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/ClassBody_c.class */
public class ClassBody_c extends Term_c implements ClassBody {
    protected List members;
    private static final Collection TOPICS = CollectionUtil.list(Report.types, Report.context);
    static Class class$polyglot$ast$ClassMember;

    public ClassBody_c(Position pos, List members) {
        super(pos);
        Class cls;
        if (class$polyglot$ast$ClassMember == null) {
            cls = class$("polyglot.ast.ClassMember");
            class$polyglot$ast$ClassMember = cls;
        } else {
            cls = class$polyglot$ast$ClassMember;
        }
        this.members = TypedList.copyAndCheck(members, cls, true);
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // polyglot.ast.ClassBody
    public List members() {
        return this.members;
    }

    @Override // polyglot.ast.ClassBody
    public ClassBody members(List members) {
        Class cls;
        ClassBody_c n = (ClassBody_c) copy();
        if (class$polyglot$ast$ClassMember == null) {
            cls = class$("polyglot.ast.ClassMember");
            class$polyglot$ast$ClassMember = cls;
        } else {
            cls = class$polyglot$ast$ClassMember;
        }
        n.members = TypedList.copyAndCheck(members, cls, true);
        return n;
    }

    @Override // polyglot.ast.ClassBody
    public ClassBody addMember(ClassMember member) {
        Class cls;
        ClassBody_c n = (ClassBody_c) copy();
        List l = new ArrayList(this.members.size() + 1);
        l.addAll(this.members);
        l.add(member);
        if (class$polyglot$ast$ClassMember == null) {
            cls = class$("polyglot.ast.ClassMember");
            class$polyglot$ast$ClassMember = cls;
        } else {
            cls = class$polyglot$ast$ClassMember;
        }
        n.members = TypedList.copyAndCheck(l, cls, true);
        return n;
    }

    protected ClassBody_c reconstruct(List members) {
        Class cls;
        if (!CollectionUtil.equals(members, this.members)) {
            ClassBody_c n = (ClassBody_c) copy();
            if (class$polyglot$ast$ClassMember == null) {
                cls = class$("polyglot.ast.ClassMember");
                class$polyglot$ast$ClassMember = cls;
            } else {
                cls = class$polyglot$ast$ClassMember;
            }
            n.members = TypedList.copyAndCheck(members, cls, true);
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        List members = visitList(this.members, v);
        return reconstruct(members);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
        if (ar.kind() == AmbiguityRemover.SUPER || ar.kind() == AmbiguityRemover.SIGNATURES) {
            return ar.bypassChildren(this);
        }
        return ar;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        if (ar.kind() == AmbiguityRemover.SIGNATURES) {
            List l = new ArrayList(this.members.size());
            Job j = ar.job();
            for (ClassMember n : this.members) {
                if (n instanceof ClassDecl) {
                    Job sj = j.spawn(ar.context(), n, Pass.CLEAN_SUPER, Pass.CLEAN_SUPER_ALL);
                    if (!sj.status()) {
                        if (!sj.reportedErrors()) {
                            throw new SemanticException("Could not disambiguate class member.", n.position());
                        }
                        throw new SemanticException();
                    }
                    ClassDecl m = (ClassDecl) sj.ast();
                    l.add(m.visit(ar.visitChildren()));
                } else {
                    l.add(n.visit(ar.visitChildren()));
                }
            }
            return members(l);
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return "{ ... }";
    }

    protected void duplicateFieldCheck(TypeChecker tc) throws SemanticException {
        ClassType type = tc.context().currentClass();
        ArrayList l = new ArrayList(type.fields());
        for (int i = 0; i < l.size(); i++) {
            FieldInstance fi = (FieldInstance) l.get(i);
            for (int j = i + 1; j < l.size(); j++) {
                FieldInstance fj2 = (FieldInstance) l.get(j);
                if (fi.name().equals(fj2.name())) {
                    throw new SemanticException(new StringBuffer().append("Duplicate field \"").append(fj2).append("\".").toString(), fj2.position());
                }
            }
        }
    }

    protected void duplicateConstructorCheck(TypeChecker tc) throws SemanticException {
        ClassType type = tc.context().currentClass();
        ArrayList l = new ArrayList(type.constructors());
        for (int i = 0; i < l.size(); i++) {
            ConstructorInstance ci = (ConstructorInstance) l.get(i);
            for (int j = i + 1; j < l.size(); j++) {
                ConstructorInstance cj = (ConstructorInstance) l.get(j);
                if (ci.hasFormals(cj.formalTypes())) {
                    throw new SemanticException(new StringBuffer().append("Duplicate constructor \"").append(cj).append("\".").toString(), cj.position());
                }
            }
        }
    }

    protected void duplicateMethodCheck(TypeChecker tc) throws SemanticException {
        ClassType type = tc.context().currentClass();
        TypeSystem ts = tc.typeSystem();
        ArrayList l = new ArrayList(type.methods());
        for (int i = 0; i < l.size(); i++) {
            MethodInstance mi = (MethodInstance) l.get(i);
            for (int j = i + 1; j < l.size(); j++) {
                MethodInstance mj = (MethodInstance) l.get(j);
                if (isSameMethod(ts, mi, mj)) {
                    throw new SemanticException(new StringBuffer().append("Duplicate method \"").append(mj).append("\".").toString(), mj.position());
                }
            }
        }
    }

    protected void duplicateMemberClassCheck(TypeChecker tc) throws SemanticException {
        ClassType type = tc.context().currentClass();
        tc.typeSystem();
        ArrayList l = new ArrayList(type.memberClasses());
        for (int i = 0; i < l.size(); i++) {
            ClassType mi = (ClassType) l.get(i);
            for (int j = i + 1; j < l.size(); j++) {
                ClassType mj = (ClassType) l.get(j);
                if (mi.name().equals(mj.name())) {
                    throw new SemanticException(new StringBuffer().append("Duplicate member type \"").append(mj).append("\".").toString(), mj.position());
                }
            }
        }
    }

    protected boolean isSameMethod(TypeSystem ts, MethodInstance mi, MethodInstance mj) {
        return mi.isSameMethod(mj);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        duplicateFieldCheck(tc);
        duplicateConstructorCheck(tc);
        duplicateMethodCheck(tc);
        duplicateMemberClassCheck(tc);
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public NodeVisitor exceptionCheckEnter(ExceptionChecker ec) throws SemanticException {
        return ec.pushNew();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if (!this.members.isEmpty()) {
            w.newline(4);
            w.begin(0);
            Iterator i = this.members.iterator();
            while (i.hasNext()) {
                ClassMember member = (ClassMember) i.next();
                printBlock(member, w, tr);
                if (i.hasNext()) {
                    w.newline(0);
                    w.newline(0);
                }
            }
            w.end();
            w.newline(0);
        }
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        return succs;
    }
}
