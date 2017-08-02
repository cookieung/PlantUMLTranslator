package Model.proc;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

public class ProcessLinkedList {
	
	private LinkedList<Map<String,LinkedList<LinkedList<String>>>> normalProcess;
	
	private FrameProcessMap optProcess;
	
	String begin = "";
	
	Stack<String> stackframe = new Stack<>();
	
	public ProcessLinkedList(){
		this.normalProcess = new LinkedList<>();
		this.optProcess = new FrameProcessMap();
	}
	
	public int size(){
		return normalProcess.size();
	}

	public void addOptInNormal(String before,Map<String,LinkedList<LinkedList<String>>> map,String typeMap,String nextTypeName,String nameframe) {
		Map<String, Map<String, Map<String, String>>> m = new LinkedHashMap<>();
		System.out.println("Before :"+before);
		System.out.println("*[Type Map :"+typeMap);
		System.out.println("[Next Type :"+nextTypeName);
		
		this.begin = before;
		
		if(!typeMap.equals("NaN"))	stackframe.push(typeMap);
		
		System.out.println("STACK :"+stackframe);
		if( typeMap.contains("end") || (!nextTypeName.contains("end") && !typeMap.contains("alt") && !typeMap.contains("opt") && !typeMap.contains("else") && !typeMap.contains("loop"))) {
			System.out.println("ABC"+typeMap+","+before);
			m.put("NaN", getForOpt(map, typeMap,nextTypeName,nameframe));
		}
		else {
			System.out.println("CDE"+typeMap+","+before);

			
			if(typeMap.contains("loop") && nextTypeName.contains("end")) {
				typeMap = typeMap;
			}
			

			System.out.println("Stack in CDE :"+stackframe);
			System.out.println("#Q"+typeMap);

			System.out.println(">><<Type Map :"+typeMap);
			m.put(typeMap, getForOpt(map, typeMap,nextTypeName,nameframe));
		}
		System.out.println(typeMap +"Add Normal Map :"+map+">>"+m);
		for (Entry<String, Map<String, Map<String, String>>> map2 : m.entrySet()) {
			optProcess.setName(map2.getKey());
			System.out.println("$$$"+map2.getKey()+"/"+begin);
			for (Entry<String, Map<String, String>> map3 : map2.getValue().entrySet()) {
				FrameProcess f = new FrameProcess(map3.getKey());
				f.addProcess(map3.getValue());
				optProcess.addFrameProcess(f);
			}
		}

//		if(typeMap.contains("alt")||typeMap.contains("opt")||typeMap.contains("loop")||typeMap.contains("end"))	
		System.out.println("Stack Frame :"+stackframe);
	}
	
	
	public void addNormal(Map<String,LinkedList<LinkedList<String>>> map,String typeMap,String nextTypeName,String nameframe) {
		normalProcess.add(map);
	}
	
	public void addNormal(String before,Map<String,LinkedList<LinkedList<String>>> map,String typeMap,String nextTypeName,String nameframe) {
		normalProcess.add(map);
		addOptInNormal(before, map, typeMap, nextTypeName, nameframe);
	}


