package Model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import Model.Proc.ProcessLinkedList;

public class SequenceProcess implements ProcessList {
	
	private ProcessLinkedList processMapByName;
	
	private ProcessLinkedList processMapByState;
	
	public SequenceProcess() {
		processMapByName = new ProcessLinkedList();
		processMapByState = new ProcessLinkedList();
	}

//	public SequenceProcess(String name,LinkedList<LinkedList<String>> linkedList) {
//		processMapByName = new ProcessLinkedList();
//		processMapByState = new ProcessLinkedList();
//		this.addProcess(name, linkedList);
//	}
	
	
	@Override
	public void addProcess(String name,LinkedList<LinkedList<String>> linkedList,String typeName) {
		Map<String, LinkedList<LinkedList<String>>> map = new LinkedHashMap<>();
		map.put(name, linkedList);
		System.out.println("Map in Sequence Process Class :"+map);
		processMapByName.addNormal(map,typeName);
		System.out.println("Object in Sequence Process Class :"+processMapByName);
	}
	
	@Override
	public Map<String, LinkedList<LinkedList<String>>> getElement(int index) {
		return processMapByName.getNormalProcess().get(index);
	}

	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcess() {
		return processMapByName.getNormalProcess();
	}

	public void setProcess(LinkedList<Map<String, LinkedList<LinkedList<String>>>> process) {
		this.processMapByName.setNormalProcess(process);
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
		return this.processMapByName.getNormalProcess();
	}

	@Override
	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcessListByState() {
		// TODO Auto-generated method stub
		return this.processMapByState.getNormalProcess();
	}
	

}
