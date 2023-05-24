package soot.jimple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.jimple.validation.FieldRefValidator;
import soot.jimple.validation.IdentityStatementsValidator;
import soot.jimple.validation.IdentityValidator;
import soot.jimple.validation.InvokeArgumentValidator;
import soot.jimple.validation.JimpleTrapValidator;
import soot.jimple.validation.MethodValidator;
import soot.jimple.validation.NewValidator;
import soot.jimple.validation.ReturnStatementsValidator;
import soot.jimple.validation.TypesValidator;
import soot.options.Options;
import soot.util.Chain;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;
/* loaded from: gencallgraphv3.jar:soot/jimple/JimpleBody.class */
public class JimpleBody extends StmtBody {
    private static final Logger logger = LoggerFactory.getLogger(JimpleBody.class);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/JimpleBody$LazyValidatorsSingleton.class */
    public static class LazyValidatorsSingleton {
        static final BodyValidator[] V = {IdentityStatementsValidator.v(), TypesValidator.v(), ReturnStatementsValidator.v(), InvokeArgumentValidator.v(), FieldRefValidator.v(), NewValidator.v(), JimpleTrapValidator.v(), IdentityValidator.v(), MethodValidator.v()};

        private LazyValidatorsSingleton() {
        }
    }

    public JimpleBody(SootMethod m) {
        super(m);
        if (Options.v().verbose()) {
            logger.debug("[" + getMethod().getName() + "] Constructing JimpleBody...");
        }
    }

    public JimpleBody() {
    }

    @Override // soot.Body
    public Object clone() {
        Body b = new JimpleBody(getMethodUnsafe());
        b.importBodyContentsFrom(this);
        return b;
    }

    @Override // soot.Body
    public Object clone(boolean noLocalsClone) {
        Body b = new JimpleBody(getMethod());
        b.importBodyContentsFrom(this, true);
        return b;
    }

    @Override // soot.Body
    public void validate() {
        List<ValidationException> exceptionList = new ArrayList<>();
        validate(exceptionList);
        if (!exceptionList.isEmpty()) {
            throw exceptionList.get(0);
        }
    }

    @Override // soot.Body
    public void validate(List<ValidationException> exceptionList) {
        BodyValidator[] bodyValidatorArr;
        super.validate(exceptionList);
        boolean runAllValidators = Options.v().debug() || Options.v().validate();
        for (BodyValidator validator : LazyValidatorsSingleton.V) {
            if (runAllValidators || validator.isBasicValidator()) {
                validator.validate((Body) this, exceptionList);
            }
        }
    }

    public void validateIdentityStatements() {
        runValidation(IdentityStatementsValidator.v());
    }

    public void insertIdentityStmts() {
        insertIdentityStmts(getMethod().getDeclaringClass());
    }

    public void insertIdentityStmts(SootClass declaringClass) {
        Jimple jimple = Jimple.v();
        PatchingChain<Unit> unitChain = getUnits();
        Chain<Local> localChain = getLocals();
        Unit lastUnit = null;
        if (!getMethod().isStatic()) {
            if (declaringClass == null) {
                throw new IllegalArgumentException(String.format("No declaring class given for method %s", this.method.getSubSignature()));
            }
            Local l = jimple.newLocal("this", RefType.v(declaringClass));
            Unit s = jimple.newIdentityStmt(l, jimple.newThisRef((RefType) l.getType()));
            localChain.add(l);
            unitChain.addFirst((PatchingChain<Unit>) s);
            lastUnit = s;
        }
        int i = 0;
        for (Type t : getMethod().getParameterTypes()) {
            Local l2 = jimple.newLocal("parameter" + i, t);
            Unit s2 = jimple.newIdentityStmt(l2, jimple.newParameterRef(l2.getType(), i));
            localChain.add(l2);
            if (lastUnit == null) {
                unitChain.addFirst((PatchingChain<Unit>) s2);
            } else {
                unitChain.insertAfter(s2, lastUnit);
            }
            lastUnit = s2;
            i++;
        }
    }

    public Stmt getFirstNonIdentityStmt() {
        Unit r = null;
        Iterator<Unit> it = getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            r = u;
            if (!(r instanceof IdentityStmt)) {
                break;
            }
        }
        if (r == null) {
            throw new RuntimeException("no non-id statements!");
        }
        return (Stmt) r;
    }
}
