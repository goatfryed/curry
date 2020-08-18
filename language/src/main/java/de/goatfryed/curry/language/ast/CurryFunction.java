package de.goatfryed.curry.language.ast;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.DirectCallNode;

public class CurryFunction implements TruffleObject {

    private final String name;
    private DirectCallNode callNode;

    public CurryFunction(String name) {
        this.name = name;
    }

    public void define(CallTarget callTarget) {
        this.callNode = DirectCallNode.create(callTarget);
    }

    public Object execute(Object ...arguments) {
        assert this.callNode != null;
        return callNode.call(arguments);
    }

    public String getName() {
        return name;
    }
}
