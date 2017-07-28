package Model.proc;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class FrameProcess {
	
	LinkedList<Map<String, String>> atomicProcess;
	String name = "";
	
	public FrameProcess(String name) {
		this.name = name;
		atomicProcess = new LinkedList<>();
	}
	
		
	public void addProcess(Map<String,String> m) {
		this.atomicProcess.add(m);
	}
	
	public LinkedList<Map<String, String>> getAtomicProcess(){
		return this.atomicProcess;
	}
	
	public String getName() { return this.name; }
	
	

}
