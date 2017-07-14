package Controller;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import View.UMLReader;
import View.UMLReaderGUI;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedHashSet;

import javax.naming.spi.DirStateFactory.Result;
import javax.sound.midi.Sequence;

import Model.oop.Diagram;
import Model.oop.ProcessList;
import Model.sequence.SequenceDiagram;
import Model.sequence.SequenceProcess;
import Model.state.StateDiagram;
import Model.state.StateProcess;

public class PlantReader {
	
//	private static Map<String,LinkedList<Map<String,LinkedList<String>>>> traceData;
	private static String input="";
	private static String[] equations;
	private static int count;
	private static Integer STATE_DG;
	private static Integer SEQUENCE_DG;
	private static ArrayList<Map<String,ArrayList<String>>> allUML = new ArrayList<>();
	
	private static ArrayList<Diagram> diagrams;
	private static Set<String> traceMsg = new LinkedHashSet<>();
	private static Set<String> originalMsg = new LinkedHashSet<>();
	
	private static Map<String, LinkedList<String>> traceMessage = new LinkedHashMap<>();

	static SequenceReader sequenceReader;
	static StateReader stateReader;
	private static ProcessList procSequence;
	
	private static Set<String> frameChannel = new HashSet<>();
	
	public PlantReader(){
		count=0;
		STATE_DG=0;
		SEQUENCE_DG=0;
	}
	
	public static void main(String[]args){
 	
//    	//Add All UML to ArrayList by separate each state
    	diagrams = new ArrayList<>();
    	    	
    	for (int i = 0; i < allUML.size(); i++) {
    		System.out.println("State "+(i+1));
    		translateToDiagram(allUML.get(i));

		}

		
    	UMLReaderGUI a = new UMLReaderGUI(new UMLReader());
    	a.run();
    		
    	    	
	}
	
	public static String showAllTraceOfMessage() {
		String str="";
		Object[] o = traceMsg.toArray();
		Object[] allM = originalMsg.toArray();
		for (int i = 0; i < allM.length; i++) {
			LinkedList<String> linklist = new LinkedList<>();
			str+= allM[i].toString().toUpperCase()+" = ";
			for (int j = 0; j < o.length; j++) {
					if(o[j].toString().contains(allM[i].toString()))
					{
						str+=o[j]+"->";
						linklist.add(o[j]+"");
					}
			}
			str+= allM[i].toString().toUpperCase()+"\n";
			traceMessage.put(allM[i]+"", linklist);
		}
		return str;
	}
	
	public static ArrayList<Map<String, ArrayList<String>>> readAllInput(String input,String name) {
		ArrayList<Map<String, ArrayList<String>>> allUML =  new ArrayList<>();
		equations = prepareInput(input.replace("\n", " "));
		String[] names = name.split(",");
    	Map<String,ArrayList<String>> map = new LinkedHashMap<>();
    	ArrayList<String> tmp = new ArrayList<>();
    	String t= "";
    	for (int i = 0; i < equations.length; i++) {
    		if(equations[i].equals("@startuml")){
    			count+=1;
    			if (equations[i+1].equals("[*]")) {
//					t = "M"+ ++STATE_DG+names[count-1];
					t = "M_"+names[count-1];
				}else{
//					t = "SQ"+ ++SEQUENCE_DG+names[count-1];
    				t = "SQ_"+names[count-1];
				}
    		}

    		tmp.add(equations[i]);
    		if(equations[i].equals("@enduml")){
    			map.put(t,tmp);
    			allUML.add(map);
    			map = new LinkedHashMap<>();
    			tmp = new ArrayList<>();
    		}
			
		}
    	
    	System.out.println("In PlantUNL"+allUML);
    	return allUML;
	}
	


