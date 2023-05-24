package org.junit.runners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
/* JADX INFO: Access modifiers changed from: package-private */
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/RuleContainer.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/RuleContainer.class */
public class RuleContainer {
    private final IdentityHashMap<Object, Integer> orderValues = new IdentityHashMap<>();
    private final List<TestRule> testRules = new ArrayList();
    private final List<MethodRule> methodRules = new ArrayList();
    static final Comparator<RuleEntry> ENTRY_COMPARATOR = new Comparator<RuleEntry>() { // from class: org.junit.runners.RuleContainer.1
        @Override // java.util.Comparator
        public int compare(RuleEntry o1, RuleEntry o2) {
            int result = compareInt(o1.order, o2.order);
            return result != 0 ? result : o1.type - o2.type;
        }

        private int compareInt(int a, int b) {
            if (a < b) {
                return 1;
            }
            return a == b ? 0 : -1;
        }
    };

    public void setOrder(Object rule, int order) {
        this.orderValues.put(rule, Integer.valueOf(order));
    }

    public void add(MethodRule methodRule) {
        this.methodRules.add(methodRule);
    }

    public void add(TestRule testRule) {
        this.testRules.add(testRule);
    }

    private List<RuleEntry> getSortedEntries() {
        List<RuleEntry> ruleEntries = new ArrayList<>(this.methodRules.size() + this.testRules.size());
        for (MethodRule rule : this.methodRules) {
            ruleEntries.add(new RuleEntry(rule, 0, this.orderValues.get(rule)));
        }
        for (TestRule rule2 : this.testRules) {
            ruleEntries.add(new RuleEntry(rule2, 1, this.orderValues.get(rule2)));
        }
        Collections.sort(ruleEntries, ENTRY_COMPARATOR);
        return ruleEntries;
    }

    public Statement apply(FrameworkMethod method, Description description, Object target, Statement statement) {
        if (this.methodRules.isEmpty() && this.testRules.isEmpty()) {
            return statement;
        }
        Statement result = statement;
        for (RuleEntry ruleEntry : getSortedEntries()) {
            if (ruleEntry.type == 1) {
                result = ((TestRule) ruleEntry.rule).apply(result, description);
            } else {
                result = ((MethodRule) ruleEntry.rule).apply(result, method, target);
            }
        }
        return result;
    }

    List<Object> getSortedRules() {
        List<Object> result = new ArrayList<>();
        for (RuleEntry entry : getSortedEntries()) {
            result.add(entry.rule);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/RuleContainer$RuleEntry.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/RuleContainer$RuleEntry.class */
    public static class RuleEntry {
        static final int TYPE_TEST_RULE = 1;
        static final int TYPE_METHOD_RULE = 0;
        final Object rule;
        final int type;
        final int order;

        /* JADX INFO: Access modifiers changed from: package-private */
        public RuleEntry(Object rule, int type, Integer order) {
            this.rule = rule;
            this.type = type;
            this.order = order != null ? order.intValue() : -1;
        }
    }
}
