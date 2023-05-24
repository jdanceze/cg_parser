package javax.xml.bind.helpers;

import java.text.MessageFormat;
import java.util.ResourceBundle;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/helpers/Messages.class */
class Messages {
    static final String INPUTSTREAM_NOT_NULL = "AbstractUnmarshallerImpl.ISNotNull";
    static final String MUST_BE_BOOLEAN = "AbstractMarshallerImpl.MustBeBoolean";
    static final String MUST_BE_STRING = "AbstractMarshallerImpl.MustBeString";
    static final String SEVERITY_MESSAGE = "DefaultValidationEventHandler.SeverityMessage";
    static final String LOCATION_UNAVAILABLE = "DefaultValidationEventHandler.LocationUnavailable";
    static final String UNRECOGNIZED_SEVERITY = "DefaultValidationEventHandler.UnrecognizedSeverity";
    static final String WARNING = "DefaultValidationEventHandler.Warning";
    static final String ERROR = "DefaultValidationEventHandler.Error";
    static final String FATAL_ERROR = "DefaultValidationEventHandler.FatalError";
    static final String ILLEGAL_SEVERITY = "ValidationEventImpl.IllegalSeverity";
    static final String MUST_NOT_BE_NULL = "Shared.MustNotBeNull";

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

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String format(String property, Object arg1, Object arg2, Object arg3) {
        return format(property, new Object[]{arg1, arg2, arg3});
    }

    static String format(String property, Object[] args) {
        String text = ResourceBundle.getBundle(Messages.class.getName()).getString(property);
        return MessageFormat.format(text, args);
    }
}
