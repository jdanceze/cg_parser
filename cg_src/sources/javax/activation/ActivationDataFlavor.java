package javax.activation;

import java.awt.datatransfer.DataFlavor;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:j2ee.jar:javax/activation/ActivationDataFlavor.class
 */
/* loaded from: gencallgraphv3.jar:javax.activation-api-1.2.0.jar:javax/activation/ActivationDataFlavor.class */
public class ActivationDataFlavor extends DataFlavor {
    private String mimeType;
    private MimeType mimeObject;
    private String humanPresentableName;
    private Class representationClass;

    public ActivationDataFlavor(Class representationClass, String mimeType, String humanPresentableName) {
        super(mimeType, humanPresentableName);
        this.mimeType = null;
        this.mimeObject = null;
        this.humanPresentableName = null;
        this.representationClass = null;
        this.mimeType = mimeType;
        this.humanPresentableName = humanPresentableName;
        this.representationClass = representationClass;
    }

    public ActivationDataFlavor(Class representationClass, String humanPresentableName) {
        super(representationClass, humanPresentableName);
        this.mimeType = null;
        this.mimeObject = null;
        this.humanPresentableName = null;
        this.representationClass = null;
        this.mimeType = super.getMimeType();
        this.representationClass = representationClass;
        this.humanPresentableName = humanPresentableName;
    }

    public ActivationDataFlavor(String mimeType, String humanPresentableName) {
        super(mimeType, humanPresentableName);
        this.mimeType = null;
        this.mimeObject = null;
        this.humanPresentableName = null;
        this.representationClass = null;
        this.mimeType = mimeType;
        try {
            this.representationClass = Class.forName("java.io.InputStream");
        } catch (ClassNotFoundException e) {
        }
        this.humanPresentableName = humanPresentableName;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public Class getRepresentationClass() {
        return this.representationClass;
    }

    public String getHumanPresentableName() {
        return this.humanPresentableName;
    }

    public void setHumanPresentableName(String humanPresentableName) {
        this.humanPresentableName = humanPresentableName;
    }

    public boolean equals(DataFlavor dataFlavor) {
        return isMimeTypeEqual(dataFlavor) && dataFlavor.getRepresentationClass() == this.representationClass;
    }

    public boolean isMimeTypeEqual(String mimeType) {
        try {
            if (this.mimeObject == null) {
                this.mimeObject = new MimeType(this.mimeType);
            }
            MimeType mt = new MimeType(mimeType);
            return this.mimeObject.match(mt);
        } catch (MimeTypeParseException e) {
            return this.mimeType.equalsIgnoreCase(mimeType);
        }
    }

    protected String normalizeMimeTypeParameter(String parameterName, String parameterValue) {
        return parameterValue;
    }

    protected String normalizeMimeType(String mimeType) {
        return mimeType;
    }
}
