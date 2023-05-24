package javax.xml.bind.util;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/util/ValidationEventCollector.class */
public class ValidationEventCollector implements ValidationEventHandler {
    private final List<ValidationEvent> events = new ArrayList();

    public ValidationEvent[] getEvents() {
        return (ValidationEvent[]) this.events.toArray(new ValidationEvent[this.events.size()]);
    }

    public void reset() {
        this.events.clear();
    }

    public boolean hasEvents() {
        return !this.events.isEmpty();
    }

    @Override // javax.xml.bind.ValidationEventHandler
    public boolean handleEvent(ValidationEvent event) {
        this.events.add(event);
        boolean retVal = true;
        switch (event.getSeverity()) {
            case 0:
                retVal = true;
                break;
            case 1:
                retVal = true;
                break;
            case 2:
                retVal = false;
                break;
            default:
                _assert(false, Messages.format("ValidationEventCollector.UnrecognizedSeverity", Integer.valueOf(event.getSeverity())));
                break;
        }
        return retVal;
    }

    private static void _assert(boolean b, String msg) {
        if (!b) {
            throw new InternalError(msg);
        }
    }
}
