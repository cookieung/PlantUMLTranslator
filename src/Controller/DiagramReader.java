package Controller;

public class DiagramReader {
	
	 public static String readAction(String[] message){
	    	String rs="";

	    	for (int i = 0; i < message.length; i++) {
				if (message[i].length()!=0) {
					rs+=message[i];
					if(i<message.length-1) rs+= " -> ";
				}
				
			}
	    	if(rs.length()==0) return "NaN";
	    	return rs;
	    }

	public static String[] messageWithStatus(String s){
			 String[] res = new String[]{"",""};
			 if(s.contains(":")){
				 res = s.split(":")[1].split("/");
			 }else{
				 res = s.split("/");
			 }
			 if(s.equals("NaN")) return res;
			 if(res[0].length()!=0){
				 res[0] = "r_"+res[0];
			 }
			 if(res.length==2){
				 res[1] = "s_"+res[1];
			 }
			 
			 return res;
		 }

}
