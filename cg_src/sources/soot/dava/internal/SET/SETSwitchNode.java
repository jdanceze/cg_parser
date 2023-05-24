package soot.dava.internal.SET;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import soot.Value;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.finders.SwitchNode;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/SET/SETSwitchNode.class */
public class SETSwitchNode extends SETDagNode {
    private List<SwitchNode> switchNodeList;
    private Value key;

    public SETSwitchNode(AugmentedStmt characterizingStmt, Value key, IterableSet body, List<SwitchNode> switchNodeList, IterableSet junkBody) {
        super(characterizingStmt, body);
        this.key = key;
        this.switchNodeList = switchNodeList;
        for (SwitchNode switchNode : switchNodeList) {
            add_SubBody(switchNode.get_Body());
        }
        add_SubBody(junkBody);
    }

    @Override // soot.dava.internal.SET.SETNode
    public IterableSet get_NaturalExits() {
        return new IterableSet();
    }

    @Override // soot.dava.internal.SET.SETNode
    public ASTNode emit_AST() {
        LinkedList<Object> indexList = new LinkedList<>();
        Map<Object, List<Object>> index2ASTBody = new HashMap<>();
        for (SwitchNode sn : this.switchNodeList) {
            Object lastIndex = sn.get_IndexSet().last();
            Iterator iit = sn.get_IndexSet().iterator();
            while (iit.hasNext()) {
                Object index = iit.next();
                indexList.addLast(index);
                if (index != lastIndex) {
                    index2ASTBody.put(index, null);
                } else {
                    index2ASTBody.put(index, emit_ASTBody(get_Body2ChildChain().get(sn.get_Body())));
                }
            }
        }
        return new ASTSwitchNode(get_Label(), this.key, indexList, index2ASTBody);
    }

    @Override // soot.dava.internal.SET.SETDagNode, soot.dava.internal.SET.SETNode
    public AugmentedStmt get_EntryStmt() {
        return get_CharacterizingStmt();
    }
}
