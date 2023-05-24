package org.junit.experimental.categories;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/categories/Categories.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/categories/Categories.class */
public class Categories extends Suite {

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/categories/Categories$ExcludeCategory.class
     */
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/categories/Categories$ExcludeCategory.class */
    public @interface ExcludeCategory {
        Class<?>[] value() default {};

        boolean matchAny() default true;
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/categories/Categories$IncludeCategory.class
     */
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/categories/Categories$IncludeCategory.class */
    public @interface IncludeCategory {
        Class<?>[] value() default {};

        boolean matchAny() default true;
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/categories/Categories$CategoryFilter.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/categories/Categories$CategoryFilter.class */
    public static class CategoryFilter extends Filter {
        private final Set<Class<?>> included;
        private final Set<Class<?>> excluded;
        private final boolean includedAny;
        private final boolean excludedAny;

        public static CategoryFilter include(boolean matchAny, Class<?>... categories) {
            return new CategoryFilter(matchAny, categories, true, (Class<?>[]) null);
        }

        public static CategoryFilter include(Class<?> category) {
            return include(true, category);
        }

        public static CategoryFilter include(Class<?>... categories) {
            return include(true, categories);
        }

        public static CategoryFilter exclude(boolean matchAny, Class<?>... categories) {
            return new CategoryFilter(true, (Class<?>[]) null, matchAny, categories);
        }

        public static CategoryFilter exclude(Class<?> category) {
            return exclude(true, category);
        }

        public static CategoryFilter exclude(Class<?>... categories) {
            return exclude(true, categories);
        }

        public static CategoryFilter categoryFilter(boolean matchAnyInclusions, Set<Class<?>> inclusions, boolean matchAnyExclusions, Set<Class<?>> exclusions) {
            return new CategoryFilter(matchAnyInclusions, inclusions, matchAnyExclusions, exclusions);
        }

