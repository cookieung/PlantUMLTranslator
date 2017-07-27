package Model.sequence.frame;

import java.util.LinkedList;
import java.util.Map;

public class LoopFrame extends SequenceFrame{

	public LoopFrame(String name, String typeFrame, LinkedList<Map<String, String>> trueFrame,
			LinkedList<Map<String, String>> falseFrame) {
		super(name, typeFrame, trueFrame, falseFrame);
	}

}
