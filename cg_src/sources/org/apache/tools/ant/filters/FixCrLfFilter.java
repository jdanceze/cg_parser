package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.EnumeratedAttribute;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/FixCrLfFilter.class */
public final class FixCrLfFilter extends BaseParamFilterReader implements ChainableReader {
    private static final int DEFAULT_TAB_LENGTH = 8;
    private static final int MIN_TAB_LENGTH = 2;
    private static final int MAX_TAB_LENGTH = 80;
    private static final char CTRLZ = 26;
    private int tabLength;
    private CrLf eol;
    private AddAsisRemove ctrlz;
    private AddAsisRemove tabs;
    private boolean javafiles;
    private boolean fixlast;
    private boolean initialized;

    public FixCrLfFilter() {
        this.tabLength = 8;
        this.javafiles = false;
        this.fixlast = true;
        this.initialized = false;
        this.tabs = AddAsisRemove.ASIS;
        if (Os.isFamily(Os.FAMILY_MAC) && !Os.isFamily(Os.FAMILY_UNIX)) {
            this.ctrlz = AddAsisRemove.REMOVE;
            setEol(CrLf.MAC);
        } else if (Os.isFamily(Os.FAMILY_DOS)) {
            this.ctrlz = AddAsisRemove.ASIS;
            setEol(CrLf.DOS);
        } else {
            this.ctrlz = AddAsisRemove.REMOVE;
            setEol(CrLf.UNIX);
        }
    }

    public FixCrLfFilter(Reader in) throws IOException {
        super(in);
        this.tabLength = 8;
        this.javafiles = false;
        this.fixlast = true;
        this.initialized = false;
        this.tabs = AddAsisRemove.ASIS;
        if (Os.isFamily(Os.FAMILY_MAC) && !Os.isFamily(Os.FAMILY_UNIX)) {
            this.ctrlz = AddAsisRemove.REMOVE;
            setEol(CrLf.MAC);
        } else if (Os.isFamily(Os.FAMILY_DOS)) {
            this.ctrlz = AddAsisRemove.ASIS;
            setEol(CrLf.DOS);
        } else {
            this.ctrlz = AddAsisRemove.REMOVE;
            setEol(CrLf.UNIX);
        }
    }

