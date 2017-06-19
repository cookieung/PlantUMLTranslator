package Model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class StateDiagram implements Diagram {
	
	private String name;
	private LinkedList<Map<String, LinkedList<String>>> processes;
	private Map<String, LinkedList<String>> map;
	private LinkedList<String> ll;
	
	public StateDiagram(String name){
		this.name = name;
		this.processes = new LinkedList<>();
		map = new LinkedHashMap<>();
		ll = new LinkedList<>();
	}
	
	

}
