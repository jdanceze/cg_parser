package org.junit.experimental.categories;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.experimental.categories.Categories;
import org.junit.runner.FilterFactory;
import org.junit.runner.FilterFactoryParams;
import org.junit.runner.manipulation.Filter;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/categories/IncludeCategories.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/categories/IncludeCategories.class */
public final class IncludeCategories extends CategoryFilterFactory {
    @Override // org.junit.experimental.categories.CategoryFilterFactory, org.junit.runner.FilterFactory
    public /* bridge */ /* synthetic */ Filter createFilter(FilterFactoryParams x0) throws FilterFactory.FilterNotCreatedException {
        return super.createFilter(x0);
    }

    @Override // org.junit.experimental.categories.CategoryFilterFactory
    protected Filter createFilter(List<Class<?>> categories) {
        return new IncludesAny(categories);
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/categories/IncludeCategories$IncludesAny.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/categories/IncludeCategories$IncludesAny.class */
    private static class IncludesAny extends Categories.CategoryFilter {
        public IncludesAny(List<Class<?>> categories) {
            this(new HashSet(categories));
        }

        public IncludesAny(Set<Class<?>> categories) {
            super(true, categories, true, (Set<Class<?>>) null);
        }

        @Override // org.junit.experimental.categories.Categories.CategoryFilter, org.junit.runner.manipulation.Filter
        public String describe() {
            return "includes " + super.describe();
        }
    }
}
