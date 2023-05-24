package org.mockito.internal.exceptions.util;

import java.util.List;
import org.mockito.internal.exceptions.VerificationAwareInvocation;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/exceptions/util/ScenarioPrinter.class */
public class ScenarioPrinter {
    public String print(List<VerificationAwareInvocation> invocations) {
        if (invocations.size() == 1) {
            return "Actually, above is the only interaction with this mock.";
        }
        StringBuilder sb = new StringBuilder("***\nFor your reference, here is the list of all invocations ([?] - means unverified).\n");
        int counter = 0;
        for (VerificationAwareInvocation i : invocations) {
            counter++;
            sb.append(counter).append(". ");
            if (!i.isVerified()) {
                sb.append("[?]");
            }
            sb.append(i.getLocation()).append("\n");
        }
        return sb.toString();
    }
}
