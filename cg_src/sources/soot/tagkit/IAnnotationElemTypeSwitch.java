package soot.tagkit;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/tagkit/IAnnotationElemTypeSwitch.class */
public interface IAnnotationElemTypeSwitch extends Switch {
    void caseAnnotationAnnotationElem(AnnotationAnnotationElem annotationAnnotationElem);

    void caseAnnotationArrayElem(AnnotationArrayElem annotationArrayElem);

    void caseAnnotationBooleanElem(AnnotationBooleanElem annotationBooleanElem);

    void caseAnnotationClassElem(AnnotationClassElem annotationClassElem);

    void caseAnnotationDoubleElem(AnnotationDoubleElem annotationDoubleElem);

    void caseAnnotationEnumElem(AnnotationEnumElem annotationEnumElem);

    void caseAnnotationFloatElem(AnnotationFloatElem annotationFloatElem);

    void caseAnnotationIntElem(AnnotationIntElem annotationIntElem);

    void caseAnnotationLongElem(AnnotationLongElem annotationLongElem);

    void caseAnnotationStringElem(AnnotationStringElem annotationStringElem);

    void defaultCase(Object obj);
}
