package com.oreilly.servlet;

import com.google.common.net.HttpHeaders;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.methods.HttpGet;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/CacheHttpServlet.class */
public abstract class CacheHttpServlet extends HttpServlet {
    CacheHttpServletResponse cacheResponse;
    long cacheLastMod = -1;
    String cacheQueryString = null;
    String cachePathInfo = null;
    String cacheServletPath = null;
    Object lock = new Object();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javax.servlet.http.HttpServlet
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals(HttpGet.METHOD_NAME)) {
            super.service(req, res);
            return;
        }
        long servletLastMod = getLastModified(req);
        if (servletLastMod == -1) {
            super.service(req, res);
        } else if ((servletLastMod / 1000) * 1000 <= req.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE)) {
            res.setStatus(304);
        } else {
            CacheHttpServletResponse localResponseCopy = null;
            synchronized (this.lock) {
                if (servletLastMod <= this.cacheLastMod && this.cacheResponse.isValid() && equal(this.cacheQueryString, req.getQueryString()) && equal(this.cachePathInfo, req.getPathInfo()) && equal(this.cacheServletPath, req.getServletPath())) {
                    localResponseCopy = this.cacheResponse;
                }
            }
            if (localResponseCopy != null) {
                localResponseCopy.writeTo(res);
                return;
            }
            CacheHttpServletResponse localResponseCopy2 = new CacheHttpServletResponse(res);
            super.service(req, (HttpServletResponse) localResponseCopy2);
            synchronized (this.lock) {
                this.cacheResponse = localResponseCopy2;
                this.cacheLastMod = servletLastMod;
                this.cacheQueryString = req.getQueryString();
                this.cachePathInfo = req.getPathInfo();
                this.cacheServletPath = req.getServletPath();
            }
        }
    }

    private boolean equal(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1 == null || s2 == null) {
            return false;
        }
        return s1.equals(s2);
    }
}
