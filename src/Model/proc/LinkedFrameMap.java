package Model.proc;

public class LinkedFrameMap extends FrameProcessMap{
	
	FrameProcessMap curr;
	LinkedFrameMap next;
	String name="";
	int size = 0;

	public LinkedFrameMap(FrameProcessMap curr) {
		super();
		this.curr = curr;
		this.next = null;
		size++;
	}
	
	
	@Override
	public String getName() { return this.name; }
	
//	public void insertSubFrame(FrameProcessMap frame) {
//		if(size==0) {
//			this.curr = frame;
//			this.name = this.curr.getName();
//		}
//		if(size==1) {
//			this.next = new LinkedFrameMap(frame);
//		}
//		FrameProcessMap temp = next;
//		this.next = new LinkedFrameMap(frame);
//		this.curr = temp;
//		size++;
//	}
	
	public String toString() {
		String s = "";
		while(hasNext()) {
			s+= next()+",";
		}
		return s;
	}
	
	
	public FrameProcessMap getCurrFrame() {
		return this.curr;
	}
	
	public LinkedFrameMap next() {
		return this.next;
	}
	
	public boolean hasNext() {
		return this.next != null;
	}


}
