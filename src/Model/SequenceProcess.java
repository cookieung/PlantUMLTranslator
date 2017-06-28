package Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import Model.Proc.ProcessLinkedList;

public class SequenceProcess extends ProcessList {
		
	public ArrayList<SqFrame> frames;
	
	public SequenceProcess() {
		super();
		frames = new ArrayList<>();
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
		checkFrame();
	}
	
	 public LinkedList<LinkedList<String>> combineSendAnsReceiveProcess(LinkedList<String> leftL,LinkedList<String> rightL){
		 LinkedList<LinkedList<String>> appender = new LinkedList<>();
		 appender.add(leftL);
		 appender.add(rightL);
		 return appender;
	 }
	 
	 public void checkFrame(){
		 Map<String, LinkedList<LinkedList<String>>> begin = new LinkedHashMap<>();
		 Map<String, LinkedList<LinkedList<String>>> end = new LinkedHashMap<>();
		 LinkedList<Map<String, Map<String, LinkedList<LinkedList<String>>>>> l = processMapByName.getAltProcess();
		 for (int i = 0; i < l.size(); i++) {
			for (Entry<String, Map<String, LinkedList<LinkedList<String>>>> map : l.get(i).entrySet()) {
				System.out.println("CV Frame :"+map.getKey());
				if(map.getKey().equals("alt")){
					begin = map.getValue();
				}
				else if(map.getKey().equals("else")){
					end = map.getValue();
					SqFrame frame = new SqFrame(begin,end);
					frames.add(frame);
				}
				System.out.println("F_b :"+begin);
				System.out.println("F_e :"+end);
			}
		}
		 if(frames.size()>0)
		 System.out.println("Frame :"+frames.get(0).toString());
	 }
	 
	 
	
	

}

class SqFrame{
	Map<String, LinkedList<LinkedList<String>>> beginList;
	Map<String, LinkedList<LinkedList<String>>> endList;
	
	public SqFrame(Map<String, LinkedList<LinkedList<String>>> beginList,Map<String, LinkedList<LinkedList<String>>> endList){
		System.out.println("begin :"+beginList);
		System.out.println("end :"+endList);
		this.beginList = beginList;
		this.endList = endList;
	}
	
	public String toString(){
		return this.beginList+"\n"+this.endList;
	}
	
//	public LinkedList<String> processName(){
//		
//	}
	
	
}
