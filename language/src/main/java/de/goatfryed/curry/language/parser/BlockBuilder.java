package de.goatfryed.curry.language.parser;

import de.goatfryed.curry.language.ast.CurryBlockNode;
import de.goatfryed.curry.language.ast.StatementNode;

import java.util.ArrayList;
import java.util.List;

public class BlockBuilder {

    private final List<StatementNode> statements = new ArrayList<>();

    public void add(StatementNode node) {
        statements.add(node);
    }

    public CurryBlockNode build() {
        return new CurryBlockNode(statements.toArray(StatementNode[]::new));
    }
}
