package de.goatfryed.curry.language.ast;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeChild("left")
@NodeChild("right")
@NodeInfo(shortName = "+")
public abstract class AdditionNode extends ExpressionNode {

    @Specialization
    protected String add(
        String left,
        String right
    ) {
        return left + right;
    }
}
