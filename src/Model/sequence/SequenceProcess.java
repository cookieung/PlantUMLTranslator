package Model.sequence;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import Model.oop.Diagram;
import Model.oop.ProcessList;
import Model.proc.ProcessLinkedList;
import Model.state.StateDiagram;

import java.util.Set;

public class SequenceProcess extends ProcessList {
		
	//A frame storage that will store all frame
	public ArrayList<SequenceFrame> frames;
	int frameCounter = 0;
	public Set<StateDiagram> states = new LinkedHashSet<>();
	
	public SequenceProcess() {
		super();
		//Initial Storage
		frames = new ArrayList<>();
	}
	
	@Override
	public void addProcess(String nameL,String nameR ,LinkedList<LinkedList<String>> linkedListL,LinkedList<LinkedList<String>> linkedListR,String typeName,String nextTypeName) {
		Map<String, LinkedList<LinkedList<String>>> map = new LinkedHashMap<>();
		map.put(nameL, linkedListL);
		map.put(nameR, linkedListR);
		System.out.println("Map in Sequence Process Class :"+map);
		if(typeName.contains("alt") || typeName.contains("opt") || typeName.contains("loop")) frameCounter++;
		processMapByName.addNormal(map,typeName,nextTypeName,"f"+frameCounter);
		addStateToSet(linkedListL);
		addStateToSet(linkedListR);
		System.out.println("Object in Sequence Process Class :"+processMapByName.getNormalProcess());
		System.out.println("Opt Object in Sequence Process Class :"+processMapByName.getOptProcess());
		System.out.println("$E$:"+nameL+" "+nameR);
	}
	
	public String getFrameTypeName(int i) {
		return frames.get(i).getTypeFrame();
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
	 
		private void addStateToSet(LinkedList<LinkedList<String>> linkedList){
			for (int i = 0; i < linkedList.size(); i++) {
				for (int j = 0; j < linkedList.get(i).size(); j++) {
					if(!linkedList.get(i).get(j).contains(">"))
					states.add(new StateDiagram("M_"+linkedList.get(i).get(j)));
					
				}
			}
		}
	 
		public Set<Diagram> getAllTempStateDiagram(){
			Set<Diagram> s  =new LinkedHashSet<>();
			for (int i = 0; i < frames.size(); i++) {
				Object[] o = frames.get(i).getAllStateDiagram().toArray();
				for (int j = 0; j < o.length; j++) {
					s.add((Diagram)o[j]);
					System.err.println("P1 :"+(Diagram)o[j]+"");
				}
			}
			Object[] ss  = states.toArray();
			for (int i = 0; i < ss.length; i++) {
				s.add((Diagram)ss[i]);
				System.err.println("P2 :"+(Diagram)ss[i]);
			}
			System.out.println("All Temp State :"+s);
			return s;
		}
		
		

	//Change the frame by the new series of frame
	public void setFrames(ArrayList<SequenceFrame> frames) {
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
		 String nameKey = "";
		 LinkedList<Map<String, Map<String, String>>> allFrameProcess = new LinkedList<>();
		 Map<String, Map<String, String>> begin = new LinkedHashMap<>();
		 Map<String, Map<String, String>> end = new LinkedHashMap<>();
		 LinkedList<Map<String, Map<String, Map<String, String>>>> l = processMapByName.getOptProcess();
		 System.out.println("L before check frame :"+l);
		 int counter = 0;
		 for (int i = 0; i < l.size(); i++) {
			for (Entry<String, Map<String, Map<String, String>>> map : l.get(i).entrySet()) {
				System.out.println("CV Frame :"+map);
				System.out.println("Map get Key :"+map.getKey()+" = "+nameKey);
				if(map.getKey().contains("alt") || map.getKey().contains("opt") || map.getKey().contains("loop")){
					begin = map.getValue();
					allFrameProcess.add(map.getValue());
					nameKey = map.getKey();
				}else if(map.getKey().contains("end")) {
					end = map.getValue();
					allFrameProcess.add(map.getValue());
					if(nameKey.contains("alt") || nameKey.contains("opt") || nameKey.contains("loop")) {
					SequenceFrame frame = new SequenceFrame("F"+ ++counter,nameKey,allFrameProcess);
					frames.add(frame);	
					}
				}
				System.out.println("All Frame Proc :"+allFrameProcess);
				System.err.println("NameKey :"+nameKey+","+map.getKey());
				System.out.println("<Update>"+map.getKey()+" :"+frames);
				System.out.println("F_b :"+begin);
				System.out.println("F_e :"+end);
			}
		}

	 }
	

	 
	 
	
	

}

