package Model.sequence.frame;

import java.util.LinkedList;
import java.util.Map;

public class AltFrame extends SequenceFrame{

	public AltFrame(String name, String typeFrame, LinkedList<Map<String, LinkedList<Map<String, String>>>> trueFrame,
			LinkedList<Map<String, LinkedList<Map<String, String>>>> falseFrame) {
		super(name, "alt", trueFrame, falseFrame);
		this.name = name+"_alt";
		this.typeFrame = "alt";
	}

	public AltFrame(String m,LinkedList<Map<String, LinkedList<String>>> result) {
		super(name, trueFrame);
	}
	
	@Override
	public String getTypeFrame() {
		return "alt";
	}
	
	@Override
	public String toString(){
		String s ="###"+this.typeFrame;
		return s+processFrame;
	}



}
