package Controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;

import Model.oop.Diagram;
import Model.oop.ProcessList;
import Model.sequence.SequenceDiagram;
import Model.sequence.SequenceProcess;

public class SequenceReader {
	
	private static ArrayList<Diagram> diagrams;
	private static Set<String> traceMsg = new LinkedHashSet<>();
	private static Set<String> originalMsg = new LinkedHashSet<>();
	static ArrayList<String> res = new ArrayList<>();
	
	static Stack<String> stackframe = new Stack<>();

	
	public SequenceReader(ArrayList<String> res,ArrayList<Diagram> diagrams,Set<String> originalMessage,Set<String> traceMsg,Stack<String> stackframe){
		this.diagrams = diagrams;
		this.originalMsg = originalMessage;
		this.traceMsg = traceMsg;
		this.res = res;
		System.out.println("RES inSQ reader");
		for (int i = 0; i < res.size(); i++) {
			System.err.print(res.get(i)+",");
		}
		this.stackframe = stackframe;
	}
	
	public Set<Diagram> getAllTempStateDiagram(){
		Set<Diagram> s  =new LinkedHashSet<>();
		for (int i = 0; i < diagrams.size(); i++) {
			if(diagrams.get(i).getClass().equals(SequenceDiagram.class)){
				Object[] o = ((SequenceDiagram)diagrams.get(i)).getAllTempStateDiagram().toArray();
				for (int j = 0; j < o.length; j++) {
					s.add((Diagram)o[j]);
				}
			}
		}
		return s;
	}
	
	public ArrayList<LinkedList<Map<String, LinkedList<Map<String, String>>>>> getRelationFrameWithSequenceDiagram(SequenceProcess process){		
		return process.getFrames();
	}
	


	
	 public static ProcessList getResult(){
		 String newMsg="",left="",right="",state="";
		 boolean isWait;
		 ProcessList list=new SequenceProcess();
		 for (int i = 1; i < res.size()-1; i++) {
			 System.out.println("III :"+res.get(i));
			 if (res.get(i+1).contains(">")) {
				left = res.get(i);
				right = res.get(i+2);
				newMsg = "s_"+res.get(i+4);
				doResult(i, newMsg, left, right, list);
			}else if(res.get(i+1).contains("<")){
				left = res.get(i+2);
				right = res.get(i);
				newMsg = "r_"+res.get(i+4);
				doResult(i, newMsg, left, right, list);
			}else continue;
			 


			 
		}
		 
		 System.out.println("List :"+list);

		 
		 list.testOpt();
		 
		 return list;
		 
	 }
	 
	 public static void doResult(int i,String newMsg,String left,String right,ProcessList list) {
		 String typeMap = res.get(i-1);
		 String nextType = res.get(i+5);
		 
		 String mmm = newMsg.substring(2);
		 
		 if(!(typeMap.contains("alt")||typeMap.contains("opt")||typeMap.contains("loop")||typeMap.contains("else")||typeMap.contains("end"))) typeMap = "NaN";
		 
		 System.out.println("<<Type Map :"+typeMap);
		 
		 System.out.println("<<Next Type :"+nextType);
		 System.err.println(mmm+" : "+haveStateDiagram(mmm));
		 
		 originalMsg.add(res.get(i+4));
		 Map<String, LinkedList<LinkedList<String>>> m = new LinkedHashMap<>();
		 LinkedList<LinkedList<String>> leftL = new LinkedList<>();
		 LinkedList<String> l = new LinkedList<>();
		 l.add(left);
		 l.add("->");
		 l.add(right);
		 leftL.add(l);
		 
		 LinkedList<LinkedList<String>> rightL = new LinkedList<>();
		 LinkedList<String> r = new LinkedList<>();
		 r.add(right);
		 r.add("->");
		 r.add(left);
		 rightL.add(r);

		 System.out.println("[Type] :"+typeMap);
//		 
//		 if(!stackframe.isEmpty() &&( nextType.contains("end"))) {
//			 System.out.println("POP Stack :"+stackframe.peek());
//		 }
//		 else if(typeMap.contains("alt") ||typeMap.contains("opt") ||typeMap.contains("loop")) stackframe.push(typeMap);
		 
		 if (newMsg.charAt(0)=='s'&&newMsg.charAt(1)=='_') {
			list.addProcess(newMsg,"r_"+newMsg.substring(2,newMsg.length()),leftL,rightL,typeMap,nextType);
			traceMsg.add(newMsg);
			traceMsg.add("r_"+newMsg.substring(2,newMsg.length()));
		}else if(newMsg.charAt(0)=='r'&&newMsg.charAt(1)=='_'){
			list.addProcess(newMsg,"s_"+newMsg.substring(2,newMsg.length()), leftL, rightL,typeMap,nextType);
			traceMsg.add("s_"+newMsg.substring(2,newMsg.length()));
			traceMsg.add(newMsg);
		}

		 if(res.get(i+5).equals("@enduml") || res.get(i+6).equals("@enduml")){
			 list.checkFrame();
		 }
		 
		 System.out.println("TypeM: "+typeMap);
//		 System.out.println("Stack Frame :"+stackframe);
	 }
	 
	 public static boolean isIndependentSequence(){
		 for (int i = 1; i < res.size()-1; i++) {
			 if(res.get(i+1).contains(">")||res.get(i+1).contains("<"))
				if(!haveStateDiagram(res.get(i+4))) return true;
		 }
		 return false;
	 }
	 
	 

	 
	 public static boolean haveStateDiagram(String msg){
		 boolean result = false;
		 for (int i = 0; i < diagrams.size(); i++) {
			if(diagrams.get(i).getName().contains("M_")){
				for (int j = 0; j < diagrams.get(i).getProcesses().getProcessListByName().size(); j++) {
					for (Entry<String, LinkedList<LinkedList<String>>> di : diagrams.get(i).getProcesses().getProcessListByName().get(j).entrySet()) {
						if(di.getKey().contains(msg) && !di.getKey().equals("NaN")) result=true;
						
					}
					
				}
			}
		}
		 return result;
	 }
	 

	 
}