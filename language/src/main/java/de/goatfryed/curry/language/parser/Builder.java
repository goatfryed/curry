package de.goatfryed.curry.language.parser;

import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.instrumentation.StandardTags;
import de.goatfryed.curry.language.CurryLanguage;
import de.goatfryed.curry.language.ast.*;
import de.goatfryed.curry.language.ast.CurryFunction;
import org.antlr.v4.runtime.Token;

import java.util.HashMap;
import java.util.Map;

public class Builder {

    private final CurryLanguage language;

    private final Map<String, CurryFunction> functionMap = new HashMap<>();

    public Builder(CurryLanguage language) {

        this.language = language;
    }

    public BlockBuilder startBlock() {
        return new BlockBuilder();
    }

    public ExpressionNode createStringLiteral(Token literalToken) {
        /* Remove the trailing and ending " */
        String literal = literalToken.getText();

        assert literal.length() >= 2 && literal.startsWith("\"") && literal.endsWith("\"");
        literal = literal.substring(1, literal.length() - 1);

        final StringLiteralNode result = new StringLiteralNode(literal.intern());
        result.getTags().add(StandardTags.ExpressionTag.class);
        return result;
    }

    public LocalAssignmentNode createAssignment(Token variable, ExpressionNode value) {
        return LocalAssignmentNodeGen.create(value, variable.getText());
    }

    public ExpressionNode createRead(Token identifier) {
        return LocalReadNodeGen.create(identifier.getText());
    }

    public CurryFunction createFunction(Token identifier, CurryBlockNode body) {
        var name = identifier.getText();

        var rootNode = new CurryRootNode(language, body, name);
        var callTarget = Truffle.getRuntime().createCallTarget(rootNode);

        var curryFunction = getOrCreateFunction(name);
        curryFunction.define(callTarget);

        return curryFunction;

    }

    public FunctionInvocationNode createInvocation(Token identifier) {
        var name = identifier.getText();
        var function = getOrCreateFunction(name);

        return new FunctionInvocationNode(function);
    }

    private CurryFunction getOrCreateFunction(String name) {
        return functionMap.computeIfAbsent(
            name,
            CurryFunction::new
        );
    }
}
