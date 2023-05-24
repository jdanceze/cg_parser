package org.junit.internal.runners.rules;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMember;
import org.junit.runners.model.TestClass;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/rules/RuleMemberValidator.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/rules/RuleMemberValidator.class */
public class RuleMemberValidator {
    public static final RuleMemberValidator CLASS_RULE_VALIDATOR = classRuleValidatorBuilder().withValidator(new DeclaringClassMustBePublic()).withValidator(new MemberMustBeStatic()).withValidator(new MemberMustBePublic()).withValidator(new FieldMustBeATestRule()).build();
    public static final RuleMemberValidator RULE_VALIDATOR = testRuleValidatorBuilder().withValidator(new MemberMustBeNonStaticOrAlsoClassRule()).withValidator(new MemberMustBePublic()).withValidator(new FieldMustBeARule()).build();
    public static final RuleMemberValidator CLASS_RULE_METHOD_VALIDATOR = classRuleValidatorBuilder().forMethods().withValidator(new DeclaringClassMustBePublic()).withValidator(new MemberMustBeStatic()).withValidator(new MemberMustBePublic()).withValidator(new MethodMustBeATestRule()).build();
    public static final RuleMemberValidator RULE_METHOD_VALIDATOR = testRuleValidatorBuilder().forMethods().withValidator(new MemberMustBeNonStaticOrAlsoClassRule()).withValidator(new MemberMustBePublic()).withValidator(new MethodMustBeARule()).build();
    private final Class<? extends Annotation> annotation;
    private final boolean methods;
    private final List<RuleValidator> validatorStrategies;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/rules/RuleMemberValidator$RuleValidator.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/rules/RuleMemberValidator$RuleValidator.class */
    public interface RuleValidator {
        void validate(FrameworkMember<?> frameworkMember, Class<? extends Annotation> cls, List<Throwable> list);
    }

    RuleMemberValidator(Builder builder) {
        this.annotation = builder.annotation;
        this.methods = builder.methods;
        this.validatorStrategies = builder.validators;
    }

    public void validate(TestClass target, List<Throwable> errors) {
        List<? extends FrameworkMember<?>> members = this.methods ? target.getAnnotatedMethods(this.annotation) : target.getAnnotatedFields(this.annotation);
        for (FrameworkMember<?> each : members) {
            validateMember(each, errors);
        }
    }

    private void validateMember(FrameworkMember<?> member, List<Throwable> errors) {
        for (RuleValidator strategy : this.validatorStrategies) {
            strategy.validate(member, this.annotation, errors);
        }
    }

    private static Builder classRuleValidatorBuilder() {
        return new Builder(ClassRule.class);
    }

