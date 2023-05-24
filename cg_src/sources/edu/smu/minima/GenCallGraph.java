package edu.smu.minima;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.toolkits.callgraph.Edge;
/* loaded from: gencallgraphv3.jar:edu/smu/minima/GenCallGraph.class */
public class GenCallGraph {
    public static String APKfile;
    public static String AndroSDK = "/home/fthung/Android/Sdk/platforms";
    public static String outFile = "";

    public static void main(String[] args) throws ParseException, IOException {
        long start = System.currentTimeMillis();
        Options options = new CLI().getOptions();
        HelpFormatter formatter = new HelpFormatter();
        if (args.length == 0) {
            formatter.printHelp("GenCallGraph", options);
            System.exit(-1);
        }
        CommandLineParser parser = new GnuParser();
        CommandLine line = parser.parse(options, args);
        if (line.hasOption("a")) {
            APKfile = line.getOptionValue("a");
        } else {
            formatter.printHelp("GenCallGraph", options);
            System.exit(-1);
        }
        if (line.hasOption("s")) {
            AndroSDK = line.getOptionValue("s");
        } else {
            formatter.printHelp("GenCallGraph", options);
            System.exit(-1);
        }
        SetupApplication analyzer = new SetupApplication(AndroSDK, APKfile);
        analyzer.getConfig().setMergeDexFiles(true);
        analyzer.getConfig().setCallgraphAlgorithm(InfoflowConfiguration.CallgraphAlgorithm.SPARK);
        analyzer.constructCallgraph();
        long end = System.currentTimeMillis();
        float sec = ((float) (end - start)) / 1000.0f;
        System.out.println("Total time to construct call graph: " + sec + " seconds");
        if (line.hasOption("o")) {
            outFile = line.getOptionValue("o");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
            Iterator<Edge> edgeIt = Scene.v().getCallGraph().iterator();
            while (edgeIt.hasNext()) {
                Edge edge = edgeIt.next();
                SootMethod smSrc = edge.src();
                Unit uSrc = edge.srcStmt();
                SootMethod smDest = edge.tgt();
                writer.write("#EDGE_FROM#" + uSrc + "#IN#" + smSrc + "#TO#" + smDest + "\n");
            }
            writer.close();
            System.out.println("The call graph is saved in " + outFile);
        }
    }
}
