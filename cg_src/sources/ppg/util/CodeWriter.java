package ppg.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/util/CodeWriter.class */
public class CodeWriter {
    Block input;
    Block current;
    Writer output;
    int width;

    public CodeWriter(OutputStream o, int width_) {
        this.output = new OutputStreamWriter(o);
        this.width = width_;
        Block block = new Block(null, 0);
        this.input = block;
        this.current = block;
    }

    public CodeWriter(Writer w, int width_) {
        this.output = w;
        this.width = width_;
        Block block = new Block(null, 0);
        this.input = block;
        this.current = block;
    }

    public void write(String s) {
        if (s.length() > 0) {
            this.current.add(new StringItem(s));
        }
    }

    public void newline() {
        newline(0);
    }

    public void begin(int n) {
        Block b = new Block(this.current, n);
        this.current.add(b);
        this.current = b;
    }

    public void end() {
        this.current = this.current.parent;
        if (this.current == null) {
            throw new RuntimeException();
        }
    }

    public void allowBreak(int n) {
        this.current.add(new AllowBreak(n, Instruction.argsep));
    }

    public void allowBreak(int n, String alt) {
        this.current.add(new AllowBreak(n, alt));
    }

    public void newline(int n) {
        this.current.add(new Newline(n));
    }

    public boolean flush() throws IOException {
        boolean success = true;
        try {
            Item.format(this.input, 0, 0, this.width, this.width, true, true);
        } catch (Overrun e) {
            success = false;
        }
        this.input.sendOutput(this.output, 0, 0);
        this.output.flush();
        this.input.free();
        Block block = new Block(null, 0);
        this.input = block;
        this.current = block;
        return success;
    }
}
