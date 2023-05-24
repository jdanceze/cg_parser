package org.junit.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/RuleChain.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/RuleChain.class */
public class RuleChain implements TestRule {
    private static final RuleChain EMPTY_CHAIN = new RuleChain(Collections.emptyList());
    private List<TestRule> rulesStartingWithInnerMost;

    public static RuleChain emptyRuleChain() {
        return EMPTY_CHAIN;
    }

    public static RuleChain outerRule(TestRule outerRule) {
        return emptyRuleChain().around(outerRule);
    }

    private RuleChain(List<TestRule> rules) {
        this.rulesStartingWithInnerMost = rules;
    }

    public RuleChain around(TestRule enclosedRule) {
        if (enclosedRule == null) {
            throw new NullPointerException("The enclosed rule must not be null");
        }
        List<TestRule> rulesOfNewChain = new ArrayList<>();
        rulesOfNewChain.add(enclosedRule);
        rulesOfNewChain.addAll(this.rulesStartingWithInnerMost);
        return new RuleChain(rulesOfNewChain);
    }

    @Override // org.junit.rules.TestRule
    public Statement apply(Statement base, Description description) {
        return new RunRules(base, this.rulesStartingWithInnerMost, description);
    }
}
