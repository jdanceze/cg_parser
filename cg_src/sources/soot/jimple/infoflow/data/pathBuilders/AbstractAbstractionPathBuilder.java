package soot.jimple.infoflow.data.pathBuilders;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/AbstractAbstractionPathBuilder.class */
public abstract class AbstractAbstractionPathBuilder implements IAbstractionPathBuilder {
    protected final InfoflowManager manager;
    protected final InfoflowConfiguration config;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected Set<IAbstractionPathBuilder.OnPathBuilderResultAvailable> resultAvailableHandlers = null;

    public AbstractAbstractionPathBuilder(InfoflowManager manager) {
        this.manager = manager;
        this.config = manager.getConfig();
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public void addResultAvailableHandler(IAbstractionPathBuilder.OnPathBuilderResultAvailable handler) {
        if (this.resultAvailableHandlers == null) {
            this.resultAvailableHandlers = new HashSet();
        }
        this.resultAvailableHandlers.add(handler);
    }
}
