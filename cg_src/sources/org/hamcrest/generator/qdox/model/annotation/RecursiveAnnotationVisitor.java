package org.hamcrest.generator.qdox.model.annotation;

import java.util.ListIterator;
import org.hamcrest.generator.qdox.model.Annotation;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/RecursiveAnnotationVisitor.class */
public class RecursiveAnnotationVisitor implements AnnotationVisitor {
    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotation(Annotation annotation) {
        for (AnnotationValue value : annotation.getPropertyMap().values()) {
            value.accept(this);
        }
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationAdd(AnnotationAdd op) {
        op.getLeft().accept(this);
        op.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationConstant(AnnotationConstant constant) {
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationDivide(AnnotationDivide op) {
        op.getLeft().accept(this);
        op.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationFieldRef(AnnotationFieldRef fieldRef) {
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationGreaterThan(AnnotationGreaterThan op) {
        op.getLeft().accept(this);
        op.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationLessThan(AnnotationLessThan op) {
        op.getLeft().accept(this);
        op.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationMultiply(AnnotationMultiply op) {
        op.getLeft().accept(this);
        op.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationParenExpression(AnnotationParenExpression parenExpression) {
        parenExpression.getValue().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationSubtract(AnnotationSubtract op) {
        op.getLeft().accept(this);
        op.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationTypeRef(AnnotationTypeRef typeRef) {
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationValueList(AnnotationValueList valueList) {
        ListIterator i = valueList.getValueList().listIterator();
        while (i.hasNext()) {
            AnnotationValue value = (AnnotationValue) i.next();
            value.accept(this);
        }
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationAnd(AnnotationAnd and) {
        and.getLeft().accept(this);
        and.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationGreaterEquals(AnnotationGreaterEquals greaterEquals) {
        greaterEquals.getLeft().accept(this);
        greaterEquals.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationLessEquals(AnnotationLessEquals lessEquals) {
        lessEquals.getLeft().accept(this);
        lessEquals.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationLogicalAnd(AnnotationLogicalAnd and) {
        and.getLeft().accept(this);
        and.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationLogicalNot(AnnotationLogicalNot not) {
        not.getValue().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationLogicalOr(AnnotationLogicalOr or) {
        or.getLeft().accept(this);
        or.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationMinusSign(AnnotationMinusSign sign) {
        sign.getValue().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationNot(AnnotationNot not) {
        not.getValue().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationOr(AnnotationOr or) {
        or.getLeft().accept(this);
        or.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationPlusSign(AnnotationPlusSign sign) {
        sign.getValue().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationRemainder(AnnotationRemainder remainder) {
        remainder.getLeft().accept(this);
        remainder.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationShiftLeft(AnnotationShiftLeft left) {
        left.getLeft().accept(this);
        left.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationShiftRight(AnnotationShiftRight right) {
        right.getLeft().accept(this);
        right.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationUnsignedShiftRight(AnnotationUnsignedShiftRight right) {
        right.getLeft().accept(this);
        right.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationEquals(AnnotationEquals annotationEquals) {
        annotationEquals.getLeft().accept(this);
        annotationEquals.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationExclusiveOr(AnnotationExclusiveOr annotationExclusiveOr) {
        annotationExclusiveOr.getLeft().accept(this);
        annotationExclusiveOr.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationNotEquals(AnnotationNotEquals annotationNotEquals) {
        annotationNotEquals.getLeft().accept(this);
        annotationNotEquals.getRight().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationQuery(AnnotationQuery annotationQuery) {
        annotationQuery.getCondition().accept(this);
        annotationQuery.getTrueExpression().accept(this);
        annotationQuery.getFalseExpression().accept(this);
        return null;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationCast(AnnotationCast annotationCast) {
        annotationCast.getValue().accept(this);
        return null;
    }
}
