import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import javax.swing.event.ListDataEvent;

public class PlantReader
{
	
	private static String plantuml = "@startuml";
	static ArrayList<LinkedList<Map>> listData;
    static boolean isSend;
    static String tail;
    static String[] result;

    public static void main(String[] args)
    {
    	listData = new ArrayList<>();
    	System.out.println("File input:");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String chk =  scanner.nextLine();
            if(chk.equals("@startuml")){
            	continue;
            }
            plantuml += " "+chk;
            if(chk.equals("@enduml")){
            	break;
            }
			
		}
        
//        System.out.println(plantuml);
        isSend=false;
        tail = "s";
        result = plantuml.split(" ");

        for(int i=0;i<result.length;i++){
        	String tmp = result[i];
        	if(tmp.equals(":")){
        		if(isSend){
        			tail = "r";
	        		result[i+1]+="r";
	        	}else{
	        		tail = "s";
	        		result[i+1]+="s";
	        	}
        		
        		updateMessageToArrayList(result[i+1]);
        		isSend = !isSend;
        	}else if(tmp.equals("->")||tmp.equals("-->")){
        		
        		LinkedList<Map> linkList = new LinkedList<>();
        		if(result[i-1].equals("[*]")){
        			updateToArrayList(result[i-1],tmp,result[i+1],result[i+3]);
            		continue;
            	}
        		updateToArrayList(result[i-1],tmp,result[i+1],result[i+3]);
        	}else if(tmp.equals("<-")){
        		updateToArrayList(result[i+1], "->", result[i-1], result[i+3]);
        	}else if(tmp.equals("<--")){
        		updateToArrayList(result[i+1], "-->", result[i-1], result[i+3]);
        	}


        }
        
        
        
        for(int i=0;i<result.length;i++){
        	System.out.println("Line "+i+"msg :"+result[i]);
        }
        
        System.out.println("/////////////////");
        
        for (int i = 0; i < listData.size(); i++) {
        	LinkedList<Map> l = listData.get(i);
        	for (int j = 0; j < l.size(); j++) {
				System.out.println(l.get(j));
			}
		}
        
        System.out.println("Translate to:");
        System.out.println(translateToChannel());
        System.out.println("State :");
        System.out.println(translateToState("M1"));
        
        
    }
    
    public String converterToCSP(String uml){
    	
    	if(uml.equals("something")) return "channel";
    	return uml;
    }
    
    public void translateMsg(String tmp,String m,int i){
    	if(tmp.equals(":")){
    		if(isSend){
    			tail = "r";
        		result[i+1]+="r";
        	}else{
        		tail = "s";
        		result[i+1]+="s";
        	}
    		
    		updateMessageToArrayList(result[i+1]);
    		isSend = !isSend;
    	}
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
		map.put(linkList, msg);
		LinkedList<Map> linkMap = new LinkedList<>();
		linkMap.add(map);
		listData.add(linkMap);
    }
    
    public static void updateMessageToArrayList(String msg){
    	Map<String, String> map;
    	map = new HashMap<>();
    	map.put("Message", msg);
    	LinkedList<Map> linkMap = new LinkedList<>();
    	linkMap.add(map);
    	listData.add(linkMap);
    }
    
    public static String translateToChannel(){
    	String res = "channel ";
    	int msg = 0;
    	for (int i = 0; i < listData.size(); i++) {
    		for (int j = 0; j < listData.get(i).size(); j++) {
    			Map map = listData.get(i).get(j);
    			if(map.containsKey("Message")){
    				msg++;
    				if(msg>1)
    				res += ","+map.get("Message");
    				else res += map.get("Message");
    			}
    			
			}
		}
    	return res;
    }
    
    public static String translateToState(String nameState){
    	String res = "";
    	String status = nameState;
    	for (int i = 0; i < listData.size(); i++) {
    		for (int j = 0; j < listData.get(i).size(); j++) {
    			Map map = listData.get(i).get(j);
    			if(!map.containsKey("Message")){
    				String[] arr = map.toString().split("=");
    				String[] state = arr[0].split(", ");
    				String a = "",b = "",c = "",d = "";
    				a= state[0].replace("{[", "");
    				b= arr[1].toString().replace("}", "");
    				c= state[1];
    				d= state[2].replace("]", "");
    				res += status+" = ";
    				status = d;
    				if(!b.equals("NaN"))
    				res += b+" "+c;
    				res += " "+d+"\n";

    			}
			}
			
		}
    	return res;
    }
    
    
}