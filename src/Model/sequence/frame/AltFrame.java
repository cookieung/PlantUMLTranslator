package Model.sequence.frame;

import java.util.LinkedList;
import java.util.Map;

public class AltFrame extends SequenceFrame{

	public AltFrame(String name, String typeFrame, LinkedList<Map<String, LinkedList<Map<String, String>>>> trueFrame,
			LinkedList<Map<String, LinkedList<Map<String, String>>>> falseFrame) {
		super(name, typeFrame, trueFrame, falseFrame);
		// TODO Auto-generated constructor stub
	}

	public AltFrame(String m,LinkedList<Map<String, LinkedList<String>>> result) {
		super(name, trueFrame);
	}



}
