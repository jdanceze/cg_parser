package com.sun.xml.bind.v2.runtime.output;

import com.sun.xml.bind.marshaller.NoEscapeHandler;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.runtime.Name;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import com.sun.xml.bind.v2.runtime.output.NamespaceContextImpl;
import com.sun.xml.bind.v2.runtime.unmarshaller.Base64Data;
import com.sun.xml.fastinfoset.stax.StAXDocumentSerializer;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.stream.XMLStreamException;
import org.jvnet.fastinfoset.VocabularyApplicationData;
import org.xml.sax.SAXException;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/FastInfosetStreamWriterOutput.class */
public final class FastInfosetStreamWriterOutput extends XMLStreamWriterOutput {
    private final StAXDocumentSerializer fiout;
    private final Encoded[] localNames;
    private final TablesPerJAXBContext tables;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/FastInfosetStreamWriterOutput$TablesPerJAXBContext.class */
    public static final class TablesPerJAXBContext {
        final int[] elementIndexes;
        final int[] elementIndexPrefixes;
        final int[] attributeIndexes;
        final int[] localNameIndexes;
        int indexOffset = 1;
        int maxIndex;
        boolean requiresClear;

        TablesPerJAXBContext(JAXBContextImpl context, int initialIndexOffset) {
            this.elementIndexes = new int[context.getNumberOfElementNames()];
            this.elementIndexPrefixes = new int[context.getNumberOfElementNames()];
            this.attributeIndexes = new int[context.getNumberOfAttributeNames()];
            this.localNameIndexes = new int[context.getNumberOfLocalNames()];
            this.maxIndex = initialIndexOffset + this.elementIndexes.length + this.attributeIndexes.length;
        }

        public void requireClearTables() {
            this.requiresClear = true;
        }

        public void clearOrResetTables(int intialIndexOffset) {
            if (this.requiresClear) {
                this.requiresClear = false;
                this.indexOffset += this.maxIndex;
                this.maxIndex = intialIndexOffset + this.elementIndexes.length + this.attributeIndexes.length;
                if (this.indexOffset + this.maxIndex < 0) {
                    clearAll();
                    return;
                }
                return;
            }
            this.maxIndex = intialIndexOffset + this.elementIndexes.length + this.attributeIndexes.length;
            if (this.indexOffset + this.maxIndex < 0) {
                resetAll();
            }
        }

        private void clearAll() {
            clear(this.elementIndexes);
            clear(this.attributeIndexes);
            clear(this.localNameIndexes);
            this.indexOffset = 1;
        }

        private void clear(int[] array) {
            for (int i = 0; i < array.length; i++) {
                array[i] = 0;
            }
        }

        public void incrementMaxIndexValue() {
            this.maxIndex++;
            if (this.indexOffset + this.maxIndex < 0) {
                resetAll();
            }
        }

        private void resetAll() {
            clear(this.elementIndexes);
            clear(this.attributeIndexes);
            clear(this.localNameIndexes);
            this.indexOffset = 1;
        }

