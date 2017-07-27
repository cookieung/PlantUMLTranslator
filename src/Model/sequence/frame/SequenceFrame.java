package Model.sequence.frame;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import Model.state.StateDiagram;

public class SequenceFrame{
	static LinkedList<Map<String, String>> trueFrame;
	static LinkedList<Map<String, String>> falseFrame;
	static LinkedList<Map<String, LinkedList<LinkedList<String>>>> processFrame;
	static String name = ""; //TypeFrame_index
	static String typeFrame = "";
	
	public SequenceFrame(String name,String typeFrame,LinkedList<Map<String, String>> trueFrame,LinkedList<Map<String, String>> falseFrame){
		this.trueFrame = trueFrame;
		this.falseFrame = falseFrame;
		this.name = name;
		this.typeFrame = typeFrame;
		this.processFrame = getAllFrameProc();
	}
	
	public String getName() { return this.name; }
	
	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcessFrame(){ return this.processFrame; }
	

	
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
		return "TRUE :"+trueFrame+"\nFALSE :"+falseFrame+"\n";
	}
	
	
	public static Map<String, LinkedList<LinkedList<String>>> processName(String m){
		LinkedList<String> res= new LinkedList<>();
		for (int i = 0; i < trueFrame.size(); i++) {
			for (Entry<String, String> bg : trueFrame.get(i).entrySet()) {
				if(bg.getKey().equals(m))
				res.add(bg.getValue());
			}
		}
		
		for (int i = 0; i < falseFrame.size(); i++) {
			for (Entry<String, String> bg : falseFrame.get(i).entrySet()) {
				if(bg.getKey().equals(m))
				res.add(bg.getValue());
			}
		}
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
		for (int i = 0; i < trueFrame.size(); i++) {
			for (Entry<String, String> bg : trueFrame.get(i).entrySet()) {
				StateDiagram stateDiagram = new StateDiagram("M_"+bg.getKey());
				s.add(stateDiagram);
			}
		}
		
		for (int i = 0; i < falseFrame.size(); i++) {
			for (Entry<String, String> bg : falseFrame.get(i).entrySet()) {
				StateDiagram stateDiagram = new StateDiagram("M_"+bg.getKey());
				s.add(stateDiagram);
			}
		}
		System.out.println("Set :"+s);
		return s;
	}
	
	//Recent
	public static Object[] getAllState(){
		Set<String> setState = new LinkedHashSet<>();
		int count=1;
		for (int i = 0; i < trueFrame.size(); i++) {
			for (Entry<String, String> bg : trueFrame.get(i).entrySet()) {
				setState.add(bg.getKey());
			}
		}
		
		for (int i = 0; i < falseFrame.size(); i++) {
			for (Entry<String, String> bg : falseFrame.get(i).entrySet()) {
				setState.add(bg.getKey());
			}
		}
		
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
