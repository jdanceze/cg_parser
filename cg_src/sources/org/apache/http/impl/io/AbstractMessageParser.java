package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/impl/io/AbstractMessageParser.class */
public abstract class AbstractMessageParser implements HttpMessageParser {
    private final SessionInputBuffer sessionBuffer;
    private final int maxHeaderCount;
    private final int maxLineLen;
    protected final LineParser lineParser;

    protected abstract HttpMessage parseHead(SessionInputBuffer sessionInputBuffer) throws IOException, HttpException, ParseException;

    public AbstractMessageParser(SessionInputBuffer buffer, LineParser parser, HttpParams params) {
        if (buffer == null) {
            throw new IllegalArgumentException("Session input buffer may not be null");
        }
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        this.sessionBuffer = buffer;
        this.maxHeaderCount = params.getIntParameter(CoreConnectionPNames.MAX_HEADER_COUNT, -1);
        this.maxLineLen = params.getIntParameter(CoreConnectionPNames.MAX_LINE_LENGTH, -1);
        this.lineParser = parser != null ? parser : BasicLineParser.DEFAULT;
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x0107, code lost:
        r0 = new org.apache.http.Header[r0.size()];
        r14 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x011b, code lost:
        if (r14 >= r0.size()) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x011e, code lost:
        r0 = (org.apache.http.util.CharArrayBuffer) r0.get(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x012a, code lost:
        r0[r14] = r9.parseHeader(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x013a, code lost:
        r16 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0148, code lost:
        throw new org.apache.http.ProtocolException(r16.getMessage());
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0149, code lost:
        r14 = r14 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0151, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static org.apache.http.Header[] parseHeaders(org.apache.http.io.SessionInputBuffer r6, int r7, int r8, org.apache.http.message.LineParser r9) throws org.apache.http.HttpException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 338
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.io.AbstractMessageParser.parseHeaders(org.apache.http.io.SessionInputBuffer, int, int, org.apache.http.message.LineParser):org.apache.http.Header[]");
    }

    @Override // org.apache.http.io.HttpMessageParser
    public HttpMessage parse() throws IOException, HttpException {
        try {
            HttpMessage message = parseHead(this.sessionBuffer);
            Header[] headers = parseHeaders(this.sessionBuffer, this.maxHeaderCount, this.maxLineLen, this.lineParser);
            message.setHeaders(headers);
            return message;
        } catch (ParseException px) {
            throw new ProtocolException(px.getMessage(), px);
        }
    }
}
