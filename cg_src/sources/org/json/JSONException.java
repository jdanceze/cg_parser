package org.json;
/* loaded from: gencallgraphv3.jar:json-20080701.jar:org/json/JSONException.class */
public class JSONException extends Exception {
    private Throwable cause;

    public JSONException(String message) {
        super(message);
    }

    public JSONException(Throwable t) {
        super(t.getMessage());
        this.cause = t;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
