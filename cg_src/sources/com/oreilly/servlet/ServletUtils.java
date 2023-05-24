package com.oreilly.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/ServletUtils.class */
public class ServletUtils {
    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: com.oreilly.servlet.ServletUtils.returnFile(java.lang.String, java.io.OutputStream):void, file: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/ServletUtils.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:115)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0xa8
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    public static void returnFile(java.lang.String r0, java.io.OutputStream r1) throws java.io.FileNotFoundException, java.io.IOException {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: com.oreilly.servlet.ServletUtils.returnFile(java.lang.String, java.io.OutputStream):void, file: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/ServletUtils.class
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oreilly.servlet.ServletUtils.returnFile(java.lang.String, java.io.OutputStream):void");
    }

    public static void returnURL(URL url, OutputStream out) throws IOException {
        InputStream in = url.openStream();
        byte[] buf = new byte[4096];
        while (true) {
            int read = in.read(buf);
            if (read != -1) {
                out.write(buf, 0, read);
            } else {
                return;
            }
        }
    }

    public static void returnURL(URL url, Writer out) throws IOException {
        BufferedReader in;
        URLConnection con = url.openConnection();
        con.connect();
        String encoding = con.getContentEncoding();
        if (encoding == null) {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(url.openStream(), encoding));
        }
        char[] buf = new char[4096];
        while (true) {
            int read = in.read(buf);
            if (read != -1) {
                out.write(buf, 0, read);
            } else {
                return;
            }
        }
    }

    public static String getStackTraceAsString(Throwable t) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter((OutputStream) bytes, true);
        t.printStackTrace(writer);
        return bytes.toString();
    }

    public static Servlet getServlet(String name, ServletRequest req, ServletContext context) {
        try {
            Servlet servlet = context.getServlet(name);
            if (servlet != null) {
                return servlet;
            }
            Socket socket = new Socket(req.getServerName(), req.getServerPort());
            socket.setSoTimeout(4000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(new StringBuffer().append("GET /servlet/").append(name).append(" HTTP/1.0").toString());
            out.println();
            try {
                socket.getInputStream().read();
            } catch (InterruptedIOException e) {
            }
            out.close();
            return context.getServlet(name);
        } catch (Exception e2) {
            return null;
        }
    }

    public static String[] split(String str, String delim) {
        Vector v = new Vector();
        StringTokenizer tokenizer = new StringTokenizer(str, delim);
        while (tokenizer.hasMoreTokens()) {
            v.addElement(tokenizer.nextToken());
        }
        String[] ret = new String[v.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (String) v.elementAt(i);
        }
        return ret;
    }

    public static URL getResource(ServletContext context, String resource) throws IOException {
        if (resource == null) {
            throw new FileNotFoundException("Requested resource was null (passed in null)");
        }
        if (resource.endsWith("/") || resource.endsWith("\\") || resource.endsWith(".")) {
            throw new MalformedURLException("Path may not end with a slash or dot");
        }
        if (resource.indexOf("..") != -1) {
            throw new MalformedURLException("Path may not contain double dots");
        }
        String upperResource = resource.toUpperCase();
        if (upperResource.startsWith("/WEB-INF") || upperResource.startsWith("/META-INF")) {
            throw new MalformedURLException("Path may not begin with /WEB-INF or /META-INF");
        }
        if (upperResource.endsWith(".JSP")) {
            throw new MalformedURLException("Path may not end with .jsp");
        }
        URL url = context.getResource(resource);
        if (url == null) {
            throw new FileNotFoundException(new StringBuffer().append("Requested resource was null (").append(resource).append(")").toString());
        }
        return url;
    }
}
