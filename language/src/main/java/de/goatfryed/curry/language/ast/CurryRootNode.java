package de.goatfryed.curry.language.ast;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.RootNode;
import de.goatfryed.curry.language.CurryLanguage;

@NodeInfo(language = CurryLanguage.ID, description = "The root of all Curry execution trees")
public class CurryRootNode extends RootNode {

    @Child private ExpressionNode body;

    private final String name;

    public CurryRootNode(
        TruffleLanguage<?> language,
        ExpressionNode body,
        String name
    ) {
        super(language);
        this.body = body;
        this.name = name;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        return body.executeGeneric(frame);
    }

    @Override
    public String getName() {
        return name;
    }
}
