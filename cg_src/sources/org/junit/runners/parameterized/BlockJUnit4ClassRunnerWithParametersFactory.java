package org.junit.runners.parameterized;

import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runners/parameterized/BlockJUnit4ClassRunnerWithParametersFactory.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runners/parameterized/BlockJUnit4ClassRunnerWithParametersFactory.class */
public class BlockJUnit4ClassRunnerWithParametersFactory implements ParametersRunnerFactory {
    @Override // org.junit.runners.parameterized.ParametersRunnerFactory
    public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
        return new BlockJUnit4ClassRunnerWithParameters(test);
    }
}
