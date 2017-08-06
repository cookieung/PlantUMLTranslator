package Model.sequence.frame;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import Model.state.StateDiagram;

public class SequenceFrame{
	static LinkedList<Map<String, LinkedList<Map<String, String>>>> trueFrame;
	static LinkedList<Map<String, LinkedList<Map<String, String>>>> falseFrame;
	LinkedList<Map<String, LinkedList<Map<String, LinkedList<String>>>>> processFrame;
	LinkedList<SequenceFrame> frames = new LinkedList<>();
	static String name = "",typeFrame = "";
	
	public SequenceFrame(String name,String typeFrame,LinkedList<Map<String, LinkedList<Map<String, String>>>> trueFrame,LinkedList<Map<String, LinkedList<Map<String, String>>>> falseFrame){
		this.falseFrame = falseFrame;
		this.trueFrame = trueFrame;
		System.out.println(typeFrame);
		System.out.println("True Frame :"+trueFrame);
		System.out.println("False Frame :"+falseFrame);
		this.name = name+"_"+typeFrame;
		this.typeFrame = typeFrame;
		this.processFrame = getAllFrameProc();
	}
	
	public SequenceFrame(String name,LinkedList<Map<String, LinkedList<Map<String, String>>>> trueFrame){
		this.falseFrame = falseFrame;
		this.trueFrame = trueFrame;		
		System.out.println("True Frame :"+trueFrame);		
		System.out.println("False Frame :"+falseFrame);		
		this.name = name;		
		this.typeFrame = typeFrame;		
		this.processFrame = getAllFrameProc();		
	}		
		
	public void addFrameInside(String m,LinkedList<Map<String,LinkedList<String>>> result) {		
		if(m.equals("loop")) {		
			frames.add(new LoopFrame(m, result));		
		}		
		else {		
			frames.add(new AltFrame(m, result));		
		}
	}
	
	public LinkedList<SequenceFrame> getAllFrame(){
		return frames;
	}
	
	public String getName() {
		return this.name;
	}
	

	
	public LinkedList<Map<String, LinkedList<Map<String, LinkedList<String>>>>> getProcessFrame() {
		return processFrame;
	}
	
	public LinkedList<SequenceFrame> getProcessFrameSq() {
		LinkedList<SequenceFrame> l = new LinkedList<>();
		System.out.println("Get ProcessFrame size :"+processFrame.size());
		for (int i = 0; i < processFrame.size(); i++) {
			for (Entry<String, LinkedList<Map<String, LinkedList<String>>>> sequenceFrame : processFrame.get(i).entrySet()) {
				if(name.equals("loop"))l.add(new LoopFrame(sequenceFrame.getKey(), sequenceFrame.getValue()));
				else l.add(new AltFrame(sequenceFrame.getKey(), sequenceFrame.getValue()));
			}
		}
		return l;
	}
	
	public String getTypeFrame() {
		return this.typeFrame;
	}



