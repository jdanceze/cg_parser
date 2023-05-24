package ppg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import ppg.lex.Lexer;
import ppg.parse.Parser;
import ppg.spec.CUPSpec;
import ppg.spec.Spec;
import ppg.util.CodeWriter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/PPG.class */
public class PPG {
    public static final String HEADER = "ppg: ";
    public static final String DEBUG_HEADER = "ppg [debug]: ";
    public static boolean debug = false;
    public static String SYMBOL_CLASS_NAME = "sym";
    public static String OUTPUT_FILE = null;

    public static void DEBUG(String s) {
        if (debug) {
            System.out.println(new StringBuffer().append(DEBUG_HEADER).append(s).toString());
        }
    }

    public static void main(String[] args) {
        String filename = null;
        int i = 0;
        while (i < args.length) {
            try {
                if (args[i].charAt(0) == '-') {
                    if (args[i].equals("-symbols")) {
                        if (args.length > i) {
                            i++;
                            SYMBOL_CLASS_NAME = args[i];
                        } else {
                            throw new Exception("No filename specified after -symbols");
                        }
                    } else if (args[i].equals("-o")) {
                        if (args.length > i) {
                            i++;
                            OUTPUT_FILE = args[i];
                        } else {
                            throw new Exception("No filename specified after -o");
                        }
                    } else {
                        throw new Exception(new StringBuffer().append("Invalid switch: ").append(args[i]).toString());
                    }
                } else if (filename == null) {
                    filename = args[i];
                } else {
                    throw new Exception("Error: multiple source files specified.");
                }
                i++;
            } catch (Exception e) {
                System.err.println(new StringBuffer().append(HEADER).append(e.getMessage()).toString());
                usage();
            }
        }
        if (filename == null) {
            System.err.println("Error: no filename specified.");
            usage();
        }
        try {
            FileInputStream fileInput = new FileInputStream(filename);
            Lexer lex = new Lexer(fileInput, filename);
            Parser parser = new Parser(filename, lex);
            try {
                parser.parse();
                Spec spec = (Spec) Parser.getProgramNode();
                File file = new File(filename);
                String parent = file.getParent();
                spec.parseChain(parent == null ? "" : parent);
                PrintStream out = System.out;
                try {
                    if (OUTPUT_FILE != null) {
                        out = new PrintStream(new FileOutputStream(OUTPUT_FILE));
                    }
                    CUPSpec combined = spec.coalesce();
                    CodeWriter cw = new CodeWriter(out, 72);
                    combined.unparse(cw);
                    cw.flush();
                } catch (IOException e2) {
                    System.out.println(new StringBuffer().append("ppg: exception: ").append(e2.getMessage()).toString());
                    System.exit(1);
                } catch (PPGError e3) {
                    System.out.println(e3.getMessage());
                    System.exit(1);
                }
            } catch (Exception e4) {
                System.out.println(new StringBuffer().append("ppg: Exception: ").append(e4.getMessage()).toString());
            }
        } catch (FileNotFoundException e5) {
            System.out.println(new StringBuffer().append("Error: ").append(filename).append(" is not found.").toString());
        } catch (ArrayIndexOutOfBoundsException e6) {
            System.out.println("ppg: Error: No file name given.");
        }
    }

    public static void usage() {
        System.err.println("Usage: ppg [-symbols ConstClass] <input file>\nwhere:\n\t-c <Class>\tclass prepended to token names to pass to <func>\n\t<input>\ta PPG or CUP source file\n");
        System.exit(1);
    }
}