	public static String[] prepareInput(String s){


		String[] ss = s.replace(">", "> ").replace("<", " <").split(" ");

		ArrayList<String> sl = new ArrayList<>();
		for (int i = 0; i < ss.length; i++) {
			if(ss[i].length()!=0)
			if(ss[i].contains(":")){
				if(sl.get(sl.size()-1).contains(">")||sl.get(sl.size()-1).contains("<")){
					int count =0;
					String[] g =ss[i].split(":");
					if(g.length==0){// this is :
						sl.add(ss[i]);
					}else if(g.length==2){// this is send and receive message
						sl.add(g[0]);
						sl.add(":");
						sl.add(g[1]);
					}
					else if(g.length==3){// this is send and receive message with name process
						sl.add(g[0]);
						sl.add(":");
						sl.add(g[1]+":"+g[2]);
					}
				}else{
					sl.add(ss[i]);
					System.out.println("Out :"+ss[i]);
				}

			}else sl.add(ss[i]);
			
		}
		String[] rs = new String[sl.size()] ;
		
		rs = sl.toArray(rs);
		
		return rs;
	}

	
	 public static  ArrayList<Diagram> translateToDiagram(Map<String,ArrayList<String>> map){
		 	Diagram diagram;
		 	System.out.println("IN TranslateToDiagram :");
	    	ArrayList<String> res = convertToArrayList(map.values().toString().replace("[", "").replace("]", "").split(", "));
	    	String state = map.keySet().toString().replace("[", "").replace("]", "");
	    	System.out.println("State :" +state);
	    	if(state.contains("M")){
	    		diagram = new StateDiagram(state);
	    		stateReader = new StateReader();
	    		diagram.addProcess(stateReader.processForStateDiagram(res));
	    		diagrams.add(diagram);
	    		System.out.println("TEST1 :"+diagram.toString());
	    	}else if(state.contains("SQ")){
	    		diagram = new SequenceDiagram(state);
	    		sequenceReader = new SequenceReader(res,diagrams, originalMsg, traceMsg);
	    		procSequence = sequenceReader.getResult();
	    		diagram.addProcess(procSequence);
	    		diagrams.add(diagram);
	    		System.out.println("Check is Ind :"+sequenceReader.isIndependentSequence());
	    		if(sequenceReader.isIndependentSequence()){
	    			Object[] tmp = sequenceReader.getAllTempStateDiagram().toArray();
	    			for (int i = 0; i < tmp.length; i++) {
	    				if(!findThisStateDiagram(((Diagram)tmp[i]).getName()))
			    		diagrams.add((Diagram)tmp[i]);
					}
	    			System.out.println("TEST STATE IND:"+diagrams);
	    		}
	    		System.out.println("TEST2 :"+diagram.toString());
	    		System.out.println("7/5/2017 :"+getRelationFrameWithSequenceDiagram((SequenceProcess) procSequence));
	    		System.out.println("RESULT SQIM1M2 :"+getRelationOfSequenceWithAllState()+"ENDD");
	    	}

	    	return diagrams;
	 }
	 
	 //For Print alt
	 public static Map<String, Map<String,LinkedList<String>>> getRelationOfSequenceWithAllState(){
		 Map<String, Map<String,LinkedList<String>>> result = new LinkedHashMap<>();
		 for (int a = 0; a < getAllSequenceDiagramName().length; a++) {
			 String s = "";
			 s+=getAllSequenceDiagramName()[a]+"I";
			 Map<String,LinkedList<String>> map = new LinkedHashMap<>();
			 LinkedList<String> listAllState = new LinkedList<>();
			 for (int i = 0; i < getAllStateDiagramName().length ; i++) {
				listAllState.add(getAllStateDiagramName()[i]+"");
				String[] ss= getAllStateDiagramName()[i].toString().split("_");  
				s+=ss[ss.length-1];
			}
			 map.put("states", listAllState);
			 LinkedList<String> listAllProcess = new LinkedList<>();
			 Object[] fchannel = frameChannel.toArray();
			 for (int i = 0; i < fchannel.length ; i++) {
				listAllProcess.add(fchannel[i]+"");
			}
			 map.put("condition", listAllProcess);
			 result.put(s, map);
		}
		 return result;
		 
	 }

	 

	 public String showRelationOfSequenceWithAllState(){
		 String s = "";
		 Map<String, Map<String,LinkedList<String>>> map = getRelationOfSequenceWithAllState();
		 System.err.println("MAP :"+map);
		 for (Entry<String, Map<String, LinkedList<String>>> mp : map.entrySet()) {
			s+= mp.getKey()+" = ";
			Map<String, LinkedList<String>> m = mp.getValue();
			System.err.println("Test m :"+mp);
			LinkedList<String> st = m.get("states");
			s+=st.get(0);
			LinkedList<String> cond = getOnlyFrameProcessBoundary(m.get("condition"));
			String c = "[|{";
			for (int i = 0; i < cond.size(); i++) {
				c+=cond.get(i);
				if(i<cond.size()-1) c+=",";
			}
			c+="}|]";
			s+=c+getRightStateForPrint(st, 1, c);
		}
		 return s;
		 
	 }
	 
