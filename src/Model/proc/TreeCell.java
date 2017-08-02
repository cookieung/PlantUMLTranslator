package Model.proc;
public class TreeCell<T>{
	private T datum;
	private TreeCell<T> left, right;


	public TreeCell(T x) { datum = x; }//LEAVE
	public TreeCell(T x, TreeCell<T> lft,TreeCell<T> rgt) {
		datum = x;
		left = lft;
		right = rgt;
	}

	public TreeCell(T datum2, TreeCell<T> treeCell) {
		datum = datum;
		left = null;
		right = treeCell;
	}
	public void setLeft(TreeCell<T> left){
		this.left = left;
	}

	public void setRight(TreeCell<T> right){
		this.right = right;
	}

	public void setDatum(T datum){
		this.datum = datum;
	}

	public TreeCell<T> getRight(){
		return this.right;
	}

	public TreeCell<T> getLeft(){
		return this.left;
	}

	public T getDatum(){
		return this.datum;
	}
	
	

}
