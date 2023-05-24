package com.sun.xml.bind.v2.runtime;

import com.sun.xml.bind.v2.model.annotation.Locatable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.JAXBException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/IllegalAnnotationException.class */
public class IllegalAnnotationException extends JAXBException {
    private final List<List<Location>> pos;
    private static final long serialVersionUID = 1;

    public IllegalAnnotationException(String message, Locatable src) {
        super(message);
        this.pos = build(src);
    }

    public IllegalAnnotationException(String message, Annotation src) {
        this(message, cast(src));
    }

    public IllegalAnnotationException(String message, Locatable src1, Locatable src2) {
        super(message);
        this.pos = build(src1, src2);
    }

    public IllegalAnnotationException(String message, Annotation src1, Annotation src2) {
        this(message, cast(src1), cast(src2));
    }

    public IllegalAnnotationException(String message, Annotation src1, Locatable src2) {
        this(message, cast(src1), src2);
    }

    public IllegalAnnotationException(String message, Throwable cause, Locatable src) {
        super(message, cause);
        this.pos = build(src);
    }

    private static Locatable cast(Annotation a) {
        if (a instanceof Locatable) {
            return (Locatable) a;
        }
        return null;
    }

    private List<List<Location>> build(Locatable... srcs) {
        List<Location> ll;
        List<List<Location>> r = new ArrayList<>();
        for (Locatable l : srcs) {
            if (l != null && (ll = convert(l)) != null && !ll.isEmpty()) {
                r.add(ll);
            }
        }
        return Collections.unmodifiableList(r);
    }

    private List<Location> convert(Locatable src) {
        if (src == null) {
            return null;
        }
        List<Location> r = new ArrayList<>();
        while (src != null) {
            r.add(src.getLocation());
            src = src.getUpstream();
        }
        return Collections.unmodifiableList(r);
    }

    public List<List<Location>> getSourcePos() {
        return this.pos;
    }

    @Override // javax.xml.bind.JAXBException, java.lang.Throwable
    public String toString() {
        StringBuilder sb = new StringBuilder(getMessage());
        for (List<Location> locs : this.pos) {
            sb.append("\n\tthis problem is related to the following location:");
            for (Location loc : locs) {
                sb.append("\n\t\tat ").append(loc.toString());
            }
        }
        return sb.toString();
    }
}