	 public String getRightStateForPrint(LinkedList<String> s, int i,String con){
		 String r = "";
		 for (int j = i; j < s.size(); j++) {
			if(j==s.size()-1) r+=s.get(j); 
			else{
				r+="("+s.get(j)+con+getRightStateForPrint(s, j+1, con)+")";
				break;
			}
		}
		 return r;
	 }
	 
	 public LinkedList<String> getOnlyFrameProcessBoundary(LinkedList<String> cond){
		LinkedList<String> ll =new LinkedList<>();
			for (int i = 0; i < cond.size(); i++) {
				 if(cond.get(i).contains("_b") || cond.get(i).contains("_e")) ll.add(cond.get(i));
			}
		return ll;
	 }
	 
	public String getRelationFrameWithSequenceDiagram() {
		return getRelationFrameWithSequenceDiagram((SequenceProcess) procSequence);
	}
	
	public String getCSP(){
		String string = "";
		string += showChannel();

		Object[] s = showAllStateDiagram().toArray();
		for (int i = 0; i < s.length; i++) {
			string += s[i]+"\n";
		}
		
		string += showSequenceDiagram()+"\n";

		if(getFrameChannel().size()>0)
		string += getRelationFrameWithSequenceDiagram();

		string += showRelationOfStateDiagram()+"\n";

		string += showAllTraceOfMessage()+"\n";

		string += showRelationOfAllMessage()+"\n";
		
		string += showRelationWithSMIAndMSG()+"\n";

//		string += "Relation between Sequence:\n";
		if(getFrameChannel().size()>0)
		string += showTheRelationBetweenSequenceDiagramAndMessage()+"\n";
		return string;
	}
	
	public static Set<String> getFrameChannel() {
		 return frameChannel;
	}
	
	public static boolean findThisStateDiagram(String name){
		for (int i = 0; i < diagrams.size(); i++) {
			if(diagrams.get(i).getName().equals(name)) return true;
		}
		return false;
	}
	 
	 
	 public Map<String, LinkedList<String>> getRelationOfStateDiagram(){
		 LinkedList<String> str= new LinkedList<>();
		 Map<String, LinkedList<String>> map = new LinkedHashMap<>();
		 Object[] names= getAllStateDiagramName();
		 for (int i = 0; i < names.length; i++) {
			 str.add(names[i]+"");
		}
		 map.put("SMI", str);
		 return map;
	 }
	 
	 public String showRelationOfStateDiagram(){
		 String str="";
		 Map<String, LinkedList<String>> m = getRelationOfStateDiagram();
		 for (Entry<String, LinkedList<String>> eachLine : m.entrySet()) {
			str+=eachLine.getKey().toUpperCase()+" = ";
			for (int i = 0; i < eachLine.getValue().size(); i++) {
				str+=eachLine.getValue().get(i);
				if(i<eachLine.getValue().size()-1) str+=" ||| ";
				
			}
		}
		 return str;
	 }

	 public Map<String,LinkedList<String>> getRelationOfAllMessage(){
		 LinkedList<String> str= new LinkedList<>();
		 Map<String, LinkedList<String>> map = new LinkedHashMap<>();
		 for (Entry<String, LinkedList<String>> eachLine : traceMessage.entrySet()) {
			 str.add(eachLine.getKey().toUpperCase());
		}
		map.put("MSG", str); 
		return map;
	 }
	 
	 public static String getRelationFrameWithSequenceDiagram(SequenceProcess proc){
		 String s ="";
		 System.out.println("TRACKER :"+proc.getFrames());
		 for (int i = 0; i < proc.getFrames().size(); i++) {
			LinkedList<Map<String, LinkedList<LinkedList<String>>>> elem = proc.getFrames().get(i);
			System.out.println("7/2/2017 :"+elem);
			for (int j = 0; j < elem.size(); j++) {
				for (Entry<String, LinkedList<LinkedList<String>>> map : elem.get(j).entrySet()) {
					s += map.getKey()+" = "+map.getKey()+"_ALT"+"\n"+map.getKey()+"_ALT = ";
					for (int k = 0; k < map.getValue().size(); k++) {
						System.out.println("DEBUG :"+map.getValue());
						s += map.getKey().split("_")[0].toLowerCase()+"_b -> ";
						frameChannel.add(map.getKey().split("_")[0].toLowerCase()+"_b");
						LinkedList<String> tl = makeForBlankSpaceProc(map.getValue().get(k));
						for (int k2 = 0; k2 < tl.size(); k2++) {
							s += formatFrame(tl.get(k2),k2+1,map.getKey().split("_")[0].toLowerCase());
							if(k2<tl.size()-1) s+=" [] ";
						}
					}
					s+="\n";
				}
			}
		 }
		 return s;
	 }
	 
