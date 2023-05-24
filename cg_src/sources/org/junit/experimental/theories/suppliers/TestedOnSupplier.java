package org.junit.experimental.theories.suppliers;

import java.util.ArrayList;
import java.util.List;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/experimental/theories/suppliers/TestedOnSupplier.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/experimental/theories/suppliers/TestedOnSupplier.class */
public class TestedOnSupplier extends ParameterSupplier {
    @Override // org.junit.experimental.theories.ParameterSupplier
    public List<PotentialAssignment> getValueSources(ParameterSignature sig) {
        List<PotentialAssignment> list = new ArrayList<>();
        TestedOn testedOn = (TestedOn) sig.getAnnotation(TestedOn.class);
        int[] ints = testedOn.ints();
        for (int i : ints) {
            list.add(PotentialAssignment.forValue("ints", Integer.valueOf(i)));
        }
        return list;
    }
}
