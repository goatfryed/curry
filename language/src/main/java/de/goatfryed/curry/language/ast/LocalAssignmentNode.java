package de.goatfryed.curry.language.ast;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.instrumentation.Tag;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "assignment")
@NodeChild(value = "value", type = ExpressionNode.class)
@NodeField(name="identifier", type = String.class)
public abstract class LocalAssignmentNode extends StatementNode {

    protected abstract String getIdentifier();

    @Specialization
    protected void write(VirtualFrame frame, String value) {
        var slot = frame.getFrameDescriptor().findOrAddFrameSlot(
            getIdentifier(), FrameSlotKind.Object
        );
        frame.getFrameDescriptor().setFrameSlotKind(slot, FrameSlotKind.Object);
        frame.setObject(slot, value);
    }

    @Override
    public boolean hasTag(Class<? extends Tag> tag) {
        return tag == StandardTags.WriteVariableTag.class || super.hasTag(tag);
    }
}
