package de.goatfryed.curry.language.ast;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "literal")
public class StringLiteralNode extends ExpressionNode {

    private final String value;

    public StringLiteralNode(String value) {
        this.value = value;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return value;
    }
}
