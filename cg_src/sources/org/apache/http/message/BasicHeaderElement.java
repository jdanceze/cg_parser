package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.LangUtils;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/message/BasicHeaderElement.class */
public class BasicHeaderElement implements HeaderElement, Cloneable {
    private final String name;
    private final String value;
    private final NameValuePair[] parameters;

    public BasicHeaderElement(String name, String value, NameValuePair[] parameters) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        this.name = name;
        this.value = value;
        if (parameters != null) {
            this.parameters = parameters;
        } else {
            this.parameters = new NameValuePair[0];
        }
    }

    public BasicHeaderElement(String name, String value) {
        this(name, value, null);
    }

    @Override // org.apache.http.HeaderElement
    public String getName() {
        return this.name;
    }

    @Override // org.apache.http.HeaderElement
    public String getValue() {
        return this.value;
    }

    @Override // org.apache.http.HeaderElement
    public NameValuePair[] getParameters() {
        return (NameValuePair[]) this.parameters.clone();
    }

    @Override // org.apache.http.HeaderElement
    public int getParameterCount() {
        return this.parameters.length;
    }

    @Override // org.apache.http.HeaderElement
    public NameValuePair getParameter(int index) {
        return this.parameters[index];
    }

    @Override // org.apache.http.HeaderElement
    public NameValuePair getParameterByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        NameValuePair found = null;
        int i = 0;
        while (true) {
            if (i >= this.parameters.length) {
                break;
            }
            NameValuePair current = this.parameters[i];
            if (!current.getName().equalsIgnoreCase(name)) {
                i++;
            } else {
                found = current;
                break;
            }
        }
        return found;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (object instanceof HeaderElement) {
            BasicHeaderElement that = (BasicHeaderElement) object;
            return this.name.equals(that.name) && LangUtils.equals(this.value, that.value) && LangUtils.equals((Object[]) this.parameters, (Object[]) that.parameters);
        }
        return false;
    }

    public int hashCode() {
        int hash = LangUtils.hashCode(17, this.name);
        int hash2 = LangUtils.hashCode(hash, this.value);
        for (int i = 0; i < this.parameters.length; i++) {
            hash2 = LangUtils.hashCode(hash2, this.parameters[i]);
        }
        return hash2;
    }

    public String toString() {
        CharArrayBuffer buffer = new CharArrayBuffer(64);
        buffer.append(this.name);
        if (this.value != null) {
            buffer.append("=");
            buffer.append(this.value);
        }
        for (int i = 0; i < this.parameters.length; i++) {
            buffer.append("; ");
            buffer.append(this.parameters[i]);
        }
        return buffer.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
