package polyglot.ast;

import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/ClassBody.class */
public interface ClassBody extends Term {
    List members();

    ClassBody members(List list);

    ClassBody addMember(ClassMember classMember);
}
