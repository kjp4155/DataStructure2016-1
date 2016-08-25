import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Subway
{
	// Hashtable DB to store every nodes.
	static Hashtable <String, Vertex> Stations = new Hashtable<String, Vertex>();
	// NameCodePair is DB to search nodes with only names. 
	static Hashtable <String, LinkedList<String> > NameCodePair = new Hashtable <String, LinkedList<String>>();

	public static final String UTF8_BOM = "\uFEFF";
	// Removing BOM character.
	// Copied Method from http://stackoverflow.com/questions/21891578/removing-bom-characters-using-java
	private static String removeUTF8BOM(String s) {
	    if (s.startsWith(UTF8_BOM)) {
	        s = s.substring(1);
	    }
	    return s;
	}
	
	public static void main(String args[]) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		// Parse data input. And put those to database.
		// file reader copied from http://stackoverflow.com/questions/5868369/how-to-read-a-large-text-file-line-by-line-using-java
		try (BufferedReader br1 = new BufferedReader(new FileReader(args[0]))) {
		    String line;
		   
		    // Process each line
		    // Make stations. 
		    while ((line = br1.readLine()) != null) {
		    	//Remove BOM
		    	line = removeUTF8BOM(line);
		    	
		    	// Blank line
		    	if( line.length() == 0 || line.charAt(0) == ' ' || line.charAt(0) == '\n' || line.charAt(0) == '\t' ) break;
		    	
		    	// Make nodes. (Stations)
		    	String code = line.split("\\s")[0];
		    	String name = line.split("\\s")[1];
		    	String linecode = line.split("\\s")[2];
		    	Stations.put( code, new Vertex(code, name, linecode) );
		    	if( NameCodePair.get(name) != null ) NameCodePair.get(name).add(code);
		    	else{
		    		NameCodePair.put(name, new LinkedList<String>());
		    		NameCodePair.get(name).add(code);
		    	}
		    }
		    
		    // Make edges
		    while ((line = br1.readLine()) != null) { 
		    	// Blank Line
		    	if( line.length() == 0 || line.charAt(0) == ' ' || line.charAt(0) == '\n' || line.charAt(0) == '\t' ) break;
		    	String srcCode = line.split("\\s")[0];
		    	String dstCode = line.split("\\s")[1];
		    	int time = Integer.parseInt(line.split("\\s")[2]);
		    	Stations.get(srcCode).addEdge( Stations.get(dstCode), time);
		    }
		}
		
		// Dealing with Transfers
		// Seperate nodes are made with same name, different lines.
		// Connect those to form a clique
		for( String key : NameCodePair.keySet() ){
			for( String v1 : NameCodePair.get(key) ){
				for( String v2 : NameCodePair.get(key) ){
					if( !v1.equals(v2) && !v1.equals(v2) ) {
						Stations.get(v1).addTransferEdge( Stations.get(v2), 5);
					}
				}
			}
		}

		
		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input)
	{
		// Remove BOM
    	input = removeUTF8BOM(input);
    	
    	// This becomes true when we have to calculate minimum transfer path. (with input '!' )
    	boolean min_change = false;
		if( input.split("\\s").length == 3 ) min_change = true;
		
		String srcName = input.split("\\s")[0];
		String dstName = input.split("\\s")[1];
		
		/* ------------------------- Initializing --------------------*/
		for(String key : Stations.keySet() ){
			Stations.get(key).initialize();
		}
		
		String srcCode = NameCodePair.get(srcName).element();
		String dstCode = NameCodePair.get(dstName).element();
		
		// Starting node could be transfer node. 
		// Deal with it by making every node in clique to 'minimum node'
		for( String code : NameCodePair.get(srcName) ){
			Stations.get(code).time = 0;
			Stations.get(code).transferCnt = 0;
		}
		/* --------------------------Initialize ends --------------------*/
		
			
		/*---------------------------Dijkstra Start ---------------------*/
		// PseudoCode referenced from https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
		
		// min_heap to calculate min_distance faster.
		// We can use Fibonacci heap for better performance. However, ode might be very longer.
		PriorityQueue<Vertex> heap = null;
		if( !min_change ){ // shortest_path mode
			heap = new PriorityQueue<Vertex>(10, new Comparator<Vertex>(){
				@Override
				public int compare(Vertex v1, Vertex v2){
					return v1.time - v2.time;
				}	
			});
		}
		else if( min_change ){ // minimum transfer path mode
			heap = new PriorityQueue<Vertex>(10, new Comparator<Vertex>(){
				@Override
				public int compare(Vertex v1, Vertex v2){
					if( v1.transferCnt != v2.transferCnt ){ // Compare with transferCnt first.
						return v1.transferCnt - v2.transferCnt;
					}
					return v1.time - v2.time;
				}	
			});
		}
		
		// Add every node to heap
		for(String key : Stations.keySet()){
			heap.add(Stations.get(key));
		}
		String currentFocus = srcCode;
		
		while( !heap.isEmpty() ){
			// find min distance in not done set
			currentFocus = heap.remove().code;
			
			// For every adjacent nodes.
			for(Edge e : Stations.get(currentFocus).adjacencyList){
				Vertex e_dst = e.dst; // just for readability
				Vertex e_src = e.src;
				
				int alt = e_src.time + e.time;
				int altTransfer = e_src.transferCnt;
				if(!e_dst.linecode.equals( e_src.linecode ) ) altTransfer ++;
				
				if( min_change ){ // Minimum transfer path Mode. Condition to relax are diferrent
					if( altTransfer < e_dst.transferCnt || (altTransfer == e_dst.transferCnt && alt < e_dst.time ) ){
						e_dst.time = alt;
						e_dst.before = e_src.code;
						// Transfering
						if( e.isTransfer == true ) e_dst.transferCnt = e_src.transferCnt + 1;
						else e_dst.transferCnt = e_src.transferCnt;
						// Readjust heap by simply remove, and add again into heap
						heap.remove(e_dst);
						heap.add(e_dst);
					}
				}
				else{ // Shortest path Mode
					if( alt < e_dst.time ){
						e_dst.time = alt;
						e_dst.before = e_src.code;
						// Readjust heap by simply remove, and add again into heap
						heap.remove(e_dst);
						heap.add(e_dst);
						}
				}
				
			}
			if( currentFocus.equals(dstCode)) break;
		}
		
		// If last node is part of clique. (transfer node) 
		if( input.split(" ").length == 2 ){ // shortest_path mode
			int min = Integer.MAX_VALUE;
			
			for( String code : NameCodePair.get(dstName) ){
				if( Stations.get(code).time < min ){
					dstCode = code;
					min = Stations.get(code).time;
				}
			}
		}
		else{ // minimum transfer path mode
			int min = Integer.MAX_VALUE;
			int minChange = Integer.MAX_VALUE;
			for( String code : NameCodePair.get(dstName) ){
				Vertex temp = Stations.get(code);
				if( temp.transferCnt < minChange){
					min = temp.time;
					minChange = temp.transferCnt;
					dstCode = temp.code;
					continue;
				}
				else if( temp.transferCnt == minChange && temp.time < min){
					dstCode = code;
					min = Stations.get(code).time;
				}
				else if( temp.transferCnt > minChange ){
					continue;
				}
			}
		}
		
		/* **************************Dijkstra ends***************************** */
		
		// Stack to contain every station codes
		LinkedList<String> Result = new LinkedList<String>();
		
		// Add every nodes to stack
		String before = Stations.get(dstCode).before;
		Result.push(dstCode);
		while(true){
			if( Stations.get(before).name.equals(srcName) ) break;
			
			Result.push(before);
			before = Stations.get(before).before;
			
		}
	
		// Make path string
		Result.push(srcCode);
		String path = "";
		String beforeName = "";
		while( !Result.isEmpty() ){
			String code = Result.pop();
			String currentName = Stations.get(code).name;
			
			// transfer nodes will occur twice consequetively in stack. Deal with it
			// Make tranfer nodes -> [ Name ]
			if( beforeName.equals(currentName) ){
				int index = path.lastIndexOf(beforeName);
				path = path.substring(0,index) + "[" + beforeName + "]" +path.substring(index+beforeName.length(),path.length()-1);
				path += " ";
			}
			else{
				path += currentName;
				path += " ";
			}
			beforeName = currentName;
		}
	
		
		int resultTime = Stations.get(dstCode).time;
		path = path.substring(0, path.length() - 1 ); // removing whitespace at last
		
		// Dealing with some exceptions.
		// Redundant transfer occured in first node
		if( path.charAt(0) == '['){
			resultTime -= 5;
			path = path.replaceFirst("\\[", "");
			path = path.replaceFirst("\\]", "");
		}
		
		// Redundant transfer occured in last node
		if( path.charAt( path.length() - 1 ) == ']' ){
			resultTime -= 5;
			int index = path.lastIndexOf('[');
			path = path.substring(0,index) + path.substring(index+1,path.length()-1);
		}
		
		System.out.println(path);
		System.out.println(resultTime);
		
	}
}