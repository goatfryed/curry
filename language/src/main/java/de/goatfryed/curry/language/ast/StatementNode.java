package de.goatfryed.curry.language.ast;

import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.GenerateWrapper;
import com.oracle.truffle.api.instrumentation.InstrumentableNode;
import com.oracle.truffle.api.instrumentation.ProbeNode;
import com.oracle.truffle.api.instrumentation.Tag;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import de.goatfryed.curry.language.CurryLanguage;

import java.util.ArrayList;
import java.util.List;

@NodeInfo(language = CurryLanguage.ID, description = "The abstract base node for all SL statements")
@GenerateWrapper
public abstract class StatementNode extends Node implements InstrumentableNode {

    protected List<Class<? extends Tag>> tags = new ArrayList<>();

    @Override
    public boolean isInstrumentable() {
        return false;
    }

    @Override
    public WrapperNode createWrapper(ProbeNode probe) {
        return new StatementNodeWrapper(this, probe);
    }

    public abstract void executeVoid(VirtualFrame frame);

    @Override
    public boolean hasTag(Class<? extends Tag> tag) {
        return tags.contains(tag);
    }

    public List<Class<? extends Tag>> getTags() {
        return tags;
    }
}
