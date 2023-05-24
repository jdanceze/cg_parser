package javax.activation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Locale;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:j2ee.jar:javax/activation/MimeType.class
 */
/* loaded from: gencallgraphv3.jar:javax.activation-api-1.2.0.jar:javax/activation/MimeType.class */
public class MimeType implements Externalizable {
    private String primaryType;
    private String subType;
    private MimeTypeParameterList parameters;
    private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";

    public MimeType() {
        this.primaryType = "application";
        this.subType = "*";
        this.parameters = new MimeTypeParameterList();
    }

    public MimeType(String rawdata) throws MimeTypeParseException {
        parse(rawdata);
    }

    public MimeType(String primary, String sub) throws MimeTypeParseException {
        if (isValidToken(primary)) {
            this.primaryType = primary.toLowerCase(Locale.ENGLISH);
            if (isValidToken(sub)) {
                this.subType = sub.toLowerCase(Locale.ENGLISH);
                this.parameters = new MimeTypeParameterList();
                return;
            }
            throw new MimeTypeParseException("Sub type is invalid.");
        }
        throw new MimeTypeParseException("Primary type is invalid.");
    }

    private void parse(String rawdata) throws MimeTypeParseException {
        int slashIndex = rawdata.indexOf(47);
        int semIndex = rawdata.indexOf(59);
        if (slashIndex < 0 && semIndex < 0) {
            throw new MimeTypeParseException("Unable to find a sub type.");
        }
        if (slashIndex < 0 && semIndex >= 0) {
            throw new MimeTypeParseException("Unable to find a sub type.");
        }
        if (slashIndex >= 0 && semIndex < 0) {
            this.primaryType = rawdata.substring(0, slashIndex).trim().toLowerCase(Locale.ENGLISH);
            this.subType = rawdata.substring(slashIndex + 1).trim().toLowerCase(Locale.ENGLISH);
            this.parameters = new MimeTypeParameterList();
        } else if (slashIndex < semIndex) {
            this.primaryType = rawdata.substring(0, slashIndex).trim().toLowerCase(Locale.ENGLISH);
            this.subType = rawdata.substring(slashIndex + 1, semIndex).trim().toLowerCase(Locale.ENGLISH);
            this.parameters = new MimeTypeParameterList(rawdata.substring(semIndex));
        } else {
            throw new MimeTypeParseException("Unable to find a sub type.");
        }
        if (!isValidToken(this.primaryType)) {
            throw new MimeTypeParseException("Primary type is invalid.");
        }
        if (!isValidToken(this.subType)) {
            throw new MimeTypeParseException("Sub type is invalid.");
        }
    }

    public String getPrimaryType() {
        return this.primaryType;
    }

    public void setPrimaryType(String primary) throws MimeTypeParseException {
        if (!isValidToken(this.primaryType)) {
            throw new MimeTypeParseException("Primary type is invalid.");
        }
        this.primaryType = primary.toLowerCase(Locale.ENGLISH);
    }

    public String getSubType() {
        return this.subType;
    }

    public void setSubType(String sub) throws MimeTypeParseException {
        if (!isValidToken(this.subType)) {
            throw new MimeTypeParseException("Sub type is invalid.");
        }
        this.subType = sub.toLowerCase(Locale.ENGLISH);
    }

    public MimeTypeParameterList getParameters() {
        return this.parameters;
    }

    public String getParameter(String name) {
        return this.parameters.get(name);
    }

    public void setParameter(String name, String value) {
        this.parameters.set(name, value);
    }

    public void removeParameter(String name) {
        this.parameters.remove(name);
    }

    public String toString() {
        return getBaseType() + this.parameters.toString();
    }

    public String getBaseType() {
        return this.primaryType + "/" + this.subType;
    }

    public boolean match(MimeType type) {
        return this.primaryType.equals(type.getPrimaryType()) && (this.subType.equals("*") || type.getSubType().equals("*") || this.subType.equals(type.getSubType()));
    }

    public boolean match(String rawdata) throws MimeTypeParseException {
        return match(new MimeType(rawdata));
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(toString());
        out.flush();
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        try {
            parse(in.readUTF());
        } catch (MimeTypeParseException e) {
            throw new IOException(e.toString());
        }
    }

    private static boolean isTokenChar(char c) {
        return c > ' ' && c < 127 && "()<>@,;:/[]?=\\\"".indexOf(c) < 0;
    }

    private boolean isValidToken(String s) {
        int len = s.length();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                char c = s.charAt(i);
                if (!isTokenChar(c)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
