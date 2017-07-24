package Model.proc;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class ProcessLinkedList {
	
	private LinkedList<Map<String,LinkedList<LinkedList<String>>>> normalProcess;
	
	private LinkedList<Map<String, Map<String, Map<String, String>>>> optProcess;
	
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
		if( !nextTypeName.contains("end") && !typeMap.contains("alt") && !typeMap.contains("opt") && !typeMap.contains("else") && !typeMap.contains("loop")) {
			m.put("NaN", getForOpt(map, typeMap,nameframe));
		}
		else {
			if(typeMap.contains("else")) typeMap = "end"+typeMap.replaceAll("\\D+", "");
			m.put(typeMap, getForOpt(map, typeMap,nameframe));
		}
		System.out.println(typeMap +"Add Normal Map :"+map+">>"+m);
		optProcess.add(m);
	}
	
	public Map<String, Map<String, String>> getForOpt(Map<String,LinkedList<LinkedList<String>>> map,String typeMap,String nameframe){
		Map<String, Map<String, String>> mm = new LinkedHashMap<>();
		if(typeMap.contains("alt") || typeMap.contains("opt") || typeMap.contains("loop")){
			Map<String, String> l  = new LinkedHashMap<>();
			for (Entry<String, LinkedList<LinkedList<String>>> s : map.entrySet()) {
				for (int i = 0; i < s.getValue().size(); i++) {
					l.put(s.getValue().get(i).get(0),s.getKey());
				}
			}
			mm.put(nameframe+"_b", l);
		}else if(typeMap.contains("else")){
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