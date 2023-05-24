package soot.jimple.infoflow.entryPointCreators;

import java.util.Collection;
import java.util.List;
import soot.SootField;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/entryPointCreators/IEntryPointCreator.class */
public interface IEntryPointCreator {
    SootMethod createDummyMain();

    void setSubstituteCallParams(boolean z);

    void setSubstituteClasses(List<String> list);

    Collection<String> getRequiredClasses();

    Collection<SootMethod> getAdditionalMethods();

    Collection<SootField> getAdditionalFields();

    SootMethod getGeneratedMainMethod();
}
