package javax.jms;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/jms/ConnectionFactory.class */
public interface ConnectionFactory {
    Connection createConnection() throws JMSException;

    Connection createConnection(String str, String str2) throws JMSException;
}
