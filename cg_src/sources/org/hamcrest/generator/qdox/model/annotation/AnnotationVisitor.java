package org.hamcrest.generator.qdox.model.annotation;

import org.hamcrest.generator.qdox.model.Annotation;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationVisitor.class */
public interface AnnotationVisitor {
    Object visitAnnotationAdd(AnnotationAdd annotationAdd);

    Object visitAnnotationSubtract(AnnotationSubtract annotationSubtract);

    Object visitAnnotationMultiply(AnnotationMultiply annotationMultiply);

    Object visitAnnotationDivide(AnnotationDivide annotationDivide);

    Object visitAnnotationGreaterThan(AnnotationGreaterThan annotationGreaterThan);

    Object visitAnnotationLessThan(AnnotationLessThan annotationLessThan);

    Object visitAnnotation(Annotation annotation);

    Object visitAnnotationConstant(AnnotationConstant annotationConstant);

    Object visitAnnotationParenExpression(AnnotationParenExpression annotationParenExpression);

    Object visitAnnotationValueList(AnnotationValueList annotationValueList);

    Object visitAnnotationTypeRef(AnnotationTypeRef annotationTypeRef);

    Object visitAnnotationFieldRef(AnnotationFieldRef annotationFieldRef);

    Object visitAnnotationLessEquals(AnnotationLessEquals annotationLessEquals);

    Object visitAnnotationGreaterEquals(AnnotationGreaterEquals annotationGreaterEquals);

    Object visitAnnotationRemainder(AnnotationRemainder annotationRemainder);

    Object visitAnnotationOr(AnnotationOr annotationOr);

    Object visitAnnotationAnd(AnnotationAnd annotationAnd);

    Object visitAnnotationShiftLeft(AnnotationShiftLeft annotationShiftLeft);

    Object visitAnnotationShiftRight(AnnotationShiftRight annotationShiftRight);

    Object visitAnnotationNot(AnnotationNot annotationNot);

    Object visitAnnotationLogicalOr(AnnotationLogicalOr annotationLogicalOr);

    Object visitAnnotationLogicalAnd(AnnotationLogicalAnd annotationLogicalAnd);

    Object visitAnnotationLogicalNot(AnnotationLogicalNot annotationLogicalNot);

    Object visitAnnotationMinusSign(AnnotationMinusSign annotationMinusSign);

    Object visitAnnotationPlusSign(AnnotationPlusSign annotationPlusSign);

    Object visitAnnotationUnsignedShiftRight(AnnotationUnsignedShiftRight annotationUnsignedShiftRight);

    Object visitAnnotationEquals(AnnotationEquals annotationEquals);

    Object visitAnnotationNotEquals(AnnotationNotEquals annotationNotEquals);

    Object visitAnnotationExclusiveOr(AnnotationExclusiveOr annotationExclusiveOr);

    Object visitAnnotationQuery(AnnotationQuery annotationQuery);

    Object visitAnnotationCast(AnnotationCast annotationCast);
}
