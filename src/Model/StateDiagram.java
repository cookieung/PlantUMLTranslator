package Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StateDiagram implements Diagram {
	
	private String name;
	private ProcessList processes;

	
	public StateDiagram(String name){
		this.name = name;
		this.processes = new StateProcess();
	}
	
	@Override
	public void addProcess(ProcessList process) {
		this.processes = process;
	}

	@Override
	public ProcessList getProcesses() {
		return processes;
	}

	public void setProcesses(ProcessList processes) {
		this.processes = processes;
	}

	public String toString() {
		return name+" = "+processes.toString()+"\n";
	}


	@Override
	public String getName() {
		return this.name;
	}
	
//	public static Map<String, LinkedList<LinkedList<String>>> checkLeftState(LinkedList<LinkedList<String>> ll){
//		List<String> list = new ArrayList<>();
//		Map<String,LinkedList<LinkedList<String>>> m = new LinkedHashMap<>();
//		for (int i = 0; i < ll.size(); i++) {
//			for (int j = 0; j < ll.get(i).size(); j++) {
//				if(j%3==0 && !list.contains(ll.get(i).get(j))){
////					System.out.println("<"+ll.get(i).get(j)+">");
//					m.put(key, getSpec(ll.get(i).get(j), ll));
//					System.out.println();
//					list.add(ll.get(i).get(j));
////					System.out.println(list);
//				}
//			}
//
//		}
//		return m;
//		
//	}
//	
//	private static LinkedList<Map<String,LinkedList<LinkedList<String>>>> getCombinationState(String s,LinkedList<Map<String,LinkedList<LinkedList<String>>>> listmap) {
//		LinkedList<LinkedList<String>> list = new LinkedList<>();
//		LinkedList<Map<String,LinkedList<LinkedList<String>>>> result = new LinkedList<>();
//		System.out.println("---------------------");
//		for (int i = 0; i < listmap.size(); i++) {
//			for (Entry<String, LinkedList<LinkedList<String>>> map : listmap.get(i).entrySet()) {
//				String nameProcess = map.getKey();
//				LinkedList<LinkedList<String>> ll = map.getValue();
//				Map<String, LinkedList<LinkedList<String>>> nmap = new LinkedHashMap<>();
//				for (int i = 0; i < ll.size(); i++) {
//					for (int j = 0; j < ll.get(i).size(); j++) {
//						if(ll.get(i).get(j).contains(">") && ll.get(i).get(j-1).equals(s))
//						{
//							list.add(ll.get(i));
//							nmap.put(nameProcess, list);
//							list = new LinkedList<>();
//						}
//
//					}
//
//				}
//				result.add(nmap);
//
//			}
//		}
//		
//		System.out.println("RESULT :"+result);
//		System.out.println("---------------------");
//		return result;
//
//	}

	
	

}