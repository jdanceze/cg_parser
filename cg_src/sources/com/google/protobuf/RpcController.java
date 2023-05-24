package com.google.protobuf;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/RpcController.class */
public interface RpcController {
    void reset();

    boolean failed();

    String errorText();

    void startCancel();

    void setFailed(String str);

    boolean isCanceled();

    void notifyOnCancel(RpcCallback<Object> rpcCallback);
}
