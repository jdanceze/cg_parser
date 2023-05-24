package com.oreilly.servlet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import net.bytebuddy.description.type.TypeDescription;
import org.apache.http.client.utils.URLEncodedUtils;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/HttpMessage.class */
public class HttpMessage {
    URL servlet;
    Hashtable headers = null;

    public HttpMessage(URL servlet) {
        this.servlet = null;
        this.servlet = servlet;
    }

    public InputStream sendGetMessage() throws IOException {
        return sendGetMessage(null);
    }

    public InputStream sendGetMessage(Properties args) throws IOException {
        String argString = "";
        if (args != null) {
            argString = new StringBuffer().append(TypeDescription.Generic.OfWildcardType.SYMBOL).append(toEncodedString(args)).toString();
        }
        URL url = new URL(new StringBuffer().append(this.servlet.toExternalForm()).append(argString).toString());
        URLConnection con = url.openConnection();
        con.setUseCaches(false);
        sendHeaders(con);
        return con.getInputStream();
    }

    public InputStream sendPostMessage() throws IOException {
        return sendPostMessage((Properties) null);
    }

    public InputStream sendPostMessage(Properties args) throws IOException {
        String argString = "";
        if (args != null) {
            argString = toEncodedString(args);
        }
        URLConnection con = this.servlet.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Content-Type", URLEncodedUtils.CONTENT_TYPE);
        sendHeaders(con);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(argString);
        out.flush();
        out.close();
        return con.getInputStream();
    }

    public InputStream sendPostMessage(Serializable obj) throws IOException {
        URLConnection con = this.servlet.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Content-Type", "application/x-java-serialized-object");
        sendHeaders(con);
        ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
        out.writeObject(obj);
        out.flush();
        out.close();
        return con.getInputStream();
    }

    public void setHeader(String name, String value) {
        if (this.headers == null) {
            this.headers = new Hashtable();
        }
        this.headers.put(name, value);
    }

    private void sendHeaders(URLConnection con) {
        if (this.headers != null) {
            Enumeration keys = this.headers.keys();
            while (keys.hasMoreElements()) {
                String name = (String) keys.nextElement();
                String value = (String) this.headers.get(name);
                con.setRequestProperty(name, value);
            }
        }
    }

    public void setCookie(String name, String value) {
        if (this.headers == null) {
            this.headers = new Hashtable();
        }
        String existingCookies = (String) this.headers.get("Cookie");
        if (existingCookies == null) {
            setHeader("Cookie", new StringBuffer().append(name).append("=").append(value).toString());
        } else {
            setHeader("Cookie", new StringBuffer().append(existingCookies).append("; ").append(name).append("=").append(value).toString());
        }
    }

    public void setAuthorization(String name, String password) {
        String authorization = Base64Encoder.encode(new StringBuffer().append(name).append(":").append(password).toString());
        setHeader("Authorization", new StringBuffer().append("Basic ").append(authorization).toString());
    }

    private String toEncodedString(Properties args) {
        StringBuffer buf = new StringBuffer();
        Enumeration names = args.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = args.getProperty(name);
            buf.append(new StringBuffer().append(URLEncoder.encode(name)).append("=").append(URLEncoder.encode(value)).toString());
            if (names.hasMoreElements()) {
                buf.append("&");
            }
        }
        return buf.toString();
    }
}
