package polyglot.visit;

import java.io.IOException;
import java.util.Date;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.FieldDecl;
import polyglot.ast.IntLit;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.main.Version;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.InitializerInstance;
import polyglot.types.ParsedClassType;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.util.Position;
import polyglot.util.TypeEncoder;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/ClassSerializer.class */
public class ClassSerializer extends NodeVisitor {
    protected TypeEncoder te;
    protected ErrorQueue eq;
    protected Date date;
    protected TypeSystem ts;
    protected NodeFactory nf;
    protected Version ver;

    public ClassSerializer(TypeSystem ts, NodeFactory nf, Date date, ErrorQueue eq, Version ver) {
        this.ts = ts;
        this.nf = nf;
        this.te = new TypeEncoder(ts);
        this.eq = eq;
        this.date = date;
        this.ver = ver;
    }

    @Override // polyglot.visit.NodeVisitor
    public Node override(Node n) {
        if ((n instanceof ClassMember) && !(n instanceof ClassDecl)) {
            return n;
        }
        return null;
    }

    @Override // polyglot.visit.NodeVisitor
    public Node leave(Node old, Node n, NodeVisitor v) {
        if (!(n instanceof ClassDecl)) {
            return n;
        }
        try {
            ClassDecl cn = (ClassDecl) n;
            ClassBody body = cn.body();
            ParsedClassType ct = cn.type();
            ct.superType();
            ct.interfaces();
            ct.memberClasses();
            ct.constructors();
            ct.methods();
            ct.fields();
            if (!ct.isTopLevel() && !ct.isMember()) {
                return n;
            }
            String suffix = this.ver.name();
            if (ct.fieldNamed(new StringBuffer().append("jlc$CompilerVersion$").append(suffix).toString()) != null || ct.fieldNamed(new StringBuffer().append("jlc$SourceLastModified$").append(suffix).toString()) != null || ct.fieldNamed(new StringBuffer().append("jlc$ClassType$").append(suffix).toString()) != null) {
                this.eq.enqueue(5, "Cannot encode Polyglot type information more than once.");
                return n;
            }
            Flags flags = Flags.PUBLIC.set(Flags.STATIC).set(Flags.FINAL);
            String version = new StringBuffer().append(this.ver.major()).append(".").append(this.ver.minor()).append(".").append(this.ver.patch_level()).toString();
            Position pos = Position.COMPILER_GENERATED;
            FieldInstance fi = this.ts.fieldInstance(pos, ct, flags, this.ts.String(), new StringBuffer().append("jlc$CompilerVersion$").append(suffix).toString());
            InitializerInstance ii = this.ts.initializerInstance(pos, ct, Flags.STATIC);
            FieldDecl f = this.nf.FieldDecl(fi.position(), fi.flags(), this.nf.CanonicalTypeNode(fi.position(), fi.type()), fi.name(), this.nf.StringLit(pos, version).type(this.ts.String()));
            ClassBody body2 = body.addMember(f.fieldInstance(fi).initializerInstance(ii));
            long time = this.date.getTime();
            FieldInstance fi2 = this.ts.fieldInstance(pos, ct, flags, this.ts.Long(), new StringBuffer().append("jlc$SourceLastModified$").append(suffix).toString());
            InitializerInstance ii2 = this.ts.initializerInstance(pos, ct, Flags.STATIC);
            FieldDecl f2 = this.nf.FieldDecl(fi2.position(), fi2.flags(), this.nf.CanonicalTypeNode(fi2.position(), fi2.type()), fi2.name(), this.nf.IntLit(pos, IntLit.LONG, time).type(this.ts.Long()));
            ClassBody body3 = body2.addMember(f2.fieldInstance(fi2).initializerInstance(ii2));
            FieldInstance fi3 = this.ts.fieldInstance(pos, ct, flags, this.ts.String(), new StringBuffer().append("jlc$ClassType$").append(suffix).toString());
            InitializerInstance ii3 = this.ts.initializerInstance(pos, ct, Flags.STATIC);
            FieldDecl f3 = this.nf.FieldDecl(fi3.position(), fi3.flags(), this.nf.CanonicalTypeNode(fi3.position(), fi3.type()), fi3.name(), this.nf.StringLit(pos, this.te.encode(ct)).type(this.ts.String()));
            return cn.body(body3.addMember(f3.fieldInstance(fi3).initializerInstance(ii3)));
        } catch (IOException e) {
            this.eq.enqueue(2, "Unable to encode Polyglot type information.");
            return n;
        }
    }
}
