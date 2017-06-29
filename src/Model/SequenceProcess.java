package Model;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
		 if(frames.size()>0){
			 System.out.println("Frame :"+frames.get(0).toString());
		 }

	 }
	 
	 
	
	

}

class SqFrame{
	static Map<String, LinkedList<LinkedList<String>>> beginList;
	static Map<String, LinkedList<LinkedList<String>>> endList;
	LinkedList<Map<String, LinkedList<LinkedList<String>>>> processFrame;
	
	public SqFrame(Map<String, LinkedList<LinkedList<String>>> beginList,Map<String, LinkedList<LinkedList<String>>> endList){
		System.out.println("begin :"+beginList);
		System.out.println("end :"+endList);
		this.beginList = beginList;
		this.endList = endList;
		this.processFrame = getAllFrameProc();
	}
	
	public String toString(){
		return "FrameChannel "+FrameChannel()+"\n"+this.processFrame+"";
	}
	
	
	public static Map<String, LinkedList<LinkedList<String>>> processName(String m){
		LinkedList<String> resultBegin= new LinkedList<>();
		LinkedList<String> resultEnd= new LinkedList<>();
		resultBegin.add("f1_b");
		resultBegin.add("f1_alt1");
		for (Entry<String, LinkedList<LinkedList<String>>> bg : beginList.entrySet()) {
			if(bg.getValue().get(0).get(0).equals(m))
			resultBegin.add(bg.getKey());
		}
		resultEnd.add("f1_alt2");
		for (Entry<String, LinkedList<LinkedList<String>>> en : endList.entrySet()) {
			if(en.getValue().get(0).get(0).equals(m))
			resultEnd.add(en.getKey());
		}
		resultEnd.add("f1_e");
		LinkedList<LinkedList<String>> result = new LinkedList<>();
		result.add(resultBegin);
		result.add(resultEnd);
		Map<String, LinkedList<LinkedList<String>>> mRes = new LinkedHashMap<>();
		mRes.put("F1_"+m, result);
		return mRes;
	}
	
	public static Object[] getAllState(){
		Set<String> setState = new LinkedHashSet<>();
		for (Entry<String, LinkedList<LinkedList<String>>> bg : beginList.entrySet()) {
			setState.add(bg.getValue().get(0).get(0));
		}
		for (Entry<String, LinkedList<LinkedList<String>>> en : endList.entrySet()) {
			setState.add(en.getValue().get(0).get(0));
		}
		System.out.println("Set :"+setState);
		return setState.toArray();
	}
	
	public static LinkedList<Map<String, LinkedList<LinkedList<String>>>> getAllFrameProc() {
		LinkedList<Map<String, LinkedList<LinkedList<String>>>> listM = new LinkedList<>();
		for (int i = 0; i < getAllState().length; i++) {
			listM.add(processName(getAllState()[i]+""));
		}
		return listM;
	}
	
	public static ArrayList<String> FrameChannel() {
		ArrayList<String> s = new ArrayList<>();
		LinkedList<Map<String, LinkedList<LinkedList<String>>>> m = getAllFrameProc();
		for (int i = 0; i < m.size(); i++) {
			for (Entry<String, LinkedList<LinkedList<String>>> map : m.get(i).entrySet()) {
				for (int j = 0; j < map.getValue().get(0).size(); j++) {
					if(map.getValue().get(0).get(j).contains("f1_"))
					{
						s.add(map.getValue().get(0).get(j));
					}
				}
			}
		}
		return s;
	}
	
	
}
