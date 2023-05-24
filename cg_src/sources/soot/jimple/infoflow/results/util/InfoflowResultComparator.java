package soot.jimple.infoflow.results.util;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import soot.jimple.infoflow.results.xml.InfoflowResultsReader;
import soot.jimple.infoflow.results.xml.SerializedInfoflowResults;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/util/InfoflowResultComparator.class */
public class InfoflowResultComparator {
    private static InfoflowResultComparator INSTANCE = null;

    public static InfoflowResultComparator v() {
        if (INSTANCE == null) {
            INSTANCE = new InfoflowResultComparator();
        }
        return INSTANCE;
    }

    public boolean resultEquals(String file1, String file2) throws XMLStreamException, IOException {
        InfoflowResultsReader rdr = new InfoflowResultsReader();
        SerializedInfoflowResults results1 = rdr.readResults(file1);
        SerializedInfoflowResults results2 = rdr.readResults(file2);
        return results1.equals(results2);
    }

    public static void main(String[] args) throws XMLStreamException, IOException {
        if (args.length != 2) {
            System.err.println("Usage: InfoflowResultComparator <file1> <file2>");
        } else if (v().resultEquals(args[0], args[1])) {
            System.out.println("Files are equal");
        } else {
            System.out.println("Files are NOT equal");
        }
    }
}
