package polyglot.types;

import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/ProcedureInstance.class */
public interface ProcedureInstance extends CodeInstance {
    List formalTypes();

    List throwTypes();

    String signature();

    String designator();

    boolean moreSpecific(ProcedureInstance procedureInstance);

    boolean hasFormals(List list);

    boolean throwsSubset(ProcedureInstance procedureInstance);

    boolean callValid(List list);

    boolean moreSpecificImpl(ProcedureInstance procedureInstance);

    boolean hasFormalsImpl(List list);

    boolean throwsSubsetImpl(ProcedureInstance procedureInstance);

    boolean callValidImpl(List list);
}
