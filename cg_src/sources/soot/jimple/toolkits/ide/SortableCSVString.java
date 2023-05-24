package soot.jimple.toolkits.ide;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/SortableCSVString.class */
public class SortableCSVString implements Comparable<SortableCSVString> {
    String value;
    int position;

    public SortableCSVString(String str, int pos) {
        this.value = str;
        this.position = pos;
    }

    @Override // java.lang.Comparable
    public int compareTo(SortableCSVString anotherString) {
        String subString = this.value.substring(0, this.value.indexOf(59));
        String anotherSubString = anotherString.value.substring(0, anotherString.value.indexOf(59));
        int result = subString.compareTo(anotherSubString);
        if (result == 0) {
            if (this.position < anotherString.position) {
                return -1;
            }
            if (this.position > anotherString.position) {
                return 1;
            }
            return 0;
        }
        return result;
    }
}
