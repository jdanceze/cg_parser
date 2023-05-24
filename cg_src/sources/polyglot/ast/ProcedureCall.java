package polyglot.ast;

import java.util.List;
import polyglot.types.ProcedureInstance;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/ProcedureCall.class */
public interface ProcedureCall extends Term {
    List arguments();

    ProcedureCall arguments(List list);

    ProcedureInstance procedureInstance();
}
