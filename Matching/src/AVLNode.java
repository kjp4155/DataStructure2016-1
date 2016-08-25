import java.util.LinkedList;

public class AVLNode {
	LinkedList<PositionNode> list = new LinkedList<PositionNode>();
	public String subString;
	private AVLNode left, right;
	
	
	//Make null Node, using this not recommended
	public AVLNode( ){
		subString = null;
		left = null; right = null;
	}
	
	public AVLNode( String newSubString, PositionNode newPosNode ){
		subString = newSubString;
		list.add(newPosNode);
	}
	
	//Add position to this string
	public void addPosition(PositionNode newPosNode){
		list.add(newPosNode);
	}
	
	//get Height of this node
	//Call recursively
	public int getHeight(){
		int leftH, rightH;
		if( left == null ) leftH = 0;
		else leftH = left.getHeight();
		if( right == null ) rightH =  0;
		else rightH = right.getHeight();
		return Math.max(leftH, rightH) + 1;
	}
	
	//Return LeftHeight - RightHeight
	public int getBalance(){
		int leftH, rightH;
		if( left == null ) leftH = 0;
		else leftH = left.getHeight();
		if( right == null ) rightH = 0;
		else rightH = right.getHeight();
		return leftH - rightH;
	}
	
	//return every position occurrence of this string
	public LinkedList<PositionNode> find(String findSubString){
		if( subString.equals(findSubString) ) return this.list;
		else if( left != null && subString.compareTo( findSubString ) > 0 ) return left.find(findSubString);
		else if( right != null ) return right.find(findSubString);
		else return null;
	}
	
	//Right rotate this node and returns new root
	public AVLNode rightRotate(){
		AVLNode x = this.left;
		AVLNode y = x.right;

		x.right = this;
		this.left = y;
		
		return x;
	}
	
	//Left rotate this node and returns new root
	public AVLNode leftRotate(){
		AVLNode x = this.right;
		AVLNode y = x.left;
		
		x.left = this;
		this.right = y;
		
		return x;
	}
	
	//insert new node.
	public AVLNode insert( String newSubString, PositionNode newPosNode){
		//System.out.println("Insert called [" + newSubString + "," + newPosNode.toString()  + "]");
		
		// Normal BST Insertion
		if( newSubString.equals(subString) ){
			//if already exists, add new position and done
			list.add(newPosNode);
		}
		else if( newSubString.compareTo(subString) < 0 ){
			if( left == null ) left = new AVLNode(newSubString, newPosNode);
			else left = left.insert( newSubString, newPosNode);
		} else{
			if( right == null ) right = new AVLNode(newSubString, newPosNode);
			else right = right.insert( newSubString, newPosNode);
		}
		
		//Check balance.
		int balance = getBalance();
		
		//If not balanced, rotate.
		// LL
		if( balance > 1 && newSubString.compareTo(this.left.subString) < 0 ){
			return this.rightRotate();
		}

		//LR
		if( balance > 1 && newSubString.compareTo(this.left.subString) > 0 ){
			this.left = this.left.leftRotate();
			return this.rightRotate();
		}
		
		//RR
		if( balance < -1 && newSubString.compareTo(this.right.subString) > 0 ){
			return this.leftRotate();
		}
		
		//RL
		if( balance < -1 && newSubString.compareTo(this.right.subString) < 0 ){
			this.right = this.right.rightRotate();
			return this.leftRotate();
		}
		
		//rotation not needed, return this (normal BST)
		return this;
	}
	
	public void delete( String deleteSubString ){
		//Not used at this problem
	}
	

	
	
	public AVLNode getLeft() {
		return left;
	}
	public void setLeft(AVLNode left) {
		this.left = left;
	}
	public AVLNode getRight() {
		return right;
	}
	public void setRight(AVLNode right) {
		this.right = right;
	}
	
	public String getSubString() {
		return subString;
	}
	public void setSubString(String subString) {
		this.subString = subString;
	}
	
	@Override
	public String toString() {
		System.out.println("toString called with " + subString);
		return subString;
	}

	public String toStringPreorder() {
		String str = subString;
		str += " ";
		if( this.getLeft() != null ) str += this.getLeft().toStringPreorder();
		if( this.getRight() != null ) str += this.getRight().toStringPreorder();
		return str;
	}
	
	public String getPosListString(){
		String str = "";
		for( PositionNode pos : list){
			str += pos.toString() + " ";
		}
		return str;
	}
	
}
