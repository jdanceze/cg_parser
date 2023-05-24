package polyglot.ext.param.types;

import java.util.List;
import polyglot.types.Type;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/param/types/InstType.class */
public interface InstType extends Type {
    PClass instantiatedFrom();

    List actuals();
}
