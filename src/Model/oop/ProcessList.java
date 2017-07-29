package Model.oop;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import Model.proc.FrameProcess;
import Model.proc.FrameProcessMap;
import Model.proc.ProcessLinkedList;

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
	
	public FrameProcessMap getProcessListOpt() {
		return processMapByName.getOptProcess();
	}
	
	public LinkedList<FrameProcess> getProcessListOptList() {
		return processMapByName.getOptProcessList();
	}
	

	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcessListByState() {
		return processMapByState.getNormalProcess();
	}	
	
	public String toString(){
		return "By Name :"+processMapByName.getNormalProcess()+"/nBy State :"+processMapByState.getNormalProcess()+"\n";
	}
	
	public void testOpt() {
		this.processMapByName.testOpt();

	}

	public void addProcess(String newMsg, String string, LinkedList<LinkedList<String>> leftL,
			LinkedList<LinkedList<String>> rightL, String typeMap,String nextMap,String before) {
		// TODO Auto-generated method stub
		
	}
	
	public void checkFrame(){
		
	}

	public void addProcess(String name, LinkedList<LinkedList<String>> linkedList,String typeName) {

	}


}
