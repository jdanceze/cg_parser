package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.types.Substitution;
import org.apache.tools.ant.util.LineTokenizer;
import org.apache.tools.ant.util.StringUtils;
import org.apache.tools.ant.util.Tokenizer;
import org.apache.tools.ant.util.regexp.Regexp;
import org.apache.tools.ant.util.regexp.RegexpUtil;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TokenFilter.class */
public class TokenFilter extends BaseFilterReader implements ChainableReader {
    private Vector<Filter> filters;
    private Tokenizer tokenizer;
    private String delimOutput;
    private String line;
    private int linePos;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TokenFilter$FileTokenizer.class */
    public static class FileTokenizer extends org.apache.tools.ant.util.FileTokenizer {
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TokenFilter$Filter.class */
    public interface Filter {
        String filter(String str);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TokenFilter$StringTokenizer.class */
    public static class StringTokenizer extends org.apache.tools.ant.util.StringTokenizer {
    }

    public TokenFilter() {
        this.filters = new Vector<>();
        this.tokenizer = null;
        this.delimOutput = null;
        this.line = null;
        this.linePos = 0;
    }

    public TokenFilter(Reader in) {
        super(in);
        this.filters = new Vector<>();
        this.tokenizer = null;
        this.delimOutput = null;
        this.line = null;
        this.linePos = 0;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        if (this.tokenizer == null) {
            this.tokenizer = new LineTokenizer();
        }
        while (true) {
            if (this.line == null || this.line.isEmpty()) {
                this.line = this.tokenizer.getToken(this.in);
                if (this.line == null) {
                    return -1;
                }
                Iterator<Filter> it = this.filters.iterator();
                while (it.hasNext()) {
                    Filter filter = it.next();
                    this.line = filter.filter(this.line);
                    if (this.line == null) {
                        break;
                    }
                }
                this.linePos = 0;
                if (this.line != null && !this.tokenizer.getPostToken().isEmpty()) {
                    if (this.delimOutput != null) {
                        this.line += this.delimOutput;
                    } else {
                        this.line += this.tokenizer.getPostToken();
                    }
                }
            } else {
                int ch = this.line.charAt(this.linePos);
                this.linePos++;
                if (this.linePos == this.line.length()) {
                    this.line = null;
                }
                return ch;
            }
        }
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public final Reader chain(Reader reader) {
        TokenFilter newFilter = new TokenFilter(reader);
        newFilter.filters = this.filters;
        newFilter.tokenizer = this.tokenizer;
        newFilter.delimOutput = this.delimOutput;
        newFilter.setProject(getProject());
        return newFilter;
    }

    public void setDelimOutput(String delimOutput) {
        this.delimOutput = resolveBackSlash(delimOutput);
    }

    public void addLineTokenizer(LineTokenizer tokenizer) {
        add(tokenizer);
    }

    public void addStringTokenizer(StringTokenizer tokenizer) {
        add(tokenizer);
    }

    public void addFileTokenizer(FileTokenizer tokenizer) {
        add(tokenizer);
    }

    public void add(Tokenizer tokenizer) {
        if (this.tokenizer != null) {
            throw new BuildException("Only one tokenizer allowed");
        }
        this.tokenizer = tokenizer;
    }

    public void addReplaceString(ReplaceString filter) {
        this.filters.addElement(filter);
    }

    public void addContainsString(ContainsString filter) {
        this.filters.addElement(filter);
    }

    public void addReplaceRegex(ReplaceRegex filter) {
        this.filters.addElement(filter);
    }

    public void addContainsRegex(ContainsRegex filter) {
        this.filters.addElement(filter);
    }

    public void addTrim(Trim filter) {
        this.filters.addElement(filter);
    }

    public void addIgnoreBlank(IgnoreBlank filter) {
        this.filters.addElement(filter);
    }

    public void addDeleteCharacters(DeleteCharacters filter) {
        this.filters.addElement(filter);
    }

    public void add(Filter filter) {
        this.filters.addElement(filter);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TokenFilter$ChainableReaderFilter.class */
    public static abstract class ChainableReaderFilter extends ProjectComponent implements ChainableReader, Filter {
        private boolean byLine = true;

        public void setByLine(boolean byLine) {
            this.byLine = byLine;
        }

        @Override // org.apache.tools.ant.filters.ChainableReader
        public Reader chain(Reader reader) {
            TokenFilter tokenFilter = new TokenFilter(reader);
            if (!this.byLine) {
                tokenFilter.add(new FileTokenizer());
            }
            tokenFilter.add(this);
            return tokenFilter;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TokenFilter$ReplaceString.class */
    public static class ReplaceString extends ChainableReaderFilter {
        private String from;
        private String to;

        public void setFrom(String from) {
            this.from = from;
        }

        public void setTo(String to) {
            this.to = to;
        }

        @Override // org.apache.tools.ant.filters.TokenFilter.Filter
        public String filter(String line) {
            if (this.from == null) {
                throw new BuildException("Missing from in stringreplace");
            }
            StringBuffer ret = new StringBuffer();
            int start = 0;
            int indexOf = line.indexOf(this.from);
            while (true) {
                int found = indexOf;
                if (found < 0) {
                    break;
                }
                if (found > start) {
                    ret.append((CharSequence) line, start, found);
                }
                if (this.to != null) {
                    ret.append(this.to);
                }
                start = found + this.from.length();
                indexOf = line.indexOf(this.from, start);
            }
            if (line.length() > start) {
                ret.append((CharSequence) line, start, line.length());
            }
            return ret.toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TokenFilter$ContainsString.class */
    public static class ContainsString extends ProjectComponent implements Filter {
        private String contains;

        public void setContains(String contains) {
            this.contains = contains;
        }

        @Override // org.apache.tools.ant.filters.TokenFilter.Filter
        public String filter(String string) {
            if (this.contains == null) {
                throw new BuildException("Missing contains in containsstring");
            }
            if (string.contains(this.contains)) {
                return string;
            }
            return null;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TokenFilter$ReplaceRegex.class */
    public static class ReplaceRegex extends ChainableReaderFilter {
        private String from;
        private String to;
        private RegularExpression regularExpression;
        private Substitution substitution;
        private boolean initialized = false;
        private String flags = "";
        private int options;
        private Regexp regexp;

        public void setPattern(String from) {
            this.from = from;
        }

        public void setReplace(String to) {
            this.to = to;
        }

        public void setFlags(String flags) {
            this.flags = flags;
        }

        private void initialize() {
            if (this.initialized) {
                return;
            }
            this.options = TokenFilter.convertRegexOptions(this.flags);
            if (this.from == null) {
                throw new BuildException("Missing pattern in replaceregex");
            }
            this.regularExpression = new RegularExpression();
            this.regularExpression.setPattern(this.from);
            this.regexp = this.regularExpression.getRegexp(getProject());
            if (this.to == null) {
                this.to = "";
            }
            this.substitution = new Substitution();
            this.substitution.setExpression(this.to);
        }

        @Override // org.apache.tools.ant.filters.TokenFilter.Filter
        public String filter(String line) {
            initialize();
            if (!this.regexp.matches(line, this.options)) {
                return line;
            }
            return this.regexp.substitute(line, this.substitution.getExpression(getProject()), this.options);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TokenFilter$ContainsRegex.class */
    public static class ContainsRegex extends ChainableReaderFilter {
        private String from;
        private String to;
        private RegularExpression regularExpression;
        private Substitution substitution;
        private boolean initialized = false;
        private String flags = "";
        private int options;
        private Regexp regexp;

        public void setPattern(String from) {
            this.from = from;
        }

        public void setReplace(String to) {
            this.to = to;
        }

        public void setFlags(String flags) {
            this.flags = flags;
        }

        private void initialize() {
            if (this.initialized) {
                return;
            }
            this.options = TokenFilter.convertRegexOptions(this.flags);
            if (this.from == null) {
                throw new BuildException("Missing from in containsregex");
            }
            this.regularExpression = new RegularExpression();
            this.regularExpression.setPattern(this.from);
            this.regexp = this.regularExpression.getRegexp(getProject());
            if (this.to == null) {
                return;
            }
            this.substitution = new Substitution();
            this.substitution.setExpression(this.to);
        }

        @Override // org.apache.tools.ant.filters.TokenFilter.Filter
        public String filter(String string) {
            initialize();
            if (!this.regexp.matches(string, this.options)) {
                return null;
            }
            if (this.substitution == null) {
                return string;
            }
            return this.regexp.substitute(string, this.substitution.getExpression(getProject()), this.options);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TokenFilter$Trim.class */
    public static class Trim extends ChainableReaderFilter {
        @Override // org.apache.tools.ant.filters.TokenFilter.Filter
        public String filter(String line) {
            return line.trim();
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TokenFilter$IgnoreBlank.class */
    public static class IgnoreBlank extends ChainableReaderFilter {
        @Override // org.apache.tools.ant.filters.TokenFilter.Filter
        public String filter(String line) {
            if (line.trim().isEmpty()) {
                return null;
            }
            return line;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/TokenFilter$DeleteCharacters.class */
    public static class DeleteCharacters extends ProjectComponent implements Filter, ChainableReader {
        private String deleteChars = "";

        public void setChars(String deleteChars) {
            this.deleteChars = TokenFilter.resolveBackSlash(deleteChars);
        }

        @Override // org.apache.tools.ant.filters.TokenFilter.Filter
        public String filter(String string) {
            StringBuffer output = new StringBuffer(string.length());
            for (int i = 0; i < string.length(); i++) {
                char ch = string.charAt(i);
                if (!isDeleteCharacter(ch)) {
                    output.append(ch);
                }
            }
            return output.toString();
        }

        @Override // org.apache.tools.ant.filters.ChainableReader
        public Reader chain(Reader reader) {
            return new BaseFilterReader(reader) { // from class: org.apache.tools.ant.filters.TokenFilter.DeleteCharacters.1
                @Override // java.io.FilterReader, java.io.Reader
                public int read() throws IOException {
                    int c;
                    do {
                        c = this.in.read();
                        if (c == -1) {
                            return c;
                        }
                    } while (DeleteCharacters.this.isDeleteCharacter((char) c));
                    return c;
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isDeleteCharacter(char c) {
            for (int d = 0; d < this.deleteChars.length(); d++) {
                if (this.deleteChars.charAt(d) == c) {
                    return true;
                }
            }
            return false;
        }
    }

    public static String resolveBackSlash(String input) {
        return StringUtils.resolveBackSlash(input);
    }

    public static int convertRegexOptions(String flags) {
        return RegexpUtil.asOptions(flags);
    }
}
