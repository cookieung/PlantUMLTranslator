package Controller;

import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import Model.oop.ProcessList;
import Model.state.StateProcess;

public class StateReader extends DiagramReader {

	 private static Set<String> originalMsg;
	private static Set<String> traceMsg;

	public StateReader(Set<String> originalMsg, Set<String> traceMsg) {
		this.originalMsg=originalMsg;
		this.traceMsg=traceMsg;
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

			String typeMap = "NaN";
			System.out.println("ST TypeMap:"+typeMap);
			String msg;
			if(j+3>=res.size()) msg = "NaN";
			else msg = res.get(j+3);
//			System.out.println(j+" Result :"+res.get(j+3));

			
			Map<String, LinkedList<LinkedList<String>>> map = new LinkedHashMap<>();
   		
   		if(msg.contains("<")|| msg.contains(">") ) msg="NaN";
   		String[] m = messageWithStatus(msg);
   		String message = readAction(m);
		for (int i = 0; i < m.length; i++) {
			if(!m[i].equals("NaN")) {
				if (m[i].contains("s_")||m[i].contains("r_")) {
					String st = m[i].substring(2);
					originalMsg.add(st);
					System.out.println("READER :"+st);
				}
			}
		}
   		System.out.println("Message :"+message);
   		LinkedList<LinkedList<String>> list = new LinkedList<>();
   		list.add(ll);
   		rs.addProcess(message,list,typeMap);
   		if(!message.equals("NaN"))
   		traceMsg.add(message);
		}
		
		rs.testAlt();
		
		return rs;			

	}
	 

	
}
