package Model.proc;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class FrameProcess {
	
	private LinkedList<Map<String, String>> atomicProcess;
	private String name = "";

	
	public FrameProcess(String name) {
		this.name = name;
		this.atomicProcess = new LinkedList<>();
	}
	
	
		
	public void addProcess(Map<String,String> m) {
		this.atomicProcess.add(m);
	}
	
	public LinkedList<Map<String, String>> getAtomicProcess(){
		return this.atomicProcess;
	}
	
	public String getName() { return this.name; }
	
	

}
