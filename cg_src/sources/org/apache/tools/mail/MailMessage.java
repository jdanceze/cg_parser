package org.apache.tools.mail;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/mail/MailMessage.class */
public class MailMessage {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 25;
    private String host;
    private int port;
    private String from;
    private final Vector<String> replyto;
    private final Vector<String> to;
    private final Vector<String> cc;
    private final Map<String, String> headers;
    private MailPrintStream out;
    private SmtpResponseReader in;
    private Socket socket;
    private static final int OK_READY = 220;
    private static final int OK_HELO = 250;
    private static final int OK_FROM = 250;
    private static final int OK_RCPT_1 = 250;
    private static final int OK_RCPT_2 = 251;
    private static final int OK_DATA = 354;
    private static final int OK_DOT = 250;
    private static final int OK_QUIT = 221;

    public MailMessage() throws IOException {
        this(DEFAULT_HOST, 25);
    }

    public MailMessage(String host) throws IOException {
        this(host, 25);
    }

    public MailMessage(String host, int port) throws IOException {
        this.port = 25;
        this.replyto = new Vector<>();
        this.to = new Vector<>();
        this.cc = new Vector<>();
        this.headers = new LinkedHashMap();
        this.port = port;
        this.host = host;
        connect();
        sendHelo();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void from(String from) throws IOException {
        sendFrom(from);
        this.from = from;
    }

    public void replyto(String rto) {
        this.replyto.addElement(rto);
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
        setHeader("Subject", subj);
    }

    public void setHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public PrintStream getPrintStream() throws IOException {
        setFromHeader();
        setReplyToHeader();
        setToHeader();
        setCcHeader();
        setHeader("X-Mailer", "org.apache.tools.mail.MailMessage (ant.apache.org)");
        sendData();
        flushHeaders();
        return this.out;
    }

    void setFromHeader() {
        setHeader("From", this.from);
    }

    void setReplyToHeader() {
        if (!this.replyto.isEmpty()) {
            setHeader("Reply-To", vectorToList(this.replyto));
        }
    }

    void setToHeader() {
        if (!this.to.isEmpty()) {
            setHeader("To", vectorToList(this.to));
        }
    }

    void setCcHeader() {
        if (!this.cc.isEmpty()) {
            setHeader("Cc", vectorToList(this.cc));
        }
    }

    String vectorToList(Vector<String> v) {
        return String.join(", ", v);
    }

    void flushHeaders() throws IOException {
        this.headers.forEach(k, v -> {
            this.out.printf("%s: %s%n", k, v);
        });
        this.out.println();
        this.out.flush();
    }

    public void sendAndClose() throws IOException {
        try {
            sendDot();
            sendQuit();
        } finally {
            disconnect();
        }
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
        this.socket = new Socket(this.host, this.port);
        this.out = new MailPrintStream(new BufferedOutputStream(this.socket.getOutputStream()));
        this.in = new SmtpResponseReader(this.socket.getInputStream());
        getReady();
    }

    void getReady() throws IOException {
        String response = this.in.getResponse();
        int[] ok = {220};
        if (!isResponseOK(response, ok)) {
            throw new IOException("Didn't get introduction from server: " + response);
        }
    }

    void sendHelo() throws IOException {
        String local = InetAddress.getLocalHost().getCanonicalHostName();
        int[] ok = {250};
        send("HELO " + local, ok);
    }

    void sendFrom(String from) throws IOException {
        int[] ok = {250};
        send("MAIL FROM: <" + sanitizeAddress(from) + ">", ok);
    }

    void sendRcpt(String rcpt) throws IOException {
        int[] ok = {250, 251};
        send("RCPT TO: <" + sanitizeAddress(rcpt) + ">", ok);
    }

    void sendData() throws IOException {
        int[] ok = {354};
        send("DATA", ok);
    }

    void sendDot() throws IOException {
        int[] ok = {250};
        send("\r\n.", ok);
    }

    void sendQuit() throws IOException {
        int[] ok = {221};
        try {
            send("QUIT", ok);
        } catch (IOException e) {
            throw new ErrorInQuitException(e);
        }
    }

    void send(String msg, int[] ok) throws IOException {
        this.out.rawPrint(msg + "\r\n");
        String response = this.in.getResponse();
        if (!isResponseOK(response, ok)) {
            throw new IOException("Unexpected reply to command: " + msg + ": " + response);
        }
    }

    boolean isResponseOK(String response, int[] ok) {
        for (int status : ok) {
            if (response.startsWith("" + status)) {
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
            try {
                this.in.close();
            } catch (IOException e) {
            }
        }
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e2) {
            }
        }
    }
}
