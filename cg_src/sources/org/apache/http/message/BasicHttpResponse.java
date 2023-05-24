package org.apache.http.message;

import java.util.Locale;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.StatusLine;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/message/BasicHttpResponse.class */
public class BasicHttpResponse extends AbstractHttpMessage implements HttpResponse {
    private StatusLine statusline;
    private HttpEntity entity;
    private ReasonPhraseCatalog reasonCatalog;
    private Locale locale;

    public BasicHttpResponse(StatusLine statusline, ReasonPhraseCatalog catalog, Locale locale) {
        if (statusline == null) {
            throw new IllegalArgumentException("Status line may not be null.");
        }
        this.statusline = statusline;
        this.reasonCatalog = catalog;
        this.locale = locale != null ? locale : Locale.getDefault();
    }

    public BasicHttpResponse(StatusLine statusline) {
        this(statusline, (ReasonPhraseCatalog) null, (Locale) null);
    }

    public BasicHttpResponse(ProtocolVersion ver, int code, String reason) {
        this(new BasicStatusLine(ver, code, reason), (ReasonPhraseCatalog) null, (Locale) null);
    }

    @Override // org.apache.http.HttpMessage
    public ProtocolVersion getProtocolVersion() {
        return this.statusline.getProtocolVersion();
    }

    @Override // org.apache.http.HttpResponse
    public StatusLine getStatusLine() {
        return this.statusline;
    }

    @Override // org.apache.http.HttpResponse
    public HttpEntity getEntity() {
        return this.entity;
    }

    @Override // org.apache.http.HttpResponse
    public Locale getLocale() {
        return this.locale;
    }

    @Override // org.apache.http.HttpResponse
    public void setStatusLine(StatusLine statusline) {
        if (statusline == null) {
            throw new IllegalArgumentException("Status line may not be null");
        }
        this.statusline = statusline;
    }

    @Override // org.apache.http.HttpResponse
    public void setStatusLine(ProtocolVersion ver, int code) {
        this.statusline = new BasicStatusLine(ver, code, getReason(code));
    }

    @Override // org.apache.http.HttpResponse
    public void setStatusLine(ProtocolVersion ver, int code, String reason) {
        this.statusline = new BasicStatusLine(ver, code, reason);
    }

    @Override // org.apache.http.HttpResponse
    public void setStatusCode(int code) {
        ProtocolVersion ver = this.statusline.getProtocolVersion();
        this.statusline = new BasicStatusLine(ver, code, getReason(code));
    }

    @Override // org.apache.http.HttpResponse
    public void setReasonPhrase(String reason) {
        if (reason != null && (reason.indexOf(10) >= 0 || reason.indexOf(13) >= 0)) {
            throw new IllegalArgumentException("Line break in reason phrase.");
        }
        this.statusline = new BasicStatusLine(this.statusline.getProtocolVersion(), this.statusline.getStatusCode(), reason);
    }

    @Override // org.apache.http.HttpResponse
    public void setEntity(HttpEntity entity) {
        this.entity = entity;
    }

    @Override // org.apache.http.HttpResponse
    public void setLocale(Locale loc) {
        if (loc == null) {
            throw new IllegalArgumentException("Locale may not be null.");
        }
        this.locale = loc;
        int code = this.statusline.getStatusCode();
        this.statusline = new BasicStatusLine(this.statusline.getProtocolVersion(), code, getReason(code));
    }

    protected String getReason(int code) {
        if (this.reasonCatalog == null) {
            return null;
        }
        return this.reasonCatalog.getReason(code, this.locale);
    }
}
