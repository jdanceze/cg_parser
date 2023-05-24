package soot.dava.internal.SET;

import soot.G;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETNodeLabel.class */
public class SETNodeLabel {
    private String name = null;

    public void set_Name() {
        if (this.name == null) {
            StringBuilder sb = new StringBuilder("label_");
            G v = G.v();
            int i = v.SETNodeLabel_uniqueId;
            v.SETNodeLabel_uniqueId = i + 1;
            this.name = sb.append(Integer.toString(i)).toString();
        }
    }

    public void set_Name(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public void clear_Name() {
        this.name = null;
    }
}
