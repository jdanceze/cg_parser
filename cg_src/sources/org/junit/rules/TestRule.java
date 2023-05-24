package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/rules/TestRule.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/rules/TestRule.class */
public interface TestRule {
    Statement apply(Statement statement, Description description);
}
