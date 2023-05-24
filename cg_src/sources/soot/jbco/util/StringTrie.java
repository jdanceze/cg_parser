package soot.jbco.util;
/* loaded from: gencallgraphv3.jar:soot/jbco/util/StringTrie.class */
public class StringTrie {
    private char[] startChars = new char[0];
    private StringTrie[] tries = new StringTrie[0];

    public void add(char[] chars, int index) {
        if (chars.length == index) {
            return;
        }
        if (this.startChars.length == 0) {
            this.startChars = new char[1];
            this.startChars[0] = chars[0];
            this.tries = new StringTrie[1];
            int i = index + 1;
            this.tries[0].add(chars, index);
            return;
        }
        int i2 = findStart(chars[index], 0, this.startChars.length - 1);
        if (i2 >= 0) {
            int i3 = index + 1;
            this.tries[i2].add(chars, index);
            return;
        }
        int i4 = addChar(chars[index]);
        int i5 = index + 1;
        this.tries[i4].add(chars, index);
    }

    private int addChar(char c) {
        int oldLength = this.startChars.length;
        int i = findSpot(c, 0, oldLength - 1);
        char[] tmp = (char[]) this.startChars.clone();
        StringTrie[] t = (StringTrie[]) this.tries.clone();
        this.startChars = new char[oldLength + 1];
        this.tries = new StringTrie[oldLength + 1];
        if (i > 0) {
            System.arraycopy(tmp, 0, this.startChars, 0, i);
            System.arraycopy(t, 0, this.tries, 0, i);
        }
        if (i < oldLength) {
            System.arraycopy(tmp, i, this.startChars, i + 1, oldLength - i);
            System.arraycopy(t, i, this.tries, i + 1, oldLength - i);
        }
        this.startChars[i] = c;
        this.tries[i] = new StringTrie();
        return i;
    }

    private int findSpot(char c, int first, int last) {
        int diff = last - first;
        if (diff == 1) {
            return c < this.startChars[first] ? first : c < this.startChars[last] ? last : last + 1;
        }
        int diff2 = diff / 2;
        if (this.startChars[first + diff2] < c) {
            return findSpot(c, first + diff2, last);
        }
        return findSpot(c, first, last - diff2);
    }

    public boolean contains(char[] chars, int index) {
        int i;
        if (chars.length == index) {
            return true;
        }
        if (this.startChars.length != 0 && (i = findStart(chars[index], 0, this.startChars.length - 1)) >= 0) {
            int i2 = index + 1;
            return this.tries[i].contains(chars, index);
        }
        return false;
    }

    private int findStart(char c, int first, int last) {
        int diff = last - first;
        if (diff <= 1) {
            if (c == this.startChars[first]) {
                return first;
            }
            if (c == this.startChars[last]) {
                return last;
            }
            return -1;
        }
        int diff2 = diff / 2;
        if (this.startChars[first + diff2] <= c) {
            return findStart(c, first + diff2, last);
        }
        return findStart(c, first, last - diff2);
    }
}
