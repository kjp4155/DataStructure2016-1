import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class Matching
{
	//Hash table with size 100
	//Initialized when file input
	static MyHashTable hashTable = null;
	
	public Matching(){
		//Initialize
	}
	
	
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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
				System.out.println("ERROR input : " + e.toString());
			}
		}
	}

	//only size of 6 substring can come in to this function
	public static int getHashCode( String str ){
		if( str.length() != 6 ) return -1; //ERROR
		int ret = 0;
		for(int i=0; i<6;i++) ret += (int) str.charAt(i);
		return ret%100;
	}
	
	
	private static void command(String input) throws IOException
	{
		
		char operation = input.charAt(0);
		String operand = input.substring(2);
		
		if( operation == '<'){
			String filename = operand;
			//Initialize hashTable for new file input
			hashTable = new MyHashTable(100);

			//file reader from http://stackoverflow.com/questions/5868369/how-to-read-a-large-text-file-line-by-line-using-java
			try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			    String line;
			    int lineNum = 0;
			   
			    //Process each line
			    while ((line = br.readLine()) != null) {
			    	String subString = null;
			    	int offset= 0;
			    	lineNum++;
			    	while( offset + 6 <= line.length() ){
			    		subString = line.substring(offset, offset+6);
			    		int code = getHashCode(subString);
			    		//If hash bin is empty, then create and add new node
			    		hashTable.insertStringPosPair(subString, new PositionNode(lineNum, offset+1));
			    		offset++;
			    	}
			    	
			    }
			}
		}
		
		
		if( operation == '@' ){
			int code = Integer.parseInt( operand );	
			System.out.println( hashTable.printEveryString(code));
		}		
		
		
		if( operation == '?'){
			String findString = operand;
			int offset= 0;
			LinkedList<PositionNode> possiblePositions = new LinkedList<PositionNode>();
			LinkedList<PositionNode> subStringPositions = new LinkedList<PositionNode>();

			//Offset will increase by 6 if enough substring is unchecked
			//Otherwise, it will move to check last substring
			while( offset+6 <= findString.length() ){
				String subFindString = findString.substring(offset, offset+6);
				int code = getHashCode( subFindString );
				
				//substring is not in data. return 0,0
				if( hashTable.findEveryPosition(subFindString) == null ){
					possiblePositions = new LinkedList<PositionNode>();
					possiblePositions.add(new PositionNode(0,0));
					break;
				}

				else subStringPositions = hashTable.findEveryPosition(subFindString);
				
				if( offset == 0 ){ //first 6 characters, add everything to possiblePositions
					for( PositionNode t : subStringPositions ){
						possiblePositions.add(t);
					}
					
					//Done
					if( findString.length() - offset == 6 ) break;
					//Less than 6 characters unchecked, care the remaining few characters
					if( findString.length() < 12  ) offset = findString.length() - 6;
					// if more than 6 characters unchecked, jump by 6.
					else offset += 6;
					continue;
				}
				
				else{ // Check if leftover substrings match with possiblePositions
					
					//list to be deleted from possibleList since not matched
                    LinkedList<PositionNode> dieList = new LinkedList<PositionNode>();
					for( PositionNode possible : possiblePositions ){
						boolean checked = false; //true when possible has to be deleted
						for( PositionNode t : subStringPositions ){
							if( t.getLineNum() == possible.getLineNum() ){
								if( t.getPosition() == possible.getPosition() +  offset ) checked = true;
							}
						}
						if( checked == false ) dieList.add( new PositionNode(possible.getLineNum(), possible.getPosition()) );
					}
                    
                    for( PositionNode die : dieList ) possiblePositions.remove( die );
				}
				
				
				//Modify offset++ as following
				//Done
				if( findString.length() - offset == 6 ) break;
				//Less than 6 characters unchecked, care the last 6 characters
				if( findString.length() - offset < 12  ) offset = findString.length() - 6;
				// if more than 6 characters unchecked, jump by 6.
				else offset += 6;
			
			}
			
			//PRINT
			String result = "";
			for( PositionNode possible : possiblePositions ){
				result += possible.toString() + " ";
			}
			System.out.println(result.substring(0,result.length()-1));
		}
		
	}
}