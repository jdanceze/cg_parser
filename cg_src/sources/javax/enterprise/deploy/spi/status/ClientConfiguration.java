package javax.enterprise.deploy.spi.status;

import java.io.Serializable;
import javax.enterprise.deploy.spi.exceptions.ClientExecuteException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/enterprise/deploy/spi/status/ClientConfiguration.class */
public interface ClientConfiguration extends Serializable {
    void execute() throws ClientExecuteException;
}
