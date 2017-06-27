package Model.Proc;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class ProcessLinkedList {
	
	private LinkedList<Map<String,LinkedList<LinkedList<String>>>> normalProcess;
	
	private LinkedList<Map<String, Map<String, LinkedList<LinkedList<String>>>>> altProcess;
	
	public ProcessLinkedList(){
		this.normalProcess = new LinkedList<>();
		this.altProcess = new LinkedList<>();
	}
	
	public int size(){
		return normalProcess.size();
	}
	
	public void addNormal(Map<String,LinkedList<LinkedList<String>>> map,String typeMap) {
		normalProcess.add(map);
		Map<String, Map<String, LinkedList<LinkedList<String>>>> m = new LinkedHashMap<>();
		m.put(typeMap, map);
		altProcess.add(m);
	}
	
	public void addAlt(Map<String, Map<String, LinkedList<LinkedList<String>>>> map) {
		altProcess.add(map);
	}

	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getNormalProcess() {
		return normalProcess;
	}

	public void setNormalProcess(LinkedList<Map<String, LinkedList<LinkedList<String>>>> normalProcess) {
		this.normalProcess = normalProcess;
	}

	public LinkedList<Map<String, Map<String, LinkedList<LinkedList<String>>>>> getAltProcess() {
		return altProcess;
	}

	public void setAltProcess(LinkedList<Map<String, Map<String, LinkedList<LinkedList<String>>>>> altProcess) {
		this.altProcess = altProcess;
	}

}