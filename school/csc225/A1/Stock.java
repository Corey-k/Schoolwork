/*
*	name 		: Corey Koelewyn
*	Student # 	:	V00869081
* 	Date		:	Jan 21, 2018
*	Description	:	Program to determine the span of stock prices, as outlined in
*					CSC 225's Assignment 1 Description.
*/
import java.util.Scanner;
import java.io.File;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;

public class Stock{

	public static final int MAX_SIZE = 10000; // max array length per outline


	/* 	we can use stack to track index's of biggest numbers. if a number is
			bigger than the one proceeding it, we pop off the old index, and push on
			the new one. if the previous index holds a bigger number, we push on the
			new index, as a new valley is formed.

			worse case running time is O(2n) which is equiv to O(n)
	*/
	public static int[] CalculateSpan(int[] p){
		int Span[] = new int[p.length];
		Stack<Integer> Span_stack = new Stack<>(); // use stack
		Span_stack.push(0);
		Span[0] = 1; //
		for(int i = 1; i < p.length; i++){
			while(p[i] >= p[Span_stack.peek()]){
				Span_stack.pop();
				if(Span_stack.empty()) break; // if the stack is empty then
			}
			if(Span_stack.empty()){
				Span[i] = i+1;
			}else{
				Span[i] = i - Span_stack.peek();
			}
			Span_stack.push(i);
		}
		return Span;
	}


	public static int[] readInput(Scanner s){
		Queue<Integer> q = new LinkedList<Integer>();
		int n=0;
		if(!s.hasNextInt()){
			return null;
		}
		int temp = s.nextInt();
		while(temp>=0){
			q.offer(temp);
			temp = s.nextInt();
			n++;
		}
		int[] inp = new int[q.size()];
		for(int i=0;i<n;i++){
			inp[i]= q.poll();
		}
		return inp;
	}


	public static void main(String[] args){
		Scanner s;
    Stock m = new Stock();
    if (args.length > 0){
    	try{
    		s = new Scanner(new File(args[0]));
    	} catch(java.io.FileNotFoundException e){
    		System.out.printf("Unable to open %s\n",args[0]);
    		return;
    	}
    	System.out.printf("Reading input values from %s.\n", args[0]);
    }else{
    	s = new Scanner(System.in);
      System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
    }

    int[] price = m.readInput(s);
    System.out.println("The stock prices are:");
    for(int i=0;i<price.length;i++){
    	System.out.print(price[i]+ (((i+1)==price.length)? ".": ", "));
    }

    if(price!=null){
    	int[] span = m.CalculateSpan(price);
    	if(span!=null){
    		System.out.println("The spans are:");
    		for(int i=0;i<span.length;i++){
    			System.out.print(span[i]+ (((i+1)==span.length)? ".": ", "));
    		}
    	}
    }
  }
}