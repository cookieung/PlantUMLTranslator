package Controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import Model.Diagram;
import Model.ProcessList;
import Model.SequenceProcess;

public class SequenceReader {
	
	private static ArrayList<Diagram> diagrams;
	private static Set<String> traceMsg = new LinkedHashSet<>();
	private static Set<String> originalMsg = new LinkedHashSet<>();

	
	public SequenceReader(ArrayList<Diagram> diagrams,Set<String> originalMessage,Set<String> traceMsg){
		this.diagrams = diagrams;
		this.originalMsg = originalMessage;
		this.traceMsg = traceMsg;
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

			 String typeMap = res.get(i-1);
			 
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
				list.addProcess(newMsg,"r_"+newMsg.substring(2,newMsg.length()),leftL,rightL,typeMap);
				traceMsg.add(newMsg);
				traceMsg.add("r_"+newMsg.substring(2,newMsg.length()));
			}else if(newMsg.charAt(0)=='r'&&newMsg.charAt(1)=='_'){
				list.addProcess("s_"+newMsg.substring(2,newMsg.length()),newMsg, leftL, rightL,typeMap);
				traceMsg.add("s_"+newMsg.substring(2,newMsg.length()));
				traceMsg.add(newMsg);
			}
			 
		}
		 
		 System.out.println("List :"+list);
		 
		 list.testAlt();
		 
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
					for (int j = 0; j < diagrams.get(i).getProcesses().getProcessListByName().size(); j++) {
						for (Entry<String, LinkedList<LinkedList<String>>> eachentry:diagrams.get(i).getProcesses().getProcessListByName().get(j).entrySet()) {
							System.out.println("Key :"+eachentry.getKey()+" = "+message);
							System.out.println("LinkList :"+eachentry.getValue());
							if(!eachentry.getKey().equals("NaN") && eachentry.getKey().contains(message)){
								for (int k = 0; k < eachentry.getValue().size(); k++) {
									res = new LinkedList<>();
									for (int l = 0; l < eachentry.getValue().get(k).size(); l++) {
										res.add(eachentry.getValue().get(k).get(l));
										
									}
									result.add(res);
									
								}
	
							}

						}

						
					}
//					result.add(res);
//					break;
				}

			}
			return result;
	}


}
