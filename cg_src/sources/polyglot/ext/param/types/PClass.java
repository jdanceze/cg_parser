package polyglot.ext.param.types;

import java.util.List;
import polyglot.types.ClassType;
import polyglot.types.Importable;
import polyglot.types.SemanticException;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/PClass.class */
public interface PClass extends Importable {
    List formals();

    ClassType clazz();

    ClassType instantiate(Position position, List list) throws SemanticException;
}
