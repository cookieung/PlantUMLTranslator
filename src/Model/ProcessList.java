package Model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import Model.Proc.ProcessLinkedList;

public class ProcessList {
	
	protected ProcessLinkedList processMapByName;
	
	protected ProcessLinkedList processMapByState;
	
	public ProcessList(){
		processMapByName = new ProcessLinkedList();
		processMapByState = new ProcessLinkedList();
	}
	
	public int getLength() {
		return processMapByName.size();
	}
	
	public int getLenghtByState(){
		return processMapByState.size();
	}
	
	public void setProcessMapByName(LinkedList<Map<String, LinkedList<LinkedList<String>>>> processMapByName) {
		this.processMapByName.setNormalProcess(processMapByName);
	}


	public void setProcessMapByState(LinkedList<Map<String, LinkedList<LinkedList<String>>>> processMapByState) {
		this.processMapByState.setNormalProcess(processMapByState);
	}


	public Map<String, LinkedList<LinkedList<String>>> getElement(int index) {
		return processMapByName.getNormalProcess().get(index);
	}


	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcessListByName() {
		return processMapByName.getNormalProcess();
	}
	
	public LinkedList<Map<String, Map<String, Map<String, String>>>> getProcessListAlt() {
		return processMapByName.getAltProcess();
	}
	

	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcessListByState() {
		return processMapByState.getNormalProcess();
	}	
	
	public String toString(){
		return "By Name :"+processMapByName.getNormalProcess()+"/nBy State :"+processMapByState.getNormalProcess()+"\n";
	}
	
	public void testAlt() {
		this.processMapByName.testAlt();

	}

	public void addProcess(String newMsg, String string, LinkedList<LinkedList<String>> leftL,
			LinkedList<LinkedList<String>> rightL, String typeMap) {
		// TODO Auto-generated method stub
		
	}
	

	public void addProcess(String name, LinkedList<LinkedList<String>> linkedList,String typeName) {

	}


}
