package beaver;

import beaver.Scanner;
import java.io.IOException;
/* loaded from: gencallgraphv3.jar:beaver/Parser.class */
public abstract class Parser {
    protected final ParsingTables tables;
    protected final short accept_action_id;
    protected short[] states = new short[256];
    protected int top;
    protected Symbol[] _symbols;
    protected Events report;

    protected abstract Symbol invokeReduceAction(int i, int i2);

    /* loaded from: gencallgraphv3.jar:beaver/Parser$Exception.class */
    public static class Exception extends java.lang.Exception {
        Exception(String msg) {
            super(msg);
        }
    }

    /* loaded from: gencallgraphv3.jar:beaver/Parser$Events.class */
    public static class Events {
        public void scannerError(Scanner.Exception e) {
            System.err.print("Scanner Error:");
            if (e.line > 0) {
                System.err.print(e.line);
                System.err.print(',');
                System.err.print(e.column);
                System.err.print(':');
            }
            System.err.print(' ');
            System.err.println(e.getMessage());
        }

        public void syntaxError(Symbol token) {
            System.err.print(':');
            System.err.print(Symbol.getLine(token.start));
            System.err.print(',');
            System.err.print(Symbol.getColumn(token.start));
            System.err.print('-');
            System.err.print(Symbol.getLine(token.end));
            System.err.print(',');
            System.err.print(Symbol.getColumn(token.end));
            System.err.print(": Syntax Error: unexpected token ");
            if (token.value != null) {
                System.err.print('\"');
                System.err.print(token.value);
                System.err.println('\"');
                return;
            }
            System.err.print('#');
            System.err.println((int) token.id);
        }

        public void unexpectedTokenRemoved(Symbol token) {
            System.err.print(':');
            System.err.print(Symbol.getLine(token.start));
            System.err.print(',');
            System.err.print(Symbol.getColumn(token.start));
            System.err.print('-');
            System.err.print(Symbol.getLine(token.end));
            System.err.print(',');
            System.err.print(Symbol.getColumn(token.end));
            System.err.print(": Recovered: removed unexpected token ");
            if (token.value != null) {
                System.err.print('\"');
                System.err.print(token.value);
                System.err.println('\"');
                return;
            }
            System.err.print('#');
            System.err.println((int) token.id);
        }

        public void missingTokenInserted(Symbol token) {
            System.err.print(':');
            System.err.print(Symbol.getLine(token.start));
            System.err.print(',');
            System.err.print(Symbol.getColumn(token.start));
            System.err.print('-');
            System.err.print(Symbol.getLine(token.end));
            System.err.print(',');
            System.err.print(Symbol.getColumn(token.end));
            System.err.print(": Recovered: inserted missing token ");
            if (token.value != null) {
                System.err.print('\"');
                System.err.print(token.value);
                System.err.println('\"');
                return;
            }
            System.err.print('#');
            System.err.println((int) token.id);
        }

        public void misspelledTokenReplaced(Symbol token) {
            System.err.print(':');
            System.err.print(Symbol.getLine(token.start));
            System.err.print(',');
            System.err.print(Symbol.getColumn(token.start));
            System.err.print('-');
            System.err.print(Symbol.getLine(token.end));
            System.err.print(',');
            System.err.print(Symbol.getColumn(token.end));
            System.err.print(": Recovered: replaced unexpected token with ");
            if (token.value != null) {
                System.err.print('\"');
                System.err.print(token.value);
                System.err.println('\"');
                return;
            }
            System.err.print('#');
            System.err.println((int) token.id);
        }

        public void errorPhraseRemoved(Symbol error) {
            System.err.print(':');
            System.err.print(Symbol.getLine(error.start));
            System.err.print(',');
            System.err.print(Symbol.getColumn(error.start));
            System.err.print('-');
            System.err.print(Symbol.getLine(error.end));
            System.err.print(',');
            System.err.print(Symbol.getColumn(error.end));
            System.err.println(": Recovered: removed error phrase");
        }
    }

    /* loaded from: gencallgraphv3.jar:beaver/Parser$TokenStream.class */
    public class TokenStream {
        private Scanner scanner;
        private Symbol[] buffer;
        private int n_marked;
        private int n_read;
        private int n_written;

        public TokenStream(Scanner scanner) {
            this.scanner = scanner;
        }

        public TokenStream(Parser parser, Scanner scanner, Symbol first_symbol) {
            this(scanner);
            alloc(1);
            this.buffer[0] = first_symbol;
            this.n_written++;
        }

