package ppg.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import ppg.lex.Lexer;
import ppg.spec.Spec;
import ppg.util.CodeWriter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/parse/ParseTest.class */
public class ParseTest {
    private static final String HEADER = "ppg [parsetest]: ";

    private ParseTest() {
    }

    public static void main(String[] args) {
        String filename = null;
        try {
            filename = args[0];
            FileInputStream fileInput = new FileInputStream(filename);
            File f = new File(filename);
            String simpleName = f.getName();
            Lexer lex = new Lexer(fileInput, simpleName);
            Parser parser = new Parser(filename, lex);
            try {
                parser.parse();
                Spec spec = (Spec) Parser.getProgramNode();
                CodeWriter cw = new CodeWriter(System.out, 72);
                try {
                    spec.unparse(cw);
                    cw.flush();
                    fileInput.close();
                } catch (IOException e) {
                    System.out.println(new StringBuffer().append("ppg [parsetest]: exception: ").append(e.getMessage()).toString());
                }
            } catch (Exception e2) {
                System.out.println(new StringBuffer().append("ppg [parsetest]: Exception: ").append(e2.getMessage()).toString());
            }
        } catch (FileNotFoundException e3) {
            System.out.println(new StringBuffer().append("Error: ").append(filename).append(" is not found.").toString());
        } catch (ArrayIndexOutOfBoundsException e4) {
            System.out.println("ppg [parsetest]: Error: No file name given.");
        }
    }
}
