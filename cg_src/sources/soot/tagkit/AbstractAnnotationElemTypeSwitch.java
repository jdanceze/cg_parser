package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AbstractAnnotationElemTypeSwitch.class */
public abstract class AbstractAnnotationElemTypeSwitch<T> implements IAnnotationElemTypeSwitch {
    T result;

    @Override // soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationAnnotationElem(AnnotationAnnotationElem v) {
        defaultCase(v);
    }

    @Override // soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationArrayElem(AnnotationArrayElem v) {
        defaultCase(v);
    }

    @Override // soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationBooleanElem(AnnotationBooleanElem v) {
        defaultCase(v);
    }

    @Override // soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationClassElem(AnnotationClassElem v) {
        defaultCase(v);
    }

    @Override // soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationDoubleElem(AnnotationDoubleElem v) {
        defaultCase(v);
    }

    @Override // soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationEnumElem(AnnotationEnumElem v) {
        defaultCase(v);
    }

    @Override // soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationFloatElem(AnnotationFloatElem v) {
        defaultCase(v);
    }

    @Override // soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationIntElem(AnnotationIntElem v) {
        defaultCase(v);
    }

    @Override // soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationLongElem(AnnotationLongElem v) {
        defaultCase(v);
    }

    @Override // soot.tagkit.IAnnotationElemTypeSwitch
    public void caseAnnotationStringElem(AnnotationStringElem v) {
        defaultCase(v);
    }

    @Override // soot.tagkit.IAnnotationElemTypeSwitch
    public void defaultCase(Object obj) {
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return this.result;
    }
}
