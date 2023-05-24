package org.apache.commons.cli;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:commons-cli-1.2.jar:org/apache/commons/cli/HelpFormatter.class */
public class HelpFormatter {
    public static final int DEFAULT_WIDTH = 74;
    public static final int DEFAULT_LEFT_PAD = 1;
    public static final int DEFAULT_DESC_PAD = 3;
    public static final String DEFAULT_SYNTAX_PREFIX = "usage: ";
    public static final String DEFAULT_OPT_PREFIX = "-";
    public static final String DEFAULT_LONG_OPT_PREFIX = "--";
    public static final String DEFAULT_ARG_NAME = "arg";
    public int defaultWidth = 74;
    public int defaultLeftPad = 1;
    public int defaultDescPad = 3;
    public String defaultSyntaxPrefix = DEFAULT_SYNTAX_PREFIX;
    public String defaultNewLine = System.getProperty("line.separator");
    public String defaultOptPrefix = DEFAULT_OPT_PREFIX;
    public String defaultLongOptPrefix = DEFAULT_LONG_OPT_PREFIX;
    public String defaultArgName = "arg";
    protected Comparator optionComparator = new OptionComparator(null);

    /* renamed from: org.apache.commons.cli.HelpFormatter$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:commons-cli-1.2.jar:org/apache/commons/cli/HelpFormatter$1.class */
    static class AnonymousClass1 {
    }

    public void setWidth(int width) {
        this.defaultWidth = width;
    }

    public int getWidth() {
        return this.defaultWidth;
    }

    public void setLeftPadding(int padding) {
        this.defaultLeftPad = padding;
    }

    public int getLeftPadding() {
        return this.defaultLeftPad;
    }

    public void setDescPadding(int padding) {
        this.defaultDescPad = padding;
    }

    public int getDescPadding() {
        return this.defaultDescPad;
    }

    public void setSyntaxPrefix(String prefix) {
        this.defaultSyntaxPrefix = prefix;
    }

    public String getSyntaxPrefix() {
        return this.defaultSyntaxPrefix;
    }

    public void setNewLine(String newline) {
        this.defaultNewLine = newline;
    }

    public String getNewLine() {
        return this.defaultNewLine;
    }

    public void setOptPrefix(String prefix) {
        this.defaultOptPrefix = prefix;
    }

    public String getOptPrefix() {
        return this.defaultOptPrefix;
    }

    public void setLongOptPrefix(String prefix) {
        this.defaultLongOptPrefix = prefix;
    }

    public String getLongOptPrefix() {
        return this.defaultLongOptPrefix;
    }

    public void setArgName(String name) {
        this.defaultArgName = name;
    }

    public String getArgName() {
        return this.defaultArgName;
    }

    public Comparator getOptionComparator() {
        return this.optionComparator;
    }

    public void setOptionComparator(Comparator comparator) {
        if (comparator == null) {
            this.optionComparator = new OptionComparator(null);
        } else {
            this.optionComparator = comparator;
        }
    }

    public void printHelp(String cmdLineSyntax, Options options) {
        printHelp(this.defaultWidth, cmdLineSyntax, null, options, null, false);
    }

    public void printHelp(String cmdLineSyntax, Options options, boolean autoUsage) {
        printHelp(this.defaultWidth, cmdLineSyntax, null, options, null, autoUsage);
    }

    public void printHelp(String cmdLineSyntax, String header, Options options, String footer) {
        printHelp(cmdLineSyntax, header, options, footer, false);
    }

    public void printHelp(String cmdLineSyntax, String header, Options options, String footer, boolean autoUsage) {
        printHelp(this.defaultWidth, cmdLineSyntax, header, options, footer, autoUsage);
    }

    public void printHelp(int width, String cmdLineSyntax, String header, Options options, String footer) {
        printHelp(width, cmdLineSyntax, header, options, footer, false);
    }

    public void printHelp(int width, String cmdLineSyntax, String header, Options options, String footer, boolean autoUsage) {
        PrintWriter pw = new PrintWriter(System.out);
        printHelp(pw, width, cmdLineSyntax, header, options, this.defaultLeftPad, this.defaultDescPad, footer, autoUsage);
        pw.flush();
    }

