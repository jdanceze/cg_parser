package org.mockito.internal.debugging;

import java.util.Arrays;
import java.util.List;
import org.mockito.MockitoDebugger;
import org.mockito.internal.invocation.UnusedStubsFinder;
import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.invocation.Invocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/debugging/MockitoDebuggerImpl.class */
public class MockitoDebuggerImpl implements MockitoDebugger {
    private final UnusedStubsFinder unusedStubsFinder = new UnusedStubsFinder();

    @Override // org.mockito.MockitoDebugger
    @Deprecated
    public String printInvocations(Object... mocks) {
        String out = "" + line("********************************");
        String out2 = (out + line("*** Mockito interactions log ***")) + line("********************************");
        for (Invocation i : AllInvocationsFinder.find(Arrays.asList(mocks))) {
            out2 = (out2 + line(i.toString())) + line(" invoked: " + i.getLocation());
            if (i.stubInfo() != null) {
                out2 = out2 + line(" stubbed: " + i.stubInfo().stubbedAt().toString());
            }
        }
        List<Invocation> invocations = this.unusedStubsFinder.find(Arrays.asList(mocks));
        if (invocations.isEmpty()) {
            return print(out2);
        }
        String out3 = ((out2 + line("********************************")) + line("***       Unused stubs       ***")) + line("********************************");
        for (Invocation i2 : invocations) {
            out3 = (out3 + line(i2.toString())) + line(" stubbed: " + i2.getLocation());
        }
        return print(out3);
    }

    private String line(String text) {
        return text + "\n";
    }

    private String print(String out) {
        System.out.println(out);
        return out;
    }
}
