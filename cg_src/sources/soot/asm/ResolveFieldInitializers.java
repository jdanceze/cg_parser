package soot.asm;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Supplier;
/* loaded from: gencallgraphv3.jar:soot/asm/ResolveFieldInitializers.class */
public class ResolveFieldInitializers {
    private Supplier<Deque> refSupplier = ArrayDeque::new;
    private Deque plainOld = new LinkedList();
}
