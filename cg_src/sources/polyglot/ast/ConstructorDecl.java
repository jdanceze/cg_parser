package polyglot.ast;

import java.util.List;
import polyglot.types.ConstructorInstance;
import polyglot.types.Flags;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/ConstructorDecl.class */
public interface ConstructorDecl extends ProcedureDecl {
    @Override // polyglot.ast.ProcedureDecl
    Flags flags();

    ConstructorDecl flags(Flags flags);

    @Override // polyglot.ast.ProcedureDecl
    String name();

    ConstructorDecl name(String str);

    @Override // polyglot.ast.ProcedureDecl
    List formals();

    ConstructorDecl formals(List list);

    @Override // polyglot.ast.ProcedureDecl
    List throwTypes();

    ConstructorDecl throwTypes(List list);

    ConstructorInstance constructorInstance();

    ConstructorDecl constructorInstance(ConstructorInstance constructorInstance);
}
