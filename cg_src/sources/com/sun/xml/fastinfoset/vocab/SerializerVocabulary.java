package com.sun.xml.fastinfoset.vocab;

import com.sun.xml.fastinfoset.EncodingConstants;
import com.sun.xml.fastinfoset.QualifiedName;
import com.sun.xml.fastinfoset.util.CharArrayIntMap;
import com.sun.xml.fastinfoset.util.FixedEntryStringIntMap;
import com.sun.xml.fastinfoset.util.KeyIntMap;
import com.sun.xml.fastinfoset.util.LocalNameQualifiedNamesMap;
import com.sun.xml.fastinfoset.util.StringIntMap;
import java.util.Iterator;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/vocab/SerializerVocabulary.class */
public class SerializerVocabulary extends Vocabulary {
    public final StringIntMap restrictedAlphabet;
    public final StringIntMap encodingAlgorithm;
    public final StringIntMap namespaceName;
    public final StringIntMap prefix;
    public final StringIntMap localName;
    public final StringIntMap otherNCName;
    public final StringIntMap otherURI;
    public final StringIntMap attributeValue;
    public final CharArrayIntMap otherString;
    public final CharArrayIntMap characterContentChunk;
    public final LocalNameQualifiedNamesMap elementName;
    public final LocalNameQualifiedNamesMap attributeName;
    public final KeyIntMap[] tables;
    protected boolean _useLocalNameAsKey;
    protected SerializerVocabulary _readOnlyVocabulary;

    public SerializerVocabulary() {
        this.tables = new KeyIntMap[12];
        KeyIntMap[] keyIntMapArr = this.tables;
        StringIntMap stringIntMap = new StringIntMap(4);
        this.restrictedAlphabet = stringIntMap;
        keyIntMapArr[0] = stringIntMap;
        KeyIntMap[] keyIntMapArr2 = this.tables;
        StringIntMap stringIntMap2 = new StringIntMap(4);
        this.encodingAlgorithm = stringIntMap2;
        keyIntMapArr2[1] = stringIntMap2;
        KeyIntMap[] keyIntMapArr3 = this.tables;
        FixedEntryStringIntMap fixedEntryStringIntMap = new FixedEntryStringIntMap(EncodingConstants.XML_NAMESPACE_PREFIX, 8);
        this.prefix = fixedEntryStringIntMap;
        keyIntMapArr3[2] = fixedEntryStringIntMap;
        KeyIntMap[] keyIntMapArr4 = this.tables;
        FixedEntryStringIntMap fixedEntryStringIntMap2 = new FixedEntryStringIntMap("http://www.w3.org/XML/1998/namespace", 8);
        this.namespaceName = fixedEntryStringIntMap2;
        keyIntMapArr4[3] = fixedEntryStringIntMap2;
        KeyIntMap[] keyIntMapArr5 = this.tables;
        StringIntMap stringIntMap3 = new StringIntMap();
        this.localName = stringIntMap3;
        keyIntMapArr5[4] = stringIntMap3;
        KeyIntMap[] keyIntMapArr6 = this.tables;
        StringIntMap stringIntMap4 = new StringIntMap(4);
        this.otherNCName = stringIntMap4;
        keyIntMapArr6[5] = stringIntMap4;
        KeyIntMap[] keyIntMapArr7 = this.tables;
        StringIntMap stringIntMap5 = new StringIntMap(4);
        this.otherURI = stringIntMap5;
        keyIntMapArr7[6] = stringIntMap5;
        KeyIntMap[] keyIntMapArr8 = this.tables;
        StringIntMap stringIntMap6 = new StringIntMap();
        this.attributeValue = stringIntMap6;
        keyIntMapArr8[7] = stringIntMap6;
        KeyIntMap[] keyIntMapArr9 = this.tables;
        CharArrayIntMap charArrayIntMap = new CharArrayIntMap(4);
        this.otherString = charArrayIntMap;
        keyIntMapArr9[8] = charArrayIntMap;
        KeyIntMap[] keyIntMapArr10 = this.tables;
        CharArrayIntMap charArrayIntMap2 = new CharArrayIntMap();
        this.characterContentChunk = charArrayIntMap2;
        keyIntMapArr10[9] = charArrayIntMap2;
        KeyIntMap[] keyIntMapArr11 = this.tables;
        LocalNameQualifiedNamesMap localNameQualifiedNamesMap = new LocalNameQualifiedNamesMap();
        this.elementName = localNameQualifiedNamesMap;
        keyIntMapArr11[10] = localNameQualifiedNamesMap;
        KeyIntMap[] keyIntMapArr12 = this.tables;
        LocalNameQualifiedNamesMap localNameQualifiedNamesMap2 = new LocalNameQualifiedNamesMap();
        this.attributeName = localNameQualifiedNamesMap2;
        keyIntMapArr12[11] = localNameQualifiedNamesMap2;
    }

