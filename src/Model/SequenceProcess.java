package Model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import Model.Proc.ProcessLinkedList;

public class SequenceProcess extends ProcessList {
		
	public SequenceProcess() {
		super();
	}

//	public SequenceProcess(String name,LinkedList<LinkedList<String>> linkedList) {
//		processMapByName = new ProcessLinkedList();
//		processMapByState = new ProcessLinkedList();
//		this.addProcess(name, linkedList);
//	}
	
	@Override
	public void addProcess(String nameL,String nameR ,LinkedList<LinkedList<String>> linkedListL,LinkedList<LinkedList<String>> linkedListR,String typeName) {
		Map<String, LinkedList<LinkedList<String>>> map = new LinkedHashMap<>();
		map.put(nameL, linkedListL);
		map.put(nameR, linkedListR);
		System.out.println("Map in Sequence Process Class :"+map);
		processMapByName.addNormal(map,typeName);
		System.out.println("Object in Sequence Process Class :"+processMapByName.getNormalProcess());
	}
	
	 public LinkedList<LinkedList<String>> combineSendAnsReceiveProcess(LinkedList<String> leftL,LinkedList<String> rightL){
		 LinkedList<LinkedList<String>> appender = new LinkedList<>();
		 appender.add(leftL);
		 appender.add(rightL);
		 return appender;
	 }
	
	

}
