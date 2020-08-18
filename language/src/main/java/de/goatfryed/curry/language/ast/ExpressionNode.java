package de.goatfryed.curry.language.ast;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.GenerateWrapper;
import com.oracle.truffle.api.instrumentation.ProbeNode;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(description = "The abstract base node for all expressions")
@GenerateWrapper
public abstract class ExpressionNode extends StatementNode {
    @Override
    public final void executeVoid(VirtualFrame frame) {
        executeGeneric(frame);
    }

    public abstract Object executeGeneric(VirtualFrame frame);

    @Override
    public WrapperNode createWrapper(ProbeNode probeNode) {
        return new ExpressionNodeWrapper(this, probeNode);
    }
}
