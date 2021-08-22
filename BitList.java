/*
I, Avishay Mamrud (315746560), assert that the work I submitted is entirely my own.
I have not received any part from any other student in the class,
nor did I give parts of it for use to others.
I realize that if my work is found to contain code that is not originally my own,
 a formal case will be opened against me with the BGU disciplinary committee.
*/

import java.util.LinkedList;

import javax.jws.Oneway;

public class BitList extends LinkedList<Bit> {
    private int numberOfOnes;

    // Do not change the constructor
    public BitList() {
        numberOfOnes = 0;
    }

    // Do not change the method
    public int getNumberOfOnes() {
        return numberOfOnes;
    }


//=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 2.1 ================================================

    public void addLast(Bit element) {
    	if(element == null) {
    		throw new IllegalArgumentException("please serve me a valid bit");
    	}
        numberOfOnes = numberOfOnes + element.toInt();
        super.addLast(element);
    }

    public void addFirst(Bit element) {
    	if(element == null) {
    		throw new IllegalArgumentException("please serve me a valid bit");
    	}
        numberOfOnes = numberOfOnes + element.toInt();
        super.addFirst(element);
    }

    public Bit removeLast() {
        Bit removed = super.removeLast();
        numberOfOnes = numberOfOnes - removed.toInt();
        return removed;
    }

    public Bit removeFirst() {
    	Bit removed = super.removeFirst();
        numberOfOnes = numberOfOnes - removed.toInt();
        return removed;
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 2.2 ================================================
    public String toString() {
        String str = "";
        for(Bit curr:this) {
        	if(curr instanceof Bit){
        		str = curr + str;
        	}
        }
        return "<" + str + ">";
    }
    
    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 2.3 ================================================
    public BitList(BitList other) {
    	if(other==null) {
    		throw new IllegalArgumentException("give me BitList full of Bits!");
    	}
    	for(Bit myBit:other) {
        	if(myBit==null) {
        		throw new IllegalArgumentException("give me BitList full of Bits!");
        	}
        	addLast(new Bit(myBit.toInt()));
        }
    }
    
    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 2.4 ================================================
    public boolean isNumber() {
    	return (size()>0 && (numberOfOnes>1 || get(size()-1).toInt()==0));//dual operators are necessary
    }
    
    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 2.5 ================================================
    public boolean isReduced() {
    	boolean con1 = size()==1 & getFirst().toInt()==0;// this = 0
    	boolean con2 = size()==2 & getFirst().toInt()==1;// this = -1 or 1
    	boolean con3 = size()>2 && (!(get(size()-2).equals(getLast())) || (numberOfOnes==2 && getLast().toInt()==1));//two operators are needed
    	return isNumber() & (con1 | con2 | con3);
    }

    public void reduce() {
    	while(!isReduced()) {
    		removeLast();
    	}
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 2.6 ================================================
    public BitList complement() {
    	BitList ans = new BitList();
    	for(Bit curr:this) {
    		ans.addLast(new Bit((curr.toInt()+1)%2));//1 if 0 and 0 if 1
    	}
    	return ans;
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 2.7 ================================================
    public Bit shiftRight() {
        Bit removed = null;//in case of empty bitList
    	if(size()>0) {
        	removed = remove();
        }
        return removed;
    }

    public void shiftLeft() {
    	addFirst(new Bit(0));
    }

    //=========================== Intro2CS 2020, ASSIGNMENT 4, TASK 2.8 ================================================
    public void padding(int newLength) {
    	if(size()<newLength) {
    		Bit lastBit = getLast();
    		for(int i = (size()+1);i<=newLength;i=i+1) {
    			addLast(lastBit);
    		}
    	}
    }

    
    //----------------------------------------------------------------------------------------------------------
    // The following overriding methods must not be changed.
    //----------------------------------------------------------------------------------------------------------
    public boolean add(Bit e) {
        throw new UnsupportedOperationException("Do not use this method!");
    }

    public void add(int index, Bit element) {
        throw new UnsupportedOperationException("Do not use this method!");
    }

    public Bit remove(int index) {
        throw new UnsupportedOperationException("Do not use this method!");
    }

    public boolean offer(Bit e) {
        throw new UnsupportedOperationException("Do not use this method!");
    }

    public boolean offerFirst(Bit e) {
        throw new UnsupportedOperationException("Do not use this method!");
    }

    public boolean offerLast(Bit e) {
        throw new UnsupportedOperationException("Do not use this method!");
    }

    public Bit set(int index, Bit element) {
        throw new UnsupportedOperationException("Do not use this method!");
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Do not use this method!");
    }
}
