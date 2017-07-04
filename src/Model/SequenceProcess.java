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
		
	//A frame storage that will store all frame
	public ArrayList<SqFrame> frames;
	
	public SequenceProcess() {
		super();
		//Initial Storage
		frames = new ArrayList<>();
	}
	
	@Override
	public void addProcess(String nameL,String nameR ,LinkedList<LinkedList<String>> linkedListL,LinkedList<LinkedList<String>> linkedListR,String typeName) {
		Map<String, LinkedList<LinkedList<String>>> map = new LinkedHashMap<>();
		map.put(nameL, linkedListL);
		map.put(nameR, linkedListR);
		System.out.println("Map in Sequence Process Class :"+map);
		processMapByName.addNormal(map,typeName);
		System.out.println("Object in Sequence Process Class :"+processMapByName.getNormalProcess());
		System.out.println("$E$:"+nameL+" "+nameR);
	}
	
	//Get All frame in this sequence to printout
	 public ArrayList<LinkedList<Map<String, LinkedList<LinkedList<String>>>>> getFrames() {
		 ArrayList<LinkedList<Map<String, LinkedList<LinkedList<String>>>>> r = new ArrayList<>();
		 for (int i=0; i< frames.size(); i++) {
			 System.err.println("Check "+frames.get(i).name+" :"+frames.get(i).getProcessFrame());
			r.add(frames.get(i).getProcessFrame());
		}
		return r;
	}

	//Change the frame by the new series of frame
	public void setFrames(ArrayList<SqFrame> frames) {
		this.frames = frames;
	}

	public LinkedList<LinkedList<String>> combineSendAnsReceiveProcess(LinkedList<String> leftL,LinkedList<String> rightL){
		 LinkedList<LinkedList<String>> appender = new LinkedList<>();
		 appender.add(leftL);
		 appender.add(rightL);
		 return appender;
	 }
	 
	//Update the frame
	@Override
	 public void checkFrame(){
		 Map<String, Map<String, String>> begin = new LinkedHashMap<>();
		 Map<String, Map<String, String>> end = new LinkedHashMap<>();
		 LinkedList<Map<String, Map<String, Map<String, String>>>> l = processMapByName.getOptProcess();
		 System.out.println("L before check frame :"+l);
		 int counter = 0;
		 for (int i = 0; i < l.size(); i++) {
			for (Entry<String, Map<String, Map<String, String>>> map : l.get(i).entrySet()) {
				System.out.println("CV Frame :"+map);
				if(map.getKey().equals("alt")){
					begin = map.getValue();
				}
				else if(map.getKey().equals("else")){
					end = map.getValue();
					SqFrame frame = new SqFrame("F"+ ++counter,begin,end);
					frames.add(frame);
				}
				System.out.println("Update"+map.getKey()+" :"+frames);
				System.out.println("F_b :"+begin);
				System.out.println("F_e :"+end);
			}
		}

	 }
	 
	 
	
	

}

class SqFrame{
	static Map<String, Map<String, String>> beginList;
	static Map<String, Map<String, String>> endList;
	LinkedList<Map<String, LinkedList<LinkedList<String>>>> processFrame;
	static String name = "";
	
	public SqFrame(String name,Map<String, Map<String, String>> beginList,Map<String, Map<String, String>> endList){
		System.out.println("begin :"+beginList);
		System.out.println("end :"+endList);
		this.beginList = beginList;
		this.endList = endList;
		this.name = name;
		this.processFrame = getAllFrameProc();
	}
	

	
	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcessFrame() {
		return processFrame;
	}



	public String toString(){
		String s ="###";
		for (int i = 0; i < processFrame.size(); i++) {
			for (Entry<String, LinkedList<LinkedList<String>>> map : processFrame.get(i).entrySet()) {
				s+=map.getKey()+" = ";
				for (int j = 0; j < map.getValue().size(); j++) {
					for (int k = 0; k < map.getValue().get(j).size(); k++) {
						s+=map.getValue().get(j).get(k);
					}
				}
			}
		}
		return "###"+processFrame;
	}
	
	
	public static Map<String, LinkedList<LinkedList<String>>> processName(String m){
		LinkedList<String> res= new LinkedList<>();
		for (Entry<String, Map<String, String>> bg : beginList.entrySet()) {
			for (Entry<String, String> string : bg.getValue().entrySet()) {
				if(string.getKey().equals(m))
				res.add(string.getValue());
			}
		}
		for (Entry<String, Map<String, String>> en : endList.entrySet()) {
			for (Entry<String, String> string : en.getValue().entrySet()) {
				if(string.getKey().equals(m))
				res.add(string.getValue());
			}
		}
		LinkedList<LinkedList<String>> result = new LinkedList<>();
		result.add(res);
		Map<String, LinkedList<LinkedList<String>>> mRes = new LinkedHashMap<>();
		mRes.put(name+"_"+m, result);
		return mRes;
	}
	
	//Recent
	public static Object[] getAllState(){
		Set<String> setState = new LinkedHashSet<>();
		int count=1;
		for (Entry<String, Map<String, String>> bg : beginList.entrySet()) {
			for (Entry<String, String> string : bg.getValue().entrySet()) {
				setState.add(string.getKey());
			}
		}
		
		for (Entry<String, Map<String, String>> en : endList.entrySet()) {
			for (Entry<String, String> string : en.getValue().entrySet()) {
				setState.add(string.getKey());
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
	
	public static ArrayList<String> FrameChannel() {
		ArrayList<String> s = new ArrayList<>();
		LinkedList<Map<String, LinkedList<LinkedList<String>>>> m = getAllFrameProc();
		for (int i = 0; i < m.size(); i++) {
			for (Entry<String, LinkedList<LinkedList<String>>> map : m.get(i).entrySet()) {
				for (int j = 0; j < map.getValue().get(0).size(); j++) {
					if(map.getValue().get(0).get(j).contains(name+"_"))
					{
						s.add(map.getValue().get(0).get(j));
					}
				}
			}
		}
		return s;
	}
	
	
}
