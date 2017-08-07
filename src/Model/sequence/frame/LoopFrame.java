package Model.sequence.frame;

import java.util.LinkedList;
import java.util.Map;

public class LoopFrame extends SequenceFrame{

	public LoopFrame(String name, String typeFrame, LinkedList<Map<String, LinkedList<Map<String, String>>>> trueFrame,
			LinkedList<Map<String, LinkedList<Map<String, String>>>> falseFrame) {
		super(name, "loop", trueFrame, falseFrame);
		this.name = name+"_loop";
		this.typeFrame = "loop";
	}

	public LoopFrame(String m,LinkedList<Map<String, LinkedList<String>>> result) {
		super(name, trueFrame);
	}
	
	@Override
	public String getTypeFrame() {
		return "loop";
	}
	
	@Override
	public String toString(){
		String s ="###"+this.typeFrame;
		return s+processFrame;
	}




}
