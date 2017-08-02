package Model.proc;

import java.util.LinkedList;
import java.util.Map;

public class FrameProcessMap{
	
	private LinkedList<FrameProcess> frameProcesslist;
	private String name="";
	
	
	public FrameProcessMap() {
		this.frameProcesslist = new LinkedList<>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
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

	public void addFrameProcess(Map<String, Map<String, String>> forOpt) {
		FrameProcess ff= new FrameProcess(forOpt.keySet().toArray()[0]+"");
		ff.addFrameProcess(forOpt);
		frameProcesslist.add(ff);
		
	}




}