package Model.proc;

import java.util.LinkedHashMap;
import java.util.Map;

public class FrameProcess {
	
	Map<String, String> atomicProcess;
	String name = "";
	
	public FrameProcess(String name) {
		this.name = name;
		atomicProcess = new LinkedHashMap<>();
	}
	
	public FrameProcess(String name,Map<String, String> atomicProcess) {
		this.name = name;
		this.atomicProcess = atomicProcess;
	}
	
	
	public void addProcess(String key,String value) {
		this.atomicProcess.put(key, value);
	}
	
	public Map<String, String> getAtomicProcess(){
		return this.atomicProcess;
	}
	
	public String getName() { return this.name; }
	
	

}
