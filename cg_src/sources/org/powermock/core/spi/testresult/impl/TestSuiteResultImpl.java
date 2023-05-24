package org.powermock.core.spi.testresult.impl;

import org.powermock.core.spi.testresult.Result;
import org.powermock.core.spi.testresult.TestSuiteResult;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/spi/testresult/impl/TestSuiteResultImpl.class */
public class TestSuiteResultImpl implements TestSuiteResult {
    private int failureCount;
    private int successCount;
    private int testCount;
    private int ignoreCount;

    public TestSuiteResultImpl() {
    }

    public TestSuiteResultImpl(int failureCount, int successCount, int testCount, int ignoreCount) {
        this.failureCount = failureCount;
        this.successCount = successCount;
        this.testCount = testCount;
        this.ignoreCount = ignoreCount;
    }

    @Override // org.powermock.core.spi.testresult.TestSuiteResult
    public int getFailureCount() {
        return this.failureCount;
    }

    @Override // org.powermock.core.spi.testresult.TestSuiteResult
    public int getSuccessCount() {
        return this.successCount;
    }

    @Override // org.powermock.core.spi.testresult.TestSuiteResult
    public int getIgnoreCount() {
        return this.ignoreCount;
    }

    @Override // org.powermock.core.spi.testresult.TestSuiteResult
    public Result getResult() {
        Result result = Result.SUCCESSFUL;
        if (this.testCount == 0) {
            result = Result.IGNORED;
        } else if (this.failureCount != 0) {
            result = Result.FAILED;
        }
        return result;
    }

    @Override // org.powermock.core.spi.testresult.TestSuiteResult
    public int getTestCount() {
        return this.testCount;
    }

    public String toString() {
        return getResult().toString();
    }
}
