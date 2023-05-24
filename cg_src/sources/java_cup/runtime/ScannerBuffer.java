package java_cup.runtime;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/ScannerBuffer.class */
public class ScannerBuffer implements Scanner {
    private Scanner inner;
    private List<Symbol> buffer = new LinkedList();

    public ScannerBuffer(Scanner inner) {
        this.inner = inner;
    }

    public List<Symbol> getBuffered() {
        return Collections.unmodifiableList(this.buffer);
    }

    @Override // java_cup.runtime.Scanner
    public Symbol next_token() throws Exception {
        Symbol buffered = this.inner.next_token();
        this.buffer.add(buffered);
        return buffered;
    }
}
