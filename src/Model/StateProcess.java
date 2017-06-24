package Model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class StateProcess implements ProcessList {
	
	private LinkedList<Map<String, LinkedList<LinkedList<String>>>> processMapByName;
	
	private LinkedList<Map<String, LinkedList<LinkedList<String>>>> processMapByState;
	
	public StateProcess(){
		processMapByName = new LinkedList<>();
		processMapByState = new LinkedList<>();
	}

	public StateProcess(String name,LinkedList<LinkedList<String>> linkedList){
		processMapByName = new LinkedList<>();
		processMapByState = new LinkedList<>();
		this.addProcess(name, linkedList);
	}
	
	public int getLength() {
		return processMapByName.size();
	}
	
	public int getLenghtByState(){
		return processMapByState.size();
	}
	
	
	
	public void createProcessMapByState(String name,LinkedList<LinkedList<String>> linkedList) {
		processMapByName.add(makeMapByState());
	}

	@Override
	public void addProcess(String name,LinkedList<LinkedList<String>> linkedList) {
		Map<String, LinkedList<LinkedList<String>>> map = new LinkedHashMap<>();
		map.put(name, linkedList);
		System.out.println("Map in State Process Class :"+map);
		processMapByName.add(map);
		System.out.println("Object in State Process Class :"+processMapByName);
		this.createProcessMapByState(name, linkedList);
	}
	
	private Map<String, LinkedList<LinkedList<String>>> makeMapByState(){
		Map<String,LinkedList<LinkedList<String>>> result = new LinkedHashMap<>();
		for (int i = 0; i < processMapByName.size(); i++) {
			for (Entry<String, LinkedList<LinkedList<String>>> map : processMapByName.get(i).entrySet()) {
				String nameMsg = map.getKey();
				LinkedList<LinkedList<String>> ll = map.getValue();
				LinkedList<String> minilist = new LinkedList<>();
				minilist.add(nameMsg);
				for (int j = 2; j < ll.size(); j++) {
					for (int k = 0; k < ll.get(j).size(); k++) {
						if(k%3==2)
						{
							minilist.add(ll.get(j).get(k-1));
							minilist.add(ll.get(i).get(k));
							LinkedList<LinkedList<String>> l = new LinkedList<>();
							l.add(minilist);
							result.put(minilist.get(k-2), l);
							
						}
					}
				}
			}
		}
		return result;
	}

	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcessMapByName() {
		return processMapByName;
	}

	public void setProcessMapByName(LinkedList<Map<String, LinkedList<LinkedList<String>>>> processMapByName) {
		this.processMapByName = processMapByName;
	}

	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcessMapByState() {
		return processMapByState;
	}

	public void setProcessMapByState(LinkedList<Map<String, LinkedList<LinkedList<String>>>> processMapByState) {
		this.processMapByState = processMapByState;
	}

	@Override
	public Map<String, LinkedList<LinkedList<String>>> getElement(int index) {
		return processMapByName.get(index);
	}

	@Override
	public LinkedList<Map<String, LinkedList<LinkedList<String>>>> getProcessList() {
		return this.getProcessMapByName();
	}
	
	public String toString(){
		return processMapByName+"";
	}
	

	
	

}
