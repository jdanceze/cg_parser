package javax.xml.soap;

import java.util.Iterator;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/MimeHeaders.class */
public class MimeHeaders {
    private Vector headers = new Vector();

    /* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/MimeHeaders$MatchingIterator.class */
    class MatchingIterator implements Iterator {
        private boolean match;
        private Iterator iterator;
        private String[] names;
        private Object nextHeader;
        private final MimeHeaders this$0;

        MatchingIterator(MimeHeaders mimeHeaders, String[] strArr, boolean z) {
            this.this$0 = mimeHeaders;
            this.match = z;
            this.names = strArr;
            this.iterator = mimeHeaders.headers.iterator();
        }

        private Object nextMatch() {
            while (this.iterator.hasNext()) {
                MimeHeader mimeHeader = (MimeHeader) this.iterator.next();
                if (this.names == null) {
                    if (this.match) {
                        return null;
                    }
                    return mimeHeader;
                }
                int i = 0;
                while (true) {
                    if (i >= this.names.length) {
                        if (!this.match) {
                            return mimeHeader;
                        }
                    } else if (!mimeHeader.getName().equalsIgnoreCase(this.names[i])) {
                        i++;
                    } else if (this.match) {
                        return mimeHeader;
                    }
                }
            }
            return null;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextHeader == null) {
                this.nextHeader = nextMatch();
            }
            return this.nextHeader != null;
        }

        @Override // java.util.Iterator
        public Object next() {
            if (this.nextHeader != null) {
                Object obj = this.nextHeader;
                this.nextHeader = null;
                return obj;
            } else if (hasNext()) {
                return this.nextHeader;
            } else {
                return null;
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iterator.remove();
        }
    }

    public String[] getHeader(String str) {
        Vector vector = new Vector();
        for (int i = 0; i < this.headers.size(); i++) {
            MimeHeader mimeHeader = (MimeHeader) this.headers.elementAt(i);
            if (mimeHeader.getName().equalsIgnoreCase(str) && mimeHeader.getValue() != null) {
                vector.addElement(mimeHeader.getValue());
            }
        }
        if (vector.size() == 0) {
            return null;
        }
        String[] strArr = new String[vector.size()];
        vector.copyInto(strArr);
        return strArr;
    }

    public void setHeader(String str, String str2) {
        boolean z = false;
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException("Illegal MimeHeader name");
        }
        int i = 0;
        while (i < this.headers.size()) {
            MimeHeader mimeHeader = (MimeHeader) this.headers.elementAt(i);
            if (mimeHeader.getName().equalsIgnoreCase(str)) {
                if (z) {
                    int i2 = i;
                    i = i2 - 1;
                    this.headers.removeElementAt(i2);
                } else {
                    this.headers.setElementAt(new MimeHeader(mimeHeader.getName(), str2), i);
                    z = true;
                }
            }
            i++;
        }
        if (z) {
            return;
        }
        addHeader(str, str2);
    }

    public void addHeader(String str, String str2) {
        if (str == null || str.equals("")) {
            throw new IllegalArgumentException("Illegal MimeHeader name");
        }
        for (int size = this.headers.size() - 1; size >= 0; size--) {
            if (((MimeHeader) this.headers.elementAt(size)).getName().equalsIgnoreCase(str)) {
                this.headers.insertElementAt(new MimeHeader(str, str2), size + 1);
                return;
            }
        }
        this.headers.addElement(new MimeHeader(str, str2));
    }

    public void removeHeader(String str) {
        int i = 0;
        while (i < this.headers.size()) {
            if (((MimeHeader) this.headers.elementAt(i)).getName().equalsIgnoreCase(str)) {
                int i2 = i;
                i = i2 - 1;
                this.headers.removeElementAt(i2);
            }
            i++;
        }
    }

    public void removeAllHeaders() {
        this.headers.removeAllElements();
    }

    public Iterator getAllHeaders() {
        return this.headers.iterator();
    }

    public Iterator getMatchingHeaders(String[] strArr) {
        return new MatchingIterator(this, strArr, true);
    }

    public Iterator getNonMatchingHeaders(String[] strArr) {
        return new MatchingIterator(this, strArr, false);
    }
}