	public Map<String, Map<String, String>> getForOpt(Map<String,LinkedList<LinkedList<String>>> map,String typeMap,String nextTypeMap,String nameframe){
		Map<String, Map<String, String>> mm = new LinkedHashMap<>();
		System.out.println("<Type Map> :"+typeMap);
		System.out.println("<Next Type Map> :"+nextTypeMap);
		String stack;
		
		Stack<String> stk = stackframe;
		
		if(stk.isEmpty()) stack = "";
		else if(stk.peek().contains("else")) {
			stk.pop();
			stack = stk.peek();
		}else stack = stk.peek();
		
		System.out.println("Checker :"+"end"+stack);
		if((typeMap.contains("alt") || typeMap.contains("opt") || typeMap.contains("loop")) && !typeMap.contains("end") ){
			Map<String, String> l  = new LinkedHashMap<>();
			for (Entry<String, LinkedList<LinkedList<String>>> s : map.entrySet()) {
				for (int i = 0; i < s.getValue().size(); i++) {
					l.put(s.getValue().get(i).get(0),s.getKey());
				}
			}
			System.out.println(typeMap+"THAT :"+stackframe+"Stack peek :"+stack);
			if(stack.contains("end"))
			mm.put("f"+stack+"_e", l);
			else {
				mm.put("f"+stack+"_b", l);
				if(nextTypeMap.contains("end"+stack)) {
					mm.put("f"+stack+"_e", new LinkedHashMap<>());
				}
			}
			
		}else if(nextTypeMap.contains("end"+stack) && !nextTypeMap.equals("@enduml")){
			Map<String, String> l  = new LinkedHashMap<>();
			for (Entry<String, LinkedList<LinkedList<String>>> s : map.entrySet()) {
				for (int i = 0; i < s.getValue().size(); i++) {
					l.put(s.getValue().get(i).get(0),s.getKey());
				}
			}
			System.out.println(typeMap+"HERE :"+stackframe+"Stack peek :"+stack);
			if(stack.contains("loop")) {
				mm.put("f"+stack+"_b", l);
				mm.put("f"+stack+"_e", new LinkedHashMap<>());
			}
			else mm.put("f"+stack+"_e", l);
			System.out.println( stackframe.size() >= 2);
			if(stackframe.size()>=2) {
				stackframe.pop();
				stackframe.pop();
			}
		}else{
			Map<String, String> l  = new LinkedHashMap<>();
			for (Entry<String, LinkedList<LinkedList<String>>> s : map.entrySet()) {
				System.out.println("<TEST>"+s);
				String value = "";
				for (int i = 0; i < s.getValue().size(); i++) {
					value = s.getValue().get(i).get(0);
					l.put(value,s.getKey());
				}
				mm.put(l.keySet().toArray()[0]+"",l);
				System.out.println("TEST RES:"+mm);
			}
		}
		return mm;
	}
	
	public Map<String, Map<String, String>> getForOpt2(Map<String,LinkedList<LinkedList<String>>> map,String typeMap,String nameframe){
		Map<String, Map<String, String>> mm = new LinkedHashMap<>();
		System.out.println("<Type Map> :"+typeMap);
		if(typeMap.contains("alt") || typeMap.contains("opt") || typeMap.contains("loop")){
			Map<String, String> l  = new LinkedHashMap<>();
			for (Entry<String, LinkedList<LinkedList<String>>> s : map.entrySet()) {
				for (int i = 0; i < s.getValue().size(); i++) {
					l.put(s.getValue().get(i).get(0),s.getKey());
				}
			}
			mm.put(nameframe+"_b", l);
		}else if(typeMap.contains("end")){
			Map<String, String> l  = new LinkedHashMap<>();
			for (Entry<String, LinkedList<LinkedList<String>>> s : map.entrySet()) {
				for (int i = 0; i < s.getValue().size(); i++) {
					l.put(s.getValue().get(i).get(0),s.getKey());
				}
			}
			mm.put(nameframe+"_e", l);
		}else{
			Map<String, String> l  = new LinkedHashMap<>();
			for (Entry<String, LinkedList<LinkedList<String>>> s : map.entrySet()) {
				System.out.println("<TEST>"+s);
				String value = "";
				for (int i = 0; i < s.getValue().size(); i++) {
					value = s.getValue().get(i).get(0);
					l.put(value,s.getKey());
				}
				mm.put(l.keySet().toArray()[0]+"",l);

			}
		}
		return mm;
	}
	
//	public void addOpt(Map<String, Map<String, Map<String, String>>> map) {
//		optProcess.add(map);
//	}

	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getNormalProcess() {
		return normalProcess;
	}

	public void setNormalProcess(LinkedList<Map<String, LinkedList<LinkedList<String>>>> normalProcess) {
		this.normalProcess = normalProcess;
	}

	public FrameProcessMap getOptProcess() {
		return optProcess;
	}
	
	public LinkedList<FrameProcess> getOptProcessList() {
		return optProcess.getFrameProcesslist();
	}	

//	public void setOptProcess(LinkedList<FrameProcess> optProcess) {
//		this.optProcess = optProcess;
//	}
	
	public void testOpt(){
		System.err.println("ALT:");
		for (FrameProcess map : this.optProcess.getFrameProcesslist()) {
			System.err.println(map.getName()+":"+map.getAtomicProcess());
		}
	}

}