package polyglot.types;

import java.io.Serializable;
import polyglot.util.Copy;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/TypeObject.class */
public interface TypeObject extends Copy, Serializable {
    boolean isCanonical();

    TypeSystem typeSystem();

    Position position();

    boolean equalsImpl(TypeObject typeObject);
}