	 private static LinkedList<String> makeForBlankSpaceProc(LinkedList<String> ll) {
		 if(ll.size()==1){
			ll.add("");
		}
		return ll;
	}
	 
	 public static Map<String ,Map<String,LinkedList<String>>> getRelationWithFrame(SequenceProcess proc){
		 String s ="";
		 Map<String ,Map<String,LinkedList<String>>> m = new LinkedHashMap<>();
		 System.out.println("TRACKER :"+proc.getFrames());
		 for (int i = 0; i < proc.getFrames().size(); i++) {
			String n=""; 
			LinkedList<Map<String, LinkedList<LinkedList<String>>>> elem = proc.getFrames().get(i);
			System.out.println("7/2/2017 :"+elem);
			LinkedList<String> state = new LinkedList<>();
			for (int j = 0; j < elem.size(); j++) {
				for (Entry<String, LinkedList<LinkedList<String>>> map : elem.get(j).entrySet()) {
					n = map.getKey().split("_")[0];
					state.add(map.getKey());
				}
			}
			Map<String,LinkedList<String>> ml = new LinkedHashMap<>();
			ml.put("state", state);
			LinkedList<String> process = new LinkedList<>();
			Object[] o = getFrameChannel().toArray();
			for (int j = 0; j < o.length; j++) {
				if(o[j].toString().contains(n.toLowerCase()))
				process.add(o[j]+"");
			}
			ml.put("condition", process);
			n+="_";
			for (int j = 0; j < state.size(); j++) {
				String[] t= state.get(j).split("_");
				n+=t[t.length-1];
			}
			m.put(n, ml);
		 }
		 return m;
	 }
	 
		 public String showRelationWithFrame(){
			 String s = "";
			 Map<String, Map<String,LinkedList<String>>> map = getRelationWithFrame((SequenceProcess)procSequence);
			 for (Entry<String, Map<String, LinkedList<String>>> mp : map.entrySet()) {
				s+= mp.getKey()+" = ";
				Map<String, LinkedList<String>> m = mp.getValue();
				LinkedList<String> st = m.get("state");
				s+=st.get(0)+"[|{";
				LinkedList<String> cond = m.get("condition");
				String c = "[|{";
				for (int i = 0; i < cond.size(); i++) {
					c+=cond.get(i);
					if(i<cond.size()-1) c+=",";
				}
				c+="}|]";
				s+=c+getRightStateForPrint(st, 1, c)+"\n";
			}
			 return s;
			 
		 }
		
		
	 public static String formatFrame(String msg,int i,String name){
		 frameChannel.add(name+"_alt"+i);
		 frameChannel.add(name+"_e");
		 if(msg.length()==0) return "("+name+"_alt"+i+" -> "+name+"_e"+" -> SKIP)";
		 return "("+name+"_alt"+i+" -> "+msg+" -> "+name+"_e"+" -> SKIP)";
	 }

	 public String showRelationOfAllMessage(){
		 String str = "";
		 Map<String, LinkedList<String>> map = getRelationOfAllMessage();
		 for (Entry<String, LinkedList<String>> eachLine:map.entrySet()) {
			 str+=eachLine.getKey()+" = ";
			 for (int i = 0; i < eachLine.getValue().size(); i++) {
				 str+=eachLine.getValue().get(i);
				 if(i<eachLine.getValue().size()-1) str+=" ||| ";
				
			}
			 
		}
		 return str+"\n";
	 }
	 
	 public Map<String ,Map<String,String>> getRelationWithSMIAndMSG(){
		 Map<String,String> map = new HashMap<String,String>();
		 map.put("state_diagram", showRelationOfStateDiagram().split(" = ")[0]);
		 map.put("condition", showAllProcess().toString().substring(1, showAllProcess().toString().length()-1));
		 map.put("message", showRelationOfAllMessage().split(" = ")[0]);
		 Map<String, Map<String,String>> m = new HashMap<String, Map<String,String>>();
		 m.put("SM", map);
		 return m;
	 }

	 public String showRelationWithSMIAndMSG(){
		 String s="";
		 Map<String ,Map<String,String>> map = getRelationWithSMIAndMSG();
		 for (Entry<String, Map<String, String>> m : map.entrySet()) {
			s+=m.getKey()+" = "+m.getValue().get("state_diagram")+"[|{"+m.getValue().get("condition")+"}|]"+m.getValue().get("message");
		}
		 return s+"\n";
	 }
	 
