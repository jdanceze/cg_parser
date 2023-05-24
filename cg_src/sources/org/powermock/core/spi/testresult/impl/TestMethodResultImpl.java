package org.powermock.core.spi.testresult.impl;

import org.powermock.core.spi.testresult.Result;
import org.powermock.core.spi.testresult.TestMethodResult;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/spi/testresult/impl/TestMethodResultImpl.class */
public class TestMethodResultImpl implements TestMethodResult {
    private final Result result;

    public TestMethodResultImpl(Result result) {
        this.result = result;
    }

    @Override // org.powermock.core.spi.testresult.TestMethodResult
    public Result getResult() {
        return this.result;
    }

    public String toString() {
        return this.result.toString();
    }
}
