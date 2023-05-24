package com.oreilly.servlet.multipart;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.cli.HelpFormatter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/multipart/MultipartParser.class */
public class MultipartParser {
    private ServletInputStream in;
    private String boundary;
    private FilePart lastFilePart;
    private byte[] buf;
    private static String DEFAULT_ENCODING = "ISO-8859-1";
    private String encoding;

    public MultipartParser(HttpServletRequest req, int maxSize) throws IOException {
        this(req, maxSize, true, true);
    }

    public MultipartParser(HttpServletRequest req, int maxSize, boolean buffer, boolean limitLength) throws IOException {
        this(req, maxSize, buffer, limitLength, null);
    }

    public MultipartParser(HttpServletRequest req, int maxSize, boolean buffer, boolean limitLength, String encoding) throws IOException {
        String line;
        this.buf = new byte[8192];
        this.encoding = DEFAULT_ENCODING;
        if (encoding != null) {
            setEncoding(encoding);
        }
        String type = null;
        String type1 = req.getHeader("Content-Type");
        String type2 = req.getContentType();
        if (type1 == null && type2 != null) {
            type = type2;
        } else if (type2 == null && type1 != null) {
            type = type1;
        } else if (type1 != null && type2 != null) {
            type = type1.length() > type2.length() ? type1 : type2;
        }
        if (type == null || !type.toLowerCase().startsWith("multipart/form-data")) {
            throw new IOException("Posted content type isn't multipart/form-data");
        }
        int length = req.getContentLength();
        if (length > maxSize) {
            throw new IOException(new StringBuffer().append("Posted content length of ").append(length).append(" exceeds limit of ").append(maxSize).toString());
        }
        String boundary = extractBoundary(type);
        if (boundary == null) {
            throw new IOException("Separation boundary was not specified");
        }
        ServletInputStream in = req.getInputStream();
        in = buffer ? new BufferedServletInputStream(in) : in;
        this.in = limitLength ? new LimitedServletInputStream(in, length) : in;
        this.boundary = boundary;
        do {
            line = readLine();
            if (line == null) {
                throw new IOException("Corrupt form data: premature ending");
            }
        } while (!line.startsWith(boundary));
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Part readNextPart() throws IOException {
        String type;
        if (this.lastFilePart != null) {
            this.lastFilePart.getInputStream().close();
            this.lastFilePart = null;
        }
        Vector headers = new Vector();
        String line = readLine();
        if (line == null || line.length() == 0) {
            return null;
        }
        while (line != null && line.length() > 0) {
            String nextLine = null;
            boolean getNextLine = true;
            while (getNextLine) {
                nextLine = readLine();
                if (nextLine != null && (nextLine.startsWith(Instruction.argsep) || nextLine.startsWith("\t"))) {
                    line = new StringBuffer().append(line).append(nextLine).toString();
                } else {
                    getNextLine = false;
                }
            }
            headers.addElement(line);
            line = nextLine;
        }
        if (line == null) {
            return null;
        }
        String name = null;
        String filename = null;
        String origname = null;
        String contentType = "text/plain";
        Enumeration elements = headers.elements();
        while (elements.hasMoreElements()) {
            String headerline = (String) elements.nextElement();
            if (headerline.toLowerCase().startsWith("content-disposition:")) {
                String[] dispInfo = extractDispositionInfo(headerline);
                name = dispInfo[1];
                filename = dispInfo[2];
                origname = dispInfo[3];
            } else if (headerline.toLowerCase().startsWith("content-type:") && (type = extractContentType(headerline)) != null) {
                contentType = type;
            }
        }
        if (filename == null) {
            return new ParamPart(name, this.in, this.boundary, this.encoding);
        }
        if (filename.equals("")) {
            filename = null;
        }
        this.lastFilePart = new FilePart(name, this.in, this.boundary, contentType, filename, origname);
        return this.lastFilePart;
    }

    private String extractBoundary(String line) {
        int index = line.lastIndexOf("boundary=");
        if (index == -1) {
            return null;
        }
        String boundary = line.substring(index + 9);
        if (boundary.charAt(0) == '\"') {
            boundary = boundary.substring(1, boundary.lastIndexOf(34));
        }
        return new StringBuffer().append(HelpFormatter.DEFAULT_LONG_OPT_PREFIX).append(boundary).toString();
    }

    private String[] extractDispositionInfo(String line) throws IOException {
        String[] retval = new String[4];
        String line2 = line.toLowerCase();
        int start = line2.indexOf("content-disposition: ");
        int end = line2.indexOf(";");
        if (start == -1 || end == -1) {
            throw new IOException(new StringBuffer().append("Content disposition corrupt: ").append(line).toString());
        }
        String disposition = line2.substring(start + 21, end);
        if (!disposition.equals("form-data")) {
            throw new IOException(new StringBuffer().append("Invalid content disposition: ").append(disposition).toString());
        }
        int start2 = line2.indexOf("name=\"", end);
        int end2 = line2.indexOf("\"", start2 + 7);
        int startOffset = 6;
        if (start2 == -1 || end2 == -1) {
            start2 = line2.indexOf("name=", end2);
            end2 = line2.indexOf(";", start2 + 6);
            if (start2 == -1) {
                throw new IOException(new StringBuffer().append("Content disposition corrupt: ").append(line).toString());
            }
            if (end2 == -1) {
                end2 = line2.length();
            }
            startOffset = 5;
        }
        String name = line.substring(start2 + startOffset, end2);
        String filename = null;
        String origname = null;
        int start3 = line2.indexOf("filename=\"", end2 + 2);
        int end3 = line2.indexOf("\"", start3 + 10);
        if (start3 != -1 && end3 != -1) {
            filename = line.substring(start3 + 10, end3);
            origname = filename;
            int slash = Math.max(filename.lastIndexOf(47), filename.lastIndexOf(92));
            if (slash > -1) {
                filename = filename.substring(slash + 1);
            }
        }
        retval[0] = disposition;
        retval[1] = name;
        retval[2] = filename;
        retval[3] = origname;
        return retval;
    }

    private static String extractContentType(String line) throws IOException {
        String line2 = line.toLowerCase();
        int end = line2.indexOf(";");
        if (end == -1) {
            end = line2.length();
        }
        return line2.substring(13, end).trim();
    }

    private String readLine() throws IOException {
        int result;
        StringBuffer sbuf = new StringBuffer();
        do {
            result = this.in.readLine(this.buf, 0, this.buf.length);
            if (result != -1) {
                sbuf.append(new String(this.buf, 0, result, this.encoding));
            }
        } while (result == this.buf.length);
        if (sbuf.length() == 0) {
            return null;
        }
        int len = sbuf.length();
        if (len >= 2 && sbuf.charAt(len - 2) == '\r') {
            sbuf.setLength(len - 2);
        } else if (len >= 1 && sbuf.charAt(len - 1) == '\n') {
            sbuf.setLength(len - 1);
        }
        return sbuf.toString();
    }
}
