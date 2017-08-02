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
import java.util.Stack;
import java.util.TreeSet;
import java.util.LinkedHashSet;

import javax.naming.spi.DirStateFactory.Result;
import javax.sound.midi.Sequence;

import Model.oop.Diagram;
import Model.oop.ProcessList;
import Model.proc.FrameProcess;
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
	private static Stack<String> stackframe = new Stack<>();
	
	public PlantReader(){
		count=0;
		STATE_DG=0;
		SEQUENCE_DG=0;
	}
	
	public static void main(String[]args){
 	
//    	//Add All UML to ArrayList by separate each state
    	diagrams = new ArrayList<>();
    	    	
//    	for (int i = 0; i < allUML.size(); i++) {
//    		System.out.println("State "+(i+1));
//    		translateToDiagram(allUML.get(i));
//
//		}

		
    	UMLReaderGUI a = new UMLReaderGUI(new UMLReader());
    	a.run();
    		
    	    	
	}

	public static Map<String,LinkedList<String>> getAllTraceOfMessage() {
		Map<String,LinkedList<String>> result= new LinkedHashMap<>();
		String str="";
		Object[] o = traceMsg.toArray();
		Object[] allM = originalMsg.toArray();
		for (int i = 0; i < allM.length; i++) {
			LinkedList<String> list = new LinkedList<>();
			Set<String> linklist = new LinkedHashSet<>();
			for (int j = 0; j < o.length; j++) {
				if(o[j].toString().contains("->")) {
					String[] s= o[j].toString().split("->");
					for (int k = 0; k < s.length; k++) {
						System.out.println(s[k]+" 1contains "+(allM[i].toString()));
						if(s[k].contains(allM[i].toString()))
						linklist.add(s[k].replace(" ", ""));
					}
				}else {
					System.out.println(o[j].toString()+" 2contains "+(allM[i].toString()));
					if(o[j].toString().contains(allM[i].toString()))
					linklist.add(o[j].toString().replace(" ", ""));
				}
			}
			for (int j = 0; j < linklist.toArray().length; j++) {
				list.add(linklist.toArray()[j].toString());
			}
			result.put(allM[i].toString().toUpperCase(), list);
		}
		return result;
	}
	
	public static String showAllTraceOfMessage() {
		String str="";
		Map<String, LinkedList<String>> m = getAllTraceOfMessage();
		for (Entry<String, LinkedList<String>> map : m.entrySet()) {
			str += map.getKey()+" = ";
			for (int i = 0; i < map.getValue().size(); i++) {
				str+=map.getValue().get(i)+"->";
			}
			str += map.getKey()+"\n";
		}
		return str;
	}
	
	public static ArrayList<Map<String, ArrayList<String>>> readAllInput(String input,String name) {
		ArrayList<Map<String, ArrayList<String>>> allUML =  new ArrayList<>();
		equations = prepareInput2(input.replace("\n", " "));
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

    		System.out.println("EQQQQ :"+equations[i]);
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
	
	public static void updateStack(String[] res) {
		Stack<String> st = new Stack<>();
		for (int i = 0; i < res.length; i++) {
			System.out.println(res[i]);
			if(!res[i].equals("@enduml") && (res[i].contains("alt")||res[i].contains("opt")||res[i].contains("loop")||res[i].contains("end") || res[i].contains("else"))) {
				stackframe.push(res[i]);
			}
		}
		
		System.out.println("STACK FRAME :"+stackframe);
	}
	
	public static String[] prepareInput2(String s){
		String[] ss = s.replace(">", "> ").replace("<", " <").split(" ");
		ArrayList<String> sl = new ArrayList<>();
		Map<String,String> countFrame = new LinkedHashMap<>();
		Map<String,String> updateFrame = new LinkedHashMap<>();
		updateStack(ss);
		Stack<String> stk = new Stack<>();
		Stack<String> trace = new Stack<>();
		for (int i = 0; i < ss.length; i++) {
			if(ss[i].length()!=0)
			if(ss[i].contains(":")){
				if(sl.get(sl.size()-1).contains(">")||sl.get(sl.size()-1).contains("<")){
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

			}else {
				System.out.println(sl+"Check ss[i] :"+ss[i]+" = "+ containsInStack(stk, ss[i]));
				if(ss[i].equals("alt")||ss[i].equals("opt")||ss[i].equals("loop")) {
					stk.push(containsInStack(stk, ss[i]));
					sl.add(stk.peek());
					trace.add(stk.peek());
				}else if(ss[i].equals("else")) {
					stk.push(containsInStack(stk, ss[i]));
					sl.add(stk.peek());
				}else if(ss[i].equals("end")) {
					System.err.println(trace);
					stk.push(ss[i]+trace.pop());
					sl.add(stk.peek());
				}else {
					sl.add(ss[i]);
				}
			}
			
		}
		String[] rs = new String[sl.size()] ;
		
		rs = sl.toArray(rs);
		System.out.println("SL :"+sl);
		
		System.out.println("Frame :"+countFrame);
		System.out.println("Update :"+updateFrame);
		return rs;
	}
	
	public static String containsInStack(Stack<String> statck,String s) {
		for (int i = 0; i < statck.size(); i++) {
			System.out.println("CHECK :"+statck.get(i)+" = "+s);
			if(statck.get(i).contains(s)) {
				return s+(Integer.parseInt(statck.get(i).replaceAll("\\D+", ""))+1);
			}
		}
		return s+"1";
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
		 	System.out.println("IN TranslateToDiagram :"+map);
	    	ArrayList<String> res = convertToArrayList(map.values().toString().replace("[", "").replace("]", "").split(", "));
	    	String state = map.keySet().toString().replace("[", "").replace("]", "");
	    	System.out.println("State :" +state+res);
	    	if(state.contains("M")){
	    		diagram = new StateDiagram(state);
	    		stateReader = new StateReader(originalMsg, traceMsg);
	    		diagram.addProcess(stateReader.processForStateDiagram(res));
	    		diagrams.add(diagram);
	    		System.out.println("TEST1 :"+diagram.toString());
	    	}else if(state.contains("SQ")){
	    		diagram = new SequenceDiagram(state);
	    		System.out.println("RES in  PlantReader:");
	    		for (int i = 0; i < res.size(); i++) {
	    			System.err.print(res.get(i)+",");
	    		}
	    		sequenceReader = new SequenceReader(res,diagrams, originalMsg, traceMsg,stackframe);
	    		procSequence = sequenceReader.getResult();
	    		diagram.addProcess(procSequence);
	    		diagrams.add(diagram);
	    		System.out.println("Proc Sequence :"+procSequence);
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
	 
	 //For Print opt
	 public static Map<String, Map<String,LinkedList<String>>> getRelationOfSequenceWithAllState(){
		 Map<String, Map<String,LinkedList<String>>> result = new LinkedHashMap<>();
		 for (int a = 0; a < getAllSequenceDiagramName().length; a++) {
			 String s = "";
			 s+=getAllSequenceDiagramName()[a]+"I";
			 Map<String,LinkedList<String>> map = new LinkedHashMap<>();
			 LinkedList<String> listAllState = new LinkedList<>();
			 for (int i = 0; i < getAllStateDiagramName().length ; i++) {
				listAllState.add(getAllStateDiagramName()[i]+"");
				String ss= getAllStateDiagramName()[i].toString();  
				s+=ss;
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

//		string += "Frame Channel :\n";
		if(frameChannel.size()>0)
		string += getRelationFrameWithSequenceDiagram();

		string += showRelationOfStateDiagram()+"\n";

		
		string += showAllTraceOfMessage()+"\n";

		string += showRelationOfAllMessage()+"\n";
		
		string += showRelationWithSMIAndMSG()+"\n";

//		string += "Relation between Sequence:\n";
//		if(getFrameChannel().size()>0)
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
	 
	 public String templateBracket(Map<String, LinkedList<String>> m){
		 String str="";
		 Stack<String> bracket = new Stack<>();
		 for (Entry<String, LinkedList<String>> eachLine : m.entrySet()) {
			str+=eachLine.getKey().toUpperCase()+" = ";
			for (int i = 0; i < eachLine.getValue().size(); i++) {
				if(i>0 && i<eachLine.getValue().size()-1) {
					str+="(";
					bracket.add(")");
				}
				str+=eachLine.getValue().get(i);
				if(i<eachLine.getValue().size()-1) str+=" ||| ";
				
			}
		}
		 while (!bracket.isEmpty()) {
			str+=bracket.pop();
		}
		 return str;
	 }
	 
	 public String showRelationOfStateDiagram(){
		 return templateBracket(getRelationOfStateDiagram());
	 }

	 public Map<String,LinkedList<String>> getRelationOfAllMessage(){
		 LinkedList<String> str= new LinkedList<>();
		 Map<String, LinkedList<String>> map = new LinkedHashMap<>();
		 for (Entry<String, LinkedList<String>> eachLine : getAllTraceOfMessage().entrySet()) {
			 str.add(eachLine.getKey().toUpperCase());
		}
		map.put("MSG", str); 
		return map;
	 }
	 
	 public static String getRelationFrameWithSequenceDiagram(SequenceProcess proc){
		 String s ="";
		 System.out.println("TRACKER :"+proc.getFrames());
		 for (int i = 0; i < proc.getFrames().size(); i++) {
			LinkedList<Map<String, LinkedList<Map<String, LinkedList<String>>>>> elem = proc.getFrames().get(i);
			String nameTypeFrame = proc.getFrameTypeName(i);
			System.out.println("7/2/2017 :"+elem);
			for (int j = 0; j < elem.size(); j++) {
				for (Entry<String, LinkedList<Map<String, LinkedList<String>>>> map : elem.get(j).entrySet()) {
					s += map.getKey().replace("_loop", "").replace("_opt", "").replace("_alt", "")+" = ";
					for (int k = 0; k < map.getValue().size(); k++) {
						System.out.println(k+"DEBUG :"+map.getValue());
						s += map.getKey().split("_")[0].toLowerCase()+"_b -> "+map.getKey().replace("_loop", "").replace("_opt", "").replace("_alt", "")+"_"+nameTypeFrame.toUpperCase()+"\n"+map.getKey()+"_"+nameTypeFrame.toUpperCase()+" = ";
						frameChannel.add(map.getKey().split("_")[0].toLowerCase()+"_b");
						Map<String, LinkedList<String>> t = map.getValue().get(k);
						System.out.println("Plant Reader ::"+t);
						int n = 0;
						for (Entry<String, LinkedList<String>> tl:t.entrySet()) {
							if(tl.getKey().contains("_b")) s += formatFrame(nameTypeFrame,1,tl.getValue(),map.getKey().split("_")[0].toLowerCase());
							else if(tl.getKey().contains("_e")) s += formatFrame(nameTypeFrame,2,tl.getValue(),map.getKey().split("_")[0].toLowerCase());
							if(n < t.keySet().size()-1) s+= " [] ";
							n++;
						}
					}
					s+="\n";
				}
			}
		 }
		 return s;
	 }
	 
	 private static Map<String, LinkedList<String>> makeForBlankSpaceProc(Map<String, LinkedList<String>> ll,String m) {
		Object[] s = ll.keySet().toArray();
		LinkedList<String> l = new LinkedList<>();
		l.add("NaN");
		System.out.println("has Begin"+!hasFBegin(s));
		System.out.println("has End"+!hasFEnd(s));
		if(!hasFBegin(s)) {
			ll.put(m+"_b", l);
		}
		if(!hasFEnd(s)) {
			ll.put(m+"_e", l);
		}
		System.out.println("LL :"+l);
		return ll;
	}
	 
	private static boolean hasFBegin(Object[] s) {
		for (int i = 0; i < s.length; i++) {
			System.out.println("W:"+s[i].toString()+(s[i].toString().contains("f") && s[i].toString().contains("_b")));
			if(s[i].toString().contains("f") && s[i].toString().contains("_b")) return true;
		}
		return false;
	} 

	private static boolean hasFEnd(Object[] s) {
		for (int i = 0; i < s.length; i++) {
			System.out.println("V:"+s[i].toString()+(s[i].toString().contains("f") && s[i].toString().contains("_e")));
			if(s[i].toString().contains("f") && s[i].toString().contains("_e")) return true;
		}
		return false;
	} 
	
	 public static Map<String ,Map<String,LinkedList<String>>> getRelationWithFrame(SequenceProcess proc){
		 String s ="";
		 Map<String ,Map<String,LinkedList<String>>> m = new LinkedHashMap<>();
		 System.out.println("TRACKER :"+proc.getFrames());
		 for (int i = 0; i < proc.getFrames().size(); i++) {
			String n=""; 
			LinkedList<Map<String, LinkedList<Map<String, LinkedList<String>>>>> elem = proc.getFrames().get(i);
			System.out.println("7/2/2017 :"+elem);
			LinkedList<String> state = new LinkedList<>();
			for (int j = 0; j < elem.size(); j++) {
				for (Entry<String, LinkedList<Map<String, LinkedList<String>>>> map : elem.get(j).entrySet()) {
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
			 System.out.println("<<>>MAP :"+map);
			 for (Entry<String, Map<String, LinkedList<String>>> mp : map.entrySet()) {
				s+= mp.getKey()+" = ";
				Map<String, LinkedList<String>> m = mp.getValue();
				System.out.println("<<>>m :"+m);
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
		
		
	 public static String formatFrame(String typeframe,int j,LinkedList<String> msg,String name){
		 frameChannel.add(name+"_"+typeframe+j);
		 frameChannel.add(name+"_e");
		 System.out.println("Message :"+msg.size());
//		 if(msg.size()==1) return "("+name+"_"+typeframe+" -> "+name+"_e"+" -> SKIP)";
		 String message ="";
		 for (int i = 0; i < msg.size(); i++) {
			if(msg.get(i).length()>0) message += msg.get(i)+" -> ";
		 }
		 return "("+name+"_"+typeframe+j+" -> "+message+name+"_e"+" -> SKIP)";
	 }

	 public String showRelationOfAllMessage(){
		 return templateBracket(getRelationOfAllMessage())+"\n";
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
		 System.out.println("IN UML Map :"+map);
		 for (Entry<String, LinkedList<String>> mp : map.entrySet()) {
			String tmp = mp.getKey();
			s+=tmp+" = ";
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
	 
	 public LinkedList<String> getLinkedListForSq(LinkedList<FrameProcess> procs,String state){
		 LinkedList<String> ll = new LinkedList<>();
		 System.out.println("Check for F1 bug :"+procs);
		 for (int j = 0; j < procs.size(); j++) {
			FrameProcess map2 = procs.get(j); 
					String string = map2.getName();
					System.err.println("State :"+state+" = "+map2.getAtomicProcess());
					if(string.charAt(0)=='f' && (string.contains("_e") ||string.contains("_b"))){
						String rr = string.replace("loop", "").replace("opt", "").replace("alt", "");
						if(!ll.contains(rr)) ll.add(rr);
					}else{
						System.out.println("Check !!!!!"+map2.getAtomicProcess().size());
						for (int i = 0; i < map2.getAtomicProcess().size(); i++) {
							for (Entry<String, String> l : map2.getAtomicProcess().get(i).entrySet()) {
								if(("M_"+l.getKey()).equals(state)){
									String rr = l.getValue().replace("loop", "").replace("opt", "").replace("alt", "");
									if(!ll.contains(rr)) ll.add(rr);
								}
							}
						}							
					}

		}
		 System.out.println("LL >>>>"+ll);
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
							System.out.println("A:");
							for (int k2 = 0; k2 < getAllSequenceDiagram().get(i).getProcesses().getProcessListOptList().size(); k2++) {
								System.out.println(getAllSequenceDiagram().get(i).getProcesses().getProcessListOptList().get(k2).getName()+":"+getAllSequenceDiagram().get(i).getProcesses().getProcessListOptList().get(k2).getAtomicProcess());
							}
							System.out.println("B:"+map2.getValue().get(k).get(0));
							ll = getLinkedListForSq(getAllSequenceDiagram().get(i).getProcesses().getProcessListOptList(),"M_"+map2.getValue().get(k).get(0));
							ll.add("SKIP");
							map.put("M_"+map2.getValue().get(0).get(0), ll);
							System.out.println("Check Here : "+map);
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
				Stack<String> br = new Stack<>();
				str+=s.getKey()+" = ";
				for (int i = 0; i < s.getValue().size(); i++) {
					String tmp = s.getValue().get(i);
					if(i>0 && i<s.getValue().size()-1) {
						str+="(";
						br.push(")");
					}
					str+=tmp;
					if(i<s.getValue().size()-1) str+=" ||| ";
				}
				while (!br.isEmpty()) {
					str+=br.pop();
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
								res += entry2.getValue().get(j).get(k).replace("-->", "->");
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