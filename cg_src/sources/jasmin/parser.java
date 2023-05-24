package jasmin;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Stack;
import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;
import java_cup.runtime.lr_parser;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jasmin/parser.class */
public class parser extends lr_parser {
    protected static final short[][] _production_table = getFromFile("tables.out/shortarray_0.obj");
    protected static final short[][] _action_table = getFromFile("tables.out/shortarray_1.obj");
    protected static final short[][] _reduce_table = getFromFile("tables.out/shortarray_2.obj");
    protected parser$CUP$parser$actions action_obj;
    public Scanner scanner;
    public ClassFile classFile;

    @Override // java_cup.runtime.lr_parser
    public final Class getSymbolContainer() {
        return sym.class;
    }

    @Deprecated
    public parser() {
    }

    @Deprecated
    public parser(java_cup.runtime.Scanner s) {
        super(s);
    }

    public parser(java_cup.runtime.Scanner s, SymbolFactory sf) {
        super(s, sf);
    }

    @Override // java_cup.runtime.lr_parser
    public short[][] production_table() {
        return _production_table;
    }

    public static short[][] getFromFile(String filename) {
        try {
            ClassLoader cl = java_cup.parser.class.getClassLoader();
            InputStream is = cl.getResourceAsStream(filename);
            ObjectInputStream ois = new ObjectInputStream(is);
            short[][] sa2 = (short[][]) ois.readObject();
            return sa2;
        } catch (Throwable t) {
            throw new RuntimeException("oups: " + t);
        }
    }

    @Override // java_cup.runtime.lr_parser
    public short[][] action_table() {
        return _action_table;
    }

    @Override // java_cup.runtime.lr_parser
    public short[][] reduce_table() {
        return _reduce_table;
    }

    @Override // java_cup.runtime.lr_parser
    protected void init_actions() {
        this.action_obj = new parser$CUP$parser$actions(this, this);
    }

    @Override // java_cup.runtime.lr_parser
    public Symbol do_action(int act_num, lr_parser parser, Stack stack, int top) throws Exception {
        return this.action_obj.CUP$parser$do_action(act_num, parser, stack, top);
    }

    @Override // java_cup.runtime.lr_parser
    public int start_state() {
        return 0;
    }

    @Override // java_cup.runtime.lr_parser
    public int start_production() {
        return 1;
    }

    @Override // java_cup.runtime.lr_parser
    public int EOF_sym() {
        return 0;
    }

    @Override // java_cup.runtime.lr_parser
    public int error_sym() {
        return 1;
    }

    @Override // java_cup.runtime.lr_parser
    public void user_init() throws Exception {
        this.action_obj.scanner = this.scanner;
        this.action_obj.classFile = this.classFile;
    }

    @Override // java_cup.runtime.lr_parser
    public Symbol scan() throws Exception {
        return this.scanner.next_token();
    }

    @Override // java_cup.runtime.lr_parser
    public void report_error(String message, Object info) {
        this.classFile.report_error("Warning - " + message);
    }

    @Override // java_cup.runtime.lr_parser
    public void report_fatal_error(String message, Object info) {
        this.classFile.report_error("Error - " + message);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public parser(ClassFile classFile, Scanner scanner) {
        this.scanner = scanner;
        this.classFile = classFile;
    }
}
