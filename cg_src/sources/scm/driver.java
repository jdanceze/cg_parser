package scm;

import jas.RuntimeConstants;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Hashtable;
import org.apache.commons.cli.HelpFormatter;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/driver.class */
class driver implements RuntimeConstants {
    static Hashtable syms;

    driver() {
    }

    public static void main(String[] argv) throws Exception {
        StreamTokenizer inp;
        if (argv.length == 0) {
            inp = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        } else {
            inp = new StreamTokenizer(new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(argv[0])))));
        }
        inp.resetSyntax();
        inp.wordChars(97, 122);
        inp.wordChars(65, 90);
        inp.wordChars(33, 33);
        inp.wordChars(63, 63);
        inp.wordChars(95, 95);
        inp.wordChars(160, 255);
        inp.whitespaceChars(0, 32);
        inp.quoteChar(34);
        inp.commentChar(59);
        inp.parseNumbers();
        inp.eolIsSignificant(false);
        Env global = new Env();
        global.definevar(Symbol.intern("define"), new Define());
        global.definevar(Symbol.intern("set!"), new Setvar());
        global.definevar(Symbol.intern("lambda"), new Lambda());
        global.definevar(Symbol.intern("quote"), new Quote());
        global.definevar(Symbol.intern("car"), new Car());
        global.definevar(Symbol.intern("cdr"), new Cdr());
        global.definevar(Symbol.intern("cons"), new Cons());
        global.definevar(Symbol.intern("cond"), new Cond());
        global.definevar(Symbol.intern("num?"), new NumP());
        global.definevar(Symbol.intern("string?"), new StringP());
        global.definevar(Symbol.intern("progn"), new Progn());
        global.definevar(Symbol.intern("mapcar"), new Mapcar());
        global.definevar(Symbol.intern("+"), new Plus());
        global.definevar(Symbol.intern(HelpFormatter.DEFAULT_OPT_PREFIX), new Minus());
        global.definevar(Symbol.intern("*"), new Mult());
        global.definevar(Symbol.intern("/"), new Div());
        global.definevar(Symbol.intern("|"), new Or());
        global.definevar(Symbol.intern("<"), new LessP());
        global.definevar(Symbol.intern(">"), new MoreP());
        global.definevar(Symbol.intern("eq?"), new EqP());
        global.definevar(Symbol.intern("acc-public"), new Selfrep(1.0d));
        global.definevar(Symbol.intern("acc-private"), new Selfrep(2.0d));
        global.definevar(Symbol.intern("acc-protected"), new Selfrep(4.0d));
        global.definevar(Symbol.intern("acc-static"), new Selfrep(8.0d));
        global.definevar(Symbol.intern("acc-final"), new Selfrep(16.0d));
        global.definevar(Symbol.intern("acc-synchronized"), new Selfrep(32.0d));
        global.definevar(Symbol.intern("acc-volatile"), new Selfrep(64.0d));
        global.definevar(Symbol.intern("acc-transient"), new Selfrep(128.0d));
        global.definevar(Symbol.intern("acc-native"), new Selfrep(256.0d));
        global.definevar(Symbol.intern("acc-interface"), new Selfrep(512.0d));
        global.definevar(Symbol.intern("acc-abstract"), new Selfrep(1024.0d));
        global.definevar(Symbol.intern("acc-strictfp"), new Selfrep(2048.0d));
        AutoInit.fillit(global);
        int opc_cnt = opcNames.length;
        for (int i = 0; i < opc_cnt; i++) {
            switch (i) {
                case 132:
                case 170:
                case 185:
                case 197:
                    break;
                default:
                    global.definevar(Symbol.intern(opcNames[i]), new InsnProcedure(i));
                    break;
            }
        }
        do {
            inp.nextToken();
            Obj c = readinp(inp);
            if (c != null) {
                c.eval(global);
            }
        } while (inp.ttype != -1);
    }

    static Obj readinp(StreamTokenizer inp) throws IOException {
        switch (inp.ttype) {
            case 40:
                return readparen(inp);
            case 41:
                throw new SchemeError("Unexpected close paren");
            default:
                return readtok(inp);
        }
    }

    static Cell readparen(StreamTokenizer inp) throws IOException {
        inp.nextToken();
        if (inp.ttype == 41) {
            return null;
        }
        return new Cell(readinp(inp), readparen(inp));
    }

    static Obj readtok(StreamTokenizer inp) {
        Obj ret;
        if (inp.ttype == -2) {
            return new Selfrep(inp.nval);
        }
        if (inp.ttype == -3) {
            return Symbol.intern(inp.sval);
        }
        if (inp.ttype == -1) {
            return null;
        }
        switch (inp.ttype) {
            case 34:
                ret = new Selfrep(inp.sval);
                break;
            case 42:
            case 43:
            case 45:
            case 47:
            case 60:
            case 62:
            case 124:
                ret = Symbol.intern(String.valueOf((char) inp.ttype));
                break;
            default:
                throw new SchemeError("Unexpected parse error");
        }
        return ret;
    }
}
