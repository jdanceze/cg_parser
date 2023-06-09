package org.apache.http.impl;

import java.util.Locale;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.ProtocolVersion;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/impl/DefaultHttpResponseFactory.class */
public class DefaultHttpResponseFactory implements HttpResponseFactory {
    protected final ReasonPhraseCatalog reasonCatalog;

    public DefaultHttpResponseFactory(ReasonPhraseCatalog catalog) {
        if (catalog == null) {
            throw new IllegalArgumentException("Reason phrase catalog must not be null.");
        }
        this.reasonCatalog = catalog;
    }

    public DefaultHttpResponseFactory() {
        this(EnglishReasonPhraseCatalog.INSTANCE);
    }

    @Override // org.apache.http.HttpResponseFactory
    public HttpResponse newHttpResponse(ProtocolVersion ver, int status, HttpContext context) {
        if (ver == null) {
            throw new IllegalArgumentException("HTTP version may not be null");
        }
        Locale loc = determineLocale(context);
        String reason = this.reasonCatalog.getReason(status, loc);
        StatusLine statusline = new BasicStatusLine(ver, status, reason);
        return new BasicHttpResponse(statusline, this.reasonCatalog, loc);
    }

    @Override // org.apache.http.HttpResponseFactory
    public HttpResponse newHttpResponse(StatusLine statusline, HttpContext context) {
        if (statusline == null) {
            throw new IllegalArgumentException("Status line may not be null");
        }
        Locale loc = determineLocale(context);
        return new BasicHttpResponse(statusline, this.reasonCatalog, loc);
    }

    protected Locale determineLocale(HttpContext context) {
        return Locale.getDefault();
    }
}
