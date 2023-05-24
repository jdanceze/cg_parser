package org.mockito.internal.debugging;

import java.util.Collection;
import java.util.LinkedList;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/debugging/InvocationsPrinter.class */
public class InvocationsPrinter {
    public String printInvocations(Object mock) {
        Collection<Invocation> invocations = Mockito.mockingDetails(mock).getInvocations();
        Collection<Stubbing> stubbings = Mockito.mockingDetails(mock).getStubbings();
        if (invocations.isEmpty() && stubbings.isEmpty()) {
            return "No interactions and stubbings found for mock: " + mock;
        }
        StringBuilder sb = new StringBuilder();
        int x = 1;
        for (Invocation i : invocations) {
            if (x == 1) {
                sb.append("[Mockito] Interactions of: ").append(mock).append("\n");
            }
            int i2 = x;
            x++;
            sb.append(Instruction.argsep).append(i2).append(". ").append(i.toString()).append("\n");
            sb.append("  ").append(i.getLocation()).append("\n");
            if (i.stubInfo() != null) {
                sb.append("   - stubbed ").append(i.stubInfo().stubbedAt()).append("\n");
            }
        }
        LinkedList<Stubbing> unused = ListUtil.filter(stubbings, new ListUtil.Filter<Stubbing>() { // from class: org.mockito.internal.debugging.InvocationsPrinter.1
            @Override // org.mockito.internal.util.collections.ListUtil.Filter
            public boolean isOut(Stubbing s) {
                return s.wasUsed();
            }
        });
        if (unused.isEmpty()) {
            return sb.toString();
        }
        sb.append("[Mockito] Unused stubbings of: ").append(mock).append("\n");
        int x2 = 1;
        for (Stubbing s : stubbings) {
            int i3 = x2;
            x2++;
            sb.append(Instruction.argsep).append(i3).append(". ").append(s.getInvocation()).append("\n");
            sb.append("  - stubbed ").append(s.getInvocation().getLocation()).append("\n");
        }
        return sb.toString();
    }
}
