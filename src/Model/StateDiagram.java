package Model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class StateDiagram implements Diagram {
	
	private String name;
	private LinkedList<Map<String, LinkedList<String>>> processes;

	
	public StateDiagram(String name){
		this.name = name;
		this.processes = new LinkedList<>();
	}
	
	public LinkedList<Map<String,LinkedList<String>>> addProcess(LinkedList<Map<String,LinkedList<String>>> process) {
		this.processes = process;
		return this.processes;
	}

	@Override
	public LinkedList<Map<String, LinkedList<String>>> getProcesses() {
		return processes;
	}

	public void setProcesses(LinkedList<Map<String, LinkedList<String>>> processes) {
		this.processes = processes;
	}



	@Override
	public String getName() {
		return this.name;
	}
	
	
	

}
