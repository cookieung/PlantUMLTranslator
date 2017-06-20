package Model;

import java.util.LinkedList;
import java.util.Map;

public interface Diagram {
	
	public LinkedList<Map<String,LinkedList<String>>> addProcess(LinkedList<Map<String,LinkedList<String>>> process);
	public LinkedList<Map<String,LinkedList<String>>> getProcesses();
	public String getName();
	

}
