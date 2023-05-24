package soot.asm;

import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/asm/LineNumberExtraction.class */
public class LineNumberExtraction {
    public void iterator() {
        Iterator it = new ArrayList().iterator();
        while (it.hasNext()) {
            Object object = it.next();
            System.out.println(object);
        }
    }
}
