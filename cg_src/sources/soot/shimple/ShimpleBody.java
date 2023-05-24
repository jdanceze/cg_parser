package soot.shimple;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.SootMethod;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.StmtBody;
import soot.options.Options;
import soot.options.ShimpleOptions;
import soot.shimple.internal.SPatchingChain;
import soot.shimple.internal.ShimpleBodyBuilder;
import soot.util.HashChain;
/* loaded from: gencallgraphv3.jar:soot/shimple/ShimpleBody.class */
public class ShimpleBody extends StmtBody {
    private static final Logger logger = LoggerFactory.getLogger(ShimpleBody.class);
    protected ShimpleOptions options;
    protected ShimpleBodyBuilder sbb;
    protected boolean isSSA;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShimpleBody(SootMethod m, Map<String, String> options) {
        super(m);
        this.isSSA = false;
        this.options = new ShimpleOptions(options);
        setSSA(true);
        this.unitChain = new SPatchingChain(this, new HashChain());
        this.sbb = new ShimpleBodyBuilder(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShimpleBody(Body body, Map<String, String> options) {
        super(body.getMethod());
        this.isSSA = false;
        if (!(body instanceof JimpleBody) && !(body instanceof ShimpleBody)) {
            throw new RuntimeException("Cannot construct ShimpleBody from given Body type: " + body.getClass());
        }
        if (Options.v().verbose()) {
            logger.debug("[" + getMethod().getName() + "] Constructing ShimpleBody...");
        }
        this.options = new ShimpleOptions(options);
        this.unitChain = new SPatchingChain(this, new HashChain());
        importBodyContentsFrom(body);
        this.sbb = new ShimpleBodyBuilder(this);
        rebuild(body instanceof ShimpleBody);
    }

    public void rebuild() {
        rebuild(true);
    }

    public void rebuild(boolean hasPhiNodes) {
        this.sbb.transform();
        setSSA(true);
    }

    public JimpleBody toJimpleBody() {
        ShimpleBody sBody = (ShimpleBody) clone();
        sBody.eliminateNodes();
        JimpleBody jBody = Jimple.v().newBody(sBody.getMethod());
        jBody.importBodyContentsFrom(sBody);
        return jBody;
    }

    public void eliminatePhiNodes() {
        this.sbb.preElimOpt();
        this.sbb.eliminatePhiNodes();
        this.sbb.postElimOpt();
        setSSA(false);
    }

    public void eliminatePiNodes() {
        this.sbb.eliminatePiNodes();
    }

    public void eliminateNodes() {
        this.sbb.preElimOpt();
        this.sbb.eliminatePhiNodes();
        if (this.options.extended()) {
            this.sbb.eliminatePiNodes();
        }
        this.sbb.postElimOpt();
        setSSA(false);
    }

    @Override // soot.Body
    public Object clone() {
        Body b = Shimple.v().newBody(getMethodUnsafe());
        b.importBodyContentsFrom(this);
        return b;
    }

    @Override // soot.Body
    public Object clone(boolean noLocalsClone) {
        Body b = Shimple.v().newBody(getMethod());
        b.importBodyContentsFrom(this, true);
        return b;
    }

    public void setSSA(boolean isSSA) {
        this.isSSA = isSSA;
    }

    public boolean isSSA() {
        return this.isSSA;
    }

    public boolean isExtendedSSA() {
        return this.options.extended();
    }

    public ShimpleOptions getOptions() {
        return this.options;
    }

    public void makeUniqueLocalNames() {
        this.sbb.makeUniqueLocalNames();
    }
}
