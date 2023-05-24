package javax.xml.bind.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.transform.sax.SAXResult;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/util/JAXBResult.class */
public class JAXBResult extends SAXResult {
    private final UnmarshallerHandler unmarshallerHandler;

    public JAXBResult(JAXBContext context) throws JAXBException {
        this(context == null ? assertionFailed() : context.createUnmarshaller());
    }

    public JAXBResult(Unmarshaller _unmarshaller) throws JAXBException {
        if (_unmarshaller == null) {
            throw new JAXBException(Messages.format("JAXBResult.NullUnmarshaller"));
        }
        this.unmarshallerHandler = _unmarshaller.getUnmarshallerHandler();
        super.setHandler(this.unmarshallerHandler);
    }

    public Object getResult() throws JAXBException {
        return this.unmarshallerHandler.getResult();
    }

    private static Unmarshaller assertionFailed() throws JAXBException {
        throw new JAXBException(Messages.format("JAXBResult.NullContext"));
    }
}
