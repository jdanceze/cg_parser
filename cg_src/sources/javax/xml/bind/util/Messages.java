package javax.xml.bind.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/util/Messages.class */
class Messages {
    static final String UNRECOGNIZED_SEVERITY = "ValidationEventCollector.UnrecognizedSeverity";
    static final String RESULT_NULL_CONTEXT = "JAXBResult.NullContext";
    static final String RESULT_NULL_UNMARSHALLER = "JAXBResult.NullUnmarshaller";
    static final String SOURCE_NULL_CONTEXT = "JAXBSource.NullContext";
    static final String SOURCE_NULL_CONTENT = "JAXBSource.NullContent";
    static final String SOURCE_NULL_MARSHALLER = "JAXBSource.NullMarshaller";

    Messages() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String format(String property) {
        return format(property, (Object[]) null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String format(String property, Object arg1) {
        return format(property, new Object[]{arg1});
    }

    static String format(String property, Object arg1, Object arg2) {
        return format(property, new Object[]{arg1, arg2});
    }

    static String format(String property, Object arg1, Object arg2, Object arg3) {
        return format(property, new Object[]{arg1, arg2, arg3});
    }

    static String format(String property, Object[] args) {
        String text = ResourceBundle.getBundle(Messages.class.getName()).getString(property);
        return MessageFormat.format(text, args);
    }
}
