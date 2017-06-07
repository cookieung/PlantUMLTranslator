import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class PlantReader {
	
	private static ArrayList<LinkedList<Map>> listData;
	private static String input="";
	private static String[] equations;
	private static int count=0;
	private static  ArrayList<ArrayList<String>> allUML;
	
	public static void main(String[]args){
    	listData = new ArrayList<>();
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
    	ArrayList<String> tmp = new ArrayList<>();
    	for (int i = 0; i < equations.length; i++) {
    		if(equations[i].equals("@startuml")){
    			count+=1;
    		}
			tmp.add(equations[i]);
    		if(equations[i].equals("@enduml")){
    			allUML.add(tmp);
    			tmp = new ArrayList<>();
    		}
//			System.out.println("M"+count+" : "+equations[i]);
			
		}
    	
    	for (int i = 0; i < allUML.size(); i++) {
    		System.out.println("State "+(i+1));
    		transStateDiagram(allUML.get(i));
    		
//			for (int j = 0; j < allUML.get(i).size(); j++) {
//				System.out.println("AAAA :"+allUML.get(i).get(j));
//				
//			}
		}
    	
    	System.out.println("Listdata :");
    	for (int i = 0; i < listData.size(); i++) {
			System.out.println(listData.get(i));
		}
    	
    	
    	
    	
    	
    	
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
    		updateMessageToArrayList(result.get(i+3));
    	}
    }
    
    public static void updateMessageToArrayList(String msg){
    	Map<String, String> map;
    	map = new HashMap<>();
    	if(msg.charAt(0)=='/' ||msg.charAt(msg.length()-1)=='/') msg = msg.replace("/", "");
    	map.put("Message", msg);
    	LinkedList<Map> linkMap = new LinkedList<>();
    	linkMap.add(map);
    	listData.add(linkMap);
    }
    
    public static void updateToArrayList(String left,String med,String right,String msg){
    	LinkedList<String> linkList = new LinkedList<>();
		Map<LinkedList<String>, String> map;
		map = new HashMap<>();
    	linkList.add(left);
		linkList.add(med);
		linkList.add(right);
		if(msg.equals("->")||msg.equals("-->")||msg.equals("<-")||msg.equals("<--")||msg.equals(":"))
			msg = "NaN";
		if(msg.charAt(0)=='/' ||msg.charAt(msg.length()-1)=='/') msg = msg.replace("/", "");
		map.put(linkList, msg);
		System.out.println("Message :"+msg);
		LinkedList<Map> linkMap = new LinkedList<>();
		linkMap.add(map);
		listData.add(linkMap);
    }
    

	
	 public static void transStateDiagram(ArrayList<String> result){
	    	for(int i=0;i<result.size();i++){
	         	String tmp = result.get(i);
	         	if (tmp.equals("participant")) {
	         		break;
				}
		    	if(tmp.equals("->")||tmp.equals("-->")){
		    		LinkedList<Map> linkList = new LinkedList<>();
		    		if(result.get(i-1).equals("[*]")){
		    			updateToArrayList(result.get(i-1),tmp,result.get(i+1),result.get(i+3));
		        		continue;
		        	}
		    		translateMsg(result.get(i-1),tmp,result.get(i+3),i,result);
		    		updateToArrayList(result.get(i-1),tmp,result.get(i+1),result.get(i+3));
		    	}else if(tmp.equals("<-")){
		    		translateMsg(result.get(i-1),result.get(i+2),result.get(i+3),i,result);
		    		updateToArrayList(result.get(i+1), "->", result.get(i-1), result.get(i+3));
		    	}else if(tmp.equals("<--")){
		    		translateMsg(result.get(i-1),result.get(i+2),result.get(i+3),i,result);
		    		updateToArrayList(result.get(i+1), "-->", result.get(i-1), result.get(i+3));
		    	}
	    	}
	    }
	
}

