package Model.proc;
public class TestTreeCell{

//	public static void main(String[]args){
//
//		TreeCell left = new TreeCell(new FrameProcessMap("alt1"),new FrameProcess("alt"));
//		TreeCell right = new TreeCell(new FrameProcessMap("alt1"));
//		TreeCell cell = new TreeCell(new FrameProcessMap("loop"),left,right);
//
//		printList(cell);
//		System.out.println("-----------");
////		cell = delete(new Integer(105), cell);
//		printList(cell);
//
//	}

	public static void printList(TreeCell T){
		if(T==null) return ;
		System.out.println(T.getDatum());
		if(T.getLeft()!= null) printList(T.getLeft());
		printList(T.getRight());
	}

	public static TreeCell remove(TreeCell T){
		if(T.getLeft()==null) return null;
		return new TreeCell(T.getDatum(),remove(T.getLeft()),T.getRight());
	}

		public static TreeCell findLeaf(TreeCell T){
			if(T.getLeft()==null) return T;
			return findLeaf(T.getLeft());
		}

//	public static TreeCell delete (Object x, TreeCell node) {
//		if(node==null) return null;
//		if(node.getDatum().compareTo(x)==0){
//			if(node.getLeft()==null && node.getRight()==null) node = null;
//			else if(node.getLeft()==null) {
//				node = node.getRight();
//			}
//			else if(node.getRight()==null)node = node.getLeft();
//			else{
//			node.setDatum(findLeaf(node.getRight()).getDatum());
//			node.setRight(remove(node.getRight()));
//			}
//			return node;
//		}
//		if(node.getDatum().compareTo(x)<0){
//			node.setRight(delete(x, node.getRight()));
//		}
//		else node.setLeft(delete(x, node.getLeft()));
//		return node;
//
//	}

}
