package polyglot.frontend;

import polyglot.frontend.Pass;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/EmptyPass.class */
public class EmptyPass extends AbstractPass {
    public EmptyPass(Pass.ID id) {
        super(id);
    }

    @Override // polyglot.frontend.AbstractPass, polyglot.frontend.Pass
    public boolean run() {
        return true;
    }
}
