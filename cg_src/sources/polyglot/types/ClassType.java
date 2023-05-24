package polyglot.types;

import java.util.List;
import polyglot.util.Enum;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/ClassType.class */
public interface ClassType extends Importable, ReferenceType, MemberInstance {
    public static final Kind TOP_LEVEL = new Kind("top-level");
    public static final Kind MEMBER = new Kind("member");
    public static final Kind LOCAL = new Kind("local");
    public static final Kind ANONYMOUS = new Kind("anonymous");

    Kind kind();

    boolean isTopLevel();

    boolean isInner();

    boolean isNested();

    boolean isInnerClass();

    boolean isMember();

    boolean isLocal();

    boolean isAnonymous();

    boolean inStaticContext();

    List constructors();

    List memberClasses();

    ClassType memberClassNamed(String str);

    @Override // polyglot.types.ReferenceType
    FieldInstance fieldNamed(String str);

    boolean isEnclosed(ClassType classType);

    boolean isEnclosedImpl(ClassType classType);

    boolean hasEnclosingInstance(ClassType classType);

    boolean hasEnclosingInstanceImpl(ClassType classType);

    ClassType outer();

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/types/ClassType$Kind.class */
    public static class Kind extends Enum {
        public Kind(String name) {
            super(name);
        }
    }
}
