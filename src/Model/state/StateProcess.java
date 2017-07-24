package Model.state;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Model.oop.ProcessList;
import Model.proc.ProcessLinkedList;

public class StateProcess extends ProcessList {
	
	
	public StateProcess(){
		super();
	}

//	public StateProcess(String name,LinkedList<LinkedList<String>> linkedList){
//		processMapByName = new ProcessLinkedList();
//		processMapByState = new ProcessLinkedList();
//		this.addProcess(name, linkedList);
//	}
	

	
	
	
//	public void createProcessMapByState(String name,LinkedList<LinkedList<String>> linkedList) {
//		System.out.println("Change :"+name+">>>"+linkedList);
//		processMapByName.add(makeMapByState());
//	}

	@Override
	public void addProcess(String name, LinkedList<LinkedList<String>> linkedList,String typeName) {
		Map<String, LinkedList<LinkedList<String>>> map = new LinkedHashMap<>();
		map.put(name, linkedList);
		System.out.println("Map in State Process Class :"+map);
		processMapByName.addNormal(map,typeName,"","");
		System.err.println("Object in State Process Class by Name :"+processMapByName);
		processMapByState.setNormalProcess(convert(makeMapByState(processMapByName.getNormalProcess())));
		System.err.println("Object in State Process Class by State :"+processMapByState);
	}
	
	private static LinkedList<Map<String, LinkedList<String>>> makeMapByState(LinkedList<Map<String, LinkedList<LinkedList<String>>>> a){
		LinkedList<Map<String,LinkedList<String>>> listRes = new LinkedList<>();
		String state="";
		System.err.println("SIZE :"+a.size());
		for (int i = 0; i < a.size(); i++) {
			for (Entry<String, LinkedList<LinkedList<String>>> map : a.get(i).entrySet()) {
				String nameMsg = map.getKey();
				LinkedList<LinkedList<String>> ll = map.getValue();
				LinkedList<String> minilist = new LinkedList<>();
				minilist.add(nameMsg);
				LinkedList<LinkedList<String>> l = new LinkedList<>();
				for (int j = 0; j < ll.size(); j++) {
					for (int k = 0; k < ll.get(j).size(); k++) {
						System.err.println(k+">>MiniList :"+ll.get(j).get(k));
						if(k%3==0){
							state = ll.get(j).get(k);
						}
						else if(k%3>0)
						{
							minilist.add(ll.get(j).get(k));							
						}
					}

				}
				l.add(minilist);
				Map<String,LinkedList<String>> result = new LinkedHashMap<>();
				
				result.put(state, minilist);
				listRes.add(result);
				
			}
		}
		System.err.println("Re :"+listRes);
		return listRes;
	}
	
	public static LinkedList<Map<String, LinkedList<LinkedList<String>>>> convert(LinkedList<Map<String, LinkedList<String>>> list){
		String s="";
		List<String> state = new ArrayList<>();
		LinkedList<String> l = new LinkedList<>();
		LinkedList<Map<String, LinkedList<LinkedList<String>>>> lm = new LinkedList<>();
		for (int i = 0; i < list.size(); i++) {
			LinkedList<LinkedList<String>> ll = new LinkedList<>();
			for (Entry<String, LinkedList<String>> map : list.get(i).entrySet()) {
				if(!state.contains(map.getKey()))
				{
					for (int j = 0; j < list.size(); j++) {
						LinkedList<LinkedList<String>> ll2 = new LinkedList<>();
						for (Entry<String, LinkedList<String>> map2 : list.get(j).entrySet()) {
							if(map2.getKey().equals(map.getKey())){
								ll.add(map2.getValue());
							}
							state.add(map.getKey());
							s=map.getKey();
						}
					}
				}
				else continue;
			}
			if(ll.size()!=0){
				Map<String, LinkedList<LinkedList<String>>> m = new LinkedHashMap<>();
				m.put(s,ll);
				lm.add(m);
			}
			
		}
		return lm;
	}



	

}