        private void reset(int[] array) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] > this.indexOffset) {
                    array[i] = (array[i] - this.indexOffset) + 1;
                } else {
                    array[i] = 0;
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/FastInfosetStreamWriterOutput$AppData.class */
    static final class AppData implements VocabularyApplicationData {
        final Map<JAXBContext, TablesPerJAXBContext> contexts = new WeakHashMap();
        final Collection<TablesPerJAXBContext> collectionOfContexts = this.contexts.values();

        AppData() {
        }

        @Override // org.jvnet.fastinfoset.VocabularyApplicationData
        public void clear() {
            for (TablesPerJAXBContext c : this.collectionOfContexts) {
                c.requireClearTables();
            }
        }
    }

    public FastInfosetStreamWriterOutput(StAXDocumentSerializer out, JAXBContextImpl context) {
        super(out, NoEscapeHandler.theInstance);
        AppData appData;
        this.fiout = out;
        this.localNames = context.getUTF8NameTable();
        VocabularyApplicationData vocabAppData = this.fiout.getVocabularyApplicationData();
        if (vocabAppData == null || !(vocabAppData instanceof AppData)) {
            appData = new AppData();
            this.fiout.setVocabularyApplicationData(appData);
        } else {
            appData = (AppData) vocabAppData;
        }
        TablesPerJAXBContext tablesPerContext = appData.contexts.get(context);
        if (tablesPerContext != null) {
            this.tables = tablesPerContext;
            this.tables.clearOrResetTables(out.getLocalNameIndex());
            return;
        }
        this.tables = new TablesPerJAXBContext(context, out.getLocalNameIndex());
        appData.contexts.put(context, this.tables);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XMLStreamWriterOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws IOException, SAXException, XMLStreamException {
        super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
        if (fragment) {
            this.fiout.initiateLowLevelWriting();
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XMLStreamWriterOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
        super.endDocument(fragment);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(Name name) throws IOException {
        this.fiout.writeLowLevelTerminationAndMark();
        if (this.nsContext.getCurrent().count() == 0) {
            int qNameIndex = this.tables.elementIndexes[name.qNameIndex] - this.tables.indexOffset;
            int prefixIndex = this.nsUriIndex2prefixIndex[name.nsUriIndex];
            if (qNameIndex >= 0 && this.tables.elementIndexPrefixes[name.qNameIndex] == prefixIndex) {
                this.fiout.writeLowLevelStartElementIndexed(0, qNameIndex);
                return;
            }
            this.tables.elementIndexes[name.qNameIndex] = this.fiout.getNextElementIndex() + this.tables.indexOffset;
            this.tables.elementIndexPrefixes[name.qNameIndex] = prefixIndex;
            writeLiteral(60, name, this.nsContext.getPrefix(prefixIndex), this.nsContext.getNamespaceURI(prefixIndex));
            return;
        }
        beginStartTagWithNamespaces(name);
    }

    public void beginStartTagWithNamespaces(Name name) throws IOException {
        NamespaceContextImpl.Element nse = this.nsContext.getCurrent();
        this.fiout.writeLowLevelStartNamespaces();
        for (int i = nse.count() - 1; i >= 0; i--) {
            String uri = nse.getNsUri(i);
            if (uri.length() != 0 || nse.getBase() != 1) {
                this.fiout.writeLowLevelNamespace(nse.getPrefix(i), uri);
            }
        }
        this.fiout.writeLowLevelEndNamespaces();
        int qNameIndex = this.tables.elementIndexes[name.qNameIndex] - this.tables.indexOffset;
        int prefixIndex = this.nsUriIndex2prefixIndex[name.nsUriIndex];
        if (qNameIndex >= 0 && this.tables.elementIndexPrefixes[name.qNameIndex] == prefixIndex) {
            this.fiout.writeLowLevelStartElementIndexed(0, qNameIndex);
            return;
        }
        this.tables.elementIndexes[name.qNameIndex] = this.fiout.getNextElementIndex() + this.tables.indexOffset;
        this.tables.elementIndexPrefixes[name.qNameIndex] = prefixIndex;
        writeLiteral(60, name, this.nsContext.getPrefix(prefixIndex), this.nsContext.getNamespaceURI(prefixIndex));
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void attribute(Name name, String value) throws IOException {
        this.fiout.writeLowLevelStartAttributes();
        int qNameIndex = this.tables.attributeIndexes[name.qNameIndex] - this.tables.indexOffset;
        if (qNameIndex >= 0) {
            this.fiout.writeLowLevelAttributeIndexed(qNameIndex);
        } else {
            this.tables.attributeIndexes[name.qNameIndex] = this.fiout.getNextAttributeIndex() + this.tables.indexOffset;
            int namespaceURIId = name.nsUriIndex;
            if (namespaceURIId == -1) {
                writeLiteral(120, name, "", "");
            } else {
                int prefix = this.nsUriIndex2prefixIndex[namespaceURIId];
                writeLiteral(120, name, this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix));
            }
        }
        this.fiout.writeLowLevelAttributeValue(value);
    }

    private void writeLiteral(int type, Name name, String prefix, String namespaceURI) throws IOException {
        int localNameIndex = this.tables.localNameIndexes[name.localNameIndex] - this.tables.indexOffset;
        if (localNameIndex < 0) {
            this.tables.localNameIndexes[name.localNameIndex] = this.fiout.getNextLocalNameIndex() + this.tables.indexOffset;
            this.fiout.writeLowLevelStartNameLiteral(type, prefix, this.localNames[name.localNameIndex].buf, namespaceURI);
            return;
        }
        this.fiout.writeLowLevelStartNameLiteral(type, prefix, localNameIndex, namespaceURI);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XMLStreamWriterOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void endStartTag() throws IOException {
        this.fiout.writeLowLevelEndStartElement();
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void endTag(Name name) throws IOException {
        this.fiout.writeLowLevelEndElement();
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XMLStreamWriterOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void endTag(int prefix, String localName) throws IOException {
        this.fiout.writeLowLevelEndElement();
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XMLStreamWriterOutput, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void text(Pcdata value, boolean needsSeparatingWhitespace) throws IOException {
        if (needsSeparatingWhitespace) {
            this.fiout.writeLowLevelText(Instruction.argsep);
        }
        if (!(value instanceof Base64Data)) {
            int len = value.length();
            if (len < this.buf.length) {
                value.writeTo(this.buf, 0);
                this.fiout.writeLowLevelText(this.buf, len);
                return;
            }
            this.fiout.writeLowLevelText(value.toString());
            return;
        }
        Base64Data dataValue = (Base64Data) value;
        this.fiout.writeLowLevelOctets(dataValue.get(), dataValue.getDataLen());
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XMLStreamWriterOutput, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void text(String value, boolean needsSeparatingWhitespace) throws IOException {
        if (needsSeparatingWhitespace) {
            this.fiout.writeLowLevelText(Instruction.argsep);
        }
        this.fiout.writeLowLevelText(value);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XMLStreamWriterOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(int prefix, String localName) throws IOException {
        this.fiout.writeLowLevelTerminationAndMark();
        int type = 0;
        if (this.nsContext.getCurrent().count() > 0) {
            NamespaceContextImpl.Element nse = this.nsContext.getCurrent();
            this.fiout.writeLowLevelStartNamespaces();
            for (int i = nse.count() - 1; i >= 0; i--) {
                String uri = nse.getNsUri(i);
                if (uri.length() != 0 || nse.getBase() != 1) {
                    this.fiout.writeLowLevelNamespace(nse.getPrefix(i), uri);
                }
            }
            this.fiout.writeLowLevelEndNamespaces();
            type = 0;
        }
        boolean isIndexed = this.fiout.writeLowLevelStartElement(type, this.nsContext.getPrefix(prefix), localName, this.nsContext.getNamespaceURI(prefix));
        if (!isIndexed) {
            this.tables.incrementMaxIndexValue();
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XMLStreamWriterOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void attribute(int prefix, String localName, String value) throws IOException {
        boolean isIndexed;
        this.fiout.writeLowLevelStartAttributes();
        if (prefix == -1) {
            isIndexed = this.fiout.writeLowLevelAttribute("", "", localName);
        } else {
            isIndexed = this.fiout.writeLowLevelAttribute(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName);
        }
        if (!isIndexed) {
            this.tables.incrementMaxIndexValue();
        }
        this.fiout.writeLowLevelAttributeValue(value);
    }
}
