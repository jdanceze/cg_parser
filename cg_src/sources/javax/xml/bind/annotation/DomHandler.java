package javax.xml.bind.annotation;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/annotation/DomHandler.class */
public interface DomHandler<ElementT, ResultT extends Result> {
    ResultT createUnmarshaller(ValidationEventHandler validationEventHandler);

    ElementT getElement(ResultT resultt);

    Source marshal(ElementT elementt, ValidationEventHandler validationEventHandler);
}
