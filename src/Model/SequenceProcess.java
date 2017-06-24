package Model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class SequenceProcess implements ProcessList {
	
	private LinkedList<Map<String,LinkedList<LinkedList<String>>>> process;
	
	public SequenceProcess() {
		process = new LinkedList<>();
	}

	public SequenceProcess(String name,LinkedList<LinkedList<String>> linkedList) {
		process = new LinkedList<>();
		this.addProcess(name, linkedList);
	}
	
	
	@Override
	public void addProcess(String name,LinkedList<LinkedList<String>> linkedList) {
		Map<String, LinkedList<LinkedList<String>>> map = new LinkedHashMap<>();
		map.put(name, linkedList);
		System.out.println("Map in Sequence Process Class :"+map);
		process.add(map);
		System.out.println("Object in Sequence Process Class :"+process);
	}
	
	@Override
	public Map<String, LinkedList<LinkedList<String>>> getElement(int index) {
		return process.get(index);
	}

	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcess() {
		return process;
	}

	public void setProcess(LinkedList<Map<String, LinkedList<LinkedList<String>>>> process) {
		this.process = process;
	}

	@Override
	public int getLength() {
		return process.size();
	}

	@Override
	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcessList() {
		return this.getProcess();
	}
	
	public String toString(){
		return process+"";
	}
	

}
