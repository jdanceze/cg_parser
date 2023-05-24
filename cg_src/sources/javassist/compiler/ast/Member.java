package javassist.compiler.ast;

import javassist.CtField;
import javassist.compiler.CompileError;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/compiler/ast/Member.class */
public class Member extends Symbol {
    private static final long serialVersionUID = 1;
    private CtField field;

    public Member(String name) {
        super(name);
        this.field = null;
    }

    public void setField(CtField f) {
        this.field = f;
    }

    public CtField getField() {
        return this.field;
    }

    @Override // javassist.compiler.ast.Symbol, javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atMember(this);
    }
}
