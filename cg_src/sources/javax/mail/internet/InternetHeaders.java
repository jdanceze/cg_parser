package javax.mail.internet;

import com.google.common.net.HttpHeaders;
import com.sun.mail.util.LineInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.mail.MessagingException;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/internet/InternetHeaders.class */
public class InternetHeaders {
    private Vector headers = new Vector();

    public InternetHeaders() {
        this.headers.addElement(new hdr("Return-Path", null));
        this.headers.addElement(new hdr("Received", null));
        this.headers.addElement(new hdr("Message-Id", null));
        this.headers.addElement(new hdr("Resent-Date", null));
        this.headers.addElement(new hdr("Date", null));
        this.headers.addElement(new hdr("Resent-From", null));
        this.headers.addElement(new hdr("From", null));
        this.headers.addElement(new hdr("Reply-To", null));
        this.headers.addElement(new hdr("Sender", null));
        this.headers.addElement(new hdr("To", null));
        this.headers.addElement(new hdr("Subject", null));
        this.headers.addElement(new hdr("Cc", null));
        this.headers.addElement(new hdr("In-Reply-To", null));
        this.headers.addElement(new hdr("Resent-Message-Id", null));
        this.headers.addElement(new hdr("Errors-To", null));
        this.headers.addElement(new hdr("Mime-Version", null));
        this.headers.addElement(new hdr("Content-Type", null));
        this.headers.addElement(new hdr("Content-Transfer-Encoding", null));
        this.headers.addElement(new hdr(HttpHeaders.CONTENT_MD5, null));
        this.headers.addElement(new hdr(":", null));
        this.headers.addElement(new hdr("Content-Length", null));
        this.headers.addElement(new hdr("Status", null));
    }

    public InternetHeaders(InputStream is) throws MessagingException {
        load(is);
    }

    public void load(InputStream is) throws MessagingException {
        String line;
        LineInputStream lis = new LineInputStream(is);
        String prevline = null;
        StringBuffer lineBuffer = new StringBuffer();
        do {
            try {
                line = lis.readLine();
                if (line != null && (line.startsWith(Instruction.argsep) || line.startsWith("\t"))) {
                    if (prevline != null) {
                        lineBuffer.append(prevline);
                        prevline = null;
                    }
                    lineBuffer.append("\r\n");
                    lineBuffer.append(line);
                } else {
                    if (prevline != null) {
                        addHeaderLine(prevline);
                    } else if (lineBuffer.length() > 0) {
                        addHeaderLine(lineBuffer.toString());
                        lineBuffer.setLength(0);
                    }
                    prevline = line;
                }
                if (line == null) {
                    break;
                }
            } catch (IOException ioex) {
                throw new MessagingException("Error in input stream", ioex);
            }
        } while (line.length() > 0);
    }

    public String[] getHeader(String name) {
        Enumeration e = this.headers.elements();
        Vector v = new Vector();
        while (e.hasMoreElements()) {
            hdr h = (hdr) e.nextElement();
            if (name.equalsIgnoreCase(h.name) && h.line != null) {
                v.addElement(h.getValue());
            }
        }
        if (v.size() == 0) {
            return null;
        }
        String[] r = new String[v.size()];
        v.copyInto(r);
        return r;
    }

    public String getHeader(String name, String delimiter) {
        String[] s = getHeader(name);
        if (s == null) {
            return null;
        }
        if (s.length == 1 || delimiter == null) {
            return s[0];
        }
        StringBuffer r = new StringBuffer(s[0]);
        for (int i = 1; i < s.length; i++) {
            r.append(delimiter);
            r.append(s[i]);
        }
        return r.toString();
    }

    public void setHeader(String name, String value) {
        int j;
        boolean found = false;
        int i = 0;
        while (i < this.headers.size()) {
            hdr h = (hdr) this.headers.elementAt(i);
            if (name.equalsIgnoreCase(h.name)) {
                if (!found) {
                    if (h.line != null && (j = h.line.indexOf(58)) >= 0) {
                        h.line = new StringBuffer().append(h.line.substring(0, j + 1)).append(Instruction.argsep).append(value).toString();
                    } else {
                        h.line = new StringBuffer().append(name).append(": ").append(value).toString();
                    }
                    found = true;
                } else {
                    this.headers.removeElementAt(i);
                    i--;
                }
            }
            i++;
        }
        if (!found) {
            addHeader(name, value);
        }
    }

    public void addHeader(String name, String value) {
        int pos = this.headers.size();
        boolean isReceived = name.equalsIgnoreCase("Received");
        if (isReceived) {
            pos = 0;
        }
        for (int i = this.headers.size() - 1; i >= 0; i--) {
            hdr h = (hdr) this.headers.elementAt(i);
            if (name.equalsIgnoreCase(h.name)) {
                if (isReceived) {
                    pos = i;
                } else {
                    this.headers.insertElementAt(new hdr(name, value), i + 1);
                    return;
                }
            }
            if (h.name.equals(":")) {
                pos = i;
            }
        }
        this.headers.insertElementAt(new hdr(name, value), pos);
    }

    public void removeHeader(String name) {
        for (int i = 0; i < this.headers.size(); i++) {
            hdr h = (hdr) this.headers.elementAt(i);
            if (name.equalsIgnoreCase(h.name)) {
                h.line = null;
            }
        }
    }

    public Enumeration getAllHeaders() {
        return new matchEnum(this.headers, null, false, false);
    }

    public Enumeration getMatchingHeaders(String[] names) {
        return new matchEnum(this.headers, names, true, false);
    }

    public Enumeration getNonMatchingHeaders(String[] names) {
        return new matchEnum(this.headers, names, false, false);
    }

    public void addHeaderLine(String line) {
        try {
            char c = line.charAt(0);
            if (c == ' ' || c == '\t') {
                hdr h = (hdr) this.headers.lastElement();
                h.line = new StringBuffer().append(h.line).append("\r\n").append(line).toString();
            } else {
                this.headers.addElement(new hdr(line));
            }
        } catch (StringIndexOutOfBoundsException e) {
        } catch (NoSuchElementException e2) {
        }
    }

    public Enumeration getAllHeaderLines() {
        return getNonMatchingHeaderLines(null);
    }

    public Enumeration getMatchingHeaderLines(String[] names) {
        return new matchEnum(this.headers, names, true, true);
    }

    public Enumeration getNonMatchingHeaderLines(String[] names) {
        return new matchEnum(this.headers, names, false, true);
    }
}
