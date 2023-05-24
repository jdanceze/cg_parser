package javax.ejb;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/MessageDrivenBean.class */
public interface MessageDrivenBean extends EnterpriseBean {
    void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) throws EJBException;

    void ejbRemove() throws EJBException;
}
