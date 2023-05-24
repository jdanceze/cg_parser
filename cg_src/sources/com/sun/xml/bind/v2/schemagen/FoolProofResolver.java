package com.sun.xml.bind.v2.schemagen;

import java.io.IOException;
import java.util.logging.Logger;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/schemagen/FoolProofResolver.class */
final class FoolProofResolver extends SchemaOutputResolver {
    private static final Logger logger;
    private final SchemaOutputResolver resolver;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !FoolProofResolver.class.desiredAssertionStatus();
        logger = com.sun.xml.bind.Util.getClassLogger();
    }

    public FoolProofResolver(SchemaOutputResolver resolver) {
        if (!$assertionsDisabled && resolver == null) {
            throw new AssertionError();
        }
        this.resolver = resolver;
    }

    @Override // javax.xml.bind.SchemaOutputResolver
    public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
        logger.entering(getClass().getName(), "createOutput", new Object[]{namespaceUri, suggestedFileName});
        Result r = this.resolver.createOutput(namespaceUri, suggestedFileName);
        if (r != null) {
            String sysId = r.getSystemId();
            logger.finer("system ID = " + sysId);
            if (sysId == null) {
                throw new AssertionError("system ID cannot be null");
            }
        }
        logger.exiting(getClass().getName(), "createOutput", r);
        return r;
    }
}
