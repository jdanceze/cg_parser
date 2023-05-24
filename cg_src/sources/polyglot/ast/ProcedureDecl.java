package polyglot.ast;

import java.util.List;
import polyglot.types.Flags;
import polyglot.types.ProcedureInstance;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/ProcedureDecl.class */
public interface ProcedureDecl extends CodeDecl {
    Flags flags();

    String name();

    List formals();

    List throwTypes();

    ProcedureInstance procedureInstance();
}
