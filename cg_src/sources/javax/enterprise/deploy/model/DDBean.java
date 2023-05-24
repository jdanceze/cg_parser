package javax.enterprise.deploy.model;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/model/DDBean.class */
public interface DDBean {
    String getXpath();

    String getText();

    String getId();

    DDBeanRoot getRoot();

    DDBean[] getChildBean(String str);

    String[] getText(String str);

    void addXpathListener(String str, XpathListener xpathListener);

    void removeXpathListener(String str, XpathListener xpathListener);

    String[] getAttributeNames();

    String getAttributeValue(String str);
}
