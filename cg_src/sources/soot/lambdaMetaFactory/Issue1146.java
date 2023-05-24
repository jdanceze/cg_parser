package soot.lambdaMetaFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
/* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Issue1146.class */
public class Issue1146 {
    public Vertrag getVertrag(String vsnr) {
        List<String> myList = Arrays.asList("element1", "element2", "element3");
        myList.forEach(element -> {
            System.out.println(element);
        });
        return new Vertrag(this, null);
    }

    public Vertrag getVertrag2(String vsnr) throws BpmnError {
        return (Vertrag) Optional.ofNullable(null).orElseThrow(() -> {
            return new BpmnError("not found");
        });
    }

    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Issue1146$Vertrag.class */
    private class Vertrag {
        private Vertrag() {
        }

        /* synthetic */ Vertrag(Issue1146 issue1146, Vertrag vertrag) {
            this();
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/Issue1146$BpmnError.class */
    private class BpmnError extends Exception {
        public BpmnError(String msg) {
            super(msg);
        }
    }
}