	 public String showSequenceDiagram(){
		 String s="";
		 Map<String, LinkedList<String>> map = getSequenceDiagram();
		 for (Entry<String, LinkedList<String>> mp : map.entrySet()) {
			String[] tmp = mp.getKey().split("_");
			s+=tmp[tmp.length-1]+" = ";
			for (int i = 0; i < mp.getValue().size(); i++) {
				s+=mp.getValue().get(i);
				if(i<mp.getValue().size()-1) s+= "->";
			}
			s+="\n";
		}
		 return s;
	 }


	 public LinkedList<String> getLinkedListForInd(LinkedList<Map<String, Map<String, Map<String, String>>>> procs,String name){
		 LinkedList<String> l= new LinkedList<>();
		 for (int j = 0; j < procs.size(); j++) {
			for (Entry<String, Map<String, Map<String, String>>> string : procs.get(j).entrySet()) {
				for (Entry<String, Map<String, String>> map2 : string.getValue().entrySet()) {
					for (Entry<String, String> s :map2.getValue().entrySet()) {
						System.out.println(map2.getKey()+"LOST :"+s.getKey()+" = "+name);
						if(s.getKey().equals(name)){
							l.add(map2.getKey()); 
							System.out.println("&^&L :"+l);
						}
						
					}
					if(map2.getKey().charAt(0)=='f' && (map2.getKey().contains("_e") ||map2.getKey().contains("_b"))){
						l.add(map2.getKey());
					}
				}
			} 

			
		}
		 return l;
	 }
	 
	 public LinkedList<String> getLinkedListForSq(LinkedList<Map<String, Map<String, Map<String, String>>>> procs,String state){
		 LinkedList<String> ll = new LinkedList<>();
		 System.out.println("Check for F1 bug :"+procs);
		 for (int j = 0; j < procs.size(); j++) {
			for (Entry<String, Map<String, Map<String, String>>> map2 : procs.get(j).entrySet()) {
				for (Entry<String, Map<String, String>> string : map2.getValue().entrySet()) {
					System.err.println("State :"+state+" = "+string);
					if(string.getKey().charAt(0)=='f' && (string.getKey().contains("_e") ||string.getKey().contains("_b"))){
						ll.add(string.getKey());
					}else{
						for (Entry<String, String> l : string.getValue().entrySet()) {
							if(l.getKey().equals(state)){
								ll.add(l.getValue());
							}
						}
					}
				}
			}
		}
		 return ll;
	 }
	 
