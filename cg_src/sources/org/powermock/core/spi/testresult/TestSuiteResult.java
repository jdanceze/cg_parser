package org.powermock.core.spi.testresult;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/spi/testresult/TestSuiteResult.class */
public interface TestSuiteResult {
    int getTestCount();

    int getIgnoreCount();

    int getSuccessCount();

    int getFailureCount();

    Result getResult();
}
