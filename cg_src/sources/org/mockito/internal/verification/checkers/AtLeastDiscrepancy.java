package org.mockito.internal.verification.checkers;

import org.mockito.internal.reporting.Discrepancy;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/verification/checkers/AtLeastDiscrepancy.class */
public class AtLeastDiscrepancy extends Discrepancy {
    public AtLeastDiscrepancy(int wantedCount, int actualCount) {
        super(wantedCount, actualCount);
    }

    @Override // org.mockito.internal.reporting.Discrepancy
    public String getPluralizedWantedCount() {
        return "*at least* " + super.getPluralizedWantedCount();
    }
}