        public Symbol nextToken() throws IOException {
            if (this.buffer != null) {
                if (this.n_read < this.n_written) {
                    Symbol[] symbolArr = this.buffer;
                    int i = this.n_read;
                    this.n_read = i + 1;
                    return symbolArr[i];
                } else if (this.n_written < this.n_marked) {
                    this.n_read++;
                    Symbol[] symbolArr2 = this.buffer;
                    int i2 = this.n_written;
                    this.n_written = i2 + 1;
                    Symbol readToken = readToken();
                    symbolArr2[i2] = readToken;
                    return readToken;
                } else {
                    this.buffer = null;
                }
            }
            return readToken();
        }

        public void alloc(int size) {
            this.n_marked = size;
            this.buffer = new Symbol[size + 1];
            this.n_written = 0;
            this.n_read = 0;
        }

        public void rewind() {
            this.n_read = 0;
        }

        public void insert(Symbol t0, Symbol t1) {
            if (this.buffer.length - this.n_written < 2) {
                throw new IllegalStateException("not enough space in the buffer");
            }
            System.arraycopy(this.buffer, 0, this.buffer, 2, this.n_written);
            this.buffer[0] = t0;
            this.buffer[1] = t1;
            this.n_written += 2;
        }

        public Symbol remove(int i) {
            Symbol token = this.buffer[i];
            int last = this.n_written - 1;
            while (i < last) {
                int i2 = i;
                i++;
                this.buffer[i2] = this.buffer[i];
            }
            this.n_written = last;
            return token;
        }

        boolean isFull() {
            return this.n_read == this.n_marked;
        }

