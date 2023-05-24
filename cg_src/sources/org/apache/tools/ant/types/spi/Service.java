package org.apache.tools.ant.types.spi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/spi/Service.class */
public class Service extends ProjectComponent {
    private List<Provider> providerList = new ArrayList();
    private String type;

    public void setProvider(String className) {
        Provider provider = new Provider();
        provider.setClassName(className);
        this.providerList.add(provider);
    }

    public void addConfiguredProvider(Provider provider) {
        provider.check();
        this.providerList.add(provider);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public InputStream getAsStream() throws IOException {
        return new ByteArrayInputStream(((String) this.providerList.stream().map((v0) -> {
            return v0.getClassName();
        }).collect(Collectors.joining("\n"))).getBytes(StandardCharsets.UTF_8));
    }

    public void check() {
        if (this.type == null) {
            throw new BuildException("type attribute must be set for service element", getLocation());
        }
        if (this.type.isEmpty()) {
            throw new BuildException("Invalid empty type classname", getLocation());
        }
        if (this.providerList.isEmpty()) {
            throw new BuildException("provider attribute or nested provider element must be set!", getLocation());
        }
    }
}
