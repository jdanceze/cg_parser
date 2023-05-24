package polyglot.ast;

import java.util.List;
import polyglot.types.Flags;
import polyglot.types.MethodInstance;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/MethodDecl.class */
public interface MethodDecl extends ProcedureDecl {
    @Override // polyglot.ast.ProcedureDecl
    Flags flags();

    MethodDecl flags(Flags flags);

    TypeNode returnType();

    MethodDecl returnType(TypeNode typeNode);

    @Override // polyglot.ast.ProcedureDecl
    String name();

    MethodDecl name(String str);

    @Override // polyglot.ast.ProcedureDecl
    List formals();

    MethodDecl formals(List list);

    @Override // polyglot.ast.ProcedureDecl
    List throwTypes();

    MethodDecl throwTypes(List list);

    MethodInstance methodInstance();

    MethodDecl methodInstance(MethodInstance methodInstance);
}
