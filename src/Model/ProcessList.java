package Model;

import java.util.LinkedList;
import java.util.Map;

public interface ProcessList {
	
	public void addProcess(String name, LinkedList<LinkedList<String>> linkedList,String typeMap);
	
	public int getLength();
	
	public Map<String, LinkedList<LinkedList<String>>> getElement(int index);
	
	public LinkedList<Map<String,LinkedList<LinkedList<String>>>> getProcessListByName();

	public LinkedList<Map<String,LinkedList<LinkedList<String>>>> getProcessListByState();	
	
	public String toString();


}
