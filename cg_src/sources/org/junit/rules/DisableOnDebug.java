package org.junit.rules;

import java.util.List;
import org.junit.internal.management.ManagementFactory;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/DisableOnDebug.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/DisableOnDebug.class */
public class DisableOnDebug implements TestRule {
    private final TestRule rule;
    private final boolean debugging;

    public DisableOnDebug(TestRule rule) {
        this(rule, ManagementFactory.getRuntimeMXBean().getInputArguments());
    }

    DisableOnDebug(TestRule rule, List<String> inputArguments) {
        this.rule = rule;
        this.debugging = isDebugging(inputArguments);
    }

    @Override // org.junit.rules.TestRule
    public Statement apply(Statement base, Description description) {
        if (this.debugging) {
            return base;
        }
        return this.rule.apply(base, description);
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x0010  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static boolean isDebugging(java.util.List<java.lang.String> r3) {
        /*
            r0 = r3
            java.util.Iterator r0 = r0.iterator()
            r4 = r0
        L7:
            r0 = r4
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L31
            r0 = r4
            java.lang.Object r0 = r0.next()
            java.lang.String r0 = (java.lang.String) r0
            r5 = r0
            java.lang.String r0 = "-Xdebug"
            r1 = r5
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L2c
            r0 = r5
            java.lang.String r1 = "-agentlib:jdwp"
            boolean r0 = r0.startsWith(r1)
            if (r0 == 0) goto L2e
        L2c:
            r0 = 1
            return r0
        L2e:
            goto L7
        L31:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.junit.rules.DisableOnDebug.isDebugging(java.util.List):boolean");
    }

    public boolean isDebugging() {
        return this.debugging;
    }
}