    private static Builder testRuleValidatorBuilder() {
        return new Builder(Rule.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/rules/RuleMemberValidator$Builder.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/rules/RuleMemberValidator$Builder.class */
    public static class Builder {
        private final Class<? extends Annotation> annotation;
        private boolean methods;
        private final List<RuleValidator> validators;

        private Builder(Class<? extends Annotation> annotation) {
            this.annotation = annotation;
            this.methods = false;
            this.validators = new ArrayList();
        }

        Builder forMethods() {
            this.methods = true;
            return this;
        }

        Builder withValidator(RuleValidator validator) {
            this.validators.add(validator);
            return this;
        }

        RuleMemberValidator build() {
            return new RuleMemberValidator(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isRuleType(FrameworkMember<?> member) {
        return isMethodRule(member) || isTestRule(member);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isTestRule(FrameworkMember<?> member) {
        return TestRule.class.isAssignableFrom(member.getType());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isMethodRule(FrameworkMember<?> member) {
        return MethodRule.class.isAssignableFrom(member.getType());
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/rules/RuleMemberValidator$MemberMustBeNonStaticOrAlsoClassRule.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/rules/RuleMemberValidator$MemberMustBeNonStaticOrAlsoClassRule.class */
    private static final class MemberMustBeNonStaticOrAlsoClassRule implements RuleValidator {
        private MemberMustBeNonStaticOrAlsoClassRule() {
        }

        @Override // org.junit.internal.runners.rules.RuleMemberValidator.RuleValidator
        public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
            String message;
            boolean isMethodRuleMember = RuleMemberValidator.isMethodRule(member);
            boolean isClassRuleAnnotated = member.getAnnotation(ClassRule.class) != null;
            if (member.isStatic()) {
                if (isMethodRuleMember || !isClassRuleAnnotated) {
                    if (RuleMemberValidator.isMethodRule(member)) {
                        message = "must not be static.";
                    } else {
                        message = "must not be static or it must be annotated with @ClassRule.";
                    }
                    errors.add(new ValidationError(member, annotation, message));
                }
            }
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/rules/RuleMemberValidator$MemberMustBeStatic.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/rules/RuleMemberValidator$MemberMustBeStatic.class */
    private static final class MemberMustBeStatic implements RuleValidator {
        private MemberMustBeStatic() {
        }

        @Override // org.junit.internal.runners.rules.RuleMemberValidator.RuleValidator
        public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
            if (!member.isStatic()) {
                errors.add(new ValidationError(member, annotation, "must be static."));
            }
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/rules/RuleMemberValidator$DeclaringClassMustBePublic.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/rules/RuleMemberValidator$DeclaringClassMustBePublic.class */
    private static final class DeclaringClassMustBePublic implements RuleValidator {
        private DeclaringClassMustBePublic() {
        }

        @Override // org.junit.internal.runners.rules.RuleMemberValidator.RuleValidator
        public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
            if (!isDeclaringClassPublic(member)) {
                errors.add(new ValidationError(member, annotation, "must be declared in a public class."));
            }
        }

        private boolean isDeclaringClassPublic(FrameworkMember<?> member) {
            return Modifier.isPublic(member.getDeclaringClass().getModifiers());
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/rules/RuleMemberValidator$MemberMustBePublic.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/rules/RuleMemberValidator$MemberMustBePublic.class */
    private static final class MemberMustBePublic implements RuleValidator {
        private MemberMustBePublic() {
        }

        @Override // org.junit.internal.runners.rules.RuleMemberValidator.RuleValidator
        public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
            if (!member.isPublic()) {
                errors.add(new ValidationError(member, annotation, "must be public."));
            }
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/rules/RuleMemberValidator$FieldMustBeARule.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/rules/RuleMemberValidator$FieldMustBeARule.class */
    private static final class FieldMustBeARule implements RuleValidator {
        private FieldMustBeARule() {
        }

        @Override // org.junit.internal.runners.rules.RuleMemberValidator.RuleValidator
        public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
            if (!RuleMemberValidator.isRuleType(member)) {
                errors.add(new ValidationError(member, annotation, "must implement MethodRule or TestRule."));
            }
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/rules/RuleMemberValidator$MethodMustBeARule.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/rules/RuleMemberValidator$MethodMustBeARule.class */
    private static final class MethodMustBeARule implements RuleValidator {
        private MethodMustBeARule() {
        }

        @Override // org.junit.internal.runners.rules.RuleMemberValidator.RuleValidator
        public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
            if (!RuleMemberValidator.isRuleType(member)) {
                errors.add(new ValidationError(member, annotation, "must return an implementation of MethodRule or TestRule."));
            }
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/rules/RuleMemberValidator$MethodMustBeATestRule.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/rules/RuleMemberValidator$MethodMustBeATestRule.class */
    private static final class MethodMustBeATestRule implements RuleValidator {
        private MethodMustBeATestRule() {
        }

        @Override // org.junit.internal.runners.rules.RuleMemberValidator.RuleValidator
        public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
            if (!RuleMemberValidator.isTestRule(member)) {
                errors.add(new ValidationError(member, annotation, "must return an implementation of TestRule."));
            }
        }
    }

    /* JADX WARN: Classes with same name are omitted:
      gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/runners/rules/RuleMemberValidator$FieldMustBeATestRule.class
     */
    /* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/runners/rules/RuleMemberValidator$FieldMustBeATestRule.class */
    private static final class FieldMustBeATestRule implements RuleValidator {
        private FieldMustBeATestRule() {
        }

        @Override // org.junit.internal.runners.rules.RuleMemberValidator.RuleValidator
        public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
            if (!RuleMemberValidator.isTestRule(member)) {
                errors.add(new ValidationError(member, annotation, "must implement TestRule."));
            }
        }
    }
}