    public void printHelp(PrintWriter pw, int width, String cmdLineSyntax, String header, Options options, int leftPad, int descPad, String footer) {
        printHelp(pw, width, cmdLineSyntax, header, options, leftPad, descPad, footer, false);
    }

    public void printHelp(PrintWriter pw, int width, String cmdLineSyntax, String header, Options options, int leftPad, int descPad, String footer, boolean autoUsage) {
        if (cmdLineSyntax == null || cmdLineSyntax.length() == 0) {
            throw new IllegalArgumentException("cmdLineSyntax not provided");
        }
        if (autoUsage) {
            printUsage(pw, width, cmdLineSyntax, options);
        } else {
            printUsage(pw, width, cmdLineSyntax);
        }
        if (header != null && header.trim().length() > 0) {
            printWrapped(pw, width, header);
        }
        printOptions(pw, width, options, leftPad, descPad);
        if (footer != null && footer.trim().length() > 0) {
            printWrapped(pw, width, footer);
        }
    }

    public void printUsage(PrintWriter pw, int width, String app, Options options) {
        StringBuffer buff = new StringBuffer(this.defaultSyntaxPrefix).append(app).append(Instruction.argsep);
        Collection processedGroups = new ArrayList();
        List optList = new ArrayList(options.getOptions());
        Collections.sort(optList, getOptionComparator());
        Iterator i = optList.iterator();
        while (i.hasNext()) {
            Option option = (Option) i.next();
            OptionGroup group = options.getOptionGroup(option);
            if (group != null) {
                if (!processedGroups.contains(group)) {
                    processedGroups.add(group);
                    appendOptionGroup(buff, group);
                }
            } else {
                appendOption(buff, option, option.isRequired());
            }
            if (i.hasNext()) {
                buff.append(Instruction.argsep);
            }
        }
        printWrapped(pw, width, buff.toString().indexOf(32) + 1, buff.toString());
    }

    private void appendOptionGroup(StringBuffer buff, OptionGroup group) {
        if (!group.isRequired()) {
            buff.append("[");
        }
        List optList = new ArrayList(group.getOptions());
        Collections.sort(optList, getOptionComparator());
        Iterator i = optList.iterator();
        while (i.hasNext()) {
            appendOption(buff, (Option) i.next(), true);
            if (i.hasNext()) {
                buff.append(" | ");
            }
        }
        if (!group.isRequired()) {
            buff.append("]");
        }
    }

    private static void appendOption(StringBuffer buff, Option option, boolean required) {
        if (!required) {
            buff.append("[");
        }
        if (option.getOpt() != null) {
            buff.append(DEFAULT_OPT_PREFIX).append(option.getOpt());
        } else {
            buff.append(DEFAULT_LONG_OPT_PREFIX).append(option.getLongOpt());
        }
        if (option.hasArg() && option.hasArgName()) {
            buff.append(" <").append(option.getArgName()).append(">");
        }
        if (!required) {
            buff.append("]");
        }
    }

    public void printUsage(PrintWriter pw, int width, String cmdLineSyntax) {
        int argPos = cmdLineSyntax.indexOf(32) + 1;
        printWrapped(pw, width, this.defaultSyntaxPrefix.length() + argPos, new StringBuffer().append(this.defaultSyntaxPrefix).append(cmdLineSyntax).toString());
    }

    public void printOptions(PrintWriter pw, int width, Options options, int leftPad, int descPad) {
        StringBuffer sb = new StringBuffer();
        renderOptions(sb, width, options, leftPad, descPad);
        pw.println(sb.toString());
    }

    public void printWrapped(PrintWriter pw, int width, String text) {
        printWrapped(pw, width, 0, text);
    }

    public void printWrapped(PrintWriter pw, int width, int nextLineTabStop, String text) {
        StringBuffer sb = new StringBuffer(text.length());
        renderWrappedText(sb, width, nextLineTabStop, text);
        pw.println(sb.toString());
    }

