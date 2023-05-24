package org.apache.tools.ant.types.selectors;

import java.util.Enumeration;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/SelectorContainer.class */
public interface SelectorContainer {
    boolean hasSelectors();

    int selectorCount();

    FileSelector[] getSelectors(Project project);

    Enumeration<FileSelector> selectorElements();

    void appendSelector(FileSelector fileSelector);

    void addSelector(SelectSelector selectSelector);

    void addAnd(AndSelector andSelector);

    void addOr(OrSelector orSelector);

    void addNot(NotSelector notSelector);

    void addNone(NoneSelector noneSelector);

    void addMajority(MajoritySelector majoritySelector);

    void addDate(DateSelector dateSelector);

    void addSize(SizeSelector sizeSelector);

    void addFilename(FilenameSelector filenameSelector);

    void addCustom(ExtendSelector extendSelector);

    void addContains(ContainsSelector containsSelector);

    void addPresent(PresentSelector presentSelector);

    void addDepth(DepthSelector depthSelector);

    void addDepend(DependSelector dependSelector);

    void addContainsRegexp(ContainsRegexpSelector containsRegexpSelector);

    void addType(TypeSelector typeSelector);

    void addDifferent(DifferentSelector differentSelector);

    void addModified(ModifiedSelector modifiedSelector);

    void add(FileSelector fileSelector);
}