	public String toString(){
		String s ="###"+typeFrame;
		return s+processFrame;
	}
	
	
	public static Map<String, LinkedList<Map<String,LinkedList<String>>>> processName(String m){
		Map<String,LinkedList<String>> res= new HashMap<>();
		LinkedList<String> l;
		for (int i = 0; i < trueFrame.size(); i++) {
			for (Entry<String, LinkedList<Map<String, String>>> bg : trueFrame.get(i).entrySet()) {
				l  = new LinkedList<>();
				for (int j = 0; j < bg.getValue().size(); j++) {
					for (Entry<String, String> string : bg.getValue().get(j).entrySet()) {
						if(string.getKey().equals(m)) {
							l.add(string.getValue());
						}		
					}
				}
				if(res.containsKey("f"+bg.getKey().substring(bg.getKey().length()-3))) {
					LinkedList<String> ll = res.get("f"+bg.getKey().substring(bg.getKey().length()-3));
					for (int j = 0; j < l.size(); j++) {
						ll.add(l.get(j));
					}
					res.put("f"+bg.getKey().substring(bg.getKey().length()-3),ll);
				}else res.put("f"+bg.getKey().substring(bg.getKey().length()-3),l);
			}
		}
		
		for (int i = 0; i < falseFrame.size(); i++) {
			for (Entry<String, LinkedList<Map<String, String>>> bg : falseFrame.get(i).entrySet()) {
				l  = new LinkedList<>();
				for (int j = 0; j < bg.getValue().size(); j++) {
					for (Entry<String, String> string : bg.getValue().get(j).entrySet()) {
						if(string.getKey().equals(m)) {
							l.add(string.getValue());
						}		
					}
				}
				if(res.containsKey("f"+bg.getKey().substring(bg.getKey().length()-3))) {
					LinkedList<String> ll = res.get("f"+bg.getKey().substring(bg.getKey().length()-3));
					for (int j = 0; j < l.size(); j++) {
						ll.add(l.get(j));
					}
					res.put("f"+bg.getKey().substring(bg.getKey().length()-3),ll);
				}else res.put("f"+bg.getKey().substring(bg.getKey().length()-3),l);
			}
		}
		LinkedList<Map<String,LinkedList<String>>> result = new LinkedList<>();
		result.add(res);
		Map<String, LinkedList<Map<String, LinkedList<String>>>> mRes = new LinkedHashMap<>();
		mRes.put(name+"_"+m, result);
		System.out.println("Process Name M :"+name);
		return mRes;
	}
	

	public Set<StateDiagram> getAllStateDiagram(){
		Set<StateDiagram> s = new LinkedHashSet<>();
		int count=1;
		for (int i = 0; i < trueFrame.size(); i++) {
			for (Entry<String, LinkedList<Map<String, String>>> bg : trueFrame.get(i).entrySet()) {
				for (int j = 0; j < bg.getValue().size(); j++) {
					for (Entry<String, String> string : bg.getValue().get(j).entrySet()) {
						StateDiagram stateDiagram = new StateDiagram("M_"+string.getKey());
						s.add(stateDiagram);
					}
				}
			}
		}
		
		
		for (int i = 0; i < falseFrame.size(); i++) {
			for (Entry<String, LinkedList<Map<String, String>>> bg : falseFrame.get(i).entrySet()) {
				for (int j = 0; j < bg.getValue().size(); j++) {
					for (Entry<String, String> string : bg.getValue().get(j).entrySet()) {
						StateDiagram stateDiagram = new StateDiagram("M_"+string.getKey());
						s.add(stateDiagram);
					}
				}				
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
			for (Entry<String, LinkedList<Map<String, String>>> bg : trueFrame.get(i).entrySet()) {
				for (int j = 0; j < bg.getValue().size(); j++) {
					for (Entry<String, String> string : bg.getValue().get(j).entrySet()) {
						setState.add(string.getKey());
					}
				}					
			}
		}
		
		for (int i = 0; i < falseFrame.size(); i++) {
			for (Entry<String, LinkedList<Map<String, String>>> bg : falseFrame.get(i).entrySet()) {
				for (int j = 0; j < bg.getValue().size(); j++) {
					for (Entry<String, String> string : bg.getValue().get(j).entrySet()) {
						setState.add(string.getKey());
					}
				}					
			}
		}
		System.out.println("Set :"+setState);
		return setState.toArray();
	}
	
	//Recent
	public static LinkedList<Map<String, LinkedList<Map<String,LinkedList<String>>>>> getAllFrameProc() {
		LinkedList<Map<String, LinkedList<Map<String,LinkedList<String>>>>> listM = new LinkedList<>();
		for (int i = 0; i < getAllState().length; i++) {
			listM.add(processName(getAllState()[i]+""));
		}
		System.out.println("ListM :"+listM);
		return listM;
	}
	
	
	
}