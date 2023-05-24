package javax.resource.cci;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/cci/ResourceAdapterMetaData.class */
public interface ResourceAdapterMetaData {
    String getAdapterVersion();

    String getAdapterVendorName();

    String getAdapterName();

    String getAdapterShortDescription();

    String getSpecVersion();

    String[] getInteractionSpecsSupported();

    boolean supportsExecuteWithInputAndOutputRecord();

    boolean supportsExecuteWithInputRecordOnly();

    boolean supportsLocalTransactionDemarcation();
}
