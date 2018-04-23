/*
* Name: Corey Koelewyn
* ID: V00869081
* Date: Completed 07/08/2017
* Filename: StackLinked.java
* Details: CSC115 Assignment 3
* Description: Class implements a stack based ADT using a linked list. 
*/

public class StackLinked<T> implements Stack<T> {
	
	int count;		//for tracking number of nodes in stack
	private StackNode<T> head;	
	
	
	// Constructor for StackLinked class
	
    public StackLinked() {
		head = null;
		count = 0;
    }
	
	// Returns integer represnting size of the array
	
    public int size() {
        return count;
    }

	/**
	* method returns information is stack is empty or not
	*Returns true if empty, false if not empty
	*/
	
    public boolean isEmpty() {
        return (count == 0);
    }

	/**
	*method for populating a stack
	*takes a generic data type and prepends it to a linked list
	*/
	
    public void push(T data) {
		StackNode<T> newNode = new StackNode<T>(data);
		newNode.next = head;
		head = newNode;		
		count++;
    }

	/**
	* method for retrieving data from stack 
	* removes and returns the first item from the stack.
	* throws an exception if the stack is empty.
	*
	*/
	
    public T pop() throws StackEmptyException {
		if(isEmpty()){
			throw new StackEmptyException();
		}
		T tmp = head.data;
		head = head.next;
		count--;
        return tmp;
    }

	/**
	* method for examining data from stack 
	* returns the first item from the stack, wihout altering the stack
	* throws an exception if the stack is empty.
	*
	*/	

    public T peek() throws StackEmptyException {
		if(isEmpty()){
			throw new StackEmptyException();
		}
		return head.data;
    }

	/**
	* Method for deleting the entire stack
	* sets head pointer to null, and count to zero
	*
	*/
    public void makeEmpty() {
		count = 0;
		head = null;
    }


    /** 
     * PURPOSE:
     *   
     * Creates and returns a string representation of the contents
     * of the Stack. This is meant to support the work of debugging
     * a solution to A#3.
     */ 
    public String toString() {
        return "";
    }
}
