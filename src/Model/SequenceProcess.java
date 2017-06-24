package Model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class SequenceProcess implements ProcessList {
	
	private LinkedList<Map<String, LinkedList<LinkedList<String>>>> processMapByName;
	
	private LinkedList<Map<String, LinkedList<LinkedList<String>>>> processMapByState;
	
	public SequenceProcess() {
		processMapByName = new LinkedList<>();
		processMapByState = new LinkedList<>();
	}

	public SequenceProcess(String name,LinkedList<LinkedList<String>> linkedList) {
		processMapByName = new LinkedList<>();
		this.addProcess(name, linkedList);
	}
	
	
	@Override
	public void addProcess(String name,LinkedList<LinkedList<String>> linkedList) {
		Map<String, LinkedList<LinkedList<String>>> map = new LinkedHashMap<>();
		map.put(name, linkedList);
		System.out.println("Map in Sequence Process Class :"+map);
		processMapByName.add(map);
		System.out.println("Object in Sequence Process Class :"+processMapByName);
	}
	
	@Override
	public Map<String, LinkedList<LinkedList<String>>> getElement(int index) {
		return processMapByName.get(index);
	}

	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcess() {
		return processMapByName;
	}

	public void setProcess(LinkedList<Map<String, LinkedList<LinkedList<String>>>> process) {
		this.processMapByName = process;
	}

	@Override
	public int getLength() {
		return processMapByName.size();
	}

	
	
	public String toString(){
		return "By Name :"+processMapByName+"/nBy State :"+processMapByState+"\n";
	}

	@Override
	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcessListByName() {
		return this.processMapByName;
	}

	@Override
	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcessListByState() {
		// TODO Auto-generated method stub
		return this.processMapByState;
	}
	

}
