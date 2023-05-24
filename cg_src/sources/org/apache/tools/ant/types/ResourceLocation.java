package org.apache.tools.ant.types;

import java.net.URL;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/ResourceLocation.class */
public class ResourceLocation {
    private String publicId = null;
    private String location = null;
    private URL base = null;

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBase(URL base) {
        this.base = base;
    }

    public String getPublicId() {
        return this.publicId;
    }

    public String getLocation() {
        return this.location;
    }

    public URL getBase() {
        return this.base;
    }
}
