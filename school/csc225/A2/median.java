// Author Corey Koelewyn
// StuID  V00869081

import java.util.Scanner;
public class median{
	static minHeap min;
	static maxHeap max;

	public median(){
		min = new minHeap();
		max = new maxHeap();
	}

	public static int calculateMedian(int x){
		max.insert(x);
		if((max.size() - min.size()) > 1){
			min.insert(max.removeMax());
		}
		try{
			if(max.peek() > min.peek()){
				min.insert(max.removeMax());
				max.insert(min.removeMin());
			}
		}catch (IndexOutOfBoundsException e) {
    System.err.println("IndexOutOfBoundsException: " + e.getMessage());
		}


		// max.printHeap();
		// System.out.print("|");
		// min.printHeap();
		// System.out.println();
		if(max.size() > min.size()){
			return max.peek();
		}else{
			return min.peek();
		}
	}

	public static void main(String[] args){
		median m = new median();

		System.out.println("Enter a list of non negative integers. To end enter a negative integers.");
		Scanner s = new Scanner(System.in);
		int current = s.nextInt();

		while(current >=0){
			System.out.println("current median:" + m.calculateMedian(current));
			current = s.nextInt();
			if(current<0)break;
			m.calculateMedian(current);
			current = s.nextInt();

		}
	}
}


/*	Minimum heap has the smallest value stored at the top of the heap, removing
 *	that value swaps the value in the last cell and then that value trickles down
 *	inserting a value inserts that value at the last spot in the heap,
 * 	and that value will bubble up.
 */

class minHeap{
	private int[] heap;
	private int size;

	public minHeap(){
		heap=new int[10000];
		size=0;
	}

	public boolean isEmpty(){
		return (size==0);
	}

	public int size(){
		return size;
	}


	/**
	 * Prints the heap-array for testing purposes.
	 */
	public void printHeap(){
			System.out.print(" -");
			for(int i = 0; i < size; i++){
				System.out.print(heap[i] + " ");
			}
	}

	/**
	 * inserts a value into the heap at the first available node. After insertion
	 * it bubblesup to its rightful spot in the hierarchy
	 * @param  x  The value to be inserted
	 */
	public void insert(int x){
		heap[size] = x;							// inserting value at first open spot in array
		bubbleup(size);							// bubbling up the value to maintain heap order
		size++;											// increasing size variable
	}

	/**
	 * To maintain heap order after an insert we will compare the value of a cell in
	 * the heap array with its parent (located at (k-1)/2). if it is smaller than its
	 * parent than we will bubble it up recursivly until it is either the root or
	 * it's parent is smaller than it
	 * @param  k  The index of the array we are attemping to bubbleup.
	 */
	public void bubbleup(int k){
		if(k == 0) return; 						// case for root
		int parent = (k-1)/2; 				// Parent node
		if(heap[k] < heap[parent]){ 	// checking if value is smaller than parent's
			exchange(k,parent);					// exchanging with parent
			bubbleup(parent);						// recursivly calling function
		}
		return;


	}

	/**
	 * Swaps values in the heap at locations i and j
	 * @param   i  Index for swapping
	 * @param		j	 Index for swapping
	 */
	public void exchange(int i,int j){
		int tmp;
		tmp = heap[i];
		heap[i] = heap[j];
		heap[j] = tmp;
	}

	/**
	 * Recursivly bubbles value down after a deletion. Swapps with smaller of its
	 * children
	 * @param   k  index that needs to potentially bubble down
	 */
	public void bubbledown(int k){
		int leftChild = (k*2)+1;
		int rightChild = (k*2)+2;
		if(heap[k] <= heap[leftChild] && heap[k] <= heap[rightChild]) return; // base case
		if(leftChild >= size) return; // no children active
		if(rightChild >= size){
			exchange(k,leftChild);
			return;
		}
		if(heap[leftChild] <= heap[rightChild]){
			exchange(k,leftChild);
			bubbledown(leftChild);
			return;
		} else {
			exchange(k,rightChild);
			bubbledown(rightChild);
			return;
		}

	}
	/**
	 * gets the value of the smallest number in the heap
	 * @return returns the value located at the top of the heap
	 */
	public int peek(){
		return heap[0];
	}


	public int removeMin(){
		int tmp = heap[0];
		heap[0] = heap[size-1];
		size--;
		bubbledown(0);
		return tmp;
	}
}

class maxHeap{
	private int[] heap;
	private int size;

	public maxHeap(){
		heap=new int[10000];
		size=0;
	}

	public boolean isEmpty(){
		return (size==0);
	}

	public int size(){
		return size;
	}

	public void insert(int x){
		heap[size] = x;
		bubbleup(size);
		size++;
	}

	public void printHeap(){
			//max head is printed from smallest to biggest
			for(int i = size-1; i >= 0; i--){
				System.out.print(heap[i] + " ");
			}
			System.out.print("+ ");
	}

	public void bubbleup(int k){
		if(k == 0) return;
		int parent = (k-1)/2;
		if(heap[k] > heap[parent]){
			exchange(k,parent);
			bubbleup(parent);
		}else{
			return;
		}


	}
	public void exchange(int i,int j){
		//System.out.println("TEST");
		int tmp;
		tmp = heap[i];
		heap[i] = heap[j];
		heap[j] = tmp;
	}


	public void bubbledown(int k){
		int leftChild = (k*2)+1;
		int rightChild = (k*2)+2;
		if(heap[k] >= heap[leftChild] && heap[k] >= heap[rightChild]) return; // base case
		// if(leftChild >= size) return; // no children active
		// if(rightChild >= size){
		// 	exchange(k,leftChild);
		// 	return;
		// }
		if(heap[leftChild] >= heap[rightChild]){
			exchange(k,leftChild);
			bubbledown(leftChild);
			return;
		} else {
			exchange(k,rightChild);
			bubbledown(rightChild);
			return;
		}

	}
	public int peek(){

		return heap[0];
	}

	public int removeMax(){
		int tmp = heap[0];
		heap[0] = heap[size-1];
		size--;
		bubbledown(0);
		return tmp;
	}
}