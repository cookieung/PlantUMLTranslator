package Controller;

public class DiagramReader {
	
	 public static String readAction(String[] message){
	    	String rs="";

	    	for (int i = 0; i < message.length; i++) {
				if (message[i].length()!=0) {
					System.out.println("MN :"+message[i]);
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
				 System.out.println("if has : "+s);
				 res = s.split(":")[1].split("/");
			 }else{
				 res = s.split("/");
			 }
			 if(s.equals("NaN")) return res;
			 System.out.println("Res1:"+res[0]);
			 if(res[0].length()!=0){
				 res[0] = "r_"+res[0];
			 }
			 System.out.println(res.length);
			 if(res.length==2){
				 System.out.println("Res2:"+res[1]);
				 res[1] = "s_"+res[1];
			 }
			 
			 for (int i = 0; i < res.length; i++) {
					System.out.println("MM :"+res[i]);
					
				}
			 return res;
		 }

}