    protected StringBuffer renderOptions(StringBuffer sb, int width, Options options, int leftPad, int descPad) {
        String lpad = createPadding(leftPad);
        String dpad = createPadding(descPad);
        int max = 0;
        List prefixList = new ArrayList();
        List<Option> optList = options.helpOptions();
        Collections.sort(optList, getOptionComparator());
        for (Option option : optList) {
            StringBuffer optBuf = new StringBuffer(8);
            if (option.getOpt() == null) {
                optBuf.append(lpad).append(new StringBuffer().append("   ").append(this.defaultLongOptPrefix).toString()).append(option.getLongOpt());
            } else {
                optBuf.append(lpad).append(this.defaultOptPrefix).append(option.getOpt());
                if (option.hasLongOpt()) {
                    optBuf.append(',').append(this.defaultLongOptPrefix).append(option.getLongOpt());
                }
            }
            if (option.hasArg()) {
                if (option.hasArgName()) {
                    optBuf.append(" <").append(option.getArgName()).append(">");
                } else {
                    optBuf.append(' ');
                }
            }
            prefixList.add(optBuf);
            max = optBuf.length() > max ? optBuf.length() : max;
        }
        int x = 0;
        Iterator i = optList.iterator();
        while (i.hasNext()) {
            Option option2 = (Option) i.next();
            int i2 = x;
            x++;
            StringBuffer optBuf2 = new StringBuffer(prefixList.get(i2).toString());
            if (optBuf2.length() < max) {
                optBuf2.append(createPadding(max - optBuf2.length()));
            }
            optBuf2.append(dpad);
            int nextLineTabStop = max + descPad;
            if (option2.getDescription() != null) {
                optBuf2.append(option2.getDescription());
            }
            renderWrappedText(sb, width, nextLineTabStop, optBuf2.toString());
            if (i.hasNext()) {
                sb.append(this.defaultNewLine);
            }
        }
        return sb;
    }

    protected StringBuffer renderWrappedText(StringBuffer sb, int width, int nextLineTabStop, String text) {
        int pos = findWrapPos(text, width, 0);
        if (pos == -1) {
            sb.append(rtrim(text));
            return sb;
        }
        sb.append(rtrim(text.substring(0, pos))).append(this.defaultNewLine);
        if (nextLineTabStop >= width) {
            nextLineTabStop = 1;
        }
        String padding = createPadding(nextLineTabStop);
        while (true) {
            text = new StringBuffer().append(padding).append(text.substring(pos).trim()).toString();
            pos = findWrapPos(text, width, 0);
            if (pos == -1) {
                sb.append(text);
                return sb;
            }
            if (text.length() > width && pos == nextLineTabStop - 1) {
                pos = width;
            }
            sb.append(rtrim(text.substring(0, pos))).append(this.defaultNewLine);
        }
    }

    protected int findWrapPos(String text, int width, int startPos) {
        char c;
        char c2;
        int indexOf = text.indexOf(10, startPos);
        int pos = indexOf;
        if (indexOf == -1 || pos > width) {
            int indexOf2 = text.indexOf(9, startPos);
            pos = indexOf2;
            if (indexOf2 == -1 || pos > width) {
                if (startPos + width >= text.length()) {
                    return -1;
                }
                int pos2 = startPos + width;
                while (pos2 >= startPos && (c2 = text.charAt(pos2)) != ' ' && c2 != '\n' && c2 != '\r') {
                    pos2--;
                }
                if (pos2 > startPos) {
                    return pos2;
                }
                int pos3 = startPos + width;
                while (pos3 <= text.length() && (c = text.charAt(pos3)) != ' ' && c != '\n' && c != '\r') {
                    pos3++;
                }
                if (pos3 == text.length()) {
                    return -1;
                }
                return pos3;
            }
        }
        return pos + 1;
    }

    protected String createPadding(int len) {
        StringBuffer sb = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    protected String rtrim(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        int pos = s.length();
        while (pos > 0 && Character.isWhitespace(s.charAt(pos - 1))) {
            pos--;
        }
        return s.substring(0, pos);
    }

    /* loaded from: gencallgraphv3.jar:commons-cli-1.2.jar:org/apache/commons/cli/HelpFormatter$OptionComparator.class */
    private static class OptionComparator implements Comparator {
        private OptionComparator() {
        }

        OptionComparator(AnonymousClass1 x0) {
            this();
        }

        @Override // java.util.Comparator
        public int compare(Object o1, Object o2) {
            Option opt1 = (Option) o1;
            Option opt2 = (Option) o2;
            return opt1.getKey().compareToIgnoreCase(opt2.getKey());
        }
    }
}
