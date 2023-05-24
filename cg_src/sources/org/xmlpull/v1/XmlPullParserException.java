package org.xmlpull.v1;

import soot.coffi.Instruction;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:xmlpull-1.1.3.4d_b4_min.jar:org/xmlpull/v1/XmlPullParserException.class
 */
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/XmlPullParserException.class */
public class XmlPullParserException extends Exception {
    protected Throwable detail;
    protected int row;
    protected int column;

    public XmlPullParserException(String s) {
        super(s);
        this.row = -1;
        this.column = -1;
    }

    public XmlPullParserException(String msg, XmlPullParser parser, Throwable chain) {
        super(new StringBuffer().append(msg == null ? "" : new StringBuffer().append(msg).append(Instruction.argsep).toString()).append(parser == null ? "" : new StringBuffer().append("(position:").append(parser.getPositionDescription()).append(") ").toString()).append(chain == null ? "" : new StringBuffer().append("caused by: ").append(chain).toString()).toString());
        this.row = -1;
        this.column = -1;
        if (parser != null) {
            this.row = parser.getLineNumber();
            this.column = parser.getColumnNumber();
        }
        this.detail = chain;
    }

    public Throwable getDetail() {
        return this.detail;
    }

    public int getLineNumber() {
        return this.row;
    }

    public int getColumnNumber() {
        return this.column;
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        if (this.detail == null) {
            super.printStackTrace();
            return;
        }
        synchronized (System.err) {
            System.err.println(new StringBuffer().append(super.getMessage()).append("; nested exception is:").toString());
            this.detail.printStackTrace();
        }
    }
}
