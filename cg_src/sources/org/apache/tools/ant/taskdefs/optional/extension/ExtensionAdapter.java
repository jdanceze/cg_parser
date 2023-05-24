package org.apache.tools.ant.taskdefs.optional.extension;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/ExtensionAdapter.class */
public class ExtensionAdapter extends DataType {
    private String extensionName;
    private org.apache.tools.ant.util.DeweyDecimal specificationVersion;
    private String specificationVendor;
    private String implementationVendorID;
    private String implementationVendor;
    private org.apache.tools.ant.util.DeweyDecimal implementationVersion;
    private String implementationURL;

    public void setExtensionName(String extensionName) {
        verifyNotAReference();
        this.extensionName = extensionName;
    }

    public void setSpecificationVersion(String specificationVersion) {
        verifyNotAReference();
        this.specificationVersion = new org.apache.tools.ant.util.DeweyDecimal(specificationVersion);
    }

    public void setSpecificationVendor(String specificationVendor) {
        verifyNotAReference();
        this.specificationVendor = specificationVendor;
    }

    public void setImplementationVendorId(String implementationVendorID) {
        verifyNotAReference();
        this.implementationVendorID = implementationVendorID;
    }

    public void setImplementationVendor(String implementationVendor) {
        verifyNotAReference();
        this.implementationVendor = implementationVendor;
    }

    public void setImplementationVersion(String implementationVersion) {
        verifyNotAReference();
        this.implementationVersion = new org.apache.tools.ant.util.DeweyDecimal(implementationVersion);
    }

    public void setImplementationUrl(String implementationURL) {
        verifyNotAReference();
        this.implementationURL = implementationURL;
    }

    @Override // org.apache.tools.ant.types.DataType
    public void setRefid(Reference reference) throws BuildException {
        if (null != this.extensionName || null != this.specificationVersion || null != this.specificationVendor || null != this.implementationVersion || null != this.implementationVendorID || null != this.implementationVendor || null != this.implementationURL) {
            throw tooManyAttributes();
        }
        super.setRefid(reference);
    }

    private void verifyNotAReference() throws BuildException {
        if (isReference()) {
            throw tooManyAttributes();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Extension toExtension() throws BuildException {
        if (isReference()) {
            return getRef().toExtension();
        }
        dieOnCircularReference();
        if (null == this.extensionName) {
            throw new BuildException("Extension is missing name.");
        }
        String specificationVersionString = null;
        if (null != this.specificationVersion) {
            specificationVersionString = this.specificationVersion.toString();
        }
        String implementationVersionString = null;
        if (null != this.implementationVersion) {
            implementationVersionString = this.implementationVersion.toString();
        }
        return new Extension(this.extensionName, specificationVersionString, this.specificationVendor, implementationVersionString, this.implementationVendor, this.implementationVendorID, this.implementationURL);
    }

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        return "{" + toExtension() + "}";
    }

    private ExtensionAdapter getRef() {
        return (ExtensionAdapter) getCheckedRef(ExtensionAdapter.class);
    }
}
