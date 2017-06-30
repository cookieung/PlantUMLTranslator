package Model.Proc;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class ProcessLinkedList {
	
	private LinkedList<Map<String,LinkedList<LinkedList<String>>>> normalProcess;
	
	private LinkedList<Map<String, Map<String, LinkedList<LinkedList<String>>>>> altProcess;
	
	public ProcessLinkedList(){
		this.normalProcess = new LinkedList<>();
		this.altProcess = new LinkedList<>();
	}
	
	public int size(){
		return normalProcess.size();
	}
	
	public void addNormal(Map<String,LinkedList<LinkedList<String>>> map,String typeMap) {
		normalProcess.add(map);
		Map<String, Map<String, LinkedList<LinkedList<String>>>> m = new LinkedHashMap<>();
		if(!typeMap.equals("alt") && !typeMap.equals("else")) m.put("NaN", getForAlt(map, typeMap));
		else m.put(typeMap, getForAlt(map, typeMap));
		System.out.println(typeMap +"Add Normal Map :"+map+">>"+m);
		altProcess.add(m);
	}
	
	public Map<String, LinkedList<LinkedList<String>>> getForAlt(Map<String,LinkedList<LinkedList<String>>> map,String typeMap){
		Map<String, LinkedList<LinkedList<String>>> mm = new LinkedHashMap<>();
		if(typeMap.equals("alt")){
			LinkedList<LinkedList<String>> l  = new LinkedList<>();
			for (Entry<String, LinkedList<LinkedList<String>>> s : map.entrySet()) {
				LinkedList<String> ll = new LinkedList<>();
				ll.add(s.getKey());
				l.add(ll);
			}
			mm.put("f1_b", l);
		}else if(typeMap.equals("else")){
			LinkedList<LinkedList<String>> l  = new LinkedList<>();
			for (Entry<String, LinkedList<LinkedList<String>>> s : map.entrySet()) {
				LinkedList<String> ll = new LinkedList<>();
				ll.add(s.getKey());
				l.add(ll);
			}
			mm.put("f1_e", l);
		}else{
			for (Entry<String, LinkedList<LinkedList<String>>> s : map.entrySet()) {
				mm.put(s.getKey(), s.getValue());
			}
		}
		return mm;
	}
	
	public void addAlt(Map<String, Map<String, LinkedList<LinkedList<String>>>> map) {
		altProcess.add(map);
	}

	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getNormalProcess() {
		return normalProcess;
	}

	public void setNormalProcess(LinkedList<Map<String, LinkedList<LinkedList<String>>>> normalProcess) {
		this.normalProcess = normalProcess;
	}

	public LinkedList<Map<String, Map<String, LinkedList<LinkedList<String>>>>> getAltProcess() {
		return altProcess;
	}

	public void setAltProcess(LinkedList<Map<String, Map<String, LinkedList<LinkedList<String>>>>> altProcess) {
		this.altProcess = altProcess;
	}
	
	public void testAlt(){
		System.err.println("ALT:"+this.altProcess);
	}

}