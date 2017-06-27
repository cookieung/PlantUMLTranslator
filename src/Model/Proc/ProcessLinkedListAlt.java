package Model.Proc;

import java.util.LinkedList;
import java.util.Map;

public class ProcessLinkedListAlt extends ProcessLinkedList {
	
	private LinkedList<Map<String, Map<String, LinkedList<LinkedList<String>>>>> altProcess;
		
	public ProcessLinkedListAlt() {
		super();
		altProcess = new LinkedList<>();
	}
	
	public void addAltProcess(Map<String, Map<String, LinkedList<LinkedList<String>>>> map){
		altProcess.add(map);
	}
	
	public int size(){
		return altProcess.size();
	}

}
