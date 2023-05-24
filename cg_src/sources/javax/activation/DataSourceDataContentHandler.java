package javax.activation;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.OutputStream;
/* JADX INFO: Access modifiers changed from: package-private */
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:j2ee.jar:javax/activation/DataSourceDataContentHandler.class
 */
/* compiled from: DataHandler.java */
/* loaded from: gencallgraphv3.jar:javax.activation-api-1.2.0.jar:javax/activation/DataSourceDataContentHandler.class */
public class DataSourceDataContentHandler implements DataContentHandler {
    private DataSource ds;
    private DataFlavor[] transferFlavors = null;
    private DataContentHandler dch;

    public DataSourceDataContentHandler(DataContentHandler dch, DataSource ds) {
        this.ds = null;
        this.dch = null;
        this.ds = ds;
        this.dch = dch;
    }

    @Override // javax.activation.DataContentHandler
    public DataFlavor[] getTransferDataFlavors() {
        if (this.transferFlavors == null) {
            if (this.dch != null) {
                this.transferFlavors = this.dch.getTransferDataFlavors();
            } else {
                this.transferFlavors = new DataFlavor[1];
                this.transferFlavors[0] = new ActivationDataFlavor(this.ds.getContentType(), this.ds.getContentType());
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
            return ds.getInputStream();
        }
        throw new UnsupportedFlavorException(df);
    }

    @Override // javax.activation.DataContentHandler
    public Object getContent(DataSource ds) throws IOException {
        if (this.dch != null) {
            return this.dch.getContent(ds);
        }
        return ds.getInputStream();
    }

    @Override // javax.activation.DataContentHandler
    public void writeTo(Object obj, String mimeType, OutputStream os) throws IOException {
        if (this.dch != null) {
            this.dch.writeTo(obj, mimeType, os);
            return;
        }
        throw new UnsupportedDataTypeException("no DCH for content type " + this.ds.getContentType());
    }
}
