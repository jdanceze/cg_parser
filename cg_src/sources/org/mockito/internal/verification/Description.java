package org.mockito.internal.verification;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/Description.class */
public class Description implements VerificationMode {
    private final VerificationMode verification;
    private final String description;

    public Description(VerificationMode verification, String description) {
        this.verification = verification;
        this.description = description;
    }

    @Override // org.mockito.verification.VerificationMode
    public void verify(VerificationData data) {
        try {
            this.verification.verify(data);
        } catch (MockitoAssertionError e) {
            throw new MockitoAssertionError(e, this.description);
        } catch (AssertionError e2) {
            throw new MockitoAssertionError(e2, this.description);
        }
    }
}
