package Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SequenceDiagram implements Diagram {
	
	private String name;
	private ProcessList processes;

	public SequenceDiagram(String name){
		this.name = name;
		this.processes = new SequenceProcess();
	}

	@Override
	public void addProcess(ProcessList process) {
		this.processes = process;
	}
	
	public Set<Diagram> getAllTempStateDiagram(){
		SequenceProcess s = (SequenceProcess)processes;
		return s.getAllTempStateDiagram();
	}

	@Override
	public ProcessList getProcesses() {
		return processes;
	}

	public void setProcesses(ProcessList processes) {
		this.processes = processes;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public String toString(){
		return name+" = "+processes.toString()+"\n";
	}
	

	

}