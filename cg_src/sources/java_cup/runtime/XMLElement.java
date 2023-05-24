package java_cup.runtime;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java_cup.runtime.ComplexSymbolFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/XMLElement.class */
public abstract class XMLElement {
    protected String tagname;

    public abstract List<XMLElement> selectById(String str);

    public abstract ComplexSymbolFactory.Location right();

    public abstract ComplexSymbolFactory.Location left();

    protected abstract void dump(XMLStreamWriter xMLStreamWriter) throws XMLStreamException;

    public static void dump(XMLStreamWriter writer, XMLElement elem, String... blacklist) throws XMLStreamException {
        dump(null, writer, elem, blacklist);
    }

    public static void dump(ScannerBuffer buffer, XMLStreamWriter writer, XMLElement elem, String... blacklist) throws XMLStreamException {
        writer.writeStartDocument("utf-8", "1.0");
        writer.writeProcessingInstruction("xml-stylesheet", "href=\"tree.xsl\" type=\"text/xsl\"");
        writer.writeStartElement("document");
        if (blacklist.length > 0) {
            writer.writeStartElement("blacklist");
            for (String s : blacklist) {
                writer.writeStartElement("symbol");
                writer.writeCharacters(s);
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeStartElement("parsetree");
        elem.dump(writer);
        writer.writeEndElement();
        if (buffer != null) {
            writer.writeStartElement("tokensequence");
            for (Symbol s2 : buffer.getBuffered()) {
                if (s2 instanceof ComplexSymbolFactory.ComplexSymbol) {
                    ComplexSymbolFactory.ComplexSymbol cs = (ComplexSymbolFactory.ComplexSymbol) s2;
                    if (cs.value != null) {
                        writer.writeStartElement("token");
                        writer.writeAttribute("name", cs.getName());
                        cs.getLeft().toXML(writer, "left");
                        writer.writeCharacters(new StringBuilder().append(cs.value).toString());
                        cs.getRight().toXML(writer, "right");
                        writer.writeEndElement();
                    } else {
                        writer.writeStartElement("keyword");
                        writer.writeAttribute("left", new StringBuilder().append(cs.getLeft()).toString());
                        writer.writeAttribute("right", new StringBuilder().append(cs.getRight()).toString());
                        writer.writeCharacters(new StringBuilder(String.valueOf(cs.getName())).toString());
                        writer.writeEndElement();
                    }
                } else if (s2 instanceof Symbol) {
                    writer.writeStartElement("token");
                    writer.writeCharacters(s2.toString());
                    writer.writeEndElement();
                }
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
        writer.close();
    }

    public String getTagname() {
        return this.tagname;
    }

    public List<XMLElement> getChildren() {
        return new LinkedList();
    }

    public boolean hasChildren() {
        return false;
    }

    /* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/XMLElement$NonTerminal.class */
    public static class NonTerminal extends XMLElement {
        private int variant;
        LinkedList<XMLElement> list;

        @Override // java_cup.runtime.XMLElement
        public boolean hasChildren() {
            return !this.list.isEmpty();
        }

        @Override // java_cup.runtime.XMLElement
        public List<XMLElement> getChildren() {
            return this.list;
        }

        @Override // java_cup.runtime.XMLElement
        public List<XMLElement> selectById(String s) {
            LinkedList<XMLElement> response = new LinkedList<>();
            if (this.tagname.equals(s)) {
                response.add(this);
            }
            Iterator<XMLElement> it = this.list.iterator();
            while (it.hasNext()) {
                XMLElement e = it.next();
                List<XMLElement> selection = e.selectById(s);
                response.addAll(selection);
            }
            return response;
        }

        public int getVariant() {
            return this.variant;
        }

        public NonTerminal(String tagname, int variant, XMLElement... l) {
            this.tagname = tagname;
            this.variant = variant;
            this.list = new LinkedList<>(Arrays.asList(l));
        }

        @Override // java_cup.runtime.XMLElement
        public ComplexSymbolFactory.Location left() {
            Iterator<XMLElement> it = this.list.iterator();
            while (it.hasNext()) {
                XMLElement e = it.next();
                ComplexSymbolFactory.Location loc = e.left();
                if (loc != null) {
                    return loc;
                }
            }
            return null;
        }

        @Override // java_cup.runtime.XMLElement
        public ComplexSymbolFactory.Location right() {
            Iterator<XMLElement> it = this.list.descendingIterator();
            while (it.hasNext()) {
                ComplexSymbolFactory.Location loc = it.next().right();
                if (loc != null) {
                    return loc;
                }
            }
            return null;
        }

        public String toString() {
            if (this.list.isEmpty()) {
                return "<nonterminal id=\"" + this.tagname + "\" variant=\"" + this.variant + "\" />";
            }
            String ret = "<nonterminal id=\"" + this.tagname + "\" left=\"" + left() + "\" right=\"" + right() + "\" variant=\"" + this.variant + "\">";
            Iterator<XMLElement> it = this.list.iterator();
            while (it.hasNext()) {
                XMLElement e = it.next();
                ret = String.valueOf(ret) + e.toString();
            }
            return String.valueOf(ret) + "</nonterminal>";
        }

        @Override // java_cup.runtime.XMLElement
        protected void dump(XMLStreamWriter writer) throws XMLStreamException {
            writer.writeStartElement("nonterminal");
            writer.writeAttribute("id", this.tagname);
            writer.writeAttribute("variant", new StringBuilder(String.valueOf(this.variant)).toString());
            ComplexSymbolFactory.Location loc = left();
            if (loc != null) {
                loc.toXML(writer, "left");
            }
            Iterator<XMLElement> it = this.list.iterator();
            while (it.hasNext()) {
                XMLElement e = it.next();
                e.dump(writer);
            }
            ComplexSymbolFactory.Location loc2 = right();
            if (loc2 != null) {
                loc2.toXML(writer, "right");
            }
            writer.writeEndElement();
        }
    }

    /* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/XMLElement$Error.class */
    public static class Error extends XMLElement {
        ComplexSymbolFactory.Location l;
        ComplexSymbolFactory.Location r;

        @Override // java_cup.runtime.XMLElement
        public boolean hasChildren() {
            return false;
        }

        @Override // java_cup.runtime.XMLElement
        public List<XMLElement> selectById(String s) {
            return new LinkedList();
        }

        public Error(ComplexSymbolFactory.Location l, ComplexSymbolFactory.Location r) {
            this.l = l;
            this.r = r;
        }

        @Override // java_cup.runtime.XMLElement
        public ComplexSymbolFactory.Location left() {
            return this.l;
        }

        @Override // java_cup.runtime.XMLElement
        public ComplexSymbolFactory.Location right() {
            return this.r;
        }

        public String toString() {
            return "<error left=\"" + this.l + "\" right=\"" + this.r + "\"/>";
        }

        @Override // java_cup.runtime.XMLElement
        protected void dump(XMLStreamWriter writer) throws XMLStreamException {
            writer.writeStartElement("error");
            writer.writeAttribute("left", new StringBuilder().append(left()).toString());
            writer.writeAttribute("right", new StringBuilder().append(right()).toString());
            writer.writeEndElement();
        }
    }

    /* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/XMLElement$Terminal.class */
    public static class Terminal extends XMLElement {
        ComplexSymbolFactory.Location l;
        ComplexSymbolFactory.Location r;
        Object value;

        @Override // java_cup.runtime.XMLElement
        public boolean hasChildren() {
            return false;
        }

        @Override // java_cup.runtime.XMLElement
        public List<XMLElement> selectById(String s) {
            List<XMLElement> ret = new LinkedList<>();
            if (this.tagname.equals(s)) {
                ret.add(this);
            }
            return ret;
        }

        public Terminal(ComplexSymbolFactory.Location l, String symbolname, ComplexSymbolFactory.Location r) {
            this(l, symbolname, null, r);
        }

        public Terminal(ComplexSymbolFactory.Location l, String symbolname, Object i, ComplexSymbolFactory.Location r) {
            this.l = l;
            this.r = r;
            this.value = i;
            this.tagname = symbolname;
        }

        public Object value() {
            return this.value;
        }

        @Override // java_cup.runtime.XMLElement
        public ComplexSymbolFactory.Location left() {
            return this.l;
        }

        @Override // java_cup.runtime.XMLElement
        public ComplexSymbolFactory.Location right() {
            return this.r;
        }

        public String toString() {
            return this.value == null ? "<terminal id=\"" + this.tagname + "\"/>" : "<terminal id=\"" + this.tagname + "\" left=\"" + this.l + "\" right=\"" + this.r + "\">" + this.value + "</terminal>";
        }

        @Override // java_cup.runtime.XMLElement
        protected void dump(XMLStreamWriter writer) throws XMLStreamException {
            writer.writeStartElement("terminal");
            writer.writeAttribute("id", this.tagname);
            writer.writeAttribute("left", new StringBuilder().append(left()).toString());
            writer.writeAttribute("right", new StringBuilder().append(right()).toString());
            if (this.value != null) {
                writer.writeCharacters(new StringBuilder().append(this.value).toString());
            }
            writer.writeEndElement();
        }
    }
}
