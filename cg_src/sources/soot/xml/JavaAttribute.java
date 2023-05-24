package soot.xml;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.tagkit.ColorTag;
import soot.tagkit.Host;
import soot.tagkit.JimpleLineNumberTag;
import soot.tagkit.LineNumberTag;
import soot.tagkit.LinkTag;
import soot.tagkit.PositionTag;
import soot.tagkit.SourceLnPosTag;
import soot.tagkit.SourcePositionTag;
import soot.tagkit.StringTag;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/xml/JavaAttribute.class */
public class JavaAttribute {
    private static final Logger logger = LoggerFactory.getLogger(JavaAttribute.class);
    private int startLn;
    private ArrayList<Tag> tags;
    private ArrayList<PosColorAttribute> vbAttrs;
    public PrintWriter writerOut;

    public int startLn() {
        return this.startLn;
    }

    public void startLn(int x) {
        this.startLn = x;
    }

    public ArrayList<Tag> tags() {
        return this.tags;
    }

    public void addTag(Tag t) {
        ArrayList<Tag> tags = this.tags;
        if (tags == null) {
            ArrayList<Tag> arrayList = new ArrayList<>();
            tags = arrayList;
            this.tags = arrayList;
        }
        tags.add(t);
    }

    public ArrayList<PosColorAttribute> vbAttrs() {
        return this.vbAttrs;
    }

    public void addVbAttr(PosColorAttribute vbAttr) {
        ArrayList<PosColorAttribute> vbAttrs = this.vbAttrs;
        if (vbAttrs == null) {
            ArrayList<PosColorAttribute> arrayList = new ArrayList<>();
            vbAttrs = arrayList;
            this.vbAttrs = arrayList;
        }
        vbAttrs.add(vbAttr);
    }

    public boolean hasColorTag() {
        if (this.tags != null) {
            Iterator<Tag> it = this.tags.iterator();
            while (it.hasNext()) {
                Tag t = it.next();
                if (t instanceof ColorTag) {
                    return true;
                }
            }
        }
        if (this.vbAttrs != null) {
            Iterator<PosColorAttribute> it2 = this.vbAttrs.iterator();
            while (it2.hasNext()) {
                PosColorAttribute t2 = it2.next();
                if (t2.hasColor()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void printAttributeTag(Tag t) {
        if (t instanceof LineNumberTag) {
            int lnNum = ((LineNumberTag) t).getLineNumber();
            printJavaLnAttr(lnNum, lnNum);
        } else if (t instanceof JimpleLineNumberTag) {
            JimpleLineNumberTag jlnTag = (JimpleLineNumberTag) t;
            printJimpleLnAttr(jlnTag.getStartLineNumber(), jlnTag.getEndLineNumber());
        } else if (t instanceof LinkTag) {
            LinkTag lt = (LinkTag) t;
            Host h = lt.getLink();
            printLinkAttr(formatForXML(lt.toString()), getJimpleLnOfHost(h), getJavaLnOfHost(h), lt.getClassName());
        } else if (t instanceof StringTag) {
            printTextAttr(formatForXML(((StringTag) t).toString()));
        } else if (t instanceof SourcePositionTag) {
            SourcePositionTag pt = (SourcePositionTag) t;
            printSourcePositionAttr(pt.getStartOffset(), pt.getEndOffset());
        } else if (t instanceof PositionTag) {
            PositionTag pt2 = (PositionTag) t;
            printJimplePositionAttr(pt2.getStartOffset(), pt2.getEndOffset());
        } else if (t instanceof ColorTag) {
            ColorTag ct = (ColorTag) t;
            printColorAttr(ct.getRed(), ct.getGreen(), ct.getBlue(), ct.isForeground());
        } else {
            printTextAttr(t.toString());
        }
    }

    private void printJavaLnAttr(int start_ln, int end_ln) {
        this.writerOut.println("<javaStartLn>" + start_ln + "</javaStartLn>");
        this.writerOut.println("<javaEndLn>" + end_ln + "</javaEndLn>");
    }

    private void printJimpleLnAttr(int start_ln, int end_ln) {
        this.writerOut.println("<jimpleStartLn>" + start_ln + "</jimpleStartLn>");
        this.writerOut.println("<jimpleEndLn>" + end_ln + "</jimpleEndLn>");
    }

    private void printTextAttr(String text) {
        this.writerOut.println("<text>" + text + "</text>");
    }

    private void printLinkAttr(String label, int jimpleLink, int javaLink, String className) {
        this.writerOut.println("<link_attribute>");
        this.writerOut.println("<link_label>" + label + "</link_label>");
        this.writerOut.println("<jimple_link>" + jimpleLink + "</jimple_link>");
        this.writerOut.println("<java_link>" + javaLink + "</java_link>");
        this.writerOut.println("<className>" + className + "</className>");
        this.writerOut.println("</link_attribute>");
    }

    private void startPrintValBoxAttr() {
        this.writerOut.println("<value_box_attribute>");
    }

    private void printSourcePositionAttr(int start, int end) {
        this.writerOut.println("<javaStartPos>" + start + "</javaStartPos>");
        this.writerOut.println("<javaEndPos>" + end + "</javaEndPos>");
    }

    private void printJimplePositionAttr(int start, int end) {
        this.writerOut.println("<jimpleStartPos>" + start + "</jimpleStartPos>");
        this.writerOut.println("<jimpleEndPos>" + end + "</jimpleEndPos>");
    }

    private void printColorAttr(int r, int g, int b, boolean fg) {
        this.writerOut.println("<red>" + r + "</red>");
        this.writerOut.println("<green>" + g + "</green>");
        this.writerOut.println("<blue>" + b + "</blue>");
        this.writerOut.println(fg ? "<fg>1</fg>" : "<fg>0</fg>");
    }

    private void endPrintValBoxAttr() {
        this.writerOut.println("</value_box_attribute>");
    }

    public void printAllTags(PrintWriter writer) {
        this.writerOut = writer;
        if (this.tags != null) {
            Iterator<Tag> it = this.tags.iterator();
            while (it.hasNext()) {
                Tag t = it.next();
                printAttributeTag(t);
            }
        }
        if (this.vbAttrs != null) {
            Iterator<PosColorAttribute> it2 = this.vbAttrs.iterator();
            while (it2.hasNext()) {
                PosColorAttribute attr = it2.next();
                if (attr.hasColor()) {
                    startPrintValBoxAttr();
                    printSourcePositionAttr(attr.javaStartPos(), attr.javaEndPos());
                    printJimplePositionAttr(attr.jimpleStartPos(), attr.jimpleEndPos());
                    endPrintValBoxAttr();
                }
            }
        }
    }

    public void printInfoTags(PrintWriter writer) {
        this.writerOut = writer;
        Iterator<Tag> it = this.tags.iterator();
        while (it.hasNext()) {
            Tag t = it.next();
            printAttributeTag(t);
        }
    }

    private String formatForXML(String in) {
        return in.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&", "&amp;");
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
}