        private Symbol readToken() throws IOException {
            while (true) {
                try {
                    return this.scanner.nextToken();
                } catch (Scanner.Exception e) {
                    Parser.this.report.scannerError(e);
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:beaver/Parser$Simulator.class */
    public class Simulator {
        private short[] states;
        private int top;
        private int min_top;

        public Simulator() {
        }

        public boolean parse(TokenStream in) throws IOException {
            initStack();
            do {
                Symbol token = in.nextToken();
                while (true) {
                    short act = Parser.this.tables.findParserAction(this.states[this.top], token.id);
                    if (act > 0) {
                        shift(act);
                    } else if (act == Parser.this.accept_action_id) {
                        return true;
                    } else {
                        if (act < 0) {
                            short nt_id = reduce(act ^ (-1));
                            short act2 = Parser.this.tables.findNextState(this.states[this.top], nt_id);
                            if (act2 <= 0) {
                                return act2 == Parser.this.accept_action_id;
                            }
                            shift(act2);
                        } else {
                            return false;
                        }
                    }
                }
            } while (!in.isFull());
            return true;
        }

        private void initStack() throws IOException {
            if (this.states == null || this.states.length < Parser.this.states.length) {
                this.states = new short[Parser.this.states.length];
                this.min_top = 0;
            }
            short[] sArr = Parser.this.states;
            int i = this.min_top;
            short[] sArr2 = this.states;
            int i2 = this.min_top;
            int i3 = Parser.this.top;
            this.top = i3;
            System.arraycopy(sArr, i, sArr2, i2, i3 + 1);
        }

        private void increaseStackCapacity() {
            short[] new_states = new short[this.states.length * 2];
            System.arraycopy(this.states, 0, new_states, 0, this.states.length);
            this.states = new_states;
        }

        private void shift(short state) {
            int i = this.top + 1;
            this.top = i;
            if (i == this.states.length) {
                increaseStackCapacity();
            }
            this.states[this.top] = state;
        }

        private short reduce(int rule_id) {
            int rule_info = Parser.this.tables.rule_infos[rule_id];
            int rhs_size = rule_info & 65535;
            this.top -= rhs_size;
            this.min_top = Math.min(this.min_top, this.top);
            return (short) (rule_info >>> 16);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Parser(ParsingTables tables) {
        this.tables = tables;
        this.accept_action_id = (short) (tables.rule_infos.length ^ (-1));
    }

    public Object parse(Scanner source) throws IOException, Exception {
        init();
        return parse(new TokenStream(source));
    }

    public Object parse(Scanner source, short alt_goal_marker_id) throws IOException, Exception {
        init();
        TokenStream in = new TokenStream(this, source, new Symbol(alt_goal_marker_id));
        return parse(in);
    }

    private Object parse(TokenStream in) throws IOException, Exception {
        while (true) {
            Symbol token = in.nextToken();
            while (true) {
                short act = this.tables.findParserAction(this.states[this.top], token.id);
                if (act > 0) {
                    shift(token, act);
                    break;
                } else if (act == this.accept_action_id) {
                    Symbol goal = this._symbols[this.top];
                    this._symbols = null;
                    return goal.value;
                } else if (act < 0) {
                    Symbol nt = reduce(act ^ (-1));
                    short act2 = this.tables.findNextState(this.states[this.top], nt.id);
                    if (act2 > 0) {
                        shift(nt, act2);
                    } else if (act2 == this.accept_action_id) {
                        this._symbols = null;
                        return nt.value;
                    } else {
                        throw new IllegalStateException("Cannot shift a nonterminal");
                    }
                } else {
                    this.report.syntaxError(token);
                    recoverFromError(token, in);
                    break;
                }
            }
        }
    }

    private void init() {
        if (this.report == null) {
            this.report = new Events();
        }
        this._symbols = new Symbol[this.states.length];
        this.top = 0;
        this._symbols[this.top] = new Symbol("none");
        this.states[this.top] = 1;
    }

    private void increaseStackCapacity() {
        short[] new_states = new short[this.states.length * 2];
        System.arraycopy(this.states, 0, new_states, 0, this.states.length);
        this.states = new_states;
        Symbol[] new_stack = new Symbol[this.states.length];
        System.arraycopy(this._symbols, 0, new_stack, 0, this._symbols.length);
        this._symbols = new_stack;
    }

    private void shift(Symbol sym, short goto_state) {
        int i = this.top + 1;
        this.top = i;
        if (i == this.states.length) {
            increaseStackCapacity();
        }
        this._symbols[this.top] = sym;
        this.states[this.top] = goto_state;
    }

    private Symbol reduce(int rule_id) {
        int rule_info = this.tables.rule_infos[rule_id];
        int rhs_size = rule_info & 65535;
        this.top -= rhs_size;
        Symbol lhs_sym = invokeReduceAction(rule_id, this.top);
        lhs_sym.id = (short) (rule_info >>> 16);
        if (rhs_size == 0) {
            int i = this._symbols[this.top].end;
            lhs_sym.end = i;
            lhs_sym.start = i;
        } else {
            lhs_sym.start = this._symbols[this.top + 1].start;
            lhs_sym.end = this._symbols[this.top + rhs_size].end;
        }
        return lhs_sym;
    }

    protected void recoverFromError(Symbol token, TokenStream in) throws IOException, Exception {
        int i;
        int index;
        int index2;
        if (token.id == 0) {
            throw new Exception("Cannot recover from the syntax error");
        }
        Simulator sim = new Simulator();
        in.alloc(3);
        short current_state = this.states[this.top];
        if (!this.tables.compressed) {
            short first_term_id = this.tables.findFirstTerminal(current_state);
            if (first_term_id >= 0) {
                Symbol term = new Symbol(first_term_id, this._symbols[this.top].end, token.start);
                in.insert(term, token);
                in.rewind();
                if (sim.parse(in)) {
                    in.rewind();
                    this.report.missingTokenInserted(term);
                    return;
                }
                int offset = this.tables.actn_offsets[current_state];
                short s = first_term_id;
                while (true) {
                    short term_id = (short) (s + 1);
                    if (term_id < this.tables.n_term && (index2 = offset + term_id) < this.tables.lookaheads.length) {
                        if (this.tables.lookaheads[index2] == term_id) {
                            term.id = term_id;
                            in.rewind();
                            if (sim.parse(in)) {
                                in.rewind();
                                this.report.missingTokenInserted(term);
                                return;
                            }
                        }
                        s = term_id;
                    }
                }
                in.remove(1);
                term.start = token.start;
                term.end = token.end;
                short s2 = first_term_id;
                while (true) {
                    short term_id2 = s2;
                    if (term_id2 < this.tables.n_term && (index = offset + term_id2) < this.tables.lookaheads.length) {
                        if (this.tables.lookaheads[index] == term_id2) {
                            term.id = term_id2;
                            in.rewind();
                            if (sim.parse(in)) {
                                in.rewind();
                                this.report.misspelledTokenReplaced(term);
                                return;
                            }
                        }
                        s2 = (short) (term_id2 + 1);
                    }
                }
                in.remove(0);
            }
        }
        if (sim.parse(in)) {
            in.rewind();
            this.report.unexpectedTokenRemoved(token);
            return;
        }
        Symbol first_sym = token;
        Symbol last_sym = token;
        do {
            short goto_state = this.tables.findNextState(this.states[this.top], this.tables.error_symbol_id);
            if (goto_state <= 0) {
                first_sym = this._symbols[this.top];
                i = this.top - 1;
                this.top = i;
            } else {
                Symbol error = new Symbol(this.tables.error_symbol_id, first_sym.start, last_sym.end);
                shift(error, goto_state);
                in.rewind();
                while (!sim.parse(in)) {
                    last_sym = in.remove(0);
                    if (last_sym.id == 0) {
                        throw new Exception("Cannot recover from the syntax error");
                    }
                    in.rewind();
                }
                error.end = last_sym.end;
                in.rewind();
                this.report.errorPhraseRemoved(error);
                return;
            }
        } while (i >= 0);
        throw new Exception("Cannot recover from the syntax error");
    }
}
