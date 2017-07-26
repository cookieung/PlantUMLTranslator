package Model.proc;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

public class ProcessLinkedList {
	
	private LinkedList<Map<String,LinkedList<LinkedList<String>>>> normalProcess;
	
	private LinkedList<Map<String, Map<String, Map<String, String>>>> optProcess;
	
	Stack<String> stackframe = new Stack<>();
	
	public ProcessLinkedList(){
		this.normalProcess = new LinkedList<>();
		this.optProcess = new LinkedList<>();
	}
	
	public int size(){
		return normalProcess.size();
	}

	public void addNormal(Map<String,LinkedList<LinkedList<String>>> map,String typeMap,String nextTypeName,String nameframe) {
		normalProcess.add(map);
		Map<String, Map<String, Map<String, String>>> m = new LinkedHashMap<>();
		System.out.println("*[Type Map :"+typeMap);
		System.out.println("[Next Type :"+nextTypeName);
		if( typeMap.contains("end") || (!nextTypeName.contains("end") && !typeMap.contains("alt") && !typeMap.contains("opt") && !typeMap.contains("else") && !typeMap.contains("loop"))) {
			System.out.println("ABC"+typeMap);
			typeMap = "NaN";
			m.put("NaN", getForOpt(map, typeMap,nameframe));
		}
		else {
			System.out.println("CDE"+typeMap);
			if(typeMap.contains("loop") && nextTypeName.contains("end")) {
				typeMap = typeMap;
			}else if(typeMap.contains("else")) {
				typeMap = "end"+typeMap.replaceAll("\\D+", "");
			}else if(nextTypeName.contains("end")) {
				typeMap = nextTypeName;
			}
			System.out.println("#Q"+typeMap);
			if(typeMap.contains("end") && !stackframe.isEmpty()) typeMap = "end"+stackframe.peek().replaceAll("\\D+", "");
			else stackframe.push(typeMap);
			System.out.println("<<Type Map :"+typeMap);
			m.put(typeMap, getForOpt(map, typeMap,nameframe));
		}
		System.out.println(typeMap +"Add Normal Map :"+map+">>"+m);
		optProcess.add(m);
//		if(typeMap.contains("alt")||typeMap.contains("opt")||typeMap.contains("loop")||typeMap.contains("end"))	
		System.out.println("Stack Frame :"+stackframe);
	}
	
	public void addNormal2(Map<String,LinkedList<LinkedList<String>>> map,String typeMap,String nextTypeName,String nameframe) {
		normalProcess.add(map);
		Map<String, Map<String, Map<String, String>>> m = new LinkedHashMap<>();
		System.out.println("[Type Map :"+typeMap);
		System.out.println("[Next Type :"+nextTypeName);
		if( typeMap.contains("end") || (!nextTypeName.contains("end") && !typeMap.contains("alt") && !typeMap.contains("opt") && !typeMap.contains("else") && !typeMap.contains("loop"))) {
			m.put("NaN", getForOpt(map, typeMap,nameframe));
		}
		else {
			
			if(typeMap.contains("loop") && nextTypeName.contains("end")) {
				typeMap = typeMap;
			}else if(typeMap.contains("else")) {
				typeMap = "end"+typeMap.replaceAll("\\D+", "");
			}else if(nextTypeName.contains("end")) {
				typeMap = nextTypeName;
			}
			
			System.out.println("<<Type Map :"+typeMap);
			m.put(typeMap, getForOpt(map, typeMap,nameframe));
		}
		System.out.println(typeMap +"Add Normal Map :"+map+">>"+m);
		optProcess.add(m);
		if(typeMap.contains("alt")||typeMap.contains("opt")||typeMap.contains("loop")||typeMap.contains("end"))
		stackframe.push(typeMap);
		System.out.println("Stack Frame :"+stackframe);
	}

	public Map<String, Map<String, String>> getForOpt(Map<String,LinkedList<LinkedList<String>>> map,String typeMap,String nameframe){
		Map<String, Map<String, String>> mm = new LinkedHashMap<>();
		System.out.println("<Type Map> :"+typeMap);
		String stack;
		if(stackframe.isEmpty()) stack = "";
		else stack = stackframe.peek();
		if(typeMap.contains("alt") || typeMap.contains("opt") || typeMap.contains("loop")){
			Map<String, String> l  = new LinkedHashMap<>();
			for (Entry<String, LinkedList<LinkedList<String>>> s : map.entrySet()) {
				for (int i = 0; i < s.getValue().size(); i++) {
					l.put(s.getValue().get(i).get(0),s.getKey());
				}
			}
			System.out.println(stackframe+"Stack peek :"+stack);
			if(stack.contains("end"))
			mm.put("f"+stack+"_e", l);
			else mm.put("f"+stack+"_b", l);
			
		}else if(typeMap.contains("end")){
			Map<String, String> l  = new LinkedHashMap<>();
			for (Entry<String, LinkedList<LinkedList<String>>> s : map.entrySet()) {
				for (int i = 0; i < s.getValue().size(); i++) {
					l.put(s.getValue().get(i).get(0),s.getKey());
				}
			}
			System.out.println(stackframe+"Stack peek :"+stack);
			mm.put("f"+stack+"_e", l);
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
	
	public void addOpt(Map<String, Map<String, Map<String, String>>> map) {
		optProcess.add(map);
	}

	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getNormalProcess() {
		return normalProcess;
	}

	public void setNormalProcess(LinkedList<Map<String, LinkedList<LinkedList<String>>>> normalProcess) {
		this.normalProcess = normalProcess;
	}

	public LinkedList<Map<String, Map<String, Map<String,String>>>> getOptProcess() {
		return optProcess;
	}

	public void setOptProcess(LinkedList<Map<String, Map<String, Map<String, String>>>> optProcess) {
		this.optProcess = optProcess;
	}
	
	public void testOpt(){
		System.err.println("ALT:"+this.optProcess);
	}

}