package java_cup.runtime;

import java.util.Stack;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/virtual_parse_stack.class */
public class virtual_parse_stack {
    protected Stack real_stack;
    protected int real_next;
    protected Stack vstack;

    public virtual_parse_stack(Stack shadowing_stack) throws Exception {
        if (shadowing_stack == null) {
            throw new Exception("Internal parser error: attempt to create null virtual stack");
        }
        this.real_stack = shadowing_stack;
        this.vstack = new Stack();
        this.real_next = 0;
        get_from_real();
    }

    protected void get_from_real() {
        if (this.real_next >= this.real_stack.size()) {
            return;
        }
        Symbol stack_sym = (Symbol) this.real_stack.elementAt((this.real_stack.size() - 1) - this.real_next);
        this.real_next++;
        this.vstack.push(new Integer(stack_sym.parse_state));
    }

    public boolean empty() {
        return this.vstack.empty();
    }

    public int top() throws Exception {
        if (this.vstack.empty()) {
            throw new Exception("Internal parser error: top() called on empty virtual stack");
        }
        return ((Integer) this.vstack.peek()).intValue();
    }

    public void pop() throws Exception {
        if (this.vstack.empty()) {
            throw new Exception("Internal parser error: pop from empty virtual stack");
        }
        this.vstack.pop();
        if (this.vstack.empty()) {
            get_from_real();
        }
    }

    public void push(int state_num) {
        this.vstack.push(new Integer(state_num));
    }
}
