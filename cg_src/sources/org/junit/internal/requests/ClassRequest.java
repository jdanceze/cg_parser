package org.junit.internal.requests;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.internal.builders.SuiteMethodBuilder;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/requests/ClassRequest.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/requests/ClassRequest.class */
public class ClassRequest extends MemoizingRequest {
    private final Class<?> fTestClass;
    private final boolean canUseSuiteMethod;

    public ClassRequest(Class<?> testClass, boolean canUseSuiteMethod) {
        this.fTestClass = testClass;
        this.canUseSuiteMethod = canUseSuiteMethod;
    }

    public ClassRequest(Class<?> testClass) {
        this(testClass, true);
    }

    @Override // org.junit.internal.requests.MemoizingRequest
    protected Runner createRunner() {
        return new CustomAllDefaultPossibilitiesBuilder().safeRunnerForClass(this.fTestClass);
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/requests/ClassRequest$CustomAllDefaultPossibilitiesBuilder.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/requests/ClassRequest$CustomAllDefaultPossibilitiesBuilder.class */
    private class CustomAllDefaultPossibilitiesBuilder extends AllDefaultPossibilitiesBuilder {
        private CustomAllDefaultPossibilitiesBuilder() {
        }

        @Override // org.junit.internal.builders.AllDefaultPossibilitiesBuilder
        protected RunnerBuilder suiteMethodBuilder() {
            return new CustomSuiteMethodBuilder();
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/requests/ClassRequest$CustomSuiteMethodBuilder.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/requests/ClassRequest$CustomSuiteMethodBuilder.class */
    private class CustomSuiteMethodBuilder extends SuiteMethodBuilder {
        private CustomSuiteMethodBuilder() {
        }

        @Override // org.junit.internal.builders.SuiteMethodBuilder, org.junit.runners.model.RunnerBuilder
        public Runner runnerForClass(Class<?> testClass) throws Throwable {
            if (testClass == ClassRequest.this.fTestClass && !ClassRequest.this.canUseSuiteMethod) {
                return null;
            }
            return super.runnerForClass(testClass);
        }
    }
}
