import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "Invalid Input.";
    public static final int MAX_DIGITS = 205;
    
    public int[] value = new int[MAX_DIGITS];
    public int digits = 0;
    public char sign = '+';
    
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\D|\\d*"); // \\d*|\\D <- Why it doesn't work?
 
 
    //initialize to 0
    public BigInteger()
    {
    	int i;
		for(i=0;i<MAX_DIGITS;i++){
			this.value[i] = 0;
		}
    }
    
    //initialize to input string & sign
    public BigInteger(String str, char sign)
    {
    	int i;
    	
    	this.sign = sign;
    	for(i=0;i<MAX_DIGITS;i++){
    		this.value[i] = 0;
    	}
    	
    	this.digits = str.length();
    	for(i=0;i<digits;i++){
    		this.value[i] = Character.getNumericValue( str.charAt(digits-i-1) );
    	}
    }
 
    //copy num to this
    public void copy(BigInteger num)
    {
    	this.sign = num.sign;
    	this.digits = num.digits;
    	for(int i=0; i<this.digits; i++){
    		this.value[i] = num.value[i];
    	}
    }
    
    //return true if num1 is larger, false when num2 is larger. same value = true
    //only compare absolute value of BigInteger
    public static boolean compare(BigInteger num1, BigInteger num2)
    {
    	if( num1.digits > num2.digits ) return true;
    	else if( num1.digits < num2.digits ) return false;
    	else{
    		int i;
    		for(i=num1.digits-1; i>=0; i--){
    			if( num1.value[i] > num2.value[i] ) return true;
    			else if( num1.value[i] < num2.value[i] ) return false;
    		}
    	}
		return true;
    }
    
    //change sign
    public void setNegetive()
    {
    	if( this.sign == '+' ) this.sign = '-';
    	else if( this.sign == '-' ) this.sign = '+';
    }
    
    //swap content of num1 and num2
    public static void swapInteger(BigInteger num1, BigInteger num2)
    {
    	int temp_digits;
    	int[] temp_value = new int[105];
    	char temp_sign;
    	int i;
    	
    	temp_digits = num1.digits;
    	temp_sign = num1.sign;
    	for(i=0;i<num1.digits;i++){
    		temp_value[i] = num1.value[i];
    	}
    	
    	num1.digits = num2.digits;
    	num1.sign = num2.sign;
    	for(i=0;i<num2.digits;i++){
    		num1.value[i] = num2.value[i];
    	}
    	
    	num2.digits = temp_digits;
    	num2.sign = temp_sign;
    	for(i=0;i<temp_digits;i++){
    		num2.value[i] = temp_value[i];
    	}
    }
    
    // num3 = num1+num2, return num3
    public static BigInteger add(BigInteger num1, BigInteger num2)
    {	
    	BigInteger num3 = new BigInteger();
    	int i=0;
    	boolean compare = BigInteger.compare(num1, num2); //true when num1 is larger
    	
    	//set temporary digits of num3
    	if( compare ) num3.digits = num1.digits;
    	else num3.digits = num2.digits;
    	
    	//same sign 
    	//=> (+) + (+) = (+) , (-) + (-) = (-)
    	if( num1.sign == num2.sign ){
    		
    		num3.sign = num1.sign;
    		
    		for(i=0;i<num3.digits;i++){
    			num3.value[i] = num1.value[i] + num2.value[i];		
    		}
    		
    		//checking carries
    		for(i=0;i<num3.digits;i++){
    			//if there's carry
    			while( num3.value[i] >= 10 ){
    				num3.value[i+1] += 1;
    				num3.value[i] -= 10;
    			}
    		}
    		
    		//carry
    		if( num3.value[num3.digits] != 0 ) num3.digits+=1;
    	}
    	
    	//different sign
    	//=> sign of adding result depends on the sign of larger number 
    	else{
    		
    		// subtract small number from larger number
    		if( compare ){
    			num3.sign = num1.sign;
	    		for(i=0;i<num3.digits;i++){
	    			 num3.value[i] = num1.value[i] - num2.value[i];
	    		}
    		}
    		else {
    			num3.sign = num2.sign;
    			for(i=0;i<num3.digits;i++){
    				 num3.value[i] = num2.value[i] - num1.value[i];
    			}			
    		}
    		
    		//checking carries
    		for(i=0;i<num3.digits;i++){
    			//if there's carry down
    			while( num3.value[i] < 0){
    				num3.value[i+1] -= 1;
    				num3.value[i] += 10;
    			}
    		}
    		
    		//set digits
    		while( num3.value[num3.digits-1] == 0 ){
    			if( num3.digits==1 ) break;
    			num3.digits-=1;
    		}
    	}
    	
    	return num3;
    }
 
    // num3 = num1 - num2, return num3
    // using a - b = a + (-b)
    public static BigInteger subtract(BigInteger num1, BigInteger num2)
    {
    	BigInteger num3 = new BigInteger();
    	num3.copy(num2);
    	num3.setNegetive();
		return BigInteger.add(num1,num3);
    }
 
    // num3 = num1 * num2, return num3
    public static BigInteger multiply(BigInteger num1, BigInteger num2)
    {
    	int i=0 ,j=0;
    	BigInteger num3 = new BigInteger();
    	//calculate digit by digit like calculating with hands
    	for(i=0;i<num1.digits;i++){
    		for(j=0;j<num2.digits;j++){
    			num3.value[i+j] += num1.value[i] * num2.value[j];
    		}
    	}
    	
    	//checking carries
		for(i=0;i<MAX_DIGITS;i++){
			//if there's carry
			while( num3.value[i] >= 10 ){
				num3.value[i+1] += 1;
				num3.value[i] -= 10;
			}
		}
		
		//set digits
		for(i=MAX_DIGITS;i>1;i--){	
			if( num3.value[i-1] != 0 ){
				break;
			}
		}
		num3.digits = i;
		
		//check sign
		if( num1.sign == num2.sign ) num3.sign = '+';
		else num3.sign = '-';
		
		return num3;
    }
 
    @Override
    public String toString()
    {
    	String result = "";
    	
    	//zero
    	if( this.digits == 1 && this.value[0] == 0 ) return "0";
    	
    	if( this.sign == '-') result += "-";
    	for(int i=0;i<digits;i++){
    		result += Integer.toString( value[digits-i-1] );
    	}
		return result;
    }
 
    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
    	BigInteger num1 = null;
    	BigInteger num2 = null; //initialize at parsing step
    	char operator = '+';
    	
    	//Parsing and initializing step
    	int cnt = 0;
    	char sign1 = '+', sign2 = '+';;
    	boolean sign1_in = false, operator_in = false;
    	Matcher m = EXPRESSION_PATTERN.matcher(input);
    	
    	while(m.find()) {
//    		System.out.println("#"+m.group());
    		
    		//last or whitespace character
    		if( m.group().length() == 0 || m.group().charAt(0) == ' '  || m.group().charAt(0) == '\t' ) continue;
    		
    		// non-digit character
    		if( !Character.isDigit( m.group().charAt(0)) ){
    			if( cnt==0 ){ //sign of num1
    				sign1 = m.group().charAt(0);
    				sign1_in = true;
    			}
    			else if( operator_in == false){ //operator
    				operator = m.group().charAt(0);
    				operator_in = true;
    			}
    			else if( operator_in == true){ //sign of num2
    				sign2 = m.group().charAt(0);
    			}
    		}
    		
    		//digit character
    	    if( Character.isDigit(m.group().charAt(0)) ){
    	    	if( cnt <= 1) num1 = new BigInteger( m.group(), sign1 );
    	    	else num2 = new BigInteger( m.group(), sign2 );
    	    }
    	    
    	    cnt++;
    	}
    	
//    	System.out.println("Integer1: "+num1.toString());
//    	System.out.println("Operator:" + operator);
//    	System.out.println("Integer2: "+num2.toString());
//    	System.out.println(num1.compare(num1,num2));
    	
    	if( operator == '+' ) return BigInteger.add(num1, num2);
    	else if( operator == '*' ) return BigInteger.multiply(num1, num2);
    	else if( operator == '-' ) return BigInteger.subtract(num1, num2);
    	
    	return null;
    }
 
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
 
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
 
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
 
            return false;
        }
    }
 
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}