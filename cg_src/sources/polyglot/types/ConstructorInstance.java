package polyglot.types;

import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/ConstructorInstance.class */
public interface ConstructorInstance extends ProcedureInstance {
    ConstructorInstance flags(Flags flags);

    ConstructorInstance formalTypes(List list);

    ConstructorInstance throwTypes(List list);

    ConstructorInstance container(ClassType classType);
}
