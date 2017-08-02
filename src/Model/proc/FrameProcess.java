package Model.proc;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class FrameProcess {
	
	private LinkedList<Map<String, String>> atomicProcess;
	private String name = "";

	
	public FrameProcess(String name) {
		this.name = name;
		this.atomicProcess = new LinkedList<>();
	}
	
	
		
	public void addFrameProcess(Map<String, Map<String, String>> forOpt) {
		for (Entry<String, Map<String, String>> map : forOpt.entrySet()) {
			System.out.println("FRAME PROCESS"+map);
			atomicProcess.add(map.getValue());
		}
	}



	public void addProcess(Map<String,String> m) {
		this.atomicProcess.add(m);
	}
	
	public LinkedList<Map<String, String>> getAtomicProcess(){
		return this.atomicProcess;
	}
	
	public String getName() { return this.name; }
	
	

}
