package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.util.Collection;
import java.util.Collections;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.namespace.QName;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/Loader.class */
public abstract class Loader {
    protected boolean expectText;

    /* JADX INFO: Access modifiers changed from: protected */
    public Loader(boolean expectText) {
        this.expectText = expectText;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Loader() {
    }

    public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
    }

    public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        reportUnexpectedChildElement(ea, true);
        state.setLoader(Discarder.INSTANCE);
        state.setReceiver(null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void reportUnexpectedChildElement(TagName ea, boolean canRecover) throws SAXException {
        if (canRecover) {
            UnmarshallingContext context = UnmarshallingContext.getInstance();
            if (!context.parent.hasEventHandler() || !context.shouldErrorBeReported()) {
                return;
            }
        }
        if (ea.uri != ea.uri.intern() || ea.local != ea.local.intern()) {
            reportError(Messages.UNINTERNED_STRINGS.format(new Object[0]), canRecover);
        } else {
            reportError(Messages.UNEXPECTED_ELEMENT.format(ea.uri, ea.local, computeExpectedElements()), canRecover);
        }
    }

    public Collection<QName> getExpectedChildElements() {
        return Collections.emptyList();
    }

    public Collection<QName> getExpectedAttributes() {
        return Collections.emptyList();
    }

    public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
        reportError(Messages.UNEXPECTED_TEXT.format(text.toString().replace('\r', ' ').replace('\n', ' ').replace('\t', ' ').trim()), true);
    }

    public final boolean expectText() {
        return this.expectText;
    }

    public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
    }

    private String computeExpectedElements() {
        StringBuilder r = new StringBuilder();
        for (QName n : getExpectedChildElements()) {
            if (r.length() != 0) {
                r.append(',');
            }
            r.append("<{").append(n.getNamespaceURI()).append('}').append(n.getLocalPart()).append('>');
        }
        if (r.length() == 0) {
            return "(none)";
        }
        return r.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void fireBeforeUnmarshal(JaxBeanInfo beanInfo, Object child, UnmarshallingContext.State state) throws SAXException {
        if (beanInfo.lookForLifecycleMethods()) {
            UnmarshallingContext context = state.getContext();
            Unmarshaller.Listener listener = context.parent.getListener();
            if (beanInfo.hasBeforeUnmarshalMethod()) {
                beanInfo.invokeBeforeUnmarshalMethod(context.parent, child, state.getPrev().getTarget());
            }
            if (listener != null) {
                listener.beforeUnmarshal(child, state.getPrev().getTarget());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void fireAfterUnmarshal(JaxBeanInfo beanInfo, Object child, UnmarshallingContext.State state) throws SAXException {
        if (beanInfo.lookForLifecycleMethods()) {
            UnmarshallingContext context = state.getContext();
            Unmarshaller.Listener listener = context.parent.getListener();
            if (beanInfo.hasAfterUnmarshalMethod()) {
                beanInfo.invokeAfterUnmarshalMethod(context.parent, child, state.getTarget());
            }
            if (listener != null) {
                listener.afterUnmarshal(child, state.getTarget());
            }
        }
    }

    protected static void handleGenericException(Exception e) throws SAXException {
        handleGenericException(e, false);
    }

    public static void handleGenericException(Exception e, boolean canRecover) throws SAXException {
        reportError(e.getMessage(), e, canRecover);
    }

    public static void handleGenericError(Error e) throws SAXException {
        reportError(e.getMessage(), false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void reportError(String msg, boolean canRecover) throws SAXException {
        reportError(msg, null, canRecover);
    }

    public static void reportError(String msg, Exception nested, boolean canRecover) throws SAXException {
        UnmarshallingContext context = UnmarshallingContext.getInstance();
        context.handleEvent(new ValidationEventImpl(canRecover ? 1 : 2, msg, context.getLocator().getLocation(), nested), canRecover);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void handleParseConversionException(UnmarshallingContext.State state, Exception e) throws SAXException {
        state.getContext().handleError(e);
    }
}
