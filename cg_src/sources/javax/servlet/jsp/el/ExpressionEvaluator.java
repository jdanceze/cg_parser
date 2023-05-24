package javax.servlet.jsp.el;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/el/ExpressionEvaluator.class */
public abstract class ExpressionEvaluator {
    public abstract Expression parseExpression(String str, Class cls, FunctionMapper functionMapper) throws ELException;

    public abstract Object evaluate(String str, Class cls, VariableResolver variableResolver, FunctionMapper functionMapper) throws ELException;
}
