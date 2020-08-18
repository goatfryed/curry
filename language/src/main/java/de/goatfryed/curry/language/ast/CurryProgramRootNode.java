package de.goatfryed.curry.language.ast;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import de.goatfryed.curry.language.CurryLanguage;


public class CurryProgramRootNode extends RootNode {

    @Child private ExpressionNode expression;

    public CurryProgramRootNode(CurryLanguage language, ExpressionNode statement) {
        super(language);
        this.expression = statement;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        assert lookupContextReference(CurryLanguage.class).get() != null;

        return expression.executeGeneric(frame);
    }
}
