package javax.activation;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
/* JADX INFO: Access modifiers changed from: package-private */
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:j2ee.jar:javax/activation/ObjectDataContentHandler.class
 */
/* compiled from: DataHandler.java */
/* loaded from: gencallgraphv3.jar:javax.activation-api-1.2.0.jar:javax/activation/ObjectDataContentHandler.class */
public class ObjectDataContentHandler implements DataContentHandler {
    private DataFlavor[] transferFlavors = null;
    private Object obj;
    private String mimeType;
    private DataContentHandler dch;

    public ObjectDataContentHandler(DataContentHandler dch, Object obj, String mimeType) {
        this.dch = null;
        this.obj = obj;
        this.mimeType = mimeType;
        this.dch = dch;
    }

    public DataContentHandler getDCH() {
        return this.dch;
    }

    @Override // javax.activation.DataContentHandler
    public synchronized DataFlavor[] getTransferDataFlavors() {
        if (this.transferFlavors == null) {
            if (this.dch != null) {
                this.transferFlavors = this.dch.getTransferDataFlavors();
            } else {
                this.transferFlavors = new DataFlavor[1];
                this.transferFlavors[0] = new ActivationDataFlavor(this.obj.getClass(), this.mimeType, this.mimeType);
            }
        }
        return this.transferFlavors;
    }

    @Override // javax.activation.DataContentHandler
    public Object getTransferData(DataFlavor df, DataSource ds) throws UnsupportedFlavorException, IOException {
        if (this.dch != null) {
            return this.dch.getTransferData(df, ds);
        }
        if (df.equals(getTransferDataFlavors()[0])) {
            return this.obj;
        }
        throw new UnsupportedFlavorException(df);
    }

    @Override // javax.activation.DataContentHandler
    public Object getContent(DataSource ds) {
        return this.obj;
    }

    @Override // javax.activation.DataContentHandler
    public void writeTo(Object obj, String mimeType, OutputStream os) throws IOException {
        if (this.dch != null) {
            this.dch.writeTo(obj, mimeType, os);
        } else if (obj instanceof byte[]) {
            os.write((byte[]) obj);
        } else if (obj instanceof String) {
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write((String) obj);
            osw.flush();
        } else {
            throw new UnsupportedDataTypeException("no object DCH for MIME type " + this.mimeType);
        }
    }
}
