package Model.sequence.frame;

import java.util.LinkedList;
import java.util.Map;

public class LoopFrame extends SequenceFrame{

	public LoopFrame(String name, String typeFrame, LinkedList<Map<String, LinkedList<Map<String, String>>>> trueFrame,
			LinkedList<Map<String, LinkedList<Map<String, String>>>> falseFrame) {
		super(name, typeFrame, trueFrame, falseFrame);
		// TODO Auto-generated constructor stub
	}

	public LoopFrame(String m,LinkedList<Map<String, LinkedList<String>>> result) {
		super(name, trueFrame);
	}




}