	 public Map<String, LinkedList<String>> getSequenceDiagram(){
		 Map<String, LinkedList<String>> map = new LinkedHashMap<>();
		 List<String> ck = new ArrayList<>();
		 if(sequenceReader.isIndependentSequence()){
			 for (int i=0;i<getAllSequenceDiagram().size();i++) {
				 String name  = getAllSequenceDiagram().get(i).getName();
				 System.out.println("GET ALL SQ SIZE :"+getAllSequenceDiagram().get(i).getName());
				 LinkedList<Map<String, LinkedList<LinkedList<String>>>> procs = getAllSequenceDiagram().get(i).getProcesses().getProcessListByName();
				 //All Diagram in ArrayList
				 for (int j = 0; j < procs.size(); j++) {
					 String curr="";
					 System.out.println("Procs SIZE :"+procs.get(j));
					LinkedList<String> ll = new LinkedList<>();
					for (Entry<String, LinkedList<LinkedList<String>>> map2 : procs.get(j).entrySet()) {
						System.out.println("<Map2> :"+map2);
						for (int k = 0; k < map2.getValue().size(); k++) {
							ll = getLinkedListForSq(getAllSequenceDiagram().get(i).getProcesses().getProcessListAlt(),map2.getValue().get(k).get(0));
							ll.add("SKIP");
							map.put(name+"_"+map2.getValue().get(0).get(0), ll);
							for (int k2 = 0; k2 < diagrams.size(); k2++) {
								System.err.println("What the res:"+diagrams.get(k2)+" = "+(map2.getValue().get(0).get(0)));
								if(diagrams.get(k2).getName().equals(map2.getValue().get(0).get(0))){
									ProcessList pc = new StateProcess();
									LinkedList<LinkedList<String>> r = new LinkedList<>();
									r.add(ll);
									pc.addProcess("from_sq", r, "");
									diagrams.get(k2).addProcess(pc);
								}
							}
							System.out.println("Update diagrams :"+diagrams);
						}
						
					}
				}
			}
		 }
		 else
		 for (int i=0;i<getAllSequenceDiagram().size();i++) {
			 String name  = getAllSequenceDiagram().get(i).getName();
			 LinkedList<Map<String, LinkedList<LinkedList<String>>>> procs = getAllSequenceDiagram().get(i).getProcesses().getProcessListByName();
			 //All Diagram in ArrayList
			 System.out.println("Procs "+name+":");
			 System.out.println(procs);
			 for (int j = 0; j < getAllStateDiagram().size(); j++) {
				 LinkedList<String> ll = new LinkedList<>();
				 name += getAllStateDiagram().get(j).getName().substring(2);
				 LinkedList<Map<String, LinkedList<LinkedList<String>>>> keys = getAllStateDiagram().get(j).getProcesses().getProcessListByName();
				 //Each Sequence Diagram : format map 
				 System.out.println("======================="+name);
				 for (int k = 0; k < procs.size(); k++) {
					 for (Entry<String, LinkedList<LinkedList<String>>> map2 : procs.get(k).entrySet()) {
						 //State 
//						 System.out.println("<<<<<<<<<<"+map2.getKey());
//						 System.out.println("***************");
						 
						 for (int l = 0; l < keys.size(); l++) {
							 for (Entry<String, LinkedList<LinkedList<String>>> map3 : keys.get(l).entrySet()) {
//								 System.out.println(">>>>>>>>>>"+map3.getKey());
								 if(map3.getKey().contains(map2.getKey()) && !ck.contains(map2.getKey()))
								 {
									 ck.add(map2.getKey());
									 ll.add(map2.getKey());
								 }
							}
						} 
						 ck = new ArrayList<>();
//						 System.out.println("***************");
						 
					}
						
				}
//				 System.out.println("=======================");
				 ll.add("SKIP");
				 map.put(name,ll);
				 name =getAllSequenceDiagram().get(i).getName();
			}
						
		}
		 System.out.println("$$"+map);
		 return map;
	 }
	 
	 
	 public static Object[] getAllStateDiagramName(){
		Set<String> s = new LinkedHashSet<>();
		for (int i = 0; i < getAllStateDiagram().size(); i++) {
			s.add(getAllStateDiagram().get(i).getName());
		}
		return s.toArray();
	 }
	 
	 public static ArrayList<Diagram> getAllStateDiagram(){
		ArrayList<Diagram> s = new ArrayList<>();
		for (int i = 0; i < diagrams.size(); i++) {
			if(diagrams.get(i).getName().contains("M_"))
			s.add(diagrams.get(i));
		}
		return s;
	 }

	 public static Object[] getAllSequenceDiagramName(){
		LinkedList<String> s = new LinkedList<>();
		for (int i = 0; i < getAllSequenceDiagram().size(); i++) {
			s.add(getAllSequenceDiagram().get(i).getName());
		}
		return s.toArray();
	 }

	 public static ArrayList<Diagram> getAllSequenceDiagram(){
		ArrayList<Diagram> s = new ArrayList<>();
		for (int i = 0; i < diagrams.size(); i++) {
			if(diagrams.get(i).getName().contains("SQ_"))
			s.add(diagrams.get(i));
		}
		return s;
	 }
	 
	 public static Set<String> showAllProcess(){
		 Set<String> processes = new LinkedHashSet<>();
		 for (int n = 0;n<diagrams.size();n++) {
	    		for (int i = 0; i < diagrams.get(n).getProcesses().getProcessListByName().size(); i++) {
	    			for ( Entry<String, LinkedList<LinkedList<String>>> line : diagrams.get(n).getProcesses().getProcessListByName().get(i).entrySet()) {
	        			if(!line.getKey().equals("NaN") && !line.getKey().contains(">"))
	        				processes.add(line.getKey());
					}

				}
			}
		 return processes;
	 }
	 
	 public static String showChannel(){
		 	String s = "";
			Set<String> allProcess = showAllProcess();
			s+="channel "+allProcess.toString().substring(1, allProcess.toString().length()-1)+"\n\n";
			
			if(getFrameChannel().size()>0){
			Set<String> allFrameProcess = getFrameChannel();
			s+="channel "+allFrameProcess.toString().substring(1, allFrameProcess.toString().length()-1)+"\n\n";
			}
			return s;
	 }

