package org.jf.dexlib2.base;

import java.util.Iterator;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.StringEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/BaseMethodParameter.class */
public abstract class BaseMethodParameter extends BaseTypeReference implements MethodParameter {
    @Override // org.jf.dexlib2.iface.MethodParameter, org.jf.dexlib2.iface.debug.LocalInfo
    @Nullable
    public String getSignature() {
        Annotation signatureAnnotation = null;
        Iterator<? extends Annotation> it = getAnnotations().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Annotation annotation = it.next();
            if (annotation.getType().equals("Ldalvik/annotation/Signature;")) {
                signatureAnnotation = annotation;
                break;
            }
        }
        if (signatureAnnotation == null) {
            return null;
        }
        ArrayEncodedValue signatureValues = null;
        Iterator<? extends AnnotationElement> it2 = signatureAnnotation.getElements().iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            AnnotationElement annotationElement = it2.next();
            if (annotationElement.getName().equals("value")) {
                EncodedValue encodedValue = annotationElement.getValue();
                if (encodedValue.getValueType() != 28) {
                    return null;
                }
                signatureValues = (ArrayEncodedValue) encodedValue;
            }
        }
        if (signatureValues == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (EncodedValue signatureValue : signatureValues.getValue()) {
            if (signatureValue.getValueType() != 23) {
                return null;
            }
            sb.append(((StringEncodedValue) signatureValue).getValue());
        }
        return sb.toString();
    }
}
