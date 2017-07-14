package Model.oop;

import java.util.LinkedList;
import java.util.Map;

public interface Diagram {
	
	public void addProcess(ProcessList process);
	public ProcessList getProcesses();
	public String getName();
	public String toString();
	

}