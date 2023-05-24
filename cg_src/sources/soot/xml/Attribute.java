package soot.xml;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import soot.tagkit.ColorTag;
import soot.tagkit.Host;
import soot.tagkit.JimpleLineNumberTag;
import soot.tagkit.LineNumberTag;
import soot.tagkit.LinkTag;
import soot.tagkit.PositionTag;
import soot.tagkit.SourceLnPosTag;
import soot.tagkit.StringTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/xml/Attribute.class */
public class Attribute {
    private ArrayList<ColorAttribute> colors;
    private ArrayList<StringAttribute> texts;
    private ArrayList<LinkAttribute> links;
    private int jimpleStartPos;
    private int jimpleEndPos;
    private int javaStartPos;
    private int javaEndPos;
    private int javaStartLn;
    private int javaEndLn;
    private int jimpleStartLn;
    private int jimpleEndLn;

    public ArrayList<ColorAttribute> colors() {
        return this.colors;
    }

    public void addColor(ColorAttribute ca) {
        ArrayList<ColorAttribute> colors = this.colors;
        if (colors == null) {
            ArrayList<ColorAttribute> arrayList = new ArrayList<>();
            colors = arrayList;
            this.colors = arrayList;
        }
        colors.add(ca);
    }

    public void addText(StringAttribute s) {
        ArrayList<StringAttribute> texts = this.texts;
        if (texts == null) {
            ArrayList<StringAttribute> arrayList = new ArrayList<>();
            texts = arrayList;
            this.texts = arrayList;
        }
        texts.add(s);
    }

    public void addLink(LinkAttribute la) {
        ArrayList<LinkAttribute> links = this.links;
        if (links == null) {
            ArrayList<LinkAttribute> arrayList = new ArrayList<>();
            links = arrayList;
            this.links = arrayList;
        }
        links.add(la);
    }

    public int jimpleStartPos() {
        return this.jimpleStartPos;
    }

    public void jimpleStartPos(int x) {
        this.jimpleStartPos = x;
    }

    public int jimpleEndPos() {
        return this.jimpleEndPos;
    }

    public void jimpleEndPos(int x) {
        this.jimpleEndPos = x;
    }

    public int javaStartPos() {
        return this.javaStartPos;
    }

    public void javaStartPos(int x) {
        this.javaStartPos = x;
    }

    public int javaEndPos() {
        return this.javaEndPos;
    }

    public void javaEndPos(int x) {
        this.javaEndPos = x;
    }

    public int jimpleStartLn() {
        return this.jimpleStartLn;
    }

    public void jimpleStartLn(int x) {
        this.jimpleStartLn = x;
    }

    public int jimpleEndLn() {
        return this.jimpleEndLn;
    }

    public void jimpleEndLn(int x) {
        this.jimpleEndLn = x;
    }

    public int javaStartLn() {
        return this.javaStartLn;
    }

    public void javaStartLn(int x) {
        this.javaStartLn = x;
    }

    public int javaEndLn() {
        return this.javaEndLn;
    }

    public void javaEndLn(int x) {
        this.javaEndLn = x;
    }

    public boolean hasColor() {
        return this.colors != null;
    }

    public void addTag(Tag t) {
        if (t instanceof LineNumberTag) {
            int lnNum = ((LineNumberTag) t).getLineNumber();
            javaStartLn(lnNum);
            javaEndLn(lnNum);
        } else if (t instanceof JimpleLineNumberTag) {
            JimpleLineNumberTag jlnTag = (JimpleLineNumberTag) t;
            jimpleStartLn(jlnTag.getStartLineNumber());
            jimpleEndLn(jlnTag.getEndLineNumber());
        } else if (t instanceof SourceLnPosTag) {
            SourceLnPosTag jlnTag2 = (SourceLnPosTag) t;
            javaStartLn(jlnTag2.startLn());
            javaEndLn(jlnTag2.endLn());
            javaStartPos(jlnTag2.startPos());
            javaEndPos(jlnTag2.endPos());
        } else if (t instanceof LinkTag) {
            LinkTag lt = (LinkTag) t;
            Host h = lt.getLink();
            addLink(new LinkAttribute(lt.getInfo(), getJimpleLnOfHost(h), getJavaLnOfHost(h), lt.getClassName(), lt.getAnalysisType()));
        } else if (t instanceof StringTag) {
            StringTag st = (StringTag) t;
            addText(new StringAttribute(formatForXML(st.getInfo()), st.getAnalysisType()));
        } else if (t instanceof PositionTag) {
            PositionTag pt = (PositionTag) t;
            jimpleStartPos(pt.getStartOffset());
            jimpleEndPos(pt.getEndOffset());
        } else if (t instanceof ColorTag) {
            ColorTag ct = (ColorTag) t;
            addColor(new ColorAttribute(ct.getRed(), ct.getGreen(), ct.getBlue(), ct.isForeground(), ct.getAnalysisType()));
        } else {
            addText(new StringAttribute(t.toString(), t.getName()));
        }
    }

