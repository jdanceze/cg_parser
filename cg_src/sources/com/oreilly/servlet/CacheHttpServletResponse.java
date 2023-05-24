package com.oreilly.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
/* compiled from: CacheHttpServlet.java */
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/CacheHttpServletResponse.class */
class CacheHttpServletResponse implements HttpServletResponse {
    private int status;
    private Hashtable headers;
    private int contentLength;
    private String contentType;
    private Locale locale;
    private Vector cookies;
    private boolean didError;
    private boolean didRedirect;
    private boolean gotStream;
    private boolean gotWriter;
    private HttpServletResponse delegate;
    private CacheServletOutputStream out;
    private PrintWriter writer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CacheHttpServletResponse(HttpServletResponse res) {
        this.delegate = res;
        try {
            this.out = new CacheServletOutputStream(res.getOutputStream());
        } catch (IOException e) {
            System.out.println(new StringBuffer().append("Got IOException constructing cached response: ").append(e.getMessage()).toString());
        }
        internalReset();
    }

    private void internalReset() {
        this.status = 200;
        this.headers = new Hashtable();
        this.contentLength = -1;
        this.contentType = null;
        this.locale = null;
        this.cookies = new Vector();
        this.didError = false;
        this.didRedirect = false;
        this.gotStream = false;
        this.gotWriter = false;
        this.out.getBuffer().reset();
    }

    public boolean isValid() {
        return (this.didError || this.didRedirect) ? false : true;
    }

    private void internalSetHeader(String name, Object value) {
        Vector v = new Vector();
        v.addElement(value);
        this.headers.put(name, v);
    }

    private void internalAddHeader(String name, Object value) {
        Vector v = (Vector) this.headers.get(name);
        if (v == null) {
            v = new Vector();
        }
        v.addElement(value);
        this.headers.put(name, v);
    }

    public void writeTo(HttpServletResponse res) {
        res.setStatus(this.status);
        if (this.contentType != null) {
            res.setContentType(this.contentType);
        }
        if (this.locale != null) {
            res.setLocale(this.locale);
        }
        Enumeration elements = this.cookies.elements();
        while (elements.hasMoreElements()) {
            Cookie c = (Cookie) elements.nextElement();
            res.addCookie(c);
        }
        Enumeration keys = this.headers.keys();
        while (keys.hasMoreElements()) {
            String name = (String) keys.nextElement();
            Vector values = (Vector) this.headers.get(name);
            Enumeration enum2 = values.elements();
            while (enum2.hasMoreElements()) {
                Object value = enum2.nextElement();
                if (value instanceof String) {
                    res.setHeader(name, (String) value);
                }
                if (value instanceof Integer) {
                    res.setIntHeader(name, ((Integer) value).intValue());
                }
                if (value instanceof Long) {
                    res.setDateHeader(name, ((Long) value).longValue());
                }
            }
        }
        res.setContentLength(this.out.getBuffer().size());
        try {
            this.out.getBuffer().writeTo(res.getOutputStream());
        } catch (IOException e) {
            System.out.println(new StringBuffer().append("Got IOException writing cached response: ").append(e.getMessage()).toString());
        }
    }

    @Override // javax.servlet.ServletResponse
    public ServletOutputStream getOutputStream() throws IOException {
        if (this.gotWriter) {
            throw new IllegalStateException("Cannot get output stream after getting writer");
        }
        this.gotStream = true;
        return this.out;
    }

    @Override // javax.servlet.ServletResponse
    public PrintWriter getWriter() throws UnsupportedEncodingException {
        if (this.gotStream) {
            throw new IllegalStateException("Cannot get writer after getting output stream");
        }
        this.gotWriter = true;
        if (this.writer == null) {
            OutputStreamWriter w = new OutputStreamWriter(this.out, getCharacterEncoding());
            this.writer = new PrintWriter((Writer) w, true);
        }
        return this.writer;
    }

    @Override // javax.servlet.ServletResponse
    public void setContentLength(int len) {
        this.delegate.setContentLength(len);
    }

    @Override // javax.servlet.ServletResponse
    public void setContentType(String type) {
        this.delegate.setContentType(type);
        this.contentType = type;
    }

    @Override // javax.servlet.ServletResponse
    public String getCharacterEncoding() {
        return this.delegate.getCharacterEncoding();
    }

    @Override // javax.servlet.ServletResponse
    public void setBufferSize(int size) throws IllegalStateException {
        this.delegate.setBufferSize(size);
    }

    @Override // javax.servlet.ServletResponse
    public int getBufferSize() {
        return this.delegate.getBufferSize();
    }

    @Override // javax.servlet.ServletResponse
    public void reset() throws IllegalStateException {
        this.delegate.reset();
        internalReset();
    }

    @Override // javax.servlet.ServletResponse
    public void resetBuffer() throws IllegalStateException {
        this.delegate.resetBuffer();
        this.contentLength = -1;
        this.out.getBuffer().reset();
    }

    @Override // javax.servlet.ServletResponse
    public boolean isCommitted() {
        return this.delegate.isCommitted();
    }

    @Override // javax.servlet.ServletResponse
    public void flushBuffer() throws IOException {
        this.delegate.flushBuffer();
    }

    @Override // javax.servlet.ServletResponse
    public void setLocale(Locale loc) {
        this.delegate.setLocale(loc);
        this.locale = loc;
    }

    @Override // javax.servlet.ServletResponse
    public Locale getLocale() {
        return this.delegate.getLocale();
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void addCookie(Cookie cookie) {
        this.delegate.addCookie(cookie);
        this.cookies.addElement(cookie);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public boolean containsHeader(String name) {
        return this.delegate.containsHeader(name);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void setStatus(int sc, String sm) {
        this.delegate.setStatus(sc, sm);
        this.status = sc;
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void setStatus(int sc) {
        this.delegate.setStatus(sc);
        this.status = sc;
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void setHeader(String name, String value) {
        this.delegate.setHeader(name, value);
        internalSetHeader(name, value);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void setIntHeader(String name, int value) {
        this.delegate.setIntHeader(name, value);
        internalSetHeader(name, new Integer(value));
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void setDateHeader(String name, long date) {
        this.delegate.setDateHeader(name, date);
        internalSetHeader(name, new Long(date));
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void sendError(int sc, String msg) throws IOException {
        this.delegate.sendError(sc, msg);
        this.didError = true;
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void sendError(int sc) throws IOException {
        this.delegate.sendError(sc);
        this.didError = true;
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void sendRedirect(String location) throws IOException {
        this.delegate.sendRedirect(location);
        this.didRedirect = true;
    }

    @Override // javax.servlet.http.HttpServletResponse
    public String encodeURL(String url) {
        return this.delegate.encodeURL(url);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public String encodeRedirectURL(String url) {
        return this.delegate.encodeRedirectURL(url);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void addHeader(String name, String value) {
        internalAddHeader(name, value);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void addIntHeader(String name, int value) {
        internalAddHeader(name, new Integer(value));
    }

    @Override // javax.servlet.http.HttpServletResponse
    public void addDateHeader(String name, long value) {
        internalAddHeader(name, new Long(value));
    }

    @Override // javax.servlet.http.HttpServletResponse
    public String encodeUrl(String url) {
        return encodeURL(url);
    }

    @Override // javax.servlet.http.HttpServletResponse
    public String encodeRedirectUrl(String url) {
        return encodeRedirectURL(url);
    }
}
