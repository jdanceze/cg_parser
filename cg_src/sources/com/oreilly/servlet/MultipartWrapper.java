package com.oreilly.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/MultipartWrapper.class */
public class MultipartWrapper extends HttpServletRequestWrapper {
    MultipartRequest mreq;

    public MultipartWrapper(HttpServletRequest req, String dir) throws IOException {
        super(req);
        this.mreq = null;
        this.mreq = new MultipartRequest(req, dir);
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public Enumeration getParameterNames() {
        return this.mreq.getParameterNames();
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public String getParameter(String name) {
        return this.mreq.getParameter(name);
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public String[] getParameterValues(String name) {
        return this.mreq.getParameterValues(name);
    }

    @Override // javax.servlet.ServletRequestWrapper, javax.servlet.ServletRequest
    public Map getParameterMap() {
        Map map = new HashMap();
        Enumeration parameterNames = getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            map.put(name, this.mreq.getParameterValues(name));
        }
        return map;
    }

    public Enumeration getFileNames() {
        return this.mreq.getFileNames();
    }

    public String getFilesystemName(String name) {
        return this.mreq.getFilesystemName(name);
    }

    public String getContentType(String name) {
        return this.mreq.getContentType(name);
    }

    public File getFile(String name) {
        return this.mreq.getFile(name);
    }
}