    private String formatForXML(String in) {
        return in.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&", "&amp;").replaceAll("\"", "&quot;");
    }

    private int getJavaLnOfHost(Host h) {
        for (Tag t : h.getTags()) {
            if (t instanceof SourceLnPosTag) {
                return ((SourceLnPosTag) t).startLn();
            }
            if (t instanceof LineNumberTag) {
                return ((LineNumberTag) t).getLineNumber();
            }
        }
        return 0;
    }

    private int getJimpleLnOfHost(Host h) {
        for (Tag t : h.getTags()) {
            if (t instanceof JimpleLineNumberTag) {
                return ((JimpleLineNumberTag) t).getStartLineNumber();
            }
        }
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<srcPos sline=\"").append(javaStartLn());
        sb.append("\" eline=\"").append(javaEndLn());
        sb.append("\" spos=\"").append(javaStartPos());
        sb.append("\" epos=\"").append(javaEndPos()).append("\"/>");
        sb.append("<jmpPos sline=\"").append(jimpleStartLn());
        sb.append("\" eline=\"").append(jimpleEndLn());
        sb.append("\" spos=\"").append(jimpleStartPos());
        sb.append("\" epos=\"").append(jimpleEndPos()).append("\"/>");
        return sb.toString();
    }

    public boolean isEmpty() {
        return this.colors == null && this.texts == null && this.links == null;
    }

    public void print(PrintWriter writerOut) {
        if (isEmpty()) {
            return;
        }
        writerOut.println("<attribute>");
        writerOut.println("<srcPos sline=\"" + javaStartLn() + "\" eline=\"" + javaEndLn() + "\" spos=\"" + javaStartPos() + "\" epos=\"" + javaEndPos() + "\"/>");
        writerOut.println("<jmpPos sline=\"" + jimpleStartLn() + "\" eline=\"" + jimpleEndLn() + "\" spos=\"" + jimpleStartPos() + "\" epos=\"" + jimpleEndPos() + "\"/>");
        if (this.colors != null) {
            Iterator<ColorAttribute> it = this.colors.iterator();
            while (it.hasNext()) {
                ColorAttribute ca = it.next();
                writerOut.println("<color r=\"" + ca.red() + "\" g=\"" + ca.green() + "\" b=\"" + ca.blue() + "\" fg=\"" + ca.fg() + "\" aType=\"" + ca.analysisType() + "\"/>");
            }
        }
        if (this.texts != null) {
            Iterator<StringAttribute> it2 = this.texts.iterator();
            while (it2.hasNext()) {
                StringAttribute sa = it2.next();
                writerOut.println("<text info=\"" + sa.info() + "\" aType=\"" + sa.analysisType() + "\"/>");
            }
        }
        if (this.links != null) {
            Iterator<LinkAttribute> it3 = this.links.iterator();
            while (it3.hasNext()) {
                LinkAttribute la = it3.next();
                writerOut.println("<link label=\"" + formatForXML(la.info()) + "\" jmpLink=\"" + la.jimpleLink() + "\" srcLink=\"" + la.javaLink() + "\" clssNm=\"" + la.className() + "\" aType=\"" + la.analysisType() + "\"/>");
            }
        }
        writerOut.println("</attribute>");
    }
}