	 public static Set<String> showAllStateDiagram(){
		 Set<String> processes = new LinkedHashSet<>();
		 for (int i =0;i<diagrams.size();i++) {
		    	if (diagrams.get(i).getName().contains("M_"))
		    		processes.add(getAllSendAndReceiveMessageForState(diagrams.get(i).getName()));	
			}
		 return processes;
	 }
	 
	 public Map<String, LinkedList<String>> getTheRelationBetweenSequenceDiagramAndMessage(){
		 Map<String, LinkedList<String>> map = new LinkedHashMap<>();
		 for (int i = 0; i < getAllSequenceDiagramName().length; i++) {
			String name = getAllSequenceDiagramName()[i].toString();
			LinkedList<String> linkedlist = new LinkedList<>();
			System.out.println("7/6/17 Map0:"+getSequenceDiagram());
			for (Entry<String, LinkedList<String>> s : getSequenceDiagram().entrySet()) {
				System.out.println("7/6/17 Map2:"+s);
				System.out.println("7/6/17 Map3:"+s.getKey()+" = "+(name));
				if(s.getKey().contains(name)){
					linkedlist.add(s.getKey());
				}		
			}
			map.put(name+"I", linkedlist);
			linkedlist = new LinkedList<>();
		}

		 return map;
	 }
	 
	 public String showAssert(){
		 String str ="";
		 Map<String, LinkedList<String>> m = getTheRelationBetweenSequenceDiagramAndMessage();
		 for (Entry<String, LinkedList<String>> map : m.entrySet()) {
			str+= "assert SM [T= "+map.getKey().replace("I", "")+"\n";
		}
		 return str;
	 }
	 
	 public String showTheRelationBetweenSequenceDiagramAndMessage(){
		 String str="";
		 Map<String, LinkedList<String>> map = getTheRelationBetweenSequenceDiagramAndMessage();
		 System.out.println("7/6/17 Map :"+map);
		 if(sequenceReader.isIndependentSequence()){
//			 	str+=getRelationFrameWithSequenceDiagram();
//				
				str+=showRelationOfSequenceWithAllState()+"\n";
				
				str+=showRelationWithFrame();
				
				str+=showTheRelationBetweenFrameSequenceDiagramAndMessage()+"\n";
				
		 }else
			for (Entry<String, LinkedList<String>> s : map.entrySet()) {
				str+=s.getKey()+" = ";
				for (int i = 0; i < s.getValue().size(); i++) {
					String[] tmp = s.getValue().get(i).split("_");
					str+=tmp[tmp.length-1];
					if(i<s.getValue().size()-1) str+=" ||| ";
				}
				str+="\n";
				
				str+=s.getKey().substring(0,s.getKey().length()-1)+" = "+s.getKey()+"[|{";
				str+=showAllProcess().toString().substring(1, showAllProcess().toString().length()-1);
				str+="}|]"+showRelationOfAllMessage().split(" = ")[0]+"\n\n";
			}
			
			

		 return str;
	 }
	 
	 
	 public static String getAllSendAndReceiveMessageForState(String nowstate){

			String res="";
			for (int n = 0;n<diagrams.size();n++)
			{
				String namestate = diagrams.get(n).getName();
			    LinkedList<Map<String, LinkedList<LinkedList<String>>>> l = diagrams.get(n).getProcesses().getProcessListByState();
			    if(namestate.equals(nowstate))
			    for (int i = 0; i < l.size(); i++) {
					for (Entry<String, LinkedList<LinkedList<String>>> entry2 : l.get(i).entrySet())
					{
						if(entry2.getKey().equals("*")) res+= namestate+" = ";
						else res += entry2.getKey()+" = ";
						for (int j = 0; j < entry2.getValue().size(); j++) {
							if(entry2.getValue().size()>1)res+="(";
							for (int k = 0; k < entry2.getValue().get(j).size(); k++) {
								if(entry2.getValue().get(j).get(k).equals("NaN")){
									k++;
									continue;
								}
								res += entry2.getValue().get(j).get(k);
//								if(k%3==1) res += " - >";
								
							}
							if(entry2.getValue().size()>1) res += ") ";
							if(j<entry2.getValue().size()-1) res+="[] ";

						}
						res+="\n";
						    
					}
					
				}
			}


			return res;
	}
	
	 

    

	
	 
