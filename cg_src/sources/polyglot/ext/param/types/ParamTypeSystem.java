package polyglot.ext.param.types;

import java.util.List;
import java.util.Map;
import polyglot.types.ClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/ParamTypeSystem.class */
public interface ParamTypeSystem extends TypeSystem {
    MuPClass mutablePClass(Position position);

    ClassType instantiate(Position position, PClass pClass, List list) throws SemanticException;

    Type subst(Type type, Map map);

    Type subst(Type type, Map map, Map map2);

    Subst subst(Map map, Map map2);
}
