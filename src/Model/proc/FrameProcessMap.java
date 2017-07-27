package Model.proc;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class FrameProcessMap {
	
	LinkedList<FrameProcess> frameProcesslist;
	String name="";
	
	
	public FrameProcessMap(String name) {
		this.name= name;
		this.frameProcesslist = new LinkedList<>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public LinkedList<FrameProcess> getFrameProcesslist() {
		return frameProcesslist;
	}

	public void setFrameProcesslist(LinkedList<FrameProcess> frameProcesslist) {
		this.frameProcesslist = frameProcesslist;
	}

	public void addFrameProcess(FrameProcess frameProcess) {
		frameProcesslist.add(frameProcess);
	}
	

}
