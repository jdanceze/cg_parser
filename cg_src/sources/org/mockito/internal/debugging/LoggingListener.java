package org.mockito.internal.debugging;

import java.util.LinkedList;
import java.util.List;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.util.StringUtil;
import org.mockito.invocation.Invocation;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/debugging/LoggingListener.class */
public class LoggingListener implements FindingsListener {
    private final boolean warnAboutUnstubbed;
    private final List<String> argMismatchStubs = new LinkedList();
    private final List<String> unusedStubs = new LinkedList();
    private final List<String> unstubbedCalls = new LinkedList();

    public LoggingListener(boolean warnAboutUnstubbed) {
        this.warnAboutUnstubbed = warnAboutUnstubbed;
    }

    @Override // org.mockito.internal.debugging.FindingsListener
    public void foundStubCalledWithDifferentArgs(Invocation unused, InvocationMatcher unstubbed) {
        String index = Integer.toString(indexOfNextPair(this.argMismatchStubs.size()));
        String padding = index.replaceAll("\\d", Instruction.argsep);
        this.argMismatchStubs.add(index + ". Stubbed " + unused.getLocation());
        this.argMismatchStubs.add(padding + "  Invoked " + unstubbed.getInvocation().getLocation());
    }

    static int indexOfNextPair(int collectionSize) {
        return (collectionSize / 2) + 1;
    }

    @Override // org.mockito.internal.debugging.FindingsListener
    public void foundUnusedStub(Invocation unused) {
        this.unusedStubs.add((this.unusedStubs.size() + 1) + ". " + unused.getLocation());
    }

    @Override // org.mockito.internal.debugging.FindingsListener
    public void foundUnstubbed(InvocationMatcher unstubbed) {
        if (this.warnAboutUnstubbed) {
            this.unstubbedCalls.add((this.unstubbedCalls.size() + 1) + ". " + unstubbed.getInvocation().getLocation());
        }
    }

    public String getStubbingInfo() {
        if (this.argMismatchStubs.isEmpty() && this.unusedStubs.isEmpty() && this.unstubbedCalls.isEmpty()) {
            return "";
        }
        List<String> lines = new LinkedList<>();
        lines.add("[Mockito] Additional stubbing information (see javadoc for StubbingInfo class):");
        if (!this.argMismatchStubs.isEmpty()) {
            lines.add("[Mockito]");
            lines.add("[Mockito] Argument mismatch between stubbing and actual invocation (is stubbing correct in the test?):");
            lines.add("[Mockito]");
            addOrderedList(lines, this.argMismatchStubs);
        }
        if (!this.unusedStubs.isEmpty()) {
            lines.add("[Mockito]");
            lines.add("[Mockito] Unused stubbing (perhaps can be removed from the test?):");
            lines.add("[Mockito]");
            addOrderedList(lines, this.unusedStubs);
        }
        if (!this.unstubbedCalls.isEmpty()) {
            lines.add("[Mockito]");
            lines.add("[Mockito] Unstubbed method invocations (perhaps missing stubbing in the test?):");
            lines.add("[Mockito]");
            addOrderedList(lines, this.unstubbedCalls);
        }
        return StringUtil.join("", lines);
    }

    private void addOrderedList(List<String> target, List<String> additions) {
        for (String a : additions) {
            target.add("[Mockito] " + a);
        }
    }
}