    public SerializerVocabulary(org.jvnet.fastinfoset.Vocabulary v, boolean useLocalNameAsKey) {
        this();
        this._useLocalNameAsKey = useLocalNameAsKey;
        convertVocabulary(v);
    }

    public SerializerVocabulary getReadOnlyVocabulary() {
        return this._readOnlyVocabulary;
    }

    protected void setReadOnlyVocabulary(SerializerVocabulary readOnlyVocabulary, boolean clear) {
        for (int i = 0; i < this.tables.length; i++) {
            this.tables[i].setReadOnlyMap(readOnlyVocabulary.tables[i], clear);
        }
    }

    public void setInitialVocabulary(SerializerVocabulary initialVocabulary, boolean clear) {
        setExternalVocabularyURI(null);
        setInitialReadOnlyVocabulary(true);
        setReadOnlyVocabulary(initialVocabulary, clear);
    }

    public void setExternalVocabulary(String externalVocabularyURI, SerializerVocabulary externalVocabulary, boolean clear) {
        setInitialReadOnlyVocabulary(false);
        setExternalVocabularyURI(externalVocabularyURI);
        setReadOnlyVocabulary(externalVocabulary, clear);
    }

    public void clear() {
        for (int i = 0; i < this.tables.length; i++) {
            this.tables[i].clear();
        }
    }

    private void convertVocabulary(org.jvnet.fastinfoset.Vocabulary v) {
        addToTable(v.restrictedAlphabets.iterator(), this.restrictedAlphabet);
        addToTable(v.encodingAlgorithms.iterator(), this.encodingAlgorithm);
        addToTable(v.prefixes.iterator(), this.prefix);
        addToTable(v.namespaceNames.iterator(), this.namespaceName);
        addToTable(v.localNames.iterator(), this.localName);
        addToTable(v.otherNCNames.iterator(), this.otherNCName);
        addToTable(v.otherURIs.iterator(), this.otherURI);
        addToTable(v.attributeValues.iterator(), this.attributeValue);
        addToTable(v.otherStrings.iterator(), this.otherString);
        addToTable(v.characterContentChunks.iterator(), this.characterContentChunk);
        addToTable(v.elements.iterator(), this.elementName);
        addToTable(v.attributes.iterator(), this.attributeName);
    }

    private void addToTable(Iterator i, StringIntMap m) {
        while (i.hasNext()) {
            addToTable((String) i.next(), m);
        }
    }

    private void addToTable(String s, StringIntMap m) {
        if (s.length() == 0) {
            return;
        }
        m.obtainIndex(s);
    }

    private void addToTable(Iterator i, CharArrayIntMap m) {
        while (i.hasNext()) {
            addToTable((String) i.next(), m);
        }
    }

    private void addToTable(String s, CharArrayIntMap m) {
        if (s.length() == 0) {
            return;
        }
        char[] c = s.toCharArray();
        m.obtainIndex(c, 0, c.length, false);
    }

    private void addToTable(Iterator i, LocalNameQualifiedNamesMap m) {
        while (i.hasNext()) {
            addToNameTable((QName) i.next(), m);
        }
    }

    private void addToNameTable(QName n, LocalNameQualifiedNamesMap m) {
        String str;
        LocalNameQualifiedNamesMap.Entry entry;
        int namespaceURIIndex = -1;
        int prefixIndex = -1;
        if (n.getNamespaceURI().length() > 0) {
            namespaceURIIndex = this.namespaceName.obtainIndex(n.getNamespaceURI());
            if (namespaceURIIndex == -1) {
                namespaceURIIndex = this.namespaceName.get(n.getNamespaceURI());
            }
            if (n.getPrefix().length() > 0) {
                prefixIndex = this.prefix.obtainIndex(n.getPrefix());
                if (prefixIndex == -1) {
                    prefixIndex = this.prefix.get(n.getPrefix());
                }
            }
        }
        int localNameIndex = this.localName.obtainIndex(n.getLocalPart());
        if (localNameIndex == -1) {
            localNameIndex = this.localName.get(n.getLocalPart());
        }
        QualifiedName name = new QualifiedName(n.getPrefix(), n.getNamespaceURI(), n.getLocalPart(), m.getNextIndex(), prefixIndex, namespaceURIIndex, localNameIndex);
        if (this._useLocalNameAsKey) {
            entry = m.obtainEntry(n.getLocalPart());
        } else {
            if (prefixIndex == -1) {
                str = n.getLocalPart();
            } else {
                str = n.getPrefix() + ":" + n.getLocalPart();
            }
            String qName = str;
            entry = m.obtainEntry(qName);
        }
        entry.addQualifiedName(name);
    }
}
