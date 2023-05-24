package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/LeafPropertyLoader.class */
public class LeafPropertyLoader extends Loader {
    private final TransducedAccessor xacc;

    public LeafPropertyLoader(TransducedAccessor xacc) {
        super(true);
        this.xacc = xacc;
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
    public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
        try {
            this.xacc.parse(state.getPrev().getTarget(), text);
        } catch (AccessorException e) {
            handleGenericException(e, true);
        } catch (RuntimeException e2) {
            handleParseConversionException(state, e2);
        }
    }
}
