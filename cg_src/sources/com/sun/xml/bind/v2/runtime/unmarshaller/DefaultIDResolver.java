package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.IDResolver;
import java.util.HashMap;
import java.util.concurrent.Callable;
import javax.xml.bind.ValidationEventHandler;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/DefaultIDResolver.class */
final class DefaultIDResolver extends IDResolver {
    private HashMap<String, Object> idmap = null;

    @Override // com.sun.xml.bind.IDResolver
    public void startDocument(ValidationEventHandler eventHandler) throws SAXException {
        if (this.idmap != null) {
            this.idmap.clear();
        }
    }

    @Override // com.sun.xml.bind.IDResolver
    public void bind(String id, Object obj) {
        if (this.idmap == null) {
            this.idmap = new HashMap<>();
        }
        this.idmap.put(id, obj);
    }

    @Override // com.sun.xml.bind.IDResolver
    public Callable resolve(final String id, Class targetType) {
        return new Callable() { // from class: com.sun.xml.bind.v2.runtime.unmarshaller.DefaultIDResolver.1
            @Override // java.util.concurrent.Callable
            public Object call() throws Exception {
                if (DefaultIDResolver.this.idmap == null) {
                    return null;
                }
                return DefaultIDResolver.this.idmap.get(id);
            }
        };
    }
}
