package org.junit.runner;

import org.junit.runner.manipulation.Filter;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/FilterFactory.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/FilterFactory.class */
public interface FilterFactory {
    Filter createFilter(FilterFactoryParams filterFactoryParams) throws FilterNotCreatedException;

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/runner/FilterFactory$FilterNotCreatedException.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/runner/FilterFactory$FilterNotCreatedException.class */
    public static class FilterNotCreatedException extends Exception {
        public FilterNotCreatedException(Exception exception) {
            super(exception.getMessage(), exception);
        }
    }
}
