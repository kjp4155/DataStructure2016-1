import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorTest
{
	public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\D|\\d*");

	//chekc a character is an valid operator
	private static boolean isValidOperator(char op){
		if( op == '(' || op == ')' || op == '^' || op == '-' || op == '*' || op == '/' || op == '%' || op == '+') return true;
		return false;
	}
	
	// return operator's precedence
	private static int getPrecedenceLevel(char op) {
		if (op == '(' || op == ')')
			return 10;
		else if (op == '^')
			return 1;
		else if (op == '~')
			return 2;
		else if (op == '*' || op == '/' || op == '%')
			return 3;
		else if (op == '+' || op == '-')
			return 4;
		return -1;
	}

	// return 1 if op1's precedence is higher than op2
	// return 0 if same precedence
	// return -1 if op2's precedence is higher than op1
	private static int comparePrecedence(char op1, char op2) {
		int op1_level, op2_level;
		op1_level = CalculatorTest.getPrecedenceLevel(op1);
		op2_level = CalculatorTest.getPrecedenceLevel(op2);
		if (op1_level < op2_level)
			return 1;
		else if (op1_level == op2_level)
			return 0;
		return -1;
	}
	
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
				e.printStackTrace();
			}
		}
	}

	
	
	private static void command(String input)
	{
		//queue for save postfix notation. both number and operators
		Queue<String> postfix = new LinkedList<String>();
	
		Matcher m = EXPRESSION_PATTERN.matcher(input);
		boolean operatorBefore = true;

		//Stack for operands when calculating postfix expression
		Stack<Long> operandStack = new Stack<>();
		//Stack for operators when infix -> postfix
		Stack<Character> operatorStack = new Stack<>();
		
		char firstChar;
		String parsedString;
		char operator = '!';
		
		boolean is_operand_before = false;
		
		while(m.find()){ //read a operator or a number
			parsedString = m.group();
			
			//remove whitespaces
			if( parsedString.length() == 0 || parsedString == null || parsedString == " " || parsedString == "\t" || parsedString == "\n" ) continue;
			firstChar = parsedString.charAt(0);
			if( firstChar == ' ' || firstChar == '\t' || firstChar == '\n' ) continue;
			
			
			//number input
			if( Character.isDigit(firstChar) ){ 
				if( is_operand_before == true){ //if two consequtive number, then ERROR
					System.out.println("ERROR");
					return;
				}
				//add number to postfix as it comes.
				postfix.add( m.group() );  //
				is_operand_before = true;
				continue;
			}
			//operator input
			else{ 
				operator = firstChar;

				if( !isValidOperator(operator) ){ //if invalid operator, then ERROR
					System.out.println("ERROR");
					return;
				}
				if( operator == '-' && !is_operand_before ) operator = '~'; //check if it's unary. if it is, change to '~' 
				else if( operator != '(' && operator != ')' && !is_operand_before ){ //if two consequtive operator, then ERROR (except '~')
					System.out.println("ERROR");
					return;
				}
			}
			
			// error handling & postfix conversion of parenthesis.
			// we can think '(' as operator, and ')' as operand. since (3+5) equals 8. (3+5)+10 == 8+10
			if( operator == '(' ){ 
				if( is_operand_before ){ //ERROR like 3(5) 
					System.out.println("ERROR");
					return;
				}
				operatorStack.push(operator); //push '('to stack.
				is_operand_before = false;
				continue;
			}
			
			if( operator == ')' ){ 
				if( operatorStack.isEmpty() ){ //if no operator left, ERROR
					System.out.println("ERROR");
					return;
				}
				
				while( operatorStack.peek() != '(' ){	
					postfix.add( operatorStack.pop().toString() );// pop and add to postfix until '(' found.
					if( operatorStack.isEmpty() ){ //if no '(' found, ERROR
						System.out.println("ERROR");
						return;
					}
				}
				operatorStack.pop(); //pop '('
				is_operand_before = true;
				continue;
			}
			
			//push operator to stack if it's the first one.
			if( operatorStack.isEmpty() || operatorStack.peek() == '(' ){
				operatorStack.push(operator);
				is_operand_before = false;
				continue;
			}

			//pop operators from stack until new operator's precedence is higher than top.
			while( !operatorStack.isEmpty() && comparePrecedence(operator, operatorStack.peek() ) < 0 ){
				postfix.add( operatorStack.pop().toString() );
			}
			
			//if equal precedence, use associative rule. pop and add to postfix only if operator is left-associative.
			if( !operatorStack.isEmpty() && comparePrecedence(operator, operatorStack.peek() ) == 0){
				if( operator == '^' || operator == '~'){}
				else{
					postfix.add( operatorStack.pop().toString() );
				}
			}
			
			is_operand_before = false;
			operatorStack.push( operator );
		} // end of while(m.find())
		
		// end of infix expression.
		// pop any leftover operators in stack.
		while( !operatorStack.isEmpty() ){
			if( operatorStack.peek() == '(' ){ //if '(' appears, it means there's not enough ')'. So ERROR.
				System.out.println("ERROR");
				return;
			}
			postfix.add( operatorStack.pop().toString() );
		}

	
		long a,b;
		//evaluate postfix expression
		for( String iter : postfix ){
			//number. push to operandStack as it comes.
			if( Character.isDigit(iter.charAt(0)) ){
				operandStack.push( Long.parseLong(iter) );
				continue;
			}
			//else operator.
			else operator = iter.charAt(0);

			//deal with operators.
			//if not enough operands for operator, then ERROR
			if( operator == '+' || operator == '-' || operator == '/' || operator == '%' || operator == '*' || operator == '^' ){
	       		 if( operandStack.size() < 2 ){
	    				System.out.println("ERROR");
	   				return;
	       		 }
			} 
			if( operator == '~' && operandStack.size() < 1 ){
  				System.out.println("ERROR");
 				return;
     		 }
        	
			//calculate operator.
        	 switch(operator){
        	 case '+':
        		 a = operandStack.pop();
        		 b = operandStack.pop();
        		 operandStack.push(a+b);
        		 break;
        	 case '-':
        		 a = operandStack.pop();
        		 b = operandStack.pop();
        		 operandStack.push(b-a);
        		 break;
        	 case '/':
        		 a = operandStack.pop();
        		 if( a == 0 ){	//ERROR if divide by zero.
        			 System.out.println("ERROR");
        			 return;
        		 }
        		 b = operandStack.pop();
        		 operandStack.push(b/a);
        		 break;
        	 case '%':
        		 a = operandStack.pop();
        		 if( a == 0 ){	//error f divide by zero
        			 System.out.println("ERROR");
        			 return;
        		 }
        		 b = operandStack.pop();
        		 operandStack.push(b%a);
        		 break;
        	 case '*':
        		 a = operandStack.pop();
        		 b = operandStack.pop();
        		 operandStack.push(b*a);
        		 break;
        	 case '^':
        		 a = operandStack.pop();
        		 b = operandStack.pop();
        		 if( a < 0 && b == 0){	//error if (0)^(negative)
        			 System.out.println("ERROR");
        			 return;
        		 }
        		 operandStack.push( (long) Math.pow(b, a) );
        		 break;
        	 case '~':
        		 a = operandStack.pop();
        		 operandStack.push(-a);
        		 break;
        	 } // end of operator switch
        
			
		} // end of for
		
		if( operandStack.isEmpty() ){	//if operandStack is empty, it means no operands came in. ex) "(())" So ERROR.
			System.out.println("ERROR");
			return;
		}
		
		
		//Print answer.
		while( postfix.size() > 1){
			System.out.print(postfix.peek() + " ");
			postfix.remove();
		}
		System.out.print(postfix.peek() );
		System.out.println();
		
		System.out.println(operandStack.pop());
		
	}
}