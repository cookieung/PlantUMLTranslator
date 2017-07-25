package Model.sequence;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import Model.state.StateDiagram;

class SequenceFrame{
	static LinkedList<Map<String, Map<String, String>>> startToEnd;
//	static Map<String, Map<String, String>> endList;
	LinkedList<Map<String, LinkedList<LinkedList<String>>>> processFrame;
	static String name = "",typeFrame = "";
	
	public SequenceFrame(String name,String typeFrame,LinkedList<Map<String, Map<String, String>>> startToEnd){
		System.out.println("start to end :"+startToEnd);
//		System.out.println("end :"+endList);
		this.startToEnd = startToEnd;
		this.name = name;
		this.typeFrame = typeFrame;
		this.processFrame = getAllFrameProc();
	}
	

	
	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcessFrame() {
		return processFrame;
	}
	
	public String getTypeFrame() {
		return this.typeFrame;
	}



	public String toString(){
		String s ="###"+typeFrame;
//		for (int i = 0; i < processFrame.size(); i++) {
//			for (Entry<String, LinkedList<LinkedList<String>>> map : processFrame.get(i).entrySet()) {
//				s+=map.getKey()+" = ";
//				for (int j = 0; j < map.getValue().size(); j++) {
//					for (int k = 0; k < map.getValue().get(j).size(); k++) {
//						s+=map.getValue().get(j).get(k);
//					}
//				}
//			}
//		}
		return s+processFrame;
	}
	
	
	public static Map<String, LinkedList<LinkedList<String>>> processName(String m){
		LinkedList<String> res= new LinkedList<>();
		for (int i = 0; i < startToEnd.size(); i++) {
			for (Entry<String, Map<String, String>> bg : startToEnd.get(i).entrySet()) {
				for (Entry<String, String> string : bg.getValue().entrySet()) {
					if(string.getKey().equals(m))
					res.add(string.getValue());
				}
			}
		}
		
//		for (Entry<String, Map<String, String>> en : endList.entrySet()) {
//			for (Entry<String, String> string : en.getValue().entrySet()) {
//				if(string.getKey().equals(m))
//				res.add(string.getValue());
//			}
//		}
		LinkedList<LinkedList<String>> result = new LinkedList<>();
		result.add(res);
		Map<String, LinkedList<LinkedList<String>>> mRes = new LinkedHashMap<>();
		mRes.put(name+"_"+m, result);
		System.out.println("Process Name M :"+name);
		return mRes;
	}
	

	public Set<StateDiagram> getAllStateDiagram(){
		Set<StateDiagram> s = new LinkedHashSet<>();
		int count=1;
		for (int i = 0; i < startToEnd.size(); i++) {
			for (Entry<String, Map<String, String>> bg : startToEnd.get(i).entrySet()) {
				for (Entry<String, String> string : bg.getValue().entrySet()) {
					StateDiagram stateDiagram = new StateDiagram("M_"+string.getKey());
					s.add(stateDiagram);
				}
			}
		}
		
		
//		for (Entry<String, Map<String, String>> en : endList.entrySet()) {
//			for (Entry<String, String> string : en.getValue().entrySet()) {
//				StateDiagram stateDiagram = new StateDiagram("M_"+string.getKey());
//				s.add(stateDiagram);
//			}
//		}
		System.out.println("Set :"+s);
		return s;
	}
	
	//Recent
	public static Object[] getAllState(){
		Set<String> setState = new LinkedHashSet<>();
		int count=1;
		for (int i = 0; i < startToEnd.size(); i++) {
			for (Entry<String, Map<String, String>> bg : startToEnd.get(i).entrySet()) {
				for (Entry<String, String> string : bg.getValue().entrySet()) {
					setState.add(string.getKey());
				}
			}
		}
		
		
//		for (Entry<String, Map<String, String>> en : endList.entrySet()) {
//			for (Entry<String, String> string : en.getValue().entrySet()) {
//				setState.add(string.getKey());
//			}
//		}
		System.out.println("Set :"+setState);
		return setState.toArray();
	}
	
	//Recent
	public static LinkedList<Map<String, LinkedList<LinkedList<String>>>> getAllFrameProc() {
		LinkedList<Map<String, LinkedList<LinkedList<String>>>> listM = new LinkedList<>();
		for (int i = 0; i < getAllState().length; i++) {
			listM.add(processName(getAllState()[i]+""));
		}
		System.out.println("ListM :"+listM);
		return listM;
	}
	
	
	
}
