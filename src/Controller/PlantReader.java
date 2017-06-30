package Controller;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Model.Diagram;
import Model.SequenceDiagram;
import Model.SequenceProcess;
import Model.StateDiagram;
import Model.StateProcess;
import Model.ProcessList;
import View.UMLReader;
import View.UMLReaderGUI;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedHashSet;

import javax.naming.spi.DirStateFactory.Result;
import javax.sound.midi.Sequence;

public class PlantReader {
	
//	private static Map<String,LinkedList<Map<String,LinkedList<String>>>> traceData;
	private static String input="";
	private static String[] equations;
	private static int count=0;
	private static Integer STATE_DG=0;
	private static Integer SEQUENCE_DG=0;
	private static ArrayList<Map<String,ArrayList<String>>> allUML = new ArrayList<>();
	
	private static ArrayList<Diagram> diagrams;
	private static Set<String> traceMsg = new LinkedHashSet<>();
	private static Set<String> originalMsg = new LinkedHashSet<>();
	
	private static Map<String, LinkedList<String>> traceMessage = new LinkedHashMap<>();

	static SequenceReader sequenceReader;
	static StateReader stateReader;
	
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
	    		diagram.addProcess(sequenceReader.getResult());
	    		System.out.println("[******]"+sequenceReader.isIndependentSequence());
	    		diagrams.add(diagram);
	    		System.out.println("TEST2 :"+diagram.toString());
	    	}

	    	return diagrams;
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
			s+=mp.getKey()+" = ";
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
					if(map2.getKey().contains("f1_")){
						l.add(map2.getKey());
					}
				}
			} 

			
		}
		 return l;
	 }
	 
	 public LinkedList<String> getLinkedListForSq(LinkedList<Map<String, Map<String, Map<String, String>>>> procs,String state){
		 LinkedList<String> ll = new LinkedList<>();
		 for (int j = 0; j < procs.size(); j++) {
			for (Entry<String, Map<String, Map<String, String>>> map2 : procs.get(j).entrySet()) {
				for (Entry<String, Map<String, String>> string : map2.getValue().entrySet()) {
					System.err.println("State :"+state+" = "+string);
					if(string.getKey().contains("f1_")){
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
				 String name  = "";
				 System.out.println("GET ALL SQ SIZE :"+getAllSequenceDiagram().size());
				 LinkedList<Map<String, LinkedList<LinkedList<String>>>> procs = getAllSequenceDiagram().get(i).getProcesses().getProcessListByName();
				 //All Diagram in ArrayList
				 for (int j = 0; j < procs.size(); j++) {
					 String curr="";
					 System.out.println("Procs SIZE :"+procs.size());
					LinkedList<String> ll = new LinkedList<>();
					for (Entry<String, LinkedList<LinkedList<String>>> map2 : procs.get(j).entrySet()) {
						System.out.println("<Map2> :"+map2);
						for (int k = 0; k < map2.getValue().size(); k++) {
							ll = getLinkedListForSq(getAllSequenceDiagram().get(i).getProcesses().getProcessListAlt(),map2.getValue().get(k).get(0));
							ll.add("SKIP");
							map.put(map2.getValue().get(0).get(0), ll);
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
	 
	 
	 public Object[] getAllStateDiagramName(){
		Set<String> s = new LinkedHashSet<>();
		for (int i = 0; i < getAllStateDiagram().size(); i++) {
			s.add(getAllStateDiagram().get(i).getName());
		}
		return s.toArray();
	 }
	 
	 public ArrayList<Diagram> getAllStateDiagram(){
		ArrayList<Diagram> s = new ArrayList<>();
		for (int i = 0; i < diagrams.size(); i++) {
			if(diagrams.get(i).getName().contains("M_"))
			s.add(diagrams.get(i));
		}
		return s;
	 }

	 public Object[] getAllSequenceDiagramName(){
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
			for (Entry<String, LinkedList<String>> s : getSequenceDiagram().entrySet()) {
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
			for (Entry<String, LinkedList<String>> s : map.entrySet()) {
				str+=s.getKey()+" = ";
				for (int i = 0; i < s.getValue().size(); i++) {
					str+=s.getValue().get(i);
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
	 
	 
	 

	 
	 
	 









}