        @Deprecated
        public CategoryFilter(Class<?> includedCategory, Class<?> excludedCategory) {
            this.includedAny = true;
            this.excludedAny = true;
            this.included = Categories.nullableClassToSet(includedCategory);
            this.excluded = Categories.nullableClassToSet(excludedCategory);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public CategoryFilter(boolean matchAnyIncludes, Set<Class<?>> includes, boolean matchAnyExcludes, Set<Class<?>> excludes) {
            this.includedAny = matchAnyIncludes;
            this.excludedAny = matchAnyExcludes;
            this.included = copyAndRefine(includes);
            this.excluded = copyAndRefine(excludes);
        }

        private CategoryFilter(boolean matchAnyIncludes, Class<?>[] inclusions, boolean matchAnyExcludes, Class<?>[] exclusions) {
            this.includedAny = matchAnyIncludes;
            this.excludedAny = matchAnyExcludes;
            this.included = Categories.createSet(inclusions);
            this.excluded = Categories.createSet(exclusions);
        }

        @Override // org.junit.runner.manipulation.Filter
        public String describe() {
            return toString();
        }

        public String toString() {
            StringBuilder description = new StringBuilder("categories ").append(this.included.isEmpty() ? "[all]" : this.included);
            if (!this.excluded.isEmpty()) {
                description.append(" - ").append(this.excluded);
            }
            return description.toString();
        }

        @Override // org.junit.runner.manipulation.Filter
        public boolean shouldRun(Description description) {
            if (hasCorrectCategoryAnnotation(description)) {
                return true;
            }
            Iterator i$ = description.getChildren().iterator();
            while (i$.hasNext()) {
                Description each = i$.next();
                if (shouldRun(each)) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasCorrectCategoryAnnotation(Description description) {
            Set<Class<?>> childCategories = categories(description);
            if (childCategories.isEmpty()) {
                return this.included.isEmpty();
            }
            if (!this.excluded.isEmpty()) {
                if (this.excludedAny) {
                    if (matchesAnyParentCategories(childCategories, this.excluded)) {
                        return false;
                    }
                } else if (matchesAllParentCategories(childCategories, this.excluded)) {
                    return false;
                }
            }
            if (this.included.isEmpty()) {
                return true;
            }
            if (this.includedAny) {
                return matchesAnyParentCategories(childCategories, this.included);
            }
            return matchesAllParentCategories(childCategories, this.included);
        }

        private boolean matchesAnyParentCategories(Set<Class<?>> childCategories, Set<Class<?>> parentCategories) {
            for (Class<?> parentCategory : parentCategories) {
                if (Categories.hasAssignableTo(childCategories, parentCategory)) {
                    return true;
                }
            }
            return false;
        }

        private boolean matchesAllParentCategories(Set<Class<?>> childCategories, Set<Class<?>> parentCategories) {
            for (Class<?> parentCategory : parentCategories) {
                if (!Categories.hasAssignableTo(childCategories, parentCategory)) {
                    return false;
                }
            }
            return true;
        }

        private static Set<Class<?>> categories(Description description) {
            Set<Class<?>> categories = new HashSet<>();
            Collections.addAll(categories, directCategories(description));
            Collections.addAll(categories, directCategories(parentDescription(description)));
            return categories;
        }

        private static Description parentDescription(Description description) {
            Class<?> testClass = description.getTestClass();
            if (testClass == null) {
                return null;
            }
            return Description.createSuiteDescription(testClass);
        }

        private static Class<?>[] directCategories(Description description) {
            if (description == null) {
                return new Class[0];
            }
            Category annotation = (Category) description.getAnnotation(Category.class);
            return annotation == null ? new Class[0] : annotation.value();
        }

        private static Set<Class<?>> copyAndRefine(Set<Class<?>> classes) {
            Set<Class<?>> c = new LinkedHashSet<>();
            if (classes != null) {
                c.addAll(classes);
            }
            c.remove(null);
            return c;
        }
    }

    public Categories(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
        try {
            Set<Class<?>> included = getIncludedCategory(klass);
            Set<Class<?>> excluded = getExcludedCategory(klass);
            boolean isAnyIncluded = isAnyIncluded(klass);
            boolean isAnyExcluded = isAnyExcluded(klass);
            filter(CategoryFilter.categoryFilter(isAnyIncluded, included, isAnyExcluded, excluded));
        } catch (NoTestsRemainException e) {
            throw new InitializationError(e);
        }
    }

    private static Set<Class<?>> getIncludedCategory(Class<?> klass) {
        IncludeCategory annotation = (IncludeCategory) klass.getAnnotation(IncludeCategory.class);
        return createSet(annotation == null ? null : annotation.value());
    }

    private static boolean isAnyIncluded(Class<?> klass) {
        IncludeCategory annotation = (IncludeCategory) klass.getAnnotation(IncludeCategory.class);
        return annotation == null || annotation.matchAny();
    }

    private static Set<Class<?>> getExcludedCategory(Class<?> klass) {
        ExcludeCategory annotation = (ExcludeCategory) klass.getAnnotation(ExcludeCategory.class);
        return createSet(annotation == null ? null : annotation.value());
    }

    private static boolean isAnyExcluded(Class<?> klass) {
        ExcludeCategory annotation = (ExcludeCategory) klass.getAnnotation(ExcludeCategory.class);
        return annotation == null || annotation.matchAny();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean hasAssignableTo(Set<Class<?>> assigns, Class<?> to) {
        for (Class<?> from : assigns) {
            if (to.isAssignableFrom(from)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Set<Class<?>> createSet(Class<?>[] classes) {
        if (classes == null || classes.length == 0) {
            return Collections.emptySet();
        }
        for (Class<?> category : classes) {
            if (category == null) {
                throw new NullPointerException("has null category");
            }
        }
        return classes.length == 1 ? Collections.singleton(classes[0]) : new LinkedHashSet(Arrays.asList(classes));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Set<Class<?>> nullableClassToSet(Class<?> nullableClass) {
        return nullableClass == null ? Collections.emptySet() : Collections.singleton(nullableClass);
    }
}
