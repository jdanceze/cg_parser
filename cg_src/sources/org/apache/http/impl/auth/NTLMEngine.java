package org.apache.http.impl.auth;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/auth/NTLMEngine.class */
public interface NTLMEngine {
    String generateType1Msg(String str, String str2) throws NTLMEngineException;

    String generateType3Msg(String str, String str2, String str3, String str4, String str5) throws NTLMEngineException;
}
