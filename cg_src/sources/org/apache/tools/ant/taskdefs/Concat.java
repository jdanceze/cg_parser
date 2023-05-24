package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.filters.util.ChainReaderHelper;
import org.apache.tools.ant.taskdefs.FixCRLF;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Intersect;
import org.apache.tools.ant.types.resources.LogOutputResource;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.StringResource;
import org.apache.tools.ant.types.resources.selectors.Exists;
import org.apache.tools.ant.types.resources.selectors.Not;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.apache.tools.ant.util.ConcatResourceInputStream;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.ReaderInputStream;
import org.apache.tools.ant.util.ResourceUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Concat.class */
public class Concat extends Task implements ResourceCollection {
    private static final int BUFFER_SIZE = 8192;
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private static final ResourceSelector EXISTS = new Exists();
    private static final ResourceSelector NOT_EXISTS = new Not(EXISTS);
    private Resource dest;
    private boolean append;
    private String encoding;
    private String outputEncoding;
    private boolean binary;
    private boolean filterBeforeConcat;
    private StringBuffer textBuffer;
    private Resources rc;
    private Vector<FilterChain> filterChains;
    private TextElement footer;
    private TextElement header;
    private String eolString;
    private String resourceName;
    private boolean forceOverwrite = true;
    private boolean force = false;
    private boolean fixLastLine = false;
    private Writer outputWriter = null;
    private boolean ignoreEmpty = true;
    private ReaderFactory<Resource> resourceReaderFactory = new ReaderFactory<Resource>() { // from class: org.apache.tools.ant.taskdefs.Concat.1
        @Override // org.apache.tools.ant.taskdefs.Concat.ReaderFactory
        public Reader getReader(Resource o) throws IOException {
            InputStreamReader inputStreamReader;
            InputStream is = o.getInputStream();
            if (Concat.this.encoding == null) {
                inputStreamReader = new InputStreamReader(is);
            } else {
                inputStreamReader = new InputStreamReader(is, Concat.this.encoding);
            }
            return new BufferedReader(inputStreamReader);
        }
    };
    private ReaderFactory<Reader> identityReaderFactory = o -> {
        return o;
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Concat$ReaderFactory.class */
    public interface ReaderFactory<S> {
        Reader getReader(S s) throws IOException;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Concat$TextElement.class */
    public static class TextElement extends ProjectComponent {
        private String value = "";
        private boolean trimLeading = false;
        private boolean trim = false;
        private boolean filtering = true;
        private String encoding = null;

        public void setFiltering(boolean filtering) {
            this.filtering = filtering;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean getFiltering() {
            return this.filtering;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public void setFile(File file) throws BuildException {
            if (!file.exists()) {
                throw new BuildException("File %s does not exist.", file);
            }
            BufferedReader reader = null;
            try {
                try {
                    if (this.encoding == null) {
                        reader = new BufferedReader(new FileReader(file));
                    } else {
                        reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath(), new OpenOption[0]), this.encoding));
                    }
                    this.value = FileUtils.safeReadFully(reader);
                    FileUtils.close((Reader) reader);
                } catch (IOException ex) {
                    throw new BuildException(ex);
                }
            } catch (Throwable th) {
                FileUtils.close((Reader) reader);
                throw th;
            }
        }

        public void addText(String value) {
            this.value += getProject().replaceProperties(value);
        }

        public void setTrimLeading(boolean strip) {
            this.trimLeading = strip;
        }

        public void setTrim(boolean trim) {
            this.trim = trim;
        }

        public String getValue() {
            char[] charArray;
            if (this.value == null) {
                this.value = "";
            }
            if (this.value.trim().isEmpty()) {
                this.value = "";
            }
            if (this.trimLeading) {
                StringBuilder b = new StringBuilder();
                boolean startOfLine = true;
                for (char ch : this.value.toCharArray()) {
                    if (startOfLine) {
                        if (ch != ' ' && ch != '\t') {
                            startOfLine = false;
                        }
                    }
                    b.append(ch);
                    if (ch == '\n' || ch == '\r') {
                        startOfLine = true;
                    }
                }
                this.value = b.toString();
            }
            if (this.trim) {
                this.value = this.value.trim();
            }
            return this.value;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Concat$LastLineFixingReader.class */
    public final class LastLineFixingReader extends Reader {
        private final Reader reader;
        private int lastPos;
        private final char[] lastChars;
        private boolean needAddSeparator;

        private LastLineFixingReader(Reader reader) {
            this.lastPos = 0;
            this.lastChars = new char[Concat.this.eolString.length()];
            this.needAddSeparator = false;
            this.reader = reader;
        }

        @Override // java.io.Reader
        public int read() throws IOException {
            if (this.needAddSeparator) {
                if (this.lastPos < Concat.this.eolString.length()) {
                    String str = Concat.this.eolString;
                    int i = this.lastPos;
                    this.lastPos = i + 1;
                    return str.charAt(i);
                }
                return -1;
            }
            int ch = this.reader.read();
            if (ch == -1) {
                if (isMissingEndOfLine()) {
                    this.needAddSeparator = true;
                    this.lastPos = 1;
                    return Concat.this.eolString.charAt(0);
                }
                return -1;
            }
            addLastChar((char) ch);
            return ch;
        }

        /* JADX WARN: Code restructure failed: missing block: B:32:0x00b9, code lost:
            if (r12 != 0) goto L20;
         */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x00bc, code lost:
            return -1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x00c0, code lost:
            return r12;
         */
        @Override // java.io.Reader
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public int read(char[] r9, int r10, int r11) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 193
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.tools.ant.taskdefs.Concat.LastLineFixingReader.read(char[], int, int):int");
        }

        @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.reader.close();
        }

        private void addLastChar(char ch) {
            System.arraycopy(this.lastChars, 1, this.lastChars, 0, (this.lastChars.length - 2) + 1);
            this.lastChars[this.lastChars.length - 1] = ch;
        }

        private boolean isMissingEndOfLine() {
            for (int i = 0; i < this.lastChars.length; i++) {
                if (this.lastChars[i] != Concat.this.eolString.charAt(i)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Concat$MultiReader.class */
    private final class MultiReader<S> extends Reader {
        private Reader reader;
        private Iterator<S> readerSources;
        private ReaderFactory<S> factory;
        private final boolean filterBeforeConcat;

        private MultiReader(Iterator<S> readerSources, ReaderFactory<S> factory, boolean filterBeforeConcat) {
            this.reader = null;
            this.readerSources = readerSources;
            this.factory = factory;
            this.filterBeforeConcat = filterBeforeConcat;
        }

        private Reader getReader() throws IOException {
            if (this.reader == null && this.readerSources.hasNext()) {
                this.reader = this.factory.getReader(this.readerSources.next());
                if (isFixLastLine()) {
                    this.reader = new LastLineFixingReader(this.reader);
                }
                if (this.filterBeforeConcat) {
                    this.reader = Concat.this.getFilteredReader(this.reader);
                }
            }
            return this.reader;
        }

        private void nextReader() throws IOException {
            close();
            this.reader = null;
        }

        @Override // java.io.Reader
        public int read() throws IOException {
            while (getReader() != null) {
                int ch = getReader().read();
                if (ch == -1) {
                    nextReader();
                } else {
                    return ch;
                }
            }
            return -1;
        }

        @Override // java.io.Reader
        public int read(char[] cbuf, int off, int len) throws IOException {
            int amountRead = 0;
            while (getReader() != null) {
                int nRead = getReader().read(cbuf, off, len);
                if (nRead == -1 || nRead == 0) {
                    nextReader();
                } else {
                    len -= nRead;
                    off += nRead;
                    amountRead += nRead;
                    if (len == 0) {
                        return amountRead;
                    }
                }
            }
            if (amountRead == 0) {
                return -1;
            }
            return amountRead;
        }

        @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.reader != null) {
                this.reader.close();
            }
        }

        private boolean isFixLastLine() {
            return Concat.this.fixLastLine && Concat.this.textBuffer == null;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Concat$ConcatResource.class */
    private final class ConcatResource extends Resource {
        private ResourceCollection c;

        private ConcatResource(ResourceCollection c) {
            this.c = c;
        }

        @Override // org.apache.tools.ant.types.Resource
        public InputStream getInputStream() {
            Reader resourceReader;
            Reader rdr;
            if (!Concat.this.binary) {
                if (!Concat.this.filterBeforeConcat) {
                    resourceReader = Concat.this.getFilteredReader(new MultiReader(this.c.iterator(), Concat.this.resourceReaderFactory, false));
                } else {
                    resourceReader = new MultiReader(this.c.iterator(), Concat.this.resourceReaderFactory, true);
                }
                if (Concat.this.header == null && Concat.this.footer == null) {
                    rdr = resourceReader;
                } else {
                    int readerCount = 1;
                    if (Concat.this.header != null) {
                        readerCount = 1 + 1;
                    }
                    if (Concat.this.footer != null) {
                        readerCount++;
                    }
                    Reader[] readers = new Reader[readerCount];
                    int pos = 0;
                    if (Concat.this.header != null) {
                        readers[0] = new StringReader(Concat.this.header.getValue());
                        if (Concat.this.header.getFiltering()) {
                            readers[0] = Concat.this.getFilteredReader(readers[0]);
                        }
                        pos = 0 + 1;
                    }
                    int i = pos;
                    int pos2 = pos + 1;
                    readers[i] = resourceReader;
                    if (Concat.this.footer != null) {
                        readers[pos2] = new StringReader(Concat.this.footer.getValue());
                        if (Concat.this.footer.getFiltering()) {
                            readers[pos2] = Concat.this.getFilteredReader(readers[pos2]);
                        }
                    }
                    rdr = new MultiReader(Arrays.asList(readers).iterator(), Concat.this.identityReaderFactory, false);
                }
                return Concat.this.outputEncoding == null ? new ReaderInputStream(rdr) : new ReaderInputStream(rdr, Concat.this.outputEncoding);
            }
            ConcatResourceInputStream result = new ConcatResourceInputStream(this.c);
            result.setManagingComponent(this);
            return result;
        }

        @Override // org.apache.tools.ant.types.Resource
        public String getName() {
            if (Concat.this.resourceName != null) {
                return Concat.this.resourceName;
            }
            return "concat (" + String.valueOf(this.c) + ")";
        }
    }

    public Concat() {
        reset();
    }

    public void reset() {
        this.append = false;
        this.forceOverwrite = true;
        this.dest = null;
        this.encoding = null;
        this.outputEncoding = null;
        this.fixLastLine = false;
        this.filterChains = null;
        this.footer = null;
        this.header = null;
        this.binary = false;
        this.outputWriter = null;
        this.textBuffer = null;
        this.eolString = System.lineSeparator();
        this.rc = null;
        this.ignoreEmpty = true;
        this.force = false;
    }

    public void setDestfile(File destinationFile) {
        setDest(new FileResource(destinationFile));
    }

    public void setDest(Resource dest) {
        this.dest = dest;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
        if (this.outputEncoding == null) {
            this.outputEncoding = encoding;
        }
    }

    public void setOutputEncoding(String outputEncoding) {
        this.outputEncoding = outputEncoding;
    }

    @Deprecated
    public void setForce(boolean forceOverwrite) {
        this.forceOverwrite = forceOverwrite;
    }

    public void setOverwrite(boolean forceOverwrite) {
        setForce(forceOverwrite);
    }

    public void setForceReadOnly(boolean f) {
        this.force = f;
    }

    public void setIgnoreEmpty(boolean ignoreEmpty) {
        this.ignoreEmpty = ignoreEmpty;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Path createPath() {
        Path path = new Path(getProject());
        add(path);
        return path;
    }

    public void addFileset(FileSet set) {
        add(set);
    }

    public void addFilelist(FileList list) {
        add(list);
    }

    public void add(ResourceCollection c) {
        synchronized (this) {
            if (this.rc == null) {
                this.rc = new Resources();
                this.rc.setProject(getProject());
                this.rc.setCache(true);
            }
        }
        this.rc.add(c);
    }

    public void addFilterChain(FilterChain filterChain) {
        if (this.filterChains == null) {
            this.filterChains = new Vector<>();
        }
        this.filterChains.addElement(filterChain);
    }

    public void addText(String text) {
        if (this.textBuffer == null) {
            this.textBuffer = new StringBuffer(text.length());
        }
        this.textBuffer.append(text);
    }

    public void addHeader(TextElement headerToAdd) {
        this.header = headerToAdd;
    }

    public void addFooter(TextElement footerToAdd) {
        this.footer = footerToAdd;
    }

    public void setFixLastLine(boolean fixLastLine) {
        this.fixLastLine = fixLastLine;
    }

    public void setEol(FixCRLF.CrLf crlf) {
        String s = crlf.getValue();
        if ("cr".equals(s) || Os.FAMILY_MAC.equals(s)) {
            this.eolString = "\r";
        } else if ("lf".equals(s) || Os.FAMILY_UNIX.equals(s)) {
            this.eolString = "\n";
        } else if ("crlf".equals(s) || Os.FAMILY_DOS.equals(s)) {
            this.eolString = "\r\n";
        }
    }

    public void setWriter(Writer outputWriter) {
        this.outputWriter = outputWriter;
    }

    public void setBinary(boolean binary) {
        this.binary = binary;
    }

    public void setFilterBeforeConcat(boolean filterBeforeConcat) {
        this.filterBeforeConcat = filterBeforeConcat;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        Resource resource;
        validate();
        if (this.binary && this.dest == null) {
            throw new BuildException("dest|destfile attribute is required for binary concatenation");
        }
        ResourceCollection c = getResources();
        if (isUpToDate(c)) {
            log(this.dest + " is up-to-date.", 3);
        } else if (c.isEmpty() && this.ignoreEmpty) {
        } else {
            try {
                ConcatResource concatResource = new ConcatResource(c);
                if (this.dest == null) {
                    resource = new LogOutputResource(this, 1);
                } else {
                    resource = this.dest;
                }
                ResourceUtils.copyResource(concatResource, resource, null, null, true, false, this.append, null, null, getProject(), this.force);
            } catch (IOException e) {
                throw new BuildException("error concatenating content to " + this.dest, e);
            }
        }
    }

    @Override // java.lang.Iterable
    public Iterator<Resource> iterator() {
        validate();
        return Collections.singletonList(new ConcatResource(getResources())).iterator();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public int size() {
        return 1;
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public boolean isFilesystemOnly() {
        return false;
    }

    private void validate() {
        sanitizeText();
        if (this.binary) {
            if (this.textBuffer != null) {
                throw new BuildException("Nested text is incompatible with binary concatenation");
            }
            if (this.encoding != null || this.outputEncoding != null) {
                throw new BuildException("Setting input or output encoding is incompatible with binary concatenation");
            }
            if (this.filterChains != null) {
                throw new BuildException("Setting filters is incompatible with binary concatenation");
            }
            if (this.fixLastLine) {
                throw new BuildException("Setting fixlastline is incompatible with binary concatenation");
            }
            if (this.header != null || this.footer != null) {
                throw new BuildException("Nested header or footer is incompatible with binary concatenation");
            }
        }
        if (this.dest != null && this.outputWriter != null) {
            throw new BuildException("Cannot specify both a destination resource and an output writer");
        }
        if (this.rc == null && this.textBuffer == null) {
            throw new BuildException("At least one resource must be provided, or some text.");
        }
        if (this.rc != null && this.textBuffer != null) {
            throw new BuildException("Cannot include inline text when using resources.");
        }
    }

    private ResourceCollection getResources() {
        if (this.rc == null) {
            return new StringResource(getProject(), this.textBuffer.toString());
        }
        if (this.dest != null) {
            Intersect checkDestNotInSources = new Intersect();
            checkDestNotInSources.setProject(getProject());
            checkDestNotInSources.add(this.rc);
            checkDestNotInSources.add(this.dest);
            if (checkDestNotInSources.size() > 0) {
                throw new BuildException("Destination resource %s was specified as an input resource.", this.dest);
            }
        }
        Restrict noexistRc = new Restrict();
        noexistRc.add(NOT_EXISTS);
        noexistRc.add(this.rc);
        Iterator<Resource> it = noexistRc.iterator();
        while (it.hasNext()) {
            Resource r = it.next();
            log(r + DirectoryScanner.DOES_NOT_EXIST_POSTFIX, 0);
        }
        Restrict result = new Restrict();
        result.add(EXISTS);
        result.add(this.rc);
        return result;
    }

    private boolean isUpToDate(ResourceCollection c) {
        return (this.dest == null || this.forceOverwrite || !c.stream().noneMatch(r -> {
            return SelectorUtils.isOutOfDate(r, this.dest, FILE_UTILS.getFileTimestampGranularity());
        })) ? false : true;
    }

    private void sanitizeText() {
        if (this.textBuffer != null && this.textBuffer.toString().trim().isEmpty()) {
            this.textBuffer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Reader getFilteredReader(Reader r) {
        if (this.filterChains == null) {
            return r;
        }
        ChainReaderHelper helper = new ChainReaderHelper();
        helper.setBufferSize(8192);
        helper.setPrimaryReader(r);
        helper.setFilterChains(this.filterChains);
        helper.setProject(getProject());
        return helper.getAssembledReader();
    }
}
