package de.goatfryed.curry.language.ast;

import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "assignment")
@NodeField(name="identifier", type = String.class)
public abstract class LocalReadNode extends ExpressionNode {

    public abstract String getIdentifier();

    @Specialization
    public Object read(VirtualFrame frame) {
        var slot = frame.getFrameDescriptor().findFrameSlot(getIdentifier());
        assert slot != null;

        try {
            return frame.getObject(slot);
        } catch (FrameSlotTypeException e) {
            throw new IllegalStateException(e);
        }
    }
}