	 private static ArrayList<String> convertToArrayList(Object[] in) {
		ArrayList<String> res = new ArrayList<>();
		 for (int i = 0; i < in.length; i++) {
			res.add(in[i]+"");
		}
		 return res;
	}
	 
	 
	 public static LinkedList<LinkedList<String>> getListOfProcessForMessage(String checkLeft,Map<String, LinkedList<LinkedList<String>>> map){
		 LinkedList<LinkedList<String>> list= new LinkedList<>();
		 for (Entry<String, LinkedList<LinkedList<String>>> m : map.entrySet()) {
			 for (int i = 0; i < m.getValue().size(); i++) {
				 list.add(m.getValue().get(i));
				
			}

		}
		 return list;
	 }
	 
	 public static Map<String, LinkedList<LinkedList<String>>> combineMessage(Map<String, LinkedList<LinkedList<String>>> map){
		 Map<String, LinkedList<LinkedList<String>>> mp = new LinkedHashMap<>();
		 List<String> trace = new ArrayList<>();
		 for (Entry<String, LinkedList<LinkedList<String>>> m : map.entrySet()) {
			if(!trace.contains(m.getKey())){
				mp.put(m.getKey(),getListOfProcessForMessage(m.getKey(), map));
				trace.add(m.getKey());
			}

		}
		 return mp;
	 }

	 public static Map<String, LinkedList<String>> checkMap(Map<String, LinkedList<String>> map){
		 Map<String, LinkedList<String>> resMap = new LinkedHashMap<>();
		 for (Entry<String, LinkedList<String>> emap : map.entrySet()) {
			if(emap.getKey().contains("->")) {
				String[] arr = emap.getKey().split(" -> ");
				LinkedList<String> origin = emap.getValue();
				LinkedList<String> left = new LinkedList<>();
				left.add(origin.get(0));
				left.add(origin.get(1));
				left.add(origin.get(0));
				LinkedList<String> right = new LinkedList<>();
				right.add(origin.get(0));
				right.add(origin.get(1));
				right.add(origin.get(2));
				
				 resMap.put(arr[0], left);
				 resMap.put(arr[1], right);
 			}else
			 resMap.put(emap.getKey(), emap.getValue());
			
		}
		 return resMap;
	 }

	public Map<String, LinkedList<String>> getTheRelationBetweenFrameSequenceDiagramAndMessage() {
		Map<String, LinkedList<String>> resM = new LinkedHashMap<>();
		Map<String, Map<String,LinkedList<String>>> map = getRelationOfSequenceWithAllState();
		Map<String, Map<String,LinkedList<String>>> map2 = getRelationWithFrame((SequenceProcess)procSequence);
		LinkedList<String> l1 = new LinkedList<>();
		LinkedList<String> l2 = new LinkedList<>();
		for (Entry<String, Map<String, LinkedList<String>>> m : map.entrySet()) {
			l1.add(m.getKey());
			l2.add(m.getKey().split("I")[0]);
		}
		resM.put("squenceFrame",l1 );
		resM.put("name",l2);
		System.out.println("2 BUG :"+map2);
		LinkedList<String> l3 = new LinkedList<>();
		Set<String> l4 = new LinkedHashSet<>();
		for (Entry<String, Map<String, LinkedList<String>>> m2 : map2.entrySet()) {
			l3.add(m2.getKey());
			for (Entry<String, LinkedList<String>> mm : m2.getValue().entrySet()) {
				if(mm.getKey().equals("condition"))
				for (int i = 0; i < mm.getValue().size(); i++) {
					l4.add(mm.getValue().get(i));
					System.err.println(mm.getValue().get(i));
				}
			}
		}
		Object[] s = l4.toArray();
		LinkedList<String> l5 = new LinkedList<>();
 		for (int i = 0; i < s.length; i++) {
			l5.add(s[i]+"");
		}
		resM.put("sequenceState", l3);
		resM.put("condition", l5);
		return resM;
	}
	 
	public String showTheRelationBetweenFrameSequenceDiagramAndMessage() {
		Map<String, LinkedList<String>> resM = getTheRelationBetweenFrameSequenceDiagramAndMessage();
		String s = resM.get("name").get(0)+" = "+resM.get("squenceFrame").get(0);
		String c = "[|{";
		LinkedList<String> l = resM.get("condition");
		for (int i = 0; i < l.size(); i++) {
			c+=l.get(i);
			if(i<l.size()-1)c+=",";
		}
		c+="}|]";
		s+=c+getRightStateForPrint(resM.get("sequenceState"), 1,c );
		
		return s;
	}
	 
	 

	 
	 
	 









}