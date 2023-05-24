package ppg.lex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/lex/LexTest.class */
public class LexTest {
    private static final String HEADER = "ppg [lexertest]: ";

    public static void main(String[] args) {
        String filename = null;
        try {
            filename = args[0];
            FileInputStream fileInput = new FileInputStream(filename);
            File f = new File(filename);
            String simpleName = f.getName();
            Lexer lex = new Lexer(fileInput, simpleName);
            while (true) {
                try {
                    Token t = lex.getToken();
                    t.unparse(System.out);
                    if (t.getCode() != 0) {
                        System.out.println();
                    } else {
                        fileInput.close();
                        return;
                    }
                } catch (Error e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                    return;
                } catch (Exception e2) {
                    System.out.println(e2.getMessage());
                    System.exit(1);
                    return;
                }
            }
        } catch (FileNotFoundException e3) {
            System.out.println(new StringBuffer().append("Error: ").append(filename).append(" is not found.").toString());
        } catch (ArrayIndexOutOfBoundsException e4) {
            System.out.println("ppg [lexertest]: Error: No file name given.");
        }
    }
}
