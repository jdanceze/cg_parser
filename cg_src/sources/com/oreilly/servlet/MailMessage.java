package com.oreilly.servlet;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javassist.compiler.TokenId;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/MailMessage.class */
public class MailMessage {
    String host;
    String from;
    Vector to;
    Vector cc;
    Hashtable headers;
    MailPrintStream out;
    BufferedReader in;
    Socket socket;

    public MailMessage() throws IOException {
        this(org.apache.tools.mail.MailMessage.DEFAULT_HOST);
    }

    public MailMessage(String host) throws IOException {
        this.host = host;
        this.to = new Vector();
        this.cc = new Vector();
        this.headers = new Hashtable();
        setHeader("X-Mailer", "com.oreilly.servlet.MailMessage (www.servlets.com)");
        connect();
        sendHelo();
    }

    public void from(String from) throws IOException {
        sendFrom(from);
        this.from = from;
    }

    public void to(String to) throws IOException {
        sendRcpt(to);
        this.to.addElement(to);
    }

    public void cc(String cc) throws IOException {
        sendRcpt(cc);
        this.cc.addElement(cc);
    }

    public void bcc(String bcc) throws IOException {
        sendRcpt(bcc);
    }

    public void setSubject(String subj) {
        this.headers.put("Subject", subj);
    }

    public void setHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public PrintStream getPrintStream() throws IOException {
        setFromHeader();
        setToHeader();
        setCcHeader();
        sendData();
        flushHeaders();
        return this.out;
    }

    void setFromHeader() {
        setHeader("From", this.from);
    }

    void setToHeader() {
        setHeader("To", vectorToList(this.to));
    }

    void setCcHeader() {
        if (!this.cc.isEmpty()) {
            setHeader("Cc", vectorToList(this.cc));
        }
    }

    String vectorToList(Vector v) {
        StringBuffer buf = new StringBuffer();
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            buf.append(e.nextElement());
            if (e.hasMoreElements()) {
                buf.append(", ");
            }
        }
        return buf.toString();
    }

    void flushHeaders() throws IOException {
        Enumeration e = this.headers.keys();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String value = (String) this.headers.get(name);
            this.out.println(new StringBuffer().append(name).append(": ").append(value).toString());
        }
        this.out.println();
        this.out.flush();
    }

    public void sendAndClose() throws IOException {
        sendDot();
        disconnect();
    }

    static String sanitizeAddress(String s) {
        int paramDepth = 0;
        int start = 0;
        int end = 0;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                paramDepth++;
                if (start == 0) {
                    end = i;
                }
            } else if (c == ')') {
                paramDepth--;
                if (end == 0) {
                    start = i + 1;
                }
            } else if (paramDepth == 0 && c == '<') {
                start = i + 1;
            } else if (paramDepth == 0 && c == '>') {
                end = i;
            }
        }
        if (end == 0) {
            end = len;
        }
        return s.substring(start, end);
    }

    void connect() throws IOException {
        this.socket = new Socket(this.host, 25);
        this.out = new MailPrintStream(new BufferedOutputStream(this.socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        getReady();
    }

    void getReady() throws IOException {
        String response = this.in.readLine();
        int[] ok = {220};
        if (!isResponseOK(response, ok)) {
            throw new IOException(new StringBuffer().append("Didn't get introduction from server: ").append(response).toString());
        }
    }

    void sendHelo() throws IOException {
        String local = InetAddress.getLocalHost().getHostName();
        int[] ok = {250};
        send(new StringBuffer().append("HELO ").append(local).toString(), ok);
    }

    void sendFrom(String from) throws IOException {
        int[] ok = {250};
        send(new StringBuffer().append("MAIL FROM: <").append(sanitizeAddress(from)).append(">").toString(), ok);
    }

    void sendRcpt(String rcpt) throws IOException {
        int[] ok = {250, 251};
        send(new StringBuffer().append("RCPT TO: <").append(sanitizeAddress(rcpt)).append(">").toString(), ok);
    }

    void sendData() throws IOException {
        int[] ok = {TokenId.PLUS_E};
        send("DATA", ok);
    }

    void sendDot() throws IOException {
        int[] ok = {250};
        send("\r\n.", ok);
    }

    void sendQuit() throws IOException {
        int[] ok = {221};
        send("QUIT", ok);
    }

    void send(String msg, int[] ok) throws IOException {
        this.out.rawPrint(new StringBuffer().append(msg).append("\r\n").toString());
        String response = this.in.readLine();
        if (!isResponseOK(response, ok)) {
            throw new IOException(new StringBuffer().append("Unexpected reply to command: ").append(msg).append(": ").append(response).toString());
        }
    }

    boolean isResponseOK(String response, int[] ok) {
        for (int i : ok) {
            if (response.startsWith(new StringBuffer().append("").append(i).toString())) {
                return true;
            }
        }
        return false;
    }

    void disconnect() throws IOException {
        if (this.out != null) {
            this.out.close();
        }
        if (this.in != null) {
            this.in.close();
        }
        if (this.socket != null) {
            this.socket.close();
        }
    }
}
