package org.apache.tools.ant.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PushbackReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/LayoutPreservingProperties.class */
public class LayoutPreservingProperties extends Properties {
    private static final long serialVersionUID = 1;
    private String eol;
    private List<LogicalLine> logicalLines;
    private Map<String, Integer> keyedPairLines;
    private boolean removeComments;

    public LayoutPreservingProperties() {
        this.eol = System.lineSeparator();
        this.logicalLines = new ArrayList();
        this.keyedPairLines = new HashMap();
    }

    public LayoutPreservingProperties(Properties defaults) {
        super(defaults);
        this.eol = System.lineSeparator();
        this.logicalLines = new ArrayList();
        this.keyedPairLines = new HashMap();
    }

    public boolean isRemoveComments() {
        return this.removeComments;
    }

    public void setRemoveComments(boolean val) {
        this.removeComments = val;
    }

    @Override // java.util.Properties
    public void load(InputStream inStream) throws IOException {
        String s = readLines(inStream);
        byte[] ba = s.getBytes(StandardCharsets.ISO_8859_1);
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        super.load(bais);
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public Object put(Object key, Object value) throws NullPointerException {
        Object obj = super.put(key, value);
        innerSetProperty(key.toString(), value.toString());
        return obj;
    }

    @Override // java.util.Properties
    public Object setProperty(String key, String value) throws NullPointerException {
        Object obj = super.setProperty(key, value);
        innerSetProperty(key, value);
        return obj;
    }

    private void innerSetProperty(String key, String value) {
        String value2 = escapeValue(value);
        if (this.keyedPairLines.containsKey(key)) {
            Integer i = this.keyedPairLines.get(key);
            ((Pair) this.logicalLines.get(i.intValue())).setValue(value2);
            return;
        }
        String key2 = escapeName(key);
        Pair p = new Pair(key2, value2);
        p.setNew(true);
        this.keyedPairLines.put(key2, Integer.valueOf(this.logicalLines.size()));
        this.logicalLines.add(p);
    }

    @Override // java.util.Hashtable, java.util.Map
    public void clear() {
        super.clear();
        this.keyedPairLines.clear();
        this.logicalLines.clear();
    }

    @Override // java.util.Hashtable, java.util.Dictionary, java.util.Map
    public Object remove(Object key) {
        Object obj = super.remove(key);
        Integer i = this.keyedPairLines.remove(key);
        if (null != i) {
            if (this.removeComments) {
                removeCommentsEndingAt(i.intValue());
            }
            this.logicalLines.set(i.intValue(), null);
        }
        return obj;
    }

    @Override // java.util.Hashtable
    public Object clone() {
        LayoutPreservingProperties dolly = (LayoutPreservingProperties) super.clone();
        dolly.keyedPairLines = new HashMap(this.keyedPairLines);
        dolly.logicalLines = new ArrayList(this.logicalLines);
        int size = dolly.logicalLines.size();
        for (int j = 0; j < size; j++) {
            LogicalLine line = dolly.logicalLines.get(j);
            if (line instanceof Pair) {
                Pair p = (Pair) line;
                dolly.logicalLines.set(j, (Pair) p.clone());
            }
        }
        return dolly;
    }

    public void listLines(PrintStream out) {
        out.println("-- logical lines --");
        for (LogicalLine line : this.logicalLines) {
            if (line instanceof Blank) {
                out.println("blank:   \"" + line + "\"");
            } else if (line instanceof Comment) {
                out.println("comment: \"" + line + "\"");
            } else if (line instanceof Pair) {
                out.println("pair:    \"" + line + "\"");
            }
        }
    }

    public void saveAs(File dest) throws IOException {
        OutputStream fos = Files.newOutputStream(dest.toPath(), new OpenOption[0]);
        store(fos, (String) null);
        fos.close();
    }

    @Override // java.util.Properties
    public void store(OutputStream out, String header) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.ISO_8859_1);
        int skipLines = 0;
        int totalLines = this.logicalLines.size();
        if (header != null) {
            osw.write("#" + header + this.eol);
            if (totalLines > 0 && (this.logicalLines.get(0) instanceof Comment) && header.equals(this.logicalLines.get(0).toString().substring(1))) {
                skipLines = 1;
            }
        }
        if (totalLines > skipLines && (this.logicalLines.get(skipLines) instanceof Comment)) {
            try {
                DateUtils.parseDateFromHeader(this.logicalLines.get(skipLines).toString().substring(1));
                skipLines++;
            } catch (ParseException e) {
            }
        }
        osw.write("#" + DateUtils.getDateForHeader() + this.eol);
        boolean writtenSep = false;
        for (LogicalLine line : this.logicalLines.subList(skipLines, totalLines)) {
            if (line instanceof Pair) {
                if (((Pair) line).isNew() && !writtenSep) {
                    osw.write(this.eol);
                    writtenSep = true;
                }
                osw.write(line.toString() + this.eol);
            } else if (line != null) {
                osw.write(line.toString() + this.eol);
            }
        }
        osw.close();
    }

    private String readLines(InputStream is) throws IOException {
        LogicalLine line;
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.ISO_8859_1);
        PushbackReader pbr = new PushbackReader(isr, 1);
        if (!this.logicalLines.isEmpty()) {
            this.logicalLines.add(new Blank());
        }
        String s = readFirstLine(pbr);
        BufferedReader br = new BufferedReader(pbr);
        boolean continuation = false;
        boolean comment = false;
        StringBuilder fileBuffer = new StringBuilder();
        StringBuilder logicalLineBuffer = new StringBuilder();
        while (s != null) {
            fileBuffer.append(s).append(this.eol);
            if (continuation) {
                s = "\n" + s;
            } else {
                comment = s.matches("^[ \t\f]*[#!].*");
            }
            if (!comment) {
                continuation = requiresContinuation(s);
            }
            logicalLineBuffer.append(s);
            if (!continuation) {
                if (comment) {
                    line = new Comment(logicalLineBuffer.toString());
                } else if (logicalLineBuffer.toString().trim().isEmpty()) {
                    line = new Blank();
                } else {
                    line = new Pair(logicalLineBuffer.toString());
                    String key = unescape(((Pair) line).getName());
                    if (this.keyedPairLines.containsKey(key)) {
                        remove(key);
                    }
                    this.keyedPairLines.put(key, Integer.valueOf(this.logicalLines.size()));
                }
                this.logicalLines.add(line);
                logicalLineBuffer.setLength(0);
            }
            s = br.readLine();
        }
        return fileBuffer.toString();
    }

    private String readFirstLine(PushbackReader r) throws IOException {
        StringBuilder sb = new StringBuilder(80);
        int ch = r.read();
        boolean hasCR = false;
        this.eol = System.lineSeparator();
        while (true) {
            if (ch >= 0) {
                if (hasCR && ch != 10) {
                    r.unread(ch);
                    break;
                }
                if (ch == 13) {
                    this.eol = "\r";
                    hasCR = true;
                } else if (ch == 10) {
                    this.eol = hasCR ? "\r\n" : "\n";
                } else {
                    sb.append((char) ch);
                }
                ch = r.read();
            } else {
                break;
            }
        }
        return sb.toString();
    }

    private boolean requiresContinuation(String s) {
        char[] ca = s.toCharArray();
        int i = ca.length - 1;
        while (i > 0 && ca[i] == '\\') {
            i--;
        }
        int tb = (ca.length - i) - 1;
        return tb % 2 == 1;
    }

    private String unescape(String s) {
        char c;
        char[] ch = new char[s.length() + 1];
        s.getChars(0, s.length(), ch, 0);
        ch[s.length()] = '\n';
        StringBuilder buffy = new StringBuilder(s.length());
        int i = 0;
        while (i < ch.length && (c = ch[i]) != '\n') {
            if (c == '\\') {
                i++;
                char c2 = ch[i];
                if (c2 == 'n') {
                    buffy.append('\n');
                } else if (c2 == 'r') {
                    buffy.append('\r');
                } else if (c2 == 'f') {
                    buffy.append('\f');
                } else if (c2 == 't') {
                    buffy.append('\t');
                } else if (c2 == 'u') {
                    i += 4;
                    buffy.append(unescapeUnicode(ch, i + 1));
                } else {
                    buffy.append(c2);
                }
            } else {
                buffy.append(c);
            }
            i++;
        }
        return buffy.toString();
    }

    private char unescapeUnicode(char[] ch, int i) {
        String s = new String(ch, i, 4);
        return (char) Integer.parseInt(s, 16);
    }

    private String escapeValue(String s) {
        return escape(s, false);
    }

    private String escapeName(String s) {
        return escape(s, true);
    }

    private String escape(String s, boolean escapeAllSpaces) {
        if (s == null) {
            return null;
        }
        char[] ch = new char[s.length()];
        s.getChars(0, s.length(), ch, 0);
        StringBuilder buffy = new StringBuilder(s.length());
        boolean leadingSpace = true;
        for (char c : ch) {
            if (c == ' ') {
                if (escapeAllSpaces || leadingSpace) {
                    buffy.append("\\");
                }
            } else {
                leadingSpace = false;
            }
            int p = "\t\f\r\n\\:=#!".indexOf(c);
            if (p != -1) {
                buffy.append("\\").append((CharSequence) "tfrn\\:=#!", p, p + 1);
            } else if (c < ' ' || c > '~') {
                buffy.append(escapeUnicode(c));
            } else {
                buffy.append(c);
            }
        }
        return buffy.toString();
    }

    private String escapeUnicode(char ch) {
        return "\\" + ((Object) UnicodeUtil.EscapeUnicode(ch));
    }

    private void removeCommentsEndingAt(int pos) {
        int end = pos - 1;
        int pos2 = end;
        while (pos2 > 0 && (this.logicalLines.get(pos2) instanceof Blank)) {
            pos2--;
        }
        if (!(this.logicalLines.get(pos2) instanceof Comment)) {
            return;
        }
        while (pos2 >= 0 && (this.logicalLines.get(pos2) instanceof Comment)) {
            pos2--;
        }
        while (true) {
            pos2++;
            if (pos2 <= end) {
                this.logicalLines.set(pos2, null);
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/LayoutPreservingProperties$LogicalLine.class */
    public static abstract class LogicalLine implements Serializable {
        private static final long serialVersionUID = 1;
        private String text;

        public LogicalLine(String text) {
            this.text = text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String toString() {
            return this.text;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/LayoutPreservingProperties$Blank.class */
    public static class Blank extends LogicalLine {
        private static final long serialVersionUID = 1;

        public Blank() {
            super("");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/LayoutPreservingProperties$Comment.class */
    public class Comment extends LogicalLine {
        private static final long serialVersionUID = 1;

        public Comment(String text) {
            super(text);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/LayoutPreservingProperties$Pair.class */
    public static class Pair extends LogicalLine implements Cloneable {
        private static final long serialVersionUID = 1;
        private String name;
        private String value;
        private boolean added;

        public Pair(String text) {
            super(text);
            parsePair(text);
        }

        public Pair(String name, String value) {
            this(name + "=" + value);
        }

        public String getName() {
            return this.name;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
            setText(this.name + "=" + value);
        }

        public boolean isNew() {
            return this.added;
        }

        public void setNew(boolean val) {
            this.added = val;
        }

        public Object clone() {
            Object dolly = null;
            try {
                dolly = super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return dolly;
        }

        private void parsePair(String text) {
            int pos = findFirstSeparator(text);
            if (pos == -1) {
                this.name = text;
                setValue(null);
            } else {
                this.name = text.substring(0, pos);
                setValue(text.substring(pos + 1));
            }
            this.name = stripStart(this.name, " \t\f");
        }

        private String stripStart(String s, String chars) {
            if (s == null) {
                return null;
            }
            int i = 0;
            while (i < s.length() && chars.indexOf(s.charAt(i)) != -1) {
                i++;
            }
            if (i == s.length()) {
                return "";
            }
            return s.substring(i);
        }

        private int findFirstSeparator(String s) {
            return indexOfAny(s.replaceAll("\\\\\\\\", "__").replaceAll("\\\\=", "__").replaceAll("\\\\:", "__").replaceAll("\\\\ ", "__").replaceAll("\\\\t", "__"), " :=\t");
        }

        private int indexOfAny(String s, String chars) {
            if (s == null || chars == null) {
                return -1;
            }
            int p = s.length() + 1;
            for (int i = 0; i < chars.length(); i++) {
                int x = s.indexOf(chars.charAt(i));
                if (x != -1 && x < p) {
                    p = x;
                }
            }
            if (p == s.length() + 1) {
                return -1;
            }
            return p;
        }
    }
}
