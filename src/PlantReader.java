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
    	System.out.println(traceData.size());
    	
    	
    	
    	
    	
    	
	}
	
//	  public static String translateToChannel(ArrayList<String> result){
//	    	String res = "channel ";
//	    	int msg = 0;
//	    	System.out.println("ListData :"+traceData);
//	    	for (int i = 0; i < result.size(); i++) {
//	    		for (int j = 0; j < traceData.size(); j++) {
//	    			if(traceData.get(j).contains(result.get(i))) res+=traceData.get(j).get(0).values()+",";
//	    			
//				}
//			}
//	    	return res;
//	    }
//	    
	    
	    
//	    public static String translateToState(String nameState,ArrayList<String> a){
//	    	String res = "";
//	    	String status = nameState;
//	    	for (int i = 0; i < traceData.size(); i++) {
//	    		for (int j = 0; j < traceData.get(i).size(); j++) {
//	    			Map map = traceData.get(i).get(j);
//	    			if(!map.containsKey("Message")){
//	    				String[] arr = map.toString().split("=");
//	    				String[] state = arr[0].split(", ");
//	    				String start = "",re = "",po = "",dest = "";
//	    				start= state[0].replace("{[", "");
//	    				re= arr[1].toString().replace("}", "");
//	    				po= state[1];
//	    				dest= state[2].replace("]", "");
//	    				res += status+" = ";
//	    				status = dest;
//	    				if(!re.equals("NaN")){
//	    				res += readAction(re);
//	    				}
//	    				res += dest+"\n";
//
//	    			}
//				}
//				
//			}
//	    	return res;
//	    }
//	    
	    public static String readAction(String message){
	    	String rs="";
	    	System.out.println("Message :"+message);
	    	ArrayList<String> a = getArrayFromMsg(message);
	    	for (int i = 0; i < a.size(); i++) {
				if (a.get(i).length()!=0) {
					rs+=a.get(i)+" -> ";
				}
				
			}
	    	return rs;
	    }
	
	 public static ArrayList<String> getArrayFromMsg(String msg){
	    	ArrayList<String> a;
	    	if(msg.contains(":")){
		    	String[] arr = msg.split(":");
		    	System.out.println("T"+arr);
		    	if(arr[1].contains("/"))
		    	a = new  ArrayList<>(Arrays.asList(arr[1].split("/")));
		    	else{ 
		    		a = new ArrayList<>();
		    		a.add(arr[1]);
		    		a.add("");
		    	}
	    	}else if(msg.contains("/") ){
				a = new ArrayList<>(Arrays.asList(msg.split("/")));
			}else {
				a = new ArrayList<>();
				a.add(msg);
				a.add("");
				}
	    	return a;
	    }
	
    public static void translateMsg(String state,String op,String msg,int i,ArrayList<String> result){
    	String res="";
    	if(result.get(i+2).equals(":")){
    		ArrayList<String> t = getArrayFromMsg(msg);
    		System.out.println("MM :"+msg);
    		System.out.println("T :"+t);
    		for (int j = 0; j < t.size(); j++) {
    			System.out.println("j :"+j+",t :"+t.get(j));
    			if(t.get(j).length()!=0)
        		if(j%2==1)
            		res += "/"+t.get(j)+"s";
        		else if(j==0)
            		res += t.get(j)+"r";
        		else res += "/"+t.get(j)+"r";
			}

//    		isSend = !isSend;
    		result.add(i+3, res);
    		System.out.println("Res :"+res);
//    		updateMessageToArrayList(result.get(i+3));
    	}
    }
    
//    public static void updateMessageToArrayList(String msg){
//    	Map<String, String> map;
//    	map = new HashMap<>();
//    	if(msg.charAt(0)=='/' ||msg.charAt(msg.length()-1)=='/') msg = msg.replace("/", "");
//    	map.put("Message", msg);
//    	LinkedList<Map> linkMap = new LinkedList<>();
//    	linkMap.add(map);
//    	traceData.add(linkMap);
//    }
//    
//    public static void updateToArrayList(String left,String med,String right,String msg){
//    	LinkedList<String> linkList = new LinkedList<>();
//		Map<LinkedList<String>, String> map;
//		map = new HashMap<>();
//    	linkList.add(left);
//		linkList.add(med);
//		linkList.add(right);
//		if(msg.equals("->")||msg.equals("-->")||msg.equals("<-")||msg.equals("<--")||msg.equals(":"))
//			msg = "NaN";
//		if(msg.charAt(0)=='/' ||msg.charAt(msg.length()-1)=='/') msg = msg.replace("/", "");
//		map.put(linkList, msg);
//		System.out.println("Message :"+msg);
//		LinkedList<Map> linkMap = new LinkedList<>();
//		linkMap.add(map);
//		traceData.add(linkMap);
//    }
    

	
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
			System.out.println("i :"+tmp);
			System.out.println("i-1 :"+res.get(j-1));
			System.out.println("i+1 :"+res.get(j+1));
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

