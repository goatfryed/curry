package de.goatfryed.curry.language.ast;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "invoke")
public class FunctionInvocationNode extends ExpressionNode {

    private final CurryFunction function;

    public FunctionInvocationNode(CurryFunction function) {
        this.function = function;
    }

    @Override
    public String executeGeneric(VirtualFrame frame) {
        var val = function.execute();
        return (String) val;
    }
}
