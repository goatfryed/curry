package de.goatfryed.curry.language.ast;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.TruffleException;
import com.oracle.truffle.api.nodes.Node;

public class CurryException extends RuntimeException implements TruffleException {
    private final Node node;

    @CompilerDirectives.TruffleBoundary
    public CurryException(String type_error, Node node) {
        super(type_error);
        this.node = node;
    }

    @Override
    public Node getLocation() {
        return node;
    }
}
