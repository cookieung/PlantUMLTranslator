import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
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
    		translateToDiagram(allUML.get(i));

		}
    	
    	System.out.println("Tracedata :");
    	System.out.println(traceData);
    	
    	
    	
    	
    	
    	
	}
	


	

	
	 public static void translateToDiagram(Map<String,ArrayList<String>> map){
	    	System.out.println("IN TranslateToDiagram :");
	    	ArrayList<String> res = convertToArrayList(map.values().toString().replace("[", "").replace("]", "").split(", "));
	    	String state = map.keySet().toString().replace("[", "").replace("]", "");
	    	System.err.println("State :" +state);
	    	if(state.contains("M")){
	    		traceData.put(state,processForStateDiagram(res));
	    		System.out.println("Trace data :"+traceData);
	    		System.err.println(getAllSendAndReceiveMessage(state));
	    	}else if(state.contains("SQ")){
	    		System.out.println("PC Sequence");
	    		getResult(res);
//	    		traceData.put(state,processForSequenceDiagram(res));

	    	}
	 }
	 
	 
	public static String getAllSendAndReceiveMessage(String nowstate){
			Collection<LinkedList<Map<String, LinkedList<String>>>> tr = traceData.values();
			String res="";
			System.out.println("Test Receive and Send Trace :");
			
			for (Entry<String, LinkedList<Map<String, LinkedList<String>>>> entry : traceData.entrySet())
			{
				String namestate = entry.getKey();
			    System.out.println("T :"+entry.getKey() + "/" + entry.getValue());
			    LinkedList<Map<String, LinkedList<String>>> l = entry.getValue();
			    if(namestate.equals(nowstate))
			    for (int i = 0; i < l.size(); i++) {
					for (Entry<String, LinkedList<String>> entry2 : l.get(i).entrySet())
					{
					    for (int j = 0; j < entry2.getValue().size(); j++) {
					    	System.out.println("entry2 key :"+entry2.getKey());
							if(j%3==1) res+= "= ";
							else{
								if(entry2.getValue().get(j).equals("*")) res += namestate+" ";
								else res+= entry2.getValue().get(j)+" ";
							}
							if(j%3==1 && !entry2.getKey().equals("NaN") ) res+= entry2.getKey()+" -> ";
						}
					    res += "\n";
					}
					
				}
			}

			return res;
	}
	

    
    public static String readAction(String[] message){
    	String rs="";
    	System.out.println("Message :"+message);
    	for (int i = 0; i < message.length; i++) {
			if (message[i].length()!=0) {
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
	 
	 public static LinkedList<Map<String,LinkedList<String>>> processForStateDiagram(ArrayList<String> res) {
		System.out.println("Process State");
		char state = 'n';
		LinkedList<Map<String,LinkedList<String>>> rs = new LinkedList<>();
		for (int j = 1; j < res.size()-1; j++) {
			String tmp = res.get(j);
			LinkedList<String> ll = new LinkedList<>();

			if(tmp.contains("<")){
	    		ll.add(res.get(j+1));
	    		ll.add(tmp.replace("<", "")+">");
	    		ll.add(res.get(j-1));
	    		state = 'r';
	    	}else if(tmp.contains(">")){
	    		ll.add(res.get(j-1));
	    		ll.add(tmp);
	    		ll.add(res.get(j+1));
	    		state = 's';
	    	}
	    	else continue;
			Map<String, LinkedList<String>> map = new HashMap<>();
    		String msg = res.get(j+3);
    		if(msg.contains("<")|| msg.contains(">") ) msg="NaN";

    		map.put(readAction(messageWithStatus(msg)), ll);
//    		rs.add(map);
    		rs.add(checkMap(map));
		}
		
		System.out.println("Process State"+rs);
		
		return rs;			

	}

	 public static Map<String, LinkedList<String>> checkMap(Map<String, LinkedList<String>> map){
		 Map<String, LinkedList<String>> resMap = new HashMap<>();
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
	 
		public static Map<String,LinkedList<String>> loopMapRight(String module1, ArrayList<String> trace, String state,
				String key, LinkedList<Map<String, LinkedList<String>>> value) {
			Map<String, LinkedList<String>> map = new HashMap<>();
			LinkedList<String> ll = new LinkedList<>();
			String msg="";
//			for (Entry<String, LinkedList<Map<String, LinkedList<String>>>> mapOfNameStateAndAllEq : traceData.entrySet())
//			{
				System.out.println("RRRRRR :"+key+" M :"+module1);
				if(key.equals(module1)){
			    	if(!trace.contains(key))
			    	state = key;
			    		System.out.println("KeyR :"+value.size());
				    for (int j = 0; j < value.size(); j++) {
				    	System.out.println("VR :"+value.get(j));
				    	for (Entry<String, LinkedList<String>> mapOfEqAndMsg : value.get(j).entrySet()) {
				    		for (int k=0; k<mapOfEqAndMsg.getValue().size(); k++) {
				    			state = mapOfEqAndMsg.getValue().get(k);
								System.out.println("Test ST :"+mapOfEqAndMsg.getValue().get(k));
								msg = mapOfEqAndMsg.getKey();
								if(state.equals("*")){
									k = 2;
									continue;
								}
								ll.add(state);
				    		}

						}
				    	
	
					}
				}else return null;

			map.put(msg, ll);
			return map;
		}
	 
		public static LinkedList<Map<String,LinkedList<String>>> loopMapLeft(String module1,String module2, ArrayList<String> trace, String state,
				String message,String key, LinkedList<Map<String, LinkedList<String>>> value) {
			LinkedList<Map<String,LinkedList<String>>> bigLL = new LinkedList<>();
			LinkedList<String> ll = new LinkedList<>();
			String msg = "";
    		Map<String,LinkedList<String>> mapLeft = new HashMap<>();
    		Map<String,LinkedList<String>> mapRight = new HashMap<>();
//			for (Entry<String, LinkedList<Map<String, LinkedList<String>>>> mapOfNameStateAndAllEq : traceData.entrySet())
//			{
				System.out.println("KKKKKKKKK :"+key+" = "+module1);
				if(key.equals(module1)){
			    	if(!trace.contains(key))
			    	state = key;
			    		System.out.println("KeyL :"+key);
				    for (int j = 1; j < value.size()-1; j++) {
				    	for (Entry<String, LinkedList<String>> mapOfEqAndMsg : value.get(j).entrySet()) {
				    		msg = mapOfEqAndMsg.getKey();
				    		if (msg.contains(message)) {
				    			for (int k=0; k<mapOfEqAndMsg.getValue().size(); k++) {
					    			state = mapOfEqAndMsg.getValue().get(k);
									System.out.println("Test ST :"+mapOfEqAndMsg.getValue().get(k));
									ll.add(state);
					    		}
					    		mapLeft.put(msg, ll);
					    		for (Entry<String, LinkedList<Map<String, LinkedList<String>>>> map2: traceData.entrySet()) {
					    		mapRight = loopMapRight(module2, trace, state, map2.getKey(), map2.getValue());
					    		System.out.println("Right :"+mapRight);
					    		}
							}
				    		

						}
				    	
	
					}
				}else return null;
				

				bigLL.add(mapLeft);
				bigLL.add(mapRight);

			return bigLL;
		}
	 

		
	 
	 public static LinkedList<Map<String,LinkedList<String>>> processForSequenceDiagram(ArrayList<String> res) {
		System.out.println("Process Sequence");
		LinkedList<Map<String,LinkedList<String>>> rs = new LinkedList<>();
		ArrayList<String> trace = new ArrayList<>();
		char status = 'r';
		String state = "",module1="",module2="";
		for (int i = 1; i < res.size()-3; i++) {
			String act = res.get(i);
			System.out.println(act);
			LinkedList<String> ll = new LinkedList<>();
			if(!act.contains(">")&&!act.contains("<")) continue;
			if(act.contains(">"))
			{	
				System.out.println("IN >");
				module1 = res.get(i-1);
				module2 = res.get(i+1);
				status = 's';
			}else if(act.contains("<")){
				System.out.println("IN <");
				module1 = res.get(i+1);
				module2 = res.get(i-1);
				status = 'r';
			}
				String msg = res.get(i+3);
				System.out.println("Module 1 :"+module1);
				System.out.println("Module 2 :"+module2);
				

				Map<String, LinkedList<String>> map = new HashMap<>();
				for (Entry<String, LinkedList<Map<String, LinkedList<String>>>> mapOfNameStateAndAllEq: traceData.entrySet()) {
		    		if(msg.contains("Nan") ||msg.contains("@") ) continue;
		    		LinkedList<Map<String, LinkedList<String>>> tmp = loopMapLeft(module1,module2,trace,state,msg,mapOfNameStateAndAllEq.getKey(),mapOfNameStateAndAllEq.getValue());
		    		if(tmp!=null) {
		    			return tmp;
		    		}
				}
				
	    		
			
		}
		System.out.println("RS :");
		System.out.println(rs);
		
		
		
		
		return rs;	
	}
	 
	 public static void getResult(ArrayList<String> res){
		 String newMsg="",left="",right="",state="";
		 for (int i = 1; i < res.size()-1; i++) {
			 if (res.get(i+1).contains(">")) {
				left = res.get(i);
				right = res.get(i+2);
				newMsg = res.get(i+4)+"s";
			}else if(res.get(i+1).contains("<")){
				left = res.get(i+2);
				right = res.get(i);
				newMsg = res.get(i+4)+"r";
			}else continue;

			 System.out.println(left+" > "+newMsg+" > "+right);
			 System.out.println("LEFT -> RIGHT");
			 System.out.println(getLinkedFromtrace(left,right, newMsg));
//			 System.out.println("RIGHT -> LEFT");
//			 System.out.println(getLinkedFromtrace(right,left, newMsg));
		}
		 
	 }
	 
	 
	 
	 public static Map<String,LinkedList<String>> getLinkedFromtrace(String left,String right,String message) {
		 LinkedList<String> res = new LinkedList<>();
		 Map<String, LinkedList<String>> map = new HashMap<>();
			for (Entry<String, LinkedList<Map<String, LinkedList<String>>>> diagram : traceData.entrySet()) {
				if(diagram.getKey().equals(left)){
					System.out.println("Name :"+diagram.getKey());
					for (int j = 0; j < diagram.getValue().size(); j++) {
						for (Entry<String, LinkedList<String>> eachentry:diagram.getValue().get(j).entrySet()) {
							System.out.println(eachentry.getKey()+" = "+message+" : "+eachentry.getKey().equals(message));
							if(eachentry.getKey().equals(message)){
								for (int i = 0; i < eachentry.getValue().size(); i++) {
									res.add(eachentry.getValue().get(i));
								}
							}

						}

						
					}
					break;
				}

			}
			for (Entry<String, LinkedList<Map<String, LinkedList<String>>>> diagram : traceData.entrySet()) {
				if(diagram.getKey().equals(right)){
					System.out.println("Name :"+diagram.getKey());
					for (int j = 0; j < diagram.getValue().size(); j++) {
						for (Entry<String, LinkedList<String>> eachentry:diagram.getValue().get(j).entrySet()) {
							System.out.println(eachentry.getKey()+" = "+message+" : "+eachentry.getKey().equals(message));
							if(eachentry.getKey().equals(message)){
								for (int i = 0; i < eachentry.getValue().size(); i++) {
									res.add(eachentry.getValue().get(i));
								}
							}

						}

						
					}
					break;
				}

			}
			if (res.size()==0)
				return null;
			map.put(message, res);
			return map;
	}
	 







	public static String[] messageWithStatus(String s){
		 String[] res = new String[]{"",""};
		 if(s.contains(":")){
			 res = s.split(":")[1].split("/");
		 }
		 if(s.equals("NaN")) return res;
		 if(res[0].length()!=0){
			 res[0]+="r";
		 }
		 System.out.println(res.length);
		 if(res.length==2){
			 res[1]+="s";
		 }
		 return res;
	 }
}
