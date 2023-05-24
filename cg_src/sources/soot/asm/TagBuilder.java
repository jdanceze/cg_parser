package soot.asm;

import android.content.ContentResolver;
import java.lang.reflect.Field;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import soot.tagkit.AnnotationTag;
import soot.tagkit.GenericAttribute;
import soot.tagkit.Host;
import soot.tagkit.VisibilityAnnotationTag;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/asm/TagBuilder.class */
public final class TagBuilder {
    private VisibilityAnnotationTag invisibleTag;
    private VisibilityAnnotationTag visibleTag;
    private final Host host;
    private final SootClassBuilder scb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TagBuilder(Host host, SootClassBuilder scb) {
        this.host = host;
        this.scb = scb;
    }

    public AnnotationVisitor visitAnnotation(final String desc, boolean visible) {
        VisibilityAnnotationTag tag;
        if (visible) {
            tag = this.visibleTag;
            if (tag == null) {
                VisibilityAnnotationTag visibilityAnnotationTag = new VisibilityAnnotationTag(0);
                tag = visibilityAnnotationTag;
                this.visibleTag = visibilityAnnotationTag;
                this.host.addTag(tag);
            }
        } else {
            tag = this.invisibleTag;
            if (tag == null) {
                VisibilityAnnotationTag visibilityAnnotationTag2 = new VisibilityAnnotationTag(1);
                tag = visibilityAnnotationTag2;
                this.invisibleTag = visibilityAnnotationTag2;
                this.host.addTag(tag);
            }
        }
        this.scb.addDep(AsmUtil.toQualifiedName(desc.substring(1, desc.length() - 1)));
        final VisibilityAnnotationTag _tag = tag;
        return new AnnotationElemBuilder() { // from class: soot.asm.TagBuilder.1
            @Override // soot.asm.AnnotationElemBuilder, org.objectweb.asm.AnnotationVisitor
            public void visitEnd() {
                _tag.addAnnotation(new AnnotationTag(desc, this.elems));
            }
        };
    }

    public void visitAttribute(Attribute attr) {
        byte[] value = null;
        try {
            Field fld = Attribute.class.getDeclaredField(ContentResolver.SCHEME_CONTENT);
            fld.setAccessible(true);
            value = (byte[]) fld.get(attr);
        } catch (Exception e) {
        }
        this.host.addTag(new GenericAttribute(attr.type, value));
    }
}
