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
    	System.out.println("EQ :"+equations.length);
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
    		System.err.println("EQ"+i+" : "+equations[i] + equations[i].length());
    		if(equations[i].equals("@enduml")){
    			map.put(t,tmp);
    			System.out.println("Map :"+map);
    			System.out.println("AllUML :"+allUML);
    			System.out.println();
    			allUML.add(map);
    			System.out.println("ALL :"+allUML);
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
	    		diagram.addProcess(processForStateDiagram(res));
	    		diagrams.add(diagram);
	    		System.out.println("TEST1 :"+diagram.toString());
	    	}else if(state.contains("SQ")){
	    		diagram = new SequenceDiagram(state);
	    		diagram.addProcess(getResult(res));
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
			s+=m.getKey()+" = "+m.getValue().get("state_diagram")+"[|"+m.getValue().get("condition")+"|]"+m.getValue().get("message");
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

	 
	 public Map<String, LinkedList<String>> getSequenceDiagram(){
		 Map<String, LinkedList<String>> map = new LinkedHashMap<>();
		 
		 for (int i=0;i<getAllSequenceDiagram().size();i++) {
			 String name  = getAllSequenceDiagram().get(i).getName();
			 LinkedList<Map<String, LinkedList<LinkedList<String>>>> procs = getAllSequenceDiagram().get(i).getProcesses().getProcessList();
			 //All Diagram in ArrayList
			 System.out.println("Procs "+name+":");
			 System.out.println(procs);;
			 for (int j = 0; j < getAllStateDiagram().size(); j++) {
				 LinkedList<String> ll = new LinkedList<>();
				 name += getAllStateDiagram().get(j).getName().substring(2);
				 LinkedList<Map<String, LinkedList<LinkedList<String>>>> keys = getAllStateDiagram().get(j).getProcesses().getProcessList();
				 //Each Sequence Diagram : format map 
				 for (int k = 0; k < procs.size(); k++) {
					 for (Entry<String, LinkedList<LinkedList<String>>> map2 : procs.get(k).entrySet()) {
						 //State 
						 for (int l = 0; l < keys.size(); l++) {
							 for (Entry<String, LinkedList<LinkedList<String>>> map3 : keys.get(l).entrySet()) {
								 if(map3.getKey().contains(map2.getKey()))
								 {
									 ll.add(map2.getKey());
								 }
							}
							
						} 
					}
						
				}
				 ll.add("SKIP");
				 map.put(name,ll);
				 name =getAllSequenceDiagram().get(i).getName();
			}
						
		}
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

	 public ArrayList<Diagram> getAllSequenceDiagram(){
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
	    		for (int i = 0; i < diagrams.get(n).getProcesses().getProcessList().size(); i++) {
	    			for ( Entry<String, LinkedList<LinkedList<String>>> line : diagrams.get(n).getProcesses().getProcessList().get(i).entrySet()) {
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
		    		processes.add(getAllSendAndReceiveMessage(diagrams.get(i).getName()));	
			}
		 return processes;
	 }
	 
	 public static ArrayList<String> showAllSequenceDiagram(){
		 ArrayList<String> processes = new ArrayList<>();
		 for (int i =0;i<diagrams.size();i++) {
		    	if (diagrams.get(i).getName().contains("SQ_"))
		    		processes.add(getAllSendAndReceiveMessage(diagrams.get(i).getName()));	
			}
		 return processes;
	 }
	 

	 
	 public static String getAllSendAndReceiveMessage(String nowstate){

			String res="";
			
			for (int n = 0;n<diagrams.size();n++)
			{
				String namestate = diagrams.get(n).getName();
			    LinkedList<Map<String, LinkedList<LinkedList<String>>>> l = diagrams.get(n).getProcesses().getProcessList();
			    if(namestate.equals(nowstate))
			    for (int i = 0; i < l.size(); i++) {
					for (Entry<String, LinkedList<LinkedList<String>>> entry2 : l.get(i).entrySet())
					{
						for (int k = 0; k < entry2.getValue().size() ; k++) {
							LinkedList<String> linklist = entry2.getValue().get(k);
							for (int j = 0; j < linklist.size(); j++) {
								if(j%3==1) res+= "= ";
								else{
									if(linklist.get(j).equals("*")) res += namestate+" ";
									else res+= linklist.get(j)+" ";
								}
								if(j%3==1 && !entry2.getKey().equals("NaN") ) res+= entry2.getKey()+" -> ";
							}
						    res += "\n";
						}
						    
					}
					
				}
			}

			return res;
	}
	

    
    public static String readAction(String[] message){
    	String rs="";

    	for (int i = 0; i < message.length; i++) {
			if (message[i].length()!=0) {
				System.out.println("MN :"+message[i]);
				rs+=message[i];
				if(i<message.length-1) rs+= " -> ";
			}
			
		}
    	if(rs.length()==0) return "NaN";
    	return rs;
    }
	
	 
	 private static ArrayList<String> convertToArrayList(Object[] in) {
		ArrayList<String> res = new ArrayList<>();
		 for (int i = 0; i < in.length; i++) {
			res.add(in[i]+"");
		}
		 return res;
	}
	 
	 public static ProcessList processForStateDiagram(ArrayList<String> res) {
		System.out.println("Process State");
		String state = "n";
		ProcessList rs = new StateProcess();
		for (int j = 1; j < res.size()-1; j++) {
			String tmp = res.get(j);
			LinkedList<String> ll = new LinkedList<>();

			if(tmp.contains("<")){
	    		ll.add(res.get(j+1));
	    		ll.add(tmp.replace("<", "")+">");
	    		ll.add(res.get(j-1));
	    		state = "r_";
	    	}else if(tmp.contains(">")){
	    		ll.add(res.get(j-1));
	    		ll.add(tmp);
	    		ll.add(res.get(j+1));
	    		state = "s_";
	    	}
	    	else continue;
			String msg;
			if(j+3>=res.size()) msg = "NaN";
			else msg = res.get(j+3);
//			System.out.println(j+" Result :"+res.get(j+3));
			Map<String, LinkedList<LinkedList<String>>> map = new LinkedHashMap<>();
    		
    		if(msg.contains("<")|| msg.contains(">") ) msg="NaN";
    		System.out.println("Message :"+readAction(messageWithStatus(msg)));
    		LinkedList<LinkedList<String>> list = new LinkedList<>();
    		list.add(ll);
    		System.out.println("TEST0 :"+list);
    		rs.addProcess(readAction(messageWithStatus(msg)),list);
		}
		
		
		return rs;			

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
	 
	 
	 public static ProcessList getResult(ArrayList<String> res){
		 String newMsg="",left="",right="",state="";
		 boolean isWait;
		 ProcessList list=new SequenceProcess();
		 for (int i = 1; i < res.size()-1; i++) {
			 if (res.get(i+1).contains(">")) {
				left = res.get(i);
				right = res.get(i+2);
				newMsg = "s_"+res.get(i+4);
			}else if(res.get(i+1).contains("<")){
				left = res.get(i+2);
				right = res.get(i);
				newMsg = "r_"+res.get(i+4);
			}else continue;

			 originalMsg.add(res.get(i+4));
			 System.out.println(left+" > "+newMsg+" > "+right);
			 System.out.println("LEFT -> RIGHT by "+newMsg);
			 System.out.println(getLinkedFromtrace(left,newMsg));
			 Map<String, LinkedList<LinkedList<String>>> m = new LinkedHashMap<>();
			 LinkedList<LinkedList<String>> leftL = getLinkedFromtrace(left, newMsg.substring(2, newMsg.length()));

			 System.out.println("===============================");
			 System.out.println("RIGHT -> LEFT by "+newMsg);
			 System.out.println(getLinkedFromtrace(right,newMsg));
			 LinkedList<LinkedList<String>> rightL = getLinkedFromtrace(right, newMsg.substring(2, newMsg.length()));

			 System.out.println("===============================");
			 
			 
			 if (newMsg.charAt(0)=='s'&&newMsg.charAt(1)=='_') {
				list.addProcess(newMsg,leftL);
				list.addProcess("r_"+newMsg.substring(2,newMsg.length()), rightL);
				traceMsg.add(newMsg);
				traceMsg.add("r_"+newMsg.substring(2,newMsg.length()));
			}else if(newMsg.charAt(0)=='r'&&newMsg.charAt(1)=='_'){
				list.addProcess("s_"+newMsg.substring(2,newMsg.length()), leftL);
				list.addProcess(newMsg, rightL);
				traceMsg.add("s_"+newMsg.substring(2,newMsg.length()));
				traceMsg.add(newMsg);
			}
			 
		}
		 
		 System.out.println("List :"+list);
		 
		 return list;
		 
	 }
	 

	 
	 
	 
	 public static LinkedList<LinkedList<String>> getLinkedFromtrace(String left,String message) {
		 LinkedList<String> res;
		 LinkedList<LinkedList<String>> result = new LinkedList<>();
//		 Map<String, LinkedList<String>> map = new LinkedHashMap<>();
			for (int i=0;i<diagrams.size();i++) {
				System.out.println("Get Link from trace :"+diagrams.get(i).getName());
				System.out.println("Message in SQ :");
				if(diagrams.get(i).getName().contains(left)){
					System.out.println("Name :"+diagrams.get(i).getName());
					result = new LinkedList<>();
					res = new LinkedList<>();
					for (int j = 0; j < diagrams.get(i).getProcesses().getProcessList().size(); j++) {
						for (Entry<String, LinkedList<LinkedList<String>>> eachentry:diagrams.get(i).getProcesses().getProcessList().get(j).entrySet()) {
							System.out.println("Key :"+eachentry.getKey()+" = "+message);
							System.out.println("LinkList :"+eachentry.getValue());
							if(!eachentry.getKey().equals("NaN") && eachentry.getKey().contains(message)){
								for (int k = 0; k < eachentry.getValue().size(); k++) {
//									for (int l = 0; l < eachentry.getValue().get(k).size(); l++) {
										res.add(eachentry.getValue().get(k)+"");
										
//									}
									
								}
	
							}

						}

						
					}
					result.add(res);
					break;
				}

			}
			return result;
	}
	 







	public static String[] messageWithStatus(String s){
		 String[] res = new String[]{"",""};
		 if(s.contains(":")){
			 System.out.println("if has : "+s);
			 res = s.split(":")[1].split("/");
		 }else{
			 res = s.split("/");
		 }
		 if(s.equals("NaN")) return res;
		 System.out.println("Res1:"+res[0]);
		 if(res[0].length()!=0){
			 res[0] = "r_"+res[0];
		 }
		 System.out.println(res.length);
		 if(res.length==2){
			 System.out.println("Res2:"+res[1]);
			 res[1] = "s_"+res[1];
		 }
		 
		 for (int i = 0; i < res.length; i++) {
				System.out.println("MM :"+res[i]);
				
			}
		 return res;
	 }
}