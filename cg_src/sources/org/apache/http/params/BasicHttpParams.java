package org.apache.http.params;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/params/BasicHttpParams.class */
public final class BasicHttpParams extends AbstractHttpParams implements Serializable, Cloneable {
    private static final long serialVersionUID = -7086398485908701455L;
    private final HashMap parameters = new HashMap();

    @Override // org.apache.http.params.HttpParams
    public Object getParameter(String name) {
        return this.parameters.get(name);
    }

    @Override // org.apache.http.params.HttpParams
    public HttpParams setParameter(String name, Object value) {
        this.parameters.put(name, value);
        return this;
    }

    @Override // org.apache.http.params.HttpParams
    public boolean removeParameter(String name) {
        if (this.parameters.containsKey(name)) {
            this.parameters.remove(name);
            return true;
        }
        return false;
    }

    public void setParameters(String[] names, Object value) {
        for (String str : names) {
            setParameter(str, value);
        }
    }

    public boolean isParameterSet(String name) {
        return getParameter(name) != null;
    }

    public boolean isParameterSetLocally(String name) {
        return this.parameters.get(name) != null;
    }

    public void clear() {
        this.parameters.clear();
    }

    @Override // org.apache.http.params.HttpParams
    public HttpParams copy() {
        BasicHttpParams clone = new BasicHttpParams();
        copyParams(clone);
        return clone;
    }

    public Object clone() throws CloneNotSupportedException {
        BasicHttpParams clone = (BasicHttpParams) super.clone();
        copyParams(clone);
        return clone;
    }

    protected void copyParams(HttpParams target) {
        for (Map.Entry me : this.parameters.entrySet()) {
            if (me.getKey() instanceof String) {
                target.setParameter((String) me.getKey(), me.getValue());
            }
        }
    }
}
