/*
I, Avishay Mamrud (315746560), assert that the work I submitted is entirely my own.
I have not received any part from any other student in the class,
nor did I give parts of it for use to others.
I realize that if my work is found to contain code that is not originally my own,
 a formal case will be opened against me with the BGU disciplinary committee.
*/

import java.util.Iterator;

public class BinaryNumber implements Comparable<BinaryNumber>{
    private static final BinaryNumber ZERO = new BinaryNumber(0);
    private static final BinaryNumber ONE  = new BinaryNumber(1);
    private BitList bits;

    // Copy constructor
    //Do not chainge this constructor
    public BinaryNumber(BinaryNumber number) {
        bits = new BitList(number.bits);
    }

    //Do not chainge this constructor
    private BinaryNumber(int i) {
        bits = new BitList();
        bits.addFirst(Bit.ZERO);
        if (i == 1)
            bits.addFirst(Bit.ONE);
        else if (i != 0)
            throw new IllegalArgumentException("This Constructor may only get either zero or one.");
    }

    //Do not chainge this method
    public int length() {
        return bits.size();
    }

    //Do not change this method
    public boolean isLegal() {
        return bits.isNumber() & bits.isReduced();
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 3.1 ================================================
    public BinaryNumber(char c) {
    	if(c<='9' & c>='0') {
    		bits = new BitList();
    		int num = c-'0';
    		for(int sqr = 2;num>0;sqr=sqr*2) {
    			bits.addLast(new Bit((num%sqr)>0));
    			num = num-(num%sqr);
    		}
    		bits.addLast(new Bit(0));//chains 0 for legal form
    	}else {
    		throw new IllegalArgumentException("I would like to get char between 0-9");
    	}
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 3.2 ================================================
    public String toString() {
        // Do not remove or change the next two lines
        if (!isLegal()) // Do not change this line
            throw new RuntimeException("I am illegal.");// Do not change this line
        //
        String str = bits.toString();
        return str.substring(1, str.length()-1);//bits.toString without "<>"
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 3.3 ================================================
    public boolean equals(Object other) {
    	boolean ans = false;
    	if(other instanceof BinaryNumber) {
	    	ans = (compareTo((BinaryNumber)other)==0);
    	}
    	return ans;
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 3.4 ================================================
    public BinaryNumber add(BinaryNumber addMe) {
    	if(!addMe.isLegal()) {
    		throw new IllegalArgumentException("illegal BinaryNumber");
    	}
    	BinaryNumber result = new BinaryNumber(0);//this 0 will be removed
    	int len = Math.max(length(), addMe.length())+1;
    	bits.padding(len);
    	addMe.bits.padding(len);
    	Iterator<Bit> mineItr = bits.iterator();
    	Iterator<Bit> foreignItr = addMe.bits.iterator();
    	Bit carry = new Bit(0);
    	while(mineItr.hasNext()) {
    		Bit mineNext = mineItr.next();
    		Bit foreignNext = foreignItr.next();
    		result.bits.addLast(Bit.fullAdderSum(mineNext, foreignNext, carry));
    		carry = Bit.fullAdderCarry(mineNext, foreignNext, carry);
    	}
    	result.bits.shiftRight();//removes initial 0 as promised
    	bits.reduce();
    	addMe.bits.reduce();
    	result.bits.reduce();
    return result;
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 3.5 ================================================
    public BinaryNumber negate() {
    	BinaryNumber temp = new BinaryNumber(0);
    	temp.bits = bits.complement();
    	return temp.add(ONE);//2complement + 1 by definition
    }
    
    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 3.6 ================================================
    public BinaryNumber subtract(BinaryNumber subtractMe) {
    	if(!isLegal()) {
    		throw new IllegalArgumentException("I am illegal!");
    	}
    	BinaryNumber temp = subtractMe.negate();
    	BinaryNumber result = add(temp);
    	return result;
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 3.7 ================================================
    public int signum() {
    	int signNum = 0;
    	int lastBit = bits.getLast().toInt();
    	if(lastBit==1) {//means it's negative
    		signNum = -1;
    	}else if(bits.getNumberOfOnes()>0) {//means it's positive
    		signNum = 1;
    	}
    	return signNum;
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 3.8 ================================================
    public int compareTo(BinaryNumber other) {
    	if(!isLegal()) {
    		throw new IllegalArgumentException("I am illegal!");
    	}
    	return subtract(other).signum();//1 -> bigger , 0 -> equal , -1 -> smaller
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 3.9 ================================================
    public int toInt() {
        // Do not remove or change the next two lines
        if (!isLegal()) // Do not change this line
            throw new RuntimeException("I am illegal.");// Do not change this line
        //
        boolean negative = false;
        BinaryNumber temp;
        if(signum()==-1) {
        	temp = negate();
        	negative = true;
        }else {
        	temp = this;//doesn't require copy
        }
        int sqr = 1;//initial value 2^0
        int ans = 0;
        for (Bit bit : temp.bits) {
			ans = ans + (bit.toInt()*sqr);//add 0 when bit = 0
			sqr = sqr*2;//now sqr = 2^n
		}
        if(negative) {
        	ans = -ans;
        }
        return ans;
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 3.10 ================================================
    // Do not change this method
    public BinaryNumber multiply(BinaryNumber multiplyMe) {
    	BinaryNumber him = multiplyMe;
    	BinaryNumber me = this;//make no changes on "me", so doesn't require copy
    	boolean isPositive = (signum()==multiplyMe.signum());
    	if(multiplyMe.signum()==-1) {
    		him = multiplyMe.negate();
    	}
    	if(signum()==-1) {
    		me = negate();
    	}
    	BinaryNumber ans = me.multiplyPositive(him);
    	if(!isPositive) {
    		ans = ans.negate();
    	}
    	return ans;
    }

    private BinaryNumber multiplyPositive(BinaryNumber multiplyMe) {
    	BinaryNumber ans = ZERO;
    	BinaryNumber temp = new BinaryNumber(multiplyMe);//will be changed, so must be copied
    	for (Bit bit : bits) {
			if(bit.toInt()==1) {
				ans = ans.add(temp);
			}
			temp.bits.shiftLeft();//just like manual multiplication
		}
    	return ans;
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 3.11 ================================================
    // Do not change this method
    public BinaryNumber divide(BinaryNumber divisor) {
    	// Do not remove or change the next two lines
    	if (divisor.equals(ZERO)) // Do not change this line
            throw new RuntimeException("Cannot divide by zero."); // Do not change this line
    	//
    	BinaryNumber him = new BinaryNumber(divisor);
    	BinaryNumber me = new BinaryNumber(this);//makes changes on "me" and "him", so require copy
    	boolean isPositive = signum()==divisor.signum();
    	if(divisor.signum()==-1) {
    		him = divisor.negate();
    	}
    	if(signum()==-1) {
    		me = negate();
    	}
    	BinaryNumber ans = me.dividePositive(him);
    	if(!isPositive) {
    		ans = ans.negate();
    	}
    	return ans;
    }

    private BinaryNumber dividePositive(BinaryNumber divisor) {//effective though not recursive
    	int gap = length()-divisor.length();
    	BinaryNumber temp = new BinaryNumber(this);
    	BinaryNumber result = new BinaryNumber(0);
    	if(gap>0) {
    		for(int i = 0;i<(gap);i=i+1) {
    			divisor.bits.shiftLeft();
    		}
    	}
    	for(int j = 0;j<(gap+1);j=j+1) {
    		BinaryNumber subtracted = temp.subtract(divisor);
    		if(subtracted.compareTo(ZERO)>=0) {//if subtraction>=0 -> temp = remainder*2
    			result.bits.addFirst(new Bit(true));
    			temp = subtracted;
    			if(temp.length()!=1) {
    				temp.bits.shiftLeft();
    			}
    		}else {//else chains 0 and multiply by 2
    			result.bits.addFirst(new Bit(false));
    			if(temp.length()!=1) {
    				temp.bits.shiftLeft();
    			}
    		}
    	}
    	result.bits.reduce();
    	return result;
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 3.12 ================================================
    public BinaryNumber(String s) {
    	this(0);
    	if(s == "") {
    		throw new IllegalArgumentException("the constructor need a non-empty string");
    	}
    	boolean negative = false;
    	if(s.charAt(0)=='-') {
    		s=s.substring(1);
    		negative = true;
    	}
    	BinaryNumber temp = new BinaryNumber(0);//at the end this 0 will be removed
    	BinaryNumber ten = new BinaryNumber(1);
    	ten.bits.shiftLeft();
    	ten.bits.addFirst(new Bit(true));
    	ten.bits.shiftLeft();//now ten = 01010
    	BinaryNumber sqr = new BinaryNumber(1);//initial value 2^0
    	for(int i = (s.length()-1);i>=0;i=i-1){
    		if(charVal(s.charAt(i))==-1) {//in case of char not in [0-9]
        		throw new IllegalArgumentException("the constructor need a numbers-string");
    		}
    		BinaryNumber byChar = new BinaryNumber(s.charAt(i));
    		temp = temp.add(byChar.multiply(sqr));
    		sqr = sqr.multiply(ten);//sqr = 2^i
    	}
    	if(negative) {
    		temp = temp.negate();
    	}
    	for (Bit bit : temp.bits) {
			bits.addLast(new Bit(bit.toInt()==1));
		}
    	bits.shiftRight();//removes first 0
    }
    	
    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 3.13 ================================================
    public String toIntString() {
        // Do not remove or change the next two lines
        if (!isLegal()) // Do not change this line
            throw new RuntimeException("I am illegal.");// Do not change this line
        //
        String result = "0";
        String power = "1";
        BinaryNumber me;//a copy of this BN
        boolean negative = false;
        if(bits.getLast().toInt()==1) {
        	negative = true;
        	me = negate();
        }else {
        	me = new BinaryNumber(this);
        }
        for (Bit bit : me.bits) {
			if(bit.toInt()==1) {
				result = adder(result, power);//sum current result and 1*2^(n)
			}
			power = multyByTwo(power);//now power = 2^(n+1)
		}
        if(negative) {
        	result = "-" + result;
        }
        return result;
    }
    
    //assistance function multiply string number by 2 (returns 2^(n+1))
    private static String multyByTwo(String power) {
    	String result="";
    	int carry = 0;
    	int curr;
    	for(int i = power.length();i>0;i=i-1) {
    		curr = (charVal(power.charAt(i-1)) * 2) + carry;
    		result = curr%10 + result;
    		carry = curr/10;
    	}
    	if(carry!=0)
    		result = carry + result;
    	return result;
    }
    
    //assistance function sum 2 strings as numbers
    private static String adder(String n1 , String n2) {
    	int carry = 0;
    	String result="";
    	String longy = n2;
    	String shorty = n1;
    	int curr;
    	boolean sameLen = (n1.length()==n2.length());
    	if(n1.length()>n2.length()) {//ensures longy is the longer one
    		longy = n1;
    		shorty = n2;
    	}
    	if(shorty != "") {
	    	for(int i = shorty.length()-1;i>=0;i=i-1) {
	    		curr = (charVal(longy.substring(longy.length()-shorty.length()).charAt(i)) + carry + charVal(shorty.charAt(i)));
	    		result = (curr%10) + result;
	    		carry = curr/10;
	    	}
    	}
    	if(!sameLen) {
    		for(int i = (longy.length()-shorty.length())-1;i>=0;i=i-1) {
    			result =  (carry + charVal(longy.charAt(i)))%10 + result;
    			carry = (carry + charVal(longy.charAt(i)))/10;
    		}
    	}//chains the rest to the result. 
    	if(carry!=0)
    		result = carry + result;
    	return result;
	}
    
    //assistance function returns intValue of char
    private static int charVal(char chr) {
    	return "0123456789".indexOf(chr);
    }
    public static void main(String[]args) {
    	String str = "-123a3";
    	BinaryNumber h = new BinaryNumber(str);
    	System.out.println(h);
    }
    // Returns this * 2
    public BinaryNumber multBy2() {
        BinaryNumber output = new BinaryNumber(this);
        output.bits.shiftLeft();
        output.bits.reduce();
        return output;
    }

    // Returens this / 2;
    public BinaryNumber divBy2() {
        BinaryNumber output = new BinaryNumber(this);
        if (!equals(ZERO)) {
            if (signum() == -1) {
                output.negate();
                output.bits.shiftRight();
                output.negate();
            } else output.bits.shiftRight();
        }
        return output;
    }
}