    @Override // org.apache.tools.ant.filters.ChainableReader
    public Reader chain(Reader rdr) {
        try {
            FixCrLfFilter newFilter = new FixCrLfFilter(rdr);
            newFilter.setJavafiles(getJavafiles());
            newFilter.setEol(getEol());
            newFilter.setTab(getTab());
            newFilter.setTablength(getTablength());
            newFilter.setEof(getEof());
            newFilter.setFixlast(getFixlast());
            newFilter.initInternalFilters();
            return newFilter;
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    public AddAsisRemove getEof() {
        return this.ctrlz.newInstance();
    }

    public CrLf getEol() {
        return this.eol.newInstance();
    }

    public boolean getFixlast() {
        return this.fixlast;
    }

    public boolean getJavafiles() {
        return this.javafiles;
    }

    public AddAsisRemove getTab() {
        return this.tabs.newInstance();
    }

    public int getTablength() {
        return this.tabLength;
    }

    private static String calculateEolString(CrLf eol) {
        if (eol == CrLf.CR || eol == CrLf.MAC) {
            return "\r";
        }
        if (eol == CrLf.CRLF || eol == CrLf.DOS) {
            return "\r\n";
        }
        return "\n";
    }

    private void initInternalFilters() {
        this.in = this.ctrlz == AddAsisRemove.REMOVE ? new RemoveEofFilter(this.in) : this.in;
        if (this.eol != CrLf.ASIS) {
            this.in = new NormalizeEolFilter(this.in, calculateEolString(this.eol), getFixlast());
        }
        if (this.tabs != AddAsisRemove.ASIS) {
            if (getJavafiles()) {
                this.in = new MaskJavaTabLiteralsFilter(this.in);
            }
            this.in = this.tabs == AddAsisRemove.ADD ? new AddTabFilter(this.in, getTablength()) : new RemoveTabFilter(this.in, getTablength());
        }
        this.in = this.ctrlz == AddAsisRemove.ADD ? new AddEofFilter(this.in) : this.in;
        this.initialized = true;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public synchronized int read() throws IOException {
        if (!this.initialized) {
            initInternalFilters();
        }
        return this.in.read();
    }

    public void setEof(AddAsisRemove attr) {
        this.ctrlz = attr.resolve();
    }

    public void setEol(CrLf attr) {
        this.eol = attr.resolve();
    }

    public void setFixlast(boolean fixlast) {
        this.fixlast = fixlast;
    }

    public void setJavafiles(boolean javafiles) {
        this.javafiles = javafiles;
    }

    public void setTab(AddAsisRemove attr) {
        this.tabs = attr.resolve();
    }

    public void setTablength(int tabLength) throws IOException {
        if (tabLength < 2 || tabLength > 80) {
            throw new IOException("tablength must be between 2 and 80");
        }
        this.tabLength = tabLength;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/FixCrLfFilter$SimpleFilterReader.class */
    private static class SimpleFilterReader extends Reader {
        private static final int PREEMPT_BUFFER_LENGTH = 16;
        private Reader in;
        private int[] preempt = new int[16];
        private int preemptIndex = 0;

        public SimpleFilterReader(Reader in) {
            this.in = in;
        }

        public void push(char c) {
            push((int) c);
        }

        public void push(int c) {
            try {
                int[] iArr = this.preempt;
                int i = this.preemptIndex;
                this.preemptIndex = i + 1;
                iArr[i] = c;
            } catch (ArrayIndexOutOfBoundsException e) {
                int[] p2 = new int[this.preempt.length * 2];
                System.arraycopy(this.preempt, 0, p2, 0, this.preempt.length);
                this.preempt = p2;
                push(c);
            }
        }

        public void push(char[] cs, int start, int length) {
            int i = (start + length) - 1;
            while (i >= start) {
                int i2 = i;
                i--;
                push(cs[i2]);
            }
        }

        public void push(char[] cs) {
            push(cs, 0, cs.length);
        }

        public boolean editsBlocked() {
            return (this.in instanceof SimpleFilterReader) && ((SimpleFilterReader) this.in).editsBlocked();
        }

        @Override // java.io.Reader
        public int read() throws IOException {
            if (this.preemptIndex > 0) {
                int[] iArr = this.preempt;
                int i = this.preemptIndex - 1;
                this.preemptIndex = i;
                return iArr[i];
            }
            return this.in.read();
        }

        @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.in.close();
        }

        @Override // java.io.Reader
        public void reset() throws IOException {
            this.in.reset();
        }

        @Override // java.io.Reader
        public boolean markSupported() {
            return this.in.markSupported();
        }

        @Override // java.io.Reader
        public boolean ready() throws IOException {
            return this.in.ready();
        }

        @Override // java.io.Reader
        public void mark(int i) throws IOException {
            this.in.mark(i);
        }

        @Override // java.io.Reader
        public long skip(long i) throws IOException {
            return this.in.skip(i);
        }

        @Override // java.io.Reader
        public int read(char[] buf) throws IOException {
            return read(buf, 0, buf.length);
        }

        @Override // java.io.Reader
        public int read(char[] buf, int start, int length) throws IOException {
            int count = 0;
            int c = 0;
            while (true) {
                int i = length;
                length--;
                if (i <= 0) {
                    break;
                }
                int read = read();
                c = read;
                if (read == -1) {
                    break;
                }
                int i2 = start;
                start++;
                buf[i2] = (char) c;
                count++;
            }
            if (count == 0 && c == -1) {
                return -1;
            }
            return count;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/FixCrLfFilter$MaskJavaTabLiteralsFilter.class */
    public static class MaskJavaTabLiteralsFilter extends SimpleFilterReader {
        private boolean editsBlocked;
        private static final int JAVA = 1;
        private static final int IN_CHAR_CONST = 2;
        private static final int IN_STR_CONST = 3;
        private static final int IN_SINGLE_COMMENT = 4;
        private static final int IN_MULTI_COMMENT = 5;
        private static final int TRANS_TO_COMMENT = 6;
        private static final int TRANS_FROM_MULTI = 8;
        private int state;

        public MaskJavaTabLiteralsFilter(Reader in) {
            super(in);
            this.editsBlocked = false;
            this.state = 1;
        }

        @Override // org.apache.tools.ant.filters.FixCrLfFilter.SimpleFilterReader
        public boolean editsBlocked() {
            return this.editsBlocked || super.editsBlocked();
        }

        @Override // org.apache.tools.ant.filters.FixCrLfFilter.SimpleFilterReader, java.io.Reader
        public int read() throws IOException {
            int thisChar = super.read();
            this.editsBlocked = this.state == 2 || this.state == 3;
            switch (this.state) {
                case 1:
                    switch (thisChar) {
                        case 34:
                            this.state = 3;
                            break;
                        case 39:
                            this.state = 2;
                            break;
                        case 47:
                            this.state = 6;
                            break;
                    }
                case 2:
                    switch (thisChar) {
                        case 39:
                            this.state = 1;
                            break;
                    }
                case 3:
                    switch (thisChar) {
                        case 34:
                            this.state = 1;
                            break;
                    }
                case 4:
                    switch (thisChar) {
                        case 10:
                        case 13:
                            this.state = 1;
                            break;
                    }
                case 5:
                    switch (thisChar) {
                        case 42:
                            this.state = 8;
                            break;
                    }
                case 6:
                    switch (thisChar) {
                        case 34:
                            this.state = 3;
                            break;
                        case 39:
                            this.state = 2;
                            break;
                        case 42:
                            this.state = 5;
                            break;
                        case 47:
                            this.state = 4;
                            break;
                        default:
                            this.state = 1;
                            break;
                    }
                case 8:
                    switch (thisChar) {
                        case 47:
                            this.state = 1;
                            break;
                    }
            }
            return thisChar;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/FixCrLfFilter$NormalizeEolFilter.class */
    public static class NormalizeEolFilter extends SimpleFilterReader {
        private boolean previousWasEOL;
        private boolean fixLast;
        private int normalizedEOL;
        private char[] eol;

        public NormalizeEolFilter(Reader in, String eolString, boolean fixLast) {
            super(in);
            this.normalizedEOL = 0;
            this.eol = null;
            this.eol = eolString.toCharArray();
            this.fixLast = fixLast;
        }

        @Override // org.apache.tools.ant.filters.FixCrLfFilter.SimpleFilterReader, java.io.Reader
        public int read() throws IOException {
            int thisChar = super.read();
            if (this.normalizedEOL == 0) {
                int numEOL = 0;
                boolean atEnd = false;
                switch (thisChar) {
                    case -1:
                        atEnd = true;
                        if (this.fixLast && !this.previousWasEOL) {
                            numEOL = 1;
                            break;
                        }
                        break;
                    case 10:
                        numEOL = 1;
                        break;
                    case 13:
                        numEOL = 1;
                        int c1 = super.read();
                        int c2 = super.read();
                        if (c1 != 13 || c2 != 10) {
                            if (c1 == 13) {
                                numEOL = 2;
                                push(c2);
                                break;
                            } else if (c1 == 10) {
                                push(c2);
                                break;
                            } else {
                                push(c2);
                                push(c1);
                                break;
                            }
                        }
                        break;
                    case 26:
                        int c = super.read();
                        if (c == -1) {
                            atEnd = true;
                            if (this.fixLast && !this.previousWasEOL) {
                                numEOL = 1;
                                push(thisChar);
                                break;
                            }
                        } else {
                            push(c);
                            break;
                        }
                        break;
                }
                if (numEOL > 0) {
                    while (true) {
                        int i = numEOL;
                        numEOL--;
                        if (i > 0) {
                            push(this.eol);
                            this.normalizedEOL += this.eol.length;
                        } else {
                            this.previousWasEOL = true;
                            thisChar = read();
                        }
                    }
                } else if (!atEnd) {
                    this.previousWasEOL = false;
                }
            } else {
                this.normalizedEOL--;
            }
            return thisChar;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/FixCrLfFilter$AddEofFilter.class */
    public static class AddEofFilter extends SimpleFilterReader {
        private int lastChar;

        public AddEofFilter(Reader in) {
            super(in);
            this.lastChar = -1;
        }

        @Override // org.apache.tools.ant.filters.FixCrLfFilter.SimpleFilterReader, java.io.Reader
        public int read() throws IOException {
            int thisChar = super.read();
            if (thisChar == -1) {
                if (this.lastChar != 26) {
                    this.lastChar = 26;
                    return this.lastChar;
                }
            } else {
                this.lastChar = thisChar;
            }
            return thisChar;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/FixCrLfFilter$RemoveEofFilter.class */
    public static class RemoveEofFilter extends SimpleFilterReader {
        private int lookAhead;

        public RemoveEofFilter(Reader in) {
            super(in);
            this.lookAhead = -1;
            try {
                this.lookAhead = in.read();
            } catch (IOException e) {
                this.lookAhead = -1;
            }
        }

        @Override // org.apache.tools.ant.filters.FixCrLfFilter.SimpleFilterReader, java.io.Reader
        public int read() throws IOException {
            int lookAhead2 = super.read();
            if (lookAhead2 == -1 && this.lookAhead == 26) {
                return -1;
            }
            int i = this.lookAhead;
            this.lookAhead = lookAhead2;
            return i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/FixCrLfFilter$AddTabFilter.class */
    public static class AddTabFilter extends SimpleFilterReader {
        private int columnNumber;
        private int tabLength;

        public AddTabFilter(Reader in, int tabLength) {
            super(in);
            this.columnNumber = 0;
            this.tabLength = 0;
            this.tabLength = tabLength;
        }

        @Override // org.apache.tools.ant.filters.FixCrLfFilter.SimpleFilterReader, java.io.Reader
        public int read() throws IOException {
            int c = super.read();
            switch (c) {
                case 9:
                    this.columnNumber = (((this.columnNumber + this.tabLength) - 1) / this.tabLength) * this.tabLength;
                    break;
                case 10:
                case 13:
                    this.columnNumber = 0;
                    break;
                case 32:
                    this.columnNumber++;
                    if (!editsBlocked()) {
                        int colNextTab = (((this.columnNumber + this.tabLength) - 1) / this.tabLength) * this.tabLength;
                        int countSpaces = 1;
                        int numTabs = 0;
                        while (true) {
                            int c2 = super.read();
                            if (c2 != -1) {
                                switch (c2) {
                                    case 9:
                                        this.columnNumber = colNextTab;
                                        numTabs++;
                                        countSpaces = 0;
                                        colNextTab += this.tabLength;
                                        break;
                                    case 32:
                                        int i = this.columnNumber + 1;
                                        this.columnNumber = i;
                                        if (i == colNextTab) {
                                            numTabs++;
                                            countSpaces = 0;
                                            colNextTab += this.tabLength;
                                            break;
                                        } else {
                                            countSpaces++;
                                            break;
                                        }
                                    default:
                                        push(c2);
                                        break;
                                }
                            }
                        }
                        while (true) {
                            int i2 = countSpaces;
                            countSpaces--;
                            if (i2 <= 0) {
                                while (true) {
                                    int i3 = numTabs;
                                    numTabs--;
                                    if (i3 > 0) {
                                        push('\t');
                                        this.columnNumber -= this.tabLength;
                                    } else {
                                        c = super.read();
                                        switch (c) {
                                            case 9:
                                                this.columnNumber += this.tabLength;
                                                break;
                                            case 32:
                                                this.columnNumber++;
                                                break;
                                        }
                                    }
                                }
                            } else {
                                push(' ');
                                this.columnNumber--;
                            }
                        }
                    }
                    break;
                default:
                    this.columnNumber++;
                    break;
            }
            return c;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/FixCrLfFilter$RemoveTabFilter.class */
    public static class RemoveTabFilter extends SimpleFilterReader {
        private int columnNumber;
        private int tabLength;

        public RemoveTabFilter(Reader in, int tabLength) {
            super(in);
            this.columnNumber = 0;
            this.tabLength = 0;
            this.tabLength = tabLength;
        }

        @Override // org.apache.tools.ant.filters.FixCrLfFilter.SimpleFilterReader, java.io.Reader
        public int read() throws IOException {
            int c = super.read();
            switch (c) {
                case 9:
                    int width = this.tabLength - (this.columnNumber % this.tabLength);
                    if (!editsBlocked()) {
                        while (width > 1) {
                            push(' ');
                            width--;
                        }
                        c = 32;
                    }
                    this.columnNumber += width;
                    break;
                case 10:
                case 13:
                    this.columnNumber = 0;
                    break;
                case 11:
                case 12:
                default:
                    this.columnNumber++;
                    break;
            }
            return c;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/FixCrLfFilter$AddAsisRemove.class */
    public static class AddAsisRemove extends EnumeratedAttribute {
        private static final AddAsisRemove ASIS = newInstance("asis");
        private static final AddAsisRemove ADD = newInstance("add");
        private static final AddAsisRemove REMOVE = newInstance("remove");

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"add", "asis", "remove"};
        }

        public boolean equals(Object other) {
            return (other instanceof AddAsisRemove) && getIndex() == ((AddAsisRemove) other).getIndex();
        }

        public int hashCode() {
            return getIndex();
        }

        AddAsisRemove resolve() throws IllegalStateException {
            if (equals(ASIS)) {
                return ASIS;
            }
            if (equals(ADD)) {
                return ADD;
            }
            if (equals(REMOVE)) {
                return REMOVE;
            }
            throw new IllegalStateException("No replacement for " + this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public AddAsisRemove newInstance() {
            return newInstance(getValue());
        }

        public static AddAsisRemove newInstance(String value) {
            AddAsisRemove a = new AddAsisRemove();
            a.setValue(value);
            return a;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/filters/FixCrLfFilter$CrLf.class */
    public static class CrLf extends EnumeratedAttribute {
        private static final CrLf ASIS = newInstance("asis");
        private static final CrLf CR = newInstance("cr");
        private static final CrLf CRLF = newInstance("crlf");
        private static final CrLf DOS = newInstance(Os.FAMILY_DOS);
        private static final CrLf LF = newInstance("lf");
        private static final CrLf MAC = newInstance(Os.FAMILY_MAC);
        private static final CrLf UNIX = newInstance(Os.FAMILY_UNIX);

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"asis", "cr", "lf", "crlf", Os.FAMILY_MAC, Os.FAMILY_UNIX, Os.FAMILY_DOS};
        }

        public boolean equals(Object other) {
            return (other instanceof CrLf) && getIndex() == ((CrLf) other).getIndex();
        }

        public int hashCode() {
            return getIndex();
        }

        CrLf resolve() {
            if (equals(ASIS)) {
                return ASIS;
            }
            if (equals(CR) || equals(MAC)) {
                return CR;
            }
            if (equals(CRLF) || equals(DOS)) {
                return CRLF;
            }
            if (equals(LF) || equals(UNIX)) {
                return LF;
            }
            throw new IllegalStateException("No replacement for " + this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public CrLf newInstance() {
            return newInstance(getValue());
        }

        public static CrLf newInstance(String value) {
            CrLf c = new CrLf();
            c.setValue(value);
            return c;
        }
    }
}
