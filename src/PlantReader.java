import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class PlantReader {
	
	private static Map<String,LinkedList<Map<String,LinkedList<String>>>> traceData;
	private static String input="";
	private static String[] equations;
	private static int count=0;
	private static Integer STATE_DG=0;
	private static Integer SEQUENCE_DG=0;
	private static  ArrayList<Map<String,ArrayList<String>>> allUML;
	
	public static void main(String[]args){
    	traceData = new HashMap<>();
    	allUML = new ArrayList<>();
		
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("Input UML :");
    	while (scanner.hasNextLine()) {
			String chk = scanner.nextLine();
			if (chk.equals("end")) {
				break;
			}
			input+=chk+" ";
		}
    	
    	equations = input.split(" ");
    	
    	//Add All UML to ArrayList by separate each state
    	Map<String,ArrayList<String>> map = new HashMap<>();
    	ArrayList<String> tmp = new ArrayList<>();
    	String t= "";
    	for (int i = 0; i < equations.length; i++) {
    		if(equations[i].equals("@startuml")){
    			count+=1;
    			if (equations[i+1].equals("participant")) {
					t = "SQ"+ ++SEQUENCE_DG;
				}else {
					t = "M"+ ++STATE_DG;
				}
    		}
    		tmp.add(equations[i]);
    		if(equations[i].equals("@enduml")){
        		System.out.println(tmp);
    			map.put(t,tmp);
    			allUML.add(map);
    			map = new HashMap<>();
    			tmp = new ArrayList<>();
    		}
//			System.out.println("M"+count+" : "+equations[i]);
			
		}
    	
    	for (int i = 0; i < allUML.size(); i++) {
    		System.out.println("State "+(i+1));
    		System.out.println(allUML.get(i));
    		translateToDiagram(allUML.get(i));
    		
//			for (int j = 0; j < allUML.get(i).size(); j++) {
//				System.out.println("AAAA :"+allUML.get(i).get(j));
//				
//			}
		}
    	
    	System.out.println("Tracedata :");
    	System.out.println(traceData);
    	
    	
    	
    	
    	
    	
	}
	

	
	 public static void translateToDiagram(Map<String,ArrayList<String>> map){
	    	System.out.println("IN TranslateToDiagram :");
	    	ArrayList<String> res = convertToArrayList(map.values().toString().replace("[", "").replace("]", "").split(", "));
	    	String state = map.keySet().toString().replace("[", "").replace("]", "");
	    	if(state.contains("M")){
	    		traceData.put(state,processForStateDiagram(res));
	    		System.out.println("Trace data :"+traceData);
	    	}else if(state.contains("S")){
	    		System.out.println("PC Sequence");
//	    		processForSequenceDiagram(res);
	    	}
	 }
	 
	 private static ArrayList<String> convertToArrayList(Object[] in) {
		ArrayList<String> res = new ArrayList<>();
		 for (int i = 0; i < in.length; i++) {
			res.add(in[i]+"");
		}
		 return res;
	}
	 
	 public static LinkedList<Map<String,LinkedList<String>>> processForStateDiagram(ArrayList<String> res) {
		System.out.println("Process State");
		LinkedList<Map<String,LinkedList<String>>> rs = new LinkedList<>();
		for (int j = 1; j < res.size()-1; j++) {
			String tmp = res.get(j);
			LinkedList<String> ll = new LinkedList<>();

			if(tmp.contains("<")){
	    		ll.add(res.get(j+1));
	    		ll.add(tmp.replace("<", "")+">");
	    		ll.add(res.get(j-1));
	    	}else if(tmp.contains(">")){
	    		ll.add(res.get(j-1));
	    		ll.add(tmp);
	    		ll.add(res.get(j+1));
	    	}
	    	else continue;
			Map<String, LinkedList<String>> map = new HashMap<>();
    		String msg = res.get(j+3);
    		if(msg.contains("<")|| msg.contains(">") ) msg="NaN";
    		map.put(msg, ll);
    		rs.add(map);
		}
		
		System.out.println("Process State"+rs);
		
		return rs;			

	}
	 
	 public static LinkedList<Map<String,LinkedList<String>>> processForSequenceDiagram(ArrayList<String> res) {
		System.out.println("Process Sequence");
		LinkedList<Map<String,LinkedList<String>>> rs = new LinkedList<>();
		for (int j = 0; j < res.size(); j++) {
			String tmp = res.get(j);
			LinkedList<String> ll = new LinkedList<>();
			
			if(tmp.contains("<")){
	    		ll.add(res.get(j+1));
	    		ll.add(tmp.replace("<", "")+">");
	    		ll.add(res.get(j-1));
	    	}else if(tmp.contains(">")){
	    		ll.add(res.get(j-1));
	    		ll.add(tmp);
	    		ll.add(res.get(j+1));
	    	}
	    	else continue;
			Map<String, LinkedList<String>> map = new HashMap<>();
    		String msg = res.get(j+3);
    		if(msg.contains("<")|| msg.contains(">") ) msg="NaN";
    		map.put(msg, ll);
    		rs.add(map);
		}
		
		return rs;	
	}
	
}

