package de.goatfryed.curry.language.ast;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.CachedContext;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.GenerateWrapper;
import com.oracle.truffle.api.instrumentation.ProbeNode;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.NodeInfo;
import de.goatfryed.curry.language.CurryContext;
import de.goatfryed.curry.language.CurryLanguage;
import de.goatfryed.curry.language.runtime.NullObject;

@NodeInfo(language = CurryLanguage.ID, shortName = "hello")
@GenerateNodeFactory
@GenerateWrapper
public abstract class HelloWorldNode extends ExpressionNode {

    @Specialization
    @CompilerDirectives.TruffleBoundary
    public Object hello(
        @CachedLibrary(limit = "3") InteropLibrary interop,
        @CachedContext(CurryLanguage.class) CurryContext context
    ) {
        context.getOutput().println(interop.toDisplayString("Hello World!"));
        return NullObject.SINGLETON;
    }

    @Override
    public boolean isInstrumentable() {
        return true;
    }

    @Override public WrapperNode createWrapper(ProbeNode probeNode) {
        return new HelloWorldNodeWrapper(this, probeNode);
    }

    @Override
    public final Object executeGeneric(VirtualFrame frame) {
        try {
            return execute(frame);
        } catch (UnsupportedSpecializationException e) {
            throw new CurryException("Type error", e.getNode());
        }
    }

    public abstract Object execute(VirtualFrame frame);
}
