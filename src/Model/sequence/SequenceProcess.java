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
	public void addProcess(String nameL,String nameR ,LinkedList<LinkedList<String>> linkedListL,LinkedList<LinkedList<String>> linkedListR,String typeName) {
		Map<String, LinkedList<LinkedList<String>>> map = new LinkedHashMap<>();
		map.put(nameL, linkedListL);
		map.put(nameR, linkedListR);
		System.out.println("Map in Sequence Process Class :"+map);
		if(typeName.equals("alt") || typeName.equals("opt")) frameCounter++;
		processMapByName.addNormal(map,typeName,"f"+frameCounter);
		addStateToSet(linkedListL);
		addStateToSet(linkedListR);
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
	 
		private void addStateToSet(LinkedList<LinkedList<String>> linkedList){
			for (int i = 0; i < linkedList.size(); i++) {
				for (int j = 0; j < linkedList.get(i).size(); j++) {
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
				}
			}
			Object[] ss  = states.toArray();
			for (int i = 0; i < ss.length; i++) {
				s.add((Diagram)ss[i]);
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
					SequenceFrame frame = new SequenceFrame("F"+ ++counter,begin,end);
					frames.add(frame);
				}
				System.out.println("Update"+map.getKey()+" :"+frames);
				System.out.println("F_b :"+begin);
				System.out.println("F_e :"+end);
			}
		}

	 }
	

	 
	 
	
	

}

