package soot.coffi;

import java.io.PrintStream;
/* loaded from: gencallgraphv3.jar:soot/coffi/Info.class */
class Info {
    public ClassFile cf;
    public long flength;
    public int cp;
    public int fields;
    public int methods;
    public int pfields;
    public int pmethods;
    public int attribsave;
    public int attribcpsave;
    public int psave;

    public Info(ClassFile newcf) {
        this.cf = newcf;
    }

    public void verboseReport(PrintStream ps) {
        ps.println("<INFO> -- Debigulation Report on " + this.cf.fn + " --");
        ps.println("<INFO>   Length: " + this.flength);
        ps.println("<INFO>       CP: " + this.cp + " reduced to " + this.cf.constant_pool_count);
        ps.println("<INFO>   Fields: " + this.fields + " (" + this.pfields + " private) reduced to " + this.cf.fields_count);
        ps.println("<INFO>  Methods: " + this.methods + " (" + this.pmethods + " private) reduced to " + this.cf.methods_count);
        int total = this.attribsave + this.attribcpsave + this.psave;
        if (total > 0) {
            ps.println("<INFO> -- Savings through debigulation --");
            if (this.attribsave > 0) {
                ps.println("<INFO>         Attributes: " + this.attribsave);
            }
            if (this.attribcpsave > 0) {
                ps.println("<INFO>     CP Compression: " + this.attribcpsave);
            }
            if (this.psave > 0) {
                ps.println("<INFO>   Private renaming: " + this.psave);
            }
            ps.println("<INFO>  Total savings: " + total);
            double d = (total * 100000.0d) / this.flength;
            int x = (int) d;
            double d2 = x / 1000.0d;
            ps.println("<INFO>          ratio: " + d2 + "%");
        }
    }
}
