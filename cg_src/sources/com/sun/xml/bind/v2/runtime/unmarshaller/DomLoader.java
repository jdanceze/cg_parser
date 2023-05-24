package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.transform.Result;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/DomLoader.class */
public class DomLoader<ResultT extends Result> extends Loader {
    private final DomHandler<?, ResultT> dom;

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/DomLoader$State.class */
    private final class State {
        private TransformerHandler handler;
        private final ResultT result;
        int depth = 1;

        public State(UnmarshallingContext context) throws SAXException {
            this.handler = null;
            this.handler = JAXBContextImpl.createTransformerHandler(context.getJAXBContext().disableSecurityProcessing);
            this.result = (ResultT) DomLoader.this.dom.createUnmarshaller(context);
            this.handler.setResult(this.result);
            try {
                this.handler.setDocumentLocator(context.getLocator());
                this.handler.startDocument();
                declarePrefixes(context, context.getAllDeclaredPrefixes());
            } catch (SAXException e) {
                context.handleError(e);
                throw e;
            }
        }

        public Object getElement() {
            return DomLoader.this.dom.getElement(this.result);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void declarePrefixes(UnmarshallingContext context, String[] prefixes) throws SAXException {
            for (int i = prefixes.length - 1; i >= 0; i--) {
                String nsUri = context.getNamespaceURI(prefixes[i]);
                if (nsUri == null) {
                    throw new IllegalStateException("prefix '" + prefixes[i] + "' isn't bound");
                }
                this.handler.startPrefixMapping(prefixes[i], nsUri);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void undeclarePrefixes(String[] prefixes) throws SAXException {
            for (int i = prefixes.length - 1; i >= 0; i--) {
                this.handler.endPrefixMapping(prefixes[i]);
            }
        }
    }

    public DomLoader(DomHandler<?, ResultT> dom) {
        super(true);
        this.dom = dom;
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
    public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        UnmarshallingContext context = state.getContext();
        if (state.getTarget() == null) {
            state.setTarget(new State(context));
        }
        DomLoader<ResultT>.State s = (State) state.getTarget();
        try {
            s.declarePrefixes(context, context.getNewlyDeclaredPrefixes());
            ((State) s).handler.startElement(ea.uri, ea.local, ea.getQname(), ea.atts);
        } catch (SAXException e) {
            context.handleError(e);
            throw e;
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
    public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        state.setLoader(this);
        DomLoader<ResultT>.State s = (State) state.getPrev().getTarget();
        s.depth++;
        state.setTarget(s);
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
    public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
        if (text.length() == 0) {
            return;
        }
        try {
            DomLoader<ResultT>.State s = (State) state.getTarget();
            ((State) s).handler.characters(text.toString().toCharArray(), 0, text.length());
        } catch (SAXException e) {
            state.getContext().handleError(e);
            throw e;
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.Loader
    public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        DomLoader<ResultT>.State s = (State) state.getTarget();
        UnmarshallingContext context = state.getContext();
        try {
            ((State) s).handler.endElement(ea.uri, ea.local, ea.getQname());
            s.undeclarePrefixes(context.getNewlyDeclaredPrefixes());
            int i = s.depth - 1;
            s.depth = i;
            if (i == 0) {
                try {
                    s.undeclarePrefixes(context.getAllDeclaredPrefixes());
                    ((State) s).handler.endDocument();
                    state.setTarget(s.getElement());
                } catch (SAXException e) {
                    context.handleError(e);
                    throw e;
                }
            }
        } catch (SAXException e2) {
            context.handleError(e2);
            throw e2;
        }
    }
}
