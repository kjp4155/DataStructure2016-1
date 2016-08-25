import java.util.LinkedList;

public class MyHashTable{
	
	private AVLNode[] data = null;
	
	public MyHashTable(int size){
		data = new AVLNode[size];
		for(int i=0; i<size; i++) data[i] = null;
	}
	
	public void delete(String delString){
		// not used
	}
	
	//only size of 6 substring can come in to this function
	public static int getHashCode( String str ){
		if( str.length() != 6 ) return -1; //ERROR
		int ret = 0;
		for(int i=0; i<6;i++) ret += (int) str.charAt(i);
		return ret%100;
	}
	
	//Insert new Data to HashTable
	public void insertStringPosPair( String newString, PositionNode newPos ){
		int code = getHashCode(newString);
		//if slot is empty, make new tree
		if( data[code] == null ) data[code] = new AVLNode(newString, newPos);
		//Insert data into tree
		else data[code] = data[code].insert(newString, newPos);
	}
	
	//Print every string in avlTree for @ operation
	public String printEveryString( int code ){
		String result = null;
		//if no data, return EMPTY
		if( data[code] == null ) result = "EMPTY ";
		else result = data[code].toStringPreorder();
		return result.substring(0, result.length() - 1);
	}
	
	//Return every position of specific substring for ? operation
	public LinkedList<PositionNode> findEveryPosition( String findString ){
		int code = getHashCode(findString);
		LinkedList<PositionNode> result = new LinkedList<PositionNode>();
		if( data[code] == null || data[code].find(findString) == null){
			result = null;
		}
		else result = data[code].find(findString);
		
		return result;
	}
	
}
