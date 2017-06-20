package Model;

import java.util.LinkedList;

public class DiagramProcess {
	
	private String name;
	private LinkedList<String> processes;
	
	public DiagramProcess(String name){
		this.name = name;
		processes = new LinkedList<>();
	}
	
	public void addNewProcess(String process){
		processes.add(process);
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<String> getProcesses() {
		return processes;
	}

	public void setProcesses(LinkedList<String> processes) {
		this.processes = processes;
	}

}
