package soot.javaToJimple;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/JimpleBodyBuilderFactory.class */
public class JimpleBodyBuilderFactory extends AbstractJBBFactory {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJBBFactory
    public AbstractJimpleBodyBuilder createJimpleBodyBuilder() {
        return new JimpleBodyBuilder();
    }
}
