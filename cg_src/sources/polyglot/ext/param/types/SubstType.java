package polyglot.ext.param.types;

import java.util.Iterator;
import polyglot.types.Type;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/SubstType.class */
public interface SubstType extends Type {
    Type base();

    Subst subst();

    Iterator entries();
}
