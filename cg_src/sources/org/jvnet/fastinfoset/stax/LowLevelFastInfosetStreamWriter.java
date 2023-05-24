package org.jvnet.fastinfoset.stax;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:org/jvnet/fastinfoset/stax/LowLevelFastInfosetStreamWriter.class */
public interface LowLevelFastInfosetStreamWriter {
    void initiateLowLevelWriting() throws XMLStreamException;

    int getNextElementIndex();

    int getNextAttributeIndex();

    int getLocalNameIndex();

    int getNextLocalNameIndex();

    void writeLowLevelTerminationAndMark() throws IOException;

    void writeLowLevelStartElementIndexed(int i, int i2) throws IOException;

    boolean writeLowLevelStartElement(int i, String str, String str2, String str3) throws IOException;

    void writeLowLevelStartNamespaces() throws IOException;

    void writeLowLevelNamespace(String str, String str2) throws IOException;

    void writeLowLevelEndNamespaces() throws IOException;

    void writeLowLevelStartAttributes() throws IOException;

    void writeLowLevelAttributeIndexed(int i) throws IOException;

    boolean writeLowLevelAttribute(String str, String str2, String str3) throws IOException;

    void writeLowLevelAttributeValue(String str) throws IOException;

    void writeLowLevelStartNameLiteral(int i, String str, byte[] bArr, String str2) throws IOException;

    void writeLowLevelStartNameLiteral(int i, String str, int i2, String str2) throws IOException;

    void writeLowLevelEndStartElement() throws IOException;

    void writeLowLevelEndElement() throws IOException;

    void writeLowLevelText(char[] cArr, int i) throws IOException;

    void writeLowLevelText(String str) throws IOException;

    void writeLowLevelOctets(byte[] bArr, int i) throws IOException;
}
