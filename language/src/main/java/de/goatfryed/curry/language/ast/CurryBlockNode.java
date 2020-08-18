package de.goatfryed.curry.language.ast;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.BlockNode;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import de.goatfryed.curry.language.runtime.NullObject;

@NodeInfo(shortName = "block", description = "A source code block")
public class CurryBlockNode extends ExpressionNode implements BlockNode.ElementExecutor<StatementNode> {

    @Node.Child private BlockNode<StatementNode> block;

    public CurryBlockNode(StatementNode[] statements) {
        this.block = statements.length > 0 ? BlockNode.create(statements, this) : null;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame, StatementNode node, int index, int argument) {
        if (node instanceof ExpressionNode) {
            return ((ExpressionNode) node).executeGeneric(frame);
        }
        node.executeVoid(frame);
        return NullObject.SINGLETON;
    }

    @Override
    public void executeVoid(VirtualFrame frame, StatementNode node, int index, int argument) {
        node.executeVoid(frame);
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        if (this.block == null) {
            return NullObject.SINGLETON;
        }
        return this.block.executeGeneric(frame, BlockNode.NO_ARGUMENT);
    }
}
