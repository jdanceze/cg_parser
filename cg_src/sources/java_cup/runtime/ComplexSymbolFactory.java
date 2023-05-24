package java_cup.runtime;

import android.provider.MediaStore;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/ComplexSymbolFactory.class */
public class ComplexSymbolFactory implements SymbolFactory {

    /* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/ComplexSymbolFactory$Location.class */
    public static class Location {
        private String unit;
        private int line;
        private int column;
        private int offset;

        public Location(Location other) {
            this(other.unit, other.line, other.column, other.offset);
        }

        public Location(String unit, int line, int column, int offset) {
            this(unit, line, column);
            this.offset = offset;
        }

        public Location(String unit, int line, int column) {
            this.unit = "unknown";
            this.offset = -1;
            this.unit = unit;
            this.line = line;
            this.column = column;
        }

        public Location(int line, int column, int offset) {
            this(line, column);
            this.offset = offset;
        }

        public Location(int line, int column) {
            this.unit = "unknown";
            this.offset = -1;
            this.line = line;
            this.column = column;
        }

        public int getColumn() {
            return this.column;
        }

        public int getLine() {
            return this.line;
        }

        public void move(int linediff, int coldiff, int offsetdiff) {
            if (this.line >= 0) {
                this.line += linediff;
            }
            if (this.column >= 0) {
                this.column += coldiff;
            }
            if (this.offset >= 0) {
                this.offset += offsetdiff;
            }
        }

        public static Location clone(Location other) {
            return new Location(other);
        }

        public String getUnit() {
            return this.unit;
        }

        public String toString() {
            return String.valueOf(getUnit()) + ":" + getLine() + "/" + getColumn() + "(" + this.offset + ")";
        }

        public void toXML(XMLStreamWriter writer, String orientation) throws XMLStreamException {
            writer.writeStartElement("location");
            writer.writeAttribute("compilationunit", this.unit);
            writer.writeAttribute(MediaStore.Images.ImageColumns.ORIENTATION, orientation);
            writer.writeAttribute("linenumber", new StringBuilder(String.valueOf(this.line)).toString());
            writer.writeAttribute("columnnumber", new StringBuilder(String.valueOf(this.column)).toString());
            writer.writeAttribute("offset", new StringBuilder(String.valueOf(this.offset)).toString());
            writer.writeEndElement();
        }

        public int getOffset() {
            return this.offset;
        }
    }

    /* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/ComplexSymbolFactory$ComplexSymbol.class */
    public static class ComplexSymbol extends Symbol {
        protected String name;
        public Location xleft;
        public Location xright;

        public ComplexSymbol(String name, int id) {
            super(id);
            this.name = name;
        }

        public ComplexSymbol(String name, int id, Object value) {
            super(id, value);
            this.name = name;
        }

        @Override // java_cup.runtime.Symbol
        public String toString() {
            return (this.xleft == null || this.xright == null) ? "Symbol: " + this.name : "Symbol: " + this.name + " (" + this.xleft + " - " + this.xright + ")";
        }

        public String getName() {
            return this.name;
        }

        public ComplexSymbol(String name, int id, int state) {
            super(id, state);
            this.name = name;
        }

        public ComplexSymbol(String name, int id, Symbol left, Symbol right) {
            super(id, left, right);
            this.name = name;
            if (left != null) {
                this.xleft = ((ComplexSymbol) left).xleft;
            }
            if (right != null) {
                this.xright = ((ComplexSymbol) right).xright;
            }
        }

        public ComplexSymbol(String name, int id, Location left, Location right) {
            super(id, left.offset, right.offset);
            this.name = name;
            this.xleft = left;
            this.xright = right;
        }

        public ComplexSymbol(String name, int id, Symbol left, Symbol right, Object value) {
            super(id, left.left, right.right, value);
            this.name = name;
            if (left != null) {
                this.xleft = ((ComplexSymbol) left).xleft;
            }
            if (right != null) {
                this.xright = ((ComplexSymbol) right).xright;
            }
        }

        public ComplexSymbol(String name, int id, Symbol left, Object value) {
            super(id, left.right, left.right, value);
            this.name = name;
            if (left != null) {
                this.xleft = ((ComplexSymbol) left).xright;
                this.xright = ((ComplexSymbol) left).xright;
            }
        }

        public ComplexSymbol(String name, int id, Location left, Location right, Object value) {
            super(id, left.offset, right.offset, value);
            this.name = name;
            this.xleft = left;
            this.xright = right;
        }

        public Location getLeft() {
            return this.xleft;
        }

        public Location getRight() {
            return this.xright;
        }
    }

    public Symbol newSymbol(String name, int id, Location left, Location right, Object value) {
        return new ComplexSymbol(name, id, left, right, value);
    }

    public Symbol newSymbol(String name, int id, Location left, Location right) {
        return new ComplexSymbol(name, id, left, right);
    }

    @Override // java_cup.runtime.SymbolFactory
    public Symbol newSymbol(String name, int id, Symbol left, Object value) {
        return new ComplexSymbol(name, id, left, value);
    }

    @Override // java_cup.runtime.SymbolFactory
    public Symbol newSymbol(String name, int id, Symbol left, Symbol right, Object value) {
        return new ComplexSymbol(name, id, left, right, value);
    }

    @Override // java_cup.runtime.SymbolFactory
    public Symbol newSymbol(String name, int id, Symbol left, Symbol right) {
        return new ComplexSymbol(name, id, left, right);
    }

    @Override // java_cup.runtime.SymbolFactory
    public Symbol newSymbol(String name, int id) {
        return new ComplexSymbol(name, id);
    }

    @Override // java_cup.runtime.SymbolFactory
    public Symbol newSymbol(String name, int id, Object value) {
        return new ComplexSymbol(name, id, value);
    }

    @Override // java_cup.runtime.SymbolFactory
    public Symbol startSymbol(String name, int id, int state) {
        return new ComplexSymbol(name, id, state);
    }
}
